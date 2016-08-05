package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.reactome.ReactomePanel;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.columns.ColumnName;

public class CustomClickableImageReactomeColumn<T> extends Column<T, ImageResource> implements MyColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final String sessionID;

	public CustomClickableImageReactomeColumn(String sessionID, ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(new CustomImageCell());
		this.sessionID = sessionID;
		this.columnName = columnName;
		this.visibleState = visibleState;
		defaultWidth = 30;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}
		setCellStyleNames("clickableImageColumn");

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent)
	 */
	@Override
	public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
		final String type = event.getType();
		GWT.log("onBrowser event in Reactome clickable column: " + type);
		if (type.equals(BrowserEvents.CLICK)) {
			if (object instanceof PathwaySummary) {
				PathwaySummary pathWay = (PathwaySummary) object;
				final String stId = pathWay.getStId();
				final Long dbId = pathWay.getDbId();
				ReactomePanel.getInstance(sessionID).selectPathWay(stId, dbId);
			}
		}

		super.onBrowserEvent(context, elem, object, event);
	}

	@Override
	public ColumnName getColumnName() {
		return columnName;
	}

	@Override
	public boolean isVisible() {
		return visibleState;
	}

	@Override
	public Header<String> getFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit getDefaultWidthUnit() {
		return Unit.PX;
	}

	@Override
	public double getDefaultWidth() {
		return defaultWidth;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public Comparator<T> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public ImageResource getValue(T object) {
		return MyClientBundle.INSTANCE.fireworksIcon();
	}

}
