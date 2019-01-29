package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.ui.wizard.Wizard;
import edu.scripps.yates.client.ui.wizard.Wizard.ButtonType;
import edu.scripps.yates.client.ui.wizard.form.AbstractFormCollection;
import edu.scripps.yates.client.ui.wizard.form.AbstractFormInformation;
import edu.scripps.yates.client.ui.wizard.form.AbstractTextBasedFormInformation;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.util.Pair;

public class WizardFormPanel extends FlexTable {

	private static final String MANDATORY_TITLE = "This field is mandatory";
	private final List<AbstractFormInformation> formInformations = new ArrayList<AbstractFormInformation>();
	private final List<FocusWidget> textBoxes = new ArrayList<FocusWidget>();
	private final Map<TextBoxBase, Boolean> containsTextByUserMap = new HashMap<TextBoxBase, Boolean>();
	private final Wizard<PintContext> wizard;

	public WizardFormPanel(AbstractFormCollection textFormInformations, Wizard<PintContext> wizard) {
		this.wizard = wizard;
		formInformations.addAll(textFormInformations);
		setStyleName(WizardStyles.WizardTextFormPanel);

		init();
		final List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> pairs = textFormInformations
				.getTextFormsLinks();
		for (final Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation> pair : pairs) {
			setTextLink(pair.getFirstElement(), pair.getSecondElement());
		}
	}

	private void init() {
		setCellSpacing(5);
		int row = 0;
		for (final AbstractFormInformation formInformation : formInformations) {
			final Label questionLabel = formInformation.getQuestionLabel();
			final String explanation = formInformation.getExplanation();
			final boolean mandatory = formInformation.isMandatory();

			// mandatory asterisk
			if (mandatory) {
				final Label mandatoryLabel = new Label("*");
				mandatoryLabel.setStyleName(WizardStyles.WizardFormTextLabel);
				mandatoryLabel.getElement().getStyle().setFontWeight(FontWeight.BOLDER);
				mandatoryLabel.setTitle(MANDATORY_TITLE);
				setWidget(row, 0, mandatoryLabel);
				mandatoryLabel.getElement().getStyle().setPaddingLeft(10, Unit.PX);
			}

			// question
			questionLabel.setStyleName(WizardStyles.WizardFormTextLabel);
			questionLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
			if (mandatory) {
				setWidget(row, 1, questionLabel);
			} else {
				setWidget(row, 0, questionLabel);
				getFlexCellFormatter().setColSpan(row, 0, 2);
			}
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_TOP);
			// explanation
			final Label explanationLabel = new Label(explanation);
			explanationLabel.setStyleName(WizardStyles.WizardExplanationLabel);
			setWidget(row + 1, 2, explanationLabel);
			getFlexCellFormatter().setHorizontalAlignment(row + 1, 2, HasHorizontalAlignment.ALIGN_RIGHT);
			getFlexCellFormatter().setVerticalAlignment(row + 1, 2, HasVerticalAlignment.ALIGN_TOP);
			// text form
			final FocusWidget formWidget = formInformation.getFormElement();
			formWidget.setStyleName(WizardStyles.WizardTextBox);
			formWidget.setTitle(questionLabel.getText());
			textBoxes.add(formWidget);
			if (mandatory) {
				setWidget(row, 2, formWidget);
			} else {
				setWidget(row, 1, formWidget);
			}
			getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
			getFlexCellFormatter().setVerticalAlignment(row, 2, HasVerticalAlignment.ALIGN_TOP);
			getFlexCellFormatter().setWidth(row, 2, "100%");
			formWidget.getElement().getStyle().setPaddingLeft(10, Unit.PX);

			// if it is mandatory, check if it is ready and consequently activate NEXT
			// button
			if (formInformation.isMandatory()) {
				GWT.log("The widget " + formWidget.getClass().getName() + " has change handlers: "
						+ (formWidget instanceof HasChangeHandlers));
				// for textboxes
				if (formWidget instanceof HasChangeHandlers) {
					((HasChangeHandlers) formWidget).addChangeHandler(new ChangeHandler() {
						@Override
						public void onChange(ChangeEvent event) {
							final boolean ready = isReady();
							wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, ready);
						}
					});
				}
				// for check boxes
				if (formWidget instanceof HasValueChangeHandlers) {
					((HasValueChangeHandlers) formWidget).addValueChangeHandler(new ValueChangeHandler() {
						@Override
						public void onValueChange(ValueChangeEvent event) {
							final boolean ready = isReady();
							wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, ready);
						}
					});
				}
				// for any widget
				formWidget.addKeyUpHandler(new KeyUpHandler() {
					@Override
					public void onKeyUp(KeyUpEvent event) {
						final boolean ready = isReady();
						wizard.setButtonEnabled(ButtonType.BUTTON_NEXT, ready);
					}
				});
			}
			if (explanation != null) {
				row += 2;
			} else {
				row++;
			}
		}
	}

	public void setTextLink(AbstractTextBasedFormInformation base, AbstractTextBasedFormInformation dependent) {
		final TextBoxBase baseTextBox = base.getTextBox();
		final TextBoxBase dependentTextBox = dependent.getTextBox();
		if (baseTextBox != null && dependentTextBox != null) {
			// anytime a key is pressed in the dependent, we record whether it contains text
			// or not by the user
			dependentTextBox.addKeyUpHandler(new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					if (!"".equals(dependentTextBox.getValue())) {
						containsTextByUserMap.put(dependentTextBox, true);
						dependentTextBox.setStyleName(WizardStyles.WizardTextBox);
					} else {
						containsTextByUserMap.put(dependentTextBox, false);
						dependentTextBox.setStyleName(WizardStyles.WizardTextBoxGrey);
						dependentTextBox.setValue(baseTextBox.getValue(), true);
					}
				}
			});
			// if dependentTextBox get the focus (by clicking on it or by tab), if there is
			// no text by user, this will be erased
			dependentTextBox.addFocusHandler(new FocusHandler() {

				@Override
				public void onFocus(FocusEvent event) {
					final Boolean containsTextByUser = containsTextByUserMap.get(dependentTextBox);
					if (containsTextByUser == null || !containsTextByUser) {
						dependentTextBox.setValue(null, true);
						dependentTextBox.setStyleName(WizardStyles.WizardTextBoxGrey);
					} else {
						dependentTextBox.setStyleName(WizardStyles.WizardTextBox);
					}

				}
			});
			baseTextBox.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					final Boolean containsTextByUser = containsTextByUserMap.get(dependentTextBox);
					if (containsTextByUser == null || !containsTextByUser) {
						if (!"".equals(baseTextBox.getValue())) {
							dependentTextBox.setValue(baseTextBox.getText(), true);
							dependentTextBox.setStyleName(WizardStyles.WizardTextBoxGrey);
						}
					}

				}
			});
		}

	}

	public boolean isReady() {
		boolean ret = true;
		for (final AbstractFormInformation textFormInformation : formInformations) {
			if (textFormInformation.isMandatory()) {
				final Object response = textFormInformation.getValueFromWidget();
				if (response == null || "".equals(response)) {
					ret = false;
					// change style
					textFormInformation.getQuestionLabel().setStyleName(WizardStyles.WizardFormTextLabelError);
				} else {
					textFormInformation.getQuestionLabel().setStyleName(WizardStyles.WizardFormTextLabel);
				}
			}
		}
		return ret;
	}
}
