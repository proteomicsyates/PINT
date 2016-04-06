package edu.scripps.yates.proteindb.queries;

public enum LogicalOperator {
	AND(" AND "), OR(" OR "), XOR(" XOR ");

	private String text;

	private LogicalOperator(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public static LogicalOperator translateToLogicalOperator(Object obj) {
		if (obj instanceof String) {
			String t = ((String) obj).trim();
			final LogicalOperator[] values = LogicalOperator.values();
			for (LogicalOperator logicalOperator : values) {
				if (logicalOperator.getText().trim().equalsIgnoreCase(t) || logicalOperator.name().equalsIgnoreCase(t))
					return logicalOperator;
			}
		} else if (obj instanceof LogicalOperator)
			return (LogicalOperator) obj;
		return null;
	}
}
