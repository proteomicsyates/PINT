package edu.scripps.yates.client.gui.columns;

import java.util.List;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DefaultView;

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
		return new ProteinTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	@Override
	public ProteinTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		addColumn(column);
		return column;
	}

	@Override
	public ProteinTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		addColumn(column);
		return column;
	}

	@Override
	public ProteinTextColumn addRatioScoreColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState,
				footerManager.getRatioScoreFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<ProteinBean> addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName) {
		// not implemented
		return null;
	}

}
