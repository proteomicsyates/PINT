package edu.scripps.yates.proteindb.queries.dataproviders.psm;

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
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromProjectCondition extends PsmDataProvider {

	private final ConditionReferenceFromCommandValue condition;

	public PsmProviderFromProjectCondition(ConditionReferenceFromCommandValue condition) {
		this.condition = condition;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (final ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (final String projectTag : projectTags) {
								final List<Psm> psMsByProjectCondition = PreparedQueries
										.getPSMsByProjectCondition(projectTag, conditionProject.getConditionName());

								if (testMode
										&& numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
									PersistenceUtils.addToPSMMapByPsmId(result,
											psMsByProjectCondition.subList(0, Math.min(psMsByProjectCondition.size(),
													QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
									return result;
								} else {
									PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition);
								}
								numPSMs += psMsByProjectCondition.size();
							}
						}
					} else {
						if (projectTags == null || projectTags.isEmpty()
								|| projectTags.contains(conditionProject.getProjectTag())) {
							final List<Psm> psMsByProjectCondition = PreparedQueries.getPSMsByProjectCondition(
									conditionProject.getProjectTag(), conditionProject.getConditionName());
							if (testMode && numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
								PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition.subList(0, Math
										.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
								return result;
							} else {
								PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition);
							}
							numPSMs += psMsByProjectCondition.size();
						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					final List<Psm> psMsByProjectCondition = PreparedQueries.getPSMsByProjectCondition(null, null);
					if (testMode && numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition.subList(0,
								Math.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
					} else {
						PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition);
					}
					numPSMs += psMsByProjectCondition.size();

				} else {
					for (final String projectTag : projectTags) {
						final List<Psm> psMsByProjectCondition = PreparedQueries.getPSMsByProjectCondition(projectTag,
								null);
						if (testMode && numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
							PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition.subList(0,
									Math.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
							return result;
						} else {
							PersistenceUtils.addToPSMMapByPsmId(result, psMsByProjectCondition);
						}
						numPSMs += psMsByProjectCondition.size();

					}
				}
			}
		}
		return result;
	}

}
