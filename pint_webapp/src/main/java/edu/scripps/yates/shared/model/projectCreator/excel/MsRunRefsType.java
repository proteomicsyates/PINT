//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, vhudson-jaxb-ri-2.2-7
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2014.12.03 at 06:58:04 PM PST
//

package edu.scripps.yates.shared.model.projectCreator.excel;

import java.util.ArrayList;
import java.util.List;

public class MsRunRefsType {

	protected List<MsRunRefType> msRunRef;

	/**
	 * Gets the value of the msRunRef property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the msRunRef property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getMsRunRef().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link MsRunRefType }
	 * 
	 * 
	 */
	public List<MsRunRefType> getMsRunRef() {
		if (msRunRef == null) {
			msRunRef = new ArrayList<MsRunRefType>();
		}
		return msRunRef;
	}

}
