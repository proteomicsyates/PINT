package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ProteinColumnManager extends AbstractColumnManager<ProteinBean> {

	public ProteinColumnManager(FooterManager<ProteinBean> footerManager, String sessionID) {
		this(footerManager, null, sessionID);
	}

	public ProteinColumnManager(FooterManager<ProteinBean> footerManager, DefaultView defaultView, String sessionID) {

		super(footerManager);
		List<ColumnWithVisibility> columns = null;
		if (defaultView != null) {
			columns = defaultView.getProteinDefaultView();
		} else {
			columns = ProteinColumns.getInstance().getColumns();
		}
		for (ColumnWithVisibility columnWithVisibility : columns) {
			if (columnWithVisibility.getColumn() == ColumnName.LINK_TO_PRIDE_CLUSTER) {
				final CustomClickableImageColumnOpenLinkToPRIDECluster<ProteinBean> customTextButtonColumn = new CustomClickableImageColumnOpenLinkToPRIDECluster<ProteinBean>(
						columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				super.addColumn(customTextButtonColumn);
			} else if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON) {
				final CustomClickableImageColumnShowPeptideTable<ProteinBean> customTextButtonColumn = new CustomClickableImageColumnShowPeptideTable<ProteinBean>(
						sessionID, columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				customTextButtonColumn.setFieldUpdater(getMyFieldUpdater(customTextButtonColumn, sessionID));
				super.addColumn(customTextButtonColumn);
			} else if (columnWithVisibility.getColumn() == ColumnName.LINK_TO_INTACT) {
				final CustomClickableImageColumnOpenLinkToIntAct customTextButtonColumn = new CustomClickableImageColumnOpenLinkToIntAct(
						columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				super.addColumn(customTextButtonColumn);
			} else if (columnWithVisibility.getColumn() == ColumnName.LINK_TO_COMPLEX_PORTAL) {
				final CustomClickableImageColumnOpenLinkToComplexPortal customTextButtonColumn = new CustomClickableImageColumnOpenLinkToComplexPortal(
						columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				super.addColumn(customTextButtonColumn);
			} else {
				super.addColumn(createColumn(columnWithVisibility.getColumn(), columnWithVisibility.isVisible()));
			}
		}

	}

	@Override
	protected MyColumn<ProteinBean> createColumn(ColumnName columnName, boolean visible) {
		MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription());
		return new ProteinTextColumn(columnName, visible, header, footerManager.getFooter(columnName));
	}

	@Override
	public ProteinTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final SafeHtml headerName = SafeHtmlUtils
				.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionName));
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName, headerName,
				SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectName));

		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState, header,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		addColumn(column);
		return column;
	}

	@Override
	public ProteinTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState, header,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		addColumn(column);
		return column;
	}

	@Override
	public ProteinTextColumn addRatioScoreColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String scoreName) {
		String headerName = SharedDataUtils.getRatioHeader(scoreName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, scoreName));
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState, header,
				footerManager.getRatioScoreFooterByConditions(condition1Name, condition2Name, projectTag, scoreName),
				condition1Name, condition2Name, projectTag, scoreName);
		addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<ProteinBean> addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		// not implemented
		return null;
	}

}
