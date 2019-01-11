package edu.scripps.yates.client.gui.components;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
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

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.CustomTextColumn;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MyIdColumn;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ContainsData;
import edu.scripps.yates.client.interfaces.ContainsDefaultView;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;

public abstract class AbstractDataTable<T> extends Composite
		implements ContainsDefaultView, HasColumns, ContainsData, ProvidesResize, RequiresResize {

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
	protected final String tableName;
	private final QueryPanel queryPanel;

	public AbstractDataTable(String sessionID, String emptyLabel, AbstractAsyncDataProvider<T> asyncDataListProvider,
			boolean multipleSelectionModel, String tableName, QueryPanel queryPanel) {
		this(sessionID, new Label(emptyLabel), asyncDataListProvider, multipleSelectionModel, tableName, queryPanel);
	}

	public AbstractDataTable(String sessionID, Widget emptyWidget, AbstractAsyncDataProvider<T> asyncDataListProvider,
			boolean multipleSelectionModel, String tableName, QueryPanel queryPanel) {
		this.tableName = tableName;
		this.sessionID = sessionID;
		this.queryPanel = queryPanel;
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
		final SimplePanel scroll = new SimplePanel(dataGrid);
		scroll.setSize("100%", "92%");
		scroll.setStyleName("HorizontalScroll");
		mainPanel.add(scroll);
		// }

		// loading panel
		final Image imageLoading = new Image(myClientBundle.horizontalLoader());
		loadingPanel = new VerticalPanel();
		loadingPanel.add(imageLoading);
		// add the pager
		pager = makePager();
		mainPanel.add(pager);
		// Add the CellList to the adapter in the database.
		asyncDataListProvider.addDataDisplay(dataGrid);
		final AsyncHandler asyncHandler = new AsyncHandler(dataGrid);
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
			dataGrid.setSelectionModel(selectionModel, DefaultSelectionEventManager.<T>createCheckboxManager());
		} else {
			selectionModel = new SingleSelectionModel<T>();
			dataGrid.setSelectionModel(selectionModel);
		}
		dataGrid.redraw();
	}

	protected abstract AbstractColumnManager<T> createColumnManager(FooterManager<T> footerManager);

	protected abstract FooterManager<T> createFooterManager(MyDataGrid<T> dataGrid);

	public final void initTableColumns(boolean addCheckBoxSelection) {
		if (addCheckBoxSelection) {
			// first column, the checkboxex for selection
			final Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(T object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			dataGrid.setColumnWidth(checkColumn, 30, Unit.PX);
		}
		// rest of the columns
		for (final MyColumn<T> myColumn : getColumnManager().getVisibleColumns()) {
			final ColumnName columnName = myColumn.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PROTEIN_AMOUNT && columnName != ColumnName.SPC_PER_CONDITION
					&& columnName != ColumnName.PROTEIN_RATIO && columnName != ColumnName.PROTEIN_SCORE
					&& columnName != ColumnName.PROTEIN_RATIO_SCORE && columnName != ColumnName.PROTEIN_RATIO_GRAPH
					&& columnName != ColumnName.PSM_AMOUNT && columnName != ColumnName.PSM_SCORE
					&& columnName != ColumnName.PTM_SCORE && columnName != ColumnName.PSM_RATIO
					&& columnName != ColumnName.PSM_RATIO_GRAPH && columnName != ColumnName.PSM_RATIO_SCORE
					&& columnName != ColumnName.PEPTIDE_AMOUNT && columnName != ColumnName.PEPTIDE_SCORE
					&& columnName != ColumnName.PEPTIDE_RATIO && columnName != ColumnName.PEPTIDE_RATIO_GRAPH
					&& columnName != ColumnName.PEPTIDE_RATIO_SCORE) {
				final boolean visible = getColumnManager().isVisible(columnName);
				if (visible) {
					final Header<String> footer = getColumnManager().getFooter(columnName);

					dataGrid.addColumn(columnName, (Column<T, ?>) myColumn, myColumn.getHeader(), footer);

					if (visible) {
						dataGrid.setColumnWidth((Column<T, ?>) myColumn, myColumn.getDefaultWidth(),
								myColumn.getDefaultWidthUnit());
					} else {
						dataGrid.setColumnWidth((Column<T, ?>) myColumn, 0, myColumn.getDefaultWidthUnit());
					}
				}
			}
		}
	}

	protected abstract SimplePager makePager();

	protected abstract MyDataGrid<T> makeDataGrid();

	public final void clearTable() {
		getAsyncDataProvider().updateRowCount(0, true);
		getAsyncDataProvider().updateRowData(0, Collections.<T>emptyList());
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
		this.setVisible(true);
		dataGrid.setForceToRefresh(true);
		RangeChangeEvent.fire(dataGrid, dataGrid.getVisibleRange());
	}

	@Override
	public final void onResize() {
		// GWT.log("Resizing DataTable in " + getClass().getCanonicalName());
		for (int i = 0; i < mainPanel.getWidgetCount(); i++) {
			final Widget child = mainPanel.getWidget(i);
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}

	public final void addColumnForScore(String scoreName, ColumnName columnName) {
		// check first if the column is already present or not
		if (!columnManager.containsScoreColumn(scoreName, columnName)) {
			final CustomTextColumn<T> column = columnManager.addScoreColumn(columnName, true, scoreName);
			if (column.isVisible()) {
				final Header<String> footer = column.getFooter();

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
			String conditionSymbol, AmountType amountType, String projectTag) {
		// check first if the column is already present or not
		if (!columnManager.containsColumn(columnName, conditionName, amountType, projectTag)) {
			final CustomTextColumn<T> column = columnManager.addAmountColumn(columnName, isVisible, conditionName,
					conditionSymbol, amountType, projectTag);
			if (column.isVisible()) {
				final Header<String> footer = column.getFooter();

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
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName) {
		if (columnName == ColumnName.PSM_RATIO) {
			GWT.log("Adding column for ratio " + ratioName + " cond1:" + condition1Name + " cond2:" + condition2Name);
		}
		// check first if the column is already present or not
		if (!columnManager.containsColumn(columnName, condition1Name, condition2Name, projectTag, ratioName)) {
			final CustomTextColumn<T> column = columnManager.addRatioColumn(columnName, isVisible, condition1Name,
					condition1Symbol, condition2Name, condition2Symbol, projectTag, ratioName);
			if (column.isVisible()) {
				final Header<String> footer = column.getFooter();

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
			String condition1Symbol, String condition2Name, String condition2Symbol, String projectTag,
			String ratioName, String scoreName) {
		if (columnName == ColumnName.PSM_RATIO) {
			GWT.log("Adding column for ratio " + ratioName + " and ratio score " + scoreName);
		}
		// check first if the column is already present or not
		if (!getColumnManager().containsColumn(columnName, condition1Name, condition2Name, projectTag, ratioName,
				scoreName)) {
			final CustomTextColumn<T> column = getColumnManager().addRatioScoreColumn(columnName, isVisible,
					condition1Name, condition1Symbol, condition2Name, condition2Symbol, projectTag, ratioName,
					scoreName);
			if (column.isVisible()) {
				dataGrid.addColumnToTable(column, getColumnManager());
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
		final Set<MyColumn<T>> columns = getColumnManager().getColumnsByColumnName(sortedBy);

		if (columns != null && !columns.isEmpty()) {
			if (sortingScore == null || "".equals(sortingScore)) {
				final Column<T, ?> column = (Column<T, ?>) columns.iterator().next();
				if (column != null) {
					// dataGrid.sortColumn(column, order);
					dataGrid.pushColumnToColumnSortList(column, order);
				}
			} else {
				for (final MyColumn<T> myColumn : columns) {
					if (myColumn instanceof MyIdColumn) {
						final MyIdColumn<T> idColumn = (MyIdColumn<T>) myColumn;
						if (sortingScore.equalsIgnoreCase(idColumn.getScoreName())) {
							final Column<T, ?> column = (Column<T, ?>) myColumn;
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

	@Override
	public final void showOrHideColumn(ColumnName columnName, boolean show) {

		for (final MyColumn<T> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

			final Column<T, ?> column = (Column<T, ?>) mycolumn;
			mycolumn.setVisible(show);
			if (show) {
				dataGrid.addColumnToTable(column, getColumnManager());
				// Scheduler.get().scheduleDeferred(getShowTableCommand());
			} else {
				dataGrid.removeColumn(column);
			}

		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				dataGrid.redrawVisibleItems();
				// reloadData();
			}
		});

	}

	private ScheduledCommand getShowTableCommand() {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				if (queryPanel != null) {
					queryPanel.selectDataTab(AbstractDataTable.this);
				}
			}
		};
	}

	@Override
	public final void showOrHideExperimentalConditionColumn(ColumnName columnName, Set<String> conditionNames,
			String projectName, boolean show) {
		for (final MyColumn<T> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {
			if (mycolumn instanceof MyIdColumn) {
				final MyIdColumn<T> idColumn = (MyIdColumn<T>) mycolumn;
				final Column<T, String> column = (Column<T, String>) mycolumn;
				final String condition1ReferredByColumn = idColumn.getExperimentalConditionName();
				final String condition2ReferredByColumn = idColumn.getExperimentalCondition2Name() != null
						? idColumn.getExperimentalCondition2Name() : condition1ReferredByColumn;

				if (projectName == null || idColumn.getProjectTag().equalsIgnoreCase(projectName)) {
					if (conditionNames == null || (conditionNames.contains(condition1ReferredByColumn)
							&& conditionNames.contains(condition2ReferredByColumn))) {

						if (show) {
							dataGrid.addColumnToTable(column, getColumnManager());
							// Scheduler.get().scheduleDeferred(getShowTableCommand());
						} else {
							dataGrid.removeColumn(column);
						}
					}
				}
			}
		}
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				dataGrid.redrawVisibleItems();
				// reloadData();
			}
		});

	}

	@Override
	public final void showOrHideExperimentalConditionColumn(String keyName, boolean show) {
		boolean found = false;
		for (final MyColumn<T> mycolumn : getColumnManager().getColumns()) {
			if (mycolumn instanceof MyIdColumn) {
				final MyIdColumn<T> idColumn = (MyIdColumn<T>) mycolumn;
				final Column<T, String> column = (Column<T, String>) mycolumn;
				if (idColumn.getKeyName() != null && idColumn.getKeyName().equals(keyName)) {
					found = true;
					if (show) {
						GWT.log(idColumn.getColumnName().name() + " " + idColumn.getKeyName() + " <-> " + keyName);
						dataGrid.addColumnToTable(column, getColumnManager());
						// Scheduler.get().scheduleDeferred(getShowTableCommand());
					} else {
						dataGrid.removeColumn(column);
					}
				}

			}
		}
		if (found) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					dataGrid.redrawVisibleItems();
					// reloadData();
				}
			});
		}
	}

	@Override
	public final void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in " + getClass().getSimpleName() + " table");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getProteinPageSize());
			pager.setPage(0);
		}
		// get default views on proteins
		final List<ColumnWithVisibility> itemDefaultView = getItemDefaultView(defaultView);
		for (final ColumnWithVisibility columnWithVisibility : itemDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}

		// get sorting parameters for the proteins
		final ORDER order = getItemOrder(defaultView);
		final String sortingScore = getSortingScore(defaultView);
		final ColumnName sortedBy = getSortedBy(defaultView);
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in " + getClass().getSimpleName() + " table END");
	}
}
