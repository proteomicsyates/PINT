package edu.scripps.yates.server.adapters;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.omim.OmimEntry;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.OmimEntryBean;

public class OmimEntryBeanAdapter implements Adapter<OmimEntryBean> {
	private final OmimEntry omimEntry;
	private static final Logger log = Logger.getLogger(OmimEntryBeanAdapter.class);

	public OmimEntryBeanAdapter(OmimEntry omimEntry) {
		this.omimEntry = omimEntry;
	}

	@Override
	public OmimEntryBean adapt() {
		OmimEntryBean ret = new OmimEntryBean();
		ret.setId(omimEntry.getId());
		if (omimEntry.getAlternativeTitles() != null) {
			for (String alternativeTitle : omimEntry.getAlternativeTitles()) {
				ret.getAlternativeTitles().add(getAppropiateCase(alternativeTitle));
			}
		}

		String preferredTitle = getAppropiateCase(omimEntry.getPreferredTitle());
		ret.setPreferredTitle(preferredTitle);
		return ret;
	}

	/**
	 * format the preferred title: first string before to ";", with capital
	 * letters in the first character other strings after ";" in capital letters
	 * as they are.
	 *
	 * @param title
	 * @return
	 */
	private String getAppropiateCase(String title) {
		log.debug("Getting approapite case from " + title);
		String ret = title;
		try {
			if (title.contains(";")) {
				final String[] split = title.split(";");
				int i = 0;
				for (String string : split) {
					if (i == 0) {
						ret = WordUtils.swapCase(WordUtils.uncapitalize(string));
					} else {
						ret += ";" + string;
					}
					i++;
				}
			} else {
				ret = WordUtils.swapCase(WordUtils.uncapitalize(title));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("Appropiate case from " + title + " is " + ret);
		return ret;

	}

	// public static void main(String[] args) {
	// String preferredTitle = "ACTIN, ALPHA-2, SMOOTH MUSCLE, AORTA; ACTA2";
	// if (preferredTitle.contains(";")) {
	// final String[] split = preferredTitle.split(";");
	// int i = 0;
	// for (String string : split) {
	// if (i == 0) {
	// preferredTitle = WordUtils.swapCase(WordUtils.uncapitalize(string));
	// } else {
	// preferredTitle += ";" + string;
	// }
	//
	// i++;
	// }
	// } else {
	// preferredTitle =
	// WordUtils.swapCase(WordUtils.uncapitalize(preferredTitle));
	// }
	// System.out.println(preferredTitle);
	// }
}
