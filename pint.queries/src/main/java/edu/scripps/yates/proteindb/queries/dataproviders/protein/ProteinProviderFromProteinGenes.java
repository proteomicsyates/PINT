package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinProviderFromProteinGenes extends ProteinDataProvider {
	// private final String projectName;
	private final Set<String> geneNames = new THashSet<String>();
	private Set<String> projectTags;

	public ProteinProviderFromProteinGenes(Collection<String> geneNames) {
		this.geneNames.addAll(geneNames);
	}

	@Override
	public Map<String, Collection<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Collection<Protein>>();
			int numProteins = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				for (final String geneName : geneNames) {
					final Map<String, Collection<Protein>> proteinsWithGene = PreparedQueries.getProteinsWithGene(null,
							geneName);
					if (testMode && numProteins + proteinsWithGene.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(proteinsWithGene,
								QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithGene);
					}
					numProteins += proteinsWithGene.size();
				}
			} else {
				for (final String projectName : projectTags) {
					for (final String geneName : geneNames) {
						final Map<String, Collection<Protein>> proteinsWithGene = PreparedQueries
								.getProteinsWithGene(projectName, geneName);
						if (testMode && numProteins + proteinsWithGene.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
									proteinsWithGene, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return result;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithGene);
						}
						numProteins += proteinsWithGene.size();
					}
				}
			}
		}
		return result;
	}

}
