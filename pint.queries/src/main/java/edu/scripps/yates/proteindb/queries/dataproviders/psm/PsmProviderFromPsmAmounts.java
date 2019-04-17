package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PsmDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromPsmAmounts extends PsmDataProvider {

	private final ConditionReferenceFromCommandValue condition;
	private final String amountTypeString;

	public PsmProviderFromPsmAmounts(ConditionReferenceFromCommandValue condition, AmountType amountType) {
		this.condition = condition;
		if (amountType != null) {
			amountTypeString = amountType.name();
		} else {
			amountTypeString = null;
		}
	}

	@Override
	public Map<String, Collection<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Collection<Psm>>();
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			int numPSMs = 0;
			for (final ConditionProject conditionProject : conditionProjects) {
				if (projectTags == null || projectTags.isEmpty() || conditionProject.getProjectTag() == null
						|| projectTags.contains(conditionProject.getProjectTag())) {
					final List<Psm> psmList = PreparedQueries.getPSMsWithPSMAmount(conditionProject.getProjectTag(),
							conditionProject.getConditionName(), amountTypeString);
					if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result,
								psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return result;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psmList);
					}
					numPSMs += psmList.size();
				}
			}
		}
		return result;
	}

}
