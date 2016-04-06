package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PeptideRelation;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.ProteomicsModelStaticStorage;

public class PSMEx implements PSM, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1713998292042049510L;
	private final String psmID;
	private MSRun msRun;
	private final String fullSequence;
	private final String sequence;
	private List<PTM> ptms;
	private Double experimentalMH;
	private Double calculatedMH;
	private Double massErrorPPM;
	private Double totalIntensity;
	private Integer spr;
	private Double ionProportion;
	private Double pi;
	private Set<Score> scores;
	private final HashSet<Amount> amounts = new HashSet<Amount>();;
	private final Set<Ratio> ratios = new HashSet<Ratio>();
	private final Set<Protein> proteins = new HashSet<Protein>();
	private PeptideRelation relation;
	private Peptide peptide;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private String afterSeq;
	private String beforeSeq;
	private final Logger log = Logger.getLogger(PSMEx.class);
	private String chargeState;
	private String scanNumber;

	public PSMEx(String psmID, String sequence, String fullSequence) {
		this.psmID = psmID;
		this.sequence = sequence;
		this.fullSequence = fullSequence;
	}

	@Override
	public String getPSMIdentifier() {
		return psmID;
	}

	@Override
	public Double getExperimentalMH() {
		return experimentalMH;
	}

	@Override
	public Double getCalcMH() {
		return calculatedMH;
	}

	@Override
	public Double getMassErrorPPM() {
		return massErrorPPM;
	}

	@Override
	public Double getTotalIntensity() {
		return totalIntensity;
	}

	@Override
	public Integer getSPR() {
		return spr;
	}

	@Override
	public Double getIonProportion() {
		return ionProportion;
	}

	@Override
	public Double getPI() {
		return pi;
	}

	@Override
	public String getFullSequence() {
		return fullSequence;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	@Override
	public List<PTM> getPTMs() {
		return ptms;
	}

	@Override
	public MSRun getMSRun() {
		return msRun;
	}

	/**
	 * @param ptms
	 *            the ptms to set
	 */
	public void setPtms(List<PTM> ptms) {
		this.ptms = ptms;
	}

	public void addPtm(PTM newPtm) {
		if (ptms == null)
			ptms = new ArrayList<PTM>();
		boolean found = false;
		for (PTM ptm : ptms) {
			if (ptm.getName().equals(newPtm.getName())) {
				boolean anyPtmIsNew = false;
				for (PTMSite newPtmSite : newPtm.getPTMSites()) {
					boolean ptmSiteFound = false;
					for (PTMSite ptmSite : ptm.getPTMSites()) {
						if (newPtmSite.getPosition() == ptmSite.getPosition()) {
							ptmSiteFound = true;
						}
					}
					if (!ptmSiteFound) {
						anyPtmIsNew = true;
					}
				}
				if (!anyPtmIsNew)
					found = true;
			}
		}
		if (!found) {
			ptms.add(newPtm);
		}
	}

	/**
	 * @param experimentalMH
	 *            the experimentalMH to set
	 */
	public void setExperimentalMH(Double experimentalMH) {
		this.experimentalMH = experimentalMH;
	}

	/**
	 * @param calculatedMH
	 *            the calculatedMH to set
	 */
	public void setCalculatedMH(Double calculatedMH) {
		this.calculatedMH = calculatedMH;
	}

	/**
	 * @param massErrorPPM
	 *            the massErrorPPM to set
	 */
	public void setMassErrorPPM(Double massErrorPPM) {
		this.massErrorPPM = massErrorPPM;
	}

	/**
	 * @param totalIntensity
	 *            the totalIntensity to set
	 */
	public void setTotalIntensity(Double totalIntensity) {
		this.totalIntensity = totalIntensity;
	}

	/**
	 * @param spr
	 *            the spr to set
	 */
	public void setSpr(Integer spr) {
		this.spr = spr;
	}

	/**
	 * @param ionProportion
	 *            the ionProportion to set
	 */
	public void setIonProportion(Double ionProportion) {
		this.ionProportion = ionProportion;
	}

	/**
	 * @param pi
	 *            the pi to set
	 */
	public void setPi(Double pi) {
		this.pi = pi;
	}

	@Override
	public Set<Score> getScores() {
		return scores;
	}

	public void setScores(Set<Score> scores) {
		this.scores = scores;
	}

	@Override
	public void addScore(Score score) {
		if (scores == null)
			scores = new HashSet<Score>();
		scores.add(score);
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public void addRatio(Ratio ratio) {
		for (Ratio ratio2 : ratios) {
			if (ratio2.equals(ratio))
				return;
		}
		ratios.add(ratio);
	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public void addAmount(Amount amount) {
		amounts.add(amount);
	}

	@Override
	public Set<Protein> getProteins() {
		return proteins;
	}

	@Override
	public void addProtein(Protein protein) {
		if (!proteins.contains(protein)) {
			proteins.add(protein);
		}
		// else {
		// log.info("Protein already present on PSM");
		// }
	}

	@Override
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;

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
	public void setPeptide(Peptide peptide) {
		this.peptide = peptide;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	/**
	 * Add a new condition just in case it is not already in the list...checking
	 * the name and the project
	 *
	 * @param newCondition
	 */
	@Override
	public void addCondition(Condition newCondition) {
		boolean found = false;
		for (Condition condition : conditions) {
			if (condition.getName().equals(newCondition.getName())) {
				if (condition.getProject().getName().equals(newCondition.getProject().getName()))
					found = true;
			}

		}
		if (!found)
			conditions.add(newCondition);
	}

	@Override
	public int getDBId() {
		return -1;
	}

	@Override
	public String getAfterSeq() {
		return afterSeq;
	}

	@Override
	public String getBeforeSeq() {
		return beforeSeq;
	}

	/**
	 * @param afterSeq
	 *            the afterSeq to set
	 */
	public void setAfterSeq(String afterSeq) {
		this.afterSeq = afterSeq;
	}

	/**
	 * @param beforeSeq
	 *            the beforeSeq to set
	 */
	public void setBeforeSeq(String beforeSeq) {
		this.beforeSeq = beforeSeq;
	}

	@Override
	public PeptideRelation getRelation() {
		return relation;
	}

	@Override
	public String toString() {
		return getFullSequence() + " in run: " + getMSRun().getRunId();
	}

	@Override
	public List<GroupableProtein> getGroupableProteins() {
		List<GroupableProtein> ret = new ArrayList<GroupableProtein>();
		ret.addAll(getProteins());
		return ret;
	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public String getChargeState() {
		return chargeState;
	}

	/**
	 * @param chargeState
	 *            the chargeState to set
	 */
	public void setChargeState(String chargeState) {
		this.chargeState = chargeState;
	}

	@Override
	public String getScanNumber() {
		return scanNumber;
	}

	public void setScanNumber(String scan) {
		scanNumber = scan;
	}

}
