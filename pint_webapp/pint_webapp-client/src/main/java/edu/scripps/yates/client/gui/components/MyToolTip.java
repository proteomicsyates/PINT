package edu.scripps.yates.client.gui.components;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;

public class MyToolTip extends PopupPanel {
	/**
	 * @param html
	 *            is to pass tip information.
	 */
	public MyToolTip(final HTMLPanel html) {
		super(true);
		setWidget(html);
	}
}
