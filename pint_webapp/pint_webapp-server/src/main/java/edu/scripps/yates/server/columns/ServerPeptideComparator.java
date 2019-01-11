package edu.scripps.yates.server.columns;

import edu.scripps.yates.server.util.ServerNumberFormat;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.comparator.SharedPeptideBeanComparator;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;

public class ServerPeptideComparator extends SharedPeptideBeanComparator {

	/**
	 *
	 */
	private static final long serialVersionUID = -4703281799299480352L;

	public ServerPeptideComparator() {
		super();
	}

	public ServerPeptideComparator(ColumnName columnName) {
		super(columnName);
	}

	public ServerPeptideComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public ServerPeptideComparator(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public ServerPeptideComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public ServerPeptideComparator(ColumnName columnName, String condition1Name, String condition2Name,
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
					projectTag, new ServerNumberFormat("#.##")).getValue();
			final String amountString2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName, amountType,
					projectTag, new ServerNumberFormat("#.##")).getValue();
			return compareNumberStrings(amountString1, amountString2);
		} else {
			return super.compare(o1, o2);
		}

	}

}