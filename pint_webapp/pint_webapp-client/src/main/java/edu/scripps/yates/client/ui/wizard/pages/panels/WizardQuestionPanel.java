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

	public WizardQuestionPanel(String question, String explanation) {

		this.question = question;
		this.explanation = explanation;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init();

	}

	private void init() {
		final Label questionLabel = new Label(question);
		questionLabel.setStyleName(WizardStyles.WizardQuestionLabel);
		setWidget(0, 0, questionLabel);

		final HorizontalPanel yesNoPanel = new HorizontalPanel();
		yesNoPanel.setWidth("100%");
		yesNoPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		final HTML leftSpacer = new HTML("&nbsp;");
		leftSpacer.setWidth("100%");
		yesNoPanel.add(leftSpacer);
		yesNoPanel.setCellWidth(leftSpacer, "45%");
		yesButton = new WizardButton("Yes");
		yesNoPanel.add(yesButton);
		final HTML spacer = new HTML("&nbsp;");
		yesNoPanel.add(spacer);
		yesNoPanel.setCellWidth(spacer, "10%");
		noButton = new WizardButton("No");
		yesNoPanel.add(noButton);
		final HTML rightSpacer = new HTML("&nbsp;");
		rightSpacer.setWidth("100%");
		yesNoPanel.add(rightSpacer);
		yesNoPanel.setCellWidth(rightSpacer, "45%");
		setWidget(1, 0, yesNoPanel);

		final Label explanationLabel = new Label(explanation);
		explanationLabel.setStyleName(WizardStyles.WizardExplanationLabel);
		explanationLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		explanationLabel.getElement().getStyle().setPaddingTop(30, Unit.PX);
		explanationLabel.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		setWidget(2, 0, explanationLabel);

		// Buttons sizes
		yesButton.setSize(buttonWidth, buttonHeight);
		noButton.setSize(buttonWidth, buttonHeight);
		// Buttons horizontal
		getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);

	}

	public void addNoClickHandler(ClickHandler clickHandler) {
		noButton.addClickHandler(clickHandler);
	}

	public void addYesClickHandler(ClickHandler clickHandler) {
		yesButton.addClickHandler(clickHandler);

	}
}
