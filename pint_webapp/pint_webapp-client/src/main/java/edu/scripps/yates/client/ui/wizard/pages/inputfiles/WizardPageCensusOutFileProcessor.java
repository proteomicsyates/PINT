package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import com.google.gwt.dom.client.Style.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
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
import edu.scripps.yates.shared.util.Pair;

public class WizardPageCensusOutFileProcessor extends AbstractWizardPageFileProcessor {

	private WizardQuestionPanel questionPanel;
	private boolean showConditionSelector = false;

	public WizardPageCensusOutFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);
	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		inputFileSummaryPanel.setOnFileSummaryReceivedTask(new DoSomethingTask<Void>() {

			@Override
			public Void doSomething() {
				final Pair<ExperimentalConditionTypeBean, ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
						.getConditionsWithRatiosByFileID(getPintImportConfg(), getFile().getId(), null);
				if (conditionsAssociatedWithFile != null) {
					inputFileSummaryPanel.addAssociatedCondition(conditionsAssociatedWithFile.getFirstElement());
					inputFileSummaryPanel.addAssociatedCondition(conditionsAssociatedWithFile.getSecondElement());
				}
				return null;
			}
		});

	}

	@Override
	public boolean isReadyForNextStep() {

		final Pair<ExperimentalConditionTypeBean, ExperimentalConditionTypeBean> conditionsWithRatiosByFileID = PintImportCfgUtil
				.getConditionsWithRatiosByFileID(getPintImportConfg(), getFile().getId(), null);
		if (conditionsWithRatiosByFileID != null) {
			return true;
		}
		return false;
	}

	@Override
	protected AbstractConditionSelectorForFileWidget createConditionSelectorPanel(FileTypeBean file2) {

		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAdded = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				updateNextButtonState();
				getInputFileSummaryPanel().addAssociatedCondition(condition);
				return null;
			}
		};
		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemoved = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				updateNextButtonState();
				getInputFileSummaryPanel().removeAssociatedCondition(condition);
				return null;
			}
		};
		final ConditionSelectorForFileWithRatiosWidget ratioSelectorPanel = new ConditionSelectorForFileWithRatiosWidget(
				getContext(), getFile(), onConditionAdded, onConditionRemoved);
		return ratioSelectorPanel;
	}

	@Override
	public void beforeShow() {
		checkConditions(); // this will create a questionPanel depending on what is in context

		super.beforeShow();// this sets the widget index to add nextWidgets
		// show questionPanel
		addNextWidget(questionPanel);
		if (showConditionSelector) {
			addNextWidget(createConditionSelectorPanel(getFile()));
		}

	}

	private void checkConditions() {
		// show the associated conditions line
		final InputFileSummaryPanel inputFileSummaryPanel = getInputFileSummaryPanel();
		if (inputFileSummaryPanel != null) {
			inputFileSummaryPanel.showAssociatedConditionsRow(true);
		}
		final int numConditions = PintImportCfgUtil.getConditions(getPintImportConfg()).size();
		if (numConditions < 2) {
			String explanation = "Census out files contain at least quantitative information from 2 experimental conditions, "
					+ "having relative abundance ratios of peptides and proteins between 2 Experimental Conditions.";
			if (numConditions == 1) {
				explanation += "\nHowever, we detected only ONE Experimental Condition: '"
						+ PintImportCfgUtil.getConditions(getPintImportConfg()).get(0).getId() + "'.";
			} else if (numConditions == 0) {
				explanation += "\nHowever, we didn't detected any Experimental Condition defined yet.";
			}
			final String question = "Click here to go to the "
					+ PageTitleController
							.getPageTitleByPageID(PageIDController.getPageIDByPageClass(WizardPageConditions.class))
					+ " wizard page and create a new Experimental Condition.";
			questionPanel = new WizardQuestionPanel(explanation, WizardStyles.WizardExplanationLabel, question,
					WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.OK);
			questionPanel.setOKButtonStyleName(WizardStyles.WizardJumpToPage, "", "");
			questionPanel.getOKButton().getElement().getStyle().setTextAlign(TextAlign.CENTER);
			questionPanel.getOKButton().getElement().getStyle().setPaddingBottom(10, Unit.PX);
			questionPanel.getOKButton().setText("Go to " + PageTitleController
					.getPageTitleByPageID(PageIDController.getPageIDByPageClass(WizardPageConditions.class)));
			questionPanel.getOKButton().setWidth("auto");
			questionPanel.addOKClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// go to conditions
					getWizard().showPage(PageIDController.getPageIDByPageClass(WizardPageConditions.class), false, true,
							true);
				}
			});
			// hide the associated conditions line
			if (inputFileSummaryPanel != null) {
				inputFileSummaryPanel.showAssociatedConditionsRow(false);
			}
		} else {
			String explanation = null;
			String question = null;
			if (numConditions == 2) {
				explanation = "Census out files contain relative abundance ratios of peptides and proteins between 2 Experimental Conditions.";
				question = "Which condition is the one in the numerator and which one is in the denominator of the relative abundance ratios contained in this input file?";

			} else if (numConditions > 2) {
				explanation = "Census out files contain relative abundance ratios of peptides and proteins between 2 conditions. However, we detected "
						+ numConditions + " defined in this wizard.";
				question = "Which condition is the one in the numerator and which one is in the denominator of the relative abundance ratios contained in this input file?";
			}

			questionPanel = new WizardQuestionPanel(explanation, WizardStyles.WizardExplanationLabel, question,
					WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.NONE);

			showConditionSelector = true;

		}
		questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
		questionPanel.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
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
