package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;

public class AsyncPSMBeanListDataProvider extends AbstractAsyncDataProvider<PSMBean> {

	public AsyncPSMBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void retrieveData(MyColumn<PSMBean> column, final int start, int end, ColumnSortInfo columnSortInfo,
			final Range range) {
		GWT.log("Getting PSM beans sorted");
		Comparator<PSMBean> comparator = null;
		if (column != null) {
			comparator = column.getComparator();
		}
		boolean isAscending = false;
		if (columnSortInfo != null) {
			isAscending = columnSortInfo.isAscending();
		}
		service.getPSMBeansFromListSorted(sessionID, start, end, comparator, isAscending,
				new AsyncCallback<PsmBeanSubList>() {

					@Override
					public void onSuccess(PsmBeanSubList result) {
						GWT.log("Result from getting PSMs");
						try {
							if (result == null) {
								updateRowCount(0, true);
								setRange(null);
								return;
							}
							updateRowData(start, result.getDataList());
							updateRowCount(result.getTotalNumber(), true);
							setRange(range);
						} finally {
							retrievingDataFinished();
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						try {
							updateRowCount(0, true);
							setRange(null);
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						} finally {
							retrievingDataFinished();
						}
					}
				});

	}

}
