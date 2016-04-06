package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromProteinScores implements ProteinProviderFromDB {

	private final String scoreNameString;
	private final String scoreTypeString;
	private Map<String, Set<Protein>> results;
	private Set<String> projectNames;

	public ProteinProviderFromProteinScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (results == null) {
			results = new HashMap<String, Set<Protein>>();
			if (projectNames == null || projectNames.isEmpty()) {
				PersistenceUtils.addToMapByPrimaryAcc(results,
						PreparedQueries.getProteinsWithScores(scoreNameString, scoreTypeString, null));

			} else {
				for (String projectName : projectNames) {
					PersistenceUtils.addToMapByPrimaryAcc(results,
							PreparedQueries.getProteinsWithScores(scoreNameString, scoreTypeString, projectName));
				}
			}
		}
		return results;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		results = null;

	}

}
