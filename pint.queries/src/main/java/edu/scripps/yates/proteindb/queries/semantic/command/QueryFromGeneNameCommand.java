package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.protein.ProteinProviderFromProteinGenes;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
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
			for (String geneName : split) {
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
			for (Gene gene : genes) {
				for (String geneName : geneNames) {
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
	public ProteinProviderFromDB initProtenProvider() {
		return new ProteinProviderFromProteinGenes(geneNames);

	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return false;
	}

}
