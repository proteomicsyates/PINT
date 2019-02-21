package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.ValueBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ExtendedTextBox;

public abstract class ItemTextBoxPropertyWidget<T> extends ItemValueBoxBasePropertyWidget<T, String> {

	private ItemTextBoxPropertyWidget(String propertyName, String propertyTitle, T itemObject, String emptyValueString,
			boolean mandatory) {
		super(propertyName, propertyTitle, itemObject, emptyValueString, mandatory);
	}

	public ItemTextBoxPropertyWidget(String propertyName, String propertyTitle, T itemObject, boolean mandatory) {
		this(propertyName, propertyTitle, itemObject, DEFAULT_EMPTY_VALUE, mandatory);

	}

	@Override
	protected ValueBoxBase<String> createValueBoxBase() {
		final ExtendedTextBox textBox = new ExtendedTextBox();
		textBox.setStyleName(WizardStyles.WizardItemWidgetPropertyValueTextArea);
		return textBox;
	}

}
