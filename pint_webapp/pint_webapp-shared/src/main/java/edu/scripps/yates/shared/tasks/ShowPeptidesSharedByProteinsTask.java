package edu.scripps.yates.shared.tasks;

public class ShowPeptidesSharedByProteinsTask extends Task {
	/**
	 *
	 */
	private static final long serialVersionUID = -5381912462047444549L;
	private String proteinAccessions;

	public ShowPeptidesSharedByProteinsTask() {

	}

	public ShowPeptidesSharedByProteinsTask(String proteinAccessions) {
		super(TaskKeyGenerator.getKeyForShowPeptidesSharedByProteinsTask(proteinAccessions),
				TaskType.SHOW_PEPTIDES_SHARED_BY_PROTEINS);
		this.proteinAccessions = proteinAccessions;
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(proteinAccessions);
	}

}
