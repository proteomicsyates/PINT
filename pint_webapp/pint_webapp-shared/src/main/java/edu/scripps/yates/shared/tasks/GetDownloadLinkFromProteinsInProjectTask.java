package edu.scripps.yates.shared.tasks;

public class GetDownloadLinkFromProteinsInProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7654228054263629916L;

	public GetDownloadLinkFromProteinsInProjectTask() {

	}

	public GetDownloadLinkFromProteinsInProjectTask(String projectName) {
		super(TaskKeyGenerator.getKeyForGetDownloadLinkFromProteins(projectName),
				TaskType.GET_DOWNLOAD_LINK_FOR_PROTEINS);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
