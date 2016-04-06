package edu.scripps.yates.client.gui.columns;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ProvidesKey;

import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.util.DefaultView.ORDER;

public class MyDataGrid<T> extends DataGrid<T> {
	private final Map<ColumnName, Set<Column<T, ?>>> columnMapByColumnName = new HashMap<ColumnName, Set<Column<T, ?>>>();
	private boolean forceToRefresh;

	public MyDataGrid(ProvidesKey<T> keyProvider) {
		super(keyProvider);
		setStyleName("MyDataGrid");
		setSize("100%", "100%");
		// Set the width of the table and put the table in fixed width mode.
		super.setTableWidth(100, Unit.PCT);
		// minimum width of the table = width of the screen minus the width of
		// the left menu of QueryPanel
		final int clientWidth2 = Window.getClientWidth();
		GWT.log(clientWidth2 + "px in window");
		final int clientWidth = clientWidth2 - Double.valueOf(QueryPanel.LEFT_MENU_WIDTH).intValue();
		super.setMinimumTableWidth(clientWidth, Unit.PX);
		super.setAlwaysShowScrollBars(true);
		setAutoHeaderRefreshDisabled(true);

	}

	public void addColumn(ColumnName columnName, Column<T, ?> myColumn, MySafeHtmlHeaderWithTooltip safeHtmlHeader,
			Header<String> footer) {
		super.addColumn(myColumn, safeHtmlHeader, footer);

		addColumnToMap(columnName, myColumn);

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
		super.addColumn(myColumn, headerString);
		addColumnToMap(columnName, myColumn);
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

		setVisibleRangeAndClearData(getVisibleRange(), true);

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

	public boolean isEmptyColumn(Column<T, String> column) {
		final List<T> visibleItems = getVisibleItems();
		for (T t : visibleItems) {
			if (!"".equals(column.getValue(t)))
				return false;
		}
		return true;
	}

	public boolean isNumberColumn(Column<T, String> column) {
		final List<T> visibleItems = getVisibleItems();
		for (T t : visibleItems) {
			final String value = column.getValue(t);
			try {
				Double.valueOf(value);
			} catch (NumberFormatException e) {
				if (!"".equals(value) && !"-".equals(value))
					return false;
			} catch (NullPointerException e) {

			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column)
	 */
	@Override
	public void addColumn(Column<T, ?> col) {
		addColumnToMap(col);
		super.addColumn(col);
	}

	private void addColumnToMap(Column<T, ?> col) {
		if (col instanceof MyColumn) {
			MyColumn<T> mycol = (MyColumn<T>) col;
			addColumnToMap(mycol.getColumnName(), col);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.user.cellview.client.Header)
	 */
	@Override
	public void addColumn(Column<T, ?> col, Header<?> header) {
		addColumnToMap(col);

		super.addColumn(col, header);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.user.cellview.client.Header,
	 * com.google.gwt.user.cellview.client.Header)
	 */
	@Override
	public void addColumn(Column<T, ?> col, Header<?> header, Header<?> footer) {
		addColumnToMap(col);

		super.addColumn(col, header, footer);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column, java.lang.String)
	 */
	@Override
	public void addColumn(Column<T, ?> col, String headerString) {
		addColumnToMap(col);

		super.addColumn(col, headerString);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void addColumn(Column<T, ?> col, SafeHtml headerHtml) {
		addColumnToMap(col);

		super.addColumn(col, headerHtml);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void addColumn(Column<T, ?> col, String headerString, String footerString) {
		addColumnToMap(col);

		super.addColumn(col, headerString, footerString);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.cellview.client.AbstractCellTable#addColumn(com.
	 * google .gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void addColumn(Column<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml) {
		addColumnToMap(col);

		super.addColumn(col, headerHtml, footerHtml);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column,
	 * com.google.gwt.user.cellview.client.Header)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, header);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column,
	 * com.google.gwt.user.cellview.client.Header,
	 * com.google.gwt.user.cellview.client.Header)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, Header<?> header, Header<?> footer) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, header, footer);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column, java.lang.String)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, String headerString) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, headerString);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, SafeHtml headerHtml) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, headerHtml);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, String headerString, String footerString) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, headerString, footerString);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#insertColumn(int,
	 * com.google.gwt.user.cellview.client.Column,
	 * com.google.gwt.safehtml.shared.SafeHtml,
	 * com.google.gwt.safehtml.shared.SafeHtml)
	 */
	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col, SafeHtml headerHtml, SafeHtml footerHtml) {
		addColumnToMap(col);

		super.insertColumn(beforeIndex, col, headerHtml, footerHtml);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#removeColumn(com
	 * .google.gwt.user.cellview.client.Column)
	 */
	@Override
	public void removeColumn(Column<T, ?> col) {
		if (col instanceof MyColumn) {
			MyColumn<T> mycol = (MyColumn<T>) col;
			columnMapByColumnName.remove(mycol.getColumnName());
		}

		super.removeColumn(col);
	}

	/*
	 * (non-Javadoc)
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
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#setColumnWidth(
	 * com.google.gwt.user.cellview.client.Column, java.lang.String)
	 */
	@Override
	public void setColumnWidth(Column<T, ?> column, String width) {
		super.setColumnWidth(column, width);
		updateTableMinimumWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.AbstractCellTable#setColumnWidth(int,
	 * java.lang.String)
	 */
	@Override
	public void setColumnWidth(int column, String width) {
		super.setColumnWidth(column, width);
		updateTableMinimumWidth();
	}

	public void updateTableMinimumWidth() {
		double numPixels = 0;
		for (int i = 0; i < getColumnCount(); i++) {
			final Column<T, ?> column = getColumn(i);
			final String columnWidth = getColumnWidth(column);

			final Double valueOf = Double.valueOf(columnWidth.substring(0, columnWidth.indexOf("px")));
			numPixels += valueOf;

		}
		numPixels += 100;
		setMinimumTableWidth(numPixels, Unit.PX);
	}

	/**
	 * Sorts the datagrid according to one provided column and an order
	 *
	 * @param column
	 * @param order
	 */
	public void sortColumn(Column<T, ?> column, ORDER order) {
		pushColumnToColumnSortList(column, order);
		// fire event to actually sort the table
		ColumnSortEvent.fire(this, getColumnSortList());
	}

	public void pushColumnToColumnSortList(Column<T, ?> column, ORDER order) {
		// push the column to be sorted
		getColumnSortList().push(column);
		// if descending, sort again
		if (ORDER.DESCENDING.equals(order)) {
			getColumnSortList().push(column);
		}
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

}
