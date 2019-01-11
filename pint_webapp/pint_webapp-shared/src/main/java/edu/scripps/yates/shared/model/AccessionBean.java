package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccessionBean implements Serializable, Comparable<AccessionBean> {

	private static final long serialVersionUID = 285204540497978045L;
	private AccessionType accessionType;
	private String accession;
	private String description;
	private boolean isPrimaryAccession;
	private List<String> alternativeNames = new ArrayList<String>();

	public AccessionBean() {

	}

	/**
	 * @param isPrimaryAccession
	 *            the isPrimaryAccession to set
	 */
	public void setPrimaryAccession(boolean isPrimaryAccession) {
		this.isPrimaryAccession = isPrimaryAccession;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public AccessionType getAccessionType() {
		return accessionType;
	}

	public String getAccession() {
		return accession;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPrimaryAccession() {
		return isPrimaryAccession;
	}

	public boolean equals(AccessionBean accession) {
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
		if (obj instanceof AccessionBean)
			return this.equals((AccessionBean) obj);
		return super.equals(obj);
	}

	/*
	 * In order to get called the equals method when containsKey is called in a
	 * HashMap where the keys are AccessionEx (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */

	@Override
	public int hashCode() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		String isPrimary = isPrimaryAccession() ? "*" : "";
		return isPrimary + getAccession();
	}

	public List<String> getAlternativeNames() {
		return alternativeNames;

	}

	public void setAlternativeNames(List<String> secNames) {
		alternativeNames = secNames;
	}

	public void addAlternativeName(String secName) {
		alternativeNames.add(secName);
	}

	/**
	 * @param accessionType
	 *            the accessionType to set
	 */
	public void setAccessionType(AccessionType accessionType) {
		this.accessionType = accessionType;
	}

	/**
	 * @param accession
	 *            the accession to set
	 */
	public void setAccession(String accession) {
		this.accession = accession;
	}

	@Override
	public int compareTo(AccessionBean o) {
		if (o != null) {
			return accession.compareTo(o.getAccession());
		}
		return 0;
	}

}
