package edu.scripps.yates.client.gui.components;

import java.util.Map;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.DefaultView;

public class ProjectInformationPanel extends Composite {
	private final FlowPanel flowPanelCenter;
	private final FlexTable flowPanelWest;
	private int numRow = 0;
	private final QueryPanel queryPanel;
	private DefaultView currentDefaultViewShowed;

	public ProjectInformationPanel(QueryPanel queryPanel) {
		this.queryPanel = queryPanel;
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		setStyleName("ProjectInformationPanel");
		DockPanel dockPanel = new DockPanel();
		mainPanel.add(dockPanel);

		dockPanel.setSize("100%", "100%");
		flowPanelWest = new FlexTable();
		flowPanelWest.setStyleName("ProjectInformationPanelLeft");
		dockPanel.add(flowPanelWest, DockPanel.WEST);
		dockPanel.setCellWidth(flowPanelWest, "50px");
		dockPanel.setCellHorizontalAlignment(flowPanelWest, HasHorizontalAlignment.ALIGN_CENTER);

		flowPanelCenter = new FlowPanel();
		dockPanel.add(flowPanelCenter, DockPanel.CENTER);
		flowPanelCenter.setSize("100%", "100%");

	}

	public void addProjectView(ProjectBean projectBean, DefaultView defaultView, boolean testMode) {
		FlowPanel panel = new FlowPanel();
		Label label = new Label(projectBean.getTag());
		label.setWordWrap(false);
		panel.add(label);
		label.setWidth("100%");
		panel.setStyleName("ProjectInformationProjectName");
		flowPanelWest.setWidget(numRow++, 0, panel);
		panel.addDomHandler(getMouseOverHandler(projectBean, defaultView, testMode), MouseOverEvent.getType());

		if (numRow == 1) {
			loadDefaultView(projectBean, defaultView, testMode);
		}
	}

	private MouseOverHandler getMouseOverHandler(final ProjectBean projectBean, final DefaultView defaultView,
			final boolean testMode) {

		MouseOverHandler mouseOverHandler = new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				loadDefaultView(projectBean, defaultView, testMode);

			}
		};

		return mouseOverHandler;
	}

	private void loadDefaultView(ProjectBean projectBean, DefaultView defaultView, boolean testMode) {
		if (!defaultView.equals(currentDefaultViewShowed)) {
			MyWelcomeProjectPanel panel = new MyWelcomeProjectPanel(projectBean, defaultView, queryPanel, testMode);
			flowPanelCenter.clear();
			flowPanelCenter.add(panel);
			currentDefaultViewShowed = defaultView;
		}
	}

	public void initPanel(Map<String, DefaultView> defaultViews) {

		for (String projectTag : defaultViews.keySet()) {
			FlowPanel panel = new FlowPanel();
			Label label = new Label(projectTag);
			panel.add(label);
			panel.setStyleName("ProjectInformationProjectName");

		}

	}
}
