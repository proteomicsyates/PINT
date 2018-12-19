package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;
import edu.scripps.yates.utilities.strings.StringUtils;
import gnu.trove.set.hash.THashSet;

/**
 * Implements a {@link Query} from Gene Name command:<br>
 * GN [Gene_name]
 *
 * @author Salva
 *
 */
public class QueryFromGeneNameCommand extends AbstractQuery {

	private final Set<String> geneNames = new THashSet<String>();
	private static Logger log = Logger.getLogger(QueryFromGeneNameCommand.class);

	public QueryFromGeneNameCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split != null && split.length > 0) {
			for (final String geneName : split) {
				if (geneName == null || "".equals(geneName)) {
					throw new MalformedQueryException("Gene name cannot be null");
				}
				geneNames.add(geneName);
			}
			return;
		}
		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		final Set<Gene> genes = link.getQueriableProtein().getGenes();
		if (genes != null) {
			// to be included in the result, at least one gene
			// will have to match
			for (final Gene gene : genes) {
				for (final String geneName : geneNames) {
					if (StringUtils.compareStrings(gene.getGeneId(), geneName, true, true, false)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {

		final Set<Gene> genes = link.getQueriableProtein().getGenes();
		if (genes != null) {
			// to be included in the result, at least one gene
			// will have to match
			for (final Gene gene : genes) {
				for (final String geneName : geneNames) {
					if (StringUtils.compareStrings(gene.getGeneId(), geneName, true, true, false)) {
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
	public DataProviderFromDB initProtenProvider() {
		// change in Ago 2nd 2018
		return null;
		// return new ProteinProviderFromProteinGenes(geneNames);

	}

	@Override
	public boolean requiresFurtherEvaluation() {
		// change in Ago 2nd 2018
		return true;
		// return false;
	}

}
