package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Threshold;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinThresholds;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a query from SCORE command: <br>
 * TH[threshold name, pass]
 *
 * @author Salva
 *
 */
public class QueryFromThresholdCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromThresholdCommand.class);
	private boolean pass;
	private String thresholdName;

	public QueryFromThresholdCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 2) {
			thresholdName = split[0].trim();
			String passString = split[1].trim();

			// all MUST be not null
			if ("".equals(thresholdName))
				throw new MalformedQueryException(
						"Threshold name must not be empty at THRESHOLD command '" + commandReference + "'");
			if ("".equals(passString))
				throw new MalformedQueryException(
						"Pass string must not be empty at THRESHOLD command '" + commandReference + "'");
			if (passString.equalsIgnoreCase("true") || passString.equalsIgnoreCase("yes"))
				pass = true;
			else if (passString.equalsIgnoreCase("false") || passString.equalsIgnoreCase("not"))
				pass = false;
			else
				throw new MalformedQueryException(
						"Pass value is not recognized. Valid values are: true, yes, false or not");

			return;
		}
		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	@Override
	public boolean evaluate(LinkBetweenProteinAndPSM link) {
		final Set<ProteinThreshold> proteinThresholds = link.getQueriableProtein().getProteinThresholds();

		for (ProteinThreshold proteinThreshold : proteinThresholds) {
			final Threshold threshold = proteinThreshold.getThreshold();
			if (threshold != null) {
				if (thresholdName.equalsIgnoreCase(threshold.getName())) {
					if (Boolean.compare(pass, proteinThreshold.isPassThreshold()) == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PROTEIN;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {
		return new ProteinProviderFromProteinThresholds(thresholdName, pass);
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return false;
	}
}
