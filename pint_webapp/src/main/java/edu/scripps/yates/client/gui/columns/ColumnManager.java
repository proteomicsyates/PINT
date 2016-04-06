package edu.scripps.yates.client.gui.columns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;

public abstract class ColumnManager<T> {
	private final List<MyColumn<T>> myColumns = new ArrayList<MyColumn<T>>();
	// protected List<ColumnName> columns;
	// protected List<Header> footers;
	// protected List<Boolean> visibles;
	private final Set<HasColumns> listeners = new HashSet<HasColumns>();
	final FooterManager<T> footerManager;
	private final Map<ColumnName, Set<MyColumn<T>>> columnsByColumnName = new HashMap<ColumnName, Set<MyColumn<T>>>();

	public ColumnManager(FooterManager<T> footerManager) {
		this.footerManager = footerManager;
	}

	public List<MyColumn<T>> getColumns() {
		return myColumns;
	}

	// public void addColumn(ColumnName column, boolean visible) {
	// addColumn(column, visible, null, 0, null);
	// }

	// {
	//
	// if (columns == null)
	// columns = new ArrayList<ColumnName>();
	// if (visibles == null)
	// visibles = new ArrayList<Boolean>();
	// if (footers == null)
	// footers = new ArrayList<Header>();
	//
	// columns.add(column);
	// visibles.add(visible);
	// footers.add(footer);
	// }

	public boolean isVisible(ColumnName columnName) {
		if (this.columnsByColumnName.containsKey(columnName)) {
			for (MyColumn<T> column : this.columnsByColumnName.get(columnName)) {
				if (column.getColumnName().equals(columnName))
					return column.isVisible();
			}
		}
		// for (int i = 0; i < columns.size(); i++) {
		// if (columns.get(i).equals(column)) {
		// return visibles.get(i);
		// }
		// }
		return false;
	}

	public void setVisible(ColumnName columnName, boolean visible) {
		notifyChange(columnName, visible);
		if (this.columnsByColumnName.containsKey(columnName)) {
			for (MyColumn<T> column : this.columnsByColumnName.get(columnName)) {
				column.setVisible(visible);
			}
		}
	}

	public void setVisible(ColumnName columnName, String conditionName, String projectTag, boolean visible) {

		notifyChange(columnName, conditionName, projectTag, visible);
	}

	public void setVisible(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			boolean visible) {

		notifyChange(columnName, condition1Name, condition2Name, projectTag, visible);
	}

	private void notifyChange(ColumnName column, boolean visible) {
		for (HasColumns columnSizeChanger : this.listeners) {
			columnSizeChanger.showOrHideColumn(column, visible);
		}

	}

	private void notifyChange(ColumnName columnName, String conditionName, String projectTag, boolean visible) {
		Set<String> set = new HashSet<String>();
		set.add(conditionName);
		for (HasColumns columnSizeChanger : this.listeners) {
			columnSizeChanger.showOrHideExperimentalConditionColumn(columnName, set, projectTag, visible);
		}

	}

	private void notifyChange(ColumnName columnName, String condition1Name, String condition2Name, String projectTag,
			boolean visible) {
		Set<String> set = new HashSet<String>();
		set.add(condition1Name);
		set.add(condition2Name);
		for (HasColumns columnSizeChanger : this.listeners) {
			columnSizeChanger.showOrHideExperimentalConditionColumn(columnName, set, projectTag, visible);
		}

	}

	public List<ColumnName> getColumnNames() {
		List<ColumnName> list = new ArrayList<ColumnName>();
		for (MyColumn<T> column : this.myColumns) {
			list.add(column.getColumnName());
		}
		return list;

	}

	public List<ColumnName> getVisibleColumns() {
		List<ColumnName> list = new ArrayList<ColumnName>();
		for (MyColumn<T> column : this.myColumns) {
			if (column.isVisible())
				list.add(column.getColumnName());
		}
		return list;
	}

	public void addChangeListener(HasColumns listener) {
		this.listeners.add(listener);

	}

	public Header<String> getFooter(ColumnName columnName) {
		final Set<MyColumn<T>> columns = this.getColumnsByColumnName(columnName);
		if (columns != null && !columns.isEmpty()) {
			return columns.iterator().next().getFooter();
		}
		return null;
	}

	/**
	 * Gets the column index. The first is the 1 (not 0)
	 *
	 * @param columnName
	 * @return the index if it is found. 0 if not.
	 */
	public int getColumnIndex(ColumnName columnName) {
		int i = 1;
		for (MyColumn<T> column : this.myColumns) {
			if (column.getColumnName() == columnName)
				return i;
			i++;
		}
		return 0;
	}

	public void addColumn(MyColumn<T> column) {
		myColumns.add(column);
		if (this.columnsByColumnName.containsKey(column.getColumnName())) {
			this.columnsByColumnName.get(column.getColumnName()).add(column);
		} else {
			Set<MyColumn<T>> set = new HashSet<MyColumn<T>>();
			set.add(column);
			this.columnsByColumnName.put(column.getColumnName(), set);
		}
	}

	public Set<MyColumn<T>> getColumnsByColumnName(ColumnName columnName) {
		if (this.columnsByColumnName.containsKey(columnName)) {
			return this.columnsByColumnName.get(columnName);
		}
		return new HashSet<MyColumn<T>>();
	}

	public void removeColumns(ColumnName columnName) {
		final Iterator<MyColumn<T>> iterator = this.myColumns.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getColumnName() == columnName)
				iterator.remove();
		}
		columnsByColumnName.remove(columnName);
	}

	public boolean containsColumn(ColumnName columnName, String conditionName, String projectTag) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (conditionName.equalsIgnoreCase(column.getExperimentalConditionName())
					&& projectTag.equalsIgnoreCase(column.getProjectTag())) {
				return true;
			}
		}
		return false;
	}

	public boolean containsColumn(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (condition1Name.equalsIgnoreCase(column.getExperimentalConditionName())
					&& condition2Name.equalsIgnoreCase(column.getExperimentalCondition2Name())
					&& projectTag.equalsIgnoreCase(column.getProjectTag())
					&& ratioName.equalsIgnoreCase(column.getRatioName())) {
				return true;
			}
		}
		return false;
	}

	public boolean containsColumn(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (conditionName.equalsIgnoreCase(column.getExperimentalConditionName())
					&& amountType == column.getAmountType() && projectTag.equalsIgnoreCase(column.getProjectTag())) {
				return true;
			}
		}
		return false;
	}

}
