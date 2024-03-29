//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.11 at 03:44:17 PM PST 
//


package edu.scripps.yates.excel.proteindb.importcfg.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for amountCombinationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="amountCombinationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NO_COMBINATION"/>
 *     &lt;enumeration value="AVERAGE"/>
 *     &lt;enumeration value="SUM"/>
 *     &lt;enumeration value="WEIGHTED_AVERAGE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "amountCombinationType")
@XmlEnum
public enum AmountCombinationType {

    NO_COMBINATION,
    AVERAGE,
    SUM,
    WEIGHTED_AVERAGE;

    public String value() {
        return name();
    }

    public static AmountCombinationType fromValue(String v) {
        return valueOf(v);
    }

}
