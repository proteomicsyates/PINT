package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class UniprotFeatureBean implements Serializable, Comparable<UniprotFeatureBean> {
	/**
	 *
	 */
	private static final long serialVersionUID = 3649977138678893323L;
	private int positionStart = -1;
	private int positionEnd = -1;
	private String description;
	private String status;
	private String ref;
	private String original;
	private String featureType;

	public UniprotFeatureBean() {

	}

	public UniprotFeatureBean(String description, int positionStart, int positionEnd, String status) {
		this.description = description;
		this.positionStart = positionStart;
		this.positionEnd = positionEnd;
		this.status = status;
	}

	public UniprotFeatureBean(String description, int position, String status) {
		this(description, position, position, status);
	}

	/**
	 * @return the positionStart
	 */
	public int getPositionStart() {
		return positionStart;
	}

	/**
	 * @param positionStart
	 *            the positionStart to set
	 */
	public void setPositionStart(int positionStart) {
		this.positionStart = positionStart;
	}

	/**
	 * @return the positionEnd
	 */
	public int getPositionEnd() {
		return positionEnd;
	}

	/**
	 * @param positionEnd
	 *            the positionEnd to set
	 */
	public void setPositionEnd(int positionEnd) {
		this.positionEnd = positionEnd;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return positionEnd - positionStart + 1;
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

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public void setRef(String ref) {
		this.ref = ref;

	}

	/**
	 * @return the ref
	 */
	public String getRef() {
		return ref;
	}

	public void setOriginal(String original) {
		this.original = original;

	}

	/**
	 * @return the original
	 */
	public String getOriginal() {
		return original;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return -1239713;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UniprotFeatureBean) {
			return toString().equals(obj.toString());
		}
		return super.equals(obj);

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UniprotFeature [start=" + positionStart + ", End=" + positionEnd + ", description=" + description
				+ ", status=" + status + ", ref=" + ref + ", original=" + original + ", featureType=" + featureType
				+ "]";
	}

	@Override
	public int compareTo(UniprotFeatureBean o2) {
		if (o2 == null) {
			return 1;
		}
		final int compareStart = Integer.compare(getPositionStart(), o2.getPositionStart());
		if (compareStart != 0) {
			return compareStart;
		}
		final int compareLength = Integer.compare(getLength(), o2.getLength());
		if (compareLength != 0) {
			return compareLength;
		}
		if (getDescription() != null) {
			return getDescription().compareTo(o2.getDescription());
		}
		if (getFeatureType() != null) {
			return getFeatureType().compareTo(o2.getFeatureType());
		}
		return 0;
	}

}
