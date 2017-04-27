package edu.scripps.yates.shared.util;

import java.text.DecimalFormat;

class NumberFormatServer extends NumberFormat {
	private final DecimalFormat formater;

	protected NumberFormatServer(String pattern) {
		formater = new DecimalFormat(pattern);
	}

	public static NumberFormat getFormat(String pattern) {
		return new NumberFormatServer(pattern);
	}

	public static NumberFormat getScientificFormat() {
		return new NumberFormatServer("0.0E0");
	}

	@Override
	public String format(double number) {
		return formater.format(number);
	}

}
