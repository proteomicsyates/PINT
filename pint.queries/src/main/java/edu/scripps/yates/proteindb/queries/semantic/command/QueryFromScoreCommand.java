package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.NumericalconditionOperator;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinScores;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromPsmScores;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinInterface;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a query from SCORE command: <br>
 * SC[Score type, score name, Numerical condition]
 *
 * @author Salva
 *
 */
public class QueryFromScoreCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromScoreCommand.class);

	private String scoreNameString;
	private NumericalCondition numericalCondition;

	private String scoreTypeString;
	private AggregationLevel aggregationLevel;

	public QueryFromScoreCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 4) {
			String aggregationLevelString = split[0].trim();
			if (!"".equals(aggregationLevelString)) {

				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null)
					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on SC command");
			}

			scoreTypeString = split[1].trim();
			if (!"".equals(scoreTypeString)) {
				final ControlVocabularyTerm cvScore = CVManager.getCvByName(scoreTypeString);
				if (cvScore == null)
					throw new MalformedQueryException(
							"'" + scoreTypeString + "' is not recognized as a valid score type.");
			}

			scoreNameString = split[2].trim();

			// or score name or score type MUST be not null
			if ("".equals(scoreNameString) && "".equals(scoreTypeString))
				throw new MalformedQueryException(
						"Score type or score name should be not empty at SC command '" + commandReference + "'");

			String numericalConditionString = split[3].trim();
			if (!"".equals(numericalConditionString)) {
				numericalCondition = new NumericalCondition(numericalConditionString);
			}

			return;
		}
		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	@Override
	public boolean evaluate(LinkBetweenProteinAndPSM link) {

		switch (aggregationLevel) {
		case PROTEIN:
			final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());
			return queryOverProtein;
		case PSM:
			final boolean queryOverPsm = queryOverPsm(link.getQueriablePsm().getPsm());
			return queryOverPsm;
		default:
			throw new IllegalArgumentException("Error in aggregation level");
		}

	}

	/**
	 * @return the scoreNameString
	 */
	public String getScoreNameString() {
		return scoreNameString;
	}

	/**
	 * @return the scoreTypeString
	 */
	public String getScoreTypeString() {
		return scoreTypeString;
	}

	private boolean queryOverPsm(Psm psm) {

		boolean found = false;
		final Set<PsmScore> psmScores = psm.getPsmScores();
		if (psmScores != null) {
			boolean scoreFound = false;
			for (PsmScore score : psmScores) {
				if (isThisScore(score.getName(), score.getConfidenceScoreType().getName())) {
					scoreFound = true;

					if (evaluateScore(score)) {
						// if !=Nan
						if (numericalCondition != null) {
							if (numericalCondition.isNanValue()
									&& numericalCondition.getOperator() == NumericalconditionOperator.NOT_EQUAL) {
								return true;
							} else {
								continue;
							}
						}
						return true;
					}
				}
			}
			if (!scoreFound) {
				// if =Nan
				if (numericalCondition != null && numericalCondition.isNanValue()
						&& numericalCondition.getOperator() == NumericalconditionOperator.EQUAL) {
					return true;
				}

			}
		}

		final Set<Ptm> ptMs = psm.getPtms();
		if (!found && ptMs != null) {
			boolean scoreFound = false;
			for (Ptm ptm : ptMs) {
				final Set<PtmSite> ptmSites = ptm.getPtmSites();
				if (ptmSites != null) {
					for (PtmSite ptmSite : ptmSites) {
						if (ptmSite.getConfidenceScoreName() != null) {
							if (isThisScore(ptmSite.getConfidenceScoreName(),
									ptmSite.getConfidenceScoreType().getName())) {
								scoreFound = true;

								if (evaluateScore(ptmSite.getConfidenceScoreName(),
										ptmSite.getConfidenceScoreType().getName(),
										ptmSite.getConfidenceScoreValue())) {
									// if !=Nan
									if (numericalCondition != null) {
										if (numericalCondition.isNanValue() && numericalCondition
												.getOperator() == NumericalconditionOperator.NOT_EQUAL) {
											return true;
										} else {
											continue;
										}
									}
									return true;
								}
							}
						}

					}
				}
			}
			if (!scoreFound) {
				// if =Nan
				if (numericalCondition != null && numericalCondition.isNanValue()
						&& numericalCondition.getOperator() == NumericalconditionOperator.EQUAL) {
					return true;
				}

			}
		}
		return false;
	}

	private boolean queryOverProtein(QueriableProteinInterface protein) {
		final Set<ProteinScore> proteinScores = protein.getProteinScores();
		if (proteinScores != null) {
			boolean scoreFound = false;
			for (ProteinScore score : proteinScores) {

				if (isThisScore(score.getName(), score.getConfidenceScoreType().getName())) {
					scoreFound = true;

					if (evaluateScore(score)) {
						// if !=Nan
						if (numericalCondition != null) {
							if (numericalCondition.isNanValue()
									&& numericalCondition.getOperator() == NumericalconditionOperator.NOT_EQUAL) {
								return true;
							} else {
								continue;
							}
						}
						return true;
					}
				}
			}
			if (!scoreFound) {
				// if =Nan
				if (numericalCondition != null && numericalCondition.isNanValue()
						&& numericalCondition.getOperator() == NumericalconditionOperator.EQUAL) {
					return true;
				}

			}
		}
		return false;
	}

	public boolean evaluateScore(ProteinScore score) {
		return evaluateScore(score.getName(), score.getConfidenceScoreType().getName(), score.getValue());
	}

	public boolean evaluateScore(PeptideScore score) {
		return evaluateScore(score.getName(), score.getConfidenceScoreType().getName(), score.getValue());
	}

	public boolean evaluateScore(PsmScore score) {
		final ConfidenceScoreType confidenceScoreType = score.getConfidenceScoreType();
		String scoreTypeName = null;
		if (confidenceScoreType != null)
			scoreTypeName = confidenceScoreType.getName();
		return evaluateScore(score.getName(), scoreTypeName, score.getValue());
	}

	public boolean evaluateScore(String scoreName, String scoreType, double scoreValue) {
		return evaluateScore(scoreName, scoreType, String.valueOf(scoreValue));
	}

	public boolean evaluateScore(String scoreName, String scoreType, String scoreValue) {
		if (!isThisScore(scoreName, scoreType))
			return false;

		if (numericalCondition != null) {
			if (scoreValue != null && !numericalCondition.matches(scoreValue))
				return false;
		}

		return true;
	}

	private boolean isThisScore(String scoreName, String scoreType) {
		if (!"".equals(scoreTypeString)) {
			if (!scoreTypeString.equalsIgnoreCase(scoreType)) {
				return false;
			}
		}
		if (!"".equals(scoreNameString)) {
			if (!scoreNameString.equalsIgnoreCase(scoreName))
				return false;
		}
		return true;
	}

	public NumericalCondition getNumericalCondition() {
		return numericalCondition;
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
			ret = new ProteinProviderFromProteinScores(scoreNameString, scoreTypeString);
			break;
		case PSM:
			ret = new PsmProviderFromPsmScores(scoreNameString, scoreTypeString);
			break;
		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}

		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (numericalCondition == null) {
			return false;
		}
		return true;
	}

}
