package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class PeptideImpl implements Peptide {
	private final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide;
	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private final Set<Amount> amounts = new THashSet<Amount>();
	private final Set<Score> scores = new THashSet<Score>();
	private final Set<Protein> proteins = new THashSet<Protein>();
	private final Set<Condition> conditions = new THashSet<Condition>();
	private boolean ratiosParsed = false;
	private boolean amountsParsed = false;
	private boolean scoresParsed = false;
	private boolean conditionsParsed = false;
	private final Set<PSM> psms = new THashSet<PSM>();
	private boolean psmsParsed = false;
	private MSRun msRun;
	private boolean msRunParsed = false;
	private final static TIntObjectHashMap<Ratio> peptideRatiosMap = new TIntObjectHashMap<Ratio>();
	private final static TIntObjectHashMap<Amount> peptideAmountMap = new TIntObjectHashMap<Amount>();;
	private final static TIntObjectHashMap<Score> peptideScoreMap = new TIntObjectHashMap<Score>();
	protected final static TIntObjectHashMap<Peptide> peptideMap = new TIntObjectHashMap<Peptide>();

	public PeptideImpl(edu.scripps.yates.proteindb.persistence.mysql.Peptide peptide) {
		hibPeptide = peptide;
		peptideMap.put(peptide.getId(), this);
	}

	@Override
	public int getDBId() {
		return hibPeptide.getId();
	}

	@Override
	public Set<PSM> getPSMs() {
		if (!psmsParsed) {
			final Set<Psm> hibPsms = hibPeptide.getPsms();
			if (hibPsms != null) {
				for (Psm hibPsm : hibPsms) {
					if (PSMImpl.psmMap.containsKey(hibPsm.getId())) {
						psms.add(PSMImpl.psmMap.get(hibPsm.getId()));
					} else {
						psms.add(new PSMImpl(hibPsm));
					}
				}
			}
			psmsParsed = true;
		}
		return psms;
	}

	@Override
	public Set<Ratio> getRatios() {
		if (!ratiosParsed) {
			final Set<PeptideRatioValue> peptideRatioValues = hibPeptide.getPeptideRatioValues();
			if (peptideRatioValues != null) {
				for (PeptideRatioValue peptideRatioValue : peptideRatioValues) {
					if (peptideRatiosMap.containsKey(peptideRatioValue.getId())) {
						ratios.add(peptideRatiosMap.get(peptideRatioValue.getId()));
					} else {
						final Ratio ratioImpl = new RatioImpl(peptideRatioValue);
						peptideRatiosMap.put(peptideRatioValue.getId(), ratioImpl);
						ratios.add(ratioImpl);
					}
				}
			}
			ratiosParsed = true;
		}
		return ratios;
	}

	@Override
	public Set<Amount> getAmounts() {
		if (!amountsParsed) {
			final Set<PeptideAmount> hibPeptideAmounts = hibPeptide.getPeptideAmounts();

			for (PeptideAmount hibPeptideAmount : hibPeptideAmounts) {
				Amount peptideAmountImpl = null;
				if (PeptideImpl.peptideAmountMap.containsKey(hibPeptideAmount.getId())) {
					peptideAmountImpl = PeptideImpl.peptideAmountMap.get(hibPeptideAmount.getId());
				} else {
					peptideAmountImpl = new AmountImpl(hibPeptideAmount);
					PeptideImpl.peptideAmountMap.put(hibPeptideAmount.getId(), peptideAmountImpl);
				}
				amounts.add(peptideAmountImpl);
			}
			amountsParsed = true;
		}
		return amounts;
	}

	@Override
	public Set<Score> getScores() {
		if (!scoresParsed) {
			final Set<PeptideScore> hibScores = hibPeptide.getPeptideScores();
			for (PeptideScore hibScore : hibScores) {
				if (peptideScoreMap.containsKey(hibScore.getId())) {
					scores.add(peptideScoreMap.get(hibScore.getId()));
				} else {
					final ScoreImpl scoreImpl = new ScoreImpl(hibScore);
					peptideScoreMap.put(hibScore.getId(), scoreImpl);
					scores.add(scoreImpl);
				}
			}
			scoresParsed = true;
		}
		return scores;
	}

	@Override
	public String getSequence() {
		return hibPeptide.getSequence();
	}

	@Override
	public Set<Protein> getProteins() {
		if (proteins == null) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> hibProteins = hibPeptide.getProteins();
			if (hibProteins != null) {
				for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : hibProteins) {
					if (ProteinImpl.proteinMap.containsKey(protein.getId())) {
						proteins.add(ProteinImpl.proteinMap.get(protein.getId()));
					} else {
						proteins.add(new ProteinImpl(protein));
					}
				}
			}
		}
		return proteins;
	}

	@Override
	public Set<Condition> getConditions() {
		if (!conditionsParsed) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> hibConditions = hibPeptide
					.getConditions();
			if (hibConditions != null) {
				for (edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition : hibConditions) {
					if (ConditionImpl.conditionsMap.containsKey(hibCondition.getId())) {
						conditions.add(ConditionImpl.conditionsMap.get(hibCondition.getId()));
					} else {
						conditions.add(new ConditionImpl(hibCondition));
					}
				}
			}
			conditionsParsed = true;
		}
		return conditions;
	}

	@Override
	public MSRun getMSRun() {
		if (!msRunParsed) {
			final MsRun hibMsRun = hibPeptide.getMsRun();
			if (MSRunImpl.msRuns.containsKey(hibMsRun.getId())) {
				msRun = MSRunImpl.msRuns.get(hibMsRun.getId());
			} else {
				msRun = new MSRunImpl(hibMsRun);
			}
			msRunParsed = true;
		}
		return msRun;
	}

	@Override
	public void addRatio(Ratio ratio) {
		if (ratio != null && !ratios.contains(ratio))
			ratios.add(ratio);
	}

	@Override
	public void addCondition(Condition condition) {
		if (condition != null && !conditions.contains(condition))
			conditions.add(condition);

	}

	@Override
	public void addScore(Score score) {
		if (score != null && !scores.contains(score)) {
			scores.add(score);
		}

	}

	@Override
	public void addAmount(Amount amount) {
		if (amount != null && !amounts.contains(amount))
			amounts.add(amount);
	}

	@Override
	public void addPSM(PSM psm) {
		if (psm != null && !psms.contains(psm)) {
			psms.add(psm);
			psm.setPeptide(this);
			Set<Protein> proteins2 = psm.getProteins();
			for (Protein protein : proteins2) {
				addProtein(protein);
				protein.addPeptide(this);
			}
			for (Protein protein : getProteins()) {
				protein.addPSM(psm);
			}
		}

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void addProtein(Protein protein) {
		if (protein != null && !proteins.contains(protein)) {
			proteins.add(protein);
			protein.addPeptide(this);
			for (PSM psm : getPSMs()) {
				psm.addProtein(protein);
				protein.addPSM(psm);
			}
		}

	}

}
