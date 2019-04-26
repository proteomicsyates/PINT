package edu.scripps.yates.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;

public class HelpPanel extends InitializableComposite {
	private final NavigationHorizontalPanel navigation;

	public HelpPanel() {

		final FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		mainPanel.setHeight("797px");

		final HeaderPanel header = new HeaderPanel();
		mainPanel.add(header);

		navigation = new NavigationHorizontalPanel(TargetHistory.HELP);
		mainPanel.add(navigation);

		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("helpMainPanel");
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setSpacing(10);
		mainPanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		final Label lblThisPageAllow = new Label("PINT help");
		lblThisPageAllow.setStyleName("title1");
		lblThisPageAllow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		verticalPanel.add(lblThisPageAllow);

		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setStyleName("verticalComponent");
		verticalPanel.add(horizontalPanel);

		final int fontsize = 17;
		final InlineLabel nlnlblSourceCodeAnd = new InlineLabel(
				"Source code and additional information is located in the following GitHub page: ");
		nlnlblSourceCodeAnd.getElement().getStyle().setFontSize(fontsize, Unit.PX);
		horizontalPanel.add(nlnlblSourceCodeAnd);

		final InlineHTML nlnhtmlNewInlinehtml_1 = new InlineHTML(
				"<a href=\"https://github.com/proteomicsyates/pint\" target=\"_blank\" class=\"linkPINT\">PINT on GitHub</a>");
		nlnhtmlNewInlinehtml_1.getElement().getStyle().setFontSize(fontsize, Unit.PX);
		horizontalPanel.add(nlnhtmlNewInlinehtml_1);

		final HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setStyleName("verticalComponent");
		verticalPanel.add(horizontalPanel_1);

		final InlineLabel nlnlblNewInlinelabel = new InlineLabel("Guide for quering and filtering the data in PINT: ");
		nlnlblNewInlinelabel.getElement().getStyle().setFontSize(fontsize, Unit.PX);
		horizontalPanel_1.add(nlnlblNewInlinelabel);

		final InlineHTML nlnhtmlhowToQuery = new InlineHTML(
				"<a href=\"pint/PINT_help_Query_Commands.pdf\" target=\"_blank\" class=\"linkPINT\">How to query data in PINT [PDF]</a>");
		nlnhtmlhowToQuery.getElement().getStyle().setFontSize(fontsize, Unit.PX);
		horizontalPanel_1.add(nlnhtmlhowToQuery);

		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("verticalComponent");
		verticalPanel.add(flowPanel);
		flowPanel.setSize("100%", "100%");

		final VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setStyleName("helpTable");
		flowPanel.add(verticalPanel_1);

		final Label lblHowToBrowse = new Label("How to browse over data");
		lblHowToBrowse.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_1.add(lblHowToBrowse);
		lblHowToBrowse.setStyleName("title2");

		final SimplePanel simplePanel = new SimplePanel();
		simplePanel.setStyleName("helpExplanation");
		verticalPanel_1.add(simplePanel);

		final InlineHTML nlnhtmlInOrderTo = new InlineHTML(
				"In order to access to the data, the user must click on 'Browse' button.<br>\r\n<br>\r\nThen, the list of available projects will be showed. By selecting each one of the projects, related information will be showed in the right panel.<br>\r\n<br>\r\nAfter selecting one or more project(s), click on 'Explore' button in order to load all the data in the browser.<br>\r\n<br>\r\nData is then presented in three different views:\r\n<ul>\r\n<li><b>Individual proteins view</b>: It is divided into two tables, one in the top listing all the proteins and one in the bottom showing the PSMs corresponding to the selected protein in the top table.</li>\r\n<li><b>Protein groups view</b>: This table is by default empty. Go to the menu 'Protein inference' in the left menu bar and click on 'click here to group proteins' button. Then, all proteins will be grouped according to shared peptides and classified according to <a href=\"https://code.google.com/p/ehu-bio/wiki/PAnalyzer\" target=\"_blank\" class=\"linkPINT\">PAnalyzer</a> protein inference algorithm.</li>\r\n<li><b>PSMs view</b>: This table is showing all PSMs belonging to all proteins in the dataset. Thus, each PSM can be associated to a different protein.</li>\r\n<ul>");
		simplePanel.setWidget(nlnhtmlInOrderTo);
		nlnhtmlInOrderTo.setSize("100%", "100%");

		final VerticalPanel verticalPanel_2 = new VerticalPanel();
		verticalPanel_2.setStyleName("helpTable");
		flowPanel.add(verticalPanel_2);

		final Label lblHowToQuery = new Label("How to query data");
		lblHowToQuery.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_2.add(lblHowToQuery);
		lblHowToQuery.setStyleName("title2");

		final SimplePanel simplePanel_1 = new SimplePanel();
		simplePanel_1.setStyleName("helpExplanation");
		verticalPanel_2.add(simplePanel_1);

		final InlineHTML nlnhtmlPintProvidesA = new InlineHTML(
				"PINT provides a set of different commands to query over the data.<br>\r\n<br>\r\nThe list of commands and examples of use are available in the following help document:<br>\r\n<ul>\r\n<li><a href=\"pint/PINT_help_Query_Commands.pdf\" class=\"linkPINT\" target=\"_blank\">How to query data in PINT [PDF]</a> [updated: April 17, 2015]</li>\r\n</ul>\r\nAfter sending a query, it will be processed by the server and the resulting list of proteins will be automatically showed in the 'dataset view' tab. The corresponding PSMs and protein groups will be showed in the appropiate tabs.<br>\r\n<br>\r\nAs can be readed in the previous help document, commands can be combined in logical operations (using !, AND, OR, XOR operators)");
		simplePanel_1.setWidget(nlnhtmlPintProvidesA);
		nlnhtmlPintProvidesA.setSize("100%", "100%");

		final VerticalPanel verticalPanel_3 = new VerticalPanel();
		verticalPanel_3.setStyleName("helpTable");
		flowPanel.add(verticalPanel_3);

		final Label lblHowToSubmit = new Label("How to submit data");
		lblHowToSubmit.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel_3.add(lblHowToSubmit);
		lblHowToSubmit.setStyleName("title2");

		final SimplePanel simplePanel_2 = new SimplePanel();
		simplePanel_2.setStyleName("helpExplanation");
		verticalPanel_3.add(simplePanel_2);

		final InlineHTML nlnhtmlNewInlinehtml = new InlineHTML(
				"Submission pipeline is currently under development.<br>\r\n<br>\r\nPINT will provide a web wizard helping the user to upload his final results in a very friendly way. Any custom Excel table will be supported. Additionally, external data files will also be supported in order to get the data from.<br>\r\n<br>\r\nList of supported input data formats:<br>\r\n<ul>\r\n<li>Any Excel file (xsl and xslx)</li>\r\n<li><i>DTASelect-filter.txt</i>: DTASelect filter files (output files from DTASelect protein identification post-processing tool)</li>\r\n<li><i>census-chro.xml</i>: Custom build output file from Census quantitative data analysis tool</li>\r\n</ul>");
		simplePanel_2.setWidget(nlnhtmlNewInlinehtml);
		nlnhtmlNewInlinehtml.setSize("100%", "100%");

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
