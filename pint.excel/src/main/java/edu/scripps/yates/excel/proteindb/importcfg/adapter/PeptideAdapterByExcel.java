package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;

public class PeptideAdapterByExcel implements Adapter<Peptide> {
	// private final static Map<String, Map<String, PeptideEx>>
	// peptideMapByMSRunID = new HashMap<String, Map<String, PeptideEx>>();
	// private final static Map<String, Set<Ratio>> ratioMap = new
	// HashMap<String, Set<Ratio>>();

	private final int rowIndex;
	private final IdentificationExcelType excelCfg;
	private final ExcelFileReader excelFileReader;
	private final MsRunType msRun;
	// public static final Map<Integer, Set<Peptide>> peptideByRowIndex = new
	// HashMap<Integer, Set<Peptide>>();
	private final Condition condition;

	public PeptideAdapterByExcel(int rowIndex, IdentificationExcelType excelCfg, ExcelFileReader excelFileReader,
			MsRunType msRun, Condition condition) {
		this.rowIndex = rowIndex;
		this.excelCfg = excelCfg;
		this.excelFileReader = excelFileReader;
		this.msRun = msRun;
		this.condition = condition;
	}

	@Override
	public Peptide adapt() {
		try {
			final SequenceType peptideSequenceCfg = excelCfg.getSequence();

			final ExcelColumn peptideSequenceColumn = excelFileReader
					.getExcelColumnFromReference(peptideSequenceCfg.getColumnRef());

			final String rawPeptideSequence = peptideSequenceColumn.getValues().get(rowIndex).toString();

			String cleanPeptideSequence = FastaParser.cleanSequence(rawPeptideSequence);
			//
			// Map<String, PeptideEx> peptideMap = null;
			// if (peptideMapByMSRunID.containsKey(msRun.getId())) {
			// peptideMap = peptideMapByMSRunID.get(msRun.getId());
			// } else {
			// peptideMap = new HashMap<String, PeptideEx>();
			// peptideMapByMSRunID.put(msRun.getId(), peptideMap);
			// }

			Peptide peptide = null;
			if (StaticProteomicsModelStorage.containsPeptide(msRun.getId(), null, -1, cleanPeptideSequence)) {
				peptide = StaticProteomicsModelStorage.getPeptide(msRun.getId(), null, -1, cleanPeptideSequence).iterator().next();
			} else {
				peptide = new PeptideEx(cleanPeptideSequence, new MSRunAdapter(msRun).adapt());

				// peptideMap.put(cleanPeptideSequence, peptide);
			}
			peptide.addCondition(condition);
			// addPeptideByRowIndex(rowIndex, peptide);
			StaticProteomicsModelStorage.addPeptide(peptide, msRun.getId(), condition.getName(), rowIndex);
			// ratios
			// DISABLED SINCE THE RATIOS ARE ASSIGNED TO THE PSMs ON
			// ImportCfgFileReader
			// Set<Ratio> ratios = getPSMRatios(psmId, rowIndex);
			// for (Ratio ratio : ratios) {
			// psm.addRatio(ratio);
			// }
			// amounts
			// DISABLED SINCE AMOUNTS ARE CREATED AT CONDITION ADAPTER
			// if (excelCfg.getPeptideAmounts() != null) {
			// final List<Amount> psmAmounts = new PSMAmountsAdapterByExcel(
			// psmId, rowIndex, excelCfg.getPeptideAmounts(),
			// excelFileReader, expCondition).adapt();
			// for (Amount psmAmount : psmAmounts) {
			// psm.addAmount(psmAmount);
			// }
			// }
			// scores
			if (excelCfg.getPeptideScore() != null) {
				for (ScoreType scoreCfg : excelCfg.getPeptideScore()) {
					peptide.addScore(new ScoreAdapter(scoreCfg, excelFileReader, rowIndex).adapt());
				}
			}
			return peptide;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// private Set<Ratio> getPSMRatios(String psmId, int rowIndex) {
	// Set<Ratio> ret = null;
	// if (ratioMap.containsKey(psmId)) {
	// ret = ratioMap.get(psmId);
	// }
	// ret = new HashSet<Ratio>();
	// // psm ratios
	// if (psmRatiosCfg != null) {
	// final List<ExcelAmountRatioType> ratioListCfg = psmRatiosCfg
	// .getAmountRatio();
	// for (ExcelAmountRatioType ratioCfg : ratioListCfg) {
	// if (ratioCfg.getNominator().getConditionRef()
	// .equals(conditionID)
	// || ratioCfg.getDenominator().getConditionRef()
	// .equals(conditionID)) {
	// final ExcelColumn ratioExcelColumn = excelFileReader
	// .getExcelColumnFromReference(ratioCfg
	// .getColumnRef());
	// if (rowIndex < ratioExcelColumn.getValues().size()) {
	// final Object ratioValueObj = ratioExcelColumn
	// .getValues().get(rowIndex);
	// try {
	// double ratioValue = Double.valueOf(ratioValueObj
	// .toString());
	//
	// ret.add(new AmountRatioAdapter(
	// ratioCfg.getName(),
	// ratioValue,
	// ratioCfg.getNominator().getConditionRef(),
	// ratioCfg.getDenominator().getConditionRef(),
	// sample, sample, project).adapt());
	// } catch (NumberFormatException e) {
	// // do nothing
	// }
	// }
	// }
	// }
	// }
	// ratioMap.put(psmId, ret);
	// return ret;
	// }

	// private static void addPeptideByRowIndex(int rowIndex, Peptide peptide) {
	// if (peptideByRowIndex.containsKey(rowIndex)) {
	// peptideByRowIndex.get(rowIndex).add(peptide);
	// } else {
	// Set<Peptide> set = new HashSet<Peptide>();
	// set.add(peptide);
	// peptideByRowIndex.put(rowIndex, set);
	// }
	// }
	//
	// protected static void clearStaticInformation() {
	// peptideMapByMSRunID.clear();
	// // ratioMap.clear();
	// }
	//
	// protected static void clearStaticInformationByRow() {
	// peptideByRowIndex.clear();
	// }
}
