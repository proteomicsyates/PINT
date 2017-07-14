package edu.scripps.yates.client.statusreporter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import edu.scripps.yates.client.gui.PopUpPanelYesNo;

public class StatusReporterImpl implements StatusReporter {
	private String statusTitle;
	private String errorTitle;

	public StatusReporterImpl() {
		this("Status:", "Error occurred:");
	}

	public StatusReporterImpl(String statusTitle, String errorTitle) {
		this.statusTitle = statusTitle;
		this.errorTitle = errorTitle;
	}

	@Override
	public void showMessage(String message) {
		// show pop up dialog
		PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, statusTitle, message, "OK", null);
		popUpDialog.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpDialog.hide();

			}
		});
		popUpDialog.show();
	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		// show pop up dialog
		PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, errorTitle, throwable.getMessage(), "OK",
				null);
		popUpDialog.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpDialog.hide();

			}
		});
		popUpDialog.show();

	}

}
