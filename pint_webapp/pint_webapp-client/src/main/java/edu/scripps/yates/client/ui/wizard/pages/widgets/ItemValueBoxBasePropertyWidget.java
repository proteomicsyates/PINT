package edu.scripps.yates.client.ui.wizard.pages.widgets;

import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemValueBoxBasePropertyWidget<T, V> extends FlexTable implements UpdateProperty<T, V> {

	protected final Label propertyValueLabel;
	private final ValueBoxBase<V> propertyValueValueBox;
	protected V previousNameValue;
	protected final static String DEFAULT_EMPTY_VALUE = "<not defined>";
	private final String emptyValueString;
	private final boolean mandatory;

	public ItemValueBoxBasePropertyWidget(String propertyName, String propertyTitle, T itemObject, boolean mandatory) {
		this(propertyName, propertyTitle, itemObject, DEFAULT_EMPTY_VALUE, mandatory);
	}

	public ItemValueBoxBasePropertyWidget(String propertyName, String propertyTitle, T itemObject,
			final String emptyValueString, boolean mandatory) {
		this.emptyValueString = emptyValueString;
		this.mandatory = mandatory;
		if (!mandatory) {
			final Label propertyNameLabel = new Label(propertyName);
			propertyNameLabel.setTitle(propertyTitle);
			propertyNameLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
			final Label optionalLabel = new Label("(optional)");
			optionalLabel.setTitle("This property is optional");
			optionalLabel.getElement().getStyle().setMarginLeft(5, Unit.PX);
			optionalLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabelSmaller);
			final HorizontalPanel panel = new HorizontalPanel();
			panel.add(propertyNameLabel);
			panel.add(optionalLabel);
			setWidget(0, 0, panel);
		} else {
			final Label propertyNameLabel = new Label(propertyName);
			propertyNameLabel.setTitle(propertyTitle);
			propertyNameLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyNameLabel);
			setWidget(0, 0, propertyNameLabel);
		}
		getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		final V propertyValue = getPropertyValueFromItem(itemObject);
		if (propertyValue == null) {
			propertyValueLabel = new Label(emptyValueString);
		} else {
			propertyValueLabel = new Label(propertyValue.toString());
		}
		propertyValueLabel.setTitle(propertyTitle);
		propertyValueLabel.setStyleName(WizardStyles.WizardItemWidgetPropertyValueLabel);
		editProperty(false);
		getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);

		propertyValueValueBox = createValueBoxBase();
		propertyValueValueBox.setValue(propertyValue);
		propertyValueValueBox.setTitle(propertyTitle);

		// when clicked, show textBox and focus on it
		propertyValueLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				editProperty(true);

				// give focus to textbox
				propertyValueValueBox.setFocus(true);
			}
		});
		// if looses focus:
		// if text is empty, keep previous
		propertyValueValueBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if ("".equals(propertyValueValueBox.getValue())) {
					propertyValueValueBox.setValue(previousNameValue);
					// reset previous name
					previousNameValue = null;
				}
				editProperty(false);
			}
		});

		// if ESCAPE key, just come back to label
		// if ENTER key, come back to label is no text or create sample if text
		propertyValueValueBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					editProperty(false);
				} else if (!(propertyValueValueBox instanceof TextArea)
						&& (event.getNativeKeyCode() == KeyCodes.KEY_ENTER
								|| event.getNativeKeyCode() == KeyCodes.KEY_MAC_ENTER)) {
					editProperty(false);
				} else {
					final V value = propertyValueValueBox.getValue();
					updateItemObjectProperty(itemObject, value);
					if (value != null) {
						propertyValueLabel.setText(value.toString());
					} else {
						propertyValueLabel.setText(null);
					}
					if (previousNameValue == null) {
						previousNameValue = value;
					}
				}
			}
		});

	}

	protected abstract ValueBoxBase<V> createValueBoxBase();

	public ValueBoxBase<V> getValueBoxBase() {
		return propertyValueValueBox;
	}

	/**
	 * To make some changes before setting the item to be edited or not
	 * 
	 * @param edit
	 */
	public abstract void beforeEditProperty(boolean edit);

	public void editProperty(boolean edit) {
		beforeEditProperty(edit);
		if (edit) {
			setWidget(0, 1, propertyValueValueBox);
			if (emptyValueString.equals(propertyValueValueBox.getValue())) {
				propertyValueValueBox.setValue(null);
			}
			// select the value if some
			if (propertyValueValueBox.getValue() != null) {
				propertyValueValueBox.selectAll();
			}
		} else {
			if ("".equals(propertyValueLabel.getText())) {
				propertyValueLabel.setText(emptyValueString);
			}
			setWidget(0, 1, propertyValueLabel);
		}
	}

	public boolean isMandatory() {
		return mandatory;
	}

}
