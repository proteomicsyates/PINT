package edu.scripps.yates.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalSplitPanel;

public class Test extends Composite {
	public Test() {

		DockLayoutPanel panel = new DockLayoutPanel(Unit.PCT);
		initWidget(panel);
		panel.setSize("900px", "1000px");

		SimplePanel simplePanel = new SimplePanel();
		panel.addNorth(simplePanel, 7.4);
		simplePanel.setSize("100%", "100%");

		SimplePanel simplePanel_3 = new SimplePanel();
		panel.addSouth(simplePanel_3, 10.0);
		simplePanel_3.setSize("100%", "100%");

		TextArea textArea = new TextArea();
		simplePanel_3.setWidget(textArea);
		textArea.setSize("100%", "100%");

		VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
		panel.addWest(verticalSplitPanel, 10.0);

		DecoratedStackPanel decoratedStackPanel = new DecoratedStackPanel();
		verticalSplitPanel.setTopWidget(decoratedStackPanel);
		decoratedStackPanel.setSize("100%", "100%");

		DecoratedStackPanel decoratedStackPanel_1 = new DecoratedStackPanel();
		verticalSplitPanel.setBottomWidget(decoratedStackPanel_1);
		decoratedStackPanel_1.setSize("100%", "100%");

		SimplePanel simplePanel_1 = new SimplePanel();
		panel.add(simplePanel_1);
		simplePanel_1.setSize("100%", "100%");

		TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(1.5, Unit.EM);
		simplePanel_1.setWidget(tabLayoutPanel);
		tabLayoutPanel.setSize("100%", "100%");
	}

}
