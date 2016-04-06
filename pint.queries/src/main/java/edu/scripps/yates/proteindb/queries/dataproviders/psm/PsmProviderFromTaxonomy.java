package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class PsmProviderFromTaxonomy implements ProteinProviderFromDB {
	private final String organismName;
	private final String ncbiTaxID;
	private Set<String> projectTags;
	private HashMap<String, Set<Psm>> psms;

	public PsmProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (psms == null) {
			psms = new HashMap<String, Set<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				PersistenceUtils.addToPSMMapByPsmId(psms, PreparedQueries.getPsmsWithTaxonomy(null, organismName, ncbiTaxID));
			} else {
				for (String projectTag : projectTags) {
					PersistenceUtils.addToPSMMapByPsmId(psms,
							PreparedQueries.getPsmsWithTaxonomy(projectTag, organismName, ncbiTaxID));
				}
			}
		}
		return psms;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		psms = null;

	}

}
