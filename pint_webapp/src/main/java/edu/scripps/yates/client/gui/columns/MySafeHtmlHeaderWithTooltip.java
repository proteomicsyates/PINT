package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.util.HorizontalAlignmentUtil;
import edu.scripps.yates.shared.columns.ColumnName;

/**
 * A grid header that contains a tooltip
 *
 * @author Salva
 *
 */
public class MySafeHtmlHeaderWithTooltip extends SafeHtmlHeader {
	private final String toolTipText;
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);

	public MySafeHtmlHeaderWithTooltip(ColumnName columnName, SafeHtml text, String tooltipText) {
		super(text);
		toolTipText = tooltipText;
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

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Header#render(com.google.gwt.cell
	 * .client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, SafeHtmlBuilder sb) {
		sb.append(template.startToolTip(toolTipText));
		super.render(context, sb);
		sb.append(template.endToolTip());
	}

}
