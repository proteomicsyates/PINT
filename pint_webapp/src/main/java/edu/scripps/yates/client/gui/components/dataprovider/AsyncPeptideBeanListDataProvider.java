package edu.scripps.yates.client.gui.components.dataprovider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;

public class AsyncPeptideBeanListDataProvider extends AbstractAsyncDataProvider<PeptideBean> {

	public AsyncPeptideBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void retrieveData(MyColumn<PeptideBean> column, final int start, int end, ColumnSortInfo columnSortInfo,
			final Range range) {
		GWT.log("Getting peptide beans sorted");
		service.getPeptideBeansFromListSorted(sessionID, start, end, column.getComparator(),
				columnSortInfo.isAscending(), new AsyncCallback<PeptideBeanSubList>() {

					@Override
					public void onSuccess(PeptideBeanSubList result) {
						try {
							GWT.log("Result of getting peptide beans sorted");
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
