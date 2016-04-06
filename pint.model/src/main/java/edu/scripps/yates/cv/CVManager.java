package edu.scripps.yates.cv;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.LocalOboControlVocabularyManager;
import org.proteored.miapeapi.cv.msi.Score;

public class CVManager {
	private static ControlVocabularyManager cvManager = new LocalOboControlVocabularyManager();

	public static String getPreferredName(CommonlyUsedCV cv) {

		final ControlVocabularyTerm cvTerm = cvManager.getCVTermByAccession(
				new Accession(cv.getId()), Score.getInstance(cvManager));
		if (cvTerm != null) {
			return cvTerm.getPreferredName();
		}
		return null;
	}

	public static ControlVocabularyTerm getCvByName(String name) {
		final Score cvset = Score.getInstance(cvManager);
		return cvset.getCVTermByPreferredName(name);

	}
}
