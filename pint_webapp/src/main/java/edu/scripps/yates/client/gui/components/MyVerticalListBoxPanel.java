package edu.scripps.yates.client.gui.components;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.interfaces.ItemList;

public class MyVerticalListBoxPanel extends Composite implements ItemList {
	protected final ListBox listBox;

	public MyVerticalListBoxPanel(boolean multipleSelect) {
		FlowPanel mainPanel = new FlowPanel();
		// initWidget(mainPanel);
		mainPanel.setStyleName("MainPanel");

		listBox = new ListBox(multipleSelect);
		initWidget(listBox);
		listBox.setStyleName("verticalComponent");
		// mainPanel.add(listBox);
		listBox.setSize("100%", "100%");
		listBox.setVisibleItemCount(5);
	}

	/**
	 * @return the listBox
	 */
	public ListBox getListBox() {
		return listBox;
	}

	public void addClickHandler(ClickHandler clickHandler) {
		listBox.addClickHandler(clickHandler);

	}

	public void addDoubleClickHandler(DoubleClickHandler doubleClickHandler) {
		listBox.addDoubleClickHandler(doubleClickHandler);

	}

	public void addValueChangedHandler(ChangeHandler valueChangeHandler) {
		listBox.addChangeHandler(valueChangeHandler);
	}

	@Override
	public List<String> getItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			ret.add(listBox.getItemText(i));
		}
		return ret;
	}

	@Override
	public List<String> getSelectedItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (listBox.isItemSelected(i))
				ret.add(listBox.getItemText(i));
		}
		return ret;
	}

	@Override
	public List<String> getNonSelectedItemList() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			if (!listBox.isItemSelected(i))
				ret.add(listBox.getItemText(i));
		}
		return ret;
	}

	public void clearList() {
		listBox.clear();

	}

	public void addItem(String item, String value) {
		listBox.addItem(item, value);
	}

	public void selectAllItems(boolean select) {
		for (int i = 0; i < listBox.getItemCount(); i++) {
			listBox.setItemSelected(i, select);
		}
	}
}
