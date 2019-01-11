package edu.scripps.yates.client.gui.columns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.DefaultView.ORDER;

public class MyDataGrid<T> extends CellTable<T> {
	private final Map<ColumnName, Set<Column<T, ?>>> columnMapByColumnName = new HashMap<ColumnName, Set<Column<T, ?>>>();
	private boolean forceToRefresh;
	private boolean forceToReload;
	private final String tableName;

	public MyDataGrid(ProvidesKey<T> keyProvider, String tableName) {
		super(keyProvider);
		this.tableName = tableName;
		setStyleName("MyDataGrid");
		setWidth("100%");
		// Set the width of the table and put the table in fixed width mode.
		// datagrid:
		// super.setTableWidth(100, Unit.PCT);
		// celltable:
		super.setWidth("100%", false);
		// minimum width of the table = width of the screen minus the width of
		// the left menu of QueryPanel
		final int clientWidth2 = Window.getClientWidth();
		GWT.log(clientWidth2 + "px in window");
		final int clientWidth = clientWidth2 - Double.valueOf(QueryPanel.LEFT_MENU_WIDTH).intValue();

		// datagrid:
		// super.setMinimumTableWidth(clientWidth, Unit.PX);
		// super.setAlwaysShowScrollBars(true);
		// celltable:

		setAutoHeaderRefreshDisabled(true);

	}

	public void addColumn(ColumnName columnName, Column<T, ?> myColumn, Header<?> safeHtmlHeader,
			Header<String> footer) {
		GWT.log("Adding column " + ((MyColumn) myColumn).getColumnName() + " to table " + tableName + " with header "
				+ safeHtmlHeader.getValue());
		addColumnToMap(columnName, myColumn);
		super.addColumn(myColumn, safeHtmlHeader, footer);

	}

	private void addColumnToMap(ColumnName columnName, Column<T, ?> myColumn) {
		// add to map
		if (this.columnMapByColumnName.containsKey(columnName)) {
			this.columnMapByColumnName.get(columnName).add(myColumn);
		} else {
			Set<Column<T, ?>> set = new HashSet<Column<T, ?>>();
			set.add(myColumn);
			this.columnMapByColumnName.put(columnName, set);
		}

	}

	public void addColumn(ColumnName columnName, Column<T, ?> myColumn, String headerString) {
		GWT.log("Adding column " + ((MyColumn) myColumn).getColumnName() + " to table " + tableName + " with header "
				+ headerString);
		addColumnToMap(columnName, myColumn);
		addColumn(myColumn, new TextHeader(headerString));
	}

	public void removeColumns(ColumnName columnName) {
		if (this.columnMapByColumnName.containsKey(columnName)) {
			final Set<Column<T, ?>> set = this.columnMapByColumnName.get(columnName);
			// remove the columns
			for (Column<T, ?> column : set) {
				this.removeColumn(column);
			}
			// remove from map
			this.columnMapByColumnName.remove(columnName);
		}
	}

	/**
	 * Calls to redrawRow to the visible rows
	 */
	public void redrawVisibleItems() {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				setForceToRefresh(true);
				setVisibleRangeAndClearData(getVisibleRange(), true);
			}
		});

		// final Range visibleRange = getVisibleRange();
		// final int start = visibleRange.getStart();
		// final int length = visibleRange.getLength();
		// final int visibleItemCount = getVisibleItemCount();
		// for (int i = 0; i < length; i++) {
		// if (i == visibleItemCount)
		// break;
		// int absRowIndex = start + i;
		// redrawRow(absRowIndex);
		// }
	}

	public boolean isEmptyColumn(Column<T, ?> column) {
		final List<T> visibleItems = getVisibleItems();
		for (T t : visibleItems) {
			if (!"".equals(column.getValue(t)))
				return false;
		}
		return true;
	}

	public boolean isNumberColumn(Column<T, ?> column) {
		final List<T> visibleItems = getVisibleItems();
		for (T t : visibleItems) {
			final Object value = column.getValue(t);
			if (value instanceof String) {
				try {
					Double.valueOf((String) value);
				} catch (NumberFormatException e) {
					if (!"".equals(value) && !"-".equals(value))
						return false;
				} catch (NullPointerException e) {

				}
			}
		}
		return true;
	}

	/**
	 * Adds a column in the proper position
	 *
	 * @param col
	 * @param columnManager
	 */
	public void addColumnToTable(Column<T, ?> col, AbstractColumnManager<T> columnManager) {
		if (containsColumn(col)) {
			return;
		}

		addColumnToMap(col);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				Header<?> header = null;
				if (col instanceof MyColumn) {
					header = ((MyColumn) col).getHeader();
				}
				// set default width to column
				final MyColumn myColumn = (MyColumn) col;
				final String width = String.valueOf(myColumn.getDefaultWidth()) + Unit.PX;
				setColumnWidth(col, width);

				// get index
				int index = getIndexForColumnInTable(myColumn, columnManager);

				insertColumn(index, col, header);
			}
		});

	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header) {
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ header.getValue());
		super.insertColumn(beforeIndex, col, header);
	}

	private int getIndexForColumnInTable(MyColumn myColumn, AbstractColumnManager<T> columnManager) {
		int index = 0;
		for (index = 0; index < getColumnCount(); index++) {
			final MyColumn column = (MyColumn) getColumn(index);
			if (columnManager.getColumnIndex(column) >= columnManager.getColumnIndex(myColumn)) {
				break;
			}
		}
		return index;
	}

	private void addColumnToMap(Column<T, ?> col) {
		if (col instanceof MyColumn) {
			MyColumn<T> mycol = (MyColumn<T>) col;
			addColumnToMap(mycol.getColumnName(), col);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.user.cellview.client.Header)
	 */
	@Override
	public void addColumn(Column<T, ?> col, Header<?> header) {
		if (containsColumn(col)) {
			return;
		}
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ header.getValue());
		addColumnToMap(col);
		addColumn(col, header, null);
	}

	private boolean containsColumn(Column<T, ?> col) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (col.equals(getColumn(i))) {
				return true;
			}
		}
		if (col instanceof MyColumn){
			final MyColumn myCol = (MyColumn)col;
			if (columnMapByColumnName.containsKey(myCol.getColumnName())){
				final Set<Column<T, ?>> set = columnMapByColumnName.get(myCol.getColumnName());
				for (Column<T, ?> column : set) {
					 if (column.equals(col)){
						 return true;
					 }
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column, java.lang.String)
	 */
	@Override
	public void addColumn(Column<T, ?> col, String headerString) {
		if (containsColumn(col)) {
			return;
		}
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ headerString);
		addColumnToMap(col);

		addColumn(col, new TextHeader(headerString));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void addColumn(Column<T, ?> col, SafeHtml headerHtml) {
		if (containsColumn(col)) {
			return;
		}
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ headerHtml.asString());
		addColumnToMap(col);

		addColumn(col, headerHtml, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addColumn(Column<T, ?> col, String headerString, String footerString) {
		if (containsColumn(col)) {
			return;
		}
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ headerString);
		addColumnToMap(col);

		addColumn(col, new TextHeader(headerString), new TextHeader(footerString));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void addColumn(Column<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml) {
		if (containsColumn(col)) {
			return;
		}
		GWT.log("Adding column " + ((MyColumn) col).getColumnName() + " to table " + tableName + " with header "
				+ headerHtml.asString());
		addColumnToMap(col);

		addColumn(col, new SafeHtmlHeader(headerHtml), new SafeHtmlHeader(footerHtml));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#removeColumn(com
	 * .google.gwt.user.cellview.client.Column)
	 */
	@Override
	public void removeColumn(Column<T, ?> col) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				if (col instanceof MyColumn) {
					MyColumn<T> mycol = (MyColumn<T>) col;
					columnMapByColumnName.remove(mycol.getColumnName());
				}
				if (containsColumn(col)) {
					MyDataGrid.super.removeColumn(col);
				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#removeColumn(int)
	 */
	@Override
	public void removeColumn(int index) {
		// TODO Auto-generated method stub
		super.removeColumn(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#setColumnWidth(
	 * com.google.gwt.user.cellview.client.Column, java.lang.String)
	 */
	@Override
	public void setColumnWidth(Column<T, ?> column, String width) {
		super.setColumnWidth(column, width);

		// updateTableMinimumWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#setColumnWidth(int,
	 * java.lang.String)
	 */
	@Override
	public void setColumnWidth(int column, String width) {
		super.setColumnWidth(column, width);
		// updateTableMinimumWidth();
	}

	// public void updateTableMinimumWidth() {
	// double numPixels = 0;
	// for (int i = 0; i < getColumnCount(); i++) {
	// final Column<T, ?> column = getColumn(i);
	// final String columnWidth = getColumnWidth(column);
	//
	// final Double valueOf = Double.valueOf(columnWidth.substring(0,
	// columnWidth.indexOf("px")));
	// numPixels += valueOf;
	//
	// }
	// numPixels += 100;
	// setMinimumTableWidth(numPixels, Unit.PX);
	//
	// }

	/**
	 * Sorts the datagrid according to one provided column and an order
	 *
	 * @param column
	 * @param order
	 */
	private void sortColumn(Column<T, ?> column, ORDER order) {
		pushColumnToColumnSortList(column, order);
		// fire event to actually sort the table
		ColumnSortEvent.fire(this, getColumnSortList());
	}

	public void pushColumnToColumnSortList(Column<T, ?> column, ORDER order) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				// push the column to be sorted
				getColumnSortList().push(column);
				// if descending, sort again
				if (ORDER.DESCENDING.equals(order)) {
					getColumnSortList().push(column);
				}
			}
		});

	}

	/**
	 * @return the forceToRefresh
	 */
	public boolean isForceToRefresh() {
		return forceToRefresh;
	}

	/**
	 * @param forceToRefresh
	 *            the forceToRefresh to set
	 */
	public void setForceToRefresh(boolean forceToRefresh) {
		this.forceToRefresh = forceToRefresh;
	}

	/**
	 * @return the forceToReload
	 */
	public boolean isForceToReload() {
		return forceToReload;
	}

	/**
	 * @param forceToReload
	 *            the forceToReload to set
	 */
	public void setforceToReload(boolean forceToReload) {
		this.forceToReload = forceToReload;
	}

}
