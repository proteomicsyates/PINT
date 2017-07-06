package edu.scripps.yates.proteindb.queries.dataproviders.protein;

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
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProjectCondition implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private Map<String, Set<Protein>> results;
	private Set<String> projectTags;

	public ProteinProviderFromProjectCondition(ConditionReferenceFromCommandValue conditionReference) {
		condition = conditionReference;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (results == null) {
			results = new THashMap<String, Set<Protein>>();
			int numProteins = 0;
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (String projectTag : projectTags) {
								final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
										.getProteinsByProjectCondition(projectTag, conditionProject.getConditionName());
								if (testMode && numProteins
										+ proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
									PersistenceUtils.addToMapByPrimaryAcc(results,
											QueriesUtil.getProteinSubList(proteinsByProjectCondition,
													QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
									return results;
								} else {
									PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByProjectCondition);
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
								PersistenceUtils.addToMapByPrimaryAcc(results, QueriesUtil.getProteinSubList(
										proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
								return results;
							} else {
								PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByProjectCondition);
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
						PersistenceUtils.addToMapByPrimaryAcc(results, QueriesUtil.getProteinSubList(
								proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return results;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByProjectCondition);
					}
				} else {
					for (String projectTag : projectTags) {
						final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
								.getProteinsByProjectCondition(projectTag, null);
						if (testMode && numProteins
								+ proteinsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(results, QueriesUtil.getProteinSubList(
									proteinsByProjectCondition, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return results;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(results, proteinsByProjectCondition);
						}
						numProteins += proteinsByProjectCondition.size();
					}
				}
			}

		}
		return results;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		results = null;

	}

}
