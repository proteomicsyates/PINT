package edu.scripps.yates.shared.model;

import java.io.Serializable;

import edu.scripps.yates.shared.util.NumberFormat;

public class ScoreBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4774612233108800677L;
	private String value;
	private String scoreType;
	private String description;
	private String scoreName;

	public ScoreBean(String value, String scoreName, String scoreType, String description) {
		this.value = value;
		this.scoreType = scoreType;
		this.description = description;
		this.scoreName = scoreName;

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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param string
	 *            the value to set
	 */
	public void setValue(String string) {
		value = string;
	}

	/**
	 * @param scoreType
	 *            the scoreType to set
	 */
	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	/**
	 * @param scoreName
	 *            the scoreName to set
	 */
	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	public ScoreBean() {

	}

	public String getValue() {
		return value;
	}

	public String getScoreName() {
		return scoreName;
	}

	public String getScoreDescription() {
		return description;
	}

	public String getScoreType() {
		return scoreType;
	}

	/**
	 * If the value is a double, it is parsed with 3 decimals. Otherwise, the
	 * value is returned as string
	 *
	 * @return
	 */
	public String getParsedValue() {
		try {
			double doubleValue = Double.valueOf(value);
			final NumberFormat format = NumberFormat.getFormat("#.###");
			return format.format(doubleValue);
		} catch (NumberFormatException e) {
			return value;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ScoreBean [scoreName=" + scoreName + ", value=" + value + "]";
	}

}
