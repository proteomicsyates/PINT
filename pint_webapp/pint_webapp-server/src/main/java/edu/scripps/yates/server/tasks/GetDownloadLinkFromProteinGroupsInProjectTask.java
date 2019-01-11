package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetDownloadLinkFromProteinGroupsInProjectTask extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 166509002498441293L;

	public GetDownloadLinkFromProteinGroupsInProjectTask() {

	}

	public GetDownloadLinkFromProteinGroupsInProjectTask(String projectName,
			boolean separateNonConclusiveProteins) {
		super(SharedTaskKeyGenerator.getKeyForGetDownloadLinkFromProteinGroups(
				projectName, separateNonConclusiveProteins));
	}
}
