package edu.scripps.yates.proteindb.queries;

public enum NumericalconditionOperator {
	GREATER_OR_EQUAL(">="), LESS_OR_EQUAL("<="), EQUAL("="), LESS("<"), GREATER(
			">"), NOT_EQUAL("!=");

	private final String text;

	private NumericalconditionOperator(String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

}
