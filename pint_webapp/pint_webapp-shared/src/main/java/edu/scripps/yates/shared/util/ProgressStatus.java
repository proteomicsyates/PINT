package edu.scripps.yates.shared.util;

import java.io.Serializable;

public class ProgressStatus implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 383858270548807382L;

	private double progressPercentage;
	private long maxSteps = 0;
	private long currentStep = 0;
	private String taskDescription;

	public ProgressStatus() {

	}

	/**
	 * @return the progressPercentage
	 */
	public double getProgressPercentage() {
		if (maxSteps > 0) {
			progressPercentage = currentStep * 100.0 / maxSteps;
		} else {
			progressPercentage = 0.0;
		}
		return progressPercentage;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return taskDescription + "[" + getProgressPercentage() + "%, " + currentStep + "/" + maxSteps + "]";
	}

	/**
	 * @return the maxSteps
	 */
	public long getMaxSteps() {
		return maxSteps;
	}

	/**
	 * @param maxSteps
	 *            the maxSteps to set
	 */
	public void setMaxSteps(long maxSteps) {
		this.maxSteps = maxSteps;
	}

	/**
	 * @return the currentStep
	 */
	public long getCurrentStep() {
		return currentStep;
	}

	/**
	 * @param currentStep
	 *            the currentStep to set
	 */
	public void setCurrentStep(long currentStep) {
		this.currentStep = currentStep;
	}

	public void setAsFinished() {
		if (maxSteps <= 0) {
			maxSteps = 100;
		}
		currentStep = maxSteps;
	}

	/**
	 * @return the taskDescription
	 */
	public String getTaskDescription() {
		return taskDescription;
	}

	/**
	 * @param taskDescription
	 *            the taskDescription to set
	 */
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public void incrementProgress(int by) {
		currentStep += by;
		if (currentStep > maxSteps)
			currentStep = maxSteps;
	}

}
