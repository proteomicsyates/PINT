package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Label;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.peptides.PeptideProviderFromPeptideLabeledAmount;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromLabeledAmount;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromPsmLabeledAmount;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a {@link Query} from Label command:<br>
 * LB [Aggregation level, label_name, ONLY|]
 *
 * @author Salva
 *
 */
public class QueryFromLabelCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromLabelCommand.class);
	private String labelString;
	private Boolean labelOnly;
	private static final String ONLY = "ONLY";
	private AggregationLevel aggregationLevel;

	public QueryFromLabelCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 3) {
			final String aggregationLevelString = split[0].trim();
			labelString = split[1].trim();
			if ("".equals(labelString))
				throw new MalformedQueryException(
						"Label name is not provided in command '" + commandReference.getCommand().name() + "'");
			final String onlyString = split[2].trim();
			if (!"".equals(aggregationLevelString)) {

				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null)

					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on '"
						+ commandReference.getCommand().name() + "' command");
			}
			if (!"".equals(onlyString)) {
				if (!AggregationLevel.PSM.equals(aggregationLevel)) {
					throw new MalformedQueryException("A value in third position of command '"
							+ commandReference.getCommand().name()
							+ "' is only allowed for PSM aggregation level. Leave that empty or change aggregation level to PSM.");
				}
				if (onlyString.equals(ONLY)) {
					labelOnly = true;
					// only valid for PSM aggregation level
					if (aggregationLevel != AggregationLevel.PSM) {
						throw new MalformedQueryException(
								"The parameter ONLY is just valid for aggregation level PSM in command '"
										+ commandReference.getCommand().name() + "'");
					}
				} else {
					throw new MalformedQueryException("'" + onlyString + "' is not valid for command "
							+ commandReference.getCommand().name() + ". Valid values are: ONLY|(empty).");
				}
			} else {
				labelOnly = false;
			}

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for "
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
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
			throw new IllegalArgumentException("Error in aggregation level");
		}

	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {

		switch (aggregationLevel) {
		case PROTEIN:
			final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());
			return queryOverProtein;

		case PEPTIDE:
			final boolean queryOverPeptide = queryOverPeptide(link.getQueriablePeptide());
			return queryOverPeptide;

		default:
			throw new IllegalArgumentException("Error in aggregation level");
		}

	}

	private boolean queryOverProtein(QueriableProteinSet protein) {

		final Set<ProteinAmount> proteinAmounts = protein.getProteinAmounts();
		if (proteinAmounts != null) {
			for (final ProteinAmount proteinAmount : proteinAmounts) {
				final Condition condition = proteinAmount.getCondition();
				if (condition != null) {
					final Sample sample = condition.getSample();
					if (sample != null) {
						final Label label = sample.getLabel();
						if (label.getName().equalsIgnoreCase(labelString)) {
							return true;
						}
					}
				}
			}
		}
		return false;

	}

	private boolean queryOverPsm(Psm psm) {
		boolean anyOtherLabel = false;
		boolean targetLabel = false;
		final Set<PsmAmount> psmAmounts = psm.getPsmAmounts();
		if (psmAmounts != null) {

			for (final PsmAmount psmAmount : psmAmounts) {
				final Condition condition = psmAmount.getCondition();
				if (condition != null) {
					final Sample sample = condition.getSample();
					if (sample != null) {
						final Label label = sample.getLabel();
						final String labelName = label.getName();
						if (labelName.equalsIgnoreCase(labelString)) {
							targetLabel = true;
						} else {
							anyOtherLabel = true;
						}
					}
				}
			}
		}
		if (targetLabel) {
			if (labelOnly != null && labelOnly) {
				if (!anyOtherLabel) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean queryOverPeptide(QueriablePeptideSet peptideSet) {
		boolean anyOtherLabel = false;
		boolean targetLabel = false;
		final Set<PeptideAmount> psmAmounts = peptideSet.getPeptideAmounts();
		if (psmAmounts != null) {

			for (final PeptideAmount peptideAmount : psmAmounts) {
				final Condition condition = peptideAmount.getCondition();
				if (condition != null) {
					final Sample sample = condition.getSample();
					if (sample != null) {
						final Label label = sample.getLabel();
						final String labelName = label.getName();
						if (labelName.equalsIgnoreCase(labelString)) {
							targetLabel = true;
						} else {
							anyOtherLabel = true;
						}
					}
				}
			}
		}
		if (targetLabel) {
			if (labelOnly != null && labelOnly) {
				if (!anyOtherLabel) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	@Override
	public DataProviderFromDB initProtenProvider() {
		DataProviderFromDB ret = null;
		switch (aggregationLevel) {
		case PROTEIN:
			ret = new ProteinProviderFromLabeledAmount(labelString);
			break;
		case PEPTIDE:
			ret = new PeptideProviderFromPeptideLabeledAmount(labelString, labelOnly);
			break;
		case PSM:
			ret = new PsmProviderFromPsmLabeledAmount(labelString, labelOnly);
			break;
		default:
			throw new IllegalArgumentException("aggregation level is not acceptable");
		}

		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return false;
	}

}
