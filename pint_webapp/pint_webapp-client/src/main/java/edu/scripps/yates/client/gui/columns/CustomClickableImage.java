package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class CustomClickableImage extends ClickableTextCell {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.cell.client.ClickableTextCell#render(com.google.gwt.cell.
	 * client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtml,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	protected void render(com.google.gwt.cell.client.Cell.Context context, SafeHtml value, SafeHtmlBuilder sb) {
		sb.appendHtmlConstant("<img width=\"20\" src=\"images/" + value.asString() + "\">");
		super.render(context, value, sb);
	}

}
