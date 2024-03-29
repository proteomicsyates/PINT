package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;

/**
 * Implements a {@link Query} from Taxonomy command:<br>
 * TX[Aggregation_level, organism_name, ncbi_tax_id]
 *
 * @author Salva
 *
 */
public class QueryFromTaxonomyCommand extends AbstractQuery {
	private static final String ONLY = "ONLY";
	private static Logger log = Logger.getLogger(QueryFromTaxonomyCommand.class);

	private String organismName;
	private String ncbiTaxID;
	private AggregationLevel aggregationLevel;
	private boolean exclusiveTaxonomy;

	public QueryFromTaxonomyCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 4) {
			final String aggregationLevelString = split[0].trim();
			final String organismNameString = split[1].trim();
			if (!"".equals(organismNameString))
				organismName = organismNameString;
			final String ncbiTaxIDString = split[2].trim();
			if (!"".equals(ncbiTaxIDString))
				ncbiTaxID = ncbiTaxIDString;
			final String onlyString = split[3].trim();
			if (!"".equals(aggregationLevelString)) {
				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null)
					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on AM command");
			}

			if (organismName == null && ncbiTaxID == null) {
				throw new MalformedQueryException("Either organism_name or NCBI_tax_ID should be provided in "
						+ commandReference.getCommand().getAbbreviation() + " command");
			}
			if (!"".equals(onlyString)) {
				if (!AggregationLevel.PSM.equals(aggregationLevel)) {
					throw new MalformedQueryException("A value in fourth position of command '"
							+ commandReference.getCommand().name()
							+ "' is only allowed for PSM aggregation level. Leave that empty or change aggregation level to PSM.");
				}
				if (onlyString.equals(ONLY)) {
					exclusiveTaxonomy = true;
				} else {
					throw new MalformedQueryException("'" + onlyString + "' is not valid for command "
							+ commandReference.getCommand().name() + ". Valid values are: ONLY|(empty).");
				}
			} else {
				exclusiveTaxonomy = false;
			}

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
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
			final boolean queryOverPsm = queryOverPsm(link.getQueriablePsm());
			return queryOverPsm;

		default:

			throw new IllegalArgumentException("Aggregation level error");
		}
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

			throw new IllegalArgumentException("Aggregation level error");
		}
	}

	private boolean queryOverProtein(QueriableProteinSet protein) {

		return isValidOrganism(protein.getOrganism());

	}

	private boolean queryOverPsm(QueriablePsm psm) {
		final Set<Organism> organisms = psm.getOrganisms();

		if (exclusiveTaxonomy) {
			if (organisms.size() == 1) {
				return isValidOrganism(organisms.iterator().next());
			}
			return false;
		} else {
			for (final Organism organism : organisms) {
				if (isValidOrganism(organism))
					return true;
			}

		}
		return false;
	}

	private boolean queryOverPeptide(QueriablePeptideSet peptide) {
		final Set<Organism> organisms = peptide.getOrganisms();

		if (exclusiveTaxonomy) {
			if (organisms.size() == 1) {
				return isValidOrganism(organisms.iterator().next());
			}
			return false;
		} else {
			for (final Organism organism : organisms) {
				if (isValidOrganism(organism))
					return true;
			}

		}
		return false;
	}

	private boolean isValidOrganism(Organism organism) {

		if (organism != null) {
			if (ncbiTaxID != null && ncbiTaxID.equalsIgnoreCase(organism.getTaxonomyId())) {
				return true;
			} else if (organismName != null && organismName.equalsIgnoreCase(organism.getName())) {
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
		final DataProviderFromDB ret = null;
		// change in Ago 2nd 2018
		// switch (aggregationLevel) {
		// case PROTEIN:
		// ret = new ProteinProviderFromTaxonomy(organismName, ncbiTaxID);
		// break;
		// case PSM:
		// ret = new PsmProviderFromTaxonomy(organismName, ncbiTaxID);
		// break;
		// case PEPTIDE:
		// ret = new PeptideProviderFromTaxonomy(organismName, ncbiTaxID);
		// break;
		// default:
		// throw new IllegalArgumentException("aggregation level is not
		// acceptable");
		// }

		return ret;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		// change in Ago 2nd 2018
		// if (!exclusiveTaxonomy) {
		// return false;
		// }
		return true; // because of the exclusive taxonomy
	}
}
