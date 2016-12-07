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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MyIdColumn;
import edu.scripps.yates.client.gui.columns.ProteinGroupColumnManager;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.ProteinGroupFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProteinGroupTablePanel extends AbstractDataTable<ProteinGroupBean> {

	public ProteinGroupTablePanel(String sessionID, Widget emptyWidget,
			AbstractAsyncDataProvider<ProteinGroupBean> asyncDataProvider, boolean multipleSelectionModel) {
		super(sessionID, emptyWidget, asyncDataProvider, multipleSelectionModel);
	}

	/**
	 * Adds the selectionHandler to the protein table.
	 *
	 * @param selectionHandler
	 */
	public void addProteinSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	@Override
	public MyDataGrid<ProteinGroupBean> makeDataGrid() {
		final ProvidesKey<ProteinGroupBean> KEY_PROVIDER = new ProvidesKey<ProteinGroupBean>() {
			@Override
			public Object getKey(ProteinGroupBean item) {

				return item == null ? null : item.getProteinDBString();
			}
		};
		MyDataGrid<ProteinGroupBean> dataGrid = new MyDataGrid<ProteinGroupBean>(KEY_PROVIDER);

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PROTEINGROUP_DEFAULT_PAGE_SIZE * 5, true);

		simplePager.setPageSize(SharedConstants.PROTEINGROUP_DEFAULT_PAGE_SIZE);
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
		for (MyColumn<ProteinGroupBean> myColumn : getColumnManager().getColumns()) {
			ColumnName columnName = myColumn.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PROTEIN_AMOUNT && columnName != ColumnName.PROTEIN_RATIO) {
				final boolean visible = getColumnManager().isVisible(columnName);
				if (visible) {
					final Header<String> footer = getColumnManager().getFooter(columnName);

					dataGrid.addColumn(columnName, (Column<ProteinGroupBean, ?>) myColumn, myColumn.getHeader(),
							footer);

					if (visible) {
						dataGrid.setColumnWidth((Column<ProteinGroupBean, ?>) myColumn, myColumn.getDefaultWidth(),
								myColumn.getDefaultWidthUnit());
					} else {
						dataGrid.setColumnWidth((Column<ProteinGroupBean, ?>) myColumn, 0,
								myColumn.getDefaultWidthUnit());
					}
				}
			}
		}
	}

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;
		for (MyColumn<ProteinGroupBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

			final Column<ProteinGroupBean, ?> column = (Column<ProteinGroupBean, ?>) mycolumn;
			if (show) {
				dataGrid.addColumnToTable(column, getColumnManager());
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
		for (MyColumn<ProteinGroupBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {
			if (mycolumn instanceof MyIdColumn) {
				MyIdColumn<ProteinGroupBean> idColumn = (MyIdColumn<ProteinGroupBean>) mycolumn;
				final Column<ProteinGroupBean, String> column = (Column<ProteinGroupBean, String>) mycolumn;
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
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in ProteinGroupTable");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getProteinGroupPageSize());
			pager.setPage(0);
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
	protected AbstractColumnManager<ProteinGroupBean> createColumnManager(
			FooterManager<ProteinGroupBean> footerManager) {
		return new ProteinGroupColumnManager(footerManager, sessionID);
	}

	@Override
	protected FooterManager<ProteinGroupBean> createFooterManager(MyDataGrid<ProteinGroupBean> dataGrid) {
		return new ProteinGroupFooterManager(dataGrid);
	}

}
