package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class ItemDraggableLabel extends Label {
	public ItemDraggableLabel(String labelText, DroppableFormat format, String data) {
		super(labelText);
		setStyleName(WizardStyles.WizardDraggableLabel);

		this.getElement().setDraggable(Element.DRAGGABLE_TRUE);
		this.addDragStartHandler(new DragStartHandler() {

			@Override
			public void onDragStart(DragStartEvent event) {
				event.setData(format.name(), data);
				GWT.log("Data set  to '" + data + " with format " + format.name());
			}
		});
	}

}
