package edu.scripps.yates.client.util.forms;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextBox;

public class TextBoxFormInformation extends AbstractTextBasedFormInformation {

	public TextBoxFormInformation(String question, String explanation, boolean mandatory, Double sizeInEM) {
		super(question, explanation, mandatory, sizeInEM);
	}

	@Override
	public FocusWidget createFormWidget() {
		return new TextBox();
	}

}
