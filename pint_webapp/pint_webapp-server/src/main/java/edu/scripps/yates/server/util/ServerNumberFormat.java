package edu.scripps.yates.server.util;

import java.text.DecimalFormat;

import edu.scripps.yates.shared.util.SharedFormater;

public class ServerNumberFormat implements SharedFormater {
	private final DecimalFormat formater;

	public ServerNumberFormat(String pattern) {
		formater = new DecimalFormat(pattern);
	}

	public static ServerNumberFormat getFormat(String pattern) {
		return new ServerNumberFormat(pattern);
	}

	public static ServerNumberFormat getScientificFormat(int minNumFractionDigits, int maxNumFractionDigits) {
		if (maxNumFractionDigits <= 0) {
			return new ServerNumberFormat("#");
		}
		String decimals = getRepeatedString("0", maxNumFractionDigits);

		return new ServerNumberFormat("0." + decimals + "E0");

	}

	private static String getRepeatedString(String string, int numDecimals) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numDecimals; i++) {
			sb.append(string);
		}
		return sb.toString();
	}

	public String format(double number) {
		return formater.format(number);
	}

	@Override
	public String formatDouble(Double number) {
		if (number < 0.01) {
			return getScientificFormat(2, 3).format(number);
		}
		return format(number);
	}

	@Override
	public String format(Double number, String pattern) {
		return new DecimalFormat(pattern).format(number);
	}

	@Override
	public String formatScientific(Double value, int minNumDecimals, int maxNumDecimals) {
		return getScientificFormat(minNumDecimals, maxNumDecimals).format(value);
	}

}
