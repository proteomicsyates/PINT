package edu.scripps.yates.proteindb.queries.semantic;

import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromAmountCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromComplexAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromConditionCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromGeneNameCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromLabelCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromMSRunCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromPTMCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromProteinAccessionsCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromRatioCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSEQCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromScoreCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromSimpleAnnotationCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromTaxonomyCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromThresholdCommand;
import edu.scripps.yates.proteindb.queries.semantic.command.QueryFromTissueCommand;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;

/**
 * This class will translate the query commands into a {@link QueryImpl} object
 *
 * @author Salva
 *
 */
public class SemanticTranslator {

	public SemanticTranslator() {
	}

	public AbstractQuery translate(String queryCommand) throws MalformedQueryException {
		if (queryCommand == null)
			throw new MalformedQueryException("Query command cannot be null");
		if ("".equals(queryCommand))
			throw new MalformedQueryException("Query command cannot be empty");

		final CommandReference commandReference = new CommandReference(queryCommand);
		AbstractQuery query = null;
		final Command command = commandReference.getCommand();
		switch (command) {
		case CONDITION_PROJECT:
			query = new QueryFromConditionCommand(commandReference);
			return query;
		case COMPLEX_ANNOTATION:
			query = new QueryFromComplexAnnotationCommand(commandReference);
			return query;
		case SIMPLE_ANNOTATION:
			query = new QueryFromSimpleAnnotationCommand(commandReference);
			return query;
		case SCORE:
			query = new QueryFromScoreCommand(commandReference);
			return query;
		case RATIO:
			query = new QueryFromRatioCommand(commandReference);
			return query;
		case AMOUNT:
			query = new QueryFromAmountCommand(commandReference);
			return query;
		case THRESHOLD:
			query = new QueryFromThresholdCommand(commandReference);
			return query;
		case GENE_NAME:
			query = new QueryFromGeneNameCommand(commandReference);
			return query;
		case LABEL:
			query = new QueryFromLabelCommand(commandReference);
			return query;
		case TAXONOMY:
			query = new QueryFromTaxonomyCommand(commandReference);
			return query;
		case PROTEIN_ACC:
			query = new QueryFromProteinAccessionsCommand(commandReference);
			return query;
		case MSRUN:
			query = new QueryFromMSRunCommand(commandReference);
			return query;
		case PTM:
			query = new QueryFromPTMCommand(commandReference, AggregationLevel.PEPTIDE);
			return query;
		case SEQUENCE:
			query = new QueryFromSEQCommand(commandReference);
			return query;
		case TISSUE:
			query = new QueryFromTissueCommand(commandReference);
		default:
			break;
		}

		throw new MalformedQueryException("Failed to recognize the query command:'" + queryCommand + "'");
	}
}
