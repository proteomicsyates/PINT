package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromTissues extends ProteinDataProvider {
	private final Set<String> tissueNames;

	public ProteinProviderFromTissues(Set<String> tissueNames) {
		this.tissueNames = tissueNames;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			int numProteins = 0;
			result = new THashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Protein> proteinsWithTissues = PreparedCriteria.getProteinsWithTissues(null, tissueNames);
				Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
				PersistenceUtils.addToMapByPrimaryAcc(map, proteinsWithTissues);
				if (testMode && numProteins + map.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result,
							QueriesUtil.getProteinSubList(map, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithTissues);
				}
				numProteins += proteinsWithTissues.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Protein> proteinsWithTissues = PreparedCriteria.getProteinsWithTissues(projectTag,
							tissueNames);
					Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
					PersistenceUtils.addToMapByPrimaryAcc(map, proteinsWithTissues);
					if (testMode && numProteins + proteinsWithTissues.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result,
								QueriesUtil.getProteinSubList(map, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithTissues);
					}
					numProteins += proteinsWithTissues.size();
				}
			}
		}
		return result;
	}

}
