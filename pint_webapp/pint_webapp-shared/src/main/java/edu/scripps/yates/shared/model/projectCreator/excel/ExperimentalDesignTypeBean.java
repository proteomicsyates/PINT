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

public class ExperimentalDesignTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1712319218454924210L;
	protected SampleSetTypeBean sampleSet;
	protected OrganismSetTypeBean organismSet;
	protected TissueSetTypeBean tissueSet;
	protected LabelSetTypeBean labelSet;

	/**
	 * Gets the value of the sampleSet property.
	 * 
	 * @return possible object is {@link SampleSetTypeBean }
	 * 
	 */
	public SampleSetTypeBean getSampleSet() {
		return sampleSet;
	}

	/**
	 * Sets the value of the sampleSet property.
	 * 
	 * @param value
	 *            allowed object is {@link SampleSetTypeBean }
	 * 
	 */
	public void setSampleSet(SampleSetTypeBean value) {
		sampleSet = value;
	}

	/**
	 * Gets the value of the organismSet property.
	 * 
	 * @return possible object is {@link OrganismSetTypeBean }
	 * 
	 */
	public OrganismSetTypeBean getOrganismSet() {
		return organismSet;
	}

	/**
	 * Sets the value of the organismSet property.
	 * 
	 * @param value
	 *            allowed object is {@link OrganismSetTypeBean }
	 * 
	 */
	public void setOrganismSet(OrganismSetTypeBean value) {
		organismSet = value;
	}

	/**
	 * Gets the value of the tissueSet property.
	 * 
	 * @return possible object is {@link TissueSetTypeBean }
	 * 
	 */
	public TissueSetTypeBean getTissueSet() {
		return tissueSet;
	}

	/**
	 * Sets the value of the tissueSet property.
	 * 
	 * @param value
	 *            allowed object is {@link TissueSetTypeBean }
	 * 
	 */
	public void setTissueSet(TissueSetTypeBean value) {
		tissueSet = value;
	}

	/**
	 * Gets the value of the labelSet property.
	 * 
	 * @return possible object is {@link LabelSetTypeBean }
	 * 
	 */
	public LabelSetTypeBean getLabelSet() {
		return labelSet;
	}

	/**
	 * Sets the value of the labelSet property.
	 * 
	 * @param value
	 *            allowed object is {@link LabelSetTypeBean }
	 * 
	 */
	public void setLabelSet(LabelSetTypeBean value) {
		labelSet = value;
	}

}
