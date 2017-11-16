package edu.scripps.yates.shared.util;

import com.google.gwt.thirdparty.guava.common.annotations.GwtCompatible;

@GwtCompatible
class NumberFormatClient extends NumberFormat {
	private final com.google.gwt.i18n.client.NumberFormat formater;

	protected NumberFormatClient(String pattern) {
		formater = com.google.gwt.i18n.client.NumberFormat.getFormat(pattern);
	}

	protected NumberFormatClient(com.google.gwt.i18n.client.NumberFormat formater) {
		this.formater = formater;
	}

	public static edu.scripps.yates.shared.util.NumberFormat getFormat(String pattern) {
		return new NumberFormatClient(pattern);
	}

	public static edu.scripps.yates.shared.util.NumberFormat getScientificFormat(int minNumFractionDigits,
			int maxNumFractionDigits) {

		return new NumberFormatClient(com.google.gwt.i18n.client.NumberFormat.getScientificFormat()
				.overrideFractionDigits(minNumFractionDigits, maxNumFractionDigits));
	}

	@Override
	public String format(double number) {
		return formater.format(number);
	}
}