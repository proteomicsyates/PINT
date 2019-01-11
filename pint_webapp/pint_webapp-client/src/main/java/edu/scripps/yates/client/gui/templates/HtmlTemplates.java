package edu.scripps.yates.client.gui.templates;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;

public interface HtmlTemplates extends SafeHtmlTemplates {
	public final static HtmlTemplates instance = com.google.gwt.core.client.GWT.create(HtmlTemplates.class);

	@Template("<span class=\"{0}\">{1}</span>")
	SafeHtml spanClass(String className, String text);

	@Template("<a href=\"{0}\" class=\"{1}\" title=\"{2}\" alt=\"{3}\" target=\"_blank\">{4}</a>")
	SafeHtml link(SafeUri href, String className, String title, String alt, String text);

	@Template("<a href=\"{0}\" class=\"{1}\" title=\"{2}\" alt=\"{3}\" target=\"_blank\">{4}</a>")
	SafeHtml link(SafeUri href, String className, String title, String alt, SafeHtml safeHtml);

	@Template("<div title=\"{0}\">")
	SafeHtml startToolTip(String toolTipText);

	@Template("<div title=\"{0}\" class=\"{1}\">")
	SafeHtml startToolTipWithClass(String toolTipText, String className);

	@Template("</div>")
	SafeHtml endToolTip();

	@Template("<div style=\"color:{0}\">{1}</div>")
	SafeHtml coloredDiv(String color, String text);

	@Template("<div style=\"text-align:center;\">{img}</div>")
	SafeHtml render(SafeHtml img);

	@Template("<img src=\"{0}\" title=\"{1}\" class=\"{2}\">")
	SafeHtml imageWithTitle(SafeUri imgSrc, String title, String className);

	@Template("<img style=\"width:{0}%\" src=\"{1}\" title=\"{2}\" class=\"Domain graphicalview\">")
	SafeHtml domainImage(double widthPercentaje, SafeUri imgSrc, String title);

	@Template("<div style=\"width:{0}%\" title=\"{1}\" class=\"{2}\"/>")
	SafeHtml simpleDiv(double percentage, String toolTipText, String className);

	@Template("<img style=\"width:{0}%\" src=\"{1}\" title=\"{2}\" class=\"buffer graphicalview\">")
	SafeHtml bufferImage(double widthPercentaje, SafeUri imgSrc, String title);

	// @Template("<img style=\"width:{0}%\" src=\"sequence_marker.png\"
	// class=\"Domain graphicalview\"></img>")
	// SafeHtml domainImage(double widthPercentaje);
	//
	// @Template("<img style=\"width:{0}%\" src=\"sequence_buffer.png\"
	// class=\"buffer graphicalview\"></img>")
	// SafeHtml bufferImage(double widthPercentaje);
}
