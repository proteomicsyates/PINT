package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedNumberFormat;

public class ClientProteinGroupComparator extends SharedProteinGroupComparator {

	/**
	 *
	 */
	private static final long serialVersionUID = -8171348085149617072L;

	public ClientProteinGroupComparator() {
		super();
	}

	public ClientProteinGroupComparator(ColumnName columnName) {
		super(columnName);
	}

	public ClientProteinGroupComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public ClientProteinGroupComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public ClientProteinGroupComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	public ClientProteinGroupComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	@Override
	public int compare(ProteinGroupBean o1, ProteinGroupBean o2) {
		if (columnName == ColumnName.PROTEIN_AMOUNT) {
			final DataGridRenderValue data1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName,
					amountType, projectTag, new SharedNumberFormat("#.##"));
			final DataGridRenderValue data2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName,
					amountType, projectTag, new SharedNumberFormat("#.##"));

			String amountString1 = data1.getValue();
			String amountString2 = data2.getValue();

			// String amountString1 = o1.getAmountString(conditionName,
			// projectTag);
			// String amountString2 = o2.getAmountString(conditionName,
			// projectTag);
			try {
				if ("".equals(amountString1))
					amountString1 = "0";
				if ("".equals(amountString2))
					amountString2 = "0";
				final Double amount1 = Double.valueOf(amountString1);
				final Double amount2 = Double.valueOf(amountString2);
				return compareNumbers(amount1, amount2);
			} catch (final NumberFormatException e) {
				return compareStrings(amountString1, amountString2, true);
			}
		} else {
			return super.compare(o1, o2);
		}

	}

}
