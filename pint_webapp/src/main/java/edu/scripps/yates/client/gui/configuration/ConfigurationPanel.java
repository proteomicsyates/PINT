package edu.scripps.yates.client.gui.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ConfigurationServiceAsync;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.StatusReporter;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.util.CryptoUtil;

public class ConfigurationPanel extends WindowBox implements StatusReporter {
	private final ConfigurationClientManager configurationClientManager;
	private final ConfigurationServiceAsync service = ConfigurationServiceAsync.Util.getInstance();
	private Label statusLabel;
	private Label labelOmimStatus;
	private Label adminPasswordLabel;

	public ConfigurationPanel() {
		super(false, true, true, false, true);
		configurationClientManager = ConfigurationClientManager.getInstance();
		// setStyleName("configurationPanel");
		setText("PINT General Configuration");
		setWidget(createMainWidget());
		setAnimationEnabled(true);
		setGlassEnabled(true);
		resize();
		// check clasibility
		checkClosability();
	}

	private void checkClosability() {
		// if everything is fine, enable the close button
		final boolean closable = !configurationClientManager.isSomeConfigurationMissing();
		setCloseIconVisible(closable);
		if (closable) {
			showMessage("PINT is successfully configured.\nNow you can close this dialog.");
		}
	}

	private Widget createMainWidget() {
		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("verticalComponent");

		InlineLabel label = new InlineLabel("Welcome to the General configuration of PINT");
		label.setStyleName("configurationPanel_welcome");
		mainPanel.add(label);
		InlineLabel label2 = new InlineLabel("First time PINT is loaded, you need to setup some parameters.");
		label2.setStyleName("configurationPanel_welcome2");

		// admin password
		CaptionPanel adminPassCaptionPanel = new CaptionPanel("Administrator password");
		adminPassCaptionPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(adminPassCaptionPanel);
		adminPassCaptionPanel.add(getAdminPassRowPanel());
		// omim key
		CaptionPanel omimKeyCaptionPanel = new CaptionPanel("OMIM key");
		omimKeyCaptionPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(omimKeyCaptionPanel);
		omimKeyCaptionPanel.add(getOMIMKeyRowPanel());
		// status
		CaptionPanel statusPanel = new CaptionPanel("Status");
		statusPanel.setStyleName("configurationPanel_Grid");
		mainPanel.add(statusPanel);
		statusLabel = new Label();
		statusLabel.setWordWrap(true);
		statusPanel.add(statusLabel);

		return mainPanel;
	}

	private Widget getOMIMKeyRowPanel() {
		final Grid grid = new Grid(1, 4);
		grid.setStyleName("configurationPanel_Grid");
		final String omimKey = configurationClientManager.getOmimKey();
		grid.setWidget(0, 0, getValidIcon(omimKey));
		final TextBox textBox = new TextBox();
		if (omimKey != null) {
			textBox.setText(omimKey);
		}
		grid.setWidget(0, 1, textBox);
		String html = "Set";
		if (omimKey != null && !"".equals(omimKey)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(0, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting new OMIM key");
				grid.setWidget(0, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setOMIMKey(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						configurationClientManager.setOmimKey(textBox.getText());
						labelOmimStatus.setText(getStatusText(textBox.getText()));
						grid.setWidget(0, 0, getValidIcon(textBox.getText()));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("OMIM key updated");
					}

					@Override
					public void onFailure(Throwable caught) {
						configurationClientManager.setOmimKey(null);
						labelOmimStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(0, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelOmimStatus = new Label(getStatusText(omimKey));
		grid.setWidget(0, 3, labelOmimStatus);
		return grid;
	}

	private String getStatusText(String item) {
		if (item == null || "".equals(item)) {
			return "not defined";
		} else {
			return "valid";
		}
	}

	private Widget getAdminPassRowPanel() {
		final Grid grid = new Grid(1, 5);
		grid.setStyleName("configurationPanel_Grid");
		final String adminPassword = configurationClientManager.getAdminPassword();
		grid.setWidget(0, 0, getValidIcon(adminPassword));
		final PasswordTextBox adminPasswordTextBox = new PasswordTextBox();
		final TextBox adminTextBox = new TextBox();
		adminTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				adminPasswordTextBox.setText(adminTextBox.getText());

			}
		});
		adminPasswordTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				adminTextBox.setText(adminPasswordTextBox.getText());

			}
		});
		if (adminPassword != null) {
			adminPasswordTextBox.setText(adminPassword);
		}
		if (adminPassword != null) {
			adminTextBox.setText(adminPassword);
		}
		grid.setWidget(0, 1, adminPasswordTextBox);
		String html = "Set";
		if (adminPassword != null && !"".equals(adminPassword)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(0, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting new password");
				grid.setWidget(0, 0, imageLoading);
				button.setEnabled(false);
				adminPasswordTextBox.setEnabled(false);
				final String decryptedAdminPassword = adminPasswordTextBox.getText();
				final String encryptedAdminPassword = CryptoUtil.encrypt(decryptedAdminPassword);
				service.setAdminPassword(encryptedAdminPassword, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						configurationClientManager.setAdminPassword(decryptedAdminPassword);
						adminPasswordLabel.setText(getStatusText(decryptedAdminPassword));
						grid.setWidget(0, 0, getValidIcon(decryptedAdminPassword));
						button.setEnabled(true);
						adminPasswordTextBox.setEnabled(true);
						checkClosability();
						showMessage("Password updated");
					}

					@Override
					public void onFailure(Throwable caught) {
						configurationClientManager.setOmimKey(null);
						adminPasswordLabel.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(0, 0, getValidIcon(false));
						button.setEnabled(true);
						adminPasswordTextBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		adminPasswordLabel = new Label(getStatusText(adminPassword));
		grid.setWidget(0, 3, adminPasswordLabel);

		final CheckBox checkBox = new CheckBox("show password");
		grid.setWidget(0, 4, checkBox);
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Boolean showPassword = checkBox.getValue();
				if (showPassword) {
					grid.setWidget(0, 1, adminTextBox);
				} else {
					grid.setWidget(0, 1, adminPasswordTextBox);
				}
			}
		});
		return grid;
	}

	private Widget getValidIcon(String item) {
		boolean valid = item != null && !"".equals(item);
		return getValidIcon(valid);
	}

	private Widget getValidIcon(boolean valid) {

		if (!valid) {
			return new Image(MyClientBundle.INSTANCE.redCross());
		} else {
			return new Image(MyClientBundle.INSTANCE.greenTick());
		}
	}

	@Override
	public void showMessage(String message) {
		statusLabel.setText(message);
		statusLabel.setStyleName("configurationPanel_Status");

	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		statusLabel.setText(throwable.getMessage());
		statusLabel.setStyleName("configurationPanel_Status_Error");

	}
}
