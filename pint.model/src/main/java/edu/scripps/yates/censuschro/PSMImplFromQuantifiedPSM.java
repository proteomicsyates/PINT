package edu.scripps.yates.censuschro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.cv.CommonlyUsedCV;
import edu.scripps.yates.model.util.PTMAdapter;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.model.factories.ScoreEx;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.util.StringPosition;
import gnu.trove.set.hash.THashSet;

public class PSMImplFromQuantifiedPSM implements PSM {

	// public static Map<QuantifiedPSMInterface, PSM> map = new
	// HashMap<QuantifiedPSMInterface, PSM>();
	private final Set<Protein> proteins = new THashSet<Protein>();
	private final QuantifiedPSMInterface quantPSM;
	private MSRun msRun;
	private final Set<Condition> conditions = new THashSet<Condition>();
	private PeptideRelation relation;
	private Peptide peptide;
	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private final Set<Amount> amounts = new THashSet<Amount>();
	private final Set<Score> scores = new THashSet<Score>();
	private boolean scoresParsed = false;
	private IsobaricQuantifiedPSM censusChroQuantifiedPSM;
	private static final String psmScoreName = CVManager
			.getPreferredName(CommonlyUsedCV.SEARCH_ENGINE_SPECIFIC_SCORE_FOR_PSM);
	private static final String xcorrName = CVManager.getPreferredName(CommonlyUsedCV.XCorrID);
	private static final String deltaCnName = CVManager.getPreferredName(CommonlyUsedCV.DeltaCNID);

	public PSMImplFromQuantifiedPSM(QuantifiedPSMInterface quantPSM, MSRun msRun) {

		this.quantPSM = quantPSM;
		censusChroQuantifiedPSM = null;
		if (quantPSM instanceof IsobaricQuantifiedPSM) {
			censusChroQuantifiedPSM = (IsobaricQuantifiedPSM) quantPSM;
		}
		this.msRun = msRun;
		// map.put(quantPSM, this);
	}

	@Override
	public Set<Score> getScores() {
		if (!scoresParsed) {

			if (censusChroQuantifiedPSM != null && censusChroQuantifiedPSM.getDeltaCN() != null) {
				Score score = new ScoreEx(censusChroQuantifiedPSM.getDeltaCN().toString(), deltaCnName, psmScoreName,
						null);
				scores.add(score);
			}

			if (censusChroQuantifiedPSM != null && censusChroQuantifiedPSM.getXcorr() != null) {
				Score score = new ScoreEx(censusChroQuantifiedPSM.getXcorr().toString(), xcorrName, psmScoreName, null);
				scores.add(score);
			}
			scoresParsed = true;
		}
		return scores;
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
	public PeptideRelation getRelation() {
		return relation;
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		return quantPSM.getGroupableProteins();
	}

	@Override
	public String getPSMIdentifier() {
		return quantPSM.getPSMIdentifier();
	}

	@Override
	public Double getExperimentalMH() {
		if (quantPSM.getMHplus() != null)
			return Double.valueOf(quantPSM.getMHplus().toString());
		return null;
	}

	@Override
	public Double getCalcMH() {
		if (quantPSM.getCalcMHplus() != null)
			return Double.valueOf(quantPSM.getCalcMHplus().toString());
		return null;
	}

	@Override
	public Double getMassErrorPPM() {
		if (censusChroQuantifiedPSM != null && censusChroQuantifiedPSM.getDeltaMass() != null)
			return Double.valueOf(censusChroQuantifiedPSM.getDeltaMass().toString());
		return null;
	}

	@Override
	public Double getTotalIntensity() {
		if (censusChroQuantifiedPSM != null && censusChroQuantifiedPSM.getTotalIntensity() != null)
			return Double.valueOf(censusChroQuantifiedPSM.getTotalIntensity().toString());
		return null;
	}

	@Override
	public Integer getSPR() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getIonProportion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getPI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullSequence() {

		return quantPSM.getFullSequence();
	}

	@Override
	public String getSequence() {
		return FastaParser.cleanSequence(getFullSequence());
	}

	@Override
	public List<PTM> getPTMs() {
		List<PTM> ret = new ArrayList<PTM>();
		final List<StringPosition> inside = FastaParser.getInside(getSequence());
		for (StringPosition stringPosition : inside) {
			try {
				double massShift = Double.valueOf(stringPosition.string);
				String aa = getSequence().substring(stringPosition.position, 1);
				int position = stringPosition.position;
				final PTM ptm = new PTMAdapter(massShift, aa, position).adapt();
				ret.add(ptm);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	@Override
	public MSRun getMSRun() {
		return msRun;
	}

	@Override
	public Set<Protein> getProteins() {
		return proteins;
	}

	@Override
	public Peptide getPeptide() {
		if (peptide == null) {
			if (StaticProteomicsModelStorage.containsPeptide(msRun, null, getSequence())) {
				peptide = StaticProteomicsModelStorage.getSinglePeptide(msRun, null, getSequence());
			} else {
				peptide = new PeptideEx(getSequence(), msRun);
				StaticProteomicsModelStorage.addPeptide(peptide, msRun, null);
			}
			peptide.addPSM(this);
		}
		return peptide;
	}

	@Override
	public void setPeptide(Peptide peptide) {
		this.peptide = peptide;
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;

	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	@Override
	public int getDBId() {
		return -1;
	}

	@Override
	public String getAfterSeq() {
		final String fullSequence = getFullSequence();
		final String afterSeq = FastaParser.getAfterSeq(fullSequence);
		return afterSeq;

	}

	@Override
	public String getBeforeSeq() {
		final String fullSequence = getFullSequence();
		final String beforeSeq = FastaParser.getBeforeSeq(fullSequence);
		return beforeSeq;

	}

	@Override
	public void addProtein(Protein protein) {
		if (!proteins.contains(protein))
			proteins.add(protein);

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void addCondition(Condition condition) {
		if (!conditions.contains(condition))
			conditions.add(condition);
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
	public void addScore(Score score) {
		if (!scores.contains(score))
			scores.add(score);

	}

	@Override
	public String getChargeState() {
		if (quantPSM.getCharge() != null)
			return quantPSM.getCharge().toString();
		return null;
	}

	@Override
	public String getScanNumber() {
		return quantPSM.getScan();
	}

}
