package edu.scripps.yates.shared.model;

public enum AmountType {

	INTENSITY("Peak instensity"), //
	NORMALIZED_INTENSITY("Normalized intensity peak"), //
	AREA("Area under the curve"), //
	XIC("Xtracted Ion Chromatogram"), //
	SPC("Spectral count"), //
	NSAF("NSAF value"), //
	dNSAF("dNSAF value"), //
	EMPAI("EMPAI value"), //
	EMPAI_COV("EMPAI cov"), //
	REGRESSION_FACTOR("Regression score");
	// FOR DEVELOPERS
	// If you add a new value in this enum, add it also to the latest schemma
	// 'import_schema_0.6.xsd' in excel module:
	private final String description;

	private AmountType(String description) {
		this.description = description;
	}

	public static AmountType fromValue(String name) {
		for (AmountType amountType : AmountType.values()) {
			if (amountType.name().equalsIgnoreCase(name)) {
				return amountType;
			}
		}
		return null;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}
