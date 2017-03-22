package edu.scripps.yates.client.gui.components.projectItems;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsFromExperimentalCondition;

public class ProjectStatsFromConditionItemPanel extends AbstractProjectStatsItemPanel<ExperimentalConditionBean> {
	private static ProjectStatsFromConditionItemPanel instance;

	public static ProjectStatsFromConditionItemPanel getInstance(ExperimentalConditionBean t) {
		if (instance == null) {
			instance = new ProjectStatsFromConditionItemPanel(t);
		} else {
			instance.updateParent(t);
		}
		return instance;
	}

	private ProjectStatsFromConditionItemPanel(ExperimentalConditionBean conditionBean) {
		super("Condition '" + conditionBean.getId() + "'", conditionBean);
	}

	@Override
	public void requestNumGenes(final ExperimentalConditionBean conditionBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowGenes, 1, imageLoader);
		super.proteinRetrievingService.getNumGenes(conditionBean.getProject().getTag(), conditionBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(conditionBean).setNumGenes(result);
						Label numGenesLabel = new Label(format.format(result));
						numGenesLabel.setStyleName("no-wrap");
						if (conditionBean.equals(selectedItem.getT())) {
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
	public void requestNumPSMs(final ExperimentalConditionBean conditionBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPSMs, 1, imageLoader);
		super.proteinRetrievingService.getNumPSMs(conditionBean.getProject().getTag(), conditionBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(conditionBean).setNumPSMs(result);
						Label numPSMsLabel = new Label(format.format(result));
						numPSMsLabel.setStyleName("no-wrap");
						if (conditionBean.equals(selectedItem.getT())) {
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
	public void requestNumPeptides(final ExperimentalConditionBean conditionBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPeptides, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentPeptides(conditionBean.getProject().getTag(), conditionBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(conditionBean).setNumPeptides(result);
						Label numPeptidesLabel = new Label(format.format(result));
						numPeptidesLabel.setStyleName("no-wrap");
						if (conditionBean.equals(selectedItem.getT())) {
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
	public void requestNumProteins(final ExperimentalConditionBean conditionBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowProteins, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentProteins(conditionBean.getProject().getTag(), conditionBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(conditionBean).setNumProteins(result);
						Label numProteinsLabel = new Label(format.format(result));
						numProteinsLabel.setStyleName("no-wrap");
						if (conditionBean.equals(selectedItem.getT())) {
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
	public void requestNumMSRuns(final ExperimentalConditionBean conditionBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowMSRuns, 1, imageLoader);
		super.proteinRetrievingService.getNumMSRuns(conditionBean.getProject().getTag(), conditionBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(conditionBean).setNumMSRuns(result);
						Label numMSRunsLabel = new Label(format.format(result));
						numMSRunsLabel.setStyleName("no-wrap");
						if (conditionBean.equals(selectedItem.getT())) {
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
	public void afterUpdateParent(ExperimentalConditionBean parent) {
		setCaption("Condition '" + parent.getId() + "'");
	}

	@Override
	public void requestNumConditions(ExperimentalConditionBean t) {
		// do nothing, no need to ask the server

	}

	@Override
	public void requestNumSamples(ExperimentalConditionBean t) {
		// do nothing, no need to ask the server

	}

	@Override
	public ProjectStats<ExperimentalConditionBean> createProjectStats(ExperimentalConditionBean conditionBean) {
		return new ProjectStatsFromExperimentalCondition(conditionBean);
	}

}
