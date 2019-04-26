package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.light.ProteinGroupBeanLight;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;

public class AsyncProteinGroupBeanListDataProvider extends AbstractAsyncDataProvider<ProteinGroupBeanLight> {

	public AsyncProteinGroupBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void retrieveData(MyColumn<ProteinGroupBeanLight> column, final int start, int end,
			ColumnSortInfo columnSortInfo, final Range range) {
		GWT.log("Getting protein groups beans sorted");
		Comparator<ProteinGroupBean> comparator = null;
		if (column != null) {
			comparator = (Comparator<ProteinGroupBean>) column.getComparator();
		}
		boolean isAscending = false;
		if (columnSortInfo != null) {
			isAscending = columnSortInfo.isAscending();
		}
		service.getProteinGroupBeansFromListSorted(sessionID, start, end, comparator, isAscending,
				new AsyncCallback<ProteinGroupBeanSubList>() {

					@Override
					public void onSuccess(ProteinGroupBeanSubList result) {
						GWT.log("Result from getting protein groups beans sorted");
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
