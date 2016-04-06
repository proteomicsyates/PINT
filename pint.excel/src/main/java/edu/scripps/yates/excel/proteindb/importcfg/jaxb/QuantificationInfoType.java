//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.18 at 09:46:51 AM PST 
//


package edu.scripps.yates.excel.proteindb.importcfg.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for quantification_InfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="quantification_InfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="excel_quant_info" type="{}quantification_excelType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="remoteFiles_quant_info" type="{}remote_InfoType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "quantification_InfoType", propOrder = {
    "excelQuantInfo",
    "remoteFilesQuantInfo"
})
public class QuantificationInfoType {

    @XmlElement(name = "excel_quant_info")
    protected List<QuantificationExcelType> excelQuantInfo;
    @XmlElement(name = "remoteFiles_quant_info")
    protected List<RemoteInfoType> remoteFilesQuantInfo;

    /**
     * Gets the value of the excelQuantInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the excelQuantInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExcelQuantInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QuantificationExcelType }
     * 
     * 
     */
    public List<QuantificationExcelType> getExcelQuantInfo() {
        if (excelQuantInfo == null) {
            excelQuantInfo = new ArrayList<QuantificationExcelType>();
        }
        return this.excelQuantInfo;
    }

    /**
     * Gets the value of the remoteFilesQuantInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the remoteFilesQuantInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRemoteFilesQuantInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RemoteInfoType }
     * 
     * 
     */
    public List<RemoteInfoType> getRemoteFilesQuantInfo() {
        if (remoteFilesQuantInfo == null) {
            remoteFilesQuantInfo = new ArrayList<RemoteInfoType>();
        }
        return this.remoteFilesQuantInfo;
    }

}
