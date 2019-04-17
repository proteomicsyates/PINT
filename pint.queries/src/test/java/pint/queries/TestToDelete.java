package pint.queries;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestToDelete {
	@Test
	public void test() {
		final String command = "notCOND[,asdf]";
		final String COMMAND_REGEX = "(.*)(\\[.*?\\].*)";

		final Pattern pattern = Pattern.compile(COMMAND_REGEX);
		final Matcher matcher = pattern.matcher(command.trim());

		String commandWord = null;
		boolean isNegative = false;
		if (matcher.find()) {
			System.out.println(matcher.group(0));

			commandWord = matcher.group(1).trim();
			if (commandWord.startsWith("!")) {
				isNegative = true;
				commandWord = commandWord.substring(1);
			} else if (commandWord.toLowerCase().startsWith("not")) {
				isNegative = true;
				commandWord = commandWord.substring(3).trim();
			}
		}

		System.out.println(commandWord);
		System.out.println(isNegative);
	}
}
