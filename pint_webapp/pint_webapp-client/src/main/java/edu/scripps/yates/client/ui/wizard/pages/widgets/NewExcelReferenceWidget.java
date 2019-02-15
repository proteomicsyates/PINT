package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class NewExcelReferenceWidget extends Composite {
	private final ListBox comboBoxSheets;
	private final ListBox comboBoxColumns;
	private final FileTypeBean file;
	private final String excelSheet;

	public NewExcelReferenceWidget(FileTypeBean file, String excelSheet) {
		this.file = file;
		this.excelSheet = excelSheet;
		final FlowPanel mainPanel = new FlowPanel();
		initWidget(mainPanel);

		final FlexTable table = new FlexTable();
		table.setStyleName(WizardStyles.ExcelColumnRefPanelNew);
		table.setCellPadding(5);
		mainPanel.add(table);

		final int row = 0;

		final Label lblSheet = new Label("Excel sheet:");
		lblSheet.setStyleName(WizardStyles.WizardInfoMessage);
		lblSheet.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		table.setWidget(row, 0, lblSheet);
		table.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		comboBoxSheets = new ListBox();
		table.setWidget(row, 1, comboBoxSheets);
		table.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);

		final Label lblColumn = new Label("Column in sheet:");
		lblColumn.setStyleName(WizardStyles.WizardInfoMessage);
		lblColumn.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		table.setWidget(row, 2, lblColumn);
		table.getCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_RIGHT);

		comboBoxColumns = new ListBox();
		table.setWidget(row, 3, comboBoxColumns);
		table.getCellFormatter().setHorizontalAlignment(row, 3, HasHorizontalAlignment.ALIGN_LEFT);

		final HTML rightSpacer = new HTML("&nbsp;");
		rightSpacer.setWidth("100%");
		table.setWidget(row, 4, rightSpacer);

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
		comboBoxSheets.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final Set<String> selectedValuesFromListBox = ClientGUIUtil
						.getSelectedValuesFromListBox(comboBoxSheets);
				if (!selectedValuesFromListBox.isEmpty()) {
					final String selectedSheetID = selectedValuesFromListBox.iterator().next();
					loadColumns(selectedSheetID);
				}
			}
		});
	}

	public void addExcelSheetsChangeHandler(ChangeHandler changeHandler) {
		comboBoxSheets.addChangeHandler(changeHandler);
	}

	public void addExcelColumnsChangeHandler(ChangeHandler changeHandler) {
		comboBoxColumns.addChangeHandler(changeHandler);
	}

	private void loadColumns(String selectedSheetId) {
		comboBoxColumns.clear();

		final SheetsTypeBean sheets = file.getSheets();
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
				if (split.length == 3) {
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
				final String string = split[1];
				return string;
			}
		}
		return null;
	}

	private void loadSheets() {
		comboBoxSheets.clear();

		final SheetsTypeBean sheets = file.getSheets();
		if (sheets != null) {
			if (sheets.getSheet().size() > 1 && this.excelSheet == null) {
				comboBoxSheets.addItem("", "");
			}
			for (final SheetTypeBean excelSheetBean : sheets.getSheet()) {
				final String sheetName = getSheetName(excelSheetBean.getId());
				// if this widget is dealing only with one excelSheet (excelSheet !=null)
				// do not add the other sheets
				if (excelSheet != null && !excelSheet.equals(sheetName)) {
					continue;
				}
				comboBoxSheets.addItem(sheetName, excelSheetBean.getId());
			}
			// load columns if only one sheet is present
			if (sheets.getSheet().size() == 1) {
				loadColumns(sheets.getSheet().get(0).getId());
			} else {
				// load columns if this widget only loads one sheet
				if (excelSheet != null) {
					for (final SheetTypeBean sheet : sheets.getSheet()) {
						if (excelSheet.equals(getSheetName(sheet.getId()))) {
							loadColumns(sheet.getId());
							break;
						}
					}
				}
			}
		}

	}

	private void loadData() {
		// clear data
		comboBoxColumns.clear();
		comboBoxSheets.clear();

		final SheetsTypeBean sheets = file.getSheets();
		if (sheets != null) {
			if (sheets.getSheet().size() > 1 && this.excelSheet == null) {
				comboBoxSheets.addItem("", "");
			}
		}
		comboBoxColumns.addItem("", "");

		// load sheets
		loadSheets();

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
	 * Selects the appropriate values in the combos for a given columnID
	 *
	 * @param columnID which is formed like fileName##sheetName##columnName
	 */
	public void selectExcelColumn(String columnID) {
		if (columnID == null)
			return;
		final String excelFileID = getExcelFileID(columnID);
		final String sheetID = getSheetID(columnID);
		final String columnKey = getColumnKey(columnID);

		final boolean selectInComboByValue2 = ProjectCreatorWizardUtil.selectInComboByValue(comboBoxSheets,
				excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID);

		if (selectInComboByValue2) {
			// load the columns of the sheet
			loadColumns(excelFileID + SharedConstants.EXCEL_ID_SEPARATOR + sheetID);
		}
		ProjectCreatorWizardUtil.selectInComboByValue(comboBoxColumns, excelFileID + SharedConstants.EXCEL_ID_SEPARATOR
				+ sheetID + SharedConstants.EXCEL_ID_SEPARATOR + columnKey);

	}

	public void setEnabled(boolean selected) {
		this.comboBoxColumns.setEnabled(selected);
		this.comboBoxSheets.setEnabled(selected);
	}
}
