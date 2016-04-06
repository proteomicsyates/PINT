package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetDownloadLinkFromProteinsfromQueryTask extends Task {
	/**
	 *
	 */
	private static final long serialVersionUID = -7654228054263629916L;

	public GetDownloadLinkFromProteinsfromQueryTask() {

	}

	public GetDownloadLinkFromProteinsfromQueryTask(String projectName) {
		super(SharedTaskKeyGenerator.getKeyForGetDownloadLinkFromProteinsFromQuery(projectName));
	}

}
