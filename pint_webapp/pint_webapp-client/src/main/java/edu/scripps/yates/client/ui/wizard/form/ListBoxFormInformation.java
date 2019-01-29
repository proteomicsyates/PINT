package edu.scripps.yates.client.ui.wizard.form;

import java.util.List;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.ListBox;

public class ListBoxFormInformation extends AbstractFormInformation<String> {

	public ListBoxFormInformation(String question, String explanation, boolean mandatory, boolean multipleSelect) {
		super(question, explanation, mandatory);
		getListBox().setMultipleSelect(multipleSelect);
	}

	@Override
	public FocusWidget createFormWidget() {
		return new ListBox();
	}

	public ListBox getListBox() {
		return ((ListBox) getFormElement());
	}

	@Override
	public String getValueFromWidget() {
		return getListBox().getSelectedValue();
	}

	public void setValues(List<String> items) {
		for (final String item : items) {
			getListBox().addItem(item.toString());
		}
	}
}
