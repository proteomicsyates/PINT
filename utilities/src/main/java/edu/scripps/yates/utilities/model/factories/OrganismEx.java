package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;

import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;

public class OrganismEx implements Organism, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7168070517312701585L;
	private final String ncbiTaxID;
	private String name;

	public OrganismEx(String ncbiTaxID) {
		if (ncbiTaxID == null || "".equals(ncbiTaxID))
			throw new IllegalArgumentException("ncbitax id cannot be null");
		final UniprotOrganism uniprotOrganism = UniprotSpeciesCodeMap
				.getInstance().get(ncbiTaxID);
		if (uniprotOrganism != null) {
			this.ncbiTaxID = String.valueOf(uniprotOrganism.getTaxonCode());
			name = uniprotOrganism.getScientificName();
		} else {
			this.ncbiTaxID = ncbiTaxID;
			name = ncbiTaxID;
		}
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ncbiTaxID
	 */
	@Override
	public String getOrganismID() {
		return ncbiTaxID;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrganismEx [ncbiTaxID=" + ncbiTaxID + ", name=" + name + "]";
	}

}
