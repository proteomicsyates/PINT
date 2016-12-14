package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class TissueBean implements Serializable, HasId {
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8230799076830462784L;
	private String tissueID;
	private String description;

	public TissueBean() {

	}

	public void setTissueID(String id) {
		tissueID = id;
	}

	/**
	 * @return the tissueID
	 */

	public String getTissueID() {
		return tissueID;
	}

	/**
	 * @return the name
	 */

	@Override
	public String getId() {
		return tissueID;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		return "TissueEx [tissueID=" + tissueID + ", description="
				+ description + "]";
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TissueBean) {
			TissueBean tissue = (TissueBean) obj;
			if (tissue.getTissueID().equals(tissueID))
				return true;
			return false;
		}
		return super.equals(obj);
	}

}
