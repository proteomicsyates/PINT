package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class PsmProviderFromMSRun implements ProteinProviderFromDB {
	private final Set<String> msRunIDs;
	private Set<String> projectTags;
	private Map<String, Set<Psm>> result;
	private final String sessionId;

	public PsmProviderFromMSRun(String sessionId, Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
		this.sessionId = sessionId;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					for (String msRunID : msRunIDs) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								PreparedQueries.getPsmsWithMSRun(projectTag, msRunID));
					}
				}
			} else {
				for (String msRunID : msRunIDs) {
					PersistenceUtils.addToPSMMapByPsmId(result, PreparedQueries.getPsmsWithMSRun(null, msRunID));
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;

	}

}
