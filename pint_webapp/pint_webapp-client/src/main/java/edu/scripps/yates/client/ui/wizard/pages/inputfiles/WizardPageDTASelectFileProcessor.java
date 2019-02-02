package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.ConditionSelectorForFileWithNORatiosWidget;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class WizardPageDTASelectFileProcessor extends AbstractWizardPageFileProcessor {

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
	protected ConditionSelectorForFileWithNORatiosWidget createConditionSelectorPanel(FileTypeBean file) {
		// show the panel to select conditions
		final ConditionSelectorForFileWithNORatiosWidget conditionSelector = new ConditionSelectorForFileWithNORatiosWidget(
				getContext(), file);

		conditionSelector.setOnConditionAddedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				getInputFileSummaryPanel().addAssociatedCondition(condition);
				final RemoteInfoTypeBean remoteInfo = new RemoteInfoTypeBean();
				remoteInfo.getFileRefs().add(file.getId());
				PintImportCfgUtil.addIdentificationToCondition(getPintImportConfg(), condition.getId(), remoteInfo);
				return null;
			}
		});
		conditionSelector.setOnConditionRemovedTask(new DoSomethingTask2<ExperimentalConditionTypeBean>() {

			@Override
			public Void doSomething(ExperimentalConditionTypeBean condition) {
				getInputFileSummaryPanel().removeAssociatedCondition(condition);
				PintImportCfgUtil.removeFileFromCondition(file.getId(), condition);
				return null;
			}
		});
		return conditionSelector;
	}

}
