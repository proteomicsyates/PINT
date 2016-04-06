package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class PsmProviderFromPsmScores implements ProteinProviderFromDB {

	private final String scoreNameString;
	private final String scoreTypeString;
	private Map<String, Set<Psm>> result;
	private Set<String> projectTags;

	public PsmProviderFromPsmScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString, null);
				List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString, null);
				final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
				PersistenceUtils.addToPSMMapByPsmId(result, psmUnion);
			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString, projectTag);
					List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString,
							projectTag);
					final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
					PersistenceUtils.addToPSMMapByPsmId(result, psmUnion);
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap());
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;
	}

}
