package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.style.Color;

/**
 * Customized TextCell that shows the cell in a custom color, when a certain
 * value is present
 *
 * @author Salva
 *
 */
public class CustomCell extends TextCell {
	private final Map<String, Color> colorsByValues = new HashMap<String, Color>();

	private static HtmlTemplates template = GWT.create(HtmlTemplates.class);

	public CustomCell(String redValue, Color color) {
		colorsByValues.put(redValue, color);

	}

	public CustomCell() {

	}

	/**
	 * Add a color to display in the cell depending on the value of it
	 *
	 * @param cellValue
	 *            if null, that color will be applied to any cell not matching
	 *            any other stated value
	 * @param color
	 */
	public void addCustomColorCell(String cellValue, Color color) {
		colorsByValues.put(cellValue, color);
	}

	/**
	 * Add a color to display in the cell depending on the value of it.<br>
	 * The difference with addCustomColorCell is that any other previous
	 * value/color association will be discarded.
	 *
	 * @param cellValue
	 *            if null, that color will be applied to any cell not matching
	 *            any other stated value
	 * @param color
	 */
	public void setCustomColorCell(String cellValue, Color color) {
		colorsByValues.clear();
		colorsByValues.put(cellValue, color);
	}

	@Override
	public void render(Context context, String data, SafeHtmlBuilder sb) {

		if (colorsByValues.containsKey(data)) {
			sb.append(template.coloredDiv(colorsByValues.get(data).toString(), data));
		} else if (colorsByValues.containsKey(null)) {
			sb.append(template.coloredDiv(colorsByValues.get(null).toString(), data));
		} else {
			super.render(context, data, sb);
		}
	}
}
