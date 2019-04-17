package edu.scripps.yates.shared.tasks;

public class GetDownloadLinkFromProteinsfromQueryTask extends Task {
	/**
	 *
	 */
	private static final long serialVersionUID = -7654228054263629916L;

	public GetDownloadLinkFromProteinsfromQueryTask() {

	}

	public GetDownloadLinkFromProteinsfromQueryTask(String projectName) {
		super(TaskKeyGenerator.getKeyForGetDownloadLinkFromProteinsFromQuery(projectName),
				TaskType.GET_DOWNLOAD_LINK_FOR_PROTEINS);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
