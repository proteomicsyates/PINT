package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.diagram.data.analysis.AnalysisResult;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;

public class AnalysisSubmiter {
	private static final String POST_ANALYSIS = "./reactome/AnalysisService/identifiers/?page=1";
	private static final String POST_ANALYSIS_EXTERNAL_URL = "./reactome/AnalysisService/identifiers/url/?page=1";
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();

	public interface AnalysisPerformedHandler {
		void onAnalysisPerformed(AnalysisResult result);
	}

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
							AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class,
									response.getText());
							handler.onAnalysisPerformed(result);
						} catch (AnalysisModelException e) {
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

	public static void analysisExternalURL(String sessionID, final AnalysisPerformedHandler handler) {

		service.getPublicDataSetURL(sessionID, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String dataURL) {
				String url = POST_ANALYSIS_EXTERNAL_URL;
				RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, url);
				requestBuilder.setHeader("Content-Type", "text/plain");
				requestBuilder.setHeader("Accept", "application/json");
				try {
					requestBuilder.sendRequest(dataURL, new RequestCallback() {
						@Override
						public void onResponseReceived(Request request, Response response) {
							if (response.getStatusCode() != Response.SC_OK) {
								// TODO ERROR
							} else {
								try {
									AnalysisResult result = AnalysisModelFactory.getModelObject(AnalysisResult.class,
											response.getText());
									handler.onAnalysisPerformed(result);
								} catch (AnalysisModelException e) {
									// TODO Console.error("Oops! This is
									// unexpected",
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

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});

	}
}
