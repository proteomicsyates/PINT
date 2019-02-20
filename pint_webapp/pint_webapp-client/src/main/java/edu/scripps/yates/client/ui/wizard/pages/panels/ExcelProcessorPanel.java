package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.PSMIdPanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.PeptideSequencePanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.ProteinAccessionPanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.RatioPanel;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.ScoresPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAccessionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SequenceTypeBean;

public class ExcelProcessorPanel extends FlexTable {

	private final PintContext context;
	private final FileTypeBean file;
	private CheckBox checkBoxProteins;
	private CheckBox checkBoxProteinScores;
	private CheckBox checkBoxProteinRatios;
	private CheckBox checkBoxPeptides;
	private CheckBox checkBoxPeptideScores;
	private CheckBox checkBoxPeptideRatios;
	private FlexTable rightPanel;
	private RatioPanel peptideRatiosPanel;
	private CheckBox checkBoxPSMs;
	private CheckBox checkBoxPSMScores;
	private CheckBox checkBoxPSMRatios;
	private ScoresPanel peptideScoresPanel;
	private PeptideSequencePanel peptideSequencePanel;
	private final Set<ExperimentalConditionTypeBean> conditions = new HashSet<ExperimentalConditionTypeBean>();
	private IdentificationExcelTypeBean excelID;
	private QuantificationExcelTypeBean excelQuant;
	private int rowForProteinScores;
	private int rowForProteinRatios;
	private int rowForPeptideScores;
	private int rowForPeptideRatios;
	private int rowForPSMScores;
	private int rowForPSMRatios;
	private FlexTable leftPanel;
	private final int proteinAccValuesRow = 0;
	private final int proteinScoreValuesRow = 1;
	private final int proteinRatiosValuesRow = 2;
	private final int peptideSequenceValuesRow = 3;
	private final int peptideScoreValuesRow = 4;
	private final int peptideRatiosValuesRow = 5;
	private final int psmIDValuesRow = 6;
	private final int psmScoreValuesRow = 7;
	private final int psmRatiosValuesRow = 8;
	private RatioPanel psmRatiosPanel;
	private RatioPanel proteinRatiosPanel;
	private ScoresPanel psmScoresPanel;
	private ScoresPanel proteinScoresPanel;
	private ProteinAccessionPanel proteinAccessionPanel;
	private PSMIdPanel psmIDPanel;
	private final String excelSheet;

	public ExcelProcessorPanel(PintContext context, FileTypeBean file, String excelSheet) {
		this.context = context;
		this.file = file;
		this.excelSheet = excelSheet;
		init();
	}

	private void init() {
		leftPanel = new FlexTable();
		leftPanel.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		leftPanel.setCellPadding(10);
		leftPanel.getElement().getStyle().setPaddingTop(10, Unit.PX);
		setWidget(0, 0, leftPanel);
		getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		rightPanel = new FlexTable();
		rightPanel.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		rightPanel.getElement().getStyle().setPaddingTop(10, Unit.PX);
		setWidget(0, 1, rightPanel);
		for (int i = 0; i < 9; i++) {
			rightPanel.setWidget(i, 0, new HTML("&nbsp"));
		}

		getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		// proteins
		int row = 0;

		final Label label0 = new Label("Does the excel file contain?");
		label0.setStyleName(WizardStyles.WizardExplanationLabel);
		leftPanel.setWidget(row, 0, label0);
		//
		row++;
		checkBoxProteins = new CheckBox("Proteins");
		checkBoxProteins.setStyleName(WizardStyles.WizardInfoMessage);
		checkBoxProteins.setTitle("A column with protein accessions");
		leftPanel.setWidget(row, 0, checkBoxProteins);
		checkBoxProteins.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showProteinsPanel(event.getValue() || checkBoxPeptides.getValue() || checkBoxPSMs.getValue());

				enableProteinsScores(event.getValue());
				enableProteinRatios(event.getValue());
			}
		});
		// protein scores
		row++;
		checkBoxProteinScores = new CheckBox("Protein Scores");
		checkBoxProteinScores.setStyleName(WizardStyles.WizardInfoMessage);
		checkBoxProteinScores.setTitle("A column with numeric scores associated to the proteins");
		leftPanel.setWidget(row, 0, checkBoxProteinScores);
		rowForProteinScores = row;
		checkBoxProteinScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showProteinScoresPanel(event.getValue());

			}
		});
		// protein ratios
		row++;
		checkBoxProteinRatios = new CheckBox("Protein ratios");
		checkBoxProteinRatios.setStyleName(WizardStyles.WizardInfoMessage);
		checkBoxProteinRatios.setTitle(
				"A column with numeric quantitative ratios associated to the proteins refering to the relative abundance of the proteins between 2 experimental conditions.");
		leftPanel.setWidget(row, 0, checkBoxProteinRatios);
		rowForProteinRatios = row;
		checkBoxProteinRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showProteinRatiosPanel(event.getValue());

			}
		});
		// peptides
		row++;
		checkBoxPeptides = new CheckBox("Peptides");
		checkBoxPeptides.setTitle("A column with peptide sequences");
		checkBoxPeptides.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.setWidget(row, 0, checkBoxPeptides);
		checkBoxPeptides.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showPeptidesPanel(event.getValue());
				// you need a protein too in the same row
				if (event.getValue() && !checkBoxProteins.getValue()) {
					checkBoxProteins.setValue(true, true);
				}
				enablePeptidesScores(event.getValue());
				enablePeptidesRatios(event.getValue());
			}
		});
		// peptide scores
		row++;
		checkBoxPeptideScores = new CheckBox("Peptide Scores");
		checkBoxPeptideScores.setTitle("A column with numeric scores associated to the peptides");
		checkBoxPeptideScores.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.setWidget(row, 0, checkBoxPeptideScores);
		rowForPeptideScores = row;
		checkBoxPeptideScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showPeptideScoresPanel(event.getValue());

			}
		});
		// peptide ratios
		row++;
		checkBoxPeptideRatios = new CheckBox("Peptide ratios");
		checkBoxPeptideRatios.setStyleName(WizardStyles.WizardInfoMessage);
		checkBoxPeptideRatios.setTitle(
				"A column with numeric quantitative ratios associated to the peptides refering to the relative abundance of the peptides between 2 experimental conditions.");
		leftPanel.setWidget(row, 0, checkBoxPeptideRatios);
		rowForPeptideRatios = row;
		checkBoxPeptideRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showPeptideRatiosPanel(event.getValue());

			}
		});
		// PSMs
		row++;
		checkBoxPSMs = new CheckBox("PSMs");
		checkBoxPSMs.setTitle("A column with PSM identifiers and other with peptide sequences");
		checkBoxPSMs.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.setWidget(row, 0, checkBoxPSMs);
		checkBoxPSMs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showPSMsPanel(event.getValue());
				// you need a protein too in the same row
				if (event.getValue() && !checkBoxProteins.getValue()) {
					checkBoxProteins.setValue(true, true);
					checkBoxPeptides.setValue(true, true);
				}
				enablePSMsScores(event.getValue());
				enablePSMsRatios(event.getValue());
			}
		});
		// pSM scores
		row++;
		checkBoxPSMScores = new CheckBox("PSM Scores");
		checkBoxPSMScores.setTitle("A column with numeric scores associated to the pSMs");
		checkBoxPSMScores.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.setWidget(row, 0, checkBoxPSMScores);
		rowForPSMScores = row;
		checkBoxPSMScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				showPSMScoresPanel(event.getValue());

			}
		});
		// pSM ratios
		row++;
		checkBoxPSMRatios = new CheckBox("PSM ratios");
		checkBoxPSMRatios.setTitle(
				"A column with numeric quantitative ratios associated to the pSMs refering to the relative abundance of the pSMs between 2 experimental conditions.");
		checkBoxPSMRatios.setStyleName(WizardStyles.WizardInfoMessage);
		leftPanel.setWidget(row, 0, checkBoxPSMRatios);
		rowForPSMRatios = row;
		checkBoxPSMRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				showPSMRatiosPanel(event.getValue());

			}
		});
		// update checkboxes from context
		updateGUIFromContext();
	}

	private void updateGUIFromContext() {

		final Set<IdentificationExcelTypeBean> excelIDAssociatedWithThisFile = PintImportCfgUtil
				.getExcelIDAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), excelSheet);
		final Set<QuantificationExcelTypeBean> excelQuantAssociatedWithThisFile = PintImportCfgUtil
				.getExcelQuantAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), excelSheet);

		if (!excelIDAssociatedWithThisFile.isEmpty()) {
			excelID = excelIDAssociatedWithThisFile.iterator().next();
			// assign this excel id to the other conditions that is associted to, in order
			// to maintain just one object
			final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
					.getConditionsAssociatedWithExcelFile(context.getPintImportConfiguration(), file.getId(),
							excelSheet);
			for (final ExperimentalConditionTypeBean condition : conditions) {
				final List<IdentificationExcelTypeBean> excelIdentInfos = condition.getIdentificationInfo()
						.getExcelIdentInfo();
				final Iterator<IdentificationExcelTypeBean> iterator = excelIdentInfos.iterator();
				while (iterator.hasNext()) {
					final IdentificationExcelTypeBean excelIdentInfo = iterator.next();
					if (excelIDAssociatedWithThisFile.contains(excelIdentInfo)) {
						iterator.remove();
					}
				}
				condition.getIdentificationInfo().getExcelIdentInfo().add(excelID);
			}
		}
		if (!excelQuantAssociatedWithThisFile.isEmpty()) {
			excelQuant = excelQuantAssociatedWithThisFile.iterator().next();
			// assign this excel id to the other conditions that is associted to, in order
			// to maintain just one object
			final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
					.getConditionsAssociatedWithExcelFile(context.getPintImportConfiguration(), file.getId(),
							excelSheet);
			for (final ExperimentalConditionTypeBean condition : conditions) {
				final List<QuantificationExcelTypeBean> excelQuantInfos = condition.getQuantificationInfo()
						.getExcelQuantInfo();
				final Iterator<QuantificationExcelTypeBean> iterator = excelQuantInfos.iterator();
				while (iterator.hasNext()) {
					final QuantificationExcelTypeBean excelQuantInfo = iterator.next();
					if (excelQuantAssociatedWithThisFile.contains(excelQuantInfo)) {
						iterator.remove();
					}
				}
				condition.getQuantificationInfo().getExcelQuantInfo().add(excelQuant);
			}
		}

		if (excelID != null) {
			// PSMs
			final List<ExcelAmountRatioTypeBean> psmRatios = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							excelSheet, true, false, false);
			checkBoxPSMs.setValue(
					excelID.getPsmId() != null || !excelID.getPsmScore().isEmpty() || !psmRatios.isEmpty(), true);
			showPSMsPanel(checkBoxPSMs.getValue());
			checkBoxPSMScores.setValue(!excelID.getPsmScore().isEmpty(), true);
			enablePSMsScores(checkBoxPSMs.getValue());
			checkBoxPSMRatios.setValue(!psmRatios.isEmpty(), true);
			enablePSMsRatios(checkBoxPSMs.getValue());

			// PEPTIDES
			final List<ExcelAmountRatioTypeBean> peptideRatios = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							excelSheet, false, true, false);
			checkBoxPeptides.setValue(excelID.getSequence() != null || !excelID.getPeptideScore().isEmpty()
					|| !peptideRatios.isEmpty() || checkBoxPSMs.getValue(), true);
			showPeptidesPanel(checkBoxPeptides.getValue());
			checkBoxPeptideScores.setValue(!excelID.getPeptideScore().isEmpty(), true);
			enablePeptidesScores(checkBoxPeptides.getValue());
			checkBoxPeptideRatios.setValue(!peptideRatios.isEmpty(), true);
			enablePeptidesRatios(checkBoxPeptides.getValue());

			// PROTEINS
			final List<ExcelAmountRatioTypeBean> proteinRatios = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							excelSheet, false, false, true);
			checkBoxProteins.setValue(
					excelID.getProteinAccession() != null || !excelID.getProteinScore().isEmpty()
							|| !proteinRatios.isEmpty() || checkBoxPeptides.getValue() || checkBoxPSMs.getValue(),
					true);
			showProteinsPanel(checkBoxProteins.getValue());
			checkBoxProteinScores.setValue(!excelID.getProteinScore().isEmpty(), true);
			enableProteinsScores(checkBoxProteins.getValue());
			checkBoxProteinRatios.setValue(!proteinRatios.isEmpty(), true);
			enableProteinRatios(checkBoxProteins.getValue());

			// TODO protein scores and psm scores
		} else {
			checkBoxPeptides.setValue(false, true);
			enablePeptidesRatios(false);
			enablePeptidesScores(false);
			checkBoxProteins.setValue(false, true);
			enableProteinRatios(false);
			enableProteinsScores(false);
			checkBoxPSMs.setValue(false, true);
			enablePSMsRatios(false);
			enablePSMsScores(false);
		}
		if (excelQuant != null) {
			// TODO
		}
		final List<ExcelAmountRatioTypeBean> psmRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, true, false, false);
		for (final ExcelAmountRatioTypeBean excelRatio : psmRatios) {
//
			enablePSMsRatios(excelRatio.getPsmId() != null);
			checkBoxPSMRatios.setValue(excelRatio.getPsmId() != null);
			showPSMRatiosPanel(excelRatio.getPsmId() != null);
		}
		final List<ExcelAmountRatioTypeBean> peptideRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, false, true, false);
		for (final ExcelAmountRatioTypeBean excelRatio : peptideRatios) {
// 
			enablePeptidesRatios(excelRatio.getPeptideSequence() != null);
			checkBoxPeptideRatios.setValue(excelRatio.getPeptideSequence() != null);
			showPeptideRatiosPanel(excelRatio.getPeptideSequence() != null);
// 
		}
		final List<ExcelAmountRatioTypeBean> proteinRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, false, false, true);
		for (final ExcelAmountRatioTypeBean excelRatio : proteinRatios) {
//
			enableProteinRatios(excelRatio.getProteinAccession() != null);
			checkBoxProteinRatios.setValue(excelRatio.getProteinAccession() != null);
			showProteinRatiosPanel(excelRatio.getProteinAccession() != null);

		}

	}

	protected void showPSMRatiosPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(psmRatiosValuesRow, 0, visible);
		if (visible) {
			if (psmRatiosPanel == null) {
				psmRatiosPanel = createPSMRatiosPanel();
			}
			rightPanel.setWidget(psmRatiosValuesRow, 0, psmRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

		}
	}

	protected void showPSMScoresPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(psmScoreValuesRow, 0, visible);
		if (visible) {
			if (psmScoresPanel == null) {
				psmScoresPanel = createPSMScoresPanel();
			}
			rightPanel.setWidget(psmScoreValuesRow, 0, psmScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().getPsmScore().clear();

		}
	}

	protected void showPSMsPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(psmIDValuesRow, 0, visible);
		if (visible) {
			if (psmIDPanel == null) {
				psmIDPanel = createPSMIdPanel();
			}
			rightPanel.setWidget(psmIDValuesRow, 0, psmIDPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmIDValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().setPsmId(null);

		}
	}

	private PSMIdPanel createPSMIdPanel() {
		if (getIDExcel().getPsmId() == null) {
			final PsmTypeBean psmID = new PsmTypeBean();
			getIDExcel().setPsmId(psmID);
		}
		final PSMIdPanel panel = new PSMIdPanel(context, file, excelSheet, getIDExcel().getPsmId());
		return panel;
	}

	protected void showPeptideRatiosPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(peptideRatiosValuesRow, 0, visible);
		if (visible) {
			if (peptideRatiosPanel == null) {
				peptideRatiosPanel = createPeptideRatiosPanel();
			}
			rightPanel.setWidget(peptideRatiosValuesRow, 0, peptideRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

		}
	}

	private RatioPanel createPeptideRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, false, true, false);
		ExcelAmountRatioTypeBean peptideRatio = null;
		if (excelAmounts.isEmpty()) {
			peptideRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addPeptideRatioFromExcel(context.getPintImportConfiguration(), peptideRatio);
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			peptideRatio = excelAmounts.get(0);

		}
		final RatioPanel panel = new RatioPanel("Peptide", context, file, excelSheet, peptideRatio);
		return panel;
	}

	private RatioPanel createPSMRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, true, false, false);
		ExcelAmountRatioTypeBean psmRatio = null;
		if (excelAmounts.isEmpty()) {
			psmRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(), psmRatio);
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			psmRatio = excelAmounts.get(0);

		}
		final RatioPanel panel = new RatioPanel("PSM", context, file, excelSheet, psmRatio);
		return panel;
	}

	private RatioPanel createProteinRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						excelSheet, false, false, true);
		ExcelAmountRatioTypeBean proteinRatio = null;
		if (excelAmounts.isEmpty()) {
			proteinRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addProteinRatioFromExcel(context.getPintImportConfiguration(), proteinRatio);
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			proteinRatio = excelAmounts.get(0);

		}
		final RatioPanel panel = new RatioPanel("Protein", context, file, excelSheet, proteinRatio);
		return panel;
	}

	protected void showPeptideScoresPanel(Boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(peptideScoreValuesRow, 0, visible);
		if (visible) {
			if (peptideScoresPanel == null) {
				peptideScoresPanel = createPeptideScoresPanel();
			}
			rightPanel.setWidget(peptideScoreValuesRow, 0, peptideScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().getPeptideScore().clear();

		}

	}

	private IdentificationExcelTypeBean getIDExcel() {
		if (excelID == null) {
			excelID = new IdentificationExcelTypeBean();
			updateContextFromGUI();
		}
		return excelID;
	}

	private ScoresPanel createPeptideScoresPanel() {

		if (getIDExcel().getPeptideScore().isEmpty()) {
			final ScoreTypeBean peptideScore = new ScoreTypeBean();
			getIDExcel().getPeptideScore().add(peptideScore);
		}
		final ScoresPanel panel = new ScoresPanel("Peptide", context, file, excelSheet, getIDExcel().getPeptideScore());
		return panel;
	}

	private ScoresPanel createProteinScoresPanel() {

		if (getIDExcel().getProteinScore().isEmpty()) {
			final ScoreTypeBean proteinScore = new ScoreTypeBean();
			getIDExcel().getProteinScore().add(proteinScore);
		}
		final ScoresPanel panel = new ScoresPanel("Protein", context, file, excelSheet, getIDExcel().getProteinScore());
		return panel;
	}

	private ScoresPanel createPSMScoresPanel() {

		if (getIDExcel().getPsmScore().isEmpty()) {
			final ScoreTypeBean psmScore = new ScoreTypeBean();
			getIDExcel().getPsmScore().add(psmScore);
		}
		final ScoresPanel panel = new ScoresPanel("PSM", context, file, excelSheet, getIDExcel().getPsmScore());
		return panel;
	}

	protected void showPeptidesPanel(boolean visible) {
		if (peptideSequencePanel == null) {
			peptideSequencePanel = createPeptideSequencePanel();
		}
		rightPanel.getFlexCellFormatter().setVisible(peptideSequenceValuesRow, 0, visible);
		if (visible) {
			rightPanel.setWidget(peptideSequenceValuesRow, 0, peptideSequencePanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideSequenceValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().setSequence(null);

		}

	}

	private PeptideSequencePanel createPeptideSequencePanel() {

		if (getIDExcel().getSequence() == null) {
			final SequenceTypeBean peptideSequence = new SequenceTypeBean();
			getIDExcel().setSequence(peptideSequence);
		}
		final PeptideSequencePanel panel = new PeptideSequencePanel(context, file, excelSheet,
				getIDExcel().getSequence());
		return panel;
	}

	protected void showProteinRatiosPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(proteinRatiosValuesRow, 0, visible);
		if (visible) {
			if (proteinRatiosPanel == null) {
				proteinRatiosPanel = createProteinRatiosPanel();
			}
			rightPanel.setWidget(proteinRatiosValuesRow, 0, proteinRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

		}

	}

	protected void showProteinScoresPanel(boolean visible) {

		rightPanel.getFlexCellFormatter().setVisible(proteinScoreValuesRow, 0, visible);
		if (visible) {
			if (proteinScoresPanel == null) {
				proteinScoresPanel = createProteinScoresPanel();
			}
			rightPanel.setWidget(proteinScoreValuesRow, 0, proteinScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().getProteinScore().clear();

		}
	}

	protected void showProteinsPanel(boolean visible) {
		if (proteinAccessionPanel == null) {
			proteinAccessionPanel = createProteinAccessionPanel();
		}
		rightPanel.getFlexCellFormatter().setVisible(proteinAccValuesRow, 0, visible);
		if (visible) {
			rightPanel.setWidget(proteinAccValuesRow, 0, proteinAccessionPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinAccValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {

			getIDExcel().setProteinAccession(null);
			getIDExcel().setProteinAnnotations(null);
			getIDExcel().setProteinDescription(null);
			getIDExcel().setProteinThresholds(null);

		}
	}

	private ProteinAccessionPanel createProteinAccessionPanel() {
		if (getIDExcel().getProteinAccession() == null) {
			final ProteinAccessionTypeBean proteinAccession = new ProteinAccessionTypeBean();
			getIDExcel().setProteinAccession(proteinAccession);
		}
		final ProteinAccessionPanel panel = new ProteinAccessionPanel(context, file, excelSheet,
				getIDExcel().getProteinAccession());
		return panel;
	}

	protected void enablePeptidesRatios(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPeptideRatios, 0, enabled);
		showPeptideRatiosPanel(checkBoxPeptideRatios.getValue() && enabled);
	}

	protected void enablePeptidesScores(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPeptideScores, 0, enabled);
		showPeptideScoresPanel(checkBoxPeptideScores.getValue() && enabled);
	}

	protected void enableProteinRatios(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForProteinRatios, 0, enabled);
		showProteinRatiosPanel(checkBoxProteinRatios.getValue() && enabled);
	}

	protected void enableProteinsScores(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForProteinScores, 0, enabled);
		showProteinScoresPanel(checkBoxProteinScores.getValue() && enabled);
	}

	protected void enablePSMsRatios(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPSMRatios, 0, enabled);
		showPSMRatiosPanel(checkBoxPSMRatios.getValue() && enabled);
	}

	protected void enablePSMsScores(boolean enabled) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPSMScores, 0, enabled);
		showPSMScoresPanel(checkBoxPSMScores.getValue() && enabled);
	}

	public void addCondition(ExperimentalConditionTypeBean condition) {
		this.conditions.add(condition);
		updateContextFromGUI();
	}

	private void updateContextFromGUI() {
		for (final ExperimentalConditionTypeBean condition : conditions) {
			if (excelID != null) {

				PintImportCfgUtil.addExcelIdentificationToCondition(context.getPintImportConfiguration(),
						condition.getId(), excelID);

			}
			if (excelQuant != null) {
				PintImportCfgUtil.addExcelQuantificationToCondition(context.getPintImportConfiguration(),
						condition.getId(), excelQuant);

			}
		}
		if (this.psmRatiosPanel != null && this.checkBoxPSMRatios.getValue() && this.psmRatiosPanel.isReady()) {
			PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(), psmRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removePSMRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId());
		}
		if (this.peptideRatiosPanel != null && this.checkBoxPeptideRatios.getValue()
				&& this.peptideRatiosPanel.isReady()) {
			PintImportCfgUtil.addPeptideRatioFromExcel(context.getPintImportConfiguration(),
					peptideRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removePeptideRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId());
		}
		if (this.proteinRatiosPanel != null && this.checkBoxProteinRatios.getValue()
				&& this.proteinRatiosPanel.isReady()) {
			PintImportCfgUtil.addProteinRatioFromExcel(context.getPintImportConfiguration(),
					proteinRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removeProteinRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId());
		}

	}

	public void removeCondition(ExperimentalConditionTypeBean condition) {
		this.conditions.remove(condition);
		updateContextFromGUI();
	}

}
