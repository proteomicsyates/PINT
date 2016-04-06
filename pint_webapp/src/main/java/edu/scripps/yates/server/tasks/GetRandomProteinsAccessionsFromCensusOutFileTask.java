package edu.scripps.yates.server.tasks;

import java.io.File;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetRandomProteinsAccessionsFromCensusOutFileTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 5073911873429957955L;

	public GetRandomProteinsAccessionsFromCensusOutFileTask() {

	}

	public GetRandomProteinsAccessionsFromCensusOutFileTask(File file, String discardDecoyExpression) {
		super(SharedTaskKeyGenerator.getKeyForGetRandomProteinsAccessionsFromCensusOutFileTask(file.getAbsolutePath(),
				file.length(), discardDecoyExpression));
	}

}
