package edu.scripps.yates.shared.util;

public class CssColorUtil {

	/**
	 * Return hex color from scalar *value*.
	 *
	 * @param {float} value Scalar value between 0 and 1
	 * @return {String} color
	 */
	public static String makeColor(double value) {
		if (value < 0 || value > 1) {
			throw new IllegalArgumentException(value + " has to be between 0 and 1");
		}
		// value must be between [0, 510]
		double value2 = Math.min(Math.max(0, value), 1) * 510;

		int redValue;
		int greenValue;
		if (value2 < 255) {
			redValue = 255;
			greenValue = (int) Math.round(Math.sqrt(value2) * 16);
		} else {
			greenValue = 255;
			value2 = value2 - 255;
			redValue = (int) Math.round(255 - (value2 * value2 / 255));
		}

		String hexString = Integer.toHexString(redValue);
		if (hexString.length() == 1) {
			hexString = "0" + hexString;
		}
		String hexString2 = Integer.toHexString(greenValue);
		if (hexString2.length() == 1) {
			hexString2 = "0" + hexString2;
		}
		return "#" + hexString + hexString2 + "00";
	}

	public static void main(String[] args) {
		System.out.println(CssColorUtil.makeColor(0.5));
		for (int i = 0; i <= 100; i++) {
			System.out.println(i + "\t" + CssColorUtil.makeColor(i / 100.0));
		}

	}
}
