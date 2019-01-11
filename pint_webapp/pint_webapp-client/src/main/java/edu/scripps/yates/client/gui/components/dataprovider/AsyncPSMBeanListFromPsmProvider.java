package edu.scripps.yates.client.gui.components.dataprovider;

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;

public class AsyncPSMBeanListFromPsmProvider extends AbstractAsyncDataProvider<PSMBean> {
	private ContainsPSMs psmProvider;

	public AsyncPSMBeanListFromPsmProvider(String sessionID) {
		super(sessionID);
	}

	public AsyncPSMBeanListFromPsmProvider(ContainsPSMs psmProvider, String sessionID) {
		super(sessionID);
		this.psmProvider = psmProvider;
	}

	public void setPSMProvider(ContainsPSMs psmProvider) {
		if (psmProvider.equals(this.psmProvider))
			return;
		this.psmProvider = psmProvider;
		newProvider = true;
	}

	@Override
	protected void retrieveData(MyColumn<PSMBean> column, final int start, int end, ColumnSortInfo columnSortInfo,
			final Range range) {
		GWT.log("Getting PSM beans sorted from provider");
		final Comparator<PSMBean> comparator = column != null ? column.getComparator() : null;

		final boolean isAscending = columnSortInfo != null ? columnSortInfo.isAscending() : false;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {

				service.getPsmBeansFromPsmProviderFromListSorted(sessionID, psmProvider, start, end,
						column != null ? comparator : null, isAscending, new AsyncCallback<PsmBeanSubList>() {

							@Override
							public void onSuccess(PsmBeanSubList result) {
								try {
									GWT.log("Result from getting PSM beans from provider");
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
		});
	}
}
