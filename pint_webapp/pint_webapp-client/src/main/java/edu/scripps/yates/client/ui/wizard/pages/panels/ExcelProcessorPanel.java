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
	private final String sheetName;

	public ExcelProcessorPanel(PintContext context, FileTypeBean file, String excelSheet) {
		this.context = context;
		this.file = file;
		this.sheetName = excelSheet;
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

				showProteinsPanel(event.getValue(), true);

				enableProteinsScores(event.getValue(), true);
				enableProteinRatios(event.getValue(), true);
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
				showProteinScoresPanel(event.getValue(), true);

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

				showProteinRatiosPanel(event.getValue(), true);

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
				showPeptidesPanel(event.getValue(), true);
				// you need a protein too in the same row
				if (event.getValue()) {
					checkBoxProteins.setValue(true, true);
					enableProteinRatios(true, false);
					enableProteinsScores(true, false);
				}
				enablePeptidesScores(event.getValue(), true);
				enablePeptidesRatios(event.getValue(), true);
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

				showPeptideScoresPanel(event.getValue(), true);

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

				showPeptideRatiosPanel(event.getValue(), true);

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

				showPSMsPanel(event.getValue(), true);
				// you need a protein too in the same row
				if (event.getValue()) {
					checkBoxProteins.setValue(true, true);
					showProteinsPanel(true, false);
					enableProteinRatios(true, false);
					enableProteinsScores(true, false);

					checkBoxPeptides.setValue(true, true);
					showPeptidesPanel(true, false);
					enablePeptidesRatios(true, false);
					enablePeptidesScores(true, false);
				}
				enablePSMsScores(event.getValue(), true);
				enablePSMsRatios(event.getValue(), true);
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

				showPSMScoresPanel(event.getValue(), true);

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
				showPSMRatiosPanel(event.getValue(), true);

			}
		});
		// update checkboxes from context
		updateGUIFromContext();
	}

	private void updateGUIFromContext() {

		final Set<IdentificationExcelTypeBean> excelIDAssociatedWithThisFile = PintImportCfgUtil
				.getExcelIDAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), sheetName);
		final Set<QuantificationExcelTypeBean> excelQuantAssociatedWithThisFile = PintImportCfgUtil
				.getExcelQuantAssociatedWithThisFile(context.getPintImportConfiguration(), file.getId(), sheetName);

		if (!excelIDAssociatedWithThisFile.isEmpty()) {
			excelID = excelIDAssociatedWithThisFile.iterator().next();
			// assign this excel id to the other conditions that is associted to, in order
			// to maintain just one object
			final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
					.getConditionsAssociatedWithExcelFile(context.getPintImportConfiguration(), file.getId(),
							sheetName);
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
							sheetName);
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
		final List<ExcelAmountRatioTypeBean> psmRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, true, false, false);
		final List<ExcelAmountRatioTypeBean> peptideRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, false, true, false);
		final List<ExcelAmountRatioTypeBean> proteinRatios = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, false, false, true);

		if (excelID != null) {
			// PSMs
			checkBoxPSMs.setValue(
					excelID.getPsmId() != null || !excelID.getPsmScore().isEmpty() || !psmRatios.isEmpty(), false);
			showPSMsPanel(checkBoxPSMs.getValue(), false);
			checkBoxPSMScores.setValue(!excelID.getPsmScore().isEmpty(), false);
			enablePSMsScores(checkBoxPSMs.getValue(), false);
			checkBoxPSMRatios.setValue(!psmRatios.isEmpty(), false);
			enablePSMsRatios(checkBoxPSMs.getValue(), false);

			// PEPTIDES
			checkBoxPeptides.setValue(excelID.getSequence() != null || !excelID.getPeptideScore().isEmpty()
					|| !peptideRatios.isEmpty() || checkBoxPSMs.getValue(), false);
			showPeptidesPanel(checkBoxPeptides.getValue(), false);
			checkBoxPeptideScores.setValue(!excelID.getPeptideScore().isEmpty(), false);
			enablePeptidesScores(checkBoxPeptides.getValue(), false);
			checkBoxPeptideRatios.setValue(!peptideRatios.isEmpty(), false);
			enablePeptidesRatios(checkBoxPeptides.getValue(), false);

			// PROTEINS
			checkBoxProteins.setValue(
					excelID.getProteinAccession() != null || !excelID.getProteinScore().isEmpty()
							|| !proteinRatios.isEmpty() || checkBoxPeptides.getValue() || checkBoxPSMs.getValue(),
					false);
			showProteinsPanel(checkBoxProteins.getValue(), false);
			checkBoxProteinScores.setValue(!excelID.getProteinScore().isEmpty(), false);
			enableProteinsScores(checkBoxProteins.getValue(), false);
			checkBoxProteinRatios.setValue(!proteinRatios.isEmpty(), false);
			enableProteinRatios(checkBoxProteins.getValue(), false);

			// TODO protein scores and psm scores
		} else {
			checkBoxPeptides.setValue(false, false);
			enablePeptidesRatios(false, false);
			enablePeptidesScores(false, false);
			checkBoxProteins.setValue(false, false);
			enableProteinRatios(false, false);
			enableProteinsScores(false, false);
			checkBoxPSMs.setValue(false, false);
			enablePSMsRatios(false, false);
			enablePSMsScores(false, false);
		}
		if (excelQuant != null) {
			// TODO
		}
		// ratios
		enablePSMsRatios(checkBoxPSMs.getValue() || !psmRatios.isEmpty(), false);
		checkBoxPSMRatios.setValue(!psmRatios.isEmpty());
		showPSMRatiosPanel(checkBoxPSMRatios.getValue(), false);
		//
		enablePeptidesRatios(checkBoxPeptides.getValue() || !peptideRatios.isEmpty(), false);
		checkBoxPeptideRatios.setValue(!peptideRatios.isEmpty());
		showPeptideRatiosPanel(checkBoxPeptideRatios.getValue(), false);
		//
		enableProteinRatios(checkBoxProteins.getValue() || !proteinRatios.isEmpty(), false);
		checkBoxProteinRatios.setValue(!proteinRatios.isEmpty());
		showProteinRatiosPanel(checkBoxProteinRatios.getValue(), false);

	}

	protected void showPSMRatiosPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(psmRatiosValuesRow, 0, visible);
		if (visible) {
			if (psmRatiosPanel == null) {
				psmRatiosPanel = createPSMRatiosPanel();
			} else {
				final boolean added = PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(),
						psmRatiosPanel.getObject());
				GWT.log("ratio added: " + added);
			}
			rightPanel.setWidget(psmRatiosValuesRow, 0, psmRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				// remove ratios
				PintImportCfgUtil.removePSMRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
						file.getId(), sheetName);
				psmRatiosPanel = null;
			}
		}
	}

	protected void showPSMScoresPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(psmScoreValuesRow, 0, visible);
		if (visible) {
			if (psmScoresPanel == null) {
				psmScoresPanel = createPSMScoresPanel();
			}
			rightPanel.setWidget(psmScoreValuesRow, 0, psmScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().getPsmScore().clear();
				psmScoresPanel = null;
			}
		}
	}

	protected void showPSMsPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(psmIDValuesRow, 0, visible);
		if (visible) {
			if (psmIDPanel == null) {
				psmIDPanel = createPSMIdPanel();
			}
			rightPanel.setWidget(psmIDValuesRow, 0, psmIDPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(psmIDValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().setPsmId(null);
				psmIDPanel = null;
			}
		}
	}

	private PSMIdPanel createPSMIdPanel() {
		if (getIDExcel().getPsmId() == null) {
			final PsmTypeBean psmID = new PsmTypeBean();
			getIDExcel().setPsmId(psmID);
			// add it to the ratios at PSM level
			final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							sheetName, true, false, false);
			for (final ExcelAmountRatioTypeBean ratio : excelAmounts) {
				ratio.setPsmId(psmID);
			}
		}
		final PSMIdPanel panel = new PSMIdPanel(context, file, sheetName, getIDExcel().getPsmId());
		return panel;
	}

	protected void showPeptideRatiosPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(peptideRatiosValuesRow, 0, visible);
		if (visible) {
			if (peptideRatiosPanel == null) {
				peptideRatiosPanel = createPeptideRatiosPanel();
			}
			rightPanel.setWidget(peptideRatiosValuesRow, 0, peptideRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				// remove ratios
				PintImportCfgUtil.removePeptideRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
						file.getId(), sheetName);
				peptideRatiosPanel = null;
			}
		}
	}

	private RatioPanel createPeptideRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, false, true, false);
		ExcelAmountRatioTypeBean peptideRatio = null;
		if (excelAmounts.isEmpty()) {
			peptideRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addPeptideRatioFromExcel(context.getPintImportConfiguration(), peptideRatio);
			// add the peptide sequences if available
			if (excelID.getSequence() != null) {
				peptideRatio.setPeptideSequence(excelID.getSequence());
			}
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			peptideRatio = excelAmounts.get(0);
			// TODO to allow more than one ratio per excel file so, this function will
			// return more than one ratioPanel

		}
		final RatioPanel panel = new RatioPanel("Peptide", context, file, sheetName, peptideRatio);
		return panel;
	}

	private RatioPanel createPSMRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, true, false, false);
		ExcelAmountRatioTypeBean psmRatio = null;
		if (excelAmounts.isEmpty()) {
			psmRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(), psmRatio);
			// add the psm id if available
			if (excelID.getPsmId() != null) {
				psmRatio.setPsmId(excelID.getPsmId());
			}
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			psmRatio = excelAmounts.get(0);
			// TODO to allow more than one ratio per excel file so, this function will
			// return more than one ratioPanel
		}
		final RatioPanel panel = new RatioPanel("PSM", context, file, sheetName, psmRatio);
		return panel;
	}

	private RatioPanel createProteinRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
						sheetName, false, false, true);
		ExcelAmountRatioTypeBean proteinRatio = null;
		if (excelAmounts.isEmpty()) {
			proteinRatio = new ExcelAmountRatioTypeBean();
			PintImportCfgUtil.addProteinRatioFromExcel(context.getPintImportConfiguration(), proteinRatio);
			// add the protein accession if available
			if (excelID.getProteinAccession() != null) {
				proteinRatio.setProteinAccession(excelID.getProteinAccession());
			}
		} else {
			GWT.log(excelAmounts.size() + " excel ratios");
			proteinRatio = excelAmounts.get(0);
			// TODO to allow more than one ratio per excel file so, this function will
			// return more than one ratioPanel

		}
		final RatioPanel panel = new RatioPanel("Protein", context, file, sheetName, proteinRatio);
		return panel;
	}

	protected void showPeptideScoresPanel(Boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(peptideScoreValuesRow, 0, visible);
		if (visible) {
			if (peptideScoresPanel == null) {
				peptideScoresPanel = createPeptideScoresPanel();
			}
			rightPanel.setWidget(peptideScoreValuesRow, 0, peptideScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().getPeptideScore().clear();
				peptideScoresPanel = null;
			}
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
		final ScoresPanel panel = new ScoresPanel("Peptide", context, file, sheetName, getIDExcel().getPeptideScore());
		return panel;
	}

	private ScoresPanel createProteinScoresPanel() {

		if (getIDExcel().getProteinScore().isEmpty()) {
			final ScoreTypeBean proteinScore = new ScoreTypeBean();
			getIDExcel().getProteinScore().add(proteinScore);
		}
		final ScoresPanel panel = new ScoresPanel("Protein", context, file, sheetName, getIDExcel().getProteinScore());
		return panel;
	}

	private ScoresPanel createPSMScoresPanel() {

		if (getIDExcel().getPsmScore().isEmpty()) {
			final ScoreTypeBean psmScore = new ScoreTypeBean();
			getIDExcel().getPsmScore().add(psmScore);
		}
		final ScoresPanel panel = new ScoresPanel("PSM", context, file, sheetName, getIDExcel().getPsmScore());
		return panel;
	}

	protected void showPeptidesPanel(boolean visible, boolean removeIfNotShown) {
		if (peptideSequencePanel == null) {
			peptideSequencePanel = createPeptideSequencePanel();
		}
		rightPanel.getFlexCellFormatter().setVisible(peptideSequenceValuesRow, 0, visible);
		if (visible) {
			rightPanel.setWidget(peptideSequenceValuesRow, 0, peptideSequencePanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(peptideSequenceValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().setSequence(null);
				peptideSequencePanel = null;
			}
		}

	}

	private PeptideSequencePanel createPeptideSequencePanel() {

		if (getIDExcel().getSequence() == null) {
			final SequenceTypeBean peptideSequence = new SequenceTypeBean();
			getIDExcel().setSequence(peptideSequence);
			// add it to the ratios at peptide level
			final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							sheetName, false, true, false);
			for (final ExcelAmountRatioTypeBean ratio : excelAmounts) {
				ratio.setPeptideSequence(peptideSequence);
			}
		}
		final PeptideSequencePanel panel = new PeptideSequencePanel(context, file, sheetName,
				getIDExcel().getSequence());

		return panel;
	}

	protected void showProteinRatiosPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(proteinRatiosValuesRow, 0, visible);
		if (visible) {
			if (proteinRatiosPanel == null) {
				proteinRatiosPanel = createProteinRatiosPanel();
			}
			rightPanel.setWidget(proteinRatiosValuesRow, 0, proteinRatiosPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinRatiosValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				// remove ratios
				PintImportCfgUtil.removeProteinRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
						file.getId(), sheetName);
				proteinRatiosPanel = null;
			}
		}

	}

	protected void showProteinScoresPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(proteinScoreValuesRow, 0, visible);
		if (visible) {
			if (proteinScoresPanel == null) {
				proteinScoresPanel = createProteinScoresPanel();
			}
			rightPanel.setWidget(proteinScoreValuesRow, 0, proteinScoresPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinScoreValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().getProteinScore().clear();
				proteinScoresPanel = null;
			}
		}
	}

	protected void showProteinsPanel(boolean visible, boolean removeIfNotShown) {

		rightPanel.getFlexCellFormatter().setVisible(proteinAccValuesRow, 0, visible);
		if (visible) {
			if (proteinAccessionPanel == null) {
				proteinAccessionPanel = createProteinAccessionPanel();
			}
			rightPanel.setWidget(proteinAccValuesRow, 0, proteinAccessionPanel);
			rightPanel.getFlexCellFormatter().setHorizontalAlignment(proteinAccValuesRow, 0,
					HasHorizontalAlignment.ALIGN_LEFT);
		} else {
			if (removeIfNotShown) {
				getIDExcel().setProteinAccession(null);
				getIDExcel().setProteinAnnotations(null);
				getIDExcel().setProteinDescription(null);
				getIDExcel().setProteinThresholds(null);
				proteinAccessionPanel = null;
			}
		}
	}

	private ProteinAccessionPanel createProteinAccessionPanel() {
		if (getIDExcel().getProteinAccession() == null) {
			final ProteinAccessionTypeBean proteinAccession = new ProteinAccessionTypeBean();
			proteinAccession.setRegexp(".*");
			getIDExcel().setProteinAccession(proteinAccession);
			// add it to the ratios at protein level
			final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
					.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId(),
							sheetName, false, false, true);
			for (final ExcelAmountRatioTypeBean ratio : excelAmounts) {
				ratio.setProteinAccession(proteinAccession);
			}
		}
		final ProteinAccessionPanel panel = new ProteinAccessionPanel(context, file, sheetName,
				getIDExcel().getProteinAccession());
		return panel;
	}

	protected void enablePeptidesRatios(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPeptideRatios, 0, enabled);
		showPeptideRatiosPanel(checkBoxPeptideRatios.getValue() && enabled, removeIfNotShown);
	}

	protected void enablePeptidesScores(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPeptideScores, 0, enabled);
		showPeptideScoresPanel(checkBoxPeptideScores.getValue() && enabled, removeIfNotShown);
	}

	protected void enableProteinRatios(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForProteinRatios, 0, enabled);
		showProteinRatiosPanel(checkBoxProteinRatios.getValue() && enabled, removeIfNotShown);
	}

	protected void enableProteinsScores(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForProteinScores, 0, enabled);
		showProteinScoresPanel(checkBoxProteinScores.getValue() && enabled, removeIfNotShown);
	}

	protected void enablePSMsRatios(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPSMRatios, 0, enabled);
		showPSMRatiosPanel(checkBoxPSMRatios.getValue() && enabled, removeIfNotShown);
	}

	protected void enablePSMsScores(boolean enabled, boolean removeIfNotShown) {
		leftPanel.getFlexCellFormatter().setVisible(rowForPSMScores, 0, enabled);
		showPSMScoresPanel(checkBoxPSMScores.getValue() && enabled, removeIfNotShown);
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
		if (this.psmRatiosPanel != null && this.checkBoxPSMRatios.getValue()) {
			PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(), psmRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removePSMRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId(), sheetName);
		}
		if (this.peptideRatiosPanel != null && this.checkBoxPeptideRatios.getValue()) {
			PintImportCfgUtil.addPeptideRatioFromExcel(context.getPintImportConfiguration(),
					peptideRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removePeptideRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId(), sheetName);
		}
		if (this.proteinRatiosPanel != null && this.checkBoxProteinRatios.getValue()) {
			PintImportCfgUtil.addProteinRatioFromExcel(context.getPintImportConfiguration(),
					proteinRatiosPanel.getObject());
		} else {
			PintImportCfgUtil.removeProteinRatioAssociatedWithFileFromExcel(context.getPintImportConfiguration(),
					file.getId(), sheetName);
		}

	}

	public void removeCondition(ExperimentalConditionTypeBean condition) {
		this.conditions.remove(condition);
		updateContextFromGUI();
	}

}
