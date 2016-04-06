package edu.scripps.yates.proteindb.queries;

import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;

public interface Query {

	public boolean evaluate(LinkBetweenProteinAndPSM link);

	public boolean isNegative();

	public CommandReference getCommandReference();

}
