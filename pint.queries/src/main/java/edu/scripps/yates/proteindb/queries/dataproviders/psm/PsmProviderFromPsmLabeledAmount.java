package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PsmDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromPsmLabeledAmount extends PsmDataProvider {
	private final String labelName;
	private final Boolean singleton;

	public PsmProviderFromPsmLabeledAmount(String labelString, Boolean singleton) {
		labelName = labelString;
		this.singleton = singleton;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					final List<Psm> psMsWithLabeledAmount = PreparedQueries.getPSMsWithLabeledAmount(projectTag,
							labelName, singleton);
					if (testMode && numPSMs + psMsWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result, psMsWithLabeledAmount.subList(0,
								Math.min(psMsWithLabeledAmount.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psMsWithLabeledAmount);
					}
					numPSMs += psMsWithLabeledAmount.size();
				}
			} else {
				final List<Psm> psMsWithLabeledAmount = PreparedQueries.getPSMsWithLabeledAmount(null, labelName,
						singleton);
				if (testMode && numPSMs + psMsWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result, psMsWithLabeledAmount.subList(0,
							Math.min(psMsWithLabeledAmount.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
					return result;
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psMsWithLabeledAmount);
				}
				numPSMs += psMsWithLabeledAmount.size();
			}
		}
		return result;
	}

}
