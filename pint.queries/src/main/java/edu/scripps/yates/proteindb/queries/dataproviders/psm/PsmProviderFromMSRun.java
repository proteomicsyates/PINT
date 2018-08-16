package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PsmDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromMSRun extends PsmDataProvider {
	private final Set<String> msRunIDs;

	public PsmProviderFromMSRun(String sessionId, Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					for (final String msRunID : msRunIDs) {
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
				for (final String msRunID : msRunIDs) {
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

}
