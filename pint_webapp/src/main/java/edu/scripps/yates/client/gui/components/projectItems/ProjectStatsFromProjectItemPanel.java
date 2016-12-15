package edu.scripps.yates.client.gui.components.projectItems;

import java.util.Date;
import java.util.Set;

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
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsFromProjectBean;

public class ProjectStatsFromProjectItemPanel extends AbstractProjectStatsItemPanel<ProjectBean> {
	private static ProjectStatsFromProjectItemPanel instance;

	public static ProjectStatsFromProjectItemPanel getInstance(ProjectBean t) {
		if (instance == null) {
			instance = new ProjectStatsFromProjectItemPanel(t);
		} else {
			instance.updateParent(t);
		}
		return instance;
	}

	private static final int rowProjectStatus = 0;
	private static final int rowProjectTitle = 1;
	private static final int rowProjectDate = 2;
	private static final int rowProjectUploadDate = 3;
	private static final int rowProjectDescription = 4;
	private static final int rowProjectPublicationLink = 5;

	private ProjectStatsFromProjectItemPanel(ProjectBean projectBean) {
		super("'" + projectBean.getTag() + "' summary", projectBean, true);

	}

	private FlexTable getProjectGeneralInformationPanel(ProjectBean projectBean) {
		FlexTable table = new FlexTable();
		// status
		final Label label1 = new Label("Project status:");
		label1.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectStatus, 0, label1);
		table.setWidget(rowProjectStatus, 1, getProjectStatus(projectBean));
		table.getCellFormatter().setAlignment(rowProjectStatus, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectStatus, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		// title
		final Label label2 = new Label("Title:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectTitle, 0, label2);
		table.setWidget(rowProjectTitle, 1, getEscapedString(projectBean.getId()));
		table.getCellFormatter().setAlignment(rowProjectTitle, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectTitle, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		// date
		final Label label3 = new Label("Experiment date:");
		label3.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectDate, 0, label3);
		table.setWidget(rowProjectDate, 1, getProjectDate(projectBean.getReleaseDate()));
		table.getCellFormatter().setAlignment(rowProjectDate, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectDate, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		// upload date
		final Label label4 = new Label("Uploaded date:");
		label4.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectUploadDate, 0, label4);
		table.setWidget(rowProjectUploadDate, 1, getProjectDate(projectBean.getReleaseDate()));
		table.getCellFormatter().setAlignment(rowProjectUploadDate, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectUploadDate, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		// description
		final Label label5 = new Label("Description:");
		label5.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectDescription, 0, label5);
		table.setWidget(rowProjectDescription, 1, getEscapedString(projectBean.getDescription()));
		table.getCellFormatter().setAlignment(rowProjectDescription, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectDescription, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		// publication link
		final Label label6 = new Label("Publication link:");
		label6.setStyleName("ProjectItemIndividualItemTitle");
		table.setWidget(rowProjectPublicationLink, 0, label6);
		table.setWidget(rowProjectPublicationLink, 1, getPublicationLink(projectBean));
		table.getCellFormatter().setAlignment(rowProjectPublicationLink, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setAlignment(rowProjectPublicationLink, 1, HasHorizontalAlignment.ALIGN_JUSTIFY,
				HasVerticalAlignment.ALIGN_TOP);
		return table;
	}

	private Widget getPublicationLink(ProjectBean projectBean) {
		final Anchor pubmedLink = ClientSafeHtmlUtils.getPubmedLink(projectBean);
		if (pubmedLink != null) {
			return pubmedLink;
		}
		return getEscapedString(null);

	}

	private Label getEscapedString(String string) {
		if (string != null) {
			return new Label(string);
		} else {
			return new Label("Not available");
		}
	}

	private Widget getProjectDate(Date date) {
		if (date != null) {
			DateLabel label = new DateLabel(
					com.google.gwt.i18n.client.DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG));
			label.setValue(date);
			return label;
		} else {
			return getEscapedString(null);
		}
	}

	private Label getProjectStatus(ProjectBean projectBean) {
		if (projectBean.isPublicAvailable()) {
			return new Label("Public");
		} else {
			return new Label("This project is private");
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
