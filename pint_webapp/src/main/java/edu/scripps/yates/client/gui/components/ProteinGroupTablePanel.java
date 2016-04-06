package edu.scripps.yates.client.gui.components;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
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
import edu.scripps.yates.client.gui.columns.ProteinGroupColumnManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinGroupFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncProteinGroupBeanListDataProvider;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.client.interfaces.RefreshData;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinGroupTablePanel extends FlowPanel implements HasColumns, RefreshData, ProvidesResize {
	private final AsyncProteinGroupBeanListDataProvider asyncDataProvider;
	private final MyDataGrid<ProteinGroupBean> dataGrid;
	private final ProteinGroupColumnManager proteinColumnManager;
	private final ProteinGroupFooterManager footerManager;
	private SelectionModel<ProteinGroupBean> selectionModel;
	private final MouseOverHandler onMouseHandler;
	private final SimplePager pager;

	public ProteinGroupTablePanel(String sessionID, boolean multipleSelectionModel, boolean addCheckBoxSelection,
			MouseOverHandler mouseOverHandler) {
		super();
		onMouseHandler = mouseOverHandler;

		dataGrid = makeDataGrid();
		asyncDataProvider = new AsyncProteinGroupBeanListDataProvider(sessionID);

		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		resizeLayoutPanel.setSize("100%", "90%");
		resizeLayoutPanel.add(dataGrid);
		this.add(resizeLayoutPanel);

		footerManager = new ProteinGroupFooterManager(dataGrid);
		proteinColumnManager = new ProteinGroupColumnManager(footerManager, sessionID);

		proteinColumnManager.addChangeListener(this);

		// Add the CellList to the adapter in the database.
		asyncDataProvider.addDataDisplay(dataGrid);

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
			selectionModel = new MultiSelectionModel<ProteinGroupBean>();
			dataGrid.setSelectionModel(selectionModel,
					DefaultSelectionEventManager.<ProteinGroupBean> createCheckboxManager());
		} else {
			selectionModel = new SingleSelectionModel<ProteinGroupBean>();
			dataGrid.setSelectionModel(selectionModel);
		}

		dataGrid.redraw();

		final Label label = new Label("Click on 'Group Proteins' button under 'Protein inference' left menu");
		label.setTitle(label.getText());
		if (onMouseHandler != null)
			label.addMouseOverHandler(onMouseHandler);
		this.setEmptyTableWidget(label);
	}

	/**
	 * Adds the selectionHandler to the protein table.
	 *
	 * @param selectionHandler
	 */
	public void addProteinSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	private MyDataGrid<ProteinGroupBean> makeDataGrid() {
		final ProvidesKey<ProteinGroupBean> KEY_PROVIDER = new ProvidesKey<ProteinGroupBean>() {
			@Override
			public Object getKey(ProteinGroupBean item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		MyDataGrid<ProteinGroupBean> dataGrid = new MyDataGrid<ProteinGroupBean>(KEY_PROVIDER);

		return dataGrid;
	}

	public void setEmptyTableWidget(String emptyLabelString) {
		if (dataGrid != null) {
			if (emptyLabelString != null)
				dataGrid.setEmptyTableWidget(new Label(emptyLabelString));
			else
				dataGrid.setEmptyTableWidget(null);
			dataGrid.redraw();
		}
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

	private SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true, 5, true);

		simplePager.setPageSize(SharedConstants.PROTEINGROUP_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(boolean addCheckBoxSelection) {
		if (addCheckBoxSelection) {
			// first column, the checkboxex for selection
			Column<ProteinGroupBean, Boolean> checkColumn = new Column<ProteinGroupBean, Boolean>(
					new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(ProteinGroupBean object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			dataGrid.setColumnWidth(checkColumn, 30, Unit.PX);
		}
		// rest of the columns
		for (MyColumn<ProteinGroupBean> myColumn : proteinColumnManager.getColumns()) {
			ColumnName columnName = myColumn.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PROTEIN_AMOUNT && columnName != ColumnName.PROTEIN_RATIO) {
				final boolean visible = proteinColumnManager.isVisible(columnName);
				final Header<String> footer = proteinColumnManager.getFooter(columnName);

				dataGrid.addColumn(columnName,
						(Column<ProteinGroupBean, ?>) myColumn, new MySafeHtmlHeaderWithTooltip(columnName,
								SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription()),
						footer);

				if (visible) {
					dataGrid.setColumnWidth((Column<ProteinGroupBean, ?>) myColumn, myColumn.getDefaultWidth(),
							myColumn.getDefaultWidthUnit());
				} else {
					dataGrid.setColumnWidth((Column<ProteinGroupBean, ?>) myColumn, 0, myColumn.getDefaultWidthUnit());
				}
			}
		}
	}

	/**
	 * @return the dataProvider
	 */
	public AsyncProteinGroupBeanListDataProvider getAsyncDataProvider() {
		return asyncDataProvider;
	}

	/**
	 * @return the dataGrid
	 */
	public DataGrid<ProteinGroupBean> getDataGrid() {
		return dataGrid;
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinGroupBean> mycolumn : proteinColumnManager.getColumnsByColumnName(columnName)) {

			final Column<ProteinGroupBean, String> column = (Column<ProteinGroupBean, String>) mycolumn;
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
		// if (redraw)
		dataGrid.redrawVisibleItems();
	}

	// @Override
	// public void showOrHideExperimentalConditionColumn(ColumnName columnName,
	// String conditionName, String projectName, boolean show) {
	// boolean redraw = false;
	// for (MyColumn<ProteinGroupBean> mycolumn : proteinColumnManager
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
		for (MyColumn<ProteinGroupBean> mycolumn : proteinColumnManager.getColumnsByColumnName(columnName)) {

			final Column<ProteinGroupBean, String> column = (Column<ProteinGroupBean, String>) mycolumn;
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
		// if (redraw)
		dataGrid.redrawVisibleItems();

	}

	public void addColumnforConditionProteinAmount(String conditionName, AmountType amountType, String conditionID,
			String projectTag) {
		// check first if the column is already present or not
		if (!proteinColumnManager.containsColumn(ColumnName.PROTEIN_AMOUNT, conditionName, amountType, projectTag)) {
			Column<ProteinGroupBean, String> column = proteinColumnManager.addProteinAmountColumn(
					ProteinGroupColumns.getInstance().getColumn(ColumnName.PROTEIN_AMOUNT).isVisible(), conditionName,
					amountType, projectTag);
			MyColumn<ProteinGroupBean> myColumn = (MyColumn<ProteinGroupBean>) column;
			Header<String> footer = myColumn.getFooter();
			final SafeHtml headerName = SafeHtmlUtils.fromSafeConstant(amountType + " (" + conditionID + ")");
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PROTEIN_AMOUNT,
					headerName, "Amount type: " + amountType + SharedConstants.SEPARATOR + "Experimental condition: "
							+ conditionName + SharedConstants.SEPARATOR + "Project: " + projectTag);
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
			Column<ProteinGroupBean, String> column = proteinColumnManager.addProteinRatioColumn(
					ProteinGroupColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO).isVisible(), condition1Name,
					condition2Name, projectTag, ratioName);
			MyColumn<ProteinGroupBean> myColumn = (MyColumn<ProteinGroupBean>) column;

			Header<String> footer = myColumn.getFooter();
			String headerName = ratioName + "(" + condition1ID + " / " + condition2ID + ")";
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PROTEIN_RATIO,
					SafeHtmlUtils.fromSafeConstant(headerName),
					condition1Name + "/" + condition2Name + SharedConstants.SEPARATOR + ratioName);
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
	public ProteinGroupColumnManager getColumnManager() {
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

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in ProteinGroupTable");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getProteinGroupPageSize());
		} // get default views on proteins
		final List<ColumnWithVisibility> proteinGroupDefaultView = defaultView.getProteinGroupDefaultView();
		for (ColumnWithVisibility columnWithVisibility : proteinGroupDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}

		// get sorting parameters for the protein groups

		final ORDER order = defaultView.getProteinGroupOrder();
		final String sortingScore = defaultView.getProteinGroupSortingScore();
		final ColumnName sortedBy = defaultView.getProteinGroupsSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in ProteinGroupTable END");
	}

	@Override
	public void pushSortingOrder(ColumnName sortedBy, ORDER order, String sortingScore) {
		GWT.log("Sorting ProteinGroupTable by " + sortedBy);
		dataGrid.getColumnSortList().clear();
		Set<MyColumn<ProteinGroupBean>> columns = getColumnManager().getColumnsByColumnName(sortedBy);

		if (columns != null && !columns.isEmpty()) {
			Column<ProteinGroupBean, ?> column = (Column<ProteinGroupBean, ?>) columns.iterator().next();
			if (column != null) {
				// dataGrid.sortColumn(column, order);
				dataGrid.pushColumnToColumnSortList(column, order);
			}
		}
	}

	public void selectFirstRow() {
		GWT.log("Selecting First row in ProteinGroupTable");

		ProteinGroupBean firstProteinGRoupBean = getFirstRowProteinGroupBean();
		if (firstProteinGRoupBean != null) {
			selectionModel.setSelected(firstProteinGRoupBean, true);
		}
		GWT.log("Selecting First row in ProteinGroupTable END");

	}

	@Override
	public void refreshData() {
		// dataGrid.redrawVisibleItems();
		RangeChangeEvent.fire(dataGrid, dataGrid.getVisibleRange());
	}

	@Override
	public void forceReloadData() {
		dataGrid.setForceToRefresh(true);
	}

	public ProteinGroupBean getFirstRowProteinGroupBean() {
		GWT.log("Getting First row ProteinGroupBean from ProteinGroupTable");
		refreshData();
		final List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
		if (visibleItems != null && !visibleItems.isEmpty()) {
			ProteinGroupBean firstProteinGroupBean = visibleItems.get(0);
			GWT.log("First row ProteinGroupBean is " + firstProteinGroupBean.getPrimaryAccessionsString());
			return firstProteinGroupBean;
		}
		GWT.log("No proteinGroupBean in first row in ProteingroupTable");
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
