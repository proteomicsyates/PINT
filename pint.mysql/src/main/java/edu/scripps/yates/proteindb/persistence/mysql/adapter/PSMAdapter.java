package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PSMAdapter implements Adapter<Psm>, Serializable {
	/**
	 *
	 */
	private static final Logger log = Logger.getLogger(PSMAdapter.class);
	private static final long serialVersionUID = 8001310054173070297L;
	private final PSM psm;
	private final Project hibProject;
	private final static Map<Integer, Psm> map = new HashMap<Integer, Psm>();

	public PSMAdapter(PSM psm, Project hibProject) {
		this.psm = psm;
		this.hibProject = hibProject;
	}

	@Override
	public Psm adapt() {

		if (map.containsKey(psm.hashCode())) {
			final Psm psm2 = map.get(psm.hashCode());
			return psm2;
		}
		MsRun msRun = new MSRunAdapter(psm.getMSRun(), hibProject).adapt();

		Psm ret = new Psm(msRun, null, psm.getSequence(), psm.getFullSequence());
		map.put(psm.hashCode(), ret);
		Peptide parentPeptide = null;
		if (psm.getPeptide() != null) {
			parentPeptide = new PeptideAdapter(psm.getPeptide(), hibProject).adapt();
		} else {
			log.error("No peptide in this psm!");
		}
		ret.setPeptide(parentPeptide);

		ret.setPsmId(psm.getPSMIdentifier());
		ret.setCalMh(psm.getCalcMH());
		ret.setIonProportion(psm.getIonProportion());
		ret.setMh(psm.getExperimentalMH());
		ret.setPi(psm.getPI());
		ret.setPpmError(psm.getMassErrorPPM());
		// ret.getProteins().add(protein);
		ret.setSpr(psm.getSPR());
		ret.setTotalIntensity(psm.getTotalIntensity());
		ret.setAfterSeq(psm.getAfterSeq());

		ret.setBeforeSeq(psm.getBeforeSeq());
		ret.setChargeState(psm.getChargeState());
		// ptms
		if (psm.getPTMs() != null) {
			for (PTM ptm : psm.getPTMs()) {
				ret.getPtms().add(new PTMAdapter(ptm, ret).adapt());
			}

		}
		// scores
		final Set<Score> scores = psm.getScores();
		if (scores != null) {
			for (Score score : scores) {
				ret.getPsmScores().add(new PSMScoreAdapter(score, ret).adapt());
			}
		}
		// psm ratios
		final Set<Ratio> peptideRatios = psm.getRatios();
		if (peptideRatios != null) {
			for (Ratio peptideRatioModel : peptideRatios) {
				if (!Double.isNaN(peptideRatioModel.getValue())) {
					final PsmRatioValue hibProteinRatio = new PSMRatioValueAdapter(peptideRatioModel, ret, hibProject)
							.adapt();
					ret.getPsmRatioValues().add(hibProteinRatio);
				}
			}
		}
		// amounts
		if (psm.getAmounts() != null) {
			// now adapt the proteins
			for (Amount amount : psm.getAmounts()) {
				if (!Double.isNaN(amount.getValue())) {
					ret.getPsmAmounts().add(new PSMAmountAdapter(amount, ret, hibProject).adapt());
				}
			}
		}

		// proteins
		// ret.getProteins().addAll(psm.getProteins());

		// proteins
		if (psm.getProteins() != null) {
			for (edu.scripps.yates.utilities.proteomicsmodel.Protein protein : psm.getProteins()) {
				ret.getProteins().add(new ProteinAdapter(protein, hibProject).adapt());
			}
		}

		// conditions
		if (psm.getConditions() != null) {
			for (Condition condition : psm.getConditions()) {
				ret.getConditions().add(new ConditionAdapter(condition, hibProject).adapt());
			}
		}
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
