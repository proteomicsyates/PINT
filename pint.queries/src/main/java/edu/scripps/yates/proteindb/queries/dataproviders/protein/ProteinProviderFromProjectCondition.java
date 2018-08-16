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
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProjectCondition extends ProteinDataProvider {

	private final ConditionReferenceFromCommandValue condition;

	public ProteinProviderFromProjectCondition(ConditionReferenceFromCommandValue conditionReference) {
		condition = conditionReference;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (final ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (final String projectTag : projectTags) {
								final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
										.getProteinsByProjectCondition(projectTag, conditionProject.getConditionName());
								if (testMode && numProteins
										+ proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
									PersistenceUtils.addToMapByPrimaryAcc(result,
											QueriesUtil.getProteinSubList(proteinsByProjectCondition,
													QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
									return result;
								} else {
									PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByProjectCondition);
								}
								numProteins += proteinsByProjectCondition.size();
							}
						}
					} else {
						if (projectTags == null || projectTags.isEmpty()
								|| projectTags.contains(conditionProject.getProjectTag())) {
							final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
									.getProteinsByProjectCondition(conditionProject.getProjectTag(),
											conditionProject.getConditionName());
							if (testMode && numProteins
									+ proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
								PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
										proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
								return result;
							} else {
								PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByProjectCondition);
							}
							numProteins += proteinsByProjectCondition.size();

						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
							.getProteinsByProjectCondition(null, null);
					if (testMode
							&& numProteins + proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
								proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return result;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByProjectCondition);
					}
				} else {
					for (final String projectTag : projectTags) {
						final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
								.getProteinsByProjectCondition(projectTag, null);
						if (testMode && numProteins
								+ proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
									proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return result;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsByProjectCondition);
						}
						numProteins += proteinsByProjectCondition.size();
					}
				}
			}

		}
		return result;
	}

}
