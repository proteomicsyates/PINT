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

public class PintImportCfgTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -541562739417779135L;
	protected FileSetTypeBean fileSet;
	protected ProjectTypeBean project;
	protected ServersTypeBean servers;
	protected String version;
	protected int importID;

	/**
	 * @return the importID
	 */
	public int getImportID() {
		return importID;
	}

	/**
	 * @param importID
	 *            the importID to set
	 */
	public void setImportID(int importID) {
		this.importID = importID;
	}

	/**
	 * Gets the value of the fileSet property.
	 * 
	 * @return possible object is {@link FileSetTypeBean }
	 * 
	 */
	public FileSetTypeBean getFileSet() {
		return fileSet;
	}

	/**
	 * Sets the value of the fileSet property.
	 * 
	 * @param value
	 *            allowed object is {@link FileSetTypeBean }
	 * 
	 */
	public void setFileSet(FileSetTypeBean value) {
		fileSet = value;
	}

	/**
	 * Gets the value of the project property.
	 * 
	 * @return possible object is {@link ProjectTypeBean }
	 * 
	 */
	public ProjectTypeBean getProject() {
		return project;
	}

	/**
	 * Sets the value of the project property.
	 * 
	 * @param value
	 *            allowed object is {@link ProjectTypeBean }
	 * 
	 */
	public void setProject(ProjectTypeBean value) {
		project = value;
	}

	/**
	 * Gets the value of the servers property.
	 * 
	 * @return possible object is {@link ServersTypeBean }
	 * 
	 */
	public ServersTypeBean getServers() {
		return servers;
	}

	/**
	 * Sets the value of the servers property.
	 * 
	 * @param value
	 *            allowed object is {@link ServersTypeBean }
	 * 
	 */
	public void setServers(ServersTypeBean value) {
		servers = value;
	}

	/**
	 * Gets the value of the version property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setVersion(String value) {
		version = value;
	}

}