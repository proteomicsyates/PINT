//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.18 at 09:46:51 AM PST 
//


package edu.scripps.yates.excel.proteindb.importcfg.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for formatType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="formatType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="excel"/>
 *     &lt;enumeration value="census_out_txt"/>
 *     &lt;enumeration value="census_chro_xml"/>
 *     &lt;enumeration value="dta_select_filter_txt"/>
 *     &lt;enumeration value="fasta"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "formatType")
@XmlEnum
public enum FormatType {

    @XmlEnumValue("excel")
    EXCEL("excel"),
    @XmlEnumValue("census_out_txt")
    CENSUS_OUT_TXT("census_out_txt"),
    @XmlEnumValue("census_chro_xml")
    CENSUS_CHRO_XML("census_chro_xml"),
    @XmlEnumValue("dta_select_filter_txt")
    DTA_SELECT_FILTER_TXT("dta_select_filter_txt"),
    @XmlEnumValue("fasta")
    FASTA("fasta");
    private final String value;

    FormatType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FormatType fromValue(String v) {
        for (FormatType c: FormatType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
