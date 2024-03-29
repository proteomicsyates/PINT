package edu.scripps.yates.client.ui.wizard.view;

import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.Wizard.Display;

/**
 * HasIndexedWidgets is an interface to describe a class that has multiple
 * indexed widgets, such as the GWT DeckPanel. It exists so that custom
 * implementations of {@link Display} don't have to support a "real" indexed
 * panel. However, in this case, the implementation must keep track of what
 * Widget should be shown at each index (e.g., through a List), as
 * {@link Wizard} calls {@link #showWidget(int)} to show pages.
 * 
 * @author Brandon Tilley
 *
 */
public interface HasIndexedWidgets {
	/**
	 * Add a Widget to the panel (after the last index).
	 * 
	 * @param wiget the Widget to add to the panel
	 */
	public void add(int index, Widget wiget);

	/**
	 * Show the Widget at the specified index.
	 * 
	 * @param index the index of the Widget to show
	 */
	public void showWidget(int index);

	/**
	 * By Salva
	 * 
	 * @param widget
	 */
	public boolean remove(Widget widget);
}
