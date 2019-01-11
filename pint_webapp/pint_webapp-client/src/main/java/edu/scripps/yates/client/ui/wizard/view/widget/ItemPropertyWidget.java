package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemPropertyWidget<T> extends FlexTable implements UpdateProperty<T> {

	protected final Label propertyValueLabel;
	private final TextBoxBase propertyValueTextBox;
	protected String previousNameValue;
	protected final static String DEFAULT_EMPTY_VALUE = "<not defined>";
	private final String emptyValueString;

	public ItemPropertyWidget(String propertyName, T itemObject) {
		this(propertyName, itemObject, DEFAULT_EMPTY_VALUE);
	}

	public ItemPropertyWidget(String propertyName, T itemObject, final String emptyValueString) {
		this.emptyValueString = emptyValueString;
		final Label propertyNameLabel = new Label(propertyName);
		propertyNameLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
		setWidget(0, 0, propertyNameLabel);
		getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		String propertyValue = getPropertyValueFromItem(itemObject);
		if (propertyValue == null) {
			propertyValue = emptyValueString;
		}
		propertyValueLabel = new Label(propertyValue);
		propertyValueLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyValueLabel);
		editProperty(false);
		getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

		propertyValueTextBox = createTextBoxBase();
		propertyValueTextBox.setValue(propertyValue);

		// when clicked, show textBox and focus on it
		propertyValueLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editProperty(true);

				// give focus to textbox
				propertyValueTextBox.setFocus(true);
			}
		});
		// if looses focus:
		// if text is empty, keep previous
		propertyValueTextBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if ("".equals(propertyValueTextBox.getValue())) {
					propertyValueTextBox.setValue(previousNameValue);
					// reset previous name
					previousNameValue = null;
				}
				editProperty(false);
			}
		});
		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		propertyValueTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editProperty(false);
				} else if (!(propertyValueTextBox instanceof TextArea)
						&& (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
								|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER)) {
					editProperty(false);
				} else {
					updateItemObjectProperty(itemObject, propertyValueTextBox.getValue());
					propertyValueLabel.setText(propertyValueTextBox.getValue());
					if (previousNameValue == null) {
						previousNameValue = propertyValueTextBox.getValue();
					}
				}
			}
		});

	}

	protected TextBoxBase createTextBoxBase() {
		final TextBox textBox = new TextBox();
		textBox.setStyleName(WizardStyles.WizardItemWidgetPropertyValueTextBox);
		return textBox;
	}

	private void editProperty(boolean edit) {
		if (edit) {
			setWidget(0, 1, propertyValueTextBox);
			if (propertyValueTextBox.getValue().equals(emptyValueString)) {
				propertyValueTextBox.setValue(null);
			}
		} else {
			if ("".equals(propertyValueLabel.getText())) {
				propertyValueLabel.setText(emptyValueString);
			}
			setWidget(0, 1, propertyValueLabel);
		}
	}

}
