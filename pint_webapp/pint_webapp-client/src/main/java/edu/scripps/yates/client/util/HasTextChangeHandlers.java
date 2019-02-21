package edu.scripps.yates.client.util;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasTextChangeHandlers extends HasHandlers {
	public HandlerRegistration addTextChangeEventHandler(TextChangeEventHandler handler);
}
