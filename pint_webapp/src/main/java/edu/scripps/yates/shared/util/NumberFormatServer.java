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

	public static NumberFormat getScientificFormat(int minNumFractionDigits, int maxNumFractionDigits) {
		if (maxNumFractionDigits <= 0) {
			return new NumberFormatServer("#");
		}
		String decimals = getRepeatedString("0", maxNumFractionDigits);

		return new NumberFormatServer("0." + decimals + "E0");

	}

	private static String getRepeatedString(String string, int numDecimals) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numDecimals; i++) {
			sb.append(string);
		}
		return sb.toString();
	}

	@Override
	public String format(double number) {
		return formater.format(number);
	}

}
