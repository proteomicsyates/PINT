package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Collection;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProteinThresholds extends ProteinDataProvider {
	// private final String projectName;
	private final String thresholdName;
	private final boolean pass;

	public ProteinProviderFromProteinThresholds(String thresholdName, boolean pass) {
		this.thresholdName = thresholdName;
		this.pass = pass;
		// this.projectName = projectName;

	}

	@Override
	public Map<String, Collection<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			int numProteins = 0;
			result = new THashMap<String, Collection<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final Map<String, Collection<Protein>> proteinsWithThreshold = PreparedQueries
						.getProteinsWithThreshold(null, thresholdName, pass);
				if (testMode && numProteins + proteinsWithThreshold.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(proteinsWithThreshold,
							QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithThreshold);
				}
				numProteins += proteinsWithThreshold.size();
			} else {
				for (final String projectName : projectTags) {
					final Map<String, Collection<Protein>> proteinsWithThreshold = PreparedQueries
							.getProteinsWithThreshold(projectName, thresholdName, pass);
					if (testMode && numProteins + proteinsWithThreshold.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
								proteinsWithThreshold, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithThreshold);
					}
					numProteins += proteinsWithThreshold.size();
				}
			}
		}
		return result;

	}

}
