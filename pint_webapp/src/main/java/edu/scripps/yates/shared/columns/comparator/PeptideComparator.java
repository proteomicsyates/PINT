package edu.scripps.yates.shared.columns.comparator;

import java.util.List;
import java.util.Map;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedDataUtils;

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

	public PeptideComparator(ColumnName columnName, String conditionName, AmountType amountType, String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public PeptideComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public PeptideComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
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

		} else if (columnName == ColumnName.PEPTIDE_RATIO) {
			return SharedDataUtils.compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);

		} else {
			try {
				switch (columnName) {
				case ACC:
					return o1.getProteinAccessionString().compareTo(o2.getProteinAccessionString());
				case DESCRIPTION:
					return o1.getProteinDescriptionString().compareTo(o2.getProteinDescriptionString());
				case PEPTIDE_SEQUENCE:
					return o1.getSequence().compareTo(o2.getSequence());
				case PEPTIDE_LENGTH:
					return Integer.compare(o1.getSequence().length(), o2.getSequence().length());

				case POSITION_IN_PROTEIN:
					int position1 = getMinPosition(o1);
					int position2 = getMinPosition(o2);
					return Integer.compare(position1, position2);

				case TAXONOMY:
					return o1.getOrganismsString().compareTo(o2.getOrganismsString());

				case CONDITION:
					return o1.getConditionsString().compareTo(o2.getConditionsString());
				case PEPTIDE_EVIDENCE:
					return o1.getRelation().name().compareTo(o2.getRelation().name());
				case SPECTRUM_COUNT:
					return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());
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
		final Map<String, List<Integer>> startingPositions = o1.getStartingPositions();
		for (List<Integer> positions : startingPositions.values()) {
			for (Integer position : positions) {
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