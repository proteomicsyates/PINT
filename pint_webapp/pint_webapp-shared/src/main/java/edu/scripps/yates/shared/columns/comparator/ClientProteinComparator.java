package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedNumberFormat;

public class ClientProteinComparator extends SharedProteinBeanComparator {

	/**
	 *
	 */
	private static final long serialVersionUID = -5795708428622637833L;

	public ClientProteinComparator() {
		super();
	};

	public ClientProteinComparator(ColumnName columnName) {
		super(columnName);
	}

	public ClientProteinComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public ClientProteinComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public ClientProteinComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	public ClientProteinComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName,
				ratioScoreName);
	}

	@Override
	public int compare(ProteinBean o1, ProteinBean o2) {
		if (columnName == ColumnName.PROTEIN_AMOUNT) {
			final DataGridRenderValue data1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName,
					amountType, projectTag, new SharedNumberFormat("#.##"));
			final DataGridRenderValue data2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName,
					amountType, projectTag, new SharedNumberFormat("#.##"));

			String amountString1 = data1.getValue();
			String amountString2 = data2.getValue();

			try {
				if ("".equals(amountString1))
					amountString1 = String.valueOf(Double.MAX_VALUE);
				if ("".equals(amountString2))
					amountString2 = String.valueOf(-Double.MAX_VALUE);
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
