package edu.scripps.yates.utilities.model.enums;

public enum AmountType {

	INTENSITY, NORMALIZED_INTENSITY, AREA, XIC, SPC, NSAF, dNSAF, EMPAI, EMPAI_COV, OTHER, REGRESSION_FACTOR;

	public static AmountType translateStringToAmountType(String amountTypeString) {

		final AmountType[] values = AmountType.values();
		for (AmountType amountType : values) {
			if (amountType.name().equalsIgnoreCase(amountTypeString))
				return amountType;
		}
		return null;

	}

	public static String getValuesString() {
		StringBuilder sb = new StringBuilder();
		final AmountType[] values = AmountType.values();
		for (AmountType amountType : values) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(amountType.name());
		}
		return sb.toString();
	}

}
