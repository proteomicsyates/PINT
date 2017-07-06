package edu.scripps.yates.proteindb.queries.dataproviders.psm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class PsmProviderFromPsmRatios implements ProteinProviderFromDB {

	private final ConditionReferenceFromCommandValue condition1;
	private final ConditionReferenceFromCommandValue condition2;
	private Map<String, Set<Psm>> result;
	private Set<String> projectNames;
	private final String ratioName;

	public PsmProviderFromPsmRatios(ConditionReferenceFromCommandValue condition1,
			ConditionReferenceFromCommandValue condition2, String ratioName) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.ratioName = ratioName;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		if (result == null) {
			result = new THashMap<String, Set<Psm>>();
			int numPSMs = 0;
			// condition1 and condition2 only can contain one ConditionProject
			if (condition1.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("First condition con only be referring to one condition");
			}
			if (condition2.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("Second condition con only be referring to one condition");
			}
			ConditionProject conditionProject1 = condition1.getConditionProjects().iterator().next();
			ConditionProject conditionProject2 = condition2.getConditionProjects().iterator().next();
			if (projectNames == null || projectNames.isEmpty()
					|| projectNames.contains(conditionProject2.getProjectTag())
					|| conditionProject2.getProjectTag() == null) {
				final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
						.getRatioDescriptorsByProject(conditionProject1.getProjectTag());
				String actualRatioName = null;
				// first look if the ratioName exists as it is provided (case
				// sentitive)
				for (RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
					if (ratioDescriptor.getDescription().equals(ratioName)) {
						actualRatioName = ratioName;
					}
				}
				if (actualRatioName == null) {
					// try to look for the ratioName case insensitive and use it
					for (RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
						if (ratioDescriptor.getDescription().equalsIgnoreCase(ratioName)) {
							actualRatioName = ratioDescriptor.getDescription();
						}
					}
				}
				if (actualRatioName == null)
					actualRatioName = ratioName;
				final Collection<Psm> psmsWithRatios = PreparedQueries.getPSMWithRatios(
						conditionProject1.getConditionName(), conditionProject2.getConditionName(),
						conditionProject1.getProjectTag(), actualRatioName);
				List<Psm> psmList = new ArrayList<Psm>();
				psmList.addAll(psmsWithRatios);
				if (testMode && numPSMs + psmList.size() > QueriesUtil.TEST_MODE_NUM_PSMS) {
					PersistenceUtils.addToPSMMapByPsmId(result,
							psmList.subList(0, Math.min(psmList.size(), QueriesUtil.TEST_MODE_NUM_PSMS - numPSMs)));
				} else {
					PersistenceUtils.addToPSMMapByPsmId(result, psmList);
				}
				numPSMs += psmList.size();
			}
		}
		return result;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		result = null;
	}

}
