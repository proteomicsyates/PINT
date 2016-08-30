package edu.scripps.yates.client.gui.components.pseaquant;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ProteinRetrievalService;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantForm;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult;

public abstract class PSEAQuantFormAbstractPanel extends InitializableComposite implements PSEAQuantForm {
	protected final edu.scripps.yates.client.ProteinRetrievalServiceAsync service = GWT
			.create(ProteinRetrievalService.class);
	private MyDialogBox loadingDialog;
	private final SimplePanel resultscontainer = new SimplePanel();
	protected FlowPanel mainPanel;

	public PSEAQuantFormAbstractPanel() {
		mainPanel = new FlowPanel();
		initWidget(mainPanel);
	}

	/**
	 * @return the resultscontainer
	 */
	public SimplePanel getResultscontainer() {
		return resultscontainer;
	}

	public void sendPSEAQuantQuery() {
		showLoadingDialog("Sending PSEA-Quant request", true, true, true);
		if (resultscontainer != null)
			resultscontainer.clear();

		final List<PSEAQuantReplicate> replicates = getReplicates();
		if (replicates == null || replicates.size() < 2) {
			showError("You should select at least two replicates");
			return;
		}
		service.sendPSEAQuantQuery(getEmail(), getOrganism(), replicates, getRatioDescriptor(), getNumberOfSamplings(),
				getQuantType(), getAnnotationDatabase(), getCVTol(), getCVTolFactor(), getLiteratureBias(),
				new AsyncCallback<PSEAQuantResult>() {

					@Override
					public void onSuccess(PSEAQuantResult result) {
						loadingDialog.hide();
						WindowBox window = new WindowBox(true, true);
						window.setText("PSEA-Quant results");
						window.setAnimationEnabled(true);

						if (resultscontainer != null) {
							final PSEAQuantResultPanel pSEAQuantResultPanel = new PSEAQuantResultPanel(result,
									getEmail());
							resultscontainer.setWidget(pSEAQuantResultPanel);
						}
						final PSEAQuantResultPanel pSEAQuantResultPanel2 = new PSEAQuantResultPanel(result, getEmail());
						window.setWidget(pSEAQuantResultPanel2);
						window.center();
					}

					@Override
					public void onFailure(Throwable caught) {
						loadingDialog.hide();
						showError(caught.getMessage());
						if (resultscontainer != null) {
							resultscontainer.setWidget(getErrorWidget(caught.getMessage()));
						}
					}
				});
	}

	private void showError(String errorMessage) {
		WindowBox window = new WindowBox(false, true, true, true, true);
		window.setText("PSEA-Quant ERROR");
		window.setWidget(getErrorWidget(errorMessage));
		window.center();
	}

	private Widget getErrorWidget(String errorMessage) {

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("VerticalComponent");
		flowPanel.add(new HTML("Some error occurred when sending the query to PSEA-Quant:"));
		flowPanel.add(new HTML(errorMessage));
		return flowPanel;
	}

	protected void showLoadingDialog(String text, boolean autohide, boolean modal, boolean showBarProgress) {

		loadingDialog = new MyDialogBox(text, autohide, modal, showBarProgress, null);
		loadingDialog.center();

	}
}
