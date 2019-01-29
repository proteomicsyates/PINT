package edu.scripps.yates.client.ui.wizard.form;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FocusWidget;

public class CheckBoxFormInformation extends AbstractFormInformation<Boolean> {

	public CheckBoxFormInformation(String question, String explanation, boolean mandatory) {
		super(question, explanation, mandatory);
	}

	@Override
	public FocusWidget createFormWidget() {
		return new CheckBox();
	}

	public CheckBox getCheckBox() {
		return (CheckBox) formElement;
	}

	@Override
	public Boolean getValueFromWidget() {
		return getCheckBox().getValue();
	}

}
