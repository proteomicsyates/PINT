package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetDownloadLinkFromProteinsInProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7654228054263629916L;

	public GetDownloadLinkFromProteinsInProjectTask() {

	}

	public GetDownloadLinkFromProteinsInProjectTask(String projectName) {
		super(SharedTaskKeyGenerator
				.getKeyForGetDownloadLinkFromProteins(projectName));
	}

}
