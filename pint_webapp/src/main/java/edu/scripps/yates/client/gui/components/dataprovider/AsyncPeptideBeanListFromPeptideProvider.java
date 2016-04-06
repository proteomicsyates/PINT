package edu.scripps.yates.client.gui.components.dataprovider;

import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.PeptideTextColumn;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;

public class AsyncPeptideBeanListFromPeptideProvider extends MyAsyncDataProvider<PeptideBean> {
	private ContainsPeptides peptideProvider;

	private Range range;

	private boolean newProvider;

	public AsyncPeptideBeanListFromPeptideProvider(String sessionID) {
		super(sessionID);
	}

	public AsyncPeptideBeanListFromPeptideProvider(ContainsPeptides peptideProvider, String sessionID) {
		super(sessionID);
		this.peptideProvider = peptideProvider;

	}

	@Override
	protected void onRangeChanged(final HasData<PeptideBean> display) {
		if (peptideProvider != null) {
			if (needsUpdate(display) || newProvider) {
				newProvider = false;
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
					service.getPeptideBeansFromPeptideProviderFromListSorted(sessionID, peptideProvider, start, end,
							column.getComparator(), columnSortInfo.isAscending(),
							new AsyncCallback<PeptideBeanSubList>() {

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
					service.getPeptideBeansFromPeptideProviderFromList(sessionID, peptideProvider, start, end,
							new AsyncCallback<PeptideBeanSubList>() {

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

	public void setPeptideProvider(ContainsPeptides peptideProvider) {
		if (peptideProvider.equals(this.peptideProvider))
			return;
		this.peptideProvider = peptideProvider;
		newProvider = true;
	}

}
