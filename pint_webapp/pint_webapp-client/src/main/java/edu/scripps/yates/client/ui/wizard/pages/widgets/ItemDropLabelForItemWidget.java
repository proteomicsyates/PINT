package edu.scripps.yates.client.ui.wizard.pages.widgets;

public class ItemDropLabelForItemWidget extends AbstractItemDropLabel<AbstractItemWidget> {

	public ItemDropLabelForItemWidget(String droppingAreaText, DroppableFormat format,
			AbstractItemWidget abstractItemWidget) {
		super(droppingAreaText, format, abstractItemWidget);

	}

	@Override
	protected void updateItemWithData(String data, DroppableFormat format, AbstractItemWidget itemWidget) {
		itemWidget.updateReferencedItemBeanID(data, format);
	}

}
