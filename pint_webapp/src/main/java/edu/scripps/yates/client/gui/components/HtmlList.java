package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

public class HtmlList extends Widget {
	private final Map<LIElement, Command> listItemsMap = new HashMap<LIElement, Command>();
	private final List<LIElement> listItems = new ArrayList<LIElement>();

	public static enum ListType {
		UNORDERED {
			@Override
			public Element createElement() {
				return Document.get().createULElement();
			}
		},
		ORDERED {
			@Override
			public Element createElement() {
				return Document.get().createULElement();
			}
		};

		public abstract Element createElement();
	}

	public HtmlList(ListType listType) {
		setElement(listType.createElement());
		setStylePrimaryName("html-list");
	}

	public void addItem(String text, Command command) {
		LIElement liElement = Document.get().createLIElement();
		liElement.setInnerSafeHtml(new SafeHtmlBuilder().appendEscaped(text).toSafeHtml());
		getElement().appendChild(liElement);

		if (command != null)
			listItemsMap.put(liElement, command);
		listItems.add(liElement);

		sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.ONCLICK);
	}

	@Override
	public void onBrowserEvent(Event event) {

		switch (event.getTypeInt()) {
		case Event.ONCLICK:
			Element target = event.getTarget();
			if (listItemsMap.containsKey(target))
				Scheduler.get().scheduleDeferred(listItemsMap.get(target));
			break;

		case Event.ONMOUSEOUT:
			event.getTarget().setClassName("html-list");
			event.getTarget().getStyle().setFontWeight(FontWeight.NORMAL);
			break;

		case Event.ONMOUSEOVER:
			event.getTarget().getStyle().setFontWeight(FontWeight.BOLDER);
			;
			break;
		}
	}

	public void setText(String text, int listIndex) {
		listItems.get(listIndex).setInnerText(text);
	}

	public void setTextAndTitle(String text, String title, int listIndex) {
		listItems.get(listIndex).setInnerText(text);
		listItems.get(listIndex).setTitle(title);
	}
}
