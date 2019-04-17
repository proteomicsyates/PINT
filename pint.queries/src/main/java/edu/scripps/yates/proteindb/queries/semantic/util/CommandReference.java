package edu.scripps.yates.proteindb.queries.semantic.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.Command;

/**
 * Class that represents a {@link Command} with its inside value
 * 
 * @author Salva
 *
 */
public class CommandReference {
	public static final String COMMAND_REGEX = "(.*?)(\\[.*?\\].*)";

	private Command command;
	private String commandValue;
	private boolean isNegative;

	/**
	 *
	 * @param command COMMAND[command value]
	 * @throws MalformedQueryException
	 */
	public CommandReference(String command) throws MalformedQueryException {

		final Pattern pattern = Pattern.compile(COMMAND_REGEX);
		final Matcher matcher = pattern.matcher(command.trim());

		if (matcher.find()) {
			isNegative = false;

			String commandWord = matcher.group(1).trim();
			if (commandWord.startsWith("!")) {
				isNegative = true;
				commandWord = commandWord.substring(1);
			} else if (commandWord.toLowerCase().startsWith("not")) {
				isNegative = true;
				commandWord = commandWord.substring(3).trim();
			}

			commandValue = getCommandFromInsideOfBrackets(matcher.group(2));
			this.command = Command.getCommand(commandWord);
			if (this.command != null && !"".equals(commandValue))
				return;
			else
				throw new MalformedQueryException("'" + commandWord + "' is not recognized as a valid command");

		}
		throw new MalformedQueryException("Command '" + command + "' is not valid");
	}

	private String getCommandFromInsideOfBrackets(String text) {
		final StringBuilder sb = new StringBuilder();
		int currentIdex = 0;

		int numOpenBrackets = 0;
		while (currentIdex <= text.length()) {

			final char currentChar = text.charAt(currentIdex);
			sb.append(currentChar);
			if (currentChar == '[')
				numOpenBrackets++;
			if (currentChar == ']') {
				numOpenBrackets--;
				if (numOpenBrackets == 0)
					return sb.toString();

			}

			currentIdex++;
		}

		return null;
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @return the commandValue including the brackets
	 */
	public String getCommandValue() {
		return commandValue;
	}

	/**
	 * @return the isNegative
	 */
	public boolean isNegative() {
		return isNegative;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		if (isNegative)
			sb.append("!");
		sb.append(command.getAbbreviation() + commandValue);
		return sb.toString();
	}
}
