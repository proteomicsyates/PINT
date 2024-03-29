package edu.scripps.yates.client.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExcelColumnRefPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.DataSourceDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;

public class ClientGUIUtil {
	public static final String DEFAULT_WIDTH = "1024px";

	public static Set<String> getSelectedItemsFromListBox(ListBox listBox) {
		final Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getItemText(i));
			}
		}
		return selectedItems;
	}

	public static Set<String> getSelectedValuesFromListBox(ListBox listBox) {
		final Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				selectedItems.add(listBox.getValue(i));
			}
		}
		return selectedItems;
	}

	public static boolean setSelectedValueInListBox(ListBox listBox, String value, boolean fireChangeEvent) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.getValue(i).equals(value)) {
				listBox.setSelectedIndex(i);
				if (fireChangeEvent) {
					DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
				}
				return true;
			}
		}
		return false;
	}

	public static Set<String> getNonSelectedItemsFromListBox(ListBox listBox) {
		final Set<String> ret = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (!listBox.isItemSelected(i)) {
				ret.add(listBox.getItemText(i));
			}
		}
		return ret;
	}

	public static Set<String> getNonSelectedValuesFromListBox(ListBox listBox) {
		final Set<String> ret = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (!listBox.isItemSelected(i)) {
				ret.add(listBox.getValue(i));
			}
		}
		return ret;
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

	/**
	 * Gets the internal ID of the selected file in the {@link ListBox}. Then, take
	 * that internal ID and search for it in the {@link ProjectCreatorRegister}. If
	 * found, and the found object is a {@link DataSourceDisclosurePanel}, then,
	 * returns the {@link FileNameWithTypeBean} represented
	 *
	 * @param fileCombo
	 * @param columnRef
	 * @return
	 */
	public static FileNameWithTypeBean getFileNameWithTypeFromFileComboSelection(ListBox fileCombo, String columnRef) {
		final String excelFileID = ExcelColumnRefPanel.getExcelFileID(columnRef);
		final String internalID = ProjectCreatorWizardUtil.getValueInCombo(fileCombo, excelFileID);
		try {
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(Integer.valueOf(internalID));
			if (projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				final DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				final FileNameWithTypeBean fileNameWithTypeBean = disclosurePanel.getFileNameWithTypeBean();
				return fileNameWithTypeBean;
			}
		} catch (final NumberFormatException e) {

		}
		return null;
	}

	public static void addSuggestionsDeferred(List<String> suggestions, SuggestBox suggestBox) {
		GWT.log("About to call a deferred command to fill the suggestions");
		final Timer timer = new Timer() {

			@Override
			public void run() {
				Scheduler.get().scheduleDeferred(new Command() {

					@Override
					public void execute() {
						GWT.log("Starting to add suggestions");
						final MultiWordSuggestOracle suggestOracle = (MultiWordSuggestOracle) suggestBox
								.getSuggestOracle();
						suggestOracle.setDefaultSuggestionsFromText(suggestions);
						suggestOracle.addAll(suggestions);
						GWT.log("Suggestions added");
					}
				});
			}
		};
		// run deferred in half second
		timer.schedule(500);

	}
}
