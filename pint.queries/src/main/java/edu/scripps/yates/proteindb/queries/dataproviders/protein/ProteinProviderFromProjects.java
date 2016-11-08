package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

public class ProteinProviderFromProjects implements ProteinProviderFromDB {

	private Map<String, Set<Protein>> results;
	private Set<String> projectTags;

	public ProteinProviderFromProjects(Set<String> projectTags) {
		this.projectTags = projectTags;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (results == null) {
			results = new HashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null && !projectTags.isEmpty()) {
				for (String projectTag : projectTags) {
					// change here to the new way of getting proteins and
					// compare with the 3 minutes after load PSM scores

					final List<MsRun> msRuns = PreparedQueries.getMSRunsByProject(projectTag);
					// for (MsRun msRun : msRuns) {
					final List<Protein> proteinsByMSRuns = PreparedQueries.getProteinsByMSRuns(msRuns);
					if (testMode && numProteins + proteinsByMSRuns.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByMSRuns.subList(0,
								Math.min(proteinsByMSRuns.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return results;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByMSRuns);
					}
					numProteins += proteinsByMSRuns.size();
					// }
					// PersistenceUtils.addToMap(results,
					// PreparedQueries.getProteinsByProjectCondition(sessionID,
					// projectTag, null));
				}
			}
		}
		return results;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		results = null;

	}

}
