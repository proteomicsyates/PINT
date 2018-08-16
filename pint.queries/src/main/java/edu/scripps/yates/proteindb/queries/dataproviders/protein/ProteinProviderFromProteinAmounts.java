package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import edu.scripps.yates.utilities.model.enums.AmountType;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProteinAmounts extends ProteinDataProvider {

	private final ConditionReferenceFromCommandValue condition;
	private final String amountTypeString;

	public ProteinProviderFromProteinAmounts(ConditionReferenceFromCommandValue condition, AmountType amountType) {
		this.condition = condition;
		if (amountType != null) {
			amountTypeString = amountType.name();
		} else {
			amountTypeString = null;
		}

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
			for (final ConditionProject conditionProject : conditionProjects) {

				if (projectTags == null || projectTags.isEmpty() || conditionProject.getProjectTag() == null
						|| (projectTags.contains(conditionProject.getProjectTag()))) {
					final Map<String, Set<Protein>> proteinsWithAmount = PreparedQueries.getProteinsWithAmount(
							conditionProject.getProjectTag(), conditionProject.getConditionName(), amountTypeString);
					if (testMode && numProteins + proteinsWithAmount.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(proteinsWithAmount,
								QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithAmount);
					}
					numProteins += proteinsWithAmount.size();
				}
			}
		}
		return result;
	}

}
