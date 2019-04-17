package edu.scripps.yates.shared.tasks;

import java.io.Serializable;

import edu.scripps.yates.shared.util.ProgressStatus;

public abstract class Task implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3043504234343802801L;
	private TaskKey key;
	private ProgressStatus progressStatus = new ProgressStatus();
	private TaskType type;

	public Task() {

	}

	public Task(TaskKey key, TaskType taskType) {
		this.key = key;
		this.type = taskType;
	}

	/**
	 * @return the key
	 */
	public TaskKey getKey() {
		return key;
	}

	public void setKey(TaskKey key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getName() + ", " + key + "]";
	}

	/**
	 * @param currentStep the currentStep to set
	 */
	public void setCurrentStep(long currentStep) {
		progressStatus.setCurrentStep(currentStep);
	}

	/**
	 * @return the progressStatus
	 */
	public ProgressStatus getProgressStatus() {
		return progressStatus;
	}

	/**
	 * @param progressStatus the progressStatus to set
	 */
	public void setProgressStatus(ProgressStatus progressStatus) {
		this.progressStatus = progressStatus;
	}

	public void setMaxSteps(long maxSteps) {
		progressStatus.setMaxSteps(maxSteps);
	}

	public void setAsFinished() {
		progressStatus.setAsFinished();
	}

	public void setTaskDescription(String taskDescription) {
		com.google.gwt.core.shared.GWT.log("Setting task description '" + taskDescription + "'");
		progressStatus.setTaskDescription(taskDescription);
	}

	public void incrementProgress(int by) {
		progressStatus.incrementProgress(by);
	}

	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	public abstract String getTaskDescription();
}
