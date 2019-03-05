package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProjects extends ProteinDataProvider {

	public ProteinProviderFromProjects(Set<String> projectTags) {
		this.projectTags = projectTags;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null && !projectTags.isEmpty()) {
				for (final String projectTag : projectTags) {
					// change here to the new way of getting proteins and
					// compare with the 3 minutes after load PSM scores

					final List<MsRun> msRuns = PreparedQueries.getMSRunsByProject(projectTag);
					// for (MsRun msRun : msRuns) {
					final List<Protein> proteinsByMSRuns = PreparedCriteria.getProteinsByMSRunsCriteria(msRuns);
					if (testMode && numProteins + proteinsByMSRuns.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByMSRuns.subList(0,
								Math.min(proteinsByMSRuns.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByMSRuns);
					}
					numProteins += proteinsByMSRuns.size();
					// }
					// PersistenceUtils.addToMap(results,
					// PreparedQueries.getProteinsByProjectCondition(sessionID,
					// projectTag, null));
				}
			}
		}
		return result;
	}

}
