package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

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
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (proteins == null) {
			int numProteins = 0;
			proteins = new HashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final Map<String, Set<Protein>> proteinsWithThreshold = PreparedQueries.getProteinsWithThreshold(null,
						thresholdName, pass);
				if (testMode && numProteins + proteinsWithThreshold.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(proteinsWithThreshold,
							QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return proteins;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithThreshold);
				}
				numProteins += proteinsWithThreshold.size();
			} else {
				for (String projectName : projectTags) {
					final Map<String, Set<Protein>> proteinsWithThreshold = PreparedQueries
							.getProteinsWithThreshold(projectName, thresholdName, pass);
					if (testMode && numProteins + proteinsWithThreshold.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(
								proteinsWithThreshold, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return proteins;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithThreshold);
					}
					numProteins += proteinsWithThreshold.size();
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
