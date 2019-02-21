package edu.scripps.yates.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.Pint;
import edu.scripps.yates.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.HtmlList;
import edu.scripps.yates.client.gui.components.HtmlList.ListType;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.client.pint.wizard.NewProjectCreatorWizard;

public class MainPanel extends InitializableComposite {

	protected final edu.scripps.yates.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();
	private final FocusPanel focusSubmitPanel;
	private final FocusPanel focusAccessDataPanel;

	private final HtmlList listItemPanel;
	private final FocusPanel focusConfigurationPanel;
	private final Pint pintEntryPoint;
	private final boolean TESTING_WIZARD = true;

	public MainPanel(Pint pintEntryPoint) {
		this.pintEntryPoint = pintEntryPoint;
		final FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);

		final HeaderPanel headerVerticalPanel = new HeaderPanel();
		mainPanel.add(headerVerticalPanel);

		final NavigationHorizontalPanel navigationHorizontalPanel = new NavigationHorizontalPanel(TargetHistory.HOME);
		mainPanel.add(navigationHorizontalPanel);

		final FlowPanel contentVerticalPanel = new FlowPanel();
		contentVerticalPanel.setStyleName("verticalComponent");
		mainPanel.add(contentVerticalPanel);
		contentVerticalPanel.setSize("100%", "100%");

		final FlowPanel horizontalPanel = new FlowPanel();
		horizontalPanel.setStyleName("verticalComponent");
		contentVerticalPanel.add(horizontalPanel);
		horizontalPanel.setWidth("100%");

		final VerticalPanel pintPresentationVerticalPanel = new VerticalPanel();
		pintPresentationVerticalPanel.setStyleName("mainPagePintExplanation");
		pintPresentationVerticalPanel.setBorderWidth(0);
		horizontalPanel.add(pintPresentationVerticalPanel);

		final InlineHTML nlnhtmlNewInlinehtml_1 = new InlineHTML("PINT:");
		nlnhtmlNewInlinehtml_1.setStyleName("title1");
		pintPresentationVerticalPanel.add(nlnhtmlNewInlinehtml_1);

		final InlineHTML nlnhtmlNewInlinehtml_2 = new InlineHTML(
				"<br>\r\n<b>PINT</b>, the <b>Proteomics INTegrator</b>, is an online experiment repository for final results coming from different qualitative and/or quantitative proteomics assays.\r\n<br>"
						+ "<br>\r\nPINT is a new comprehensive system to store, visualize, and analyze data for proteomics results obtained under different experimental conditions.<br>"
						+ "<ul><li>PINT provides an extremely <b>flexible and powerful query interface</b> that allows data filtering based on numerous proteomics features such as confidence values, abundance levels or ratios, dataset overlaps, etc...</li>"
						+ "<li>Furthermore, proteomics results can be <b>combined with queries over the vast majority of the UniprotKB annotations</b>, which are transparently incorporated into the system. For example, these queries can allow rapid identification of proteins with a confidence score above a given threshold that are known to be associated to diseases or they may highlight proteins with a least one phosphorylated site that are shared between a set of experimental conditions.</li>"
						+ "<li>In addition, PINT allows the developers to incorporate data visualization and analysis tools, serving its role as a <b>centralized hub of proteomics data analysis tools</b>. One example is the recent integration of enrichment analysis with <a href=\"http://pseaquant.scripps.edu/\" class=\"geneLink\" target=\"_blank\" title=\"Go to PSEA-Quant webpage\">PSEA-Quant</a> online tool.</li></ul>"
						+ "PINT will thus facilitate interpretation of proteomics results and expedite biological conclusions and, by the same means, deal with the \u2018big data\u2019 paradigm in proteomics.");
		nlnhtmlNewInlinehtml_2.setStyleName("mainPagePintTitleExplanation");
		pintPresentationVerticalPanel.add(nlnhtmlNewInlinehtml_2);

		final FlowPanel sectionsHorizontalPanel = new FlowPanel();
		sectionsHorizontalPanel.setStyleName("verticalComponent");
		contentVerticalPanel.add(sectionsHorizontalPanel);
		sectionsHorizontalPanel.setSize("100%", "100%");

		final FlexTable submitVerticalPanel = new FlexTable();

		final FlexTable dataStatsVerticalPanel = new FlexTable();
		sectionsHorizontalPanel.add(dataStatsVerticalPanel);
		dataStatsVerticalPanel.setWidth("");
		dataStatsVerticalPanel.setStyleName("mainPageDataStatistics");
		dataStatsVerticalPanel.setBorderWidth(0);
		dataStatsVerticalPanel.setCellSpacing(10);

		final InlineHTML nlnhtmlNewInlinehtml = new InlineHTML("PINT database statistics:");
		nlnhtmlNewInlinehtml.setStyleName("title2");
		dataStatsVerticalPanel.setWidget(0, 0, nlnhtmlNewInlinehtml);
		dataStatsVerticalPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		dataStatsVerticalPanel.getFlexCellFormatter().setHeight(0, 0, "4em");
		final InlineHTML text = new InlineHTML("Currently, this instance of PINT contains:");
		text.setStyleName("mainPageBoxExplanation");
		dataStatsVerticalPanel.setWidget(1, 0, text);
		listItemPanel = new HtmlList(ListType.UNORDERED);
		listItemPanel.setStyleName("mainPageDataStatistics-numbers");
		dataStatsVerticalPanel.setWidget(2, 0, listItemPanel);
		listItemPanel.addItem("Loading number of projects...", null);
		listItemPanel.addItem("Loading number of exp. conditions...", null);
		listItemPanel.addItem("Loading number of MS runs...", null);
		listItemPanel.addItem("Loading number of proteins...", null);
		listItemPanel.addItem("Loading number of genes...", null);
		listItemPanel.addItem("Loading number of peptides...", null);
		listItemPanel.addItem("Loading number of PSMs...", null);
		listItemPanel.unsinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
		listItemPanel.getElement().getStyle().setMargin(0, Unit.PX);
		focusSubmitPanel = new FocusPanel(submitVerticalPanel);
		sectionsHorizontalPanel.add(focusSubmitPanel);
		submitVerticalPanel.setBorderWidth(0);
		submitVerticalPanel.setStyleName("mainPageBoxes");
		submitVerticalPanel.setCellSpacing(10);

		final InlineHTML nlnhtmlNewInlinehtml_3 = new InlineHTML("Submit:");
		nlnhtmlNewInlinehtml_3.setStyleName("title2");
		submitVerticalPanel.setWidget(0, 0, nlnhtmlNewInlinehtml_3);
		submitVerticalPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		submitVerticalPanel.getFlexCellFormatter().setHeight(0, 0, "4em");

		final InlineHTML nlnhtmlSubmitExplanation = new InlineHTML(
				"Click here to create a new dataset. The tool will guide you in order to capture your data in an appropiate way.");
		nlnhtmlSubmitExplanation.setStyleName("mainPageBoxExplanation");
		submitVerticalPanel.setWidget(1, 0, nlnhtmlSubmitExplanation);
		submitVerticalPanel.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		//
		final FlexTable accessDataVerticalPanel = new FlexTable();
		focusAccessDataPanel = new FocusPanel(accessDataVerticalPanel);
		sectionsHorizontalPanel.add(focusAccessDataPanel);
		accessDataVerticalPanel.setCellSpacing(10);
		accessDataVerticalPanel.setBorderWidth(0);
		accessDataVerticalPanel.setStyleName("mainPageBoxes");

		final InlineHTML nlnhtmlAccessData = new InlineHTML("Browse data:");
		nlnhtmlAccessData.setStyleName("title2");
		accessDataVerticalPanel.setWidget(0, 0, nlnhtmlAccessData);
		accessDataVerticalPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		accessDataVerticalPanel.getFlexCellFormatter().setHeight(0, 0, "4em");
		//
		final InlineHTML nlnhtmlAccessDataExplanation = new InlineHTML(
				"Click here to see the list of the stored projects in PINT. You will be able to select the ones in which you are interested.");
		nlnhtmlAccessDataExplanation.setStyleName("mainPageBoxExplanation");
		accessDataVerticalPanel.setWidget(1, 0, nlnhtmlAccessDataExplanation);
		accessDataVerticalPanel.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		////

		final FlexTable configurationVerticalPanel = new FlexTable();
		focusConfigurationPanel = new FocusPanel(configurationVerticalPanel);
		sectionsHorizontalPanel.add(focusConfigurationPanel);
		configurationVerticalPanel.setCellSpacing(10);
		configurationVerticalPanel.setBorderWidth(0);
		configurationVerticalPanel.setStyleName("mainPageBoxes");

		final InlineHTML nlnhtmlConfiguration = new InlineHTML("Configuration:");
		nlnhtmlConfiguration.setStyleName("title2");
		configurationVerticalPanel.setWidget(0, 0, nlnhtmlConfiguration);
		configurationVerticalPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		configurationVerticalPanel.getFlexCellFormatter().setHeight(0, 0, "4em");

		final InlineHTML nlnhtmlConfigurationExplanation = new InlineHTML(
				"Click here to go to the basic configuration of PINT (master password-protected).\nYou can edit the master password, the database connection parameters, the location of the internal files in the server and the projects you want to pre-load in cache for a faster accession.");
		nlnhtmlConfigurationExplanation.setStyleName("mainPageBoxExplanation");
		configurationVerticalPanel.setWidget(1, 0, nlnhtmlConfigurationExplanation);
		configurationVerticalPanel.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		////

		setStyleName("MainPanel");

		loadStatistics();

		loadMouseOverHandlers();
	}

	private void loadMouseOverHandlers() {
		focusSubmitPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (TESTING_WIZARD) {
					GWT.log("STARTING WIZARD!!!");
					final NewProjectCreatorWizard wizard = new NewProjectCreatorWizard(pintEntryPoint.getSessionID());
					RootLayoutPanel.get().add(wizard);
				} else {
					History.newItem(TargetHistory.SUBMIT.getTargetHistory());
				}
			}
		});
		focusAccessDataPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem(TargetHistory.BROWSE.getTargetHistory());
			}
		});
		focusConfigurationPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkLoginBeforeStartconfiguration();
			}
		});
	}

	protected void checkLoginBeforeStartconfiguration() {
		// check first the login
		final PopUpPanelPasswordChecker loginPanel = new PopUpPanelPasswordChecker(true, true, "PINT security",
				"Enter PINT master password to access to PINT basic configuration:");
		loginPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				final PopupPanel popup = event.getTarget();
				final Widget widget = popup.getWidget();
				if (widget instanceof PopUpPanelPasswordChecker) {
					final PopUpPanelPasswordChecker loginPanel = (PopUpPanelPasswordChecker) widget;
					if (loginPanel.isLoginOK()) {
						pintEntryPoint.startupConfiguration(true);
					}
				}
			}
		});
		loginPanel.show();

	}

	public void loadStatistics() {
//		GWT.runAsync(new RunAsyncCallback() {
//
//			@Override
//			public void onFailure(Throwable reason) {
//				StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//			}
//
//			@Override
//			public void onSuccess() {
		proteinRetrievingService.getNumExperiments(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " projects.", 0);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 0);
			}
		});
		proteinRetrievingService.getNumConditions(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " experimental conditions.", 1);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 1);
			}
		});
		proteinRetrievingService.getNumMSRuns(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " MS runs.", 2);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 2);
			}
		});
		proteinRetrievingService.getNumDifferentProteins(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " proteins.", 3);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 3);
			}
		});
		proteinRetrievingService.getNumGenes(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " genes.", 4);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 4);
			}
		});
		proteinRetrievingService.getNumDifferentPeptides(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " peptide sequences.", 5);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 5);
			}
		});
		proteinRetrievingService.getNumPSMs(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(NumberFormat.getDecimalFormat().format(result) + " PSMs.", 6);
			}

			@Override
			public void onFailure(Throwable caught) {
				listItemPanel.setTextAndTitle("- (?)", caught.getMessage(), 6);
			}
		});
//			}
//		});

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

}
