package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromProjects implements ProteinProviderFromDB {

	private Map<String, Set<Protein>> results;
	private Set<String> projectTags;

	public ProteinProviderFromProjects(Set<String> projectTags) {
		this.projectTags = projectTags;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (results == null) {
			results = new HashMap<String, Set<Protein>>();
			if (projectTags != null && !projectTags.isEmpty()) {
				for (String projectTag : projectTags) {
					// change here to the new way of getting proteins and
					// compare with the 3 minutes after load PSM scores

					final List<MsRun> msRuns = PreparedQueries.getMSRunsByProject(projectTag);
					// for (MsRun msRun : msRuns) {
					PersistenceUtils.addToMapByPrimaryAcc(results, PreparedQueries.getProteinsByMSRuns(msRuns));
					// }
					// PersistenceUtils.addToMap(results,
					// PreparedQueries.getProteinsByProjectCondition(sessionID,
					// projectTag, null));
				}
			}
		}
		return results;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		results = null;

	}

}
