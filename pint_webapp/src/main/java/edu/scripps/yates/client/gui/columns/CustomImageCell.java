package edu.scripps.yates.client.gui.columns;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

public class CustomImageCell extends AbstractCell<ImageResource> {
	private static ImageResourceRenderer renderer;

	/**
	 * Construct a new ImageResourceCell.
	 */
	public CustomImageCell() {
		super(BrowserEvents.CLICK);
		if (renderer == null) {
			renderer = new ImageResourceRenderer();
		}
	}

	@Override
	public void render(Context context, ImageResource value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(renderer.render(value));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent,
	 * com.google.gwt.cell.client.ValueUpdater)
	 */
	@Override
	public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, ImageResource value,
			NativeEvent event, ValueUpdater<ImageResource> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
	}

}
