package edu.scripps.yates.client.gui.components;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.components.projectItems.ConditionsItemPanel;
import edu.scripps.yates.client.gui.components.projectItems.MSRunsItemPanel;
import edu.scripps.yates.client.gui.components.projectItems.OrganismsItemPanel;
import edu.scripps.yates.client.gui.components.projectItems.ProjectStatsFromProjectItemPanel;
import edu.scripps.yates.client.gui.components.projectItems.SamplesItemPanel;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask2;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.util.DefaultView;

public class ProjectInformationPanel extends Composite {
	private final FlexTable gridInCenter;
	private final FlexTable flexTableInWest;
	private int numRow = 0;
	private DefaultView currentDefaultViewShowed;
	private final Map<ProjectBean, FlowPanel> panelsByProject = new HashMap<ProjectBean, FlowPanel>();
	private final Map<FlowPanel, ProjectBean> projectsByPanels = new HashMap<FlowPanel, ProjectBean>();

	private final Map<ProjectBean, DefaultView> defaultViewsByProjectBean = new HashMap<ProjectBean, DefaultView>();
	private FlowPanel selectedPanel;
	private final boolean testMode;
	private final QueryPanel queryPanel;

	/**
	 * The parameters are for the recommended queries
	 * 
	 * @param queryPanel
	 * @param testMode
	 */
	public ProjectInformationPanel(QueryPanel queryPanel, boolean testMode) {
		final FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);
		setStyleName("ProjectInformationPanel");
		final DockPanel dockPanel = new DockPanel();
		mainPanel.add(dockPanel);

		dockPanel.setSize("100%", "100%");

		final CaptionPanel captionPanelProjects = new CaptionPanel("Projects loaded");
		captionPanelProjects.setStyleName("ProjectLoadedPanel");
		flexTableInWest = new FlexTable();
		flexTableInWest.setStyleName("ProjectInformationPanelLeft");
		captionPanelProjects.add(flexTableInWest);

		dockPanel.add(captionPanelProjects, DockPanel.WEST);
		dockPanel.setCellWidth(captionPanelProjects, "50px");
		dockPanel.setCellHorizontalAlignment(captionPanelProjects, HasHorizontalAlignment.ALIGN_CENTER);
		gridInCenter = new FlexTable();
		// gridInCenter = new Grid(5, 2);
		dockPanel.add(gridInCenter, DockPanel.CENTER);
		gridInCenter.setSize("100%", "100%");

		this.queryPanel = queryPanel;
		this.testMode = testMode;
	}

	public void addProjectView(ProjectBean projectBean, DefaultView defaultView) {
		if (!defaultViewsByProjectBean.containsKey(projectBean)) {
			defaultViewsByProjectBean.put(projectBean, defaultView);
			// small rounded panel with the name of the project
			final FlowPanel panel = new FlowPanel();
			final Label label = new Label(projectBean.getTag());
			label.setWordWrap(false);
			panel.add(label);
			label.setWidth("100%");
			panel.setStyleName("ProjectInformationProjectName");
			panel.addDomHandler(getMouseOverHandler(projectBean, defaultView), MouseOverEvent.getType());
			panel.addDomHandler(getClickHandler(projectBean), ClickEvent.getType());
			panel.addDomHandler(getMouseOutHandler(projectBean), MouseOutEvent.getType());
			panelsByProject.put(projectBean, panel);
			projectsByPanels.put(panel, projectBean);
			flexTableInWest.setWidget(numRow++, 0, panel);

			if (numRow == 1) {
				loadDefaultView(projectBean, defaultView, true);
			}
		}
	}

	private MouseOutHandler getMouseOutHandler(final ProjectBean projectBean) {
		final MouseOutHandler mouseOutHandler = new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				final FlowPanel flowPanel = panelsByProject.get(projectBean);
				if (!flowPanel.equals(selectedPanel)) {
					flowPanel.setStyleName("ProjectInformationProjectName");
				}
				if (selectedPanel != null) {
					final ProjectBean selectedProject = projectsByPanels.get(selectedPanel);
					loadDefaultView(selectedProject, defaultViewsByProjectBean.get(selectedProject), true);
				}
			}
		};
		return mouseOutHandler;
	}

	private ClickHandler getClickHandler(final ProjectBean projectBean) {
		final ClickHandler clickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final FlowPanel flowPanel = panelsByProject.get(projectBean);
				if (flowPanel.equals(selectedPanel)) {
					selectedPanel = null;
					flowPanel.setStyleName("ProjectInformationProjectName");
				} else {
					// de select the previous selectedPanel if available
					if (selectedPanel != null) {
						selectedPanel.setStyleName("ProjectInformationProjectName");
					}
					flowPanel.setStyleName("ProjectInformationProjectName-Selected");
					selectedPanel = flowPanel;
				}
			}
		};
		return clickHandler;
	}

	private MouseOverHandler getMouseOverHandler(final ProjectBean projectBean, final DefaultView defaultView) {

		final MouseOverHandler mouseOverHandler = new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				GWT.log("On mouse over event");
				loadDefaultView(projectBean, defaultView, true);

			}
		};

		return mouseOverHandler;
	}

	private void loadDefaultView(ProjectBean projectBean, DefaultView defaultView, boolean resetItems) {
		if (!defaultView.equals(currentDefaultViewShowed)) {
			// MyWelcomeProjectPanel panel = new
			// MyWelcomeProjectPanel(projectBean, defaultView, queryPanel,
			// testMode);
			gridInCenter.clear();
			gridInCenter.getColumnFormatter().setWidth(0, "400px");
			gridInCenter.getColumnFormatter().setWidth(1, "400px");
			// projectStats
			final ProjectStatsFromProjectItemPanel projectStatsItemPanel = ProjectStatsFromProjectItemPanel
					.getInstance(queryPanel, testMode, projectBean, defaultView, resetItems);
			gridInCenter.setWidget(0, 0, projectStatsItemPanel);
			gridInCenter.getFlexCellFormatter().setColSpan(0, 0, 2);
			// conditions
			final ConditionsItemPanel conditionsItemPanel = ConditionsItemPanel.getInstance(projectBean, resetItems);
			gridInCenter.setWidget(1, 0, conditionsItemPanel);
			conditionsItemPanel.addOnItemSelectedEvent(new DoSomethingTask2<ExperimentalConditionBean>() {
				@Override
				public Void doSomething(ExperimentalConditionBean conditionBean) {

					Widget widget = null;
					try {
						widget = gridInCenter.getWidget(1, 1);
					} catch (final IndexOutOfBoundsException e) {
					}
					// if there is not widget yet

					if (widget == null) {
						gridInCenter.setWidget(1, 1, conditionsItemPanel.getSelectedItemStatsPanel());
					}
					return null;
				}
			});

			// msRuns
			final MSRunsItemPanel msRunsItemPanel = MSRunsItemPanel.getInstance(projectBean, resetItems);
			gridInCenter.setWidget(2, 0, msRunsItemPanel);
			msRunsItemPanel.addOnItemSelectedEvent(new DoSomethingTask2<MSRunBean>() {
				@Override
				public Void doSomething(MSRunBean msRun) {
					Widget widget = null;
					try {
						widget = gridInCenter.getWidget(2, 1);
					} catch (final IndexOutOfBoundsException e) {
					}
					// if there is not widget yet

					if (widget == null) {
						gridInCenter.setWidget(2, 1, msRunsItemPanel.getSelectedItemStatsPanel());
					}
					return null;
				}
			});

			// samples
			final SamplesItemPanel samplesItemPanel = SamplesItemPanel.getInstance(projectBean, resetItems);
			gridInCenter.setWidget(3, 0, samplesItemPanel);
			samplesItemPanel.addOnItemSelectedEvent(new DoSomethingTask2<SampleBean>() {
				@Override
				public Void doSomething(SampleBean sampleBean) {

					Widget widget = null;
					try {
						widget = gridInCenter.getWidget(3, 1);
					} catch (final IndexOutOfBoundsException e) {
					}
					// if there is not widget yet

					if (widget == null) {
						gridInCenter.setWidget(3, 1, samplesItemPanel.getSelectedItemStatsPanel());
					}
					return null;
				}
			});
			// organisms
			final OrganismsItemPanel organismItemPanel = OrganismsItemPanel.getInstance(projectBean, resetItems);
			gridInCenter.setWidget(4, 0, organismItemPanel);

			currentDefaultViewShowed = defaultView;
		}
	}

	public void initPanel(Map<String, DefaultView> defaultViews) {

		for (final String projectTag : defaultViews.keySet()) {
			final FlowPanel panel = new FlowPanel();
			final Label label = new Label(projectTag);
			panel.add(label);
			panel.setStyleName("ProjectInformationProjectName");

		}

	}
}
