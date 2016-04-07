package edu.scripps.yates.client.gui.columns;

import java.util.List;

import edu.scripps.yates.client.gui.columns.footers.PeptideFooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;

public class PeptideColumnManager extends ColumnManager<PeptideBean> {

	public PeptideColumnManager(PeptideFooterManager footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PeptideColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : columns) {
			super.addColumn(createColumn(columnWithOrder.getColumn(), columnWithOrder.isVisible()));
		}
	}

	private MyColumn<PeptideBean> createColumn(ColumnName columnName, boolean visible) {
		return new PeptideTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	public PeptideTextColumn addPeptideAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	public PeptideTextColumn addPeptideScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	public PeptideTextColumn addPeptideRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	public boolean containsPeptideScoreColumn(String scoreName) {
		for (MyColumn<PeptideBean> column : super.getColumnsByColumnName(ColumnName.PEPTIDE_SCORE)) {
			if (scoreName.equalsIgnoreCase(column.getScoreName())) {
				return true;
			}
		}
		return false;
	}

}
