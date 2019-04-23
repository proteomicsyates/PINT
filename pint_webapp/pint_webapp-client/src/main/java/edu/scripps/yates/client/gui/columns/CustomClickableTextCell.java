package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import edu.scripps.yates.shared.columns.ColumnName;

public class CustomClickableTextCell<T> extends AbstractCell<T> {

	public CustomClickableTextCell(ColumnName columnName) {
		super(BrowserEvents.CLICK);

	}

	@Override
	public void render(Context context, T value, SafeHtmlBuilder sb) {
		// TODO Auto-generated method stub

	}

}
