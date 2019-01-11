package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;

import edu.scripps.yates.client.style.Color;

public abstract class CustomColumn<T> extends Column<T, String> {
	private final String headerName;

	public CustomColumn() {
		super(new CustomCell());
		this.headerName = "Custom column";
	}

	public CustomColumn(String headerName) {
		super(new CustomCell());
		this.headerName = headerName;
	}

	/**
	 * States that, for a certain value, the cell will have a certain
	 * {@link Color}
	 *
	 * @param cellValue
	 *            if null, that color will be applied to any cell not matching
	 *            any other stated value
	 * @param color
	 */
	public void addCustomColorCell(String cellValue, Color color) {
		final Cell<String> cell = getCell();
		final CustomCell customCell = (CustomCell) cell;
		customCell.addCustomColorCell(cellValue, color);
	}

	/**
	 * States that, for a certain value, the cell will have a certain
	 * {@link Color}, removing any other color/value association stated before
	 *
	 * @param cellValue
	 *            if null, that color will be applied to any cell not matching
	 *            any other stated value
	 * @param color
	 */
	public void setCustomColorCell(String cellValue, Color color) {
		final Cell<String> cell = getCell();
		final CustomCell customCell = (CustomCell) cell;
		customCell.setCustomColorCell(cellValue, color);
	}

	/**
	 * @return the headerName
	 */
	public String getHeaderName() {
		return headerName;
	}
}
