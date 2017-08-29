package edu.scripps.yates.proteindb.queries.dataproviders.psm;

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
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromProjectCondition implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private Map<String, Set<Psm>> psms;
	private Set<String> projectTags;

	public PsmProviderFromProjectCondition(ConditionReferenceFromCommandValue condition) {
		this.condition = condition;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (psms == null) {
			psms = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (String projectTag : projectTags) {
								final List<Psm> psMsByProjectCondition = PreparedQueries
										.getPSMsByProjectCondition(projectTag, conditionProject.getConditionName());

								if (testMode
										&& numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
									PersistenceUtils.addToPSMMapByPsmId(psms,
											psMsByProjectCondition.subList(0, Math.min(psMsByProjectCondition.size(),
													QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
									return psms;
								} else {
									PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition);
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
								PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition.subList(0, Math
										.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
								return psms;
							} else {
								PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition);
							}
							numPSMs += psMsByProjectCondition.size();
						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					final List<Psm> psMsByProjectCondition = PreparedQueries.getPSMsByProjectCondition(null, null);
					if (testMode && numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
						PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition.subList(0,
								Math.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
					} else {
						PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition);
					}
					numPSMs += psMsByProjectCondition.size();

				} else {
					for (String projectTag : projectTags) {
						final List<Psm> psMsByProjectCondition = PreparedQueries.getPSMsByProjectCondition(projectTag,
								null);
						if (testMode && numPSMs + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
							PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition.subList(0,
									Math.min(psMsByProjectCondition.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
							return psms;
						} else {
							PersistenceUtils.addToPSMMapByPsmId(psms, psMsByProjectCondition);
						}
						numPSMs += psMsByProjectCondition.size();

					}
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
		projectTags = projectNames;
		psms = null;
	}
}
