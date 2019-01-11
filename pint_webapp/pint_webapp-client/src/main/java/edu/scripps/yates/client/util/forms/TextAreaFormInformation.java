package edu.scripps.yates.client.util.forms;

import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.TextArea;

public class TextAreaFormInformation extends AbstractTextBasedFormInformation {

	public TextAreaFormInformation(String question, String explanation, boolean mandatory, Double sizeInEM) {
		super(question, explanation, mandatory, sizeInEM);
	}

	@Override
	public FocusWidget createFormWidget() {
		final TextArea textArea = new TextArea();
		textArea.setVisibleLines(5);
		return textArea;
	}

}
