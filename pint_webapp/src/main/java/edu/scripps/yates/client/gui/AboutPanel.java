package edu.scripps.yates.client.gui;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;

public class AboutPanel extends InitializableComposite {
	private final NavigationHorizontalPanel navigation;

	public AboutPanel() {

		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);

		HeaderPanel header = new HeaderPanel();
		mainPanel.add(header);

		navigation = new NavigationHorizontalPanel(TargetHistory.ABOUT);
		mainPanel.add(navigation);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("helpMainPanel");
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setSpacing(10);
		mainPanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		Label lblThisPageAllow = new Label("About PINT");
		lblThisPageAllow.setStyleName("title1");
		lblThisPageAllow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblThisPageAllow);

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("verticalComponent");
		verticalPanel.add(flowPanel);
		flowPanel.setSize("100%", "100%");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setStyleName("aboutLine");
		flowPanel.add(verticalPanel_1);

		Label lblHowToBrowse = new Label("What is PINT?");
		lblHowToBrowse.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel_1.add(lblHowToBrowse);
		lblHowToBrowse.setStyleName("title2");

		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("aboutExplanation");
		verticalPanel_1.add(simplePanel);

		InlineHTML nlnhtmlInOrderTo = new InlineHTML(
				"The <b>motivation</b> of the Proteomics INTegrator is based on these four ideas:\r\n<ul>\r\n<li>PINT tries to be an experiment repository for all the quantitative data generated in the laboratory</li>\r\n<li>We want to store each dataset and to reference it in their corresponding publications with a PINT identifier</li>\r\n<li>We want to provide the data to the scientific community</li>\r\n<li>We want to avoid the common situation in which the data dies when the scientist is gone</li>\r\n</ul>");
		simplePanel.setWidget(nlnhtmlInOrderTo);
		nlnhtmlInOrderTo.setSize("100%", "100%");

		Label lblRelatedPublicationsAnd = new Label("Related publications and presentations on scientific symposiums");
		lblRelatedPublicationsAnd.setStyleName("title2");
		lblRelatedPublicationsAnd.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel_1.add(lblRelatedPublicationsAnd);

		SimplePanel simplePanel_1 = new SimplePanel();
		simplePanel_1.setStyleName("aboutExplanation");
		verticalPanel_1.add(simplePanel_1);

		InlineHTML nlnhtmloralCommunicationIn = new InlineHTML(
				"Presentations:\r\n<ul>\r\n<li>Oral communication in the <a href=\"http://www.hupo2014.com\" class=\"_blank\">HUPO 13th Annual World Congress</a> in Madrid, Spain from October 5 \u2013 8, 2014.<br>\r\n<b>Title:</b> <i>PINT: A new platform for the comprehensive integration of complex large-scale proteomics data and results</i><br>\r\n<b>Presented by:</b> Salvador Martinez-Bartolome (salvador at scripps.edu)\r\n</li>\r\n</ul>");
		simplePanel_1.setWidget(nlnhtmloralCommunicationIn);
		nlnhtmloralCommunicationIn.setSize("100%", "100%");

		Label lblDeveloperTeam = new Label("Developer team");
		lblDeveloperTeam.setStyleName("title2");
		lblDeveloperTeam.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel_1.add(lblDeveloperTeam);

		SimplePanel simplePanel_2 = new SimplePanel();
		simplePanel_2.setStyleName("aboutExplanation");
		verticalPanel_1.add(simplePanel_2);

		InlineHTML nlnhtmlTeamOfDevelopers = new InlineHTML(
				"Team of developers\r\n<ul>\r\n<li>Salvador Martinez-Bartolome (salvador at scripps.edu)</li>\r\n<li>Mathieu Lavall\u00E9e-Adam (malaval at scripps.edu)</li>\r\n<li>Robin (Sung Kyu) Park (rpark at scripps.edu)</li>\r\n</ul>\r\n");
		simplePanel_2.setWidget(nlnhtmlTeamOfDevelopers);
		nlnhtmlTeamOfDevelopers.setSize("100%", "100%");

		HorizontalPanel horizontalPanelImages = new HorizontalPanel();
		horizontalPanelImages.setStyleName("aboutExplanation");
		verticalPanel_1.add(horizontalPanelImages);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
		Image logo1 = new Image(myClientBundle.yateslablogo());
		logo1.setTitle("YATES Laboratory");
		logo1.setStyleName("imagesHeader");
		horizontalPanelImages.add(logo1);
		Image logo2 = new Image(myClientBundle.tsrilogo());
		logo2.setTitle("TSRI: The Scripps Research Institute");
		logo2.setStyleName("imagesHeader");
		horizontalPanelImages.add(logo2);

	}

	private MyDialogBox loadingDialog;

	private void showLoadingDialog(String text) {

		loadingDialog = new MyDialogBox(text, false, false);
		loadingDialog.center();
	}

	private void hiddeLoadingDialog() {
		if (loadingDialog != null)
			loadingDialog.hide();
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}
}
