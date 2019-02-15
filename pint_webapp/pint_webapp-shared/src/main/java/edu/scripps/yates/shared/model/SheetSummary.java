package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class SheetSummary implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6948886799261228595L;
	public String name;
	public int numNumericColumns;
	public int numNonNumericColumns;
	public int numTotalColumns;
	public int numRows;

	public SheetSummary() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumNumericColumns() {
		return numNumericColumns;
	}

	public void setNumNumericColumns(int numNumericColumns) {
		this.numNumericColumns = numNumericColumns;
	}

	public int getNumNonNumericColumns() {
		return numNonNumericColumns;
	}

	public void setNumNonNumericColumns(int numNonNumericColumns) {
		this.numNonNumericColumns = numNonNumericColumns;
	}

	public int getNumTotalColumns() {
		return numTotalColumns;
	}

	public void setNumTotalColumns(int numTotalColumns) {
		this.numTotalColumns = numTotalColumns;
	}

	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	@Override
	public String toString() {
		return "Total columns: " + numTotalColumns + " (" + numNumericColumns + " numeric), Num rows: " + numRows;
	}
}
