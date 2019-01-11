package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.shared.columns.ColumnName;

public interface MyColumn<T> {

	public ColumnName getColumnName();

	public boolean isVisible();

	public Header<String> getFooter();

	public Header<?> getHeader();

	public Unit getDefaultWidthUnit();

	public double getDefaultWidth();

	public double getWidth();

	public void setWidth(double width);

	public Comparator<T> getComparator();

	public void setVisible(boolean visible);

}
