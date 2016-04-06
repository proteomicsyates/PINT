package edu.scripps.yates.utilities.ipi;

public class UniprotEntry {
	public static enum UNIPROT_TYPE {
		SWISSPROT, TREMBL, NA
	}

	private final String acc;
	private final UNIPROT_TYPE type;
	private final String name;

	public UniprotEntry(String acc, UNIPROT_TYPE type, String name) {
		this.acc = acc;
		this.type = type;
		this.name = name;
	}

	/**
	 * @return the acc
	 */
	public String getAcc() {
		return acc;
	}

	/**
	 * @return the type
	 */
	public UNIPROT_TYPE getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
