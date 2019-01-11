package edu.scripps.yates.client.util.forms;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;

public abstract class AbstractFormInformation<T> {
	private final Label questionLabel;
	private final String explanation;
	private final boolean mandatory;
	private final Double sizeInEM;
	protected final FocusWidget formElement;

	public AbstractFormInformation(String question, String explanation) {
		this(question, explanation, false);
	}

	public AbstractFormInformation(String question, String explanation, boolean mandatory) {
		this(question, explanation, mandatory, null);
	}

	public AbstractFormInformation(String question, String explanation, boolean mandatory, Double sizeInEM) {
		this.questionLabel = new Label(question);
		this.explanation = explanation;
		this.mandatory = mandatory;

		this.sizeInEM = sizeInEM;
		formElement = createFormWidget();

		if (sizeInEM != null && formElement instanceof UIObject) {
			((UIObject) formElement).setWidth(sizeInEM + Unit.EM.toString());
		}
	}

	public abstract FocusWidget createFormWidget();

	public Label getQuestionLabel() {
		return questionLabel;
	}

	public String getExplanation() {
		return explanation;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public Double getSizeInEM() {
		return sizeInEM;
	}

	public FocusWidget getFormElement() {
		return this.formElement;
	}

	public void linkToObject(UpdateAction updateAction) {
		if (formElement instanceof HasChangeHandlers) {
			((HasChangeHandlers) formElement).addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					updateAction.onChange(event);
				}
			});
		}
		if (formElement instanceof HasValueChangeHandlers) {
			((HasValueChangeHandlers) formElement).addValueChangeHandler(new ValueChangeHandler() {

				@Override
				public void onValueChange(ValueChangeEvent event) {
					updateAction.onChange(event);

				}
			});
		}

	}

	public abstract T getValueFromWidget();
}
