package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinAccs;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

/**
 * Implements a query from ProteinAccessions command: <br>
 * AC[Accession, accession2, accession3, ...]
 *
 * @author Salva
 *
 */
public class QueryFromProteinAccessionsCommand extends AbstractQuery {
	private final List<String> accs = new ArrayList<String>();

	public QueryFromProteinAccessionsCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length >= 1) {
			for (final String string : split) {
				if ("".equals(string.trim()))
					continue;
				accs.add(string);
			}
			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	/**
	 * @return the accs
	 */
	public List<String> getAccs() {
		return accs;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {
		return queryOverProtein(link.getQueriableProtein());

	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		return queryOverProtein(link.getQueriableProtein());

	}

	private boolean queryOverProtein(QueriableProteinSet protein) {

		final Set<String> proteinAccessions = protein.getProteinAccessions();
		for (final String proteinAccession : proteinAccessions) {
			if (accs.contains(proteinAccession))
				return true;
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PROTEIN;
	}

	@Override
	public DataProviderFromDB initProtenProvider() {
		return new ProteinProviderFromProteinAccs(accs);
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return false;
	}
}
