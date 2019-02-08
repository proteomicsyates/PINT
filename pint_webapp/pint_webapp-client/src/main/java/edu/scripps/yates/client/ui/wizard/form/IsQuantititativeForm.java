package edu.scripps.yates.client.ui.wizard.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.util.Pair;

public class IsQuantititativeForm extends AbstractFormCollection {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7348951751362361744L;
	private static final String explanation = "A dataset is quantitative when it contains some type of quantitative values, "
			+ "such as relative ratios comparing different experimental conditions or abundance measurements such as intensities.";
	private static final String YES = "yes";
	private static final String NO = "no";
	private static final String EMPTY = "";

	public IsQuantititativeForm(final PintContext context) {
		super(context);

		final boolean multipleSelect = false;
		final ListBoxFormInformation listBoxInfo = new ListBoxFormInformation("Is the dataset quantitative?",
				explanation, true, multipleSelect);
		add(listBoxInfo);
		final List<String> items = new ArrayList<String>();
		items.add(EMPTY);
		items.add(YES);
		items.add(NO);
		listBoxInfo.setValues(items);
		listBoxInfo.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				final String valueFromWidget = listBoxInfo.getValueFromWidget();
				Boolean quantitative = null;
				if (quantitative != null) {
					if (valueFromWidget.equals(YES)) {
						quantitative = true;
					} else {
						quantitative = false;
					}
				}
//				context.setQuantitative(quantitative);
			}
		});
		// update but not firing event
//		final String value = getValueFromBoolean(context.isQuantitative());
//		listBoxInfo.getListBox().setItemSelected(items.indexOf(value), false);

	}

	private String getValueFromBoolean(Boolean quantitative) {
		if (quantitative == null) {
			return EMPTY;
		} else if (!quantitative) {
			return NO;
		} else {
			return YES;
		}
	}

	@Override
	public List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> getTextFormsLinks() {

		return Collections.emptyList();
	}
}
