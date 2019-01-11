package edu.scripps.yates.shared.util;

import java.io.Serializable;

public class FileDescriptor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1916604121562867648L;
	private String name;
	private String size;

	public FileDescriptor() {

	}

	public FileDescriptor(String name, String sizeString) {
		super();
		this.name = name;
		size = sizeString;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

}
