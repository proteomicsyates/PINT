package edu.scripps.yates.client.gui.components.dataprovider;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;

import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.MyDataGrid;

public abstract class MyAsyncDataProvider<T> extends AsyncDataProvider<T> {
	protected final ProteinRetrievalServiceAsync service = GWT.create(ProteinRetrievalService.class);
	protected final String sessionID;
	protected Range range;
	private ColumnSortInfo currentSortInfo;

	public MyAsyncDataProvider(String sessionID) {
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
	protected boolean needsUpdate(HasData<T> display) {
		final ColumnSortList columnSortList = getColumnSortList(display);
		if (!display.getVisibleRange().equals(range)) {
			return true;
		}
		if (display instanceof MyDataGrid) {
			MyDataGrid dataGrid = (MyDataGrid) display;
			if (dataGrid.isForceToRefresh()) {
				dataGrid.setForceToRefresh(false);
				return true;
			}
		}
		if (columnSortList.size() == 0) {
			return false;
		}
		if (currentSortInfo == null) {
			return true;
		}
		final ColumnSortInfo columnSortInfo = columnSortList.get(0);
		if (Boolean.compare(currentSortInfo.isAscending(), columnSortInfo.isAscending()) != 0) {
			return true;
		}
		if (!columnSortInfo.equals(currentSortInfo)) {
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
	protected ColumnSortList getColumnSortList(HasData<T> display) {
		if (display instanceof AbstractCellTable) {
			AbstractCellTable<T> dataGRid = (AbstractCellTable) display;
			return dataGRid.getColumnSortList();
		}
		throw new IllegalArgumentException("display not supported");
	}

	protected void setCurrentSortInfo(ColumnSortInfo info) {
		this.currentSortInfo = info;
	}

	protected void refreshDisplay(HasData<T> display) {
		RangeChangeEvent.fire(display, range);
	}
}
