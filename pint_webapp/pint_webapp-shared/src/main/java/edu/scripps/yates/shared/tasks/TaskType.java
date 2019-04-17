package edu.scripps.yates.shared.tasks;

public enum TaskType {
	PROTEINS_BY_PROJECT("Loading proteins from project %...", "Loading proteins from % projects..."), //
	PSMS_BY_PROJECT("Loading PSMs from project %...", "Loading PSMs from % projects..."), //
	GROUP_PROTEINS("Grouping % proteins...", "Grouping % proteins..."), //
	PSMS_BY_PROTEIN("Loading PSMs from protein %...", "Loading PSMs from % proteins..."), //
	PSMS_BY_PROTEIN_GROUP("Loading PSMs from protein group %...", "Loading PSMs from % protein groups..."), //
	QUERY_SENT("Waiting for query results. Please wait...", "Waiting for query results. Please wait..."), //
	SHOW_PEPTIDES_SHARED_BY_PROTEINS("Retrieving peptides shared by protein %...", "Retrieving peptides shared by protein group"), //
	CANCELLING_REQUEST("Cancelling request...", "Cancelling requests..."), //
	PROTEINS_FROM_FILE("Retrieving proteins from file...", "Retrieving proteins from files..."),
	GET_DOWNLOAD_LINK_FOR_PROTEINGROUPS("Generating download link for protein groups"),
	GET_DOWNLOAD_LINK_FOR_PROTEINS("Generating download link for proteins");

	private String singleTaskMessage;
	private String multipleTaskMessage;

	private TaskType() {

	}

	private TaskType(String taskMessage) {
		this(taskMessage, taskMessage);
	}

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
