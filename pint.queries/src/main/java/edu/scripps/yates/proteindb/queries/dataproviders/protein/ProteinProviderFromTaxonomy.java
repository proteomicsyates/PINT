package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromTaxonomy implements ProteinProviderFromDB {
	private final String organismName;
	private final String ncbiTaxID;
	private HashMap<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				PersistenceUtils.addToMapByPrimaryAcc(proteins,
						PreparedQueries.getProteinsWithTaxonomy(null, organismName, ncbiTaxID));
			} else {
				for (String projectTag : projectTags) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins,
							PreparedQueries.getProteinsWithTaxonomy(projectTag, organismName, ncbiTaxID));
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

	public Map<String, Set<Psm>> getPSMMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}
}
