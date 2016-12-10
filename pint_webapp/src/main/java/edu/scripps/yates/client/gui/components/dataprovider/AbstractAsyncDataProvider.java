package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;

public abstract class AbstractAsyncDataProvider<T> extends AsyncDataProvider<T> {
	protected final ProteinRetrievalServiceAsync service = GWT.create(ProteinRetrievalService.class);
	protected final String sessionID;
	private Range range;
	private ColumnSortInfo currentSortInfo;
	private boolean containsData = false;
	private boolean retrievingData = false;
	protected boolean newProvider = false;
	private boolean sortingChanged = false;
	private boolean forceToRefresh = false;
	private int start;
	private List<T> values;
	private int size;
	private boolean exact;
	private boolean forceToReload;

	public AbstractAsyncDataProvider(String sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * Returns true if it is needed to retrieve a new set of data, and that is
	 * when the range is changed or when the order to the columnSortList is
	 * changed.
	 *
	 * @param display
	 * @return
	 */
	private boolean needsUpdate(HasData<T> display) {
		if (!containsData) {
			return true;
		}
		if (!display.getVisibleRange().equals(range)) {
			return true;
		}
		// if (display.getVisibleRange().equals(range)) {
		// return false;
		// }

		if (display instanceof MyDataGrid) {
			MyDataGrid dataGrid = (MyDataGrid) display;
			if (dataGrid.isForceToRefresh()) {
				dataGrid.setForceToRefresh(false);
				forceToRefresh = true;
			}
			if (dataGrid.isForceToReload()) {
				dataGrid.setforceToReload(false);
				forceToReload = true;
			}
			if (forceToRefresh || forceToReload) {
				return true;
			}
		}
		final ColumnSortList columnSortList = getColumnSortList(display);
		if (columnSortList.size() == 0) {
			return false;
		}
		if (currentSortInfo == null) {
			sortingChanged = true;
			return true;
		}
		final ColumnSortInfo columnSortInfo = columnSortList.get(0);
		if (Boolean.compare(currentSortInfo.isAscending(), columnSortInfo.isAscending()) != 0) {
			sortingChanged = true;
			return true;
		}
		if (!columnSortInfo.equals(currentSortInfo)) {
			sortingChanged = true;
			return true;
		}

		return false;
	}

	/**
	 * Gets a {@link ColumnSortList} from the display, which only works if it is
	 * an {@link AbstractCellTable}. Otherwise an
	 * {@link IllegalArgumentException} is thrown
	 *
	 * @param display
	 * @return
	 */
	private ColumnSortList getColumnSortList(HasData<T> display) {
		if (display instanceof AbstractCellTable) {
			AbstractCellTable<T> dataGRid = (AbstractCellTable) display;
			return dataGRid.getColumnSortList();
		}
		throw new IllegalArgumentException("display not supported");
	}

	private void setCurrentSortInfo(ColumnSortInfo info) {
		this.currentSortInfo = info;
	}

	public boolean rangeChanged(HasData<T> display) {
		final boolean rangeChanged = !display.getVisibleRange().equals(range);
		return rangeChanged;
	}

	@Override
	public void updateRowData(final int start, final List<T> values) {
		if (values == null || values.isEmpty()) {
			containsData = false;
		} else {
			containsData = true;
		}
		this.start = start;
		this.values = values;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				updateRowDataInSuper(start, values);
			}
		});

	}

	protected void updateRowDataInSuper(int start2, List<T> values2) {
		super.updateRowData(start2, values2);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.view.client.AsyncDataProvider#updateRowCount(int,
	 * boolean)
	 */
	@Override
	public void updateRowCount(final int size, final boolean exact) {
		if (size == 0) {
			containsData = false;
		} else {
			containsData = true;
		}
		this.size = size;
		this.exact = exact;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				updateRowCountInSuper(size, exact);
			}
		});
	}

	protected void updateRowCountInSuper(int size2, boolean exact2) {
		super.updateRowCount(size2, exact2);

	}

	/**
	 * @return the range
	 */
	public Range getRange() {
		return range;
	}

	/**
	 * @param range
	 *            the range to set
	 */
	public void setRange(Range range) {
		this.range = range;
	}

	@Override
	protected final void onRangeChanged(final HasData<T> display) {
		try {
			boolean rangeChanged = rangeChanged(display);
			if (needsUpdate(display)) {
				GWT.log("forceToReload: " + forceToReload + "\tforceToRefresh: " + forceToRefresh + "\tnewProvider: "
						+ newProvider + "\tsortingChanged: " + sortingChanged + "\trangeChanged:" + rangeChanged
						+ "\tcontainsData:" + containsData);
				final Range range = display.getVisibleRange();
				final int start = range.getStart();
				int end = start + range.getLength();

				if (!retrievingData) {
					final ColumnSortList columnSortList = getColumnSortList(display);
					if (columnSortList.size() > 0) {
						if (forceToReload || sortingChanged || newProvider || rangeChanged
								|| (!rangeChanged && !containsData)) {
							if (display instanceof MyDataGrid) {
								((MyDataGrid) display).setForceToRefresh(false);
							}

							ColumnSortInfo columnSortInfo = null;
							MyColumn<T> column = null;
							if (columnSortList.size() > 0) {
								columnSortInfo = columnSortList.get(0);
								setCurrentSortInfo(columnSortInfo);
								column = (MyColumn<T>) columnSortInfo.getColumn();
							}
							// display.setVisibleRangeAndClearData(display.getVisibleRange(),
							// true);
							retrievingData = true;
							retrieveData(column, start, end, columnSortInfo, range);

						} else if (forceToRefresh) {
							refresh();
						}
						if (rangeChanged) {
							setRange(range);
						}
					}
				}
			}
		} finally {
			newProvider = false;
			sortingChanged = false;
			forceToRefresh = false;
			forceToReload = false;
		}
	}

	private void refresh() {
		GWT.log("Refreshing from " + getClass().getCanonicalName());
		updateRowCount(size, exact);
		updateRowData(start, values);
	}

	protected void retrievingDataFinished() {
		this.retrievingData = false;
	}

	protected abstract void retrieveData(MyColumn<T> column, int start, int end, ColumnSortInfo columnSortInfo,
			Range range);
}
