package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class RatioDistribution implements Serializable {
	/**
		 *
		 */
	private static final long serialVersionUID = 7207314158043164374L;
	private double minRatio;
	private double maxRatio;
	private String ratioKey;

	public RatioDistribution() {

	}

	/**
	 * @return the minRatio
	 */
	public double getMinRatio() {
		return minRatio;
	}

	/**
	 * @return the maxRatio
	 */
	public double getMaxRatio() {
		return maxRatio;
	}

	/**
	 * returns maxRatio - minRatio
	 *
	 * @return
	 */
	public double getWidth() {
		return maxRatio - minRatio;
	}

	/**
	 * @return the ratioKey
	 */
	public String getRatioKey() {
		return ratioKey;
	}

	/**
	 * @param minRatio the minRatio to set
	 */
	public void setMinRatio(double minRatio) {
		this.minRatio = minRatio;
	}

	/**
	 * @param maxRatio the maxRatio to set
	 */
	public void setMaxRatio(double maxRatio) {
		this.maxRatio = maxRatio;
	}

	/**
	 * @param ratioKey the ratioKey to set
	 */
	public void setRatioKey(String ratioKey) {
		this.ratioKey = ratioKey;
	}

	/**
	 * Return the maximum absolute number between the minimum and the maximum
	 *
	 * @return
	 */
	public double getMaxAbsRatio() {
		return Math.max(Math.abs(getMinRatio()), Math.abs(getMaxRatio()));
	}

}
