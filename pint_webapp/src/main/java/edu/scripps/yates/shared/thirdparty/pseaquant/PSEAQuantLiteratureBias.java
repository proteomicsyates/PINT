package edu.scripps.yates.shared.thirdparty.pseaquant;

public enum PSEAQuantLiteratureBias {

	BIAS("bias"), NO_BIAS("nobias");
	private final String literatureBias;

	private PSEAQuantLiteratureBias(String liter) {
		literatureBias = liter;
	}

	/**
	 * @return the cvTol
	 */
	public String getLiteratureBias() {
		return literatureBias;
	}

	public String getParameterName() {
		return "nullmodel";
	}
}
