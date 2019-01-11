package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;

import edu.scripps.yates.ProteinRetrievalService;
import edu.scripps.yates.ProteinRetrievalServiceAsync;
import edu.scripps.yates.shared.util.Pair;

public class ReplicatePickerPanel extends Composite {
	protected static final String SEPARATOR = "&&&&&&&";
	private final ListBox listBox;
	private final RadioButton rdbtnMsRuns;
	private final RadioButton rdbtnExpConditions;
	private final ProteinRetrievalServiceAsync service = GWT.create(ProteinRetrievalService.class);
	private final List<String> projectTags = new ArrayList<String>();
	private static final Map<String, Set<String>> conditionsByProjectTag = new HashMap<String, Set<String>>();
	private static final Map<String, Set<String>> msRunsByProjectTag = new HashMap<String, Set<String>>();
	private static int numInstance = 0;

	public ReplicatePickerPanel(Collection<String> projectTags) {
		numInstance++;
		this.projectTags.addAll(projectTags);
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("ReplicatePickerPanel");
		initWidget(mainPanel);

		FlexTable table = new FlexTable();
		mainPanel.add(table);
		table.setStyleName("replicatePickerPanel-Table");

		rdbtnMsRuns = new RadioButton("type" + numInstance, "MS runs");
		rdbtnMsRuns.setValue(true);
		rdbtnMsRuns.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				loadMsRunsInList();
			}
		});
		table.setWidget(0, 0, rdbtnMsRuns);

		listBox = new ListBox();
		listBox.setMultipleSelect(true);
		listBox.setSize("200px", "100%");
		table.setWidget(0, 1, listBox);
		listBox.setVisibleItemCount(3);

		rdbtnExpConditions = new RadioButton("type" + numInstance, "Exp. conditions");
		rdbtnExpConditions.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				loadExperimentalConditionsInList();

			}
		});
		table.setWidget(1, 0, rdbtnExpConditions);
		table.getFlexCellFormatter().setRowSpan(0, 1, 2);
		table.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		table.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		table.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		table.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		FlexTableHelper.fixRowSpan(table);

		// load ms runs
		loadMsRunsInList();
	}

	private void loadExperimentalConditionsInList() {
		listBox.clear();

		for (final String projectTag : projectTags) {
			if (conditionsByProjectTag.containsKey(projectTag)) {
				addConditions(conditionsByProjectTag.get(projectTag), projectTag, projectTags.size());
			} else {
				service.getExperimentalConditionsFromProject(projectTag, new AsyncCallback<Set<String>>() {

					@Override
					public void onSuccess(Set<String> result) {
						addConditions(result, projectTag, projectTags.size());
						if (conditionsByProjectTag.containsKey(projectTag)) {
							conditionsByProjectTag.get(projectTag).addAll(result);
						} else {
							Set<String> set = new HashSet<String>();
							set.addAll(result);
							conditionsByProjectTag.put(projectTag, set);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
			}
		}

	}

	private void addConditions(Collection<String> conditionCollection, String projectTag, int numProjects) {
		String projectName = projectTag + "-";
		if (numProjects == 1) {
			projectName = "";
		}
		List<String> list = new ArrayList<String>();
		list.addAll(conditionCollection);
		Collections.sort(list);
		for (String conditionName : list) {
			listBox.addItem(projectName + conditionName, projectTag + SEPARATOR + conditionName);
		}

	}

	private void loadMsRunsInList() {
		listBox.clear();

		for (final String projectTag : projectTags) {
			if (msRunsByProjectTag.containsKey(projectTag)) {
				addMSRuns(msRunsByProjectTag.get(projectTag), projectTag, projectTags.size());
			} else {
				service.getMsRunsFromProject(projectTag, new AsyncCallback<Set<String>>() {

					@Override
					public void onSuccess(Set<String> result) {
						addMSRuns(result, projectTag, projectTags.size());

						if (msRunsByProjectTag.containsKey(projectTag)) {
							msRunsByProjectTag.get(projectTag).addAll(result);
						} else {
							Set<String> list = new HashSet<String>();
							list.addAll(result);
							msRunsByProjectTag.put(projectTag, list);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
			}
		}

	}

	private void addMSRuns(Collection<String> msrunIds, String projectTag, int numProjects) {
		String projectName = projectTag + "-";
		if (numProjects == 1) {
			projectName = "";
		}
		List<String> list = new ArrayList<String>();
		list.addAll(msrunIds);
		Collections.sort(list);
		for (String msRunID : list) {
			listBox.addItem(projectName + msRunID, projectTag + SEPARATOR + msRunID);
		}

	}

	public Map<String, Set<String>> getSelectedMSRunsByProject() {
		if (rdbtnMsRuns.getValue() != null && rdbtnMsRuns.getValue()) {
			return getMapByProjectFromListBox();
		}
		return null;
	}

	public Map<String, Set<String>> getSelectedConditionsByProject() {
		if (rdbtnExpConditions.getValue() != null && rdbtnExpConditions.getValue()) {
			return getMapByProjectFromListBox();

		}
		return null;
	}

	public boolean isConditionsSelected() {
		return rdbtnExpConditions.getValue();
	}

	public boolean isMSRunsSelected() {
		return rdbtnMsRuns.getValue();
	}

	private Map<String, Set<String>> getMapByProjectFromListBox() {
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				Pair<String, String> pair = getPairFromText(listBox.getValue(i));
				if (pair != null) {
					if (ret.containsKey(pair.getFirstElement())) {
						ret.get(pair.getFirstElement()).add(pair.getSecondElement());
					} else {
						Set<String> set = new HashSet<String>();
						set.add(pair.getSecondElement());
						ret.put(pair.getFirstElement(), set);
					}
				}
			}
		}
		return ret;
	}

	private Pair<String, String> getPairFromText(String value) {
		if (value != null) {
			if (value.contains(SEPARATOR)) {
				final String[] split = value.split(SEPARATOR);
				Pair<String, String> ret = new Pair<String, String>(split[0], split[1]);
				return ret;
			}
		}
		return null;
	}
}
