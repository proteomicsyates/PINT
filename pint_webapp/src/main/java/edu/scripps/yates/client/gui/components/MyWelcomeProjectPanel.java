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
			+ "The 'Query' tab allows to write queries to filter the data. Look at 'Query commands' left menu to see available commands.\n"
			+ "The 'Data view' tab shows the actual data, divided into three subtabs: 'Individual proteins view' (showing the list of proteins in the dataset), "
			+ "'Protein groups view' (showing the groups comming from PAnalyzer protein inference algorithm)"
			+ " and 'PSMs view' (containing all PSMs in the dataset).\n"
			+ "At the left of the page, the tool shows a set of menus with information about the project(s) and "
			+ "experimental conditions that have been loaded, as well as interactive controls to change the view of the data tables (upper half of left menus).\n"
			+ "It also provides some menus for the possible values the user should use in the different query commands (botton half of left menus).";

	private final QueryPanel queryPanel;
	private final CaptionPanel cptnpnlBigProject;
	private final ProjectBean projectBean;

	/**
	 *
	 * @param defaultView
	 * @param queryPanel
	 *            provide it for call to loadProteins when clicking on the
	 *            query.
	 */
	public MyWelcomeProjectPanel(ProjectBean projectBean, DefaultView defaultView, QueryPanel queryPanel) {
		this.queryPanel = queryPanel;
		this.projectBean = projectBean;
		setStyleName("WelcomeProjectPanel");

		cptnpnlBigProject = new CaptionPanel("Important notice");
		cptnpnlBigProject.setVisible(projectBean.isBig());
		cptnpnlBigProject.setCaptionHTML("Important notice");
		cptnpnlBigProject.setStyleName("WelcomeProjectPanelBigProjectPanel");
		add(cptnpnlBigProject);
		String noticeText = "Regular queries have been disabled in this dataset."
				+ "<br>However you can still look for invidual proteins/genes in the 'query' panel."
				+ "<br>In order to do that, go to the 'Query' tab and type something in the 'Simple query editor' text box.";
		SafeHtml bigProjectNoticeSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(noticeText).toSafeHtml();
		HTMLPanel importantNoticeHtml = new HTMLPanel(bigProjectNoticeSafeHtml);
		importantNoticeHtml.setStyleName("WelcomeProjectPanelBigProjectPanelHtml");
		cptnpnlBigProject.setContentWidget(importantNoticeHtml);
		importantNoticeHtml.setSize("100%", "100%");

		String projectDescription = defaultView.getProjectDescription();
		if (projectDescription == null || "".equals(projectDescription)) {
			projectDescription = defaultProjectDescriptionString;
		}
		// replace all \n by <br>
		projectDescription = projectDescription.replace("\n", "<br>");

		CaptionPanel cptnpnlProjectDescription = new CaptionPanel("Project description");
		cptnpnlProjectDescription.setCaptionHTML("Project short description");
		cptnpnlProjectDescription.setStyleName("WelcomeProjectPanelInternalPanel");
		add(cptnpnlProjectDescription);

		SafeHtml projectDescriptionSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectDescription).toSafeHtml();

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
		CaptionPanel captionPanel = new CaptionPanel("Project description");
		captionPanel.setCaptionHTML("Data description");
		captionPanel.setStyleName("WelcomeProjectPanelInternalPanel");
		add(captionPanel);

		SafeHtml projectInstructionsSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectInstructions)
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

		CaptionPanel cptnpnlDataViewComments = new CaptionPanel("Data view comments");
		cptnpnlDataViewComments.setStyleName("WelcomeProjectPanelInternalPanel");
		add(cptnpnlDataViewComments);

		SafeHtml projectViewCommentsSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectViewComments)
				.toSafeHtml();

		projectViewCommentsHtml = new HTMLPanel(projectViewCommentsSafeHtml);
		projectViewCommentsHtml.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		cptnpnlDataViewComments.setContentWidget(projectViewCommentsHtml);
		projectViewCommentsHtml.setSize("100%", "100%");

		CaptionPanel availableQueriesCaptionPanel = new CaptionPanel("Available Views of the project");
		availableQueriesCaptionPanel.setStyleName("WelcomeProjectPanelInternalPanel");
		add(availableQueriesCaptionPanel);

		StringBuilder availableQueriesString = new StringBuilder();
		final List<ProjectNamedQuery> projectNamedQueries = defaultView.getProjectNamedQueries();
		List<Panel> clickableLabels = new ArrayList<Panel>();
		if (!projectBean.isBig()) {
			if (projectNamedQueries.isEmpty()) {
				availableQueriesString
						.append("The project will be fully loaded by default. No data views were defined.");
			} else {
				if (projectNamedQueries.size() == 1) {
					availableQueriesString.append("Default view defined for this project:\n");

				} else {
					availableQueriesString.append(
							"Several views were defined for this project. The one marked with (*) will be automatically loaded. Click on any other to load that view:\n");
				}
				for (int index = 0; index < projectNamedQueries.size(); index++) {
					ProjectNamedQuery projectNamedQuery = projectNamedQueries.get(index);
					boolean defaultOne = false;
					if (index == 0 && projectNamedQueries.size() > 1) {
						defaultOne = true;
					}
					clickableLabels.add(getLinkToDataView(projectNamedQuery, defaultOne));
				}
			}
		} else {
			availableQueriesString.append("Default views are disabled for this project.\n");
		}
		SafeHtml availableQueriesContent = new SafeHtmlBuilder().appendEscapedLines(availableQueriesString.toString())
				.toSafeHtml();

		availableQueriesHtmlPanel = new HTMLPanel(availableQueriesContent);
		availableQueriesHtmlPanel.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		availableQueriesCaptionPanel.setContentWidget(availableQueriesHtmlPanel);
		availableQueriesHtmlPanel.setSize("100%", "100%");

		for (Panel panel : clickableLabels) {
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
	private Panel getLinkToDataView(final ProjectNamedQuery projectNamedQuery, boolean defaultOne) {
		Panel ret = new HorizontalPanel();
		ret.getElement().setAttribute("cellpadding", "5");
		String queryName = projectNamedQuery.getName();
		if (defaultOne) {
			queryName = "(*) " + queryName;
		}
		final Label nameLabel = new Label(queryName);
		nameLabel.setWidth("100%");
		nameLabel.setStyleName("defaultQueryLink");
		nameLabel.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		nameLabel.setTitle("Click here to load this data view:\n\t" + projectNamedQuery.getName() + ": '"
				+ projectNamedQuery.getQuery() + "'");
		nameLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				queryPanel.loadProteinsFromProject(null, projectNamedQuery.getIndex());
			}
		});
		ret.add(nameLabel);
		return ret;
	}
}
