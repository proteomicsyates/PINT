package edu.scripps.yates.shared.thirdparty.pseaquant;

public enum PSEAQuantCVTol {

	IND("Ind"), CV("CV");
	private final String cvTol;

	private PSEAQuantCVTol(String quantTol) {
		cvTol = quantTol;
	}

	/**
	 * @return the cvTol
	 */
	public String getCvTol() {
		return cvTol;
	}

	public String getParameterName() {
		return "CVTol";
	}
}
