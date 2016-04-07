package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.footers.ProteinFooterManager;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.util.DefaultView;

public class ProteinColumnManager extends ColumnManager<ProteinBean> {
	private final String sessionID;
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();

	public ProteinColumnManager(ProteinFooterManager footerManager, String sessionID) {
		this(footerManager, null, sessionID);
	}

	public ProteinColumnManager(ProteinFooterManager footerManager, DefaultView defaultView, String sessionID) {

		super(footerManager);
		this.sessionID = sessionID;
		List<ColumnWithVisibility> columns = null;
		if (defaultView != null) {
			columns = defaultView.getProteinDefaultView();
		} else {
			columns = ProteinColumns.getInstance().getColumns();
		}
		for (ColumnWithVisibility columnWithVisibility : columns) {
			if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON) {
				final CustomClickableImageColumn<ProteinBean> customTextButtonColumn = new CustomClickableImageColumn<ProteinBean>(
						sessionID, columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				customTextButtonColumn.setFieldUpdater(getMyFieldUpdater(customTextButtonColumn));
				super.addColumn(customTextButtonColumn);
			} else {
				super.addColumn(createColumn(columnWithVisibility.getColumn(), columnWithVisibility.isVisible()));
			}
		}

	}

	// public ProteinColumnManager(ProteinFooterManager footerManager,
	// DefaultView defaultView, String sessionID) {
	//
	// super(footerManager);
	// this.sessionID = sessionID;
	// List<ColumnWithVisibility> columns = null;
	// if (defaultView != null) {
	// columns = defaultView.getProteinDefaultView();
	// } else {
	// columns = ProteinColumns.getInstance().getColumns();
	// }
	// for (ColumnWithVisibility columnWithVisibility : columns) {
	// if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON)
	// {
	// final CustomTextButtonColumn<ProteinBean> customTextButtonColumn = new
	// CustomTextButtonColumn<ProteinBean>(
	// sessionID, columnWithVisibility.getColumn(),
	// columnWithVisibility.isVisible(), null);
	// customTextButtonColumn.setFieldUpdater(getMyFieldUpdater(customTextButtonColumn));
	// super.addColumn(customTextButtonColumn);
	// } else {
	// super.addColumn(createColumn(columnWithVisibility.getColumn(),
	// columnWithVisibility.isVisible()));
	// }
	// }
	//
	// }
	public FieldUpdater<ProteinBean, ImageResource> getMyFieldUpdater(
			final CustomClickableImageColumn<ProteinBean> customTextButtonColumn) {
		FieldUpdater<ProteinBean, ImageResource> ret = new FieldUpdater<ProteinBean, ImageResource>() {

			@Override
			public void update(int index, final ProteinBean proteinBean, ImageResource image) {
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

	// public FieldUpdater<ProteinBean, String> getMyFieldUpdater(
	// final CustomTextButtonColumn<ProteinBean> customTextButtonColumn) {
	// FieldUpdater<ProteinBean, String> ret = new FieldUpdater<ProteinBean,
	// String>() {
	//
	// @Override
	// public void update(int index, final ProteinBean proteinBean, String
	// value) {
	// service.getProteinsByPeptide(sessionID, proteinBean, new
	// AsyncCallback<ProteinPeptideCluster>() {
	//
	// @Override
	// public void onSuccess(ProteinPeptideCluster result) {
	// customTextButtonColumn.showSharingPeptidesTablePanel(proteinBean,
	// result);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// StatusReportersRegister.getInstance().notifyStatusReporters(caught);
	// }
	// });
	// }
	// };
	// return ret;
	// }

	private MyColumn<ProteinBean> createColumn(ColumnName columnName, boolean visible) {
		return new ProteinTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	public ProteinTextColumn addProteinAmountColumn(boolean visibleState, String conditionName, AmountType amountType,
			String projectName) {
		final ProteinTextColumn column = new ProteinTextColumn(ColumnName.PROTEIN_AMOUNT, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	public ProteinTextColumn addProteinRatioColumn(ColumnName columnName, boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final ProteinTextColumn column = new ProteinTextColumn(columnName, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

	public ProteinTextColumn addProteinRatioScoreColumn(boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final ProteinTextColumn column = new ProteinTextColumn(ColumnName.PROTEIN_RATIO_SCORE, visibleState,
				footerManager.getRatioScoreFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

}
