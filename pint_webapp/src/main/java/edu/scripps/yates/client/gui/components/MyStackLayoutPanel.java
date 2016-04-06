package edu.scripps.yates.client.gui.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MyStackLayoutPanel extends Composite {

	private static final double HEADER_SIZE = 25;
	private final StackLayoutPanel mainPanel;

	public MyStackLayoutPanel() {
		mainPanel = new StackLayoutPanel(Unit.PX);
		initWidget(mainPanel);
		mainPanel.addStyleName("stack-panel");
		mainPanel.setAnimationDuration(200);
		// mainPanel.setStyleName("MainPanel");
	}

	public void addWidget(Widget widget, String label) {
		mainPanel.add(widget, label, HEADER_SIZE);
	}

}
