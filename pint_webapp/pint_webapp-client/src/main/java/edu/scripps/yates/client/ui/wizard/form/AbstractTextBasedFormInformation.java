package edu.scripps.yates.client.ui.wizard.form;

import com.google.gwt.user.client.ui.TextBoxBase;

public abstract class AbstractTextBasedFormInformation extends AbstractFormInformation<String> {

	public AbstractTextBasedFormInformation(String question, String explanation, boolean mandatory, Double sizeInEM) {
		super(question, explanation, mandatory, sizeInEM);
	}

	public TextBoxBase getTextBox() {

		return (TextBoxBase) formElement;

	}

	@Override
	public String getValueFromWidget() {
		return getTextBox().getValue().trim();
	}
}
