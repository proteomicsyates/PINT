package edu.scripps.yates.client.gui.components.dataprovider;

import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.view.client.Range;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.reactome.AnalysisSubmiter;
import edu.scripps.yates.shared.util.SharedConstants;

public class AsyncPathwaySummaryDataProvider extends AbstractAsyncDataProvider<PathwaySummary> {

	private String token;

	public AsyncPathwaySummaryDataProvider() {
		super(null);
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	protected void retrieveData(MyColumn<PathwaySummary> column, final int start, int end,
			ColumnSortInfo columnSortInfo, final Range range) {
		if (token != null) {
			GWT.log("submitting reactome analysis");
			// updateRowCount(0, true);

			int page = getPageNumber(start);
			AnalysisSubmiter.getSubmittedReactomeAnalysis(token, page,
					SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE, columnSortInfo,
					new edu.scripps.yates.client.gui.reactome.AnalysisPerformedHandler() {

						@Override
						public void onAnalysisPerformed(AnalysisResult result) {
							GWT.log("Result of reactome analysis received");
							try {
								if (result == null) {
									updateRowCount(0, true);
									setRange(null);
									return;
								}
								updateRowData(start, result.getPathways());
								updateRowCount(result.getPathwaysFound(), true);
								setRange(range);
							} finally {
								retrievingDataFinished();
							}
						}
					});
		} else {
			retrievingDataFinished();
		}
	}

	private int getPageNumber(int start) {
		int numPage = start / SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE + 1;
		return numPage;
	}
}
