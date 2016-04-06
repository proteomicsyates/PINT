package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromProteinGenes implements ProteinProviderFromDB {
	// private final String projectName;
	private final Set<String> geneNames = new HashSet<String>();
	private Map<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromProteinGenes(Collection<String> geneNames) {
		this.geneNames.addAll(geneNames);
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				for (String geneName : geneNames) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins, PreparedQueries.getProteinsWithGene(null, geneName));
				}
			} else {
				for (String projectName : projectTags) {
					for (String geneName : geneNames) {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, PreparedQueries.getProteinsWithGene(projectName, geneName));
					}
				}
			}
		}
		return proteins;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		proteins = null;
	}
}
