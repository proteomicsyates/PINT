package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class PsmProviderFromPsmLabeledAmount implements ProteinProviderFromDB {
	private final String labelName;
	private Set<String> projectTags;
	private Map<String, Set<Psm>> result;
	private final Boolean singleton;

	public PsmProviderFromPsmLabeledAmount(String labelString, Boolean singleton) {
		labelName = labelString;
		this.singleton = singleton;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							PreparedQueries.getPSMsWithLabeledAmount(projectTag, labelName, singleton));
				}
			} else {
				PersistenceUtils.addToPSMMapByPsmId(result,
						PreparedQueries.getPSMsWithLabeledAmount(null, labelName, singleton));
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
