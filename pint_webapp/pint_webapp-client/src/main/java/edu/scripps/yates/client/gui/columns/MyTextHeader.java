package edu.scripps.yates.client.gui.columns;

import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import edu.scripps.yates.client.util.HorizontalAlignmentUtil;
import edu.scripps.yates.shared.columns.ColumnName;

/**
 * A grid header that contains a tooltip
 *
 * @author Salva
 *
 */
public class MyTextHeader extends TextHeader {

	public MyTextHeader(ColumnName columnName, String text) {
		super(text);

		final HorizontalAlignmentConstant horizontalAlignment = HorizontalAlignmentUtil
				.getHorizontalAlignmentConstant(columnName.getHorizontalAlignment());
		if (horizontalAlignment == HasHorizontalAlignment.ALIGN_CENTER) {
			setHeaderStyleNames("tableHeaderCenter");
		} else if (horizontalAlignment == HasHorizontalAlignment.ALIGN_LEFT) {
			setHeaderStyleNames("tableHeaderLeft");
		} else if (horizontalAlignment == HasHorizontalAlignment.ALIGN_RIGHT) {
			setHeaderStyleNames("tableHeaderRight");
		} else {
			setHeaderStyleNames("tableHeader");
		}
	}

}
