package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public abstract class AbstractConditionSelectorForFileWidget extends FlexTable {

	protected PintContext context;
	protected FileTypeBean file;
	private final List<DoSomethingTask2<ExperimentalConditionTypeBean>> onConditionAddedTasks = new ArrayList<DoSomethingTask2<ExperimentalConditionTypeBean>>();
	private final List<DoSomethingTask2<ExperimentalConditionTypeBean>> onConditionRemovedTasks = new ArrayList<DoSomethingTask2<ExperimentalConditionTypeBean>>();

	public AbstractConditionSelectorForFileWidget(PintContext context, FileTypeBean file,
			DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask2,
			DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask2) {
		this.context = context;
		this.file = file;
		this.onConditionAddedTasks.add(onConditionAddedTask2);
		this.onConditionRemovedTasks.add(onConditionRemovedTask2);
	}

	protected List<DoSomethingTask2<ExperimentalConditionTypeBean>> getOnConditionAddedTasks() {
		return onConditionAddedTasks;
	}

	public void addOnConditionAddedTask(DoSomethingTask2<ExperimentalConditionTypeBean> onConditionAddedTask) {
		this.onConditionAddedTasks.add(onConditionAddedTask);
	}

	protected List<DoSomethingTask2<ExperimentalConditionTypeBean>> getOnConditionRemovedTasks() {
		return onConditionRemovedTasks;
	}

	public void addOnConditionRemovedTask(DoSomethingTask2<ExperimentalConditionTypeBean> onConditionRemovedTask) {
		this.onConditionRemovedTasks.add(onConditionRemovedTask);
	}

	public abstract List<ExperimentalConditionTypeBean> getConditions();
}
