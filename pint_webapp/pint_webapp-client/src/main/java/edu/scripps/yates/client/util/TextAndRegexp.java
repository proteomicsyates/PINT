package edu.scripps.yates.client.util;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class TextAndRegexp {
	private final String accession;
	private final RegExp regexp;

	public TextAndRegexp(String accession, RegExp regexp) {
		this.accession = accession;
		this.regexp = regexp;
	}

	/**
	 * @return the accession
	 */
	public String getAccession() {
		return accession;
	}

	/**
	 * @return the regep
	 */
	public RegExp getRegexp() {
		return regexp;
	}

	public String getParsedAccession() {
		if (regexp != null) {
			final MatchResult exec = regexp.exec(accession);
			if (exec != null) {
				if (exec.getGroupCount() > 0) {
					return exec.getGroup(exec.getGroupCount() - 1);
				}
			}
		}
		return null;
	}
}
