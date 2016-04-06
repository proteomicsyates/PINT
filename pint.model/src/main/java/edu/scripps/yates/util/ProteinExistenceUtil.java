package edu.scripps.yates.util;

import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;

public class ProteinExistenceUtil {
	private static String[] levels = { "Evidence at protein level",
			"Evidence at transcript level", "Inferred from homology",
			"Predicted", "Uncertain" };

	public static AnnotationType getAnnotationType(String proteinExistence) {
		// check if it is a number
		try {
			final Integer pe = Integer.valueOf(proteinExistence);
			if (pe > 0 && pe < 6) {
				return AnnotationType
						.translateStringToAnnotationType(levels[pe - 1]);
			}
		} catch (NumberFormatException e) {

		}
		return AnnotationType.translateStringToAnnotationType(proteinExistence);
	}

}
