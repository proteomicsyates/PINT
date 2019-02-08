package edu.scripps.yates.client.ui.wizard.pages.panels;

import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.pages.widgets.excel.PeptideRatiosPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class ExcelProcessorPanel extends FlexTable {

	private final PintContext context;
	private final FileTypeBean file;
	private CheckBox checkBoxProteins;
	private CheckBox checkBoxProteinScores;
	private CheckBox checkBoxProteinRatios;
	private CheckBox checkBoxPeptides;
	private CheckBox checkBoxPeptideScores;
	private CheckBox checkBoxPeptideRatios;
	private SimplePanel rightPanel;
	private FlexTable peptideRatiosPanel;
	private CheckBox checkBoxPSMs;
	private CheckBox checkBoxPSMScores;
	private CheckBox checkBoxPSMRatios;

	public ExcelProcessorPanel(PintContext context, FileTypeBean file) {
		this.context = context;
		this.file = file;
		init();
	}

	private void init() {
		final FlexTable leftPanel = new FlexTable();
		leftPanel.setStyleName(WizardStyles.WizardQuestionPanel);
		leftPanel.setCellPadding(10);
		setWidget(0, 0, leftPanel);
		rightPanel = new SimplePanel();
		setWidget(0, 1, rightPanel);
		// proteins
		int row = 0;
		checkBoxProteins = new CheckBox("Proteins");
		checkBoxProteins.setTitle("A column with protein accessions");
		leftPanel.setWidget(row, 0, checkBoxProteins);
		checkBoxProteins.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showProteinsPanel();
				}
				enableProteinsScores(event.getValue());
				enableProteinRatios(event.getValue());
			}
		});
		// protein scores
		row++;
		checkBoxProteinScores = new CheckBox("Protein Scores");
		checkBoxProteinScores.setTitle("A column with numeric scores associated to the proteins");
		leftPanel.setWidget(row, 0, checkBoxProteinScores);
		checkBoxProteinScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showProteinScoresPanel();
				}
			}
		});
		// protein ratios
		row++;
		checkBoxProteinRatios = new CheckBox("Protein ratios");
		checkBoxProteinRatios.setTitle(
				"A column with numeric quantitative ratios associated to the proteins refering to the relative abundance of the proteins between 2 experimental conditions.");
		leftPanel.setWidget(row, 0, checkBoxProteinRatios);
		checkBoxProteinRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showProteinRatiosPanel();
				}
			}
		});
		// peptides
		row++;
		checkBoxPeptides = new CheckBox("Peptides");
		checkBoxPeptides.setTitle("A column with peptide sequences");
		leftPanel.setWidget(row, 0, checkBoxPeptides);
		checkBoxPeptides.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPeptidesPanel();
				}
				enablePeptidesScores(event.getValue());
				enablePeptidesRatios(event.getValue());
			}
		});
		// peptide scores
		row++;
		checkBoxPeptideScores = new CheckBox("Peptide Scores");
		checkBoxPeptideScores.setTitle("A column with numeric scores associated to the peptides");
		leftPanel.setWidget(row, 0, checkBoxPeptideScores);
		checkBoxPeptideScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPeptideScoresPanel();
				}
			}
		});
		// peptide ratios
		row++;
		checkBoxPeptideRatios = new CheckBox("Peptide ratios");
		checkBoxPeptideRatios.setTitle(
				"A column with numeric quantitative ratios associated to the peptides refering to the relative abundance of the peptides between 2 experimental conditions.");
		leftPanel.setWidget(row, 0, checkBoxPeptideRatios);
		checkBoxPeptideRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPeptideRatiosPanel();
				}
			}
		});
		// PSMs
		row++;
		checkBoxPSMs = new CheckBox("PSMs");
		checkBoxPSMs.setTitle("A column with PSM identifiers and other with peptide sequences");
		leftPanel.setWidget(row, 0, checkBoxPSMs);
		checkBoxPSMs.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPSMsPanel();
				}
				enablePSMsScores(event.getValue());
				enablePSMsRatios(event.getValue());
			}
		});
		// pSM scores
		row++;
		checkBoxPSMScores = new CheckBox("PSM Scores");
		checkBoxPSMScores.setTitle("A column with numeric scores associated to the pSMs");
		leftPanel.setWidget(row, 0, checkBoxPSMScores);
		checkBoxPSMScores.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPSMScoresPanel();
				}
			}
		});
		// pSM ratios
		row++;
		checkBoxPSMRatios = new CheckBox("PSM ratios");
		checkBoxPSMRatios.setTitle(
				"A column with numeric quantitative ratios associated to the pSMs refering to the relative abundance of the pSMs between 2 experimental conditions.");
		leftPanel.setWidget(row, 0, checkBoxPSMRatios);
		checkBoxPSMRatios.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					showPSMRatiosPanel();
				}
			}
		});
	}

	protected void showPSMRatiosPanel() {
		// TODO Auto-generated method stub

	}

	protected void showPSMScoresPanel() {
		// TODO Auto-generated method stub

	}

	protected void enablePSMsRatios(boolean enabled) {
		checkBoxPSMRatios.setEnabled(enabled);
	}

	protected void enablePSMsScores(boolean enabled) {
		checkBoxPSMScores.setEnabled(enabled);
	}

	protected void showPSMsPanel() {
		// TODO Auto-generated method stub
	}

	protected void showPeptideRatiosPanel() {
		if (peptideRatiosPanel == null) {
			peptideRatiosPanel = createPeptideRatiosPanel();
		}
		rightPanel.setWidget(peptideRatiosPanel);
	}

	private FlexTable createPeptideRatiosPanel() {
		final List<ExcelAmountRatioTypeBean> excelAmounts = PintImportCfgUtil
				.getExcelAmountRatioTypeBeansAssociatedWithFile(context.getPintImportConfiguration(), file.getId());
		ExcelAmountRatioTypeBean peptideRatio = null;
		if (excelAmounts.isEmpty()) {
			peptideRatio = new ExcelAmountRatioTypeBean();
		} else {
			peptideRatio = excelAmounts.get(0);
		}
		final PeptideRatiosPanel panel = new PeptideRatiosPanel(context, file, peptideRatio);
		return panel;
	}

	protected void showPeptideScoresPanel() {
		// TODO Auto-generated method stub

	}

	protected void showPeptidesPanel() {
		// TODO Auto-generated method stub

	}

	protected void showProteinRatiosPanel() {
		// TODO Auto-generated method stub

	}

	protected void showProteinScoresPanel() {
		// TODO Auto-generated method stub

	}

	protected void showProteinsPanel() {
		// TODO Auto-generated method stub

	}

	protected void enablePeptidesRatios(boolean enabled) {
		this.checkBoxPeptideRatios.setEnabled(enabled);
	}

	protected void enablePeptidesScores(boolean enabled) {
		this.checkBoxPeptideScores.setEnabled(enabled);
	}

	protected void enableProteinRatios(boolean enabled) {
		this.checkBoxProteinRatios.setEnabled(enabled);
	}

	protected void enableProteinsScores(boolean enabled) {
		this.checkBoxProteinScores.setEnabled(enabled);
	}

}
