package edu.scripps.yates.client.gui.components.dataprovider;

import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.PeptideTextColumn;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;

public class AsyncPeptideBeanListDataProvider extends MyAsyncDataProvider<PeptideBean> {

	public AsyncPeptideBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void onRangeChanged(final HasData<PeptideBean> display) {
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
				final PeptideTextColumn column = (PeptideTextColumn) columnSortInfo.getColumn();
				display.setVisibleRangeAndClearData(display.getVisibleRange(), true);
				service.getPeptideBeansFromListSorted(sessionID, start, end, column.getComparator(),
						columnSortInfo.isAscending(), new AsyncCallback<PeptideBeanSubList>() {

							@Override
							public void onSuccess(PeptideBeanSubList result) {
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
				service.getPeptideBeansFromList(sessionID, start, end, new AsyncCallback<PeptideBeanSubList>() {

					@Override
					public void onSuccess(PeptideBeanSubList result) {
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
