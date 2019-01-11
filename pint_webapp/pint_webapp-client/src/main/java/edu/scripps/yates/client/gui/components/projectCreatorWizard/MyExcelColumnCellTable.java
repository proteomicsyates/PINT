package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;

public class MyExcelColumnCellTable<T> extends CellTable<T> {
	private final List<CustomColumn<T>> columns = new ArrayList<CustomColumn<T>>();
	private final ListDataProvider<T> dataProvider = new ListDataProvider<T>();

	public MyExcelColumnCellTable() {
		dataProvider.addDataDisplay(this);
		setEmptyTableWidget(new Label("(Click on check to load the table)"));
		setSize("100%", "100%");
		setPageSize(100);
	}

	public void addData(Collection<T> data) {
		this.dataProvider.getList().addAll(data);
	}

	public void addData(T data) {
		this.dataProvider.getList().add(data);
	}

	public void clearData() {
		this.dataProvider.getList().clear();
	}

	public void addColumn(CustomColumn<T> column) {
		this.columns.add(column);
		column.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		String headerName = column.getHeaderName();
		if (headerName == null)
			headerName = "header name";
		super.addColumn(column, headerName);
	}

	public void addColumn(CustomColumn<T> column, Header<?> header) {
		this.columns.add(column);
		column.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		super.addColumn(column, header);
	}

	public boolean isEmpty() {
		return this.dataProvider.getList().isEmpty();
	}

	/**
	 * @return the columns
	 */
	public List<CustomColumn<T>> getColumns() {
		return columns;
	}

	/**
	 * @return the dataProvider
	 */
	public ListDataProvider<T> getDataProvider() {
		return dataProvider;
	}

}
