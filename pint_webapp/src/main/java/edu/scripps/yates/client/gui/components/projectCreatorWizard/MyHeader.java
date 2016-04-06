package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.HasText;

public class MyHeader extends Header<String> {
	private final HasText hasText;

	public MyHeader(HasText hasText) {
		super(new CustomCell());
		this.hasText = hasText;

	}

	@Override
	public String getValue() {
		return hasText.getText();
	}

}