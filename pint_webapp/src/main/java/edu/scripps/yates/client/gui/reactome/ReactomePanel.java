package edu.scripps.yates.client.gui.reactome;

import java.util.List;

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
import org.reactome.web.pwp.model.classes.Pathway;
import org.reactome.web.pwp.model.client.RESTFulClient;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.gui.columns.MyColumn;
import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.gui.columns.MySafeHtmlHeaderWithTooltip;
import edu.scripps.yates.client.gui.columns.PathWaysColumnManager;
import edu.scripps.yates.client.gui.components.ScrolledTabLayoutPanel;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPathwaySummaryDataProvider;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;

public class ReactomePanel extends FlowPanel
		implements NodeSelectedHandler, FireworksLoadedHandler, AnalysisResetHandler, NodeHoverHandler,
		NodeSelectedResetHandler, NodeHoverResetHandler, NodeOpenedHandler, ProfileChangedHandler {

	private final SimplePanel fireWorksContainer = new SimplePanel();
	private FireworksViewer fireworks;
	private static ReactomePanel instance;

	private final MyDataGrid<PathwaySummary> pathWayDataGrid;
	private final String sessionID;
	private final ScrolledTabLayoutPanel tabPanel;
	private final DiagramLoader diagramLoader;
	private final DiagramViewer diagram;
	private boolean fireWorksLoaded;
	private final SimpleCheckBox includeInteractorsCheckBox;
	private final SingleSelectionModel<PathwaySummary> selectionModel;
	private final AsyncPathwaySummaryDataProvider asyncDataListProvider = new AsyncPathwaySummaryDataProvider();

	private Pathway toHighlight;
	private Pathway toSelect;
	private Pathway toOpen;
	static {
		// RESTFulClient
		RESTFulClient.SERVER = "./reactome";
		AnalysisClient.SERVER = "./reactome";
		// FireworksFactory
		FireworksFactory.SHOW_DIAGRAM_BTN = true;
		FireworksFactory.CONSOLE_VERBOSE = true;
		FireworksFactory.SERVER = "./reactome";
		FireworksFactory.SHOW_INFO = false;
		// DiagramFactory
		DiagramFactory.SHOW_FIREWORKS_BTN = true;
		DiagramFactory.SERVER = "./reactome";
		DiagramFactory.CONSOLE_VERBOSE = true;
		DiagramFactory.SHOW_INFO = false;

	}

	public static ReactomePanel getInstance(String sessionID) {
		if (instance == null || !instance.sessionID.equals(sessionID)) {
			instance = new ReactomePanel(sessionID);
		}
		return instance;
	}

	private ReactomePanel(final String sessionID) {
		this.sessionID = sessionID;

		this.setStyleName("queryPanelDataTablesPanel");
		tabPanel = new ScrolledTabLayoutPanel();
		tabPanel.setHeight("100%");
		add(tabPanel);

		FlowPanel mainPanelAnalysis = new FlowPanel();
		mainPanelAnalysis.setSize("100%", "100%");
		tabPanel.add(mainPanelAnalysis, "Data Analysis");
		CaptionPanel captionPanel = new CaptionPanel("Reactome data analysis service client");
		mainPanelAnalysis.add(captionPanel);
		captionPanel.setHeight("20%");
		// panelAnalysis.setWidgetTopHeight(captionPanel, 0, Unit.PCT, 20,
		// Unit.PCT);

		FlowPanel submissionButtonPanel = new FlowPanel();
		submissionButtonPanel.setStyleName("reactomePanelSubmit");
		captionPanel.add(submissionButtonPanel);
		final Label label = new Label(
				"Click to submit in order to search for enriched pathways in the current dataset");
		label.getElement().getStyle().setMargin(10, Unit.PX);
		final Button reactomeSubmitButton = new Button("Submit");
		reactomeSubmitButton.getElement().getStyle().setMargin(10, Unit.PX);
		submissionButtonPanel.add(label);
		final Label labelIncludeInteractors = new Label(
				"Include IntAct interactors to increase the analysis background:");
		String title = "IntAct interactors are used to increase the analysis background";
		labelIncludeInteractors.setTitle(title);
		FlexTable table = new FlexTable();
		table.getElement().getStyle().setMargin(10, Unit.PX);
		submissionButtonPanel.add(table);
		table.setWidget(0, 0, labelIncludeInteractors);
		includeInteractorsCheckBox = new SimpleCheckBox();
		table.setWidget(0, 1, includeInteractorsCheckBox);
		submissionButtonPanel.add(reactomeSubmitButton);
		reactomeSubmitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				requestFireworks("Homo_sapiens.json");
				reactomeSubmitButton.setEnabled(false);
				AnalysisSubmiter.analysisExternalURL(sessionID, includeInteractorsCheckBox.getValue(),
						new AnalysisPerformedHandler() {

							@Override
							public void onAnalysisPerformed(AnalysisResult result) {
								reactomeSubmitButton.setEnabled(true);
								final String token = result.getSummary().getToken();
								fireworks.setAnalysisToken(token, "TOTAL");
								// set the token to the asyncDataListProvider
								asyncDataListProvider.setToken(token);
								loadAnalysisOnTable(result);
								StatusReportersRegister.getInstance()
										.notifyStatusReporters("Reactome analysis received with "
												+ result.getPathwaysFound() + " pathways found.");

							}
						});

			}
		});
		//
		FlowPanel flowPanelTable = new FlowPanel();
		mainPanelAnalysis.add(flowPanelTable);
		flowPanelTable.setSize("100%", "80%");
		ResizeLayoutPanel resizePanel = new ResizeLayoutPanel();
		resizePanel.setSize("100%", "90%");
		flowPanelTable.add(resizePanel);

		pathWayDataGrid = createDataGrid();
		pathWayDataGrid.setVisible(false);
		asyncDataListProvider.addDataDisplay(pathWayDataGrid);
		AsyncHandler asyncHandler = new AsyncHandler(pathWayDataGrid);
		pathWayDataGrid.addColumnSortHandler(asyncHandler);
		pathWayDataGrid.setAutoHeaderRefreshDisabled(true);

		resizePanel.add(pathWayDataGrid);
		// add the pager
		SimplePager pager = makePager();
		flowPanelTable.add(pager);
		// add the selection manager
		selectionModel = new SingleSelectionModel<PathwaySummary>();
		pathWayDataGrid.setSelectionModel(selectionModel);

		// panelAnalysis.setWidgetTopHeight(flowPanel, 20, Unit.PCT, 70,
		// Unit.PCT);

		ScrollPanel scroll2 = new ScrollPanel();
		tabPanel.add(scroll2, "Reactome view");
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == 1) {
					requestFireworks("Homo_sapiens.json");
				} else if (event.getSelectedItem() == 0) {
					// refresh table
					refreshTable();
				}

			}
		});
		FlowPanel flowPanelGraphical = new FlowPanel();
		scroll2.add(flowPanelGraphical);
		flowPanelGraphical.add(fireWorksContainer);

		fireWorksContainer.setStyleName("reactome_fireworks_container");
		fireWorksContainer.add(getLoadingMessage());
		diagram = DiagramFactory.createDiagramViewer();
		// diagram.asWidget().getElement().getStyle().setMarginTop(20, Unit.PX);
		// diagram.asWidget().setHeight("600px");
		// diagram.asWidget().setWidth("1000px");
		diagram.asWidget().setStyleName("reactome_diagram");
		diagramLoader = new DiagramLoader(diagram);
		FireworksOpenedHandler handler = new FireworksOpenedHandler() {

			@Override
			public void onFireworksOpened(FireworksOpenedEvent event) {
				diagram.setVisible(false);
				fireWorksContainer.setVisible(true);
				fireworks.onResize();
				fireworks.selectNode(fireworks.getSelected().getDbId());
			}
		};
		diagramLoader.addFireworksOpenedHandler(handler);
		SimplePanel diagramContainer = new SimplePanel();
		diagramContainer.setStyleName("reactome_diagram_container");
		diagramContainer.add(diagram);

		flowPanelGraphical.add(diagramContainer);
		diagram.setVisible(false);

	}

	protected void refreshTable() {
		pathWayDataGrid.redraw();
	}

	/**
	 * Adds the selectionHandler to the pathway table.
	 *
	 * @param selectionHandler
	 */
	public void addPeptideSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
		selectionModel.addSelectionChangeHandler(selectionHandler);
	}

	private SimplePager makePager() {
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		SimplePager simplePager = new SimplePager(TextLocation.CENTER, pagerResources, true, 25 * 5, true);
		simplePager.setPageSize(25);
		simplePager.setDisplay(pathWayDataGrid);
		return simplePager;
	}

	private MyDataGrid<PathwaySummary> createDataGrid() {
		final ProvidesKey<PathwaySummary> KEY_PROVIDER = new ProvidesKey<PathwaySummary>() {
			@Override
			public Object getKey(PathwaySummary item) {
				return item == null ? null : item.getDbId();
			}
		};
		final MyDataGrid<PathwaySummary> dataGrid = new MyDataGrid<PathwaySummary>(KEY_PROVIDER);
		PathWaysColumnManager pathwaysColumnManager = new PathWaysColumnManager(sessionID);

		final List<MyColumn<PathwaySummary>> columns = pathwaysColumnManager.getColumns();
		for (MyColumn<PathwaySummary> column : columns) {
			ColumnName columnName = column.getColumnName();
			final boolean visible = pathwaysColumnManager.isVisible(columnName);
			final Header<String> footer = pathwaysColumnManager.getFooter(columnName);

			dataGrid.addColumn(columnName, (Column<PathwaySummary, ?>) column,
					new MySafeHtmlHeaderWithTooltip(columnName, SafeHtmlUtils.fromSafeConstant(columnName.getAbr()),
							columnName.getDescription()),
					footer);

			if (visible) {
				dataGrid.setColumnWidth((Column<PathwaySummary, ?>) column, column.getDefaultWidth(),
						column.getDefaultWidthUnit());
			} else {
				dataGrid.setColumnWidth((Column<PathwaySummary, ?>) column, 0, column.getDefaultWidthUnit());
			}
		}
		return dataGrid;
	}

	private void loadFireworks(String species) {

		fireworks = FireworksFactory.createFireworksViewer(species);
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
		fireWorksContainer.clear();
		fireWorksContainer.add(fireworks);

		fireWorksLoaded = true;
		// diagram.setVisible(false);
	}

	private void onFireworksLoadError(String error) {
		StatusReportersRegister.getInstance().notifyStatusReporters(error);
	}

	private void requestFireworks(String speciesJSonName) {
		if (fireWorksLoaded) {
			// fireworks.setVisible(true);
			// fireworks.onResize();
			fireworks.showAll();

			return;
		}
		String url = "./reactome/download/current/fireworks/" + speciesJSonName;
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
		requestBuilder.setHeader("Accept", "application/json");
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					switch (response.getStatusCode()) {
					case Response.SC_OK:
						String json = response.getText();
						loadFireworks(json);
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
		// if (event.getNode() != null) {
		// diagram.setVisible(true);
		// diagram.onResize();
		// diagramLoader.load(event.getNode().getStId());
		//
		// } else {
		// diagram.setVisible(false);
		// }
	}

	protected void loadAnalysisOnTable(AnalysisResult result) {
		pathWayDataGrid.setVisible(true);
		asyncDataListProvider.updateRowCount(result.getPathwaysFound(), true);
		asyncDataListProvider.updateRowData(0, result.getPathways());
		pathWayDataGrid.setForceToRefresh(true);
		RangeChangeEvent.fire(pathWayDataGrid, pathWayDataGrid.getVisibleRange());
	}

	public void selectPathWay(String stId, Long dbId) {
		GWT.log("Selecting pathway: " + stId + "\t" + dbId);
		// select tab for graphics
		selectGraphicTab();
		// reset selections and highlights
		fireworks.resetHighlight();
		fireworks.resetSelection();
		// highlight and select node
		StatusReportersRegister.getInstance().notifyStatusReporters("Selecting stId" + stId);
		fireworks.highlightNode(stId);
		fireworks.selectNode(stId);
		fireworks.openPathway(stId);
		StatusReportersRegister.getInstance().notifyStatusReporters("Selecting dbId" + dbId);
		fireworks.highlightNode(dbId);
		fireworks.selectNode(dbId);
		fireworks.openPathway(dbId);

	}

	private void selectGraphicTab() {
		tabPanel.selectTab(1, true);

	}

	private void applyCarriedActions() {
		// ORDER IS IMPORTANT IN THE FOLLOWING CONDITIONS
		if (toOpen != null) {
			fireworks.openPathway(toOpen.getDbId());
			toOpen = null;
			toSelect = null;
			toHighlight = null;
		}
		if (toSelect != null) {
			fireworks.selectNode(toSelect.getDbId());
			toSelect = null;
		}
		if (toHighlight != null) {
			fireworks.highlightNode(toHighlight.getDbId());
			toHighlight = null;
		}

	}

	@Override
	public void onAnalysisReset() {
		GWT.log("onAnalysisReset");
	}

	@Override
	public void onFireworksLoaded(FireworksLoadedEvent event) {
		GWT.log("on fireworks loaded");
	}

	@Override
	public void onNodeHover(NodeHoverEvent event) {
		GWT.log("on node hover");
		// fireworks.highlightPathway(event.getNode().getDbId());
	}

	@Override
	public void onNodeOpened(NodeOpenedEvent event) {
		if (event.getNode() != null) {
			fireWorksContainer.setVisible(false);
			diagram.setVisible(true);
			diagram.onResize();
			diagramLoader.load(event.getNode().getStId());

		} else {
			diagram.setVisible(false);
			fireWorksContainer.setVisible(true);
		}
		// // presenter.showPathwayDiagram(event.getNode().getDbId());
		// // hide the fireworks container
		//
		GWT.log("onNodeOpened");
	}

	@Override
	public void onNodeSelectionReset() {
		// presenter.resetPathwaySelection();
		GWT.log("onNodeSelectionReset");
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
		}
	}

	/**
	 * Getting a panel with loading message and symbol.
	 *
	 * @return Widget
	 */
	private Widget getLoadingMessage() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(new Image(MyClientBundle.INSTANCE.horizontalLoader()));
		hp.add(new HTMLPanel("Loading pathways overview graph..."));
		hp.setSpacing(5);

		return hp;
	}
}
