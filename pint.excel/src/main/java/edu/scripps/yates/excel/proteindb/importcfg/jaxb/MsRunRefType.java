//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.11 at 03:44:17 PM PST 
//


package edu.scripps.yates.excel.proteindb.importcfg.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for msRunRefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="msRunRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="msRunRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msRunRefType")
public class MsRunRefType {

    @XmlAttribute(name = "msRunRef", required = true)
    protected String msRunRef;

    /**
     * Gets the value of the msRunRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsRunRef() {
        return msRunRef;
    }

    /**
     * Sets the value of the msRunRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsRunRef(String value) {
        this.msRunRef = value;
    }

}
