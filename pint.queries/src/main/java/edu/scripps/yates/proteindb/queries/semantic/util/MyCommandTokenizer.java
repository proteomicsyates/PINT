package edu.scripps.yates.proteindb.queries.semantic.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.queries.LogicalOperator;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.Command;

public class MyCommandTokenizer implements Enumeration<String> {
	private final static Logger log = Logger.getLogger(MyCommandTokenizer.class);
	private final List<String> tokens = new ArrayList<String>();
	private int currentIndex = 0;

	public MyCommandTokenizer(String inputText) {
		// preprocess text before to go
		String text = preprocessText(inputText);

		// analyze the text
		while (!"".equals(text.trim())) {
			text = text.trim();
			// String separator = getNextSeparator(text);
			// int indexOfSeparator = Integer.MAX_VALUE;
			// if (separator != null)
			// indexOfSeparator = text.indexOf(separator) + separator.length();
			String nextToken = getNextToken(text);
			int indexOfToken = Integer.MAX_VALUE;
			if (nextToken != null)
				indexOfToken = text.toLowerCase().indexOf(nextToken.toLowerCase().trim()) + nextToken.trim().length();
			// if (separator == null) {
			// tokens.add(text);
			// break;
			// }

			if (nextToken == null) {
				// if (nextToken == null && separator == null) {
				tokens.add(text);
				break;
			}

			int indexOf = 0;
			// if (indexOfSeparator < indexOfToken)
			// indexOf = indexOfSeparator;
			// else
			indexOf = indexOfToken;

			String token = text.substring(0, indexOf).trim();
			if (!"".equals(token))
				tokens.add(token);
			// tokens.add(separator);
			text = text.substring(indexOf);

		}
	}

	/**
	 * Convert ']AND' --> '] AND'<br>
	 * ')AND' --> ') AND'<br>
	 * ' [' --> '['
	 *
	 * @param inputText
	 * @return
	 */
	private String preprocessText(String inputText) {
		// inputText = separateLogicalParenthesis(inputText);
		while (inputText.contains(" (")) {
			inputText = inputText.replace(" (", "(");
		}
		while (inputText.contains("( ")) {
			inputText = inputText.replace("( ", "(");
		}
		while (inputText.contains(" )")) {
			inputText = inputText.replace(" )", ")");
		}
		while (inputText.contains(") ")) {
			inputText = inputText.replace(") ", ")");
		}
		inputText = inputText.replace("(", " ( ");
		inputText = inputText.replace(")", " ) ");
		for (LogicalOperator operator : LogicalOperator.values()) {
			final String operatorText = operator.getText();
			while (inputText.contains("]" + operatorText.trim())) {
				try {
					inputText = inputText.replaceAll("]" + operatorText.trim(), "]" + operatorText);
				} catch (NullPointerException e) {
				}
			}
			while (inputText.contains(")" + operatorText.trim())) {
				try {
					inputText = inputText.replace(")" + operatorText.trim(), ")" + operatorText);
				} catch (NullPointerException e) {

				}
			}

		}
		while (inputText.contains(" [")) {
			try {
				inputText = inputText.replace(" [", "[");
			} catch (NullPointerException e) {

			}
		}
		return inputText;
	}

	private boolean endsByACommand(String string) {
		for (Command command : Command.values()) {
			final String abbreviation = command.getAbbreviation();
			if (string.endsWith(abbreviation))
				return true;
		}
		return false;
	}

	private String getNextToken(String text) {
		// separators can be parenthesis and logical operators
		int openParenthesis = text.indexOf("(");
		if (openParenthesis == -1)
			openParenthesis = Integer.MAX_VALUE;
		int closeParenthesis = text.indexOf(")");
		if (closeParenthesis == -1)
			closeParenthesis = Integer.MAX_VALUE;
		LogicalOperator nextOperator = null;
		int logicalSeparatorIndex = Integer.MAX_VALUE;
		for (LogicalOperator operator : LogicalOperator.values()) {
			int indexOf = text.toLowerCase().indexOf(operator.getText().toLowerCase().trim());
			if (indexOf == -1)
				indexOf = Integer.MAX_VALUE;
			if (indexOf == -1)
				indexOf = Integer.MAX_VALUE;
			if (indexOf < logicalSeparatorIndex) {
				nextOperator = operator;
				logicalSeparatorIndex = indexOf;
			}
		}

		String command = getNextCommand(text);
		int commandIndex = Integer.MAX_VALUE;
		if (command != null)
			commandIndex = text.indexOf(command);

		// look which is the minimum index
		if (openParenthesis < closeParenthesis && openParenthesis < logicalSeparatorIndex
				&& openParenthesis < commandIndex)
			return "(";
		if (closeParenthesis < openParenthesis && closeParenthesis < logicalSeparatorIndex
				&& closeParenthesis < commandIndex)
			return ")";
		if (logicalSeparatorIndex < openParenthesis && logicalSeparatorIndex < closeParenthesis
				&& logicalSeparatorIndex < commandIndex)
			return nextOperator.getText();
		if (commandIndex < openParenthesis && commandIndex < closeParenthesis && commandIndex < logicalSeparatorIndex)
			return command;
		return null;
	}

	private String getNextCommand(String text) {

		try {
			CommandReference command = new CommandReference(text);
			if (command != null) {
				return command.toString();
			}
		} catch (MalformedQueryException e) {

		}
		// // TODO remove this:
		// Pattern pattern = Pattern.compile(CommandReference.COMMAND_REGEX);
		// final Matcher matcher = pattern.matcher(text);
		// if (matcher.find()) {
		// String commandWord = matcher.group(1)
		// + getInsideOfBrackets(matcher.group(2));
		// return commandWord;
		// }
		return null;
	}

	// private String getInsideOfBrackets(String text) {
	// StringBuilder sb = new StringBuilder();
	// int currentIdex = 0;
	//
	// int numOpenBrackets = 0;
	// while (currentIdex <= text.length()) {
	//
	// char currentChar = text.charAt(currentIdex);
	// sb.append(currentChar);
	// if (currentChar == '[')
	// numOpenBrackets++;
	// if (currentChar == ']') {
	// numOpenBrackets--;
	// if (numOpenBrackets == 0)
	// return sb.toString();
	//
	// }
	//
	// currentIdex++;
	// }
	//
	// return null;
	// }

	@Override
	public boolean hasMoreElements() {
		return currentIndex + 1 <= tokens.size() ? true : false;
	}

	@Override
	public String nextElement() {

		return tokens.get(currentIndex++);
	}

	public String nextToken() {
		return nextElement();
	}

	public boolean hasMoreTokens() {
		return hasMoreElements();
	}

	public static int priority(Object n1) {
		if (isOperator(n1))
			return 1;
		return 0;
	}

	public static boolean isOperator(Object word) {
		if (word instanceof String) {
			if (word != null)
				for (LogicalOperator operator : LogicalOperator.values()) {
					if (((String) word).equalsIgnoreCase(operator.getText())
							|| ((String) word).equalsIgnoreCase(operator.name()))
						return true;
				}
		} else if (word instanceof LogicalOperator)
			return true;
		return false;
	}

	public static boolean isParenthesis(Object word) {
		if (word instanceof String) {
			if ("(".equals(word) || ")".equals(word))
				return true;
		}
		return false;
	}

	public static void main(String[] args) {
		boolean operator = MyCommandTokenizer.isOperator("AND");
		System.out.println(operator);
		operator = MyCommandTokenizer.isOperator("OR");
		System.out.println(operator);
		operator = MyCommandTokenizer.isOperator("and");
		System.out.println(operator);
		operator = MyCommandTokenizer.isOperator("");
		System.out.println(operator);
		operator = MyCommandTokenizer.isOperator(null);
		System.out.println(operator);

	}

	public static String[] splitCommand(String commandvalue) {
		int currentIndex = 0;
		List<String> list = new ArrayList<String>();
		int numOpenBrackets = 0;
		StringBuilder sb = new StringBuilder();
		while (currentIndex < commandvalue.length()) {
			char currentChar = commandvalue.charAt(currentIndex);

			sb.append(currentChar);
			if (currentChar == '[') {
				numOpenBrackets++;
				if (numOpenBrackets == 1)
					// remove the last comma
					sb.deleteCharAt(sb.length() - 1);
			}
			if (currentChar == ']') {
				numOpenBrackets--;
				if (numOpenBrackets == 0) {
					// remove the last closing bracket
					sb.deleteCharAt(sb.length() - 1);
					// add the last token
					list.add(sb.toString());
				}
			}
			if (currentChar == ',') {
				if (numOpenBrackets == 1) {
					// remove the last comma
					sb.deleteCharAt(sb.length() - 1);
					list.add(sb.toString());
					sb = new StringBuilder();
				}
			}
			currentIndex++;
		}
		String[] ret = new String[list.size()];
		int i = 0;
		for (String string : list) {
			ret[i++] = string;
		}
		return ret;
	}
}
