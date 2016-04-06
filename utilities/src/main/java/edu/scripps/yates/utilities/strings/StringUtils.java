package edu.scripps.yates.utilities.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that provides a {@link String} comparator with some additional features
 * 
 * @author Salva
 * 
 */
public class StringUtils {
	/**
	 * Comparison between two {@link String} with several options
	 * 
	 * @param text1
	 * @param text2
	 * @param ignoreCase
	 *            the case will be ignored
	 * @param performContains
	 *            if true, it will return true if one String contains the other
	 *            (see reverseComparison paremeter)
	 * @param reverseComparison
	 *            if false, the containing check only will return true if text1
	 *            contains text2. If true, it will also return true if text2
	 *            contains text1. This parameter will be ignored if
	 *            performContains is false.
	 * @return
	 */
	public static boolean compareStrings(String text1, String text2,
			boolean ignoreCase, boolean performContains,
			boolean reverseComparison) {
		if (text1 == null && text2 == null)
			return true;
		if ((text1 == null && text2 != null)
				|| (text1 != null && text2 == null))
			return false;

		if (ignoreCase && text1.equalsIgnoreCase(text2))
			return true;
		if (!ignoreCase && text1.equals(text2))
			return true;
		if (performContains) {
			if (ignoreCase && text1.toLowerCase().contains(text2.toLowerCase()))
				return true;
			if (!ignoreCase && text1.contains(text2))
				return true;
			if (reverseComparison) {
				if (ignoreCase
						&& text2.toLowerCase().contains(text1.toLowerCase()))
					return true;
				if (!ignoreCase && text2.contains(text1))
					return true;
			}
		}
		return false;
	}

	public static List<Integer> allIndexOf(String sourceString,
			String targetString) {
		List<Integer> ret = new ArrayList<Integer>();
		if (sourceString != null && targetString != null
				&& !"".equals(sourceString) && !"".equals(targetString)) {
			Pattern p = Pattern.compile(targetString, Pattern.LITERAL);
			Matcher m = p.matcher(sourceString);
			int start = 0;
			while (m.find(start)) {
				start = m.start() + 1;
				ret.add(start);
			}
		}
		return ret;
	}

	public static String convertStreamToString(InputStream is, int bufferSize,
			String encoding) throws IOException {

		Reader reader = new BufferedReader(new InputStreamReader(is, encoding));
		StringBuffer content = new StringBuffer();
		char[] buffer = new char[bufferSize];
		int n;

		while ((n = reader.read(buffer)) != -1) {
			content.append(buffer, 0, n);
		}

		return content.toString();
	}
}
