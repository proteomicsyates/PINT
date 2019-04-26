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
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPSMs;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;

public class AsyncPSMBeanListFromPsmProvider extends AbstractAsyncDataProvider<PSMBeanLight> {
	private ContainsLightPSMs psmProvider;

	public AsyncPSMBeanListFromPsmProvider(String sessionID) {
		super(sessionID);
	}

	public AsyncPSMBeanListFromPsmProvider(ContainsLightPSMs psmProvider, String sessionID) {
		super(sessionID);
		this.psmProvider = psmProvider;
	}

	public void setPSMProvider(ContainsLightPSMs psmProvider) {
		if (psmProvider != null && psmProvider.equals(this.psmProvider))
			return;
		this.psmProvider = psmProvider;
		newProvider = true;
	}

	@Override
	protected void retrieveData(MyColumn<PSMBeanLight> column, final int start, int end, ColumnSortInfo columnSortInfo,
			final Range range) {
		if (psmProvider == null) {
			retrievingDataFinished();
			return;
		}
		GWT.log("Getting PSM beans sorted from provider");
		final Comparator<PSMBean> comparator = column != null ? (Comparator<PSMBean>) column.getComparator() : null;

		final boolean isAscending = columnSortInfo != null ? columnSortInfo.isAscending() : false;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {

				service.getPsmBeansFromPsmProviderFromListSorted(sessionID, psmProvider, start, end, comparator,
						isAscending, new AsyncCallback<PsmBeanSubList>() {

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
