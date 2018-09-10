package edu.scripps.yates.client.gui.components;

import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class MyWarningMultipleProjectsLoadedPanel extends FlowPanel {

	public MyWarningMultipleProjectsLoadedPanel(List<String> loadedProjects) {

		setStyleName("WelcomeProjectPanel");

		final CaptionPanel cptnpnlBigProject = new CaptionPanel("");
		cptnpnlBigProject.setVisible(true);
		cptnpnlBigProject.setCaptionHTML("");
		cptnpnlBigProject.setStyleName("WelcomeProjectPanelBigProjectPanel");
		add(cptnpnlBigProject);
		final String noticeText = "Because you are loading multiple projects, the datasets associated with them will not be loaded.<br>"
				+ "However, you can close this dialog and use the query dialog to query over them.<br>"
				+ "You can also go to the 'Project information' tab to explore the experimental design and statistics on each of the projects.";
		final SafeHtml bigProjectNoticeSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(noticeText).toSafeHtml();
		final HTMLPanel importantNoticeHtml = new HTMLPanel(bigProjectNoticeSafeHtml);
		importantNoticeHtml.setStyleName("WelcomeProjectPanelBigProjectPanelHtml");
		cptnpnlBigProject.setContentWidget(importantNoticeHtml);
		importantNoticeHtml.setSize("100%", "100%");

		final StringBuilder projectsTagString = new StringBuilder("<ul>");

		for (final String loadedProject : loadedProjects) {
			projectsTagString.append("<li>").append(loadedProject).append("</li>");
		}
		projectsTagString.append("</ul>");

		final CaptionPanel cptnpnlProjectsLoaded = new CaptionPanel("Projects loaded");
		cptnpnlProjectsLoaded.setCaptionHTML("Projects loaded");
		cptnpnlProjectsLoaded.setStyleName("WelcomeProjectPanelInternalPanel");
		add(cptnpnlProjectsLoaded);

		final SafeHtml projectLoadedSafeHtml = new SafeHtmlBuilder().appendHtmlConstant(projectsTagString.toString())
				.toSafeHtml();

		final HTMLPanel projectLoadedHtml = new HTMLPanel(projectLoadedSafeHtml);
		projectLoadedHtml.setStyleName("WelcomeProjectPanelInternalPanelHtml");
		cptnpnlProjectsLoaded.setContentWidget(projectLoadedHtml);
		projectLoadedHtml.setSize("100%", "100%");

	}

}
