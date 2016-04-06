package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
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

public class ProteinProviderFromProteinAmounts implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private final String amountTypeString;
	private Map<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromProteinAmounts(ConditionReferenceFromCommandValue condition, AmountType amountType) {
		this.condition = condition;
		if (amountType != null) {
			amountTypeString = amountType.name();
		} else {
			amountTypeString = null;
		}

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			for (ConditionProject conditionProject : conditionProjects) {

				if (projectTags == null || projectTags.isEmpty() || conditionProject.getProjectTag() == null
						|| (projectTags.contains(conditionProject.getProjectTag()))) {
					final Map<String, Set<Protein>> proteinsWithAmount = PreparedQueries.getProteinsWithAmount(
							conditionProject.getProjectTag(), conditionProject.getConditionName(), amountTypeString);
					PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithAmount);
				}
			}
		}
		return proteins;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		proteins = null;

	}
}
