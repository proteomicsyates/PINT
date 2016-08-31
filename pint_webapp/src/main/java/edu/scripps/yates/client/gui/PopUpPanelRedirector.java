package edu.scripps.yates.client.gui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.history.TargetHistory;

public class PopUpPanelRedirector extends VerticalPanel {

	private final ClickHandler listenerToClose;
	private final PopupPanel popup;

	/**
	 * A {@link PopupPanel} with a button. When closed, the browser will be
	 * redirected to the {@link TargetHistory} token provided if so.
	 *
	 * @param autoHide
	 * @param modal
	 * @param messageText
	 * @param target
	 */
	public PopUpPanelRedirector(boolean autoHide, boolean modal, boolean glassEnabled, String titleText,
			String messageText, final TargetHistory target) {
		this.setStyleName("popUpPanelRedirector");
		popup = new PopupPanel(autoHide, modal);
		popup.setAutoHideOnHistoryEventsEnabled(true);
		popup.setAnimationEnabled(true);
		popup.getElement().getStyle().setProperty("borderRadius", 10, Unit.PX);

		if (titleText != null)
			popup.setTitle(titleText);

		// popup.setStyleName("mypopUpPanel");
		listenerToClose = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
				// if (target != null) {
				// History.newItem(target.getTargetHistory());
				// }
			}
		};

		popup.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if (target != null) {
					History.newItem(target.getTargetHistory());
				}
			}
		});
		if (titleText != null && !"".equals(titleText)) {
			HTML titleHTML = new HTML(new SafeHtmlBuilder().appendEscapedLines(titleText).toSafeHtml());
			titleHTML.setStyleName("popUpPanelRedirectorTitle");
			this.add(titleHTML);
		}
		HTML message = new HTML(new SafeHtmlBuilder().appendEscapedLines(messageText).toSafeHtml());
		message.setStyleName("popUpPanelRedirectorMessage");
		this.add(message);
		Button button = new Button("Go to Browse", listenerToClose);
		button.setTitle("Click here to go to the Browse menu");
		SimplePanel buttonContainer = new SimplePanel(button);
		buttonContainer.setStyleName("popUpPanelRedirectorButtonContainer");
		this.add(buttonContainer);
		popup.setWidget(this);
		popup.setGlassEnabled(glassEnabled);
	}

	public void show() {
		popup.center();
	}

}
