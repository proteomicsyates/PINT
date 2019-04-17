package edu.scripps.yates.shared.tasks;

import java.util.Collection;

public class GetProteinsFromQuery extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = -5233321199916449319L;

	public GetProteinsFromQuery() {

	}

	public GetProteinsFromQuery(Collection<String> projectTags, String queryText) {
		super(TaskKeyGenerator.getKeyForGetProteinsFromQuery(projectTags, queryText), TaskType.QUERY_SENT);
	}

	@Override
	public String getTaskDescription() {
		return getType().getSingleTaskMessage(null);
	}

}
