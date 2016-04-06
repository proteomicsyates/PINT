package edu.scripps.yates.shared.util;


/**
 * The purpose of this class is to allow number formatting on both the client
 * and server side.
 */
public abstract class NumberFormat {

	public static NumberFormat getFormat(String pattern) {
		if (!com.google.gwt.core.shared.GWT.isClient())
			return NumberFormatServer.getFormat(pattern);
		else
			return NumberFormatClient.getFormat(pattern);

	}

	public abstract String format(double number);

	// @GwtCompatible
	// private static class NumberFormatClient extends NumberFormat {
	// private final NumberFormat formater;
	//
	// protected NumberFormatClient(String pattern) {
	// formater = NumberFormat.getFormat(pattern);
	// }
	//
	// public static NumberFormat getFormat(String pattern) {
	// return new NumberFormatClient(pattern);
	// }
	//
	// @Override
	// public String format(double number) {
	// return formater.format(number);
	// }
	// }
	//
	// @GwtIncompatible("Server version of the class")
	// private static class NumberFormatServer extends NumberFormat {
	// private final DecimalFormat formater;
	//
	// protected NumberFormatServer(String pattern) {
	// formater = new DecimalFormat(pattern);
	// }
	//
	// public static NumberFormat getFormat(String pattern) {
	// return new NumberFormatServer(pattern);
	// }
	//
	// @Override
	// public String format(double number) {
	// return formater.format(number);
	// }
	//
	// }

	// public String format(double number) {
	// System.out.println(" formating: " + number);
	//
	// if (GWT.isClient()) {
	// System.out.println("as client");
	// final NumberFormat format = NumberFormat.getFormat(pattern);
	// System.out.println("formater ready");
	// final String format2 = format.format(number);
	// System.out.println("format:" + format2);
	// return format2;
	// } else {
	// System.out.println("as server");
	// final DecimalFormat decimalFormat = new DecimalFormat(pattern);
	// System.out.println("formater ready");
	// final String format = decimalFormat.format(number);
	// return format;
	// }
	//
	// }
}
