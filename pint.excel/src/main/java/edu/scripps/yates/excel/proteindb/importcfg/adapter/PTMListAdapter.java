package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PtmScoreType;
import edu.scripps.yates.excel.util.ExcelUtils;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PTMSiteEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.ScoreEx;
import gnu.trove.map.hash.THashMap;

public class PTMListAdapter implements edu.scripps.yates.utilities.pattern.Adapter<List<PTM>> {
	private final String rawPeptideSequence;
	private final IdentificationExcelType excelTypeCfg;
	private final ExcelFileReader excelFileReader;
	private final int rowIndex;
	private static final Map<String, List<PTM>> map = new THashMap<String, List<PTM>>();
	private final String psmId;

	public PTMListAdapter(String psmId, int rowIndex, String rawPeptideSequence, IdentificationExcelType excelTypeCfg,
			ExcelFileReader excelFileReader) {
		this.psmId = psmId;
		this.rowIndex = rowIndex;
		this.rawPeptideSequence = rawPeptideSequence;
		this.excelTypeCfg = excelTypeCfg;
		this.excelFileReader = excelFileReader;
	}

	@Override
	public List<PTM> adapt() {
		if (map.containsKey(psmId)) {
			return map.get(psmId);
		}
		final List<PtmScoreType> ptmScoresCfg = excelTypeCfg.getPtmScore();
		List<Score> ptmScores = getScores(ptmScoresCfg);
		List<PTM> ptms = ExcelUtils.getPTMsFromRawSequence(rawPeptideSequence);
		if (ptmScores.size() == 1) {
			// if there is just one ptmscore, apply to all ptms
			for (PTM ptm : ptms) {
				for (PTMSite site : ptm.getPTMSites()) {
					PTMSiteEx siteEx = (PTMSiteEx) site;
					siteEx.setScore(ptmScores.get(0));
				}

			}
		} else if (!ptmScores.isEmpty()) {
			try {
				// if there is more than one, they must be the same
				// number
				// of ptms sites, and assign each one in the list to the
				// corresponding one in the ptm list
				for (PTM ptm : ptms) {
					for (int j = 0; j < ptm.getPTMSites().size(); j++) {
						final PTMSiteEx ptmSite = (PTMSiteEx) ptm.getPTMSites().get(j);
						final Score score = ptmScores.get(j);
						ptmSite.setScore(score);
					}

				}
			} catch (IndexOutOfBoundsException e) {
				throw new IllegalArgumentException(
						"some error happened when trying to assign PTM scores to peptide sequence '"
								+ rawPeptideSequence + "'");
			}
		}
		map.put(psmId, ptms);
		return ptms;
	}

	private List<Score> getScores(List<PtmScoreType> ptmScores) {
		List<Score> scores = new ArrayList<Score>();
		if (ptmScores != null)
			for (PtmScoreType scoreCfg : ptmScores) {
				final ExcelColumn excelColumnFromReference = excelFileReader
						.getExcelColumnFromReference(scoreCfg.getColumnRef());
				final String scoreName = scoreCfg.getScoreName();
				final Object object = excelColumnFromReference.getValues().get(rowIndex);

				ScoreEx score = new ScoreEx(object.toString(), scoreName, scoreCfg.getScoreType(),
						scoreCfg.getDescription());
				scores.add(score);

			}
		return scores;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
