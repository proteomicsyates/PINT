package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.NumericalconditionOperator;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinRatiosAndScore;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromPsmRatios;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a {@link Query} from RATIO command:<br>
 * RATIO[Aggregation_level, Condition1, Condition2, Ratio_naem, Numerical
 * condition, Score command]
 *
 * @author Salva
 *
 */
public class QueryFromRatioCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromRatioCommand.class);

	private ConditionReferenceFromCommandValue condition1;
	private ConditionReferenceFromCommandValue condition2;
	private NumericalCondition numericalCondition;
	private String ratioName;
	private QueryFromScoreCommand scoreThresholdQuery;

	private AggregationLevel aggregationLevel;

	private ConditionProject conditionProject1;

	private ConditionProject conditionProject2;

	public QueryFromRatioCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 6) {
			String aggregationLevelString = split[0].trim();
			if (!"".equals(aggregationLevelString)) {

				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null)
					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on RA command");
			}

			final String condition1Command = split[1].trim();
			condition1 = new ConditionReferenceFromCommandValue(
					new CommandReference(condition1Command).getCommandValue());
			final String condition2Command = split[2].trim();
			condition2 = new ConditionReferenceFromCommandValue(
					new CommandReference(condition2Command).getCommandValue());

			if (condition1 == null)
				throw new MalformedQueryException(
						"First condition is not recognized on RATIO command '" + commandReference + "'");
			if (condition2 == null)
				throw new MalformedQueryException(
						"Second condition is not recognized on RATIO command '" + commandReference + "'");
			if (condition1.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("First condition con only be referring to one condition");
			}
			if (condition2.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("Second condition con only be referring to one condition");
			}
			conditionProject1 = condition1.getConditionProjects().iterator().next();
			conditionProject2 = condition2.getConditionProjects().iterator().next();
			// if one project is stated, the other also has to be stated

			if (conditionProject1.getProjectTag() != null) {
				if (conditionProject2.getProjectTag() == null)
					throw new MalformedQueryException("Project on '" + conditionProject2.getConditionName()
							+ "' should be '" + conditionProject1.getProjectTag() + "' on RATIO command '"
							+ commandReference + "'");
			} else if (conditionProject2.getProjectTag() != null) {
				if (conditionProject1.getProjectTag() == null)
					throw new MalformedQueryException("Project on '" + conditionProject1.getConditionName()
							+ "' should be '" + conditionProject2.getProjectTag() + "' on RATIO command '"
							+ commandReference + "'");
			} else if (conditionProject1.getProjectTag() != null && conditionProject2.getProjectTag() != null
					&& !conditionProject1.getProjectTag().equals(conditionProject2.getProjectTag()))
				throw new MalformedQueryException("Conditions '" + conditionProject1.getConditionName() + "' and '"
						+ conditionProject2.getConditionName()
						+ "' are not present in the same project on RATIO command '" + commandReference + "'");

			final String ratioNameString = split[3].trim();
			if (!"".equals(ratioNameString))
				ratioName = ratioNameString;

			final String numericalConditionString = split[4].trim();

			String scoreThresholdString = split[5].trim();
			if (!"".equals(scoreThresholdString)) {
				if (scoreThresholdString.startsWith("!")) {
					scoreThresholdString = scoreThresholdString.substring(1);
				}
				// check if the score command is valid. if not, an exception
				// will be thrown
				scoreThresholdQuery = new QueryFromScoreCommand(new CommandReference(scoreThresholdString));
			}
			if (!"".equals(numericalConditionString)) {
				numericalCondition = new NumericalCondition(numericalConditionString);
			} else {
				if (scoreThresholdQuery == null) {
					throw new MalformedQueryException("Numerical condition cannot be empty on command '"
							+ commandReference.getCommand().name() + "' if score threshold is not provided.");
				}
			}
			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	private boolean queryOverPsm(Psm psm) {

		final Set<PsmRatioValue> psmRatios = PersistenceUtils.getPsmRatiosBetweenTwoConditions(psm,
				conditionProject1.getConditionName(), conditionProject2.getConditionName(), null);
		boolean valid = false;
		boolean specifiedRatioPresent = false;

		if (psmRatios != null && !psmRatios.isEmpty()) {
			for (PsmRatioValue psmRatio : psmRatios) {

				valid = true;
				double ratioValue = getAppropiateValue(psmRatio, conditionProject1.getConditionName(),
						conditionProject2.getConditionName());
				if (ratioName != null) {
					final String description = psmRatio.getRatioDescriptor().getDescription();
					if (!ratioName.equalsIgnoreCase(description)) {
						continue;
					} else {
						specifiedRatioPresent = true;
						// =Nan
						if (numericalCondition != null && numericalCondition.isNanValue()
								&& NumericalconditionOperator.EQUAL.equals(numericalCondition.getOperator())) {
							continue;
						}
					}
				}

				if (valid && (numericalCondition == null || (numericalCondition != null
						&& !numericalCondition.isNanValue() && !numericalCondition.matches(ratioValue)))) {
					continue;
				}

				if (valid && scoreThresholdQuery != null) {
					if (psmRatio.getConfidenceScoreValue() != null) {
						String scoreType = null;
						String scoreValue = null;
						if (psmRatio.getConfidenceScoreType() != null) {
							scoreType = psmRatio.getConfidenceScoreType().getName();
						}
						if (psmRatio.getConfidenceScoreValue() != null) {
							scoreValue = psmRatio.getConfidenceScoreValue().toString();
						}
						valid = scoreThresholdQuery.evaluateScore(psmRatio.getConfidenceScoreName(), scoreType,
								scoreValue);
					} else {
						continue;
					}
				}

				if (valid) {
					return true;
				}
			}
		}
		// if we dont have found the specified ratio type and the
		// numerical condition is =Nan, add to results
		if (!specifiedRatioPresent
				&& (numericalCondition == null || (numericalCondition != null && numericalCondition.isNanValue()
						&& NumericalconditionOperator.EQUAL == numericalCondition.getOperator()))) {
			return true;
		}
		if (!specifiedRatioPresent && NumericalconditionOperator.NOT_EQUAL == numericalCondition.getOperator()
				&& !numericalCondition.isNanValue()) {
			return true;
		}

		return false;

	}

	private boolean queryOverProtein(QueriableProteinSet queriableProtein) {
		// protein ratios
		boolean specifiedRatioPresent = false;
		if (isTheProteinToLog(queriableProtein)) {
			log.info("*****" + this);
		}
		final Set<ProteinRatioValue> proteinRatios = queriableProtein.getProteinRatiosBetweenTwoConditions(
				conditionProject1.getConditionName(), conditionProject2.getConditionName(), ratioName);

		if (proteinRatios != null && !proteinRatios.isEmpty()) {
			if (isTheProteinToLog(queriableProtein)) {
				log.info("*****" + this + proteinRatios.size()
						+ " protein ratios between these 2 conditions in the protein");
			}
			for (ProteinRatioValue proteinRatio : proteinRatios) {
				if (ratioName != null) {
					if (!ratioName.equals(proteinRatio.getRatioDescriptor().getDescription())) {
						continue;
					} else {
						specifiedRatioPresent = true;
						// =Nan
						if (numericalCondition != null && numericalCondition.isNanValue()
								&& NumericalconditionOperator.EQUAL.equals(numericalCondition.getOperator())) {
							continue;
						}
					}
				}
				double ratioValue = getAppropiateValue(proteinRatio, conditionProject1.getConditionName(),
						conditionProject2.getConditionName());

				if ((numericalCondition == null && scoreThresholdQuery == null) || (numericalCondition != null
						&& !numericalCondition.isNanValue() && !numericalCondition.matches(ratioValue))) {
					continue;
				}

				if (scoreThresholdQuery != null) {
					if (proteinRatio.getConfidenceScoreValue() != null) {
						final ConfidenceScoreType confidenceScoreType = proteinRatio.getConfidenceScoreType();
						String scoreType = null;
						String scoreValue = null;
						if (confidenceScoreType != null) {
							scoreType = confidenceScoreType.getName();
						}
						if (proteinRatio.getConfidenceScoreValue() != null) {
							scoreValue = proteinRatio.getConfidenceScoreValue().toString();
						}
						if (isTheProteinToLog(queriableProtein)) {
							log.info("*****" + this + " Protein ratio confidentScore value is not null: confscorename: "
									+ proteinRatio.getConfidenceScoreName() + " confscorevalue: " + scoreValue
									+ " confscoretype: " + scoreType);
						}
						boolean valid = scoreThresholdQuery.evaluateScore(proteinRatio.getConfidenceScoreName(),
								scoreType, scoreValue);
						if (isTheProteinToLog(queriableProtein)) {
							log.info("***** result=" + valid);
						}
						if (valid) {
							return true;
						} else {
							continue;
						}
					} else {
						continue;
					}
				}

				return true;

			}
		}

		// if we dont have found the specified ratio type and the
		// numerical condition is =Nan, add to results
		if (!specifiedRatioPresent && numericalCondition != null && numericalCondition.isNanValue()
				&& NumericalconditionOperator.EQUAL == numericalCondition.getOperator()) {
			return true;
		}
		if (!specifiedRatioPresent && numericalCondition != null
				&& NumericalconditionOperator.NOT_EQUAL == numericalCondition.getOperator()
				&& !numericalCondition.isNanValue()) {
			return true;
		}

		return false;
	}

	private boolean isTheProteinToLog(QueriableProteinSet protein) {
		if (protein.getPrimaryAccession().equals("Q99442")) {
			return true;
		}
		return false;
	}

	private double getAppropiateValue(ProteinRatioValue proteinRatio, String condition1Name, String condition2Name) {
		final RatioDescriptor ratioDescriptor = proteinRatio.getRatioDescriptor();
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name))
			return proteinRatio.getValue();
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name))
			return -proteinRatio.getValue();
		throw new IllegalArgumentException("ERROR");
	}

	private double getAppropiateValue(PsmRatioValue proteinRatio, String condition1Name, String condition2Name) {
		final RatioDescriptor ratioDescriptor = proteinRatio.getRatioDescriptor();
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name))
			return proteinRatio.getValue();
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name))
			return -proteinRatio.getValue();
		throw new IllegalArgumentException("ERROR");
	}

	private double getAppropiateValue(PeptideRatioValue proteinRatio, String condition1Name, String condition2Name) {
		final RatioDescriptor ratioDescriptor = proteinRatio.getRatioDescriptor();
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name)) {
			return proteinRatio.getValue();
		}
		if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)
				&& ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name)) {
			return -proteinRatio.getValue();
		}
		throw new IllegalArgumentException("ERROR");
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		switch (aggregationLevel) {
		case PROTEIN:

			final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());
			return queryOverProtein;

		case PSM:

			final boolean queryOverPsm = queryOverPsm(link.getQueriablePsm().getPsm());
			return queryOverPsm;

		default:
			throw new IllegalArgumentException("Aggregation level not supported");
		}

	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {
		ProteinProviderFromDB ret = null;
		switch (aggregationLevel) {
		case PROTEIN:

			log.info("Setting protein provider for getting proteins with ratios and scores");
			ret = new ProteinProviderFromProteinRatiosAndScore(condition1, condition2, ratioName, numericalCondition,
					scoreThresholdQuery);

			break;
		case PSM:
			ret = new PsmProviderFromPsmRatios(condition1, condition2, ratioName);
			break;
		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}

		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (aggregationLevel != AggregationLevel.PROTEIN)
			return true;
		// TODO to change
		// if (numericalCondition == null && scoreThresholdQuery == null) {
		return false;
		// }
		// return true;
	}

}
