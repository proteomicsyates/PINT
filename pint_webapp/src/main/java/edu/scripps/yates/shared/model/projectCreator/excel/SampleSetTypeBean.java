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
import java.util.ArrayList;
import java.util.List;

public class SampleSetTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8025118240145999995L;
	protected List<SampleTypeBean> sample;

	/**
	 * Gets the value of the sample property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the sample property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSample().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link SampleTypeBean }
	 * 
	 * 
	 */
	public List<SampleTypeBean> getSample() {
		if (sample == null) {
			sample = new ArrayList<SampleTypeBean>();
		}
		return sample;
	}

}
