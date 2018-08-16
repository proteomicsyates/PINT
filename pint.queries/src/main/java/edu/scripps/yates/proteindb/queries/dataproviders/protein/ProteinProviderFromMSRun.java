package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromMSRun extends ProteinDataProvider {
	private final Set<String> msRunIDs;

	public ProteinProviderFromMSRun(Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					for (final String msRunID : msRunIDs) {
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
				for (final String msRunID : msRunIDs) {
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

}
