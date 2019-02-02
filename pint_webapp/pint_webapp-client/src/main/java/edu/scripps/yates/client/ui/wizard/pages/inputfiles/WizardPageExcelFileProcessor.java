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
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageExcelFileProcessor extends AbstractWizardPage {

	private FlexTable panel;
	private final FileTypeBean file;
	private final String text1;
	private String text2;
	private String text3;
	private int row;
	protected boolean extractQuantData;
	private boolean isReadyForNextStep = false;
	private int rowForNextWidget;

	public WizardPageExcelFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(fileNumber + "-" + file.getName());
		this.file = file;
		text1 = "Processing input file " + fileNumber + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + file.getName() + "'";

	}

	protected void showExtractQuantificationData() {
		// TODO Auto-generated method stub

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
		rowForNextWidget = row + 1;
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
		// sumary of excel file
		final InputFileSummaryPanel extractIDPanel = new InputFileSummaryPanel(wizard.getContext(), file);
		panel.setWidget(rowForNextWidget, 0, extractIDPanel);

		// question about whether to extract quant
		final String explanation = "With quantitative information we are referring to either abundances (intensities, spectral counts, etc...) or relative abundance ratios";
		final String question = "Do you want to extract quantitative information from this input file?";
		final ClickHandler yesClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				extractQuantData = true;
				showExtractQuantificationData();
				isReadyForNextStep = true;
				updateNextButtonState();
			}
		};
		final ClickHandler noClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				extractQuantData = false;
				showExtractIdentificationData(false);
				isReadyForNextStep = true;
				updateNextButtonState();
			}
		};
		final WizardQuestionPanel questionPanel = new WizardQuestionPanel(question, explanation);
		questionPanel.addNoClickHandler(noClickHandler);
		questionPanel.addYesClickHandler(yesClickHandler);
		panel.setWidget(rowForNextWidget + 1, 0, questionPanel);
		// disable next button
		wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		wizard.setButtonOverride(true);
		//
		updateNextButtonState();
		super.beforeShow();
	}

	private void showExtractIdentificationData(boolean createNSAFQuantValues) {

		// TODO

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageExcelFileProcessor) {
			final WizardPageExcelFileProcessor page2 = (WizardPageExcelFileProcessor) obj;
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
