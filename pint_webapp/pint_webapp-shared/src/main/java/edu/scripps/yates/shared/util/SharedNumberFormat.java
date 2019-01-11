package edu.scripps.yates.shared.util;

import java.text.DecimalFormat;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.NumberFormat;

public class SharedNumberFormat implements SharedFormater {
	private final String pattern;

	public SharedNumberFormat(String pattern) {
		this.pattern = pattern;
	}

	public String format(Number number) {
		if (GWT.isClient()) {
			return NumberFormat.getFormat(pattern).format(number);
		} else {
			return new DecimalFormat(pattern).format(number.doubleValue());
		}
	}

	@Override
	public String formatDouble(Double number) {
		if (GWT.isClient()) {
			return NumberFormat.getFormat(pattern).format(number);
		} else {
			return new DecimalFormat(pattern).format(number.doubleValue());
		}
	}

	@Override
	public String format(Double number, String pattern) {
		if (GWT.isClient()) {
			return NumberFormat.getFormat(pattern).format(number);
		} else {
			return new DecimalFormat(pattern).format(number.doubleValue());
		}
	}

	@Override
	public String formatScientific(Double number, int minNumDecimals, int maxNumDecimals) {
		if (GWT.isClient()) {
			return NumberFormat.getScientificFormat().overrideFractionDigits(minNumDecimals, maxNumDecimals)
					.format(number);
		} else {
			if (maxNumDecimals <= 0) {
				return new DecimalFormat("#").format(number);
			}
			final String decimals = getRepeatedString("0", maxNumDecimals);
			final String pattern = "0." + decimals + "E0";
			return new DecimalFormat(pattern).format(number);
		}
	}

	private static String getRepeatedString(String string, int numDecimals) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numDecimals; i++) {
			sb.append(string);
		}
		return sb.toString();
	}
}
