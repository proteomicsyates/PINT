package edu.scripps.yates.client.gui.components.projectItems;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.NumberLabel;

import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;
import edu.scripps.yates.shared.model.projectStats.ProjectStatsFromMSRun;

public class ProjectStatsFromMSRunItemPanel extends AbstractProjectStatsItemPanel<MSRunBean> {
	private static ProjectStatsFromMSRunItemPanel instance;

	public static ProjectStatsFromMSRunItemPanel getInstance(MSRunBean t) {
		if (instance == null) {
			instance = new ProjectStatsFromMSRunItemPanel(t);
		} else {
			instance.updateParent(t);
		}
		return instance;
	}

	private ProjectStatsFromMSRunItemPanel(MSRunBean msRunBean) {
		super("Condition '" + msRunBean.getId() + "'", msRunBean);
	}

	@Override
	public void requestNumGenes(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowGenes, 1, imageLoader);
		super.proteinRetrievingService.getNumGenes(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumGenes(result);
						Label numGenesLabel = new Label(format.format(result));
						numGenesLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
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
	public void requestNumPSMs(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPSMs, 1, imageLoader);
		super.proteinRetrievingService.getNumPSMs(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumPSMs(result);
						Label numPSMsLabel = new Label(format.format(result));
						numPSMsLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
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
	public void requestNumPeptides(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowPeptides, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentPeptides(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumPeptides(result);
						Label numPeptidesLabel = new Label(format.format(result));
						numPeptidesLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
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
	public void requestNumProteins(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowProteins, 1, imageLoader);
		super.proteinRetrievingService.getNumDifferentProteins(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumProteins(result);
						Label numProteinsLabel = new Label(format.format(result));
						numProteinsLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
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
	public void requestNumMSRuns(final MSRunBean msRunBean) {

		final NumberLabel<Integer> label = new NumberLabel<Integer>();
		label.setValue(1);
		label.setStyleName("no-wrap");
		rightPanel.setWidget(rowMSRuns, 1, label);

	}

	@Override
	public void afterUpdateParent(MSRunBean parent) {
		setCaption("MS run '" + parent.getId() + "'");
	}

	@Override
	public void requestNumConditions(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowConditions, 1, imageLoader);
		super.proteinRetrievingService.getNumConditions(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumConditions(result);
						Label numConditionsLabel = new Label(format.format(result));
						numConditionsLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
							rightPanel.setWidget(rowConditions, 1, numConditionsLabel);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						final Label widget = new Label("-");
						widget.setTitle(caught.getMessage());
						rightPanel.setWidget(rowConditions, 1, widget);
					}
				});

	}

	@Override
	public void requestNumSamples(final MSRunBean msRunBean) {
		final ImageResource smallLoader = clientBundle.smallLoader();
		Image imageLoader = new Image(smallLoader);
		rightPanel.setWidget(rowSamples, 1, imageLoader);
		super.proteinRetrievingService.getNumSamples(msRunBean.getProject().getTag(), msRunBean,
				new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						projectStatsMap.get(msRunBean).setNumSamples(result);
						Label numSamplesLabel = new Label(format.format(result));
						numSamplesLabel.setStyleName("no-wrap");
						if (msRunBean.equals(selectedItem.getT())) {
							rightPanel.setWidget(rowSamples, 1, numSamplesLabel);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						final Label widget = new Label("-");
						widget.setTitle(caught.getMessage());
						rightPanel.setWidget(rowSamples, 1, widget);
					}
				});

	}

	@Override
	public ProjectStats<MSRunBean> createProjectStats(MSRunBean msRunBean) {
		return new ProjectStatsFromMSRun(msRunBean);
	}

}
