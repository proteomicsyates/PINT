package edu.scripps.yates.utilities.taxonomy;

public enum UniprotKingdom {
	ARCHEA('A', "archaea (=archaebacteria)"), BACTERIA('B',
			"bacteria (=prokaryota or eubacteria)"), EUKARYOTA('E',
			"eukaryota (=eukarya)"), VIRUS('V', "viruses and phages (=viridae)");
	private final char code;
	private final String name;

	private UniprotKingdom(char code, String name) {
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public char getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public static UniprotKingdom getByCodeChar(char codeChar) {
		final UniprotKingdom[] values = UniprotKingdom.values();
		for (UniprotKingdom uniprotKingdom : values) {
			if (uniprotKingdom.code == codeChar) {
				return uniprotKingdom;
			}
		}
		return null;
	}

	public static UniprotKingdom getByName(String name) {
		final UniprotKingdom[] values = UniprotKingdom.values();
		for (UniprotKingdom uniprotKingdom : values) {
			if (uniprotKingdom.name.equals(name)) {
				return uniprotKingdom;
			}
		}
		return null;
	}
}
