package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPeptides;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;

public class AsyncPeptideBeanListFromPeptideProvider extends AbstractAsyncDataProvider<PeptideBeanLight> {
	private ContainsLightPeptides peptideProvider;

	public AsyncPeptideBeanListFromPeptideProvider(String sessionID) {
		super(sessionID);
	}

	public AsyncPeptideBeanListFromPeptideProvider(ContainsLightPeptides peptideProvider, String sessionID) {
		super(sessionID);
		this.peptideProvider = peptideProvider;

	}

	@Override
	protected void retrieveData(MyColumn<PeptideBeanLight> column, final int start, int end,
			ColumnSortInfo columnSortInfo, final Range range) {
		if (peptideProvider == null) {
			retrievingDataFinished();
			return;
		}
		GWT.log("Getting proteins beans sorted from peptide bean provider");
		Comparator<PeptideBean> comparator = null;
		if (column != null) {
			comparator = (Comparator<PeptideBean>) column.getComparator();
		}
		boolean isAscending = false;
		if (columnSortInfo != null) {
			isAscending = columnSortInfo.isAscending();
		}
		service.getPeptideBeansFromPeptideProviderFromListSorted(sessionID, peptideProvider, start, end, comparator,
				isAscending, new AsyncCallback<PeptideBeanSubList>() {

					@Override
					public void onSuccess(PeptideBeanSubList result) {
						GWT.log("Received peptide beans sorted from provider");
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

	public void setPeptideProvider(ContainsLightPeptides peptideProvider) {
		if (peptideProvider != null && peptideProvider.equals(this.peptideProvider))
			return;
		this.peptideProvider = peptideProvider;
		newProvider = true;
	}

}
