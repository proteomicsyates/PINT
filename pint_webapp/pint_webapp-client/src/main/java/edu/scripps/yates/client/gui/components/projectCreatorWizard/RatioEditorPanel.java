package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.DataSourceDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;

public class RatioEditorPanel extends Composite implements ReferencesDataObject, ContainsImportJobID {
	private ListBox remoteDataSourceListBox;
	private ListBox excelFilesListBox;
	private SimplePanel simplePanel;
	private RatioInfoFromExcelFilePanel ratioInfoExcelPanel;
	private RatioFromRemoteFilePanel ratioInfoRemoteFilePanel;

	private int importJobID;
	private TabNameChanger tabNameChanger;
	private Label denominatorConditionLabel;
	private Label numeratorConditionLabel;
	private final RatioDescriptorTypeBean ratioDescriptorTypeBean;
	private final RemoteFilesRatioTypeBean remoteFilesRatioBean;
	private final ExcelAmountRatioTypeBean excelRatioBean;
	private String sessionID;
	private final int referencedDisclosurePanelID;
	private Set<SharedAggregationLevel> aggregationLevels = new HashSet<SharedAggregationLevel>();

	public RatioEditorPanel(String sessionID, int importJobID, RatioDescriptorTypeBean ratioDescriptorTypeBean,
			int referencedRatioDisclosurePanelID, Collection<SharedAggregationLevel> aggregationLevels) {
		if (aggregationLevels != null) {
			this.aggregationLevels.addAll(aggregationLevels);
		}
		this.sessionID = sessionID;
		this.importJobID = importJobID;
		initRatioEditorPanel();
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		remoteFilesRatioBean = null;
		excelRatioBean = null;
		referencedDisclosurePanelID = referencedRatioDisclosurePanelID;
		// register as a listener of that ratio
		ProjectCreatorRegister.registerAsListenerByObjectID(referencedDisclosurePanelID, this);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public RatioEditorPanel(int importJobID, RemoteFilesRatioTypeBean remoteFilesRatioBean,
			RatioDescriptorTypeBean ratioDescriptorTypeBean, int referencedRatioDisclosurePanelID,
			Collection<SharedAggregationLevel> aggregationLevels) {
		if (aggregationLevels != null) {
			this.aggregationLevels.addAll(aggregationLevels);
		}
		this.importJobID = importJobID;
		referencedDisclosurePanelID = referencedRatioDisclosurePanelID;
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		this.remoteFilesRatioBean = remoteFilesRatioBean;
		excelRatioBean = null;
		initRatioEditorPanel();

		if (remoteFilesRatioBean.getFileRef() != null) {
			// select remote file in combo
			ProjectCreatorWizardUtil.selectInCombo(remoteDataSourceListBox, remoteFilesRatioBean.getFileRef());
			remoteDataSourceSelected(aggregationLevels);
			if (ratioInfoRemoteFilePanel == null) {
				ratioInfoRemoteFilePanel = new RatioFromRemoteFilePanel(sessionID, importJobID,
						ratioDescriptorTypeBean);
			}
			ratioInfoRemoteFilePanel.updateGUIFromObjectData(remoteFilesRatioBean);
			if (aggregationLevels != null) {
				ratioInfoRemoteFilePanel.setAggregationLevels(aggregationLevels);
			}
		}
		// register as a listener of that ratio
		ProjectCreatorRegister.registerAsListenerByObjectID(referencedDisclosurePanelID, this);

	}

	public RatioEditorPanel(int importJobID, ExcelAmountRatioTypeBean excelRatioBean,
			SharedAggregationLevel aggregationLevel, RatioDescriptorTypeBean ratioDescriptorTypeBean,
			int referencedRatioDisclosurePanelID) {
		if (aggregationLevels != null) {
			aggregationLevels.addAll(aggregationLevels);
		}
		this.importJobID = importJobID;
		referencedDisclosurePanelID = referencedRatioDisclosurePanelID;
		this.ratioDescriptorTypeBean = ratioDescriptorTypeBean;
		remoteFilesRatioBean = null;
		this.excelRatioBean = excelRatioBean;
		initRatioEditorPanel();
		if (excelRatioBean.getColumnRef() != null) {
			// select excel file in combo
			final String excelFileRef = ExcelColumnRefPanel.getExcelFileID(excelRatioBean.getColumnRef());
			ProjectCreatorWizardUtil.selectInCombo(excelFilesListBox, excelFileRef);
			excelFileSelected(aggregationLevel);
			if (ratioInfoExcelPanel == null) {
				ratioInfoExcelPanel = new RatioInfoFromExcelFilePanel(sessionID, importJobID, excelRatioBean,
						ratioDescriptorTypeBean);
			}
			ratioInfoExcelPanel.updateGUIFromObjectData(excelRatioBean);
		}
		// register as a listener of that ratio
		ProjectCreatorRegister.registerAsListenerByObjectID(referencedDisclosurePanelID, this);

	}

	private void initRatioEditorPanel() {
		ScrollPanel scroll = new ScrollPanel();
		FlexTable mainPanel = new FlexTable();
		scroll.setWidget(mainPanel);
		initWidget(scroll);
		scroll.setSize("100%", "100%");
		mainPanel.setSize("100%", "100%");

		Grid grid_1 = new Grid(5, 2);
		grid_1.setBorderWidth(0);
		mainPanel.setWidget(0, 0, grid_1);
		// grid_1.setWidth("100%");
		grid_1.setCellSpacing(4);

		Label lblRatioEditor = new Label("Ratio editor:");
		grid_1.setWidget(0, 0, lblRatioEditor);

		Label lblNumerator = new Label("Numerator:");
		grid_1.setWidget(1, 0, lblNumerator);

		numeratorConditionLabel = new Label("-");
		grid_1.setWidget(1, 1, numeratorConditionLabel);

		Label lblDenominator = new Label("Denominator:");
		grid_1.setWidget(2, 0, lblDenominator);

		denominatorConditionLabel = new Label("-");
		grid_1.setWidget(2, 1, denominatorConditionLabel);

		Label lblNewLabel = new Label("Uploaded Excel files:");
		grid_1.setWidget(3, 0, lblNewLabel);

		excelFilesListBox = new ListBox();
		grid_1.setWidget(3, 1, excelFilesListBox);
		excelFilesListBox.setVisibleItemCount(1);
		grid_1.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblRemoteDataSources = new Label("Remote data sources:");
		grid_1.setWidget(4, 0, lblRemoteDataSources);

		remoteDataSourceListBox = new ListBox();
		grid_1.setWidget(4, 1, remoteDataSourceListBox);
		remoteDataSourceListBox.setVisibleItemCount(1);

		grid_1.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);

		simplePanel = new SimplePanel();
		simplePanel.setStyleName("ExperimentalConditionEditorPanel_IdentificationData");
		mainPanel.setWidget(1, 0, simplePanel);
		simplePanel.setWidth("100%");
		mainPanel.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		mainPanel.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

		addHandlers();

		// listen to changes on data sources
		ProjectCreatorRegister.registerAsListenerByObjectClass(DataSourceBean.class, this);

		updateGUIReferringToDataObjects();
	}

	private void addHandlers() {

		// set a IdentificationInfoFromExcel if selected an excel
		excelFilesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				SharedAggregationLevel aggregationLevel = null; // unknown yet
				excelFileSelected(aggregationLevel);
			}
		});

		// set a IdentificationInfoFromRemote if selected a remote file or
		// uploaded a file
		remoteDataSourceListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Set<SharedAggregationLevel> aggregationLevels = new HashSet<SharedAggregationLevel>(); // unknown
																										// yet
				remoteDataSourceSelected(aggregationLevels);
			}
		});
	}

	private void excelFileSelected(SharedAggregationLevel aggregrationLevel) {
		if (ratioInfoExcelPanel != null)
			ratioInfoExcelPanel.resetData();
		if (excelFilesListBox.getSelectedIndex() == 0) {
			if (ratioInfoExcelPanel != null)
				ratioInfoExcelPanel.removeFromParent();
			resizeParentWindowBox();
		} else {
			// set the excel file to the panel
			final Integer selectedExcelFileInternalID = Integer
					.valueOf(excelFilesListBox.getValue(excelFilesListBox.getSelectedIndex()));
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(selectedExcelFileInternalID);
			if (projectObjectRepresenter != null && projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel dataSourceDisclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				// take the remoteFileWithType from the
				FileNameWithTypeBean fileBean = dataSourceDisclosurePanel.getFileNameWithTypeBean();
				if (fileBean != null) {
					if (ratioInfoExcelPanel == null) {
						ratioInfoExcelPanel = new RatioInfoFromExcelFilePanel(sessionID, importJobID,
								ratioDescriptorTypeBean);
					}
					if (aggregrationLevel != null) {
						ratioInfoExcelPanel.setAggregationLevel(aggregrationLevel);
					}
					if (ratioInfoExcelPanel.setRemoteFileBean(fileBean)) {
						ratioInfoExcelPanel.startCheck();
					}
				}
			}
			setContent(ratioInfoExcelPanel);
			if (ratioInfoRemoteFilePanel != null) {
				ratioInfoRemoteFilePanel.removeFromParent();
			}
		}

	}

	private void remoteDataSourceSelected(Collection<SharedAggregationLevel> aggregationLevels) {
		if (ratioInfoRemoteFilePanel != null)
			ratioInfoRemoteFilePanel.resetData();

		if (remoteDataSourceListBox.getSelectedIndex() == 0) {
			if (ratioInfoRemoteFilePanel != null)
				ratioInfoRemoteFilePanel.removeFromParent();
			resizeParentWindowBox();
		} else {
			final Integer selectedRemoteFileInternalID = Integer
					.valueOf(remoteDataSourceListBox.getValue(remoteDataSourceListBox.getSelectedIndex()));
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(selectedRemoteFileInternalID);
			if (projectObjectRepresenter != null && projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel dataSourceDisclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				// take the remoteFileWithType from the
				final FileNameWithTypeBean fileBean = dataSourceDisclosurePanel.getFileNameWithTypeBean();
				if (fileBean != null) {
					if (ratioInfoRemoteFilePanel == null) {
						ratioInfoRemoteFilePanel = new RatioFromRemoteFilePanel(sessionID, importJobID,
								ratioDescriptorTypeBean);
					}
					if (aggregationLevels != null) {
						ratioInfoRemoteFilePanel.setAggregationLevels(aggregationLevels);
					}
					// if (
					ratioInfoRemoteFilePanel.setRemoteFileBean(fileBean);// )
					// automatically starts the checking
					// ratioInfoRemoteFilePanel.startCheck();
				}
			}
			setContent(ratioInfoRemoteFilePanel);
			if (ratioInfoExcelPanel != null) {
				ratioInfoExcelPanel.removeFromParent();
			}
		}
	}

	protected void setContent(Widget widget) {
		final Widget oldWidget = simplePanel.getWidget();
		simplePanel.setWidget(widget);
		if (oldWidget == null || !oldWidget.equals(widget))
			resizeParentWindowBox();
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		if (tabNameChanger != null) {
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(this);
			if (projectObjectRepresenter != null) {
				final String newRatioName = projectObjectRepresenter.getID();
				tabNameChanger.changeTabName(newRatioName);
			}
		}
		updateExcelFileCombo();
		updateRemoteFileCombo();

		// update numerator and denominator labels
		updateRatioConditionLabels();

	}

	private void updateRatioConditionLabels() {
		String condition1ID = null;
		String condition2ID = null;
		if (ratioDescriptorTypeBean != null && ratioDescriptorTypeBean.getCondition1() != null) {
			condition1ID = ratioDescriptorTypeBean.getCondition1().getId();
		} else if (excelRatioBean != null && excelRatioBean.getNumerator() != null) {
			condition1ID = excelRatioBean.getNumerator();
		} else if (remoteFilesRatioBean != null && remoteFilesRatioBean.getNumerator() != null) {
			condition1ID = remoteFilesRatioBean.getNumerator();
		} else {
			condition1ID = "-";
		}
		if (ratioDescriptorTypeBean != null && ratioDescriptorTypeBean.getCondition2() != null) {
			condition2ID = ratioDescriptorTypeBean.getCondition2().getId();
		} else if (excelRatioBean != null && excelRatioBean.getDenominator() != null) {
			condition2ID = excelRatioBean.getDenominator();
		} else if (remoteFilesRatioBean != null && remoteFilesRatioBean.getDenominator() != null) {
			condition2ID = remoteFilesRatioBean.getDenominator();
		} else {
			condition2ID = "-";
		}

		numeratorConditionLabel.setText(condition1ID);
		denominatorConditionLabel.setText(condition2ID);

	}

	private void updateRemoteFileCombo() {
		// update the remote data files combo
		ProjectCreatorWizardUtil.updateCombo(remoteDataSourceListBox, DataSourceBean.class);
		// as the updataCombo will add all types of DataSourceBeans to the
		// combo:

		// remove the ones that are not censuscrho
		boolean itemRemoved = true;
		while (itemRemoved) {
			itemRemoved = false;
			for (int index = 1; index < remoteDataSourceListBox.getItemCount(); index++) {
				Integer excelFileInternalID = Integer.valueOf(remoteDataSourceListBox.getValue(index));
				if (excelFileInternalID != null) {
					final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
							.getProjectObjectRepresenter(excelFileInternalID);
					if (projectObjectRepresenter != null
							&& projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
						DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
						if (disclosurePanel.getObject() != null) {
							final FileFormat format = disclosurePanel.getObject() != null
									? disclosurePanel.getObject().getFormat() : null;
							if (format == null || (format != null && !format.isQuantitativeData())) {
								remoteDataSourceListBox.removeItem(index);
								itemRemoved = true;
							}
						}
					}
				}
			}
		}
		remoteDataSourceSelected(null);
	}

	private void updateExcelFileCombo() {
		// update the Excel files combo
		ProjectCreatorWizardUtil.updateCombo(excelFilesListBox, DataSourceBean.class);
		// as the updataCombo will add all types of DataSourceBeans to the
		// combo:

		// remove the ones that are not excel files
		boolean itemRemoved = true;
		while (itemRemoved) {
			itemRemoved = false;
			for (int index = 1; index < excelFilesListBox.getItemCount(); index++) {
				Integer excelFileInternalID = Integer.valueOf(excelFilesListBox.getValue(index));
				if (excelFileInternalID != null) {
					final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
							.getProjectObjectRepresenter(excelFileInternalID);
					if (projectObjectRepresenter != null
							&& projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
						DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
						final FileFormat format = disclosurePanel.getObject() != null
								? disclosurePanel.getObject().getFormat() : null;
						if (!FileFormat.EXCEL.equals(format)) {
							excelFilesListBox.removeItem(index);
							itemRemoved = true;
						}
					}
				}
			}
		}
		excelFileSelected(null);
	}

	public RemoteFilesRatioTypeBean getRemoteFilesRatioTypeBean() {
		if (ratioInfoRemoteFilePanel != null) {
			if (remoteDataSourceListBox.getSelectedIndex() > 0) {
				String condition1ID = null;
				String condition2ID = null;
				String id = null;
				final RemoteFilesRatioTypeBean remoteFileRatioInfoBean = ratioInfoRemoteFilePanel.getObject();
				if (ratioDescriptorTypeBean != null) {
					if (ratioDescriptorTypeBean.getCondition1() != null) {
						condition1ID = ratioDescriptorTypeBean.getCondition1().getId();
					}
					if (ratioDescriptorTypeBean.getCondition2() != null) {
						condition2ID = ratioDescriptorTypeBean.getCondition2().getId();
					}
					id = ratioDescriptorTypeBean.getId();
				}
				if (remoteFilesRatioBean != null) {
					condition1ID = remoteFilesRatioBean.getNumerator();
					condition2ID = remoteFilesRatioBean.getDenominator();
					id = remoteFilesRatioBean.getId();
				}
				if (excelRatioBean != null) {
					condition1ID = excelRatioBean.getNumerator();
					condition2ID = excelRatioBean.getDenominator();
					id = excelRatioBean.getId();
				}

				remoteFileRatioInfoBean.setNumerator(condition1ID);
				remoteFileRatioInfoBean.setDenominator(condition2ID);
				remoteFileRatioInfoBean.setId(id);

				return remoteFileRatioInfoBean;
			}
		}
		return null;
	}

	public ExcelAmountRatioTypeBean getProteinExcelRatioTypeBean() {
		if (ratioInfoExcelPanel != null) {
			if (excelFilesListBox.getSelectedIndex() > 0)
				return ratioInfoExcelPanel.getProteinExcelRatioTypeBean();
		}
		return null;
	}

	public ExcelAmountRatioTypeBean getPSMExcelRatioTypeBean() {
		if (ratioInfoExcelPanel != null) {
			if (excelFilesListBox.getSelectedIndex() > 0)
				return ratioInfoExcelPanel.getPSMExcelRatioTypeBean();
		}
		return null;
	}

	public ExcelAmountRatioTypeBean getPeptideExcelRatioTypeBean() {
		if (ratioInfoExcelPanel != null) {
			if (excelFilesListBox.getSelectedIndex() > 0)
				return ratioInfoExcelPanel.getPeptideExcelRatioTypeBean();
		}
		return null;
	}

	/**
	 * Sets the {@link TabNameChanger} which will be called when
	 * updateGUIReferringToDataObjects is called.
	 *
	 * @param tabNameChanger
	 *            the tabNameChanger to set
	 */
	public void setTabNameChanger(TabNameChanger tabNameChanger) {
		this.tabNameChanger = tabNameChanger;
	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;
		if (ratioInfoExcelPanel != null)
			ratioInfoExcelPanel.setImportJobID(importJobID);
		if (ratioInfoRemoteFilePanel != null)
			ratioInfoRemoteFilePanel.setImportJobID(importJobID);
	}

	private void resizeParentWindowBox() {
		Widget parentWidget = getParent();
		while (parentWidget != null && !(parentWidget instanceof WindowBox)) {
			parentWidget = parentWidget.getParent();
		}
		if (parentWidget != null && parentWidget instanceof WindowBox) {
			((WindowBox) parentWidget).resize();
		}
	}

	@Override
	public void unregisterAsListener() {
		if (ratioInfoExcelPanel != null)
			ratioInfoExcelPanel.unregisterAsListener();
		if (ratioInfoRemoteFilePanel != null)
			ratioInfoRemoteFilePanel.unregisterAsListener();
		ProjectCreatorRegister.deleteListener(this);
	}

	/**
	 * @return the aggregationLevel
	 */
	public Set<SharedAggregationLevel> getAggregationLevels() {
		if (ratioInfoRemoteFilePanel != null) {
			aggregationLevels = ratioInfoRemoteFilePanel.getSelectedAggregationLevels();
		}
		if (ratioInfoExcelPanel != null) {
			aggregationLevels.clear();
			aggregationLevels.add(ratioInfoExcelPanel.getAggregationLevel());
		}
		return aggregationLevels;
	}

}
