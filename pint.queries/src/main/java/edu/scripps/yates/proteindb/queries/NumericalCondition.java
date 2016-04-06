package edu.scripps.yates.proteindb.queries;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;

/**
 * Class for representing a numerical condition such as:
 * <ul>
 * <li>'<' number,</li>
 * <li>'>' number,</li>
 * <li>'=' number,</li>
 * <li>'!=' number,</li>
 * <li>'=' Nan,</li>
 * <li>'=' INFINITY</li>
 * <li>'=' -INFINITY or</li>
 * <li>'=' text</li>
 * <ul>
 * Note that when comparing INFINITIES, the Double.MAX_VALUE and Double.MIN_VALUES are considered as equivalent to Double.POSITIVE_INFINITY and Double.NEGATIVE_INFINITY respectively.
 * @author Salva
 * 
 */
public class NumericalCondition {
	private static final Logger log = Logger
			.getLogger(NumericalCondition.class);
	private final NumericalconditionOperator operator;
	private Double value;
	private String stringValue; // a value that is not a Double
	/**
	 * The wilcard for accepting any value
	 */
	public static final String WILCARD = "*";

	public NumericalCondition(NumericalconditionOperator operator, Number value) {
		this.operator = operator;
		this.value = value.doubleValue();
	}

	public NumericalCondition(String text) throws MalformedQueryException {

		final NumericalconditionOperator[] values = NumericalconditionOperator
				.values();
		for (NumericalconditionOperator numericalconditionOperator : values) {
			if (text.startsWith(numericalconditionOperator.getText())) {
				operator = numericalconditionOperator;
				try {
					final String substring = text.substring(
							numericalconditionOperator.getText().length())
							.trim();
					value = getValueFromStringValue(substring);
					stringValue = null;
					// exit
					return;
				} catch (NumberFormatException e) {
					// if the text after the operator is not a number
					// it is just valid with the operator "="
					if (operator == NumericalconditionOperator.EQUAL) {
						stringValue = text.substring(
								numericalconditionOperator.getText().length())
								.trim();
						value = null;
						// exit
						return;
					} else {
						throw new MalformedQueryException(
								"Error parsing Numerical Condition. If the value is a text, only the \"=\" operator is allowed");
					}
				}
			}
		}
		if (text != null && !"".equals(text)) {
			// assume that if no operator, it is an equals
			operator = NumericalconditionOperator.EQUAL;
			// try to get the number from the text
			try {
				value = getValueFromStringValue(text);
				stringValue = null;
			} catch (NumberFormatException e) {
				stringValue = text;
				value = null;
			}
		} else {
			throw new MalformedQueryException(
					"Error parsing Numerical Condition '" + text + "'");
		}
	}

	private Double getValueFromStringValue(String substring) {
		Double ret = null;
		if (substring != null) {
			if (substring.equalsIgnoreCase("NaN")) {
				ret = Double.NaN;
			} else if (substring.equalsIgnoreCase("INF")
					|| substring.equalsIgnoreCase("+INF")
					|| substring.equalsIgnoreCase("+ INF")
					|| substring.equalsIgnoreCase("INFINITY")
					|| substring.equalsIgnoreCase("+INFINITY")
					|| substring.equalsIgnoreCase("+ INFINITY")) {
				ret = Double.POSITIVE_INFINITY;
			} else if (substring.equalsIgnoreCase("-INF")
					|| substring.equalsIgnoreCase("- INF")
					|| substring.equalsIgnoreCase("-INFINITY")
					|| substring.equalsIgnoreCase("- INFINITY")) {
				ret = Double.NEGATIVE_INFINITY;
			} else {
				ret = Double.valueOf(substring);
			}
		}
		return ret;
	}

	private boolean areEquals(double num1, double num2) {
		if (Double.compare(num1, Double.POSITIVE_INFINITY) == 0
				|| Double.compare(num1, Double.MAX_VALUE) == 0) {
			if (Double.compare(num2, Double.POSITIVE_INFINITY) == 0
					|| Double.compare(num2, Double.MAX_VALUE) == 0) {
				return true;
			}
		}
		if (Double.compare(num1, Double.NEGATIVE_INFINITY) == 0
				|| Double.compare(num1, -Double.MAX_VALUE) == 0) {
			if (Double.compare(num2, Double.NEGATIVE_INFINITY) == 0
					|| Double.compare(num2, -Double.MAX_VALUE) == 0) {
				return true;
			}
		}
		return Double.compare(num1, num2) == 0;
	}

	/**
	 * @return the operator
	 */
	public NumericalconditionOperator getOperator() {
		return operator;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @return the string value
	 */
	public String getStringValue() {
		return stringValue;
	}

	public boolean matches(Number num) {
		try {
			if (value != null) {
				double numDouble = num.doubleValue();

				switch (operator) {
				case EQUAL:
					return areEquals(numDouble, value);
				case GREATER_OR_EQUAL:
					return numDouble >= value;
				case GREATER:
					return numDouble > value;
				case LESS:
					return numDouble < value;
				case LESS_OR_EQUAL:
					return numDouble <= value;
				case NOT_EQUAL:
					return !areEquals(numDouble, value);
				default:
					break;
				}
			} else if (stringValue != null) {
				if (WILCARD.equals(stringValue)) {
					return true; // return true anyway
				}
			}
		} catch (NumberFormatException e) {
			log.info(e.getMessage());
		}
		return false;
	}

	public boolean matches(String num) {
		try {
			if (value != null) {
				double numDouble = Double.valueOf(num);
				return matches(numDouble);
			} else if (stringValue != null) {
				if (WILCARD.equals(stringValue)) {
					return true; // return true anyway
				} else if (num.equalsIgnoreCase(stringValue)) {
					return true;
				}
			}
		} catch (NumberFormatException e) {
			log.info(e.getMessage());
		}
		return false;
	}

	public static void main(String[] args) {
		NumericalCondition condition = new NumericalCondition(
				NumericalconditionOperator.NOT_EQUAL, Double.NaN);
		System.out.println(condition.matches(0.2));

		NumericalCondition condition2 = new NumericalCondition(
				NumericalconditionOperator.EQUAL, Double.NaN);
		System.out.println(condition2.matches(0.2));
		System.out.println(condition2.matches(Double.NaN));
	}

	public boolean isNanValue() {
		if (value != null && Double.compare(Double.NaN, value) == 0) {
			return true;
		}
		return false;
	}

}
