package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;

public class AccessionEx implements Accession, Serializable {

	private static final long serialVersionUID = 285204540497978045L;
	private AccessionType accessionType;
	private String accession;
	private String description;
	private List<String> alternativeNames;

	public AccessionEx() {

	}

	public AccessionEx(String accession, AccessionType accessionType) {
		this.accession = accession;
		this.accessionType = accessionType;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public AccessionType getAccessionType() {
		return accessionType;
	}

	@Override
	public String getAccession() {
		return accession;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean equals(Accession accession) {
		if (accession.getAccessionType().equals(getAccessionType()))
			if (accession.getAccession().equalsIgnoreCase(getAccession()))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Accession)
			return this.equals((Accession) obj);
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAccession();
	}

	/**
	 * @return the alternativeNames
	 */
	@Override
	public List<String> getAlternativeNames() {
		return alternativeNames;
	}

	/**
	 * @param alternativeNames
	 *            the alternativeNames to set
	 */
	public void setAlternativeNames(List<String> alternativeNames) {
		this.alternativeNames = alternativeNames;
	}

	public void addAlternativeName(String alternativeName) {
		if (alternativeNames == null)
			alternativeNames = new ArrayList<String>();
		alternativeNames.add(alternativeName);
	}
}
