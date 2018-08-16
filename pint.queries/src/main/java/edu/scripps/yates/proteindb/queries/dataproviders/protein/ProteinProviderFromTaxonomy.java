package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromTaxonomy extends ProteinDataProvider {
	private final String organismName;
	private final String ncbiTaxID;

	public ProteinProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			int numProteins = 0;
			result = new THashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final Map<String, Set<Protein>> proteinsWithTaxonomy = PreparedQueries.getProteinsWithTaxonomy(null,
						organismName, ncbiTaxID);
				if (testMode && numProteins + proteinsWithTaxonomy.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(proteinsWithTaxonomy,
							QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithTaxonomy);
				}
				numProteins += proteinsWithTaxonomy.size();
			} else {
				for (final String projectTag : projectTags) {
					final Map<String, Set<Protein>> proteinsWithTaxonomy = PreparedQueries
							.getProteinsWithTaxonomy(projectTag, organismName, ncbiTaxID);
					if (testMode && numProteins + proteinsWithTaxonomy.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
								proteinsWithTaxonomy, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithTaxonomy);
					}
					numProteins += proteinsWithTaxonomy.size();
				}
			}
		}
		return result;
	}

}
