package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.analysis.client.exceptions.AnalysisModelException;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.factory.AnalysisModelFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.PathWayTextColumn;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;

public class AnalysisSubmiter {
	private static final String POST_ANALYSIS = "./reactome/AnalysisService/identifiers/?page=1&pageSize="
			+ SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE;
	private static final String POST_ANALYSIS_EXTERNAL_URL = "./reactome/AnalysisService/identifiers/url/?page=1&pageSize="
			+ SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE;
	private static final String POST_ANALYSIS_EXTERNAL_URL_PROJECTED_TO_HUMAN = "./reactome/AnalysisService/identifiers/url/projection?page=1&pageSize="
			+ SharedConstants.REACTOME_PATHWAYS_DEFAULT_PAGE_SIZE;

	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();

	public static void analysis(String data, final AnalysisPerformedHandler handler) {

		String url = POST_ANALYSIS;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
		requestBuilder.setHeader("Content-Type", "text/plain");
		requestBuilder.setHeader("Accept", "application/json");
		try {
			requestBuilder.sendRequest(data, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						// TODO ERROR
					} else {
						try {
							AnalysisResult result;

							result = AnalysisModelFactory.getModelObject(AnalysisResult.class, response.getText());

							handler.onAnalysisPerformed(result);
						} catch (org.reactome.web.analysis.client.exceptions.AnalysisModelException e) {
							// TODO Console.error("Oops! This is unexpected",
							// this);
						}
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					// TODO fireEvent(new ServiceUnavailableEvent());
				}
			});
		} catch (RequestException ex) {
			// TODO fireEvent(new ServiceUnavailableEvent());
		}
	}

	public static void analysisExternalURL(String sessionID, final Boolean includeInteractors, boolean projectToHuman,
			final edu.scripps.yates.client.gui.reactome.AnalysisPerformedHandler handler) {

		service.getDownloadLinkForReactomeAnalysisResult(sessionID, new AsyncCallback<FileDescriptor>() {

			@Override
			public void onSuccess(FileDescriptor fileDescriptor) {
				String dataURL = null;
				final String hostPageBaseURL = GWT.getHostPageBaseURL();
				if (!hostPageBaseURL.contains("127.0.0.1")) {
					dataURL = ClientSafeHtmlUtils.getDownloadURL(fileDescriptor.getName(),
							SharedConstants.REACTOME_ANALYSIS_RESULT_FILE_TYPE);
				} else {
					dataURL = "http://sealion.scripps.edu/pint/pint/download?" + SharedConstants.FILE_TO_DOWNLOAD
							+ "=Reactome.txt&" + SharedConstants.FILE_TYPE + "="
							+ SharedConstants.REACTOME_ANALYSIS_RESULT_FILE_TYPE;
				}
				if (dataURL == null) {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("WARNING: Dataset is not ready or is empty");
				} else {
					submitReactomeAnalysis(dataURL, includeInteractors, projectToHuman, handler);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}
		});

	}

	protected static void submitReactomeAnalysis(String dataURL, Boolean includeInteractors, boolean projectToHuman,
			final AnalysisPerformedHandler handler) {
		// TODO for tests purposes
		// dataURL =
		// "http://sealion.scripps.edu/pint/pint/download?filetodownload=Reactome.txt&fileType=reactomeAnalysisResultFile";
		String postURL = POST_ANALYSIS_EXTERNAL_URL;
		if (projectToHuman) {
			postURL = POST_ANALYSIS_EXTERNAL_URL_PROJECTED_TO_HUMAN;
		}
		if (includeInteractors != null) {
			postURL += "&interactors=" + includeInteractors;
		}
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, postURL);
		requestBuilder.setHeader("Content-Type", "text/plain");
		requestBuilder.setHeader("Accept", "application/json");
		try {
			// String externalURL = Window.Location.getProtocol() + "//"
			// + Window.Location.getHost() + dataURL;
			String externalURL = dataURL;
			// StatusReportersRegister.getInstance()
			// .notifyStatusReporters("Submitting analysis to Reactome as '" +
			// postURL + "'");

			// externalURL =
			// "http://sealion.scripps.edu/pint/Reactome.txt";
			requestBuilder.sendRequest(externalURL, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						StatusReportersRegister.getInstance().notifyStatusReporters(response.getStatusText());
					} else {
						try {
							AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class,
									response.getText());
							handler.onAnalysisPerformed(result);
						} catch (AnalysisModelException e) {
							StatusReportersRegister.getInstance().notifyStatusReporters(e);
						}
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					handler.onAnalysisError(exception);
					StatusReportersRegister.getInstance().notifyStatusReporters(exception);

				}
			});
		} catch (RequestException ex) {
			StatusReportersRegister.getInstance().notifyStatusReporters(ex);
		}

	}

	public static void getSubmittedReactomeAnalysis(String token, int page, int pageSize, ColumnSortInfo columnSortInfo,
			final AnalysisPerformedHandler handler) {

		String getURL = getAnalysisPagedResultByToken(token, page, pageSize, columnSortInfo);
		GWT.log("URL for reactome: " + getURL);
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, getURL);
		requestBuilder.setHeader("Content-Type", "text/plain");
		requestBuilder.setHeader("Accept", "application/json");
		try {

			requestBuilder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						StatusReportersRegister.getInstance().notifyStatusReporters(response.getStatusText());
					} else {
						try {
							AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class,
									response.getText());
							handler.onAnalysisPerformed(result);
						} catch (AnalysisModelException e) {
							StatusReportersRegister.getInstance().notifyStatusReporters(e);
						}
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					StatusReportersRegister.getInstance().notifyStatusReporters(exception);

				}
			});
		} catch (RequestException ex) {
			StatusReportersRegister.getInstance().notifyStatusReporters(ex);
		}

	}

	private static String getAnalysisPagedResultByToken(String token, int page, int pageSize,
			ColumnSortInfo columnSortInfo) {
		String string = "./reactome/AnalysisService/token/" + token + "?page=" + page + "&pageSize=" + pageSize;
		if (columnSortInfo != null) {
			String order = columnSortInfo.isAscending() ? "ASC" : "DESC";
			string += "&order=" + order;
			String sortBy = ColumnName.getReactomeAnalysisPathwayColumnName(
					((PathWayTextColumn) columnSortInfo.getColumn()).getColumnName());
			if (sortBy != null) {
				string += "&sortBy=" + sortBy;
			}
		}
		return string;
	}
}
