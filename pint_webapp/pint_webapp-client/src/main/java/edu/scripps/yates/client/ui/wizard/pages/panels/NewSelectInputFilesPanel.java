package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogStartHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueueErrorHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.ProgressBar;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class NewSelectInputFilesPanel extends FlexTable {

	protected Uploader uploader;
	protected final PintContext context;
	private final Map<String, ProgressBar> progressBars = new LinkedHashMap<String, ProgressBar>();
	protected final Map<String, Image> cancelButtons = new LinkedHashMap<String, Image>();
	protected final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private VerticalPanel uploadedFilesVerticalPanel;
	private final Set<FileTypeBean> uploadedFiles = new HashSet<FileTypeBean>();
	protected final Map<String, HorizontalPanel> uploaderRows = new HashMap<String, HorizontalPanel>();
	protected final Wizard<PintContext> wizard;

	public NewSelectInputFilesPanel(Wizard<PintContext> wizard) {
		this.context = wizard.getContext();
		this.wizard = wizard;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init();

	}

	/**
	 * Sets a different text than "Click here to upload one or more files"
	 * 
	 * @param text
	 */
	public void setMessageText(String text) {
		uploader.setButtonText("<span class=\"linkText\">" + text + "</span>");
	}

	public void setButtonWidth(int width) {
		uploader.setButtonWidth(width);
	}

	private void init() {
		final VerticalPanel progressBarPanel = new VerticalPanel();
		progressBarPanel.setStyleName(WizardStyles.PROGRESSBAR_TABLE);

		uploader = new Uploader();
		final UploadCompleteHandler uploadCompleteHandler = getUploadCompleteHandler();
		uploader.setButtonText("<span class=\"linkText\">Click here to upload one or more files</span>")//
				.setButtonTextStyle(
						".linkText{font-size: 14px; color: #BB4B44;} .linkText:hover{ font-size: 14px;  color: #000000;}")//
				.setButtonWidth(250)//
				.setButtonHeight(22)//
				.setButtonCursor(Uploader.Cursor.HAND)//
				.setButtonAction(Uploader.ButtonAction.SELECT_FILES)//

				.setFileSizeLimit("2 GB") //
				.setFileQueuedHandler(new FileQueuedHandler() {
					@Override
					public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
						// Create a Progress Bar for this file
						final ProgressBar progressBar = new ProgressBar(0.0, 1.0, 0.0,
								new CancelProgressBarTextFormatter());
						progressBar.setTitle(fileQueuedEvent.getFile().getName());
						progressBar.setHeight("18px");
						progressBar.setWidth("200px");
						progressBars.put(fileQueuedEvent.getFile().getId(), progressBar);

						// Add Cancel Button Image
						final Image cancelButton = new Image(GWT.getModuleBaseURL() + "wizard/icons/cancel.png");
						cancelButton.setStyleName("cancelButton");
						cancelButton.addClickHandler(new ClickHandler() {
							@Override
							public void onClick(ClickEvent event) {
								uploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
								progressBars.get(fileQueuedEvent.getFile().getId()).setProgress(-1.0d);
								cancelButton.removeFromParent();
							}
						});
						cancelButtons.put(fileQueuedEvent.getFile().getId(), cancelButton);

						// Add the Bar and Button to the interface
						final HorizontalPanel progressBarAndButtonPanel = new HorizontalPanel();
						uploaderRows.put(fileQueuedEvent.getFile().getId(), progressBarAndButtonPanel);
						progressBarAndButtonPanel.setStyleName(WizardStyles.PROGRESSBAR_ROW);
						final Label fileNameLabel = new Label(fileQueuedEvent.getFile().getName());
						fileNameLabel.setStyleName(WizardStyles.WizardLabel3);
						fileNameLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);
						progressBarAndButtonPanel.add(progressBar);
						progressBarAndButtonPanel.add(fileNameLabel);
						progressBarAndButtonPanel.setCellHorizontalAlignment(fileNameLabel,
								HasHorizontalAlignment.ALIGN_RIGHT);
						progressBarAndButtonPanel.add(cancelButton);

						progressBarAndButtonPanel.setCellHorizontalAlignment(progressBar,
								HasHorizontalAlignment.ALIGN_LEFT);
						progressBarPanel.add(progressBarAndButtonPanel);

						return true;
					}
				})

				// to start the upload right away
				.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
					@Override
					public boolean onFileDialogComplete(FileDialogCompleteEvent dialogCompleteEvent) {
						if (dialogCompleteEvent.getTotalFilesInQueue() > 0) {
							uploader.startUpload();
						}
						return true;
					}
				})
				// show progress
				.setUploadProgressHandler(new UploadProgressHandler() {
					@Override
					public boolean onUploadProgress(UploadProgressEvent uploadProgressEvent) {
						final ProgressBar progressBar = progressBars.get(uploadProgressEvent.getFile().getId());
						progressBar.setProgress(
								(double) uploadProgressEvent.getBytesComplete() / uploadProgressEvent.getBytesTotal());
						return true;
					}
				})
				// upload success
				.setUploadCompleteHandler(uploadCompleteHandler)
				.setFileDialogStartHandler(new FileDialogStartHandler() {
					@Override
					public boolean onFileDialogStartEvent(FileDialogStartEvent fileDialogStartEvent) {
						if (uploader.getStats().getUploadsInProgress() <= 0) {
							// Clear the uploads that have completed, if none are in process
							progressBarPanel.clear();
							progressBars.clear();
							cancelButtons.clear();
						}
						return true;
					}
				}).setFileDialogCompleteHandler(new FileDialogCompleteHandler() {
					@Override
					public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
						if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
							if (uploader.getStats().getUploadsInProgress() <= 0) {
								uploader.startUpload();
							}
						}
						return true;
					}
				}).setFileQueueErrorHandler(getFileQueueErrorHandler()).setUploadErrorHandler(getUploadErrorHandler());
		final VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(uploader);
		if (Uploader.isAjaxUploadWithProgressEventsSupported()) {
			final Label dropFilesLabel = new Label("Drop one or more files here");
			dropFilesLabel.setStyleName("dropFilesLabel");
			dropFilesLabel.addDragOverHandler(new DragOverHandler() {
				@Override
				public void onDragOver(DragOverEvent event) {
					if (!uploader.getButtonDisabled()) {
						dropFilesLabel.addStyleName("dropFilesLabelHover");
					}
				}
			});
			dropFilesLabel.addDragLeaveHandler(new DragLeaveHandler() {
				@Override
				public void onDragLeave(DragLeaveEvent event) {
					dropFilesLabel.removeStyleName("dropFilesLabelHover");
				}
			});
			dropFilesLabel.addDropHandler(new DropHandler() {
				@Override
				public void onDrop(DropEvent event) {
					dropFilesLabel.removeStyleName("dropFilesLabelHover");

					if (uploader.getStats().getUploadsInProgress() <= 0) {
						progressBarPanel.clear();
						progressBars.clear();
						cancelButtons.clear();
					}

					uploader.addFilesToQueue(Uploader.getDroppedFiles(event.getNativeEvent()));
					event.preventDefault();
				}
			});
			verticalPanel.add(dropFilesLabel);
		}
		setUploadURL();

		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(verticalPanel);
		horizontalPanel.add(progressBarPanel);
		horizontalPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		horizontalPanel.setCellHorizontalAlignment(uploader, HorizontalPanel.ALIGN_LEFT);
		horizontalPanel.setCellHorizontalAlignment(progressBarPanel, HorizontalPanel.ALIGN_RIGHT);
		horizontalPanel.setStyleName(WizardStyles.UPLOADER_PANEL);
		setWidget(0, 0, horizontalPanel);

		// then the table with the uploaded files
		uploadedFilesVerticalPanel = new VerticalPanel();
		setWidget(1, 0, uploadedFilesVerticalPanel);
		// update list of already uploaded files
		loadUploadedFiles(false);
		//
		updateNextButtonState();
	}

	protected UploadErrorHandler getUploadErrorHandler() {
		return new UploadErrorHandler() {
			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				cancelButtons.get(uploadErrorEvent.getFile().getId()).removeFromParent();
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Upload of file " + uploadErrorEvent.getFile().getName()
								+ " failed due to [" + uploadErrorEvent.getErrorCode().toString() + "]: "
								+ uploadErrorEvent.getMessage());
				return true;
			}
		};
	}

	protected FileQueueErrorHandler getFileQueueErrorHandler() {
		return new FileQueueErrorHandler() {
			@Override
			public boolean onFileQueueError(FileQueueErrorEvent fileQueueErrorEvent) {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Upload of file " + fileQueueErrorEvent.getFile().getName()
								+ " failed due to [" + fileQueueErrorEvent.getErrorCode().toString() + "]: "
								+ fileQueueErrorEvent.getMessage());
				return true;
			}
		};
	}

	protected void setUploadURL() {

		// set url for servlet in the server
		uploader.setUploadURL("/pint/newFileUpload?" + SharedConstants.JOB_ID_PARAM + "="
				+ wizard.getContext().getPintImportConfiguration().getImportID());
	}

	public UploadCompleteHandler getUploadCompleteHandler() {
		final UploadCompleteHandler uploadCompleteHandler = new UploadCompleteHandler() {
			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				// remove cancel button
				final File uploadedFile = uploadCompleteEvent.getFile();
				cancelButtons.get(uploadedFile.getId()).removeFromParent();
				// start other upload?
				uploader.startUpload();
				// update object with no format

				service.getUploadedFileID(wizard.getContext().getPintImportConfiguration().getImportID(),
						getUploadedFileSignature(uploadedFile), new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}

							@Override
							public void onSuccess(String fileIDInServer) {

								try {
									PintImportCfgUtil.addFile(context.getPintImportConfiguration(), uploadedFile,
											fileIDInServer);
									// update list of uploaded files
									loadUploadedFiles(true);
									//
									updateNextButtonState();
								} catch (final PintException e) {
									StatusReportersRegister.getInstance().notifyStatusReporters(e);
									final FileNameWithTypeBean dataFile = new FileNameWithTypeBean();
									dataFile.setId(uploadedFile.getId());
									dataFile.setFileName(uploadedFile.getName());
									service.removeDataFile(context.getSessionID(),
											context.getPintImportConfiguration().getImportID(), dataFile,
											new AsyncCallback<Void>() {

												@Override
												public void onFailure(Throwable caught) {
													StatusReportersRegister.getInstance().notifyStatusReporters(
															"Error deleting file from server: " + e.getMessage());
												}

												@Override
												public void onSuccess(Void result) {
													StatusReportersRegister.getInstance()
															.notifyStatusReporters("File deleted from server");
												}
											});

								}
							}
						});

				// remove with delay the uploading row
				final Timer timer = new Timer() {

					@Override
					public void run() {
						final HorizontalPanel horizontalPanel = uploaderRows.get(uploadedFile.getId());
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

	protected String getUploadedFileSignature(File uploadedFile) {
		return uploadedFile.getName() + uploadedFile.getSize();
	}

	public void setPostParams(JSONObject postParams) {
		uploader.setPostParams(postParams);
	}

	/**
	 * Checks whether there is at least one file and whether it contains a format
	 * 
	 * @return
	 */
	private boolean isReadyForNextStep() {
		if (context.getPintImportConfiguration().getFileSet() == null) {
			return false;
		}
		if (context.getPintImportConfiguration().getFileSet().getFile().isEmpty()) {
			return false;
		}
		for (final FileTypeBean fileTypeBean : context.getPintImportConfiguration().getFileSet().getFile()) {
			if (fileTypeBean.getFormat() == null) {
				return false;
			}
			if (fileTypeBean.getFormat() == FileFormat.EXCEL) {
				if (fileTypeBean.getSheets() == null) {
					return false;
				}
			}
		}
		return true;
	}

	private void loadUploadedFiles(boolean fireFormatChangeEvent) {

		if (context.getPintImportConfiguration().getFileSet() != null) {
			final List<FileTypeBean> fileSet = context.getPintImportConfiguration().getFileSet().getFile();
			for (final FileTypeBean fileTypeBean : fileSet) {
				if (!uploadedFiles.contains(fileTypeBean)) {
					uploadedFiles.add(fileTypeBean);
					final HorizontalPanel horizontalPanel = createHorizontalPanelForUploadedFile(fileTypeBean,
							fireFormatChangeEvent);
					uploadedFilesVerticalPanel.add(horizontalPanel);
				}
			}
		}
	}

	private HorizontalPanel createHorizontalPanelForUploadedFile(FileTypeBean fileTypeBean,
			boolean fireFormatChangeEvent) {
		final HorizontalPanel ret = new HorizontalPanel();
		ret.setStyleName(WizardStyles.PROGRESSBAR_ROW);
		ret.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		// delete button
		final Label deleteButton = new Label("delete");
		deleteButton.setTitle("Delete file");
		deleteButton.setStyleName(WizardStyles.WizardButtonSmall);
		deleteButton.getElement().getStyle().setMarginRight(10, Unit.PX);
		ret.add(deleteButton);

		// file name
		final Label fileNameLabel = new Label(fileTypeBean.getName());
		fileNameLabel.setStyleName(WizardStyles.WizardLabel3);
		fileNameLabel.getElement().getStyle().setPaddingRight(10, Unit.PX);
		fileNameLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		ret.add(fileNameLabel);

		// label saying that the format is not set
		final String NO_FORMAT_SELECTED = "no format has been selected";

		final Label labelIfNoFormat = new Label(NO_FORMAT_SELECTED);
		labelIfNoFormat.setStyleName(WizardStyles.WizardCriticalMessage);
		labelIfNoFormat.getElement().getStyle().setPaddingRight(10, Unit.PX);
		labelIfNoFormat.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		labelIfNoFormat.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		ret.add(labelIfNoFormat);
		ret.setCellHorizontalAlignment(labelIfNoFormat, HasHorizontalAlignment.ALIGN_RIGHT);
		ret.setCellWidth(labelIfNoFormat, "100%");
		if (fileTypeBean.getFormat() != null) {
			labelIfNoFormat.setVisible(false);
		}
		// format combo
		final ListBox formats = new ListBox();
		formats.setTitle("Set the type of file so that PINT knows how to process it");
		formats.setMultipleSelect(false);
		addFormats(formats);
		ret.add(formats);
		ret.setCellHorizontalAlignment(formats, HasHorizontalAlignment.ALIGN_RIGHT);
		final Image loadingImage = new Image(MyClientBundle.INSTANCE.smallLoader());
		loadingImage.setVisible(false);
		loadingImage.getElement().getStyle().setPaddingLeft(10, Unit.PX);
		loadingImage.setTitle("Processing file in server");
		ret.add(loadingImage);
		// format change handler
		formats.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (formats.getSelectedIndex() == 0) {
					labelIfNoFormat.setVisible(true);
					labelIfNoFormat.setStyleName(WizardStyles.WizardCriticalMessage);
					labelIfNoFormat.setText(NO_FORMAT_SELECTED);
				} else {
					labelIfNoFormat.setVisible(false);
					final FileFormat fileFormat = FileFormat.getFileFormatFromString(formats.getSelectedValue());
					// update object
					fileTypeBean.setFormat(fileFormat);
					formats.setEnabled(false);
					loadingImage.setVisible(true);
					wizard.getView().startProcessing();
					final int previousSelectedIndex = formats.getSelectedIndex();
					service.updateFileFormat(context.getSessionID(), context.getPintImportConfiguration().getImportID(),
							fileTypeBean, new AsyncCallback<FileTypeBean>() {

								@Override
								public void onFailure(Throwable caught) {
									wizard.getView().stopProcessing();
									formats.setEnabled(true);
									loadingImage.setVisible(false);
									// parsing error?

									// if the format selected is Excel the error is probably because cannot be
									// parsed as Excel file
									if (fileFormat == FileFormat.EXCEL) {
										labelIfNoFormat.setText("parsing error as Excel");
									} else {
										// any other error
										labelIfNoFormat.setText("error updating format");
										StatusReportersRegister.getInstance().notifyStatusReporters(caught);
									}
									labelIfNoFormat.setStyleName(WizardStyles.WizardCriticalMessage);
									labelIfNoFormat.setVisible(true);
									// select the previous index
									formats.setSelectedIndex(previousSelectedIndex);
									updateNextButtonState();
								}

								@Override
								public void onSuccess(FileTypeBean result) {
									wizard.getView().stopProcessing();
									formats.setEnabled(true);
									loadingImage.setVisible(false);

									if (result.getSheets() != null) {
										fileTypeBean.setSheets(result.getSheets());
										// update the information label
										labelIfNoFormat.setStyleName(WizardStyles.WizardInfoMessage);
										labelIfNoFormat.setVisible(true);
										final String excelDescription = PintImportCfgUtil
												.getExcelImportDescription(fileTypeBean.getSheets());
										labelIfNoFormat.setText(excelDescription);
									} else {
										fileTypeBean.setSheets(null);// just in case it had before
										labelIfNoFormat.setVisible(false);
									}
									updateNextButtonState();
								}
							});
				}
			}
		});

		// select format if exist
		if (fileTypeBean.getFormat() != null) {
			selectFormat(formats, fileTypeBean.getFormat(), fireFormatChangeEvent);
		} else {
			// try to guess the format by name
			final FileFormat format = guessFormat(fileTypeBean);
			if (format != null) {
				fileTypeBean.setFormat(format);
				labelIfNoFormat.setVisible(false);
				selectFormat(formats, fileTypeBean.getFormat(), true);
			}
		}

		// delete button handler
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				wizard.getView().startProcessing();
				loadingImage.setVisible(true);
				final FileNameWithTypeBean fileNameWithTypeBean = PintImportCfgUtil
						.getFileNameWithTypeBeanl(fileTypeBean);

				labelIfNoFormat.setText("Deleting file on server");
				labelIfNoFormat.setStyleName(WizardStyles.WizardCriticalMessage);
				labelIfNoFormat.setVisible(true);
				service.removeDataFile(context.getSessionID(), context.getPintImportConfiguration().getImportID(),
						fileNameWithTypeBean, new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								wizard.getView().stopProcessing();
								loadingImage.setVisible(false);
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}

							@Override
							public void onSuccess(Void result) {
								wizard.getView().stopProcessing();
								// delete from conf object
								PintImportCfgUtil.removeFile(context.getPintImportConfiguration(),
										fileTypeBean.getId());

								loadingImage.setVisible(false);
								labelIfNoFormat.setText("File deleted on server");
								labelIfNoFormat.setStyleName(WizardStyles.WizardCriticalMessage);
								labelIfNoFormat.setVisible(true);
								final Timer timer = new Timer() {

									@Override
									public void run() {
										ret.removeFromParent();
									}
								};
								timer.schedule(1000);
								updateNextButtonState();
							}
						});
			}
		});

		return ret;
	}

	private FileFormat guessFormat(FileTypeBean fileTypeBean) {
		final String fileName = fileTypeBean.getName();
		if (fileName.toLowerCase().contains("census_chro")) {
			return FileFormat.CENSUS_CHRO_XML;
		} else if (fileName.toLowerCase().contains("census_out")) {
			return FileFormat.CENSUS_OUT_TXT;
		} else if (fileName.toLowerCase().endsWith("mzid")) {
			return FileFormat.MZIDENTML;
		} else if (fileName.toLowerCase().contains("dtaselect")) {
			return FileFormat.DTA_SELECT_FILTER_TXT;
		} else if (fileName.toLowerCase().endsWith("xls") || fileName.toLowerCase().endsWith("xlsx")) {
			return FileFormat.EXCEL;
		} else if (fileName.toLowerCase().contains("fasta")) {
			return FileFormat.FASTA;
		}
		return null;
	}

	protected void updateNextButtonState() {
		final boolean readyForNextStep = isReadyForNextStep();
		this.wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, readyForNextStep);
	}

	private boolean selectFormat(ListBox formats, FileFormat format, boolean fireChangeEvent) {
		for (int index = 0; index < formats.getItemCount(); index++) {
			if (formats.getValue(index).equals(format.name())) {
				formats.setSelectedIndex(index);
				if (fireChangeEvent) {
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), formats);
				}
				return true;
			}
		}
		return false;
	}

	private void addFormats(ListBox formatCombo2) {
		formatCombo2.clear();
		formatCombo2.addItem("");

		for (final FileFormat format : FileFormat.values()) {
			if (format == FileFormat.UNKNOWN) {
				continue;
			}
			formatCombo2.addItem(format.getName(), format.name());
		}

	}

	protected class CancelProgressBarTextFormatter extends ProgressBar.TextFormatter {
		@Override
		protected String getText(ProgressBar bar, double curProgress) {
			if (curProgress < 0) {
				return "Cancelled";
			}
			return ((int) (100 * bar.getPercent())) + "%";
		}
	}

}
