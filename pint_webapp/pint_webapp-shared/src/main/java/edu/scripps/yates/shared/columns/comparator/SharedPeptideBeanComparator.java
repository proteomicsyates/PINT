package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class SharedPeptideBeanComparator extends AbstractPeptideBeanComparator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4915182842968594067L;

	public SharedPeptideBeanComparator() {
		super();
	}

	public SharedPeptideBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public SharedPeptideBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public SharedPeptideBeanComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public SharedPeptideBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public SharedPeptideBeanComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	@Override
	public int compare(PeptideBean o1, PeptideBean o2) {
		if (columnName == ColumnName.PEPTIDE_SCORE && scoreName != null) {
			final ScoreBean score1 = o1.getScoreByName(scoreName);
			final ScoreBean score2 = o2.getScoreByName(scoreName);
			String value1 = null;
			String value2 = null;
			if (score1 != null)
				value1 = score1.getValue();
			if (score2 != null)
				value2 = score2.getValue();

			return compareNumberStrings(value1, value2);

		} else if (columnName == ColumnName.PEPTIDE_RATIO || columnName == ColumnName.PEPTIDE_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);

		} else if (columnName == ColumnName.PEPTIDE_RATIO_SCORE) {
			return compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName, scoreName, false);
		} else {
			try {
				switch (columnName) {
				case ACC:
					return compareStrings(o1.getProteinAccessionString(), o2.getProteinAccessionString(), false,
							o1.getFullSequence(), o2.getFullSequence());
				case PEPTIDE_SEQUENCE:
					return o1.getSequence().compareTo(o2.getSequence());
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					final int position1 = getMinPosition(o1);
					final int position2 = getMinPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return compareStrings(o1.getOrganismsString(), o2.getOrganismsString(), true, o1.getFullSequence(),
							o2.getFullSequence());

				case CONDITION:
					return compareStrings(o1.getConditionsString(), o2.getConditionsString(), true,
							o1.getFullSequence(), o2.getFullSequence());
				case PEPTIDE_EVIDENCE:
					return compareStrings(o1.getRelation().name(), o2.getRelation().name(), true, o1.getFullSequence(),
							o2.getFullSequence());
				case SPECTRUM_COUNT:
					return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());
				case SPC_PER_CONDITION:
					return Integer.compare(o1.getNumPSMsByCondition(projectTag, conditionName),
							o2.getNumPSMsByCondition(projectTag, conditionName));
				case PEPTIDE_DOMAIN_FAMILIES:
				case PEPTIDE_NATURAL_VARIATIONS:
				case PEPTIDE_SECONDARY_STRUCTURE:
				case PEPTIDE_EXPERIMENTAL_INFO:
				case PEPTIDE_MOLECULAR_PROCESSING:
				case PEPTIDE_PTM:
					final String[] uniprotFeatures = UniprotFeatures.getUniprotFeaturesByColumnName(columnName);
					if (uniprotFeatures != null && uniprotFeatures.length > 0) {
						final String uniprotFeatureString1 = SharedDataUtil.getUniprotFeatureString(o1,
								uniprotFeatures);
						final String uniprotFeatureString2 = SharedDataUtil.getUniprotFeatureString(o2,
								uniprotFeatures);
						return compareStrings(uniprotFeatureString1, uniprotFeatureString2, true, o1.getFullSequence(),
								o2.getFullSequence());
					} else {
						return 0;
					}
				default:
					break;
				}
			} catch (final NullPointerException e) {

			}
		}
		return 0;
	}

}
