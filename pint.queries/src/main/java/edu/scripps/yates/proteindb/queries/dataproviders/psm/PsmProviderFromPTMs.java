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

public class PsmProviderFromPTMs extends PsmDataProvider {

	private final String ptmName;

	public PsmProviderFromPTMs(String ptmName) {
		this.ptmName = ptmName;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, null);
				if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psms.subList(0, Math.min(psms.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psms);
				}
				numPSMs += psms.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, projectTag);
					if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								psms.subList(0, Math.min(psms.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psms);
					}
					numPSMs += psms.size();
				}
			}
		}
		return result;
	}

}
