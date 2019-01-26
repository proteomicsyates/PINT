package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.ValueBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemDoublePropertyWidget<T> extends ItemValueBoxBasePropertyWidget<T, Double> {

	public ItemDoublePropertyWidget(String propertyName, String propertyTitle, T itemObject, String emptyValueString,
			boolean mandatory) {
		super(propertyName, propertyTitle, itemObject, mandatory);
	}

	public ItemDoublePropertyWidget(String propertyName, String propertyTitle, T itemObject, boolean mandatory) {
		this(propertyName, propertyTitle, itemObject, DEFAULT_EMPTY_VALUE, mandatory);

	}

	@Override
	public void beforeEditProperty(boolean edit) {
		// do nothing

	}

	@Override
	protected ValueBoxBase<Double> createValueBoxBase() {
		final DoubleBox textBox = new DoubleBox();
		textBox.setStyleName(WizardStyles.WizardItemWidgetPropertyValueTextArea);
		return textBox;
	}

}
