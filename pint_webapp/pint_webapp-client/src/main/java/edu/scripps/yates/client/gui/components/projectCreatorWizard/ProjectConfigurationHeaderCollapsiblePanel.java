package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

import edu.scripps.yates.client.gui.components.CollapsiblePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.LabelBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.SampleBean;
import edu.scripps.yates.shared.model.TissueBean;
import edu.scripps.yates.shared.model.interfaces.HasId;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatioDescriptorTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class ProjectConfigurationHeaderCollapsiblePanel extends Composite implements RepresentsDataObject {
	private final IncrementableTextBox incrementableTextBoxDataSources;
	private final IncrementableTextBox incrementableTextBoxServers;
	private final IncrementableTextBox incrementableTextBoxConditions;
	private final IncrementableTextBox incrementableTextBoxSamples;
	private final IncrementableTextBox incrementableTextBoxRuns;
	private final IncrementableTextBox incrementableTextBoxTissues;
	private final IncrementableTextBox incrementableTextBoxOrganisms;
	private final IncrementableTextBox incrementableTextBoxLabels;
	private final IncrementableTextBox incrementableTextBoxRatios;

	private final List<DataSourceBean> dataSources = new ArrayList<DataSourceBean>();
	private final List<RatioDescriptorTypeBean> ratios = new ArrayList<RatioDescriptorTypeBean>();

	private final CollapsiblePanel collapsiblePanel;
	private final TextBox textBoxProjectTag;
	private final TextArea textAreaProjectDescription;
	private final DateBox dateBoxProjectReleaseDate;
	private PintImportCfgBean pintImportCfgTypeBean;
	private final TextBox textBoxProjectName;
	private final Format format = new DateBox.DefaultFormat(DateTimeFormat.getFormat("MM/dd/yyyy"));
	public static int NUM_PROJECT = 1;
	public int internalID = NUM_PROJECT++;
	private final MyClientBundle clientBundle = GWT.create(MyClientBundle.class);
	private final PushButton uploadButton;
	private final PushButton saveConfigurationButton;
	private final Anchor linkToImportCfgFile;

	public ProjectConfigurationHeaderCollapsiblePanel(PintImportCfgBean pintImportCfgTypeBean,
			List<DataSourceBean> dataSourceBeans) {

		this.pintImportCfgTypeBean = pintImportCfgTypeBean;

		if (this.pintImportCfgTypeBean == null) {
			this.pintImportCfgTypeBean = new PintImportCfgBean();
		}

		// get the other objects referred from the project
		keepRelatedObjects(this.pintImportCfgTypeBean);
		if (this.pintImportCfgTypeBean.getProject() == null) {
			// create a new Project
			final ProjectTypeBean newProjectTypeBean = new ProjectTypeBean();
			this.pintImportCfgTypeBean.setProject(newProjectTypeBean);
			newProjectTypeBean.setName("Project name");
			newProjectTypeBean.setTag("Project tag");
			newProjectTypeBean.setDescription("Project description");
			newProjectTypeBean.setReleaseDate(new Date());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalConditions() == null) {
			this.pintImportCfgTypeBean.getProject().setExperimentalConditions(new ExperimentalConditionsTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalDesign() == null) {
			this.pintImportCfgTypeBean.getProject().setExperimentalDesign(new ExperimentalDesignTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalDesign().getLabelSet() == null) {
			this.pintImportCfgTypeBean.getProject().getExperimentalDesign().setLabelSet(new LabelSetTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalDesign().getOrganismSet() == null) {
			this.pintImportCfgTypeBean.getProject().getExperimentalDesign().setOrganismSet(new OrganismSetTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet() == null) {
			this.pintImportCfgTypeBean.getProject().getExperimentalDesign().setSampleSet(new SampleSetTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getExperimentalDesign().getTissueSet() == null) {
			this.pintImportCfgTypeBean.getProject().getExperimentalDesign().setTissueSet(new TissueSetTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getMsRuns() == null) {
			this.pintImportCfgTypeBean.getProject().setMsRuns(new MsRunsTypeBean());
		}
		if (this.pintImportCfgTypeBean.getProject().getRatios() == null) {
			this.pintImportCfgTypeBean.getProject().setRatios(new RatiosTypeBean());
		}
		if (this.pintImportCfgTypeBean.getServers() == null) {
			this.pintImportCfgTypeBean.setServers(new ServersTypeBean());
		}
		if (this.pintImportCfgTypeBean.getFileSet() == null) {
			this.pintImportCfgTypeBean.setFileSet(new FileSetTypeBean());
		}

		if (dataSourceBeans != null) {
			dataSources.addAll(dataSourceBeans);
		}

		final FlowPanel mainPanel = new FlowPanel();
		mainPanel.setSize("365px", "100%");
		mainPanel.setStyleName("ProjectConfigurationHeaderPanel");

		collapsiblePanel = new CollapsiblePanel();
		collapsiblePanel.setCollapsedState(false);
		collapsiblePanel.add(mainPanel);
		initWidget(collapsiblePanel);
		// collapsiblePanel.setHeight("838px");

		// button to show or hidde the collapsible panel
		final ToggleButton controlButton = new ToggleButton("(click to pin)", "(click to collapse)");
		controlButton.setStyleName("CollapsibleToggle");

		// panel containing the control button
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setWidth("100%");

		panel.setCellHorizontalAlignment(controlButton, HasHorizontalAlignment.ALIGN_LEFT);

		panel.add(controlButton);
		controlButton.setSize("100%", "100%");
		panel.setCellWidth(controlButton, "1px");
		panel.setCellHorizontalAlignment(controlButton, HasHorizontalAlignment.ALIGN_CENTER);
		panel.setStyleName("nav-tree-title");
		mainPanel.add(panel);

		collapsiblePanel.hookupControlToggle(controlButton);

		final FlowPanel uploadIconFlowPanel = new FlowPanel();
		uploadIconFlowPanel.setStyleName("ProjectConfigurationHeaderPanelTopHeader-button");
		mainPanel.add(uploadIconFlowPanel);

		final Grid grid_2 = new Grid(3, 2);
		grid_2.setCellPadding(10);
		uploadIconFlowPanel.add(grid_2);

		final Label lblSaveImportConfiguration = new Label("Save import cfg on server:");
		lblSaveImportConfiguration.setStyleName("horizontalComponent");
		grid_2.setWidget(0, 0, lblSaveImportConfiguration);

		saveConfigurationButton = new PushButton(new Image(clientBundle.floppyBlack24()));
		saveConfigurationButton.getDownFace().setImage(new Image(clientBundle.floppyOrange24()));
		saveConfigurationButton.getUpHoveringFace().setImage(new Image(clientBundle.floppyGold24()));
		saveConfigurationButton.getUpDisabledFace().setImage(new Image(clientBundle.floppyGrey24()));
		saveConfigurationButton.setTitle("Save import data configuration to the server");
		saveConfigurationButton.setStyleName("horizontalComponent");
		grid_2.setWidget(0, 1, saveConfigurationButton);
		saveConfigurationButton.setSize("24px", "24px");
		grid_2.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid_2.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);

		linkToImportCfgFile = new Anchor("[xml]");
		linkToImportCfgFile.setVisible(false);
		linkToImportCfgFile.setTarget("_blank");
		linkToImportCfgFile.setTitle("Click to download saved Import Configuration file");
		linkToImportCfgFile.setStyleName("linkPINT");
		grid_2.setWidget(1, 1, linkToImportCfgFile);

		final Label lblNewLabel_1 = new Label("Submit data to server:");
		grid_2.setWidget(2, 0, lblNewLabel_1);
		lblNewLabel_1.setStyleName("horizontalComponent");
		uploadButton = new PushButton(new Image(clientBundle.uploadBlack40()));
		uploadButton.setEnabled(false);
		uploadButton.getUpDisabledFace().setImage(new Image(clientBundle.uploadGrey40()));
		uploadButton.getUpHoveringFace().setImage(new Image(clientBundle.uploadGold40()));
		uploadButton.getDownFace().setImage(new Image(clientBundle.uploadOrange40()));
		uploadButton.setSize("40px", "40px");
		uploadButton.setStyleName("horizontalComponent");
		uploadButton.setTitle("Submit data to server");
		grid_2.setWidget(2, 1, uploadButton);
		grid_2.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		grid_2.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		grid_2.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);

		final Label lblDownloadImportConfiguration = new Label("Download import cfg file:");
		grid_2.setWidget(1, 0, lblDownloadImportConfiguration);
		grid_2.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		grid_2.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid_2.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		grid_2.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		final Label lblProjectConfiguration = new Label("Project definition");
		mainPanel.add(lblProjectConfiguration);
		lblProjectConfiguration.setStyleName("ProjectConfigurationHeaderPanelTopHeader");

		final FlowPanel flowPanel_6 = new FlowPanel();
		flowPanel_6.setStyleName("verticalComponent");
		mainPanel.add(flowPanel_6);

		final FlowPanel flowPanel_2 = new FlowPanel();
		flowPanel_6.add(flowPanel_2);
		flowPanel_2.setStyleName("ProjectconfigurationHeaderPanelLeftComponents");

		final Label lblProjectShortName = new Label("Project tag:");
		final String projectTagTooltip = "This tag is going to be the idenfier of the project.\nIt is limited to 15 characters.";
		lblProjectShortName.setTitle(projectTagTooltip);
		flowPanel_2.add(lblProjectShortName);
		lblProjectShortName.setStyleName("verticalComponent");

		textBoxProjectTag = new TextBox();
		textBoxProjectTag.setTitle(projectTagTooltip);
		flowPanel_2.add(textBoxProjectTag);
		textBoxProjectTag.setVisibleLength(15);
		textBoxProjectTag.setStyleName("verticalComponent");
		textBoxProjectTag.setMaxLength(15);

		final FlowPanel flowPanel_1 = new FlowPanel();
		flowPanel_1.setStyleName("ProjectconfigurationHeaderPanelLeftComponents");
		flowPanel_6.add(flowPanel_1);

		final Label lblNewLabel = new Label("Project name:");
		lblNewLabel.setStyleName("verticalComponent");
		flowPanel_1.add(lblNewLabel);

		textBoxProjectName = new TextBox();
		textBoxProjectName.setWidth("90%");
		textBoxProjectName.setMaxLength(150);
		flowPanel_1.add(textBoxProjectName);

		final FlowPanel flowPanel_3 = new FlowPanel();
		flowPanel_6.add(flowPanel_3);
		flowPanel_3.setStyleName("ProjectconfigurationHeaderPanelLeftComponents");

		final Label lblProjectDescription = new Label("Project description:");
		lblProjectDescription.setStyleName("verticalComponent");
		flowPanel_3.add(lblProjectDescription);

		textAreaProjectDescription = new TextArea();
		textAreaProjectDescription.setStyleName("ProjectconfigurationHeaderTextArea");
		textAreaProjectDescription.setVisibleLines(7);
		flowPanel_3.add(textAreaProjectDescription);
		textAreaProjectDescription.setWidth("90%");

		final FlowPanel flowPanel_4 = new FlowPanel();
		flowPanel_6.add(flowPanel_4);
		flowPanel_4.setStyleName("ProjectconfigurationHeaderPanelLeftComponents");

		final Label lblReleaseDate = new Label("Release date:");
		lblReleaseDate.setStyleName("verticalComponent");
		flowPanel_4.add(lblReleaseDate);

		dateBoxProjectReleaseDate = new DateBox();
		dateBoxProjectReleaseDate.setValue(new Date());
		dateBoxProjectReleaseDate.setStyleName("verticalComponent");
		dateBoxProjectReleaseDate.setFormat(format);
		flowPanel_4.add(dateBoxProjectReleaseDate);

		final FlowPanel flowPanel_8 = new FlowPanel();
		flowPanel_8.setStyleName("verticalComponent");
		mainPanel.add(flowPanel_8);
		flowPanel_8.setHeight("100%");

		final FlowPanel flowPanel = new FlowPanel();
		flowPanel.setStyleName("ProjectconfigurationHeaderPanelLeftComponents");
		flowPanel_8.add(flowPanel);

		final InlineLabel nlnlblNewInlinelabel = new InlineLabel(
				"Configure your project by adding or removing the following elements:");
		flowPanel.add(nlnlblNewInlinelabel);
		nlnlblNewInlinelabel.setStyleName("verticalComponent");

		final Grid grid = new Grid(9, 2);
		flowPanel_8.add(grid);
		grid.setWidth("100%");

		final InlineLabel nlnlblNewInlinelabel_1 = new InlineLabel("Experimental condition(s):");
		grid.setWidget(0, 0, nlnlblNewInlinelabel_1);

		incrementableTextBoxConditions = new IncrementableTextBox(0);
		grid.setWidget(0, 1, incrementableTextBoxConditions);

		final InlineLabel lblRatiosBetweenConditions = new InlineLabel("Ratio(s) between conditions:");
		grid.setWidget(1, 0, lblRatiosBetweenConditions);

		incrementableTextBoxRatios = new IncrementableTextBox(0);
		incrementableTextBoxRatios.setValue(0);
		grid.setWidget(1, 1, incrementableTextBoxRatios);

		final InlineLabel nlnlblNewInlinelabel_2 = new InlineLabel("Sample(s):");
		grid.setWidget(2, 0, nlnlblNewInlinelabel_2);

		incrementableTextBoxSamples = new IncrementableTextBox(0);
		grid.setWidget(2, 1, incrementableTextBoxSamples);

		final InlineLabel nlnlblNewInlinelabel_3 = new InlineLabel("MS run(s):");
		grid.setWidget(3, 0, nlnlblNewInlinelabel_3);

		incrementableTextBoxRuns = new IncrementableTextBox(0);
		grid.setWidget(3, 1, incrementableTextBoxRuns);

		final InlineLabel nlnlblNewInlinelabel_7 = new InlineLabel("Input file(s):");
		grid.setWidget(4, 0, nlnlblNewInlinelabel_7);

		incrementableTextBoxDataSources = new IncrementableTextBox(0);
		grid.setWidget(4, 1, incrementableTextBoxDataSources);

		final InlineLabel nlnlblNewInlinelabel_8 = new InlineLabel("Server connection(s):");
		grid.setWidget(5, 0, nlnlblNewInlinelabel_8);

		incrementableTextBoxServers = new IncrementableTextBox(0);
		grid.setWidget(5, 1, incrementableTextBoxServers);

		final InlineLabel nlnlblNewInlinelabel_4 = new InlineLabel("Sample origin(s):");
		grid.setWidget(6, 0, nlnlblNewInlinelabel_4);

		// second column
		incrementableTextBoxTissues = new IncrementableTextBox(0);
		grid.setWidget(6, 1, incrementableTextBoxTissues);

		final InlineLabel nlnlblNewInlinelabel_5 = new InlineLabel("Organism(s):");
		grid.setWidget(7, 0, nlnlblNewInlinelabel_5);

		incrementableTextBoxOrganisms = new IncrementableTextBox(0);
		grid.setWidget(7, 1, incrementableTextBoxOrganisms);

		final InlineLabel nlnlblNewInlinelabel_6 = new InlineLabel("Label(s):");
		grid.setWidget(8, 0, nlnlblNewInlinelabel_6);

		incrementableTextBoxLabels = new IncrementableTextBox(0);
		grid.setWidget(8, 1, incrementableTextBoxLabels);
		grid.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(7, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(8, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		// add modification handlers
		addModificationHandlers();

		// update the numbers according to the list of conditions and msRuns
		updateIncrementableTextBoxes();

		// update project GUI
		updateGUIFromObjectData();
	}

	public void uploadButtonEnabled(boolean enabled) {
		uploadButton.setEnabled(enabled);
	}

	/**
	 * Save in the instance lists the objects related to the project:
	 * {@link SampleBean}, {@link LabelBean}, {@link OrganismBean} and
	 * {@link TissueBean}
	 *
	 * @param pintImportCfgTypeBean
	 */
	private void keepRelatedObjects(PintImportCfgTypeBean pintImportCfgTypeBean) {
		final ProjectTypeBean project = pintImportCfgTypeBean.getProject();
		if (project != null && project.getExperimentalDesign() != null) {
			final ExperimentalDesignTypeBean experimentalDesignTypeBean = project.getExperimentalDesign();
			if (experimentalDesignTypeBean.getSampleSet() != null) {
				for (final SampleTypeBean sampleTypeBean : experimentalDesignTypeBean.getSampleSet().getSample()) {
					addSample(sampleTypeBean);
				}
			}
			if (experimentalDesignTypeBean.getLabelSet() != null) {
				for (final LabelTypeBean labelTypeBean : experimentalDesignTypeBean.getLabelSet().getLabel()) {
					addLabel(labelTypeBean);
				}
			}
			if (experimentalDesignTypeBean.getOrganismSet() != null) {
				for (final OrganismTypeBean organismTypeBean : experimentalDesignTypeBean.getOrganismSet()
						.getOrganism()) {
					addOrganism(organismTypeBean);
				}
			}
			if (experimentalDesignTypeBean.getTissueSet() != null) {
				for (final TissueTypeBean tissueTypeBean : experimentalDesignTypeBean.getTissueSet().getTissue()) {
					addTissue(tissueTypeBean);
				}
			}
		}

	}

	private void addModificationHandlers() {
		textBoxProjectTag.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateRepresentedObject();
			}
		});
		textBoxProjectName.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateRepresentedObject();
			}
		});
		textAreaProjectDescription.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateRepresentedObject();
			}
		});
		dateBoxProjectReleaseDate.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				updateRepresentedObject();
			}
		});

	}

	/**
	 * Adds a click handler to be call when submission click is occurring
	 *
	 * @param clickHandler
	 */
	public void addDataSubmissionClickHandler(ClickHandler clickHandler) {
		uploadButton.addClickHandler(clickHandler);
	}

	/**
	 * Adds a click handler to be call when save configuration click is
	 * occurring
	 *
	 * @param clickHandler
	 */
	public void addSaveImportConfigurationClickHandler(ClickHandler clickHandler) {
		saveConfigurationButton.addClickHandler(clickHandler);
	}

	public void addConditions(Collection<ExperimentalConditionTypeBean> conditions) {
		for (final ExperimentalConditionTypeBean experimentalConditionBean : conditions) {
			addCondition(experimentalConditionBean);
		}
	}

	public void addCondition(ExperimentalConditionTypeBean experimentalConditionBean) {
		if (!pintImportCfgTypeBean.getProject().getExperimentalConditions().getExperimentalCondition()
				.contains(experimentalConditionBean)) {
			pintImportCfgTypeBean.getProject().getExperimentalConditions().getExperimentalCondition()
					.add(experimentalConditionBean);
			updateIncrementableTextBoxes();
		}
	}

	public void addMSRun(Collection<MsRunTypeBean> msRuns) {
		for (final MsRunTypeBean msRunBean : msRuns) {
			addMSRun(msRunBean);
		}
	}

	public void addMSRun(MsRunTypeBean msRunBean) {
		if (!pintImportCfgTypeBean.getProject().getMsRuns().getMsRun().contains(msRunBean)) {
			pintImportCfgTypeBean.getProject().getMsRuns().getMsRun().add(msRunBean);
			updateIncrementableTextBoxes();
		}
	}

	public void addRatio(Collection<RatioDescriptorTypeBean> ratios) {
		for (final RatioDescriptorTypeBean ratioBean : ratios) {
			addRatio(ratioBean);
		}
	}

	public void addRatio(RatioDescriptorTypeBean ratioBean) {
		if (!ratios.contains(ratioBean)) {
			ratios.add(ratioBean);
			updateIncrementableTextBoxes();
		}
	}

	/**
	 * Updates the number on the text box according to the objects of the class,
	 * such as {@link ProjectBean} and {@link MsRunTypeBean}
	 */
	public void updateIncrementableTextBoxes() {
		incrementableTextBoxConditions.setValue(getExperimentalConditions().size());
		incrementableTextBoxRatios.setValue(ratios.size());
		incrementableTextBoxRuns.setValue(getMSRuns().size());
		incrementableTextBoxLabels.setValue(getLabels().size());
		final List<OrganismTypeBean> organisms = getOrganisms();
		incrementableTextBoxOrganisms.setValue(organisms.size());
		incrementableTextBoxSamples.setValue(getSamples().size());
		incrementableTextBoxTissues.setValue(getTissues().size());
		incrementableTextBoxDataSources.setValue(getDataSources().size());
		incrementableTextBoxServers.setValue(getServers().size());

	}

	public List<ServerTypeBean> getServers() {
		if (pintImportCfgTypeBean.getServers() != null)
			return pintImportCfgTypeBean.getServers().getServer();
		return null;
	}

	public List<DataSourceBean> getDataSources() {
		return dataSources;
	}

	public List<LabelTypeBean> getLabels() {

		return pintImportCfgTypeBean.getProject().getExperimentalDesign().getLabelSet().getLabel();
	}

	public List<OrganismTypeBean> getOrganisms() {

		return pintImportCfgTypeBean.getProject().getExperimentalDesign().getOrganismSet().getOrganism();
	}

	public List<TissueTypeBean> getTissues() {

		return pintImportCfgTypeBean.getProject().getExperimentalDesign().getTissueSet().getTissue();
	}

	public List<MsRunTypeBean> getMSRuns() {
		return pintImportCfgTypeBean.getProject().getMsRuns().getMsRun();
	}

	public List<SampleTypeBean> getSamples() {
		return pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet().getSample();
	}

	public List<ExperimentalConditionTypeBean> getExperimentalConditions() {
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null)
			return pintImportCfgTypeBean.getProject().getExperimentalConditions().getExperimentalCondition();
		return Collections.emptyList();
	}

	public List<ExcelAmountRatioTypeBean> getPeptideExcelRatios() {
		final List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();
			// peptide ratios
			final PeptideRatiosTypeBean peptideAmountRatios = ratiosType.getPeptideAmountRatios();
			if (peptideAmountRatios != null) {
				final List<ExcelAmountRatioTypeBean> excelRatios = peptideAmountRatios.getExcelRatio();
				if (excelRatios != null) {
					return excelRatios;
				}

			}

		}
		return ret;
	}

	public List<ExcelAmountRatioTypeBean> getPSMExcelRatios() {
		final List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();
			// peptide ratios
			final PsmRatiosTypeBean psmAmountRatios = ratiosType.getPsmAmountRatios();
			if (psmAmountRatios != null) {
				final List<ExcelAmountRatioTypeBean> excelRatios = psmAmountRatios.getExcelRatio();
				if (excelRatios != null) {
					return excelRatios;
				}

			}

		}
		return ret;
	}

	public List<RemoteFilesRatioTypeBean> getPSMRemoteFileRatios() {
		final List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();
			// peptide ratios
			final PsmRatiosTypeBean psmAmountRatios = ratiosType.getPsmAmountRatios();
			if (psmAmountRatios != null) {

				final List<RemoteFilesRatioTypeBean> remoteFilesRatios = psmAmountRatios.getRemoteFilesRatio();
				if (remoteFilesRatios != null) {
					return remoteFilesRatios;
				}
			}

		}
		return ret;
	}

	public List<RemoteFilesRatioTypeBean> getPeptideRemoteFileRatios() {
		final List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();
			// peptide ratios
			final PeptideRatiosTypeBean peptideAmountRatios = ratiosType.getPeptideAmountRatios();
			if (peptideAmountRatios != null) {

				final List<RemoteFilesRatioTypeBean> remoteFilesRatios = peptideAmountRatios.getRemoteFilesRatio();
				if (remoteFilesRatios != null) {
					return remoteFilesRatios;
				}
			}

		}
		return ret;
	}

	public List<RemoteFilesRatioTypeBean> getProteinRemoteFileRatios() {
		final List<RemoteFilesRatioTypeBean> ret = new ArrayList<RemoteFilesRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();
			// peptide ratios
			final ProteinRatiosTypeBean proteinAmountRatios = ratiosType.getProteinAmountRatios();
			if (proteinAmountRatios != null) {

				final List<RemoteFilesRatioTypeBean> remoteFilesRatios = proteinAmountRatios.getRemoteFilesRatio();
				if (remoteFilesRatios != null) {
					return remoteFilesRatios;
				}
			}

		}
		return ret;
	}

	public List<ExcelAmountRatioTypeBean> getProteinExcelRatios() {
		final List<ExcelAmountRatioTypeBean> ret = new ArrayList<ExcelAmountRatioTypeBean>();
		if (pintImportCfgTypeBean != null && pintImportCfgTypeBean.getProject() != null
				&& pintImportCfgTypeBean.getProject().getRatios() != null) {

			final RatiosTypeBean ratiosType = pintImportCfgTypeBean.getProject().getRatios();

			// protein ratios
			final ProteinRatiosTypeBean proteinAmountRatios = ratiosType.getProteinAmountRatios();
			if (proteinAmountRatios != null) {
				final List<ExcelAmountRatioTypeBean> excelRatios = proteinAmountRatios.getExcelRatio();
				if (excelRatios != null) {
					return excelRatios;
				}
			}
		}
		return ret;
	}

	private RatioDescriptorTypeBean getRatio(String condition1ID, String condition2ID) {
		final RatioDescriptorTypeBean ratio = new RatioDescriptorTypeBean();
		ratio.setCondition1(getExperimentalConditionBeanByID(condition1ID));
		ratio.setCondition2(getExperimentalConditionBeanByID(condition2ID));
		return ratio;
	}

	private ExperimentalConditionTypeBean getExperimentalConditionBeanByID(String conditionID) {
		final List<ExperimentalConditionTypeBean> experimentalConditions = getExperimentalConditions();
		for (final ExperimentalConditionTypeBean experimentalConditionTypeBean : experimentalConditions) {
			if (experimentalConditionTypeBean.getId().equals(conditionID))
				return experimentalConditionTypeBean;
		}
		return null;
	}

	public void addHandlerAddCondition(ClickHandler clickHandler) {
		incrementableTextBoxConditions.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveCondition(ClickHandler clickHandler) {
		incrementableTextBoxConditions.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddLabel(ClickHandler clickHandler) {
		incrementableTextBoxLabels.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveLabel(ClickHandler clickHandler) {
		incrementableTextBoxLabels.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddOrganism(ClickHandler clickHandler) {
		incrementableTextBoxOrganisms.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveOrganism(ClickHandler clickHandler) {
		incrementableTextBoxOrganisms.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddMSRun(ClickHandler clickHandler) {
		incrementableTextBoxRuns.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveMSRun(ClickHandler clickHandler) {
		incrementableTextBoxRuns.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddSample(ClickHandler clickHandler) {
		incrementableTextBoxSamples.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveSample(ClickHandler clickHandler) {
		incrementableTextBoxSamples.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddTissue(ClickHandler clickHandler) {
		incrementableTextBoxTissues.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveTissue(ClickHandler clickHandler) {
		incrementableTextBoxTissues.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddRatio(ClickHandler clickHandler) {
		incrementableTextBoxRatios.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveRatio(ClickHandler clickHandler) {
		incrementableTextBoxRatios.addMinusButtonHandler(clickHandler);
	}

	public void removeCondition(ExperimentalConditionTypeBean condition) {
		pintImportCfgTypeBean.getProject().getExperimentalConditions().getExperimentalCondition().remove(condition);
		updateIncrementableTextBoxes();
	}

	public void removeRatio(RatioDescriptorTypeBean ratio) {
		ratios.remove(ratio);
		updateIncrementableTextBoxes();
	}

	public void removeSample(SampleTypeBean sampleToRemove) {
		pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet().getSample().remove(sampleToRemove);
		for (final ExperimentalConditionTypeBean condition : pintImportCfgTypeBean.getProject()
				.getExperimentalConditions().getExperimentalCondition()) {
			final String sample = condition.getSampleRef();
			if (sample != null && sample.equals(sampleToRemove.getId())) {
				condition.setSampleRef(null);
			}
		}
		updateIncrementableTextBoxes();
	}

	public void removeLabel(LabelTypeBean labelToRemove) {
		pintImportCfgTypeBean.getProject().getExperimentalDesign().getLabelSet().getLabel().remove(labelToRemove);
		for (final SampleTypeBean sample : pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet()
				.getSample()) {
			final String label = sample.getLabelRef();
			if (label != null && label.equals(labelToRemove.getId())) {
				sample.setLabelRef(null);
			}
		}
		updateIncrementableTextBoxes();
	}

	public void removeMSRun(MsRunTypeBean msRunToRemove) {
		pintImportCfgTypeBean.getProject().getMsRuns().getMsRun().remove(msRunToRemove);
		updateIncrementableTextBoxes();
	}

	public void removeOrganism(OrganismTypeBean organismToRemove) {
		pintImportCfgTypeBean.getProject().getExperimentalDesign().getOrganismSet().getOrganism()
				.remove(organismToRemove);
		for (final SampleTypeBean sample : pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet()
				.getSample()) {
			if (sample != null && sample.getOrganismRef() != null
					&& sample.getOrganismRef().equals(organismToRemove.getId())) {
				sample.setOrganismRef(null);
			}
		}
		updateIncrementableTextBoxes();
	}

	public void removeTissue(TissueTypeBean tissueToRemove) {
		pintImportCfgTypeBean.getProject().getExperimentalDesign().getTissueSet().getTissue().remove(tissueToRemove);
		for (final SampleTypeBean sample : pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet()
				.getSample()) {
			final String tissue = sample.getTissueRef();
			if (tissue != null && tissue.equals(tissueToRemove.getId())) {
				sample.setTissueRef(null);
			}
		}
		updateIncrementableTextBoxes();
	}

	public void addDataSource(DataSourceBean dataSource) {
		if (!dataSources.contains(dataSource)) {
			dataSources.add(dataSource);
			updateIncrementableTextBoxes();
		}
	}

	public void removeDataSource(DataSourceBean dataSourceToRemove) {
		dataSources.remove(dataSourceToRemove);
		updateIncrementableTextBoxes();
	}

	public void addServer(ServerTypeBean server) {
		if (!pintImportCfgTypeBean.getServers().getServer().contains(server)) {
			pintImportCfgTypeBean.getServers().getServer().add(server);
			updateIncrementableTextBoxes();
		}
	}

	public void removeServer(ServerTypeBean serverToRemove) {
		pintImportCfgTypeBean.getServers().getServer().remove(serverToRemove);

	}

	public void addHandlerAddDataSource(ClickHandler clickHandler) {
		incrementableTextBoxDataSources.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveDataSource(ClickHandler clickHandler) {
		incrementableTextBoxDataSources.addMinusButtonHandler(clickHandler);
	}

	public void addHandlerAddServer(ClickHandler clickHandler) {
		incrementableTextBoxServers.addPlusButtonHandler(clickHandler);
	}

	public void addHandlerRemoveServer(ClickHandler clickHandler) {
		incrementableTextBoxServers.addMinusButtonHandler(clickHandler);
	}

	@Override
	public int getInternalID() {
		return internalID;
	}

	@Override
	public String getID() {
		return textBoxProjectName.getName();
	}

	@Override
	public ProjectTypeBean getObject() {
		return pintImportCfgTypeBean.getProject();
	}

	@Override
	public void updateRepresentedObject() {
		pintImportCfgTypeBean.getProject().setDescription(textAreaProjectDescription.getText());
		pintImportCfgTypeBean.getProject().setTag(textBoxProjectTag.getText());
		pintImportCfgTypeBean.getProject().setName(textBoxProjectName.getText());
		pintImportCfgTypeBean.getProject().setReleaseDate(dateBoxProjectReleaseDate.getValue());
	}

	/**
	 * @return the pintImportCfgTypeBean
	 */
	public PintImportCfgBean getPintImportCfgTypeBean() {
		updateRepresentedObject();
		return pintImportCfgTypeBean;
	}

	@Override
	public void updateGUIFromObjectData(HasId object) {
		if (object instanceof ProjectTypeBean) {
			pintImportCfgTypeBean.setProject((ProjectTypeBean) object);
			updateGUIFromObjectData();
		}

	}

	@Override
	public void updateGUIFromObjectData() {
		if (pintImportCfgTypeBean != null) {
			final ProjectTypeBean projectTypeBean = pintImportCfgTypeBean.getProject();
			textAreaProjectDescription.setText(projectTypeBean.getDescription());
			textBoxProjectTag.setText(projectTypeBean.getTag());
			textBoxProjectName.setText(projectTypeBean.getName());
			dateBoxProjectReleaseDate.setValue(projectTypeBean.getReleaseDate());
		}

	}

	public String getProjectTag() {
		return textBoxProjectTag.getText();
	}

	public List<FileNameWithTypeBean> getFileNamesWithTypes() {
		final List<FileNameWithTypeBean> ret = new ArrayList<FileNameWithTypeBean>();
		for (final DataSourceBean dataSourceBean : getDataSources()) {
			// only include if datasource has no server ref and has some format
			if (dataSourceBean.getServer() == null && dataSourceBean.getFormat() != null) {

				final FileNameWithTypeBean bean = new FileNameWithTypeBean();
				bean.setId(dataSourceBean.getId());
				bean.setFileName(dataSourceBean.getFileName());
				bean.setFileFormat(dataSourceBean.getFormat());
				bean.setFastaDigestionBean(dataSourceBean.getFastaDigestionBean());
				ret.add(bean);
			}
		}
		return ret;
	}

	public List<RemoteFileWithTypeBean> getRemoteFileWithTypeBeans() {
		final List<RemoteFileWithTypeBean> ret = new ArrayList<RemoteFileWithTypeBean>();
		for (final DataSourceBean dataSourceBean : getDataSources()) {
			// only include if datasource has server ref and has some format,
			// fileName and relativePAth
			if (dataSourceBean.getServer() != null && dataSourceBean.getFormat() != null
					&& dataSourceBean.getFileName() != null && dataSourceBean.getRelativePath() != null) {
				final RemoteFileWithTypeBean bean = new RemoteFileWithTypeBean();
				bean.setId(dataSourceBean.getId());
				bean.setFileName(dataSourceBean.getFileName());
				bean.setFileFormat(dataSourceBean.getFormat());
				bean.setRemotePath(dataSourceBean.getRelativePath());
				bean.setServer(dataSourceBean.getServer());
				ret.add(bean);
			}
		}
		return ret;
	}

	public void addSample(SampleTypeBean sample) {
		if (!pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet().getSample().contains(sample))
			pintImportCfgTypeBean.getProject().getExperimentalDesign().getSampleSet().getSample().add(sample);
	}

	public void addOrganism(OrganismTypeBean organism) {
		if (!pintImportCfgTypeBean.getProject().getExperimentalDesign().getOrganismSet().getOrganism()
				.contains(organism))
			pintImportCfgTypeBean.getProject().getExperimentalDesign().getOrganismSet().getOrganism().add(organism);
	}

	public void addTissue(TissueTypeBean tissue) {
		if (!pintImportCfgTypeBean.getProject().getExperimentalDesign().getTissueSet().getTissue().contains(tissue))
			pintImportCfgTypeBean.getProject().getExperimentalDesign().getTissueSet().getTissue().add(tissue);

	}

	public void addLabel(LabelTypeBean label) {
		if (!pintImportCfgTypeBean.getProject().getExperimentalDesign().getLabelSet().getLabel().contains(label))
			pintImportCfgTypeBean.getProject().getExperimentalDesign().getLabelSet().getLabel().add(label);

	}

	public void enableDownloadImportCfgFile(String projectTag) {
		// final String href = Window.Location.getProtocol() + "//" +
		// Window.Location.getHost() + "/pint/download?"
		// + SharedConstants.FILE_TO_DOWNLOAD + "=" + projectTag + ".xml" + "&"
		// + SharedConstants.FILE_TYPE + "="
		// + SharedConstants.IMPORT_CFG_FILE_TYPE;
		final String href = ClientSafeHtmlUtils.getDownloadURL(projectTag + ".xml",
				SharedConstants.IMPORT_CFG_FILE_TYPE);

		linkToImportCfgFile.setVisible(true);
		linkToImportCfgFile.setHref(href);
		linkToImportCfgFile.setText("[" + projectTag + ".xml]");
	}

	public void disableDownloadImportCfgFile() {
		linkToImportCfgFile.setVisible(false);

	}

}
