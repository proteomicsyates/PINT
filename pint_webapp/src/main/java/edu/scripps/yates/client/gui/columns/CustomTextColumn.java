package edu.scripps.yates.client.gui.columns;

import com.google.gwt.user.cellview.client.Column;

import edu.scripps.yates.client.util.HorizontalAlignmentUtil;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.HorizontalAlignmentSharedConstant;

public abstract class CustomTextColumn<T> extends Column<T, String> {

	public CustomTextColumn(ColumnName columnName) {
		super(new CustomTextCell(columnName));
		setCellStyleNames("textColumn");
		final HorizontalAlignmentSharedConstant horizontalAlignment = columnName.getHorizontalAlignment();

		setHorizontalAlignment(HorizontalAlignmentUtil.getHorizontalAlignmentConstant(horizontalAlignment));
	}

}