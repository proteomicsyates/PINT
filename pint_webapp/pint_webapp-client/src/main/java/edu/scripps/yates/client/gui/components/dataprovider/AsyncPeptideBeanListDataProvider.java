package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;

public class AsyncPeptideBeanListDataProvider extends AbstractAsyncDataProvider<PeptideBeanLight> {
	private boolean isReadyForProvidingData = false;

	public AsyncPeptideBeanListDataProvider(String sessionID) {
		super(sessionID);
	}

	@Override
	protected void retrieveData(MyColumn<PeptideBeanLight> column, final int start, int end,
			ColumnSortInfo columnSortInfo, final Range range) {
		if (!isReadyForProvidingData) {
			GWT.log("Asynchronous data provider for peptides is not set to ready, so it will not ask for data");
			retrievingDataFinished();
			return;
		}
		GWT.log("Getting peptide beans sorted");
		Comparator<PeptideBean> comparator = null;
		if (column != null) {
			comparator = (Comparator<PeptideBean>) column.getComparator();
		}
		boolean isAscending = false;
		if (columnSortInfo != null) {
			isAscending = columnSortInfo.isAscending();
		}
		service.getPeptideBeansFromListSorted(sessionID, start, end, comparator, isAscending,
				new AsyncCallback<PeptideBeanSubList>() {

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

	public boolean isReadyForProvidingData() {
		return isReadyForProvidingData;
	}

	public void setReadyForProvidingData(boolean isReadyForProvidingData) {
		this.isReadyForProvidingData = isReadyForProvidingData;
	}

}
