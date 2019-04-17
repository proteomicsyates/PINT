package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.NumericalconditionOperator;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.peptides.PeptideProviderFromPeptideAmounts;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinAmounts;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromPsmAmounts;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType;
import gnu.trove.set.hash.THashSet;

/**
 * Implements a {@link Query} from Amount command:<br>
 * AM [Aggregation level, Amount type, Condition, Numerical condition]
 *
 * @author Salva
 *
 */
public class QueryFromAmountCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromAmountCommand.class);

	private AmountType amountType;
	private ConditionReferenceFromCommandValue condition;
	private NumericalCondition numericalCondition;
	private AggregationLevel aggregationLevel;

	private boolean thereIsProteinAmountManuallyAdded;

	public QueryFromAmountCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 4) {
			final String aggregationLevelString = split[0].trim();
			final String amountTypeString = split[1].trim();
			final String conditionCommandString = split[2].trim();
			final String numericalConditionString = split[3].trim();

			if (!"".equals(aggregationLevelString)) {

				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null)
					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on AM command");
			}

			if (!"".equals(amountTypeString)) {
				amountType = AmountType.translateStringToAmountType(amountTypeString);
				if (amountType == null) {
					throw new MalformedQueryException("Amount type is not recognized: '" + amountTypeString
							+ "'. Allowed values are: " + AmountType.getValuesString());
				}

				if (amountType == AmountType.SPC && (aggregationLevel != AggregationLevel.PROTEIN
						&& aggregationLevel != AggregationLevel.PEPTIDE)) {
					throw new MalformedQueryException("SPC amount type is only valid for aggregation level of "
							+ AggregationLevel.PROTEIN.name());
				}
			}
			if (!"".equals(conditionCommandString)) {
				condition = new ConditionReferenceFromCommandValue(
						new CommandReference(conditionCommandString).getCommandValue());
			}

			if (!"".equals(numericalConditionString))
				numericalCondition = new NumericalCondition(numericalConditionString);

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	private boolean queryOverProtein(QueriableProteinSet queriableProtein) {

		boolean amountFound = false;

		final Set<ProteinAmount> proteinAmounts = getProteinAmountsByType(queriableProtein);
		for (final ProteinAmount proteinAmount : proteinAmounts) {
			if (condition != null && condition.passCondition(proteinAmount.getCondition())) {
				amountFound = true;
			}
			final boolean valid = evaluateAmount(proteinAmount);
			if (valid) {
				return true;
			}
		}

		// if we dont have found the specified amount type and the
		// numerical condition is =Nan, add to results
		if (!amountFound && numericalCondition.isNanValue()
				&& NumericalconditionOperator.EQUAL == numericalCondition.getOperator()) {
			return true;
		}
		if (!amountFound && NumericalconditionOperator.NOT_EQUAL == numericalCondition.getOperator()
				&& !numericalCondition.isNanValue()) {
			return true;
		}

		// for SPC, take the SPC off all the proteins in that condition
		if (!thereIsProteinAmountManuallyAdded && amountType == AmountType.SPC) {
			final Set<ProteinAmount> specCounts = new THashSet<ProteinAmount>();
			final List<Condition> conditions = queriableProtein.getConditions();
			for (final Condition condition : conditions) {
				specCounts.add(getSpectralCountProteinAmount(condition, queriableProtein));
			}
			for (final ProteinAmount specCount : specCounts) {
				final boolean valid = evaluateAmount(specCount);
				if (valid) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean evaluateAmount(ProteinAmount proteinAmount) {
		if (condition != null && condition.passCondition(proteinAmount.getCondition())) {
			if (numericalCondition != null) {
				if (numericalCondition.matches(proteinAmount.getValue())) {
					return true;
				}
			} else {
				return true;
			}
		} else if (condition == null) {
			if (numericalCondition != null) {
				if (numericalCondition.matches(proteinAmount.getValue())) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean queryOverPsm(QueriablePsm psm) {
		final Set<PsmAmount> psmAmounts = getPsmAmountsByType(psm);
		boolean amountFound = false;
		for (final PsmAmount psmAmount : psmAmounts) {
			if (condition.passCondition(psmAmount.getCondition())) {
				amountFound = true;
				if (numericalCondition != null) {
					if (numericalCondition.matches(psmAmount.getValue()))
						return true;
				} else {
					return true;
				}
			}
		}
		// if we dont have found the specified amount type and the
		// numerical condition is =Nan, add to results
		if (!amountFound && numericalCondition.isNanValue()
				&& NumericalconditionOperator.EQUAL == numericalCondition.getOperator()) {
			return true;
		}
		if (!amountFound && NumericalconditionOperator.NOT_EQUAL == numericalCondition.getOperator()
				&& !numericalCondition.isNanValue()) {
			return true;
		}
		return false;
	}

	private boolean queryOverPeptide(QueriablePeptideSet peptide) {
		final Set<PeptideAmount> peptideAmounts = peptide.getPeptideAmounts(getAmountType());
		boolean amountFound = false;
		for (final PeptideAmount peptideAmount : peptideAmounts) {
			if (condition.passCondition(peptideAmount.getCondition())) {
				amountFound = true;
				if (numericalCondition != null) {
					if (numericalCondition.matches(peptideAmount.getValue()))
						return true;
				} else {
					return true;
				}
			}
		}
		// if we dont have found the specified amount type and the
		// numerical condition is =Nan, add to results
		if (!amountFound && numericalCondition.isNanValue()
				&& NumericalconditionOperator.EQUAL == numericalCondition.getOperator()) {
			return true;
		}
		if (!amountFound && NumericalconditionOperator.NOT_EQUAL == numericalCondition.getOperator()
				&& !numericalCondition.isNanValue()) {
			return true;
		}
		return false;
	}

	private Set<PsmAmount> getPsmAmountsByType(QueriablePsm psm) {
		final Set<PsmAmount> ret = new THashSet<PsmAmount>();
		final Set<PsmAmount> psmAmounts = psm.getPsm().getPsmAmounts();
		for (final PsmAmount psmAmount : psmAmounts) {
			if (amountType == null) {
				ret.add(psmAmount);
			} else if (amountType != null && psmAmount.getAmountType().getName().equalsIgnoreCase(amountType.name())) {
				ret.add(psmAmount);
				continue;
			}
		}
		return ret;
	}

	private Set<ProteinAmount> getProteinAmountsByType(QueriableProteinSet protein) {
		final Set<ProteinAmount> ret = new THashSet<ProteinAmount>();
		final List<ProteinAmount> proteinAmounts = protein.getProteinAmounts();
		thereIsProteinAmountManuallyAdded = false;
		for (final ProteinAmount proteinAmount : proteinAmounts) {
			if (amountType == null) {
				ret.add(proteinAmount);
			} else if (amountType != null
					&& proteinAmount.getAmountType().getName().equalsIgnoreCase(amountType.name())) {
				if (condition != null && !condition.passCondition(proteinAmount.getCondition())) {
					continue;
				}
				if (proteinAmount.getManualSPC() != null && proteinAmount.getManualSPC()) {
					thereIsProteinAmountManuallyAdded = true;
				}
				// not add SPC if it is not manual added
				if (!thereIsProteinAmountManuallyAdded && amountType == AmountType.SPC) {
				} else {
					ret.add(proteinAmount);
				}
			}
		}
		if (!thereIsProteinAmountManuallyAdded && amountType == AmountType.SPC) {
			final List<Condition> conditions = protein.getConditions();
			for (final Condition condition : conditions) {
				ret.add(getSpectralCountProteinAmount(condition, protein));
			}
		}
		return ret;
	}

	public ProteinAmount getSpectralCountProteinAmount(Condition condition, QueriableProteinSet protein) {
		final ProteinAmount spc = new ProteinAmount();
		spc.setAmountType(new edu.scripps.yates.proteindb.persistence.mysql.AmountType(AmountType.SPC.name()));
		spc.setManualSPC(false);
		int spcNum = 0;

		if (protein.getLinksToPSMs() != null) {
			for (final LinkBetweenQueriableProteinSetAndPSM link : protein.getLinksToPSMs()) {
				if (link.getQueriablePsm().getConditions().contains(condition)) {
					spcNum++;
				}
			}
		} else {
			for (final LinkBetweenQueriableProteinSetAndPeptideSet link : protein.getLinksToPeptides()) {
				if (link.getQueriablePeptideSet().getConditions().contains(condition)) {
					final List<Peptide> peptides = link.getQueriablePeptideSet().getIndividualPeptides();
					for (final Peptide peptide : peptides) {
						if (peptide.getConditions().contains(condition)) {
							spcNum += peptide.getNumPsms();
						}
					}
				}
			}
		}
		spc.setValue(spcNum);
		spc.setCondition(condition);
		return spc;

	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	/**
	 * @return the amountType
	 */
	public AmountType getAmountType() {
		return amountType;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {
		switch (aggregationLevel) {
		case PROTEIN:

			final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());

			return queryOverProtein;

		case PSM:
			final boolean queryOverPsm = queryOverPsm(link.getQueriablePsm());

			return queryOverPsm;

		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}
	}

	@Override
	public DataProviderFromDB initProtenProvider() {
		DataProviderFromDB ret = null;
		switch (aggregationLevel) {
		case PROTEIN:
			ret = new ProteinProviderFromProteinAmounts(condition, amountType);
			break;
		case PEPTIDE:
			ret = new PeptideProviderFromPeptideAmounts(condition, amountType);
		case PSM:
			ret = new PsmProviderFromPsmAmounts(condition, amountType);
			break;
		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}

		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (numericalCondition != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		switch (aggregationLevel) {
		case PROTEIN:

			final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());

			return queryOverProtein;

		case PEPTIDE:
			final boolean queryOverPeptide = queryOverPeptide(link.getQueriablePeptideSet());

			return queryOverPeptide;

		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}
	}

}
