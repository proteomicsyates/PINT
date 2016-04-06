package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromPTMs;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a {@link Query} from PTM command:<br>
 * PTM [PTM name, mass_diff, tolerance_da, Numerical condition ]
 *
 * @author Salva
 *
 */
public class QueryFromPTMCommand extends AbstractQuery {

	private String ptmName;
	private Double massDiff;
	private Double massTolerance;
	private NumericalCondition numericalCondition;
	private static Logger log = Logger.getLogger(QueryFromPTMCommand.class);

	public QueryFromPTMCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 4) {
			ptmName = split[0].trim();
			if ("".equals(ptmName)) {
				ptmName = null;
			}
			String massDiffString = split[1].trim();
			if (!"".equals(massDiffString)) {
				try {
					massDiff = Double.valueOf(massDiffString);
				} catch (NumberFormatException e) {
					throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for "
							+ commandReference.getCommand().name()
							+ " command. Second argument, mass_diff, should be a valid number");
				}
			}
			String massToleranceString = split[2].trim();
			if (!"".equals(massToleranceString)) {
				try {
					massTolerance = Double.valueOf(massToleranceString);
				} catch (NumberFormatException e) {
					throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for "
							+ commandReference.getCommand().name()
							+ " command. Third argument, mass_tolerance, should be a valid number");
				}
			}

			String numericalConditionString = split[3].trim();
			if (!"".equals(numericalConditionString)) {
				try {
					numericalCondition = new NumericalCondition(numericalConditionString);
				} catch (MalformedQueryException e) {
					throw new MalformedQueryException(
							"Numerical condition of '" + commandReference + "' is not well formed for "
									+ commandReference.getCommand().name() + " command." + e.getMessage());
				}
			}

			if (ptmName == null && massDiff == null && massTolerance == null && numericalCondition == null) {
				throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for "
						+ commandReference.getCommand().name()
						+ " command. At least PTM_name or PTM_mass_diff should be provided. Usage:"
						+ commandReference.getCommand().getFormat());
			}
			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	@Override
	public boolean evaluate(LinkBetweenProteinAndPSM link) {
		final Psm psm = link.getQueriablePsm().getPsm();

		final Set<Ptm> ptms = psm.getPtms();
		int numValidPTMs = 0;
		if (ptms != null) {
			// to be included in the result, at least one gene
			// will have to match
			for (Ptm ptm : ptms) {
				boolean valid = true;
				if (ptmName != null) {
					if (!ptmName.equals(ptm.getName())) {
						valid = false;
					}
				}
				if (massDiff != null) {
					if (massTolerance != null) {
						if (Math.abs(ptm.getMassShift() - massDiff) > massTolerance) {
							valid = false;
						}
					} else {
						if (massDiff != ptm.getMassShift()) {
							valid = false;
						}
					}
				}
				if (numericalCondition != null && valid) {
					numValidPTMs += ptm.getPtmSites().size();
				}
				if (valid && numericalCondition == null) {
					return true;
				}
			}
		}
		if (numericalCondition != null && numericalCondition.matches(numValidPTMs)) {
			return true;
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PSM;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {

		return new PsmProviderFromPTMs(ptmName);

	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (massDiff == null && numericalCondition == null) {
			return false;
		}
		// due to the tolerance and mass errors or in case of not providing a
		// PTMName and just a number of PTMs
		return true;
	}

}
