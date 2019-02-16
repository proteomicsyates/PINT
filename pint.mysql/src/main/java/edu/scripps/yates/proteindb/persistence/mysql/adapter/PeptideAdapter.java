package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PeptideAdapter implements Adapter<Peptide>, Serializable {
	private static final TIntObjectHashMap<Peptide> map = new TIntObjectHashMap<Peptide>();
	private final edu.scripps.yates.utilities.proteomicsmodel.Peptide peptide;
	private final Project hibProject;
	/**
	 *
	 */
	private static final long serialVersionUID = 2009879379965783199L;

	public PeptideAdapter(edu.scripps.yates.utilities.proteomicsmodel.Peptide peptide, Project hibProject) {
		if (peptide == null)
			throw new IllegalArgumentException("Peptide cannot be null");
		this.peptide = peptide;
		this.hibProject = hibProject;
	}

	@Override
	public Peptide adapt() {
		if (map.containsKey(peptide.hashCode())) {
			final Peptide peptide2 = map.get(peptide.hashCode());
			return peptide2;
		}

		final Peptide ret = new Peptide(peptide.getSequence());
		for (final MSRun msRun : peptide.getMSRuns()) {
			final MsRun hibMsRun = new MSRunAdapter(msRun, hibProject).adapt();
			ret.getMsRuns().add(hibMsRun);
		}
		ret.setFullSequence(ModelUtils.getFullSequence(peptide.getSequence(), peptide.getPTMs()));

		map.put(peptide.hashCode(), ret);

		// peptide amounts
		final Set<Amount> peptideAmounts = peptide.getAmounts();
		if (peptideAmounts != null) {
			for (final Amount amount : peptideAmounts) {
				if (!Double.isNaN(amount.getValue())) {
					ret.getPeptideAmounts().add(new PeptideAmountAdapter(amount, ret, hibProject).adapt());
				}
			}
		}
		// ratios
		final Set<Ratio> ratios = peptide.getRatios();
		if (ratios != null) {
			for (final Ratio ratio : ratios) {
				if (!Double.isNaN(ratio.getValue())) {
					ret.getPeptideRatioValues().add(new PeptideRatioValueAdapter(ratio, ret, hibProject).adapt());
				}
			}
		}

		// scores
		final Set<Score> scores = peptide.getScores();
		if (scores != null) {
			for (final Score score : scores) {
				ret.getPeptideScores().add(new PeptideScoreAdapter(score, ret).adapt());
			}
		}

		// psms
		final List<PSM> psMs = peptide.getPSMs();
		if (psMs != null && !psMs.isEmpty()) {
			for (final PSM psm : psMs) {
				final Psm hibPsm = new PSMAdapter(psm, hibProject).adapt();
				ret.getPsms().add(hibPsm);
				hibPsm.setPeptide(ret);
			}
			ret.setNumPsms(psMs.size());
		} else {
			// peptide has to have psms
			throw new IllegalArgumentException("peptide has to have psms");
		}
		// ptms (AFTER PSMS)
		final List<PTM> ptms = peptide.getPTMs();
		if (ptms != null && !ptms.isEmpty()) {
			for (final PTM ptm : ptms) {
				final Ptm hibPtm = new PTMAdapter(ptm, ret).adapt();
				ret.getPtms().add(hibPtm);
				hibPtm.setPeptide(ret);
			}
		}
		// proteins
		final Set<edu.scripps.yates.utilities.proteomicsmodel.Protein> proteins = peptide.getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			for (final edu.scripps.yates.utilities.proteomicsmodel.Protein protein : proteins) {
				final Protein hibProtein = new ProteinAdapter(protein, hibProject).adapt();
				ret.getProteins().add(hibProtein);
				hibProtein.getPeptides().add(ret);
			}
		} else {
			// peptide has to have proteins
			throw new IllegalArgumentException("peptide has to have proteins");
		}
		// conditions
		final Set<Condition> conditions = peptide.getConditions();
		for (final Condition condition : conditions) {
			ret.getConditions().add(new ConditionAdapter(condition, hibProject).adapt());
		}
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
