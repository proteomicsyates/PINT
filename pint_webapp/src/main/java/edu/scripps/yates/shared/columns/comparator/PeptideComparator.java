package edu.scripps.yates.shared.columns.comparator;

import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class PeptideComparator extends BeanComparator<PeptideBean> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4703281799299480352L;

	public PeptideComparator() {
		super();
	}

	public PeptideComparator(ColumnName columnName) {
		super(columnName);
	}

	public PeptideComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public PeptideComparator(ColumnName columnName, String conditionName, AmountType amountType, String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public PeptideComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public PeptideComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	@Override
	public int compare(PeptideBean o1, PeptideBean o2) {
		if (columnName == ColumnName.PEPTIDE_AMOUNT && conditionName != null) {
			// if the column name doesn't fit with any other column , it is
			// because
			// it is a experimental condition name
			String amountString1 = DataGridRenderValue
					.getAmountDataGridRenderValue(o1, conditionName, amountType, projectTag).getValue();
			String amountString2 = DataGridRenderValue
					.getAmountDataGridRenderValue(o2, conditionName, amountType, projectTag).getValue();
			return compareNumberStrings(amountString1, amountString2);

		} else if (columnName == ColumnName.PEPTIDE_SCORE && scoreName != null) {
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
					return compareStrings(o1.getProteinAccessionString(), o2.getProteinAccessionString(), false);
				case PEPTIDE_SEQUENCE:
					return o1.getSequence().compareTo(o2.getSequence());
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					int position1 = getMinPosition(o1);
					int position2 = getMinPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return compareStrings(o1.getOrganismsString(), o2.getOrganismsString(), true);

				case CONDITION:
					return compareStrings(o1.getConditionsString(), o2.getConditionsString(), true);
				case PEPTIDE_EVIDENCE:
					return compareStrings(o1.getRelation().name(), o2.getRelation().name(), true);
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
						return SharedDataUtils.getUniprotFeatureString(o1, uniprotFeatures)
								.compareTo(SharedDataUtils.getUniprotFeatureString(o2, uniprotFeatures));
					} else {
						return 0;
					}
				default:
					break;
				}
			} catch (NullPointerException e) {

			}
		}
		return 0;
	}

	private int getMinPosition(PeptideBean o1) {
		int min = Integer.MAX_VALUE;
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = o1.getStartingPositions();
		for (List<Pair<Integer, Integer>> positions : startingPositions.values()) {
			for (Pair<Integer, Integer> startAndEnd : positions) {
				int position = startAndEnd.getFirstElement();
				if (min > position)
					min = position;
			}
		}

		return min;
	}

	protected static int compareNumberStrings(String string1, String string2) {
		String tmp1 = string1;
		String tmp2 = string2;
		if ("".equals(string1) || string1 == null)
			tmp1 = "0";
		if ("".equals(string2) || string2 == null)
			tmp2 = "0";
		try {
			Double d1 = Double.valueOf(tmp1);
			Double d2 = Double.valueOf(tmp2);
			return d1.compareTo(d2);
		} catch (NumberFormatException e) {
			return tmp1.compareTo(tmp2);
		}

	}

}