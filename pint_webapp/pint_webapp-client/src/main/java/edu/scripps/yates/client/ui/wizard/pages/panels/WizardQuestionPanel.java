package edu.scripps.yates.client.ui.wizard.pages.panels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

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
	private int row;
	private final String questionStyleName;
	private final String explanationStyleName;
	private HorizontalPanel buttonsPanel;

	public enum WizardQuestionPanelButtons {
		YES_NO, OK, NONE
	};

	/**
	 * By default is with a yes/no buttom
	 * 
	 * @param question
	 * @param explanation
	 */
	public WizardQuestionPanel(String question, String explanation) {
		this(question, explanation, WizardQuestionPanelButtons.YES_NO);
	}

	/**
	 * Default {@link WizardQuestionPanel} with styles:
	 * WizardStyles.WizardQuestionLabel and WizardStyles.WizardExplanationLabel for
	 * the question and the explanation, respectively.
	 * 
	 * @param question
	 * @param explanation
	 * @param buttons
	 */
	public WizardQuestionPanel(String question, String explanation, WizardQuestionPanelButtons buttons) {
		this(question, WizardStyles.WizardQuestionLabel, explanation, WizardStyles.WizardExplanationLabel, buttons);
	}

	public WizardQuestionPanel(String question, String questionStyleName, String explanation,
			String explanationStyleName, WizardQuestionPanelButtons buttons) {

		this.question = question;
		this.questionStyleName = questionStyleName;
		this.explanation = explanation;
		this.explanationStyleName = explanationStyleName;
		setStyleName(WizardStyles.WizardQuestionPanel);
		init(buttons);

	}

	private void init(WizardQuestionPanelButtons buttons) {
		row = 0;
		final Label questionLabel = new Label(question);
		questionLabel.setStyleName(questionStyleName);
		setWidget(row, 0, questionLabel);
		//
		row++;
		final Label explanationLabel = new Label(explanation);
		explanationLabel.setStyleName(explanationStyleName);
		explanationLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		explanationLabel.getElement().getStyle().setPaddingTop(30, Unit.PX);
		explanationLabel.getElement().getStyle().setPaddingBottom(20, Unit.PX);
		setWidget(row, 0, explanationLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_CENTER);
		//
		if (buttons != WizardQuestionPanelButtons.NONE) {
			row++;
			buttonsPanel = new HorizontalPanel();
			buttonsPanel.setWidth("100%");
			buttonsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			if (buttons == WizardQuestionPanelButtons.YES_NO) {
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
			} else if (buttons == WizardQuestionPanelButtons.OK) {
				okButton = new WizardButton("Ok");
				buttonsPanel.add(okButton);
				// Buttons sizes
				okButton.setSize(buttonWidth, buttonHeight);
			}
			setWidget(row, 0, buttonsPanel);
		}
	}

	public void setOKButtonStyleName(String okButtonStyleName, String width, String height) {
		if (okButton != null) {
			this.okButton.setStyleName(okButtonStyleName);
			// set size
			this.okButton.setWidth(width);
			this.okButton.setHeight(height);
		}
	}

	public void setNOButtonStyleName(String noButtonStyleName) {
		if (noButton != null) {
			this.noButton.setStyleName(noButtonStyleName);
		}
	}

	public void setYESButtonStyleName(String yesButtonStyleName) {
		if (yesButton != null) {
			this.yesButton.setStyleName(yesButtonStyleName);
		}
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

	public void addBottomWidget(Widget widget, HorizontalAlignmentConstant horizontalAligment) {
		row++;
		setWidget(row, 0, widget);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, horizontalAligment);
	}

	public WizardButton getOKButton() {
		return this.okButton;
	}
}
