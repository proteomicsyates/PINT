package edu.scripps.yates;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.cache.ClientCacheGeneralObjects;
import edu.scripps.yates.client.cache.GeneralObject;
import edu.scripps.yates.client.gui.AboutPanel;
import edu.scripps.yates.client.gui.BrowsePanel;
import edu.scripps.yates.client.gui.HelpPanel;
import edu.scripps.yates.client.gui.MainPanel;
import edu.scripps.yates.client.gui.PopUpPanelRedirector;
import edu.scripps.yates.client.gui.QueryPanel;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.MyWindowScrollPanel;
import edu.scripps.yates.client.gui.components.pseaquant.PSEAQuantFormPanel;
import edu.scripps.yates.client.gui.configuration.ConfigurationPanel;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.client.pint.wizard.NewProjectCreatorWizard;
import edu.scripps.yates.client.statusreporter.StatusReporterImpl;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.tasks.PendingTasksManager;
import edu.scripps.yates.client.util.ClientToken;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;
import edu.scripps.yates.shared.tasks.TaskType;
import edu.scripps.yates.shared.util.CryptoUtil;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Pint implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	private static boolean psmCentric = false; // by default

	private static Boolean isTestServer;
	private QueryPanel queryPanel;
	private MainPanel mainPanel;
	private BrowsePanel browsePanel;

	private MyWindowScrollPanel scroll;
	private String sessionID;

	private MyDialogBox loadingDialog;

	private ConfigurationPanel configurationPanel;
	private boolean testMode = false;
	private final Logger log = Logger.getLogger("");
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();

	public static void setPSMCentric(boolean b) {
		psmCentric = b;
	}

	public static boolean getPSMCentric() {
		return psmCentric;
	}

	@Override
	public void onModuleLoad() {

		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				final Throwable unwrapped = unwrap(e);
				GWT.log("Exception uncaught! " + e.getMessage());

				// Window.alert(unwrapped.getMessage());
				StatusReportersRegister.getInstance().notifyStatusReporters(unwrapped);
			}

			public Throwable unwrap(Throwable e) {
				e.printStackTrace();
				if (e instanceof UmbrellaException) {
					final UmbrellaException ue = (UmbrellaException) e;
					if (ue.getCauses().size() == 1) {
						return unwrap(ue.getCauses().iterator().next());
					}
				}
				return e;
			}
		});
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {

				showLoadingDialog(
						"Please wait while PINT is initialized.\nIt may take some seconds for the first time...", null);

				log.info("WELCOME TO PINT");
				GWT.log("Module base URL is: " + GWT.getModuleBaseURL());
				GWT.log("Host page base UTL is:  " + GWT.getHostPageBaseURL());
				GWT.log("Module base for static files is:  " + GWT.getModuleBaseForStaticFiles());
				GWT.log("Module name is:  " + GWT.getModuleName());
				GWT.log("Version is:  " + GWT.getVersion());
				GWT.log("Production mode: " + GWT.isProdMode());

				// register as status reporter
				final StatusReporterImpl statusReporter = new StatusReporterImpl();
				StatusReportersRegister.getInstance().registerNewStatusReporter(statusReporter);
				//

				// login
				login();

			}
		});

	}

	public void startupConfiguration(final boolean forceToShowPanel) {

		// code splitting
//		GWT.runAsync(new RunAsyncCallback() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				Window.alert("Code download failed");
//			}
//
//			@Override
//			public void onSuccess() {
		final ConfigurationServiceAsync service = ConfigurationServiceAsync.Util.getInstance();

		service.getPintConfigurationProperties(new AsyncCallback<PintConfigurationProperties>() {

			@Override
			public void onSuccess(PintConfigurationProperties properties) {
				if (properties != null) {
					psmCentric = properties.getPsmCentric();
					if (forceToShowPanel || properties.isSomeConfigurationMissing()) {
						showConfigurationPanel(properties);
					} else {

					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				GWT.log("Error setting up PINT: " + caught.getMessage());

			}
		});
//			}
//		});

	}

	protected void showConfigurationPanel(PintConfigurationProperties pintConfigurationProperties) {
		if (configurationPanel == null) {
			configurationPanel = new ConfigurationPanel(pintConfigurationProperties);
		}
		loadingDialog.hide();
		configurationPanel.center();
	}

	private void showLoadingDialog(String text, String buttonText) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, true, true, null, buttonText);
		}
		loadingDialog.setText(text);
		if (buttonText != null && !"".equals(buttonText)) {
			loadingDialog.setButtonText(buttonText);
		}
		loadingDialog.center();
	}

	private void loadGUI() {

		// code splitting
//		GWT.runAsync(new RunAsyncCallback() {
//
//			@Override
//			public void onFailure(Throwable reason) {
//				StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//			}
//
//			@Override
//			public void onSuccess() {

		// setup historychangehandler
		setUpHistory();

		final RootLayoutPanel rootPanel = RootLayoutPanel.get();
		rootPanel.setSize("100%", "100%");
		rootPanel.animate(100);
		scroll = new MyWindowScrollPanel();
		rootPanel.add(scroll);

		// MAIN PANEL
		mainPanel = new MainPanel(Pint.this);
		scroll.add(mainPanel);

		// / PROJECT CREATOR #projectCreator
		// createProjectPanel = new ProjectCreator();

		final String projectParameter = com.google.gwt.user.client.Window.Location.getParameter("project");
		final String testParameter = com.google.gwt.user.client.Window.Location.getParameter("test");

		if (testParameter != null) {
			try {
				testMode = Boolean.parseBoolean(testParameter);
			} catch (final Exception e) {

			}
		}
		parseEncryptedProjectValues(projectParameter);
		if (!"".equals(History.getToken())) {
			History.fireCurrentHistoryState();
		}
		loadingDialog.hide();
//			}
//		});

	}

	private void parseEncryptedProjectValues(String projectEncryptedValue) {

		if (projectEncryptedValue != null && !"".equals(projectEncryptedValue)) {
			String decodedProjectTag = "";
			if (projectEncryptedValue.contains("CFTR")) {
				decodedProjectTag = "_CFTR_";
			} else {
				decodedProjectTag = CryptoUtil.decrypt(projectEncryptedValue);

			}
			if (decodedProjectTag == null) {
				final MyDialogBox instance = new MyDialogBox(
						"PINT doesn't recognize the encrypted code as a valid project identifier", true, true, null,
						null);
				instance.setShowLoadingBar(false);
				instance.center();
				return;
			}
			final Set<String> projectTags = new HashSet<String>();
			if (decodedProjectTag.contains(",")) {
				final String[] split = decodedProjectTag.split(",");
				for (final String string : split) {
					projectTags.add(string);
				}
			} else {
				projectTags.add(decodedProjectTag);
			}
			final boolean directAccess = true;
			queryPanel = new QueryPanel(getSessionID(), projectTags, testMode, directAccess);
			History.newItem(TargetHistory.QUERY.getTargetHistory());
		}

	}

	private Set<String> getProjectValues(String projectValue) {
		final Set<String> projectTags = new HashSet<String>();
		if (projectValue.contains("?")) {
			projectValue = projectValue.substring(projectValue.indexOf("?") + 1);
		}
		if (projectValue.contains("project")) {
			projectValue = projectValue.substring(projectValue.indexOf("project") + "project".length() + 1);
		}

		if (projectValue != null && !"".equals(projectValue)) {

			if (projectValue.contains(",")) {
				final String[] split = projectValue.split(",");
				for (final String string : split) {
					projectTags.add(string.trim());
				}
			} else {
				projectTags.add(projectValue);
			}

		}
		return projectTags;
	}

	private void parseProjectValues(String projectValue) {

		final Set<String> projectTags = getProjectValues(projectValue);
		if (!projectTags.isEmpty()) {

			if (queryPanel != null && queryPanel.hasLoadedThisProjects(projectTags)) {
			} else {
				final boolean directAccess = false; // if private, they has to
													// login
				queryPanel = new QueryPanel(getSessionID(), projectTags, testMode, directAccess);
			}
//			queryPanel.showLoadingDialogOLD("Loading dataset view...", null, null);
			History.newItem(TargetHistory.QUERY.getTargetHistory());

		} else {
			final PopUpPanelRedirector popup = new PopUpPanelRedirector(true, true, true, "No projects selected",
					"You need to select one project before to query the data.\nClick here to go to Browse menu.",
					TargetHistory.BROWSE);
			popup.show();
		}

	}

	private void login() {

//		GWT.runAsync(new RunAsyncCallback() {
//
//			@Override
//			public void onSuccess() {
		isTestServer();

		final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();
		final String clientToken = ClientToken.getToken();

		final String userName = "guest";
		final String password = "guest";
		service.login(clientToken, userName, password, new AsyncCallback<String>() {

			@Override
			public void onSuccess(String sessionID) {
				GWT.log("New session id:" + sessionID);
				Pint.this.setSessionID(sessionID);

				loadScoreTypes();

			}

			private void loadScoreTypes() {
				Pint.this.service.getScoreTypes(getSessionID(), new AsyncCallback<List<String>>() {

					@Override
					public void onFailure(Throwable caught) {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						GWT.log("Error loading score types: " + caught.getMessage());
						loadingDialog.hide();
					}

					@Override
					public void onSuccess(List<String> result) {
						ClientCacheGeneralObjects.getInstance().addtoCache(result, GeneralObject.SCORE_TYPES);
						loadPTMNames();

					}

					private void loadPTMNames() {
						Pint.this.service.getPTMNames(getSessionID(), new AsyncCallback<List<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
								GWT.log("Error loading PTM names: " + caught.getMessage());
								loadingDialog.hide();
							}

							@Override
							public void onSuccess(List<String> result) {
								ClientCacheGeneralObjects.getInstance().addtoCache(result, GeneralObject.PTM_NAMES);
								loadTissueList();

							}

							private void loadTissueList() {
								Pint.this.service.getTissueList(getSessionID(), new AsyncCallback<List<String>>() {

									@Override
									public void onFailure(Throwable caught) {
										StatusReportersRegister.getInstance().notifyStatusReporters(caught);
										GWT.log("Error loading Tissue list: " + caught.getMessage());
										loadingDialog.hide();
									}

									@Override
									public void onSuccess(List<String> result) {
										ClientCacheGeneralObjects.getInstance().addtoCache(result,
												GeneralObject.TISSUE_LIST);
										loadGUI();
										startupConfiguration(false);
									}

								});
							}
						});

					}
				});

			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				GWT.log("Error in login: " + caught.getMessage());
				loadingDialog.hide();
			}
		});
//			}
//
//			@Override
//			public void onFailure(Throwable reason) {
//				StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//			}
//		});

	}

	private void setUpHistory() {

		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			private HelpPanel helpPanel;
			private AboutPanel aboutPanel;
			private NewProjectCreatorWizard createProjectWizardPanel;

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				final String historyToken = event.getValue();

				GWT.log("HISTORY VALUE: " + historyToken);
				// Parse the history token
				if (historyToken.contains(TargetHistory.QUERY.getTargetHistory())) {
 
					// queryPanel is suppose to be already created
					if (queryPanel == null || queryPanel.getLoadedProjects().isEmpty()) {
						final PopUpPanelRedirector popup = new PopUpPanelRedirector(true, true, true,
								"No projects selected",
								"You need to select one project before to query the data.\nClick here to go to Browse menu.",
								TargetHistory.BROWSE);
						popup.show();
					} else {
						loadPanel(queryPanel);

					}
					return;
//						}
//
//					});
					// we have to return here, otherwise we are going to clear
					// the loadedProjects and the previous if clause is going to
					// be true
//					return;
				}
				// removePending task
				PendingTasksManager.removeAllTasks(TaskType.PROTEINS_BY_PROJECT);
				PendingTasksManager.removeAllTasks(TaskType.QUERY_SENT);
				QueryPanel.loadedProjects.clear();

				if (historyToken.contains(TargetHistory.BROWSE.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					if (browsePanel == null)
						browsePanel = new BrowsePanel();
					loadPanel(browsePanel);
//						}
//					});
				}
				if (historyToken.contains(TargetHistory.HOME.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					if (mainPanel == null)
						mainPanel = new MainPanel(Pint.this);
					loadPanel(mainPanel);
					mainPanel.loadStatistics();
//						}
//					});
				}
				if (historyToken.contains(TargetHistory.RELOAD.getTargetHistory())) {
					parseProjectValues(historyToken);

				}
				if (historyToken.equals(TargetHistory.SUBMIT.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					if (createProjectWizardPanel == null) {
						// not create again to not let the user loose
						// everything
						createProjectWizardPanel = new NewProjectCreatorWizard(getSessionID());
					}
					loadPanel(createProjectWizardPanel);
//						}
//					});

					// if (createProjectPanel == null)
					// createProjectPanel = new ProjectCreator();
					// loadPanel(createProjectPanel);
				}
				if (historyToken.equals(TargetHistory.HELP.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					if (helpPanel == null) {
						helpPanel = new HelpPanel();
					}
					loadPanel(helpPanel);
//						}
//					});
				}
				if (historyToken.equals(TargetHistory.ABOUT.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					if (aboutPanel == null) {
						aboutPanel = new AboutPanel();
					}
					loadPanel(aboutPanel);
//						}
//					});
				}
				if (historyToken.startsWith(TargetHistory.LOAD_PROJECT.getTargetHistory())) {
					parseProjectValues(historyToken);
				}
				if (historyToken.contains(TargetHistory.PSEAQUANT.getTargetHistory())) {
//					GWT.runAsync(new RunAsyncCallback() {
//
//						@Override
//						public void onFailure(Throwable reason) {
//							StatusReportersRegister.getInstance().notifyStatusReporters(reason);
//						}
//
//						@Override
//						public void onSuccess() {
					final PSEAQuantFormPanel pseaQuant = new PSEAQuantFormPanel(getProjectValues(historyToken));
					loadPanel(pseaQuant);
//						}
//					});
				}

			}

		});

	}

	protected void loadPanel(Widget widget) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				RootLayoutPanel.get().clear();

				if (widget instanceof QueryPanel) {
					RootLayoutPanel.get().add(widget);
				} else if (widget instanceof NewProjectCreatorWizard) {
					RootLayoutPanel.get().add(widget);
				} else {
					// in any other case, add with a scrollpanel
					RootLayoutPanel.get().add(scroll);
					scroll.clear();
					scroll.add(widget);

				}
				if (widget != null && widget instanceof InitializableComposite) {
					((InitializableComposite) widget).initialize();
				}
			}
		});

	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public static boolean isTestServer() {
		if (isTestServer == null) {
			ProteinRetrievalServiceAsync.Util.getInstance().isTestServer(new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				}

				@Override
				public void onSuccess(Boolean result) {
					isTestServer = result;
				}
			});
		}
		if (isTestServer == null) {
			return true;
		}
		return isTestServer;
	}

}
