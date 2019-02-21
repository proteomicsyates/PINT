package edu.scripps.yates.client.util;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextBox;

public class ExtendedTextBox extends TextBox implements HasHandlers {
	private final HandlerManager handlerManager;

	public ExtendedTextBox() {
		super();

		handlerManager = new HandlerManager(this);

		// For all browsers - catch onKeyUp
		sinkEvents(Event.ONKEYUP);

		// For IE and Firefox - catch onPaste
		sinkEvents(Event.ONPASTE);

		// For Opera - catch onInput
//		sinkEvents(Event.ONINPUT);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch (DOM.eventGetType(event)) {
		case Event.ONKEYUP:
		case Event.ONPASTE:
//		case Event.ONINPUT:
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {

				@Override
				public void execute() {
					fireEvent(new TextChangeEvent());
				}

			});
			break;

		default:
			// do nothing
		}
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addTextChangeEventHandler(TextChangeEventHandler handler) {
		return handlerManager.addHandler(TextChangeEvent.TYPE, handler);
	}
}
