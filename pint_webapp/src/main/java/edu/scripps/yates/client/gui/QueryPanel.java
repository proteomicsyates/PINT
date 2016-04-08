package edu.scripps.yates.client.gui;

import java.util.ArrayList;
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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.cache.ClientCacheConditionsByProject;
import edu.scripps.yates.client.cache.ClientCacheDefaultViewByProjectTag;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.components.MyQueryEditorPanel;
import edu.scripps.yates.client.gui.components.MyVerticalCheckBoxListPanel;
import edu.scripps.yates.client.gui.components.MyVerticalConditionsListBoxPanel;
import edu.scripps.yates.client.gui.components.MyVerticalListBoxPanel;
import edu.scripps.yates.client.gui.components.MyVerticalProteinInferenceCommandPanel;
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
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.pseaquant.PSEAQuantFormPanel;
import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.gui.reactome.ReactomePanel;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.ShowHiddePanel;
import edu.scripps.yates.client.tasks.PendingTasks;
import edu.scripps.yates.client.tasks.TaskType;
import edu.scripps.yates.client.util.ClientGUIUtil;
import edu.scripps.yates.client.util.ProjectsBeanSet;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinProjection;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.TAB;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;
import edu.scripps.yates.shared.util.sublists.QueryResultSubLists;

public class QueryPanel extends Composite implements ShowHiddePanel {
	private static final double HEADER_HEIGHT = 25;
	private final java.util.logging.Logger log = java.util.logging.Logger.getLogger("NameOfYourLogger");

	private final TextArea textAreaStatus;
	private MyVerticalListBoxPanel projectListPanel;
	private MyVerticalConditionsListBoxPanel conditionsPanel;
	private MyVerticalProteinInferenceCommandPanel proteinGroupingCommandPanel;
	private final ProteinTablePanel proteinTablePanel;
	private final PSMTablePanel psmTablePanel;
	private final PSMTablePanel psmOnlyTablePanel;
	private final PeptideTablePanel peptideTablePanel;
	private final MyVerticalCheckBoxListPanel<ProteinBean> proteinColumnNamesPanel;
	private final MyVerticalCheckBoxListPanel<PSMBean> psmColumnNamesPanel;
	private final MyVerticalCheckBoxListPanel<PeptideBean> peptideColumnNamesPanel;
	private final ProteinGroupTablePanel proteinGroupTablePanel;
	private final MyVerticalCheckBoxListPanel<ProteinGroupBean> proteinGroupColumnNamesPanel;
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

	protected final edu.scripps.yates.client.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();

	private final Set<String> loadedProjects = new HashSet<String>();
	private final ProjectsBeanSet projectBeanSet = new ProjectsBeanSet();
	private MyDialogBox loadingDialog;

	private StackLayoutPanel menuUpStackLayoutPanel;

	private LayoutPanel layoutPanel;
	/**
	 * This variable will implement the doSomething() method in order to show or
	 * hidde the loading dialog depending on the pending tasks in client
	 */
	private DoSomethingTask<Void> loadingDialogShowerTask;
	private WindowBox welcomeToProjectWindowBox;
	private final AsyncPSMBeanListFromPsmProvider asyncDataProviderForPSMsOfSelectedProtein;
	private final AsyncPSMBeanListFromPsmProvider asyncDataProviderForPSMsOfSelectedPeptide;
	private String sessionID;
	private ProjectInformationPanel projectInformationPanel;
	private ScrollPanel scrollQueryPanel;
	private ScrollPanel scrollProjectInformationPanel;

	/**
	 * Returns true if the projects contained in the loadedProjects set is the
	 * same than the provided
	 *
	 * @param projectNames
	 * @return
	 */
	public boolean hasLoadedThisProjects(Set<String> projectNames) {
		for (String projectName : projectNames) {
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

	private static final ReactomePanel reactome = new ReactomePanel();

	public QueryPanel(String sessionID, Set<String> projectTags) {
		this(sessionID);
		loadProjectListFromServer(projectTags);
		// PSEA QUANT
		PSEAQuantFormPanel pseaQuantFormPanel = new PSEAQuantFormPanel(loadedProjects);
		ScrollPanel pseaQuantScrollPanel = new ScrollPanel(pseaQuantFormPanel);
		firstLevelTabPanel.add(pseaQuantScrollPanel, "PSEA-Quant");
		firstLevelTabPanel.add(reactome, "Reactome");
	}

	/**
	 * @wbp.parser.constructor
	 */
	private QueryPanel(String sessionID) {
		this.sessionID = sessionID;
		asyncDataProviderForPSMsOfSelectedProtein = new AsyncPSMBeanListFromPsmProvider(sessionID);
		asyncDataProviderForPSMsOfSelectedPeptide = new AsyncPSMBeanListFromPsmProvider(sessionID);
		DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);
		showLoadingDialog("Loading PINT components...");

		// HeaderPanel header = new HeaderPanel();
		// mainPanel.add(header);

		NavigationHorizontalPanel navigationPanel = new NavigationHorizontalPanel(TargetHistory.QUERY);
		mainPanel.addNorth(navigationPanel, 40.0);

		textAreaStatus = new TextArea();
		mainPanel.addSouth(textAreaStatus, 100.0);
		textAreaStatus.setSize("100%", "100%");
		textAreaStatus.setVisibleLines(4);
		textAreaStatus.setText("Starting data view...");
		textAreaStatus.setReadOnly(true);

		SplitLayoutPanel splitLayoutPanel = new SplitLayoutPanel(7);
		mainPanel.add(splitLayoutPanel);
		splitLayoutPanel.setStyleName("queryPanelMainPanel");
		SplitLayoutPanel splitPanelWest = new SplitLayoutPanel(7);
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
		firstLevelTabPanel = new ScrolledTabLayoutPanel(null);
		firstLevelTabPanel.setHeight("100%");
		// create the content panel
		// Widget[] contentWidgets = { mainDataTabPanel };
		FlowPanel contentPanel = new FlowPanel();
		contentPanel.add(firstLevelTabPanel);
		contentPanel.setStyleName("queryPanelDataTablesPanel");
		splitLayoutPanel.add(contentPanel);

		// ANNOTATIONS PANEL
		annotationsTypePanel = new MyVerticalListBoxPanel(false);
		annotationsTypePanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> annotationTypeNames = ClientGUIUtil
						.getSelectedItemsFromListBox(annotationsTypePanel.getListBox());
				if (annotationTypeNames != null) {
					String annotationType = annotationTypeNames.iterator().next();
					appendTextToQueryTextBox(annotationType);
				}

			}

		});

		// UNIPROT HEADER LINES
		uniprotHeaderLinesPanel = new MyVerticalListBoxPanel(false);
		uniprotHeaderLinesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> uniprotHeaderLines = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(uniprotHeaderLinesPanel.getListBox());
				if (uniprotHeaderLines != null) {
					String uniprotHeaderLine = uniprotHeaderLines.iterator().next();
					appendTextToQueryTextBox(uniprotHeaderLine);
				}

			}

		});

		// SCORE TYPES PANEL
		scoreTypesPanel = new MyVerticalListBoxPanel(false);
		scoreTypesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> scoreTypes = ClientGUIUtil.getSelectedItemsFromListBox(scoreTypesPanel.getListBox());
				if (scoreTypes != null) {
					String scoreType = scoreTypes.iterator().next();
					appendTextToQueryTextBox(scoreType);
				}

			}

		});
		// SCORE NAMES PANEL
		scoreNamesPanel = new MyVerticalListBoxPanel(false);
		scoreNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> scoreNames = ClientGUIUtil.getSelectedItemsFromListBox(scoreNamesPanel.getListBox());
				if (scoreNames != null) {
					String scoreType = scoreNames.iterator().next();
					appendTextToQueryTextBox(scoreType);
				}

			}

		});
		// THRESHOLD PANEL
		thresholdNamesPanel = new MyVerticalListBoxPanel(false);
		thresholdNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> commandNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(thresholdNamesPanel.getListBox());
				if (commandNames != null) {
					String commandFormatting = commandNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// RATIO NAMES PANEL
		ratioNamesPanel = new MyVerticalListBoxPanel(false);
		ratioNamesPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> ratioNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(ratioNamesPanel.getListBox());
				if (ratioNames != null) {
					String commandFormatting = ratioNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// PROJECT LIST PANEL
		projectListPanel = new MyVerticalListBoxPanel(true);
		projectListPanel.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> selectedProjects = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox((ListBox) event.getSource());
				if (selectedProjects != null) {
					String selectedProject = selectedProjects.iterator().next();
					appendTextToQueryTextBox(selectedProject);
				}

			}
		});
		// EXPERIMENTAL CONDITIONS PANEL
		conditionsPanel = new MyVerticalConditionsListBoxPanel(projectListPanel, true);
		conditionsPanel.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				Set<String> selectedConditionNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox((ListBox) event.getSource());
				if (selectedConditionNames != null) {
					String conditionName = selectedConditionNames.iterator().next();
					appendTextToQueryTextBox(conditionName);
				}
			}

		});
		conditionsPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (false) {
					// check the checkboxes to show the amounts and the ratios
					boolean showProteinGroupAmounts = proteinGroupColumnNamesPanel
							.isSelected(ColumnName.PROTEIN_AMOUNT);
					boolean showProteinAmounts = proteinColumnNamesPanel.isSelected(ColumnName.PROTEIN_AMOUNT);
					boolean showPeptideAmounts = peptideColumnNamesPanel.isSelected(ColumnName.PEPTIDE_AMOUNT);
					boolean showPSMAmounts = psmColumnNamesPanel.isSelected(ColumnName.PSM_AMOUNT);
					boolean showProteinGroupRatios = proteinGroupColumnNamesPanel.isSelected(ColumnName.PROTEIN_RATIO);
					boolean showProteinRatios = proteinColumnNamesPanel.isSelected(ColumnName.PROTEIN_RATIO);
					boolean showPeptideRatios = peptideColumnNamesPanel.isSelected(ColumnName.PEPTIDE_RATIO);
					boolean showPsmRatios = psmColumnNamesPanel.isSelected(ColumnName.PSM_RATIO);
					final ListBox listbox = (ListBox) event.getSource();
					if (!(showProteinGroupAmounts || showProteinGroupRatios || showProteinAmounts || showProteinRatios
							|| showPSMAmounts || showPsmRatios || showPeptideAmounts || showPeptideRatios))
						return;
					Set<String> selectedConditionNames = ClientGUIUtil.getSelectedItemsFromListBox(listbox);
					// show columns related to selected conditions
					if (selectedConditionNames != null && !selectedConditionNames.isEmpty()) {

						Map<String, Set<String>> conditionNamesByProjectName = new HashMap<String, Set<String>>();
						for (String selectedConditionName : selectedConditionNames) {
							String conditionName = SharedDataUtils
									.getConditionNameFromConditionSelection(selectedConditionName);
							String projectSymbol = SharedDataUtils
									.getProjectSymbolFromConditionSelection(selectedConditionName);
							String projectName = SharedDataUtils.getProjectNameFromListBox(projectSymbol,
									projectListPanel.getListBox());
							if (conditionNamesByProjectName.containsKey(projectName)) {
								conditionNamesByProjectName.get(projectName).add(conditionName);
							} else {
								Set<String> set = new HashSet<String>();
								set.add(conditionName);
								conditionNamesByProjectName.put(projectName, set);
							}
						}
						// iterate over the map
						for (String projectName : conditionNamesByProjectName.keySet()) {
							final Set<String> conditionNames = conditionNamesByProjectName.get(projectName);
							if (showProteinGroupAmounts)
								proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
										conditionNames, projectName, true);
							if (showProteinGroupRatios)
								proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
										conditionNames, projectName, true);
							if (showProteinAmounts)
								proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
										conditionNames, projectName, true);
							if (showProteinRatios)
								proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
										conditionNames, projectName, true);
							if (showPeptideAmounts)
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
										conditionNames, projectName, true);
							if (showPeptideRatios)
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
										conditionNames, projectName, true);
							if (showPSMAmounts) {
								psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
										conditionNames, projectName, true);
								psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
										conditionNames, projectName, true);
							}
							if (showPsmRatios) {
								psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
										conditionNames, projectName, true);
								psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
										conditionNames, projectName, true);
							}
						}

					}

					// Hide columns related to non selected conditions
					Set<String> nonSelectedConditionNames = ClientGUIUtil.getNonSelectedItemsFromListBox(listbox);
					if (nonSelectedConditionNames != null) {

						Map<String, Set<String>> conditionNamesByProjectName = new HashMap<String, Set<String>>();
						for (String nonSelectedConditionName : nonSelectedConditionNames) {
							String conditionName = SharedDataUtils
									.getConditionNameFromConditionSelection(nonSelectedConditionName);
							String projectSymbol = SharedDataUtils
									.getProjectSymbolFromConditionSelection(nonSelectedConditionName);
							String projectName = SharedDataUtils.getProjectNameFromListBox(projectSymbol,
									projectListPanel.getListBox());
							if (conditionNamesByProjectName.containsKey(projectName)) {
								conditionNamesByProjectName.get(projectName).add(conditionName);
							} else {
								Set<String> set = new HashSet<String>();
								set.add(conditionName);
								conditionNamesByProjectName.put(projectName, set);
							}
						}
						// iterate over the map
						for (String projectName : conditionNamesByProjectName.keySet()) {
							Set<String> conditionNames = conditionNamesByProjectName.get(projectName);
							if (showProteinGroupAmounts)
								proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
										conditionNames, projectName, false);
							if (showProteinGroupRatios)
								proteinGroupTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
										conditionNames, projectName, false);
							if (showProteinAmounts)
								proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_AMOUNT,
										conditionNames, projectName, false);
							if (showProteinRatios)
								proteinTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PROTEIN_RATIO,
										conditionNames, projectName, false);
							if (showPeptideAmounts)
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_AMOUNT,
										conditionNames, projectName, false);
							if (showPeptideRatios)
								peptideTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PEPTIDE_RATIO,
										conditionNames, projectName, false);
							if (showPSMAmounts) {
								psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
										conditionNames, projectName, false);
								psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_AMOUNT,
										conditionNames, projectName, false);
							}
							if (showPsmRatios) {
								psmTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
										conditionNames, projectName, false);
								psmOnlyTablePanel.showOrHideExperimentalConditionColumn(ColumnName.PSM_RATIO,
										conditionNames, projectName, false);
							}
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
				Set<String> commandNames = ProjectCreatorWizardUtil
						.getSelectedItemValuesFromListBox(commandsPanel.getListBox());
				if (commandNames != null) {
					String commandFormatting = commandNames.iterator().next();
					appendTextToQueryTextBox(commandFormatting);
				}

			}

		});
		// PROTEIN GROUPING
		proteinGroupingCommandPanel = new MyVerticalProteinInferenceCommandPanel(
				createStartGroupingButtonClickHandler());

		proteinTablePanel = new ProteinTablePanel(sessionID, false, false);

		proteinColumnNamesPanel = new MyVerticalCheckBoxListPanel<ProteinBean>(proteinTablePanel.getColumnManager());

		// PSM COLUMN NAMES
		psmTablePanel = new PSMTablePanel("Select one protein to load PSMs", null,
				asyncDataProviderForPSMsOfSelectedProtein);
		layoutPanel = new LayoutPanel();
		layoutPanel.add(psmTablePanel);
		layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 50, Unit.PCT);
		psmColumnNamesPanel = new MyVerticalCheckBoxListPanel<PSMBean>(psmTablePanel.getColumnManager());

		// PROTEIN GROUP PANEL
		proteinGroupTablePanel = new ProteinGroupTablePanel(sessionID, false, false,
				getSelectProteinGroupingMenuHandler());

		proteinGroupColumnNamesPanel = new MyVerticalCheckBoxListPanel<ProteinGroupBean>(
				proteinGroupTablePanel.getColumnManager());

		// PSM ONLY TAB
		psmOnlyTablePanel = new PSMTablePanel(null, this, new AsyncPSMBeanListDataProvider(sessionID));
		psmColumnNamesPanel.addColumnManager(psmOnlyTablePanel.getColumnManager());

		// PEPTIDE ONLY TAB
		peptideTablePanel = new PeptideTablePanel(null, this, new AsyncPeptideBeanListDataProvider(sessionID));
		peptideColumnNamesPanel = new MyVerticalCheckBoxListPanel<PeptideBean>(peptideTablePanel.getColumnManager());
		peptideColumnNamesPanel.addColumnManager(peptideTablePanel.getColumnManager());
		// DATA PANEL: this panel will contain the protein/protein groups in the
		// top half and the PSMs in the botton half

		// create a handler in order to retrieve the PSMs from a selected
		// peptide and add them to the psm table
		peptideTablePanel.addPeptideSelectionHandler(createPeptideSelectionHandler());

		// create a handler in order to retrieve the PSMs from a selected
		// protein and add them to the psm table
		proteinTablePanel.addProteinSelectionHandler(createProteinSelectionHandler());

		// create a handler in order to retrieve the PSMs from a selected
		// protein and add them to the psm table
		proteinGroupTablePanel.addProteinSelectionHandler(createProteinGroupSelectionHandler());

		// two tabs, one for the protein and the other for the protein groups
		secondLevelTabPanel = new ScrolledTabLayoutPanel(psmOnlyTablePanel);
		secondLevelTabPanel.add(proteinGroupTablePanel, "Protein Groups");
		secondLevelTabPanel.add(proteinTablePanel, "Proteins");
		secondLevelTabPanel.add(peptideTablePanel, "Peptides");
		secondLevelTabPanel.add(psmOnlyTablePanel, "PSMs");
		secondLevelTabPanel.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				final Integer item = event.getSelectedItem();
				if (item != null) {
					if (item == 0) {
						proteinGroupTablePanel.forceReloadData();
						proteinGroupTablePanel.refreshData();
					} else if (item == 1) {
						proteinTablePanel.forceReloadData();
						proteinTablePanel.refreshData();
					} else if (item == 2) {
						peptideTablePanel.forceReloadData();
						peptideTablePanel.refreshData();
					} else if (item == 3) {
						psmOnlyTablePanel.forceReloadData();
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
				sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion());

			}
		};
		final ClickHandler sendSimpleQueryByProteinNameClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String queryText = queryEditorPanel.getTranslatedQueryFromProteinName();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion());
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		final ClickHandler sendSimpleQueryByAccClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String queryText = queryEditorPanel.getTranslatedQueryFromAcc();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion());
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		final ClickHandler sendSimpleQueryByGeneNameClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String queryText = queryEditorPanel.getTranslatedQueryFromGeneName();
				if (queryText != null) {
					// set the query into the regular query editor
					queryEditorPanel.getComplexQueryTextBox().setText(queryText);
					// send query to server
					sendQueryToServer(queryText, queryEditorPanel.getSelectedUniprotVersion());
				} else {
					updateStatus("The query is not valid. Only suggested values are allowed in a simple query.");
				}
			}
		};
		queryEditorPanel = new MyQueryEditorPanel(sendQueryclickHandler, sendSimpleQueryByProteinNameClickHandler,
				sendSimpleQueryByAccClickHandler, sendSimpleQueryByGeneNameClickHandler);

		projectInformationPanel = new ProjectInformationPanel(this);
		scrollProjectInformationPanel = new ScrollPanel(projectInformationPanel);
		firstLevelTabPanel.add(scrollProjectInformationPanel, "Project information");
		scrollQueryPanel = new ScrollPanel(queryEditorPanel);
		firstLevelTabPanel.add(scrollQueryPanel, "Query");
		firstLevelTabPanel.add(layoutPanel, "Data view");

		menuUpStackLayoutPanel.add(commandsPanel, "Query commands", HEADER_HEIGHT);
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
		menuUpStackLayoutPanel.add(new ScrollPanel(psmColumnNamesPanel), "PSM columns", HEADER_HEIGHT);

		initStackPanel();

		// hiddeLoadingDialog();
		setStyleName("MainPanel");

		// select data tab
		firstLevelTabPanel.selectTab(layoutPanel);
	}

	private MouseOverHandler getSelectProteinGroupingMenuHandler() {
		MouseOverHandler ret = new MouseOverHandler() {

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
			selectQueryTab();
		}
		// proteinTablePanel.getDataGrid().setFocus(true);
		// proteinTablePanel.getDataGrid().redraw();
	}

	private void selectQueryTab() {
		firstLevelTabPanel.selectTab(scrollQueryPanel);
	}

	private ClickHandler createStartGroupingButtonClickHandler() {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				proteinGroupTablePanel.clearTable();

				final String logMessage = TaskType.GROUP_PROTEINS.getSingleTaskMessage("");
				updateStatus(logMessage);

				// add PendingTask
				PendingTasks.addPendingTask(TaskType.GROUP_PROTEINS, "");
				getLoadingDialogShowerTask().doSomething();
				proteinGroupTablePanel.clearTable();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						GWT.log("Sending proteins to group");
						proteinRetrievingService.groupProteins(sessionID,
								proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(),
								proteinGroupTablePanel.getDataGrid().getPageSize(),
								new AsyncCallback<ProteinGroupBeanSubList>() {

									@Override
									public void onFailure(Throwable caught) {
										updateStatus(caught);
										// remove PendingTask
										PendingTasks.removeTask(TaskType.GROUP_PROTEINS, "");
										getLoadingDialogShowerTask().doSomething();
									}

									@Override
									public void onSuccess(ProteinGroupBeanSubList proteinGroupSubList) {
										if (proteinGroupSubList != null) {
											updateStatus("Proteins grouped into " + proteinGroupSubList.getTotalNumber()
													+ " groups");
											loadProteinGroupsOnGrid(0, proteinGroupSubList);
										}
										// remove PendingTask
										PendingTasks.removeTask(TaskType.GROUP_PROTEINS, "");
										getLoadingDialogShowerTask().doSomething();
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
			getLoadingDialogShowerTask().doSomething();
		} else {
			// fill the grid
			proteinGroupTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(), true);
			proteinGroupTablePanel.getAsyncDataProvider().updateRowData(start, result.getDataList());
			proteinGroupTablePanel.forceReloadData();
			proteinGroupTablePanel.refreshData();
		}
	}

	private Handler createProteinSelectionHandler() {
		final SelectionChangeEvent.Handler handler = new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				GWT.log("Selection event in protein table");
				// psmTablePanel.clearTable();
				psmTablePanel.setLoadingWidget();
				final Object source = event.getSource();

				if (source instanceof SingleSelectionModel) {
					GWT.log("protein table with a single selection model");

					SingleSelectionModel<ProteinBean> selectionModel = (SingleSelectionModel<ProteinBean>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						ProteinBean selectedProtein = (ProteinBean) selectedObject;
						asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedProtein);
						psmTablePanel.forceReloadData();
						psmTablePanel.refreshData();
					}
					Widget widget = null;
					psmTablePanel.setEmptyTableWidget(widget);

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

					SingleSelectionModel<PeptideBean> selectionModel = (SingleSelectionModel<PeptideBean>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						PeptideBean selectedPeptide = (PeptideBean) selectedObject;
						asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedPeptide);
						psmTablePanel.forceReloadData();
						psmTablePanel.refreshData();
					}
					Widget widget = null;
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
				psmTablePanel.setLoadingWidget();
				if (source instanceof SingleSelectionModel) {
					SingleSelectionModel<ProteinGroupBean> selectionModel = (SingleSelectionModel<ProteinGroupBean>) source;
					final Object selectedObject = selectionModel.getSelectedObject();
					if (selectedObject != null) {
						ProteinGroupBean selectedProteinGroup = (ProteinGroupBean) selectedObject;
						asyncDataProviderForPSMsOfSelectedProtein.setPSMProvider(selectedProteinGroup);
						psmTablePanel.forceReloadData();
						psmTablePanel.refreshData();

					}
					Widget widget = null;
					psmTablePanel.setEmptyTableWidget(widget); // set default
					// widget
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

		// in case of text area, scroll down
		if (textElement instanceof TextArea) {
			final TextArea textAreaToScroll = (TextArea) textElement;
			ClientGUIUtil.scrollToBottom(textAreaToScroll.getElement());
			// textAreaToScroll.getElement().setScrollTop(
			// textAreaToScroll.getElement().getScrollHeight());

		}
	}

	private String insertTextInCursorPosition(String oldText, String toInsert, int cursorPos, String selectedText) {
		if ("".equals(oldText))
			return toInsert;
		if ("".equals(selectedText)) {
			String left = oldText.substring(0, cursorPos);
			String rigth = oldText.substring(cursorPos, oldText.length());
			return left + toInsert + rigth;
		} else {
			String left = oldText.substring(0, cursorPos);
			String rigth = oldText.substring(cursorPos + selectedText.length(), oldText.length());
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
						if (result != null) {
							String fileName = SafeHtmlUtils.htmlEscape(result.getName());
							String fileSizeString = result.getSize();
							queryEditorPanel.setSendingStatusText(null);
							final String href = GWT.getModuleBaseURL() + "download?" + SharedConstants.FILE_TO_DOWNLOAD
									+ "=" + fileName + "&" + SharedConstants.FILE_TYPE + "="
									+ SharedConstants.ID_DATA_FILE_TYPE;
							queryEditorPanel.setLinksToResultsVisible(true);
							queryEditorPanel.setLinksToProteinResults(href, "Proteins [" + fileSizeString + "]");
						}
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
						if (result != null) {
							String fileName = SafeHtmlUtils.htmlEscape(result.getName());
							String fileSizeString = result.getSize();
							queryEditorPanel.setSendingStatusText(null);
							final String href = GWT.getModuleBaseURL() + "download?" + SharedConstants.FILE_TO_DOWNLOAD
									+ "=" + fileName + "&" + SharedConstants.FILE_TYPE + "="
									+ SharedConstants.ID_DATA_FILE_TYPE;
							queryEditorPanel.setLinksToResultsVisible(true);
							queryEditorPanel.setLinksToProteinGroupResults(href,
									"Protein groups [" + fileSizeString + "]");
						}
					}
				});
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
							List<String> list = new ArrayList<String>();
							if (!result.isEmpty())
								list.addAll(result);
							Collections.sort(list, new Comparator<String>() {
								@Override
								public int compare(String o1, String o2) {
									return o2.compareTo(o1);
								}
							});
							for (String uniprotVersion : list) {
								queryEditorPanel.addUniprotVersion(uniprotVersion);
							}
						}
					});

		}

	}

	protected Set<String> getNotCachedProjects(Set<String> selectedProjects) {
		Set<String> ret = new HashSet<String>();
		for (String selectedProject : selectedProjects) {
			if (!loadedProjects.contains(selectedProject)) {
				ret.add(selectedProject);
			}
		}

		return ret;
	}

	protected void sendQueryToServer(final String queryText, String uniprotVersion) {

		// set empty table widget on PSM table
		psmTablePanel.setEmptyTableWidget(PSMTablePanel.SELECT_PROTEIN_TO_LOAD_PSMS_TEXT);
		proteinGroupTablePanel.clearTable();
		proteinTablePanel.clearTable();
		psmTablePanel.clearTable();
		psmOnlyTablePanel.clearTable();
		peptideTablePanel.clearTable();
		if ("".equals(queryText)) {
			updateStatus("Empty query. Loading whole project...");
			loadProteinsFromProject(uniprotVersion, null);
			return;
		}

		final String logMessage = "Waiting for query results...";
		proteinGroupTablePanel.setEmptyTableWidget(logMessage);
		proteinTablePanel.setEmptyTableWidget(logMessage);
		psmOnlyTablePanel.setEmptyTableWidget(logMessage);
		peptideTablePanel.setEmptyTableWidget(logMessage);
		psmTablePanel.setEmptyTableWidget(logMessage);

		queryEditorPanel.setSendingStatusText(logMessage);
		queryEditorPanel.setLinksToResultsVisible(false);
		queryEditorPanel.updateQueryResult(null);
		// add pending task
		PendingTasks.addPendingTask(TaskType.QUERY_SENT, queryText);
		getLoadingDialogShowerTask().doSomething();
		// set status
		updateStatus("Sending query '" + queryText + "' to server...");
		// send query to server
		proteinRetrievingService.getProteinsFromQuery(sessionID, queryText, loadedProjects,
				proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), true,
				new AsyncCallback<QueryResultSubLists>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
						queryEditorPanel.setSendingStatusText(null);
						// remove pending task
						PendingTasks.removeTask(TaskType.QUERY_SENT, queryText);
						getLoadingDialogShowerTask().doSomething();
					}

					@Override
					public void onSuccess(QueryResultSubLists result) {
						String nullString = null;
						queryEditorPanel.updateQueryResult(result);
						proteinGroupTablePanel.setEmptyTableWidget(nullString);
						proteinTablePanel.setEmptyTableWidget(nullString);
						psmOnlyTablePanel.setEmptyTableWidget(nullString);
						peptideTablePanel.setEmptyTableWidget(nullString);
						psmTablePanel.setEmptyTableWidget(PSMTablePanel.SELECT_PROTEIN_TO_LOAD_PSMS_TEXT);

						updateStatus(result.getPsmSubList().getTotalNumber() + " psms received.");
						updateStatus(result.getPeptideSubList().getTotalNumber() + " peptides received.");
						updateStatus(result.getProteinSubList().getTotalNumber() + " proteins received.");
						updateStatus(result.getProteinGroupSubList().getTotalNumber() + " protein groups received.");

						loadProteinsOnGrid(0, result.getProteinSubList());
						loadPSMsOnlyOnGrid(0, result.getPsmSubList());
						loadPeptidesOnlyOnGrid(0, result.getPeptideSubList());
						loadProteinGroupsOnGrid(0, result.getProteinGroupSubList());

						// remove pending task
						PendingTasks.removeTask(TaskType.QUERY_SENT, queryText);
						getLoadingDialogShowerTask().doSomething();
						if (result.getPsmSubList().getTotalNumber() > 0) {
							prepareDownloadLinksForQuery();
						}
					}
				});

	}

	private void loadConditionsFromProjects(final Set<String> selectedProjects) {
		conditionsPanel.clearList();
		if (selectedProjects != null && !selectedProjects.isEmpty()) {
			Set<String> notCachedProject = new HashSet<String>();
			for (String selectedProject : selectedProjects) {
				if (ClientCacheConditionsByProject.getInstance().contains(selectedProject)) {
					final Set<String> conditionsFromProject = ClientCacheConditionsByProject.getInstance()
							.getFromCache(selectedProject);
					List<String> sortedList = new ArrayList<String>();
					sortedList.addAll(conditionsFromProject);
					Collections.sort(sortedList);

					for (String conditionName : sortedList) {
						String newElementName = SharedDataUtils.getNewElementNameForCondition(conditionName,
								selectedProject, conditionsPanel.getListBox(), projectListPanel.getListBox());
						conditionsPanel.addItem(newElementName, conditionName);
					}
				} else {
					notCachedProject.add(selectedProject);
				}
			}
			proteinGroupTablePanel.removeColumn(ColumnName.PROTEIN_AMOUNT);
			proteinTablePanel.removeColumn(ColumnName.PROTEIN_AMOUNT);
			psmTablePanel.removeColumn(ColumnName.PSM_AMOUNT);
			psmOnlyTablePanel.removeColumn(ColumnName.PSM_AMOUNT);
			peptideTablePanel.removeColumn(ColumnName.PEPTIDE_AMOUNT);
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
									List<String> sortedConditionNames = new ArrayList<String>();
									sortedConditionNames.addAll(result);
									java.util.Collections.sort(sortedConditionNames);

									for (String conditionName : sortedConditionNames) {
										String newElementName = SharedDataUtils.getNewElementNameForCondition(
												conditionName, projectTag, conditionsPanel.getListBox(),
												projectListPanel.getListBox());

										// add to the experimeental
										// condition panel
										String letter = SharedDataUtils
												.getConditionSymbolFromConditionElement(newElementName);
										String number = SharedDataUtils
												.getProjectSymbolFromConditionSelection(newElementName);
										letter = number + letter;
										conditionsPanel.addItem(newElementName, conditionName);

										addAmountColumns(projectTag, conditionName, letter);

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

	private void addAmountColumns(final String projectTag, final String conditionName, final String letter) {
		addPSMAmountColumns(projectTag, conditionName, letter);
		addPeptideAmountColumns(projectTag, conditionName, letter);
		addProteinAmountColumns(projectTag, conditionName, letter);
	}

	private void addPSMAmountColumns(final String projectTag, final String conditionName, final String letter) {

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
							for (AmountType amountType : result) {
								psmTablePanel.addColumnforConditionPSMAmount(conditionName, amountType, letter,
										projectTag);
								psmOnlyTablePanel.addColumnforConditionPSMAmount(conditionName, amountType, letter,
										projectTag);

							}
							if (!result.isEmpty()) {
								// add handlers related to that
								// condition, for the psm and
								// the protein tables
								psmColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(ColumnName.PSM_AMOUNT,
										conditionName, projectTag, conditionsPanel);

							}
						}
					}
				});

	}

	private void addPeptideAmountColumns(final String projectTag, final String conditionName, final String letter) {

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
							for (AmountType amountType : result) {
								peptideTablePanel.addColumnforConditionPeptideAmount(conditionName, amountType, letter,
										projectTag);
							}
							if (!result.isEmpty()) {
								// add handlers related to that
								// condition, for the psm and
								// the protein tables
								peptideColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
										ColumnName.PEPTIDE_AMOUNT, conditionName, projectTag, conditionsPanel);

							}
						}
					}
				});

	}

	private void addProteinAmountColumns(final String projectTag, final String conditionName, final String letter) {

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
							for (AmountType amountType : result) {
								proteinTablePanel.addColumnforConditionProteinAmount(conditionName, amountType, letter,
										projectTag);
								proteinGroupTablePanel.addColumnforConditionProteinAmount(conditionName, amountType,
										letter, projectTag);
							}
							if (!result.isEmpty()) {// add handlers related to
													// that
								// condition, for the psm and
								// the protein tables
								proteinColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
										ColumnName.PROTEIN_AMOUNT, conditionName, projectTag, conditionsPanel);
								proteinGroupColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
										ColumnName.PROTEIN_AMOUNT, conditionName, projectTag, conditionsPanel);
							}
						}
					}
				});

	}

	private void loadPSMScoreNames(Set<String> selectedProjects) {
		scoreTypesPanel.clearList();
		proteinRetrievingService.getPSMScoreNamesFromProjects(selectedProjects, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				addPSMScoreNames(result);
			}
		});
		proteinRetrievingService.getPTMScoreNamesFromProjects(selectedProjects, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				addPTMScoreNames(result);
			}
		});

	}

	private void loadPSMScoreTypes(Set<String> selectedProjects) {
		scoreTypesPanel.clearList();
		proteinRetrievingService.getPSMScoreTypesFromProjects(selectedProjects, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				addPSMScoreTypes(result);
			}
		});
		proteinRetrievingService.getPTMScoreTypesFromProjects(selectedProjects, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				updateStatus(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				addPSMScoreTypes(result);
			}
		});

	}

	private void addPSMScoreTypes(List<String> result) {
		if (!result.isEmpty()) {
			Collections.sort(result);
			for (String scoreType : result) {
				scoreTypesPanel.getListBox().addItem(scoreType);
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " score types loaded.");
		}
	}

	private void addPSMScoreNames(List<String> result) {
		if (!result.isEmpty()) {
			Collections.sort(result);
			for (String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				psmTablePanel.addColumnforPSMScore(scoreName);
				psmOnlyTablePanel.addColumnforPSMScore(scoreName);
			}
			// add to suggestions in the query editor
			queryEditorPanel.addSuggestionsToComplexQuery(result);
			updateStatus(result.size() + " PSM scores loaded.");
		}
	}

	private void addPTMScoreNames(List<String> result) {
		if (!result.isEmpty()) {
			Collections.sort(result);
			for (String scoreName : result) {
				scoreNamesPanel.getListBox().addItem(scoreName);
				psmTablePanel.addColumnforPTMScore(scoreName);
				psmOnlyTablePanel.addColumnforPTMScore(scoreName);
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
					List<String> list = new ArrayList<String>();
					list.addAll(result);
					Collections.sort(list);
					for (String thresholdNameType : list) {
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
					List<String> ordererList = new ArrayList<String>();
					ordererList.addAll(keySet);
					Collections.sort(ordererList);
					for (String uniprotHeaderKey : ordererList) {
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
					for (String projectName : result) {
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
	 * Remove the information of the older loaded projects and load the
	 * information of the new ones.
	 *
	 * @param projectTags
	 */
	public void loadProjectListFromServer(final Set<String> projectTags) {

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
		projectBeanSet.clear();
		loadedProjects.addAll(projectTags);
		for (final String projectTag : projectTags) {
			proteinRetrievingService.getProjectBean(projectTag, new AsyncCallback<ProjectBean>() {

				@Override
				public void onFailure(Throwable caught) {
					PendingTasks.removeTask(TaskType.PROTEINS_BY_PROJECT, projectTag);

					loadedProjects.remove(projectTag);
					loadingDialog.hide();
					updateStatus(caught);
					MyDialogBox errorDialog = MyDialogBox.getInstance(
							"Error loading project: '" + projectTag + "':\n" + caught.getMessage(), true, false, false);
					errorDialog.center();
				}

				@Override
				public void onSuccess(ProjectBean projectBean) {
					projectBeanSet.add(projectBean);

					GWT.log("project bean " + projectBean.getTag() + " received");
					if (projectTags.size() > 1) {
						projectListPanel.getListBox().addItem(
								SharedDataUtils.getNewElementNameForProject(projectTag, projectListPanel.getListBox()),
								projectTag);
					} else {
						projectListPanel.getListBox().addItem(projectTag, projectTag);
					}
					// if all projects are received
					if (projectBeanSet.size() == loadedProjects.size()) {
						// show the welcome box
						boolean shorWelcome = true;
						final ProjectBean projectBeanForDisplay = projectBeanSet.getBigProject() != null
								? projectBeanSet.getBigProject() : projectBean;
						requestDefaultViews(projectBeanForDisplay, false, shorWelcome, false,
								projectBeanSet.containsBigProject());
						// load the project
						if (projectBeanSet.containsBigProject()) {
							// disable queries for big projects. Only allow
							// simple ones.
							queryEditorPanel.enableQueries(false);
							// change default empty message on tables
							String emptyLabelString = "Project will not be loaded by default. Please go to 'Query' tab and use the 'Simple Query Editor'";
							proteinTablePanel.setEmptyTableWidget(emptyLabelString);
							proteinGroupTablePanel.setEmptyTableWidget(emptyLabelString);
							peptideTablePanel.setEmptyTableWidget(emptyLabelString);
							psmTablePanel.setEmptyTableWidget(emptyLabelString);
							// change tab to query
							selectQueryTab();
						} else {
							loadProteinsFromProject(null, null);
						}
					}
				}
			});
		}

		loadStaticDataFromProjects(projectTags);
		loadSimpleQuerySuggestions(projectTags);
	}

	private void loadSimpleQuerySuggestions(Set<String> projectTags) {
		// load protein projections in the projects
		for (String projectTag : projectTags) {
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
		loadRatioDescriptors(projectNames);

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
				List<String> orderedList = new ArrayList<String>();
				orderedList.addAll(keySet);
				Collections.sort(orderedList);
				for (String commandAbbreviature : orderedList) {
					String formattedCommand = result.get(commandAbbreviature);
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
	 * @param uniprotVersion
	 *            if null, it will use the latest
	 * @param defaultQueryIndex
	 *            if not null, it will ask for the default query defined at that
	 *            index in the array of default queries
	 */
	public void loadProteinsFromProject(final String uniprotVersion, Integer defaultQueryIndex) {
		// emtpy protein group panel
		proteinGroupTablePanel.clearTable();
		proteinGroupTablePanel.setVisible(false);
		proteinTablePanel.setVisible(true);
		proteinTablePanel.setEmptyTableWidget("Please wait. Proteins will be loaded in the table...");
		psmTablePanel.setEmptyTableWidget("Please wait. PSMs will be loaded in the table...");
		proteinGroupTablePanel.setEmptyTableWidget("Please wait. Protein groups will be loaded in the table...");
		psmOnlyTablePanel.setEmptyTableWidget("Please wait. PSMs will be loaded in the table...");
		peptideTablePanel.setEmptyTableWidget("Please wait. Peptides will be loaded in the table...");

		if (loadedProjects == null || loadedProjects.isEmpty()) {
			// loadProteinsOnGrid(null);
			loadProteinsOnGrid(0, null);
		} else {
			String plural = "";
			if (loadedProjects.size() > 1)
				plural = "s";
			String logMessage = "Getting proteins from project" + plural;
			if (defaultQueryIndex != null) {
				logMessage = "Getting proteins from query";
			}
			logMessage += "...";

			// showLoadingDialog(logMessage);
			updateStatus(logMessage);

			// clear proteins in the table
			proteinTablePanel.clearTable();
			// clear psms in the table
			psmTablePanel.clearTable();
			// emtpy PSM only panel
			psmOnlyTablePanel.clearTable();
			// emtpy Peptide only panel
			peptideTablePanel.clearTable();
			// enable PSM only tab
			psmOnlyTablePanel.setEmptyTableWidget("Please wait for PSMs to be loaded...");
			// enable Peptide only tab
			peptideTablePanel.setEmptyTableWidget("Please wait for Peptides to be loaded...");
			if (!loadedProjects.isEmpty()) {

				PendingTasks.addPendingTasks(TaskType.PROTEINS_BY_PROJECT, loadedProjects);
				getLoadingDialogShowerTask().doSomething();
				// remove the score columns for psms
				psmTablePanel.removeColumn(ColumnName.PSM_SCORE);
				psmOnlyTablePanel.removeColumn(ColumnName.PSM_SCORE);
				peptideTablePanel.removeColumn(ColumnName.PEPTIDE_SCORE);
				psmTablePanel.removeColumn(ColumnName.PTM_SCORE);
				psmOnlyTablePanel.removeColumn(ColumnName.PTM_SCORE);

				scoreNamesPanel.clearList();
				scoreTypesPanel.clearList();

				// final ProteinsByProjectLoadingDialog progressDialog = new
				// ProteinsByProjectLoadingDialog(sessionID,
				// projectTag, uniprotVersion);
				StringBuilder sb = new StringBuilder();
				for (final String projectTag : loadedProjects) {
					if (!"".equals(sb.toString())) {
						sb.append(",");
					}
					sb.append(projectTag);
				}
				String projectTagsString = sb.toString();
				GWT.log("Getting proteins from project " + projectTagsString);
				proteinRetrievingService.getProteinsFromProjects(sessionID, loadedProjects, uniprotVersion,
						proteinGroupingCommandPanel.isSeparateNonConclusiveProteins(), defaultQueryIndex,
						new AsyncCallback<QueryResultSubLists>() {

							@Override
							public void onFailure(Throwable caught) {
								// progressDialog.finishAndHide();
								updateStatus(caught);
								// removePending task
								PendingTasks.removeTasks(TaskType.PROTEINS_BY_PROJECT, loadedProjects);
								// show loading dialog if there are pending
								// tasks
								getLoadingDialogShowerTask().doSomething();
							}

							@Override
							public void onSuccess(QueryResultSubLists result) {
								try {
									// progressDialog.finishAndHide(2000);
									queryEditorPanel.updateQueryResult(result);
									if (result != null) {

										updateStatus(result.getPsmSubList().getTotalNumber() + " psms received.");
										updateStatus(
												result.getPeptideSubList().getTotalNumber() + " peptides received.");
										updateStatus(
												result.getProteinSubList().getTotalNumber() + " proteins received.");
										updateStatus(result.getProteinGroupSubList().getTotalNumber()
												+ " protein groups received.");

										addPSMScoreNames(result.getPSMScoreNames());
										addPTMScoreNames(result.getPTMScoreNames());
										addPSMScoreTypes(result.getPSMScoreTypes());

										// apply default views
										final String projectTag = loadedProjects.iterator().next();
										if (!loadedProjects.isEmpty() && ClientCacheDefaultViewByProjectTag
												.getInstance().contains(projectTag)) {
											final DefaultView defaultView = ClientCacheDefaultViewByProjectTag
													.getInstance().getFromCache(projectTag);
											applyDefaultViews(projectBeanSet.getByTag(projectTag), defaultView, true,
													false, true, false);
										}

										// load on the grid
										// loadProteinsOnGrid(result);
										loadProteinsOnGrid(0, result.getProteinSubList());
										loadPSMsOnlyOnGrid(0, result.getPsmSubList());
										loadPeptidesOnlyOnGrid(0, result.getPeptideSubList());
										loadProteinGroupsOnGrid(0, result.getProteinGroupSubList());

										if (result.isEmpty()) {
											psmOnlyTablePanel.setEmptyTableWidget("No data to show");
											peptideTablePanel.setEmptyTableWidget("No data to show");
											proteinTablePanel.setEmptyTableWidget("No data to show");
											proteinGroupTablePanel.setEmptyTableWidget("No data to show");
											psmTablePanel.setEmptyTableWidget("No data to show");
										}
									}
								} finally {
									// removePending task
									PendingTasks.removeTasks(TaskType.PROTEINS_BY_PROJECT, loadedProjects);
									// show loading dialog if there are pending
									// tasks
									getLoadingDialogShowerTask().doSomething();
								}
							}
						});

			} else {
			}
		}

	}

	@Override
	public void hiddePanel() {
		// hidde psmtable, thae one that is below the proteins
		if (layoutPanel.getWidgetIndex(psmTablePanel) >= 0)
			layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 0, Unit.PCT);
		if (layoutPanel.getWidgetIndex(secondLevelTabPanel) >= 0)
			layoutPanel.setWidgetTopHeight(secondLevelTabPanel, 0, Unit.PCT, 100, Unit.PCT);
	}

	@Override
	public void showPanel() {
		if (layoutPanel.getWidgetIndex(psmTablePanel) >= 0)
			layoutPanel.setWidgetBottomHeight(psmTablePanel, 0, Unit.PCT, 50, Unit.PCT);

		if (layoutPanel.getWidgetIndex(secondLevelTabPanel) >= 0)
			layoutPanel.setWidgetTopHeight(secondLevelTabPanel, 0, Unit.PCT, 50, Unit.PCT);

	}

	private void loadProteinsOnGrid(int start, final ProteinBeanSubList result) {
		if (result == null || result.isEmpty()) {
			proteinGroupTablePanel.clearTable();
			proteinTablePanel.clearTable();
			psmTablePanel.clearTable();
			psmOnlyTablePanel.clearTable();
			queryEditorPanel.setLinksToResultsVisible(false);
			queryEditorPanel.setSendingStatusText(null);
		} else {

			// fill the grid
			proteinTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(), true);
			proteinTablePanel.getAsyncDataProvider().updateRowData(start, result.getDataList());
			proteinTablePanel.forceReloadData();
			proteinTablePanel.refreshData();
			selectDataTab(proteinTablePanel);
		}

	}

	private void selectDataTab(Widget widget) {
		firstLevelTabPanel.selectTab(layoutPanel);
		secondLevelTabPanel.selectTab(widget);
	}

	/**
	 * This method retrieves the default views for the data tables and applies
	 * it to them. The default view depends on each project and it is configured
	 * in the server. If the client currently has loaded more than one project,
	 * the only the default view of one of them will be retrieved.
	 *
	 * @param projectBean
	 * @param modifyColumns
	 * @param showWelcomeWindowBox
	 * @param changeTab
	 * @param bigProject
	 */
	private void requestDefaultViews(final ProjectBean projectBean, final boolean modifyColumns,
			final boolean showWelcomeWindowBox, final boolean changeTab, final boolean bigProject) {

		if (loadedProjects.isEmpty()) {
			return;
		}

		for (final String projectTag : loadedProjects) {

			if (ClientCacheDefaultViewByProjectTag.getInstance().contains(projectTag)) {

				applyDefaultViews(projectBean,
						ClientCacheDefaultViewByProjectTag.getInstance().getFromCache(projectTag), modifyColumns,
						showWelcomeWindowBox, changeTab, bigProject);

			} else {
				proteinRetrievingService.getDefaultViewByProject(projectTag, new AsyncCallback<DefaultView>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
					}

					@Override
					public void onSuccess(DefaultView defaultViews) {
						if (defaultViews != null) {
							if (projectBean.getTag().equals(projectTag)) {
								applyDefaultViews(projectBean, defaultViews, modifyColumns, showWelcomeWindowBox,
										changeTab, bigProject);
							}
							ClientCacheDefaultViewByProjectTag.getInstance().addtoCache(defaultViews, projectTag);
							projectInformationPanel.addProjectView(projectBean, defaultViews);
						}
					}
				});
			}
		}

	}

	private void applyDefaultViews(ProjectBean projectBean, DefaultView defaultViews, boolean modifyColumns,
			boolean showWelcomeWindowBox, boolean changeTab, boolean bigProject) {

		if (changeTab) {
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
				selectDataTab(psmOnlyTablePanel);
				break;
			case PEPTIDE:
				selectDataTab(peptideTablePanel);
				break;
			default:
				break;
			}
			GWT.log("Changing tab to " + defaultTab.name() + " END");
		}
		if (modifyColumns) {
			GWT.log("Setting default view in tables");

			// tables
			proteinGroupTablePanel.setDefaultView(defaultViews);
			proteinTablePanel.setDefaultView(defaultViews);
			psmTablePanel.setDefaultView(defaultViews);
			psmOnlyTablePanel.setDefaultView(defaultViews);
			peptideTablePanel.setDefaultView(defaultViews);

			GWT.log("Setting default view in checkboxes");
			// checkboxes
			psmColumnNamesPanel.setDefaultView(defaultViews.getPsmDefaultView());
			peptideColumnNamesPanel.setDefaultView(defaultViews.getPeptideDefaultView());
			proteinColumnNamesPanel.setDefaultView(defaultViews.getProteinDefaultView());
			proteinGroupColumnNamesPanel.setDefaultView(defaultViews.getProteinGroupDefaultView());
		}
		// if (selectFirstRowAfterDefaultView) {
		// GWT.log("Selecting First row in tables");
		// final ProteinBean firstRowProteinBean = proteinTablePanel
		// .getFirstRowProteinBean();
		// if (firstRowProteinBean != null) {
		// loadPSMsOnPSMGrid(firstRowProteinBean.getDbIds());
		// }
		// final ProteinGroupBean firstRowProteinGroupBean =
		// proteinGroupTablePanel
		// .getFirstRowProteinGroupBean();
		// if (firstRowProteinGroupBean != null) {
		// loadPSMsOnPSMGrid(firstRowProteinGroupBean.getDbIds());
		// }
		// // proteinTablePanel.selectFirstRow();
		// // proteinGroupTablePanel.selectFirstRow();
		// // psmTablePanel.selectFirstRow();
		// }
		if (showWelcomeWindowBox) {
			GWT.log("Showing welcome window for project");

			// TODO only show the dialog if some information is present in the
			// defaultViews
			GWT.log("Showing Welcome frame for project '" + defaultViews.getProjectTag() + "'");

			welcomeToProjectWindowBox = new WindowBox(true, true, true, true, false);
			welcomeToProjectWindowBox.setAnimationEnabled(true);
			welcomeToProjectWindowBox.setWidget(new MyWelcomeProjectPanel(projectBean, defaultViews, this));
			welcomeToProjectWindowBox.setText("Project '" + defaultViews.getProjectTag() + "'");
			// add loadingDialogShowerTask to close event
			welcomeToProjectWindowBox.addCloseEventDoSomethingTask(getLoadingDialogShowerTask());
			welcomeToProjectWindowBox.setGlassEnabled(true);
			welcomeToProjectWindowBox.center();
			GWT.log("Showing welcome window for project END");
		}

	}

	/**
	 * Creates an implementation of {@link DoSomethingTask} that checks if some
	 * of the tables are waiting to be loaded, and if so, it center the
	 * corresponding loading dialog.
	 *
	 * @return
	 */
	private DoSomethingTask<Void> getLoadingDialogShowerTask() {
		if (loadingDialogShowerTask == null) {
			loadingDialogShowerTask = new DoSomethingTask<Void>() {
				@Override
				public Void doSomething() {
					// if the welcome project windo is visible, dont do nothing
					if (welcomeToProjectWindowBox != null && welcomeToProjectWindowBox.isShowing())
						return null;

					boolean someTaskIsPending = false;
					final TaskType[] taskTypes = TaskType.values();
					for (TaskType taskType : taskTypes) {
						final List<String> pendingTaskKeys = PendingTasks.getPendingTaskKeys(taskType);
						if (!pendingTaskKeys.isEmpty()) {
							someTaskIsPending = true;
							if (pendingTaskKeys.size() == 1) {
								showLoadingDialog(taskType.getSingleTaskMessage("'" + pendingTaskKeys.get(0) + "'"));
							} else {
								showLoadingDialog(taskType.getMultipleTaskMessage("" + pendingTaskKeys.size()));
							}
						}
					}
					// if there is not any pending task, hidde the loading
					// dialog
					if (!someTaskIsPending)
						hiddeLoadingDialog();
					return null;
				}
			};
		}
		return loadingDialogShowerTask;
	}

	private void loadPSMsOnlyOnGrid(int start, PsmBeanSubList result) {
		if (result == null || result.isEmpty()) {
			psmOnlyTablePanel.clearTable();
			getLoadingDialogShowerTask().doSomething();
		} else {
			psmOnlyTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(), true);
			psmOnlyTablePanel.getAsyncDataProvider().updateRowData(start, result.getDataList());
			psmOnlyTablePanel.forceReloadData();
			psmOnlyTablePanel.refreshData();
		}
	}

	private void loadPeptidesOnlyOnGrid(int start, PeptideBeanSubList result) {
		if (result == null || result.isEmpty()) {
			peptideTablePanel.clearTable();
			getLoadingDialogShowerTask().doSomething();
		} else {
			peptideTablePanel.getAsyncDataProvider().updateRowCount(result.getTotalNumber(), true);
			peptideTablePanel.getAsyncDataProvider().updateRowData(start, result.getDataList());
			peptideTablePanel.forceReloadData();
			peptideTablePanel.refreshData();
		}
	}

	private void showLoadingDialog(String text, boolean autohide, boolean modal) {
		if (loadingDialog == null) {
			loadingDialog = MyDialogBox.getInstance(text, autohide, modal);
		} else {
			loadingDialog.setText(text);
			loadingDialog.setAutoHideEnabled(autohide);
			loadingDialog.setModal(modal);
		}
		loadingDialog.center();

	}

	/**
	 * Modal false and autohide true
	 *
	 * @param text
	 */
	private void showLoadingDialog(String text) {
		showLoadingDialog(text, true, false);
	}

	private void hiddeLoadingDialog() {
		loadingDialog.hide();
	}

	private void loadRatioDescriptors(final Set<String> selectedProjects) {
		ratioNamesPanel.clearList();
		final Set<String> uniqueRatioNames = new HashSet<String>();
		GWT.log("Loading ratios from " + selectedProjects.size() + " projects");
		proteinRetrievingService.getRatioDescriptorsFromProjects(selectedProjects,
				new AsyncCallback<List<RatioDescriptorBean>>() {

					@Override
					public void onFailure(Throwable caught) {
						updateStatus(caught);
					}

					@Override
					public void onSuccess(List<RatioDescriptorBean> result) {
						// remove the previous columns related to ratios
						proteinTablePanel.removeColumn(ColumnName.PROTEIN_RATIO);
						proteinGroupTablePanel.removeColumn(ColumnName.PROTEIN_RATIO);
						psmTablePanel.removeColumn(ColumnName.PSM_RATIO);
						psmOnlyTablePanel.removeColumn(ColumnName.PSM_RATIO);
						peptideTablePanel.removeColumn(ColumnName.PEPTIDE_RATIO);
						// also the ones
						proteinTablePanel.removeColumn(ColumnName.PROTEIN_RATIO_SCORE);

						// create the columns related to the ratio descriptors
						if (!result.isEmpty()) {
							for (RatioDescriptorBean ratioDescriptor : result) {
								if (!uniqueRatioNames.contains(ratioDescriptor.getRatioName())) {
									ratioNamesPanel.getListBox().addItem(ratioDescriptor.getRatioName());
									uniqueRatioNames.add(ratioDescriptor.getRatioName());
								}
							}
							updateStatus(uniqueRatioNames.size() + " ratio names loaded.");
							for (String projectTag : selectedProjects) {
								// get the conditions from the
								// "Condition" list panel

								for (RatioDescriptorBean ratioDescriptor : result) {
									String condition1 = ratioDescriptor.getCondition1Name();
									String projectNameFromRatio = ratioDescriptor.getProjectTag();
									if (projectTag.equals(projectNameFromRatio)) {
										String condition2 = ratioDescriptor.getCondition2Name();
										String letter1 = getLetterFromCondition(condition1, projectTag);
										String letter2 = getLetterFromCondition(condition2, projectTag);

										switch (ratioDescriptor.getAggregationLevel()) {
										// addcolumn for pairs of conditions of
										// the ratio descriptors
										// add the corresponding ratio score
										// column
										case PROTEINGROUP:
											proteinGroupTablePanel.addColumnforConditionProteinRatio(condition1,
													letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());
											proteinGroupColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
													ColumnName.PROTEIN_RATIO, condition1, condition2, projectTag,
													conditionsPanel);
											break;
										case PROTEIN:
											proteinTablePanel.addColumnforConditionProteinRatio(
													ColumnName.PROTEIN_RATIO, condition1, letter1, condition2, letter2,
													projectTag, ratioDescriptor.getRatioName());
											proteinTablePanel.addColumnforConditionProteinRatio(
													ColumnName.PROTEIN_RATIO_GRAPH, condition1, letter1, condition2,
													letter2, projectTag, ratioDescriptor.getRatioName());
											proteinTablePanel.addColumnforConditionProteinRatioScore(condition1,
													letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());
											proteinColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
													ColumnName.PROTEIN_RATIO, condition1, condition2, projectTag,
													conditionsPanel);
											proteinColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
													ColumnName.PROTEIN_RATIO_SCORE, condition1, condition2, projectTag,
													conditionsPanel);
											break;
										case PEPTIDE:
											peptideTablePanel.addColumnforConditionPeptideRatio(
													ColumnName.PEPTIDE_RATIO, condition1, letter1, condition2, letter2,
													projectTag, ratioDescriptor.getRatioName());
											peptideTablePanel.addColumnforConditionPeptideRatio(
													ColumnName.PEPTIDE_RATIO_GRAPH, condition1, letter1, condition2,
													letter2, projectTag, ratioDescriptor.getRatioName());
											peptideColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
													ColumnName.PEPTIDE_RATIO, condition1, condition2, projectTag,
													conditionsPanel);
											break;
										case PSM:
											psmTablePanel.addColumnforConditionPSMRatio(ColumnName.PSM_RATIO,
													condition1, letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());
											psmTablePanel.addColumnforConditionPSMRatio(ColumnName.PSM_RATIO_GRAPH,
													condition1, letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());
											psmOnlyTablePanel.addColumnforConditionPSMRatio(ColumnName.PSM_RATIO,
													condition1, letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());

											psmOnlyTablePanel.addColumnforConditionPSMRatio(ColumnName.PSM_RATIO_GRAPH,
													condition1, letter1, condition2, letter2, projectTag,
													ratioDescriptor.getRatioName());
											psmColumnNamesPanel.addConditionRelatedColumnCheckBoxHandler(
													ColumnName.PSM_RATIO, condition1, condition2, projectTag,
													conditionsPanel);
											break;
										default:
											break;
										}

										// add column handlers

									}
								}
							}

						}
					}

				});

	}

	private String getLetterFromCondition(String conditionName, String projectName) {

		final String projectSymbol = SharedDataUtils.getProjectSymbolFromListBox(projectName,
				projectListPanel.getListBox());

		final ListBox listBox = conditionsPanel.getListBox();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			final String conditionString = listBox.getItemText(i);
			String conditionName2 = listBox.getValue(i);
			if (conditionName2.equals(conditionName)) {
				final String projectSymbolFromCondition = SharedDataUtils
						.getProjectSymbolFromConditionSelection(conditionString);
				if (projectSymbolFromCondition == null || "".equals(projectSymbolFromCondition)) {
					// just one project in the list
					return SharedDataUtils.getConditionSymbolFromConditionElement(conditionString);
				} else if (projectSymbolFromCondition.equals(projectSymbol)) {
					return projectSymbolFromCondition
							+ SharedDataUtils.getConditionSymbolFromConditionElement(conditionString);
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
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

}
