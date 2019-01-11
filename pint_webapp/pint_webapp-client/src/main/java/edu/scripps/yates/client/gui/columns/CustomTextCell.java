package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.CssColorUtil;

public class CustomTextCell extends TextCell {
	private final boolean colorScale;

	private static HtmlTemplates template = HtmlTemplates.instance;

	public CustomTextCell(ColumnName columnName) {
		// TODO take a different color scale depending on the column, or pass
		// the color scale somehow in the constructor
		if (columnName == ColumnName.PROTEIN_RATIO_SCORE) {
			colorScale = true;
		} else {
			colorScale = false;
		}

	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {

		if (colorScale && value != null) {
			try {
				double doubleValue = Double.valueOf(value);
				if (doubleValue >= 0.0 && doubleValue <= 1.0) {
					final String color = CssColorUtil.makeColor(doubleValue);
					// The template will sanitize the URI.
					sb.append(template.coloredDiv(color, value));
					return;
				}
			} catch (NumberFormatException e) {

			}
		}
		sb.appendEscaped(value);
	}
}
