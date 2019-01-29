package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.form.RatioSelectorForFileForm;
import edu.scripps.yates.client.ui.wizard.pages.AbstractWizardPage;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageConditions;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageCensusOutFileProcessor extends AbstractWizardPage {

	private FlexTable panel;
	private final FileTypeBean file;
	private final String text1;
	private String text2;
	private String text3;

	private int row;
	private ClickHandler noClickHandler;
	private ClickHandler yesClickHandler;
	protected boolean extractQuantData;
	private FlexTable questionPanel;
	private boolean isReadyForNextStep = false;

	public WizardPageCensusOutFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(fileNumber + " " + file.getName());
		this.file = file;

		text1 = "Processing input file " + fileNumber + "/"
				+ PintImportCfgUtil.getFiles(context.getPintImportConfiguration()).size() + " '" + file.getName() + "'";

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
		row++;
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
		checkConditions();

		panel.setWidget(row, 0, questionPanel);
		// disable next button
		wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, false);
		wizard.setButtonOverride(true);
		//

		super.beforeShow();
	}

	private void checkConditions() {
		final int numConditions = PintImportCfgUtil.getConditions(getPintImportConfg()).size();
		if (numConditions < 2) {
			String explanation = "Census out files contain at least quantitative information from 2 experimental conditions, "
					+ "having relative abundance ratios of peptides and proteins between 2 conditions.";
			if (numConditions == 1) {
				explanation += "\nHowever, we detected only ONE experimental condition: '"
						+ PintImportCfgUtil.getConditions(getPintImportConfg()).get(0).getId() + "'.";
			} else if (numConditions == 0) {
				explanation += "\nHowever, we didn't detected any experimental condition defined yet.";
			}
			final String question = "Click here to go to the " + PageTitleController.getPageTitleByPageID(
					PageIDController.getPageIDByPageClass(WizardPageConditions.class)) + " wizard page.";
			questionPanel = new WizardQuestionPanel(question, explanation, true);
			((WizardQuestionPanel) questionPanel).addOKClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// go to conditions
					getWizard().showPage(PageIDController.getPageIDByPageClass(WizardPageConditions.class), false, true,
							true);
				}
			});

		} else {
			String explanation = null;
			String question = null;
			if (numConditions == 2) {
				explanation = "Census out files contain relative abundance ratios of peptides and proteins between 2 conditions.";
				question = "Which condition is the one in the numerator and which one is in the denominator of the relative abundance ratios contained in this input file?";

			} else if (numConditions > 2) {
				explanation = "Census out files contain relative abundance ratios of peptides and proteins between 2 conditions. However, we detected "
						+ numConditions + " defined in this wizard.";
				question = "Which condition is the one in the numerator and which one is in the denominator of the relative abundance ratios contained in this input file?";
			}
			questionPanel = new WizardQuestionPanel(question, explanation, true);
			((WizardQuestionPanel) questionPanel).addOKClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {

					RatioSelectorForFileForm ratioSelectorPanel = new RatioSelectorForFileForm(getContext(), file);
					ratioSelectorPanel.addOnTwoConditionsSelectedTask(new DoSomethingTask<Void>() {

						@Override
						public Void doSomething() {
							isReadyForNextStep = true;
							updateNextButtonState();
							return null;
						}
					});
					ratioSelectorPanel.addOnNotTwoConditionsSelectedTask(new DoSomethingTask<Void>() {

						@Override
						public Void doSomething() {
							isReadyForNextStep = false;
							updateNextButtonState();
							return null;
						}
					});
					panel.setWidget(row, 0, ratioSelectorPanel);

				}
			});
		}

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