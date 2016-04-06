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

public class ProteinProviderFromProjectCondition implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private Map<String, Set<Protein>> results;
	private Set<String> projectTags;

	public ProteinProviderFromProjectCondition(ConditionReferenceFromCommandValue conditionReference) {
		condition = conditionReference;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (results == null) {
			results = new HashMap<String, Set<Protein>>();
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (String projectTag : projectTags) {
								PersistenceUtils.addToMapByPrimaryAcc(results, PreparedQueries.getProteinsByProjectCondition(
										projectTag, conditionProject.getConditionName()));
							}
						}
					} else {
						if (projectTags == null || projectTags.isEmpty()
								|| projectTags.contains(conditionProject.getProjectTag())) {
							PersistenceUtils.addToMapByPrimaryAcc(results, PreparedQueries.getProteinsByProjectCondition(
									conditionProject.getProjectTag(), conditionProject.getConditionName()));
						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					PersistenceUtils.addToMapByPrimaryAcc(results, PreparedQueries.getProteinsByProjectCondition(null, null));
				} else {
					for (String projectTag : projectTags) {
						PersistenceUtils.addToMapByPrimaryAcc(results,
								PreparedQueries.getProteinsByProjectCondition(projectTag, null));
					}
				}
			}

		}
		return results;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		results = null;

	}

}
