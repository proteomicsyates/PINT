package edu.scripps.yates.client.tasks;

public enum TaskType {
	PROTEINS_BY_PROJECT("Loading proteins from project %...", "Loading proteins from % projects..."), //
	PSMS_BY_PROJECT("Loading PSMs from project %...", "Loading PSMs from % projects..."), //
	GROUP_PROTEINS("Grouping % proteins...", "Grouping % proteins..."), //
	PSMS_BY_PROTEIN("Loading PSMs from protein %...", "Loading PSMs from % proteins..."), //
	PSMS_BY_PROTEIN_GROUP("Loading PSMs from protein group %...", "Loading PSMs from % protein groups..."), //
	QUERY_SENT("Waiting for query results. Please wait...", "Waiting for query results. Please wait..."), //
	PROTEINS_BY_PEPTIDE("Retrieving peptides shared by % protein...", "Retrieving peptides shared by protein group"), //
	CANCELLING_REQUEST("Cancelling request...", "Cancelling requests...");

	private final String singleTaskMessage;
	private final String multipleTaskMessage;

	private TaskType(String taskMessage, String multipleTaskMessage) {
		singleTaskMessage = taskMessage;
		this.multipleTaskMessage = multipleTaskMessage;
	}

	/**
	 * @return the taskMessage
	 */
	public String getSingleTaskMessage(String inset) {
		if (inset != null) {
			if (singleTaskMessage.contains("%"))
				return singleTaskMessage.replace("%", inset);
		}
		return singleTaskMessage;
	}

	/**
	 * @return the multipleTaskMessage
	 */
	public String getMultipleTaskMessage(String inset) {
		if (inset != null) {
			if (multipleTaskMessage.contains("%"))
				return multipleTaskMessage.replace("%", inset);
		}
		return multipleTaskMessage;
	}

}
