package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.SharedDataUtils;

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
		MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription());
		return new PSMTextColumn(columnName, visible, header, footerManager.getFooter(columnName));
	}

	@Override
	public PSMTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			String conditionSymbol, AmountType amountType, String projectName) {
		final SafeHtml headerName = SafeHtmlUtils
				.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionSymbol));
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName, headerName,
				SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectName));
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(scoreName), scoreName);
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		super.addColumn(column);
		return column;
	}

	@Override
	public PSMTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
		final PSMTextColumn column = new PSMTextColumn(columnName, visibleState, header,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<PSMBean> addRatioScoreColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition1Symbol, String condition2Name, String condition2Symbol,
			String projectTag, String ratioName, String ratioScore) {
		// TODO Auto-generated method stub
		return null;
	}

}
