package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.util.SharedConstants;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;

public class SelectInputFilesPanel extends FlexTable {
	private final String question;
	private final String explanation;
	private final String buttonWidth = "100px";
	private final String buttonHeight = "40px";
	private WizardButton yesButton;
	private WizardButton noButton;
	private MultiUploader uploader;
	private ListBox formatCombo;
	private final PintContext context;

	public SelectInputFilesPanel(String question, String explanation, PintContext context) {
		this.context = context;
		this.question = question;
		this.explanation = explanation;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init();

	}

	private void init() {
		final Label label1 = new Label(question);
		label1.setStyleName(WizardStyles.WizardQuestionLabel);

		setWidget(0, 0, label1);
		formatCombo = new ListBox();
		formatCombo.setMultipleSelect(false);
		addFormats(formatCombo);
		setWidget(1, 0, formatCombo);

		uploader = new MultiUploader();
		uploader.addOnFinishUploadHandler(getOnFinishUploaderHandler());
		uploader.setEnabled(false); // until select the format

		setWidget(2, 0, uploader);

	}

	private void addFormats(ListBox formatCombo2) {
		formatCombo.clear();
		formatCombo.addItem("");

		for (final FileFormat format : FileFormat.values()) {
			formatCombo.addItem(format.getName(), format.name());
		}

	}

	/**
	 * Updates the ServletPath
	 */
	private void updateUploaderServletPath() {
		final String format = formatCombo.getValue(formatCombo.getSelectedIndex());

		final String path = "dataFilesProject.gupld?" + SharedConstants.JOB_ID_PARAM + "="
				+ context.getPintImportConfiguration().getImportID() + "&" + SharedConstants.FILE_ID_PARAM + "="
				+ getFileID() + "&" + SharedConstants.FILE_FORMAT + "=" + format;
		final String oldPath = uploader.getServletPath();
		if (!path.equals(oldPath)) {
			uploader.setServletPath(path);
		}

	}

	private String getFileID() {
		// TODO Auto-generated method stub
		return null;
	}

	private OnFinishUploaderHandler getOnFinishUploaderHandler() {
		final OnFinishUploaderHandler onfinish = new OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				final Status status = uploader.getStatus();
				if (status.equals(Status.SUCCESS)) {
					final UploadedInfo serverInfo = uploader.getServerInfo();
					final String fileNameUploaded = serverInfo.getFileName();
					GWT.log("File uploaded: " + fileNameUploaded);

				} else {
					StatusReportersRegister.getInstance().notifyStatusReporters(uploader.getServerRawResponse());
				}
			}
		};
		return onfinish;
	}

	public void addNoClickHandler(ClickHandler clickHandler) {
		noButton.addClickHandler(clickHandler);
	}

	public void addYesClickHandler(ClickHandler clickHandler) {
		yesButton.addClickHandler(clickHandler);

	}
}
