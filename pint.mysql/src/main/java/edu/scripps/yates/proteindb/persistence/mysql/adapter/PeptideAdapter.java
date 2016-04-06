package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PeptideAdapter implements Adapter<Peptide>, Serializable {
	private static final HashMap<Integer, Peptide> map = new HashMap<Integer, Peptide>();
	private final edu.scripps.yates.utilities.proteomicsmodel.Peptide peptide;
	private final Project hibProject;
	private final boolean createPSMsAndProteins;
	/**
	 *
	 */
	private static final long serialVersionUID = 2009879379965783199L;

	public PeptideAdapter(edu.scripps.yates.utilities.proteomicsmodel.Peptide peptide, Project hibProject,
			boolean createPSMs) {
		if (peptide == null)
			throw new IllegalArgumentException("Peptide cannot be null");
		this.peptide = peptide;
		this.hibProject = hibProject;
		createPSMsAndProteins = createPSMs;
	}

	@Override
	public Peptide adapt() {
		if (map.containsKey(peptide.hashCode())) {
			final Peptide peptide2 = map.get(peptide.hashCode());
			return peptide2;
		}

		final MsRun msRun = new MSRunAdapter(peptide.getMSRun(), hibProject).adapt();
		Peptide ret = new Peptide(msRun, peptide.getSequence());
		map.put(peptide.hashCode(), ret);

		// peptide amounts
		final Set<Amount> peptideAmounts = peptide.getAmounts();
		if (peptideAmounts != null) {
			for (Amount amount : peptideAmounts) {
				if (!Double.isNaN(amount.getValue())) {
					ret.getPeptideAmounts().add(new PeptideAmountAdapter(amount, ret, hibProject).adapt());
				}
			}
		}
		// ratios
		final Set<Ratio> ratios = peptide.getRatios();
		if (ratios != null) {
			for (Ratio ratio : ratios) {
				if (!Double.isNaN(ratio.getValue())) {
					ret.getPeptideRatioValues().add(new PeptideRatioValueAdapter(ratio, ret, hibProject).adapt());
				}
			}
		}

		// scores
		final Set<Score> scores = peptide.getScores();
		if (scores != null) {
			for (Score score : scores) {
				ret.getPeptideScores().add(new PeptideScoreAdapter(score, ret).adapt());
			}
		}
		if (createPSMsAndProteins) {
			// psms
			final Set<PSM> psMs = peptide.getPSMs();
			if (psMs != null && !psMs.isEmpty()) {
				for (PSM psm : psMs) {
					final Psm hibPsm = new PSMAdapter(psm, hibProject, createPSMsAndProteins).adapt();
					ret.getPsms().add(hibPsm);
					hibPsm.setPeptide(ret);
				}
			} else {
				// peptide has to have psms
				throw new IllegalArgumentException("peptide has to have psms");
			}

			// proteins
			final Set<edu.scripps.yates.utilities.proteomicsmodel.Protein> proteins = peptide.getProteins();
			if (proteins != null && !proteins.isEmpty()) {
				for (edu.scripps.yates.utilities.proteomicsmodel.Protein protein : proteins) {
					final Protein hibProtein = new ProteinAdapter(protein, hibProject, createPSMsAndProteins).adapt();
					ret.getProteins().add(hibProtein);
					hibProtein.getPeptides().add(ret);
				}
			} else {
				// peptide has to have proteins
				throw new IllegalArgumentException("peptide has to have proteins");
			}
		}
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
