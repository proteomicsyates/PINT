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

public class ProteinProviderFromLabeledAmount implements ProteinProviderFromDB {
	private final String labelName;
	private Set<String> projectTags;
	private Map<String, Set<Protein>> result;

	public ProteinProviderFromLabeledAmount(String labelString) {
		labelName = labelString;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					final List<Protein> proteinsWithLabeledAmount = PreparedQueries
							.getProteinsWithLabeledAmount(projectTag, labelName);
					if (testMode
							&& numProteins + proteinsWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result,
								proteinsWithLabeledAmount.subList(0, Math.min(proteinsWithLabeledAmount.size(),
										QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithLabeledAmount);
					}
					numProteins += proteinsWithLabeledAmount.size();
				}
			} else {
				final List<Protein> proteinsWithLabeledAmount = PreparedQueries.getProteinsWithLabeledAmount(null,
						labelName);
				if (testMode && numProteins + proteinsWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithLabeledAmount.subList(0, Math
							.min(proteinsWithLabeledAmount.size(), QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins)));
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithLabeledAmount);
				}
				numProteins += proteinsWithLabeledAmount.size();
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
