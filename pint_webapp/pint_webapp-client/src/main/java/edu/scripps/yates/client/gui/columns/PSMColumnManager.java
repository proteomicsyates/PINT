package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.MyVerticalCheckBoxListPanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class PSMColumnManager extends AbstractColumnManager<PSMBeanLight> {

	public PSMColumnManager(FooterManager<PSMBeanLight> footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PSMColumns.getInstance().getColumns();
		for (final ColumnWithVisibility columnWithOrder : columns) {
			if (columnWithOrder.getColumn().isAddColumnByDefault()) {
				if (columnWithOrder.getColumn() == ColumnName.LINK_TO_PRIDE_CLUSTER) {
					final CustomClickableImageColumnOpenLinkToPRIDECluster<PSMBeanLight> customTextButtonColumn = new CustomClickableImageColumnOpenLinkToPRIDECluster<PSMBeanLight>(
							columnWithOrder.getColumn(), columnWithOrder.isVisible(), null);
					super.addColumn(customTextButtonColumn);
				} else {
					super.addColumn(createColumn(columnWithOrder.getColumn(), columnWithOrder.isVisible()));
				}
			}
		}

	}

	@Override
	protected MyIdColumn<PSMBeanLight> createColumn(ColumnName columnName, boolean visible) {
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription());
		return new PSMTextColumn(columnName, visible, header, footerManager.getFooter(columnName));
	}

	@Override
	public PSMTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			String conditionSymbol, AmountType amountType, String projectName) {
		final SafeHtml headerName = SafeHtmlUtils
				.fromSafeConstant(SharedDataUtil.getAmountHeader(amountType, conditionSymbol));
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName, headerName,
				SharedDataUtil.getAmountHeaderTooltip(amountType, conditionName, projectName));
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, conditionName, conditionSymbol,
				amountType.name(), projectName));
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(scoreName), scoreName);
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, scoreName));
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		final String headerName = SharedDataUtil.getRatioHeader(ratioName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtil.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, condition1Name, condition1Symbol,
				condition2Name, condition2Symbol, projectTag, ratioName));
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addRatioScoreColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName, String scoreName) {
		final String headerName = SharedDataUtil.getRatioScoreHeader(scoreName, ratioName, condition1Symbol,
				condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName), SharedDataUtil.getRatioScoreHeaderTooltip(columnName,
						condition1Name, condition2Name, ratioName, scoreName));
		final PSMTextColumn column = new PSMTextColumn(
				columnName, visibleState, header, footerManager.getRatioScoreFooterByConditions(condition1Name,
						condition2Name, projectTag, ratioName, scoreName),
				condition1Name, condition2Name, projectTag, ratioName, scoreName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, condition1Name, condition1Symbol,
				condition2Name, condition2Symbol, projectTag, ratioName, scoreName));
		addColumn(column);
		return column;
	}

}
