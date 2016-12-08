package edu.scripps.yates.client.gui.components;

import edu.scripps.yates.shared.util.SharedDataUtils;

public class MyVerticalConditionsListBoxPanel extends MyVerticalListBoxPanel {
	private final MyVerticalListBoxPanel projectPanel;

	public MyVerticalConditionsListBoxPanel(
			MyVerticalListBoxPanel projectPanel, boolean multipleSelect) {
		super(multipleSelect);
		this.projectPanel = projectPanel;
	}

	public boolean isSelected(String condition, String project) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i)) {
				String itemText = listBox.getItemText(i);
				final String conditionName = SharedDataUtils
						.parseConditionNameFromConditionSelection(itemText);
				if (condition.equals(conditionName)) {
					final String projectSymbol = SharedDataUtils
							.parseProjectSymbolFromConditionSelection(conditionName);
					final String projectName = SharedDataUtils.parseProjectNameFromListBox(
							projectSymbol, projectPanel.getListBox());
					if (project.equalsIgnoreCase(projectName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
