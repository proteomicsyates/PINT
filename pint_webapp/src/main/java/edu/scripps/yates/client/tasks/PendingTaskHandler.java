package edu.scripps.yates.client.tasks;

public interface PendingTaskHandler {
	public void onTaskRemoved();

	public void onTaskAdded();
}
