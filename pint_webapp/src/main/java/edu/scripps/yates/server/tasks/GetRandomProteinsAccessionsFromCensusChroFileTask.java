package edu.scripps.yates.server.tasks;

import java.io.File;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetRandomProteinsAccessionsFromCensusChroFileTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 4898720580801108831L;

	public GetRandomProteinsAccessionsFromCensusChroFileTask() {

	}

	public GetRandomProteinsAccessionsFromCensusChroFileTask(File file,
			String discardDecoyExpression) {
		super(SharedTaskKeyGenerator
				.getKeyForGetRandomProteinsAccessionsFromCensusChroFileTask(
						file.getAbsolutePath(), file.length(),
						discardDecoyExpression));
	}

}
