package edu.scripps.yates.server.tasks;

import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.scripps.yates.shared.util.ProgressStatus;

public abstract class Task implements Serializable {
	private static Logger log = Logger.getLogger(Task.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 3043504234343802801L;
	private String key;
	private ProgressStatus progressStatus = new ProgressStatus();

	public Task() {

	}

	public Task(String key) {
		this.key = key;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "[" + this.getClass().getName() + ", " + key + "]";
	}

	/**
	 * @param currentStep
	 *            the currentStep to set
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
	 * @param progressStatus
	 *            the progressStatus to set
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
		log.info("Setting task description '" + taskDescription + "'");
		progressStatus.setTaskDescription(taskDescription);
	}

	public void incrementProgress(int by) {
		progressStatus.incrementProgress(by);
	}
}
