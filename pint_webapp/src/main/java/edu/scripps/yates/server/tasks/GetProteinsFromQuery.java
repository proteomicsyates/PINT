package edu.scripps.yates.server.tasks;

import java.util.Collection;

import edu.scripps.yates.shared.tasks.SharedTaskKeyGenerator;

public class GetProteinsFromQuery extends Task {

	/**
	 *
	 */
	private static final long serialVersionUID = -5233321199916449319L;

	public GetProteinsFromQuery() {

	}

	public GetProteinsFromQuery(Collection<String> projectTags, String queryInOrder) {
		super(SharedTaskKeyGenerator.getKeyForGetProteinsFromQuery(projectTags, queryInOrder));
	}

}
