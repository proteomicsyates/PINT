package edu.scripps.yates.shared.util;

public interface SharedFormater {
	public String formatDouble(Double number);

	public String format(Double number, String pattern);

	public String formatScientific(Double value, int minNumDecimals, int maxNumDecimals);
}
