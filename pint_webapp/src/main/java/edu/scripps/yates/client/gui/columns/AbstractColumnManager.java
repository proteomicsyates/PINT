package edu.scripps.yates.client.gui.columns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.columns.footers.FooterManager;
import edu.scripps.yates.client.interfaces.HasColumns;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;

public abstract class AbstractColumnManager<T> {
	protected static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();

	private final List<MyColumn<T>> myColumns = new ArrayList<MyColumn<T>>();
	// protected List<ColumnName> columns;
	// protected List<Header> footers;
	// protected List<Boolean> visibles;
	private final Set<HasColumns> listeners = new HashSet<HasColumns>();
	final FooterManager<T> footerManager;
	private final Map<ColumnName, Set<MyColumn<T>>> columnsByColumnName = new HashMap<ColumnName, Set<MyColumn<T>>>();

	public AbstractColumnManager(FooterManager<T> footerManager) {
		this.footerManager = footerManager;
	}

	public List<MyColumn<T>> getColumns() {
		return myColumns;
	}

	public boolean isVisible(ColumnName columnName) {
		if (this.columnsByColumnName.containsKey(columnName)) {
			for (MyColumn<T> column : this.columnsByColumnName.get(columnName)) {
				if (column.getColumnName().equals(columnName)) {
					return column.isVisible();
				}
			}
		}

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

	public void setVisible(String keyName, boolean visible) {
		notifyChange(keyName, visible);
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

	private void notifyChange(String keyName, boolean visible) {

		for (HasColumns columnSizeChanger : this.listeners) {
			columnSizeChanger.showOrHideExperimentalConditionColumn(keyName, visible);
		}

	}

	public List<ColumnName> getColumnNames() {
		List<ColumnName> list = new ArrayList<ColumnName>();
		for (MyColumn<T> column : this.myColumns) {
			list.add(column.getColumnName());
		}
		return list;

	}

	public List<MyColumn<T>> getVisibleColumns() {
		List<MyColumn<T>> list = new ArrayList<MyColumn<T>>();
		for (MyColumn<T> column : this.myColumns) {
			if (column.isVisible()) {
				list.add(column);
			}
		}
		return list;
	}

	/**
	 * Add a {@link HasColumns} lister which will be called (showOrHideColumn)
	 * when notifyChange in the column manager is called.
	 *
	 * @param listener
	 */
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
	 * @param column
	 * @return the index if it is found. 0 if not.
	 */
	public int getColumnIndex(MyColumn<T> column) {
		int i = 1;
		for (MyColumn<T> column2 : this.myColumns) {
			if (column.equals(column2)) {
				return i;
			}
			i++;
		}
		return 0;
	}

	/**
	 * Add a column to the column manager with information about an score
	 *
	 * @param columnName
	 * @param visibleState
	 * @param scoreName
	 * @return
	 */
	public abstract CustomTextColumn<T> addScoreColumn(ColumnName columnName, boolean visibleState, String scoreName);

	/**
	 * Add a column to the column manager with information about an amount
	 *
	 * @param columnName
	 * @param visibleState
	 * @param conditionName
	 * @param amountType
	 * @param projectName
	 * @return
	 */
	public abstract CustomTextColumn<T> addAmountColumn(ColumnName columnName, boolean visibleState,
			String conditionName, String conditionSymbol, AmountType amountType, String projectName);

	/**
	 * Add a column to the column manager with information about a ratio
	 *
	 * @param columnName
	 * @param visibleState
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @return
	 */
	public abstract CustomTextColumn<T> addRatioColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition1Symbol, String condition2Name, String condition2Symbol,
			String projectTag, String ratioName);

	/**
	 * creates a simple {@link MyColumn}
	 *
	 * @param columnName
	 * @param visible
	 * @return
	 */
	protected abstract MyColumn<T> createColumn(ColumnName columnName, boolean visible);

	/**
	 * Add a column to the column manager with information about an score of a
	 * ratio
	 *
	 * @param columnName
	 * @param visibleState
	 * @param condition1Name
	 * @param condition2Name
	 * @param projectTag
	 * @param ratioName
	 * @param scoreName
	 * @return
	 */
	public abstract CustomTextColumn<T> addRatioScoreColumn(ColumnName columnName, boolean visibleState,
			String condition1Name, String condition1Symbol, String condition2Name, String condition2Symbol,
			String projectTag, String ratioName, String scoreName);

	protected void addColumn(MyColumn<T> column) {
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
			if (column instanceof MyIdColumn) {
				MyIdColumn idColumn = (MyIdColumn) column;
				if (conditionName.equalsIgnoreCase(idColumn.getExperimentalConditionName())
						&& projectTag.equalsIgnoreCase(idColumn.getProjectTag())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsColumn(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (column instanceof MyIdColumn) {
				MyIdColumn idColumn = (MyIdColumn) column;
				if (condition1Name.equalsIgnoreCase(idColumn.getExperimentalConditionName())
						&& condition2Name.equalsIgnoreCase(idColumn.getExperimentalCondition2Name())
						&& projectTag.equalsIgnoreCase(idColumn.getProjectTag())
						&& ratioName.equalsIgnoreCase(idColumn.getRatioName())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean containsColumn(ColumnName columnName, String conditionName, AmountType amountType,
			String projectTag) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (column instanceof MyIdColumn) {
				MyIdColumn idColumn = (MyIdColumn) column;
				if (conditionName.equalsIgnoreCase(idColumn.getExperimentalConditionName())
						&& amountType == idColumn.getAmountType()
						&& projectTag.equalsIgnoreCase(idColumn.getProjectTag())) {
					return true;
				}
			}
		}
		return false;
	}

	public final boolean containsScoreColumn(String scoreName, ColumnName columnName) {
		for (MyColumn<T> column : getColumnsByColumnName(columnName)) {
			if (column instanceof MyIdColumn) {
				MyIdColumn<T> idColumn = (MyIdColumn<T>) column;
				if (scoreName.equalsIgnoreCase(idColumn.getScoreName())) {
					return true;
				}
			}
		}
		return false;
	}

}
