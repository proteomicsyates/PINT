//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.18 at 09:46:51 AM PST 
//


package edu.scripps.yates.excel.proteindb.importcfg.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ptm_scoreType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ptm_scoreType">
 *   &lt;complexContent>
 *     &lt;extension base="{}scoreType">
 *       &lt;attribute name="modification_name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ptm_scoreType")
public class PtmScoreType
    extends ScoreType
{

    @XmlAttribute(name = "modification_name", required = true)
    protected String modificationName;

    /**
     * Gets the value of the modificationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModificationName() {
        return modificationName;
    }

    /**
     * Sets the value of the modificationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModificationName(String value) {
        this.modificationName = value;
    }

}
