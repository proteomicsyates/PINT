package edu.scripps.yates.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.shared.model.ProteinBean;

public class ClientGUIUtil {
	public static final String DEFAULT_WIDTH = "1024px";

	public static Set<String> getSelectedItemsFromListBox(ListBox listBox) {
		Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getItemText(i));
			}
		}
		return selectedItems;
	}

	public static Set<String> getSelectedValuesFromListBox(ListBox listBox) {
		Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getValue(i));
			}
		}
		return selectedItems;
	}

	public static Set<String> getNonSelectedItemsFromListBox(ListBox listBox) {
		Set<String> ret = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (!listBox.isItemSelected(i)) {
				ret.add(listBox.getItemText(i));
			}
		}
		return ret;
	}

	public static Set<String> getNonSelectedValuesFromListBox(ListBox listBox) {
		Set<String> ret = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (!listBox.isItemSelected(i)) {
				ret.add(listBox.getValue(i));
			}
		}
		return ret;
	}

	public static void scrollToBottom(Element element) {
		try {
			// element.setScrollTop(element.getScrollHeight()
			// - element.getClientHeight());
		} catch (Exception e) {

		}
	}

	public static void sortProteinsBySPC(List<ProteinBean> sortedProteins) {
		Collections.sort(sortedProteins, getProteinBeanComparatorBySPC());
	}

	private static Comparator<ProteinBean> getProteinBeanComparatorBySPC() {
		return new Comparator<ProteinBean>() {

			@Override
			public int compare(ProteinBean o1, ProteinBean o2) {
				return Integer.compare(o2.getNumPSMs(), o1.getNumPSMs());
			}
		};
	}

}
