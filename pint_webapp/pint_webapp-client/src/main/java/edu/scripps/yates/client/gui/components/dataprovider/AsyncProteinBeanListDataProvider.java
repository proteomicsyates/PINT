package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;

public class AsyncProteinBeanListDataProvider extends AbstractAsyncDataProvider<ProteinBean> {

	public AsyncProteinBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void retrieveData(MyColumn<ProteinBean> column, final int start, int end, ColumnSortInfo columnSortInfo,
			final Range range) {
		GWT.log("Getting proteins beans sorted");
		Comparator<ProteinBean> comparator = null;
		if (column != null) {
			comparator = column.getComparator();
		}
		boolean isAscending = false;
		if (columnSortInfo != null) {
			isAscending = columnSortInfo.isAscending();
		}
		service.getProteinBeansFromListSorted(sessionID, start, end, comparator, isAscending,
				new AsyncCallback<ProteinBeanSubList>() {

					@Override
					public void onSuccess(ProteinBeanSubList result) {
						try {
							GWT.log("Result of getting proteins beans sorted");
							if (result == null) {
								updateRowCount(0, true);
								setRange(null);
								return;
							}
							updateRowData(start, result.getDataList());
							updateRowCount(result.getTotalNumber(), true);
						} finally {
							retrievingDataFinished();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						try {
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							updateRowCount(0, true);
							setRange(null);
						} finally {
							retrievingDataFinished();
						}
					}
				});

	}
}
