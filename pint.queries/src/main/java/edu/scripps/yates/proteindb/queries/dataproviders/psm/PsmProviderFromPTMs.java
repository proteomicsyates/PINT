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

public class PsmProviderFromPTMs implements ProteinProviderFromDB {

	private final String ptmName;
	private Map<String, Set<Psm>> result;
	private Set<String> projectTags;

	public PsmProviderFromPTMs(String ptmName) {
		this.ptmName = ptmName;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, null);
				if (testMode && numPSMs + psms.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psms.subList(0, Math.min(psms.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psms);
				}
				numPSMs += psms.size();
			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, projectTag);
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
