package edu.scripps.yates.model.util;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import gnu.trove.set.hash.THashSet;

public class PeptideImplFromProteinsAndPSMs implements Peptide {
	private final Set<PSM> psms = new THashSet<PSM>();
	private final Set<Protein> proteins = new THashSet<Protein>();
	private final Set<Amount> amounts = new THashSet<Amount>();
	private MSRun msrun;
	private final Set<Condition> conditions = new THashSet<Condition>();
	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private final Set<Score> scores = new THashSet<Score>();
	private boolean conditionsParsed = false;

	public PeptideImplFromProteinsAndPSMs(List<Protein> proteins, Collection<PSM> psms, MSRun msrun) {
		for (PSM psm : psms) {
			this.psms.add(psm);
			psm.setPeptide(this);
		}
		if (proteins != null) {
			for (Protein protein : proteins) {
				// this.proteins.add(protein);
				protein.addPeptide(this);
			}
		}
		this.msrun = msrun;
	}

	@Override
	public int getDBId() {
		return -1;
	}

	@Override
	public Set<PSM> getPSMs() {
		return psms;
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public Set<Score> getScores() {
		return scores;
	}

	@Override
	public String getSequence() {
		return psms.iterator().next().getFullSequence();
	}

	@Override
	public Set<Protein> getProteins() {
		return proteins;
	}

	@Override
	public Set<Condition> getConditions() {
		if (!conditionsParsed) {

			for (PSM psm : psms) {
				final Set<Condition> conditions = psm.getConditions();
				for (Condition condition : conditions) {
					if (!conditions.contains(condition))
						conditions.add(condition);
				}

			}
			conditionsParsed = true;
		}
		return conditions;
	}

	@Override
	public MSRun getMSRun() {
		return msrun;
	}

	@Override
	public void setMSRun(MSRun msrun) {
		this.msrun = msrun;
	}

	@Override
	public void addProtein(Protein protein) {

		if (!proteins.contains(protein))
			proteins.add(protein);
	}

	@Override
	public void addPSM(PSM psm) {

		if (!psms.contains(psm))
			psms.add(psm);
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
	public void addScore(Score score) {
		if (!scores.contains(score))
			scores.add(score);

	}

}
