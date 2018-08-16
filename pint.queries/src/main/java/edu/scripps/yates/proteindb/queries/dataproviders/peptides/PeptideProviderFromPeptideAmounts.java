package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.model.enums.AmountType;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromPeptideAmounts extends PeptideDataProvider {

	private final ConditionReferenceFromCommandValue condition;
	private final String amountTypeString;

	public PeptideProviderFromPeptideAmounts(ConditionReferenceFromCommandValue condition, AmountType amountType) {
		this.condition = condition;
		if (amountType != null) {
			amountTypeString = amountType.name();
		} else {
			amountTypeString = null;
		}
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			int numPSMs = 0;
			for (final ConditionProject conditionProject : conditionProjects) {
				if (projectTags == null || projectTags.isEmpty() || conditionProject.getProjectTag() == null
						|| projectTags.contains(conditionProject.getProjectTag())) {
					final List<Peptide> peptideList = PreparedQueries.getPeptidesWithPeptideAmount(
							conditionProject.getProjectTag(), conditionProject.getConditionName(), amountTypeString);
					if (testMode && numPSMs + peptideList.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptideList.subList(0,
								Math.min(peptideList.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPSMs)));
						return result;
					} else {
						result.addAll(peptideList);
					}
					numPSMs += peptideList.size();
				}
			}
		}
		return result;
	}

}
