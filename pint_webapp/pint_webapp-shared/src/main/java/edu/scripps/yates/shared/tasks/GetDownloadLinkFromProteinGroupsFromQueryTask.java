package edu.scripps.yates.shared.tasks;

public class GetDownloadLinkFromProteinGroupsFromQueryTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 166509002498441293L;
	private String projectName;

	public GetDownloadLinkFromProteinGroupsFromQueryTask() {

	}

	public GetDownloadLinkFromProteinGroupsFromQueryTask(String projectName, boolean separateNonConclusiveProteins) {
		super(TaskKeyGenerator.getKeyForGetDownloadLinkFromProteinGroupsFromQuery(projectName,
				separateNonConclusiveProteins), TaskType.GET_DOWNLOAD_LINK_FOR_PROTEINGROUPS);
		this.projectName = projectName;
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(projectName);
	}
}
