package edu.scripps.yates.census.analysis.wrappers;

public enum SanXotResultProperty {
	VARIANCE("Variance ="), K("K =");
	private final String lineBegin;

	private SanXotResultProperty(String lineBegin) {
		this.lineBegin = lineBegin;
	}

	/**
	 * @return the lineBegin
	 */
	public String getLineBegin() {
		return lineBegin;
	}

}
