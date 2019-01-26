package edu.scripps.yates.client.ui.wizard.pages.widgets;

public interface UpdateProperty<T, P> {
	public void updateItemObjectProperty(T item, P propertyValue);

	public P getPropertyValueFromItem(T item);
}
