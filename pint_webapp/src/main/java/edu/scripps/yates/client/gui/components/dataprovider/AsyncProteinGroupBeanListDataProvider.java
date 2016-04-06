package edu.scripps.yates.client.gui.components.dataprovider;

import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.ProteinGroupTextColumn;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;

public class AsyncProteinGroupBeanListDataProvider extends MyAsyncDataProvider<ProteinGroupBean> {

	public AsyncProteinGroupBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void onRangeChanged(final HasData<ProteinGroupBean> display) {
		if (needsUpdate(display)) {
			range = display.getVisibleRange();
			final int start = range.getStart();
			int end = start + range.getLength();
			final ColumnSortList columnSortList = getColumnSortList(display);
			if (columnSortList.size() > 0) {
				if (display instanceof MyDataGrid) {
					((MyDataGrid) display).setForceToRefresh(false);
				}
				final ColumnSortInfo columnSortInfo = columnSortList.get(0);
				setCurrentSortInfo(columnSortInfo);
				final ProteinGroupTextColumn column = (ProteinGroupTextColumn) columnSortInfo.getColumn();
				display.setVisibleRangeAndClearData(display.getVisibleRange(), true);
				service.getProteinGroupBeansFromListSorted(sessionID, start, end, column.getComparator(),
						columnSortInfo.isAscending(), new AsyncCallback<ProteinGroupBeanSubList>() {

							@Override
							public void onSuccess(ProteinGroupBeanSubList result) {
								if (result == null) {
									updateRowCount(0, true);
									refreshDisplay(display);
									return;
								}
								updateRowData(start, result.getDataList());
								updateRowCount(result.getTotalNumber(), true);
								refreshDisplay(display);
							}

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}
						});

			} else {
				service.getProteinGroupBeansFromList(sessionID, start, end,
						new AsyncCallback<ProteinGroupBeanSubList>() {

							@Override
							public void onSuccess(ProteinGroupBeanSubList result) {
								if (result == null) {
									updateRowCount(0, true);
									refreshDisplay(display);
									return;
								}
								updateRowCount(result.getTotalNumber(), true);
								updateRowData(start, result.getDataList());
								refreshDisplay(display);
							}

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}
						});
			}
		}
	}

}
