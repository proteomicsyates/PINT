package edu.scripps.yates.shared.tasks;

public class GetProteinsFromProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2378741807806928132L;
	private String projectName;

	public GetProteinsFromProjectTask() {

	}

	public GetProteinsFromProjectTask(String projectName, String uniprotVersion) {
		super(TaskKeyGenerator.getKeyForGetProteinsFromProjectTask(projectName, uniprotVersion),
				TaskType.PROTEINS_BY_PROJECT);
		this.projectName = projectName;
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(projectName);
	}

}
