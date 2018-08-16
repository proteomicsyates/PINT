package edu.scripps.yates.proteindb.queries;

import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;

public interface Query {

	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link);

	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link);

	public boolean isNegative();

	public CommandReference getCommandReference();

}
