package edu.scripps.yates.census.read.util;

import org.apache.log4j.Logger;

public enum QuantificationLabel {

	LIGHT, HEAVY, //
	TMT_6PLEX_126, TMT_6PLEX_127, TMT_6PLEX_128, TMT_6PLEX_129, TMT_6PLEX_130, TMT_6PLEX_131, //
	N14, N15;
	private final static Logger log = Logger.getLogger(QuantificationLabel.class);

	public static QuantificationLabel getByName(String labelName) {
		if (labelName == null)
			return null;
		if (labelName.equalsIgnoreCase("l"))
			return LIGHT;
		if (labelName.equalsIgnoreCase("h"))
			return HEAVY;

		final QuantificationLabel[] values = values();
		for (QuantificationLabel quantificationLabel : values) {
			if (quantificationLabel.name().equalsIgnoreCase(labelName))
				return quantificationLabel;

		}
		if (labelName.contains("126")) {
			return QuantificationLabel.TMT_6PLEX_126;
		}
		if (labelName.contains("127")) {
			return QuantificationLabel.TMT_6PLEX_127;
		}
		if (labelName.contains("128")) {
			return QuantificationLabel.TMT_6PLEX_128;
		}
		if (labelName.contains("129")) {
			return QuantificationLabel.TMT_6PLEX_129;
		}
		if (labelName.contains("130")) {
			return QuantificationLabel.TMT_6PLEX_130;
		}
		if (labelName.contains("131")) {
			return QuantificationLabel.TMT_6PLEX_131;
		}
		log.warn("Quantification label '" + labelName + "' not recognized");
		return null;
	}
}
