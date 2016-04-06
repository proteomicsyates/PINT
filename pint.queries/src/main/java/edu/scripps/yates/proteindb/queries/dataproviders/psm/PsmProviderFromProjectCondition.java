package edu.scripps.yates.proteindb.queries.dataproviders.psm;

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

public class PsmProviderFromProjectCondition implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition;
	private Map<String, Set<Psm>> psms;
	private Set<String> projectTags;

	public PsmProviderFromProjectCondition(ConditionReferenceFromCommandValue condition) {
		this.condition = condition;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		if (psms == null) {
			psms = new HashMap<String, Set<Psm>>();
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (String projectTag : projectTags) {
								PersistenceUtils.addToPSMMapByPsmId(psms, PreparedQueries.getPSMsByProjectCondition(projectTag,
										conditionProject.getConditionName()));
							}
						}
					} else {
						if (projectTags == null || projectTags.isEmpty()
								|| projectTags.contains(conditionProject.getProjectTag())) {
							PersistenceUtils.addToPSMMapByPsmId(psms, PreparedQueries.getPSMsByProjectCondition(
									conditionProject.getProjectTag(), conditionProject.getConditionName()));
						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					PersistenceUtils.addToPSMMapByPsmId(psms, PreparedQueries.getPSMsByProjectCondition(null, null));
				} else {
					for (String projectTag : projectTags) {
						PersistenceUtils.addToPSMMapByPsmId(psms, PreparedQueries.getPSMsByProjectCondition(projectTag, null));
					}
				}
			}
		}
		return psms;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap());
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		projectTags = projectNames;
		psms = null;
	}
}
