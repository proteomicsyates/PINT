package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalPanelWithHeader extends Composite {
	private final FlowPanel headerPanel;
	private final FlowPanel horizontalPanel;

	public HorizontalPanelWithHeader(String header) {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("HorizontalPanelWithHeader");
		headerPanel = new FlowPanel();
		headerPanel.setStyleName("HorizontalPanelWithHeader-Header");
		Label label = new Label(header);
		label.setStyleName("HorizontalPanelWithHeader_Header-label");
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		headerPanel.add(label);
		mainPanel.add(headerPanel);
		horizontalPanel = new FlowPanel();
		horizontalPanel.setStyleName("HorizontalPanelWithHeader-Container");
		mainPanel.add(horizontalPanel);

		initWidget(mainPanel);

	}

	public void addToHorizontalPanel(Widget widget) {
		widget.setStyleName("HorizontalPanelWithHeader-Content");
		horizontalPanel.add(widget);
	}
}
