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

public enum AmountCombinationTypeBean implements Serializable{

	NO_COMBINATION, AVERAGE, SUM;

	public String value() {
		return name();
	}

	public static AmountCombinationTypeBean fromValue(String v) {
		return valueOf(v);
	}

}