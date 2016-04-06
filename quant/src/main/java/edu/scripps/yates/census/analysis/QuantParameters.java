package edu.scripps.yates.census.analysis;

public class QuantParameters {
	private boolean performCalibration = true;
	private boolean performRemoveOutliers = true;
	private double fdr = 0.01;

	public QuantParameters(boolean calibration, boolean outlierRemoval,
			double fdr) {
		performCalibration = calibration;
		performRemoveOutliers = outlierRemoval;
		this.fdr = fdr;
	}

	public QuantParameters() {

	}

	/**
	 * @return the performCalibration
	 */
	public boolean isPerformCalibration() {
		return performCalibration;
	}

	/**
	 * @return the performRemoveOutliers
	 */
	public boolean isPerformRemoveOutliers() {
		return performRemoveOutliers;
	}

	/**
	 * @return the fdr
	 */
	public double getOutlierRemovalFdr() {
		return fdr;
	}

	/**
	 * @param performCalibration
	 *            the performCalibration to set
	 */
	public void setPerformCalibration(boolean performCalibration) {
		this.performCalibration = performCalibration;
	}

	/**
	 * @param performRemoveOutliers
	 *            the performRemoveOutliers to set
	 */
	public void setPerformRemoveOutliers(boolean performRemoveOutliers) {
		this.performRemoveOutliers = performRemoveOutliers;
	}

	/**
	 * @param fdr
	 *            the fdr to set
	 */
	public void setFdr(double fdr) {
		this.fdr = fdr;
	}
}
