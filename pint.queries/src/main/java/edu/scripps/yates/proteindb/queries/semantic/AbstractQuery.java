package edu.scripps.yates.proteindb.queries.semantic;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

public abstract class AbstractQuery implements Query {
	protected final CommandReference commandReference;
	protected final Logger log = Logger.getLogger(AbstractQuery.class);

	protected ProteinProviderFromDB proteinProvider;

	protected AbstractQuery(CommandReference commandReference) {
		this.commandReference = commandReference;

	}

	/**
	 * Creates an appropiate {@link ProteinProviderFromDB}
	 */
	public abstract ProteinProviderFromDB initProtenProvider();

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

	public final ProteinProviderFromDB getProteinProvider() {
		if (proteinProvider == null) {
			proteinProvider = initProtenProvider();
		}
		return proteinProvider;
	}

	/**
	 * Returns true if the query still requires to evaluate the
	 * {@link QueriableProteinSet2PSMLink} even when the user submit only one
	 * simple query
	 *
	 * @return
	 */
	public abstract boolean requiresFurtherEvaluation();
}
