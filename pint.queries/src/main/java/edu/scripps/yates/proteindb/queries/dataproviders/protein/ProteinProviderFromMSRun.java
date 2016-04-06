package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromMSRun implements ProteinProviderFromDB {
	private final Set<String> msRunIDs;
	private Set<String> projectTags;
	private Map<String, Set<Protein>> result;

	public ProteinProviderFromMSRun(Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (result == null) {
			result = new HashMap<String, Set<Protein>>();
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					for (String msRunID : msRunIDs) {
						PersistenceUtils.addToMapByPrimaryAcc(result, PreparedQueries.getProteinsWithMSRun(projectTag, msRunID));
					}

				}
			} else {
				for (String msRunID : msRunIDs) {
					PersistenceUtils.addToMapByPrimaryAcc(result, PreparedQueries.getProteinsWithMSRun(null, msRunID));
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;

	}
}
