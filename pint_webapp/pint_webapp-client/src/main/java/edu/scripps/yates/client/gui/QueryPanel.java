package edu.scripps.yates.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.Pint;
import edu.scripps.yates.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.cache.ClientCacheConditionsByProject;
import edu.scripps.yates.client.cache.ClientCacheDefaultViewByProjectTag;
import edu.scripps.yates.client.gui.components.AbstractDataTable;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.MyQueryEditorPanel;
import edu.scripps.yates.client.gui.components.MyVerticalCheckBoxListPanel;
import edu.scripps.yates.client.gui.components.MyVerticalConditionsListBoxPanel;
import edu.scripps.yates.client.gui.components.MyVerticalListBoxPanel;
import edu.scripps.yates.client.gui.components.MyVerticalProteinInferenceCommandPanel;
import edu.scripps.yates.client.gui.components.MyWarningMultipleProjectsLoadedPanel;
import edu.scripps.yates.client.gui.components.MyWelcomeProjectPanel;
import edu.scripps.yates.client.gui.components.PSMTablePanel;
import edu.scripps.yates.client.gui.components.PeptideTablePanel;
import edu.scripps.yates.client.gui.components.ProjectInformationPanel;
import edu.scripps.yates.client.gui.components.ProteinGroupTablePanel;
import edu.scripps.yates.client.gui.components.ProteinTablePanel;
import edu.scripps.yates.client.gui.components.ScrolledTabLayoutPanel;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPSMBeanListDataProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPSMBeanListFromPsmProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPeptideBeanListDataProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncPeptideBeanListFromPeptideProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncProteinBeanListDataProvider;
import edu.scripps.yates.client.gui.components.dataprovider.AsyncProteinGroupBeanListDataProvider;
import edu.scripps.yates.client.gui.components.progressbar.AdvancedProgressDialog;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.pseaquant.PSEAQuantFormPanel;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.reactome.ReactomePanel;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.client.statusreporter.StatusReporter;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.tasks.PendingTaskHandler;
import edu.scripps.yates.client.tasks.PendingTasksManager;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.client.util.ClientDataUtil;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.client.util.ProjectsBeanSet;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinProjection;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.model.light.ProteinGroupBeanLight;
import edu.scripps.yates.shared.tasks.CancellingTask;
import edu.scripps.yates.shared.tasks.GetProteinsFromProjectTask;
import edu.scripps.yates.shared.tasks.GetProteinsFromQuery;
import edu.scripps.yates.shared.tasks.GroupProteinsTask;
import edu.scripps.yates.shared.tasks.Task;
import edu.scripps.yates.shared.tasks.TaskType;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.TAB;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.QueryResultSubLists;

public class QueryPanel extends InitializableComposite implements ShowHiddePanel, PendingTaskHandler, StatusReporter {
	private static final double HEADER_HEIGHT = 25;

	private final TextArea textAreaStatus;
	private MyVerticalListBoxPanel projectListPanel;
	private MyVerticalConditionsListBoxPanel conditionsPanel;
	private MyVerticalProteinInferenceCommandPanel proteinGroupingCommandPanel;
	private final ProteinTablePanel proteinTablePanel;
	private PSMTablePanel psmTablePanel;
	private PSMTablePanel psmOnlyTablePanel;
	private final PeptideTablePanel peptideOnlyTablePanel;
	private PeptideTablePanel peptideTablePanel;
	private final MyVerticalCheckBoxListPanel<ProteinBeanLight> proteinColumnNamesPanel;
	private final MyVerticalCheckBoxListPanel<PSMBeanLight> psmColumnNamesPanel;
	private MyVerticalCheckBoxListPanel<PeptideBeanLight> peptideColumnNamesPanel;
	private final ProteinGroupTablePanel proteinGroupTablePanel;
	private final MyVerticalCheckBoxListPanel<ProteinGroupBeanLight> proteinGroupColumnNamesPanel;
	private final ScrolledTabLayoutPanel secondLevelTabPanel;
	private final MyVerticalListBoxPanel annotationsTypePanel;
	private final MyVerticalListBoxPanel uniprotHeaderLinesPanel;
	private final MyVerticalListBoxPanel scoreTypesPanel;
	private final MyVerticalListBoxPanel thresholdNamesPanel;
	private final MyVerticalListBoxPanel ratioNamesPanel;
	private final MyQueryEditorPanel queryEditorPanel;
	private final ScrolledTabLayoutPanel firstLevelTabPanel;
	private MyVerticalListBoxPanel commandsPanel;
	private final MyVerticalListBoxPanel scoreNamesPanel;
	public static final double LEFT_MENU_WIDTH = 200.0;

	protected final edu.scripps.yates.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();

	public static final Set<String> loadedProjects = new HashSet<String>();
	private final ProjectsBeanSet loadedProjectBeanSet = new ProjectsBeanSet();
	private MyDialogBox loadingDialog;

	private StackLayoutPanel menuUpStackLayoutPanel;

	private LayoutPanel layoutPanel;

	private WindowBox welcomeToProjectWindowBox;
	private AsyncPSMBeanListFromPsmProvider asyncDataProviderForPSMsOfSelectedProtein;
	private AsyncPeptideBeanListFromPeptideProvider asyncDataProviderForPeptidesOfSelectedProtein;

	private String sessionID;
	private ProjectInformationPanel projectInformationPanel;
	private ScrollPanel scrollQueryPanel;
	private ScrollPanel scrollProjectInformationPanel;

	/**
	 * Returns true if the projects contained in the loadedProjects set is the same
	 * than the provided
	 *
	 * @param projectNames
	 * @return
	 */
	public boolean hasLoadedThisProjects(Set<String> projectNames) {
		for (final String projectName : projectNames) {
			if (!loadedProjects.contains(projectName))
				return false;
		}
		if (loadedProjects.size() != projectNames.size())
			return false;

		GWT.log(projectNames.size() + " projects were already loaded.");
		return true;
	}

	/**
	 * @return the loadedProjects
	 */
	public Set<String> getLoadedProjects() {
		return loadedProjects;
	}

	private final ReactomePanel reactomePanel;
	private Timer timer;
	private final QueryHelpPanel helpPanel;

	private AdvancedProgressDialog advancedProgressDialog;

	public QueryPanel(String sessionID, Set<String> projectTags, boolean testMode, boolean directAccess) {
		this(sessionID, testMode);
		loadProjectListFromServer(projectTags, testMode, directAccess);
		// PSEA QUANT
		final PSEAQuantFormPanel pseaQuantFormPanel = new PSEAQuantFormPanel(loadedProjects);
		final ScrollPanel pseaQuantScrollPanel = new ScrollPanel(pseaQuantFormPanel);
		firstLevelTabPanel.add(pseaQuantScrollPanel, "PSEA-Quant");
		firstLevelTabPanel.add(reactomePanel, "Reactome");
		firstLevelTabPanel.add(helpPanel, "Help");
	}

	/**
	 * @wbp.parser.constructor
	 */
	private QueryPanel(String sessionID, final boolean testMode) {
		PendingTasksManager.registerPendingTaskController(this);
		this.sessionID = sessionID;

		// reactome panel
		reactomePanel = ReactomePanel.getInstance(sessionID);
		// help panel
		helpPanel = QueryHelpPanel.getInstance(sessionID);

		final DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);
//		showLoadingDialogOLD("Loading PINT components...", null, null);

		// HeaderPanel header = new HeaderPanel();
		// mainPanel.add(header);

		final NavigationHorizontalPanel navigationPanel = new NavigationHorizontalPanel(TargetHistory.QUERY);
		mainPanel.addNorth(navigationPanel, 40.0);

		textAreaStatus = new TextArea();
		mainPanel.addSouth(textAreaStatus, 100.0);
		textAreaStatus.setSize("100%", "100%");
		textAreaStatus.setVisibleLines(4);
		textAreaStatus.setText("Starting dataset view...");
		textAreaStatus.setReadOnly(true);

		final SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel(7);
		mainPanel.add(splitLayoutPanel);
		splitLayoutPanel.setStyleName("queryPanelMainPanel");
		final SplitLayoutPanel splitPanelWest = new SplitLayoutPanel(7);
		splitPanelWest.setStyleName("queryPanelcommandsMenu");
		splitLayoutPanel.addWest(splitPanelWest, LEFT_MENU_WIDTH);

		menuUpStackLayoutPanel = new StackLayoutPanel(Unit.PX);
		// menuUpStackLayoutPanel.setStyleName("gwt-StackLayoutPanel");
		splitPanelWest.add(menuUpStackLayoutPanel);

		// StackLayoutPanel menuDownStackLayoutPanel = new
		// StackLayoutPanel(Unit.PX);
		// menuDownStackLayoutPanel.setStyleName("gwt-StackLayoutPanel");
		// splitPanelWest.add(menuDownStackLayoutPanel);
		// menuDownStackLayoutPanel.setSize("100%", "100%");

		// end second tab

		// create the tab panel
		firstLevelTabPanel = new ScrolledTabLayoutPanel(null, null);
		firstLevelTabPanel.setHeight("100%");
		firstLevelTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				final Integer item = event.getSelectedItem();
				if (item != null) {
					if (item == 0) {
						// project info
					} else if (item == 1) {
						// Dataset view
						final int secondLevelIndex = secondLevelTabPanel.getSelectedIndex();
						if (secondLevelIndex == 0) {
							// protein groups
							selectDataTab(proteinGroupTablePanel);
						} else if (secondLevelIndex == 1) {
							// proteins
							selectDataTab(proteinTablePanel);
						} else if (secondLevelIndex == 2) {
							// peptides
							selectDataTab(peptideOnlyTablePanel);
						}
					} else {
						// other
					}
				}
			}
		});
		// create the content panel
		// Widget[] contentWidgets = { mainDataTabPanel };
		final FlowPanel contentPanel = new FlowPanel();
		contentPanel.add(firstLevelTabPanel);
		contentPanel.setStyleName("queryPanelDataTablesPanel");
		splitLayoutPanel.add(contentPanel);

		// ANNOTATIONS PANEL
		annotationsTypePanel = new MyVerticalListBoxPanel(false);
		annotationsTypePanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> annotationTypeNames = ClientGUIUtil
						.getSelectedItemsFromListBox(annotationsTypePanel.getListBox());
				if (annotationTypeNames != null) {
					final String annotationType = annotationTypeNames.iterator().next();
					appendTextToQueryTextBox(annotationType);
				}

			}

		});

		// UNIPROT HEADER LINES
		uniprotHeaderLinesPanel = new MyVerticalListBoxPanel(false);
		uniprotHeaderLinesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> uniprotHeaderLines = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(uniprotHeaderLinesPanel.getListBox());
				if (uniprotHeaderLines != null) {
					final String uniprotHeaderLine = uniprotHeaderLines.iterator().next();
					appendTextToQueryTextBox(uniprotHeaderLine);
				}

			}

		});

		// SCORE TYPES PANEL
		scoreTypesPanel = new MyVerticalListBoxPanel(false);
		scoreTypesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> scoreTypes = ClientGUIUtil.getSelectedItemsFromListBox(scoreTypesPanel.getListBox());
				if (scoreTypes != null) {
					final String scoreType = scoreTypes.iterator().next();
					appendTextToQueryTextBox(scoreType);
				}

			}

		});
		// SCORE NAMES PANEL
		scoreNamesPanel = new MyVerticalListBoxPanel(false);
		scoreNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> scoreNames = ClientGUIUtil.getSelectedItemsFromListBox(scoreNamesPanel.getListBox());
				if (scoreNames != null) {
					final String scoreType = scoreNames.iterator().next();
					appendTextToQueryTextBox(scoreType);
				}

			}

		});
		// THRESHOLD PANEL
		thresholdNamesPanel = new MyVerticalListBoxPanel(false);
		thresholdNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> commandNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(thresholdNamesPanel.getListBox());
				if (commandNames != null) {
					final String commandFormatting = commandNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// RATIO NAMES PANEL
		ratioNamesPanel = new MyVerticalListBoxPanel(false);
		ratioNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> ratioNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(ratioNamesPanel.getListBox());
				if (ratioNames != null) {
					final String commandFormatting = ratioNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// PROJECT LIST PANEL
		projectListPanel = new MyVerticalListBoxPanel(true);
		projectListPanel.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> selectedProjects = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox((ListBox) event.getSource());
				if (selectedProjects != null) {
					final String selectedProject = selectedProjects.iterator().next();
					appendTextToQueryTextBox(selectedProject);
				}

			}
		});
		// EXPERIMENTAL CONDITIONS PANEL
		conditionsPanel = new MyVerticalConditionsListBoxPanel(projectListPanel, true);
		conditionsPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> selectedConditionNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox((ListBox) event.getSource());
				if (selectedConditionNames != null) {
					final String conditionName = selectedConditionNames.iterator().next();
					appendTextToQueryTextBox(conditionName);
				}
			}

		});
		conditionsPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				// check the checkboxes to show the amounts and the ratios
				final boolean showProteinGroupAmounts = proteinGroupColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_AMOUNT);
				final boolean showProteinAmounts = proteinColumnNamesPanel.isSelected(ColumnName.PROTEIN_AMOUNT);
				final boolean showPeptideAmounts = peptideColumnNamesPanel.isSelected(ColumnName.PEPTIDE_AMOUNT);
				final boolean showPSMAmounts = Pint.getPSMCentric()
						&& psmColumnNamesPanel.isSelected(ColumnName.PSM_AMOUNT);

				boolean showProteinGroupSPCPercondition = false;
				boolean showProteinSPCPercondition = false;
//				if (Pint.getPSMCentric()) {
				showProteinGroupSPCPercondition = proteinGroupColumnNamesPanel.isSelected(ColumnName.SPC_PER_CONDITION);
				showProteinSPCPercondition = proteinColumnNamesPanel.isSelected(ColumnName.SPC_PER_CONDITION);
//				}
				final boolean showPeptideSPCPercondition = peptideColumnNamesPanel
						.isSelected(ColumnName.SPC_PER_CONDITION);

				final boolean showProteinGroupRatios = proteinGroupColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_RATIO);
				final boolean showProteinGroupRatioScores = proteinGroupColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_RATIO_SCORE);
				final boolean showProteinGroupRatioGraphs = proteinGroupColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_RATIO_GRAPH);

				final boolean showProteinRatios = proteinColumnNamesPanel.isSelected(ColumnName.PROTEIN_RATIO);
				final boolean showProteinRatioScores = proteinColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_RATIO_SCORE);
				final boolean showProteinRatioGraphs = proteinColumnNamesPanel
						.isSelected(ColumnName.PROTEIN_RATIO_GRAPH);

				final boolean showPeptideRatios = peptideColumnNamesPanel.isSelected(ColumnName.PEPTIDE_RATIO);
				final boolean showPeptideRatioScores = peptideColumnNamesPanel
						.isSelected(ColumnName.PEPTIDE_RATIO_SCORE);
				final boolean showPeptideRatioGraphs = peptideColumnNamesPanel
						.isSelected(ColumnName.PEPTIDE_RATIO_GRAPH);

				final boolean showPsmRatios = Pint.getPSMCentric()
						&& psmColumnNamesPanel.isSelected(ColumnName.PSM_RATIO);
				final boolean showPsmRatioScores = Pint.getPSMCentric()
						&& psmColumnNamesPanel.isSelected(ColumnName.PSM_RATIO_SCORE);
				final boolean showPsmRatioGraphs = Pint.getPSMCentric()
						&& psmColumnNamesPanel.isSelected(ColumnName.PSM_RATIO_GRAPH);

				final ListBox listbox = (ListBox) event.getSource();
				if (!(showProteinGroupAmounts || showProteinGroupRatios || showProteinAmounts || showProteinRatios
						|| showPSMAmounts || showPsmRatios || showPeptideAmounts || showPeptideRatios))
					return;
				final Set<String> selectedConditionNames = ClientGUIUtil.getSelectedItemsFromListBox(listbox);
				// show columns related to selected conditions
				if (selectedConditionNames != null && !selectedConditionNames.isEmpty()) {

					final Map<String, Set<String>> conditionNamesByProjectName = new HashMap<String, Set<String>>();
					for (final String selectedConditionName : selectedConditionNames) {
						final String conditionName = SharedDataUtil
								.parseConditionNameFromConditionSelection(selectedConditionName);
						final String projectSymbol = ClientDataUtil
								.parseProjectSymbolFromConditionSelection(selectedConditionName);
						final String projectName = ClientDataUtil.parseProjectNameFromListBox(projectSymbol,
								projectListPanel.getListBox());
						if (conditionNamesByProjectName.containsKey(projectName)) {
							conditionNamesByProjectName.get(projectName).add(conditionName);
						} else {
							final Set<String> set = new HashSet<String>();
							set.add(conditionName);
							conditionNamesByProjectName.put(projectName, set);
						}
					}
					// iterate over the map
					for (final String projectName : conditionNamesByProjectName.keySet()) {
						final Set<String> conditionNames = conditionNamesByProjectName.get(projectName);
						// PROTEIN GROUPS
						if (showProteinGroupAmounts) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
									conditionNames, projectName, true);
						}
						if (showProteinGroupRatios) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
									conditionNames, projectName, true);
						}
						if (showProteinGroupRatioGraphs) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_GRAPH,
									conditionNames, projectName, true);
						}
						if (showProteinGroupRatioScores) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_SCORE,
									conditionNames, projectName, true);
						}
						if (showProteinGroupSPCPercondition) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, true);
						}
						// PROTEINS
						if (showProteinAmounts) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
									conditionNames, projectName, true);
						}
						if (showProteinRatios) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
									conditionNames, projectName, true);
						}
						if (showProteinRatioGraphs) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_GRAPH,
									conditionNames, projectName, true);
						}
						if (showProteinRatioScores) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_SCORE,
									conditionNames, projectName, true);
						}
						if (showProteinSPCPercondition) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, true);
						}
						// PEPTIDES
						if (showPeptideAmounts) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
									conditionNames, projectName, true);
							peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
									conditionNames, projectName, true);
						}
						if (showPeptideRatios) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, true);
							peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, true);
						}
						if (showPeptideRatioGraphs) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_GRAPH,
									conditionNames, projectName, true);
							peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, true);
						}
						if (showPeptideRatioScores) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_SCORE,
									conditionNames, projectName, true);
							peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, true);
						}
						if (showPeptideSPCPercondition) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, true);
							peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, true);
						}
						// PSMs
						if (showPSMAmounts) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT, conditionNames,
									projectName, true);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
									conditionNames, projectName, true);
						}
						if (showPsmRatios) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO, conditionNames,
									projectName, true);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
									conditionNames, projectName, true);
						}
						if (showPsmRatioGraphs) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_GRAPH,
									conditionNames, projectName, true);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_GRAPH,
									conditionNames, projectName, true);
						}
						if (showPsmRatioScores) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_SCORE,
									conditionNames, projectName, true);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_SCORE,
									conditionNames, projectName, true);
						}
					}

				}

				// Hide columns related to non selected conditions
				final Set<String> nonSelectedConditionNames = ClientGUIUtil.getNonSelectedItemsFromListBox(listbox);
				if (nonSelectedConditionNames != null) {

					final Map<String, Set<String>> conditionNamesByProjectName = new HashMap<String, Set<String>>();
					for (final String nonSelectedConditionName : nonSelectedConditionNames) {
						final String conditionName = SharedDataUtil
								.parseConditionNameFromConditionSelection(nonSelectedConditionName);
						final String projectSymbol = ClientDataUtil
								.parseProjectSymbolFromConditionSelection(nonSelectedConditionName);
						final String projectName = ClientDataUtil.parseProjectNameFromListBox(projectSymbol,
								projectListPanel.getListBox());
						if (conditionNamesByProjectName.containsKey(projectName)) {
							conditionNamesByProjectName.get(projectName).add(conditionName);
						} else {
							final Set<String> set = new HashSet<String>();
							set.add(conditionName);
							conditionNamesByProjectName.put(projectName, set);
						}
					}
					// iterate over the map
					for (final String projectName : conditionNamesByProjectName.keySet()) {
						final Set<String> conditionNames = conditionNamesByProjectName.get(projectName);
						// PROTEIN GROUPS
						if (showProteinGroupAmounts) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
									conditionNames, projectName, false);
						}
						if (showProteinGroupRatios) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
									conditionNames, projectName, false);
						}
						if (showProteinGroupRatioGraphs) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_GRAPH,
									conditionNames, projectName, false);
						}
						if (showProteinGroupRatioScores) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_SCORE,
									conditionNames, projectName, false);
						}
						if (showProteinGroupSPCPercondition) {
							proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, false);
						}
						// PROTEINS
						if (showProteinAmounts) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
									conditionNames, projectName, false);
						}
						if (showProteinRatios) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
									conditionNames, projectName, false);
						}
						if (showProteinRatioGraphs) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_GRAPH,
									conditionNames, projectName, false);
						}
						if (showProteinRatioScores) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO_SCORE,
									conditionNames, projectName, false);
						}
						if (showProteinSPCPercondition) {
							proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, false);
						}
						// PEPTIDES
						if (showPeptideAmounts) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
									conditionNames, projectName, false);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
										conditionNames, projectName, false);
							}
						}
						if (showPeptideRatios) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
									conditionNames, projectName, false);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
										conditionNames, projectName, false);
							}
						}
						if (showPeptideRatioGraphs) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_GRAPH,
									conditionNames, projectName, false);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_GRAPH,
										conditionNames, projectName, false);
							}
						}
						if (showPeptideRatioScores) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_SCORE,
									conditionNames, projectName, false);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO_SCORE,
										conditionNames, projectName, false);
							}
						}
						if (showPeptideSPCPercondition) {
							peptideOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
									conditionNames, projectName, false);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.SPC_PER_CONDITION,
										conditionNames, projectName, false);
							}
						}
						// PSMs
						if (showPSMAmounts) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT, conditionNames,
									projectName, false);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
									conditionNames, projectName, false);
						}
						if (showPsmRatios) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO, conditionNames,
									projectName, false);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
									conditionNames, projectName, false);
						}
						if (showPsmRatioGraphs) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_GRAPH,
									conditionNames, projectName, false);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_GRAPH,
									conditionNames, projectName, false);
						}
						if (showPsmRatioScores) {
							psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_SCORE,
									conditionNames, projectName, false);
							psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO_SCORE,
									conditionNames, projectName, false);
						}
					}
				}
			}

		});
		// COMMANDS PANEL
		commandsPanel = new MyVerticalListBoxPanel(false);
		commandsPanel.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final Set<String> commandNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(commandsPanel.getListBox());
				if (commandNames != null) {
					final String commandFormatting = commandNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// PROTEIN GROUPING
		proteinGroupingCommandPanel = new MyVerticalProteinInferenceCommandPanel(
				createStartGroupingButtonClickHandler());

		proteinTablePanel = new ProteinTablePanel(sessionID, null, new AsyncProteinBeanListDataProvider(sessionID),
				SharedConstants.TABLE_WITH_MULTIPLE_SELECTION, this);

		proteinColumnNamesPanel = new MyVerticalCheckBoxListPanel<ProteinBeanLight>(
				proteinTablePanel.getColumnManager());

		// PSM COLUMN NAMES
		if (Pint.getPSMCentric()) {
			asyncDataProviderForPSMsOfSelectedProtein = new AsyncPSMBeanListFromPsmProvider(sessionID);
			psmTablePanel = new PSMTablePanel(sessionID, "Select one protein to load PSMs", null,
					asyncDataProviderForPSMsOfSelectedProtein, SharedConstants.TABLE_WITH_MULTIPLE_SELECTION,
					"PSM table", this);
		} else {
			// PEPTIDE COLUMN NAMES
			asyncDataProviderForPeptidesOfSelectedProtein = new AsyncPeptideBeanListFromPeptideProvider(sessionID);
			peptideTablePanel = new PeptideTablePanel(sessionID, "Select one protein to load peptides", null,
					asyncDataProviderForPeptidesOfSelectedProtein, SharedConstants.TABLE_WITH_MULTIPLE_SELECTION,
					"Peptide table", this);
		}
		layoutPanel = new LayoutPanel();
		// TODO
		if (Pint.getPSMCentric()) {
			layoutPanel.add(psmTablePanel);
			layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 50, Unit.PCT);
			psmColumnNamesPanel = new MyVerticalCheckBoxListPanel<PSMBeanLight>(psmTablePanel.getColumnManager());
		} else {
			layoutPanel.add(peptideTablePanel);
			layoutPanel.setWidgetBottomHeight(peptideTablePanel, 0, Unit.PCT, 50, Unit.PCT);
			peptideColumnNamesPanel = new MyVerticalCheckBoxListPanel<PeptideBeanLight>(
					peptideTablePanel.getColumnManager());
			psmColumnNamesPanel = null;
		}
		// PROTEIN GROUP PANEL

		final Label emptyWidget = new Label("Click on 'Group Proteins' button under 'Protein inference' left menu");
		emptyWidget.addMouseOverHandler(

				getSelectProteinGroupingMenuHandler());
		proteinGroupTablePanel = new ProteinGroupTablePanel(sessionID, emptyWidget,
				new AsyncProteinGroupBeanListDataProvider(sessionID), false, this);

		proteinGroupColumnNamesPanel = new MyVerticalCheckBoxListPanel<ProteinGroupBeanLight>(
				proteinGroupTablePanel.getColumnManager());

		// PSM ONLY TAB
		if (Pint.getPSMCentric()) {
			psmOnlyTablePanel = new PSMTablePanel(sessionID, null, this, new AsyncPSMBeanListDataProvider(sessionID),
					SharedConstants.TABLE_WITH_MULTIPLE_SELECTION, "PSM ONLY table", this);
			psmColumnNamesPanel.addColumnManager(psmOnlyTablePanel.getColumnManager());
		}

		// PEPTIDE ONLY TAB
		peptideOnlyTablePanel = new PeptideTablePanel(sessionID, null, this,
				new AsyncPeptideBeanListDataProvider(sessionID), SharedConstants.TABLE_WITH_MULTIPLE_SELECTION, this);
		if (Pint.getPSMCentric()) {
			peptideColumnNamesPanel = new MyVerticalCheckBoxListPanel<PeptideBeanLight>(
					peptideOnlyTablePanel.getColumnManager());
		}
		peptideColumnNamesPanel.addColumnManager(peptideOnlyTablePanel.getColumnManager());
		// DATA PANEL: this panel will contain the protein/protein groups in the
		// top half and the PSMs in the botton half

		// create a handler in order to retrieve the PSMs from a selected
		// peptide and add them to the psm table
		if (Pint.getPSMCentric()) {
			peptideOnlyTablePanel.addSelectionHandler(createPeptideSelectionHandler());
		}
		// create a handler in order to retrieve the PSMs from a selected
		// protein and add them to the psm table
		proteinTablePanel.addSelectionHandler(createProteinSelectionHandler());

		// create a handler in order to retrieve the PSMs from a selected
		// protein and add them to the psm table
		proteinGroupTablePanel.addSelectionHandler(createProteinGroupSelectionHandler());

		// two tabs, one for the protein and the other for the protein groups
		if (Pint.getPSMCentric()) {
			secondLevelTabPanel = new ScrolledTabLayoutPanel(psmOnlyTablePanel, null);
		} else {
			secondLevelTabPanel = new ScrolledTabLayoutPanel(null, peptideOnlyTablePanel);
		}
		secondLevelTabPanel.add(proteinGroupTablePanel, "Protein Groups");
		secondLevelTabPanel.add(proteinTablePanel, "Proteins");
		secondLevelTabPanel.add(peptideOnlyTablePanel, "Peptides");
		if (Pint.getPSMCentric()) {
			secondLevelTabPanel.add(psmOnlyTablePanel, "PSMs");
		}
		secondLevelTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				final Integer item = event.getSelectedItem();
				if (item != null) {
					if (item == 0) {
						// proteinGroupTablePanel.reloadData();
						proteinGroupTablePanel.refreshData();
					} else if (item == 1) {
						// proteinTablePanel.reloadData();
						proteinTablePanel.refreshData();
					} else if (item == 2) {
						// peptideTablePanel.reloadData();
						peptideOnlyTablePanel.refreshData();
					} else if (item == 3 && Pint.getPSMCentric()) {
						// psmOnlyTablePanel.reloadData();
						psmOnlyTablePanel.refreshData();
					}
				}
			}

		});
		layoutPanel.add(secondLevelTabPanel);
		layoutPanel.setWidgetTopHeight(secondLevelTabPanel, 0, Unit.PCT, 50, Unit.PCT);

		// Widgets of the tabs

		// first tab
		final ClickHandler sendQueryclickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				String queryText = queryEditorPanel.getComplexQueryTextBox().getText();
				queryText = queryText.replace("\n", " ");
				sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion(), testMode);

			}
		};
		final ClickHandler sendSimpleQueryByProteinNameClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String queryText = queryEditorPanel.getTranslatedQueryFromProteinName();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion(), testMode);
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		final ClickHandler sendSimpleQueryByAccClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String queryText = queryEditorPanel.getTranslatedQueryFromAcc();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion(), testMode);
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		final ClickHandler sendSimpleQueryByGeneNameClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				final String queryText = queryEditorPanel.getTranslatedQueryFromGeneName();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion(), testMode);
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		queryEditorPanel = new MyQueryEditorPanel(sendQueryclickHandler, sendSimpleQueryByProteinNameClickHandler,
				sendSimpleQueryByAccClickHandler, sendSimpleQueryByGeneNameClickHandler);

		projectInformationPanel = new ProjectInformationPanel(this, testMode);
		scrollProjectInformationPanel = new ScrollPanel(projectInformationPanel);
		firstLevelTabPanel.add(scrollProjectInformationPanel, "Project information");
		scrollQueryPanel = new ScrollPanel(queryEditorPanel);
		firstLevelTabPanel.add(layoutPanel, "Dataset view");
		firstLevelTabPanel.add(scrollQueryPanel, "Query");

		menuUpStackLayoutPanel.add(commandsPanel, "Filters / Query commands", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(annotationsTypePanel, "Annotation Types", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(uniprotHeaderLinesPanel, "Uniprot Header Line", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(scoreTypesPanel, "Score Types", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(scoreNamesPanel, "Score Names", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(ratioNamesPanel, "Ratio Names", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(thresholdNamesPanel, "Custom Thresholds", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(projectListPanel, "Projects", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(conditionsPanel, "Conditions", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(proteinGroupingCommandPanel, "Protein inference", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(new ScrollPanel(proteinGroupColumnNamesPanel), "Protein group columns",
				HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(new ScrollPanel(proteinColumnNamesPanel), "Protein columns", HEADER_HEIGHT);
		menuUpStackLayoutPanel.add(new ScrollPanel(peptideColumnNamesPanel), "Peptide columns", HEADER_HEIGHT);
		if (Pint.getPSMCentric()) {
			menuUpStackLayoutPanel.add(new ScrollPanel(psmColumnNamesPanel), "PSM columns", HEADER_HEIGHT);
		}
		initStackPanel();

		// hiddeLoadingDialog();
		setStyleName("MainPanel");

		// select project tab
		changeToProjectInfoTab();

	}

	private MouseOverHandler getSelectProteinGroupingMenuHandler() {
		final MouseOverHandler ret = new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				menuUpStackLayoutPanel.showWidget(proteinGroupingCommandPanel);
			}
		};
		return ret;
	}

	/**
	 *
	 * Loads several things from the server: annotation types, header lines,
	 * commands...
	 */
	private void initStackPanel() {

		// load annotation types
		loadAnnotationTypesFromServer();

		// load uniprot header lines from server
		loadUniprotHeaderLinesFromServer();

		// load commands
		loadCommands();

	}

	protected void appendTextToQueryTextBox(String newText) {

		final TextBoxBase suggestBoxQuery = queryEditorPanel.getComplexQueryTextBox();
		final int cursorPos = suggestBoxQuery.getCursorPos();

		update(suggestBoxQuery, newText, true, false, false, cursorPos, suggestBoxQuery.getSelectedText());

		// show the query tab
		final Widget widget = firstLevelTabPanel.getWidget(firstLevelTabPanel.getSelectedIndex());
		if (!widget.equals(queryEditorPanel)) {
			changeToQueryTab();
		}
		// proteinTablePanel.getDataGrid().setFocus(true);
		// proteinTablePanel.getDataGrid().redraw();
	}

	private void changeToQueryTab() {
		firstLevelTabPanel.selectTab(scrollQueryPanel);
	}

	private void changeToProjectInfoTab() {
		firstLevelTabPanel.selectTab(scrollProjectInformationPanel);
	}

	private ClickHandler createStartGroupingButtonClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				proteinGroupTablePanel.clearTable();

				final String logMessage = TaskType.GROUP_PROTEINS.getSingleTaskMessage("");
				updateStatus(logMessage);

				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						GWT.log("Sending proteins to group");
						// add PendingTask
						final Task task = PendingTasksManager.addPendingTask(new GroupProteinsTask(loadedProjects));
						proteinGroupTablePanel.clearTable();
						proteinRetrievingService.groupProteins(sessionID,
								proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(),
								proteinGroupTablePanel.getDataGrid().getPageSize(), task,
								new AsyncCallback<ProteinGroupBeanSubList>() {

									@Override
									public void onFailure(Throwable caught) {
										updateStatus(caught);
										// remove PendingTask
										PendingTasksManager.removeTask(task);
									}

									@Override
									public void onSuccess(ProteinGroupBeanSubList proteinGroupSubList) {
										if (proteinGroupSubList != null) {
											updateStatus("Proteins grouped into " + proteinGroupSubList.getTotalNumber()
													+ " groups");
											loadProteinGroupsOnGrid(0, proteinGroupSubList);
										}
										// remove PendingTask
										PendingTasksManager.removeTask(task);
									}

								});

					}
				});

				// }
				// });

			}
		};
	}

	private void loadProteinGroupsOnGrid(int start, final ProteinGroupBeanSubList result) {
		if (result == null || result.isEmpty()) {
			proteinGroupTablePanel.clearTable();
		} else {
			// fill the grid
			proteinGroupTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(), true);
			proteinGroupTablePanel.getAsyncDataProvider().updateRowData(start, result.getDataList());
			// proteinGroupTablePanel.reloadData();
			proteinGroupTablePanel.refreshData();
		}
	}

	private Handler createProteinSelectionHandler() {
		final SelectionChangeEvent.Handler handler = new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				GWT.log("Selection event in protein table");
				// psmTablePanel.clearTable();
				if (Pint.getPSMCentric()) {
					psmTablePanel.setLoadingWidget();
				} else {

					peptideTablePanel.setEmptyTableWidget(peptideTablePanel.getLoadingWidget());
					peptideTablePanel.clearTable();
					peptideTablePanel.refreshData();
				}
				final Object source = event.getSource();

				if (source instanceof SingleSelectionModel) {
					GWT.log("protein table with a single selection model");

					final SingleSelectionModel<ProteinBeanLight> selectionModel = (SingleSelectionModel<ProteinBeanLight>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						final ProteinBeanLight selectedProtein = (ProteinBeanLight) selectedObject;
						if (Pint.getPSMCentric()) {
							asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedProtein);
							// psmTablePanel.reloadData();
							psmTablePanel.refreshData();
						} else {
							asyncDataProviderForPeptidesOfSelectedProtein.setPeptideProvider(selectedProtein);
							peptideTablePanel.refreshData();
						}
					}

				}
			}

		};
		return handler;
	}

	private Handler createPeptideSelectionHandler() {
		final SelectionChangeEvent.Handler handler = new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				GWT.log("Selection event in peptide table");
				// psmTablePanel.clearTable();
				psmTablePanel.setLoadingWidget();

				final Object source = event.getSource();

				if (source instanceof SingleSelectionModel) {
					GWT.log("peptide table with a single selection model");

					final SingleSelectionModel<PeptideBeanLight> selectionModel = (SingleSelectionModel<PeptideBeanLight>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						final PeptideBeanLight selectedPeptide = (PeptideBeanLight) selectedObject;
						asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedPeptide);
						// psmTablePanel.reloadData();
						psmTablePanel.refreshData();
					}
					final Widget widget = null;
					psmTablePanel.setEmptyTableWidget(widget);

				}
			}

		};
		return handler;
	}

	private Handler createProteinGroupSelectionHandler() {
		final SelectionChangeEvent.Handler handler = new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				// psmTablePanel.clearTable();
				final Object source = event.getSource();
				if (Pint.getPSMCentric()) {
					psmTablePanel.setLoadingWidget();
				} else {
//					peptideTablePanel.setLoadingWidget();
					peptideTablePanel.setEmptyTableWidget(peptideTablePanel.getLoadingWidget());

					peptideTablePanel.clearTable();
				}
				if (source instanceof SingleSelectionModel) {
					final SingleSelectionModel<ProteinGroupBeanLight> selectionModel = (SingleSelectionModel<ProteinGroupBeanLight>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						final ProteinGroupBeanLight selectedProteinGroup = (ProteinGroupBeanLight) selectedObject;
						if (Pint.getPSMCentric()) {
							asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedProteinGroup);
							psmTablePanel.refreshData();
						} else {
							asyncDataProviderForPeptidesOfSelectedProtein.setPeptideProvider(selectedProteinGroup);
							peptideTablePanel.refreshData();
						}

					}

				}

			}

		};
		return handler;
	}

	protected void updateStatus(Throwable caught) {
		caught.printStackTrace();
		updateStatus("ERROR: " + caught.getMessage());
	}

	protected void updateStatus(String message) {
		GWT.log(message);
		update(textAreaStatus, DateTimeFormat.getFormat("h:mm:ss a").format(new Date()) + ": " + message, true, true,
				true, null, null);
	}

	private void update(HasText textElement, String message, boolean append, boolean newline, boolean beginning,
			Integer cursorPos, String selectedText) {

		if (!append) {
			textElement.setText(message);
		} else {
			if (cursorPos == null) {
				String newText = textElement.getText();
				if (beginning) {
					if (!newline) {
						if (!newText.startsWith(" ")) {
							newText = " " + newText;
						}
					} else {
						// just a new line if newText !=""
						if (!"".equals(newText))
							newText = "\n" + newText;
					}
					newText = message + newText;
				} else {
					if (!newline) {
						if (!newText.endsWith(" ")) {
							newText += " ";
						}
					} else {
						// just a new line if newText !=""
						if (!"".equals(newText))
							newText += "\n";
					}
					newText += message;
				}
				textElement.setText(newText);
			} else {
				textElement
						.setText(insertTextInCursorPosition(textElement.getText(), message, cursorPos, selectedText));
			}
		}

	}

	private String insertTextInCursorPosition(String oldText, String toInsert, int cursorPos, String selectedText) {
		if ("".equals(oldText))
			return toInsert;
		if ("".equals(selectedText)) {
			final String left = oldText.substring(0, cursorPos);
			final String rigth = oldText.substring(cursorPos, oldText.length());
			return left + toInsert + rigth;
		} else {
			final String left = oldText.substring(0, cursorPos);
			final String rigth = oldText.substring(cursorPos + selectedText.length(), oldText.length());
			return left + toInsert + rigth;
		}
	}

	private void prepareDownloadLinksForQuery() {
		queryEditorPanel.setSendingStatusText("Preparing download link...");
		proteinRetrievingService.getDownloadLinkForProteinsFromQuery(sessionID,
				queryEditorPanel.getComplexQueryTextBox().getText(), loadedProjects,
				new AsyncCallback<FileDescriptor>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
						queryEditorPanel.setLinksToResultsVisible(false);
					}

					@Override
					public void onSuccess(FileDescriptor result) {
						setDownloadProteinLink(result);
					}
				});
		proteinRetrievingService.getDownloadLinkForProteinGroupsFromQuery(sessionID,
				queryEditorPanel.getComplexQueryTextBox().getText(), loadedProjects,
				proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), new AsyncCallback<FileDescriptor>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
						queryEditorPanel.setLinksToResultsVisible(false);
					}

					@Override
					public void onSuccess(FileDescriptor result) {
						setDownloadProteinGroupLink(result);
					}
				});
	}

	private void prepareDownloadLinksForProject() {
		proteinRetrievingService.getDownloadLinkForProteinsInProject(loadedProjects,
				new AsyncCallback<FileDescriptor>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
						queryEditorPanel.setLinksToResultsVisible(false);
					}

					@Override
					public void onSuccess(FileDescriptor result) {
						setDownloadProteinLink(result);

					}
				});
		proteinRetrievingService.getDownloadLinkForProteinGroupsInProject(loadedProjects,
				proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), new AsyncCallback<FileDescriptor>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
						queryEditorPanel.setLinksToResultsVisible(false);
					}

					@Override
					public void onSuccess(FileDescriptor result) {
						setDownloadProteinGroupLink(result);
					}
				});
	}

	protected void setDownloadProteinGroupLink(FileDescriptor result) {
		if (result != null) {
			final String fileName = SafeHtmlUtils.htmlEscape(result.getName());
			final String fileSizeString = result.getSize();
			queryEditorPanel.setSendingStatusText(null);
			// final String href = GWT.getModuleBaseURL() +
			// "download?" + SharedConstants.FILE_TO_DOWNLOAD
			// + "=" + fileName + "&" +
			// SharedConstants.FILE_TYPE + "="
			// + SharedConstants.ID_DATA_FILE_TYPE;
			final String href = ClientSafeHtmlUtils.getDownloadURL(fileName, SharedConstants.ID_DATA_FILE_TYPE);
			queryEditorPanel.setLinksToResultsVisible(true);
			queryEditorPanel.setLinksToProteinGroupResults(href, "Protein groups [" + fileSizeString + "]");
		}
	}

	protected void setDownloadProteinLink(FileDescriptor result) {
		if (result != null) {
			final String fileName = SafeHtmlUtils.htmlEscape(result.getName());
			final String fileSizeString = result.getSize();
			queryEditorPanel.setSendingStatusText(null);
			final String href = ClientSafeHtmlUtils.getDownloadURL(fileName, SharedConstants.ID_DATA_FILE_TYPE);
			queryEditorPanel.setLinksToResultsVisible(true);
			queryEditorPanel.setLinksToProteinResults(href, "Proteins [" + fileSizeString + "]");
		}
	}

	protected void requestUniprotVersionsFromProjects(Set<String> selectedProjects) {
		if (selectedProjects == null || selectedProjects.isEmpty()) {
			queryEditorPanel.clearUniprotVersionList();
		} else {

			proteinRetrievingService.getUniprotAnnotationsFromProjects(selectedProjects,
					new AsyncCallback<Set<String>>() {
						@Override
						public void onFailure(Throwable caught) {
							updateStatus(caught);
						}

						@Override
						public void onSuccess(Set<String> result) {
							updateStatus(
									result.size() + " uniprot versions available associated with selected projects");

							queryEditorPanel.clearUniprotVersionList();

							// sort from latest to oldest, so in reverse
							// alphabetic order
							final List<String> list = new ArrayList<String>();
							if (!result.isEmpty())
								list.addAll(result);
							Collections.sort(list, new Comparator<String>() {
								@Override
								public int compare(String o1, String o2) {
									return o2.compareTo(o1);
								}
							});
							for (final String uniprotVersion : list) {
								queryEditorPanel.addUniprotVersion(uniprotVersion);
							}
						}
					});

		}

	}

	protected Set<String> getNotCachedProjects(Set<String> selectedProjects) {
		final Set<String> ret = new HashSet<String>();
		for (final String selectedProject : selectedProjects) {
			if (!loadedProjects.contains(selectedProject)) {
				ret.add(selectedProject);
			}
		}

		return ret;
	}

	protected void sendQueryToServer(final String queryText, String uniprotVersion, final boolean testMode) {

		// set empty table widget on PSM table
		if (Pint.getPSMCentric()) {
			psmTablePanel.setEmptyTableWidget(PSMTablePanel.SELECT_PROTEIN_TO_LOAD_PSMS_TEXT);
		} else {
			peptideTablePanel.setEmptyTableWidget(PeptideTablePanel.SELECT_PEPTIDE_TO_LOAD_PSMS_TEXT);
		}
		// proteinGroupTablePanel.clearTable();
		// proteinTablePanel.clearTable();
		// psmTablePanel.clearTable();
		// psmOnlyTablePanel.clearTable();
		// peptideTablePanel.clearTable();
		if ("".equals(queryText)) {
			updateStatus("Empty query. Loading whole project...");
			loadProteinsFromProject(uniprotVersion, null, null, //
					false // showProjectWindow
					, testMode);
			return;
		}

		final String logMessage = "Waiting for query results...";
		proteinGroupTablePanel.setEmptyTableWidget(logMessage);
		proteinTablePanel.setEmptyTableWidget(logMessage);
		if (Pint.getPSMCentric()) {
			psmOnlyTablePanel.setEmptyTableWidget(logMessage);
			psmTablePanel.setEmptyTableWidget(logMessage);
		} else {
			peptideTablePanel.setEmptyTableWidget(logMessage);
		}
		peptideOnlyTablePanel.setEmptyTableWidget(logMessage);

		queryEditorPanel.setSendingStatusText(logMessage);
		queryEditorPanel.setLinksToResultsVisible(false);
		queryEditorPanel.updateQueryResultSummary(null);
		// add pending task
		final Task task = PendingTasksManager.addPendingTask(new GetProteinsFromQuery(loadedProjects, queryText));
		// set status
		updateStatus("Sending query '" + queryText + "' to server...");
		// send query to server
		proteinRetrievingService.getProteinsFromQuery(sessionID, queryText, loadedProjects, uniprotVersion,
				proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), true, testMode, false, false, task,
				new AsyncCallback<QueryResultSubLists>() {

					@Override
					public void onFailure(Throwable caught) {
						try {
							if (caught instanceof PintException) {
								GWT.log(caught.getMessage());
							}
							updateStatus(caught);
							queryEditorPanel.setSendingStatusText(null);
						} finally {
							// remove pending task
							PendingTasksManager.removeTask(task);
						}
					}

					@Override
					public void onSuccess(QueryResultSubLists result) {
						try {
							final String nullString = null;
							queryEditorPanel.updateQueryResultSummary(result);
							queryEditorPanel.setSendingStatusText(null); // set null to come back to default
							proteinGroupTablePanel.setEmptyTableWidget(nullString);
							proteinTablePanel.setEmptyTableWidget(nullString);
							if (Pint.getPSMCentric()) {
								psmOnlyTablePanel.setEmptyTableWidget(nullString);
								psmTablePanel.setEmptyTableWidget(PSMTablePanel.SELECT_PROTEIN_TO_LOAD_PSMS_TEXT);
							} else {
								peptideTablePanel
										.setEmptyTableWidget(PeptideTablePanel.SELECT_PEPTIDE_TO_LOAD_PSMS_TEXT);
							}
							peptideOnlyTablePanel.setEmptyTableWidget(nullString);
							if (Pint.getPSMCentric()) {
								updateStatus(result.getNumTotalPSMs() + " psms received.");
							}
//							updateStatus(result.getNumDifferentSequences() + " peptides received.");
							updateStatus(result.getNumTotalProteins() + " proteins received.");
							updateStatus(result.getNumTotalProteinGroups() + " protein groups received.");

							if (result.getNumTotalProteins() > 0) {
								prepareDownloadLinksForQuery();
							}

							// if (!defaultViewsApplied) {
							requestDefaultViews(loadedProjectBeanSet.iterator().next(), true, // modify columns
									false, // showMaxNumberMaxNumberOfProjectAtOnceMessage
									true, // changeToDataTab
									loadedProjectBeanSet.containsBigProject(), //
									testMode, //
									false // showWelcomeProjectWindow
							);
							proteinGroupTablePanel.reloadData();
							proteinTablePanel.reloadData();

							if (Pint.getPSMCentric()) {
								asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(null);
								psmOnlyTablePanel.reloadData();
								psmTablePanel.reloadData();
							} else {
								asyncDataProviderForPeptidesOfSelectedProtein.setPeptideProvider(null);
								peptideTablePanel.reloadData();
							}
							peptideOnlyTablePanel.reloadData();

							// }
						} finally {
							// remove pending task
							PendingTasksManager.removeTask(task);
						}
					}
				});

	}

	private void loadConditionsFromProjects(final Set<String> selectedProjects) {
		conditionsPanel.clearList();
		if (selectedProjects != null && !selectedProjects.isEmpty()) {
			final Set<String> notCachedProject = new HashSet<String>();
			for (final String selectedProject : selectedProjects) {
				if (ClientCacheConditionsByProject.getInstance().contains(selectedProject)) {
					final Set<String> conditionsFromProject = ClientCacheConditionsByProject.getInstance()
							.getFromCache(selectedProject);
					final List<String> sortedList = new ArrayList<String>();
					sortedList.addAll(conditionsFromProject);
					Collections.sort(sortedList);

					for (final String conditionName : sortedList) {
						final String newElementName = ClientDataUtil.getNewElementNameForCondition(conditionName,
								selectedProject, conditionsPanel.getListBox(), projectListPanel.getListBox());
						conditionsPanel.addItem(newElementName, conditionName);
					}
				} else {
					notCachedProject.add(selectedProject);
				}
			}
			proteinGroupTablePanel.removeColumn(ColumnName.PROTEIN_AMOUNT);
			proteinTablePanel.removeColumn(ColumnName.PROTEIN_AMOUNT);
			proteinGroupTablePanel.removeColumn(ColumnName.SPC_PER_CONDITION);
			proteinTablePanel.removeColumn(ColumnName.SPC_PER_CONDITION);
			if (Pint.getPSMCentric()) {
				psmTablePanel.removeColumn(ColumnName.PSM_AMOUNT);
				psmOnlyTablePanel.removeColumn(ColumnName.PSM_AMOUNT);

			} else {
				peptideTablePanel.removeColumn(ColumnName.PEPTIDE_AMOUNT);
				peptideTablePanel.removeColumn(ColumnName.SPC_PER_CONDITION);
			}
			peptideOnlyTablePanel.removeColumn(ColumnName.PEPTIDE_AMOUNT);
			peptideOnlyTablePanel.removeColumn(ColumnName.SPC_PER_CONDITION);
			if (!notCachedProject.isEmpty()) {
				for (final String projectTag : notCachedProject) {

					proteinRetrievingService.getExperimentalConditionsFromProject(projectTag,
							new AsyncCallback<Set<String>>() {
								@Override
								public void onFailure(Throwable caught) {
									updateStatus(caught);
								}

								@Override
								public void onSuccess(Set<String> result) {

									ClientCacheConditionsByProject.getInstance().addtoCache(result, projectTag);
									// add the conditions to the
									// "Condition" list panel

									// sort alphabetically
									final List<String> sortedConditionNames = new ArrayList<String>();
									sortedConditionNames.addAll(result);
									java.util.Collections.sort(sortedConditionNames);

									for (final String conditionName : sortedConditionNames) {
										final String newElementName = ClientDataUtil.getNewElementNameForCondition(
												conditionName, projectTag, conditionsPanel.getListBox(),
												projectListPanel.getListBox());

										// add to the experimeental
										// condition panel
										String conditionSymbol = ClientDataUtil
												.parseConditionSymbolFromConditionSelection(newElementName);
										final String projectSymbol = ClientDataUtil
												.parseProjectSymbolFromConditionSelection(newElementName);
										conditionSymbol = projectSymbol + conditionSymbol;
										conditionsPanel.addItem(newElementName, conditionName);

										addAmountColumns(projectTag, conditionName, conditionSymbol);

									}
									updateStatus(result.size()
											+ " experimental conditions added on 'Conditions' panel from '" + projectTag
											+ "' project.");

								}

							});
				}

			}
		}
	}

	private void addAmountColumns(final String projectTag, final String conditionName, final String conditionSymbol) {
		if (Pint.getPSMCentric()) {
			addPSMAmountColumns(projectTag, conditionName, conditionSymbol);
		}
		addPeptideAmountColumns(projectTag, conditionName, conditionSymbol);
		addProteinAmountColumns(projectTag, conditionName, conditionSymbol);
		addSPCByCondition(projectTag, conditionName, conditionSymbol);
	}

	private void addSPCByCondition(String projectTag, String conditionName, String conditionSymbol) {

		final String checkBoxName = MyVerticalCheckBoxListPanel.getCheckBoxNameForAmount(AmountType.SPC,
				conditionSymbol);
		// to peptides
		final ColumnWithVisibility peptideColumn = PeptideColumns.getInstance().getColumn(ColumnName.SPC_PER_CONDITION);
		peptideOnlyTablePanel.addColumnForConditionAmount(ColumnName.SPC_PER_CONDITION, peptideColumn.isVisible(),
				conditionName, conditionSymbol, AmountType.SPC, projectTag);
		peptideTablePanel.addColumnForConditionAmount(ColumnName.SPC_PER_CONDITION, peptideColumn.isVisible(),
				conditionName, conditionSymbol, AmountType.SPC, projectTag);
		peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.SPC_PER_CONDITION, checkBoxName,
				MyVerticalCheckBoxListPanel.getKeyName(ColumnName.SPC_PER_CONDITION, conditionName, conditionSymbol,
						AmountType.SPC.name(), projectTag));
		// to proteins
//		if (Pint.getPSMCentric()) {
		final ColumnWithVisibility proteinColumn = ProteinColumns.getInstance().getColumn(ColumnName.SPC_PER_CONDITION);
		proteinTablePanel.addColumnForConditionAmount(ColumnName.SPC_PER_CONDITION, proteinColumn.isVisible(),
				conditionName, conditionSymbol, AmountType.SPC, projectTag);
		proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.SPC_PER_CONDITION, checkBoxName,
				MyVerticalCheckBoxListPanel.getKeyName(ColumnName.SPC_PER_CONDITION, conditionName, conditionSymbol,
						AmountType.SPC.name(), projectTag));
		// o protein groups
		final ColumnWithVisibility proteinGroupColumn = ProteinGroupColumns.getInstance()
				.getColumn(ColumnName.SPC_PER_CONDITION);
		proteinGroupTablePanel.addColumnForConditionAmount(ColumnName.SPC_PER_CONDITION, proteinGroupColumn.isVisible(),
				conditionName, conditionSymbol, AmountType.SPC, projectTag);
		proteinGroupColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.SPC_PER_CONDITION, checkBoxName,
				MyVerticalCheckBoxListPanel.getKeyName(ColumnName.SPC_PER_CONDITION, conditionName, conditionSymbol,
						AmountType.SPC.name(), projectTag));
//		}
	}

	private void addPSMAmountColumns(final String projectTag, final String conditionName,
			final String conditionSymbol) {

		// add PSM amounts
		proteinRetrievingService.getPSMAmountTypesByCondition(sessionID, projectTag, conditionName,
				new AsyncCallback<Set<AmountType>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Set<AmountType> result) {
						if (result != null) {
							for (final AmountType amountType : result) {
								final String checkBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForAmount(amountType, conditionSymbol);
								final ColumnWithVisibility psmAmountColumn = PSMColumns.getInstance()
										.getColumn(ColumnName.PSM_AMOUNT);
								psmTablePanel.addColumnForConditionAmount(ColumnName.PSM_AMOUNT,
										psmAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								psmOnlyTablePanel.addColumnForConditionAmount(ColumnName.PSM_AMOUNT,
										psmAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								// add checkbox for PSMAmount of that type
								psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PSM_AMOUNT, checkBoxName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PSM_AMOUNT, conditionName,
												conditionSymbol, amountType.name(), projectTag));

								if (!result.isEmpty()) {

									// add handlers related to that
									// condition, for the psm and
									// the protein tables
									// psmColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(ColumnName.PSM_AMOUNT,
									// checkBoxName, conditionName, projectTag,
									// conditionsPanel);
								}
							}
						}
					}
				});

	}

	private void addPeptideAmountColumns(final String projectTag, final String conditionName,
			final String conditionSymbol) {

		// add PSM amounts
		proteinRetrievingService.getPeptideAmountTypesByCondition(sessionID, projectTag, conditionName,
				new AsyncCallback<Set<AmountType>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Set<AmountType> result) {
						if (result != null) {
							for (final AmountType amountType : result) {
								final String checkBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForAmount(amountType, conditionSymbol);
								final ColumnWithVisibility peptideAmountColumn = PeptideColumns.getInstance()
										.getColumn(ColumnName.PEPTIDE_AMOUNT);
								peptideOnlyTablePanel.addColumnForConditionAmount(ColumnName.PEPTIDE_AMOUNT,
										peptideAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								peptideTablePanel.addColumnForConditionAmount(ColumnName.PEPTIDE_AMOUNT,
										peptideAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								// add checkbox for PeptideAmount of that type
								peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PEPTIDE_AMOUNT,
										checkBoxName, MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_AMOUNT,
												conditionName, conditionSymbol, amountType.name(), projectTag));

								if (!result.isEmpty()) {
									// add handlers related to that
									// condition, for the psm and
									// the protein tables

									// peptideColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
									// ColumnName.PEPTIDE_AMOUNT, checkBoxName,
									// conditionName, projectTag,
									// conditionsPanel);

								}
							}
						}
					}
				});

	}

	private void addProteinAmountColumns(final String projectTag, final String conditionName,
			final String conditionSymbol) {

		// add PSM amounts
		proteinRetrievingService.getProteinAmountTypesByCondition(sessionID, projectTag, conditionName,
				new AsyncCallback<Set<AmountType>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Set<AmountType> result) {
						if (result != null) {
							for (final AmountType amountType : result) {
								final ColumnWithVisibility proteinAmountColumn = ProteinColumns.getInstance()
										.getColumn(ColumnName.PROTEIN_AMOUNT);
								proteinTablePanel.addColumnForConditionAmount(ColumnName.PROTEIN_AMOUNT,
										proteinAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								proteinGroupTablePanel.addColumnForConditionAmount(ColumnName.PROTEIN_AMOUNT,
										proteinAmountColumn.isVisible(), conditionName, conditionSymbol, amountType,
										projectTag);
								// add checkbox for ProteinAmount of that type
								final String checkBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForAmount(amountType, conditionSymbol);
								proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_AMOUNT,
										checkBoxName, MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_AMOUNT,
												conditionName, conditionSymbol, amountType.name(), projectTag));
								proteinGroupColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_AMOUNT,
										checkBoxName, MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_AMOUNT,
												conditionName, conditionSymbol, amountType.name(), projectTag));

								if (!result.isEmpty()) {// add handlers related
														// to
														// that
									// condition, for the psm and
									// the protein tables
									// proteinColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
									// ColumnName.PROTEIN_AMOUNT, checkBoxName,
									// conditionName, projectTag,
									// conditionsPanel);
									// proteinGroupColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
									// ColumnName.PROTEIN_AMOUNT, conditionName,
									// checkBoxName, projectTag,
									// conditionsPanel);
								}
							}
						}
					}
				});

	}

	private void addProteinScoreNames(List<String> result) {
		if (result != null && !result.isEmpty()) {
			for (final String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				proteinTablePanel.addColumnForScore(scoreName, ColumnName.PROTEIN_SCORE);
				proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_SCORE, scoreName,
						MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_SCORE, scoreName));
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " protein scores loaded.");
		}
	}

	private void addScoreTypes(List<String> result) {
		if (result != null && !result.isEmpty()) {
			for (final String scoreType : result) {
				scoreTypesPanel.getListBox().addItem(scoreType);
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " score types loaded.");
		}
	}

	private void addPeptideScoreNames(List<String> result) {
		if (result != null && !result.isEmpty()) {
			for (final String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				peptideOnlyTablePanel.addColumnForScore(scoreName, ColumnName.PEPTIDE_SCORE);
				peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PEPTIDE_SCORE, scoreName,
						MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_SCORE, scoreName));
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " peptide scores loaded.");
		}
	}

	private void addPSMScoreNames(List<String> result) {
		if (result != null && !result.isEmpty()) {
			for (final String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				if (Pint.getPSMCentric()) {
					psmTablePanel.addColumnForScore(scoreName, ColumnName.PSM_SCORE);
					psmOnlyTablePanel.addColumnForScore(scoreName, ColumnName.PSM_SCORE);
					psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PSM_SCORE, scoreName,
							MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PSM_SCORE, scoreName));
				}
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " PSM scores loaded.");
		}
	}

	private void addPTMScoreNames(List<String> result) {
		if (result != null && !result.isEmpty()) {
			for (final String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				if (Pint.getPSMCentric()) {
					psmTablePanel.addColumnForScore(scoreName, ColumnName.PTM_SCORE);
					psmOnlyTablePanel.addColumnForScore(scoreName, ColumnName.PTM_SCORE);
					psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PTM_SCORE, scoreName,
							MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PTM_SCORE, scoreName));
				}
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " PTM scores loaded.");
		}
	}

	private void loadThresholdNamesFromServer(Set<String> selectedProjects) {
		thresholdNamesPanel.clearList();
		proteinRetrievingService.getThresholdNamesFromProjects(selectedProjects, new AsyncCallback<Set<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(Set<String> result) {

				if (!result.isEmpty()) {
					final List<String> list = new ArrayList<String>();
					list.addAll(result);
					Collections.sort(list);
					for (final String thresholdNameType : list) {
						thresholdNamesPanel.getListBox().addItem(thresholdNameType);
					}
					// add to suggestions in the query editor
					queryEditorPanel.addSuggestionsToComplexQuery(result);
					updateStatus(result.size() + " threshold names loaded.");
				}
			}
		});

	}

	private void loadUniprotHeaderLinesFromServer() {
		proteinRetrievingService.getUniprotHeaderLines(new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				if (!result.isEmpty()) {
					final Set<String> keySet = result.keySet();
					final List<String> ordererList = new ArrayList<String>();
					ordererList.addAll(keySet);
					Collections.sort(ordererList);
					for (final String uniprotHeaderKey : ordererList) {
						uniprotHeaderLinesPanel.getListBox().addItem(
								uniprotHeaderKey + " (" + result.get(uniprotHeaderKey) + ")", uniprotHeaderKey);
					}
					updateStatus(result.size() + " uniprot header lines loaded.");
				}
			}
		});

	}

	private void loadAnnotationTypesFromServer() {
		proteinRetrievingService.getAnnotationTypes(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				if (!result.isEmpty()) {
					for (final String projectName : result) {
						annotationsTypePanel.getListBox().addItem(projectName);
					}
					// add to suggestions in the query editor
					queryEditorPanel.addSuggestionsToComplexQuery(result);
					updateStatus(result.size() + " annotations loaded.");
				}
			}
		});

	}

	/**
	 * Remove the information of the older loaded projects and load the information
	 * of the new ones.
	 *
	 * @param projectTags
	 * @param test        if true, it will only load a certain number of
	 *                    proteins/peptides/psms
	 */
	public void loadProjectListFromServer(final Set<String> projectTags, final boolean testMode, boolean directAccess) {

		if (projectTags == null || projectTags.isEmpty()) {
			final String msg = "There is not projects to load. Please, select projects from 'Browse' section";
			Window.alert(msg);
			updateStatus(msg);
			return;
		}
		if (hasLoadedThisProjects(projectTags)) {
			GWT.log(projectTags.size() + " projects already loaded");
			return;
		}
		updateStatus("Loading projects from database...");
		projectListPanel.clearList();
		loadedProjects.clear();
		loadedProjectBeanSet.clear();
		loadedProjects.addAll(projectTags);
		final String projectTagString = getProjectTagString(projectTags);
		proteinRetrievingService.getProjectBeans(projectTags, new AsyncCallback<List<ProjectBean>>() {

			@Override
			public void onFailure(Throwable caught) {

				PendingTasksManager.removeAllTasks(TaskType.PROTEINS_BY_PROJECT);

				loadedProjects.removeAll(projectTags);
				loadingDialog.hide();
				updateStatus(caught);
				final MyDialogBox errorDialog = new MyDialogBox(
						"Error loading project(s): '" + projectTagString + "':\n" + caught.getMessage(), true, false,
						false, getTimerOnClosingLoadingDialog(), null);
				errorDialog.center();
			}

			@Override
			public void onSuccess(List<ProjectBean> projectBeans) {
				loadedProjectBeanSet.addAll(projectBeans);

				GWT.log("project bean " + projectTagString + " received");
				if (projectTags.size() > 1) {
					for (final String projectTag : projectTags) {
						projectListPanel.getListBox().addItem(
								ClientDataUtil.getNewElementNameForProject(projectTag, projectListPanel.getListBox()),
								projectTag);
					}
				} else {
					projectListPanel.getListBox().addItem(projectBeans.get(0).getTag(), projectBeans.get(0).getTag());
				}
				// if all projects are received
				if (loadedProjectBeanSet.size() == loadedProjects.size()) {
					checkLoadedProjectsAndLoadProjects(projectBeans, true, testMode, directAccess);

				}
			}
		});

		loadStaticDataFromProjects(projectTags);
		loadSimpleQuerySuggestions(projectTags);
	}

	protected void checkLoadedProjectsAndLoadProjects(List<ProjectBean> projectBeans, boolean checkPrivateProjects,
			boolean testMode, boolean directAccess) {

		final ProjectBean projectBeanForDisplay = loadedProjectBeanSet.getBigProject() != null
				? loadedProjectBeanSet.getBigProject()
				: projectBeans.get(0);
		final boolean modifyColumns = loadedProjectBeanSet.containsBigProject();
		final boolean containsPrivateProject = loadedProjectBeanSet.containsPrivateProject();
		if (checkPrivateProjects && containsPrivateProject && !directAccess) {
			// ask for login
			loginToOpenPrivateProjects(projectBeans, testMode, directAccess);
			return;
		}
		final boolean changeToDataTab = false;
		final boolean showMaxNumberOfProjectsAtOnceMessage = loadedProjectBeanSet
				.size() > SharedConstants.MAX_NUM_PROJECTS_LOADED_AT_ONCE;
		requestDefaultViews(projectBeanForDisplay, modifyColumns, showMaxNumberOfProjectsAtOnceMessage, changeToDataTab,
				loadedProjectBeanSet.containsBigProject(), testMode, //
				false // showWelcomeProjectWindow
		);
		// load the project
		if (loadedProjectBeanSet.containsBigProject()) {
			// disable queries for big projects. Only allow
			// simple ones.
			queryEditorPanel.enableQueries(false);
			// change default empty message on tables
			final String emptyLabelString = "Project will not be loaded by default. "
					+ "Please go to 'Query' tab and use the 'Simple Query Editor'";
			proteinTablePanel.setEmptyTableWidget(emptyLabelString);
			proteinGroupTablePanel.setEmptyTableWidget(emptyLabelString);
			peptideTablePanel.setEmptyTableWidget(emptyLabelString);
			if (Pint.getPSMCentric()) {
				psmTablePanel.setEmptyTableWidget(emptyLabelString);
				psmOnlyTablePanel.setEmptyTableWidget(emptyLabelString);
			} else {
				peptideOnlyTablePanel.setEmptyTableWidget(emptyLabelString);
			}

		} else if (showMaxNumberOfProjectsAtOnceMessage) {
			// enable queries
			queryEditorPanel.enableQueries(true);
			final String emptyLabelString = "The projects will not be automatically loaded because they are more than one. "
					+ "Please go to 'Query' tab to query data over the two projects";
			proteinTablePanel.setEmptyTableWidget(emptyLabelString);
			proteinGroupTablePanel.setEmptyTableWidget(emptyLabelString);
			peptideTablePanel.setEmptyTableWidget(emptyLabelString);
			if (Pint.getPSMCentric()) {
				psmTablePanel.setEmptyTableWidget(emptyLabelString);
				psmOnlyTablePanel.setEmptyTableWidget(emptyLabelString);
			} else {
				peptideOnlyTablePanel.setEmptyTableWidget(emptyLabelString);
			}
		} else {
			loadProteinsFromProject(null, null, null, //
					true, // showWelcomeProject
					testMode);
		}
	}

	private void loginToOpenPrivateProjects(List<ProjectBean> projectBeans, boolean testMode, boolean directAccess) {
		// check first the login
		final PopUpPanelPasswordChecker loginPanel = new PopUpPanelPasswordChecker(true, true, "PINT security",
				"Some projects are private.\nYou need the PINT master password to be able to query them:");
		loginPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				final PopupPanel popup = event.getTarget();
				final Widget widget = popup.getWidget();
				if (widget instanceof PopUpPanelPasswordChecker) {
					final PopUpPanelPasswordChecker loginPanel = (PopUpPanelPasswordChecker) widget;
					if (loginPanel.isLoginOK()) {
						checkLoadedProjectsAndLoadProjects(projectBeans, false, testMode, directAccess);
					} else {
						History.newItem(TargetHistory.BROWSE.getTargetHistory());
					}
				}
			}
		});
		loginPanel.show();

	}

	private String getProjectTagString(Collection<String> projectTags) {
		final List<String> list = new ArrayList<String>();
		list.addAll(projectTags);
		Collections.sort(list);
		final StringBuilder sb = new StringBuilder();
		for (final String projectTag : list) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(projectTag);
		}
		return sb.toString();
	}

	private Timer getTimerOnClosingLoadingDialog() {
		if (timer == null) {
			timer = new Timer() {
				@Override
				public void run() {
					onTaskRemovedOrAdded();
				}
			};
		}
		return timer;
	}

	private void loadSimpleQuerySuggestions(Set<String> projectTags) {
		// load protein projections in the projects
		for (final String projectTag : projectTags) {
			// by protein name
			proteinRetrievingService.getProteinProjectionsByProteinNameFromProject(projectTag,
					new AsyncCallback<Map<String, Set<ProteinProjection>>>() {

						@Override
						public void onFailure(Throwable caught) {
							updateStatus(caught);
							queryEditorPanel.enableSimpleQueriesByProteinName(false);
						}

						@Override
						public void onSuccess(Map<String, Set<ProteinProjection>> proteinProjections) {
							queryEditorPanel
									.addSimpleQueryByProteinNameSuggestionsAsProteinProjections(proteinProjections);
							queryEditorPanel.enableSimpleQueriesByProteinName(true);
						}
					});
			// by acc
			proteinRetrievingService.getProteinProjectionsByProteinACCFromProject(projectTag,
					new AsyncCallback<Map<String, Set<ProteinProjection>>>() {

						@Override
						public void onFailure(Throwable caught) {
							updateStatus(caught);
							queryEditorPanel.enableSimpleQueriesByAcc(false);
						}

						@Override
						public void onSuccess(Map<String, Set<ProteinProjection>> proteinProjections) {
							queryEditorPanel.addSimpleQueryByAccSuggestionsAsProteinProjections(proteinProjections);
							queryEditorPanel.enableSimpleQueriesByAcc(true);
						}
					});
			// by gene name
			proteinRetrievingService.getProteinProjectionsByGeneNameFromProject(projectTag,
					new AsyncCallback<Map<String, Set<ProteinProjection>>>() {

						@Override
						public void onFailure(Throwable caught) {
							updateStatus(caught);
							queryEditorPanel.enableSimpleQueriesByGeneName(false);
						}

						@Override
						public void onSuccess(Map<String, Set<ProteinProjection>> proteinProjections) {
							queryEditorPanel
									.addSimpleQueryByGeneNameSuggestionsAsProteinProjections(proteinProjections);
							queryEditorPanel.enableSimpleQueriesByGeneName(true);
						}
					});
		}

	}

	/**
	 * Loads several things from projects: conditions, uniprot version,
	 * thresholdnames and ratiodescriptors
	 *
	 * @param projectNames
	 */
	protected void loadStaticDataFromProjects(Set<String> projectNames) {

		// load experimental conditions
		loadConditionsFromProjects(projectNames);
		// load uniprot versions
		requestUniprotVersionsFromProjects(projectNames);
		// load threshold names
		loadThresholdNamesFromServer(projectNames);
		// load PSM score names
		// loadPSMScoreNames(projectNames);
		// load PSM score types
		// loadPSMScoreTypes(projectNames);
		// load ratios
//		loadRatioDescriptors(projectNames);

	}

	private void loadCommands() {
		proteinRetrievingService.getCommands(new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				final Set<String> keySet = result.keySet();
				final List<String> orderedList = new ArrayList<String>();
				orderedList.addAll(keySet);
				Collections.sort(orderedList);
				for (final String commandAbbreviature : orderedList) {
					final String formattedCommand = result.get(commandAbbreviature);
					commandsPanel.getListBox().addItem(commandAbbreviature, formattedCommand);
					// add to suggestions in the query editor
					queryEditorPanel.addSuggestionToComplexQuery(formattedCommand);
				}
			}
		});
	}

	/**
	 * Loads the proteins of the projects in 'loadedProjects' set.
	 *
	 * @param uniprotVersion    if null, it will use the latest
	 * @param defaultQueryIndex if not null, it will ask for the default query
	 *                          defined at that index in the array of default
	 *                          queries
	 * @param string
	 */
	public void loadProteinsFromProject(final String uniprotVersion, Integer defaultQueryIndex, String queryName,
			boolean showWelcomeProjectWindowAfterQuery, final boolean testMode) {
		// emtpy protein group panel
		proteinGroupTablePanel.clearTable();
		proteinGroupTablePanel.setVisible(false);
		proteinTablePanel.setVisible(true);
		proteinTablePanel.setEmptyTableWidget("Please wait. Proteins will be loaded in the table...");
		if (Pint.getPSMCentric()) {
			psmTablePanel.setEmptyTableWidget("Please wait. PSMs will be loaded in the table...");
			psmOnlyTablePanel.setEmptyTableWidget("Please wait. PSMs will be loaded in the table...");
		} else {
			peptideTablePanel.setEmptyTableWidget("Please wait. PSMs will be loaded in the table...");
		}
		proteinGroupTablePanel.setEmptyTableWidget("Please wait. Protein groups will be loaded in the table...");
		peptideOnlyTablePanel.setEmptyTableWidget("Please wait. Peptides will be loaded in the table...");
		clearTablesLinksAndStatusText();
		if (loadedProjects != null && !loadedProjects.isEmpty()) {

			String plural = "";
			if (loadedProjects.size() > 1)
				plural = "s";
			String logMessage = "Getting proteins from project" + plural;
			if (defaultQueryIndex != null) {
				logMessage = "Getting proteins from query '" + queryName + "'";
			}
			logMessage += "...";

			// showLoadingDialog(logMessage);
			updateStatus(logMessage);

			// clear proteins in the table
			proteinTablePanel.clearTable();
			if (Pint.getPSMCentric()) {
				// clear psms in the table
				psmTablePanel.clearTable();
				// emtpy PSM only panel
				psmOnlyTablePanel.clearTable();
				// enable PSM only tab
				psmOnlyTablePanel.setEmptyTableWidget("Please wait for PSMs to be loaded...");
			} else {
				peptideTablePanel.clearTable();
			}
			// emtpy Peptide only panel
			peptideOnlyTablePanel.clearTable();

			// enable Peptide only tab
			peptideOnlyTablePanel.setEmptyTableWidget(getPeptideOnlyTableEmptyWidget());
			if (!loadedProjects.isEmpty()) {

				if (Pint.getPSMCentric()) {
					// remove the score columns for psms
					psmTablePanel.removeColumn(ColumnName.PSM_SCORE);
					psmOnlyTablePanel.removeColumn(ColumnName.PSM_SCORE);
					psmTablePanel.removeColumn(ColumnName.PTM_SCORE);
					psmOnlyTablePanel.removeColumn(ColumnName.PTM_SCORE);
				} else {
					peptideTablePanel.removeColumn(ColumnName.PEPTIDE_SCORE);
					peptideTablePanel.removeColumn(ColumnName.PTM_SCORE);
					peptideOnlyTablePanel.removeColumn(ColumnName.PEPTIDE_SCORE);
				}

				// remove the score columns for proteins
				proteinTablePanel.removeColumn(ColumnName.PROTEIN_SCORE);

				scoreNamesPanel.clearList();
				scoreTypesPanel.clearList();

				// final ProteinsByProjectLoadingDialog progressDialog = new
				// ProteinsByProjectLoadingDialog(sessionID,
				// projectTag, uniprotVersion);

				final String projectTagsString = getProjectTagString(loadedProjects);
				GWT.log("Getting proteins from project " + projectTagsString);
				final Task task = PendingTasksManager
						.addPendingTask(new GetProteinsFromProjectTask(projectTagsString, uniprotVersion));
				proteinRetrievingService.getProteinsFromProjects(sessionID, loadedProjects, uniprotVersion,
						proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), defaultQueryIndex, testMode,
						task, new AsyncCallback<QueryResultSubLists>() {

							@Override
							public void onFailure(Throwable caught) {
								try {
									// progressDialog.finishAndHide();
									updateStatus(caught);
								} finally {
									// removePending task
									PendingTasksManager.removeTask(task);
								}
							}

							@Override
							public void onSuccess(QueryResultSubLists result) {
								try {
									// progressDialog.finishAndHide(2000);
									queryEditorPanel.updateQueryResultSummary(result);
									if (result != null) {
										if (Pint.getPSMCentric()) {
											updateStatus(result.getNumTotalPSMs() + " psms received.");
										}
//										updateStatus(
//												result.getPeptideSubList().getTotalNumber() + " peptides received.");
										updateStatus(result.getNumTotalProteins() + " proteins received.");
										updateStatus(result.getNumTotalProteinGroups() + " protein groups received.");
										if (Pint.getPSMCentric()) {
											addPSMScoreNames(result.getPsmScores());
										}
										addProteinScoreNames(result.getProteinScores());
										addPeptideScoreNames(result.getPeptideScores());
										// TODO check this how to add ptm scores
										// if they are coming from peptides?
										addPTMScoreNames(result.getPsmScores());
										addScoreTypes(result.getScoreTypes());
										addRatioDescriptorsToTables(result.getRatioDescriptors(), loadedProjects);
										// apply default views
										final String projectTag = loadedProjects.iterator().next();
										if (!loadedProjects.isEmpty() && ClientCacheDefaultViewByProjectTag
												.getInstance().contains(projectTag)) {
											final DefaultView defaultView = ClientCacheDefaultViewByProjectTag
													.getInstance().getFromCache(projectTag);
											applyDefaultViews(loadedProjectBeanSet.getByTag(projectTag), loadedProjects,
													defaultView, //
													true, // modifyColumns
													false, // showMaxNumberOfProjectsAtOnceMessage
													true, // changeToDataTab
													false, // bigProject
													testMode, // testMode
													true // showWelcomeProjectWindow
											);
										}
										if (result.getNumTotalProteins() > 0) {
											prepareDownloadLinksForProject();
										}
										// load on the grid
										// loadProteinsOnGrid(result);
										// NOT LOAD THE DATA YET, WAIT UNTIL
										// DEFAULT VIEWS ASK FOR SORTED DATA
										// loadProteinsOnGrid(0,
										// result.getProteinSubList());
										// loadPSMsOnlyOnGrid(0,
										// result.getPsmSubList());
										// loadPeptidesOnlyOnGrid(0,
										// result.getPeptideSubList());
										// loadProteinGroupsOnGrid(0,
										// result.getProteinGroupSubList());

										if (result.isEmpty()) {
											if (Pint.getPSMCentric()) {
												psmOnlyTablePanel.setEmptyTableWidget("No data to show");
												psmTablePanel.setEmptyTableWidget("No data to show");
											} else {
												peptideTablePanel.setEmptyTableWidget("No data to show");
											}
											peptideOnlyTablePanel.setEmptyTableWidget("No data to show");
											proteinTablePanel.setEmptyTableWidget("No data to show");
											proteinGroupTablePanel.setEmptyTableWidget("No data to show");
										}
									}
								} finally {
									// removePending task
									PendingTasksManager.removeTask(task);
								}
							}
						});

			} else {
			}
		}

	}

	/**
	 * This widget will show a button to load the entire peptide table if desired,
	 * warning of that it could be slow depending on the amount of data.
	 * 
	 * @return
	 */
	private Widget getPeptideOnlyTableEmptyWidget() {
		final FlexTable table = new FlexTable();

		final Label label1 = new Label("In order to load the peptide table click on the button below.");
		label1.setStyleName("ProjectItemContentTitle");
		label1.getElement().getStyle().setMargin(10, Unit.PX);
		table.setWidget(0, 0, label1);
		table.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		//
		final Label label2 = new Label(
				"Depending on the size of the dataset this task can take from seconds to minutes.");
		label2.setStyleName("ProjectItemContentTitle");
		label2.getElement().getStyle().setMargin(10, Unit.PX);
		table.setWidget(1, 0, label2);
		table.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		//
		final Label button = new Label("Load peptide table");
		button.setStyleName(WizardStyles.WizardButton);
		table.setWidget(2, 0, button);
		table.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				peptideOnlyTablePanel.setEmptyTableWidget(peptideOnlyTablePanel.getLoadingWidget());
				peptideOnlyTablePanel.clearTable();
				showMessage("Loading peptide table...");
				peptideOnlyTablePanel.setReadyToShowData(true);
				peptideOnlyTablePanel.refreshData();
			}
		});

		return table;
	}

	@Override
	public void hiddePanel() {
		// hidde psmtable, the one that is below the proteins
		if (Pint.getPSMCentric() && layoutPanel.getWidgetIndex(psmTablePanel) >= 0) {
			layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 0, Unit.PCT);
		} else if (layoutPanel.getWidgetIndex(peptideTablePanel) >= 0) {
			layoutPanel.setWidgetBottomHeight(peptideTablePanel, 0, Unit.PCT, 0, Unit.PCT);
		}
		if (layoutPanel.getWidgetIndex(secondLevelTabPanel) >= 0) {
			layoutPanel.setWidgetTopHeight(secondLevelTabPanel, 0, Unit.PCT, 100, Unit.PCT);
		}
	}

	@Override
	public void showPanel() {
		if (Pint.getPSMCentric() && layoutPanel.getWidgetIndex(psmTablePanel) >= 0) {
			layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 50, Unit.PCT);
		} else if (layoutPanel.getWidgetIndex(peptideTablePanel) >= 0) {
			layoutPanel.setWidgetBottomHeight(peptideTablePanel, 0, Unit.PCT, 50, Unit.PCT);
		}

		if (layoutPanel.getWidgetIndex(secondLevelTabPanel) >= 0)
			layoutPanel.setWidgetTopHeight(secondLevelTabPanel, 0, Unit.PCT, 50, Unit.PCT);

	}

	private void clearTablesLinksAndStatusText() {
		// if (result == null || result.isEmpty()) {
		proteinGroupTablePanel.clearTable();
		proteinTablePanel.clearTable();
		if (Pint.getPSMCentric()) {
			psmTablePanel.clearTable();
			psmOnlyTablePanel.clearTable();
		} else {
			peptideTablePanel.clearTable();
			peptideOnlyTablePanel.clearTable();
		}
		queryEditorPanel.setLinksToResultsVisible(false);
		queryEditorPanel.setSendingStatusText(null);
		// } else {
		//
		// // fill the grid
		// proteinTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(),
		// true);
		// proteinTablePanel.getAsyncDataProvider().updateRowData(start,
		// result.getDataList());
		// // proteinTablePanel.reloadData();
		// proteinTablePanel.refreshData();
		// selectDataTab(proteinTablePanel);
		// }

	}

	public void selectDataTab(AbstractDataTable table) {
		if ((Pint.getPSMCentric() && table == psmOnlyTablePanel) || table == peptideOnlyTablePanel
				|| table == proteinGroupTablePanel || table == proteinTablePanel) {
			GWT.log("Changing tab to " + table.getClass().getName());
			firstLevelTabPanel.selectTab(layoutPanel);
			secondLevelTabPanel.selectTab(table);
			table.refreshData();
		}
	}

	/**
	 * This method retrieves the default views for the data tables and applies it to
	 * them. The default view depends on each project and it is configured in the
	 * server. If the client currently has loaded more than one project, the only
	 * the default view of one of them will be retrieved.
	 *
	 * @param projectBean
	 * @param modifyColumns
	 * @param showWelcomeWindowBox
	 * @param changeToDataTab
	 * @param bigProject
	 */
	private void requestDefaultViews(final ProjectBean projectBean, final boolean modifyColumns,
			boolean showMaxNumberOfProjectsAtOnceMessage, final boolean changeToDataTab, final boolean bigProject,
			final boolean testMode, boolean showWelcomeProjectWindow) {
		if (loadedProjects.isEmpty()) {
			return;
		}
		GWT.log("Requesting default views");

		for (final String projectTag : loadedProjects) {

			if (ClientCacheDefaultViewByProjectTag.getInstance().contains(projectTag)) {
				GWT.log("Default default view of project " + projectTag + " found in client cache");

				final DefaultView defaultViews = ClientCacheDefaultViewByProjectTag.getInstance()
						.getFromCache(projectTag);
				applyDefaultViews(projectBean, loadedProjects, defaultViews, modifyColumns,
						showMaxNumberOfProjectsAtOnceMessage, changeToDataTab, bigProject, testMode,
						showWelcomeProjectWindow);
				projectInformationPanel.addProjectView(projectBean, defaultViews);
			} else {
				GWT.log("Requesting default view of project " + projectTag + " ");
				proteinRetrievingService.getDefaultViewByProject(projectTag, new AsyncCallback<DefaultView>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("Problem requesting default view of project " + projectTag + ": "
								+ caught.getMessage());
						updateStatus(caught);
					}

					@Override
					public void onSuccess(DefaultView defaultViews) {
						GWT.log("Default view of project " + projectTag + " received");
						if (defaultViews != null) {
							GWT.log("Default view of project " + projectTag + " has "
									+ defaultViews.getProjectNamedQueries().size() + " recommended queries");
							// only apply default views of the projectBean in
							// the parameters
							if (projectBean.getTag().equals(projectTag)) {
								applyDefaultViews(projectBean, loadedProjects, defaultViews, modifyColumns,
										showMaxNumberOfProjectsAtOnceMessage, changeToDataTab, bigProject, testMode,
										showWelcomeProjectWindow);
							}
							ClientCacheDefaultViewByProjectTag.getInstance().addtoCache(defaultViews, projectTag);
							final ProjectBean projectBean2 = loadedProjectBeanSet.getByTag(projectTag);
							projectInformationPanel.addProjectView(projectBean2, defaultViews);
						}
					}
				});
			}
		}

	}

	private void changeToDataTab(DefaultView defaultViews) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				// select default tab
				final TAB defaultTab = defaultViews.getDefaultTab();
				GWT.log("Changing tab to " + defaultTab.name());

				switch (defaultTab) {
				case PROTEIN:
					selectDataTab(proteinTablePanel);
					break;
				case PROTEIN_GROUP:
					selectDataTab(proteinGroupTablePanel);
					break;
				case PSM:
					if (Pint.getPSMCentric()) {
						selectDataTab(psmOnlyTablePanel);
					}
					break;
				case PEPTIDE:
					selectDataTab(peptideOnlyTablePanel);
					break;
				default:
					break;
				}
				GWT.log("Changing tab to " + defaultTab.name() + " END");
			}
		});

	}

	private void setDefaultViewInTables(DefaultView defaultViews) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				GWT.log("Setting default view in tables");

				// tables
				proteinGroupTablePanel.setDefaultView(defaultViews);
				proteinTablePanel.setDefaultView(defaultViews);
				if (Pint.getPSMCentric()) {
					psmTablePanel.setDefaultView(defaultViews);
					psmOnlyTablePanel.setDefaultView(defaultViews);
				} else {
					peptideTablePanel.setDefaultView(defaultViews);
				}
				peptideOnlyTablePanel.setDefaultView(defaultViews);
			}
		});
	}

	private void setDefaultViewInCheckBoxes(DefaultView defaultViews) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				GWT.log("Setting default view in checkboxes");
				// checkboxes
				if (Pint.getPSMCentric()) {
					psmColumnNamesPanel.setDefaultView(defaultViews.getPsmDefaultView());
				}
				peptideColumnNamesPanel.setDefaultView(defaultViews.getPeptideDefaultView());
				proteinColumnNamesPanel.setDefaultView(defaultViews.getProteinDefaultView());
				proteinGroupColumnNamesPanel.setDefaultView(defaultViews.getProteinGroupDefaultView());
			}
		});

	}

	private void applyDefaultViews(ProjectBean projectBean, Set<String> loadedprojects, DefaultView defaultViews,
			boolean modifyColumns, boolean showMaxNumberOfProjectsAtOnceMessage, boolean changeToDataTab,
			boolean bigProject, boolean testMode, boolean showWelcomeProjectWindow) {

		if (modifyColumns) {
			setDefaultViewInTables(defaultViews);
			setDefaultViewInCheckBoxes(defaultViews);
		}
		if (showMaxNumberOfProjectsAtOnceMessage) {
			GWT.log("Showing warning of multiple projects");
			welcomeToProjectWindowBox = new WindowBox(true, true, true, true, false);
			welcomeToProjectWindowBox.setAnimationEnabled(true);
			final List<String> list = new ArrayList<String>();
			list.addAll(loadedprojects);
			Collections.sort(list);
			welcomeToProjectWindowBox.setWidget(new MyWarningMultipleProjectsLoadedPanel(list));
			welcomeToProjectWindowBox.setText("Multiple projects loaded");
			if (!changeToDataTab) {
				final DoSomethingTask<Void> doSomething = new DoSomethingTask<Void>() {
					@Override
					public Void doSomething() {
						onTaskRemovedOrAdded();
						// show project information tab when closing
						changeToProjectInfoTab();
						return null;
					}
				};
				// add loadingDialogShowerTask to close event
				welcomeToProjectWindowBox.addCloseEventDoSomethingTask(doSomething);
			}
			welcomeToProjectWindowBox.setGlassEnabled(true);
			welcomeToProjectWindowBox.center();
		} else if (showWelcomeProjectWindow) {
			showWelcomeToProjectWindow(projectBean, defaultViews, testMode, !changeToDataTab);
		}
		if (changeToDataTab) {
			changeToDataTab(defaultViews);
		}
	}

	protected void showWelcomeToProjectWindow(ProjectBean projectBean, DefaultView defaultViews, boolean testMode,
			boolean openProjectInfoTabAfterClose) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				GWT.log("Showing welcome window for project");
				welcomeToProjectWindowBox = new WindowBox(true, true, true, true, false);
				welcomeToProjectWindowBox.setAnimationEnabled(true);
				welcomeToProjectWindowBox.setWidget(new MyWelcomeProjectPanel(welcomeToProjectWindowBox, projectBean,
						defaultViews, QueryPanel.this, testMode));
				welcomeToProjectWindowBox.setText("Project '" + defaultViews.getProjectTag() + "'");
				if (openProjectInfoTabAfterClose) {
					final DoSomethingTask<Void> doSomething = new DoSomethingTask<Void>() {
						@Override
						public Void doSomething() {
							onTaskRemovedOrAdded();
							// show project information tab when closing
							changeToProjectInfoTab();
							return null;
						}
					};
					// add loadingDialogShowerTask to close event
					welcomeToProjectWindowBox.addCloseEventDoSomethingTask(doSomething);
				}
				welcomeToProjectWindowBox.setGlassEnabled(true);
				welcomeToProjectWindowBox.center();
			}
		});
	}

	@Override
	public void onTaskRemoved() {
		onTaskRemovedOrAdded();
	}

	@Override
	public void onTaskAdded() {
		onTaskRemovedOrAdded();
	}

	/**
	 * Creates an implementation of {@link DoSomethingTask} that checks if some of
	 * the tables are waiting to be loaded, and if so, it centers the corresponding
	 * loading dialog.
	 *
	 * @return
	 */
	private void onTaskRemovedOrAdded() {
		// if the welcome project window is visible, dont do nothing
		if (welcomeToProjectWindowBox != null && welcomeToProjectWindowBox.isShowing())
			return;

		boolean someTaskIsPending = false;
		final TaskType[] taskTypes = TaskType.values();
		for (final TaskType taskType : taskTypes) {
			String buttonText = null;
			ClickHandler handler = null;
			final List<Task> pendingTasks = PendingTasksManager.getPendingTasks(taskType);
			if (!pendingTasks.isEmpty()) {
				if (taskType == TaskType.QUERY_SENT || taskType == TaskType.PROTEINS_BY_PROJECT) {
					buttonText = "Cancel";
					handler = new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							showLoadingDialogOLD("Cancelling...", null, null);
							final Task task = PendingTasksManager.addPendingTask(new CancellingTask());
							proteinRetrievingService.cancelQuery(sessionID, new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									StatusReportersRegister.getInstance().notifyStatusReporters(caught);
									updateStatus("Error trying to cancel task");
									updateStatus(caught);
									hiddeLoadingDialog();
									PendingTasksManager.removeTask(task);
								}

								@Override
								public void onSuccess(Void result) {
									for (final Task pendingTask : pendingTasks) {
										PendingTasksManager.removeTask(pendingTask);
									}
									PendingTasksManager.removeTask(task);
									updateStatus("Task cancelled.");
									hiddeLoadingDialog();
								}
							});

						}
					};
				}

				someTaskIsPending = true;
				if (pendingTasks.size() == 1) {
//					final String singleTaskMessage = taskType
//							.getSingleTaskMessage("'" + pendingTaskKeys.get(0).getTaskKey() + "'");
					showNewLoadingDialog(sessionID, pendingTasks.get(0), true, true, buttonText, handler);
//					showLoadingDialog(singleTaskMessage,							buttonText, handler);
				} else {
					showLoadingDialogOLD(taskType.getMultipleTaskMessage("" + pendingTasks.size()), buttonText,
							handler);
				}
			}
		}
		// if there is not any pending task, hidde the loading
		// dialog
		if (!someTaskIsPending) {
			hiddeLoadingDialog();
			hiddeAdvancedProgressDialog();
		}
		return;
	}

	// private void loadPSMsOnlyOnGrid(int start, PsmBeanSubList result) {
	// if (result == null || result.isEmpty()) {
	// psmOnlyTablePanel.clearTable();
	// } else {
	// psmOnlyTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(),
	// true);
	// psmOnlyTablePanel.getAsyncDataProvider().updateRowData(start,
	// result.getDataList());
	// // psmOnlyTablePanel.reloadData();
	// psmOnlyTablePanel.refreshData();
	// }
	// }
	//
	// private void loadPeptidesOnlyOnGrid(int start, PeptideBeanSubList result)
	// {
	// if (result == null || result.isEmpty()) {
	// peptideOnlyTablePanel.clearTable();
	// } else {
	// peptideOnlyTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(),
	// true);
	// peptideOnlyTablePanel.getAsyncDataProvider().updateRowData(start,
	// result.getDataList());
	// // peptideTablePanel.reloadData();
	// peptideOnlyTablePanel.refreshData();
	// }
	// }

	private void showLoadingDialogOLD(String text, boolean autohide, boolean modal, String buttonText,
			ClickHandler clickHandler) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, autohide, modal, getTimerOnClosingLoadingDialog(), buttonText);
		} else {
			loadingDialog.setText(text);
			loadingDialog.setAutoHideEnabled(autohide);
			loadingDialog.setModal(modal);
			loadingDialog.setButtonText(buttonText);
		}
		loadingDialog.addClickHandler(clickHandler);
		loadingDialog.center();

	}

	private void showNewLoadingDialog(String sessionID, Task task, boolean autohide, boolean modal, String buttonText,
			ClickHandler clickHandler) {

		advancedProgressDialog = new AdvancedProgressDialog(sessionID, task);
		advancedProgressDialog.setAutoHideEnabled(autohide);
		advancedProgressDialog.setModal(modal);
		advancedProgressDialog.setButtonText(buttonText);
		advancedProgressDialog.setButtonAction(clickHandler);
		advancedProgressDialog.center();

	}

	/**
	 * Modal false and autohide true
	 *
	 * @param text
	 */
	public void showLoadingDialogOLD(String text, String buttonText, ClickHandler handler) {
		showLoadingDialogOLD(text, true, false, buttonText, handler);
	}

	private void hiddeLoadingDialog() {
		if (loadingDialog != null) {
			loadingDialog.hide();
		}
	}

	private void hiddeAdvancedProgressDialog() {
		if (advancedProgressDialog != null) {
			advancedProgressDialog.finishAndHide();
		}
	}

	private void loadRatioDescriptors(final Set<String> selectedProjects) {
		ratioNamesPanel.clearList();

		GWT.log("Loading ratios from " + selectedProjects.size() + " projects");
		proteinRetrievingService.getRatioDescriptorsFromProjects(selectedProjects,
				new AsyncCallback<List<RatioDescriptorBean>>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
					}

					@Override
					public void onSuccess(List<RatioDescriptorBean> result) {
						addRatioDescriptorsToTables(result, selectedProjects);
					}

				});

	}

	protected void addRatioDescriptorsToTables(List<RatioDescriptorBean> result, Collection<String> selectedProjects) {
		final Set<String> uniqueRatioNames = new HashSet<String>();
		// remove the previous columns related to ratios
		proteinTablePanel.removeColumn(ColumnName.PROTEIN_RATIO);
		proteinGroupTablePanel.removeColumn(ColumnName.PROTEIN_RATIO);
		proteinTablePanel.removeColumn(ColumnName.PROTEIN_RATIO_SCORE);

		if (Pint.getPSMCentric()) {
			psmTablePanel.removeColumn(ColumnName.PSM_RATIO);
			psmOnlyTablePanel.removeColumn(ColumnName.PSM_RATIO);
			psmTablePanel.removeColumn(ColumnName.PSM_RATIO_SCORE);
			psmOnlyTablePanel.removeColumn(ColumnName.PSM_RATIO_SCORE);
		} else {
			peptideTablePanel.removeColumn(ColumnName.PEPTIDE_RATIO);
			peptideTablePanel.removeColumn(ColumnName.PEPTIDE_RATIO_SCORE);
		}

		peptideOnlyTablePanel.removeColumn(ColumnName.PEPTIDE_RATIO);
		peptideOnlyTablePanel.removeColumn(ColumnName.PEPTIDE_RATIO_SCORE);

		// create the columns related to the ratio descriptors
		if (!result.isEmpty()) {
			for (final RatioDescriptorBean ratioDescriptor : result) {
				if (!uniqueRatioNames.contains(ratioDescriptor.getRatioName())) {
					ratioNamesPanel.getListBox().addItem(ratioDescriptor.getRatioName());
					uniqueRatioNames.add(ratioDescriptor.getRatioName());
				}
			}
			updateStatus(uniqueRatioNames.size() + " ratio names loaded.");
			for (final String projectTag : selectedProjects) {
				// get the conditions from the
				// "Condition" list panel

				for (final RatioDescriptorBean ratioDescriptor : result) {
					final String condition1 = ratioDescriptor.getCondition1Name();
					final String projectNameFromRatio = ratioDescriptor.getProjectTag();
					if (projectTag.equals(projectNameFromRatio)) {
						final String condition2 = ratioDescriptor.getCondition2Name();
						final String letter1 = getProjectConditionSymbolOfCondition(condition1, projectTag);
						final String letter2 = getProjectConditionSymbolOfCondition(condition2, projectTag);

						final ColumnWithVisibility proteinRatioColumn = ProteinColumns.getInstance()
								.getColumn(ColumnName.PROTEIN_RATIO);
						final String ratioName = ratioDescriptor.getRatioName();
						final String keyName = MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_RATIO,
								condition1, letter1, condition2, letter2, projectTag, ratioName);
						final String ratioCheckBoxName = MyVerticalCheckBoxListPanel.getCheckBoxNameForRatio(ratioName,
								letter1, letter2);
						final String proteinRatioGraphCheckBoxName = MyVerticalCheckBoxListPanel
								.getCheckBoxNameForRatioGraph(ColumnName.PROTEIN_RATIO_GRAPH, ratioName);
						switch (ratioDescriptor.getAggregationLevel()) {
						// addcolumn for pairs of conditions of
						// the ratio descriptors
						// add the corresponding ratio score
						// column
						case PROTEINGROUP:
							// add checkbox for ProteinRatio for
							// that ratio
							proteinGroupColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO,
									ratioCheckBoxName, keyName);

							proteinGroupColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO_GRAPH,
									proteinRatioGraphCheckBoxName,
									MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_RATIO_GRAPH, condition1,
											letter1, condition2, letter2, projectTag, ratioName));
							proteinGroupTablePanel.addColumnForConditionRatio(ColumnName.PROTEIN_RATIO,
									proteinRatioColumn.isVisible(), condition1, letter1, condition2, letter2,
									projectTag, ratioName);

							proteinGroupTablePanel.addColumnForConditionRatio(ColumnName.PROTEIN_RATIO_GRAPH,
									proteinRatioColumn.isVisible(), condition1, letter1, condition2, letter2,
									projectTag, ratioName);
							// proteinGroupColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
							// ColumnName.PROTEIN_RATIO,
							// ratioCheckBoxName, condition1,
							// condition2,
							// projectTag, conditionsPanel);
							final String proteinGroupRatioScoreName = ratioDescriptor.getProteinScoreName();
							if (proteinGroupRatioScoreName != null) {
								proteinGroupTablePanel.addColumnForConditionRatioScore(ColumnName.PROTEIN_RATIO_SCORE,
										ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO_SCORE)
												.isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName,
										proteinGroupRatioScoreName);
								proteinGroupColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO_SCORE,
										proteinGroupRatioScoreName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_RATIO_SCORE,
												condition1, letter1, condition2, letter2, projectTag, ratioName,
												proteinGroupRatioScoreName));
								final String ratioScoreCheckBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForRatioScore(proteinGroupRatioScoreName, ratioName);
								// proteinGroupColumnNamesPanel
								// .addRatioConditionRelatedColumnCheckBoxHandler(
								// ColumnName.PROTEIN_RATIO_SCORE,
								// ratioScoreCheckBoxName,
								// condition1, condition2,
								// projectTag, conditionsPanel);
							}
							break;
						case PROTEIN:
							// add checkbox for ProteinRatio for
							// that ratio
							proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO,
									ratioCheckBoxName, keyName);

							proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO_GRAPH,
									proteinRatioGraphCheckBoxName,
									MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_RATIO_GRAPH, condition1,
											letter1, condition2, letter2, projectTag, ratioName));
							proteinTablePanel.addColumnForConditionRatio(ColumnName.PROTEIN_RATIO,
									proteinRatioColumn.isVisible(), condition1, letter1, condition2, letter2,
									projectTag, ratioName);

							proteinTablePanel.addColumnForConditionRatio(ColumnName.PROTEIN_RATIO_GRAPH,
									ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO_GRAPH).isVisible(),
									condition1, letter1, condition2, letter2, projectTag, ratioName);
							// proteinColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
							// ColumnName.PROTEIN_RATIO,
							// ratioCheckBoxName, condition1,
							// condition2,
							// projectTag, conditionsPanel);
							final String proteinRatioScoreName = ratioDescriptor.getProteinScoreName();
							if (proteinRatioScoreName != null) {
								proteinTablePanel.addColumnForConditionRatioScore(ColumnName.PROTEIN_RATIO_SCORE,
										ProteinColumns.getInstance().getColumn(ColumnName.PROTEIN_RATIO_SCORE)
												.isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName,
										proteinRatioScoreName);
								final String ratioScoreCheckBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForRatioScore(proteinRatioScoreName, ratioName);
								proteinColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PROTEIN_RATIO_SCORE,
										ratioScoreCheckBoxName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PROTEIN_RATIO_SCORE,
												condition1, letter1, condition2, letter2, projectTag, ratioName,
												proteinRatioScoreName));
								// proteinColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
								// ColumnName.PROTEIN_RATIO_SCORE,
								// ratioScoreCheckBoxName,
								// condition1, condition2,
								// projectTag, conditionsPanel);
							}
							break;
						case PEPTIDE:
							// add checkbox for ProteinRatio for
							// that ratio
							peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PEPTIDE_RATIO,
									ratioCheckBoxName, MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_RATIO,
											condition1, letter1, condition2, letter2, projectTag, ratioName));
							final String peptideRatioGraphCheckBoxName = MyVerticalCheckBoxListPanel
									.getCheckBoxNameForRatioGraph(ColumnName.PEPTIDE_RATIO_GRAPH, ratioName);
							peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PEPTIDE_RATIO_GRAPH,
									peptideRatioGraphCheckBoxName,
									MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_RATIO_GRAPH, condition1,
											letter1, condition2, letter2, projectTag, ratioName));
							final ColumnWithVisibility peptideRatioColumn = PeptideColumns.getInstance()
									.getColumn(ColumnName.PEPTIDE_RATIO);
							peptideOnlyTablePanel.addColumnForConditionRatio(ColumnName.PEPTIDE_RATIO,
									peptideRatioColumn.isVisible(), condition1, letter1, condition2, letter2,
									projectTag, ratioName);
							peptideOnlyTablePanel.addColumnForConditionRatio(ColumnName.PEPTIDE_RATIO_GRAPH,
									PeptideColumns.getInstance().getColumn(ColumnName.PEPTIDE_RATIO_GRAPH).isVisible(),
									condition1, letter1, condition2, letter2, projectTag, ratioName);
							if (!Pint.getPSMCentric()) {
								peptideTablePanel.addColumnForConditionRatio(ColumnName.PEPTIDE_RATIO,
										peptideRatioColumn.isVisible(), condition1, letter1, condition2, letter2,
										projectTag, ratioName);
								peptideTablePanel.addColumnForConditionRatio(ColumnName.PEPTIDE_RATIO_GRAPH,
										PeptideColumns.getInstance().getColumn(ColumnName.PEPTIDE_RATIO_GRAPH)
												.isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName);
							}
							// peptideColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
							// ColumnName.PEPTIDE_RATIO,
							// ratioCheckBoxName, condition1,
							// condition2,
							// projectTag, conditionsPanel);
							final String peptideRatioScoreName = ratioDescriptor.getPeptideScoreName();
							if (peptideRatioScoreName != null) {
								peptideOnlyTablePanel.addColumnForConditionRatioScore(ColumnName.PEPTIDE_RATIO_SCORE,
										PeptideColumns.getInstance().getColumn(ColumnName.PEPTIDE_RATIO_SCORE)
												.isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName,
										peptideRatioScoreName);
								if (!Pint.getPSMCentric()) {
									peptideTablePanel.addColumnForConditionRatioScore(ColumnName.PEPTIDE_RATIO_SCORE,
											PeptideColumns.getInstance().getColumn(ColumnName.PEPTIDE_RATIO_SCORE)
													.isVisible(),
											condition1, letter1, condition2, letter2, projectTag, ratioName,
											peptideRatioScoreName);
								}
								final String ratioScoreCheckBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForRatioScore(peptideRatioScoreName, ratioName);
								peptideColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PEPTIDE_RATIO_SCORE,
										ratioScoreCheckBoxName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PEPTIDE_RATIO_SCORE,
												condition1, letter1, condition2, letter2, projectTag, ratioName,
												peptideRatioScoreName));
								// peptideColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
								// ColumnName.PEPTIDE_RATIO_SCORE,
								// ratioScoreCheckBoxName,
								// condition1, condition2,
								// projectTag, conditionsPanel);
							}
							break;
						case PSM:
							if (Pint.getPSMCentric()) {
								// add checkbox for ProteinRatio
								// for
								// that ratio
								psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PSM_RATIO, ratioCheckBoxName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PSM_RATIO, condition1,
												letter1, condition2, letter2, projectTag, ratioName));
								final String psmRatioGraphCheckBoxName = MyVerticalCheckBoxListPanel
										.getCheckBoxNameForRatioGraph(ColumnName.PSM_RATIO_GRAPH, ratioName);
								psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PSM_RATIO_GRAPH,
										psmRatioGraphCheckBoxName,
										MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PSM_RATIO_GRAPH, condition1,
												letter1, condition2, letter2, projectTag, ratioName));
								psmTablePanel.addColumnForConditionRatio(ColumnName.PSM_RATIO,
										PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO).isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName);
								psmTablePanel.addColumnForConditionRatio(ColumnName.PSM_RATIO_GRAPH,
										PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO_GRAPH).isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName);
								psmOnlyTablePanel.addColumnForConditionRatio(ColumnName.PSM_RATIO,
										PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO).isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName);
								psmOnlyTablePanel.addColumnForConditionRatio(ColumnName.PSM_RATIO_GRAPH,
										PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO_GRAPH).isVisible(),
										condition1, letter1, condition2, letter2, projectTag, ratioName);
								// psmColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
								// ColumnName.PSM_RATIO,
								// ratioCheckBoxName,
								// condition1,
								// condition2,
								// projectTag, conditionsPanel);
								final String psmRatioScoreName = ratioDescriptor.getPsmScoreName();
								if (psmRatioScoreName != null) {
									psmTablePanel.addColumnForConditionRatioScore(ColumnName.PSM_RATIO_SCORE,
											PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO_SCORE).isVisible(),
											condition1, letter1, condition2, letter2, projectTag, ratioName,
											psmRatioScoreName);
									psmOnlyTablePanel.addColumnForConditionRatioScore(ColumnName.PSM_RATIO_SCORE,
											PSMColumns.getInstance().getColumn(ColumnName.PSM_RATIO_SCORE).isVisible(),
											condition1, letter1, condition2, letter2, projectTag, ratioName,
											psmRatioScoreName);
									final String ratioScoreCheckBoxName = MyVerticalCheckBoxListPanel
											.getCheckBoxNameForRatioScore(psmRatioScoreName, ratioName);
									psmColumnNamesPanel.addColumnCheckBoxByKeyName(ColumnName.PSM_RATIO_SCORE,
											ratioScoreCheckBoxName,
											MyVerticalCheckBoxListPanel.getKeyName(ColumnName.PSM_RATIO_SCORE,
													condition1, letter1, condition2, letter2, projectTag, ratioName,
													psmRatioScoreName));
									// psmColumnNamesPanel.addRatioConditionRelatedColumnCheckBoxHandler(
									// ColumnName.PSM_RATIO_SCORE,
									// ratioScoreCheckBoxName,
									// condition1,
									// condition2, projectTag,
									// conditionsPanel);
								}
							}
							break;

						default:
							break;
						}

						// add column handlers

					}
				}
			}

		}
		// set to false in order to force its application again
		// when performing a query
	}

	/**
	 * Given a conditionName and a project name, goes to the conditionsPanel and
	 * search for the symbol of the condition of that project
	 *
	 * @param conditionName
	 * @param projectName
	 * @return
	 */
	private String getProjectConditionSymbolOfCondition(String conditionName, String projectName) {

		final String projectSymbol = ClientDataUtil.parseProjectSymbolFromListBox(projectName,
				projectListPanel.getListBox());

		final ListBox listBox = conditionsPanel.getListBox();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			final String conditionString = listBox.getItemText(i);
			final String conditionName2 = listBox.getValue(i);
			if (conditionName2.equals(conditionName)) {
				final String projectSymbolFromCondition = ClientDataUtil
						.parseProjectSymbolFromConditionSelection(conditionString);
				if (projectSymbolFromCondition == null || "".equals(projectSymbolFromCondition)) {
					// just one project in the list
					return ClientDataUtil.parseConditionSymbolFromConditionSelection(conditionString);
				} else if (projectSymbolFromCondition.equals(projectSymbol)) {
					return projectSymbolFromCondition
							+ ClientDataUtil.parseConditionSymbolFromConditionSelection(conditionString);
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.Widget#onUnload()
	 */
	@Override
	protected void onUnload() {

		// close session
		// proteinRetrievingService.closeSession(sessionID,
		// new AsyncCallback<Void>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// StatusReportersRegister.getInstance()
		// .notifyStatusReporters(caught);
		//
		// }
		//
		// @Override
		// public void onSuccess(Void result) {
		// // TODO Auto-generated method stub
		// System.out.println("Session closed in server");
		// }
		// });
		super.onUnload();
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMessage(String message) {
		updateStatus(message);
	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		updateStatus(throwable);
	}

	@Override
	public String getStatusReporterKey() {
		return this.getClass().getName();
	}

}
