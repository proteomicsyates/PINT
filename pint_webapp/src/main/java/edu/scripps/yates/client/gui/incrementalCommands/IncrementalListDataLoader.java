package edu.scripps.yates.client.gui.incrementalCommands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.view.client.ListDataProvider;

import edu.scripps.yates.shared.model.interfaces.ContainsId;

public class IncrementalListDataLoader<T extends ContainsId, Y> implements
		RepeatingCommand {
	private final ListDataProvider<T> dataProvider;
	private final static int defaultWorkChunk = 100;
	private final int workChunk;
	private final List<T> data;
	private int index = 0;
	private final Set<String> uniqueIds = new HashSet<String>();
	private final DoSomethingTask<Y> afterDoneTask;

	public IncrementalListDataLoader(ListDataProvider<T> dataProvider,
			List<T> data, int workChunk) {
		this.dataProvider = dataProvider;
		this.data = data;
		this.workChunk = workChunk;
		afterDoneTask = null;
	}

	public IncrementalListDataLoader(ListDataProvider<T> dataProvider,
			List<T> data) {
		this.dataProvider = dataProvider;
		this.data = data;
		workChunk = defaultWorkChunk;
		afterDoneTask = null;
	}

	public IncrementalListDataLoader(ListDataProvider<T> dataProvider,
			List<T> data, int workChunk, DoSomethingTask<Y> afterDoneTask) {
		this.dataProvider = dataProvider;
		this.data = data;
		this.workChunk = workChunk;
		this.afterDoneTask = afterDoneTask;
	}

	public IncrementalListDataLoader(ListDataProvider<T> dataProvider,
			List<T> data, DoSomethingTask<Y> afterDoneTask) {
		this.dataProvider = dataProvider;
		this.data = data;
		workChunk = defaultWorkChunk;
		this.afterDoneTask = afterDoneTask;
	}

	@Override
	public boolean execute() {
		GWT.log("DataLoader execute call. Working from " + index + " to "
				+ data.size());
		int numIterationsInThisCall = 0;
		// iterate from 'index'
		for (int i = index; i < data.size(); i++) {
			index = i;
			final T element = data.get(i);
			if (!uniqueIds.contains(element.getId())) {
				dataProvider.getList().add(element);
				uniqueIds.add(element.getId());
			}
			numIterationsInThisCall++;
			if (numIterationsInThisCall == workChunk) {
				GWT.log(data.size() - index + " elements left to add");
				dataProvider.refresh();
				return true;
			}
		}
		GWT.log("All elements added to dataProvider");
		if (afterDoneTask != null) {
			GWT.log("Executing after done task");
			afterDoneTask.doSomething();
		}
		GWT.log("DataLoader is done. No more calls are needed");

		return false;
	}

}
