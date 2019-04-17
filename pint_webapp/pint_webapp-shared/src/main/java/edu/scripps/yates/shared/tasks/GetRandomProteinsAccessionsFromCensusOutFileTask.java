package edu.scripps.yates.shared.tasks;

public class GetRandomProteinsAccessionsFromCensusOutFileTask extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = 5073911873429957955L;

	public GetRandomProteinsAccessionsFromCensusOutFileTask() {

	}

	public GetRandomProteinsAccessionsFromCensusOutFileTask(String fileAbsolutePath, long fileLength,
			String discardDecoyExpression) {
		super(TaskKeyGenerator.getKeyForGetRandomProteinsAccessionsFromCensusOutFileTask(fileAbsolutePath, fileLength,
				discardDecoyExpression), TaskType.PROTEINS_FROM_FILE);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
