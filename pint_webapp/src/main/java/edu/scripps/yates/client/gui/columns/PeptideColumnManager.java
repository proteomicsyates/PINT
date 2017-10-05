package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.MyVerticalCheckBoxListPanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PeptideColumnManager extends AbstractColumnManager<PeptideBean> {

	public PeptideColumnManager(FooterManager<PeptideBean> footerManager) {
		super(footerManager);

		final List<ColumnWithVisibility> columns = PeptideColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : columns) {
			if (columnWithOrder.getColumn().isAddColumnByDefault()) {
				final ColumnName columnName = columnWithOrder.getColumn();
				if (columnName == ColumnName.LINK_TO_PRIDE_CLUSTER) {
					final CustomClickableImageColumnOpenLinkToPRIDECluster<PeptideBean> customTextButtonColumn = new CustomClickableImageColumnOpenLinkToPRIDECluster<PeptideBean>(
							columnName, columnWithOrder.isVisible(), null);
					super.addColumn(customTextButtonColumn);
				} else {

					super.addColumn(createColumn(columnName, columnWithOrder.isVisible()));
				}
			}
		}
	}

	@Override
	protected MyIdColumn<PeptideBean> createColumn(ColumnName columnName, boolean visible) {
		MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription());
		return new PeptideTextColumn(columnName, visible, header, footerManager.getFooter(columnName));
	}

	@Override
	public PeptideTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			String conditionSymbol, AmountType amountType, String projectName) {
		final SafeHtml headerName = SafeHtmlUtils
				.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionSymbol));
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName, headerName,
				SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectName));
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState, header,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, amountType.name()));
		super.addColumn(column);
		return column;
	}

	@Override
	public PeptideTextColumn addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(scoreName), scoreName);
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState, header,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, scoreName));
		super.addColumn(column);
		return column;
	}

	@Override
	public PeptideTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState, header,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, ratioName));
		super.addColumn(column);
		return column;
	}

	@Override
	public PeptideTextColumn addRatioScoreColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName, String scoreName) {
		String headerName = SharedDataUtils.getRatioScoreHeader(scoreName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName), SharedDataUtils.getRatioScoreHeaderTooltip(columnName,
						condition1Name, condition2Name, ratioName, scoreName));
		final PeptideTextColumn column = new PeptideTextColumn(columnName, visibleState, header,
				footerManager.getRatioScoreFooterByConditions(condition1Name, condition2Name, projectTag, scoreName),
				condition1Name, condition2Name, projectTag, ratioName, scoreName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, scoreName));
		addColumn(column);
		return column;
	}

}
