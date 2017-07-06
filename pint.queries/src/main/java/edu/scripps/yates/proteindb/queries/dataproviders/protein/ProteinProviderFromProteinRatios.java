package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProteinRatios implements ProteinProviderFromDB {
	private final static Logger log = Logger.getLogger(ProteinProviderFromProteinRatios.class);
	private final ConditionReferenceFromCommandValue condition1;
	private final ConditionReferenceFromCommandValue condition2;
	private Map<String, Set<Protein>> result;
	private Set<String> projectNames;
	private final String ratioName;

	public ProteinProviderFromProteinRatios(ConditionReferenceFromCommandValue condition1,
			ConditionReferenceFromCommandValue condition2, String ratioName) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.ratioName = ratioName;

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			int numProteins = 0;
			result = new THashMap<String, Set<Protein>>();
			// condition1 and condition2 only can contain one ConditionProject
			if (condition1.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("First condition con only be referring to one condition");
			}
			if (condition2.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("Second condition con only be referring to one condition");
			}
			ConditionProject conditionProject1 = condition1.getConditionProjects().iterator().next();
			ConditionProject conditionProject2 = condition2.getConditionProjects().iterator().next();
			if (conditionProject1.getProjectTag() != null && conditionProject2.getProjectTag() != null
					&& !conditionProject1.getProjectTag().equals(conditionProject2.getProjectTag())) {
				throw new IllegalArgumentException(
						"Condition 1 and condition 2 must be referring to conditions of the same project");
			}
			if (projectNames != null && !projectNames.isEmpty()) {
				for (String projectName : projectNames) {
					String projectTagFromQuery = conditionProject2.getProjectTag();
					final Map<String, Set<Protein>> proteinsWithRatios = PreparedQueries.getProteinsWithRatios(
							conditionProject1.getConditionName(), conditionProject2.getConditionName(), projectName,
							ratioName);
					if (projectTagFromQuery == null) {
						if (testMode && numProteins + proteinsWithRatios.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
									proteinsWithRatios, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return result;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatios);

						}
					} else {
						if (projectName.equals(projectTagFromQuery)) {
							if (testMode
									&& numProteins + proteinsWithRatios.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
								PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
										proteinsWithRatios, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
								return result;
							} else {
								PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatios);

							}
						}
					}
					numProteins += proteinsWithRatios.size();
				}
			} else {

				final Map<String, Set<Protein>> proteinsWithRatios = PreparedQueries.getProteinsWithRatios(
						conditionProject1.getConditionName(), conditionProject2.getConditionName(),
						conditionProject2.getProjectTag(), ratioName);
				if (testMode && numProteins + proteinsWithRatios.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(proteinsWithRatios,
							QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatios);
				}
				numProteins += proteinsWithRatios.size();
			}
		}
		return result;

	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectNames) {
		this.projectNames = projectNames;
		result = null;

	}

}
