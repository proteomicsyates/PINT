package edu.scripps.yates.client.gui.columns;

import java.util.List;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;

public class PSMColumnManager extends AbstractColumnManager<PSMBean> {

	public PSMColumnManager(FooterManager<PSMBean> footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PSMColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : columns) {
			if (columnWithOrder.getColumn() == ColumnName.LINK_TO_PRIDE_CLUSTER) {
				final CustomClickableImageColumnOpenLinkToPRIDECluster<PSMBean> customTextButtonColumn = new CustomClickableImageColumnOpenLinkToPRIDECluster<PSMBean>(
						columnWithOrder.getColumn(), columnWithOrder.isVisible(), null);
				super.addColumn(customTextButtonColumn);
			} else {
				super.addColumn(createColumn(columnWithOrder.getColumn(), columnWithOrder.isVisible()));
			}
		}

	}

	@Override
	protected MyIdColumn<PSMBean> createColumn(ColumnName columnName, boolean visible) {
		return new PSMTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	@Override
	public PSMTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<PSMBean> addRatioScoreColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition2Name, String projectTag, String ratioName) {
		// TODO Auto-generated method stub
		return null;
	}

}
