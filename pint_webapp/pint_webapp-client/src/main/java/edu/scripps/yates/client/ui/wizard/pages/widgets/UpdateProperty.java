package edu.scripps.yates.client.ui.wizard.pages.widgets;

public interface UpdateProperty<T> {
	public void updateItemObjectProperty(T item, String propertyValue);

	public String getPropertyValueFromItem(T item);
}
