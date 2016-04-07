package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.columns.ColumnManager;
import edu.scripps.yates.client.interfaces.ItemList;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;

public class MyVerticalCheckBoxListPanel<T> extends VerticalPanel implements ItemList {
	private final List<CheckBox> listCheckBox = new ArrayList<CheckBox>();
	private final Map<ColumnName, CheckBox> map = new HashMap<ColumnName, CheckBox>();
	private final Set<ColumnManager<T>> columnManagers = new HashSet<ColumnManager<T>>();

	public MyVerticalCheckBoxListPanel(ColumnManager<T> columnManager) {
		this.columnManagers.add(columnManager);
		setSize("100%", "100%");
		setVerticalAlignment(ALIGN_TOP);
		final List<ColumnName> columns = columnManager.getColumnNames();

		for (ColumnName columnName : columns) {
			final boolean visible = columnManager.isVisible(columnName);
			addCheckBox(columnName, visible);
		}
	}

	public void addColumnManager(ColumnManager<T> columnManager) {
		this.columnManagers.add(columnManager);
	}

	private void addCheckBox(ColumnName column, boolean selected) {
		final CheckBox checkBox = new CheckBox(column.getName());
		// not add the click handler for the column amount, since, it will be a
		// clickhandler fired by experimental condition name, and they will be
		// created when receiving the condition names from server at Query
		if (ColumnName.PROTEIN_AMOUNT != column && ColumnName.PSM_AMOUNT != column
				&& ColumnName.PEPTIDE_AMOUNT != column && ColumnName.PROTEIN_RATIO != column
				&& ColumnName.PEPTIDE_RATIO != column && ColumnName.PSM_RATIO != column && ColumnName.PSM_SCORE != null
				&& ColumnName.PEPTIDE_SCORE != column)
			checkBox.addClickHandler(getClickHandler(column));
		else {
		}
		checkBox.setTitle(column.getDescription());
		checkBox.setValue(selected);
		listCheckBox.add(checkBox);
		// add to the map
		map.put(column, checkBox);
		// add to the panel
		add(checkBox);
	}

	public void addConditionRelatedColumnCheckBoxHandler(ColumnName columnName, String condition1Name,
			String condition2Name, String projectTag, MyVerticalConditionsListBoxPanel conditionsPanel) {
		for (CheckBox checkBox : listCheckBox) {
			if (checkBox.getText().equals(columnName.getName())) {
				checkBox.addClickHandler(getConditionRelatedColumnClickHandler(columnName, condition1Name,
						condition2Name, projectTag, conditionsPanel));
			}
		}
	}

	public void addConditionRelatedColumnCheckBoxHandler(ColumnName columnName, String conditionName, String projectTag,
			MyVerticalConditionsListBoxPanel conditionsPanel) {
		for (CheckBox checkBox : listCheckBox) {
			if (checkBox.getText().equals(columnName.getName())) {
				checkBox.addClickHandler(
						getConditionRelatedColumnClickHandler(columnName, conditionName, projectTag, conditionsPanel));
			}
		}
	}

	/**
	 * This {@link ClickHandler} gets the selection from the {@link CheckBox}
	 * source of the event and pass it to the {@link ColumnManager} to show or
	 * hide the corresponding column.
	 *
	 * @param column
	 * @return
	 */
	private ClickHandler getClickHandler(final ColumnName column) {
		ClickHandler handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				CheckBox checkbox = (CheckBox) event.getSource();
				final Boolean visible = checkbox.getValue();
				for (ColumnManager<T> columnManager : columnManagers) {
					columnManager.setVisible(column, visible);
				}
			}
		};
		return handler;
	}

	private ClickHandler getConditionRelatedColumnClickHandler(final ColumnName columnName, final String conditionName,
			final String projectTag, final MyVerticalConditionsListBoxPanel conditionsPanel) {

		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				CheckBox checkbox = (CheckBox) event.getSource();
				final Boolean setVisible = checkbox.getValue();
				boolean visibility = false;
				if (setVisible
				// && conditionsPanel.isSelected(conditionName,
				// projectTag)
				) {
					visibility = true;
				}
				// else if (!setVisible
				// && !conditionsPanel.isSelected(conditionName,
				// projectTag)) {
				// visibility = true;
				// }
				//
				for (ColumnManager<T> columnManager : columnManagers) {
					columnManager.setVisible(columnName, conditionName, projectTag, visibility);
				}

			}
		};
		return handler;
	}

	private ClickHandler getConditionRelatedColumnClickHandler(final ColumnName columnName, final String condition1Name,
			final String condition2Name, final String projectTag,
			final MyVerticalConditionsListBoxPanel conditionsPanel) {

		ClickHandler handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				CheckBox checkbox = (CheckBox) event.getSource();
				final Boolean setVisible = checkbox.getValue();
				boolean visibility = false;
				if (setVisible
				// && conditionsPanel.isSelected(condition1Name,
				// projectTag)
				// && conditionsPanel.isSelected(condition2Name,
				// projectTag)
				) {
					visibility = true;
				}
				// else if (!setVisible
				// && !conditionsPanel.isSelected(conditionName,
				// projectTag)) {
				// visibility = true;
				// }
				//

				for (ColumnManager<T> columnManager : columnManagers) {
					columnManager.setVisible(columnName, condition1Name, condition2Name, projectTag, visibility);
				}

			}
		};
		return handler;
	}

	public boolean isSelected(ColumnName column) {
		if (map.containsKey(column))
			return map.get(column).getValue();
		return false;
	}

	@Override
	public List<String> getItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listCheckBox.size(); i++) {
			ret.add(listCheckBox.get(i).getText());
		}
		return ret;
	}

	@Override
	public List<String> getSelectedItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listCheckBox.size(); i++) {
			if (listCheckBox.get(i).getValue())
				ret.add(listCheckBox.get(i).getText());
		}
		return ret;
	}

	@Override
	public List<String> getNonSelectedItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listCheckBox.size(); i++) {
			if (!listCheckBox.get(i).getValue())
				ret.add(listCheckBox.get(i).getText());
		}
		return ret;
	}

	/**
	 * Changes the value of the {@link CheckBox}es according to the
	 * corresponding values in the list of {@link ColumnWithVisibility}
	 *
	 * @param columnsWithVisibility
	 */
	public void setDefaultView(List<ColumnWithVisibility> columnsWithVisibility) {
		for (ColumnWithVisibility columnWithVisibility : columnsWithVisibility) {
			final ColumnName column = columnWithVisibility.getColumn();
			if (map.containsKey(column)) {
				final boolean visible = columnWithVisibility.isVisible();
				map.get(column).setValue(visible);
			}
		}
	}

}
