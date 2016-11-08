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
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.model.enums.AmountType;

public class PsmProviderFromPsmAmounts implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private final String amountTypeString;
	private Map<String, Set<Psm>> psms;
	private Set<String> projectNames;

	public PsmProviderFromPsmAmounts(ConditionReferenceFromCommandValue condition, AmountType amountType) {
		this.condition = condition;
		if (amountType != null) {
			amountTypeString = amountType.name();
		} else {
			amountTypeString = null;
		}
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (psms == null) {
			psms = new HashMap<String, Set<Psm>>();
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			int numPSMs = 0;
			for (ConditionProject conditionProject : conditionProjects) {
				if (projectNames == null || projectNames.isEmpty() || conditionProject.getProjectTag() == null
						|| projectNames.contains(conditionProject.getProjectTag())) {
					List<Psm> psmList = PreparedQueries.getPSMsWithPSMAmount(conditionProject.getProjectTag(),
							conditionProject.getConditionName(), amountTypeString);
					if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(psms,
								psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
						return psms;
					} else {
						PersistenceUtils.addToPSMMapByPsmId(psms, psmList);
					}
					numPSMs += psmList.size();
				}
			}
		}
		return psms;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		psms = null;
	}
}
