package edu.scripps.yates.proteindb.queries.semantic;

import edu.scripps.yates.proteindb.queries.LogicalOperator;

public class QueryBinaryTreeElement {
	private final AbstractQuery abstractQuery;
	private final LogicalOperator logicalOperator;

	public QueryBinaryTreeElement(AbstractQuery t) {
		abstractQuery = t;
		logicalOperator = null;
	}

	public QueryBinaryTreeElement(String logicalOperatorWord) {
		logicalOperator = LogicalOperator.translateToLogicalOperator(logicalOperatorWord);
		if (logicalOperator == null)
			throw new IllegalArgumentException(logicalOperatorWord + " is not a valid logical operator.");
		abstractQuery = null;
	}

	/**
	 * @return the abstractQuery
	 */
	public AbstractQuery getAbstractQuery() {
		return abstractQuery;
	}

	/**
	 * @return the logicalOperator
	 */
	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (abstractQuery != null) {
			String negative = "";
			if (abstractQuery.isNegative()) {
				negative = "!";
			}
			return negative + abstractQuery.toString();
		}
		if (logicalOperator != null) {
			return logicalOperator.getText();
		}
		return super.toString();
	}

}
