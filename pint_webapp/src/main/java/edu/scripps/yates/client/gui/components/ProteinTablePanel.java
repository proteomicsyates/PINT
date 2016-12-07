package edu.scripps.yates.client.gui.components;

import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MyIdColumn;
import edu.scripps.yates.client.gui.columns.ProteinColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinTablePanel extends AbstractDataTable<ProteinBean> {

	private static final String cwDataGridEmpty = "No proteins shown";

	public ProteinTablePanel(String sessionID, String emptyLabel,
			AbstractAsyncDataProvider<ProteinBean> asyncProteinBeanListDataProvider, boolean multipleSelectionModel) {
		super(sessionID, emptyLabel, asyncProteinBeanListDataProvider, multipleSelectionModel);

		dataGrid.redraw();
	}

	@Override
	protected MyDataGrid<ProteinBean> makeDataGrid() {
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

	@Override
	protected SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PROTEIN_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	@Override
	protected void initTableColumns(boolean addCheckBoxSelection) {
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
		for (MyColumn<ProteinBean> myColumn : getColumnManager().getColumns()) {
			ColumnName columnName = myColumn.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PROTEIN_AMOUNT && columnName != ColumnName.PROTEIN_RATIO
					&& columnName != ColumnName.PROTEIN_RATIO_SCORE && columnName != ColumnName.PROTEIN_RATIO_GRAPH) {
				final boolean visible = getColumnManager().isVisible(columnName);
				if (visible) {
					final Header<String> footer = getColumnManager().getFooter(columnName);

					dataGrid.addColumn(columnName, (Column<ProteinBean, ?>) myColumn, myColumn.getHeader(), footer);

					if (visible) {
						dataGrid.setColumnWidth((Column<ProteinBean, ?>) myColumn, myColumn.getDefaultWidth(),
								myColumn.getDefaultWidthUnit());
					} else {
						dataGrid.setColumnWidth((Column<ProteinBean, ?>) myColumn, 0, myColumn.getDefaultWidthUnit());
					}
				}
			}
		}
	}

	/**
	 * @return the dataGrid
	 */
	@Override
	public MyDataGrid<ProteinBean> getDataGrid() {
		return dataGrid;
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

			final Column<ProteinBean, ?> column = (Column<ProteinBean, ?>) mycolumn;
			if (show) {
				// new dec 7, 2016:
				dataGrid.addColumnToTable(column, getColumnManager());
				// dataGrid.redraw();
				if (false) {
					final String newWidth = String.valueOf(mycolumn.getDefaultWidth())
							+ mycolumn.getDefaultWidthUnit().getType();
					final String columnWidth = dataGrid.getColumnWidth(column);
					if (!columnWidth.equals(newWidth)) {

						if (dataGrid.isEmptyColumn(column))
							redraw = true;
						dataGrid.setColumnWidth(column, newWidth);
						mycolumn.setWidth(mycolumn.getDefaultWidth());
					}
				}
			} else {
				// new dec 7, 2016:
				dataGrid.removeColumn(column);
				if (false) {
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
	public void showOrHideExperimentalConditionColumn(ColumnName columnName, Set<String> conditionNames,
			String projectName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {
			if (mycolumn instanceof MyIdColumn) {
				MyIdColumn<ProteinBean> idColumn = (MyIdColumn<ProteinBean>) mycolumn;
				final Column<ProteinBean, String> column = (Column<ProteinBean, String>) mycolumn;
				String condition1ReferredByColumn = idColumn.getExperimentalConditionName();
				String condition2ReferredByColumn = idColumn.getExperimentalCondition2Name() != null
						? idColumn.getExperimentalCondition2Name() : condition1ReferredByColumn;

				if (projectName == null || idColumn.getProjectTag().equalsIgnoreCase(projectName)) {
					if (conditionNames == null || (conditionNames.contains(condition1ReferredByColumn)
							&& conditionNames.contains(condition2ReferredByColumn))) {
						if (show) {
							dataGrid.addColumnToTable(column, getColumnManager());
							if (false) {
								final String columnWidth = dataGrid.getColumnWidth(column);
								final String newWidth = String.valueOf(mycolumn.getDefaultWidth())
										+ mycolumn.getDefaultWidthUnit().getType();
								if (!columnWidth.equals(newWidth)) {
									if (dataGrid.isEmptyColumn(column))
										redraw = true;
									dataGrid.setColumnWidth(column, newWidth);
									mycolumn.setWidth(mycolumn.getDefaultWidth());
								}
							}
						} else {
							dataGrid.removeColumn(column);
							if (false) {
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
	protected AbstractColumnManager<ProteinBean> createColumnManager(FooterManager<ProteinBean> footerManager) {
		return new ProteinColumnManager(footerManager, sessionID);
	}

	@Override
	protected FooterManager<ProteinBean> createFooterManager(MyDataGrid<ProteinBean> dataGrid) {
		return new ProteinFooterManager(dataGrid);
	}

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in Protein table");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getProteinPageSize());
			pager.setPage(0);
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
		final ORDER order = defaultView.getProteinOrder();
		final String sortingScore = defaultView.getProteinSortingScore();
		final ColumnName sortedBy = defaultView.getProteinsSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in Protein table END");
	}
}
