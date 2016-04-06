package edu.scripps.yates.utilities.proteomicsmodel;

public enum UniprotLineHeader {
	CC("comment"), FT("feature"), PE("Protein existence"), DR(
			"Database cross-Reference"), RC("Reference Comment"), GO(
			"Gene Ontology"), RX("Reference cross-reference"), KWR("keyword"), STA(
			"status"), MAN("Manual annotation"), DT(
			"Entry and sequence dates and versions");
	private final String description;

	private UniprotLineHeader(String description) {
		this.description = description;
	}

	public static UniprotLineHeader translateStringToUniprotLineHeader(
			String uniprotHeader) {
		final UniprotLineHeader[] values = UniprotLineHeader.values();
		for (UniprotLineHeader uniprotLineHeader : values) {
			if (uniprotLineHeader.name().equalsIgnoreCase(uniprotHeader))
				return uniprotLineHeader;
		}
		return null;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

}
