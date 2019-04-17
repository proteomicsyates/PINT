package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetDownloadLinkFromProteinGroupsFromQueryTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 166509002498441293L;

	public GetDownloadLinkFromProteinGroupsFromQueryTask() {

	}

	public GetDownloadLinkFromProteinGroupsFromQueryTask(String projectName, boolean separateNonConclusiveProteins) {
		super(SharedTaskKeyGenerator.getKeyForGetDownloadLinkFromProteinGroupsFromQuery(projectName,
				separateNonConclusiveProteins));
	}
}
