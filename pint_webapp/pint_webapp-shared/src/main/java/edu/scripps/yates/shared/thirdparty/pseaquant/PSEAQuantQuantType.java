package edu.scripps.yates.shared.thirdparty.pseaquant;

public enum PSEAQuantQuantType {
	FREE("free", "Absolute Quantification (e.g. spectral count, non-log transformed intensity values)"), BASED("based",
			"Abundance ratio"), BASED_PLUS_INVERSE("based_Inv", "Abundance ratios (+ inverse ratios)");
	private final String quantType;
	private final String text;

	private PSEAQuantQuantType(String organismName, String text) {
		quantType = organismName;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	/**
	 * @return the quant type
	 */
	public String getQuanttype() {
		return quantType;
	}

	public static String getParameterName() {
		return "Quanttype";
	}

	public static PSEAQuantQuantType getByQuantType(String quantTypeName) {
		for (PSEAQuantQuantType quantType : PSEAQuantQuantType.values()) {
			if (quantType.getQuanttype().equals(quantTypeName)) {
				return quantType;
			}
		}
		return null;
	}

}
