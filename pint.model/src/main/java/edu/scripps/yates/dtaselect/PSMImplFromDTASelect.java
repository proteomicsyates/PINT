package edu.scripps.yates.dtaselect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.cv.CommonlyUsedCV;
import edu.scripps.yates.dtaselectparser.util.DTASelectModification;
import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.model.factories.ScoreEx;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.ProteomicsModelStaticStorage;

public class PSMImplFromDTASelect implements edu.scripps.yates.utilities.proteomicsmodel.PSM {
	private final DTASelectPSM dtaSelectPSM;
	private final List<PTM> ptms;
	private final Set<Score> scores = new HashSet<Score>();
	private final Set<Protein> proteins = new HashSet<Protein>();
	private PeptideRelation relation;
	private final Set<Amount> psmAmounts = new HashSet<Amount>();
	private Peptide peptide;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private MSRun msRun;
	private boolean scoreParsed = false;
	private final Set<Ratio> ratios = new HashSet<Ratio>();
	// protected static final Map<DTASelectPSM, PSM> psmMap = new
	// HashMap<DTASelectPSM, PSM>();
	// protected static final Map<String, Map<String, Peptide>>
	// peptideMapByMSRun = new HashMap<String, Map<String, Peptide>>();
	// private static final String probability =
	// CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID);
	private static final String psmScoreName = CVManager
			.getPreferredName(CommonlyUsedCV.SEARCH_ENGINE_SPECIFIC_SCORE_FOR_PSM);
	private static final String deltaCnName = CVManager.getPreferredName(CommonlyUsedCV.DeltaCNID);
	private static final String xcorrName = CVManager.getPreferredName(CommonlyUsedCV.XCorrID);

	public PSMImplFromDTASelect(DTASelectPSM dtaSelectPSM, MSRun msRun) {
		// psmMap.put(dtaSelectPSM, this);
		this.dtaSelectPSM = dtaSelectPSM;
		// load ptms
		ptms = new ArrayList<PTM>();
		final List<DTASelectModification> modifications = this.dtaSelectPSM.getModifications();
		if (modifications != null) {
			for (DTASelectModification dtaSelectModification : modifications) {
				ptms.add(new PTMImplFromDTASelect(dtaSelectModification));
			}
		}
		this.msRun = msRun;
	}

	@Override
	public String getPSMIdentifier() {
		return dtaSelectPSM.getPsmIdentifier();
	}

	@Override
	public Double getExperimentalMH() {
		return dtaSelectPSM.getMh();
	}

	@Override
	public Double getCalcMH() {
		return dtaSelectPSM.getCalcMh();
	}

	@Override
	public Double getMassErrorPPM() {
		return dtaSelectPSM.getPpmError();
	}

	@Override
	public Double getTotalIntensity() {
		return dtaSelectPSM.getTotalIntensity();
	}

	@Override
	public Integer getSPR() {
		return dtaSelectPSM.getSpr();
	}

	@Override
	public Double getIonProportion() {
		return dtaSelectPSM.getIonProportion();
	}

	@Override
	public Double getPI() {
		return dtaSelectPSM.getPi();
	}

	@Override
	public String getFullSequence() {
		return dtaSelectPSM.getSequence().getRawSequence();
	}

	@Override
	public String getSequence() {
		return dtaSelectPSM.getSequence().getSequence();
	}

	@Override
	public List<PTM> getPTMs() {
		return ptms;

	}

	@Override
	public MSRun getMSRun() {
		if (msRun == null) {
			msRun = new MSRunEx(dtaSelectPSM.getRunID(), dtaSelectPSM.getRunPath());
		}
		return msRun;
	}

	@Override
	public Set<Score> getScores() {
		if (!scoreParsed) {
			final Double conf = dtaSelectPSM.getConf();

			// if (conf != null) {
			// final String confidenceScore = CVManager
			// .getPreferredName(CommonlyUsedCV.confidenceScoreID);
			// Score score = new ScoreEx(conf.toString(), "Conf%",
			// confidenceScore, null);
			// scores.add(score);
			// }

			// final Double prob = dtaSelectPSM.getProb();
			// if (prob != null) {
			// Score score = new ScoreEx(prob.toString(), "Prob%",
			// probability, null);
			// scores.add(score);
			// }
			//
			// final Double probScore = dtaSelectPSM.getProb_score();
			// if (probScore != null) {
			// Score score = new ScoreEx(probScore.toString(), "Prob Score",
			// probability, null);
			// scores.add(score);
			// }

			final Double xcorr = dtaSelectPSM.getXcorr();
			if (xcorr != null) {

				Score score = new ScoreEx(xcorr.toString(), xcorrName, psmScoreName, null);
				scores.add(score);
			}

			final Double deltaCn = dtaSelectPSM.getDeltacn();
			if (deltaCn != null) {

				Score score = new ScoreEx(deltaCn.toString(), deltaCnName, psmScoreName, null);
				scores.add(score);
			}
			scoreParsed = true;
		}
		return scores;
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public Set<Amount> getAmounts() {
		return psmAmounts;
	}

	@Override
	public Set<Protein> getProteins() {

		return proteins;
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;

	}

	@Override
	public void setPeptide(Peptide peptide) {
		this.peptide = peptide;
	}

	@Override
	public Peptide getPeptide() {
		if (peptide == null) {
			if (ProteomicsModelStaticStorage.containsPeptide(msRun, null, getSequence())) {
				peptide = ProteomicsModelStaticStorage.getSinglePeptide(msRun, null, getSequence());
			} else {
				peptide = new PeptideEx(getSequence(), msRun);
				ProteomicsModelStaticStorage.addPeptide(peptide, msRun, null);
			}
		}
		return peptide;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	@Override
	public void addCondition(Condition condition) {
		if (!conditions.contains(condition))
			conditions.add(condition);
	}

	@Override
	public int getDBId() {
		return -1;
	}

	@Override
	public String getAfterSeq() {
		return String.valueOf(dtaSelectPSM.getSequence().getAfterSeq());
	}

	@Override
	public String getBeforeSeq() {
		return String.valueOf(dtaSelectPSM.getSequence().getBeforeSeq());
	}

	@Override
	public PeptideRelation getRelation() {
		return relation;
	}

	@Override
	public void setMSRun(MSRun msrun) {
		msRun = msrun;
	}

	@Override
	public void addProtein(Protein protein) {
		if (!proteins.contains(protein))
			proteins.add(protein);

	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		List<GroupableProtein> ret = new ArrayList<GroupableProtein>();
		ret.addAll(getProteins());
		return ret;
	}

	@Override
	public void addScore(Score score) {
		if (!scores.contains(score))
			scores.add(score);

	}

	@Override
	public void addRatio(Ratio ratio) {
		if (!ratios.contains(ratio))
			ratios.add(ratio);
	}

	@Override
	public void addAmount(Amount amount) {
		if (!psmAmounts.contains(amount))
			psmAmounts.add(amount);

	}

	@Override
	public String getChargeState() {
		final Integer charge = dtaSelectPSM.getChargeState();
		if (charge != null) {
			return String.valueOf(charge);
		}
		return null;
	}

	@Override
	public String getScanNumber() {
		return dtaSelectPSM.getScan();
	}

}
