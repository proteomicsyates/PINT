package edu.scripps.yates.utilities.taxonomy;

public class UniprotOrganism {

	private String commonName = null;
	private final String scientificName;
	private final String code;
	private String synonim = null;
	private final long taxonCode;
	private final UniprotKingdom kingdom;

	public UniprotOrganism(String code, UniprotKingdom kingdom, long taxonCode,
			String scientificName) {
		this.code = code;
		this.kingdom = kingdom;
		this.taxonCode = taxonCode;
		this.scientificName = scientificName;
	}

	/**
	 * @return the commonName
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the synonim
	 */
	public String getSynonim() {
		return synonim;
	}

	/**
	 * @return the taxonCode
	 */
	public long getTaxonCode() {
		return taxonCode;
	}

	/**
	 * @return the kingdom
	 */
	public UniprotKingdom getKingdom() {
		return kingdom;
	}

	/**
	 * @param commonName
	 *            the commonName to set
	 */
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	/**
	 * @param synonim
	 *            the synonim to set
	 */
	public void setSynonim(String synonim) {
		this.synonim = synonim;
	}
}
