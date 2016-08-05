package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class PsmProviderFromPTMs implements ProteinProviderFromDB {

	private final String ptmName;
	private Map<String, Set<Psm>> result;
	private Set<String> projectTags;

	public PsmProviderFromPTMs(String ptmName) {
		this.ptmName = ptmName;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, null);
				PersistenceUtils.addToPSMMapByPsmId(result, psms);
			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms = PreparedQueries.getPSMsContainingPTM(ptmName, projectTag);
					PersistenceUtils.addToPSMMapByPsmId(result, psms);
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;
	}

}
