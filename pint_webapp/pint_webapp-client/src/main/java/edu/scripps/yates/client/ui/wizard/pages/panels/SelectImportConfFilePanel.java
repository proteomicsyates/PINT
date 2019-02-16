package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.HashSet;
import java.util.Set;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class SelectImportConfFilePanel extends NewSelectInputFilesPanel {

	private Integer importProcessKey = null;
	private final Set<String> errors = new HashSet<String>();

	public SelectImportConfFilePanel(Wizard<PintContext> wizard) {
		super(wizard);
		setMessageText("Click here to upload a previously saved import dataset session file");
		setButtonWidth(410);

	}

	private int getImportProcessKey() {
		if (importProcessKey == null) {
			importProcessKey = hashCode();
		}
		return importProcessKey;
	}

	@Override
	public UploadCompleteHandler getUploadCompleteHandler() {
		final UploadCompleteHandler uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				// remove cancel button
				final File uploadedFile = uploadCompleteEvent.getFile();
				if (uploadedFile == null) {
					return false;
				}
				final String id = uploadedFile.getId();

				cancelButtons.get(id).removeFromParent();
				if (errors.contains(id)) {
					errors.remove(id);
					return true;
				}

//				// start other upload?
//				uploader.startUpload();
				// update object with no format

				service.getPintImportCfgTypeBeanByProcessKey(getImportProcessKey(),
						new AsyncCallback<PintImportCfgBean>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(
										"Error reading uploaded file. Please check that you are using the correct format");
							}

							@Override
							public void onSuccess(PintImportCfgBean result) {
								wizard.getContext().setPintImportConfiguration(result);
								wizard.showNextPage();
							}
						});

				// remove with delay the uploading row
				final Timer timer = new Timer() {

					@Override
					public void run() {
						final HorizontalPanel horizontalPanel = uploaderRows.get(id);
						if (horizontalPanel != null) {
							horizontalPanel.removeFromParent();
						}
					}
				};
				timer.schedule(3000);// after 3 sc.

				return true;
			}
		};
		return uploadCompleteHandler;
	}

	@Override
	public void setUploadURL() {
		// set url for servlet in the server

		uploader.setUploadURL(
				"/pint/newFileUpload?" + SharedConstants.FILE_FORMAT + "=" + SharedConstants.IMPORT_CFG_FILE_TYPE + "&"
						+ SharedConstants.IMPORT_CFG_FILE_KEY + "=" + String.valueOf(getImportProcessKey()));
	}

	@Override
	protected UploadErrorHandler getUploadErrorHandler() {
		return new UploadErrorHandler() {

			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				errors.add(uploadErrorEvent.getFile().getId());
				cancelButtons.get(uploadErrorEvent.getFile().getId()).removeFromParent();
				StatusReportersRegister.getInstance().notifyStatusReporters(getErrorMessage(uploadErrorEvent));
				return true;
			}
		};
	}

	@Override
	protected FileQueueErrorHandler getFileQueueErrorHandler() {
		return new FileQueueErrorHandler() {
			@Override
			public boolean onFileQueueError(FileQueueErrorEvent fileQueueErrorEvent) {
				errors.add(fileQueueErrorEvent.getFile().getId());
				StatusReportersRegister.getInstance().notifyStatusReporters(getErrorMessage(fileQueueErrorEvent));
				return true;
			}
		};
	}

	private String getErrorMessage(UploadErrorEvent erroEvent) {

		return "Upload error event for file '" + erroEvent.getFile().getName() + "'.\n"
				+ "Check that your file is an XML file with the appropiate format.\n"
				+ "Are you sure you got that file from a previous import dataset session?";

	}

	private String getErrorMessage(FileQueueErrorEvent fileQueueErrorEvent) {
		return "File queue error event for file '" + fileQueueErrorEvent.getFile().getName() + "'.\n"
				+ "Check that your file is an XML file with the appropiate format.\n"
				+ "Are you sure you got that file from a previous import dataset session?";

	}
}
