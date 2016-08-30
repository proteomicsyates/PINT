package edu.scripps.yates.client.gui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.client.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.shared.util.CryptoUtil;

public class PopUpPanelPasswordChecker extends VerticalPanel {

	private final ClickHandler listenerToButton;
	private final PopupPanel popup;
	private final edu.scripps.yates.client.ImportWizardServiceAsync service = GWT.create(ImportWizardService.class);
	private final PasswordTextBox passwordTextBox;
	private final Label statusLabel = new Label();
	private boolean loginOK = false;
	private Label titleLabel;

	/**
	 * A {@link PopupPanel} with a button. When closed, the browser will be
	 * redirected to the {@link TargetHistory} token provided if so.
	 *
	 * @param autoHide
	 * @param modal
	 * @param messageText
	 * @param target
	 */
	public PopUpPanelPasswordChecker(boolean autoHide, boolean modal, String title, String messageText) {
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

		// popup.setStyleName("mypopUpPanel");
		listenerToButton = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				checkLogin();

			}
		};

		// popup.addCloseHandler(new CloseHandler<PopupPanel>() {
		//
		// @Override
		// public void onClose(CloseEvent<PopupPanel> event) {
		//
		// }
		// });

		Button button = new Button("login", listenerToButton);
		Grid holder = new Grid(1, 2);
		holder.setStyleName("checkLoginPopUp-grid");
		holder.setWidget(0, 1, button);

		HTML message = new HTML(new SafeHtmlBuilder().appendEscapedLines(messageText).toSafeHtml());
		message.setStyleName("checkLoginPopUp-message");
		this.add(message);
		passwordTextBox = new PasswordTextBox();
		passwordTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				reset();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					checkLogin();
				} else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
					hide();
				}
			}
		});
		holder.setWidget(0, 0, passwordTextBox);
		this.add(holder);
		this.add(statusLabel);
		statusLabel.setStyleName("checkLoginPopUp-status");
		popup.setWidget(this);
		popup.setGlassEnabled(false);
	}

	protected void checkLogin() {
		statusLabel.setText("Checking login...");
		String encryptedPassword = CryptoUtil.encrypt(passwordTextBox.getValue());
		service.checkUserLogin(null, encryptedPassword, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				loginOk();
				hideAfterTime(1000);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof PintException) {
					final PintException pintException = (PintException) caught;
					if (pintException.getPintErrorType() == PINT_ERROR_TYPE.LOGIN_FAILED) {
						loginFailed();
						return;
					}
				}
				statusLabel.setText(caught.getMessage());
			}
		});
	}

	protected void hideAfterTime(int milliseconds) {
		Timer timer = new Timer() {

			@Override
			public void run() {
				hide();

			}
		};
		timer.schedule(milliseconds);
	}

	private void reset() {
		loginOK = false;
		statusLabel.setText("");
	}

	/**
	 * @return the loginOK
	 */
	public boolean isLoginOK() {
		return loginOK;
	}

	/**
	 * Adds {@link CloseHandler} to do something when closing the panel
	 *
	 * @param closeHandler
	 */
	public void addCloseHandler(CloseHandler<PopupPanel> closeHandler) {
		popup.addCloseHandler(closeHandler);
	}

	private void loginOk() {
		statusLabel.setText("Login OK");
		loginOK = true;
	}

	private void loginFailed() {
		statusLabel.setText("Login failed");
		loginOK = false;
	}

	public void show() {
		popup.center();
	}

	public void hide() {
		popup.hide();
	}

}
