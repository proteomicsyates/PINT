package edu.scripps.yates.client.ui.wizard.view.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;

import edu.scripps.yates.client.ui.wizard.Wizard.Display;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.ui.wizard.view.HasWizardTitles;

/**
 * The default implementation of {@link Display#getPageList()}.
 * 
 * @author Brandon Tilley
 *
 */
public class WizardPageList extends Composite implements HasWizardTitles {

	public interface WizardPageListImages extends ClientBundle {
		@Source("arrow_right.png")
		ImageResource indicator();
	}

	private final WizardPageListImages images = GWT.create(WizardPageListImages.class);

	private final FlexTable table;
	private final List<String> pageNames;
	private final Image arrow = new Image(images.indicator());

	private final Map<Integer, String> activeStyle = new HashMap<Integer, String>();
	private final Map<Integer, String> inactiveStyle = new HashMap<Integer, String>();

	public WizardPageList() {
		table = new FlexTable();
		initWidget(table);
		pageNames = new ArrayList<String>();
		table.setStyleName(WizardStyles.wizardPageList);
	}

	@Override
	public void addPage(String name, String styleNameActive, String styleNameInactive) {
		if (!pageNames.contains(name))
			pageNames.add(name);

		final int index = pageNames.indexOf(name);
		activeStyle.put(index, styleNameActive);
		inactiveStyle.put(index, styleNameInactive);
		table.setHTML(index, 1, name);
		table.getCellFormatter().setVerticalAlignment(index, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		table.getCellFormatter().setVerticalAlignment(index, 1, HasVerticalAlignment.ALIGN_BOTTOM);
		table.getCellFormatter().addStyleName(index, 1, styleNameInactive);
	}

	@Override
	public void addPage(String name) {
		addPage(name, "inactive", "active");

	}

	@Override
	public void setCurrentPage(String name) {
		// some pages may not have titles
		if (pageNames.indexOf(name) == -1) {
			return;
		}

		final int index = pageNames.indexOf(name);
		for (int i = 0; i < pageNames.size(); i++) {
			final String activeStyleName = activeStyle.get(i);
			final String inactiveStyleName = inactiveStyle.get(i);
			if (i <= index) {
				table.getCellFormatter().removeStyleName(i, 1, inactiveStyleName);
				table.getCellFormatter().addStyleName(i, 1, activeStyleName);
			} else {
				table.getCellFormatter().addStyleName(i, 1, inactiveStyleName);
				table.getCellFormatter().removeStyleName(i, 1, activeStyleName);
			}

			if (i == index) {
				table.remove(arrow);
				table.setWidget(i, 0, arrow);
			}
		}
	}

	@Override
	public void removePage(String name) {
		final int index = pageNames.indexOf(name);
		pageNames.remove(index);
		table.removeRow(index);
		final Map<Integer, String> newActiveStyle = new HashMap<Integer, String>();
		final Map<Integer, String> newInactiveStyle = new HashMap<Integer, String>();
		for (final Integer index2 : activeStyle.keySet()) {
			if (index2 < index) {
				newActiveStyle.put(index2, activeStyle.get(index2));
				newInactiveStyle.put(index2, inactiveStyle.get(index2));
			} else if (index2 > index) {
				if (index2 > 0) {
					newActiveStyle.put(index2 - 1, activeStyle.get(index2));
					newInactiveStyle.put(index2 - 1, inactiveStyle.get(index2));
				}
			}
		}
		activeStyle.clear();
		activeStyle.putAll(newActiveStyle);
		inactiveStyle.clear();
		inactiveStyle.putAll(newInactiveStyle);

	}

}
