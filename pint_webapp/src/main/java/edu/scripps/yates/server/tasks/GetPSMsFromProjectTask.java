package edu.scripps.yates.server.tasks;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetPSMsFromProjectTask extends Task {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5921782024955982928L;

	public GetPSMsFromProjectTask() {

	}

	public GetPSMsFromProjectTask(String projectTag) {
		super(SharedTaskKeyGenerator
				.getKeyForGetPSMsFromProjectTask(projectTag));
	}
}
