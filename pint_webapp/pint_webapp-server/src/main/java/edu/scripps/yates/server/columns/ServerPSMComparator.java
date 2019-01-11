package edu.scripps.yates.server.columns;

import edu.scripps.yates.server.util.ServerNumberFormat;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.comparator.SharedPSMBeanComparator;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;

public class ServerPSMComparator extends SharedPSMBeanComparator {

	/**
	 *
	 */
	private static final long serialVersionUID = 702229333822421895L;

	public ServerPSMComparator() {
		super();
	}

	public ServerPSMComparator(ColumnName columnName) {
		super(columnName);
	}

	public ServerPSMComparator(ColumnName columnName, String conditionName, AmountType amountType, String projectTag) {
		super(columnName, conditionName, amountType, projectTag);
	}

	public ServerPSMComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName);
	}

	public ServerPSMComparator(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	public ServerPSMComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	@Override
	public int compare(PSMBean o1, PSMBean o2) {
		if (columnName == ColumnName.PSM_AMOUNT && conditionName != null) {
			// if the column name doesn't fit with any other column , it is
			// because
			// it is a experimental condition name
			String amountString1 = DataGridRenderValue.getAmountDataGridRenderValue(o1, conditionName, amountType,
					projectTag, new ServerNumberFormat("#.##")).getValue();
			String amountString2 = DataGridRenderValue.getAmountDataGridRenderValue(o2, conditionName, amountType,
					projectTag, new ServerNumberFormat("#.##")).getValue();
			return compareNumberStrings(amountString1, amountString2, true);

		} else {
			return super.compare(o1, o2);
		}

	}

}