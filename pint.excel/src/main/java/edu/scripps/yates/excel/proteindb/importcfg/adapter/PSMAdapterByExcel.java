package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.List;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.factories.PSMEx;
import edu.scripps.yates.utilities.pattern.Adapter;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.ProteomicsModelStaticStorage;

public class PSMAdapterByExcel implements Adapter<PSM> {
	// private final static Map<String, Map<String, PSMEx>> psmMapByMSRunID =
	// new HashMap<String, Map<String, PSMEx>>();
	// private final static Map<String, Set<Ratio>> ratioMap = new
	// HashMap<String, Set<Ratio>>();

	private final int rowIndex;
	private final IdentificationExcelType excelCfg;
	private final ExcelFileReader excelFileReader;
	private final MsRunType msRun;
	private final Condition condition;

	// private static final Map<String, Map<Integer, Set<PSM>>>
	// psmsByMSRunAndRowIndex = new HashMap<String, Map<Integer, Set<PSM>>>();

	public PSMAdapterByExcel(int rowIndex, IdentificationExcelType excelCfg, ExcelFileReader excelFileReader,
			MsRunType msRun, Condition condition) {
		this.rowIndex = rowIndex;
		this.excelCfg = excelCfg;
		this.excelFileReader = excelFileReader;
		this.msRun = msRun;
		this.condition = condition;
	}

	@Override
	public PSM adapt() {
		try {
			final SequenceType psmSequenceCfg = excelCfg.getSequence();

			final ExcelColumn psmSequenceColumn = excelFileReader
					.getExcelColumnFromReference(psmSequenceCfg.getColumnRef());

			final String rawPsmSequence = psmSequenceColumn.getValues().get(rowIndex).toString();

			String psmId = (rowIndex + 1) + "-" + msRun.getId();

			String cleanPsmSequence = FastaParser.cleanSequence(rawPsmSequence);

			PSM psm = null;
			// Map<String, PSMEx> psmMap = null;
			// if (psmMapByMSRunID.containsKey(msRun.getId())) {
			// psmMap = psmMapByMSRunID.get(msRun.getId());
			// } else {
			// psmMap = new HashMap<String, PSMEx>();
			// psmMapByMSRunID.put(msRun.getId(), psmMap);
			// }
			if (ProteomicsModelStaticStorage.containsPSM(msRun.getId(), null, rowIndex, psmId)) {
				psm = ProteomicsModelStaticStorage.getPSM(msRun.getId(), null, rowIndex, psmId).iterator().next();
			} else {
				psm = new PSMEx(psmId, cleanPsmSequence, rawPsmSequence);
				psm.setMSRun(new MSRunAdapter(msRun).adapt());

				// psmMap.put(psmId, psm);
			}
			psm.addCondition(condition);
			// addPSMByMSRunIDAndRowIndex(msRun.getId(), rowIndex, psm);
			ProteomicsModelStaticStorage.addPSM(psm, msRun.getId(), condition.getName(), rowIndex);

			// modifications

			List<PTM> ptms = new PTMListAdapter(psmId, rowIndex, rawPsmSequence, excelCfg, excelFileReader).adapt();
			for (PTM ptm : ptms) {
				if (psm instanceof PSMEx) {
					((PSMEx) psm).addPtm(ptm);
				}
			}

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
			if (excelCfg.getPsmScore() != null) {
				for (ScoreType scoreCfg : excelCfg.getPsmScore()) {
					psm.addScore(new ScoreAdapter(scoreCfg, excelFileReader, rowIndex).adapt());
				}
			}
			return psm;
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

	// private static void addPSMByMSRunIDAndRowIndex(String msRunRef, int
	// rowIndex, PSM psm) {
	// if (psmsByMSRunAndRowIndex.containsKey(msRunRef)) {
	// final Map<Integer, Set<PSM>> map = psmsByMSRunAndRowIndex.get(msRunRef);
	// addPSMByRowIndex(map, rowIndex, psm);
	// } else {
	// Map<Integer, Set<PSM>> map = new HashMap<Integer, Set<PSM>>();
	// addPSMByRowIndex(map, rowIndex, psm);
	// psmsByMSRunAndRowIndex.put(msRunRef, map);
	// }
	// }
	//
	// private static void addPSMByRowIndex(Map<Integer, Set<PSM>> map, int
	// rowIndex, PSM psm) {
	// if (map.containsKey(rowIndex)) {
	// map.get(rowIndex).add(psm);
	// } else {
	// Set<PSM> set = new HashSet<PSM>();
	// set.add(psm);
	// map.put(rowIndex, set);
	// }
	// }
	//
	// protected static void clearStaticInformation() {
	// psmMapByMSRunID.clear();
	// // ratioMap.clear();
	// }
	//
	// protected static void clearStaticInformationByRow() {
	// psmsByMSRunAndRowIndex.clear();
	// }

	// protected static Set<PSM> getPSMsByMSRunAndRow(int rowIndex, String
	// msRunRef) {
	// Set<PSM> ret = new HashSet<PSM>();
	// if (psmsByMSRunAndRowIndex.containsKey(msRunRef)) {
	// final Map<Integer, Set<PSM>> psmsByRowIndex =
	// psmsByMSRunAndRowIndex.get(msRunRef);
	// if (psmsByRowIndex.containsKey(rowIndex)) {
	// return psmsByRowIndex.get(rowIndex);
	// }
	// }
	// return ret;
	// }

}
