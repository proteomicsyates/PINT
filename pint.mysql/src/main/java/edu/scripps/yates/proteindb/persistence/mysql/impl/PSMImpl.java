package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class PSMImpl implements PSM {
	private final static TIntObjectHashMap<PTM> ptmMap = new TIntObjectHashMap<PTM>();
	private final static TIntObjectHashMap<Score> psmScoresMap = new TIntObjectHashMap<Score>();
	private final static TIntObjectHashMap<Amount> psmAmountMap = new TIntObjectHashMap<Amount>();
	private final static TIntObjectHashMap<Ratio> psmRatiosMap = new TIntObjectHashMap<Ratio>();
	protected final static TIntObjectHashMap<PSM> psmMap = new TIntObjectHashMap<PSM>();
	private final Psm hibPSM;
	private List<PTM> ptms;
	private MSRun msRun;
	private final Set<Score> scores = new THashSet<Score>();
	private final Set<Amount> amounts = new THashSet<Amount>();
	private final Set<Protein> proteins = new THashSet<Protein>();

	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private PeptideRelation relation;
	private boolean scoresParsed = false;
	private boolean psmAmountsParsed = false;
	private boolean psmRatioParsed = false;
	private final Set<Condition> conditions = new THashSet<Condition>();
	private boolean conditionsParsed = false;
	private Peptide peptide;
	private boolean peptideParsed = false;

	public PSMImpl(Psm hibPSM) {
		this.hibPSM = hibPSM;
		psmMap.put(hibPSM.getId(), this);
	}

	@Override
	public String getPSMIdentifier() {
		return hibPSM.getPsmId();
	}

	@Override
	public Double getExperimentalMH() {
		return hibPSM.getMh();
	}

	@Override
	public Double getCalcMH() {
		return hibPSM.getCalMh();
	}

	@Override
	public Double getMassErrorPPM() {
		return hibPSM.getPpmError();
	}

	@Override
	public Double getTotalIntensity() {
		return hibPSM.getTotalIntensity();
	}

	@Override
	public Integer getSPR() {
		return hibPSM.getSpr();
	}

	@Override
	public Double getIonProportion() {
		return hibPSM.getIonProportion();
	}

	@Override
	public Double getPI() {
		return hibPSM.getPi();
	}

	@Override
	public String getFullSequence() {
		return hibPSM.getFullSequence();
	}

	@Override
	public String getSequence() {
		return hibPSM.getSequence();
	}

	@Override
	public List<PTM> getPTMs() {
		if (ptms == null) {
			ptms = new ArrayList<PTM>();
			final Set<Ptm> hibPTMs = hibPSM.getPtms();
			for (Ptm hibPTM : hibPTMs) {
				if (ptmMap.containsKey(hibPTM.getId())) {
					ptms.add(ptmMap.get(hibPTM.getId()));
				} else {
					final PTMImpl ptmImpl = new PTMImpl(hibPTM);
					ptmMap.put(hibPTM.getId(), ptmImpl);
					ptms.add(ptmImpl);
				}
			}
		}
		return ptms;
	}

	@Override
	public MSRun getMSRun() {
		if (msRun == null) {
			final MsRun hibMSRun = hibPSM.getMsRun();
			if (hibMSRun != null) {
				if (MSRunImpl.msRuns.containsKey(hibMSRun.getId())) {
					msRun = MSRunImpl.msRuns.get(hibMSRun.getId());
				} else {
					msRun = new MSRunImpl(hibMSRun);
				}
			}
		}
		return msRun;
	}

	@Override
	public Set<Score> getScores() {
		if (!scoresParsed) {
			final Set<PsmScore> hibScores = hibPSM.getPsmScores();
			for (PsmScore hibScore : hibScores) {
				if (psmScoresMap.containsKey(hibScore.getId())) {
					scores.add(psmScoresMap.get(hibScore.getId()));
				} else {
					final ScoreImpl scoreImpl = new ScoreImpl(hibScore);
					psmScoresMap.put(hibScore.getId(), scoreImpl);
					scores.add(scoreImpl);
				}
			}
			scoresParsed = true;
		}
		return scores;
	}

	@Override
	public Set<Ratio> getRatios() {
		if (!psmRatioParsed) {
			final Set<PsmRatioValue> psmRatioValues = hibPSM.getPsmRatioValues();
			if (psmRatioValues != null) {
				for (PsmRatioValue psmRatioValue : psmRatioValues) {
					if (psmRatiosMap.containsKey(psmRatioValue.getId())) {
						ratios.add(psmRatiosMap.get(psmRatioValue.getId()));
					} else {
						final Ratio proteinRatioImpl = new RatioImpl(psmRatioValue);
						psmRatiosMap.put(psmRatioValue.getId(), proteinRatioImpl);
						ratios.add(proteinRatioImpl);
					}
				}
			}
			psmRatioParsed = true;
		}
		return ratios;
	}

	@Override
	public Set<Amount> getAmounts() {
		if (!psmAmountsParsed) {
			final Set<PsmAmount> hibPsmAmounts = hibPSM.getPsmAmounts();

			for (PsmAmount hibpsmAmount : hibPsmAmounts) {
				Amount psmAmountImpl = null;
				if (PSMImpl.psmAmountMap.containsKey(hibpsmAmount.getId())) {
					psmAmountImpl = PSMImpl.psmAmountMap.get(hibpsmAmount.getId());
				} else {
					psmAmountImpl = new AmountImpl(hibpsmAmount);
					PSMImpl.psmAmountMap.put(hibpsmAmount.getId(), psmAmountImpl);
				}
				amounts.add(psmAmountImpl);
			}
			psmAmountsParsed = true;
		}
		return amounts;
	}

	@Override
	public Set<Protein> getProteins() {
		if (proteins == null) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> hibProteins = hibPSM.getProteins();
			for (edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein : hibProteins) {
				if (ProteinImpl.proteinMap.containsKey(hibProtein.getId())) {
					proteins.add(ProteinImpl.proteinMap.get(hibProtein.getId()));
				} else {
					final ProteinImpl proteinImpl = new ProteinImpl(hibProtein);
					proteins.add(proteinImpl);
					proteinImpl.getPSMs().add(this);
				}
			}
		}
		return proteins;
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;
	}

	@Override
	public Peptide getPeptide() {
		if (!peptideParsed) {
			final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = hibPSM.getPeptide();
			if (hibPeptide != null) {
				if (PeptideImpl.peptideMap.containsKey(hibPeptide.getId()))
					peptide = PeptideImpl.peptideMap.get(hibPeptide.getId());
				else
					peptide = new PeptideImpl(hibPeptide);
			}
			peptideParsed = true;
		}
		return peptide;
	}

	@Override
	public Set<Condition> getConditions() {
		if (!conditionsParsed) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> hibConditions = hibPSM.getConditions();
			if (hibConditions != null) {
				for (edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition : hibConditions) {
					conditions.add(new ConditionImpl(hibCondition));
				}
			}
			conditionsParsed = true;
		}
		return conditions;
	}

	@Override
	public int getDBId() {
		return hibPSM.getId();
	}

	@Override
	public String getAfterSeq() {
		return hibPSM.getAfterSeq();
	}

	@Override
	public String getBeforeSeq() {
		return hibPSM.getBeforeSeq();
	}

	@Override
	public PeptideRelation getRelation() {
		return relation;
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
		if (!amounts.contains(amount))
			amounts.add(amount);

	}

	@Override
	public void addCondition(Condition condition) {
		if (!conditions.contains(condition))
			conditions.add(condition);

	}

	@Override
	public void setPeptide(Peptide peptide) {
		this.peptide = peptide;

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void addProtein(Protein protein) {
		if (!proteins.contains(protein))
			proteins.add(protein);
	}

	@Override
	public String getChargeState() {
		return hibPSM.getChargeState();
	}

	@Override
	public String getScanNumber() {
		return "";
	}

}
