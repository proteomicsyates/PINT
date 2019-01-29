package edu.scripps.yates.client.ui.wizard.pages.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.ui.wizard.view.widget.WizardButton;

public class WizardQuestionPanel extends FlexTable {
	private final String question;
	private final String explanation;
	private final String buttonWidth = "100px";
	private final String buttonHeight = "40px";
	private WizardButton yesButton;
	private WizardButton noButton;
	private WizardButton okButton;

	public WizardQuestionPanel(String question, String explanation) {
		this(question, explanation, false);
	}

	public WizardQuestionPanel(String question, String explanation, boolean justOKButton) {

		this.question = question;
		this.explanation = explanation;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init(justOKButton);

	}

	private void init(boolean justOKButton) {
		final Label questionLabel = new Label(question);
		questionLabel.setStyleName(WizardStyles.WizardQuestionLabel);
		setWidget(0, 0, questionLabel);

		final HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setWidth("100%");
		buttonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		if (!justOKButton) {
			final HTML leftSpacer = new HTML("&nbsp;");
			leftSpacer.setWidth("100%");
			buttonsPanel.add(leftSpacer);
			buttonsPanel.setCellWidth(leftSpacer, "45%");
			yesButton = new WizardButton("Yes");
			buttonsPanel.add(yesButton);
			final HTML spacer = new HTML("&nbsp;");
			buttonsPanel.add(spacer);
			buttonsPanel.setCellWidth(spacer, "10%");
			noButton = new WizardButton("No");
			buttonsPanel.add(noButton);
			final HTML rightSpacer = new HTML("&nbsp;");
			rightSpacer.setWidth("100%");
			buttonsPanel.add(rightSpacer);
			buttonsPanel.setCellWidth(rightSpacer, "45%");
			// Buttons sizes
			yesButton.setSize(buttonWidth, buttonHeight);
			noButton.setSize(buttonWidth, buttonHeight);
		} else {
			okButton = new WizardButton("Ok");
			buttonsPanel.add(okButton);
			// Buttons sizes
			okButton.setSize(buttonWidth, buttonHeight);
		}
		setWidget(1, 0, buttonsPanel);

		final Label explanationLabel = new Label(explanation);
		explanationLabel.setStyleName(WizardStyles.WizardExplanationLabel);
		explanationLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		explanationLabel.getElement().getStyle().setPaddingTop(30, Unit.PX);
		explanationLabel.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		setWidget(2, 0, explanationLabel);
		getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

	}

	public void addNoClickHandler(ClickHandler clickHandler) {
		if (noButton != null) {
			noButton.addClickHandler(clickHandler);
		}
	}

	public void addYesClickHandler(ClickHandler clickHandler) {
		if (yesButton != null) {
			yesButton.addClickHandler(clickHandler);
		}
	}

	public void addOKClickHandler(ClickHandler clickHandler) {
		if (okButton != null) {
			okButton.addClickHandler(clickHandler);
		}
	}
}
