package edu.scripps.yates.shared.model.projectCreator.excel;

import java.io.Serializable;

public class PsmTypeBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6549630095190025544L;
	private String columnRef;

	/**
	 * @return the columnRef
	 */
	public String getColumnRef() {
		return columnRef;
	}

	/**
	 * @param columnRef
	 *            the columnRef to set
	 */
	public void setColumnRef(String columnRef) {
		this.columnRef = columnRef;
	}
}
