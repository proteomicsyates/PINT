package edu.scripps.yates.client.gui.components.projectItems;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsFromSample;

public class ProjectStatsFromSampleItemPanel extends AbstractProjectStatsItemPanel<SampleBean> {
	private static ProjectStatsFromSampleItemPanel instance;

	public static ProjectStatsFromSampleItemPanel getInstance(SampleBean t) {
		if (instance == null) {
			instance = new ProjectStatsFromSampleItemPanel(t);
		} else {
			instance.updateParent(t);
		}
		return instance;
	}

	private ProjectStatsFromSampleItemPanel(SampleBean sampleBean) {
		super("Condition '" + sampleBean.getId() + "'", sampleBean);
	}

	@Override
	public void requestNumGenes(final SampleBean sampleBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowGenes, 1, imageLoader);
		super.proteinRetrievingService.getNumGenes(sampleBean.getProject().getTag(), sampleBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(sampleBean).setNumGenes(result);
						Label numGenesLabel = new Label(format.format(result));
						if (sampleBean.equals(selectedItem.getT())) {
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
	public void requestNumPSMs(final SampleBean sampleBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPSMs, 1, imageLoader);
		super.proteinRetrievingService.getNumPSMs(sampleBean.getProject().getTag(), sampleBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(sampleBean).setNumPSMs(result);
						Label numPSMsLabel = new Label(format.format(result));
						if (sampleBean.equals(selectedItem.getT())) {
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
	public void requestNumPeptides(final SampleBean sampleBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPeptides, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentPeptides(sampleBean.getProject().getTag(), sampleBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(sampleBean).setNumPeptides(result);
						Label numPeptidesLabel = new Label(format.format(result));
						if (sampleBean.equals(selectedItem.getT())) {
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
	public void requestNumProteins(final SampleBean sampleBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowProteins, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentProteins(sampleBean.getProject().getTag(), sampleBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(sampleBean).setNumProteins(result);
						Label numProteinsLabel = new Label(format.format(result));
						if (sampleBean.equals(selectedItem.getT())) {
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
	public void requestNumMSRuns(final SampleBean sampleBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowMSRuns, 1, imageLoader);
		super.proteinRetrievingService.getNumMSRuns(sampleBean.getProject().getTag(), sampleBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(sampleBean).setNumMSRuns(result);
						Label numMSRunsLabel = new Label(format.format(result));
						if (sampleBean.equals(selectedItem.getT())) {
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
	public void afterUpdateParent(SampleBean parent) {
		setCaption("Sample '" + parent.getId() + "'");
	}

	@Override
	public void requestNumConditions(SampleBean t) {
		// do nothing, no need to ask the server

	}

	@Override
	public void requestNumSamples(SampleBean t) {
		// do nothing, no need to ask the server

	}

	@Override
	public ProjectStats<SampleBean> createProjectStats(SampleBean sampleBean) {
		return new ProjectStatsFromSample(sampleBean);
	}

}
