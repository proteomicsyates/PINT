package edu.scripps.yates.client.gui.components;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.gui.columns.ColumnManager;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.PSMColumnManager;
import edu.scripps.yates.client.gui.columns.PSMTextColumn;
import edu.scripps.yates.client.gui.columns.footers.PSMFooterManager;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.client.interfaces.RefreshData;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PSMTablePanel extends Composite implements HasColumns, RefreshData, ProvidesResize {
	public static final String SELECT_PROTEIN_TO_LOAD_PSMS_TEXT = "Select one protein to load PSMs";
	public static final String NO_DATA_FROM_THIS_PROTEIN = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for PSMs to be loaded...";
	private final MyDataGrid<PSMBean> dataGrid;
	private final PSMColumnManager psmColumnManager;
	private final PSMFooterManager footerManager;
	private VerticalPanel loadingPanel;
	private final String emptyLabelString;
	private final ShowHiddePanel psmLoaderFromProjects;
	private final AsyncDataProvider<PSMBean> asyncDataListProvider;

	private final SingleSelectionModel<PSMBean> selectionModel;

	private final SimplePager pager;
	private final FlowPanel mainPanel;

	public PSMTablePanel(String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AsyncDataProvider<PSMBean> asyncDataListProvider) {
		super();
		emptyLabelString = emptyLabel;
		if (emptyLabelString != null)
			setEmptyTableWidget(emptyLabelString);
		psmLoaderFromProjects = showhiddePSMPanel;
		mainPanel = new FlowPanel();
		initWidget(mainPanel);
		dataGrid = makeDataGrid();
		footerManager = new PSMFooterManager(dataGrid);
		psmColumnManager = new PSMColumnManager(footerManager);

		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		resizeLayoutPanel.setSize("100%", "90%");
		resizeLayoutPanel.add(dataGrid);
		mainPanel.add(resizeLayoutPanel);

		psmColumnManager.addChangeListener(this);

		// Add the CellList to the adapter in the database.
		this.asyncDataListProvider = asyncDataListProvider;
		asyncDataListProvider.addDataDisplay(dataGrid);
		AsyncHandler asyncHandler = new AsyncHandler(dataGrid);
		dataGrid.addColumnSortHandler(asyncHandler);

		dataGrid.setAutoHeaderRefreshDisabled(true);

		// Initialize the columns.
		initTableColumns();

		// add the pager
		pager = makePager();
		mainPanel.add(pager);

		// add the selection manager
		selectionModel = new SingleSelectionModel<PSMBean>();
		dataGrid.setSelectionModel(selectionModel);

		dataGrid.redraw();
	}

	private MyDataGrid<PSMBean> makeDataGrid() {
		/**
		 * The key provider that provides the unique ID of a PSMBean.
		 */
		final ProvidesKey<PSMBean> KEY_PROVIDER = new ProvidesKey<PSMBean>() {
			@Override
			public Object getKey(PSMBean item) {
				return item == null ? null : item.getPsmID();
			}
		};
		MyDataGrid<PSMBean> dataGrid = new MyDataGrid<PSMBean>(KEY_PROVIDER);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;

		Image imageLoading = new Image(myClientBundle.horizontalLoader());
		loadingPanel = new VerticalPanel();
		loadingPanel.add(imageLoading);

		return dataGrid;
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

	private SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true, 5, true);
		simplePager.setPageSize(SharedConstants.PSM_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns() {

		for (MyColumn<PSMBean> column : psmColumnManager.getColumns()) {
			ColumnName columnName = column.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PSM_AMOUNT && columnName != ColumnName.PSM_SCORE
					&& columnName != ColumnName.PTM_SCORE && columnName != ColumnName.PSM_RATIO) {
				final Header<String> footer = psmColumnManager.getFooter(columnName);

				dataGrid.addColumn(columnName,
						(Column<PSMBean, ?>) column, new MySafeHtmlHeaderWithTooltip(columnName,
								SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription()),
						footer);

				if (column.isVisible()) {
					dataGrid.setColumnWidth((Column<PSMBean, ?>) column, column.getDefaultWidth(),
							column.getDefaultWidthUnit());
				} else {
					dataGrid.setColumnWidth((Column<PSMBean, ?>) column, 0, column.getDefaultWidthUnit());
				}
			}
		}
	}

	/**
	 * @return the dataProvider
	 */
	public AsyncDataProvider<PSMBean> getAsyncDataProvider() {
		return asyncDataListProvider;
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;

		for (MyColumn<PSMBean> mycolumn : psmColumnManager.getColumnsByColumnName(columnName)) {

			final PSMTextColumn column = (PSMTextColumn) mycolumn;

			if (show) {
				final String newWidth = String.valueOf(column.getDefaultWidth())
						+ column.getDefaultWidthUnit().getType();
				final String columnWidth = dataGrid.getColumnWidth(column);
				if (!newWidth.equals(columnWidth)) {
					if (dataGrid.isEmptyColumn(column))
						redraw = true;
					dataGrid.setColumnWidth(column, newWidth);
					column.setWidth(column.getDefaultWidth());
				}

			} else {
				final String newWidth = "0.0" + column.getDefaultWidthUnit().getType();
				final String columnWidth = dataGrid.getColumnWidth(column);
				if (!newWidth.equals(columnWidth)) {
					if (!dataGrid.isNumberColumn(column))
						redraw = true;
					dataGrid.setColumnWidth(column, newWidth);
					column.setWidth(0);
				}
			}

		}
		dataGrid.setForceToRefresh(true);
		// if (redraw) {
		dataGrid.redrawVisibleItems();
		// dataGrid.updateTableMinimumWidth();
		// }

	}

	// @Override
	// public void showOrHideExperimentalConditionColumn(ColumnName columnName,
	// String conditionName, String projectName, boolean show) {
	// boolean redraw = false;
	// for (MyColumn<PSMBean> mycolumn : psmColumnManager
	// .getColumnsByColumnName(columnName)) {
	//
	// final PSMTextColumn column = (PSMTextColumn) mycolumn;
	//
	// if (projectName.equalsIgnoreCase(column.getProjectName())
	// && (conditionName.equalsIgnoreCase(column
	// .getExperimentalConditionName()) || conditionName
	// .equalsIgnoreCase(column
	// .getExperimentalCondition2Name()))) {
	//
	// System.out.println(column.getExperimentalConditionName() + " "
	// + column.getExperimentalCondition2Name() + " " + show);
	//
	// if (show) {
	// final String newWidth = String.valueOf(column
	// .getDefaultWidth())
	// + column.getDefaultWidthUnit().getType();
	// final String columnWidth = dataGrid.getColumnWidth(column);
	// if (!columnWidth.equals(newWidth)) {
	// redraw = true;
	// dataGrid.setColumnWidth(column, newWidth);
	// column.setWidth(column.getDefaultWidth());
	// }
	// } else {
	// final String newWidth = "0.0"
	// + column.getDefaultWidthUnit().getType();
	// final String columnWidth = dataGrid.getColumnWidth(column);
	// if (!columnWidth.equals(newWidth)) {
	// redraw = true;
	// dataGrid.setColumnWidth(column, newWidth);
	// column.setWidth(0);
	// }
	// }
	// }
	// }
	// if (redraw)
	// dataProvider.refresh();
	//
	// }

	@Override
	public void showOrHideExperimentalConditionColumn(ColumnName columnName, Set<String> conditionNames,
			String projectName, boolean show) {
		boolean redraw = false;
		for (MyColumn<PSMBean> mycolumn : psmColumnManager.getColumnsByColumnName(columnName)) {

			final PSMTextColumn column = (PSMTextColumn) mycolumn;
			String condition1ReferredByColumn = column.getExperimentalConditionName();
			String condition2ReferredByColumn = column.getExperimentalCondition2Name() != null
					? column.getExperimentalCondition2Name() : condition1ReferredByColumn;

			if (projectName == null || column.getProjectTag().equalsIgnoreCase(projectName)) {
				if (conditionNames == null || (conditionNames.contains(condition1ReferredByColumn)
						&& conditionNames.contains(condition2ReferredByColumn))) {

					if (show) {
						final String newWidth = String.valueOf(column.getDefaultWidth())
								+ column.getDefaultWidthUnit().getType();
						final String columnWidth = dataGrid.getColumnWidth(column);
						if (!columnWidth.equals(newWidth)) {
							if (dataGrid.isEmptyColumn(column))
								redraw = true;
							dataGrid.setColumnWidth(column, newWidth);
							column.setWidth(column.getDefaultWidth());
						}
					} else {
						final String newWidth = "0.0" + column.getDefaultWidthUnit().getType();
						final String columnWidth = dataGrid.getColumnWidth(column);
						if (!columnWidth.equals(newWidth)) {
							if (!dataGrid.isNumberColumn(column))
								redraw = true;
							dataGrid.setColumnWidth(column, newWidth);
							column.setWidth(0);
						}
					}
				}
			}
		}
		if (redraw) {
			dataGrid.redrawVisibleItems();
			dataGrid.updateTableMinimumWidth();
		}

	}

	public void addColumnforConditionPSMAmount(String conditionName, AmountType amountType, String conditionID,
			String projectTag) {
		// check first if the column is already present or not
		if (!psmColumnManager.containsColumn(ColumnName.PSM_AMOUNT, conditionName, amountType, projectTag)) {
			PSMTextColumn column = psmColumnManager.addPSMAmountColumn(ColumnName.PSM_AMOUNT,
					PSMColumns.getInstance().getColumn(ColumnName.PSM_AMOUNT).isVisible(), conditionName, amountType,
					projectTag);

			Header<String> footer = column.getFooter();
			final SafeHtml headerName = SafeHtmlUtils
					.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionID));
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PSM_AMOUNT,
					headerName, SharedDataUtils.getAmountHeaderTooltip(amountType, conditionName, projectTag));
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public void addColumnforConditionPSMRatio(String condition1Name, String condition1ID, String condition2Name,
			String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!psmColumnManager.containsColumn(ColumnName.PSM_RATIO, condition1Name, condition2Name, projectTag,
				ratioName)) {
			PSMTextColumn column = psmColumnManager.addPSMRatioColumn(
					PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO).isVisible(), condition1Name,
					condition2Name, projectTag, ratioName);

			Header<String> footer = column.getFooter();
			String headerName = SharedDataUtils.getRatioHeader(ratioName, condition1ID, condition2ID);
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PSM_RATIO,
					SafeHtmlUtils.fromSafeConstant(headerName),
					SharedDataUtils.getRatioHeaderTooltip(condition1Name, condition2Name, ratioName));
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public void addColumnforPSMScore(String scoreName) {
		// check first if the column is already present or not
		if (!psmColumnManager.containsPSMScoreColumn(scoreName)) {
			PSMTextColumn column = psmColumnManager.addPSMScoreColumn(ColumnName.PSM_SCORE, true, scoreName);

			Header<String> footer = column.getFooter();
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PSM_SCORE,
					SafeHtmlUtils.fromSafeConstant(scoreName), scoreName);
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	public void addColumnforPTMScore(String scoreName) {
		// check first if the column is already present or not
		if (!psmColumnManager.containsPTMScoreColumn(scoreName)) {
			PSMTextColumn column = psmColumnManager.addPTMScoreColumn(ColumnName.PTM_SCORE, true, scoreName);

			Header<String> footer = column.getFooter();
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PTM_SCORE,
					SafeHtmlUtils.fromSafeConstant(scoreName), "PTM score: " + scoreName);
			if (footer != null) {
				dataGrid.addColumn(column, header, footer);
			} else {
				dataGrid.addColumn(column, header);
			}
			dataGrid.setColumnWidth(column, column.getWidth(), column.getDefaultWidthUnit());
			// it is not necessary to draw because by default the column will be
			// hidden (width=0)
			// dataGrid.redraw();
		}
	}

	/**
	 * @return the proteinColumns
	 */
	public ColumnManager<PSMBean> getColumnManager() {
		return psmColumnManager;
	}

	public void clearTable() {
		getAsyncDataProvider().updateRowCount(0, true);
		getAsyncDataProvider().updateRowData(0, Collections.EMPTY_LIST);
		refreshData();
	}

	@Override
	public void removeColumn(ColumnName columnName) {
		psmColumnManager.removeColumns(columnName);
		dataGrid.removeColumns(columnName);
	}

	/**
	 * After calling this method, the empty widget will be the loading bar
	 */
	public void setLoadingWidget() {
		dataGrid.setEmptyTableWidget(loadingPanel);

	}

	public void hiddePSMPanel() {
		if (psmLoaderFromProjects != null) {
			psmLoaderFromProjects.hiddePanel();
		}
	}

	public void showPSMPanel() {
		if (psmLoaderFromProjects != null) {
			psmLoaderFromProjects.showPanel();
		}
	}

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in PSMTable");

		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getPsmPageSize());
		}
		// get default views on proteins
		final List<ColumnWithVisibility> psmsDefaultView = defaultView.getPsmDefaultView();
		for (ColumnWithVisibility columnWithVisibility : psmsDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}
		// get sorting parameters for the protein groups
		final ORDER order = defaultView.getPsmOrder();
		final String sortingScore = defaultView.getPsmSortingScore();
		final ColumnName sortedBy = defaultView.getPsmsSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in PSMTable END");
	}

	@Override
	public void pushSortingOrder(ColumnName sortedBy, ORDER order, String sortingScore) {
		GWT.log("Sorting PSMTable");
		dataGrid.getColumnSortList().clear();
		Set<MyColumn<PSMBean>> columns = getColumnManager().getColumnsByColumnName(sortedBy);

		if (columns != null && !columns.isEmpty()) {
			if (sortingScore == null || "".equals(sortingScore)) {
				Column<PSMBean, ?> column = (Column<PSMBean, ?>) columns.iterator().next();
				if (column != null) {
					// dataGrid.sortColumn(column, order);
					dataGrid.pushColumnToColumnSortList(column, order);
				}
			} else {
				for (MyColumn<PSMBean> myColumn : columns) {
					if (sortingScore.equalsIgnoreCase(myColumn.getScoreName())) {
						Column<PSMBean, ?> column = (Column<PSMBean, ?>) myColumn;
						// dataGrid.sortColumn(column, order);
						dataGrid.pushColumnToColumnSortList(column, order);
					}
				}
			}
		}
	}

	public void selectFirstRow() {
		GWT.log("Selecting First row in PSMTable");

		final PSMBean firstRowPSMBean = getFirstRowPSMBean();
		if (firstRowPSMBean != null) {
			selectionModel.setSelected(firstRowPSMBean, true);
		}
		GWT.log("Selecting First row in PSMTable END");

	}

	public PSMBean getFirstRowPSMBean() {
		GWT.log("Getting First row PSMBean from PsmTable");
		refreshData();
		final List<PSMBean> visibleItems = dataGrid.getVisibleItems();
		if (visibleItems != null && !visibleItems.isEmpty()) {
			PSMBean firstPSMBean = visibleItems.get(0);
			GWT.log("First row PSMBean is " + firstPSMBean.getPsmID());
			return firstPSMBean;
		}
		GWT.log("No PSMBean in first row in PSMTable");
		return null;
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

	public void onResize() {
		for (int i = 0; i < mainPanel.getWidgetCount(); i++) {
			Widget child = mainPanel.getWidget(i);
			if (child instanceof RequiresResize) {
				((RequiresResize) child).onResize();
			}
		}
	}
}