package edu.scripps.yates.proteindb.queries.semantic;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AggregationLevel;

public abstract class AbstractQuery implements Query {
	protected final CommandReference commandReference;
	protected final Logger log = Logger.getLogger(AbstractQuery.class);

	protected DataProviderFromDB proteinProvider;

	protected AbstractQuery(CommandReference commandReference) {
		this.commandReference = commandReference;

	}

	/**
	 * Creates an appropiate {@link DataProviderFromDB}
	 */
	public abstract DataProviderFromDB initProtenProvider();

	@Override
	public final String toString() {
		// return commandReference.getCommandValue();
		return commandReference.toString();
	}

	@Override
	public final boolean isNegative() {
		return commandReference.isNegative();
	}

	/**
	 * @return the commandReference
	 */
	@Override
	public final CommandReference getCommandReference() {
		return commandReference;
	}

	public abstract AggregationLevel getAggregationLevel();

	public final DataProviderFromDB getProteinProvider() {
		if (proteinProvider == null) {
			proteinProvider = initProtenProvider();
		}
		return proteinProvider;
	}

	/**
	 * Returns true if the query still requires to evaluate the
	 * {@link LinkBetweenQueriableProteinSetAndPSM} even when the user submit only one
	 * simple query
	 *
	 * @return
	 */
	public abstract boolean requiresFurtherEvaluation();
}
