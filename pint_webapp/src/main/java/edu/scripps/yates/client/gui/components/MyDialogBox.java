package edu.scripps.yates.client.gui.components;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.scripps.yates.client.gui.templates.MyClientBundle;

public class MyDialogBox extends DialogBox {
	private final InlineHTML inlineHTML;
	private Image loadingBar;
	private final VerticalPanel panel;

	/**
	 * This constructor can also specify whether to show the loader bar or not
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @param showLoaderBar
	 * @return
	 */
	public static MyDialogBox getInstance(String text, boolean autoHide, boolean modal, boolean showLoaderBar) {
		MyDialogBox instance = new MyDialogBox(text, autoHide, modal, showLoaderBar);
		return instance;
	}

	/**
	 * By default is showing the loader bar
	 *
	 * @param text
	 * @param autoHide
	 * @param modal
	 * @return
	 */
	public static MyDialogBox getInstance(String text, boolean autoHide, boolean modal) {
		MyDialogBox instance = new MyDialogBox(text, autoHide, modal, true);
		return instance;
	}

	private MyDialogBox(String text, boolean autoHide, boolean modal, boolean showLoaderBar) {

		super(autoHide, modal);

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
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		inlineHTML = new InlineHTML(text);
		inlineHTML.setWidth("100%");
		panel.add(inlineHTML);

		if (showLoaderBar) {
			loadingBar = new Image(myClientBundle.horizontalLoader());
			panel.add(loadingBar);
		}
		// panel.add(ok);
		//
		setWidget(panel);
	}

	@Override
	public void setText(String text) {
		inlineHTML.setHTML(new SafeHtmlBuilder().appendEscapedLines(text).toSafeHtml());
	}

	public void setShowLoadingBar(boolean showLoadingBar) {
		loadingBar.removeFromParent();
		if (showLoadingBar) {
			panel.add(loadingBar);
		}
	}

}
