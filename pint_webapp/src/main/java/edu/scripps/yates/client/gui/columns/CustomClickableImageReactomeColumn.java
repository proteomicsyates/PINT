package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import org.reactome.web.analysis.client.model.PathwaySummary;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.reactome.ReactomePanel;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.columns.ColumnName;

public class CustomClickableImageReactomeColumn extends AbsctractCustomClickableImageColumn<PathwaySummary> {
	private final String sessionID;

	public CustomClickableImageReactomeColumn(String sessionID, ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(columnName, visibleState, footer, new MyTextHeader(columnName, "ID"), 30);
		this.sessionID = sessionID;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent)
	 */
	@Override
	public void onBrowserEventImplementation(Context context, Element elem, final PathwaySummary object,
			NativeEvent event) {
		final String type = event.getType();
		GWT.log("onBrowser event in Reactome clickable column: " + type);
		if (type.equals(BrowserEvents.CLICK)) {

			PathwaySummary pathWay = object;
			final String stId = pathWay.getStId();
			final Long dbId = pathWay.getDbId();
			ReactomePanel.getInstance(sessionID).selectPathWay(stId, dbId);

		}

	}

	@Override
	public ImageResource getValue(PathwaySummary object) {
		return MyClientBundle.INSTANCE.fireworksIcon();
	}

	@Override
	public Comparator<PathwaySummary> getComparator() {
		return null;
	}

}
