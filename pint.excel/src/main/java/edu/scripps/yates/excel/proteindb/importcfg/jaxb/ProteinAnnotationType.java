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
 * <p>Java class for protein_annotationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="protein_annotationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="binary" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="yes_value" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="columnRef" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "protein_annotationType")
public class ProteinAnnotationType {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "binary", required = true)
    protected boolean binary;
    @XmlAttribute(name = "yes_value", required = true)
    protected String yesValue;
    @XmlAttribute(name = "columnRef", required = true)
    protected String columnRef;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the binary property.
     * 
     */
    public boolean isBinary() {
        return binary;
    }

    /**
     * Sets the value of the binary property.
     * 
     */
    public void setBinary(boolean value) {
        this.binary = value;
    }

    /**
     * Gets the value of the yesValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYesValue() {
        return yesValue;
    }

    /**
     * Sets the value of the yesValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYesValue(String value) {
        this.yesValue = value;
    }

    /**
     * Gets the value of the columnRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getColumnRef() {
        return columnRef;
    }

    /**
     * Sets the value of the columnRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColumnRef(String value) {
        this.columnRef = value;
    }

}
