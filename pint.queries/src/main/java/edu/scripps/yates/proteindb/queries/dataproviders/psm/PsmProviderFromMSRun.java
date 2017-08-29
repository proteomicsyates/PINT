package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

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
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					for (String msRunID : msRunIDs) {
						final List<Psm> psmsWithMSRun = PreparedQueries.getPsmsWithMSRun(projectTag, msRunID);
						if (testMode && numPSMs + psmsWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
							PersistenceUtils.addToPSMMapByPsmId(result, psmsWithMSRun.subList(0,
									Math.min(psmsWithMSRun.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
							return result;
						} else {
							PersistenceUtils.addToPSMMapByPsmId(result, psmsWithMSRun);
						}
						numPSMs += psmsWithMSRun.size();
					}
				}
			} else {
				for (String msRunID : msRunIDs) {
					final List<Psm> psmsWithMSRun = PreparedQueries.getPsmsWithMSRun(null, msRunID);

					if (testMode && numPSMs + psmsWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result, psmsWithMSRun.subList(0,
								Math.min(psmsWithMSRun.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psmsWithMSRun);
					}
					numPSMs += psmsWithMSRun.size();
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;

	}

}
