package edu.scripps.yates.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.HtmlList;
import edu.scripps.yates.client.gui.components.HtmlList.ListType;
import edu.scripps.yates.client.history.TargetHistory;

public class MainPanel_OLD extends Composite {

	protected final edu.scripps.yates.client.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();
	private final InlineHTML nlnhtmlExperiments;
	private final InlineHTML nlnhtmlProteins;
	private final InlineHTML nlnhtmlPeptides;
	private final InlineHTML inlineHTMLConditions;
	private final FocusPanel focusSubmitPanel;
	private final FocusPanel focusAccessDataPanel;
	private final FocusPanel focusToolsPanel;
	private final FocusPanel focusNewsPanel;
	private final HtmlList listItemPanel;

	public MainPanel_OLD() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);

		HeaderPanel headerVerticalPanel = new HeaderPanel();
		mainPanel.add(headerVerticalPanel);

		NavigationHorizontalPanel navigationHorizontalPanel = new NavigationHorizontalPanel(TargetHistory.HOME);
		mainPanel.add(navigationHorizontalPanel);
		navigationHorizontalPanel.setWidth("");

		FlowPanel contentVerticalPanel = new FlowPanel();
		contentVerticalPanel.setStyleName("verticalComponent");
		mainPanel.add(contentVerticalPanel);
		contentVerticalPanel.setSize("100%", "100%");

		FlowPanel horizontalPanel = new FlowPanel();
		horizontalPanel.setStyleName("verticalComponent");
		contentVerticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("100%", "480px");

		VerticalPanel pintPresentationVerticalPanel = new VerticalPanel();
		pintPresentationVerticalPanel.setStyleName("mainPagePintExplanation");
		pintPresentationVerticalPanel.setBorderWidth(0);
		horizontalPanel.add(pintPresentationVerticalPanel);

		InlineHTML nlnhtmlNewInlinehtml_1 = new InlineHTML("PINT:");
		nlnhtmlNewInlinehtml_1.setStyleName("title1");
		pintPresentationVerticalPanel.add(nlnhtmlNewInlinehtml_1);

		InlineHTML nlnhtmlNewInlinehtml_2 = new InlineHTML(
				"<br>\r\n<b>PINT</b>, the <b>Proteomics INTegrator</b>, is an online experiment repository for final results comming from different qualitative and/or quantitative proteomics assays.\r\n<br><br>\r\nPINT is a new comprehensive system to store, visualize, and analyze data for proteomics results obtained under different experimental conditions. PINT provides an extremely flexible and powerful query interface that allows data filtering based on numerous proteomics features such as confidence values, abundance levels or ratios, dataset overlaps, etc. Furthermore, proteomics results can be combined with queries over the vast majority of the UniprotKB annotations, which are transparently incorporated into the system. For example, these queries can allow rapid identification of proteins with a confidence score above a given threshold that are known to be associated to diseases or they may highlight proteins with a least one phosphorylated site that are shared between a set of experimental conditions. In addition, PINT allows the developers to incorporate data visualization and analysis tools, serving its role as a centralized hub of proteomics data analysis tools. PINT will thus facilitate interpretation of proteomics results and expedite biological conclusions and, by the same means, deal with the \u2018big data\u2019 paradigm in proteomics.");
		nlnhtmlNewInlinehtml_2.setStyleName("mainPagePintTitleExplanation");
		pintPresentationVerticalPanel.add(nlnhtmlNewInlinehtml_2);

		VerticalPanel dataStatsVerticalPanel = new VerticalPanel();
		dataStatsVerticalPanel.setStyleName("mainPageDataStatistics");
		dataStatsVerticalPanel.setBorderWidth(0);
		horizontalPanel.add(dataStatsVerticalPanel);

		InlineHTML nlnhtmlNewInlinehtml = new InlineHTML("Data statistics:");
		nlnhtmlNewInlinehtml.setStyleName("title2");
		nlnhtmlNewInlinehtml.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		dataStatsVerticalPanel.add(nlnhtmlNewInlinehtml);

		listItemPanel = new HtmlList(ListType.UNORDERED);
		dataStatsVerticalPanel.add(listItemPanel);

		nlnhtmlExperiments = new InlineHTML("Loading number of projects...");
		nlnhtmlExperiments.setStyleName("numbers");
		nlnhtmlExperiments.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		listItemPanel.addItem("Loading number of projects...", null);

		inlineHTMLConditions = new InlineHTML("Loading experimental conditions...");
		inlineHTMLConditions.setStyleName("numbers");
		inlineHTMLConditions.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		listItemPanel.addItem("Loading experimental conditions...", null);

		nlnhtmlProteins = new InlineHTML("Loading number of proteins...");
		nlnhtmlProteins.setStyleName("numbers");
		nlnhtmlProteins.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		listItemPanel.addItem("Loading number of proteins...", null);

		nlnhtmlPeptides = new InlineHTML("Loading number of peptides...");
		nlnhtmlPeptides.setStyleName("numbers");
		nlnhtmlPeptides.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		listItemPanel.addItem("Loading number of peptides...", null);

		FlowPanel sectionsHorizontalPanel = new FlowPanel();
		sectionsHorizontalPanel.setStyleName("verticalComponent");
		contentVerticalPanel.add(sectionsHorizontalPanel);
		sectionsHorizontalPanel.setSize("100%", "100%");

		VerticalPanel submitVerticalPanel = new VerticalPanel();
		focusSubmitPanel = new FocusPanel(submitVerticalPanel);
		submitVerticalPanel.setBorderWidth(0);
		submitVerticalPanel.setStyleName("mainPageBoxes");
		submitVerticalPanel.setSpacing(10);
		sectionsHorizontalPanel.add(focusSubmitPanel);

		InlineHTML nlnhtmlNewInlinehtml_3 = new InlineHTML("Submit:");
		nlnhtmlNewInlinehtml_3.setStyleName("title2");
		submitVerticalPanel.add(nlnhtmlNewInlinehtml_3);

		InlineHTML nlnhtmlSubmitExplanation = new InlineHTML(
				"Click here to upload new data into the PInt database. The tool will guide you in order to capture your data in an appropiate way.");
		nlnhtmlSubmitExplanation.setStyleName("mainPageBoxExplanation");
		submitVerticalPanel.add(nlnhtmlSubmitExplanation);

		VerticalPanel accessDataVerticalPanel = new VerticalPanel();
		focusAccessDataPanel = new FocusPanel(accessDataVerticalPanel);
		sectionsHorizontalPanel.add(focusAccessDataPanel);
		accessDataVerticalPanel.setSpacing(10);
		accessDataVerticalPanel.setBorderWidth(0);
		accessDataVerticalPanel.setStyleName("mainPageBoxes");

		InlineHTML nlnhtmlAccessData = new InlineHTML("Access data:");
		nlnhtmlAccessData.setStyleName("title2");
		accessDataVerticalPanel.add(nlnhtmlAccessData);

		InlineHTML nlnhtmlAccessDataExplanation = new InlineHTML(
				"Click here to access to the data. The list of available data projects will be listed, and the user will be able to select the projects in which is interested.");
		nlnhtmlAccessDataExplanation.setStyleName("mainPageBoxExplanation");
		accessDataVerticalPanel.add(nlnhtmlAccessDataExplanation);

		VerticalPanel toolsVerticalPanel = new VerticalPanel();
		focusToolsPanel = new FocusPanel(toolsVerticalPanel);
		toolsVerticalPanel.setBorderWidth(0);
		toolsVerticalPanel.setStyleName("mainPageBoxes");
		toolsVerticalPanel.setSpacing(10);
		sectionsHorizontalPanel.add(focusToolsPanel);

		InlineHTML nlnhtmlTools = new InlineHTML("Tools:");
		nlnhtmlTools.setStyleName("title2");
		toolsVerticalPanel.add(nlnhtmlTools);

		InlineHTML nlnhtmlToolsExplanation = new InlineHTML(
				"Description of external tools or features linked to PINT.");
		nlnhtmlToolsExplanation.setStyleName("mainPageBoxExplanation");
		toolsVerticalPanel.add(nlnhtmlToolsExplanation);

		VerticalPanel NewsVerticalPanel = new VerticalPanel();
		focusNewsPanel = new FocusPanel(NewsVerticalPanel);
		NewsVerticalPanel.setBorderWidth(0);
		NewsVerticalPanel.setStyleName("mainPageBoxes");
		sectionsHorizontalPanel.add(focusNewsPanel);
		NewsVerticalPanel.setSpacing(10);

		InlineHTML nlnhtmlNews = new InlineHTML("News:");
		nlnhtmlNews.setStyleName("title2");
		NewsVerticalPanel.add(nlnhtmlNews);

		InlineHTML nlnhtmlNewsExplanation = new InlineHTML("See latest changes and new features here.");
		nlnhtmlNewsExplanation.setStyleName("numbers");
		NewsVerticalPanel.add(nlnhtmlNewsExplanation);

		setStyleName("MainPanel");

		loadStatistics();

		loadMouseOverHandlers();
	}

	private void loadMouseOverHandlers() {
		focusSubmitPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem(TargetHistory.SUBMIT.getTargetHistory());
			}
		});
		focusAccessDataPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem(TargetHistory.BROWSE.getTargetHistory());
			}
		});
		focusNewsPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem(TargetHistory.NEWS.getTargetHistory());
			}
		});
		focusToolsPanel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				History.newItem(TargetHistory.TOOLS.getTargetHistory());
			}
		});
	}

	public void loadStatistics() {
		proteinRetrievingService.getNumExperiments(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(result + " projects.", 0);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});
		proteinRetrievingService.getNumDifferentPeptides(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(result + " peptide sequences.", 3);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});
		proteinRetrievingService.getNumDifferentProteins(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(result + " proteins.", 2);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});
		proteinRetrievingService.getNumConditions(new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				listItemPanel.setText(result + " experimental conditions.", 1);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});

	}
}
