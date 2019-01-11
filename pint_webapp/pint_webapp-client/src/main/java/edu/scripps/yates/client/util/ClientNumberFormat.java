package edu.scripps.yates.client.util;

import com.google.gwt.i18n.client.NumberFormat;

import edu.scripps.yates.shared.util.SharedFormater;

public class ClientNumberFormat implements SharedFormater {
	private final NumberFormat formater;

	public ClientNumberFormat(String pattern) {
		formater = NumberFormat.getFormat(pattern);
	}

	public ClientNumberFormat(NumberFormat formater) {
		this.formater = formater;
	}

	public static ClientNumberFormat getFormat(String pattern) {
		return new ClientNumberFormat(pattern);
	}

	public static ClientNumberFormat getScientificFormat(int minNumFractionDigits, int maxNumFractionDigits) {
		if (maxNumFractionDigits <= 0) {
			return new ClientNumberFormat("#");
		}

		return new ClientNumberFormat(
				NumberFormat.getScientificFormat().overrideFractionDigits(minNumFractionDigits, maxNumFractionDigits));

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
		return NumberFormat.getFormat(pattern).format(number);
	}

	@Override
	public String formatScientific(Double value, int minNumDecimals, int maxNumDecimals) {
		return getScientificFormat(minNumDecimals, maxNumDecimals).format(value);
	}

}
