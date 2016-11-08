package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.ArrayList;
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
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

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
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			int numPSMs = 0;
			result = new HashMap<String, Set<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString, null);
				List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString, null);
				final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
				List<Psm> psmList = new ArrayList<Psm>();
				psmList.addAll(psmUnion);
				if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psmUnion);
				}
				numPSMs += psmList.size();

			} else {
				for (String projectTag : projectTags) {
					List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString, projectTag);
					List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString,
							projectTag);
					final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
					List<Psm> psmList = new ArrayList<Psm>();
					psmList.addAll(psmUnion);
					if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psmUnion);
					}
					numPSMs += psmList.size();
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		result = null;
	}

}
