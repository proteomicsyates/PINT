package edu.scripps.yates.client.gui.components;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.ProteinColumnManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncProteinBeanListDataProvider;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.client.interfaces.RefreshData;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ProteinTablePanel extends FlowPanel implements HasColumns, RefreshData, ProvidesResize {

	private static final String cwDataGridEmpty = "No proteins shown";
	private final AsyncProteinBeanListDataProvider asyncProteinDataListProvider;
	private final MyDataGrid<ProteinBean> dataGrid;
	private final ProteinColumnManager proteinColumnManager;
	private final ProteinFooterManager footerManager;
	private final SelectionModel<ProteinBean> selectionModel;
	private final SimplePager pager;

	public ProteinTablePanel(String sessionID, boolean multipleSelectionModel, boolean addCheckBoxSelection) {
		super();
		dataGrid = makeDataGrid();
		asyncProteinDataListProvider = new AsyncProteinBeanListDataProvider(sessionID);
		footerManager = new ProteinFooterManager(dataGrid);
		proteinColumnManager = new ProteinColumnManager(footerManager, sessionID);

		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		resizeLayoutPanel.setSize("100%", "90%");
		resizeLayoutPanel.add(dataGrid);
		this.add(resizeLayoutPanel);

		proteinColumnManager.addChangeListener(this);

		// Add the CellList to the adapter in the database.
		asyncProteinDataListProvider.addDataDisplay(dataGrid);

		AsyncHandler asyncHandler = new AsyncHandler(dataGrid);
		dataGrid.addColumnSortHandler(asyncHandler);

		dataGrid.setAutoHeaderRefreshDisabled(true);

		// Initialize the columns.
		initTableColumns(addCheckBoxSelection);

		// add the pager
		pager = makePager();
		this.add(pager);

		// add the selection manager
		if (multipleSelectionModel) {
			selectionModel = new MultiSelectionModel<ProteinBean>();
			dataGrid.setSelectionModel(selectionModel,
					DefaultSelectionEventManager.<ProteinBean> createCheckboxManager());
		} else {
			selectionModel = new SingleSelectionModel<ProteinBean>();
			dataGrid.setSelectionModel(selectionModel);
		}

		dataGrid.redraw();
	}

	public void setEmptyTableWidget(String emptyLabelString) {
		setEmptyTableWidget(new Label(emptyLabelString));
	}

	public void setEmptyTableWidget(Widget emptyWidget) {
		if (dataGrid != null) {
			if (emptyWidget != null)
				dataGrid.setEmptyTableWidget(emptyWidget);
			else
				dataGrid.setEmptyTableWidget(null);
			dataGrid.redraw();
		}
	}

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in ProteinTable");

		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getProteinPageSize());
		}
		// get default views on proteins
		final List<ColumnWithVisibility> proteinDefaultView = defaultView.getProteinDefaultView();
		for (ColumnWithVisibility columnWithVisibility : proteinDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}
		// get sorting parameters for the proteins
		final ORDER proteinOrder = defaultView.getProteinOrder();
		final String proteinSortingScore = defaultView.getProteinSortingScore();
		final ColumnName proteinsSortedBy = defaultView.getProteinsSortedBy();
		pushSortingOrder(proteinsSortedBy, proteinOrder, proteinSortingScore);
		GWT.log("Setting default view in ProteinTable END");

	}

	@Override
	public void pushSortingOrder(ColumnName columnName, ORDER order, String sortingScore) {
		GWT.log("Sorting ProteinTable by " + columnName);
		dataGrid.getColumnSortList().clear();
		Set<MyColumn<ProteinBean>> columns = getColumnManager().getColumnsByColumnName(columnName);
		if (columns != null && !columns.isEmpty()) {
			Column<ProteinBean, ?> column = (Column<ProteinBean, ?>) columns.iterator().next();
			if (column != null) {
				// dataGrid.sortColumn(column, order);
				dataGrid.pushColumnToColumnSortList(column, order);
			}
		}
	}

	/**
	 * Adds the selectionHandler to the protein table.
	 *
	 * @param selectionHandler
	 */
	public void addProteinSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	private MyDataGrid<ProteinBean> makeDataGrid() {
		final ProvidesKey<ProteinBean> KEY_PROVIDER = new ProvidesKey<ProteinBean>() {
			@Override
			public Object getKey(ProteinBean item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		MyDataGrid<ProteinBean> dataGrid = new MyDataGrid<ProteinBean>(KEY_PROVIDER);
		dataGrid.setEmptyTableWidget(new Label(cwDataGridEmpty));

		return dataGrid;
	}

	private SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true, 5, true);
		simplePager.setPageSize(SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(boolean addCheckBoxSelection) {
		if (addCheckBoxSelection) {
			// first column, the checkboxex for selection
			Column<ProteinBean, Boolean> checkColumn = new Column<ProteinBean, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(ProteinBean object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			dataGrid.setColumnWidth(checkColumn, 30, Unit.PX);
		}
		// rest of the columns
		for (MyColumn<ProteinBean> myColumn : proteinColumnManager.getColumns()) {
			ColumnName columnName = myColumn.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PROTEIN_AMOUNT && columnName != ColumnName.PROTEIN_RATIO) {
				final boolean visible = proteinColumnManager.isVisible(columnName);
				final Header<String> footer = proteinColumnManager.getFooter(columnName);

				dataGrid.addColumn(columnName,
						(Column<ProteinBean, ?>) myColumn, new MySafeHtmlHeaderWithTooltip(columnName,
								SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription()),
						footer);

				if (visible) {
					dataGrid.setColumnWidth((Column<ProteinBean, ?>) myColumn, myColumn.getDefaultWidth(),
							myColumn.getDefaultWidthUnit());
				} else {
					dataGrid.setColumnWidth((Column<ProteinBean, ?>) myColumn, 0, myColumn.getDefaultWidthUnit());
				}
			}
		}
	}

	/**
	 * @return the dataProvider
	 */
	public AsyncProteinBeanListDataProvider getAsyncDataProvider() {
		return asyncProteinDataListProvider;
	}

	/**
	 * @return the dataGrid
	 */
	public DataGrid<ProteinBean> getDataGrid() {
		return dataGrid;
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinBean> mycolumn : proteinColumnManager.getColumnsByColumnName(columnName)) {

			final Column<ProteinBean, String> column = (Column<ProteinBean, String>) mycolumn;
			if (show) {
				final String newWidth = String.valueOf(mycolumn.getDefaultWidth())
						+ mycolumn.getDefaultWidthUnit().getType();
				final String columnWidth = dataGrid.getColumnWidth(column);
				if (!columnWidth.equals(newWidth)) {

					if (dataGrid.isEmptyColumn(column))
						redraw = true;
					dataGrid.setColumnWidth(column, newWidth);
					mycolumn.setWidth(mycolumn.getDefaultWidth());
				}
			} else {
				final String newWidth = "0.0" + mycolumn.getDefaultWidthUnit().getType();
				final String columnWidth = dataGrid.getColumnWidth(column);
				if (!newWidth.equals(columnWidth)) {
					if (!dataGrid.isNumberColumn(column))
						redraw = true;
					dataGrid.setColumnWidth(column, newWidth);
					mycolumn.setWidth(0);
				}
			}

		}
		dataGrid.setForceToRefresh(true);
		// if (true || redraw) {
		dataGrid.redrawVisibleItems();
		// dataGrid.updateTableMinimumWidth();
		// }
	}

	// @Override
	// public void showOrHideExperimentalConditionColumn(ColumnName columnName,
	// String conditionName, String projectName, boolean show) {
	// boolean redraw = false;
	// for (MyColumn<ProteinBean> mycolumn : proteinColumnManager
	// .getColumnsByColumnName(columnName)) {
	//
	// final ProteinTextColumn column = (ProteinTextColumn) mycolumn;
	//
	// if (projectName.equalsIgnoreCase(column.getProjectName())
	// && (conditionName.equalsIgnoreCase(column
	// .getExperimentalConditionName()) || conditionName
	// .equalsIgnoreCase(column
	// .getExperimentalCondition2Name()))) {
	// if (show) {
	// final String columnWidth = dataGrid.getColumnWidth(column);
	// final String newWidth = String.valueOf(column
	// .getDefaultWidth())
	// + column.getDefaultWidthUnit().getType();
	// if (!columnWidth.equals(newWidth)) {
	// redraw = true;
	// dataGrid.setColumnWidth(column, newWidth);
	// column.setWidth(column.getDefaultWidth());
	// }
	// } else {
	// final String columnWidth = dataGrid.getColumnWidth(column);
	// final String newWidth = "0.0"
	// + column.getDefaultWidthUnit().getType();
	// if (!columnWidth.equals(newWidth)) {
	// redraw = true;
	// dataGrid.setColumnWidth(column, newWidth);
	// column.setWidth(0);
	// }
	// }
	// }
	// }
	// if (redraw)
	// dataGrid.redraw();
	//
	// }

	@Override
	public void showOrHideExperimentalConditionColumn(ColumnName columnName, Set<String> conditionNames,
			String projectName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinBean> mycolumn : proteinColumnManager.getColumnsByColumnName(columnName)) {

			final Column<ProteinBean, String> column = (Column<ProteinBean, String>) mycolumn;
			String condition1ReferredByColumn = mycolumn.getExperimentalConditionName();
			String condition2ReferredByColumn = mycolumn.getExperimentalCondition2Name() != null
					? mycolumn.getExperimentalCondition2Name() : condition1ReferredByColumn;

			if (projectName == null || mycolumn.getProjectTag().equalsIgnoreCase(projectName)) {
				if (conditionNames == null || (conditionNames.contains(condition1ReferredByColumn)
						&& conditionNames.contains(condition2ReferredByColumn))) {
					if (show) {
						final String columnWidth = dataGrid.getColumnWidth(column);
						final String newWidth = String.valueOf(mycolumn.getDefaultWidth())
								+ mycolumn.getDefaultWidthUnit().getType();
						if (!columnWidth.equals(newWidth)) {
							if (dataGrid.isEmptyColumn(column))
								redraw = true;
							dataGrid.setColumnWidth(column, newWidth);
							mycolumn.setWidth(mycolumn.getDefaultWidth());
						}
					} else {
						final String columnWidth = dataGrid.getColumnWidth(column);
						final String newWidth = "0.0" + mycolumn.getDefaultWidthUnit().getType();
						if (!columnWidth.equals(newWidth)) {
							if (!dataGrid.isNumberColumn(column))
								redraw = true;
							dataGrid.setColumnWidth(column, newWidth);
							mycolumn.setWidth(0);
						}
					}
				}
			}
		}
		dataGrid.setForceToRefresh(true);
		// if (true || redraw) {
		dataGrid.redrawVisibleItems();
		// dataGrid.updateTableMinimumWidth();
		// }
	}

	public void addColumnforConditionProteinAmount(String conditionName, AmountType amountType, String conditionID,
			String projectTag) {
		// check first if the column is already present or not
		if (!proteinColumnManager.containsColumn(ColumnName.PROTEIN_AMOUNT, conditionName, amountType, projectTag)) {
			Column<ProteinBean, String> column = proteinColumnManager.addProteinAmountColumn(
					ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_AMOUNT).isVisible(), conditionName,
					amountType, projectTag);
			MyColumn<ProteinBean> myColumn = (MyColumn<ProteinBean>) column;

			Header<String> footer = myColumn.getFooter();
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PROTEIN_AMOUNT,
					SafeHtmlUtils.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionID)),
					SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectTag));
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, myColumn.getWidth(), myColumn.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public void addColumnforConditionProteinRatio(String condition1Name, String condition1ID, String condition2Name,
			String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!proteinColumnManager.containsColumn(ColumnName.PROTEIN_RATIO, condition1Name, condition2Name, projectTag,
				ratioName)) {
			Column<ProteinBean, String> column = proteinColumnManager.addProteinRatioColumn(
					ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO).isVisible(), condition1Name,
					condition2Name, projectTag, ratioName);
			MyColumn<ProteinBean> myColumn = (MyColumn<ProteinBean>) column;
			Header<String> footer = myColumn.getFooter();
			String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1ID, condition2ID);
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PROTEIN_RATIO,
					SafeHtmlUtils.fromSafeConstant(headerName),
					SharedDataUtils.getRatioHeaderTooltip(condition1Name, condition2Name, ratioName));
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, myColumn.getWidth(), myColumn.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public void addColumnforConditionProteinRatioScore(String condition1Name, String condition1ID,
			String condition2Name, String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!proteinColumnManager.containsColumn(ColumnName.PROTEIN_RATIO_SCORE, condition1Name, condition2Name,
				projectTag, ratioName)) {
			Column<ProteinBean, String> column = proteinColumnManager.addProteinRatioScoreColumn(
					ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO_SCORE).isVisible(), condition1Name,
					condition2Name, projectTag, ratioName);
			MyColumn<ProteinBean> myColumn = (MyColumn<ProteinBean>) column;
			Header<String> footer = myColumn.getFooter();
			String headerName = ColumnName.PROTEIN_RATIO_SCORE.getAbr();
			String tooltipText = "Confident score associated to ratio: " + ratioName;
			tooltipText += SharedConstants.SEPARATOR + "Ratio between conditions: " + condition1Name + " / "
					+ condition2Name;
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PROTEIN_RATIO_SCORE,
					SafeHtmlUtils.fromSafeConstant(headerName), tooltipText);
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, myColumn.getWidth(), myColumn.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	/**
	 * @return the proteinColumns
	 */
	public ProteinColumnManager getColumnManager() {
		return proteinColumnManager;
	}

	public void clearTable() {

		getAsyncDataProvider().updateRowCount(0, true);
		getAsyncDataProvider().updateRowData(0, Collections.EMPTY_LIST);

		refreshData();
	}

	@Override
	public void removeColumn(ColumnName columnName) {
		proteinColumnManager.removeColumns(columnName);
		dataGrid.removeColumns(columnName);
	}

	public void selectFirstRow() {
		GWT.log("Selecting First row in ProteinTable");

		ProteinBean firstProteinBean = getFirstRowProteinBean();
		if (firstProteinBean != null) {
			selectionModel.setSelected(firstProteinBean, true);
		}
		GWT.log("Selecting First row in ProteinTable END");

	}

	@Override
	public void refreshData() {
		// dataProvider.refresh();
		// dataGrid.redrawVisibleItems();
		RangeChangeEvent.fire(dataGrid, dataGrid.getVisibleRange());
	}

	@Override
	public void forceReloadData() {
		dataGrid.setForceToRefresh(true);
	}

	public ProteinBean getFirstRowProteinBean() {
		GWT.log("Getting First row ProteinBean from ProteinTable");
		refreshData();
		final List<ProteinBean> visibleItems = dataGrid.getVisibleItems();
		if (visibleItems != null && !visibleItems.isEmpty()) {
			ProteinBean firstProteinBean = visibleItems.get(0);
			GWT.log("First row ProteinBean is " + firstProteinBean.getPrimaryAccession().getAccession());
			return firstProteinBean;
		}
		GWT.log("No proteinBean in first row in ProteinTable");
		return null;
	}

	public void onResize() {
		for (int i = 0; i < getWidgetCount(); i++) {
			Widget child = getWidget(i);
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}
}
