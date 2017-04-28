package edu.scripps.yates.client.gui.configuration;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ConfigurationServiceAsync;
import edu.scripps.yates.client.gui.PopUpPanelYesNo;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.StatusReporter;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;
import edu.scripps.yates.shared.util.CryptoUtil;

public class ConfigurationPanel extends WindowBox implements StatusReporter {
	private final PintConfigurationProperties pintConfigurationProperties;
	private final ConfigurationServiceAsync service = ConfigurationServiceAsync.Util.getInstance();
	private Label statusLabel;
	private Label labelOmimStatus;
	private Label adminPasswordLabel;
	private Label labelDBURLStatus;
	private Label labelDBUserNameStatus;
	private Label labelDBPasswordStatus;
	private Label labelProjectfilePathStatus;
	private Label labelIsPreLoadPublicProjectStatus;
	private Label labelprojectsToPreloadStatus;
	private Label labelprojectsToNotPreloadStatus;
	private boolean somethingchanged = false;

	public ConfigurationPanel(PintConfigurationProperties pintConfigurationProperties) {
		super(false, true, true, false, true);
		this.pintConfigurationProperties = pintConfigurationProperties;
		// setStyleName("configurationPanel");
		setText("PINT basic configuration");
		ScrollPanel scroll = new ScrollPanel(createMainWidget());
		scroll.setSize("500px", "600px");
		setWidget(scroll);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		resize();
		// check closability
		checkClosability();
		setSize("500px", "600px");
		addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				showRealodDialog();
			}
		});
	}

	protected void showRealodDialog() {
		if (somethingchanged) {
			String text = "Click OK to reload PINT with the new configuration";
			PopUpPanelYesNo reloadDialog = new PopUpPanelYesNo(false, true, true, "Reload page", text, "OK", null);
			reloadDialog.addButton1ClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					reloadDialog.hide();
					Window.Location.reload();
				}
			});

			reloadDialog.show();
		}

	}

	private void checkClosability() {
		// if everything is fine, enable the close button
		final boolean closable = !pintConfigurationProperties.isSomeConfigurationMissing();
		setCloseIconVisible(closable);
		// if (closable) {
		// showMessage("PINT is successfully configured.\nNow you can close this
		// dialog.");
		// }
	}

	private Widget createMainWidget() {
		FlowPanel mainPanel = new FlowPanel();
		InlineLabel label = new InlineLabel("Welcome to the basic configuration of PINT");
		label.setStyleName("configurationPanel_welcome");
		mainPanel.add(label);
		CaptionPanel firstCaptionPanel = new CaptionPanel("Explanation");
		firstCaptionPanel.setStyleName("configurationPanel_CaptionPanel");
		VerticalPanel verticalpanel = new VerticalPanel();
		verticalpanel.getElement().getStyle().setMargin(10, Unit.PX);

		InlineLabel label2 = new InlineLabel("Here you can set or update some basic parameters.");
		verticalpanel.add(label2);

		Label label3 = new Label(
				"This form will update the file located at your server at: pint_webapp/WEB-INF/pint.properties. So you could also directly edit that file and then restart the server.");
		verticalpanel.add(label3);
		HorizontalPanel horizontal = new HorizontalPanel();
		Label label4 = new Label("For a template of a pint.properties file, go to ");
		horizontal.add(label4);
		Anchor link = new Anchor(" here", true,
				"https://raw.githubusercontent.com/proteomicsyates/PINT/master/pint_webapp/src/main/webapp/WEB-INF/pint.properties",
				"_blank");
		horizontal.add(link);
		verticalpanel.add(horizontal);
		firstCaptionPanel.add(verticalpanel);
		mainPanel.add(firstCaptionPanel);
		// admin password
		CaptionPanel adminPassCaptionPanel = new CaptionPanel("PINT master password");
		adminPassCaptionPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(adminPassCaptionPanel);
		adminPassCaptionPanel.add(getAdminPassRowPanel());
		// omim key
		CaptionPanel omimKeyCaptionPanel = new CaptionPanel("OMIM key");
		omimKeyCaptionPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(omimKeyCaptionPanel);
		omimKeyCaptionPanel.add(getOMIMKeyRowPanel());
		// database connection
		CaptionPanel databaseConnectionPanel = new CaptionPanel("Database connection settings");
		databaseConnectionPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(databaseConnectionPanel);
		final FlexTable grid = new FlexTable();
		grid.setWidget(0, 0, new Label(
				"All projects in PINT are stored in a database. Here you can configure the access to that database."));
		HorizontalPanel horizontal2 = new HorizontalPanel();
		Anchor link2 = new Anchor("create_db_schema_096.sql", true,
				"https://raw.githubusercontent.com/proteomicsyates/PINT/master/pint.mysql/create_db_schema_096.sql",
				"_blank");
		horizontal2.add(new Label("SQL script to create the database: "));
		horizontal2.add(link2);
		grid.setWidget(1, 0, horizontal2);
		grid.setWidget(2, 0, getDataBaseConnectionRowPanel());
		grid.setWidget(3, 0, getDataBaseUserNameRowPanel());
		grid.setWidget(4, 0, getDataBasePasswordRowPanel());
		databaseConnectionPanel.add(grid);

		// server storage folder
		CaptionPanel projectPreloadingPanel = new CaptionPanel("Project preloading");
		projectPreloadingPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(projectPreloadingPanel);
		final Grid grid2 = new Grid(3, 1);
		grid2.setWidget(0, 0, getProjectPreLoadPublicRowPanel());
		grid2.setWidget(1, 0, getProjectsToPreLoadRowPanel());
		grid2.setWidget(2, 0, getProjectsToNotPreLoadRowPanel());
		projectPreloadingPanel.add(grid2);
		// status
		CaptionPanel statusPanel = new CaptionPanel("Status");
		statusPanel.setStyleName("configurationPanel_CaptionPanel");
		mainPanel.add(statusPanel);
		statusLabel = new Label();
		statusLabel.setWordWrap(true);
		statusPanel.add(statusLabel);

		return mainPanel;
	}

	private Widget getProjectsToPreLoadRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");
		grid.setWidget(0, 0, new Label("Projects to pre-load (comma separated):"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 4);
		final String projectsToPreload = pintConfigurationProperties.getProjectsToPreLoad();
		grid.setWidget(1, 0, getValidIcon(projectsToPreload, false));
		final TextBox textBox = new TextBox();
		if (projectsToPreload != null) {
			textBox.setText(projectsToPreload);
		}
		grid.setWidget(1, 1, textBox);
		String html = "Set";
		if (projectsToPreload != null && !"".equals(projectsToPreload)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting projects to preload");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setProjectsToPreload(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setProjectsToPreLoad(textBox.getText());
						labelprojectsToPreloadStatus.setText(getStatusTextForCSVList(textBox.getText()));
						grid.setWidget(1, 0, getGreenTickIcon());
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("Projects to preload updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setProjectsToPreLoad(null);
						labelprojectsToPreloadStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelprojectsToPreloadStatus = new Label(getStatusText(projectsToPreload));
		grid.setWidget(1, 3, labelprojectsToPreloadStatus);
		return grid;
	}

	private Widget getProjectsToNotPreLoadRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");
		grid.setWidget(0, 0, new Label("Public projects to not pre-load (comma separated):"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 4);
		final String projectsToNotPreload = pintConfigurationProperties.getProjectsToNotPreLoad();
		grid.setWidget(1, 0, getValidIcon(projectsToNotPreload, false));
		final TextBox textBox = new TextBox();
		if (projectsToNotPreload != null) {
			textBox.setText(projectsToNotPreload);
		}
		grid.setWidget(1, 1, textBox);
		String html = "Set";
		if (projectsToNotPreload != null && !"".equals(projectsToNotPreload)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting projects to not pre-load");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setProjectsToNotPreload(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setProjectsToNotPreLoad(textBox.getText());
						labelprojectsToNotPreloadStatus.setText(getStatusTextForCSVList(textBox.getText()));
						grid.setWidget(1, 0, getGreenTickIcon());
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("Projects to not pre-load updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setProjectsToNotPreLoad(null);
						labelprojectsToNotPreloadStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelprojectsToNotPreloadStatus = new Label(getStatusText(projectsToNotPreload));
		grid.setWidget(1, 3, labelprojectsToNotPreloadStatus);
		return grid;
	}

	private Widget getProjectPreLoadPublicRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");

		final Boolean isPreLoadPublicProjects = pintConfigurationProperties.isPreLoadPublicProjects();
		grid.setWidget(0, 0, getGreenTickIcon());

		final CheckBox checkBox = new CheckBox("Pre load public projects");
		if (isPreLoadPublicProjects != null) {
			checkBox.setValue(isPreLoadPublicProjects);
		}
		grid.setWidget(0, 1, checkBox);
		String html = "Set";
		if (isPreLoadPublicProjects != null && !"".equals(isPreLoadPublicProjects)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(0, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting pre load public projects");
				grid.setWidget(0, 0, imageLoading);
				button.setEnabled(false);
				checkBox.setEnabled(false);
				service.setPreLoadPublicProjects(checkBox.getValue(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setPreLoadPublicProjects(checkBox.getValue());
						labelIsPreLoadPublicProjectStatus.setText(getStatusText(checkBox.getValue()));
						grid.setWidget(0, 0, getGreenTickIcon());
						button.setEnabled(true);
						checkBox.setEnabled(true);
						checkClosability();
						showMessage("Pre load public projects set to " + checkBox.getValue());
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setPreLoadPublicProjects(null);
						labelIsPreLoadPublicProjectStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(0, 0, getValidIcon(false));
						button.setEnabled(true);
						checkBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelIsPreLoadPublicProjectStatus = new Label(getStatusText(isPreLoadPublicProjects));
		grid.setWidget(0, 3, labelIsPreLoadPublicProjectStatus);
		return grid;
	}

	private Widget getDataBaseUserNameRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");
		grid.setWidget(0, 0, new Label("Database connection user name:"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 4);
		final String dbUserName = pintConfigurationProperties.getDb_username();
		grid.setWidget(1, 0, getValidIcon(dbUserName, true));
		final TextBox textBox = new TextBox();
		if (dbUserName != null) {
			textBox.setText(dbUserName);
		}
		grid.setWidget(1, 1, textBox);
		String html = "Set";
		if (dbUserName != null && !"".equals(dbUserName)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting database user name");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setDBUserName(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setDb_username(textBox.getText());
						labelDBUserNameStatus.setText(getStatusText(textBox.getText()));
						grid.setWidget(1, 0, getValidIcon(textBox.getText(), true));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("Database user name valid and updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setDb_username(null);
						labelDBUserNameStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelDBUserNameStatus = new Label(getStatusText(dbUserName));
		grid.setWidget(1, 3, labelDBUserNameStatus);
		return grid;
	}

	private Widget getDataBasePasswordRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");
		grid.setWidget(0, 0, new Label("Database connection password:"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 5);
		final String dbPassword = pintConfigurationProperties.getDb_password();
		grid.setWidget(1, 0, getValidIcon(dbPassword, true));
		final PasswordTextBox passwordTextBox = new PasswordTextBox();
		final TextBox textBox = new TextBox();
		textBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				passwordTextBox.setText(textBox.getText());

			}
		});
		passwordTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				textBox.setText(passwordTextBox.getText());

			}
		});
		if (dbPassword != null) {
			passwordTextBox.setText(dbPassword);
		}
		if (dbPassword != null) {
			textBox.setText(dbPassword);
		}

		grid.setWidget(1, 1, passwordTextBox);
		String html = "Set";
		if (dbPassword != null && !"".equals(dbPassword)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting database connection password URL");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				passwordTextBox.setEnabled(false);
				final String decryptedPassword = passwordTextBox.getText();
				final String encryptedPassword = CryptoUtil.encrypt(decryptedPassword);
				service.setDBPassword(encryptedPassword, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						final String decryptedPassword = passwordTextBox.getText();
						final String encryptedPassword = CryptoUtil.encrypt(decryptedPassword);
						pintConfigurationProperties.setDb_password(encryptedPassword);
						labelDBPasswordStatus.setText(getStatusText(passwordTextBox.getText()));
						grid.setWidget(1, 0, getValidIcon(passwordTextBox.getText(), true));
						button.setEnabled(true);
						passwordTextBox.setEnabled(true);
						checkClosability();
						showMessage("Database connection password valid and updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setDb_password(null);
						labelDBPasswordStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						passwordTextBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelDBPasswordStatus = new Label(getStatusText(dbPassword));
		grid.setWidget(1, 3, labelDBPasswordStatus);
		final CheckBox checkBox = new CheckBox("show password");
		grid.setWidget(1, 4, checkBox);
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Boolean showPassword = checkBox.getValue();
				if (showPassword) {
					grid.setWidget(1, 1, textBox);
				} else {
					grid.setWidget(1, 1, passwordTextBox);
				}
			}
		});
		return grid;
	}

	private Widget getDataBaseConnectionRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");

		grid.setWidget(0, 0, new Label("Connection URL:"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 4);
		final String dbURL = pintConfigurationProperties.getDb_url();
		grid.setWidget(1, 0, getValidIcon(dbURL, true));
		final TextBox textBox = new TextBox();
		if (dbURL != null) {
			textBox.setText(dbURL);
		}
		grid.setWidget(1, 1, textBox);
		String html = "Set";
		if (dbURL != null && !"".equals(dbURL)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting database URL");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setDBURL(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setDb_url(textBox.getText());
						labelDBURLStatus.setText(getStatusText(textBox.getText()));
						grid.setWidget(1, 0, getValidIcon(textBox.getText(), true));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("Database URL valid and updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setDb_url(null);
						labelDBURLStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelDBURLStatus = new Label(getStatusText(dbURL));
		grid.setWidget(1, 3, labelDBURLStatus);
		return grid;
	}

	private Widget getOMIMKeyRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setWidget(0, 0, new Label("OMIM personal key:"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 4);
		grid.setStyleName("configurationPanel_Grid");
		final String omimKey = pintConfigurationProperties.getOmimKey();
		grid.setWidget(1, 0, getValidIcon(omimKey, false));
		final TextBox textBox = new TextBox();
		if (omimKey != null) {
			textBox.setText(omimKey);
		}
		grid.setWidget(1, 1, textBox);
		String html = "Set";
		if (omimKey != null && !"".equals(omimKey)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting new OMIM key");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				textBox.setEnabled(false);
				service.setOMIMKey(textBox.getText(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setOmimKey(textBox.getText());
						labelOmimStatus.setText(getStatusText(textBox.getText()));
						grid.setWidget(1, 0, getValidIcon(textBox.getText(), false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
						showMessage("OMIM key updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setOmimKey(null);
						labelOmimStatus.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						textBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		labelOmimStatus = new Label(getStatusText(omimKey));
		grid.setWidget(1, 3, labelOmimStatus);
		return grid;
	}

	private String getStatusText(Object item) {
		if (item == null || "".equals(item.toString())) {
			return "not defined";
		} else {
			return "valid";
		}
	}

	private String getStatusTextForCSVList(String csvList) {
		if (getValidIconForCSVList(csvList).equals(getRedCrossIcon())) {
			return "Malformed list. Project tags have no spaces";
		} else {
			return "valid";
		}
	}

	private Widget getAdminPassRowPanel() {
		final FlexTable grid = new FlexTable();
		grid.setStyleName("configurationPanel_Grid");
		grid.setWidget(0, 0, new Label(
				"This password will be needed to enter in this configuration, and also for data submission and deletion"));
		grid.getFlexCellFormatter().setColSpan(0, 0, 5);
		final String encryptedAdminPassword = pintConfigurationProperties.getAdminPassword();
		final String adminPassword = CryptoUtil.decrypt(encryptedAdminPassword);
		grid.setWidget(1, 0, getValidIcon(adminPassword, true));
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
		grid.setWidget(1, 1, adminPasswordTextBox);
		String html = "Set";
		if (adminPassword != null && !"".equals(adminPassword)) {
			html = "Update";
		}
		final Button button = new Button(html);
		grid.setWidget(1, 2, button);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Image imageLoading = new Image(MyClientBundle.INSTANCE.smallLoader());
				imageLoading.setTitle("Setting new password");
				grid.setWidget(1, 0, imageLoading);
				button.setEnabled(false);
				adminPasswordTextBox.setEnabled(false);
				final String decryptedAdminPassword = adminPasswordTextBox.getText();
				final String encryptedAdminPassword = CryptoUtil.encrypt(decryptedAdminPassword);
				service.setAdminPassword(encryptedAdminPassword, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						pintConfigurationProperties.setAdminPassword(decryptedAdminPassword);
						adminPasswordLabel.setText(getStatusText(decryptedAdminPassword));
						grid.setWidget(1, 0, getValidIcon(decryptedAdminPassword, true));
						button.setEnabled(true);
						adminPasswordTextBox.setEnabled(true);
						checkClosability();
						showMessage("Password updated");
						somethingchanged = true;
					}

					@Override
					public void onFailure(Throwable caught) {
						showErrorMessage(caught);
						pintConfigurationProperties.setOmimKey(null);
						adminPasswordLabel.setText(getStatusText(null));
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error setting up PINT: " + caught.getMessage());
						grid.setWidget(1, 0, getValidIcon(false));
						button.setEnabled(true);
						adminPasswordTextBox.setEnabled(true);
						checkClosability();
					}
				});

			}
		});
		adminPasswordLabel = new Label(getStatusText(adminPassword));
		grid.setWidget(1, 3, adminPasswordLabel);

		final CheckBox checkBox = new CheckBox("show password");
		grid.setWidget(1, 4, checkBox);
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final Boolean showPassword = checkBox.getValue();
				if (showPassword) {
					grid.setWidget(1, 1, adminTextBox);
				} else {
					grid.setWidget(1, 1, adminPasswordTextBox);
				}
			}
		});
		return grid;
	}

	private Widget getValidIcon(String item, boolean mandatory) {
		if (mandatory) {
			if (item == null || "".equals(item)) {
				return getValidIcon(false);
			}
		} else {
			// if it is not mandatory, do not add any icon
			if (item == null || "".equals(item)) {
				return null;
			}

		}
		return getValidIcon(true);
	}

	private Widget getValidIconForCSVList(String cvList) {
		if (cvList == null) {
			return getValidIcon(false);
		}

		return getValidIcon(true);
	}

	private Widget getValidIcon(boolean valid) {

		if (!valid) {
			return getRedCrossIcon();
		} else {
			return getGreenTickIcon();
		}
	}

	private Widget getRedCrossIcon() {
		return new Image(MyClientBundle.INSTANCE.redCross());
	}

	private Widget getGreenTickIcon() {
		return new Image(MyClientBundle.INSTANCE.greenTick());
	}

	@Override
	public void showMessage(String message) {
		statusLabel.setText(message);
		statusLabel.setStyleName("configurationPanel_Status");
		// show pop up dialog
		PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, "Configuration property updated", message,
				"OK", null);
		popUpDialog.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpDialog.hide();

			}
		});
		popUpDialog.show();

	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		statusLabel.setText(throwable.getMessage());
		statusLabel.setStyleName("configurationPanel_Status_Error");
		// show pop up dialog
		PopUpPanelYesNo popUpDialog = new PopUpPanelYesNo(true, true, true, "Error updating configuration property",
				throwable.getMessage(), "OK", null);
		popUpDialog.addButton1ClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popUpDialog.hide();

			}
		});
		popUpDialog.show();
	}
}
