package edu.scripps.yates.client.gui.columns;

import java.util.List;

import edu.scripps.yates.client.gui.columns.footers.PSMFooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;

public class PSMColumnManager extends ColumnManager<PSMBean> {

	public PSMColumnManager(PSMFooterManager footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PSMColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : columns) {
			super.addColumn(createColumn(columnWithOrder.getColumn(), columnWithOrder.isVisible()));
		}
	}

	private MyColumn<PSMBean> createColumn(ColumnName columnName, boolean visible) {
		return new PSMTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	public PSMTextColumn addPSMAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	public PSMTextColumn addPSMScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	public PSMTextColumn addPTMScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	public PSMTextColumn addPSMRatioColumn(boolean visibleState, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		final PSMTextColumn column = new PSMTextColumn(ColumnName.PSM_RATIO, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	public boolean containsPSMScoreColumn(String scoreName) {
		for (MyColumn<PSMBean> column : super.getColumnsByColumnName(ColumnName.PSM_SCORE)) {
			if (scoreName.equalsIgnoreCase(column.getScoreName())) {
				return true;
			}
		}
		return false;
	}

	public boolean containsPTMScoreColumn(String scoreName) {
		for (MyColumn<PSMBean> column : super.getColumnsByColumnName(ColumnName.PTM_SCORE)) {
			if (scoreName.equalsIgnoreCase(column.getScoreName())) {
				return true;
			}
		}
		return false;
	}
}
