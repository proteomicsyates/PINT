package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedNumberFormat;

public class ClientPeptideComparator extends SharedPeptideBeanComparator {

	/**
	 *
	 */
	private static final long serialVersionUID = -4703281799299480352L;

	public ClientPeptideComparator() {
		super();
	}

	public ClientPeptideComparator(ColumnName columnName) {
		super(columnName);
	}

	public ClientPeptideComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public ClientPeptideComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public ClientPeptideComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public ClientPeptideComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	@Override
	public int compare(PeptideBean o1, PeptideBean o2) {
		if (columnName == ColumnName.PEPTIDE_AMOUNT && conditionName != null) {
			// if the column name doesn't fit with any other column , it is
			// because
			// it is a experimental condition name
			final String amountString1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName, amountType,
					projectTag, new SharedNumberFormat("#.##")).getValue();
			final String amountString2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName, amountType,
					projectTag, new SharedNumberFormat("#.##")).getValue();
			return compareNumberStrings(amountString1, amountString2);
		} else {
			return super.compare(o1, o2);
		}

	}

}