package edu.scripps.yates.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.ProjectSaverServiceAsync;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.ScrolledTabLayoutPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExperimentalConditionEditorPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.HorizontalPanelWithHeader;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectConfigurationHeaderCollapsiblePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.RatioEditorPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.TabNameChanger;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.ClosableWithTitlePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.ConditionDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.DataSourceDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.LabelDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.OrganismDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.RatioDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.RunDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.SampleDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.ServerDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.TissueDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FormatTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.IUploader.ServerMessage;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;

public class ProjectCreatorWizard extends InitializableComposite implements ContainsImportJobID {
	private final HorizontalPanel contentPanel;
	private SingleUploader uploader;
	private final ImportWizardServiceAsync importWizardService = ImportWizardServiceAsync.Util.getInstance();
	private final ProjectSaverServiceAsync projectSaverService = ProjectSaverServiceAsync.Util.getInstance();
	private int importJobID = -1;
	private boolean projectBeanReceived = false;
	private PintImportCfgBean pintImportCfgTypeBean;
	private MyDialogBox loadingDialog;
	private final InlineHTML statusInLineHTML;
	private final List<ClosableWithTitlePanel> disclosurePanels = new ArrayList<ClosableWithTitlePanel>();
	private HorizontalPanelWithHeader horizontalPanelConditions;
	private HorizontalPanelWithHeader horizontalPanelMSRuns;
	private HorizontalPanelWithHeader horizontalPanelSamples;
	private ProjectConfigurationHeaderCollapsiblePanel projectConfigurationPanel;
	private HorizontalPanelWithHeader horizontalPanelDataSources;
	private HorizontalPanelWithHeader horizontalPanelRatios;
	private boolean dataSourceBeansReceived;
	private List<DataSourceBean> dataSourceBeans;
	private HorizontalPanelWithHeader horizontalPanelServers;
	private HorizontalPanelWithHeader horizontalPanelTissues;
	private HorizontalPanelWithHeader horizontalPanelOrganisms;
	private HorizontalPanelWithHeader horizontalPanelLabels;
	private ScrolledTabLayoutPanel tabPanel;
	private final FlowPanel statusFlowPanel;
	private final List<ExperimentalConditionEditorPanel> conditionEditors = new ArrayList<ExperimentalConditionEditorPanel>();
	private final List<RatioEditorPanel> ratioEditors = new ArrayList<RatioEditorPanel>();
	private final String sessionID;

	public ProjectCreatorWizard(String sessionID) {
		this.sessionID = sessionID;

		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		mainPanel.setHeight("1100px");
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);

		// FlowPanel flowVerticalPanel = new FlowPanel();
		// flowVerticalPanel.setStyleName("verticalComponent");
		// HeaderPanel header = new HeaderPanel();
		// flowVerticalPanel.add(header);

		NavigationHorizontalPanel navigationPanel = new NavigationHorizontalPanel(TargetHistory.SUBMIT);
		mainPanel.addNorth(navigationPanel, 40);
		// flowVerticalPanel.add(navigationPanel);

		statusFlowPanel = new FlowPanel();
		statusFlowPanel.setStyleName("verticalComponent");
		// flowVerticalPanel.add(statusFlowPanel);
		// mainPanel.addNorth(flowVerticalPanel, 240.0);

		statusInLineHTML = new InlineHTML();
		statusInLineHTML.setStyleName("ProjectCreatorWizardStatusLabel");
		statusInLineHTML.setText("-");
		statusInLineHTML.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		statusFlowPanel.add(statusInLineHTML);

		contentPanel = new HorizontalPanel();
		contentPanel.setSize("100%", "100%");
		mainPanel.add(contentPanel);

		// askQuestionToUser();

	}

	public void askQuestionToUser() {

		final PopUpPanelYesNo yesNoPanel = new PopUpPanelYesNo(false, true, true, "Import data to PINT",
				"Do you want to start from a previously created import data configuration file?");
		yesNoPanel.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startFromPreviousXML();
				yesNoPanel.hide();
			}
		});
		yesNoPanel.addButton2ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startFromScratch();
				yesNoPanel.hide();
			}
		});
		yesNoPanel.show();
	}

	protected void startFromScratch() {
		contentPanel.clear();
		loadGUI(null, null);
		showDialog("Wait while a new import process is registered...", true, false, true);
		importWizardService.startNewImportProcess(sessionID, projectConfigurationPanel.getProjectTag(),
				projectConfigurationPanel.getFileNamesWithTypes(),
				projectConfigurationPanel.getRemoteFileWithTypeBeans(), new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						hiddeDialog();
					}

					@Override
					public void onSuccess(Integer importJobID) {
						if (importJobID != null) {
							setImportJobID(importJobID);
							StatusReportersRegister.getInstance()
									.notifyStatusReporters("Import process registered (id:'" + importJobID + "')");
						}
						hiddeDialog();
					}

				});

	}

	protected void startFromPreviousXML() {
		contentPanel.clear();
		// if (false) {
		pintImportCfgTypeBean = null;
		// allow the user to select the file
		uploader = new SingleUploader();

		contentPanel.add(uploader);
		uploader.setServletPath("xmlProject.gupld?" + SharedConstants.JOB_ID_PARAM + "=" + importJobID);
		uploader.addOnFinishUploadHandler(getOnFinishUploaderHandler());
		uploader.addOnCancelUploadHandler(getOnCancelUploaderHandler());
		uploader.addOnStartUploadHandler(new OnStartUploaderHandler() {

			@Override
			public void onStart(IUploader uploader) {
				showDialog("Uploading file...", true, false, true);
			}
		});
		final TextBox textBox = new TextBox();
		contentPanel.add(textBox);
		Button saveProjectButton = new Button("Save project");
		contentPanel.add(saveProjectButton);
		final Label label = new Label("status");
		contentPanel.add(label);
		saveProjectButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				List<String> projectTagNames = new ArrayList<String>();
				projectTagNames.add(textBox.getValue());
				projectSaverService.saveProject(sessionID, projectTagNames, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						label.setText(caught.getMessage());
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
					}

					@Override
					public void onSuccess(Void result) {
						label.setText("EVERYTHING IS OK");

					}
				});

			}
		});

	}

	/**
	 * Sets the instance variable importJobID and sets the importJobID to all
	 * the {@link ClosableWithTitlePanel} already created
	 *
	 * @param importJobID
	 */
	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;
		for (ClosableWithTitlePanel disclosurePanel : disclosurePanels) {
			disclosurePanel.setImportJobID(importJobID);
		}
		for (ExperimentalConditionEditorPanel editor : conditionEditors) {
			editor.setImportJobID(importJobID);
		}
		for (RatioEditorPanel editor : ratioEditors) {
			editor.setImportJobID(importJobID);
		}

	}

	private OnCancelUploaderHandler getOnCancelUploaderHandler() {
		OnCancelUploaderHandler ret = new OnCancelUploaderHandler() {

			@Override
			public void onCancel(IUploader uploader) {
				showDialog(uploader.getServerRawResponse(), false, true, false);
			}
		};

		return ret;
	}

	private OnFinishUploaderHandler getOnFinishUploaderHandler() {
		OnFinishUploaderHandler ret = new OnFinishUploaderHandler() {

			@Override
			public void onFinish(IUploader uploader) {
				hiddeDialog();
				if (uploader.getStatus() == Status.SUCCESS) {
					// The server sends useful information to the client by
					// default
					UploadedInfo uploadedInfo = uploader.getServerInfo();
					GWT.log("File name " + uploadedInfo.name);
					GWT.log("File content-type " + uploadedInfo.ctype);
					GWT.log("File size " + uploadedInfo.size);

					// You can send any customized message and parse it
					final ServerMessage serverMessage = uploader.getServerMessage();
					GWT.log("Server from uploader message " + serverMessage.getMessage());
					statusInLineHTML.setText("Using uploaded import configuration XML file: " + uploadedInfo.name);
					showDialog("Wait while a new import process is registered...", true, false, true);
					// create a new import job
					importWizardService.startNewImportProcess(sessionID, uploadedInfo.name,
							new AsyncCallback<Integer>() {

								@Override
								public void onSuccess(Integer importJobID) {
									if (importJobID != null) {
										setImportJobID(importJobID);
										StatusReportersRegister.getInstance().notifyStatusReporters(
												"Import process registered (id:'" + importJobID + "')");
										showDialog("Building interface...", true, false, true);
										// ask for the projectbean
										importWizardService.getPintImportCfgTypeBean(sessionID, importJobID,
												new AsyncCallback<PintImportCfgBean>() {

													@Override
													public void onFailure(Throwable caught) {
														StatusReportersRegister.getInstance()
																.notifyStatusReporters(caught);
													}

													@Override
													public void onSuccess(PintImportCfgBean result) {
														pintImportCfgTypeBean = result;
														projectBeanReceived = true;
														loadGUIIfAllRequiredDataIsReceived();
													}

												});

										// ask for data sources
										importWizardService.getDataSourceBeans(sessionID, importJobID,
												new AsyncCallback<List<DataSourceBean>>() {

													@Override
													public void onFailure(Throwable caught) {
														StatusReportersRegister.getInstance()
																.notifyStatusReporters(caught);
													}

													@Override
													public void onSuccess(List<DataSourceBean> result) {
														dataSourceBeans = result;
														dataSourceBeansReceived = true;
														loadGUIIfAllRequiredDataIsReceived();
													}

												});

									}

								}

								@Override
								public void onFailure(Throwable caught) {
									StatusReportersRegister.getInstance().notifyStatusReporters(caught);
								}
							});
				}

			}
		};
		return ret;
	}

	private void showDialog(String text, boolean showLoaderBar, boolean autoHide, boolean modal) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, autoHide, modal, showLoaderBar);
		} else {
			loadingDialog.setAutoHideEnabled(autoHide);
			loadingDialog.setModal(modal);
			loadingDialog.setText(text);
			loadingDialog.setShowLoadingBar(showLoaderBar);
		}
		loadingDialog.center();
	}

	private void hiddeDialog() {
		loadingDialog.hide();
	}

	private void loadGUIIfAllRequiredDataIsReceived() {
		if (projectBeanReceived && dataSourceBeansReceived) {
			GWT.runAsync(new RunAsyncCallback() {

				@Override
				public void onFailure(Throwable reason) {
					StatusReportersRegister.getInstance().notifyStatusReporters(reason);
				}

				@Override
				public void onSuccess() {
					// remove the uploader if present
					if (uploader != null)
						uploader.removeFromParent();
					contentPanel.clear();
					// load the main GUI
					loadGUI(pintImportCfgTypeBean, dataSourceBeans);
					setImportJobID(importJobID);
					hiddeDialog();
				}
			});

		}

	}

	private void loadGUI(PintImportCfgBean pintImportCfgBean, List<DataSourceBean> dataSourceBeans) {
		// reset everything
		ProjectCreatorRegister.clearRegister();
		if (!disclosurePanels.isEmpty()) {
			final Iterator<ClosableWithTitlePanel> iterator = disclosurePanels.iterator();
			while (iterator.hasNext()) {
				final ClosableWithTitlePanel disclosurePanel = iterator.next();
				if (disclosurePanel != null)
					disclosurePanel.removeFromParent();
			}
		}
		disclosurePanels.clear();

		//

		projectConfigurationPanel = new ProjectConfigurationHeaderCollapsiblePanel(pintImportCfgBean, dataSourceBeans);
		addPlusMinusHandlers();
		projectConfigurationPanel.addSaveImportConfigurationClickHandler(getSaveConfigurationClickHandler());
		projectConfigurationPanel.addDataSubmissionClickHandler(getDataSubmissionClickHandler());
		contentPanel.add(projectConfigurationPanel);

		tabPanel = new ScrolledTabLayoutPanel(30, Unit.PX, false);
		tabPanel.setSize("1400px", "1000px");
		contentPanel.add(tabPanel);
		// tabPanel.setSize("100%", "100%");
		// contentPanel.setCellWidth(tabPanel, "100%");

		FlowPanel disclosurePanelsContainer = new FlowPanel();
		// disclosurePanelsContainer.setSize("1400px", "900px");
		ScrollPanel scrollPanel = new ScrollPanel(disclosurePanelsContainer);
		scrollPanel.setSize("100%", "100%");
		tabPanel.add(scrollPanel, "Project elements");
		// conditions
		horizontalPanelConditions = new HorizontalPanelWithHeader("Experimental conditions:");
		disclosurePanelsContainer.add(horizontalPanelConditions);
		// ratios
		horizontalPanelRatios = new HorizontalPanelWithHeader("Ratios:");
		disclosurePanelsContainer.add(horizontalPanelRatios);
		// samples
		horizontalPanelSamples = new HorizontalPanelWithHeader("Samples:");
		disclosurePanelsContainer.add(horizontalPanelSamples);

		// MS Runs
		horizontalPanelMSRuns = new HorizontalPanelWithHeader("MS Runs:");
		disclosurePanelsContainer.add(horizontalPanelMSRuns);

		// data sources
		horizontalPanelDataSources = new HorizontalPanelWithHeader("Input data files:");
		disclosurePanelsContainer.add(horizontalPanelDataSources);
		// servers
		horizontalPanelServers = new HorizontalPanelWithHeader("Servers:");
		disclosurePanelsContainer.add(horizontalPanelServers);

		// tissues
		horizontalPanelTissues = new HorizontalPanelWithHeader("Sample origin (tissue/cell line):");
		disclosurePanelsContainer.add(horizontalPanelTissues);

		// Organisms
		horizontalPanelOrganisms = new HorizontalPanelWithHeader("Organisms:");
		disclosurePanelsContainer.add(horizontalPanelOrganisms);

		// labels
		horizontalPanelLabels = new HorizontalPanelWithHeader("Labels:");
		disclosurePanelsContainer.add(horizontalPanelLabels);

		// servers
		List<ServerTypeBean> serverBeans = projectConfigurationPanel.getServers();
		for (ServerTypeBean serverBean : serverBeans) {
			createNewServerDisclosurePanel(serverBean);
		}

		// MSRuns
		List<MsRunTypeBean> msRunBeans = projectConfigurationPanel.getMSRuns();
		for (MsRunTypeBean msRunBean : msRunBeans) {
			createNewRundisclosurePanel(msRunBean);
		}

		// tissues
		List<TissueTypeBean> tissues = projectConfigurationPanel.getTissues();
		for (TissueTypeBean tissueBean : tissues) {
			createNewTissueDisclosurePanel(tissueBean);
		}
		// organisms
		List<OrganismTypeBean> organisms = projectConfigurationPanel.getOrganisms();
		for (OrganismTypeBean organismBean : organisms) {
			createNewOrganismDisclosurePanel(organismBean);
		}
		// labels
		List<LabelTypeBean> labels = projectConfigurationPanel.getLabels();
		for (LabelTypeBean labelBean : labels) {
			createNewLabelDisclosurePanel(labelBean);
		}
		// Samples
		final List<SampleTypeBean> samples = projectConfigurationPanel.getSamples();
		for (SampleTypeBean sampleBean : samples) {
			createNewSampleDisclosurePanel(sampleBean);
		}

		// data sources
		dataSourceBeans = projectConfigurationPanel.getDataSources();
		for (DataSourceBean dataSourceBean : dataSourceBeans) {
			createNewDataSourceDisclosurePanel(dataSourceBean);
		}

		SharedDataUtils.excelBeansByExcelFileWithFormatBeansMap.clear();
		pintImportCfgTypeBean = projectConfigurationPanel.getPintImportCfgTypeBean();
		// register the fileNameWithTypes
		if (!pintImportCfgTypeBean.getFileSet().getFile().isEmpty()) {
			for (FileTypeBean fileTypeBean : pintImportCfgTypeBean.getFileSet().getFile()) {
				if (dataSourceBeans != null) {
					for (ClosableWithTitlePanel disclosurePanel : disclosurePanels) {
						if (disclosurePanel instanceof DataSourceDisclosurePanel) {
							DataSourceDisclosurePanel dataSourcePanel = (DataSourceDisclosurePanel) disclosurePanel;
							if (dataSourcePanel.getFileNameWithTypeBean() != null
									&& dataSourcePanel.getFileNameWithTypeBean().getId().equals(fileTypeBean.getId())) {
								SharedDataUtils.excelBeansByExcelFileWithFormatBeansMap
										.put(dataSourcePanel.getFileNameWithTypeBean(), fileTypeBean);
							}
						}
					}
				}
			}
		}
		// conditions
		List<ExperimentalConditionTypeBean> conditions = projectConfigurationPanel.getExperimentalConditions();
		for (ExperimentalConditionTypeBean conditionBean : conditions) {
			createNewConditionDisclosurePanel(conditionBean);
		}

		// ratios

		// psm excel ratios
		final List<ExcelAmountRatioTypeBean> psmExcelRatios = projectConfigurationPanel.getPSMExcelRatios();
		for (ExcelAmountRatioTypeBean excelRatioTypeBean : psmExcelRatios) {
			RatioDisclosurePanel disclosurePanel = createNewRatioDisclosurePanel(excelRatioTypeBean,
					SharedAggregationLevel.PSM);
			disclosurePanel.updateGUIFromObjectData(excelRatioTypeBean);
		}
		// peptide excel ratios
		final List<ExcelAmountRatioTypeBean> peptideExcelRatios = projectConfigurationPanel.getPeptideExcelRatios();
		for (ExcelAmountRatioTypeBean excelRatioTypeBean : peptideExcelRatios) {
			RatioDisclosurePanel disclosurePanel = createNewRatioDisclosurePanel(excelRatioTypeBean,
					SharedAggregationLevel.PEPTIDE);
			disclosurePanel.updateGUIFromObjectData(excelRatioTypeBean);
		}
		// protein excel ratios
		final List<ExcelAmountRatioTypeBean> proteinExcelRatios = projectConfigurationPanel.getProteinExcelRatios();
		for (ExcelAmountRatioTypeBean excelRatioTypeBean : proteinExcelRatios) {
			RatioDisclosurePanel disclosurePanel = createNewRatioDisclosurePanel(excelRatioTypeBean,
					SharedAggregationLevel.PROTEIN);
			disclosurePanel.updateGUIFromObjectData(excelRatioTypeBean);
		}

		// if the remote file ratios are equal between them, do not create a new
		// ratio disclusure panel for each, just add a new aggregation level
		Map<RemoteFilesRatioTypeBean, Set<SharedAggregationLevel>> remoteFilesRatioTypeBeanMap = new HashMap<RemoteFilesRatioTypeBean, Set<SharedAggregationLevel>>();
		final List<RemoteFilesRatioTypeBean> psmRemoteFileRatios = projectConfigurationPanel.getPSMRemoteFileRatios();
		for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : psmRemoteFileRatios) {
			if (remoteFilesRatioTypeBeanMap.containsKey(remoteFilesRatioTypeBean)) {
				remoteFilesRatioTypeBeanMap.get(remoteFilesRatioTypeBean).add(SharedAggregationLevel.PSM);
			}
		}
		final List<RemoteFilesRatioTypeBean> peptideRemoteFileRatios = projectConfigurationPanel
				.getPeptideRemoteFileRatios();
		for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : peptideRemoteFileRatios) {
			if (remoteFilesRatioTypeBeanMap.containsKey(remoteFilesRatioTypeBean)) {
				remoteFilesRatioTypeBeanMap.get(remoteFilesRatioTypeBean).add(SharedAggregationLevel.PEPTIDE);
			}
		}
		final List<RemoteFilesRatioTypeBean> proteinRemoteFileRatios = projectConfigurationPanel
				.getProteinRemoteFileRatios();
		for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : proteinRemoteFileRatios) {
			if (remoteFilesRatioTypeBeanMap.containsKey(remoteFilesRatioTypeBean)) {
				remoteFilesRatioTypeBeanMap.get(remoteFilesRatioTypeBean).add(SharedAggregationLevel.PROTEIN);
			}
		}
		for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean : remoteFilesRatioTypeBeanMap.keySet()) {
			RatioDisclosurePanel disclosurePanel = createNewRatioDisclosurePanel(remoteFilesRatioTypeBean,
					remoteFilesRatioTypeBeanMap.get(remoteFilesRatioTypeBean));
			disclosurePanel.updateGUIFromObjectData(remoteFilesRatioTypeBean);
		}
		// // psm remote file ratios
		// final List<RemoteFilesRatioTypeBean> psmRemoteFileRatios =
		// projectConfigurationPanel.getPSMRemoteFileRatios();
		// for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean :
		// psmRemoteFileRatios) {
		// RatioDisclosurePanel disclosurePanel =
		// createNewRatioDisclosurePanel(remoteFilesRatioTypeBean,
		// SharedAggregationLevel.PSM);
		// disclosurePanel.updateGUIFromObjectData(remoteFilesRatioTypeBean);
		// }
		//
		// // peptide remote file ratios
		// final List<RemoteFilesRatioTypeBean> peptideRemoteFileRatios =
		// projectConfigurationPanel
		// .getPeptideRemoteFileRatios();
		// for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean :
		// peptideRemoteFileRatios) {
		// RatioDisclosurePanel disclosurePanel =
		// createNewRatioDisclosurePanel(remoteFilesRatioTypeBean,
		// SharedAggregationLevel.PEPTIDE);
		// disclosurePanel.updateGUIFromObjectData(remoteFilesRatioTypeBean);
		// }
		//
		// // protein remote file ratios
		// final List<RemoteFilesRatioTypeBean> proteinRemoteFileRatios =
		// projectConfigurationPanel
		// .getProteinRemoteFileRatios();
		// for (RemoteFilesRatioTypeBean remoteFilesRatioTypeBean :
		// proteinRemoteFileRatios) {
		// RatioDisclosurePanel disclosurePanel =
		// createNewRatioDisclosurePanel(remoteFilesRatioTypeBean,
		// SharedAggregationLevel.PROTEIN);
		// disclosurePanel.updateGUIFromObjectData(remoteFilesRatioTypeBean);
		// }
		// iterate over the list of EditableDisclosurePanels to update their GUI
		// that represents a data object
		for (ClosableWithTitlePanel editableDisclosurePanel : disclosurePanels) {
			if (editableDisclosurePanel instanceof ReferencesDataObject) {
				((ReferencesDataObject) editableDisclosurePanel).updateGUIReferringToDataObjects();
			}
		}
		// iterate over the list of EditableDisclosurePanels to update their GUI
		// that is referring to an external data object
		for (ClosableWithTitlePanel editableDisclosurePanel : disclosurePanels) {
			editableDisclosurePanel.updateGUIFromObjectData();
		}

		// once all references are setup
		// check access of the datasources
		// and firemodification event in order to become green if it is valid
		for (ClosableWithTitlePanel disclosurePanel : disclosurePanels) {
			if (disclosurePanel instanceof DataSourceDisclosurePanel) {
				((DataSourceDisclosurePanel) disclosurePanel).checkAccess();
			}

			// disclosurePanel.fireModificationEvent();
			disclosurePanel.updateHeaderColor();
			disclosurePanel.updateRepresentedObject();
			ProjectCreatorRegister.fireModificationEvent(disclosurePanel.getInternalID());
		}
	}

	// /**
	// * Gets the action for when a {@link DisclosurePanel} is closed, which is,
	// * to update the header text boxes counts
	// *
	// * @return
	// */
	// private ClickHandler getCloseDisclosurePanelClickHandler() {
	// if (closeDisclosurePanelClickHandler == null) {
	// closeDisclosurePanelClickHandler = new ClickHandler() {
	// @Override
	// public void onClick(ClickEvent event) {
	// projectConfigurationPanel.updateIncrementableTextBoxes();
	// }
	// };
	// }
	// return closeDisclosurePanelClickHandler;
	// }

	/**
	 * Creates the handlers for running when the plus and minus buttons are
	 * pressed on the {@link ProjectConfigurationHeaderPanel} to generate or to
	 * remove the appropiate object data beans.
	 *
	 * @param projectConfigurationPanel
	 */
	private void addPlusMinusHandlers() {

		// add data source
		projectConfigurationPanel.addHandlerAddDataSource(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewDataSourceDisclosurePanel(null);
			}
		});
		// remove data source
		projectConfigurationPanel.addHandlerRemoveDataSource(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding
				// type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof DataSourceDisclosurePanel) {
						DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});
		// add ratio
		projectConfigurationPanel.addHandlerAddRatio(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Set<SharedAggregationLevel> aggregationLevels = new HashSet<SharedAggregationLevel>(); // unknown
																										// yet
				createNewRatioDisclosurePanel(aggregationLevels);
			}
		});
		// remove ratio
		projectConfigurationPanel.addHandlerRemoveRatio(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding
				// type

				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof RatioDisclosurePanel) {
						RatioDisclosurePanel disclosurePanel = (RatioDisclosurePanel) disclosurePanels.get(i);
						disclosurePanels.remove(disclosurePanel);
						disclosurePanel.close();
						break;
					}
				}

			}
		});
		// add server
		projectConfigurationPanel.addHandlerAddServer(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewServerDisclosurePanel(null);
			}
		});
		// remove server
		projectConfigurationPanel.addHandlerRemoveServer(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof ServerDisclosurePanel) {
						ServerDisclosurePanel disclosurePanel = (ServerDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});

		// add condition
		projectConfigurationPanel.addHandlerAddCondition(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewConditionDisclosurePanel(null);
			}
		});
		// remove condition
		projectConfigurationPanel.addHandlerRemoveCondition(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof ConditionDisclosurePanel) {
						ConditionDisclosurePanel disclosurePanel = (ConditionDisclosurePanel) disclosurePanels.get(i);
						final ExperimentalConditionTypeBean bean = disclosurePanel.getObject();
						projectConfigurationPanel.removeCondition(bean);
						disclosurePanel.close();
						break;
					}
				}

			}
		});
		// add Sample
		projectConfigurationPanel.addHandlerAddSample(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewSampleDisclosurePanel(null);

			}
		});
		// remove Sample
		projectConfigurationPanel.addHandlerRemoveSample(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof SampleDisclosurePanel) {
						SampleDisclosurePanel disclosurePanel = (SampleDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});

		// add msruns
		projectConfigurationPanel.addHandlerAddMSRun(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewRundisclosurePanel(null);
			}
		});
		// remove MSRun
		projectConfigurationPanel.addHandlerRemoveMSRun(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof RunDisclosurePanel) {
						RunDisclosurePanel disclosurePanel = (RunDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});

		// add organisms
		projectConfigurationPanel.addHandlerAddOrganism(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewOrganismDisclosurePanel(null);

			}
		});
		// remove Organism
		projectConfigurationPanel.addHandlerRemoveOrganism(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof OrganismDisclosurePanel) {
						OrganismDisclosurePanel disclosurePanel = (OrganismDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});

		// add tissues
		projectConfigurationPanel.addHandlerAddTissue(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewTissueDisclosurePanel(null);

			}
		});
		// remove tissues
		projectConfigurationPanel.addHandlerRemoveTissue(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof TissueDisclosurePanel) {
						TissueDisclosurePanel disclosurePanel = (TissueDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});
		// add label
		projectConfigurationPanel.addHandlerAddLabel(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewLabelDisclosurePanel(null);
			}
		});
		// remove label
		projectConfigurationPanel.addHandlerRemoveLabel(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// take the last disclosure panel of the corresponding type
				for (int i = disclosurePanels.size() - 1; i >= 0; i--) {
					if (disclosurePanels.get(i) instanceof LabelDisclosurePanel) {
						LabelDisclosurePanel disclosurePanel = (LabelDisclosurePanel) disclosurePanels.get(i);
						disclosurePanel.close();
						break;
					}
				}
			}
		});
	}

	private ConditionDisclosurePanel createNewConditionDisclosurePanel(ExperimentalConditionTypeBean conditionBean) {
		final ConditionDisclosurePanel disclosurePanel = new ConditionDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeCondition(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				// is already called in removeCondition
				// projectConfigurationPanel.updateIncrementableTextBoxes();
				// close the tab having the name like the condition
				closeConditionTab(disclosurePanel.getObject().getId());
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		if (conditionBean == null) {
			projectConfigurationPanel.addCondition(disclosurePanel.getObject());
			conditionBean = disclosurePanel.getObject();
		} else {
			disclosurePanel.updateGUIFromObjectData(conditionBean);
		}
		horizontalPanelConditions.addToHorizontalPanel(disclosurePanel);
		disclosurePanel.updateGUIReferringToDataObjects();

		createNewConditionEditorPanel(conditionBean, disclosurePanel);
		return disclosurePanel;
	}

	private SampleDisclosurePanel createNewSampleDisclosurePanel(SampleTypeBean sampleBean) {
		final SampleDisclosurePanel sampleDisclosurePanel = new SampleDisclosurePanel(sessionID, importJobID);
		sampleDisclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeSample(sampleDisclosurePanel.getObject());
				disclosurePanels.remove(sampleDisclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(sampleDisclosurePanel);
		if (sampleBean == null)
			projectConfigurationPanel.addSample(sampleDisclosurePanel.getObject());
		else
			sampleDisclosurePanel.updateGUIFromObjectData(sampleBean);

		horizontalPanelSamples.addToHorizontalPanel(sampleDisclosurePanel);

		sampleDisclosurePanel.updateGUIReferringToDataObjects();
		return sampleDisclosurePanel;
	}

	private LabelDisclosurePanel createNewLabelDisclosurePanel(LabelTypeBean labelBean) {
		final LabelDisclosurePanel disclosurePanel = new LabelDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeLabel(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		if (labelBean == null)
			projectConfigurationPanel.addLabel(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(labelBean);

		horizontalPanelLabels.addToHorizontalPanel(disclosurePanel);
		return disclosurePanel;
	}

	private OrganismDisclosurePanel createNewOrganismDisclosurePanel(OrganismTypeBean organismBean) {
		final OrganismDisclosurePanel disclosurePanel = new OrganismDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeOrganism(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		if (organismBean == null)
			projectConfigurationPanel.addOrganism(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(organismBean);

		horizontalPanelOrganisms.addToHorizontalPanel(disclosurePanel);
		return disclosurePanel;
	}

	private TissueDisclosurePanel createNewTissueDisclosurePanel(TissueTypeBean tissueBean) {
		final TissueDisclosurePanel disclosurePanel = new TissueDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeTissue(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		if (tissueBean == null)
			projectConfigurationPanel.addTissue(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(tissueBean);
		horizontalPanelTissues.addToHorizontalPanel(disclosurePanel);
		return disclosurePanel;
	}

	private RunDisclosurePanel createNewRundisclosurePanel(MsRunTypeBean msRunBean) {
		final RunDisclosurePanel disclosurePanel = new RunDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeMSRun(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		horizontalPanelMSRuns.addToHorizontalPanel(disclosurePanel);
		if (msRunBean == null)
			projectConfigurationPanel.addMSRun(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(msRunBean);

		return disclosurePanel;
	}

	private ServerDisclosurePanel createNewServerDisclosurePanel(ServerTypeBean serverBean) {
		final ServerDisclosurePanel disclosurePanel = new ServerDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeServer(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
			}
		}, true);
		disclosurePanels.add(disclosurePanel);
		if (serverBean == null)
			projectConfigurationPanel.addServer(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(serverBean);
		horizontalPanelServers.addToHorizontalPanel(disclosurePanel);
		return disclosurePanel;
	}

	private RatioDisclosurePanel createNewRatioDisclosurePanel(ExcelAmountRatioTypeBean excelRatioTypeBean,
			SharedAggregationLevel aggregationLevel) {
		final RatioDisclosurePanel ratioDisclosurePanel = new RatioDisclosurePanel(sessionID, importJobID);
		final RatioDescriptorTypeBean ratioBean = ratioDisclosurePanel.getObject();
		ratioDisclosurePanel.addCloseClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeRatio(ratioBean);
				disclosurePanels.remove(ratioDisclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
				closeRatioTab(ratioBean.getId());
			}
		}, true);
		disclosurePanels.add(ratioDisclosurePanel);
		projectConfigurationPanel.addRatio(ratioBean);
		horizontalPanelRatios.addToHorizontalPanel(ratioDisclosurePanel);
		ratioDisclosurePanel.updateGUIReferringToDataObjects();
		if (excelRatioTypeBean != null) {
			ratioDisclosurePanel.updateGUIFromObjectData(excelRatioTypeBean);
		}
		createNewRatioEditorPanel(excelRatioTypeBean, ratioDisclosurePanel, aggregationLevel);
		return ratioDisclosurePanel;
	}

	private RatioDisclosurePanel createNewRatioDisclosurePanel(RemoteFilesRatioTypeBean remoteFilesRatioTypeBean,
			Collection<SharedAggregationLevel> aggregationLevels) {
		final RatioDisclosurePanel ratioDisclosurePanel = new RatioDisclosurePanel(sessionID, importJobID);
		final RatioDescriptorTypeBean ratioBean = ratioDisclosurePanel.getObject();
		ratioDisclosurePanel.addCloseClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeRatio(ratioBean);
				disclosurePanels.remove(ratioDisclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
				closeRatioTab(ratioBean.getId());
			}
		}, true);
		disclosurePanels.add(ratioDisclosurePanel);
		projectConfigurationPanel.addRatio(ratioBean);
		horizontalPanelRatios.addToHorizontalPanel(ratioDisclosurePanel);
		ratioDisclosurePanel.updateGUIReferringToDataObjects();
		if (remoteFilesRatioTypeBean != null)
			ratioDisclosurePanel.updateGUIFromObjectData(remoteFilesRatioTypeBean);

		createNewRatioEditorPanel(remoteFilesRatioTypeBean, ratioDisclosurePanel, aggregationLevels);
		return ratioDisclosurePanel;
	}

	private RatioDisclosurePanel createNewRatioDisclosurePanel(Collection<SharedAggregationLevel> aggregationLevels) {
		final RatioDisclosurePanel ratioDisclosurePanel = new RatioDisclosurePanel(sessionID, importJobID);
		final RatioDescriptorTypeBean ratioBean = ratioDisclosurePanel.getObject();
		ratioDisclosurePanel.addCloseClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeRatio(ratioBean);
				disclosurePanels.remove(ratioDisclosurePanel);
				projectConfigurationPanel.updateIncrementableTextBoxes();
				closeRatioTab(ratioBean.getId());
			}
		}, true);
		disclosurePanels.add(ratioDisclosurePanel);
		projectConfigurationPanel.addRatio(ratioBean);
		horizontalPanelRatios.addToHorizontalPanel(ratioDisclosurePanel);
		ratioDisclosurePanel.updateGUIReferringToDataObjects();

		createNewRatioEditorPanel(ratioBean, ratioDisclosurePanel, aggregationLevels);
		return ratioDisclosurePanel;
	}

	private DataSourceDisclosurePanel createNewDataSourceDisclosurePanel(DataSourceBean dataSourceBean) {
		final DataSourceDisclosurePanel disclosurePanel = new DataSourceDisclosurePanel(sessionID, importJobID);
		disclosurePanel.addCloseClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				projectConfigurationPanel.removeDataSource(disclosurePanel.getObject());
				disclosurePanels.remove(disclosurePanel);
				importWizardService.removeDataFile(sessionID, importJobID, disclosurePanel.getFileNameWithTypeBean(),
						new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}

							@Override
							public void onSuccess(Void result) {
								// do nothing
							}
						});
			}
		}, true);

		disclosurePanels.add(disclosurePanel);
		if (dataSourceBean == null)
			projectConfigurationPanel.addDataSource(disclosurePanel.getObject());
		else
			disclosurePanel.updateGUIFromObjectData(dataSourceBean);

		horizontalPanelDataSources.addToHorizontalPanel(disclosurePanel);
		return disclosurePanel;
	}

	private RatioEditorPanel createNewRatioEditorPanel(ExcelAmountRatioTypeBean excelRatioBean,
			RatioDisclosurePanel ratioDisclosurePanel, SharedAggregationLevel aggregationLevel) {
		// create a new editor ratio

		final RatioEditorPanel ratioEditor = new RatioEditorPanel(importJobID, excelRatioBean, aggregationLevel,
				ratioDisclosurePanel.getObject(), ratioDisclosurePanel.getInternalID());
		ratioEditors.add(ratioEditor);

		// add the ratio editor as a new tab
		tabPanel.add(ratioEditor, excelRatioBean.getId());
		ratioEditor.setTabNameChanger(new TabNameChanger() {
			@Override
			public void changeTabName(String tabName) {
				tabPanel.setTabText(tabPanel.getWidgetIndex(ratioEditor), tabName);
			}
		});
		return ratioEditor;

	}

	private RatioEditorPanel createNewRatioEditorPanel(RatioDescriptorTypeBean ratioDescriptorTypeBean,
			RatioDisclosurePanel ratioDisclosurePanel, Collection<SharedAggregationLevel> aggregationLevels) {
		// create a new editor ratio
		final RatioEditorPanel ratioEditor = new RatioEditorPanel(sessionID, importJobID, ratioDescriptorTypeBean,
				ratioDisclosurePanel.getInternalID(), aggregationLevels);
		ratioEditors.add(ratioEditor);

		// add the ratio editor as a new tab
		tabPanel.add(ratioEditor, ratioDescriptorTypeBean.getId());
		ratioEditor.setTabNameChanger(new TabNameChanger() {
			@Override
			public void changeTabName(String tabName) {
				tabPanel.setTabText(tabPanel.getWidgetIndex(ratioEditor), tabName);
			}
		});
		return ratioEditor;

	}

	private RatioEditorPanel createNewRatioEditorPanel(RemoteFilesRatioTypeBean remoteFilesRatioBean,
			RatioDisclosurePanel ratioDisclosurePanel, Collection<SharedAggregationLevel> aggregationLevels) {
		// create a new editor ratio
		final RatioEditorPanel ratioEditor = new RatioEditorPanel(importJobID, remoteFilesRatioBean,
				ratioDisclosurePanel.getObject(), ratioDisclosurePanel.getInternalID(), aggregationLevels);
		ratioEditors.add(ratioEditor);

		// add the ratio editor as a new tab
		tabPanel.add(ratioEditor, remoteFilesRatioBean.getId());
		ratioEditor.setTabNameChanger(new TabNameChanger() {
			@Override
			public void changeTabName(String tabName) {
				tabPanel.setTabText(tabPanel.getWidgetIndex(ratioEditor), tabName);
			}
		});
		return ratioEditor;

	}

	private void closeConditionTab(String conditionName) {
		for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
			// remove from the list of condition Editors to save mem

			final Widget tabWidget = tabPanel.getTabWidget(i);
			if (tabWidget instanceof HasText) {
				String tabName = ((HasText) tabWidget).getText();
				if (tabName.equals(conditionName)) {
					tabPanel.remove(i);

				}
			}
		}
		// iterate over condition editors
		for (ExperimentalConditionEditorPanel conditionEditor : conditionEditors) {
			if (conditionEditor.getExperimentalConditionTypeBean() != null
					&& conditionEditor.getExperimentalConditionTypeBean().getId().equals(conditionName)) {
				conditionEditor.unregisterAsListener();
				conditionEditors.remove(conditionEditor);
			}
		}

	}

	private void closeRatioTab(String ratioName) {
		for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
			final Widget tabWidget = tabPanel.getTabWidget(i);
			if (tabWidget instanceof HasText) {
				String tabName = ((HasText) tabWidget).getText();
				if (tabName.equals(ratioName)) {
					tabPanel.remove(i);
				}
			}

		}
		// iterate over ratioEditors
		for (RatioEditorPanel ratioEditor : ratioEditors) {
			boolean found = false;
			if (ratioEditor.getPeptideExcelRatioTypeBean() != null
					&& ratioEditor.getPeptideExcelRatioTypeBean().getId().equals(ratioName)) {
				found = true;
			}
			if (ratioEditor.getProteinExcelRatioTypeBean() != null
					&& ratioEditor.getProteinExcelRatioTypeBean().getId().equals(ratioName)) {
				found = true;
			}
			if (ratioEditor.getPSMExcelRatioTypeBean() != null
					&& ratioEditor.getPSMExcelRatioTypeBean().getId().equals(ratioName)) {
				found = true;
			}
			if (ratioEditor.getRemoteFilesRatioTypeBean() != null
					&& ratioEditor.getRemoteFilesRatioTypeBean().getId().equals(ratioName)) {
				found = true;
			}
			if (found) {
				ratioEditor.unregisterAsListener();
				ratioEditors.remove(ratioEditor);
			}
		}
	}

	/**
	 * Creates a new Tab and add it to the TabPanel, containing a
	 * {@link ExperimentalConditionEditorPanel} to edit a certain
	 * {@link ExperimentalConditionTypeBean} and related to a
	 * {@link ConditionDisclosurePanel}
	 *
	 * @param conditionBean
	 * @param disclosurePanel
	 * @return
	 */
	private ExperimentalConditionEditorPanel createNewConditionEditorPanel(ExperimentalConditionTypeBean conditionBean,
			ConditionDisclosurePanel disclosurePanel) {
		// create a new editor condition
		final ExperimentalConditionEditorPanel conditionEditor = new ExperimentalConditionEditorPanel(sessionID,
				importJobID, conditionBean);
		conditionEditors.add(conditionEditor);

		// register as a listener of that condition
		ProjectCreatorRegister.registerAsListenerByObjectID(disclosurePanel.getInternalID(), conditionEditor);
		// add the condition editor as a new tab
		tabPanel.add(conditionEditor, conditionBean.getId());
		conditionEditor.setTabNameChanger(new TabNameChanger() {
			@Override
			public void changeTabName(String tabName) {
				tabPanel.setTabText(tabPanel.getWidgetIndex(conditionEditor), tabName);
			}
		});
		return conditionEditor;
	}

	// @Override
	// public void showMessage(final String message) {
	// // showDialog(message, false);
	//
	// final MoleAnimation animation = new
	// MoleAnimation(statusFlowPanel.getElement());
	//
	// animation.animateMole(0, 40, 10);
	//
	// statusInLineHTML.setHTML(new
	// SafeHtmlBuilder().appendEscapedLines(message).toSafeHtml());
	//
	// Timer timer = new Timer() {
	//
	// @Override
	// public void run() {
	// // if (statusLabel.getText().equals(message))
	// // statusLabel.setText("");
	// animation.animateMole(40, 0, 1000);
	//
	// }
	// };
	// timer.schedule(1000 * 5);
	// }

	private ClickHandler getDataSubmissionClickHandler() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dataSubmission();
			}
		};
		return clickHandler;
	}

	protected void dataSubmission() {

		// check first the login
		final PopUpPanelPasswordChecker loginPanel = new PopUpPanelPasswordChecker(true, true, "PINT security",
				"enter PINT master password for project submission:");
		loginPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (loginPanel.isLoginOK()) {
					try {
						updateRepresentedObject();
						showDialog(
								"Submitting data to the server and retrieving corresponding UniProt annotations...\nThis may take several minutes.",
								true, false, true);
						importWizardService.submitProject(importJobID, pintImportCfgTypeBean,
								new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
										StatusReportersRegister.getInstance().notifyStatusReporters(caught);
									}

									@Override
									public void onSuccess(String encrypt) {
										// final String encrypt = CryptoUtil
										// .encrypt(pintImportCfgTypeBean.getProject().getTag());
										String privateURL = Window.Location.getProtocol() + "//"
												+ Window.Location.getHost() + "/pint/?project=" + encrypt;
										StatusReportersRegister.getInstance().notifyStatusReporters(
												"Project has been successfully uploaded in the database\n"
														+ "by default, the project will be private, only accesible by this encoded URL: "
														+ privateURL + "\n");
										hiddeDialog();
										showDialog(
												"Project has been successfully uploaded in the database\nTo access the project, go to:\n"
														+ privateURL,
												false, true, false);
									}
								});
					} catch (PintException e) {
						errorSavingProject(e);
					}
				}
			}
		});
		loginPanel.show();

	}

	private ClickHandler getSaveConfigurationClickHandler() {
		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveImportConfiguration();
			}
		};
		return clickHandler;
	}

	private void saveImportConfiguration() {
		try {
			updateRepresentedObject();
			showDialog("Saving project configuration...", true, false, true);
			pintImportCfgTypeBean.setImportID(importJobID);
			importWizardService.saveImportConfiguration(importJobID, pintImportCfgTypeBean,
					new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							errorSavingProject(caught);
						}

						@Override
						public void onSuccess(Boolean valid) {
							if (valid) {
								StatusReportersRegister.getInstance().notifyStatusReporters(
										"Project XML file saved on server. Project is ready to be submited");
								// set upload server enabled
								projectConfigurationPanel.uploadButtonEnabled(true);

							} else {
								StatusReportersRegister.getInstance().notifyStatusReporters(
										"Project XML file saved on server. However, project IS NOT ready for submission");
								// set upload server disabled
								projectConfigurationPanel.uploadButtonEnabled(false);
							}
							// enable anyway download of the file
							projectConfigurationPanel
									.enableDownloadImportCfgFile(pintImportCfgTypeBean.getProject().getTag());
							hiddeDialog();
						}
					});
		} catch (PintException e) {
			errorSavingProject(e);
		}
	}

	protected void errorSavingProject(Throwable caught) {
		// hiddeDialog();
		StatusReportersRegister.getInstance().notifyStatusReporters(caught);
		// set upload server disabled
		projectConfigurationPanel.uploadButtonEnabled(false);
		// enable download of the file
		if (caught instanceof PintException
				&& ((PintException) caught).getPintErrorType() == PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR) {
			projectConfigurationPanel.enableDownloadImportCfgFile(pintImportCfgTypeBean.getProject().getTag());
		} else {
			projectConfigurationPanel.disableDownloadImportCfgFile();
		}
	}

	private List<RemoteFilesRatioTypeBean> getPSMRemoteFilesRatios() {
		List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			if (ratioEditorPanel.getAggregationLevels().isEmpty()) {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "' Define the aggregation level on the ratio tab.");
			}
			if (ratioEditorPanel.getAggregationLevels().contains(SharedAggregationLevel.PSM)) {
				final RemoteFilesRatioTypeBean remoteFilesRatioTypeBean = ratioEditorPanel
						.getRemoteFilesRatioTypeBean();
				if (remoteFilesRatioTypeBean != null) {
					ret.add(remoteFilesRatioTypeBean);
				} else {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Ratio definition is not complete for ratio '"
									+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
				}
			}
		}
		return ret;
	}

	private List<RemoteFilesRatioTypeBean> getPeptideRemoteFilesRatios() {
		List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			if (ratioEditorPanel.getAggregationLevels().isEmpty()) {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "' Define the aggregation level on the ratio tab.");
			}
			if (ratioEditorPanel.getAggregationLevels().contains(SharedAggregationLevel.PEPTIDE)) {
				final RemoteFilesRatioTypeBean remoteFilesRatioTypeBean = ratioEditorPanel
						.getRemoteFilesRatioTypeBean();
				if (remoteFilesRatioTypeBean != null) {
					ret.add(remoteFilesRatioTypeBean);
				} else {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Ratio definition is not complete for ratio '"
									+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
				}
			}
		}
		return ret;
	}

	private List<RemoteFilesRatioTypeBean> getProteinRemoteFilesRatios() {
		List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			if (ratioEditorPanel.getAggregationLevels().isEmpty()) {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "' Define the aggregation level on the ratio tab.");
			}
			if (ratioEditorPanel.getAggregationLevels().contains(SharedAggregationLevel.PROTEIN)) {
				final RemoteFilesRatioTypeBean remoteFilesRatioTypeBean = ratioEditorPanel
						.getRemoteFilesRatioTypeBean();
				if (remoteFilesRatioTypeBean != null) {
					ret.add(remoteFilesRatioTypeBean);
				} else {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Ratio definition is not complete for ratio '"
									+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
				}
			}
		}
		return ret;
	}

	private List<ExcelAmountRatioTypeBean> getProteinExcelRatioTypeBeans() {
		List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			final ExcelAmountRatioTypeBean proteinExcelRatioTypeBean = ratioEditorPanel.getProteinExcelRatioTypeBean();
			if (proteinExcelRatioTypeBean != null) {
				ret.add(proteinExcelRatioTypeBean);
			} else {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
			}
		}
		return ret;
	}

	private List<ExcelAmountRatioTypeBean> getPeptideExcelRatioTypeBeans() {
		List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			final ExcelAmountRatioTypeBean peptideExcelRatioTypeBean = ratioEditorPanel.getPeptideExcelRatioTypeBean();
			if (peptideExcelRatioTypeBean != null) {
				ret.add(peptideExcelRatioTypeBean);
			} else {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
			}
		}
		return ret;
	}

	private List<ExcelAmountRatioTypeBean> getPSMExcelRatioTypeBeans() {
		List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		for (RatioEditorPanel ratioEditorPanel : ratioEditors) {
			final ExcelAmountRatioTypeBean psmExcelRatioTypeBean = ratioEditorPanel.getPSMExcelRatioTypeBean();
			if (psmExcelRatioTypeBean != null) {
				ret.add(psmExcelRatioTypeBean);
			} else {
				StatusReportersRegister.getInstance()
						.notifyStatusReporters("Ratio definition is not complete for ratio '"
								+ ratioEditorPanel.getTitle() + "'. Select an input file on the ratio tab.");
			}
		}
		return ret;
	}

	private void updateRepresentedObject() throws PintException {

		for (ClosableWithTitlePanel objectPanel : disclosurePanels) {
			objectPanel.updateRepresentedObject();
		}

		pintImportCfgTypeBean = projectConfigurationPanel.getPintImportCfgTypeBean();
		// file set is constructed in the server, when the files are present
		// there
		final FileSetTypeBean fileSetTypeBean = new FileSetTypeBean();
		pintImportCfgTypeBean.setFileSet(fileSetTypeBean);

		// experimental conditions
		ExperimentalConditionsTypeBean experimentalConditionsTypeBean = new ExperimentalConditionsTypeBean();
		for (ExperimentalConditionEditorPanel conditionEditorpanel : conditionEditors) {
			final ExperimentalConditionTypeBean experimentalConditionTypeBean = conditionEditorpanel
					.getExperimentalConditionTypeBean();
			if (experimentalConditionTypeBean != null)
				experimentalConditionsTypeBean.getExperimentalCondition().add(experimentalConditionTypeBean);
		}
		pintImportCfgTypeBean.getProject().setExperimentalConditions(experimentalConditionsTypeBean);

		// remote files
		final List<DataSourceBean> dataSources = projectConfigurationPanel.getDataSources();
		for (DataSourceBean dataSourceBean : dataSources) {
			// add the remote files (not the uploaded files)
			// also add the fasta files anyway in order to upload the fasta
			// digestion part
			if (FileFormat.FASTA.equals(dataSourceBean.getFormat()) || (dataSourceBean.getServer() != null
					&& dataSourceBean.getServer().isValid() && dataSourceBean.getFileName() != null
					&& dataSourceBean.getFormat() != null && dataSourceBean.getRelativePath() != null)) {
				if (pintImportCfgTypeBean.getFileSet() == null) {
					pintImportCfgTypeBean.setFileSet(new FileSetTypeBean());
				}
				final FileTypeBean fileTypeBean = new FileTypeBean();
				fileTypeBean.setFormat(FormatTypeBean.fromValue(dataSourceBean.getFormat().name()));
				fileTypeBean.setId(dataSourceBean.getId());
				fileTypeBean.setName(dataSourceBean.getFileName());
				fileTypeBean.setRelativePath(dataSourceBean.getRelativePath());
				if (dataSourceBean.getServer() != null)
					fileTypeBean.setServerRef(dataSourceBean.getServer().getId());
				fileTypeBean.setFastaDigestion(dataSourceBean.getFastaDigestionBean());
				pintImportCfgTypeBean.getFileSet().getFile().add(fileTypeBean);

				// add the server if not already present
				final ServerTypeBean server = dataSourceBean.getServer();
				if (server != null) {
					if (pintImportCfgTypeBean.getServers() == null) {
						pintImportCfgTypeBean.setServers(new ServersTypeBean());
					}
					boolean serverFound = false;
					for (ServerTypeBean server2 : pintImportCfgTypeBean.getServers().getServer()) {
						if (server2.getId().equals(server.getId())) {
							serverFound = true;
							break;
						}
					}
					if (!serverFound) {
						pintImportCfgTypeBean.getServers().getServer().add(server);
					}
				}
			}

		}

		// ratios
		RatiosTypeBean ratiosTypeBean = new RatiosTypeBean();

		// PSM ratios
		final PsmRatiosTypeBean psmRatiosTypeBean = new PsmRatiosTypeBean();
		// PSM Excel ratios
		final List<ExcelAmountRatioTypeBean> psmExcelRatioTypeBeans = getPSMExcelRatioTypeBeans();
		if (!psmExcelRatioTypeBeans.isEmpty()) {
			psmRatiosTypeBean.getExcelRatio().addAll(psmExcelRatioTypeBeans);
		}
		// PSM Remote ratios
		final List<RemoteFilesRatioTypeBean> psmRemoteFilesRatios = getPSMRemoteFilesRatios();
		psmRatiosTypeBean.getRemoteFilesRatio().addAll(psmRemoteFilesRatios);
		if (!psmExcelRatioTypeBeans.isEmpty() || !psmRemoteFilesRatios.isEmpty())
			ratiosTypeBean.setPsmAmountRatios(psmRatiosTypeBean);

		// Peptide ratios
		final PeptideRatiosTypeBean peptideRatiosTypeBean = new PeptideRatiosTypeBean();
		// Peptide Excel ratios
		final List<ExcelAmountRatioTypeBean> peptideExcelRatioTypeBeans = getPeptideExcelRatioTypeBeans();
		if (!peptideExcelRatioTypeBeans.isEmpty()) {
			peptideRatiosTypeBean.getExcelRatio().addAll(peptideExcelRatioTypeBeans);
		}
		// peptide remote ratios
		final List<RemoteFilesRatioTypeBean> peptideRemoteFilesRatios = getPeptideRemoteFilesRatios();
		peptideRatiosTypeBean.getRemoteFilesRatio().addAll(peptideRemoteFilesRatios);
		if (!peptideExcelRatioTypeBeans.isEmpty() || !peptideRemoteFilesRatios.isEmpty()) {
			ratiosTypeBean.setPeptideAmountRatios(peptideRatiosTypeBean);
		}

		// Protein ratios
		final ProteinRatiosTypeBean proteinRatiosTypeBean = new ProteinRatiosTypeBean();
		// Protein Excel ratios
		final List<ExcelAmountRatioTypeBean> proteinExcelRatioTypeBeans = getProteinExcelRatioTypeBeans();
		if (!proteinExcelRatioTypeBeans.isEmpty()) {
			proteinRatiosTypeBean.getExcelRatio().addAll(proteinExcelRatioTypeBeans);
		}
		// protein remote ratios
		final List<RemoteFilesRatioTypeBean> proteinRemoteFilesRatios = getProteinRemoteFilesRatios();
		proteinRatiosTypeBean.getRemoteFilesRatio().addAll(proteinRemoteFilesRatios);
		if (!proteinRatiosTypeBean.getExcelRatio().isEmpty() || !proteinRemoteFilesRatios.isEmpty()) {
			ratiosTypeBean.setProteinAmountRatios(proteinRatiosTypeBean);
		}
		// throw an error if there is no ratios, but instead, there are some
		// ratio editor panels
		if (ratiosTypeBean.getPeptideAmountRatios() == null && ratiosTypeBean.getPsmAmountRatios() == null
				&& ratiosTypeBean.getProteinAmountRatios() == null) {
			for (ClosableWithTitlePanel disclosurePanel : disclosurePanels) {
				if (disclosurePanel instanceof RatioDisclosurePanel) {
					throw new PintException(
							"Ratio definition: '" + disclosurePanel.getID()
									+ "' is not well defined.\nCheck the information and try to save again.",
							PINT_ERROR_TYPE.MISSING_INFORMATION);
				}
			}
		}
		// ratios
		pintImportCfgTypeBean.getProject().setRatios(ratiosTypeBean);

	}

	@Override
	public void initialize() {
		askQuestionToUser();
	}
}
