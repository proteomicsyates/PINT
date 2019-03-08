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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

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

	public static final int MAX_LENGTH_TITLE = 35;

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
	public void addPage(String name, int indexToInsert, String styleNameActive, String styleNameInactive) {
		boolean isTheLastPage = false;
		if (!pageNames.contains(name)) {
			if (indexToInsert == pageNames.size()) {
				isTheLastPage = true;
			}
			pageNames.add(indexToInsert, name);
		}

		final int index = pageNames.indexOf(name);

		String reducedName = name;
		if (reducedName.length() > MAX_LENGTH_TITLE) {
			reducedName = reducedName.substring(0, MAX_LENGTH_TITLE) + "...";
		}
		final Label label = new Label(reducedName);
		label.setStyleName(WizardStyles.WizardInfoMessage);
		label.setTitle(name);

		if (!isTheLastPage) {
			// move all widgets to the bottom one position from the position to inser
			final int rowCount = table.getRowCount();
			for (int i = rowCount - 1; i >= indexToInsert; i--) {
				final Widget lastWidget = table.getWidget(i, 1);
				final int rowToInsert = i + 1;
				table.setWidget(rowToInsert, 1, lastWidget);
				table.getCellFormatter().setVerticalAlignment(rowToInsert, 0, HasVerticalAlignment.ALIGN_MIDDLE);
				table.getCellFormatter().setHorizontalAlignment(rowToInsert, 1, HasHorizontalAlignment.ALIGN_LEFT);
				table.getCellFormatter().setVerticalAlignment(rowToInsert, 1, HasVerticalAlignment.ALIGN_BOTTOM);
				table.getCellFormatter().addStyleName(rowToInsert, 1, styleNameInactive);
				activeStyle.put(rowToInsert, styleNameActive);
				inactiveStyle.put(rowToInsert, styleNameInactive);
			}
		}
		table.setWidget(index, 1, label);
		activeStyle.put(index, styleNameActive);
		inactiveStyle.put(index, styleNameInactive);
		table.getCellFormatter().setVerticalAlignment(index, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		table.getCellFormatter().setHorizontalAlignment(index, 1, HasHorizontalAlignment.ALIGN_LEFT);
		table.getCellFormatter().setVerticalAlignment(index, 1, HasVerticalAlignment.ALIGN_BOTTOM);
		table.getCellFormatter().addStyleName(index, 1, styleNameInactive);
	}

	@Override
	public void addPage(String name, int index) {
		addPage(name, index, WizardStyles.WizardInfoMessageSelected, WizardStyles.WizardInfoMessageNonSelected);

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
			final Widget widget = table.getWidget(i, 1);
			if (i < index) {
				widget.addStyleName(WizardStyles.wizardInfoMessageSelectedPast);
				widget.removeStyleName(activeStyleName);
				widget.removeStyleName(inactiveStyleName);
			} else if (i == index) {
				widget.addStyleName(activeStyleName);
				widget.removeStyleName(inactiveStyleName);
			} else {
				widget.addStyleName(inactiveStyleName);
				widget.removeStyleName(activeStyleName);
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
