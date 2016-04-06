package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromLabeledAmount implements ProteinProviderFromDB {
	private final String labelName;
	private Set<String> projectTags;
	private Map<String, Set<Protein>> result;

	public ProteinProviderFromLabeledAmount(String labelString) {
		labelName = labelString;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (result == null) {
			result = new HashMap<String, Set<Protein>>();
			if (projectTags != null) {
				for (String projectTag : projectTags) {
					PersistenceUtils.addToMapByPrimaryAcc(result,
							PreparedQueries.getProteinsWithLabeledAmount(projectTag, labelName));
				}
			} else {
				PersistenceUtils.addToMapByPrimaryAcc(result, PreparedQueries.getProteinsWithLabeledAmount(null, labelName));
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;

	}
}
