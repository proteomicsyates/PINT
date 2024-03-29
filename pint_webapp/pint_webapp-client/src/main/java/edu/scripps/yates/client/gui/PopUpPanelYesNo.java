package edu.scripps.yates.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;

public class PopUpPanelYesNo extends VerticalPanel {

	private final PopupPanel popup;
	private final Label statusLabel = new Label();
	private Label titleLabel;
	private Button button1;
	private Button button2;
	private final HTML message;
	private HTML explanation;

	public PopUpPanelYesNo(boolean autoHide, boolean modal, boolean glassEnabled, String title, String messageText) {
		this(autoHide, modal, glassEnabled, title, messageText, "Yes", "No");
	}

	/**
	 * A {@link PopupPanel} with a button. When closed, the browser will be
	 * redirected to the {@link TargetHistory} token provided if so.
	 *
	 * @param autoHide
	 * @param modal
	 * @param messageText
	 * @param target
	 */
	public PopUpPanelYesNo(boolean autoHide, boolean modal, boolean glassEnabled, String title, String messageText,
			String button1Text, String button2Text) {
		this(autoHide, modal, glassEnabled, title, messageText, null, button1Text, button2Text);
	}

	/**
	 * A {@link PopupPanel} with a button. When closed, the browser will be
	 * redirected to the {@link TargetHistory} token provided if so.
	 *
	 * @param autoHide
	 * @param modal
	 * @param messageText
	 * @param explanationText
	 * @param target
	 */
	public PopUpPanelYesNo(boolean autoHide, boolean modal, boolean glassEnabled, String title, String messageText,
			String explanationText, String button1Text, String button2Text) {
		super();
		setHorizontalAlignment(ALIGN_CENTER);
		if (title != null) {
			titleLabel = new Label(title);
			titleLabel.setStyleName("checkLoginPopUp-title");

			this.add(titleLabel);
		}
		popup = new PopupPanel(autoHide, modal);
		popup.setAutoHideOnHistoryEventsEnabled(true);
		popup.setAnimationEnabled(true);
		popup.getElement().getStyle().setProperty("borderRadius", 10, Unit.PX);
		if (explanationText != null && !"".equals(explanationText)) {
			explanation = new HTML(new SafeHtmlBuilder().appendEscapedLines(explanationText).toSafeHtml());
			explanation.setStyleName(WizardStyles.WizardInfoMessage);
			this.add(explanation);
			explanation.getElement().getStyle().setPaddingBottom(10, Unit.PX);
		}

		message = new HTML(new SafeHtmlBuilder().appendEscapedLines(messageText).toSafeHtml());
		message.setStyleName("checkLoginPopUp-message");
		this.add(message);

		final Grid holder = new Grid(1, 2);
		holder.setStyleName("checkLoginPopUp-grid");
		if (button1Text != null) {
			button1 = new Button(button1Text);
			if (button1Text != null) {
				holder.setWidget(0, 0, button1);
			}
		}
		if (button2Text != null) {
			button2 = new Button(button2Text);
			if (button2Text != null) {
				holder.setWidget(0, 1, button2);
			}
		}
		this.add(holder);
		this.add(statusLabel);
		statusLabel.setStyleName("checkLoginPopUp-status");
		popup.setWidget(this);
		popup.setGlassEnabled(glassEnabled);
	}

	public void alignMessage(HorizontalAlignmentConstant align) {
		message.setHorizontalAlignment(align);
	}

	public void addButton1ClickHandler(ClickHandler clickHandler) {
		if (button1 != null) {
			button1.addClickHandler(clickHandler);
		}
	}

	public void addButton2ClickHandler(ClickHandler clickHandler) {
		if (button2 != null) {
			button2.addClickHandler(clickHandler);
		}
	}

	protected void hideAfterTime(int milliseconds) {
		final Timer timer = new Timer() {

			@Override
			public void run() {
				hide();

			}
		};
		timer.schedule(milliseconds);
	}

	public void reset() {
		statusLabel.setText("");
	}

	/**
	 * Adds {@link CloseHandler} to do something when closing the panel
	 *
	 * @param closeHandler
	 */
	public void addCloseHandler(CloseHandler<PopupPanel> closeHandler) {
		popup.addCloseHandler(closeHandler);
	}

	public void show() {
		popup.center();
	}

	public void hide() {
		popup.hide();
	}

}
