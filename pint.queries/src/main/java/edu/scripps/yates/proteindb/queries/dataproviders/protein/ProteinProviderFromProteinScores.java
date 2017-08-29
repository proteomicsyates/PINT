package edu.scripps.yates.proteindb.queries.dataproviders.protein;

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

public class ProteinProviderFromProteinScores implements ProteinProviderFromDB {

	private final String scoreNameString;
	private final String scoreTypeString;
	private Map<String, Set<Protein>> results;
	private Set<String> projectNames;

	public ProteinProviderFromProteinScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (results == null) {
			int numProteins = 0;

			results = new THashMap<String, Set<Protein>>();
			if (projectNames == null || projectNames.isEmpty()) {
				final List<Protein> proteinsWithScores = PreparedQueries.getProteinsWithScores(scoreNameString,
						scoreTypeString, null);
				if (testMode && numProteins + proteinsWithScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(results, proteinsWithScores.subList(0,
							Math.min(proteinsWithScores.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
					return results;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(results, proteinsWithScores);
				}
				numProteins += proteinsWithScores.size();
			} else {
				for (String projectName : projectNames) {
					final List<Protein> proteinsWithScores = PreparedQueries.getProteinsWithScores(scoreNameString,
							scoreTypeString, projectName);
					if (testMode && numProteins + proteinsWithScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(results, proteinsWithScores.subList(0,
								Math.min(proteinsWithScores.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return results;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(results, proteinsWithScores);
					}
					numProteins += proteinsWithScores.size();
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
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		results = null;

	}

}
