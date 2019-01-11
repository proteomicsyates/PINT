package edu.scripps.yates.server.tasks;

import java.io.File;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetRandomProteinsAccessionsFromDTASelectFileTask extends Task {
	/**
	 *
	 */
	private static final long serialVersionUID = -5381912462047444549L;

	public GetRandomProteinsAccessionsFromDTASelectFileTask() {

	}

	public GetRandomProteinsAccessionsFromDTASelectFileTask(File file,
			String discardDecoyExpression) {
		super(SharedTaskKeyGenerator
				.getKeyForGetRandomProteinsAccessionsFromDTASelectFileTask(
						file.getAbsolutePath(), file.length(),
						discardDecoyExpression));
	}

}
