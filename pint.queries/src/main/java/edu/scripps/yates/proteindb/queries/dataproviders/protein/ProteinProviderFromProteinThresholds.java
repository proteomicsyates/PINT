package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromProteinThresholds implements ProteinProviderFromDB {
	// private final String projectName;
	private final String thresholdName;
	private final boolean pass;
	private Map<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromProteinThresholds(String thresholdName, boolean pass) {
		this.thresholdName = thresholdName;
		this.pass = pass;
		// this.projectName = projectName;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				PersistenceUtils.addToMapByPrimaryAcc(proteins,
						PreparedQueries.getProteinsWithThreshold(null, thresholdName, pass));
			} else {
				for (String projectName : projectTags) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins,
							PreparedQueries.getProteinsWithThreshold(projectName, thresholdName, pass));
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
