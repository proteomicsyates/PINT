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
	public Map<String, Set<Psm>> getPsmMap() {
		if (psms == null) {
			psms = new HashMap<String, Set<Psm>>();
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			for (ConditionProject conditionProject : conditionProjects) {
				if (projectNames == null || projectNames.isEmpty() || conditionProject.getProjectTag() == null
						|| projectNames.contains(conditionProject.getProjectTag())) {
					List<Psm> psmList = PreparedQueries.getPSMsWithPSMAmount(conditionProject.getProjectTag(),
							conditionProject.getConditionName(), amountTypeString);
					PersistenceUtils.addToPSMMapByPsmId(psms, psmList);
				}
			}
		}
		return psms;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		psms = null;
	}
}
