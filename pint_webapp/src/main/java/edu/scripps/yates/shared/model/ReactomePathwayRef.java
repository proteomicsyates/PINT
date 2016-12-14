package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class ReactomePathwayRef implements Serializable, Comparable<ReactomePathwayRef> {
	/**
		 *
		 */
	private static final long serialVersionUID = 988223103216017413L;
	private String id;
	private String description;

	public ReactomePathwayRef() {
	}

	public ReactomePathwayRef(String id, String description) {
		this.id = id;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int compareTo(ReactomePathwayRef o) {
		if (o != null) {
			return getDescription().compareTo(o.getDescription());
		}
		return 0;
	}

}
