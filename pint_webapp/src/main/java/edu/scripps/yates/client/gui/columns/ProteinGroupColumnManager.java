package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.MyVerticalCheckBoxListPanel;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ProteinGroupColumnManager extends AbstractColumnManager<ProteinGroupBean> {

	public ProteinGroupColumnManager(FooterManager<ProteinGroupBean> footerManager, String sessionID) {
		this(footerManager, null, sessionID);
	}

	public ProteinGroupColumnManager(FooterManager<ProteinGroupBean> footerManager, DefaultView defaultView,
			String sessionID) {

		super(footerManager);
		List<ColumnWithVisibility> columns = null;
		if (defaultView != null) {
			columns = defaultView.getProteinGroupDefaultView();
		} else {
			columns = ProteinGroupColumns.getInstance().getColumns();
		}
		for (ColumnWithVisibility columnWithVisibility : columns) {
			if (columnWithVisibility.getColumn().isAddColumnByDefault()) {
				if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON) {
					final CustomClickableImageColumnShowPeptideTable<ProteinGroupBean> column = new CustomClickableImageColumnShowPeptideTable<ProteinGroupBean>(
							sessionID, columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
					column.setFieldUpdater(getMyFieldUpdater(column, sessionID));
					super.addColumn(column);
				} else {
					super.addColumn(createColumn(columnWithVisibility.getColumn(), columnWithVisibility.isVisible()));
				}
			}
		}

	}

	@Override
	protected MyColumn<ProteinGroupBean> createColumn(ColumnName columnName, boolean visible) {
		MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription());
		return new ProteinGroupTextColumn(columnName, visible, header, footerManager.getFooter(columnName));
	}

	@Override
	public ProteinGroupTextColumn addAmountColumn(ColumnName columnName, boolean visibleState, String conditionName,
			String conditionSymbol, AmountType amountType, String projectName) {
		final SafeHtml headerName = SafeHtmlUtils
				.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionSymbol));
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName, headerName,
				SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectName));
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(ColumnName.PROTEIN_AMOUNT, visibleState,
				header, footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, conditionName, conditionSymbol,
				amountType.name(), projectName));
		super.addColumn(column);
		return column;
	}

	@Override
	public ProteinGroupTextColumn addRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1Symbol, condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName),
				SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(ColumnName.PROTEIN_RATIO, visibleState, header,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, condition1Name, condition1Symbol,
				condition2Name, condition2Symbol, projectTag, ratioName));
		super.addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<ProteinGroupBean> addScoreColumn(ColumnName columnName, boolean visibleState,
			String scoreName) {
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(scoreName), scoreName);
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(columnName, visibleState, header,
				footerManager.getScoreFooterByScore(scoreName), scoreName);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, scoreName));
		super.addColumn(column);
		return column;
	}

	@Override
	public CustomTextColumn<ProteinGroupBean> addRatioScoreColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition1Symbol, String condition2Name, String condition2Symbol,
			String projectTag, String ratioName, String ratioScore) {
		String headerName = SharedDataUtils.getRatioScoreHeader(ratioScore, ratioName, condition1Symbol,
				condition2Symbol);
		final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
				SafeHtmlUtils.fromSafeConstant(headerName), SharedDataUtils.getRatioScoreHeaderTooltip(columnName,
						condition1Name, condition2Name, ratioName, ratioScore));
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(columnName,
				visibleState, header, footerManager.getRatioScoreFooterByConditions(condition1Name, condition2Name,
						projectTag, ratioName, ratioScore),
				condition1Name, condition2Name, projectTag, ratioName, ratioScore);
		column.setKeyName(MyVerticalCheckBoxListPanel.getKeyName(columnName, condition1Name, condition1Symbol,
				condition2Name, condition2Symbol, projectTag, ratioName, ratioScore));
		addColumn(column);
		return column;
	}

	private FieldUpdater<ProteinGroupBean, ImageResource> getMyFieldUpdater(
			final CustomClickableImageColumnShowPeptideTable<ProteinGroupBean> customTextButtonColumn,
			final String sessionID) {
		FieldUpdater<ProteinGroupBean, ImageResource> ret = new FieldUpdater<ProteinGroupBean, ImageResource>() {

			@Override
			public void update(int index, final ProteinGroupBean proteinBean, ImageResource image) {
				service.getProteinsByPeptide(sessionID, proteinBean, new AsyncCallback<ProteinPeptideCluster>() {

					@Override
					public void onSuccess(ProteinPeptideCluster result) {
						customTextButtonColumn.showSharingPeptidesTablePanel(proteinBean, result);
					}

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
					}
				});

			}

		};
		return ret;
	}
}
