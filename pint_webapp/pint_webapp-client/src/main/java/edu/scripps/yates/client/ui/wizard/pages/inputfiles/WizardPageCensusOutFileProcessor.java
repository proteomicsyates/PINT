package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.PageIDController;
import edu.scripps.yates.client.ui.wizard.pages.PageTitleController;
import edu.scripps.yates.client.ui.wizard.pages.WizardPageConditions;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel.WizardQuestionPanelButtons;
import edu.scripps.yates.client.ui.wizard.pages.widgets.AbstractConditionSelectorForFileWidget;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionSelectorForFileWithRatiosWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageCensusOutFileProcessor extends AbstractWizardPageFileProcessor {

	protected boolean extractQuantData;
	private WizardQuestionPanel questionPanel;

	public WizardPageCensusOutFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);
		checkConditions(); // this will create a questionPanel depending on what is in context
	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		// show questionPanel
		addNextWidget(questionPanel);
	}

	@Override
	protected AbstractConditionSelectorForFileWidget createConditionSelectorPanel(FileTypeBean file2) {
		final ConditionSelectorForFileWithRatiosWidget ratioSelectorPanel = new ConditionSelectorForFileWithRatiosWidget(
				getContext(), getFile());
		ratioSelectorPanel.setOnConditionAddedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				setReadyForNextStep(true);
				updateNextButtonState();
				return null;
			}
		});
		ratioSelectorPanel.setOnConditionRemovedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				setReadyForNextStep(false);
				updateNextButtonState();
				return null;
			}
		});
		return ratioSelectorPanel;
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
			questionPanel = new WizardQuestionPanel(explanation, WizardStyles.WizardExplanationLabel, question,
					WizardStyles.WizardExplanationLabel, WizardQuestionPanelButtons.OK);
			questionPanel.setOKButtonStyleName(WizardStyles.WizardJumpToPage, "", "");
			questionPanel.getOKButton().getElement().getStyle().setTextAlign(TextAlign.CENTER);
			questionPanel.getOKButton().setText("Go to " + PageTitleController
					.getPageTitleByPageID(PageIDController.getPageIDByPageClass(WizardPageConditions.class)));
			questionPanel.addOKClickHandler(new ClickHandler() {

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

			final AbstractConditionSelectorForFileWidget ratioSelectorPanel = createConditionSelectorPanel(getFile());
			questionPanel = new WizardQuestionPanel(explanation, WizardStyles.WizardExplanationLabel, question,
					WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.NONE);
			questionPanel.getElement().getStyle().setPadding(10, Unit.PX);
			questionPanel.addBottomWidget(ratioSelectorPanel, HasHorizontalAlignment.ALIGN_CENTER);
			ratioSelectorPanel.getElement().getStyle().setMarginTop(10, Unit.PX);
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

}
