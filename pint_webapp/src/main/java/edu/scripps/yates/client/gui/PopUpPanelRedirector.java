package edu.scripps.yates.client.gui;

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
	public PopUpPanelRedirector(boolean autoHide, boolean modal, String title, String messageText,
			final TargetHistory target) {

		popup = new PopupPanel(autoHide, modal);
		popup.setAutoHideOnHistoryEventsEnabled(true);
		popup.setAnimationEnabled(true);
		if (title != null)
			popup.setTitle(title);

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

		Button button = new Button("Close", listenerToClose);
		SimplePanel holder = new SimplePanel();
		holder.add(button);
		holder.setStyleName("mypopUpPanel-footer");

		HTML message = new HTML(new SafeHtmlBuilder().appendEscapedLines(messageText).toSafeHtml());
		this.add(message);
		this.add(holder);
		popup.setWidget(this);
		popup.setGlassEnabled(false);
	}

	public void show() {
		popup.center();
	}

}
