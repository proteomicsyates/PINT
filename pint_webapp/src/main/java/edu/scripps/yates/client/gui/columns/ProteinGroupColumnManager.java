package edu.scripps.yates.client.gui.columns;

import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.footers.ProteinGroupFooterManager;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.util.DefaultView;

public class ProteinGroupColumnManager extends ColumnManager<ProteinGroupBean> {
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();
	private final String sessionID;

	public ProteinGroupColumnManager(ProteinGroupFooterManager footerManager, String sessionID) {
		this(footerManager, null, sessionID);
	}

	public ProteinGroupColumnManager(ProteinGroupFooterManager footerManager, DefaultView defaultView,
			String sessionID) {

		super(footerManager);
		this.sessionID = sessionID;
		List<ColumnWithVisibility> columns = null;
		if (defaultView != null) {
			columns = defaultView.getProteinGroupDefaultView();
		} else {
			columns = ProteinGroupColumns.getInstance().getColumns();
		}
		for (ColumnWithVisibility columnWithVisibility : columns) {
			if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON) {
				final CustomClickableImageColumn<ProteinGroupBean> column = new CustomClickableImageColumn<ProteinGroupBean>(
						sessionID, columnWithVisibility.getColumn(), columnWithVisibility.isVisible(), null);
				column.setFieldUpdater(getMyFieldUpdater(column));
				super.addColumn(column);
			} else {
				super.addColumn(createColumn(columnWithVisibility.getColumn(), columnWithVisibility.isVisible()));
			}
		}

	}
	// public ProteinGroupColumnManager(ProteinGroupFooterManager footerManager,
	// DefaultView defaultView,
	// String sessionID) {
	//
	// super(footerManager);
	// this.sessionID = sessionID;
	// List<ColumnWithVisibility> columns = null;
	// if (defaultView != null) {
	// columns = defaultView.getProteinGroupDefaultView();
	// } else {
	// columns = ProteinGroupColumns.getInstance().getColumns();
	// }
	// for (ColumnWithVisibility columnWithVisibility : columns) {
	// if (columnWithVisibility.getColumn() == ColumnName.PEPTIDES_TABLE_BUTTON)
	// {
	// final CustomTextButtonColumn<ProteinGroupBean> column = new
	// CustomTextButtonColumn<ProteinGroupBean>(
	// sessionID, columnWithVisibility.getColumn(),
	// columnWithVisibility.isVisible(), null);
	// column.setFieldUpdater(getMyFieldUpdater(column));
	// super.addColumn(column);
	// } else {
	// super.addColumn(createColumn(columnWithVisibility.getColumn(),
	// columnWithVisibility.isVisible()));
	// }
	// }
	//
	// }

	private FieldUpdater<ProteinGroupBean, ImageResource> getMyFieldUpdater(
			final CustomClickableImageColumn<ProteinGroupBean> customTextButtonColumn) {
		FieldUpdater<ProteinGroupBean, ImageResource> ret = new FieldUpdater<ProteinGroupBean, ImageResource>() {

			@Override
			public void update(int index, final ProteinGroupBean proteinGroup, ImageResource image) {
				service.getProteinsByPeptide(sessionID, proteinGroup, new AsyncCallback<ProteinPeptideCluster>() {

					@Override
					public void onSuccess(ProteinPeptideCluster result) {
						customTextButtonColumn.showSharingPeptidesTablePanel(proteinGroup, result);
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
	// private FieldUpdater<ProteinGroupBean, String> getMyFieldUpdater(
	// final CustomTextButtonColumn<ProteinGroupBean> customTextButtonColumn) {
	// FieldUpdater<ProteinGroupBean, String> ret = new
	// FieldUpdater<ProteinGroupBean, String>() {
	//
	// @Override
	// public void update(int index, final ProteinGroupBean proteinGroup, String
	// value) {
	// service.getProteinsByPeptide(sessionID, proteinGroup, new
	// AsyncCallback<ProteinPeptideCluster>() {
	//
	// @Override
	// public void onSuccess(ProteinPeptideCluster result) {
	// customTextButtonColumn.showSharingPeptidesTablePanel(proteinGroup,
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

	private MyColumn<ProteinGroupBean> createColumn(ColumnName columnName, boolean visible) {
		return new ProteinGroupTextColumn(columnName, visible, footerManager.getFooter(columnName));
	}

	public ProteinGroupTextColumn addProteinAmountColumn(boolean visibleState, String conditionName,
			AmountType amountType, String projectName) {
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(ColumnName.PROTEIN_AMOUNT, visibleState,
				footerManager.getAmountFooterByCondition(conditionName, amountType, projectName), conditionName,
				amountType, projectName);
		super.addColumn(column);
		return column;
	}

	public ProteinGroupTextColumn addProteinRatioColumn(boolean visibleState, String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final ProteinGroupTextColumn column = new ProteinGroupTextColumn(ColumnName.PROTEIN_RATIO, visibleState,
				footerManager.getRatioFooterByConditions(condition1Name, condition2Name, projectTag, ratioName),
				condition1Name, condition2Name, projectTag, ratioName);
		super.addColumn(column);
		return column;
	}

}
