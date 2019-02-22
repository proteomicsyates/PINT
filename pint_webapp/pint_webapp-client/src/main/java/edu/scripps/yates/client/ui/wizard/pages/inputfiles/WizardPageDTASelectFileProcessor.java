package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.panels.InputFileSummaryPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel;
import edu.scripps.yates.client.ui.wizard.pages.panels.WizardQuestionPanel.WizardQuestionPanelButtons;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionSelectorForFileWithNORatiosWidget;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class WizardPageDTASelectFileProcessor extends AbstractWizardPageFileProcessor {

	private WizardQuestionPanel questionPanel;

	public WizardPageDTASelectFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageDTASelectFileProcessor) {
			final WizardPageDTASelectFileProcessor page2 = (WizardPageDTASelectFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

	@Override
	protected void setOnFileSummaryReceivedTask(InputFileSummaryPanel inputFileSummaryPanel) {
		inputFileSummaryPanel.setOnFileSummaryReceivedTask(new DoSomethingTask<Void>() {

			@Override
			public Void doSomething() {
				final List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
						.getConditionsAssociatedWithFile(context.getPintImportConfiguration(), getFile().getId(), null);
				if (conditionsAssociatedWithFile != null) {
					for (final ExperimentalConditionTypeBean condition : conditionsAssociatedWithFile) {
						inputFileSummaryPanel.addAssociatedCondition(condition);
					}
				}
				return null;
			}
		});

	}

	@Override
	public boolean isReadyForNextStep() {
		final List<ExperimentalConditionTypeBean> conditionsAssociatedWithFile = PintImportCfgUtil
				.getConditionsAssociatedWithFile(getPintImportConfg(), getFile().getId(), null);
		final boolean empty = conditionsAssociatedWithFile.isEmpty();
		return !empty;
	}

	@Override
	protected ConditionSelectorForFileWithNORatiosWidget createConditionSelectorPanel(FileTypeBean file) {
		// show the panel to select conditions

		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {

				getInputFileSummaryPanel().addAssociatedCondition(condition);
				final RemoteInfoTypeBean remoteInfo = new RemoteInfoTypeBean();
				remoteInfo.getFileRefs().add(file.getId());
				PintImportCfgUtil.addIdentificationToCondition(getPintImportConfg(), condition.getId(), remoteInfo);
				updateNextButtonState();
				return null;
			}
		};
		final DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask = new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {

				getInputFileSummaryPanel().removeAssociatedCondition(condition);
				PintImportCfgUtil.removeFileFromCondition(file.getId(), null, condition);
				updateNextButtonState();
				return null;
			}
		};
		final ConditionSelectorForFileWithNORatiosWidget conditionSelector = new ConditionSelectorForFileWithNORatiosWidget(
				getContext(), file, onConditionAddedTask, onConditionRemovedTask, null);
		return conditionSelector;
	}

	@Override
	public void beforeShow() {
		super.beforeShow();// this sets the widget index to add nextWidgets

		// create the question
		questionPanel = new WizardQuestionPanel(getExplanation(), WizardStyles.WizardExplanationLabel, getQuestion(),
				WizardStyles.WizardQuestionLabel, WizardQuestionPanelButtons.NONE);

		questionPanel.getElement().getStyle().setPadding(20, Unit.PX);
		questionPanel.getElement().getStyle().setPaddingBottom(0, Unit.PX);
		questionPanel.getElement().getStyle().setWidth(690, Unit.PX);
		// show questionPanel
		addNextWidget(questionPanel);
		addNextWidget(createConditionSelectorPanel(getFile()));

		super.updateNextButtonState();
	}

	public String getQuestion() {
		return "Drag and drop the Experimental Conditions of the sample of the proteins and peptides detected in this input file.";

	}

	public String getExplanation() {
		return "DTASelect output files contain proteins and peptides that can belong to multiple Experimental Conditions.";
	}
}
