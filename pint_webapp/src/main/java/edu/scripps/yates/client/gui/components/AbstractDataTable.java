package edu.scripps.yates.client.gui.components;

import java.util.Collections;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel.AbstractSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.CustomTextColumn;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MyIdColumn;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ContainsData;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public abstract class AbstractDataTable<T> extends Composite
		implements HasColumns, ContainsData, ProvidesResize, RequiresResize {

	private final AsyncDataProvider<T> asyncDataListProvider;
	protected final MyDataGrid<T> dataGrid;
	protected final SimplePager pager;
	protected final FlowPanel mainPanel;
	protected final AbstractSelectionModel<T> selectionModel;
	private final AbstractColumnManager<T> columnManager;
	private final VerticalPanel loadingPanel;
	private final static MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
	private final FooterManager<T> footerManager;
	private final Widget emptyWidget;
	protected final String sessionID;

	public AbstractDataTable(String sessionID, String emptyLabel, AbstractAsyncDataProvider<T> asyncDataListProvider,
			boolean multipleSelectionModel) {
		this(sessionID, new Label(emptyLabel), asyncDataListProvider, multipleSelectionModel);
	}

	public AbstractDataTable(String sessionID, Widget emptyWidget, AbstractAsyncDataProvider<T> asyncDataListProvider,
			boolean multipleSelectionModel) {
		this.sessionID = sessionID;
		if (emptyWidget != null) {
			setEmptyTableWidget(emptyWidget);
		}
		this.emptyWidget = emptyWidget;
		mainPanel = new FlowPanel();
		initWidget(mainPanel);
		this.asyncDataListProvider = asyncDataListProvider;
		this.dataGrid = makeDataGrid();

		// if datagrid is a DataGrid instead of a CellTable
		// if (dataGrid instanceof DataGrid<T>) {
		// ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		// resizeLayoutPanel.setSize("100%", "92%");
		// resizeLayoutPanel.add(dataGrid);
		// mainPanel.add(resizeLayoutPanel);
		// } else if (dataGrid instanceof CellTable) {
		// include it in a scrollpanel with horizontal scroll
		SimplePanel scroll = new SimplePanel(dataGrid);
		scroll.setSize("100%", "92%");
		scroll.setStyleName("HorizontalScroll");
		mainPanel.add(scroll);
		// }

		// loading panel
		Image imageLoading = new Image(myClientBundle.horizontalLoader());
		loadingPanel = new VerticalPanel();
		loadingPanel.add(imageLoading);
		// add the pager
		pager = makePager();
		mainPanel.add(pager);
		// Add the CellList to the adapter in the database.
		asyncDataListProvider.addDataDisplay(dataGrid);
		AsyncHandler asyncHandler = new AsyncHandler(dataGrid);
		dataGrid.addColumnSortHandler(asyncHandler);
		dataGrid.setAutoHeaderRefreshDisabled(true);

		footerManager = createFooterManager(dataGrid);
		columnManager = createColumnManager(footerManager);
		columnManager.addChangeListener(this);

		// Initialize the columns.
		initTableColumns(false);

		// add the selection manager
		if (multipleSelectionModel) {
			selectionModel = new MultiSelectionModel<T>();
			dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<T> createCheckboxManager());
		} else {
			selectionModel = new SingleSelectionModel<T>();
			dataGrid.setSelectionModel(selectionModel);
		}
		dataGrid.redraw();
	}

	protected abstract AbstractColumnManager<T> createColumnManager(FooterManager<T> footerManager);

	protected abstract FooterManager<T> createFooterManager(MyDataGrid<T> dataGrid);

	protected abstract void initTableColumns(boolean addCheckBoxSelection);

	protected abstract SimplePager makePager();

	protected abstract MyDataGrid<T> makeDataGrid();

	public final void clearTable() {
		getAsyncDataProvider().updateRowCount(0, true);
		getAsyncDataProvider().updateRowData(0, Collections.<T> emptyList());
		setEmptyTableWidget(emptyWidget);
		refreshData();
	}

	/**
	 * @return the dataProvider
	 */
	public final AsyncDataProvider<T> getAsyncDataProvider() {
		return asyncDataListProvider;
	}

	/**
	 * @return the columnManager
	 */
	public final AbstractColumnManager<T> getColumnManager() {
		return columnManager;
	}

	@Override
	public final void reloadData() {
		dataGrid.setforceToReload(true);
		refreshData();
	}

	public final void setEmptyTableWidget(String emptyLabelString) {
		setEmptyTableWidget(new Label(emptyLabelString));
	}

	public final void setEmptyTableWidget(Widget emptyWidget) {
		if (dataGrid != null) {
			if (emptyWidget != null) {
				dataGrid.setEmptyTableWidget(emptyWidget);
			} else {
				dataGrid.setEmptyTableWidget(null);
			}
			dataGrid.redraw();
		}
	}

	@Override
	public final void refreshData() {
		// dataGrid.redrawVisibleItems();
		dataGrid.setForceToRefresh(true);
		RangeChangeEvent.fire(dataGrid, dataGrid.getVisibleRange());
	}

	@Override
	public final void onResize() {
		// GWT.log("Resizing DataTable in " + getClass().getCanonicalName());
		for (int i = 0; i < mainPanel.getWidgetCount(); i++) {
			Widget child = mainPanel.getWidget(i);
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}

	/**
	 * Apply the {@link DefaultView} to the data table
	 */
	@Override
	public abstract void setDefaultView(DefaultView defaultView);

	public final void addColumnForScore(String scoreName, ColumnName columnName) {
		// check first if the column is already present or not
		if (!columnManager.containsScoreColumn(scoreName, columnName)) {
			CustomTextColumn<T> column = columnManager.addScoreColumn(columnName, true, scoreName);
			if (column.isVisible()) {
				Header<String> footer = column.getFooter();

				if (footer != null) {
					dataGrid.addColumn(column, column.getHeader(), footer);
				} else {
					dataGrid.addColumn(column, column.getHeader());
				}
				dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			}
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public final void addColumnForConditionAmount(ColumnName columnName, boolean isVisible, String conditionName,
			AmountType amountType, String conditionID, String projectTag) {
		// check first if the column is already present or not
		if (!columnManager.containsColumn(columnName, conditionName, amountType, projectTag)) {
			CustomTextColumn<T> column = columnManager.addAmountColumn(columnName, isVisible, conditionName, amountType,
					projectTag);
			if (column.isVisible()) {
				Header<String> footer = column.getFooter();

				if (footer != null) {
					dataGrid.addColumn(column, column.getHeader(), footer);
				} else {
					dataGrid.addColumn(column, column.getHeader());
				}
				dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			}
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public final void addColumnForConditionRatio(ColumnName columnName, boolean isVisible, String condition1Name,
			String condition1ID, String condition2Name, String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!columnManager.containsColumn(columnName, condition1Name, condition2Name, projectTag, ratioName)) {
			CustomTextColumn<T> column = columnManager.addRatioColumn(columnName, isVisible, condition1Name,
					condition2Name, projectTag, ratioName);
			if (column.isVisible()) {
				Header<String> footer = column.getFooter();

				if (footer != null) {
					dataGrid.addColumn(column, column.getHeader(), footer);
				} else {
					dataGrid.addColumn(column, column.getHeader());
				}
				dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			}
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public final void addColumnForConditionRatioScore(ColumnName columnName, boolean isVisible, String condition1Name,
			String condition1ID, String condition2Name, String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!getColumnManager().containsColumn(columnName, condition1Name, condition2Name, projectTag, ratioName)) {
			CustomTextColumn<T> column = getColumnManager().addRatioScoreColumn(columnName, isVisible, condition1Name,
					condition2Name, projectTag, ratioName);
			if (column.isVisible()) {
				Header<String> footer = column.getFooter();
				String headerName = columnName.getAbr();
				String tooltipText = "Confident score associated to ratio: " + ratioName;
				tooltipText += SharedConstants.SEPARATOR + "Ratio between conditions: " + condition1Name + " / "
						+ condition2Name;
				final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
						SafeHtmlUtils.fromSafeConstant(headerName), tooltipText);
				if (footer != null) {
					dataGrid.addColumn(column, header, footer);
				} else {
					dataGrid.addColumn(column, header);
				}
				dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			}
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	@Override
	public final void removeColumn(ColumnName columnName) {
		columnManager.removeColumns(columnName);
		dataGrid.removeColumns(columnName);
	}

	@Override
	public final void pushSortingOrder(ColumnName sortedBy, ORDER order, String sortingScore) {
		GWT.log("Sorting Table in " + getClass().getCanonicalName());
		dataGrid.getColumnSortList().clear();
		Set<MyColumn<T>> columns = getColumnManager().getColumnsByColumnName(sortedBy);

		if (columns != null && !columns.isEmpty()) {
			if (sortingScore == null || "".equals(sortingScore)) {
				Column<T, ?> column = (Column<T, ?>) columns.iterator().next();
				if (column != null) {
					// dataGrid.sortColumn(column, order);
					dataGrid.pushColumnToColumnSortList(column, order);
				}
			} else {
				for (MyColumn<T> myColumn : columns) {
					if (myColumn instanceof MyIdColumn) {
						MyIdColumn<T> idColumn = (MyIdColumn<T>) myColumn;
						if (sortingScore.equalsIgnoreCase(idColumn.getScoreName())) {
							Column<T, ?> column = (Column<T, ?>) myColumn;
							// dataGrid.sortColumn(column, order);
							dataGrid.pushColumnToColumnSortList(column, order);
						}
					}
				}
			}
		}
	}

	/**
	 * After calling this method, the empty widget will be the loading bar
	 */
	public final void setLoadingWidget() {
		dataGrid.setEmptyTableWidget(loadingPanel);

	}

	/**
	 * Adds the selectionHandler to the table.
	 *
	 * @param selectionHandler
	 */
	public final void addSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	/**
	 * @return the dataGrid
	 */
	public MyDataGrid<T> getDataGrid() {
		return dataGrid;
	}

}
