package edu.scripps.yates.client.gui.incrementalCommands;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;

public class IncrementalListAdder<T, Y> implements RepeatingCommand {
	private final List<T> targetList;
	private final static int defaultWorkChunk = 100;
	private final int workChunk;
	private final List<T> data;
	private int index = 0;
	private final DoSomethingTask<Y> afterDoneTask;

	public IncrementalListAdder(List<T> targetList, List<T> data, int workChunk) {
		this.targetList = targetList;
		this.data = data;
		this.workChunk = workChunk;
		afterDoneTask = null;
	}

	public IncrementalListAdder(List<T> targetList, List<T> data) {
		this.targetList = targetList;
		this.data = data;
		workChunk = defaultWorkChunk;
		afterDoneTask = null;
	}

	public IncrementalListAdder(List<T> targetList, List<T> data,
			int workChunk, DoSomethingTask<Y> afterDoneTask) {
		this.targetList = targetList;
		this.data = data;
		this.workChunk = workChunk;
		this.afterDoneTask = afterDoneTask;
	}

	public IncrementalListAdder(List<T> targetList, List<T> data,
			DoSomethingTask<Y> afterDoneTask) {
		this.targetList = targetList;
		this.data = data;
		workChunk = defaultWorkChunk;
		this.afterDoneTask = afterDoneTask;
	}

	@Override
	public boolean execute() {
		GWT.log("IncrementalListAdder execute call. Working from " + index
				+ " to " + data.size());
		int numIterationsInThisCall = 0;
		// iterate from 'index'
		for (int i = index; i < data.size(); i++) {
			index = i;
			final T element = data.get(i);

			targetList.add(element);

			numIterationsInThisCall++;
			if (numIterationsInThisCall == workChunk) {
				GWT.log(data.size() - index + " elements left to add");
				return true;
			}
		}
		GWT.log("All elements added to targetList");
		if (afterDoneTask != null) {
			GWT.log("Executing after done task");
			afterDoneTask.doSomething();
		}
		GWT.log("IncrementalListAdder is done. No more calls are needed");

		return false;
	}

}
