package edu.scripps.yates.shared.util;

public enum SequenceOverlapping {
	TOTALLY_COVERED("pep. fully covered", "fullyCoveredSequenceRegion"),
	PARTIALLY_COVERED("pep. partially covered", "partiallyCoveredSequenceRegion"), NOT_COVERED("not covered", "");
	private final String description;
	private final String cssClassName;

	private SequenceOverlapping(String description, String cssClassName) {
		this.description = description;
		this.cssClassName = cssClassName;
	}

	public String getDescription() {
		return description;
	}

	public String getCSSClassName() {
		return cssClassName;
	}
}
