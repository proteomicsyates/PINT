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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for msRunType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="msRunType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="path" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="date" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="fastaFileRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enzymeResidues" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enzymeNocutResidues" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msRunType")
public class MsRunType {

    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "path", required = true)
    protected String path;
    @XmlAttribute(name = "date")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    @XmlAttribute(name = "fastaFileRef")
    protected String fastaFileRef;
    @XmlAttribute(name = "enzymeResidues")
    protected String enzymeResidues;
    @XmlAttribute(name = "enzymeNocutResidues")
    protected String enzymeNocutResidues;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the path property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the value of the path property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPath(String value) {
        this.path = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the fastaFileRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFastaFileRef() {
        return fastaFileRef;
    }

    /**
     * Sets the value of the fastaFileRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFastaFileRef(String value) {
        this.fastaFileRef = value;
    }

    /**
     * Gets the value of the enzymeResidues property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnzymeResidues() {
        return enzymeResidues;
    }

    /**
     * Sets the value of the enzymeResidues property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnzymeResidues(String value) {
        this.enzymeResidues = value;
    }

    /**
     * Gets the value of the enzymeNocutResidues property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnzymeNocutResidues() {
        return enzymeNocutResidues;
    }

    /**
     * Sets the value of the enzymeNocutResidues property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnzymeNocutResidues(String value) {
        this.enzymeNocutResidues = value;
    }

}
