package edu.scripps.yates.utilities.model.factories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

public class ProteinEx implements Protein, Serializable, GroupableProtein {
	private static final Logger log = Logger.getLogger(ProteinEx.class);
	/**
	 *
	 */
	private static final long serialVersionUID = -1435542806814270031L;
	protected final Set<Gene> genes = new HashSet<Gene>();
	private final Set<Amount> proteinAmounts = new HashSet<Amount>();
	private Set<ProteinAnnotation> proteinAnnotations;
	private final Set<Ratio> ratios = new HashSet<Ratio>();
	private Set<Threshold> thresholds;
	private final Set<PSM> psms = new HashSet<PSM>();
	private final Set<Peptide> peptides = new HashSet<Peptide>();
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private int length;
	private double pi;
	private double mw;
	protected Accession primaryAccession;
	private String sequence;
	private int dbID = 0;
	private ProteinEvidence evidence;
	private Set<Score> scores = new HashSet<Score>();
	private ProteinGroup proteinGroup;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private MSRun msrun;
	protected Organism organism;
	protected static int numInstances = 0;

	public ProteinEx(AccessionType accessionType, String accession, Organism organism) {
		Set<Accession> set = new HashSet<Accession>();
		final AccessionEx accessionEx = new AccessionEx(accession, accessionType);
		set.add(accessionEx);
		primaryAccession = accessionEx;
		this.organism = organism;
		numInstances++;
		log.debug(numInstances + " proteinEx");
	}

	public ProteinEx(AccessionType accessionType, String accession) {
		this(accessionType, accession, null);
	}

	public ProteinEx(Accession accession) {
		this(accession.getAccessionType(), accession.getAccession());
	}

	@Override
	public Set<Gene> getGenes() {
		return genes;
	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		return proteinAnnotations;
	}

	@Override
	public Set<Amount> getAmounts() {
		return proteinAmounts;
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public Set<Threshold> getThresholds() {
		return thresholds;
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		final Set<Threshold> thresholds2 = getThresholds();
		for (Threshold threshold : thresholds2) {
			if (threshold.getName().equals(thresholdName))
				return threshold.isPassThreshold();
		}
		return null;
	}

	@Override
	public Set<PSM> getPSMs() {
		return psms;
	}

	public void addSecondaryAccession(AccessionType accessionType, String accession) {
		final AccessionEx newAccession = new AccessionEx(accession, accessionType);
		secondaryAccessions.add(newAccession);
	}

	public void addSecondaryAccession(Accession accession) {
		secondaryAccessions.add(accession);
	}

	@Override
	public void addGene(Gene entrezGene) {
		genes.add(entrezGene);
	}

	@Override
	public void addAmount(Amount proteinAmount) {

		proteinAmounts.add(proteinAmount);
	}

	/**
	 * @param proteinAnnotations
	 *            the proteinAnnotations to set
	 */
	public void setProteinAnnotations(Set<ProteinAnnotation> proteinAnnotations) {
		this.proteinAnnotations = proteinAnnotations;
	}

	public void addProteinAnnotation(ProteinAnnotation proteinAnnotation) {
		if (proteinAnnotations == null)
			proteinAnnotations = new HashSet<ProteinAnnotation>();
		proteinAnnotations.add(proteinAnnotation);
	}

	public void addProteinThreshold(Threshold threshold) {
		if (thresholds == null)
			thresholds = new HashSet<Threshold>();
		thresholds.add(threshold);
	}

	@Override
	public void addRatio(Ratio proteinRatio) {
		ratios.add(proteinRatio);
	}

	/**
	 * @param thresholds
	 *            the thresholds to set
	 */
	public void setThresholds(Set<Threshold> thresholds) {
		this.thresholds = thresholds;
	}

	public void addThreshold(Threshold threshold) {
		if (thresholds == null)
			thresholds = new HashSet<Threshold>();
		thresholds.add(threshold);
	}

	@Override
	public void addPSM(PSM psm) {

		if (psm != null && !psms.contains(psm))
			psms.add(psm);
		// else
		// log.info("PSM already in the list of the protein");

	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		return secondaryAccessions;
	}

	@Override
	public int getDBId() {
		return dbID;
	}

	public void setDBId(int id) {
		dbID = id;
	}

	@Override
	public Accession getPrimaryAccession() {
		return primaryAccession;
	}

	@Override
	public String getAccession() {
		return getPrimaryAccession().getAccession();
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public double getPi() {
		return pi;
	}

	@Override
	public double getMW() {
		return mw;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	@Override
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param pi
	 *            the pi to set
	 */
	@Override
	public void setPi(double pi) {
		this.pi = pi;
	}

	/**
	 * @param mw
	 *            the mw to set
	 */
	@Override
	public void setMw(double mw) {
		this.mw = mw;
	}

	/**
	 * @param primaryAccession
	 *            the primaryAccession to set
	 */
	public void setPrimaryAccession(Accession primaryAccession) {
		this.primaryAccession = primaryAccession;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		this.proteinGroup = proteinGroup;

	}

	@Override
	public Set<Peptide> getPeptides() {
		return peptides;
	}

	@Override
	public void addPeptide(Peptide peptide) {
		if (!peptides.contains(peptide))
			peptides.add(peptide);
		// else
		// log.info("Peptide already in the list of the protein");
	}

	@Override
	public Organism getOrganism() {
		return organism;
	}

	@Override
	public void setOrganism(Organism organism) {
		this.organism = organism;
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
		scores.add(score);
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return proteinGroup;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	@Override
	public void addCondition(Condition condition) {
		conditions.add(condition);
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
	public List<GroupablePSM> getGroupablePSMs() {
		List<GroupablePSM> list = new ArrayList<GroupablePSM>();
		final Set<PSM> psMs2 = getPSMs();
		for (PSM psm : psMs2) {
			if (psm instanceof GroupablePSM)
				list.add(psm);
		}
		return list;
	}

	@Override
	public String toString() {
		String msRunId = null;
		if (getMSRun() != null) {
			msRunId = getMSRun().getRunId();
		}
		return getAccession() + " in run: " + msRunId;
	}

}
