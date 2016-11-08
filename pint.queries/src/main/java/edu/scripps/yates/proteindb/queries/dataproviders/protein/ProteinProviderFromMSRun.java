package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

public class ProteinProviderFromMSRun implements ProteinProviderFromDB {
	private final Set<String> msRunIDs;
	private Set<String> projectTags;
	private Map<String, Set<Protein>> result;

	public ProteinProviderFromMSRun(Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new HashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					for (String msRunID : msRunIDs) {
						final List<Protein> proteinsWithMSRun = PreparedQueries.getProteinsWithMSRun(projectTag,
								msRunID);
						if (testMode && numProteins + proteinsWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithMSRun.subList(0, Math
									.min(proteinsWithMSRun.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
							return result;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithMSRun);
						}
						numProteins += proteinsWithMSRun.size();
					}

				}
			} else {
				for (String msRunID : msRunIDs) {
					final List<Protein> proteinsWithMSRun = PreparedQueries.getProteinsWithMSRun(null, msRunID);
					if (testMode && numProteins + proteinsWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithMSRun.subList(0,
								Math.min(proteinsWithMSRun.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithMSRun);
					}
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;

	}
}
