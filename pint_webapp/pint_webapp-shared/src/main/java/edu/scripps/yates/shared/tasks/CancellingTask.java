package edu.scripps.yates.shared.tasks;

public class CancellingTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 251415155243576383L;

	public CancellingTask() {
		super(TaskKeyGenerator.getKeyForCancellingTask(), TaskType.GROUP_PROTEINS);
	}

	@Override
	public String getTaskDescription() {
		return getType().getMultipleTaskMessage(null);
	}

}
