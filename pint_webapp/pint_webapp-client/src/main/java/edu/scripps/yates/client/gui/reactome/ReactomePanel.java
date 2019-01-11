package edu.scripps.yates.client.gui.reactome;

import org.reactome.web.analysis.client.AnalysisClient;
import org.reactome.web.analysis.client.model.AnalysisResult;
import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.diagram.client.DiagramFactory;
import org.reactome.web.diagram.client.DiagramViewer;
import org.reactome.web.diagram.events.FireworksOpenedEvent;
import org.reactome.web.diagram.handlers.FireworksOpenedHandler;
import org.reactome.web.fireworks.client.FireworksFactory;
import org.reactome.web.fireworks.client.FireworksViewer;
import org.reactome.web.fireworks.events.FireworksLoadedEvent;
import org.reactome.web.fireworks.events.NodeHoverEvent;
import org.reactome.web.fireworks.events.NodeOpenedEvent;
import org.reactome.web.fireworks.events.NodeSelectedEvent;
import org.reactome.web.fireworks.events.ProfileChangedEvent;
import org.reactome.web.fireworks.handlers.AnalysisResetHandler;
import org.reactome.web.fireworks.handlers.FireworksLoadedHandler;
import org.reactome.web.fireworks.handlers.NodeHoverHandler;
import org.reactome.web.fireworks.handlers.NodeHoverResetHandler;
import org.reactome.web.fireworks.handlers.NodeOpenedHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedHandler;
import org.reactome.web.fireworks.handlers.NodeSelectedResetHandler;
import org.reactome.web.fireworks.handlers.ProfileChangedHandler;
import org.reactome.web.fireworks.model.Node;
import org.reactome.web.pwp.model.client.RESTFulClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.PopUpPanelYesNo;
import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.components.ScrolledTabLayoutPanel;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPathwaySummaryDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;

public class ReactomePanel extends ResizeLayoutPanel
		implements NodeSelectedHandler, FireworksLoadedHandler, AnalysisResetHandler, NodeHoverHandler,
		NodeSelectedResetHandler, NodeHoverResetHandler, NodeOpenedHandler, ProfileChangedHandler {

	private final SimpleLayoutPanel fireworksContainer = new SimpleLayoutPanel();
	private final SimplePanel diagramContainer = new SimplePanel();
	private FireworksViewer fireworks;
	private static ReactomePanel instance;

	private final String sessionID;
	private final ScrolledTabLayoutPanel tabPanel;
	private final DiagramLoader diagramLoader;
	private final DiagramViewer diagram;
	private boolean fireWorksLoaded;
	private final SimpleCheckBox includeInteractorsCheckBox;
	private final AsyncPathwaySummaryDataProvider asyncDataListProvider;

	private PathwaySummary toHighlight;
	private PathwaySummary toSelect;
	private PathwaySummary toOpen;
	private final ReactomePathwaysTablePanel reactomeTable;
	private Node selectedNodeInFireWorks;
	private boolean requestingFireworks;
	private final ListBox speciesComboBox;
	private ReactomeSupportedSpecies dataSpecies;
	private final Button reactomeSubmitButton;
	private final SimpleCheckBox projectToHumanCheckBox;
	private String currentProjectSpecies;

	static {
		// RESTFulClient
		RESTFulClient.SERVER = "./reactome";
		AnalysisClient.SERVER = "./reactome";
		// FireworksFactory
		FireworksFactory.SHOW_DIAGRAM_BTN = true;
		FireworksFactory.CONSOLE_VERBOSE = true;
		FireworksFactory.SERVER = "./reactome";
		FireworksFactory.SHOW_INFO = false;
		FireworksFactory.ILLUSTRATION_SERVER = FireworksFactory.SERVER;
		// DiagramFactory
		DiagramFactory.SHOW_FIREWORKS_BTN = true;
		DiagramFactory.SERVER = "./reactome";
		DiagramFactory.CONSOLE_VERBOSE = true;
		DiagramFactory.SHOW_INFO = false;

	}

	public static ReactomePanel getInstance(String sessionID) {
		if (instance == null || (sessionID != null && !instance.sessionID.equals(sessionID))) {
			instance = new ReactomePanel(sessionID);
		}
		return instance;
	}

	private ReactomePanel(final String sessionID) {
		this.sessionID = sessionID;
		asyncDataListProvider = new AsyncPathwaySummaryDataProvider();
		this.setStyleName("queryPanelDataTablesPanel");
		tabPanel = new ScrolledTabLayoutPanel();
		tabPanel.setHeight("100%");
		add(tabPanel);

		final DockLayoutPanel mainPanelAnalysis = new DockLayoutPanel(Unit.PX);
		mainPanelAnalysis.setSize("100%", "100%");
		tabPanel.add(mainPanelAnalysis, "Data Analysis");
		final CaptionPanel captionPanel = new CaptionPanel("Reactome data analysis service client");
		mainPanelAnalysis.addNorth(captionPanel, 200);
		// captionPanel.setHeight("20%");
		// panelAnalysis.setWidgetTopHeight(captionPanel, 0, Unit.PCT, 20,
		// Unit.PCT);

		final FlowPanel submissionButtonPanel = new FlowPanel();
		submissionButtonPanel.setStyleName("reactomePanelSubmit");
		captionPanel.add(submissionButtonPanel);
		final Label label = new Label(
				"Click to submit in order to search for enriched pathways in the current dataset");
		label.getElement().getStyle().setMargin(10, Unit.PX);
		reactomeSubmitButton = new Button("Submit");
		reactomeSubmitButton.getElement().getStyle().setMargin(10, Unit.PX);
		submissionButtonPanel.add(label);
		final Label labelIncludeInteractors = new Label(
				"Include IntAct interactors to increase the analysis background:");
		final String title = "IntAct interactors are used to increase the analysis background";
		labelIncludeInteractors.setTitle(title);
		final FlexTable table = new FlexTable();
		table.getElement().getStyle().setMargin(10, Unit.PX);
		submissionButtonPanel.add(table);
		table.setWidget(0, 0, labelIncludeInteractors);
		includeInteractorsCheckBox = new SimpleCheckBox();
		includeInteractorsCheckBox.setTitle(title);
		table.setWidget(0, 1, includeInteractorsCheckBox);
		final Label labelProjectToHuman = new Label("Project the result to Homo Sapiens:");
		final String titleProjectToHuman = "Analyse the identifiers in the file over the different \n"
				+ "species and projects the result to Homo Sapiens.\n"
				+ "The projection is calculated by the orthologous \n" + "slot in the Reactome database. ";
		labelProjectToHuman.setTitle(titleProjectToHuman);
		table.setWidget(1, 0, labelProjectToHuman);
		projectToHumanCheckBox = new SimpleCheckBox();
		projectToHumanCheckBox.setTitle(titleProjectToHuman);
		projectToHumanCheckBox.setValue(true);// selected by default
		// capture if it is selected or not, to change the selection in the
		// species comboBox
		// if it is selected, select Human in the comboBox
		projectToHumanCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// if selected, change to human the species comboBox
				if (event.getValue() != null && event.getValue() == true) {
					if (speciesComboBox != null) {
						GWT.log("Selecting Homo_sapiens");
						selectSpecies(ReactomeSupportedSpecies.Homo_sapiens);
					}
				}

			}
		});
		table.setWidget(1, 1, projectToHumanCheckBox);
		submissionButtonPanel.add(reactomeSubmitButton);
		reactomeSubmitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				reactomeSubmitButton.setEnabled(false);
				reactomeTable.setVisible(false);
				// if taxonomy of the data is different from selected, change to
				// the selected
				// but only if projection to human is not selected, in that
				// case, the dataSpecies should be Human
				if (projectToHumanCheckBox.getValue() == true
						&& getSelectedSpecies() != ReactomeSupportedSpecies.Homo_sapiens) {
					final PopUpPanelYesNo yesNo = new PopUpPanelYesNo(false, true, true, "Pathway browser taxonomy",
							"Selecting the analysis projection to Human requires to load the '"
									+ ReactomeSupportedSpecies.Homo_sapiens.getScientificName() + "' pathways.\n"
									+ "The view will automatically change to show '"
									+ ReactomeSupportedSpecies.Homo_sapiens.getScientificName() + "' pathways.",
							"OK", null);
					yesNo.addButton1ClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							selectSpecies(ReactomeSupportedSpecies.Homo_sapiens);
							submitReactomeAnalysis();
							yesNo.hide();
						}
					});
					yesNo.show();
				} else if (dataSpecies != null && dataSpecies != getSelectedSpecies()) {
					final PopUpPanelYesNo yesNo = new PopUpPanelYesNo(false, true, true, "Pathway browser taxonomy",
							"The taxonomy of the proteins that are going to be analyzed ('"
									+ dataSpecies.getScientificName() + "')\n"
									+ "is different from the selected species in the Reactome fireworks view '"
									+ getSelectedSpecies().getScientificName() + "'.\n"
									+ "The view will be changed now to show pathways for '"
									+ dataSpecies.getScientificName() + "'.",
							"OK", null);
					yesNo.addButton1ClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							selectSpecies(dataSpecies);
							submitReactomeAnalysis();
							yesNo.hide();
						}
					});
					yesNo.show();
				} else {
					submitReactomeAnalysis();

				}
			}
		});
		//
		// FlowPanel flowPanelTable = new FlowPanel();
		reactomeTable = new ReactomePathwaysTablePanel(sessionID, asyncDataListProvider);
		reactomeTable.setVisible(false);
		// Grid grid = new Grid(1, 1);
		// grid.setWidget(0, 0, reactomeTable);
		// mainPanelAnalysis.add(grid);
		mainPanelAnalysis.add(reactomeTable);
		// panelAnalysis.setWidgetTopHeight(flowPanel, 20, Unit.PCT, 70,
		// Unit.PCT);

		final FlowPanel flowPanelGraphical = new FlowPanel();
		tabPanel.add(flowPanelGraphical, "Reactome view");
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 1) {
					// requestFireworks(getSelectedSpecies(), false);
				} else if (event.getSelectedItem() == 0) {
					// refresh table
					reactomeTable.refreshData();
				}

			}
		});

		flowPanelGraphical.setSize("100%", "100%");
		flowPanelGraphical.setStyleName("flowPanelGraphical");
		final FlexTable tableSpecies = new FlexTable();
		tableSpecies.setStyleName("reactomeTableSpecies");
		tableSpecies.setWidget(0, 0, new Label("Pathways for:"));
		speciesComboBox = createSpeciesComboBox();
		tableSpecies.setWidget(0, 1, speciesComboBox);
		flowPanelGraphical.add(tableSpecies);
		flowPanelGraphical.add(fireworksContainer);

		fireworksContainer.setStyleName("reactome_fireworks_container");

		diagram = DiagramFactory.createDiagramViewer();
		// diagram.asWidget().getElement().getStyle().setMarginTop(20, Unit.PX);
		// diagram.asWidget().setHeight("600px");
		// diagram.asWidget().setWidth("1000px");
		diagram.asWidget().setStyleName("reactome_diagram");
		diagramLoader = new DiagramLoader(diagram);
		final FireworksOpenedHandler handler = new FireworksOpenedHandler() {

			@Override
			public void onFireworksOpened(FireworksOpenedEvent event) {
				GWT.log("from ReactomePanel onFireworksOpened:" + event.toDebugString());

				fireworksContainer.setVisible(true);
				fireworks.showAll();

				final Node nodeSelected = fireworks.getSelected();
				if (nodeSelected != null) {
					fireworks.selectNode(nodeSelected.getDbId());
				}
				diagramContainer.setVisible(false);
			}
		};
		diagramLoader.addFireworksOpenedHandler(handler);
		diagramContainer.setStyleName("reactome_diagram_container");
		diagramContainer.setVisible(false);
		diagramContainer.add(diagram);

		flowPanelGraphical.add(diagramContainer);

		requestFireworks(getSelectedSpecies(), false);
		tabPanel.selectTab(flowPanelGraphical);
	}

	private void submitReactomeAnalysis() {
		AnalysisSubmiter.analysisExternalURL(sessionID, includeInteractorsCheckBox.getValue(),
				projectToHumanCheckBox.getValue(), new AnalysisPerformedHandler() {

					@Override
					public void onAnalysisPerformed(AnalysisResult result) {
						reactomeTable.setVisible(true);
						reactomeSubmitButton.setEnabled(true);
						final String token = result.getSummary().getToken();

						fireworks.setAnalysisToken(token, "TOTAL");
						// set the token to the asyncDataListProvider
						asyncDataListProvider.setToken(token);
						loadAnalysisOnTable(result);
						StatusReportersRegister.getInstance()
								.notifyStatusReporters("Reactome analysis received with " + result.getPathwaysFound()
										+ " pathways found.\n" + "Click on a pathway to open the pathway browser.");

					}

					@Override
					public void onAnalysisError(Throwable exception) {
						reactomeSubmitButton.setEnabled(true);
					}
				});

	}

	private ListBox createSpeciesComboBox() {
		final ListBox ret = new ListBox();
		int index = 0;
		int selectedIndex = 0;
		for (final ReactomeSupportedSpecies species : ReactomeSupportedSpecies.values()) {
			if (dataSpecies != null && species == dataSpecies) {
				selectedIndex = index;
			}
			ret.addItem(species.getScientificName(), species.getScientificName());
			index++;
		}
		ret.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final ReactomeSupportedSpecies selectedSpecies = getSelectedSpecies();
				if (selectedSpecies != null) {
					requestFireworks(selectedSpecies, true);
				}

			}
		});
		// select the selectedIndex
		ret.setItemSelected(selectedIndex, true);
		return ret;
	}

	public void setDataSpecies(String speciesName) {
		final ReactomeSupportedSpecies selectedSpecies = getSelectedSpecies();
		dataSpecies = ReactomeSupportedSpecies.getByScientificName(speciesName);
		if (dataSpecies == null) {
			GWT.log("Species from the data is not supported: " + speciesName);
		} else {
			GWT.log("Species from the data supported: " + speciesName);
			if (dataSpecies != selectedSpecies) {
				selectSpecies(dataSpecies);
			}
		}
	}

	private void selectSpecies(ReactomeSupportedSpecies dataSpecies2) {
		for (int index = 0; index < speciesComboBox.getItemCount(); index++) {
			if (speciesComboBox.getValue(index).equals(dataSpecies2.getScientificName())) {
				speciesComboBox.setItemSelected(index, true);
				requestFireworks(dataSpecies2, true);
			} else {
				speciesComboBox.setItemSelected(index, false);
			}
		}

	}

	private ReactomeSupportedSpecies getSelectedSpecies() {
		return ReactomeSupportedSpecies.getByScientificName(speciesComboBox.getSelectedValue());
	}

	private void loadFireworks(String json) {
		GWT.log("**********Creating new Canvas for firworksViewer");
		fireworks = FireworksFactory.createFireworksViewer(json);
		fireworks.asWidget().setStyleName("reactome_fireworks");
		fireworks.addAnalysisResetHandler(this);
		fireworks.addFireworksLoaded(this);
		fireworks.addNodeHoverResetHandler(this);
		fireworks.addNodeHoverHandler(this);
		fireworks.addNodeOpenedHandler(this);
		fireworks.addNodeSelectedHandler(this);
		fireworks.addNodeSelectedResetHandler(this);
		fireworks.addProfileChangedHandler(this);
		// fireworks.asWidget().setHeight("600px");
		// fireworks.asWidget().setWidth("1000px");
		fireworks.addNodeSelectedHandler(this);
		fireworksContainer.clear();
		fireworksContainer.add(fireworks);

		// diagram.setVisible(false);
	}

	private void onFireworksLoadError(String error) {
		StatusReportersRegister.getInstance().notifyStatusReporters(error);
	}

	private void requestFireworks(ReactomeSupportedSpecies species, boolean forceRequest) {
		try {
			GWT.log("requestFireworks START");
			if (requestingFireworks) {
				GWT.log("requestingFireworks is true");
				return;
			}
			if (!forceRequest && fireWorksLoaded) {
				GWT.log("calling to showAll");
				fireworks.showAll();
				GWT.log("showAll back");
				return;
			}
			GWT.log("Actually requesting fireworks remotelly");
			fireworksContainer.clear();
			fireworksContainer.add(getLoadingMessage(species));
			final String url = "./reactome/download/current/fireworks/" + species.toJSonString();
			final RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
			requestBuilder.setHeader("Accept", "application/json");
			try {
				requestingFireworks = true;
				requestBuilder.sendRequest(null, new RequestCallback() {
					@Override
					public void onResponseReceived(Request request, Response response) {
						try {
							GWT.log("Response received from requestFireworks");

							switch (response.getStatusCode()) {
							case Response.SC_OK:
								final String json = response.getText();
								GWT.log("Calling loadFireworks");

								loadFireworks(json);
								GWT.log("loadFireworks back");

								break;
							default:
								final String errorMsg = "A problem has occurred while loading the pathways overview data. "
										+ response.getStatusText();
								onFireworksLoadError(errorMsg);
							}
						} finally {
							requestingFireworks = false;
						}
					}

					@Override
					public void onError(Request request, Throwable exception) {
						try {
							final String errorMsg = "A problem has occurred while loading the pathways overview data. "
									+ exception.getMessage();
							onFireworksLoadError(errorMsg);
						} finally {
							requestingFireworks = false;
						}
					}
				});
				GWT.log("fireworks remotelly requested");

			} catch (final RequestException ex) {
				final String errorMsg = "A problem has occurred while connecting to the server. " + ex.getMessage();
				onFireworksLoadError(errorMsg);
			}
		} finally {
			GWT.log("requestFireworks END");
		}
	}

	@Override
	public void onNodeSelected(NodeSelectedEvent event) {
		if (event.getNode() != null) {
			if (event.getNode().equals(selectedNodeInFireWorks)) {
				return;
			}
			GWT.log("Selected node: " + event.getNode().getStId());
			// we want to make a zoom to the pathway node in the fireworks
			// fireworks.openPathway(event.getNode().getStId());
			// fireworks.selectNode(event.getNode().getDbId());
			selectedNodeInFireWorks = event.getNode();
			// diagram.setVisible(true);
			// diagram.onResize();
			// diagramLoader.load(event.getNode().getStId());
			//
		} else {
			GWT.log("Selected null node");
			// diagram.setVisible(false);
			// fireworks.setVisible(true);
		}
	}

	protected void loadAnalysisOnTable(AnalysisResult result) {
		reactomeTable.setVisible(true);
		asyncDataListProvider.updateRowCount(result.getPathwaysFound(), true);
		asyncDataListProvider.updateRowData(0, result.getPathways());
		reactomeTable.refreshData();
		// RangeChangeEvent.fire(pathWayDataGrid,
		// pathWayDataGrid.getVisibleRange());
	}

	public void selectPathWay(PathwaySummary pathway) {
		GWT.log("Selecting pathway: " + pathway.getStId() + "\t" + pathway.getDbId());

		selectGraphicTab();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				fireworks.onResize();
				// reset selections and highlights
				GWT.log("Calling to resetHighlight");
				fireworks.resetHighlight();
				GWT.log("resetHighlight back");

				GWT.log("Calling to resetSelection");
				fireworks.resetSelection();
				GWT.log("resetSelection back");

				GWT.log("Calling to showAll");
				fireworks.showAll();
				GWT.log("showAll back");

				// highlight and select node
				StatusReportersRegister.getInstance().notifyStatusReporters(
						"Selecting Pathway stId:" + pathway.getStId() + " dbId:" + pathway.getDbId(),
						QueryPanel.class.getName());
				toHighlight = pathway;
				toSelect = pathway;
				// toOpen = pathway;
				applyCarriedActions();

				if (fireworks.getSelected() == null) {
					StatusReportersRegister.getInstance().notifyStatusReporters(
							"Pathway stId:" + pathway.getStId() + " dbId:" + pathway.getDbId() + " not found");
				}
			}
		});

	}

	private void selectGraphicTab() {
		tabPanel.selectTab(1, true);

	}

	private void applyCarriedActions() {
		// ORDER IS IMPORTANT IN THE FOLLOWING CONDITIONS
		if (toOpen != null) {
			GWT.log("Calling to openPahtway");
			fireworks.openPathway(toOpen.getDbId());
			GWT.log("openPahtway back");

			toOpen = null;
			toSelect = null;
			toHighlight = null;
		}
		if (toSelect != null) {
			final Long dbId = toSelect.getDbId();
			GWT.log("Calling to selectNode");
			fireworks.selectNode(dbId);
			GWT.log("selectNode back");

			toSelect = null;
		}
		if (toHighlight != null) {
			GWT.log("Calling to highlightNode");

			fireworks.highlightNode(toHighlight.getDbId());
			GWT.log("highlightNode back");

			toHighlight = null;
		}

	}

	@Override
	public void onAnalysisReset() {
		GWT.log("onAnalysisReset");
	}

	@Override
	public void onFireworksLoaded(FireworksLoadedEvent event) {
		GWT.log("on fireworks loaded START");
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				fireworks.onResize();
				GWT.log("calling showAll in loadFireworks");
				fireworks.showAll();
				GWT.log("showAll in loadFireworks back");
				fireWorksLoaded = true;
				GWT.log("on fireworks loaded END");
			}
		});

	}

	@Override
	public void onNodeHover(NodeHoverEvent event) {
		GWT.log("on node hover");
		if (fireWorksLoaded) {
			// fireworks.highlightNode(event.getNode().getDbId());
		}
	}

	@Override
	public void onNodeOpened(NodeOpenedEvent event) {
		GWT.log("onNodeOpened START");
		if (event.getNode() != null) {
			fireworksContainer.setVisible(false);
			diagramContainer.setVisible(true);
			diagram.onResize();
			diagramLoader.load(event.getNode().getStId());

		} else {
			fireworksContainer.setVisible(true);
			diagramContainer.setVisible(false);
		}
		// // presenter.showPathwayDiagram(event.getNode().getDbId());
		// // hide the fireworks container
		//
		GWT.log("onNodeOpened END");
	}

	@Override
	public void onNodeSelectionReset() {
		// presenter.resetPathwaySelection();
		GWT.log("onNodeSelectionReset");
		selectedNodeInFireWorks = null;
	}

	@Override
	public void onNodeHoverReset() {
		// presenter.resetPathwayHighlighting();
		GWT.log("onNodeHoverReset");
	}

	@Override
	public void onProfileChanged(ProfileChangedEvent event) {
		// presenter.profileChanged(event.getProfile().getName());
		GWT.log("onProfileChanged");
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && fireworks != null) {
			fireworks.onResize();
			fireworks.showAll();
		}
	}

	/**
	 * Getting a panel with loading message and symbol.
	 *
	 * @return Widget
	 */
	private Widget getLoadingMessage(ReactomeSupportedSpecies specie) {
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(MyClientBundle.INSTANCE.horizontalLoader()));
		hp.add(new HTMLPanel("Loading pathways overview graph for '" + specie.getScientificName() + "'..."));
		hp.setSpacing(5);

		return hp;
	}

	public void setProjectDataSpecies(String speciesName) {
		currentProjectSpecies = speciesName;

	}
}
