package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PsmDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromPsmScores extends PsmDataProvider {

	private final String scoreNameString;
	private final String scoreTypeString;

	public PsmProviderFromPsmScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;
	}

	@Override
	public Map<String, Collection<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			int numPSMs = 0;
			result = new THashMap<String, Collection<Psm>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString, null);
				final List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString, null);
				final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
				final List<Psm> psmList = new ArrayList<Psm>();
				psmList.addAll(psmUnion);
				if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psmUnion);
				}
				numPSMs += psmList.size();

			} else {
				for (final String projectTag : projectTags) {
					final List<Psm> psms1 = PreparedQueries.getPsmsWithScores(scoreNameString, scoreTypeString,
							projectTag);
					final List<Psm> psms2 = PreparedQueries.getPsmsWithPTMScores(scoreNameString, scoreTypeString,
							projectTag);
					final Collection<Psm> psmUnion = PersistenceUtils.psmUnion(psms1, psms2);
					final List<Psm> psmList = new ArrayList<Psm>();
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

}
