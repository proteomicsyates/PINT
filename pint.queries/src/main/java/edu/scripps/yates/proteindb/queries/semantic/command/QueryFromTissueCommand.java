package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromTissues;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;
import gnu.trove.set.hash.THashSet;

/**
 * Implements a {@link Query} from Taxonomy command:<br>
 * TX[Aggregation_level, organism_name, ncbi_tax_id]
 *
 * @author Salva
 *
 */
public class QueryFromTissueCommand extends AbstractQuery {
	private static Logger log = Logger.getLogger(QueryFromTissueCommand.class);

	private Set<String> tissueNames = new THashSet<String>();

	public QueryFromTissueCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length >= 1) {
			for (String string : split) {
				tissueNames.add(string);
			}

		}

		if (tissueNames.isEmpty()) {
			throw new MalformedQueryException("Tissue name should be provided in "
					+ commandReference.getCommand().getAbbreviation() + " command");
		}

		return;

	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());
		return queryOverProtein;

	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {

		final boolean queryOverProtein = queryOverProtein(link.getQueriableProtein());
		return queryOverProtein;

	}

	private boolean queryOverProtein(QueriableProteinSet protein) {

		return isValidTissue(protein.getTissues());

	}

	private boolean isValidTissue(Set<Tissue> tissues) {

		if (tissues != null) {
			for (Tissue tissue : tissues) {
				if (tissueNames.contains(tissue.getName()) || tissueNames.contains(tissue.getTissueId())) {
					return true;
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
	public DataProviderFromDB initProtenProvider() {
		DataProviderFromDB ret = new ProteinProviderFromTissues(tissueNames);
		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {

		return false; // because it is already queries from the DB
	}
}
