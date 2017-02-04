package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
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
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;
import edu.scripps.yates.shared.util.SharedDataUtils;

/**
 * Represents a children of an quantification_info (
 * {@link QuantificationInfoType} ) that can be a {@link RemoteInfoType} or an
 * {@link QuantificationExcelType}
 *
 * @author Salva
 *
 */
public class QuantificationInfoEditorPanel extends Composite implements ReferencesDataObject, ContainsImportJobID {
	private final CheckBox chckbxUseFastaFile;
	private final ListBox fastaFilelistBox;
	private final ListBox otherFilesListBox;
	private final ListBox excelFilesListBox;
	private final SimplePanel simplePanel;
	private QuantificationInfoFromExcelFilePanel quantificationInfoExcelPanel;
	private InfoFromRemoteFilePanel quantificationInfoRemoteFilePanel;
	private int importJobID;
	private final String sessionID;

	public QuantificationInfoEditorPanel(String sessionID, int importJobID) {
		this.sessionID = sessionID;

		this.importJobID = importJobID;

		ScrollPanel scroll = new ScrollPanel();
		FlexTable mainPanel = new FlexTable();
		scroll.setWidget(mainPanel);
		initWidget(scroll);
		scroll.setSize("100%", "100%");
		mainPanel.setSize("100%", "100%");

		Grid grid_1 = new Grid(4, 3);
		grid_1.setBorderWidth(0);
		mainPanel.setWidget(0, 0, grid_1);
		grid_1.setWidth("100%");
		grid_1.setCellSpacing(4);

		Label lblNewLabel = new Label("Uploaded Excel files:");
		grid_1.setWidget(0, 0, lblNewLabel);

		excelFilesListBox = new ListBox();
		grid_1.setWidget(0, 1, excelFilesListBox);
		excelFilesListBox.setVisibleItemCount(1);
		grid_1.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		grid_1.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblRemoteDataSources = new Label("Other files:");
		grid_1.setWidget(1, 0, lblRemoteDataSources);

		otherFilesListBox = new ListBox();
		grid_1.setWidget(1, 1, otherFilesListBox);
		otherFilesListBox.setVisibleItemCount(1);

		chckbxUseFastaFile = new CheckBox("Use fasta file");
		chckbxUseFastaFile.setHTML("Use fasta file:");

		grid_1.setWidget(2, 0, chckbxUseFastaFile);

		fastaFilelistBox = new ListBox();
		fastaFilelistBox.setEnabled(false);
		grid_1.setWidget(2, 1, fastaFilelistBox);
		fastaFilelistBox.setVisibleItemCount(1);
		fastaFilelistBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				fastaFileSelected();

			}
		});
		grid_1.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblexcelOrRemote = new Label("(Include fasta files for applying protein inference)");
		lblexcelOrRemote
				.setTitle("Input file should include protein-peptide mappings. However, for example in DTASelect, "
						+ "depending on the option the user used to generated the file, not all the proteins that a "
						+ "peptide maps to are included in the output file.\n "
						+ "In order to avoid missing mapping between peptides and proteins, the user can here to select a FASTA file.\n"
						+ "The FASTA file is suppose to be the one used in the search of the peptides.");
		grid_1.setWidget(3, 2, lblexcelOrRemote);
		grid_1.getCellFormatter().setHorizontalAlignment(3, 2, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_RIGHT);

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
		// if checkBox use fasta is selected, enable fastaFileBox
		chckbxUseFastaFile.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				final Boolean selected = event.getValue();
				fastaFilelistBox.setEnabled(selected);
				// if not enabled, then select the empty
				if (!selected && fastaFilelistBox.getItemCount() > 0) {
					fastaFilelistBox.setSelectedIndex(0);
				}
			}
		});

		// set a IdentificationInfoFromExcel if selected an excel
		excelFilesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				excelFileSelected();
			}
		});

		// set a IdentificationInfoFromRemote if selected a remote file or
		// uploaded a file
		otherFilesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				remoteDataSourceSelected();
			}
		});

		fastaFilelistBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				fastaFileSelected();
			}
		});
	}

	private void excelFileSelected() {
		if (quantificationInfoExcelPanel != null)
			quantificationInfoExcelPanel.resetData();
		if (excelFilesListBox.getSelectedIndex() == 0) {
			if (quantificationInfoExcelPanel != null)
				quantificationInfoExcelPanel.removeFromParent();
			resizeParentWindowBox();
		} else {
			// set the remote file to the panel
			final Integer selectedExcelFileInternalID = Integer
					.valueOf(excelFilesListBox.getValue(excelFilesListBox.getSelectedIndex()));
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(selectedExcelFileInternalID);
			if (projectObjectRepresenter != null && projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel dataSourceDisclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				// take the remoteFileWithType from the
				FileNameWithTypeBean fileBean = dataSourceDisclosurePanel.getFileNameWithTypeBean();
				if (fileBean != null) {
					if (quantificationInfoExcelPanel == null) {
						quantificationInfoExcelPanel = new QuantificationInfoFromExcelFilePanel(sessionID, importJobID);
					}
					if (quantificationInfoExcelPanel.setFileNameWithTypeBean(fileBean))
						quantificationInfoExcelPanel.startCheck();
				}
			}
			setContent(quantificationInfoExcelPanel);
			if (quantificationInfoRemoteFilePanel != null) {
				quantificationInfoRemoteFilePanel.removeFromParent();
			}

		}
	}

	private void fastaFileSelected() {
		if (quantificationInfoRemoteFilePanel != null) {
			final Integer selectedfastaFileInternalID = Integer
					.valueOf(fastaFilelistBox.getValue(fastaFilelistBox.getSelectedIndex()));
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(selectedfastaFileInternalID);
			FileNameWithTypeBean fastaFileBean = null;
			if (projectObjectRepresenter != null && projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel dataSourceDisclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				// take the remoteFileWithType from the
				fastaFileBean = dataSourceDisclosurePanel.getFileNameWithTypeBean();
			}
			// quantificationInfoRemoteFilePanel.setFastaFileBean(fastaFileBean);
		}

	}

	private void remoteDataSourceSelected() {
		if (quantificationInfoRemoteFilePanel != null)
			quantificationInfoRemoteFilePanel.resetData();
		if (otherFilesListBox.getSelectedIndex() == 0) {
			if (quantificationInfoRemoteFilePanel != null)
				quantificationInfoRemoteFilePanel.removeFromParent();
		} else {
			final Integer selectedRemoteFileInternalID = Integer
					.valueOf(otherFilesListBox.getValue(otherFilesListBox.getSelectedIndex()));
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(selectedRemoteFileInternalID);
			if (projectObjectRepresenter != null && projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel dataSourceDisclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				if (quantificationInfoRemoteFilePanel == null) {
					quantificationInfoRemoteFilePanel = new InfoFromRemoteFilePanel(sessionID, importJobID);
				}
				quantificationInfoRemoteFilePanel.setFileNameWithTypeProvider(dataSourceDisclosurePanel);

			}
			setContent(quantificationInfoRemoteFilePanel);
			if (quantificationInfoExcelPanel != null) {
				quantificationInfoExcelPanel.removeFromParent();
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

		updateExcelFileCombo();
		updateRemoteFileCombo();
		updateFastaFileCombo();
	}

	private void updateFastaFileCombo() {
		// update the fasta files combo
		ProjectCreatorWizardUtil.updateCombo(fastaFilelistBox, DataSourceBean.class);
		// as the updataCombo will add all types of DataSourceBeans to the
		// combo:

		// remove the ones that are not fasta files
		boolean itemRemoved = true;
		while (itemRemoved) {
			itemRemoved = false;
			for (int index = 1; index < fastaFilelistBox.getItemCount(); index++) {
				Integer excelFileInternalID = Integer.valueOf(fastaFilelistBox.getValue(index));
				if (excelFileInternalID != null) {
					final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
							.getProjectObjectRepresenter(excelFileInternalID);
					if (projectObjectRepresenter != null
							&& projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
						DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
						final FileFormat format = disclosurePanel.getObject() != null
								? disclosurePanel.getObject().getFormat() : null;
						if (!FileFormat.FASTA.equals(format)) {
							fastaFilelistBox.removeItem(index);
							itemRemoved = true;
						}
					}
				}
			}
		}
		fastaFileSelected();
	}

	private void updateRemoteFileCombo() {
		// update the remote data files combo
		ProjectCreatorWizardUtil.updateCombo(otherFilesListBox, DataSourceBean.class);
		// as the updataCombo will add all types of DataSourceBeans to the
		// combo:

		// remove the ones that are not dtaselectfilters or censuscrho
		boolean itemRemoved = true;
		while (itemRemoved) {
			itemRemoved = false;
			for (int index = 1; index < otherFilesListBox.getItemCount(); index++) {
				Integer excelFileInternalID = Integer.valueOf(otherFilesListBox.getValue(index));
				if (excelFileInternalID != null) {
					final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
							.getProjectObjectRepresenter(excelFileInternalID);
					if (projectObjectRepresenter != null
							&& projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
						DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
						final FileFormat format = disclosurePanel.getObject() != null
								? disclosurePanel.getObject().getFormat() : null;
						if (format == null || (format != null && !format.isDataFile())) {
							otherFilesListBox.removeItem(index);
							itemRemoved = true;
						}
					}
				}
			}
		}
		remoteDataSourceSelected();
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
		excelFileSelected();
	}

	public QuantificationExcelTypeBean getQuantificationExcelTypeBean() {
		if (quantificationInfoExcelPanel != null) {
			return quantificationInfoExcelPanel.getObject();
		}
		return null;
	}

	public RemoteInfoTypeBean getRemoteInfoTypeBean() {
		// if (otherFilesListBox.getSelectedIndex() > 0) {
		if (quantificationInfoRemoteFilePanel != null) {
			final RemoteInfoTypeBean remoteInfoTypeBean = quantificationInfoRemoteFilePanel.getRemoteInfoTypeBean();
			if (fastaFilelistBox.getSelectedIndex() > 0) {
				final String fastaFileID = fastaFilelistBox.getItemText(fastaFilelistBox.getSelectedIndex());
				remoteInfoTypeBean.getFileRefs().add(fastaFileID);

			}
			if (otherFilesListBox.getSelectedIndex() > 0) {
				final String otherFileID = otherFilesListBox.getItemText(otherFilesListBox.getSelectedIndex());
				remoteInfoTypeBean.getFileRefs().add(otherFileID);
			}
			return remoteInfoTypeBean;
		}
		// }
		return null;
	}

	public void setQuantificationExcelTypeBean(QuantificationExcelTypeBean quantificationExcelTypeBean) {

		if (quantificationExcelTypeBean != null) {
			if (quantificationInfoExcelPanel == null) {
				quantificationInfoExcelPanel = new QuantificationInfoFromExcelFilePanel(sessionID, importJobID);
			}

			// if an excel file is selected in protein accession, take the
			// fileNameWithType and apply to the panel
			String columnRef = null;
			if (quantificationExcelTypeBean.getProteinAmounts() != null) {
				for (AmountTypeBean amountType : quantificationExcelTypeBean.getProteinAmounts()) {
					columnRef = amountType.getColumnRef();
					break;
				}
			}
			if (columnRef == null && quantificationExcelTypeBean.getPsmAmounts() != null) {
				for (AmountTypeBean amountType : quantificationExcelTypeBean.getPsmAmounts()) {
					columnRef = amountType.getColumnRef();
					break;
				}
			}
			if (columnRef == null && quantificationExcelTypeBean.getPeptideAmounts() != null) {
				for (AmountTypeBean amountType : quantificationExcelTypeBean.getPeptideAmounts()) {
					columnRef = amountType.getColumnRef();
					break;
				}
			}
			if (columnRef != null) {
				FileNameWithTypeBean fileNameWithTypeBean = SharedDataUtils
						.getFileNameWithTypeFromFileComboSelection(excelFilesListBox, columnRef);
				if (fileNameWithTypeBean != null) {
					quantificationInfoExcelPanel.setFileNameWithTypeBean(fileNameWithTypeBean);
				}
				final String excelFileID = ExcelColumnRefPanel.getExcelFileID(columnRef);
				if (excelFileID != null) {
					ProjectCreatorWizardUtil.selectInCombo(excelFilesListBox, excelFileID);
					// fire select excel file
					excelFileSelected();
				}
			}
			quantificationInfoExcelPanel.updateGUIFromObjectData(quantificationExcelTypeBean);

		} else {
			quantificationInfoExcelPanel.removeFromParent();
		}
	}

	public void setRemoteInfoTypeBean(RemoteInfoTypeBean remoteInfoTypeBean) {
		if (remoteInfoTypeBean != null) {
			if (quantificationInfoRemoteFilePanel == null) {
				quantificationInfoRemoteFilePanel = new InfoFromRemoteFilePanel(sessionID, importJobID);
			}
			final List<String> fileRefs = remoteInfoTypeBean.getFileRefs();
			for (String fileRef : fileRefs) {
				// look in the remote file list
				final boolean selectInCombo = ProjectCreatorWizardUtil.selectInCombo(otherFilesListBox, fileRef);
				if (selectInCombo) {
					remoteDataSourceSelected();
					continue;
				}
				// look in the fasta file list
				final boolean selectInCombo2 = ProjectCreatorWizardUtil.selectInCombo(fastaFilelistBox, fileRef);
				if (selectInCombo2) {
					// enable checkbox
					chckbxUseFastaFile.setValue(true);
					fastaFilelistBox.setEnabled(true);
					fastaFileSelected();
				}
			}
			quantificationInfoRemoteFilePanel.updateGUIFromObjectData(remoteInfoTypeBean);

		} else {
			ProjectCreatorWizardUtil.selectInCombo(otherFilesListBox, "");
			ProjectCreatorWizardUtil.selectInCombo(fastaFilelistBox, "");
			chckbxUseFastaFile.setValue(false);
			quantificationInfoRemoteFilePanel.removeFromParent();
		}
	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;
		if (quantificationInfoExcelPanel != null)
			quantificationInfoExcelPanel.setImportJobID(importJobID);
		if (quantificationInfoRemoteFilePanel != null)
			quantificationInfoRemoteFilePanel.setImportJobID(importJobID);
	}

	public void addChangeHandlerForComboBoxes(ChangeHandler handler) {
		excelFilesListBox.addChangeHandler(handler);
		fastaFilelistBox.addChangeHandler(handler);
		otherFilesListBox.addChangeHandler(handler);
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

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#removeFromParent()
	 */
	@Override
	public void unregisterAsListener() {
		if (quantificationInfoExcelPanel != null)
			quantificationInfoExcelPanel.unregisterAsListener();
		if (quantificationInfoRemoteFilePanel != null)
			quantificationInfoRemoteFilePanel.unregisterAsListener();
		ProjectCreatorRegister.deleteListener(this);
	}
}
