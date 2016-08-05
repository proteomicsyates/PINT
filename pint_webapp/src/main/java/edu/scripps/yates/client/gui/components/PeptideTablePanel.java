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
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.columns.AbstractColumnManager;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.PeptideColumnManager;
import edu.scripps.yates.client.gui.columns.PeptideTextColumn;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.PeptideFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class PeptideTablePanel extends AbstractDataTable<PeptideBean> {
	public static final String SELECT_PEPTIDE_TO_LOAD_PSMS_TEXT = "Select one peptide to load PSMs";
	public static final String NO_DATA_FROM_THIS_PEPTIDE = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for Peptides to be loaded...";
	private final ShowHiddePanel peptideLoaderFromProjects;

	public PeptideTablePanel(String sessionID, String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AbstractAsyncDataProvider<PeptideBean> asyncDataListProvider, boolean multipleSelectionModel) {
		super(sessionID, emptyLabel, asyncDataListProvider, multipleSelectionModel);
		peptideLoaderFromProjects = showhiddePSMPanel;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.client.gui.components.AbstractDataTable#
	 * createColumnManager()
	 */
	@Override
	protected AbstractColumnManager<PeptideBean> createColumnManager(FooterManager<PeptideBean> footerManager) {
		return new PeptideColumnManager(footerManager);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.client.gui.components.AbstractDataTable#
	 * createFooterManager()
	 */
	@Override
	protected FooterManager<PeptideBean> createFooterManager(MyDataGrid<PeptideBean> dataGrid) {
		return new PeptideFooterManager(dataGrid);
	}

	@Override
	public MyDataGrid<PeptideBean> makeDataGrid() {
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

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PEPTIDE_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PEPTIDE_DEFAULT_PAGE_SIZE);
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
			Column<PeptideBean, Boolean> checkColumn = new Column<PeptideBean, Boolean>(new CheckboxCell(true, false)) {
				@Override
				public Boolean getValue(PeptideBean object) {
					// Get the value from the selection model.
					return selectionModel.isSelected(object);
				}
			};
			dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
			dataGrid.setColumnWidth(checkColumn, 30, Unit.PX);
		}
		for (MyColumn<PeptideBean> column : getColumnManager().getColumns()) {
			ColumnName columnName = column.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PEPTIDE_AMOUNT && columnName != ColumnName.PEPTIDE_SCORE
					&& columnName != ColumnName.PEPTIDE_RATIO && columnName != ColumnName.PEPTIDE_RATIO_GRAPH) {
				final Header<String> footer = getColumnManager().getFooter(columnName);

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

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;

		for (MyColumn<PeptideBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

			final Column<PeptideBean, ?> column = (Column<PeptideBean, ?>) mycolumn;

			if (show) {
				final String newWidth = String.valueOf(mycolumn.getDefaultWidth())
						+ mycolumn.getDefaultWidthUnit().getType();
				final String columnWidth = dataGrid.getColumnWidth(column);
				if (!newWidth.equals(columnWidth)) {
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
		for (MyColumn<PeptideBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

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
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				dataGrid.redrawVisibleItems();
				// reloadData();
			}
		});

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
		GWT.log("Setting default view in Peptide table");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getPeptidePageSize());
			pager.setPage(0);
		}
		// get default views on peptides
		final List<ColumnWithVisibility> peptideDefaultView = defaultView.getPeptideDefaultView();
		for (ColumnWithVisibility columnWithVisibility : peptideDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}

		// get sorting parameters for the peptides
		final ORDER order = defaultView.getPeptideOrder();
		final String sortingScore = defaultView.getPeptideSortingScore();
		final ColumnName sortedBy = defaultView.getPeptidesSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in Peptide table END");
	}
}