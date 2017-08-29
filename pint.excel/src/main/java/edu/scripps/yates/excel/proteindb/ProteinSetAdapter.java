package edu.scripps.yates.excel.proteindb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.cv.CommonlyUsedCV;
import edu.scripps.yates.excel.proteindb.agent.ProteinProviderFromExcel;
import edu.scripps.yates.excel.proteindb.enums.ConditionName;
import edu.scripps.yates.excel.util.ColumnIndexManager;
import edu.scripps.yates.excel.util.ColumnIndexManager.ColumnName;
import edu.scripps.yates.excel.util.NewRunsMapReaderFromTxt;
import edu.scripps.yates.persistence.memory.DataManagerMemory;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ConditionEx;
import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.model.factories.SampleEx;
import edu.scripps.yates.utilities.model.factories.TissueEx;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinSetAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Set<Protein>> {
	private static final Logger log = Logger.getLogger(ProteinSetAdapter.class);
	public static final ColumnName[] expression_analysis_TMT__126_label = {
			ColumnName.expression_analysis_TMT__126_label_HBE, ColumnName.expression_analysis_TMT__127_label_HBE,
			ColumnName.expression_analysis_TMT__128_label_HBE };
	public static final ColumnName[] expression_analysis_TMT__126_label2 = {
			ColumnName.expression_analysis_TMT__129_label_CFBE, ColumnName.expression_analysis_TMT__130_label_CFBE,
			ColumnName.expression_analysis_TMT__131_label_CFBE };
	public static final ColumnName[] wt_columns = { ColumnName.wt_022810, ColumnName.wt_040910, ColumnName.wt_072408,
			ColumnName.wt_110207, ColumnName.wt_1107, ColumnName.wt_112409, ColumnName.wt_HOAB };
	public static final ColumnName[] mut_columns = { ColumnName.mut_102609, ColumnName.mut_121108,
			ColumnName._0h_092809, ColumnName._0h_110109, ColumnName._0h_091109, ColumnName.mut_OAB, ColumnName.mut_S45,
			ColumnName.mut_S46, ColumnName.mut_S47, ColumnName.mut_S48 };
	public static final ColumnName[] background_columns = { ColumnName.mock_022308, ColumnName.mock_022408,
			ColumnName.mock_061910, ColumnName.silent_021408, ColumnName.silent_072208 };
	public static final ColumnName[] tsa_columns = { ColumnName.TSA_042010, ColumnName.TSA_070708,
			ColumnName.TSA_071908, ColumnName.TSA_Cinhib, ColumnName.TSA_Hinhib };
	public static final ColumnName[] saha_columns = { ColumnName.SAHA1, ColumnName.SAHA2, ColumnName.SAHA3,
			ColumnName.SAHA4 };
	public static final ColumnName[] dmso_column = { ColumnName.DMSO_031010, ColumnName.DMSO_050908,
			ColumnName.DMSO_051508, ColumnName.DMSO_100209 };
	public static final ColumnName[] siHDAC7_columns = { ColumnName.siHDAC7_011909, ColumnName.siHDAC7_030809,
			ColumnName.siHDAC7_031909 };
	public static final ColumnName[] corr4a_columns = { ColumnName.Corr4A_032510, ColumnName.Corr4A_021210,
			ColumnName.Corr4A_032610 };
	public static final ColumnName[] _1h_columns = { ColumnName._1h_092909, ColumnName._1h_111709,
			ColumnName._1h_112909, ColumnName._1h_120909 };
	public static final ColumnName[] _6h_columns = { ColumnName._6h_091109, ColumnName._6h_101209,
			ColumnName._6h_101709, ColumnName._6h_10209 };
	public static final ColumnName[] _24h_columns = { ColumnName._24h_050610, ColumnName._24h_050710 };
	public static final ColumnName[] _24hrev_columns = { ColumnName._24hrev_051010, ColumnName._24hrev_053010 };
	private static String QUANTIFICATION_PVALUE = CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID);

	/**
	 * List of rows by sheet number
	 */
	private final TIntObjectHashMap<List<Row>> rows = new TIntObjectHashMap<List<Row>>();
	private final ColumnIndexManager columnManager;
	private final static DataManagerMemory dataManager = ProteinProviderFromExcel.datamanager;
	public static Organism humanOrganism;
	public static Tissue tissue;
	public static Project generalProject;
	private final List<MSRun> msRuns = new ArrayList<MSRun>();
	private final Set<Condition> conditions = new THashSet<Condition>();
	private final MSRun tmtMSRun = new MSRunEx("TMT analysis run", "not provided");

	static {
		if (generalProject == null) {
			generalProject = new ProjectEx(
					"Remodeling of a ∆F508 CFTR-mutation specific interactome promotes rescue of Cystic Fibrosis",
					"The dataset contains the experimental data for the wt and ∆F508 CFTR interactome in lung epithelial cells and ∆F508 CFTR interactomes upon temperature shift to 28˚C (time-series) or inhibition of HDAC activity by TSA, SAHA or HDAC7 siRNA as well as control datasets. Comparison of the HBE and CFBE41o- proteome with the CFTR interactome is also provided.");
			((ProjectEx) generalProject).setTag("CFTR");
		}
		if (tissue == null)
			tissue = getTissue("human tissue ID", "Human tissue");
		if (humanOrganism == null)
			humanOrganism = getOrganism("9606", "Human");

		if (QUANTIFICATION_PVALUE == null)
			QUANTIFICATION_PVALUE = CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID);
	}

	public ProteinSetAdapter(Row row, int sheetNumber, ColumnIndexManager colManager) {
		addRow(row, sheetNumber);

		if (colManager == null)
			throw new IllegalArgumentException("ColumnIndexManager is null");
		columnManager = colManager;
	}

	private Row getRow(int numSheet, int numRow) {
		final List<Row> list = rows.get(numSheet);
		if (list != null && list.size() >= numRow + 1)
			return list.get(numRow);
		return null;
	}

	public void addRow(Row row, int sheetNumber) {
		if (rows.containsKey(sheetNumber)) {
			rows.get(sheetNumber).add(row);
		} else {
			List<Row> list = new ArrayList<Row>();
			list.add(row);
			rows.put(sheetNumber, list);
		}
	}

	public static Sample getSample(String name, boolean wt, boolean labeled, Organism organism, Tissue tissue) {

		List<Sample> samples = dataManager.getSamplesByName(name);
		if (samples != null) {
			return samples.get(0);
		} else {
			SampleEx sample2 = new SampleEx(name);
			sample2.setLabel(null);
			sample2.setOrganism(organism);
			sample2.setWildType(wt);
			sample2.setTissue(tissue);
			dataManager.saveSample(sample2);
			return sample2;
		}

	}

	private static Organism getOrganism(String ncbiTaxID, String name) {
		Organism organism = dataManager.getOrganismByID(ncbiTaxID);
		if (organism != null) {
			return organism;
		} else {
			OrganismEx organism2 = new OrganismEx(name);
			organism2.setName(name);
			dataManager.saveOrganism(organism2);
			return organism2;
		}
	}

	private static Tissue getTissue(String tissueID, String name) {
		Tissue tissue = dataManager.getTissueByID(tissueID);
		if (tissue != null) {
			return tissue;
		} else {
			TissueEx tissue2 = new TissueEx(tissueID);
			tissue2.setName(name);
			dataManager.saveTissue(tissue2);
			return tissue2;
		}
	}

	// private Set<ProteinAmount> getMockIPsAmounts() {
	// Set<ProteinAmount> amounts = new THashSet<ProteinAmount>();
	//
	// // 1h 30C Condition
	// ExperimentalCondition mockIPs_Condition = getExperimentalCondition(
	// "Mock IPs", "Mock IPs");
	//
	// // Protein amounts for each 4 replicates:
	// // Set<ProteinAmount> replicatesAmounts = new THashSet<ProteinAmount>();
	// // ColumnName[] columnNames = { ColumnName.DMSO_031010,
	// // ColumnName.DMSO_050908, ColumnName.DMSO_051508 };
	// // replicatesAmounts = getProteinAmounts(columnNames,
	// // hDAC7siRNACondition,
	// // experiment);
	// // if (!replicatesAmounts.isEmpty()) {
	// // amounts.addAll(replicatesAmounts);
	//
	// final Set<ProteinAmount> proteinAmounts = getProteinAmounts(
	// new ColumnName[] {}, mockIPs_Condition);
	// if (proteinAmounts != null)
	// amounts.addAll(proteinAmounts);
	// // }
	// return amounts;
	// }

	// private Set<ProteinAmount> getNullIPsAmounts() {
	// Set<ProteinAmount> amounts = new THashSet<ProteinAmount>();
	//
	// // 1h 30C Condition
	// ExperimentalCondition nullIPs_Condition = getExperimentalCondition(
	// "Null IPs", "Null IPs");
	//
	// // Protein amounts for each 4 replicates:
	// // Set<ProteinAmount> replicatesAmounts = new THashSet<ProteinAmount>();
	// // ColumnName[] columnNames = { ColumnName.DMSO_031010,
	// // ColumnName.DMSO_050908, ColumnName.DMSO_051508 };
	// // replicatesAmounts = getProteinAmounts(columnNames,
	// // hDAC7siRNACondition,
	// // experiment, AmountType.SPC);
	// // if (!replicatesAmounts.isEmpty()) {
	// // amounts.addAll(replicatesAmounts);
	//
	// final Set<ProteinAmount> proteinAmounts = getProteinAmounts(
	// new ColumnName[] {}, nullIPs_Condition);
	// if (proteinAmounts != null)
	// amounts.addAll(proteinAmounts);
	// // }
	// return amounts;
	// }

	public static ConditionEx getExperimentalCondition(ConditionName experimentalConditionName, String description,
			Sample sample) {
		List<Condition> expConditions = dataManager.getExperimentalConditionsByName(experimentalConditionName.name());
		if (expConditions != null) {
			final ConditionEx experimentalCondition = (ConditionEx) expConditions.get(0);
			return experimentalCondition;
		} else {
			ConditionEx expCondition2 = new ConditionEx(experimentalConditionName.name(), sample, generalProject);
			((ProjectEx) generalProject).addExperimentalCondition(expCondition2);
			expCondition2.setDescription(description);
			dataManager.saveExperimentalCondition(expCondition2);
			return expCondition2;
		}
	}

	public void addDMSOSumAmount(double spc) {

	}

	private Double getDoubleValue(Cell cell) {
		if (cell == null)
			return null;
		try {

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return Double.valueOf(cell.getStringCellValue());
			case Cell.CELL_TYPE_NUMERIC:

				return cell.getNumericCellValue();

			default:
				return null;
			}

		} catch (NumberFormatException e) {

		}
		return null;
	}

	private Cell getCell(ColumnName columnName, int rowIndex) {
		Cell cell = getRow(columnManager.getSheetIndex(columnName), rowIndex)
				.getCell(columnManager.getColumnIndex(columnName));
		return cell;
	}

	@Override
	public Set<Protein> adapt() {
		Set<Protein> ret = new THashSet<Protein>();
		// iterate over all the possible replicates, and look if there are some
		// amount for the protein. If yes, create the protein

		Set<Protein> proteins = getProteinsFromColumnNames(ProteinImpl2._1h_columns, ConditionName._1H_30C,
				ColumnName._1H_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2._24h_columns, ConditionName._24H_30C, ColumnName._24h_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2._24hrev_columns, ConditionName._24h_30C_14h_37C,
				ColumnName._24H_rev_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2._6h_columns, ConditionName._6H_30C, ColumnName._6h_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.background_columns, ConditionName.BACKGROUND, null);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.corr4a_columns, ConditionName.CORR4A, ColumnName._4A_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.dmso_column, ConditionName.DMSO, null);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.mut_columns, ConditionName.MUT, ColumnName.MUT_global);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.saha_columns, ConditionName.SAHA, ColumnName.SAHA_glob);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.siHDAC7_columns, ConditionName.HDACsi,
				ColumnName.HDAC7si_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.tsa_columns, ConditionName.TSA, ColumnName.TSA_SUM);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.wt_columns, ConditionName.WT, ColumnName.WT_global);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.expression_analysis_TMT__126_label, ConditionName.WT, null,
				AmountType.NORMALIZED_INTENSITY);
		if (!proteins.isEmpty())
			ret.addAll(proteins);

		proteins = getProteinsFromColumnNames(ProteinImpl2.expression_analysis_TMT__126_label2, ConditionName.MUT, null,
				AmountType.NORMALIZED_INTENSITY);
		if (!proteins.isEmpty())
			ret.addAll(proteins);
		return ret;
	}

	private Amount getTotalAmount(ColumnName columnName, ConditionName condition) {
		Double spc = getDoubleValue(getCell(columnName, 0));
		if (spc != null && spc != 0.0) {
			Sample sample = getSample(condition.name(), false, false, humanOrganism, tissue);
			final Amount proteinAmount = new AmountEx(spc, AmountType.SPC, CombinationType.SUM,
					getExperimentalCondition(condition, condition.name(), sample));
			return proteinAmount;
		}
		return null;
	}

	private Set<Protein> getProteinsFromColumnNames(ColumnName[] columnNames, ConditionName condition,
			ColumnName totalColumnName) {
		return getProteinsFromColumnNames(columnNames, condition, totalColumnName, AmountType.SPC);
	}

	private Set<Protein> getProteinsFromColumnNames(ColumnName[] columnNames, ConditionName condition,
			ColumnName totalColumnName, AmountType amountType) {
		Set<Protein> ret = new THashSet<Protein>();
		for (ColumnName columnName : columnNames) {
			final ProteinImpl2 protein = getProteinFromReplicate(condition, columnName, amountType);
			if (protein != null) {
				if (totalColumnName != null) {
					final Amount totalAmount = getTotalAmount(totalColumnName, condition);
					if (totalAmount != null)
						protein.addAmount(totalAmount);
				}

				ret.add(protein);
			} else {

			}
		}
		return ret;
	}

	private ProteinImpl2 getProteinFromReplicate(ConditionName condition, ColumnName columnName,
			AmountType amountType) {
		Sample sample = getSample(condition.name(), false, false, humanOrganism, tissue);

		final ConditionEx experimentalCondition = getExperimentalCondition(condition, condition.name(), sample);
		final Amount proteinAmount = getProteinAmount(columnName, experimentalCondition, amountType);

		if (proteinAmount != null) {
			MSRun msRun = dataManager.getMSRun(columnName.name(),
					NewRunsMapReaderFromTxt.getPathByReplicateName(columnName.name()));
			if (msRun == null) {
				msRun = tmtMSRun;
			}
			ProteinImpl2 protein = new ProteinImpl2(columnName, rows, columnManager, msRun);
			protein.addAmount(proteinAmount);
			if (protein.getAccession().equals("P52907")) {
				log.info("asdasdf");
			}
			experimentalCondition.addProtein(protein);
			return protein;
		}
		return null;
	}

	private Amount getProteinAmount(ColumnName columnName, ConditionEx condition, AmountType amountType) {
		String replicateName = columnName.name();
		final MSRun msRun = dataManager.getMSRun(

				replicateName, NewRunsMapReaderFromTxt.getPathByReplicateName(replicateName));
		if (msRun != null)
			msRuns.add(msRun);

		Double spc = getDoubleValue(getCell(columnName, 0));
		if (spc != null && spc != 0.0) {
			// EXCEPTIONS> SPECIAL CASES

			// wt_1107, in this case the SPC should be
			// multiplied by 5
			if (columnName.equals(ColumnName.wt_1107))
				spc *= 5;

			// ignore mut_S46 && mut_S48 (they were removed from the
			// calculation)
			if (columnName.equals(ColumnName.mut_S46) || columnName.equals(ColumnName.mut_S48)) {
				spc = 0.0;
			}
			final AmountEx proteinAmountEx = new AmountEx(spc, amountType, condition);
			conditions.add(condition);
			((ProjectEx) generalProject).addExperimentalCondition(condition);

			return proteinAmountEx;
		}

		return null;
	}
}
