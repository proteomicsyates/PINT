package edu.scripps.yates.client.gui;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;

import edu.scripps.yates.client.gui.templates.MyClientBundle;

public class HeaderPanel extends Composite {
	public HeaderPanel() {

		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("headerPanel");
		initWidget(mainPanel);

		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;

		FlowPanel scrippsLogoHorizontalPanel = new FlowPanel();
		scrippsLogoHorizontalPanel.setStyleName("whiteBackground");

		// mainPanel.add(scrippsLogoHorizontalPanel);

		Image image = new Image(myClientBundle.tsrilogo());
		image.setStyleName("imagesHeader");
		scrippsLogoHorizontalPanel.add(image);
		// Image("images/tsrilogo.gif");
		image.setTitle("The Scripps Research Institute");
		image.setSize("70px", "40px");

		Image image_1 = new Image(myClientBundle.yatesLabSmall());
		image_1.setStyleName("imagesHeader");
		scrippsLogoHorizontalPanel.add(image_1);
		image_1.setTitle("Yates Laboratory");
		image_1.setSize("120px", "40px");

		DockPanel pintLogoHorizontalPanel = new DockPanel();
		pintLogoHorizontalPanel.setBorderWidth(0);
		pintLogoHorizontalPanel.setStyleName("header");
		pintLogoHorizontalPanel.setSpacing(10);
		pintLogoHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		mainPanel.add(pintLogoHorizontalPanel);

		InlineHTML nlnhtmlPint = new InlineHTML("PINT");
		nlnhtmlPint.setStyleName("pintWord");
		nlnhtmlPint.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pintLogoHorizontalPanel.add(nlnhtmlPint, DockPanel.WEST);

		InlineHTML nlnhtmlProteomicsIntegrator = new InlineHTML("Proteomics INTegrator");
		nlnhtmlProteomicsIntegrator.setStyleName("proteomics_integrator");
		nlnhtmlProteomicsIntegrator.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		pintLogoHorizontalPanel.add(nlnhtmlProteomicsIntegrator, DockPanel.WEST);
		nlnhtmlProteomicsIntegrator.setWidth("95%");

		// TODO
		// removed the search by now
		// HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		// searchHorizontalPanel
		// .setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		// searchHorizontalPanel.setBorderWidth(0);
		// searchHorizontalPanel.setStyleName("searchBox");
		// searchHorizontalPanel
		// .setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		// searchHorizontalPanel.setSpacing(10);
		//
		//
		// pintLogoHorizontalPanel.add(searchHorizontalPanel,
		// DockPanel.EAST);
		//
		// pintLogoHorizontalPanel.setCellHorizontalAlignment(
		// searchHorizontalPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		// searchHorizontalPanel.setSize("85%", "85%");
		//
		// TextBox searchTextBox = new TextBox();
		// searchTextBox.setAlignment(TextAlignment.LEFT);
		// searchHorizontalPanel.add(searchTextBox);
		// searchHorizontalPanel.setCellHorizontalAlignment(searchTextBox,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// searchTextBox.setSize("410px", "");

		// TextButton txtbtnNewButton = new TextButton("Search");
		// searchHorizontalPanel.add(txtbtnNewButton);
		// searchHorizontalPanel.setCellVerticalAlignment(txtbtnNewButton,
		// HasVerticalAlignment.ALIGN_MIDDLE);
		// searchHorizontalPanel.setCellHorizontalAlignment(txtbtnNewButton,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// txtbtnNewButton.setSize("100px", "");

		FlowPanel menuHorizontalPanel = new FlowPanel();
		menuHorizontalPanel.setStyleName("menu1");

		mainPanel.add(menuHorizontalPanel);

		Hyperlink homeHyperlink = new Hyperlink("Home", false, "home");
		homeHyperlink.setStyleName("menu1button");
		menuHorizontalPanel.add(homeHyperlink);
		homeHyperlink.setWidth("");

		Hyperlink submitHyperlink = new Hyperlink("Submit", false, "submit");
		submitHyperlink.setStyleName("menu1button");
		menuHorizontalPanel.add(submitHyperlink);
		submitHyperlink.setWidth("");

		Hyperlink browseHyperlink = new Hyperlink("Browse", false, "browse");
		browseHyperlink.setStyleName("menu1button");
		menuHorizontalPanel.add(browseHyperlink);
		browseHyperlink.setWidth("");

		Hyperlink queryHyperlink = new Hyperlink("Query", false, "query");
		queryHyperlink.setStyleName("menu1button");
		menuHorizontalPanel.add(queryHyperlink);
		queryHyperlink.setWidth("");
		HorizontalPanel emptyPanel = new HorizontalPanel();
		emptyPanel.setStyleName("horizontalComponent-alignLeft");
		menuHorizontalPanel.add(emptyPanel);
		emptyPanel.setSize("100px", "30px");
		HorizontalPanel emptyPanel2 = new HorizontalPanel();
		emptyPanel2.setStyleName("horizontalComponent-alignLeft");
		menuHorizontalPanel.add(emptyPanel2);
		emptyPanel2.setSize("100px", "30px");
		HorizontalPanel emptyPanel3 = new HorizontalPanel();
		emptyPanel3.setStyleName("horizontalComponent-alignLeft");
		menuHorizontalPanel.add(emptyPanel3);
		emptyPanel3.setSize("100px", "30px");
		Hyperlink aboutHyperlink = new Hyperlink("About", false, "about");
		aboutHyperlink.setStyleName("menu1button");
		menuHorizontalPanel.add(aboutHyperlink);
		aboutHyperlink.setWidth("");

		Hyperlink helpHyperlink = new Hyperlink("Help", false, "help");
		helpHyperlink.setStyleName("menu2button");
		menuHorizontalPanel.add(helpHyperlink);
		helpHyperlink.setWidth("");

	}
}
