package edu.scripps.yates.shared.tasks;

public class GetRandomProteinsAccessionsFromCensusChroFileTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 4898720580801108831L;

	public GetRandomProteinsAccessionsFromCensusChroFileTask() {

	}

	public GetRandomProteinsAccessionsFromCensusChroFileTask(String fileAbsolutePath, long fileLength,
			String discardDecoyExpression) {
		super(TaskKeyGenerator.getKeyForGetRandomProteinsAccessionsFromCensusChroFileTask(fileAbsolutePath, fileLength,
				discardDecoyExpression), TaskType.PROTEINS_FROM_FILE);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
