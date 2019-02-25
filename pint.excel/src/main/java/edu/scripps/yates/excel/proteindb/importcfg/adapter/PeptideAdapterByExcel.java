package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.Collection;
import java.util.Set;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PeptideEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import gnu.trove.set.hash.THashSet;

public class PeptideAdapterByExcel implements Adapter<Peptide> {
	// private final static Map<String, Map<String, PeptideEx>>
	// peptideMapByMSRunID = new THashMap<String, Map<String, PeptideEx>>();
	// private final static Map<String, Set<Ratio>> ratioMap = new
	// HashMap<String, Set<Ratio>>();

	private final int rowIndex;
	private final IdentificationExcelType excelCfg;
	private final ExcelFileReader excelFileReader;
	private final Set<MSRun> msRuns = new THashSet<MSRun>();
	// public static final TIntObjectHashMap< Set<Peptide>> peptideByRowIndex =
	// new
	// TIntObjectHashMap< Set<Peptide>>();
	private final Condition condition;

	public PeptideAdapterByExcel(int rowIndex, IdentificationExcelType excelCfg, ExcelFileReader excelFileReader,
			Collection<MSRun> msRuns, Condition condition) {
		this.rowIndex = rowIndex;
		this.excelCfg = excelCfg;
		this.excelFileReader = excelFileReader;
		this.msRuns.addAll(msRuns);
		this.condition = condition;
	}

	@Override
	public Peptide adapt() {
		try {
			final SequenceType peptideSequenceCfg = excelCfg.getSequence();

			final ExcelColumn peptideSequenceColumn = excelFileReader
					.getExcelColumnFromReference(peptideSequenceCfg.getColumnRef());

			final String rawPeptideSequence = peptideSequenceColumn.getValues().get(rowIndex).toString();

			final String sequenceInBetween = FastaParser.getSequenceInBetween(rawPeptideSequence);
			//
			// Map<String, PeptideEx> peptideMap = null;
			// if (peptideMapByMSRunID.containsKey(msRun.getId())) {
			// peptideMap = peptideMapByMSRunID.get(msRun.getId());
			// } else {
			// peptideMap = new THashMap<String, PeptideEx>();
			// peptideMapByMSRunID.put(msRun.getId(), peptideMap);
			// }

			Peptide peptide = null;
			if (StaticProteomicsModelStorage.containsPeptide(msRuns, null, sequenceInBetween)) {
				peptide = StaticProteomicsModelStorage.getSinglePeptide(msRuns, null, sequenceInBetween);
			} else {
				peptide = new PeptideEx(rawPeptideSequence);
				// peptideMap.put(cleanPeptideSequence, peptide);
			}
			for (final MSRun msRun2 : msRuns) {
				peptide.addMSRun(msRun2);
			}

			peptide.addCondition(condition);
			// addPeptideByRowIndex(rowIndex, peptide);
			StaticProteomicsModelStorage.addPeptide(peptide, msRuns, condition.getName(), rowIndex);
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
				for (final ScoreType scoreCfg : excelCfg.getPeptideScore()) {
					peptide.addScore(new ScoreAdapter(scoreCfg, excelFileReader, rowIndex).adapt());
				}
			}
			return peptide;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// private Set<Ratio> getPSMRatios(String psmId, int rowIndex) {
	// Set<Ratio> ret = null;
	// if (ratioMap.containsKey(psmId)) {
	// ret = ratioMap.get(psmId);
	// }
	// ret = new THashSet<Ratio>();
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
	// Set<Peptide> set = new THashSet<Peptide>();
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
