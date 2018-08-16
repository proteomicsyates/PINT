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

public class ProteinProviderFromProteinScores extends ProteinDataProvider {

	private final String scoreNameString;
	private final String scoreTypeString;

	public ProteinProviderFromProteinScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			int numProteins = 0;

			result = new THashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Protein> proteinsWithScores = PreparedQueries.getProteinsWithScores(scoreNameString,
						scoreTypeString, null);
				if (testMode && numProteins + proteinsWithScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithScores.subList(0,
							Math.min(proteinsWithScores.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithScores);
				}
				numProteins += proteinsWithScores.size();
			} else {
				for (final String projectName : projectTags) {
					final List<Protein> proteinsWithScores = PreparedQueries.getProteinsWithScores(scoreNameString,
							scoreTypeString, projectName);
					if (testMode && numProteins + proteinsWithScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithScores.subList(0,
								Math.min(proteinsWithScores.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithScores);
					}
					numProteins += proteinsWithScores.size();
				}
			}
		}
		return result;
	}

}
