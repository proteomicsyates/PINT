package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.scripps.yates.ImportWizardServiceAsync;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ReferencesDataObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.interfaces.ContainsImportJobID;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinDescriptionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class IdentificationInfoFromExcelFilePanel extends Composite
		implements ReferencesDataObject, ContainsImportJobID, RepresentsObject<IdentificationExcelTypeBean> {

	private final Button checkButton;

	private final SimpleCheckBox discardDecoysCheckBox;
	private final RegularExpressionTextBox decoyRegexpSuggestBox;
	private FileNameWithTypeBean excelFileWithFormatBean;
	private final ImportWizardServiceAsync serviceAsync = ImportWizardServiceAsync.Util.getInstance();

	private int importJobID;
	private int numTestCases = 100;
	private final FlowPanel flowPanelProteinAnnotation;
	private final MyExcelSectionFlowPanel flowPanelProteinDescription;
	private final MyExcelSectionFlowPanel flowPanelProteinAccession;
	private final FlowPanel flowPanelPTMScore;
	private final FlowPanel flowPanelPSMScore;
	private final FlowPanel flowPanelPeptideScore;
	private final MyExcelSectionFlowPanel flowPanelPeptideSequence;
	private final FlowPanel flowPanelProteinScore;
	private final IncrementableTextBox proteinAnnotationsIncrementableTextBox;
	private final IncrementableTextBox proteinScoresIncrementableTextBox;
	private final IncrementableTextBox psmScoresIncrementableTextBox;
	private final IncrementableTextBox ptmScoresIncrementableTextBox;
	private final IncrementableTextBox peptideScoresIncrementableTextBox;
	private final FlowPanel flowPanel_2;
	private final Label lblCustomizableOptionsFor;
	private final SimplePanel flowPanelOptions;
	private ProteinAccessionPanel proteinAccessionPanel;
	private ProteinDescriptionPanel proteinDescriptionPanel;
	private FileTypeBean excelFileBean;
	private final List<ProteinAnnotationPanel> proteinAnnotationPanels = new ArrayList<ProteinAnnotationPanel>();
	private final List<ProteinThresholdPanel> proteinThresholdPanels = new ArrayList<ProteinThresholdPanel>();

	private final List<ScorePanel> proteinScorePanels = new ArrayList<ScorePanel>();
	private PeptideSequencePanel peptideSequencePanel;
	private final List<ScorePanel> psmScorePanels = new ArrayList<ScorePanel>();
	private final List<ScorePanel> peptideScorePanels = new ArrayList<ScorePanel>();
	private final List<PTMScorePanel> pTMScorePanels = new ArrayList<PTMScorePanel>();
	private final FlowPanel flowPanel_3;
	private final Label lblMoveTheMouse;
	private final SimplePanel scrollPanelTable;
	private final Label lblMsRun;
	private final ListBox msRunComboListBox;

	private IdentificationExcelTypeBean identificationExcelTypeBean = new IdentificationExcelTypeBean();
	private final Label lblProteinDescription;

	private final String sessionID;
	private final IncrementableTextBox proteinThresholdsIncrementableTextBox;

	private final FlowPanel flowPanelProteinThresholds;

	private final Image loadingImage;
	private int numLoadingProcesses = 0;

	private synchronized void increaseLoadingProcesses() {
		numLoadingProcesses++;
		loadingImage.setVisible(numLoadingProcesses > 0);
		checkButton.setEnabled(numLoadingProcesses <= 0);
	}

	private synchronized void decreaseLoadingProcesses() {
		numLoadingProcesses--;
		loadingImage.setVisible(numLoadingProcesses > 0);
		checkButton.setEnabled(numLoadingProcesses <= 0);
	}

	public IdentificationInfoFromExcelFilePanel(String sessionID, int importJobID) {
		this.importJobID = importJobID;
		this.sessionID = sessionID;

		FlexTable flexTable = new FlexTable();
		initWidget(flexTable);
		flexTable.setBorderWidth(0);
		flexTable.setSize("900px", "500px");

		FlexTable grid = new FlexTable();
		grid.setCellPadding(5);
		grid.setBorderWidth(0);
		flexTable.setWidget(0, 0, grid);
		grid.setStyleName("verticalComponent");
		grid.setSize("400px", "100%");

		Label lblDecoy = new Label("Discard decoy hits:");
		grid.setWidget(0, 0, lblDecoy);
		grid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		discardDecoysCheckBox = new SimpleCheckBox();
		grid.setWidget(0, 1, discardDecoysCheckBox);

		lblDecoy.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// change the value of discard decoys checkbox
				discardDecoysCheckBox.setValue(!discardDecoysCheckBox.getValue());
			}
		});

		Label lblRegularExpressionFor = new Label("Regular expression for decoy accessions:");
		grid.setWidget(1, 0, lblRegularExpressionFor);

		decoyRegexpSuggestBox = new RegularExpressionTextBox("Reverse", 1);
		decoyRegexpSuggestBox.setEnabled(false);
		grid.setWidget(1, 1, decoyRegexpSuggestBox);
		grid.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		lblMsRun = new Label("MS Run:");
		grid.setWidget(2, 0, lblMsRun);

		msRunComboListBox = new ListBox();
		msRunComboListBox.setMultipleSelect(true);
		msRunComboListBox.setVisibleItemCount(4);
		grid.setWidget(2, 1, msRunComboListBox);

		InlineLabel nlnlblCheckRemoteData = new InlineLabel("Re-load data:");
		final String title = "Click on the button in order to re-load a sample of the data and visualize it in the different tables showed for each configured item below";
		nlnlblCheckRemoteData.setTitle(title);
		grid.setWidget(3, 0, nlnlblCheckRemoteData);

		checkButton = new Button("check");
		checkButton.setTitle(title);

		checkButton.setText("re-load data");
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(checkButton);
		loadingImage = new Image(MyClientBundle.INSTANCE.smallLoader());
		loadingImage.setTitle("Loading data from Excel...");
		loadingImage.setVisible(false);
		panel.add(loadingImage);
		panel.getElement().getStyle().setPadding(20, Unit.PX);
		grid.setWidget(3, 1, panel);

		grid.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		grid.getCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);

		FlowPanel flowPanel = new FlowPanel();

		flowPanel.setStyleName("IdentificationInfoRemoteFilePanel");
		flexTable.setWidget(2, 0, flowPanel);

		FlowPanel righFlowPanel = new FlowPanel();

		FlowPanel flowPanel_6 = new FlowPanel();
		flowPanel_6.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		righFlowPanel.add(flowPanel_6);
		Label lblDataTable = new Label("Data table:");
		flowPanel_6.add(lblDataTable);

		scrollPanelTable = new SimplePanel();
		scrollPanelTable.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		scrollPanelTable.setSize("", "450px");
		righFlowPanel.add(scrollPanelTable);

		FlowPanel flowPanelCenter = new FlowPanel();
		flowPanelCenter.setSize("480px", "");

		Grid grid_1 = new Grid(1, 3);
		grid_1.setBorderWidth(0);
		grid_1.setCellPadding(10);
		flowPanel.add(grid_1);
		grid_1.setSize("100%", "240px");

		FlowPanel flowPanelLeft = new FlowPanel();
		grid_1.setWidget(0, 0, flowPanelLeft);
		flowPanelLeft.setWidth("305px");

		flowPanel_3 = new FlowPanel();
		flowPanel_3.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		flowPanelLeft.add(flowPanel_3);

		lblMoveTheMouse = new Label("Move the mouse over these options to configure:");
		flowPanel_3.add(lblMoveTheMouse);

		flowPanelOptions = new SimplePanel();
		flowPanelOptions.setStyleName("IdentificationInfoFromExcelEmptyPanel");

		flowPanelProteinAccession = new MyExcelSectionFlowPanel(null, flowPanelOptions, getProteinAccessionPanel(),
				scrollPanelTable);
		flowPanelLeft.add(flowPanelProteinAccession);

		Label proteinAccessionLabel = new Label("Protein accession");
		flowPanelProteinAccession.add(proteinAccessionLabel);

		flowPanelProteinDescription = new MyExcelSectionFlowPanel(null, flowPanelOptions, getProteinDescriptionPanel(),
				scrollPanelTable);
		flowPanelLeft.add(flowPanelProteinDescription);

		lblProteinDescription = new Label("Protein description");
		flowPanelProteinDescription.add(lblProteinDescription);

		flowPanelProteinAnnotation = new FlowPanel();
		flowPanelProteinAnnotation.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelProteinAnnotation);

		Grid grid_2 = new Grid(1, 2);
		flowPanelProteinAnnotation.add(grid_2);
		grid_2.setWidth("100%");
		Label label = new Label("Protein annotations:");
		grid_2.setWidget(0, 0, label);

		proteinAnnotationsIncrementableTextBox = new IncrementableTextBox(0);
		grid_2.setWidget(0, 1, proteinAnnotationsIncrementableTextBox);
		grid_2.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		proteinAnnotationsIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinAnnotation
				if (identificationExcelTypeBean.getProteinAnnotations() == null) {
					identificationExcelTypeBean.setProteinAnnotations(new ProteinAnnotationsTypeBean());
				}
				final ProteinAnnotationTypeBean newProteinAnnotationTypeBean = new ProteinAnnotationTypeBean();
				identificationExcelTypeBean.getProteinAnnotations().getProteinAnnotation()
						.add(newProteinAnnotationTypeBean);
				createNewProteinAnnotationPanel(newProteinAnnotationTypeBean);

			}
		});
		proteinAnnotationsIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinAnnotation
				if (!proteinAnnotationPanels.isEmpty()) {
					ProteinAnnotationPanel proteinAnnotationPanel = proteinAnnotationPanels
							.get(proteinAnnotationPanels.size() - 1);
					identificationExcelTypeBean.getProteinAnnotations().getProteinAnnotation()
							.remove(proteinAnnotationPanel.getRepresentedObject());
					proteinAnnotationPanel.removeFromParent();
					if (proteinAnnotationPanel.getRelatedExcelSectionFlowPanel() != null) {
						proteinAnnotationPanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					proteinAnnotationPanels.remove(proteinAnnotationPanel);
				}
			}
		});

		flowPanelProteinThresholds = new FlowPanel();
		flowPanelProteinThresholds.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelProteinThresholds);

		Grid grid_7 = new Grid(1, 2);
		flowPanelProteinThresholds.add(grid_7);
		grid_7.setWidth("100%");

		Label lblProteinThresholds = new Label("Protein thresholds:");
		grid_7.setWidget(0, 0, lblProteinThresholds);

		proteinThresholdsIncrementableTextBox = new IncrementableTextBox(0);
		proteinThresholdsIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinThreshold
				if (identificationExcelTypeBean.getProteinThresholds() == null) {
					identificationExcelTypeBean.setProteinThresholds(new ProteinThresholdsTypeBean());
				}
				final ProteinThresholdTypeBean newProteinThresholdTypeBean = new ProteinThresholdTypeBean();
				identificationExcelTypeBean.getProteinThresholds().getProteinThreshold()
						.add(newProteinThresholdTypeBean);
				createNewProteinThresholdPanel(newProteinThresholdTypeBean);

			}
		});
		proteinThresholdsIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// create a new MyExcelSectionFlowPanel and add it to
				// the flowPanelproteinThreshold
				if (!proteinThresholdPanels.isEmpty()) {
					ProteinThresholdPanel proteinThresholdPanel = proteinThresholdPanels
							.get(proteinThresholdPanels.size() - 1);
					identificationExcelTypeBean.getProteinThresholds().getProteinThreshold()
							.remove(proteinThresholdPanel.getRepresentedObject());
					proteinThresholdPanel.removeFromParent();
					if (proteinThresholdPanel.getRelatedExcelSectionFlowPanel() != null) {
						proteinThresholdPanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					proteinThresholdPanels.remove(proteinThresholdPanel);
				}
			}
		});
		grid_7.setWidget(0, 1, proteinThresholdsIncrementableTextBox);
		grid_7.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

		flowPanelProteinScore = new FlowPanel();
		flowPanelProteinScore.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelProteinScore);

		Grid grid_3 = new Grid(1, 2);
		flowPanelProteinScore.add(grid_3);
		grid_3.setWidth("100%");

		Label label2 = new Label("Protein scores:");
		grid_3.setWidget(0, 0, label2);
		proteinScoresIncrementableTextBox = new IncrementableTextBox(0);
		grid_3.setWidget(0, 1, proteinScoresIncrementableTextBox);
		proteinScoresIncrementableTextBox.setWidth("");
		grid_3.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		proteinScoresIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ScoreTypeBean newScoreTypeBean = new ScoreTypeBean();
				identificationExcelTypeBean.getProteinScore().add(newScoreTypeBean);
				createNewProteinScorePanel(newScoreTypeBean);

			}
		});
		proteinScoresIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!proteinScorePanels.isEmpty()) {
					ScorePanel proteinScorePanel = proteinScorePanels.get(proteinScorePanels.size() - 1);
					identificationExcelTypeBean.getProteinScore().remove(proteinScorePanel.getRepresentedObject());
					if (proteinScorePanel.getRelatedExcelSectionFlowPanel() != null) {
						proteinScorePanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					proteinScorePanel.removeFromParent();
					proteinScorePanels.remove(proteinScorePanel);
				}

			}
		});
		FlowPanel flowPanel_1 = new FlowPanel();
		flowPanel_1.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanel_1);

		flowPanelPeptideSequence = new MyExcelSectionFlowPanel(null, flowPanelOptions, getPeptideSequencePanel(),
				scrollPanelTable);
		flowPanelLeft.add(flowPanelPeptideSequence);
		Label lblPeptideSequence = new Label("Peptide sequence");
		flowPanelPeptideSequence.add(lblPeptideSequence);
		flowPanelPSMScore = new FlowPanel();
		flowPanelPSMScore.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelPSMScore);

		Grid grid_4 = new Grid(1, 2);
		flowPanelPSMScore.add(grid_4);
		grid_4.setWidth("100%");

		Label label3 = new Label("Id. scores at PSM-level:");
		grid_4.setWidget(0, 0, label3);
		psmScoresIncrementableTextBox = new IncrementableTextBox(0);
		grid_4.setWidget(0, 1, psmScoresIncrementableTextBox);
		grid_4.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		psmScoresIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ScoreTypeBean newScoreTypeBean = new ScoreTypeBean();
				identificationExcelTypeBean.getPsmScore().add(newScoreTypeBean);
				createNewPSMScorePanel(newScoreTypeBean);

			}
		});
		psmScoresIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!psmScorePanels.isEmpty()) {
					ScorePanel psmScorePanel = psmScorePanels.get(psmScorePanels.size() - 1);
					identificationExcelTypeBean.getPsmScore().remove(psmScorePanel.getRepresentedObject());
					if (psmScorePanel.getRelatedExcelSectionFlowPanel() != null) {
						psmScorePanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					psmScorePanel.removeFromParent();
					psmScorePanels.remove(psmScorePanel);
				}
			}
		});
		flowPanelPTMScore = new FlowPanel();
		flowPanelPTMScore.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelPTMScore);

		Grid grid_5 = new Grid(1, 2);
		flowPanelPTMScore.add(grid_5);
		grid_5.setWidth("100%");

		Label label4 = new Label("PTM score:");
		label4.setText("PTM scores (i.e. localization score):");
		grid_5.setWidget(0, 0, label4);
		ptmScoresIncrementableTextBox = new IncrementableTextBox(0);
		grid_5.setWidget(0, 1, ptmScoresIncrementableTextBox);
		grid_5.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		ptmScoresIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final PtmScoreTypeBean newPtmScoreTypeBean = new PtmScoreTypeBean();
				identificationExcelTypeBean.getPtmScore().add(newPtmScoreTypeBean);
				createNewPTMScorePanel(newPtmScoreTypeBean);

			}
		});
		ptmScoresIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!pTMScorePanels.isEmpty()) {
					PTMScorePanel ptmScorePanel = pTMScorePanels.get(pTMScorePanels.size() - 1);
					identificationExcelTypeBean.getPtmScore().remove(ptmScorePanel.getRepresentedObject());
					ptmScorePanel.removeFromParent();
					if (ptmScorePanel.getRelatedExcelSectionFlowPanel() != null) {
						ptmScorePanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					pTMScorePanels.remove(ptmScorePanel);
				}
			}
		});
		// NEW FOR PEPTIDES

		flowPanelPeptideScore = new FlowPanel();
		flowPanelPeptideScore.setStyleName("IdentificationInfoFromExcelEmptyPanel");
		flowPanelLeft.add(flowPanelPeptideScore);

		Grid grid_6 = new Grid(1, 2);
		flowPanelPeptideScore.add(grid_6);
		grid_6.setWidth("100%");

		Label label5 = new Label("Id. scores at Peptide-level:");
		grid_6.setWidget(0, 0, label5);
		peptideScoresIncrementableTextBox = new IncrementableTextBox(0);
		grid_6.setWidget(0, 1, peptideScoresIncrementableTextBox);
		grid_6.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		peptideScoresIncrementableTextBox.addPlusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final ScoreTypeBean newScoreTypeBean = new ScoreTypeBean();
				identificationExcelTypeBean.getPeptideScore().add(newScoreTypeBean);
				createNewPeptideScorePanel(newScoreTypeBean);

			}
		});
		peptideScoresIncrementableTextBox.addMinusButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!peptideScorePanels.isEmpty()) {
					ScorePanel peptideScorePanel = peptideScorePanels.get(peptideScorePanels.size() - 1);
					identificationExcelTypeBean.getPeptideScore().remove(peptideScorePanel.getRepresentedObject());
					if (peptideScorePanel.getRelatedExcelSectionFlowPanel() != null) {
						peptideScorePanel.getRelatedExcelSectionFlowPanel().removeFromParent();
					}
					peptideScorePanel.removeFromParent();
					peptideScorePanels.remove(peptideScorePanel);
				}
			}
		});
		// NEW FOR PEPTIDES

		grid_1.setWidget(0, 1, flowPanelCenter);

		flowPanel_2 = new FlowPanel();
		flowPanel_2.setStyleName("IdentificationInfoFromExcelEmptyPanel-color");
		flowPanelCenter.add(flowPanel_2);
		flowPanel_2.setWidth("");

		lblCustomizableOptionsFor = new Label("Customizable options for the selected information:");
		flowPanel_2.add(lblCustomizableOptionsFor);

		flowPanelCenter.add(flowPanelOptions);
		flowPanelOptions.setSize("", "450px");
		grid_1.setWidget(0, 2, righFlowPanel);
		righFlowPanel.setWidth("305px");
		grid_1.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_LEFT);
		grid_1.getCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		grid_1.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();
		updateExcelIdentificationTypeObjectFromGUIOptions();

		// register as a listener of msruns
		ProjectCreatorRegister.registerAsListenerByObjectClass(MsRunTypeBean.class, this);
		updateGUIReferringToDataObjects();
	}

	private void createNewPTMScorePanel(PtmScoreTypeBean newPtmScoreTypeBean) {
		PTMScorePanel ptmScorePanel = new PTMScorePanel(sessionID, excelFileBean,
				"PTM score " + (pTMScorePanels.size() + 1), newPtmScoreTypeBean);
		ptmScorePanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		pTMScorePanels.add(ptmScorePanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("PTM score " + pTMScorePanels.size(),
				flowPanelOptions, ptmScorePanel, scrollPanelTable);
		flowPanelPTMScore.add(panel);

	}

	private void createNewProteinScorePanel(ScoreTypeBean newScoreTypeBean) {
		ScorePanel proteinScorePanel = new ScorePanel(sessionID, excelFileBean,
				"Protein score " + (proteinScorePanels.size() + 1), newScoreTypeBean);
		proteinScorePanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		proteinScorePanels.add(proteinScorePanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("Protein score " + proteinScorePanels.size(),
				flowPanelOptions, proteinScorePanel, scrollPanelTable);
		flowPanelProteinScore.add(panel);

	}

	private void createNewProteinAnnotationPanel(ProteinAnnotationTypeBean newProteinAnnotationTypeBean) {
		ProteinAnnotationPanel proteinAnnotationPanel = new ProteinAnnotationPanel(excelFileBean,
				newProteinAnnotationTypeBean);
		proteinAnnotationPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		proteinAnnotationPanels.add(proteinAnnotationPanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel(
				"Protein annotation " + proteinAnnotationPanels.size(), flowPanelOptions, proteinAnnotationPanel,
				scrollPanelTable);
		flowPanelProteinAnnotation.add(panel);

	}

	private void createNewProteinThresholdPanel(ProteinThresholdTypeBean newProteinThresholdTypeBean) {
		ProteinThresholdPanel proteinThresholdPanel = new ProteinThresholdPanel(excelFileBean,
				newProteinThresholdTypeBean);
		proteinThresholdPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		proteinThresholdPanels.add(proteinThresholdPanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel(
				"Protein threshold " + proteinThresholdPanels.size(), flowPanelOptions, proteinThresholdPanel,
				scrollPanelTable);
		flowPanelProteinThresholds.add(panel);

	}

	private void createNewPSMScorePanel(ScoreTypeBean newScoreTypeBean) {
		ScorePanel psmScorePanel = new ScorePanel(sessionID, excelFileBean, "PSM score " + (psmScorePanels.size() + 1),
				newScoreTypeBean);
		psmScorePanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		psmScorePanels.add(psmScorePanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("PSM score " + psmScorePanels.size(),
				flowPanelOptions, psmScorePanel, scrollPanelTable);
		flowPanelPSMScore.add(panel);

	}

	private void createNewPeptideScorePanel(ScoreTypeBean newScoreTypeBean) {
		ScorePanel peptideScorePanel = new ScorePanel(sessionID, excelFileBean,
				"Peptide score " + (peptideScorePanels.size() + 1), newScoreTypeBean);
		peptideScorePanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		peptideScorePanels.add(peptideScorePanel);
		MyExcelSectionFlowPanel panel = new MyExcelSectionFlowPanel("Peptide score " + peptideScorePanels.size(),
				flowPanelOptions, peptideScorePanel, scrollPanelTable);
		flowPanelPeptideScore.add(panel);

	}

	/**
	 * Calls to getExcelFileBean using the web service
	 *
	 * @param waitUntilGettingExcelFileBean
	 */
	private void getExcelFileBean() {
		FileNameWithTypeBean found = null;
		for (FileNameWithTypeBean fileNameWithTypeBean : SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap
				.keySet()) {
			if (fileNameWithTypeBean.equals(excelFileWithFormatBean)) {
				found = fileNameWithTypeBean;
				break;
			}
		}
		if (found != null) {
			setExcelFileBean(SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap.get(found));
		} else {
			serviceAsync.getExcelFileBean(sessionID, importJobID, excelFileWithFormatBean,
					new AsyncCallback<FileTypeBean>() {

						@Override
						public void onFailure(Throwable caught) {
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(FileTypeBean result) {
							setExcelFileBean(result);
							SharedDataUtil.excelBeansByExcelFileWithFormatBeansMap.put(excelFileWithFormatBean,
									result);
						}
					});

		}

	}

	private void updateExcelIdentificationTypeObjectFromGUIOptions() {

		// protein accession
		getProteinAccessionPanel().updateRepresentedObject();

		// protein description
		getProteinDescriptionPanel().updateRepresentedObject();
		if (getProteinDescriptionPanel().getExcelColumnRefPanel().getColumnRef() == null) {
			identificationExcelTypeBean.setProteinDescription(null);
		} else {
			identificationExcelTypeBean.setProteinDescription(getProteinDescriptionPanel().getRepresentedObject());
		}

		// protein annotations
		for (int index = 0; index < proteinAnnotationPanels.size(); index++) {
			ProteinAnnotationPanel proteinAnnotationPanel = proteinAnnotationPanels.get(index);
			proteinAnnotationPanel.updateRepresentedObject();
		}

		// protein thresholds
		for (int index = 0; index < proteinThresholdPanels.size(); index++) {
			ProteinThresholdPanel proteinThresholdPanel = proteinThresholdPanels.get(index);
			proteinThresholdPanel.updateRepresentedObject();
		}

		// protein scores
		for (ScorePanel proteinScorePanel : proteinScorePanels) {
			proteinScorePanel.updateRepresentedObject();
		}

		// peptide sequence
		getPeptideSequencePanel().updateRepresentedObject();
		if (getPeptideSequencePanel().getExcelColumnRefPanel().getColumnRef() == null) {
			identificationExcelTypeBean.setSequence(null);
		} else {
			identificationExcelTypeBean.setSequence(getPeptideSequencePanel().getRepresentedObject());
		}

		// psm scores
		for (ScorePanel psmScorePanel : psmScorePanels) {
			psmScorePanel.updateRepresentedObject();
		}
		// ptm scores
		for (PTMScorePanel pTMScorePanel : pTMScorePanels) {
			pTMScorePanel.updateRepresentedObject();
		}

		// peptide scores
		for (ScorePanel peptideScorePanel : peptideScorePanels) {
			peptideScorePanel.updateRepresentedObject();
		}
		// ms run ref
		if (msRunComboListBox.getSelectedIndex() != -1) {
			identificationExcelTypeBean
					.setMsRunRef(ProjectCreatorWizardUtil.getSelectedItemTextsFromListBox(msRunComboListBox, ","));
		}

		// discard decoys
		if (discardDecoysCheckBox.getValue() && decoyRegexpSuggestBox.checkRegularExpression()) {
			identificationExcelTypeBean.setDiscardDecoys(decoyRegexpSuggestBox.getCurrentRegularExpressionText());
		}
	}

	private void addHandlers() {
		// if discard decoy hits checkbox is enabled, enable regular exp textbox
		discardDecoysCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				decoyRegexpSuggestBox.setEnabled(event.getValue());
			}
		});
		// if check button is clicked, then start the check
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetData();
				startCheck();
			}
		});

		// // add panels hover handlers
		// addHoverHandlers();

	}

	private PeptideSequencePanel getPeptideSequencePanel() {
		if (peptideSequencePanel == null) {
			final SequenceTypeBean newPeptideSequenceTypeBean = new SequenceTypeBean();
			identificationExcelTypeBean.setSequence(newPeptideSequenceTypeBean);
			peptideSequencePanel = new PeptideSequencePanel(excelFileBean, newPeptideSequenceTypeBean);
			peptideSequencePanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		}
		return peptideSequencePanel;
	}

	/**
	 * Sets the object {@link ExcelFileBean} and sets that object to all the panels
	 * that depends on it
	 *
	 * @param excelFileBean
	 */
	private void setExcelFileBean(FileTypeBean excelFileBean) {
		this.excelFileBean = excelFileBean;
		getProteinAccessionPanel().setExcelFileBean(excelFileBean);
		getProteinDescriptionPanel().setExcelFileBean(excelFileBean);
		for (ProteinAnnotationPanel proteinAnnotationPanel : proteinAnnotationPanels) {
			proteinAnnotationPanel.setExcelFileBean(excelFileBean);
		}
		for (ScorePanel proteinScorePanel : proteinScorePanels) {
			proteinScorePanel.setExcelFileBean(excelFileBean);
		}

		for (ScorePanel psmScorePanel : psmScorePanels) {
			psmScorePanel.setExcelFileBean(excelFileBean);
		}
		getPeptideSequencePanel().setExcelFileBean(excelFileBean);
		for (ScorePanel peptideScorePanel : peptideScorePanels) {
			peptideScorePanel.setExcelFileBean(excelFileBean);
		}
		for (PTMScorePanel pTMScorePanel : pTMScorePanels) {
			pTMScorePanel.setExcelFileBean(excelFileBean);
		}
	}

	private ProteinAccessionPanel getProteinAccessionPanel() {
		if (proteinAccessionPanel == null) {
			identificationExcelTypeBean.setProteinAccession(new ProteinAccessionTypeBean());
			proteinAccessionPanel = new ProteinAccessionPanel(excelFileBean,
					identificationExcelTypeBean.getProteinAccession());
			proteinAccessionPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		}
		return proteinAccessionPanel;
	}

	private ProteinDescriptionPanel getProteinDescriptionPanel() {
		if (proteinDescriptionPanel == null) {
			final ProteinDescriptionTypeBean newProteinDescriptionTypeBean = new ProteinDescriptionTypeBean();
			identificationExcelTypeBean.setProteinDescription(newProteinDescriptionTypeBean);
			proteinDescriptionPanel = new ProteinDescriptionPanel(excelFileBean, newProteinDescriptionTypeBean);
			proteinDescriptionPanel.addExcelColumnsChangeHandler(getStartCheckChangeHandler());
		}
		return proteinDescriptionPanel;
	}

	private ChangeHandler getStartCheckChangeHandler() {
		ChangeHandler ret = new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				startCheck();
			}
		};
		return ret;
	}

	protected void startCheck() {
		// disable start check button
		numLoadingProcesses = 0;
		// if regular expression is a valid regular expression
		if (discardDecoysCheckBox.getValue()) {
			if (decoyRegexpSuggestBox.checkRegularExpression()) {
				retrieveData();
			}
		} else {
			retrieveData();
		}

	}

	/**
	 * Returns a list of String representing the accessions of the proteins of the
	 * data source.<br>
	 * If the list is not loaded, ask for it to the server in a synchonous way
	 *
	 * @return
	 */
	private void retrieveData() {

		loadProteinAccessionRandomData(proteinAccessionPanel);
		loadProteinDescriptionRandomData(proteinDescriptionPanel);
		for (int index = 0; index < proteinScorePanels.size(); index++) {
			loadProteinScoreRandomData(index, proteinScorePanels.get(index));
		}
		for (int index = 0; index < proteinAnnotationPanels.size(); index++) {
			loadProteinAnnotationsRandomData(index, proteinAnnotationPanels.get(index));
		}
		for (int index = 0; index < proteinThresholdPanels.size(); index++) {
			loadProteinThreholdRandomData(index, proteinThresholdPanels.get(index));
		}
		for (int index = 0; index < psmScorePanels.size(); index++) {
			loadPsmScoreRandomData(index, psmScorePanels.get(index));
		}
		for (int index = 0; index < pTMScorePanels.size(); index++) {
			loadPtmScoreRandomData(index, pTMScorePanels.get(index));
		}
		for (int index = 0; index < peptideScorePanels.size(); index++) {
			loadPeptideScoreRandomData(index, peptideScorePanels.get(index));
		}
		loadPeptideSequenceRandomData(peptideSequencePanel);
	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link SequenceTypeBean} object created from the GUI
	 *
	 * @param peptideSequencePanel
	 *
	 */
	public void loadPeptideSequenceRandomData(PeptideSequencePanel peptideSequencePanel) {
		final MyExcelColumnCellTable<Pair<String, SequenceTypeBean>> table = peptideSequencePanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = peptideSequencePanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, SequenceTypeBean> pair = new Pair<String, SequenceTypeBean>(value,
										identificationExcelTypeBean.getSequence());
								table.addData(pair);
							}
						}
					});
		} else {
		}
	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index                  : index of the {@link ScoreTypeBean} of protein
	 *                               scores
	 * @param proteinAnnotationPanel
	 *
	 */
	private void loadProteinAnnotationsRandomData(final int index, ProteinAnnotationPanel proteinAnnotationPanel) {
		final MyExcelColumnCellTable<Pair<String, ProteinAnnotationTypeBean>> table = proteinAnnotationPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinAnnotationPanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								final ProteinAnnotationTypeBean proteinAnnotationType = identificationExcelTypeBean
										.getProteinAnnotations().getProteinAnnotation().get(index);
								Pair<String, ProteinAnnotationTypeBean> pair = new Pair<String, ProteinAnnotationTypeBean>(
										value, proteinAnnotationType);
								table.addData(pair);
							}
						}
					});
		} else {
			proteinAnnotationPanel.updateRepresentedObject();
			table.getDataProvider().refresh();

		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index                 : index of the {@link ScoreTypeBean} of protein
	 *                              scores
	 * @param proteinThresholdPanel
	 *
	 */
	private void loadProteinThreholdRandomData(final int index, ProteinThresholdPanel proteinThresholdPanel) {
		final MyExcelColumnCellTable<Pair<String, ProteinThresholdTypeBean>> table = proteinThresholdPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinThresholdPanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								final ProteinThresholdTypeBean proteinThresholdType = identificationExcelTypeBean
										.getProteinThresholds().getProteinThreshold().get(index);
								Pair<String, ProteinThresholdTypeBean> pair = new Pair<String, ProteinThresholdTypeBean>(
										value, proteinThresholdType);
								table.addData(pair);
							}
						}
					});
		} else {
			proteinThresholdPanel.updateRepresentedObject();
			table.getDataProvider().refresh();
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index             : index of the {@link ScoreTypeBean} of protein
	 *                          scores
	 * @param proteinScorePanel
	 *
	 */
	private void loadProteinScoreRandomData(final int index, ScorePanel proteinScorePanel) {
		final MyExcelColumnCellTable<Pair<String, ScoreTypeBean>> table = proteinScorePanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinScorePanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, ScoreTypeBean> pair = new Pair<String, ScoreTypeBean>(value,
										identificationExcelTypeBean.getProteinScore().get(index));
								table.addData(pair);
							}
						}
					});
		} else {
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index         : index of the {@link ScoreTypeBean} of psm scores
	 * @param psmScorePanel
	 *
	 */
	private void loadPsmScoreRandomData(final int index, ScorePanel psmScorePanel) {
		final MyExcelColumnCellTable<Pair<String, ScoreTypeBean>> table = psmScorePanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = psmScorePanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, ScoreTypeBean> pair = new Pair<String, ScoreTypeBean>(value,
										identificationExcelTypeBean.getPsmScore().get(index));
								table.addData(pair);
							}
						}
					});
		} else {
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link PtmScoreTypeBean} object created from the GUI
	 *
	 * @param index         : index of the {@link PtmScoreTypeBean} of ptm scores
	 * @param ptmScorePanel
	 *
	 */
	private void loadPtmScoreRandomData(final int index, PTMScorePanel ptmScorePanel) {
		final MyExcelColumnCellTable<Pair<String, PtmScoreTypeBean>> table = ptmScorePanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = ptmScorePanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, PtmScoreTypeBean> pair = new Pair<String, PtmScoreTypeBean>(value,
										identificationExcelTypeBean.getPtmScore().get(index));
								table.addData(pair);
							}
						}
					});
		} else {
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ScoreTypeBean} object created from the GUI
	 *
	 * @param index             : index of the {@link ScoreTypeBean} of peptide
	 *                          scores
	 * @param peptideScorePanel
	 *
	 */
	private void loadPeptideScoreRandomData(final int index, ScorePanel peptideScorePanel) {
		final MyExcelColumnCellTable<Pair<String, ScoreTypeBean>> table = peptideScorePanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = peptideScorePanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, ScoreTypeBean> pair = new Pair<String, ScoreTypeBean>(value,
										identificationExcelTypeBean.getPeptideScore().get(index));
								table.addData(pair);
							}
						}
					});
		} else {
		}

	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ProteinAccessionTypeBean} object created from the GUI
	 *
	 * @param proteinAccessionPanel
	 *
	 */
	public void loadProteinAccessionRandomData(ProteinAccessionPanel proteinAccessionPanel) {
		final MyExcelColumnCellTable<Pair<String, ProteinAccessionTypeBean>> table = proteinAccessionPanel.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinAccessionPanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							decreaseLoadingProcesses();
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();

							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							for (String value : result) {
								Pair<String, ProteinAccessionTypeBean> pair = new Pair<String, ProteinAccessionTypeBean>(
										value, identificationExcelTypeBean.getProteinAccession());
								table.addData(pair);
							}
						}
					});
		} else {

			updateExcelIdentificationTypeObjectFromGUIOptions();
			table.redraw();
		}
	}

	/**
	 * Calls to getRandomValues in the server to load the data using the
	 * {@link ProteinDescriptionTypeBean} object created from the GUI
	 *
	 * @param proteinDescriptionPanel
	 *
	 */
	public void loadProteinDescriptionRandomData(ProteinDescriptionPanel proteinDescriptionPanel) {
		final MyExcelColumnCellTable<Pair<String, ProteinDescriptionTypeBean>> table = proteinDescriptionPanel
				.getTable();
		ExcelColumnRefPanel excelColumnRefPanel = proteinDescriptionPanel.getExcelColumnRefPanel();
		if (table.isEmpty() && excelColumnRefPanel.getSheetRef() != null
				&& excelColumnRefPanel.getColumnRef() != null) {
			ExcelDataReference excelDataReference = getExcelDataReference(excelFileWithFormatBean, excelColumnRefPanel);
			increaseLoadingProcesses();
			serviceAsync.getRandomValues(sessionID, importJobID, excelDataReference, numTestCases,
					new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							decreaseLoadingProcesses();
							StatusReportersRegister.getInstance().notifyStatusReporters(caught);
						}

						@Override
						public void onSuccess(List<String> result) {
							decreaseLoadingProcesses();
							table.clearData();
							updateExcelIdentificationTypeObjectFromGUIOptions();
							final ProteinDescriptionTypeBean proteinDescription = identificationExcelTypeBean
									.getProteinDescription();
							if (proteinDescription == null)
								System.out.println("ProteinDescriptionTypeBean cannot be null!");
							for (String value : result) {
								Pair<String, ProteinDescriptionTypeBean> pair = new Pair<String, ProteinDescriptionTypeBean>(
										value, proteinDescription);
								table.addData(pair);
							}
						}
					});
		} else {
			table.redraw();
		}
	}

	/**
	 * Creates an {@link ExcelDataReference} containing information from
	 * {@link FileNameWithTypeBean} and {@link ExcelColumnRefPanel}
	 *
	 * @param excelFileWithFormatBean2
	 * @param excelColumnRefPanel
	 * @return
	 */
	private ExcelDataReference getExcelDataReference(FileNameWithTypeBean excelFileWithFormatBean2,
			ExcelColumnRefPanel excelColumnRefPanel) {
		ExcelDataReference excelDataReference = new ExcelDataReference();
		excelDataReference.setColumnId(excelColumnRefPanel.getColumnRef());
		excelDataReference.setExcelFileNameWithTypeBean(excelFileWithFormatBean);
		excelDataReference.setSheetId(excelColumnRefPanel.getSheetRef());
		return excelDataReference;
	}

	/**
	 * Sets the number of cases that is going to test. In this case, protein
	 * accessions.
	 *
	 * @param numTestCases the numTestCases to set
	 */
	public void setNumTestCases(int numTestCases) {
		this.numTestCases = numTestCases;
	}

	/**
	 * @param remoteFileBean the remoteFileWithType to set
	 */
	public boolean setFileNameWithTypeBean(FileNameWithTypeBean remoteFileBean) {
		if ((excelFileWithFormatBean != null && !excelFileWithFormatBean.equals(remoteFileBean))
				|| excelFileWithFormatBean == null && remoteFileBean != null) {
			excelFileWithFormatBean = remoteFileBean;
			getExcelFileBean();

			return true;
		}
		return false;
	}

	public IdentificationExcelTypeBean getExcelIdentificationInfoBean() {
		updateExcelIdentificationTypeObjectFromGUIOptions();

		// if (getProteinAccessionPanel().isValid())
		return identificationExcelTypeBean;
		// return null;
	}

	@Override
	public void updateGUIReferringToDataObjects() {
		ProjectCreatorWizardUtil.updateCombo(msRunComboListBox, MsRunTypeBean.class, false);

	}

	@Override
	public void setImportJobID(int importJobID) {
		this.importJobID = importJobID;

	}

	public void resetData() {
		proteinAccessionPanel.resetData();
		proteinDescriptionPanel.resetData();
		for (int index = 0; index < proteinScorePanels.size(); index++) {
			proteinScorePanels.get(index).resetData();
		}
		for (int index = 0; index < proteinAnnotationPanels.size(); index++) {
			proteinAnnotationPanels.get(index).resetData();
		}
		peptideSequencePanel.resetData();
	}

	@Override
	public void unregisterAsListener() {
		ProjectCreatorRegister.deleteListener(this);
	}

	@Override
	public IdentificationExcelTypeBean getObject() {
		return identificationExcelTypeBean;
	}

	@Override
	public void updateRepresentedObject() {
		updateExcelIdentificationTypeObjectFromGUIOptions();
	}

	@Override
	public void updateGUIFromObjectData(IdentificationExcelTypeBean dataObject) {
		identificationExcelTypeBean = dataObject;
		updateGUIFromObjectData();
	}

	@Override
	public void updateGUIFromObjectData() {

		// select MS run
		if (identificationExcelTypeBean.getMsRunRef() != null
				&& !"".equals(identificationExcelTypeBean.getMsRunRef())) {
			ProjectCreatorWizardUtil.selectInCombo(msRunComboListBox, identificationExcelTypeBean.getMsRunRef(),
					SharedConstants.MULTIPLE_ITEM_SEPARATOR);
		}
		// protein acc
		if (identificationExcelTypeBean.getProteinAccession() != null) {
			getProteinAccessionPanel().updateGUIFromObjectData(identificationExcelTypeBean.getProteinAccession());
		}
		// protein description
		if (identificationExcelTypeBean.getProteinDescription() != null) {
			getProteinDescriptionPanel().updateGUIFromObjectData(identificationExcelTypeBean.getProteinDescription());
		}
		// protein annotations
		if (identificationExcelTypeBean.getProteinAnnotations() != null) {
			// remove all previous protein annotation panels
			for (ProteinAnnotationPanel proteinAnnotationPanel : proteinAnnotationPanels) {
				proteinAnnotationPanel.removeFromParent();
			}
			proteinAnnotationPanels.clear();
			proteinAnnotationsIncrementableTextBox.setValue(0);
			if (identificationExcelTypeBean.getProteinAnnotations().getProteinAnnotation() != null) {
				for (ProteinAnnotationTypeBean proteinAnnotationTypeBean : identificationExcelTypeBean
						.getProteinAnnotations().getProteinAnnotation()) {
					createNewProteinAnnotationPanel(proteinAnnotationTypeBean);
					proteinAnnotationsIncrementableTextBox
							.setValue(proteinAnnotationsIncrementableTextBox.getIntegerNumber() + 1);
				}
			}
		}
		// protein thresholds
		if (identificationExcelTypeBean.getProteinThresholds() != null) {
			// remove all previous protein threshold panels
			for (ProteinThresholdPanel proteinThresholdPanel : proteinThresholdPanels) {
				proteinThresholdPanel.removeFromParent();
			}
			proteinThresholdPanels.clear();
			proteinThresholdsIncrementableTextBox.setValue(0);
			if (identificationExcelTypeBean.getProteinThresholds().getProteinThreshold() != null) {
				for (ProteinThresholdTypeBean proteinThresholdTypeBean : identificationExcelTypeBean
						.getProteinThresholds().getProteinThreshold()) {
					createNewProteinThresholdPanel(proteinThresholdTypeBean);
					proteinThresholdsIncrementableTextBox
							.setValue(proteinThresholdsIncrementableTextBox.getIntegerNumber() + 1);
				}
			}
		}
		// protein scores
		if (identificationExcelTypeBean.getProteinScore() != null) {
			// remove all previous protein score panels
			for (ScorePanel proteinScorePanel : proteinScorePanels) {
				proteinScorePanel.removeFromParent();
			}
			proteinScorePanels.clear();
			proteinScoresIncrementableTextBox.setValue(0);
			for (ScoreTypeBean scoreType : identificationExcelTypeBean.getProteinScore()) {
				createNewProteinScorePanel(scoreType);
				proteinScoresIncrementableTextBox.setValue(proteinScoresIncrementableTextBox.getIntegerNumber() + 1);
			}
		}

		// psm scores
		if (identificationExcelTypeBean.getPsmScore() != null) {
			// remove all previous psm score panels.
			for (ScorePanel peptideScorePanel : psmScorePanels) {
				peptideScorePanel.removeFromParent();
			}
			psmScorePanels.clear();
			psmScoresIncrementableTextBox.setValue(0);
			for (ScoreTypeBean scoreTypeBean : identificationExcelTypeBean.getPsmScore()) {
				createNewPSMScorePanel(scoreTypeBean);
				psmScoresIncrementableTextBox.setValue(psmScoresIncrementableTextBox.getIntegerNumber() + 1);
			}
		}
		// ptm scores
		if (identificationExcelTypeBean.getPtmScore() != null) {
			// remove all previous ptm score panels.
			for (PTMScorePanel ptmScorePanel : pTMScorePanels) {
				ptmScorePanel.removeFromParent();
			}
			pTMScorePanels.clear();
			ptmScoresIncrementableTextBox.setValue(0);
			for (PtmScoreTypeBean ptmScoreTypeBean : identificationExcelTypeBean.getPtmScore()) {
				createNewPTMScorePanel(ptmScoreTypeBean);
				ptmScoresIncrementableTextBox.setValue(ptmScoresIncrementableTextBox.getIntegerNumber() + 1);
			}
		}
		// peptide sequence
		if (identificationExcelTypeBean.getSequence() != null) {
			getPeptideSequencePanel().updateGUIFromObjectData(identificationExcelTypeBean.getSequence());
		}
		// peptide scores
		if (identificationExcelTypeBean.getPeptideScore() != null) {
			// remove all previous peptide score panels.
			for (ScorePanel peptideScorePanel : peptideScorePanels) {
				peptideScorePanel.removeFromParent();
			}
			peptideScorePanels.clear();
			peptideScoresIncrementableTextBox.setValue(0);
			for (ScoreTypeBean scoreTypeBean : identificationExcelTypeBean.getPeptideScore()) {
				createNewPeptideScorePanel(scoreTypeBean);
				peptideScoresIncrementableTextBox.setValue(peptideScoresIncrementableTextBox.getIntegerNumber() + 1);
			}
		}

		// discard decoy
		if (identificationExcelTypeBean.getDiscardDecoys() != null
				&& !"".equals(identificationExcelTypeBean.getDiscardDecoys())) {
			discardDecoysCheckBox.setValue(true);
			decoyRegexpSuggestBox.setText(identificationExcelTypeBean.getDiscardDecoys());

		} else {
			discardDecoysCheckBox.setValue(false);
			decoyRegexpSuggestBox.setText("");
		}
		decoyRegexpSuggestBox.checkRegularExpression();
	}
}
