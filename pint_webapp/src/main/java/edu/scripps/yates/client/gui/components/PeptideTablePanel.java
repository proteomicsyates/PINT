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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.gui.columns.ColumnManager;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.PeptideColumnManager;
import edu.scripps.yates.client.gui.columns.PeptideTextColumn;
import edu.scripps.yates.client.gui.columns.footers.PeptideFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.MyAsyncDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.client.interfaces.RefreshData;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PeptideTablePanel extends Composite implements HasColumns, RefreshData, ProvidesResize {
	public static final String SELECT_PEPTIDE_TO_LOAD_PSMS_TEXT = "Select one peptide to load PSMs";
	public static final String NO_DATA_FROM_THIS_PEPTIDE = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for Peptides to be loaded...";
	private final MyDataGrid<PeptideBean> dataGrid;
	private final PeptideColumnManager peptideColumnManager;
	private final PeptideFooterManager footerManager;
	private VerticalPanel loadingPanel;
	private final String emptyLabelString;
	private final ShowHiddePanel peptideLoaderFromProjects;
	private final MyAsyncDataProvider<PeptideBean> asyncDataListProvider;

	private final SingleSelectionModel<PeptideBean> selectionModel;

	private final SimplePager pager;
	private final FlowPanel mainPanel;

	public PeptideTablePanel(String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			MyAsyncDataProvider<PeptideBean> asyncDataListProvider) {
		super();
		emptyLabelString = emptyLabel;
		if (emptyLabelString != null)
			setEmptyTableWidget(emptyLabelString);
		peptideLoaderFromProjects = showhiddePSMPanel;
		mainPanel = new FlowPanel();
		initWidget(mainPanel);
		dataGrid = makeDataGrid();
		footerManager = new PeptideFooterManager(dataGrid);
		peptideColumnManager = new PeptideColumnManager(footerManager);

		ResizeLayoutPanel resizeLayoutPanel = new ResizeLayoutPanel();
		resizeLayoutPanel.setSize("100%", "90%");
		resizeLayoutPanel.add(dataGrid);
		mainPanel.add(resizeLayoutPanel);

		peptideColumnManager.addChangeListener(this);

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
		selectionModel = new SingleSelectionModel<PeptideBean>();
		dataGrid.setSelectionModel(selectionModel);

		dataGrid.redraw();
	}

	private MyDataGrid<PeptideBean> makeDataGrid() {
		/**
		 * The key provider that provides the unique ID of a PeptideBean.
		 */
		final ProvidesKey<PeptideBean> KEY_PROVIDER = new ProvidesKey<PeptideBean>() {
			@Override
			public Object getKey(PeptideBean item) {
				return item == null ? null : item.getSequence();
			}
		};
		MyDataGrid<PeptideBean> dataGrid = new MyDataGrid<PeptideBean>(KEY_PROVIDER);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;

		Image imageLoading = new Image(myClientBundle.horizontalLoader());
		loadingPanel = new VerticalPanel();
		loadingPanel.add(imageLoading);

		return dataGrid;
	}

	/**
	 * Adds the selectionHandler to the peptide table.
	 *
	 * @param selectionHandler
	 */
	public void addPeptideSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
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
		simplePager.setPageSize(SharedConstants.PEPTIDE_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns() {

		for (MyColumn<PeptideBean> column : peptideColumnManager.getColumns()) {
			ColumnName columnName = column.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PEPTIDE_AMOUNT && columnName != ColumnName.PEPTIDE_SCORE
					&& columnName != ColumnName.PEPTIDE_RATIO && columnName != ColumnName.PEPTIDE_RATIO_GRAPH) {
				final Header<String> footer = peptideColumnManager.getFooter(columnName);

				dataGrid.addColumn(columnName,
						(Column<PeptideBean, ?>) column, new MySafeHtmlHeaderWithTooltip(columnName,
								SafeHtmlUtils.fromSafeConstant(columnName.getAbr()), columnName.getDescription()),
						footer);

				if (column.isVisible()) {
					dataGrid.setColumnWidth((Column<PeptideBean, ?>) column, column.getDefaultWidth(),
							column.getDefaultWidthUnit());
				} else {
					dataGrid.setColumnWidth((Column<PeptideBean, ?>) column, 0, column.getDefaultWidthUnit());
				}
			}
		}
	}

	/**
	 * @return the dataProvider
	 */
	public AsyncDataProvider<PeptideBean> getAsyncDataProvider() {
		return asyncDataListProvider;
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;

		for (MyColumn<PeptideBean> mycolumn : peptideColumnManager.getColumnsByColumnName(columnName)) {

			final PeptideTextColumn column = (PeptideTextColumn) mycolumn;

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
		// if (true || redraw) {
		dataGrid.redrawVisibleItems();
		// dataGrid.updateTableMinimumWidth();
		// }
	}

	// @Override
	// public void showOrHideExperimentalConditionColumn(ColumnName columnName,
	// String conditionName, String projectName, boolean show) {
	// boolean redraw = false;
	// for (MyColumn<PeptideBean> mycolumn : psmColumnManager
	// .getColumnsByColumnName(columnName)) {
	//
	// final PEPTIDETextColumn column = (PEPTIDETextColumn) mycolumn;
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
		for (MyColumn<PeptideBean> mycolumn : peptideColumnManager.getColumnsByColumnName(columnName)) {

			final PeptideTextColumn column = (PeptideTextColumn) mycolumn;
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
		dataGrid.setForceToRefresh(true);
		// if (true || redraw) {
		dataGrid.redrawVisibleItems();
		// dataGrid.updateTableMinimumWidth();
		// }

	}

	public void addColumnforConditionPeptideAmount(String conditionName, AmountType amountType, String conditionID,
			String projectTag) {
		// check first if the column is already present or not
		if (!peptideColumnManager.containsColumn(ColumnName.PEPTIDE_AMOUNT, conditionName, amountType, projectTag)) {
			PeptideTextColumn column = peptideColumnManager.addPeptideAmountColumn(ColumnName.PEPTIDE_AMOUNT,
					PeptideColumns.getInstance().getColumn(ColumnName.PEPTIDE_AMOUNT).isVisible(), conditionName,
					amountType, projectTag);

			Header<String> footer = column.getFooter();
			final SafeHtml headerName = SafeHtmlUtils
					.fromSafeConstant(SharedDataUtils.getAmountHeader(amountType, conditionID));
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PEPTIDE_AMOUNT,
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

	public void addColumnforConditionPeptideRatio(ColumnName columnName, String condition1Name, String condition1ID,
			String condition2Name, String condition2ID, String projectTag, String ratioName) {
		// check first if the column is already present or not
		if (!peptideColumnManager.containsColumn(columnName, condition1Name, condition2Name, projectTag, ratioName)) {
			PeptideTextColumn column = peptideColumnManager.addPeptideRatioColumn(columnName,
					PeptideColumns.getInstance().getColumn(columnName).isVisible(), condition1Name, condition2Name,
					projectTag, ratioName);

			Header<String> footer = column.getFooter();
			String headerName = SharedDataUtils.getRatioHeader(columnName, ratioName, condition1ID, condition2ID);
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(columnName,
					SafeHtmlUtils.fromSafeConstant(headerName),
					SharedDataUtils.getRatioHeaderTooltip(columnName, condition1Name, condition2Name, ratioName));
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

	public void addColumnforPEPTIDEScore(String scoreName) {
		// check first if the column is already present or not
		if (!peptideColumnManager.containsPeptideScoreColumn(scoreName)) {
			PeptideTextColumn column = peptideColumnManager.addPeptideScoreColumn(ColumnName.PEPTIDE_SCORE, true,
					scoreName);

			Header<String> footer = column.getFooter();
			final MySafeHtmlHeaderWithTooltip header = new MySafeHtmlHeaderWithTooltip(ColumnName.PEPTIDE_SCORE,
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

	/**
	 * @return the proteinColumns
	 */
	public ColumnManager<PeptideBean> getColumnManager() {
		return peptideColumnManager;
	}

	public void clearTable() {
		getAsyncDataProvider().updateRowCount(0, true);
		getAsyncDataProvider().updateRowData(0, Collections.EMPTY_LIST);
		refreshData();
	}

	@Override
	public void removeColumn(ColumnName columnName) {
		peptideColumnManager.removeColumns(columnName);
		dataGrid.removeColumns(columnName);
	}

	/**
	 * After calling this method, the empty widget will be the loading bar
	 */
	public void setLoadingWidget() {
		dataGrid.setEmptyTableWidget(loadingPanel);

	}

	public void hiddePeptidePanel() {
		if (peptideLoaderFromProjects != null) {
			peptideLoaderFromProjects.hiddePanel();
		}
	}

	public void showPeptidePanel() {
		if (peptideLoaderFromProjects != null) {
			peptideLoaderFromProjects.showPanel();
		}
	}

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in PEPTIDETable");

		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getPeptidePageSize());
		}
		// get default views on proteins
		final List<ColumnWithVisibility> peptidesDefaultView = defaultView.getPeptideDefaultView();
		for (ColumnWithVisibility columnWithVisibility : peptidesDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}
		// get sorting parameters for the protein groups
		final ORDER order = defaultView.getPeptideOrder();
		final String sortingScore = defaultView.getPeptideSortingScore();
		final ColumnName sortedBy = defaultView.getPeptidesSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in PEPTIDETable END");
	}

	@Override
	public void pushSortingOrder(ColumnName sortedBy, ORDER order, String sortingScore) {
		GWT.log("Sorting PEPTIDETable");
		dataGrid.getColumnSortList().clear();
		Set<MyColumn<PeptideBean>> columns = getColumnManager().getColumnsByColumnName(sortedBy);

		if (columns != null && !columns.isEmpty()) {
			if (sortingScore == null || "".equals(sortingScore)) {
				Column<PeptideBean, ?> column = (Column<PeptideBean, ?>) columns.iterator().next();
				if (column != null) {
					// dataGrid.sortColumn(column, order);
					dataGrid.pushColumnToColumnSortList(column, order);
				}
			} else {
				for (MyColumn<PeptideBean> myColumn : columns) {
					if (sortingScore.equalsIgnoreCase(myColumn.getScoreName())) {
						Column<PeptideBean, ?> column = (Column<PeptideBean, ?>) myColumn;
						// dataGrid.sortColumn(column, order);
						dataGrid.pushColumnToColumnSortList(column, order);

					}
				}
			}
		}
	}

	public void selectFirstRow() {
		GWT.log("Selecting First row in PEPTIDETable");

		final PeptideBean firstRowPeptideBean = getFirstRowPeptideBean();
		if (firstRowPeptideBean != null) {
			selectionModel.setSelected(firstRowPeptideBean, true);
		}
		GWT.log("Selecting First row in PEPTIDETable END");

	}

	public PeptideBean getFirstRowPeptideBean() {
		GWT.log("Getting First row PeptideBean from PeptideTable");
		refreshData();
		final List<PeptideBean> visibleItems = dataGrid.getVisibleItems();
		if (visibleItems != null && !visibleItems.isEmpty()) {
			PeptideBean firstPeptideBean = visibleItems.get(0);

			return firstPeptideBean;
		}
		GWT.log("No PeptideBean in first row in PEPTIDETable");
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