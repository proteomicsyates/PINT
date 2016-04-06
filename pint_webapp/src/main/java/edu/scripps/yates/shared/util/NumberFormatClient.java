package edu.scripps.yates.shared.util;

import com.google.gwt.thirdparty.guava.common.annotations.GwtCompatible;

@GwtCompatible
class NumberFormatClient extends NumberFormat {
	private final com.google.gwt.i18n.client.NumberFormat formater;

	protected NumberFormatClient(String pattern) {
		formater = com.google.gwt.i18n.client.NumberFormat.getFormat(pattern);
	}

	public static NumberFormat getFormat(String pattern) {
		return new NumberFormatClient(pattern);
	}

	@Override
	public String format(double number) {
		return formater.format(number);
	}
}