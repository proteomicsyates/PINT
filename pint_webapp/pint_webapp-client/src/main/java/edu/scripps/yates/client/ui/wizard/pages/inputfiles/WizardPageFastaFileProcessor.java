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
import edu.scripps.yates.client.ui.wizard.pages.panels.FastaDigestionPanelNew;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardExtractIdentificationDataFromDTASelectPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageFastaFileProcessor extends AbstractWizardPage {

	private FlexTable panel;
	private final FileTypeBean file;
	private final int fileNumber;
	private final String text1;
	private final String text2;
	private String text3;
	private final String question;
	private final String explanation;
	private int row;
	private final ClickHandler noClickHandler;
	private final ClickHandler yesClickHandler;
	protected boolean extractQuantData;
	private WizardQuestionPanel questionPanel;

	public WizardPageFastaFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(fileNumber + " " + file.getName());
		this.file = file;
		this.fileNumber = fileNumber;
		text1 = "Processing input file " + fileNumber + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + file.getName() + "'";

		text2 = "Fasta files are used to map peptides to proteins because sometimes the input files don't contain all the proteins mapping to the identified peptides (depending on how the software generating that file works).";
		question = "Do you want to use this file?";
		explanation = "If so, we will need to ask you about how to in-silico cleave the proteins to get the peptides. Otherwise, it will be ignored.";
		yesClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				context.setUseFasta(true);
				showFastaDefinition();
				updateNextButtonState();
			}
		};
		noClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				context.setUseFasta(false);
				updateNextButtonState();
				wizard.showNextPage();
			}
		};

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

		if (wizard.getContext().isUseFasta() == null) {
			panel.setWidget(row, 0, questionPanel);
			// disable next button
			wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
			wizard.setButtonOverride(true);
		} else {
			if (wizard.getContext().isUseFasta()) {
				// show what we have
				panel.setWidget(row, 0, new FastaDigestionPanelNew(file));
			} else {
				// not using the fasta file

			}
		}
		//
		super.beforeShow();
	}

	@Override
	public void beforeFirstShow() {
		if (noClickHandler != null && yesClickHandler != null) {
			row++;
			questionPanel = new WizardQuestionPanel(question, explanation, false);
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
		if (obj instanceof WizardPageFastaFileProcessor) {
			final WizardPageFastaFileProcessor page2 = (WizardPageFastaFileProcessor) obj;
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
		if (context.isUseFasta() == null) {
			return false;
		}
		if (!context.isUseFasta()) {
			return true;
		} else {
			if (file.getFastaDigestion().getCleavageAAs() == null
					&& "".equals(file.getFastaDigestion().getCleavageAAs())) {
				return false;
			}
			if (file.getFastaDigestion().getMisscleavages() < 0) {
				return false;
			}
			return true;
		}
	}
}
