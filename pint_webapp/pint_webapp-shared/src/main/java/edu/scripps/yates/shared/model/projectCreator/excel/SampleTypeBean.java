//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-7
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2014.12.03 at 06:58:04 PM PST
//

package edu.scripps.yates.shared.model.projectCreator.excel;

import java.io.Serializable;

import edu.scripps.yates.shared.model.interfaces.HasId;

public class SampleTypeBean extends HasId implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7298735453756295696L;
	protected String description;
	protected String id;
	protected String organismRef;
	protected String tissueRef;
	protected String labelRef;
	protected Boolean wt;

	/**
	 * Gets the value of the description property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the value of the description property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setDescription(String value) {
		description = value;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setId(String value) {
		id = value;
	}

	/**
	 * Gets the value of the organismRef property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getOrganismRef() {
		return organismRef;
	}

	/**
	 * Sets the value of the organismRef property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setOrganismRef(String value) {
		organismRef = value;
	}

	/**
	 * Gets the value of the tissueRef property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getTissueRef() {
		return tissueRef;
	}

	/**
	 * Sets the value of the tissueRef property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setTissueRef(String value) {
		tissueRef = value;
	}

	/**
	 * Gets the value of the labelRef property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getLabelRef() {
		return labelRef;
	}

	/**
	 * Sets the value of the labelRef property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setLabelRef(String value) {
		labelRef = value;
	}

	/**
	 * Gets the value of the wt property.
	 *
	 * @return possible object is {@link Boolean }
	 *
	 */
	public Boolean isWt() {
		return wt;
	}

	/**
	 * Sets the value of the wt property.
	 *
	 * @param value
	 *            allowed object is {@link Boolean }
	 *
	 */
	public void setWt(Boolean value) {
		wt = value;
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
		if (obj instanceof SampleTypeBean) {
			SampleTypeBean sample = (SampleTypeBean) obj;
			if (sample.getId().equals(id))
				return true;
		}
		return super.equals(obj);
	}
}
