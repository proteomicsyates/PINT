package edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

public class ServerDisclosurePanel extends ClosableWithTitlePanel {
	private ServerTypeBean server = new ServerTypeBean();
	private final TextBox hostName;
	private final TextBox userName;
	private final PasswordTextBox password;
	private final Image roundLoader;
	private static int numServers = 1;
	private final edu.scripps.yates.client.ImportWizardServiceAsync service = ImportWizardServiceAsync.Util
			.getInstance();
	private final Label errorLabel;
	private final Image greenTick;
	private KeyUpHandler hideGreenTickHandler;
	private KeyUpHandler returnHandler;

	public ServerDisclosurePanel(String sessionID, int importJobID) {
		super(sessionID, importJobID, "Remote location " + numServers++, true);
		Label hostNameLabel = new Label("Host name:");
		addWidget(hostNameLabel);
		hostName = new TextBox();
		hostName.setWidth("25" + Unit.EM.getType());
		hostName.addKeyUpHandler(getHideGreenTickHandler());
		hostName.addKeyUpHandler(getReturnHandler());
		addWidget(hostName);
		Label userNameLabel = new Label("User name:");
		addWidget(userNameLabel);
		userName = new TextBox();
		userName.addKeyUpHandler(getHideGreenTickHandler());
		userName.addKeyUpHandler(getReturnHandler());
		addWidget(userName);
		Label passwordLabel = new Label("Password:");
		addWidget(passwordLabel);
		password = new PasswordTextBox();
		password.addKeyUpHandler(getHideGreenTickHandler());
		password.addKeyUpHandler(getReturnHandler());
		addWidget(password);

		HorizontalPanel checkPanel = new HorizontalPanel();
		checkPanel.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		Button checkAccessButton = new Button("Check access");
		checkAccessButton.setStyleName("horizontalComponent");
		checkPanel.add(checkAccessButton);
		final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
		roundLoader = new Image(myClientBundle.roundedLoader());
		roundLoader.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		roundLoader.setVisible(false);

		addWidget(checkPanel);
		greenTick = new Image(myClientBundle.greenTick());
		greenTick.setStyleName("ServerDisclosurePanelErrorFlowPanel");
		greenTick.setVisible(false);
		checkPanel.add(greenTick);
		greenTick.setSize("20px", "20px");

		checkPanel.add(roundLoader);
		roundLoader.setSize("20px", "20px");

		errorLabel = new Label();
		errorLabel.setStyleName("ServerDisclosurePanelErrorLabel");
		errorLabel.setVisible(false);
		checkPanel.add(errorLabel);

		checkPanel.setCellVerticalAlignment(checkAccessButton, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(greenTick, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(errorLabel, HasVerticalAlignment.ALIGN_MIDDLE);
		checkPanel.setCellVerticalAlignment(roundLoader, HasVerticalAlignment.ALIGN_MIDDLE);

		// add click handler to check the access
		checkAccessButton.addClickHandler(getCheckAccessClickHandler());

		updateRepresentedObject();

		fireModificationEvent();
	}

	/**
	 * {@link KeyUpHandler} that hides the greenTick image and also hides the
	 * error message
	 *
	 * @return
	 */
	private KeyUpHandler getHideGreenTickHandler() {
		if (hideGreenTickHandler == null) {
			hideGreenTickHandler = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					greenTick.setVisible(false);
					hideErrorMessage();
				}
			};
		}
		return hideGreenTickHandler;
	}

	/**
	 * {@link KeyUpHandler} that hides the greenTick image and also hides the
	 * error message
	 *
	 * @return
	 */
	private KeyUpHandler getReturnHandler() {
		if (returnHandler == null) {
			returnHandler = new KeyUpHandler() {
				@Override
				public void onKeyUp(KeyUpEvent event) {
					fireModificationEvent();
					if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						checkLogin();
					}
				}
			};
		}
		return returnHandler;
	}

	private void showErrorMessage(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
	}

	private void hideErrorMessage() {
		errorLabel.setText("");
		errorLabel.setVisible(false);
	}

	private ClickHandler getCheckAccessClickHandler() {
		ClickHandler ret = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkLogin();

			}
		};
		return ret;
	}

	protected void checkLogin() {
		roundLoader.setVisible(true);
		greenTick.setVisible(false);
		hideErrorMessage();
		updateRepresentedObject();
		service.checkServerAccessibility(sessionID, server, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				roundLoader.setVisible(false);
				greenTick.setVisible(true);
				fireModificationEvent();
			}

			@Override
			public void onFailure(Throwable caught) {
				roundLoader.setVisible(false);
				greenTick.setVisible(false);
				showErrorMessage(caught.getMessage());
			}
		});

	}

	@Override
	public ServerTypeBean getObject() {
		return server;
	}

	@Override
	public void updateRepresentedObject() {
		server.setId(getID());
		if (!"".equals(hostName.getText())) {
			server.setHostName(hostName.getText());
		} else {
			server.setHostName(null);
		}
		final String passwordText = password.getValue();
		if (!"".equals(passwordText)) {
			server.setPassword(passwordText);
		} else {
			server.setPassword(null);
		}
		if (!"".equals(userName.getText())) {
			server.setUserName(userName.getText());
		} else {
			server.setUserName(null);
		}
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof ServerTypeBean) {
			server = (ServerTypeBean) object;
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {
		// set name
		setId(server.getId());
		hostName.setText(server.getHostName());
		userName.setText(server.getUserName());
		final String passw = server.getPassword();
		password.setText(passw);
	}

	/**
	 * Decrease the counter and then call to editableDisclosurePanel.close()
	 */
	@Override
	public void close() {
		super.close();
	}

	@Override
	protected boolean isValid() {
		updateRepresentedObject();
		final ServerTypeBean server = getObject();
		if (server.getId() == null || "".equals(server.getId()))
			return false;
		if (server.getHostName() == null)
			return false;
		if (server.getUserName() == null)
			return false;
		if (server.getPassword() == null)
			return false;

		return true;

	}

}
