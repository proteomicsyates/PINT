

package java.text;

/**
 * The purpose of this class is to allow Decimal format to exist in Shared code,
 * even though it is never called.
 */
@SuppressWarnings("UnusedParameters")
public class DecimalFormat {
	public DecimalFormat(String pattern) {
	}

	public static DecimalFormat getInstance() {
		return null;
	}

	public static DecimalFormat getIntegerInstance() {
		return null;
	}

	public String format(double num) {
		return null;
	}

	public Number parse(String num) {
		return null;
	}
}