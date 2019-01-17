package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemLongPropertyWidget<T> extends ItemPropertyWidget<T> {

	public ItemLongPropertyWidget(String propertyName, T itemObject, String emptyValueString) {
		super(propertyName, itemObject, emptyValueString);
		propertyValueLabel.getElement().getStyle().setHeight(2, Unit.EM);
	}

	public ItemLongPropertyWidget(String propertyName, T itemObject) {
		this(propertyName, itemObject, DEFAULT_EMPTY_VALUE);

	}

	@Override
	protected TextBoxBase createTextBoxBase() {
		final TextArea textArea = new TextArea();
		textArea.getElement().getStyle().setHeight(2, Unit.EM);
		textArea.setStyleName(WizardStyles.WizardItemWidgetPropertyValueTextArea);
		return textArea;
	}

}
