package edu.scripps.yates.client.gui.columns;

import java.util.List;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;

public class PeptideColumnManager extends AbstractColumnManager<PeptideBean> {

	public PeptideColumnManager(FooterManager<PeptideBean> footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PeptideColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : columns) {
			if (columnWithOrder.getColumn() == ColumnName.LINK_TO_PRIDE_CLUSTER) {
				final CustomClickableImageColumnOpenLinkToPRIDECluster<PeptideBean> customTextButtonColumn = new CustomClickableImageColumnOpenLinkToPRIDECluster<PeptideBean>(
						columnWithOrder.getColumn(), columnWithOrder.isVisible(), null);
				super.addColumn(customTextButtonColumn);
			} else {
				super.addColumn(createColumn(columnWithOrder.getColumn(), columnWithOrder.isVisible()));
			}
		}
	}

	@Override
	protected MyIdColumn<PeptideBean> createColumn(ColumnName columnName, boolean visible) {
		return new PeptideTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	@Override
	public PeptideTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PeptideTextColumn addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PeptideTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<PeptideBean> addRatioScoreColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition2Name, String projectTag, String ratioName) {
		// not implemented
		return null;
	}

}
