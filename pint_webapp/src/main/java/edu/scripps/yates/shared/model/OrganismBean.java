package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class OrganismBean extends HasId implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -7168070517312701585L;
	private String ncbiTaxID;
	private String name;

	public OrganismBean() {
	}

	/**
	 * @return the ncbiTaxID
	 */
	public String getNcbiTaxID() {
		return ncbiTaxID;
	}

	/**
	 * @param ncbiTaxID
	 *            the ncbiTaxID to set
	 */
	public void setNcbiTaxID(String ncbiTaxID) {
		this.ncbiTaxID = ncbiTaxID;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the name
	 */

	@Override
	public String getId() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return name + " [" + ncbiTaxID + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrganismBean) {
			OrganismBean organism = (OrganismBean) obj;
			if (organism.getNcbiTaxID().equals(ncbiTaxID))
				return true;
			return false;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

}
