package edu.scripps.yates.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ProjectSaverServiceAsync;
import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.cache.ClientCacheOrganismsByProjectTag;
import edu.scripps.yates.client.cache.ClientCacheProjectBeansByProjectTag;
import edu.scripps.yates.client.cache.ClientCacheProteinFileDescriptorByProjectName;
import edu.scripps.yates.client.cache.ClientCacheProteinGroupFileDescriptorByProjectName;
import edu.scripps.yates.client.gui.components.MyDialogBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.history.TargetHistory;
import edu.scripps.yates.client.interfaces.InitializableComposite;
import edu.scripps.yates.client.interfaces.StatusReporter;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.util.FileDescriptor;
import edu.scripps.yates.shared.util.SharedConstants;

public class BrowsePanel extends InitializableComposite implements StatusReporter {
	private final Label projectTagLabel;
	private final Label projectTitleLabel;
	private final Label projectDescriptionLabel;
	private final DateLabel projectDateLabel;
	private final DateLabel projectUploadDateLabel;
	protected final edu.scripps.yates.client.ProteinRetrievalServiceAsync proteinRetrievingService = ProteinRetrievalServiceAsync.Util
			.getInstance();
	private final InlineLabel numSelectedProjectsLabel;
	private final String NO_SELECTION = "No selection";
	private final List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
	private final Button btnQueryOverSelected;
	private final FlowPanel projectListVerticalPanel;
	private Label speciesDataLabel;
	private final Anchor proteinsLink = new Anchor();
	private final Anchor proteinGroupsLink = new Anchor();
	private Grid grid;
	private final MyClientBundle myClientBundle = MyClientBundle.INSTANCE;
	private String selectedProjectTag;
	private Label projectStatusLabel;
	private final Map<String, Boolean> projectAvailability = new HashMap<String, Boolean>();
	private InlineLabel numSelectedProjects2;
	private Button btnPSEAQuant;
	private FlexTable projectsTable;
	private final String uploadDateTitle = "Date in which the project was uploaded to PINT";
	private final String projectTagTitle = "Unique project tag serving as identifier";
	private final String availabilityTag = "Availability of the project:\n\tprivate (only accessible by direct URL)\n\tpublic (accessible by selection).";
	private final String publicationTag = "Link to publication in PUBMED, if available";
	private MyDialogBox loadingDialog;

	public BrowsePanel() {
		StatusReportersRegister.getInstance().registerNewStatusReporter(this);
		proteinGroupsLink.setStyleName("linkPINT");
		proteinsLink.setStyleName("linkPINT");

		FlowPanel mainPanel = new FlowPanel();
		mainPanel.setStyleName("MainPanel");
		initWidget(mainPanel);

		HeaderPanel header = new HeaderPanel();
		mainPanel.add(header);
		NavigationHorizontalPanel navigationPanel = new NavigationHorizontalPanel(TargetHistory.BROWSE);
		navigationPanel.setStyleName("navigationBar");
		mainPanel.add(navigationPanel);

		FlowPanel horizontalFlowPanel = new FlowPanel();
		mainPanel.add(horizontalFlowPanel);
		horizontalFlowPanel.setStyleName("verticalComponent");

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("browserLeftPart");
		horizontalFlowPanel.add(flowPanel);

		CaptionPanel captionPanel = new CaptionPanel("Available Projects");
		flowPanel.add(captionPanel);
		captionPanel.setStyleName("browserAvailableProjects");

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setStyleName("BrowserVerticalScroll");
		captionPanel.setContentWidget(scrollPanel);

		projectListVerticalPanel = new FlowPanel();
		scrollPanel.setWidget(projectListVerticalPanel);

		projectsTable = new FlexTable();
		projectsTable.setSize("100%", "100%");

		final Label label1 = new Label("#");
		label1.setStyleName("font-bold");
		projectsTable.setWidget(0, 0, label1);
		final Label label2 = new Label("Tag");
		label2.setTitle(projectTagTitle);
		label2.setStyleName("font-bold");
		projectsTable.setWidget(0, 1, label2);
		final Label label3 = new Label("Upload date");

		label3.setTitle(uploadDateTitle);
		label3.setStyleName("font-bold");
		projectsTable.setWidget(0, 2, label3);
		final Label label4 = new Label("Availability");
		label4.setTitle(availabilityTag);
		label4.setStyleName("font-bold");
		projectsTable.setWidget(0, 3, label4);
		projectsTable.getCellFormatter().setAlignment(0, 3, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		final Label label5 = new Label("Publication");
		label5.setTitle(publicationTag);
		label5.setStyleName("font-bold");
		projectsTable.setWidget(0, 4, label5);
		projectsTable.getCellFormatter().setAlignment(0, 4, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);

		CaptionPanel querySelectedProjectsCaptionPanel = new CaptionPanel("Explore data:");
		flowPanel.add(querySelectedProjectsCaptionPanel);
		querySelectedProjectsCaptionPanel.setStyleName("browserSelectedProject");

		FlexTable horizontalPanel_1 = new FlexTable();
		horizontalPanel_1.setStyleName("horizontalComponent");

		querySelectedProjectsCaptionPanel.setContentWidget(horizontalPanel_1);
		horizontalPanel_1.setSize("100%", "100%");
		// selectedProjectLabelVerticalPanel.setSpacing(10);

		numSelectedProjectsLabel = new InlineLabel(NO_SELECTION);
		numSelectedProjectsLabel.setWordWrap(false);
		numSelectedProjectsLabel.setStyleName("selectionLabel");
		numSelectedProjectsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.setWidget(0, 0, numSelectedProjectsLabel);
		numSelectedProjectsLabel.setWidth("100%");

		btnQueryOverSelected = new Button("Explore");
		btnQueryOverSelected.setStyleName("selectionButton");
		horizontalPanel_1.setWidget(0, 1, btnQueryOverSelected);
		horizontalPanel_1.getCellFormatter().setWidth(0, 1, "30%");
		btnQueryOverSelected.setWidth("100px");

		CaptionPanel cptnpnlExploreEnrichmentAnalysis = new CaptionPanel("Explore Enrichment Analysis:");
		cptnpnlExploreEnrichmentAnalysis.setStyleName("browserSelectedProject");
		flowPanel.add(cptnpnlExploreEnrichmentAnalysis);

		FlexTable horizontalPanel = new FlexTable();
		cptnpnlExploreEnrichmentAnalysis.setContentWidget(horizontalPanel);
		horizontalPanel.setSize("100%", "100%");

		numSelectedProjects2 = new InlineLabel("No selection");
		numSelectedProjects2.setWordWrap(false);
		numSelectedProjects2.setStyleName("selectionLabel");
		numSelectedProjects2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.setWidget(0, 0, numSelectedProjects2);
		numSelectedProjects2.setWidth("");

		btnPSEAQuant = new Button("PSEA-Quant");
		btnPSEAQuant.setStyleName("selectionButton");
		btnPSEAQuant.setText("PSEA-Quant");
		horizontalPanel.setWidget(0, 1, btnPSEAQuant);
		horizontalPanel.getCellFormatter().setWidth(0, 1, "30%");
		btnPSEAQuant.setWidth("100px");
		btnPSEAQuant.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<String> projectTags = getSelectedProjects();
				if (projectTags == null || projectTags.isEmpty()) {
					StatusReportersRegister.getInstance().notifyStatusReporters("Select at least one project");
					return;
				}

				String historyToken = TargetHistory.PSEAQUANT.getTargetHistory() + "?project=";
				StringBuilder projectTagsString = new StringBuilder();
				for (String projectTag : projectTags) {
					if (!"".equals(projectTagsString.toString()))
						projectTagsString.append(",");
					projectTagsString.append(projectTag);
				}
				historyToken = historyToken + projectTagsString.toString();
				History.newItem(historyToken);
			}
		});
		btnQueryOverSelected.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Set<String> projectTags = getSelectedProjects();
				if (projectTags == null || projectTags.isEmpty()) {
					StatusReportersRegister.getInstance().notifyStatusReporters("Select at least one project");
					return;
				}

				String historyToken = TargetHistory.LOAD_PROJECT.getTargetHistory() + "?project=";
				StringBuilder projectTagsString = new StringBuilder();
				for (String projectTag : projectTags) {
					if (!"".equals(projectTagsString.toString()))
						projectTagsString.append(",");
					projectTagsString.append(projectTag);
				}
				historyToken = historyToken + projectTagsString.toString();
				History.newItem(historyToken);
			}
		});
		CaptionPanel cptnpnlDeleteProject = new CaptionPanel("Delete project:");
		cptnpnlDeleteProject.setStyleName("browserSelectedProject");
		flowPanel.add(cptnpnlDeleteProject);

		FlexTable horizontalPanel2 = new FlexTable();
		cptnpnlDeleteProject.setContentWidget(horizontalPanel2);
		horizontalPanel2.setSize("100%", "100%");

		final TextBox projectTagTextBox = new TextBox();
		horizontalPanel2.setWidget(0, 0, projectTagTextBox);

		Button btnDeleteProject = new Button("Delete");
		btnDeleteProject.setStyleName("selectionButton");
		horizontalPanel2.setWidget(0, 1, btnDeleteProject);
		horizontalPanel2.getCellFormatter().setWidth(0, 1, "30%");
		btnDeleteProject.setWidth("100px");
		btnDeleteProject.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkUserNameForDeletion(projectTagTextBox.getText());
			}
		});
		FlowPanel verticalPanelRigth = new FlowPanel();
		verticalPanelRigth.setStyleName("browserRigthPart");
		horizontalFlowPanel.add(verticalPanelRigth);

		CaptionPanel cptnpnlNewPanel = new CaptionPanel("Experiment information:");
		cptnpnlNewPanel.setStyleName("browserExperimentInformation");
		verticalPanelRigth.add(cptnpnlNewPanel);

		grid = new Grid(10, 2);
		grid.setCellPadding(2);
		grid.setCellSpacing(4);
		grid.setBorderWidth(0);
		cptnpnlNewPanel.setContentWidget(grid);
		grid.setSize("100%", "100%");

		Label lblProjectTag = new Label("Project tag:");
		lblProjectTag.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(1, 0, lblProjectTag);
		lblProjectTag.setHeight("2em");

		projectTagLabel = new Label("-");
		grid.setWidget(1, 1, projectTagLabel);
		grid.getCellFormatter().setWidth(0, 1, "100%");
		grid.getCellFormatter().setWordWrap(0, 1, true);
		projectTagLabel.setWidth("100%");

		Label lblProjectStatus = new Label("Project status:");
		lblProjectStatus.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(0, 0, lblProjectStatus);
		lblProjectStatus.setHeight("2em");

		projectStatusLabel = new Label("-");
		grid.setWidget(0, 1, projectStatusLabel);
		grid.getCellFormatter().setWidth(1, 1, "100%");
		grid.getCellFormatter().setWordWrap(1, 1, true);
		projectStatusLabel.setWidth("100%");

		Label lblTitle = new Label("Title:");
		lblTitle.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(2, 0, lblTitle);
		lblTitle.setHeight("2em");

		projectTitleLabel = new Label("-");
		grid.setWidget(2, 1, projectTitleLabel);
		grid.getCellFormatter().setWidth(1, 1, "100%");
		projectTitleLabel.setWidth("100%");
		grid.getCellFormatter().setWordWrap(1, 1, true);

		Label lblDescription = new Label("Description:");
		lblDescription.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(9, 0, lblDescription);
		lblDescription.setHeight("10em");

		projectDescriptionLabel = new Label("-");
		grid.setWidget(9, 1, projectDescriptionLabel);
		grid.getCellFormatter().setWidth(9, 1, "100%");
		grid.getCellFormatter().setWordWrap(9, 1, true);
		projectDescriptionLabel.setWidth("100%");
		grid.getCellFormatter().setWidth(4, 1, "100%");
		grid.getCellFormatter().setWidth(5, 1, "100%");
		grid.getCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(2, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setHorizontalAlignment(7, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(7, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(8, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label speciesLabel = new Label("Species:");
		speciesLabel.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(3, 0, speciesLabel);

		speciesDataLabel = new Label("-");
		grid.setWidget(3, 1, speciesDataLabel);
		speciesDataLabel.setWidth("100%");

		Label lblExpDate = new Label("Experiment date:");
		lblExpDate.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(4, 0, lblExpDate);
		lblExpDate.setSize("146px", "");

		projectDateLabel = new DateLabel();
		grid.setWidget(4, 1, projectDateLabel);
		projectDateLabel.setWidth("100%");

		Label lblUploadedDate = new Label("Uploaded date:");
		lblUploadedDate.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(5, 0, lblUploadedDate);
		lblUploadedDate.setSize("100%", "");

		projectUploadDateLabel = new DateLabel();
		grid.setWidget(5, 1, projectUploadDateLabel);
		projectUploadDateLabel.setWidth("100%");

		Label lblNewLabel = new Label("Download links:");
		lblNewLabel.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(6, 0, lblNewLabel);

		Label proteinLabel = new Label("proteins");
		proteinLabel.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(7, 0, proteinLabel);

		Label emptyLabel = new Label("-");
		grid.setWidget(7, 1, emptyLabel);

		Label proteinGroupsLabel = new Label("protein groups");
		proteinGroupsLabel.setStyleName("browserExperimentInformationItemHeader");
		grid.setWidget(8, 0, proteinGroupsLabel);

		Label emptyLabel2 = new Label("-");
		grid.setWidget(8, 1, emptyLabel2);
		grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(2, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(3, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(3, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(4, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(4, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(5, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(6, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(6, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(7, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(7, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(8, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(8, 1, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(9, 0, HasVerticalAlignment.ALIGN_TOP);
		grid.getCellFormatter().setVerticalAlignment(9, 1, HasVerticalAlignment.ALIGN_TOP);

		// load project list
		loadProjectList();

		setStyleName("MainPanel");
	}

	protected void checkUserNameForDeletion(final String projectTag) {
		// check first the login
		PopUpPanelPasswordChecker loginPanel = new PopUpPanelPasswordChecker(true, true, "PINT security",
				"Enter password for project deletion:");
		loginPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				final PopupPanel popup = event.getTarget();
				final Widget widget = popup.getWidget();
				if (widget instanceof PopUpPanelPasswordChecker) {
					PopUpPanelPasswordChecker loginPanel = (PopUpPanelPasswordChecker) widget;
					if (loginPanel.isLoginOK()) {
						showLoadingDialog(
								"Deleting project '" + projectTag + "'. Please wait, this may take some minutes.",
								false, true, true);
						ProjectSaverServiceAsync.Util.getInstance().deleteProject(projectTag,
								new AsyncCallback<Void>() {

									@Override
									public void onFailure(Throwable caught) {
										StatusReportersRegister.getInstance().notifyStatusReporters(caught);

									}

									@Override
									public void onSuccess(Void result) {
										StatusReportersRegister.getInstance()
												.notifyStatusReporters("Project '" + projectTag + "' deleted");
										loadProjectList();
									}
								});
					}
				}
			}
		});
		loginPanel.show();
	}

	protected Set<String> getSelectedProjects() {
		Set<String> ret = new HashSet<String>();
		for (CheckBox checkBox : checkBoxes) {
			if (checkBox.getValue().booleanValue())
				ret.add(checkBox.getText());
		}
		return ret;
	}

	private void loadProjectList() {
		projectListVerticalPanel.clear();
		final MyDialogBox loadingDialog = new MyDialogBox("Loading projects...", true, false);
		loadingDialog.center();
		projectListVerticalPanel.clear();
		proteinRetrievingService.getProjectBeans(new AsyncCallback<Set<ProjectBean>>() {

			@Override
			public void onFailure(Throwable caught) {
				projectListVerticalPanel.add(new Label(caught.getMessage()));

				loadingDialog.hide();
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}

			@Override
			public void onSuccess(Set<ProjectBean> projectBeans) {
				projectListVerticalPanel.add(new ScrollPanel(projectsTable));
				// sort projects by release date
				List<ProjectBean> projectList = new ArrayList<ProjectBean>();
				projectList.addAll(projectBeans);
				Collections.sort(projectList, new Comparator<ProjectBean>() {

					@Override
					public int compare(ProjectBean o1, ProjectBean o2) {
						if (o1.getUploadedDate() != null) {
							if (o1.getUploadedDate().equals(o2.getUploadedDate())) {
								return o1.getId().compareTo(o2.getId());
							} else {
								return o1.getUploadedDate().compareTo(o2.getUploadedDate());
							}
						}
						return o1.getTag().compareTo(o2.getTag());
					}
				});

				int row = 1;

				// create a checkbox per each project
				for (ProjectBean projectBean : projectList) {
					addToProjectTable(row, projectBean);

					row++;
				}
				loadingDialog.hide();

			}
		});

	}

	private void addToProjectTable(int row, ProjectBean projectBean) {
		// keep the availability for allowing exploring and
		// for the links to download the data
		projectAvailability.put(projectBean.getTag(), projectBean.isPublicAvailable());
		final MyCheckBox checkBox = new MyCheckBox(projectBean.getTag());
		checkBox.setTitle("Project '" + projectBean.getTag() + "':\nName: " + projectBean.getId() + "\nDescription: "
				+ projectBean.getDescription());
		checkBox.getElement().getStyle().setDisplay(Display.BLOCK);

		checkBoxes.add(checkBox);

		projectsTable.setWidget(row, 0, new Label(String.valueOf(row)));
		projectsTable.setWidget(row, 1, checkBox);
		DateTimeFormat format = com.google.gwt.i18n.client.DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
		final DateLabel dateLabel = new DateLabel(format);
		dateLabel.setTitle(uploadDateTitle + ":\n" + format.format(projectBean.getUploadedDate()));
		dateLabel.setValue(projectBean.getUploadedDate());

		projectsTable.setWidget(row, 2, dateLabel);
		String publicOrPrivate = "private";
		String styleName = "font-red";
		String titlePublicOrPrivate = "This project is private. It is only accessible by the owner.";
		if (projectBean.isPublicAvailable()) {
			publicOrPrivate = "public";
			styleName = "font-green";
			titlePublicOrPrivate = "This project is public and it is available through this web page.\nIn order to load it, check the checkbox at the left and press on Explore button.";
		}
		final Label publicPrivateLabel = new Label(publicOrPrivate);
		publicPrivateLabel.setStyleName(styleName);
		publicPrivateLabel.setTitle(titlePublicOrPrivate);
		publicPrivateLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				checkBox.setValue(!checkBox.getValue());
				updateNumSelectedProjects();
			}
		});

		projectsTable.setWidget(row, 3, publicPrivateLabel);
		projectsTable.getCellFormatter().setAlignment(row, 3, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		// publication
		final String pubmedId = projectBean.getPubmedLink();
		if (pubmedId != null) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendEscaped("[ pubmed ]");

			final Anchor link = new Anchor(sb.toSafeHtml(),
					UriUtils.fromString("http://www.ncbi.nlm.nih.gov/pubmed/" + pubmedId));
			link.setStyleName("linkPINT");
			link.setTarget("_blank");
			link.setTitle("Click here to go to the publication in PUBMED (id:" + pubmedId + ")");
			projectsTable.setWidget(row, 4, link);
		} else {
			projectsTable.setWidget(row, 4, new Label("-"));
		}
		projectsTable.getCellFormatter().setAlignment(row, 4, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
		// projectListVerticalPanel.add(checkBox);

	}

	protected void updateNumSelectedProjects() {
		int numSelected = 0;
		boolean allowExploreData = true;
		for (CheckBox checkBox : checkBoxes) {
			if (checkBox.getValue().booleanValue()) {
				if (!projectAvailability.get(checkBox.getText()))
					allowExploreData = false;
				numSelected++;
			}
		}
		if (numSelected == 0) {
			numSelectedProjectsLabel.setText(NO_SELECTION);
			numSelectedProjects2.setText(NO_SELECTION);
			btnQueryOverSelected.setEnabled(false);
			final String title = "Select some projects to enable query";
			btnQueryOverSelected.setTitle(title);
			btnPSEAQuant.setTitle(title);
			numSelectedProjectsLabel.setStyleName(null);
			numSelectedProjects2.setStyleName(null);
		} else {
			if (allowExploreData) {
				final String text = numSelected + " projects selected";
				numSelectedProjectsLabel.setText(text);
				numSelectedProjects2.setText(text);
				btnQueryOverSelected.setEnabled(true);
				btnQueryOverSelected.setTitle(text);
				btnPSEAQuant.setEnabled(true);
				btnPSEAQuant.setTitle(text);
				numSelectedProjectsLabel.setStyleName(null);
				numSelectedProjects2.setStyleName(null);
			} else {
				final String text = "Some selected project is not available yet";
				numSelectedProjectsLabel.setText(text);
				btnQueryOverSelected.setEnabled(false);
				btnQueryOverSelected.setTitle(text);
				btnPSEAQuant.setEnabled(false);
				btnPSEAQuant.setTitle(text);
				numSelectedProjectsLabel.setStyleName("browserSelectedProjectRed");
				numSelectedProjects2.setStyleName("browserSelectedProjectRed");
			}

		}
	}

	protected void loadProjectInfo(final String projectTag) {
		selectedProjectTag = projectTag;
		if (projectTag == null) {
			loadProjectBean(null);
			loadOrganisms(null);
			loadProteinLink(null);
			loadProteinGroupLink(null);
			return;
		}
		ProjectBean projectBean = null;
		if (ClientCacheProjectBeansByProjectTag.getInstance().contains(projectTag)) {
			projectBean = ClientCacheProjectBeansByProjectTag.getInstance().getFromCache(projectTag);
			loadProjectBean(projectBean);

		} else {
			proteinRetrievingService.getProjectBean(projectTag, new AsyncCallback<ProjectBean>() {

				@Override
				public void onSuccess(ProjectBean projectBean) {
					// add to cache
					ClientCacheProjectBeansByProjectTag.getInstance().addtoCache(projectBean, projectTag);
					loadProjectBean(projectBean);
				}

				@Override
				public void onFailure(Throwable caught) {
					StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				}
			});
		}

		if (ClientCacheOrganismsByProjectTag.getInstance().contains(projectTag)) {
			loadOrganisms(ClientCacheOrganismsByProjectTag.getInstance().getFromCache(projectTag));
		} else {
			proteinRetrievingService.getOrganismsByProject(projectTag, new AsyncCallback<Set<OrganismBean>>() {

				@Override
				public void onSuccess(Set<OrganismBean> organisms) {
					// add to cache
					ClientCacheOrganismsByProjectTag.getInstance().addtoCache(organisms, projectTag);
					if (projectTag.equals(selectedProjectTag)) {
						loadOrganisms(organisms);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					StatusReportersRegister.getInstance().notifyStatusReporters(caught);
				}
			});
		}
		// only load links if the project is available
		if (projectAvailability.containsKey(projectTag) && projectAvailability.get(projectTag)) {
			// protein link
			if (ClientCacheProteinFileDescriptorByProjectName.getInstance().contains(projectTag)) {
				loadProteinLink(ClientCacheProteinFileDescriptorByProjectName.getInstance().getFromCache(projectTag));
			} else {
				setLoadingImagesForDownloadLinks();
				List<String> projectTags = new ArrayList<String>();
				projectTags.add(projectTag);
				proteinRetrievingService.getDownloadLinkForProteinsInProject(projectTags,
						new AsyncCallback<FileDescriptor>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught.getMessage());
							}

							@Override
							public void onSuccess(FileDescriptor result) {

								if (projectTag.equals(selectedProjectTag)) {
									loadProteinLink(result);
								}

								ClientCacheProteinFileDescriptorByProjectName.getInstance().addtoCache(result,
										projectTag);

							}
						});

			}
			// protein groups link
			if (ClientCacheProteinGroupFileDescriptorByProjectName.getInstance().contains(projectTag)) {
				loadProteinGroupLink(
						ClientCacheProteinGroupFileDescriptorByProjectName.getInstance().getFromCache(projectTag));
			} else {
				List<String> projectTags = new ArrayList<String>();
				projectTags.add(projectTag);
				proteinRetrievingService.getDownloadLinkForProteinGroupsInProject(projectTags, true,
						new AsyncCallback<FileDescriptor>() {

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught.getMessage());
							}

							@Override
							public void onSuccess(FileDescriptor result) {
								loadProteinGroupLink(result);
								if (result != null) {
									ClientCacheProteinGroupFileDescriptorByProjectName.getInstance().addtoCache(result,
											projectTag);
								}
							}
						});
			}
		} else {
			loadProteinLink(null);
			loadProteinGroupLink(null);
		}
	}

	private void loadProteinLink(FileDescriptor file) {
		if (file == null) {
			setProteinLink(null);
		} else {
			loadLink(file, proteinsLink);
			setProteinLink(proteinsLink);
		}
	}

	private void loadProteinGroupLink(FileDescriptor file) {
		if (file == null) {
			setProteinGroupsLink(null);
		} else {
			loadLink(file, proteinGroupsLink);
			setProteinGroupsLink(proteinGroupsLink);
		}
	}

	private void loadLink(FileDescriptor fileDescriptor, Anchor anchor) {
		if (fileDescriptor != null) {
			// final String href = GWT.getModuleBaseURL() + "/download?" +
			// SharedConstants.FILE_TO_DOWNLOAD + "="
			// + file.getName() + "&" + SharedConstants.FILE_TYPE + "=" +
			// SharedConstants.ID_DATA_FILE_TYPE;
			final String href = ClientSafeHtmlUtils.getDownloadURL(fileDescriptor.getName(),
					SharedConstants.ID_DATA_FILE_TYPE);
			anchor.setText("[" + fileDescriptor.getSize() + "]");
			anchor.setHref(href);
			anchor.setTarget("_blank");
		} else {
			anchor.setHref("");
			anchor.setText("not available");
		}
	}

	protected void loadOrganisms(Set<OrganismBean> organisms) {
		if (organisms != null) {
			StringBuilder sb = new StringBuilder();
			for (OrganismBean organismBean : organisms) {
				if (!"".equals(sb.toString()))
					sb.append("\n");
				sb.append(organismBean.toString());
			}
			speciesDataLabel.setText(sb.toString());
		} else {
			speciesDataLabel.setText("-");
		}
	}

	private void loadProjectBean(ProjectBean project) {
		if (project != null) {
			if (project.getTag().equals(selectedProjectTag)) {
				projectTitleLabel.setText(project.getId());
				projectDescriptionLabel.setText(project.getDescription());
				projectUploadDateLabel.setValue(project.getUploadedDate());
				projectDateLabel.setValue(project.getReleaseDate());
				projectTagLabel.setText(project.getTag());
				if (project.isPublicAvailable()) {
					projectStatusLabel.setText("public");
					projectStatusLabel.setStyleName("gwt-Label");
				} else {
					projectStatusLabel.setText("This project will be available soon");
					projectStatusLabel.setStyleName("browserSelectedProjectRed");
				}
			}
		} else {
			projectTitleLabel.setText("-");
			projectDescriptionLabel.setText("-");
			projectUploadDateLabel.setValue(null);
			projectDateLabel.setValue(null);
			projectTagLabel.setText("-");
			projectStatusLabel.setText("-");
		}
	}

	private void setProteinGroupsLink(Anchor anchor) {
		if (anchor != null)
			grid.setWidget(8, 1, anchor);
		else
			grid.setWidget(8, 1, new Label("-"));
	}

	private void setProteinLink(Anchor anchor) {
		if (anchor != null)
			grid.setWidget(7, 1, anchor);
		else
			grid.setWidget(7, 1, new Label("-"));
	}

	private void setLoadingImagesForDownloadLinks() {
		grid.setWidget(7, 1, new Image(myClientBundle.smallLoader()));
		grid.setWidget(8, 1, new Image(myClientBundle.smallLoader()));
	}

	private class MyCheckBoxEventHandler implements MouseOverHandler, MouseOutHandler, ClickHandler {

		@Override
		public void onMouseOver(MouseOverEvent event) {
			final Object source = event.getSource();
			if (source instanceof CheckBox) {
				CheckBox checkbox = (CheckBox) source;
				loadProjectInfo(checkbox.getText());
			}
		}

		@Override
		public void onMouseOut(MouseOutEvent event) {
			loadSelectedProjectInfo();
		}

		@Override
		public void onClick(ClickEvent event) {
			updateNumSelectedProjects();
			Object source = event.getSource();
			if (source instanceof CheckBox) {
				CheckBox checkBox = (CheckBox) source;
				String projectName = checkBox.getText();
				if (checkBox.getValue()) {
					loadProjectInfo(projectName);
				} else {
					loadProjectInfo(null);
				}
			}

		}

	}

	private class MyCheckBox extends CheckBox {
		public MyCheckBox(String label) {
			super(label);
			addMouseOverHandler(new MyCheckBoxEventHandler());
			addMouseOutHandler(new MyCheckBoxEventHandler());
			addClickHandler(new MyCheckBoxEventHandler());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		@Override
		public void sinkEvents(int eventBitsToAdd) {
			if (isOrWasAttached()) {
				Event.sinkEvents(getElement(), eventBitsToAdd | Event.getEventsSunk(getElement()));
			} else
				super.sinkEvents(eventBitsToAdd);
		}
	}

	public void loadSelectedProjectInfo() {
		final Set<String> selectedProjects = getSelectedProjects();
		if (selectedProjects.size() == 1) {
			loadProjectInfo(selectedProjects.iterator().next());
		} else if (selectedProjects.isEmpty()) {
			loadProjectInfo(null);
		}
	}

	@Override
	public void showMessage(String message) {
		GWT.log("Message: " + message);
		showLoadingDialog(message, true, false, false);
	}

	@Override
	public void showErrorMessage(Throwable throwable) {
		showMessage(throwable.getMessage());
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	private void showLoadingDialog(String text, boolean autohide, boolean modal, boolean showLoaderBar) {
		if (loadingDialog == null) {
			loadingDialog = new MyDialogBox(text, autohide, modal, showLoaderBar);
		} else {
			loadingDialog.setText(text);
			loadingDialog.setAutoHideEnabled(autohide);
			loadingDialog.setModal(modal);
		}
		loadingDialog.center();

	}
}
