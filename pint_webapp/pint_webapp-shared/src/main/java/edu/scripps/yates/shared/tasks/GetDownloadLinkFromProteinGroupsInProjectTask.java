package edu.scripps.yates.shared.tasks;

public class GetDownloadLinkFromProteinGroupsInProjectTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166509002498441293L;

	public GetDownloadLinkFromProteinGroupsInProjectTask() {

	}

	public GetDownloadLinkFromProteinGroupsInProjectTask(String projectName, boolean separateNonConclusiveProteins) {
		super(TaskKeyGenerator.getKeyForGetDownloadLinkFromProteinGroups(projectName, separateNonConclusiveProteins),
				TaskType.GET_DOWNLOAD_LINK_FOR_PROTEINGROUPS);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}
}
