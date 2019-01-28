package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.pages.AbstractWizardPage;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardExtractIdentificationDataFromDTASelectPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageCensusOutFileProcessor extends AbstractWizardPage {

	private FlexTable panel;
	private final FileTypeBean file;
	private final int fileNumber;
	private final String text1;
	private String text2;
	private String text3;
	private String question;
	private String explanation;
	private int row;
	private ClickHandler noClickHandler;
	private ClickHandler yesClickHandler;
	protected boolean extractQuantData;
	private WizardQuestionPanel questionPanel;
	private boolean isReadyForNextStep = false;

	public WizardPageCensusOutFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(fileNumber + " " + file.getName());
		this.file = file;
		this.fileNumber = fileNumber;
		text1 = "Processing input file " + fileNumber + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + file.getName() + "'";
		if (file.getFormat().isQuantitativeData() || file.getFormat() == FileFormat.EXCEL) {
			explanation = "With quantitative information we are referring to either abundances (intensities, spectral counts, etc...) or relative abundance ratios";
			question = "Do you want to extract quantitative information from this input file?";
			yesClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					extractQuantData = true;
					showExtractQuantificationData();
					isReadyForNextStep = true;
					updateNextButtonState();
				}
			};
			noClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					extractQuantData = false;
					showExtractIdentificationData(false);
					isReadyForNextStep = true;
					updateNextButtonState();
				}
			};
		} else if (file.getFormat() == FileFormat.FASTA) {
			text2 = "Fasta files are used to map peptides to proteins because sometimes the input files don't contain all the proteins mapping to the identified peptides (depending on how the software generating that file works).";
			question = "Do you want to use this file?";
			explanation = "If so, we will need to ask you about how to in-silico cleave the proteins to get the peptides";
			yesClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					context.setUseFasta(true);
					showFastaDefinition();
					isReadyForNextStep = true;
					updateNextButtonState();
				}
			};
			noClickHandler = new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					context.setUseFasta(false);
					wizard.showNextPage();
				}
			};
		} else {
			// no quant file
			if (file.getFormat() == FileFormat.DTA_SELECT_FILTER_TXT) {
				question = "Do you want to extract the NSAF (Normalized Spectral Abundance Factor) from this DTASelect output file?";
				explanation = "This type of file has not quantitative data, but still we can extract the NSAF (Normalized Spectral Abundance Factor).";
				yesClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						showExtractIdentificationData(true);
						isReadyForNextStep = true;
						updateNextButtonState();
					}
				};
				noClickHandler = new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						showExtractIdentificationData(false);
						isReadyForNextStep = true;
						updateNextButtonState();
					}
				};
			} else {
				// this will be zIdentML
				text2 = "We don't need more information about which type of information we can extract from this file.";
				text3 = "This is an standard format file containing PSMs, peptides and proteins.";
				isReadyForNextStep = true;
				updateNextButtonState();
			}
		}
	}

	@Override
	protected Widget createPage() {
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.add(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);
		//
		row = 0;
		final Label welcomeLabel1 = new Label(text1);
		welcomeLabel1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(row, 0, welcomeLabel1);
		//
		if (text2 != null) {
			row++;
			final Label welcomeLabel2 = new Label(text2);
			welcomeLabel2.setStyleName(WizardStyles.WizardExplanationLabel);
			panel.setWidget(row, 0, welcomeLabel2);
		}
		//
		if (text3 != null) {
			row++;
			final Label welcomeLabel3 = new Label(text3);
			welcomeLabel3.setStyleName(WizardStyles.WizardExplanationLabel);
			panel.setWidget(row, 0, welcomeLabel3);
		}
		return ret;
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDForInputFileProcessor(getTitle());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}

	@Override
	public void beforeShow() {
		// TODO add other thing if questionPanel is null that happens when thereis no
		// question
		panel.setWidget(row, 0, questionPanel);
		// disable next button
		wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		wizard.setButtonOverride(true);
		//
		super.beforeShow();
	}

	@Override
	public void beforeFirstShow() {
		if (noClickHandler != null && yesClickHandler != null) {
			row++;
			questionPanel = new WizardQuestionPanel(question, explanation);
			questionPanel.addNoClickHandler(noClickHandler);
			questionPanel.addYesClickHandler(yesClickHandler);

		}
		super.beforeFirstShow();
	}

	protected void showExtractIdentificationData(boolean createNSAFQuantValues) {
		if (file.getFormat() == FileFormat.DTA_SELECT_FILTER_TXT) {
			final WizardExtractIdentificationDataFromDTASelectPanel extractIDPanel = new WizardExtractIdentificationDataFromDTASelectPanel(
					wizard.getContext(), file, createNSAFQuantValues);
			panel.setWidget(row, 0, extractIDPanel);
		} else {
			// TODO
		}

	}

	protected void showExtractQuantificationData() {
		// TODO Auto-generated method stub

	}

	protected void showFastaDefinition() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageCensusOutFileProcessor) {
			final WizardPageCensusOutFileProcessor page2 = (WizardPageCensusOutFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	protected void updateNextButtonState() {
		final boolean readyForNextStep = isReadyForNextStep();
		this.wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, readyForNextStep);
	}

	private boolean isReadyForNextStep() {
		return isReadyForNextStep;
	}
}
