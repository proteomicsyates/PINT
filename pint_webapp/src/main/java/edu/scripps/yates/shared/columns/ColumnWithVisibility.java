package edu.scripps.yates.shared.columns;

import java.io.Serializable;

public class ColumnWithVisibility implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -567359401711338287L;
	private ColumnName column;
	private boolean visible;

	public ColumnWithVisibility() {

	}

	public ColumnWithVisibility(ColumnName column, boolean visible) {
		super();
		this.column = column;
		if (column == null)
			System.out.println("ASDF");
		this.visible = visible;
	}

	/**
	 * @return the column
	 */
	public ColumnName getColumn() {
		if (column == null)
			System.out.println("ASDF");
		return column;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	public void setColumn(ColumnName column) {
		this.column = column;
	}

	/**
	 * @param visible
	 *            the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
