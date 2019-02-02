package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class AbstractItemDropLabel<T> extends Label {
	private final T itemWidget;
	private final DroppableFormat format;
	private boolean hasData = false;
	private final String originalDroppingAreaText;
	private final boolean changeAppearanceOnDrop;

	public AbstractItemDropLabel(String droppingAreaText, DroppableFormat format, T abstractItemWidget,
			boolean mandatory) {
		this(droppingAreaText, format, abstractItemWidget, mandatory, true);
	}

	public AbstractItemDropLabel(String droppingAreaText, DroppableFormat format, T abstractItemWidget,
			boolean mandatory, boolean changeAppearanceOnDrop) {
		super(droppingAreaText);
		this.changeAppearanceOnDrop = changeAppearanceOnDrop;
		if (mandatory) {
			setStyleName(WizardStyles.WizardDragTargetLabel);
		} else {
			setStyleName(WizardStyles.WizardDragTargetLabelOptional);
		}
		this.originalDroppingAreaText = droppingAreaText;
		addStyleName("droppable");
		this.itemWidget = abstractItemWidget;
		this.format = format;
		this.addDropHandler(getDropHandler());
		this.addDragEnterHandler(getDragEnterHandler());
		this.addDragLeaveHandler(getDragLeaveHandler());
		this.addDragOverHandler(getDragOverHandler());
//		addDomHandler(new DropHandler() {
//
//			@Override
//
//			public void onDrop(DropEvent event) {
//				event.preventDefault();
//				GWT.log("on drop 2!!!");
//
//				final String data = event.getData(format.name());
//				setText(data);
//				setStyleName(WizardStyles.WizardDragAndDropLabel);
//				itemWidget.setDropData(data);
//			}
//		}, DropEvent.getType());
	}

	public DropHandler getDropHandler() {
		final DropHandler ret = new DropHandler() {

			@Override
			public void onDrop(DropEvent event) {
				final String itemBeanID = event.getData(format.name());
				GWT.log("'" + itemBeanID + "' data from " + format.name());
				for (final DroppableFormat format2 : DroppableFormat.values()) {
					GWT.log("'" + event.getData(format2.name()) + "' data from " + format2.name());

				}
				if (!"".equals(itemBeanID)) {
					event.preventDefault();
					setDroppedData(itemBeanID);
				} else {
					setText(originalDroppingAreaText);
					setStyleName(WizardStyles.WizardDragTargetLabel);
				}
			}
		};
		return ret;
	}

	public void setDroppedData(String data) {
		hasData = true;
		if (changeAppearanceOnDrop) {
			setText(data);
			setStyleName(WizardStyles.WizardDraggableLabelFixed);
		}
		updateItemWithData(data, format, this.itemWidget);

	}

	protected abstract void updateItemWithData(String data, DroppableFormat format, T itemWidget2);

	public DragOverHandler getDragOverHandler() {
		final DragOverHandler ret = new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				// IMPORTANT, OTHERWISE IT IS NOT WORKING
				event.preventDefault();

			}
		};
		return ret;
	}

	public DragEnterHandler getDragEnterHandler() {
		final DragEnterHandler ret = new DragEnterHandler() {

			@Override
			public void onDragEnter(DragEnterEvent event) {
				final String itemBeanID = event.getData(format.name());
				GWT.log("'" + itemBeanID + "' data from " + format.name());
				for (final DroppableFormat format2 : DroppableFormat.values()) {
					GWT.log("'" + event.getData(format2.name()) + "' data from " + format2.name());

				}
				event.preventDefault();
				setStyleName(WizardStyles.WizardItemWidgetDragEnter);

			}
		};
		return ret;
	}

	public DragLeaveHandler getDragLeaveHandler() {
		final DragLeaveHandler ret = new DragLeaveHandler() {

			@Override
			public void onDragLeave(DragLeaveEvent event) {

				if (!hasData) {
					setStyleName(WizardStyles.WizardDragTargetLabel);
				} else {
					setStyleName(WizardStyles.WizardDraggableLabel);
				}

			}
		};
		return ret;
	}

}
