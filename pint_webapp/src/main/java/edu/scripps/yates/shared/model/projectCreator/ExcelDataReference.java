package edu.scripps.yates.shared.model.projectCreator;

import java.io.Serializable;

import edu.scripps.yates.shared.util.SharedConstants;

/**
 * this class will reference data from an Excel file involved in a import wizard
 * job
 * 
 * @author Salva
 * 
 */
public class ExcelDataReference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7917202004589413639L;
	private String sheetId;
	private String columnId;
	private FileNameWithTypeBean excelFileNameWithTypeBean;
	//
	private String regexp;

	/**
	 * @return the sheetId
	 */
	public String getSheetId() {
		return sheetId;
	}

	/**
	 * @param sheetId
	 *            the sheetId to set
	 */
	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}

	/**
	 * @return the columnId
	 */
	public String getColumnId() {
		return columnId;
	}

	/**
	 * @param columnId
	 *            the columnId to set
	 */
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	/**
	 * @return the regexp
	 */
	public String getRegexp() {
		return regexp;
	}

	/**
	 * @param regexp
	 *            the regexp to set
	 */
	public void setRegexp(String regexp) {
		this.regexp = regexp;
	}

	/**
	 * @return the excelFileNameWithTypeBean
	 */
	public FileNameWithTypeBean getExcelFileNameWithTypeBean() {
		return excelFileNameWithTypeBean;
	}

	/**
	 * @param excelFileNameWithTypeBean
	 *            the excelFileNameWithTypeBean to set
	 */
	public void setExcelFileNameWithTypeBean(
			FileNameWithTypeBean excelFileNameWithTypeBean) {
		this.excelFileNameWithTypeBean = excelFileNameWithTypeBean;
	}

	public String getSheetName() {
		if (sheetId != null) {

			if (sheetId.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = sheetId
						.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length == 2) {
					return split[1];
				}
			}

		}

		return null;
	}

	public String getColumnKey() {
		if (columnId != null) {

			if (columnId.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
				final String[] split = columnId
						.split(SharedConstants.EXCEL_ID_SEPARATOR);
				if (split.length == 3) {
					return split[2];
				}
			}
		}

		return null;
	}
}
