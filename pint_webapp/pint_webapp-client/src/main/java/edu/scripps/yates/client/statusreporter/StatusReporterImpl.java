package edu.scripps.yates.client.statusreporter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import edu.scripps.yates.client.gui.PopUpPanelYesNo;

public class StatusReporterImpl implements StatusReporter {
	private final String statusTitle;
	private final String errorTitle;

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
		final PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, statusTitle, message, "OK", null);
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
		GWT.log("Error captured", throwable);
		String message = throwable.getLocalizedMessage();
		if (message == null) {
			message = throwable.getMessage();
		}
		if (message == null) {
			if (throwable.getCause() != null) {
				message = throwable.getCause().getLocalizedMessage();
				if (message == null) {
					message = throwable.getCause().getMessage();
				}
			}

		}

		if (message == null) {
			// do not show it to the user
			return;
		}
		final String exceptionString = "Exception:";
		if (message.contains(exceptionString)) {
			message = message.substring(message.lastIndexOf(exceptionString) + exceptionString.length()).trim();
		}
		message = new SafeHtmlBuilder().appendHtmlConstant(message).toSafeHtml().toString();
		// show pop up dialog
		final PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, errorTitle, message, "OK", null);
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
