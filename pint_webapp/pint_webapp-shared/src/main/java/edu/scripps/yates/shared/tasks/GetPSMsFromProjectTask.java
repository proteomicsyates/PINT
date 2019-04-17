package edu.scripps.yates.shared.tasks;

public class GetPSMsFromProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5921782024955982928L;
	private String projectTag;

	public GetPSMsFromProjectTask() {

	}

	public GetPSMsFromProjectTask(String projectTag) {
		super(TaskKeyGenerator.getKeyForGetPSMsFromProjectTask(projectTag), TaskType.PSMS_BY_PROJECT);
		this.projectTag = projectTag;
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(projectTag);
	}
}
