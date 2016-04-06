package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetProteinsFromProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2378741807806928132L;

	public GetProteinsFromProjectTask() {

	}

	public GetProteinsFromProjectTask(String projectName, String uniprotVersion) {
		super(SharedTaskKeyGenerator.getKeyForGetProteinsFromProjectTask(
				projectName, uniprotVersion));
	}

}
