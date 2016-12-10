package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.util.HorizontalAlignmentUtil;
import edu.scripps.yates.shared.columns.ColumnName;

public abstract class AbsctractCustomClickableImageColumn<T> extends Column<T, ImageResource> implements MyColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	protected final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private final Header<String> footer;
	private final Header<?> header;

	public AbsctractCustomClickableImageColumn(ColumnName columnName, boolean visibleState, Header<String> footer,
			Header<?> header, int defaultWidth) {
		super(new CustomImageCell());
		this.columnName = columnName;
		this.visibleState = visibleState;
		this.defaultWidth = defaultWidth;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}

		this.footer = footer;
		this.header = header;
		setHorizontalAlignment(
				HorizontalAlignmentUtil.getHorizontalAlignmentConstant(columnName.getHorizontalAlignment()));
		setCellStyleNames("clickableImageColumn");

	}

	@Override
	public final void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
		onBrowserEventImplementation(context, elem, object, event);
		super.onBrowserEvent(context, elem, object, event);

	}

	public abstract void onBrowserEventImplementation(Context context, Element elem, T object, NativeEvent event);

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
		return footer;
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
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public abstract ImageResource getValue(T object);

	@Override
	public Header<?> getHeader() {
		return header;
	}

}
