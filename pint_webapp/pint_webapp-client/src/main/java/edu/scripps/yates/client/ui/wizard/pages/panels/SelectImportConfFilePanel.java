package edu.scripps.yates.client.ui.wizard.pages.panels;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;

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
				final String id = uploadedFile.getId();
				cancelButtons.get(id).removeFromParent();
//				// start other upload?
//				uploader.startUpload();
				// update object with no format

				service.getPintImportCfgTypeBeanByProcessKey(getImportProcessKey(),
						new AsyncCallback<PintImportCfgBean>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
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
				"/newFileUpload?" + SharedConstants.FILE_FORMAT + "=" + SharedConstants.IMPORT_CFG_FILE_TYPE + "&"
						+ SharedConstants.IMPORT_CFG_FILE_KEY + "=" + String.valueOf(getImportProcessKey()));
	}

}
