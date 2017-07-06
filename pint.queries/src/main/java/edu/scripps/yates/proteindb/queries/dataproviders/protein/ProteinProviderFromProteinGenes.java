package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinProviderFromProteinGenes implements ProteinProviderFromDB {
	// private final String projectName;
	private final Set<String> geneNames = new THashSet<String>();
	private Map<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromProteinGenes(Collection<String> geneNames) {
		this.geneNames.addAll(geneNames);
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (proteins == null) {
			proteins = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				for (String geneName : geneNames) {
					final Map<String, Set<Protein>> proteinsWithGene = PreparedQueries.getProteinsWithGene(null,
							geneName);
					if (testMode && numProteins + proteinsWithGene.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(proteinsWithGene,
								QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return proteins;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithGene);
					}
					numProteins += proteinsWithGene.size();
				}
			} else {
				for (String projectName : projectTags) {
					for (String geneName : geneNames) {
						final Map<String, Set<Protein>> proteinsWithGene = PreparedQueries
								.getProteinsWithGene(projectName, geneName);
						if (testMode && numProteins + proteinsWithGene.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(
									proteinsWithGene, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return proteins;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithGene);
						}
						numProteins += proteinsWithGene.size();
					}
				}
			}
		}
		return proteins;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		proteins = null;
	}
}
