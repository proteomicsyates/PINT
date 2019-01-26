package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemTextAreaPropertyWidget<T> extends ItemValueBoxBasePropertyWidget<T, String> {

	private ItemTextAreaPropertyWidget(String propertyName, String propertyTitle, T itemObject, String emptyValueString,
			boolean mandatory) {
		super(propertyName, propertyTitle, itemObject, emptyValueString, mandatory);
	}

	public ItemTextAreaPropertyWidget(String propertyName, String propertyTitle, T itemObject, boolean mandatory) {
		this(propertyName, propertyTitle, itemObject, DEFAULT_EMPTY_VALUE, mandatory);

	}

	@Override
	protected ValueBoxBase<String> createValueBoxBase() {
		final TextArea textArea = new TextArea();
		textArea.getElement().getStyle().setHeight(2, Unit.EM);
		textArea.setStyleName(WizardStyles.WizardItemWidgetPropertyValueTextArea);
		return textArea;
	}

	@Override
	public void beforeEditProperty(boolean edit) {
		if (edit) {
			getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
			getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
			getWidget(0, 0).getElement().getStyle().setHeight(2, Unit.EM);

		} else {
			getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
			getWidget(0, 0).getElement().getStyle().setHeight(1, Unit.EM);
		}
	}

}
