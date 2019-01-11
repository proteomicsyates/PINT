package edu.scripps.yates.shared.thirdparty.pseaquant;

public enum PSEAQuantSupportedOrganism {

	HOMO_SAPIENS("Human", 9606), MUS_MUSCULUS("Mouse", 10090), RATTUS_NORVEGICUS("Rat",
			10116), DROSOPHILA_MELANOGASTER("Drosophila", 7227), SACCHAROMYCSE_CEREVISIAE("Yeast", 559292);
	private final String organismName;
	private final int ncbiID;

	private PSEAQuantSupportedOrganism(String organismName, int ncbiId) {
		this.organismName = organismName;
		ncbiID = ncbiId;
	}

	/**
	 * @return the organismName
	 */
	public String getOrganismName() {
		return organismName;
	}

	public String getParameterName() {
		return "Organism";
	}

	public static PSEAQuantSupportedOrganism getValueFromOrganismName(String text) {
		for (PSEAQuantSupportedOrganism item : values()) {
			if (item.getOrganismName().equalsIgnoreCase(text))
				return item;
		}
		return null;
	}

	public static PSEAQuantSupportedOrganism getValueFromOrganismName(int ncbiID) {
		for (PSEAQuantSupportedOrganism item : values()) {
			if (item.getNcbiID() == ncbiID)
				return item;
		}
		return null;
	}

	/**
	 * @return the ncbiID
	 */
	public int getNcbiID() {
		return ncbiID;
	}
}
