package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.InfoFromRemoteFilePanel.FileNameWithTypeProvider;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.HasId;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;

public class DataSourceDisclosurePanel extends ClosableWithTitlePanel
		implements ReferencesDataObject, FileNameWithTypeProvider {
	private final static String NO_FILE_UPLOADED_YET = "No file uploaded yet";
	private final static String FILE_UPLOADED_IN_SERVER = "File uploaded on server";
	protected static final String SELECT_A_SERVER_AND_THE_PATH = "File format, file name, server and relative path are needed";

	private DataSourceBean dataSource = new DataSourceBean();
	private final TextBox fileName;
	private final TextBox relativePath;
	private final ListBox serverRefCombo;
	private final ListBox formatCombo;
	private static int numDataSource = 1;

	private final Image roundLoader;
	private final Image greenTick;
	private final edu.scripps.yates.client.ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private KeyUpHandler hideGreenTickHandler;
	private KeyUpHandler returnHandler;
	private final SingleUploader uploader;
	private final Label statusLabel;
	protected boolean fileUploaded = false;
	private final RadioButton locatedInServerRadioButton;
	private final RadioButton uploadFileRadioButton;
	protected boolean fileSent;
	private final FastaDigestionPanel fastaDigestionPanel = new FastaDigestionPanel();
	private Button showFastaDigestionButton;
	private WindowBox windowBox;
	private Button checkAccessButton;

	public DataSourceDisclosurePanel(final String sessionID, int importJobID) {
		super(sessionID, importJobID, "Input data file " + numDataSource++, true);
		// fileFormat

		Label formatLabel = new Label("File format:");
		addWidget(formatLabel);
		FlowPanel horizPanel = new FlowPanel();
		formatCombo = new ListBox();
		formatCombo.setMultipleSelect(false);
		addFormats(formatCombo);
		horizPanel.add(formatCombo);
		addWidget(horizPanel);
		showFastaDigestionButton = new Button("show fasta digestion");
		showFastaDigestionButton.setHeight("19px");
		showFastaDigestionButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showFastaDigestionPanel();

			}
		});
		showFastaDigestionButton.setTitle("Click here to define how to digest the fasta database");
		showFastaDigestionButton.setVisible(false);
		horizPanel.add(showFastaDigestionButton);
		formatCombo.setVisibleItemCount(1);
		formatCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (formatCombo.getSelectedIndex() > 0) {
					final String value = formatCombo.getValue(formatCombo.getSelectedIndex());
					final FileFormat fileFormatFromString = FileFormat.getFileFormatFromString(value);
					if (fileFormatFromString != null) {
						// just put the name if no name has been written yet
						if ("".equals(fileName.getText())) {
							if (fileFormatFromString == FileFormat.DTA_SELECT_FILTER_TXT) {
								fileName.setText("DTASelect-filter.txt");
							} else if (fileFormatFromString == FileFormat.CENSUS_CHRO_XML) {
								fileName.setText("census_chro.xml");
							} else if (fileFormatFromString == FileFormat.CENSUS_OUT_TXT) {
								fileName.setText("census-out.txt");
							}
						}
						if (fileFormatFromString == FileFormat.FASTA) {
							showFastaDigestionButton(true);
						} else {
							showFastaDigestionButton(false);
						}
						// set upload path with the new format and enable
						// uploader if not server ref is selected
						updateUploaderServletPath();
						if (uploadFileRadioButton.getValue()) {
							uploader.setEnabled(true);
						}
					}
				} else {
					showFastaDigestionButton(false);
					uploader.setEnabled(false);
				}
				fireModificationEvent();
				registerFileNameWithTypeBeanOnServer();
			}
		});

		// fileName
		Label fileNameLabel = new Label("File name:");
		addWidget(fileNameLabel);
		fileName = new TextBox();
		fileName.setEnabled(false);
		fileName.setWidth("30" + Unit.EM.getType());
		fileName.addKeyUpHandler(getReturnHandler());
		fileName.addKeyUpHandler(getHideGreenTickHandler());
		fileName.addKeyUpHandler(getResetUploadedFileHandler());
		addWidget(fileName);

		String radioButtonGroupName = "sshserver or upload file" + hashCode();
		// server ref
		locatedInServerRadioButton = new RadioButton(radioButtonGroupName, "Located in server");
		locatedInServerRadioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			// enable or disable serverRefCombo and relativePath
			// according to this ratio value change
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				serverRefCombo.setEnabled(true);
				relativePath.setEnabled(true);
				fileName.setEnabled(true);
				uploader.setEnabled(false);
				checkAccessButton.setEnabled(true);
				statusLabel.setText(SELECT_A_SERVER_AND_THE_PATH);
				statusLabel.setStyleName("DataSourceDisclosurePanelRedLabel");
				registerFileNameWithTypeBeanOnServer();

				// if servercombo only have one server, select it
				// black + item1
				if (serverRefCombo.getItemCount() == 2) {
					serverRefCombo.setItemSelected(1, true);
				}
				fireModificationEvent();
			}
		});
		addWidget(locatedInServerRadioButton);
		serverRefCombo = new ListBox();
		serverRefCombo.setMultipleSelect(false);
		serverRefCombo.setVisibleItemCount(1);
		serverRefCombo.setEnabled(false);
		serverRefCombo.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setServerToDataSourceFromComboSelection();
				registerFileNameWithTypeBeanOnServer();
				fireModificationEvent();
			}
		});
		// register this as a listener of servers
		ProjectCreatorRegister.registerAsListenerByObjectClass(ServerTypeBean.class, this);
		addWidget(serverRefCombo);

		// relativePath
		Label relatvePathLabel = new Label("Relative path on server");
		addWidget(relatvePathLabel);
		relativePath = new TextBox();
		relativePath.setEnabled(false);
		relativePath.setWidth("30" + Unit.EM.getType());
		relativePath.addKeyUpHandler(getReturnHandler());
		relativePath.addKeyUpHandler(getHideGreenTickHandler());
		addWidget(relativePath);

		// check access to remote file
		HorizontalPanel checkPanel = new HorizontalPanel();
		checkPanel.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		checkAccessButton = new Button("Check access");
		checkAccessButton.setEnabled(false);
		checkAccessButton.setStyleName("horizontalComponent");
		checkPanel.add(checkAccessButton);
		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
		roundLoader = new Image(myClientBundle.roundedLoader());
		roundLoader.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		roundLoader.setVisible(false);

		addWidget(checkPanel);
		greenTick = new Image(myClientBundle.greenTick());
		greenTick.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		greenTick.setVisible(false);
		checkPanel.add(greenTick);
		greenTick.setSize("20px", "20px");

		addWidget(checkPanel);
		checkPanel.add(roundLoader);
		roundLoader.setSize("20px", "20px");

		checkPanel.setCellVerticalAlignment(checkAccessButton, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(greenTick, HasVerticalAlignment.ALIGN_MIDDLE);

		checkPanel.setCellVerticalAlignment(roundLoader, HasVerticalAlignment.ALIGN_MIDDLE);

		// add click handler to check the access
		checkAccessButton.addClickHandler(getCheckAccessClickHandler());

		// upload file
		uploadFileRadioButton = new RadioButton(radioButtonGroupName, "Upload local file");
		uploadFileRadioButton.setValue(true);
		uploadFileRadioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			// enable or disable the uploader according to the value of
			// this radio
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				// enable uploader only if a format is selected
				if (formatCombo.getSelectedIndex() > 0)
					uploader.setEnabled(true);
				serverRefCombo.setEnabled(false);
				relativePath.setEnabled(false);
				checkAccessButton.setEnabled(false);

				fileName.setEnabled(false);
				if (!fileUploaded) {
					statusLabel.setText(NO_FILE_UPLOADED_YET);
					statusLabel.setStyleName("DataSourceDisclosurePanelRedLabel");
				} else {
					statusLabel.setText(FILE_UPLOADED_IN_SERVER);
					statusLabel.setStyleName("DataSourceDisclosurePanelGreenLabel");
				}
				registerFileNameWithTypeBeanOnServer();
			}
		});
		addWidget(uploadFileRadioButton);

		uploader = new SingleUploader();
		uploader.addOnFinishUploadHandler(getOnFinishUploaderHandler());
		uploader.setEnabled(false); // until select the format
		addWidget(uploader);

		statusLabel = new Label(NO_FILE_UPLOADED_YET);
		statusLabel.setStyleName("DataSourceDisclosurePanelRedLabel");
		addWidget(statusLabel);

		addOnChangeIDTask(new ExecutableTaskOnChangeID() {

			@Override
			public void executeTask(final String oldID, final String newID) {
				if (oldID == null || newID == null || oldID.equals(newID))
					return;
				if (uploadFileRadioButton.getValue() && fileUploaded) {
					FileNameWithTypeBean oldFile = new FileNameWithTypeBean();
					FileFormat fileFormat = null;
					if (formatCombo.getSelectedIndex() > 0)
						fileFormat = FileFormat
								.getFileFormatFromString(formatCombo.getValue(formatCombo.getSelectedIndex()));
					oldFile.setFileFormat(fileFormat);
					oldFile.setId(oldID);
					oldFile.setFileName(fileName.getText());

					FileNameWithTypeBean newFile = new FileNameWithTypeBean();
					newFile.setFileFormat(fileFormat);
					newFile.setFileName(fileName.getText());
					newFile.setId(newID);

					service.moveDataFile(sessionID, DataSourceDisclosurePanel.this.importJobID, oldFile, newFile,
							new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable caught) {
									StatusReportersRegister.getInstance().notifyStatusReporters(caught);
									// keep the old id
									setId(oldID);
									updateUploaderServletPath();
									fireModificationEvent();
								}

								@Override
								public void onSuccess(Void result) {
									GWT.log("ID of the file changed on server");
									setId(newID);
									updateUploaderServletPath();
								}
							});
				}
			}
		});
		updateGUIReferringToDataObjects();
		updateRepresentedObject();

		fireModificationEvent();

	}

	private void showFastaDigestionPanel() {
		showDialog(fastaDigestionPanel, "FASTA digestion parameters");
	}

	private void showDialog(Widget widget, String title) {
		if (windowBox == null) {
			windowBox = new WindowBox(widget, title);
		} else {
			windowBox.setWidget(widget);
			windowBox.setText(title);
		}
		windowBox.center();
		windowBox.addCloseEventDoSomethingTask(new DoSomethingTask<Void>() {
			@Override
			public Void doSomething() {
				fireModificationEvent();
				return null;
			}
		});
	}

	private void showFastaDigestionButton(boolean visible) {
		showFastaDigestionButton.setVisible(visible);

	}

	private OnStartUploaderHandler getOnStartUploaderHandler() {
		OnStartUploaderHandler ret = new OnStartUploaderHandler() {

			@Override
			public void onStart(IUploader uploader) {
				updateUploaderServletPath();

			}
		};
		return ret;
	}

	/**
	 * Updates the ServletPath
	 */
	private void updateUploaderServletPath() {
		String format = formatCombo.getValue(formatCombo.getSelectedIndex());
		uploader.setServletPath("dataFilesProject.gupld?" + SharedConstants.JOB_ID_PARAM + "=" + importJobID + "&"
				+ SharedConstants.FILE_ID_PARAM + "=" + getID() + "&" + SharedConstants.FILE_FORMAT + "=" + format);

	}

	/**
	 * Sets fileUploaded to false and shows the message of NOT FILE UPLOADED YET
	 * if uploader is enabled
	 *
	 * @return
	 */
	private KeyUpHandler getResetUploadedFileHandler() {
		KeyUpHandler ret = new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				fileUploaded = false;
				if (uploadFileRadioButton.getValue() != null && uploadFileRadioButton.getValue()) {
					statusLabel.setText(NO_FILE_UPLOADED_YET);
					statusLabel.setStyleName("DataSourceDisclosurePanelRedLabel");
				}
			}
		};
		return ret;
	}

	/**
	 * Sets the name of the {@link DataSourceDisclosurePanel} as the name of the
	 * uploaded file
	 *
	 * @return
	 */
	private OnFinishUploaderHandler getOnFinishUploaderHandler() {
		OnFinishUploaderHandler onfinish = new OnFinishUploaderHandler() {
			@Override
			public void onFinish(IUploader uploader) {
				Status status = uploader.getStatus();
				if (status.equals(Status.SUCCESS)) {
					final UploadedInfo serverInfo = uploader.getServerInfo();
					final String fileNameUploaded = serverInfo.getFileName();
					fileName.setText(fileNameUploaded);
					// disableEdition();
					fileUploaded = true;
					statusLabel.setText(FILE_UPLOADED_IN_SERVER);
					statusLabel.setStyleName("DataSourceDisclosurePanelGreenLabel");
					fireModificationEvent();
					registerFileNameWithTypeBeanOnServer();
					checkAccessButton.setEnabled(true);

				}
			}
		};
		return onfinish;
	}

	/**
	 * Disable the edition of the panel, so the user can only change the ID and
	 * the format
	 */
	private void disableEdition() {
		fileName.setEnabled(false);
		relativePath.setEnabled(false);
		locatedInServerRadioButton.setEnabled(false);
		relativePath.setEnabled(false);
		serverRefCombo.setEnabled(false);
		uploadFileRadioButton.setEnabled(false);
		uploader.setEnabled(false);
	}

	/**
	 * {@link KeyUpHandler} that hides the greenTick image and also hides the
	 * error message
	 *
	 * @return
	 */
	private KeyUpHandler getReturnHandler() {
		if (returnHandler == null) {
			returnHandler = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					fireModificationEvent();
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						checkAccess();
					}
				}
			};
		}
		return returnHandler;
	}

	private void hideErrorMessage() {
		showMessage("");
	}

	private ClickHandler getCheckAccessClickHandler() {
		ClickHandler ret = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkAccess();

			}
		};
		return ret;
	}

	/**
	 * Check the access of the data source, being a remote file or a uploaded
	 * file in the server. If it is available, it is registered in the server
	 * for further processing of the file
	 */
	public void checkAccess() {
		roundLoader.setVisible(true);
		greenTick.setVisible(false);
		hideErrorMessage();
		updateGUIFromObjectData();

		if (locatedInServerRadioButton.getValue()) {
			RemoteFileWithTypeBean remoteFile = new RemoteFileWithTypeBean();
			remoteFile.setId(getID());
			remoteFile.setFileName(fileName.getText());
			remoteFile.setFileFormat(
					FileFormat.getFileFormatFromString(formatCombo.getValue(formatCombo.getSelectedIndex())));
			remoteFile.setServer(dataSource.getServer());
			remoteFile.setRemotePath(relativePath.getText());
			if (remoteFile.getFileFormat() == FileFormat.FASTA) {
				remoteFile.setFastaDigestionBean(fastaDigestionPanel.getObject());
			}
			service.checkRemoteFileInfoAccessibility(sessionID, remoteFile, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					roundLoader.setVisible(false);
					greenTick.setVisible(true);
					registerFileNameWithTypeBeanOnServer();
					updateHeaderColor();
					checkAccessButton.setEnabled(true);
				}

				@Override
				public void onFailure(Throwable caught) {
					roundLoader.setVisible(false);
					greenTick.setVisible(false);
					updateHeaderColor(false);
					setFileNotAccessibleStatus();
					checkAccessButton.setEnabled(true);
					StatusReportersRegister.getInstance().notifyStatusReporters(caught);

				}
			});
		} else if (uploadFileRadioButton.getValue()) {
			FileNameWithTypeBean remoteFile = new FileNameWithTypeBean();
			remoteFile.setFileName(fileName.getText());
			final String itemText = formatCombo.getItemText(formatCombo.getSelectedIndex());
			final FileFormat fileFormatFromString = FileFormat.getFileFormatFromString(itemText);
			remoteFile.setFileFormat(fileFormatFromString);
			if (remoteFile.getFileFormat() == FileFormat.FASTA) {
				remoteFile.setFastaDigestionBean(fastaDigestionPanel.getObject());
			}
			remoteFile.setId(getID());
			service.checkDataFileAvailability(sessionID, importJobID, remoteFile, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					roundLoader.setVisible(false);
					greenTick.setVisible(true);
					registerFileNameWithTypeBeanOnServer();
					updateHeaderColor();
				}

				@Override
				public void onFailure(Throwable caught) {
					roundLoader.setVisible(false);
					greenTick.setVisible(false);
					fileUploaded = false;
					updateHeaderColor(false);
					setFileNotAccessibleStatus();
					StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				}
			});
		}
	}

	private void setFileNotAccessibleStatus() {
		showMessage("File not accessible");
	}

	private void setFormatToDataSourceFromComboSelection() {
		final int selectedIndex = formatCombo.getSelectedIndex();
		if (selectedIndex >= 0) {
			final String selectedFormat = formatCombo.getValue(selectedIndex);
			final FileFormat fileFormatFromString = FileFormat.getFileFormatFromString(selectedFormat);
			dataSource.setFormat(fileFormatFromString);
		} else {
			dataSource.setFormat(null);
		}
	}

	/**
	 * Ask to the service for the available formats and add them to the
	 * formatCombo
	 *
	 * @param formatCombo2
	 */
	private void addFormats(ListBox formatCombo2) {
		formatCombo.addItem("");

		for (FileFormat format : FileFormat.values()) {
			formatCombo.addItem(format.getName(), format.name());
		}

	}

	private void setServerToDataSourceFromComboSelection() {
		final int selectedIndex = serverRefCombo.getSelectedIndex();
		if (serverRefCombo.isEnabled() && selectedIndex > 0) {
			final int serverID = Integer.valueOf(serverRefCombo.getValue(selectedIndex));
			final RepresentsDataObject projectObject = ProjectCreatorRegister.getProjectObjectRepresenter(serverID);
			if (projectObject != null) {
				final ServerTypeBean serverBean = (ServerTypeBean) projectObject.getObject();
				dataSource.setServer(serverBean);
			}
		} else {
			dataSource.setServer(null);
		}
	}

	@Override
	public DataSourceBean getObject() {
		// updateRepresentedObject();
		return dataSource;
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof DataSourceBean) {
			dataSource = (DataSourceBean) object;
			fastaDigestionPanel.updateGUIFromObjectData(dataSource.getFastaDigestionBean());
			updateGUIFromObjectData();
		}
	}

	@Override
	public void updateGUIFromObjectData() {
		// set name
		setId(dataSource.getId());
		fileName.setText(dataSource.getFileName());
		relativePath.setText(dataSource.getRelativePath());
		if (dataSource.getFormat() != null) {
			ProjectCreatorWizardUtil.selectInCombo(formatCombo, dataSource.getFormat().name());
			if (dataSource.getFormat() == FileFormat.FASTA) {
				showFastaDigestionButton.setVisible(true);
				fastaDigestionPanel.updateGUIFromObjectData(dataSource.getFastaDigestionBean());
			}
		} else {
			formatCombo.setSelectedIndex(0);
		}
		// server ref
		if (dataSource.getServer() != null) {
			ProjectCreatorWizardUtil.selectInCombo(serverRefCombo, dataSource.getServer().getId());
			locatedInServerRadioButton.setValue(true);
			serverRefCombo.setEnabled(true);
			relativePath.setEnabled(true);
			uploader.setEnabled(false);
			fileUploaded = false;
		} else {
			uploadFileRadioButton.setValue(true);
			fileUploaded = true;
		}
		if (dataSource.getUrl() != null) {
			fileUploaded = true;
			statusLabel.setText(DataSourceDisclosurePanel.FILE_UPLOADED_IN_SERVER);
			statusLabel.setStyleName("DataSourceDisclosurePanelGreenLabel");
			uploadFileRadioButton.setValue(true);
			uploader.setEnabled(true);
			serverRefCombo.setEnabled(false);
			relativePath.setEnabled(false);

		}
		updateUploaderServletPath();
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		// update server combo
		ProjectCreatorWizardUtil.updateCombo(serverRefCombo, ServerTypeBean.class);
		updateUploaderServletPath();

	}

	@Override
	public void updateRepresentedObject() {
		dataSource.setId(getID());
		if (!"".equals(fileName.getText()))
			dataSource.setFileName(fileName.getText());
		else
			dataSource.setFileName(null);
		if (relativePath.isEnabled()) {
			dataSource.setRelativePath(relativePath.getText());
		} else {
			dataSource.setRelativePath(null);
		}
		if (fileUploaded && uploadFileRadioButton.getValue()) {
			dataSource.setUrl(fileName.getText());
		}
		setServerToDataSourceFromComboSelection();
		setFormatToDataSourceFromComboSelection();
		if (dataSource.getFormat() == FileFormat.FASTA) {
			dataSource.setFastaDigestionBean(fastaDigestionPanel.getObject());
		} else {
			dataSource.setFastaDigestionBean(null);
		}
	}

	private void registerFileNameWithTypeBeanOnServer() {
		// send to the server if it is valid
		if (isValid()) {
			final FileNameWithTypeBean fileNameWithTypeBean = getFileNameWithTypeBean();
			if (fileNameWithTypeBean != null) {
				service.addDataFile(sessionID, importJobID, fileNameWithTypeBean, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						fileUploaded = false;
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						statusLabel.setText("File not accessible");
						statusLabel.setStyleName("DataSourceDisclosurePanelLabel");
						updateHeaderColor(false);
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
			}
		}
	}

	/**
	 * {@link KeyUpHandler} that hides the greenTick image and also hides the
	 * error message
	 *
	 * @return
	 */
	private KeyUpHandler getHideGreenTickHandler() {
		if (hideGreenTickHandler == null) {
			hideGreenTickHandler = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					greenTick.setVisible(false);
					hideErrorMessage();
				}
			};
		}
		return hideGreenTickHandler;
	}

	/**
	 * Return a {@link FileNameWithTypeBean}. <br>
	 * <b> Note that the returned object can also be an instance of
	 * {@link RemoteFileWithTypeBean} in case of being a remote file
	 *
	 * @return
	 */
	@Override
	public FileNameWithTypeBean getFileNameWithTypeBean() {
		// updateRepresentedObject();
		updateGUIFromObjectData();
		if (uploadFileRadioButton.getValue() && fileUploaded) {
			FileNameWithTypeBean ret = new FileNameWithTypeBean();
			ret.setFileName(dataSource.getFileName());
			ret.setFileFormat(dataSource.getFormat());
			if (dataSource.getFormat() == FileFormat.FASTA) {
				ret.setFastaDigestionBean(fastaDigestionPanel.getObject());
			}
			ret.setId(dataSource.getId());
			if (ret.isValid())
				return ret;
		} else if (locatedInServerRadioButton.getValue()) {
			RemoteFileWithTypeBean ret = new RemoteFileWithTypeBean();
			ret.setFileName(dataSource.getFileName());
			ret.setFileFormat(dataSource.getFormat());
			ret.setServer(dataSource.getServer());
			ret.setRemotePath(dataSource.getRelativePath());
			ret.setId(dataSource.getId());
			if (dataSource.getFormat() == FileFormat.FASTA) {
				ret.setFastaDigestionBean(fastaDigestionPanel.getObject());
			}
			// check if valid
			if (ret.isValid())
				return ret;
		}

		return null;
	}

	/**
	 * Decrease the counter and then call to editableDisclosurePanel.close()
	 */
	@Override
	public void close() {
		super.close();
	}

	@Override
	protected boolean isValid() {
		updateRepresentedObject();
		final DataSourceBean dataSource = getObject();
		if (dataSource.getId() == null || "".equals(dataSource.getId()))
			return false;
		if (dataSource.getFileName() == null || "".equals(dataSource.getFileName()))
			return false;
		if (dataSource.getFormat() == null)
			return false;
		if (locatedInServerRadioButton.getValue()) {
			if (serverRefCombo.getSelectedIndex() <= 0)
				return false;
			if (dataSource.getRelativePath() == null || "".equals(dataSource.getRelativePath()))
				return false;
			if (!dataSource.getServer().isValid())
				return false;
		} else if (uploadFileRadioButton.getValue()) {
			if (!fileUploaded)
				return false;
		}

		statusLabel.setText("Data source is OK");
		statusLabel.setStyleName("DataSourceDisclosurePanelGreenLabel");
		return true;
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	public void showMessage(String message) {
		statusLabel.setText(message);
		statusLabel.setStyleName("DataSourceDisclosurePanelRedLabel");

	}

	public void showErrorMessage(Throwable throwable) {
		showMessage(throwable.getMessage());

	}

}
