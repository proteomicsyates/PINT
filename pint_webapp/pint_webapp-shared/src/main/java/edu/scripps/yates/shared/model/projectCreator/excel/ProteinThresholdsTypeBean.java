//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-7
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2015.10.19 at 02:27:17 PM PDT
//

package edu.scripps.yates.shared.model.projectCreator.excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProteinThresholdsTypeBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2456114443646427731L;
	protected List<ProteinThresholdTypeBean> proteinThreshold;

	/**
	 * Gets the value of the proteinThreshold property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the proteinThreshold property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getProteinThreshold().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ProteinThresholdTypeBean }
	 * 
	 * 
	 */
	public List<ProteinThresholdTypeBean> getProteinThreshold() {
		if (proteinThreshold == null) {
			proteinThreshold = new ArrayList<ProteinThresholdTypeBean>();
		}
		return proteinThreshold;
	}

}
