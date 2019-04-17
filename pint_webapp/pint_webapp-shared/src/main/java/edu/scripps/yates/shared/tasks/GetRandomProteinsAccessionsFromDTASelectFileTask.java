package edu.scripps.yates.shared.tasks;

public class GetRandomProteinsAccessionsFromDTASelectFileTask extends Task {
	/**
	 *
	 */
	private static final long serialVersionUID = -5381912462047444549L;

	public GetRandomProteinsAccessionsFromDTASelectFileTask() {

	}

	public GetRandomProteinsAccessionsFromDTASelectFileTask(String fileAbsolutePath, long fileLength,
			String discardDecoyExpression) {
		super(TaskKeyGenerator.getKeyForGetRandomProteinsAccessionsFromDTASelectFileTask(fileAbsolutePath, fileLength,
				discardDecoyExpression), TaskType.PROTEINS_FROM_FILE);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
