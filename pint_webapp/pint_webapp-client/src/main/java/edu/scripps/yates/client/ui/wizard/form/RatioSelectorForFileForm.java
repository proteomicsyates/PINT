package edu.scripps.yates.client.ui.wizard.form;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.util.Pair;

public class RatioSelectorForFileForm extends AbstractFormCollection {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242527427961501168L;

	public RatioSelectorForFileForm(PintContext context, String question, String explanation) {
		super(context);

		final ListBoxFormInformation listBox = new ListBoxFormInformation(question, explanation, true, true);
		add(listBox);
		listBox.setValues(items);
		listBox.linkToObject(new UpdateAction() {

			@Override
			public void onChange(GwtEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> getTextFormsLinks() {
		return null;
	}

}
