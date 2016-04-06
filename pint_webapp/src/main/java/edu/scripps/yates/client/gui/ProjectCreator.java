package edu.scripps.yates.client.gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.ProjectSaverServiceAsync;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.history.TargetHistory;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.IUploader.ServerMessage;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;

public class ProjectCreator extends Composite {

	private MultiUploader dataFilesUploader;
	private MultiUploader xmlProjectUploader;

	private final Set<String> uploadedDataFiles = new HashSet<String>();
	private final List<String> uploadedProjectFiles = new ArrayList<String>();

	private final Label projectXMLlabel;
	private final Label dataFilesLabel;
	private final Button btnCreateSannyProject;
	private final NavigationHorizontalPanel navigation;
	private final String sessionID;

	public ProjectCreator(String sessionID) {
		this.sessionID = sessionID;
		FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);

		HeaderPanel header = new HeaderPanel();
		mainPanel.add(header);

		navigation = new NavigationHorizontalPanel(TargetHistory.SUBMIT);
		mainPanel.add(navigation);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStyleName("verticalComponent");
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setSpacing(10);
		mainPanel.add(verticalPanel);
		verticalPanel.setSize("100%", "100%");

		Label lblThisPageAllow = new Label(
				"This page allow to upload the files required to create a new project in the PINT web application");
		lblThisPageAllow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(lblThisPageAllow);

		DisclosurePanel disclosurePanel = new DisclosurePanel("Data Files");
		disclosurePanel.setOpen(true);
		verticalPanel.add(disclosurePanel);
		disclosurePanel.setSize("100%", "30%");

		VerticalPanel verticalPanelDataFiles = new VerticalPanel();
		VerticalPanel verticalPanelProjectFile = new VerticalPanel();

		verticalPanelDataFiles.setSpacing(10);
		disclosurePanel.setContent(verticalPanelDataFiles);
		verticalPanelDataFiles.setSize("100%", "100%");

		Label lblUploadDataFiles = new Label("Upload data files:");
		verticalPanelDataFiles.add(lblUploadDataFiles);

		dataFilesLabel = new Label("");
		verticalPanelDataFiles.add(dataFilesLabel);
		dataFilesLabel.setHeight("2EM");

		DisclosurePanel disclosurePanel_1 = new DisclosurePanel("Project XML file");
		disclosurePanel_1.setOpen(true);
		verticalPanel.add(disclosurePanel_1);
		disclosurePanel_1.setSize("100%", "30%");

		verticalPanelProjectFile.setSpacing(10);
		disclosurePanel_1.setContent(verticalPanelProjectFile);
		verticalPanelProjectFile.setSize("100%", "100%");

		Label lblUploadProjectXml = new Label("Upload project XML file:");
		verticalPanelProjectFile.add(lblUploadProjectXml);

		projectXMLlabel = new Label("");
		verticalPanelProjectFile.add(projectXMLlabel);
		projectXMLlabel.setHeight("2EM");

		DisclosurePanel disclosurePanel_2 = new DisclosurePanel("Create project");
		disclosurePanel_2.setOpen(true);
		verticalPanel.add(disclosurePanel_2);
		disclosurePanel_2.setSize("100%", "30%");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSpacing(10);
		disclosurePanel_2.setContent(verticalPanel_1);
		verticalPanel_1.setSize("100%", "100%");

		lblStatus = new Label("No files uploaded");
		verticalPanel_1.add(lblStatus);
		lblStatus.setWidth("100%");

		btnCreateProject = new Button("Create project");
		btnCreateProject.setEnabled(false);
		btnCreateProject.addClickHandler(getCreateProjectClickHandler());
		verticalPanel_1.add(btnCreateProject);

		// ///////////////////////////////////////////////////////
		// THESE LINES SHOULD BE REMOVED ONCE THE PROJECT IS CREATED
		btnCreateSannyProject = new Button("Create Sanny project");
		btnCreateSannyProject.addClickHandler(getCreateSannyProjectClickHandler());
		// verticalPanel_1.add(btnCreateSannyProject);
		// ///////////////////////////////////////////////////////

		loadUploaders(verticalPanelDataFiles, verticalPanelProjectFile);
	}

	private ClickHandler getCreateProjectClickHandler() {
		ClickHandler ret = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String plural = uploadedProjectFiles.size() > 1 ? "s" : "";
				showLoadingDialog("Creating project" + plural + ". This can take several minutes.\nPlease wait...");
				ProjectSaverServiceAsync.Util.getInstance().saveProject(sessionID, uploadedProjectFiles,
						new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								hiddeLoadingDialog();

								Window.alert("PROJECT CREATED");
								navigation.updateLink(true);
							}

							@Override
							public void onFailure(Throwable caught) {
								hiddeLoadingDialog();
								Window.alert("ERROR CREATING THE PROJECT: " + caught.getMessage());

							}
						});

			}
		};
		return ret;
	}

	private ClickHandler getCreateSannyProjectClickHandler() {
		ClickHandler ret = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				showLoadingDialog("Creating Sanny project. Please wait...");
				// ProjectSaver.Util.getInstance().saveSannyProject(sessionID,
				// new AsyncCallback<Void>() {
				//
				// @Override
				// public void onSuccess(Void result) {
				// hiddeLoadingDialog();
				//
				// Window.alert("sanny PROJECT CREATED");
				// navigation.updateLink(true);
				// }
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// hiddeLoadingDialog();
				// Window.alert("ERROR CREATING THE PROJECT: "
				// + caught.getMessage());
				//
				// }
				// });

			}
		};
		return ret;
	}

	private void loadUploaders(VerticalPanel verticalPanelDataFiles, VerticalPanel verticalPanelProjectFile) {
		// add Multiple upload to verticalPanelDataFiles
		dataFilesUploader = new MultiUploader();
		// Add a finish handler which will load the image once the upload
		// finishes
		dataFilesUploader.addOnFinishUploadHandler(onFinishDataFilesUploaderHandler);
		dataFilesUploader.setServletPath("dataFilesProject.gupld");
		dataFilesUploader.addOnCancelUploadHandler(onCancelDataFilesUploaderHandler);
		dataFilesUploader.addOnStartUploadHandler(onStartUploaderHandler);
		verticalPanelDataFiles.add(dataFilesUploader);

		// add Single upload to verticalPanelProjectFile
		xmlProjectUploader = new MultiUploader();
		// Add a finish handler which will load the image once the upload
		// finishes
		xmlProjectUploader.addOnFinishUploadHandler(onFinishxmlProjectUploaderHandler);
		xmlProjectUploader.addOnCancelUploadHandler(onCancelXmlProjectUploaderHandler);
		xmlProjectUploader.setServletPath("xmlProject.gupld");
		verticalPanelProjectFile.add(xmlProjectUploader);
	}

	private final IUploader.OnFinishUploaderHandler onFinishxmlProjectUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				// new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by
				// default
				UploadedInfo info = uploader.getServerInfo();
				uploadedProjectFiles.addAll(uploader.getFileInput().getFilenames());
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				final ServerMessage serverMessage = uploader.getServerMessage();
				System.out.println("Server message " + serverMessage.getMessage());
				projectXMLlabel.setText(serverMessage.getMessage() + " uploaded correctly");
				updateStatus();
			}
		}
	};
	private final IUploader.OnFinishUploaderHandler onFinishDataFilesUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		@Override
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				// new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by
				// default
				UploadedInfo info = uploader.getServerInfo();
				uploadedDataFiles.addAll(uploader.getFileInput().getFilenames());
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				final ServerMessage serverMessage = uploader.getServerMessage();
				System.out.println("Server message " + serverMessage.getMessage());
				dataFilesLabel.setText(serverMessage.getMessage());
				updateStatus();
			}
		}
	};

	private final IUploader.OnCancelUploaderHandler onCancelDataFilesUploaderHandler = new IUploader.OnCancelUploaderHandler() {

		@Override
		public void onCancel(IUploader uploader) {
			System.out.println("CANCEL data files upload");
			final List<String> filenames = uploader.getFileInput().getFilenames();
			for (String fileName : filenames) {
				final boolean removed = uploadedDataFiles.remove(fileName);
				if (removed)
					updateStatus();
			}

			// clear label
			dataFilesLabel.setText("");
		}
	};

	private final IUploader.OnCancelUploaderHandler onCancelXmlProjectUploaderHandler = new IUploader.OnCancelUploaderHandler() {

		@Override
		public void onCancel(IUploader uploader) {
			System.out.println("CANCEL xml project upload " + uploader.getInputName());
			final List<String> filenames = uploader.getFileInput().getFilenames();
			for (String fileName : filenames) {
				final boolean removed = uploadedProjectFiles.remove(fileName);
				if (removed)
					updateStatus();
			}

			// clear label
			projectXMLlabel.setText("");
		}
	};

	private final OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {

		@Override
		public void onStart(IUploader uploader) {
			// TODO

		}
	};
	private final Label lblStatus;
	private final Button btnCreateProject;
	private MyDialogBox loadingDialog;

	/**
	 * Update the status of the label summarizing the number of files uploaded,
	 * and also enabling or disabling the create project button depending on
	 * whether the files are uploaded or not
	 */
	protected void updateStatus() {
		if (uploadedDataFiles.isEmpty() && uploadedProjectFiles.isEmpty()) {
			lblStatus.setText("No files uploaded");
			btnCreateProject.setEnabled(false);
		} else {
			String numDataFilesString = "No";
			if (!uploadedDataFiles.isEmpty())
				numDataFilesString = String.valueOf(uploadedDataFiles.size());
			String numXmlProjectFilesString = "no";
			if (!uploadedProjectFiles.isEmpty())
				numXmlProjectFilesString = String.valueOf(uploadedProjectFiles.size());
			lblStatus.setText(
					numDataFilesString + " data files uploaded and " + numXmlProjectFilesString + " project file.");

			if (!uploadedProjectFiles.isEmpty())
				btnCreateProject.setEnabled(true);
			else
				btnCreateProject.setEnabled(false);
		}
	}

	private void showLoadingDialog(String text) {

		loadingDialog = MyDialogBox.getInstance(text, false, false);
		loadingDialog.center();
	}

	private void hiddeLoadingDialog() {
		if (loadingDialog != null)
			loadingDialog.hide();
	}
}
