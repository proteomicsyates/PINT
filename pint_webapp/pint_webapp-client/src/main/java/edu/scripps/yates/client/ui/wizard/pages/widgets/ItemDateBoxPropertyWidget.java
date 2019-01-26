package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.Date;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DateBox;

import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public abstract class ItemDateBoxPropertyWidget<T> extends FlexTable implements UpdateProperty<T, Date> {

	private final DateBox propertyDateBox;
	private final boolean mandatory;

	public ItemDateBoxPropertyWidget(String propertyName, String propertyTitle, T itemObject, boolean mandatory) {
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
		final Date propertyValue = getPropertyValueFromItem(itemObject);

		propertyDateBox = new DateBox();
		propertyDateBox.setValue(propertyValue);
		propertyDateBox.setTitle(propertyTitle);

		propertyDateBox.setFormat(new DateBox.DefaultFormat(PintImportCfgUtil.dateFormatter));
		propertyDateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				final Date value = propertyDateBox.getValue();
				updateItemObjectProperty(itemObject, value);
			}
		});
		setWidget(0, 1, propertyDateBox);
		getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
	}

	public boolean isMandatory() {
		return mandatory;
	}

}
