package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ExcelColumnRefPanel extends Composite {
	private final List<FileTypeBean> excelFileBeans = new ArrayList<FileTypeBean>();
	private final ListBox comboBoxFiles;
	private final ListBox comboBoxSheets;
	private final ListBox comboBoxColumns;
	private final Logger log = Logger.getLogger("PINT");

	public ExcelColumnRefPanel(FileTypeBean excelFileBean) {
		if (excelFileBean != null)
			excelFileBeans.add(excelFileBean);
		final FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);

		final FlexTable grid = new FlexTable();
		grid.setStyleName("ExcelColumnRefPanel");
		grid.setCellPadding(5);
		mainPanel.add(grid);

		final Label lblFile = new Label("File:");
		grid.setWidget(0, 0, lblFile);

		comboBoxFiles = new ListBox();
		grid.setWidget(0, 1, comboBoxFiles);
		comboBoxFiles.setVisibleItemCount(1);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);

		final Label lblSheet = new Label("Sheet:");
		grid.setWidget(1, 0, lblSheet);

		comboBoxSheets = new ListBox();
		grid.setWidget(1, 1, comboBoxSheets);

		final Label lblColumn = new Label("Column:");
		grid.setWidget(2, 0, lblColumn);

		comboBoxColumns = new ListBox();
		grid.setWidget(2, 1, comboBoxColumns);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		addHandlers();
		loadData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#removeFromParent()
	 */
	@Override
	public void removeFromParent() {
		// TODO Auto-generated method stub
		super.removeFromParent();
	}

	private void addHandlers() {
		comboBoxFiles.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				final Set<String> selectedValuesFromListBox = ClientGUIUtil.getSelectedValuesFromListBox(comboBoxFiles);
				if (!selectedValuesFromListBox.isEmpty()) {
					final String selectedFileId = selectedValuesFromListBox.iterator().next();
					loadSheets(selectedFileId);
				}

			}
		});
		comboBoxSheets.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final Set<String> selectedValuesFromListBox = ClientGUIUtil
						.getSelectedValuesFromListBox(comboBoxSheets);
				if (!selectedValuesFromListBox.isEmpty()) {
					final String selectedSheetID = selectedValuesFromListBox.iterator().next();
					final String[] split = selectedSheetID.split(SharedConstants.EXCEL_ID_SEPARATOR);
					loadColumns(split[0], selectedSheetID);
				}
			}
		});
	}

	public void addExcelFilesChangeHandler(ChangeHandler changeHandler) {
		comboBoxFiles.addChangeHandler(changeHandler);
	}

	public void addExcelSheetsChangeHandler(ChangeHandler changeHandler) {
		comboBoxSheets.addChangeHandler(changeHandler);
	}

	public void addExcelColumnsChangeHandler(ChangeHandler changeHandler) {
		comboBoxColumns.addChangeHandler(changeHandler);
	}

	private void loadColumns(String selectedFileId, String selectedSheetId) {
		comboBoxColumns.clear();

		for (final FileTypeBean excelFileBean : excelFileBeans) {
			if (excelFileBean.getId().equals(selectedFileId)) {
				final SheetsTypeBean sheets = excelFileBean.getSheets();
				if (sheets != null) {
					for (final SheetTypeBean excelSheetBean : sheets.getSheet()) {
						if (excelSheetBean.getId().equals(selectedSheetId)) {
							final List<ColumnTypeBean> columns = excelSheetBean.getColumn();
							if (columns != null) {
								if (columns.size() > 1)
									comboBoxColumns.addItem("", "");
								for (final ColumnTypeBean excelColumnBean : columns) {
									comboBoxColumns.addItem(
											getColumnKey(excelColumnBean.getId()) + "-" + excelColumnBean.getHeader(),
											excelColumnBean.getId());
								}
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Gets the Column key from the column ID
	 *
	 * @param columnID
	 * @return
	 */
	public static String getColumnKey(String columnID) {
		if (columnID != null) {
			if (columnID.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = columnID.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length > 2) {
					return split[2];
				}
			}
		}
		return null;
	}

	/**
	 * Gets the Sheet ID from the column ID
	 *
	 * @param columnID
	 * @return
	 */
	public static String getSheetID(String columnID) {
		if (columnID != null) {
			if (columnID.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = columnID.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length > 1)
					return split[1];
			}
		}
		return null;
	}

	/**
	 * Gets the Excel File ID from the column ID
	 *
	 * @param columnID
	 * @return
	 */
	public static String getExcelFileID(String columnID) {
		if (columnID != null) {
			if (columnID.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = columnID.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length > 0)
					return split[0];
			}
		}
		return null;
	}

	public static String getSheetName(String sheetID) {
		if (sheetID != null) {
			if (sheetID.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = sheetID.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length > 1) {
					final String string = split[1];
					return string;
				}
			}
		}
		return null;
	}

	private void loadSheets(String selectedFileId) {
		comboBoxSheets.clear();

		FileTypeBean selectedExcelFileBean = null;
		for (final FileTypeBean excelFileBean : excelFileBeans) {
			if (excelFileBean.getId().equals(selectedFileId)) {
				selectedExcelFileBean = excelFileBean;
			}
		}
		if (selectedExcelFileBean != null) {
			final SheetsTypeBean sheets = selectedExcelFileBean.getSheets();
			if (sheets != null) {
				if (sheets.getSheet().size() > 1)
					comboBoxSheets.addItem("", "");
				for (final SheetTypeBean excelSheetBean : sheets.getSheet()) {
					comboBoxSheets.addItem(getSheetName(excelSheetBean.getId()), excelSheetBean.getId());
				}
				// load columns if only one sheet is present
				if (sheets.getSheet().size() == 1) {
					loadColumns(selectedFileId, sheets.getSheet().get(0).getId());
				}
			}
		}

	}

	public void addExcelFileBean(FileTypeBean excelFileBean) {
		if (excelFileBean != null) {
			excelFileBeans.add(excelFileBean);
		}
		loadData();
	}

	private void loadData() {
		// clear data
		comboBoxFiles.clear();
		comboBoxColumns.clear();
		comboBoxSheets.clear();
		// add data
		// just if more than one excelFileBean
		if (excelFileBeans.size() > 1)
			comboBoxFiles.addItem("", "");
		comboBoxSheets.addItem("", "");
		comboBoxColumns.addItem("", "");
		for (final FileTypeBean excelFileBean : excelFileBeans) {
			comboBoxFiles.addItem(excelFileBean.getId(), excelFileBean.getId());
		}
		// load sheets if only one file is present
		if (excelFileBeans.size() == 1) {
			loadSheets(excelFileBeans.get(0).getId());
		}

	}

	/**
	 * Removes all the {@link ExcelFileBean} and add the provided, and load its data
	 * in the panel
	 *
	 * @param excelFileBean
	 */
	public void setExcelFileBean(FileTypeBean excelFileBean) {
		excelFileBeans.clear();
		excelFileBeans.add(excelFileBean);
		loadData();
	}

	/**
	 * Gets the selected columnRef
	 *
	 * @return
	 */
	public String getColumnRef() {
		if (comboBoxColumns.getSelectedIndex() > -1) {
			final String value = comboBoxColumns.getValue(comboBoxColumns.getSelectedIndex());
			if (!"".equals(value))
				return value;
		}
		return null;
	}

	public String getColumnName() {
		if (comboBoxColumns.getSelectedIndex() > -1) {
			final String itemText = comboBoxColumns.getItemText(comboBoxColumns.getSelectedIndex());
			if (!"".equals(itemText))
				return itemText;
		}
		return null;
	}

	/**
	 * Gets the selected sheetRef
	 *
	 * @return
	 */
	public String getSheetRef() {
		if (comboBoxSheets.getSelectedIndex() > -1) {
			final String value = comboBoxSheets.getValue(comboBoxSheets.getSelectedIndex());
			if (!"".equals(value))
				return value;
		}
		return null;
	}

	public String getSheetName() {
		if (comboBoxSheets.getSelectedIndex() > -1) {
			final String itemText = comboBoxSheets.getItemText(comboBoxSheets.getSelectedIndex());
			if (!"".equals(itemText))
				return itemText;
		}
		return null;
	}

	/**
	 * Selects the appropiate values in the combos for a given columnID
	 *
	 * @param columnID which is formed like fileName##sheetName##columnName
	 */
	public void selectExcelColumn(String columnID) {
		if (columnID == null)
			return;
		final String excelFileID = getExcelFileID(columnID);
		final String sheetID = getSheetID(columnID);
		final String columnKey = getColumnKey(columnID);
		final boolean selectInComboByValue = ProjectCreatorWizardUtil.selectInComboByValue(comboBoxFiles, excelFileID);
		if (!selectInComboByValue) {
			log.warning(excelFileID + " couldn't be selected in Excel file combo");
		} else {
			// load the sheets of the file
			loadSheets(excelFileID);
		}
		final boolean selectInComboByValue2 = ProjectCreatorWizardUtil.selectInComboByValue(comboBoxSheets,
				excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID);

		if (!selectInComboByValue2) {
			log.warning(excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID
					+ " couldn't  be selected in Excel Sheet combo");
		} else {
			// load the columns of the sheet
			loadColumns(excelFileID, excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID);
		}
		final boolean selectInComboByValue3 = ProjectCreatorWizardUtil.selectInComboByValue(comboBoxColumns, excelFileID
				+ SharedConstants.EXCEL_ID_SEPARATOR + sheetID + SharedConstants.EXCEL_ID_SEPARATOR + columnKey);
		if (!selectInComboByValue3) {
			log.warning(excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID + SharedConstants.EXCEL_ID_SEPARATOR
					+ columnKey + " couldn't be selected in Excel Column combo");
		}
	}
}
