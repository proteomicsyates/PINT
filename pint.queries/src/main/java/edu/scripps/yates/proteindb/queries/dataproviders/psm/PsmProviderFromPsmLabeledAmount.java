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
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

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
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new HashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (projectTags != null) {
				for (String projectTag : projectTags) {
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
