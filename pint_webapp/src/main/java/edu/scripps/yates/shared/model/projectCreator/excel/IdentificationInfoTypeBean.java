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

public class IdentificationInfoTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4194694519621605138L;
	protected List<IdentificationExcelTypeBean> excelIdentInfo;
	protected List<RemoteInfoTypeBean> remoteFilesIdentInfo;

	/**
	 * Gets the value of the excelIdentInfo property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the excelIdentInfo property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getExcelIdentInfo().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link IdentificationExcelTypeBean }
	 * 
	 * 
	 */
	public List<IdentificationExcelTypeBean> getExcelIdentInfo() {
		if (excelIdentInfo == null) {
			excelIdentInfo = new ArrayList<IdentificationExcelTypeBean>();
		}
		return excelIdentInfo;
	}

	/**
	 * Gets the value of the remoteFilesIdentInfo property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the remoteFilesIdentInfo property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRemoteFilesIdentInfo().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link RemoteInfoTypeBean }
	 * 
	 * 
	 */
	public List<RemoteInfoTypeBean> getRemoteFilesIdentInfo() {
		if (remoteFilesIdentInfo == null) {
			remoteFilesIdentInfo = new ArrayList<RemoteInfoTypeBean>();
		}
		return remoteFilesIdentInfo;
	}

}
