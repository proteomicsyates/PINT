package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.ProjectNamedQuery;

public class MyWelcomeProjectPanel extends FlowPanel {
	private final HTMLPanel projectDescriptionHtml;
	private final HTMLPanel projectInstructionsHtml;
	private final HTMLPanel projectViewCommentsHtml;
	private final HTMLPanel availableQueriesHtmlPanel;
	private static final String defaultProjectViewString = "By default all proteins and PSMs will be loaded in the 'Data view'.\n"
			+ "Any query sent to the server will overwrite the data shown in the 'Data view'.";

	private static final String defaultProjectDescriptionString = "This interactive page of PInt tool will allow you to actually look at the data of this project.\n";
	private static final String defaultProjectInstructionsString = "How to navegate in this page:\n"
			+ "There are two main tabs:\n"
			+ "The 'Query' tab allows to write queries to filter the data. Look at 'Filters / Query commands' left menu to see available commands.\n"
			+ "The 'Dataset view' tab shows the actual data, divided into three subtabs: 'Individual proteins view' (showing the list of proteins in the dataset), "
			+ "'Protein groups view' (showing the groups comming from PAnalyzer protein inference algorithm)"
			+ " and 'Peptides view' (containing all Peptides in the dataset).\n"
			+ "At the left of the page, the tool shows a set of menus with information about the project(s) and "
			+ "experimental conditions that have been loaded, as well as interactive controls to change the view of the data tables (upper half of left menus).\n"
			+ "It also provides some menus for the possible values the user should use in the different query commands (botton half of left menus).";

	private final CaptionPanel cptnpnlBigProject;
	private final WindowBox parentWindow;

	/**
	 *
	 * @param parentWindow
	 * @param defaultView
	 * @param queryPanel
	 *            provide it for call to loadProteins when clicking on the
	 *            query.
	 */
	public MyWelcomeProjectPanel(WindowBox parentWindow, ProjectBean projectBean, DefaultView defaultView,
			QueryPanel queryPanel, boolean testMode) {
		this.parentWindow = parentWindow;
		setStyleName("WelcomeProjectPanel");

		cptnpnlBigProject = new CaptionPanel("Important notice");
		cptnpnlBigProject.setVisible(projectBean.isBig());
		cptnpnlBigProject.setCaptionHTML("Important notice");
		cptnpnlBigProject.setStyleName("WelcomeProjectPanelBigProjectPanel");
		add(cptnpnlBigProject);
		final String noticeText = "Regular queries have been disabled in this dataset."
				+ "<br>However you can still look for invidual proteins/genes in the 'query' panel."
				+ "<br>In order to do that, go to the 'Query' tab and type something in the 'Simple query editor' text box.";
		final SafeHtml bigProjectNoticeSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(noticeText).toSafeHtml();
		final HTMLPanel importantNoticeHtml = new HTMLPanel(bigProjectNoticeSafeHtml);
		importantNoticeHtml.setStyleName("WelcomeProjectPanelBigProjectPanelHtml");
		cptnpnlBigProject.setContentWidget(importantNoticeHtml);
		importantNoticeHtml.setSize("100%", "100%");

		String projectDescription = defaultView.getProjectDescription();
		if (projectDescription == null || "".equals(projectDescription)) {
			projectDescription = defaultProjectDescriptionString;
		}
		// replace all \n by <br>
		projectDescription = projectDescription.replace("\n", "<br>");

		final CaptionPanel cptnpnlProjectDescription = new CaptionPanel("Project description");
		cptnpnlProjectDescription.setCaptionHTML("Project short description");
		cptnpnlProjectDescription.setStyleName("WelcomeProjectPanelInternalPanel");
		add(cptnpnlProjectDescription);

		final SafeHtml projectDescriptionSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectDescription)
				.toSafeHtml();

		projectDescriptionHtml = new HTMLPanel(projectDescriptionSafeHtml);
		projectDescriptionHtml.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		cptnpnlProjectDescription.setContentWidget(projectDescriptionHtml);
		projectDescriptionHtml.setSize("100%", "100%");

		String projectInstructions = defaultView.getProjectInstructions();
		if (projectInstructions == null || "".equals(projectInstructions)) {
			projectInstructions = defaultProjectInstructionsString;
		}
		// replace all \n by <br>
		projectInstructions = projectInstructions.replace("\n", "<br>");
		final CaptionPanel captionPanel = new CaptionPanel("Project description");
		captionPanel.setCaptionHTML("Data description");
		captionPanel.setStyleName("WelcomeProjectPanelInternalPanel");
		add(captionPanel);

		final SafeHtml projectInstructionsSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectInstructions)
				.toSafeHtml();

		projectInstructionsHtml = new HTMLPanel(projectInstructionsSafeHtml);
		projectInstructionsHtml.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		captionPanel.setContentWidget(projectInstructionsHtml);
		projectInstructionsHtml.setSize("100%", "100%");

		String projectViewComments = defaultView.getProjectViewComments();
		if (projectViewComments == null || "".equals(projectViewComments)) {
			projectViewComments = defaultProjectViewString;
		}
		// replace all \n by <br>
		projectViewComments = projectViewComments.replace("\n", "<br>");

		final CaptionPanel cptnpnlDataViewComments = new CaptionPanel("Data view comments");
		cptnpnlDataViewComments.setStyleName("WelcomeProjectPanelInternalPanel");
		add(cptnpnlDataViewComments);

		final SafeHtml projectViewCommentsSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectViewComments)
				.toSafeHtml();

		projectViewCommentsHtml = new HTMLPanel(projectViewCommentsSafeHtml);
		projectViewCommentsHtml.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		cptnpnlDataViewComments.setContentWidget(projectViewCommentsHtml);
		projectViewCommentsHtml.setSize("100%", "100%");

		final CaptionPanel availableQueriesCaptionPanel = new CaptionPanel("Available data for this project");
		availableQueriesCaptionPanel.setStyleName("WelcomeProjectPanelInternalPanel");
		add(availableQueriesCaptionPanel);

		final StringBuilder availableQueriesString = new StringBuilder();
		final List<ProjectNamedQuery> projectNamedQueries = defaultView.getProjectNamedQueries();
		final List<Panel> clickableLabels = new ArrayList<Panel>();
		if (!projectBean.isBig()) {
			availableQueriesString.append(
					"The data associated with this project is being loaded. Once it is loaded, you can explore or you can query the data.\n");
			if (!projectNamedQueries.isEmpty()) {

				availableQueriesString
						.append("Click in the query below to load a subset of the data associated with the project.\n");

				for (int index = 0; index < projectNamedQueries.size(); index++) {
					final ProjectNamedQuery projectNamedQuery = projectNamedQueries.get(index);
					clickableLabels.add(getLinkToDataView(this.parentWindow, projectNamedQuery, queryPanel, testMode,
							"defaultQueryLink"));
				}
			}
		} else {
			availableQueriesString.append("Default views are disabled for this project.\n");
		}
		final SafeHtml availableQueriesContent = new SafeHtmlBuilder()
				.appendEscapedLines(availableQueriesString.toString()).toSafeHtml();

		availableQueriesHtmlPanel = new HTMLPanel(availableQueriesContent);
		availableQueriesHtmlPanel.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		availableQueriesCaptionPanel.setContentWidget(availableQueriesHtmlPanel);
		availableQueriesHtmlPanel.setSize("100%", "100%");

		for (final Panel panel : clickableLabels) {
			availableQueriesHtmlPanel.add(panel);
		}

	}

	/**
	 *
	 * @param projectNamedQuery
	 * @param defaultOne
	 *            print an '*' before the query name
	 * @return
	 */
	public static Panel getLinkToDataView(WindowBox parentWindowBox, final ProjectNamedQuery projectNamedQuery,
			QueryPanel queryPanel, boolean testMode, String styleName) {
		final Panel ret = new HorizontalPanel();
		if (parentWindowBox != null) {
			ret.getElement().setAttribute("cellpadding", "5");
		}
		final String queryName = projectNamedQuery.getName();

		final Label nameLabel = new Label(queryName + ": " + projectNamedQuery.getQuery());
		nameLabel.setWidth("100%");
		if (styleName != null) {
			nameLabel.setStyleName(styleName);
		}
		nameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		nameLabel.setTitle("Click here to load this dataset view:\n\t" + projectNamedQuery.getName() + ": '"
				+ projectNamedQuery.getQuery() + "'");
		nameLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// close this panel first
				if (parentWindowBox != null) {
					parentWindowBox.hide();
				}
				queryPanel.loadProteinsFromProject(null, projectNamedQuery.getIndex(), projectNamedQuery.getName(),
						testMode);
			}
		});
		ret.add(nameLabel);
		return ret;
	}

	/**
	 *
	 * @param projectNamedQuery
	 * @param defaultOne
	 *            print an '*' before the query name
	 * @return
	 */
	public static Panel getLinkToLoadProject(WindowBox parentWindowBox, QueryPanel queryPanel, boolean testMode,
			String styleName, String projectTag) {
		final Panel ret = new HorizontalPanel();
		if (parentWindowBox != null) {
			ret.getElement().setAttribute("cellpadding", "0");
		}
		final Label nameLabel = new Label("Load the whole project: COND[, " + projectTag + "]");
		nameLabel.setWidth("100%");
		if (styleName != null) {
			nameLabel.setStyleName(styleName);
		}
		nameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		nameLabel.setTitle("Load the whole project: COND[, " + projectTag + "]");
		nameLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// close this panel first
				if (parentWindowBox != null) {
					parentWindowBox.hide();
				}
				queryPanel.loadProteinsFromProject(null, null, null, testMode);
			}
		});
		ret.add(nameLabel);
		return ret;
	}
}
