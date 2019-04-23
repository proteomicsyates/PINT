package edu.scripps.yates.shared.tasks;

public class GetDownloadLinkForInputFilesOfProjectTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 166509002498441293L;
	private String projectName;

	public GetDownloadLinkForInputFilesOfProjectTask() {

	}

	public GetDownloadLinkForInputFilesOfProjectTask(String projectName) {
		super(TaskKeyGenerator.getKeyForGetDownloadLinkForInputFilesOfProjectTask(projectName),
				TaskType.GET_DOWNLOAD_LINK_FOR_PROTEINGROUPS);
		this.projectName = projectName;
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(projectName);
	}
}
