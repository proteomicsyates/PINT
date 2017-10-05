package edu.scripps.yates.client.statusreporter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

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
		String message = throwable.getLocalizedMessage();
		final String exceptionString = "Exception:";
		if (message.contains(exceptionString)) {
			message = message.substring(message.lastIndexOf(exceptionString) + exceptionString.length()).trim();
		}
		// show pop up dialog
		PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, errorTitle, message, "OK", null);
		popUpDialog.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpDialog.hide();

			}
		});
		// align message to left
		popUpDialog.alignMessage(HasHorizontalAlignment.ALIGN_LEFT);
		popUpDialog.show();

	}

	@Override
	public String getStatusReporterKey() {
		return this.getClass().getName();
	}

}
