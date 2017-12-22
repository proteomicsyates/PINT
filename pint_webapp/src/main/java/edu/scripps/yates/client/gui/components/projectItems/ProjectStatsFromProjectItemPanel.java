package edu.scripps.yates.client.gui.components.projectItems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.components.MyWelcomeProjectPanel;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsFromProjectBean;
import edu.scripps.yates.shared.util.ProjectNamedQuery;

public class ProjectStatsFromProjectItemPanel extends AbstractProjectStatsItemPanel<ProjectBean> {
	private static ProjectStatsFromProjectItemPanel instance;

	public static ProjectStatsFromProjectItemPanel getInstance(QueryPanel queryPanel, boolean testMode, ProjectBean t,
			List<ProjectNamedQuery> recommendedQueries) {
		if (instance == null) {
			GWT.log("creating project stats from project item panel");
			instance = new ProjectStatsFromProjectItemPanel(queryPanel, testMode, t, recommendedQueries);
			GWT.log("project stats from project item panel created");
		} else {
			GWT.log("Updating parent with project");
			instance.updateParent(t);
			GWT.log("setting recommended queries");
			instance.setRecommendedQueries(recommendedQueries);
		}
		return instance;
	}

	private void setRecommendedQueries(List<ProjectNamedQuery> recommendedQueries) {
		this.recommendedQueries = recommendedQueries;
	}

	private static final int rowProjectStatus = 0;
	private static final int rowProjectTitle = 1;
	private static final int rowProjectDate = 2;
	private static final int rowProjectUploadDate = 3;
	private static final int rowProjectDescription = 4;
	private static final int rowProjectPublicationLink = 5;
	private static final int rowProjectRecommendedQueries = 6;
	private List<ProjectNamedQuery> recommendedQueries;
	private final QueryPanel queryPanel;
	private boolean testMode;

	private ProjectStatsFromProjectItemPanel(QueryPanel queryPanel, boolean testMode, ProjectBean projectBean,
			List<ProjectNamedQuery> recommendedQueries) {
		super("'" + projectBean.getTag() + "' summary", projectBean, true);
		GWT.log("after super in constructor of ProjectStatsFromProjectItemPanel");
		this.recommendedQueries = recommendedQueries;
		this.queryPanel = queryPanel;
		this.testMode = testMode;
		super.updateParent(projectBean);

	}

	private FlexTable getProjectGeneralInformationPanel(ProjectBean projectBean) {
		GWT.log("getting project general information panel: " + projectBean);

		FlexTable table = new FlexTable();
		// status

		final Label label1 = new Label("Project status:");
		label1.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectStatus, 0, label1);
		Label projectStatus = getProjectStatus(projectBean);
		table.setWidget(rowProjectStatus, 1, projectStatus);
		table.getCellFormatter().setAlignment(rowProjectStatus, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectStatus, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// title
		final Label label2 = new Label("Title:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectTitle, 0, label2);
		table.setWidget(rowProjectTitle, 1, getEscapedString(projectBean.getId(), true));
		table.getCellFormatter().setAlignment(rowProjectTitle, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectTitle, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// date
		final Label label3 = new Label("Experiment date:");
		label3.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectDate, 0, label3);
		table.setWidget(rowProjectDate, 1, getProjectDate(projectBean.getReleaseDate()));
		table.getCellFormatter().setAlignment(rowProjectDate, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectDate, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// upload date
		final Label label4 = new Label("Uploaded date:");
		label4.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectUploadDate, 0, label4);
		table.setWidget(rowProjectUploadDate, 1, getProjectDate(projectBean.getReleaseDate()));
		table.getCellFormatter().setAlignment(rowProjectUploadDate, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectUploadDate, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// description
		final Label label5 = new Label("Description:");
		label5.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectDescription, 0, label5);
		table.setWidget(rowProjectDescription, 1, getEscapedString(projectBean.getDescription(), true));
		table.getCellFormatter().setAlignment(rowProjectDescription, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectDescription, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// publication link
		final Label label6 = new Label("Publication link:");
		label6.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectPublicationLink, 0, label6);
		table.setWidget(rowProjectPublicationLink, 1, getPublicationLink(projectBean));
		table.getCellFormatter().setAlignment(rowProjectPublicationLink, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectPublicationLink, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		// default queries
		final Label label7 = new Label("Recommended queries:");
		label7.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectRecommendedQueries, 0, label7);
		table.getCellFormatter().setAlignment(rowProjectRecommendedQueries, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		int row = rowProjectRecommendedQueries;
		List<Panel> defaultQueriesPanels = getDefaultQueriesPanels();
		for (Panel panel : defaultQueriesPanels) {
			table.setWidget(row, 1, panel);
			table.getCellFormatter().setAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_TOP);
			row++;
		}
		return table;
	}

	private List<Panel> getDefaultQueriesPanels() {
		List<Panel> ret = new ArrayList<Panel>();
		if (recommendedQueries != null) {
			boolean defaultOne = true;
			for (ProjectNamedQuery recommendedQueries : recommendedQueries) {
				ret.add(MyWelcomeProjectPanel.getLinkToDataView(null, recommendedQueries, defaultOne, queryPanel,
						testMode, "defaultQueryLinkSmall"));
				defaultOne = false;
			}
		}
		return ret;
	}

	private Widget getPublicationLink(ProjectBean projectBean) {
		final Anchor pubmedLink = ClientSafeHtmlUtils.getPubmedLink(projectBean);
		if (pubmedLink != null) {
			return pubmedLink;
		}
		return getEscapedString(null, false);

	}

	private Label getEscapedString(String string, boolean skipStyle) {
		if (string != null) {
			Label label = new Label(string);
			if (!skipStyle) {
				label.setStyleName("no-wrap");
			}
			return label;
		} else {
			Label label = new Label("Not available");
			if (!skipStyle) {
				label.setStyleName("no-wrap");
			}
			return label;
		}
	}

	private Widget getProjectDate(Date date) {
		if (date != null) {
			DateLabel label = new DateLabel(
					com.google.gwt.i18n.client.DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG));
			label.setValue(date);
			label.setStyleName("no-wrap");
			return label;
		} else {
			return getEscapedString(null, false);
		}
	}

	private Label getProjectStatus(ProjectBean projectBean) {
		if (projectBean.isPublicAvailable()) {
			Label label = new Label("Public");
			label.setStyleName("no-wrap");
			return label;
		} else {
			Label label = new Label("This project is private");
			label.setStyleName("no-wrap");
			return label;
		}
	}

	@Override
	public ProjectStats<ProjectBean> createProjectStats(ProjectBean projectBean) {
		return new ProjectStatsFromProjectBean(projectBean);
	}

	@Override
	public void requestNumGenes(final ProjectBean projectBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowGenes, 1, imageLoader);
		super.proteinRetrievingService.getNumGenes(projectBean.getTag(), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				projectStatsMap.get(projectBean).setNumGenes(result);
				Label numGenesLabel = new Label(format.format(result));
				numGenesLabel.setStyleName("no-wrap");
				if (projectBean.equals(selectedItem.getT())) {
					rightPanel.setWidget(rowGenes, 1, numGenesLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				final Label widget = new Label("-");
				widget.setTitle(caught.getMessage());
				rightPanel.setWidget(rowGenes, 1, widget);
			}
		});

	}

	@Override
	public void requestNumPSMs(final ProjectBean projectBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPSMs, 1, imageLoader);
		super.proteinRetrievingService.getNumPSMs(projectBean.getTag(), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				projectStatsMap.get(projectBean).setNumPSMs(result);
				Label numPSMsLabel = new Label(format.format(result));
				numPSMsLabel.setStyleName("no-wrap");
				if (projectBean.equals(selectedItem.getT())) {
					rightPanel.setWidget(rowPSMs, 1, numPSMsLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				final Label widget = new Label("-");
				widget.setTitle(caught.getMessage());
				rightPanel.setWidget(rowPSMs, 1, widget);
			}
		});
	}

	@Override
	public void requestNumPeptides(final ProjectBean projectBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPeptides, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentPeptides(projectBean.getTag(), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				projectStatsMap.get(projectBean).setNumPeptides(result);
				Label numPeptidesLabel = new Label(format.format(result));
				numPeptidesLabel.setStyleName("no-wrap");
				if (projectBean.equals(selectedItem.getT())) {
					rightPanel.setWidget(rowPeptides, 1, numPeptidesLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				final Label widget = new Label("-");
				widget.setTitle(caught.getMessage());
				rightPanel.setWidget(rowPeptides, 1, widget);
			}
		});

	}

	@Override
	public void requestNumProteins(final ProjectBean projectBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowProteins, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentProteins(projectBean.getTag(), new AsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				projectStatsMap.get(projectBean).setNumProteins(result);
				Label numProteinsLabel = new Label(format.format(result));
				numProteinsLabel.setStyleName("no-wrap");
				if (projectBean.equals(selectedItem.getT())) {
					rightPanel.setWidget(rowProteins, 1, numProteinsLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				final Label widget = new Label("-");
				widget.setTitle(caught.getMessage());
				rightPanel.setWidget(rowProteins, 1, widget);
			}
		});

	}

	@Override
	public void requestNumMSRuns(final ProjectBean projectBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowMSRuns, 1, imageLoader);
		super.proteinRetrievingService.getMsRunsFromProject(projectBean.getTag(), new AsyncCallback<Set<String>>() {

			@Override
			public void onSuccess(Set<String> result) {
				projectStatsMap.get(projectBean).setNumMSRuns(result.size());
				Label numMSRunsLabel = new Label(format.format(result.size()));
				numMSRunsLabel.setStyleName("no-wrap");
				if (projectBean.equals(selectedItem.getT())) {
					rightPanel.setWidget(rowMSRuns, 1, numMSRunsLabel);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				final Label widget = new Label("-");
				widget.setTitle(caught.getMessage());
				rightPanel.setWidget(rowMSRuns, 1, widget);
			}
		});

	}

	@Override
	public void afterUpdateParent(ProjectBean projectBean) {
		GWT.log("afterUpdateParent beginning projectBan: " + projectBean);
		// add the general description panel to the left
		FlexTable leftPanel = getProjectGeneralInformationPanel(projectBean);
		setUniqueItemToList(leftPanel);
		setCaption("'" + projectBean.getTag() + "' summary");
		selectFirstItem();
	}

	@Override
	public void requestNumConditions(ProjectBean t) {
		// do nothing, no need to ask the server

	}

	@Override
	public void requestNumSamples(ProjectBean t) {
		// do nothing, no need to ask the server

	}

}
