package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromScoreCommand;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.map.hash.THashMap;

public class ProteinProviderFromProteinRatiosAndScore extends ProteinDataProvider {
	private final static Logger log = Logger.getLogger(ProteinProviderFromProteinRatiosAndScore.class);
	private final ConditionReferenceFromCommandValue condition1;
	private final ConditionReferenceFromCommandValue condition2;
	private final String ratioName;
	private final QueryFromScoreCommand scoreThresholdQuery;
	private final NumericalCondition numericalCondition;

	public ProteinProviderFromProteinRatiosAndScore(ConditionReferenceFromCommandValue condition1,
			ConditionReferenceFromCommandValue condition2, String ratioName, NumericalCondition numericalCondition,
			QueryFromScoreCommand scoreThresholdQuery) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.ratioName = ratioName;
		this.scoreThresholdQuery = scoreThresholdQuery;
		this.numericalCondition = numericalCondition;
	}

	@Override
	public Map<String, Collection<Protein>> getProteinMap(boolean testMode) {
		if (result == null) {
			final int numProteins = 0;
			result = new THashMap<String, Collection<Protein>>();
			// condition1 and condition2 only can contain one ConditionProject
			if (condition1.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("First condition con only be referring to one condition");
			}
			if (condition2.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("Second condition con only be referring to one condition");
			}
			final ConditionProject conditionProject1 = condition1.getConditionProjects().iterator().next();
			final ConditionProject conditionProject2 = condition2.getConditionProjects().iterator().next();
			if (conditionProject1.getProjectTag() != null && conditionProject2.getProjectTag() != null
					&& !conditionProject1.getProjectTag().equals(conditionProject2.getProjectTag())) {
				throw new IllegalArgumentException(
						"Condition 1 and condition 2 must be referring to conditions of the same project");
			}
			String ratioOperator = null;
			Double ratioValue = null;
			if (numericalCondition != null) {
				ratioOperator = numericalCondition.getOperator().getText();
				ratioValue = numericalCondition.getValue();
			}
			String scoreName = null;
			String scoreType = null;
			String scoreOperator = null;
			Double scoreValue = null;
			if (scoreThresholdQuery != null) {
				scoreName = scoreThresholdQuery.getScoreNameString();
				scoreType = scoreThresholdQuery.getScoreTypeString();
				if (scoreThresholdQuery.getNumericalCondition() != null) {
					scoreOperator = scoreThresholdQuery.getNumericalCondition().getOperator().getText();
					scoreValue = scoreThresholdQuery.getNumericalCondition().getValue();
				}
			}
			if (projectTags != null && !projectTags.isEmpty()) {
				for (final String projectTag : projectTags) {
					final String projectTagFromQuery = conditionProject2.getProjectTag();
					final Map<String, Collection<Protein>> proteinsWithRatiosAndScores = PreparedQueries
							.getProteinsWithRatiosAndScores(conditionProject1.getConditionName(),
									conditionProject2.getConditionName(), projectTag, ratioName, ratioOperator,
									ratioValue, scoreName, scoreType, scoreOperator, scoreValue);
					if (projectTagFromQuery == null) {
						if (testMode && numProteins
								+ proteinsWithRatiosAndScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
							PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
									proteinsWithRatiosAndScores, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
							return result;
						} else {
							PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatiosAndScores);
						}
					} else {
						if (projectTag.equals(projectTagFromQuery)) {
							if (testMode && numProteins
									+ proteinsWithRatiosAndScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
								PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
										proteinsWithRatiosAndScores, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
								return result;
							} else {
								PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatiosAndScores);
							}
						}
					}
				}
			} else {
				final Map<String, Collection<Protein>> proteinsWithRatiosAndScores = PreparedQueries
						.getProteinsWithRatiosAndScores(conditionProject1.getConditionName(),
								conditionProject2.getConditionName(), null, ratioName, ratioOperator, ratioValue,
								scoreName, scoreType, scoreOperator, scoreValue);
				if (testMode && numProteins + proteinsWithRatiosAndScores.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(result, QueriesUtil.getProteinSubList(
							proteinsWithRatiosAndScores, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return result;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(result, proteinsWithRatiosAndScores);
				}

			}
		}
		return result;

	}

}
