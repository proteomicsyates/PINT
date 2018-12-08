package edu.scripps.yates.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
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
import gnu.trove.set.hash.THashSet;

public class AnnotatedProteinImpl implements Protein {
	private final Protein protein;
	private final Set<ProteinAnnotation> annotations = new THashSet<ProteinAnnotation>();
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private final Protein annotatedProtein;
	private final Accession primaryAccession;
	private ProteinGroup group;
	private ProteinEvidence evidence;
	private final Set<Amount> amounts = new THashSet<Amount>();
	private boolean amountsParsed = false;
	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private boolean ratiosParsed = false;
	private final Set<Condition> conditions = new THashSet<Condition>();
	private boolean conditionsParsed = false;
	private final Set<Score> scores = new THashSet<Score>();
	private boolean scoresParsed = false;
	private final Set<PSM> psms = new THashSet<PSM>();
	private boolean psmsParsed = false;
	private Organism organism;
	private MSRun msRun;
	private double mw;
	private double pi;
	private int lentgh;
	private final Set<Peptide> peptides = new THashSet<Peptide>();
	private boolean peptidesParsed = false;
	private final Set<Gene> genes = new THashSet<Gene>();
	private boolean genesParsed = false;
	private boolean lengthParsed = false;
	private boolean piParsed = false;
	private boolean mwParsed = false;

	public AnnotatedProteinImpl(Protein protein, Protein annotatedProtein) {
		this.protein = protein;
		this.annotatedProtein = annotatedProtein;
		annotations.addAll(protein.getAnnotations());
		annotations.addAll(annotatedProtein.getAnnotations());
		primaryAccession = annotatedProtein.getPrimaryAccession();
	}

	public AnnotatedProteinImpl(Protein protein, Protein annotatedProtein, boolean includeUniprotAnnotation) {
		this.protein = protein;
		this.annotatedProtein = annotatedProtein;
		annotations.addAll(protein.getAnnotations());
		if (includeUniprotAnnotation)
			annotations.addAll(annotatedProtein.getAnnotations());
		primaryAccession = annotatedProtein.getPrimaryAccession();
	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		if (secondaryAccessions.isEmpty()) {
			// get first the accessions from the annotated Protein
			// in order to get the accessions annotated with the primary
			// accession property
			final List<Accession> accessionSetFromAnnotated = annotatedProtein.getSecondaryAccessions();
			for (Accession accessionFromAnnotated : accessionSetFromAnnotated) {
				addToSecondaryAccessions(accessionFromAnnotated);
			}

			// get the accesssions from the other protein instance
			final List<Accession> accessionSet = protein.getSecondaryAccessions();
			for (Accession accessionFrom : accessionSet) {
				addToSecondaryAccessions(accessionFrom);
			}

			// add the primary accession to the secondary ones if different from
			// the actual primary
			final Accession primaryAccession2 = protein.getPrimaryAccession();
			if (!primaryAccession2.equals(annotatedProtein.getAccession())) {
				addToSecondaryAccessions(primaryAccession2);
			}
		}
		return secondaryAccessions;
	}

	private void addToSecondaryAccessions(Accession acc) {
		boolean found = false;
		for (Accession acc2 : secondaryAccessions) {
			if (acc2.getAccession().equals(acc.getAccession()))
				found = true;
		}
		if (!found)
			secondaryAccessions.add(acc);
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			if (protein.getGenes() != null)
				genes.addAll(protein.getGenes());
			if (annotatedProtein.getGenes() != null)
				genes.addAll(annotatedProtein.getGenes());
			genesParsed = true;
		}

		return genes;
	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		return annotations;
	}

	@Override
	public Set<Amount> getAmounts() {
		if (!amountsParsed) {
			amounts.addAll(protein.getAmounts());
			amountsParsed = true;
		}
		return amounts;
	}

	@Override
	public Set<Ratio> getRatios() {
		if (!ratiosParsed) {
			ratios.addAll(protein.getRatios());
			ratiosParsed = true;
		}
		return ratios;
	}

	@Override
	public Set<Threshold> getThresholds() {
		return protein.getThresholds();
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		return protein.passThreshold(thresholdName);
	}

	@Override
	public Set<PSM> getPSMs() {
		if (!psmsParsed) {
			psms.addAll(protein.getPSMs());
			psmsParsed = true;
		}
		return psms;
	}

	@Override
	public int getUniqueID() {
		return protein.getUniqueID();
	}

	@Override
	public Accession getPrimaryAccession() {
		return primaryAccession;
	}

	@Override
	public String getAccession() {
		return primaryAccession.getAccession();
	}

	@Override
	public int getLength() {
		if (!lengthParsed) {
			if (protein.getLength() > 0)
				lentgh = protein.getLength();
			else
				lentgh = annotatedProtein.getLength();
			lengthParsed = true;
		}
		return lentgh;
	}

	@Override
	public double getPi() {
		if (!piParsed) {
			if (protein.getPi() != 0.0)
				pi = protein.getPi();
			else
				pi = annotatedProtein.getPi();
			piParsed = true;
		}
		return pi;
	}

	@Override
	public double getMW() {
		if (mwParsed) {
			if (protein.getMW() != 0.0)
				mw = protein.getMW();
			else
				mw = annotatedProtein.getMW();
			mwParsed = true;
		}
		return mw;
	}

	@Override
	public String getSequence() {
		return annotatedProtein.getSequence();
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		group = proteinGroup;

	}

	@Override
	public Set<Peptide> getPeptides() {
		if (!peptidesParsed) {
			peptides.addAll(protein.getPeptides());
			peptidesParsed = true;
		}
		return peptides;
	}

	@Override
	public Organism getOrganism() {
		// get the organism from the annotated protein if possible
		if (annotatedProtein.getOrganism() != null)
			return annotatedProtein.getOrganism();

		return protein.getOrganism();
	}

	@Override
	public Set<Score> getScores() {
		if (!scoresParsed) {
			scores.addAll(protein.getScores());
			scoresParsed = true;
		}
		return scores;

	}

	@Override
	public ProteinEvidence getEvidence() {
		return null;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return null;
	}

	@Override
	public Set<Condition> getConditions() {
		if (!conditionsParsed) {
			if (protein.getConditions() != null)
				conditions.addAll(protein.getConditions());
			if (annotatedProtein.getConditions() != null)
				conditions.addAll(annotatedProtein.getConditions());
			conditionsParsed = true;
		}
		return conditions;
	}

	@Override
	public MSRun getMSRun() {
		final MSRun msRun = annotatedProtein.getMSRun();
		if (msRun != null)
			return msRun;
		final MSRun msRun2 = protein.getMSRun();
		if (msRun2 != null) {
			return msRun2;
		}
		return null;
	}

	@Override
	public List<GroupablePeptide> getGroupablePeptides() {
		List<GroupablePeptide> ret = new ArrayList<GroupablePeptide>();
		ret.addAll(getPSMs());
		return ret;
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
	public void addPSM(PSM psm) {
		if (!psms.contains(psm))
			psms.add(psm);

	}

	@Override
	public void setOrganism(Organism organism) {
		this.organism = organism;

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void setMw(double mw) {
		this.mw = mw;

	}

	@Override
	public void setPi(double pi) {
		this.pi = pi;

	}

	@Override
	public void setLength(int length) {
		lentgh = length;

	}

	@Override
	public void addPeptide(Peptide peptide) {
		if (!peptides.contains(peptide))
			peptides.add(peptide);
	}

	@Override
	public void addGene(Gene gene) {
		if (!genes.contains(gene))
			genes.add(gene);
	}

}
