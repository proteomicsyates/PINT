package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.FlexTable;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public abstract class AbstractConditionSelectorForFileWidget extends FlexTable {

	protected PintContext context;
	protected FileTypeBean file;
	private DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask;
	private DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask;

	public AbstractConditionSelectorForFileWidget(PintContext context, FileTypeBean file) {
		this.context = context;
		this.file = file;

	}

	protected DoSomethingTask2<ExperimentalConditionTypeBean> getOnConditionAddedTask() {
		return onConditionAddedTask;
	}

	public void setOnConditionAddedTask(DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask) {
		this.onConditionAddedTask = onConditionAddedTask;
	}

	protected DoSomethingTask2<ExperimentalConditionTypeBean> getOnConditionRemovedTask() {
		return onConditionRemovedTask;
	}

	public void setOnConditionRemovedTask(DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask) {
		this.onConditionRemovedTask = onConditionRemovedTask;
	}

}
