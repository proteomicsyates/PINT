package edu.scripps.yates.client.gui.components;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * This is an {@link ScrollPanel} that automatically resizes to the client
 * window browser size
 * 
 * @author Salva
 * 
 */
public class MyWindowScrollPanel extends ScrollPanel {
	public MyWindowScrollPanel() {
		setSize("100%", Window.getClientHeight() + "px");
		Window.addResizeHandler(getResizeHandler());
	}

	private ResizeHandler getResizeHandler() {
		ResizeHandler handler = new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				setHeight(event.getHeight() + "px");
				setWidth(event.getWidth() + "px");
			}
		};
		return handler;
	}
}
