package edu.scripps.yates.client.gui.components;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.templates.MyClientBundle;

public class MyDialogBox extends DialogBox {
	private final InlineHTML textLabel;
	private final Image loadingBar;
	private final VerticalPanel panel;
	private Timer timerTaskOnCloseDialog;
	private Button button;
	private HandlerRegistration buttonClickHandlerRegistry;

	/**
	 * By default is showing the loader bar
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal) {
		this(text, autoHide, modal, true, null);
	}

	/**
	 * By default is showing the loader bar
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, Timer timerTaskOnCloseDialog) {
		this(text, autoHide, modal, true, timerTaskOnCloseDialog, null);
	}

	/**
	 * This constructor can also specify whether to show the loader bar or not
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @param showLoaderBar
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, boolean showLoaderBar) {
		this(text, autoHide, modal, showLoaderBar, null);
	}

	/**
	 * By default is showing the loader bar
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, String textButton) {
		this(text, autoHide, modal, true, null, textButton);
	}

	/**
	 * By default is showing the loader bar
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, Timer timerTaskOnCloseDialog, String textButton) {
		this(text, autoHide, modal, true, timerTaskOnCloseDialog, textButton);
	}

	/**
	 * This constructor can also specify whether to show the loader bar or not
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @param showLoaderBar
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, boolean showLoaderBar, String textButton) {
		this(text, autoHide, modal, showLoaderBar, null, textButton);
	}

	/**
	 * This constructor can also specify whether to show the loader bar or not
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @param showLoaderBar
	 * @return
	 */
	public MyDialogBox(String text, boolean autoHide, boolean modal, boolean showLoaderBar,
			Timer timerTaskOnCloseDialog, String buttonText) {

		super(autoHide, modal);
		this.timerTaskOnCloseDialog = timerTaskOnCloseDialog;
		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;

		// Set the dialog box's caption.
		// setText(text);

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		// DialogBox is a SimplePanel, so you have to set its widget
		// property to whatever you want its contents to be.
		// Button ok = new Button("OK");
		// ok.addClickHandler(new ClickHandler() {
		// public void onClick(ClickEvent event) {
		// MyDialog.this.hide();
		// }
		// });

		panel = new VerticalPanel();
		panel.setSize("100%", "100%");
		panel.setStyleName("DialogBox-content");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		textLabel = new InlineHTML(new SafeHtmlBuilder().appendEscaped(text).toSafeHtml());
		textLabel.setStyleName("DialogBox-textlabel");
		textLabel.setWidth("100%");
		panel.add(textLabel);
		if (buttonText != null && !"".equals(buttonText)) {
			button = new Button(buttonText);
			panel.add(button);
		} else {
			button = new Button("not defined");
			button.setVisible(false);
		}
		button.setStyleName("Dialogbox-button");
		panel.add(button);
		loadingBar = new Image(myClientBundle.horizontalLoader());
		loadingBar.setStyleName("Dialogbox-loadingBar");
		if (showLoaderBar) {
			panel.add(loadingBar);
		}
		// panel.add(ok);
		//
		setWidget(panel);
	}

	public void addClickHandler(ClickHandler handler) {
		if (button == null) {
			throw new IllegalArgumentException("Button is null. Create this object with a name for the button");
		}
		if (handler != null) {
			// remove first previous handlers
			if (buttonClickHandlerRegistry != null) {
				buttonClickHandlerRegistry.removeHandler();
			}
			buttonClickHandlerRegistry = button.addClickHandler(handler);
		}
	}

	@Override
	public void setText(String text) {
		textLabel.setHTML(new SafeHtmlBuilder().appendEscapedLines(text).toSafeHtml());
	}

	public void setShowLoadingBar(boolean showLoadingBar) {

		loadingBar.removeFromParent();
		if (showLoadingBar) {
			panel.add(loadingBar);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.DialogBox#hide(boolean)
	 */
	@Override
	public void hide(boolean autoClosed) {
		if (timerTaskOnCloseDialog != null) {
			timerTaskOnCloseDialog.schedule(1000);
		}
		super.hide(autoClosed);
	}

	/**
	 * @param timerTaskOnCloseDialog
	 *            the timerTaskOnCloseDialog to set
	 */
	public void setTimerTaskOnCloseDialog(Timer timerTaskOnCloseDialog) {
		this.timerTaskOnCloseDialog = timerTaskOnCloseDialog;
	}

	public void setButtonText(String buttonText) {
		if (buttonText != null && !"".equals(buttonText)) {
			this.button.setText(buttonText);
			this.button.setVisible(true);
		} else {
			this.button.setVisible(false);
		}
	}

}
