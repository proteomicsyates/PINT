package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromMSRun;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
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
public class QueryFromMSRunCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromMSRunCommand.class);
	private final Set<String> msRunIDs = new HashSet<String>();

	public QueryFromMSRunCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split != null && split.length > 0) {
			for (int i = 0; i < split.length; i++) {
				String msRunID = split[i].trim();
				if ("".equals(msRunID))
					throw new MalformedQueryException(
							"MS Run ID found empty in command '" + commandReference.getCommand().name() + "'");
				msRunIDs.add(msRunID);
			}

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	@Override
	public boolean evaluate(LinkBetweenProteinAndPSM link) {

		final boolean queryOverProtein = queryOverPsm(link.getQueriablePsm().getPsm());
		return queryOverProtein;

	}

	// private boolean queryOverProtein(QueriableProtein queriableProtein) {
	//
	// String msRunID = queriableProtein.getMsRun().getRunId();
	// if (msRunIDs.contains(msRunID)) {
	// return true;
	// }
	// return false;
	// }

	private boolean queryOverPsm(Psm psm) {

		final String msRunID = psm.getMsRun().getRunId();
		if (msRunIDs.contains(msRunID)) {
			if (!psm.getProteins().isEmpty())
				return true;
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PROTEIN;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {
		return new ProteinProviderFromMSRun(msRunIDs);
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return false;
	}

}
