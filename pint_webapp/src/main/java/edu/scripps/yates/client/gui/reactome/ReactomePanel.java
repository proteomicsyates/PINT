package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.data.analysis.AnalysisResult;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.handlers.NodeSelectedHandler;
import org.reactome.web.pwp.model.client.RESTFulClient;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.scripps.yates.client.gui.DiagramLoader;

public class ReactomePanel extends ScrollPanel implements NodeSelectedHandler {

	private final SimplePanel fireWorksContainer = new SimplePanel();
	private final SimplePanel diagramContainer = new SimplePanel();
	private FireworksViewer fireworks;

	private final FlowPanel mainContainer;
	private static final DiagramLoader diagramLoader;
	private static final DiagramViewer diagram;

	static {
		// RESTFulClient
		RESTFulClient.SERVER = "./reactome";
		// FireworksFactory
		FireworksFactory.SHOW_DIAGRAM_BTN = false;
		FireworksFactory.CONSOLE_VERBOSE = false;
		FireworksFactory.SERVER = "./reactome";
		// DiagramFactory
		DiagramFactory.SHOW_FIREWORKS_BTN = false;
		DiagramFactory.SERVER = "./reactome";
		DiagramFactory.CONSOLE_VERBOSE = false;

		diagram = DiagramFactory.createDiagramViewer();
		// diagram.asWidget().getElement().getStyle().setMarginTop(20, Unit.PX);
		// diagram.asWidget().setHeight("600px");
		// diagram.asWidget().setWidth("1000px");
		diagram.asWidget().setStyleName("reactome_diagram");
		diagramLoader = new DiagramLoader(diagram);
	}

	public ReactomePanel() {
		mainContainer = new FlowPanel();
		fireWorksContainer.setStyleName("reactome_fireworks_container");
		fireWorksContainer.add(new Label("Loading Reactome Pathways Overview. Please wait..."));
		diagramContainer.setStyleName("reactome_diagram_container");
		diagramContainer.add(diagram);
		mainContainer.add(fireWorksContainer);
		mainContainer.add(diagramContainer);
		diagram.setVisible(false);
		add(mainContainer);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		// TODO Auto-generated method stub
		super.setVisible(visible);
		loadFireworks("Homo_sapiens.json");
	}

	private void addFireworks(String species) {
		fireWorksContainer.clear();
		fireworks = FireworksFactory.createFireworksViewer(species);
		fireworks.asWidget().setStyleName("reactome_fireworks");

		// fireworks.asWidget().setHeight("600px");
		// fireworks.asWidget().setWidth("1000px");
		fireworks.addNodeSelectedHandler(this);
		fireWorksContainer.add(fireworks);

		// diagram.setVisible(false);
	}

	private void onFireworksLoadError(String error) {
		// TODO
	}

	private void loadFireworks(String name) {
		String url = "./reactome/download/current/fireworks/" + name;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
		requestBuilder.setHeader("Accept", "application/json");
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					switch (response.getStatusCode()) {
					case Response.SC_OK:
						String json = response.getText();
						addFireworks(json);
						break;
					default:
						String errorMsg = "A problem has occurred while loading the pathways overview data. "
								+ response.getStatusText();
						onFireworksLoadError(errorMsg);
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					String errorMsg = "A problem has occurred while loading the pathways overview data. "
							+ exception.getMessage();
					onFireworksLoadError(errorMsg);
				}
			});
		} catch (RequestException ex) {
			String errorMsg = "A problem has occurred while connecting to the server. " + ex.getMessage();
			onFireworksLoadError(errorMsg);
		}
	}

	@Override
	public void onNodeSelected(NodeSelectedEvent event) {
		if (event.getNode() != null) {
			diagram.setVisible(true);
			diagram.onResize();
			diagramLoader.load(event.getNode().getStId());
			String data = "#Test\nPTEN\nUNC5B\nPINK";
			AnalysisSubmiter.analysis(data, new AnalysisSubmiter.AnalysisPerformedHandler() {

				@Override
				public void onAnalysisPerformed(AnalysisResult result) {
					fireworks.setAnalysisToken(result.getSummary().getToken(), "TOTAL");
				}
			});
		} else {
			diagram.setVisible(false);
		}
	}
}
