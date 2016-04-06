package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;

public class ProjectCreatorWizardUtil {
	/**
	 * Fill a {@link ListBox} with all the references to the objects registered
	 * with Class class1. it automatically add an empty element as first element
	 * on the list.
	 *
	 * @param combo
	 * @param class1
	 */
	public static void updateCombo(ListBox combo, Class class1) {
		updateCombo(combo, class1, true);
	}

	/**
	 * Fill a {@link ListBox} with all the references to the objects registered
	 * with Class class1
	 *
	 * @param combo
	 * @param class1
	 * @param addEmptyFirstValue
	 *            to add or not an empty value at the beginning of the list
	 */
	public static void updateCombo(ListBox combo, Class class1, boolean addEmptyFirstValue) {
		Map<Integer, RepresentsDataObject> projectObjectsMap = ProjectCreatorRegister
				.getProjectObjectsMapRepresentingClass(class1);

		// if combo is empty, then add a new item which will be the empty item
		// in index 0
		if (addEmptyFirstValue && combo.getItemCount() == 0) {
			combo.addItem("", String.valueOf(-1));
		}

		final int selectedIndex = combo.getSelectedIndex();
		// remove items in sample combo that are not in the list, starting by
		// the second in case of addEmptyFirstValue=true
		int starting = 0;
		if (addEmptyFirstValue) {
			starting = 1;
		}
		Set<Integer> projectObjectInternalIDs = new HashSet<Integer>();
		projectObjectInternalIDs.addAll(projectObjectsMap.keySet());

		int itemCount = combo.getItemCount();
		int i = starting;
		while (i < itemCount) {
			// add or update or remove the objects in the list

			final Integer internalID = Integer.valueOf(combo.getValue(i));
			if (!projectObjectsMap.containsKey(internalID)) {
				// remove from sample combo
				combo.removeItem(i);
				i--;
				if (selectedIndex == i) {
					// assuming that in 0 index is no selection (empty)
					combo.setSelectedIndex(0);
				}
			} else {
				combo.setItemText(i, projectObjectsMap.get(internalID).getID());
				// remove from set
				projectObjectInternalIDs.remove(internalID);

			}
			itemCount = combo.getItemCount();
			i++;
		}
		// add the ones not found
		for (Integer internalID : projectObjectInternalIDs) {
			combo.addItem(projectObjectsMap.get(internalID).getID(), String.valueOf(internalID));
		}

	}

	/**
	 * Look for a text value in a {@link ListBox} and if it is found, it is
	 * selected
	 *
	 * @param combo
	 * @param text
	 * @return true if the item has been found and selected.
	 */
	public static boolean selectInCombo(ListBox combo, String text) {
		if (combo != null) {
			for (int i = 0; i < combo.getItemCount(); i++) {
				// check the text in the checkbox
				if (combo.getItemText(i).equals(text)) {
					combo.setSelectedIndex(i);
					return true;
				}
				// check the value in the checkbox
				if (combo.getValue(i).equals(text)) {
					combo.setSelectedIndex(i);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Look for a text value in a {@link ListBox} and if it is found, it is
	 * selected. The text value can be a separated list of values with a certain
	 * separator.
	 *
	 * @param combo
	 * @param text
	 * @return true if the item has been found and selected.
	 */
	public static boolean selectInCombo(ListBox combo, String text, String separator) {
		if (combo != null) {
			List<String> list = new ArrayList<String>();
			if (text.contains(separator)) {
				list.addAll(Arrays.asList(text.split(separator)));
			} else {
				list.add(text);
			}

			for (String element : list) {
				boolean found = false;
				for (int i = 0; i < combo.getItemCount(); i++) {
					if (combo.getItemText(i).equals(element)) {
						combo.setItemSelected(i, true);
						found = true;
					}
				}
				if (!found)
					return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * Look for a value in a {@link ListBox} and if it is found, it is selected
	 *
	 * @param combo
	 * @param text
	 * @return true if the item has been found and selected.
	 */
	public static boolean selectInComboByValue(ListBox combo, String value) {
		if (combo != null) {
			for (int i = 0; i < combo.getItemCount(); i++) {
				if (combo.getValue(i).equals(value)) {
					combo.setSelectedIndex(i);
					return true;
				}
			}
		}
		return false;
	}

	public static String getValueInCombo(ListBox combo, String itemText) {
		if (combo != null) {
			for (int i = 0; i < combo.getItemCount(); i++) {
				if (combo.getItemText(i).equals(itemText)) {
					return combo.getValue(i);
				}
			}
		}
		return null;
	}

	public static Set<String> getSelectedItemValuesFromListBox(ListBox listBox) {
		Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getValue(i));
			}
		}
		return selectedItems;
	}

	public static Set<String> getSelectedItemTextsFromListBox(ListBox listBox) {
		Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getItemText(i));
			}
		}
		return selectedItems;
	}

	/**
	 * Returns an string that is a sorted list with a certain separator in
	 * between the selected items in a listBox
	 *
	 * @param listBox
	 * @param separator
	 * @return
	 */
	public static String getSelectedItemValuesFromListBox(ListBox listBox, String separator) {
		final Set<String> selectedItemValuesFromListBox = getSelectedItemValuesFromListBox(listBox);
		List<String> sortedList = new ArrayList<String>();
		sortedList.addAll(selectedItemValuesFromListBox);
		Collections.sort(sortedList);
		StringBuilder ret = new StringBuilder();
		for (String string : sortedList) {
			if (!ret.toString().equals("")) {
				ret.append(separator);
			}
			ret.append(string);
		}
		return ret.toString();
	}

	/**
	 * Returns an string that is a sorted list with a certain separator in
	 * between the selected items in a listBox
	 *
	 * @param listBox
	 * @param separator
	 * @return
	 */
	public static String getSelectedItemTextsFromListBox(ListBox listBox, String separator) {
		final Set<String> selectedItemTextsFromListBox = getSelectedItemTextsFromListBox(listBox);
		List<String> sortedList = new ArrayList<String>();
		sortedList.addAll(selectedItemTextsFromListBox);
		Collections.sort(sortedList);
		StringBuilder ret = new StringBuilder();
		for (String string : sortedList) {
			if (!ret.toString().equals("")) {
				ret.append(separator);
			}
			ret.append(string);
		}
		return ret.toString();
	}
}
