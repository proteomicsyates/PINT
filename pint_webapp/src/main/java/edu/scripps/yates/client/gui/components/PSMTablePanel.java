package edu.scripps.yates.client.gui.components;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
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
import edu.scripps.yates.client.gui.columns.PSMColumnManager;
import edu.scripps.yates.client.gui.columns.PSMTextColumn;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.gui.columns.footers.PSMFooterManager;
import edu.scripps.yates.client.gui.components.dataprovider.AbstractAsyncDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.SharedConstants;

public class PSMTablePanel extends AbstractDataTable<PSMBean> {
	public static final String SELECT_PROTEIN_TO_LOAD_PSMS_TEXT = "Select one protein to load PSMs";
	public static final String NO_DATA_FROM_THIS_PROTEIN = "There is not any PSM information for that selection";
	public static final String WAIT_TO_BE_LOADED = "Please wait for PSMs to be loaded...";

	private final ShowHiddePanel psmLoaderFromProjects;

	public PSMTablePanel(String sessionID, String emptyLabel, ShowHiddePanel showhiddePSMPanel,
			AbstractAsyncDataProvider<PSMBean> asyncDataListProvider, boolean multipleSelectionModel) {
		super(sessionID, emptyLabel, asyncDataListProvider, multipleSelectionModel);
		psmLoaderFromProjects = showhiddePSMPanel;

	}

	@Override
	protected MyDataGrid<PSMBean> makeDataGrid() {
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

		return dataGrid;
	}

	@Override
	protected SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true,
				SharedConstants.PSM_DEFAULT_PAGE_SIZE * 5, true);
		simplePager.setPageSize(SharedConstants.PSM_DEFAULT_PAGE_SIZE);
		simplePager.setDisplay(dataGrid);
		return simplePager;
	}

	/**
	 * Add the columns to the table.
	 */
	@Override
	protected void initTableColumns(boolean addCheckBoxSelection) {

		for (MyColumn<PSMBean> column : getColumnManager().getColumns()) {
			ColumnName columnName = column.getColumnName();
			// don't do anything with amount because the conditions
			// are not loaded yet
			if (columnName != ColumnName.PSM_AMOUNT && columnName != ColumnName.PSM_SCORE
					&& columnName != ColumnName.PTM_SCORE && columnName != ColumnName.PSM_RATIO
					&& columnName != ColumnName.PSM_RATIO_GRAPH) {
				final Header<String> footer = getColumnManager().getFooter(columnName);

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

	@Override
	public void showOrHideColumn(ColumnName columnName, boolean show) {
		boolean redraw = false;

		for (MyColumn<PSMBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

			final Column<PSMBean, ?> column = (Column<PSMBean, ?>) mycolumn;

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
		for (MyColumn<PSMBean> mycolumn : getColumnManager().getColumnsByColumnName(columnName)) {

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
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				dataGrid.redrawVisibleItems();
				// reloadData();
			}
		});

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
	protected AbstractColumnManager<PSMBean> createColumnManager(FooterManager<PSMBean> footerManager) {
		return new PSMColumnManager(footerManager);
	}

	@Override
	protected FooterManager<PSMBean> createFooterManager(MyDataGrid<PSMBean> dataGrid) {
		return new PSMFooterManager(dataGrid);
	}

	@Override
	public void setDefaultView(DefaultView defaultView) {
		GWT.log("Setting default view in PSM table");
		// apply page size
		if (pager != null) {
			pager.setPageSize(defaultView.getPsmPageSize());
			pager.setPage(0);
		} // get default views on proteins
		final List<ColumnWithVisibility> psmDefaultView = defaultView.getPsmDefaultView();
		for (ColumnWithVisibility columnWithVisibility : psmDefaultView) {
			final ColumnName column = columnWithVisibility.getColumn();
			final boolean visible = columnWithVisibility.isVisible();
			showOrHideColumn(column, visible);
			showOrHideExperimentalConditionColumn(column, null, null, visible);
		}

		// get sorting parameters for the psms

		final ORDER order = defaultView.getPsmOrder();
		final String sortingScore = defaultView.getPsmSortingScore();
		final ColumnName sortedBy = defaultView.getPsmsSortedBy();
		pushSortingOrder(sortedBy, order, sortingScore);
		GWT.log("Setting default view in PSM table END");
	}

}