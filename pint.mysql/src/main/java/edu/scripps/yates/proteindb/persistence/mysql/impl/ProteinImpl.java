package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
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

public class ProteinImpl implements Protein {
	private static HashMap<Integer, Amount> proteinAmountMap = new HashMap<Integer, Amount>();
	private static HashMap<Integer, Ratio> proteinRatiosMap = new HashMap<Integer, Ratio>();
	protected static HashMap<Integer, Protein> proteinMap = new HashMap<Integer, Protein>();
	private final static HashMap<Integer, Score> scoreMap = new HashMap<Integer, Score>();

	private final edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein;
	private final Set<Gene> genes = new HashSet<Gene>();
	private Set<ProteinAnnotation> annotations;
	private final Set<Amount> amounts = new HashSet<Amount>();

	private final Set<Ratio> ratios = new HashSet<Ratio>();
	private Set<Threshold> thresholds;
	private final Set<PSM> psms = new HashSet<PSM>();
	private final Set<Peptide> peptides = new HashSet<Peptide>();
	private List<Accession> secondaryAccessions;
	private ProteinEvidence evidence;
	private ProteinGroup group;
	private Accession primaryAcc;
	private final Set<Score> scores = new HashSet<Score>();
	private boolean proteinAmountsParsed = false;
	private boolean conditionsParsed = false;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private boolean proteinRatiosParsed = false;
	private boolean psmsParsed = false;
	private Organism organism;
	private boolean parsedOrganism = false;
	private boolean parsedScores = false;
	private MSRun msRun;
	private boolean parsedMSRun = false;
	private double mw;
	private double pi;
	private int length;
	private boolean genesParsed = false;
	private boolean piParsed = false;
	private boolean mwParsed = false;
	private boolean lengthParsed = false;

	public ProteinImpl(edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein) {
		this.hibProtein = hibProtein;
		proteinMap.put(hibProtein.getId(), this);
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Gene> entrezGenes = hibProtein.getGenes();
			for (edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene : entrezGenes) {
				if (GeneImpl.genesMap.containsKey(hibGene.getGeneId())) {
					genes.add(GeneImpl.genesMap.get(hibGene.getGeneId()));
				} else {
					genes.add(new GeneImpl(hibGene));
				}
			}
			genesParsed = true;
		}
		return genes;
	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		if (annotations == null) {
			annotations = new HashSet<ProteinAnnotation>();
			final Set<edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation> proteinAnnotations = hibProtein
					.getProteinAnnotations();
			for (edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation proteinAnnotation : proteinAnnotations) {
				if (ProteinAnnotationImpl.proteinAnnotationsMap.containsKey(proteinAnnotation.getId())) {
					annotations.add(ProteinAnnotationImpl.proteinAnnotationsMap.get(proteinAnnotation.getId()));
				} else {
					annotations.add(new ProteinAnnotationImpl(proteinAnnotation));
				}
			}
		}
		return annotations;
	}

	@Override
	public Set<Amount> getAmounts() {
		if (!proteinAmountsParsed) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount> hibProteinAmounts = hibProtein
					.getProteinAmounts();

			for (edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount hibProteinAmount : hibProteinAmounts) {
				Amount proteinAmountImpl = null;
				if (ProteinImpl.proteinAmountMap.containsKey(hibProteinAmount.getId())) {
					proteinAmountImpl = ProteinImpl.proteinAmountMap.get(hibProteinAmount.getId());
				} else {
					proteinAmountImpl = new AmountImpl(hibProteinAmount);
					ProteinImpl.proteinAmountMap.put(hibProteinAmount.getId(), proteinAmountImpl);
				}
				amounts.add(proteinAmountImpl);
			}
			proteinAmountsParsed = true;
		}
		return amounts;
	}

	@Override
	public Set<Ratio> getRatios() {
		if (!proteinRatiosParsed) {
			final Set<ProteinRatioValue> proteinRatioValues = hibProtein.getProteinRatioValues();
			if (proteinRatioValues != null) {
				for (ProteinRatioValue proteinRatioValue : proteinRatioValues) {
					if (proteinRatiosMap.containsKey(proteinRatioValue.getId())) {
						ratios.add(proteinRatiosMap.get(proteinRatioValue.getId()));
					} else {
						final RatioImpl proteinRatioImpl = new RatioImpl(proteinRatioValue);
						proteinRatiosMap.put(proteinRatioValue.getId(), proteinRatioImpl);
						ratios.add(proteinRatioImpl);
					}
				}
			}
			proteinRatiosParsed = true;
		}
		return ratios;
	}

	@Override
	public Set<Threshold> getThresholds() {
		if (thresholds == null) {
			thresholds = new HashSet<Threshold>();
			final Set<ProteinThreshold> proteinThresholds = hibProtein.getProteinThresholds();

			for (ProteinThreshold hibProteinThreshold : proteinThresholds) {
				if (ThresholdImpl.thresholdsMap.containsKey(hibProteinThreshold.getId())) {
					thresholds.add(ThresholdImpl.thresholdsMap.get(hibProteinThreshold.getId()));
				} else {
					final Threshold thresholdImpl = new ThresholdImpl(hibProteinThreshold);
					thresholds.add(thresholdImpl);
				}
			}
		}
		return thresholds;
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		final Set<Threshold> thresholds2 = getThresholds();
		for (Threshold threshold2 : thresholds2) {
			if (threshold2.getName().equals(thresholdName))
				return threshold2.isPassThreshold();
		}
		return null;
	}

	@Override
	public Set<PSM> getPSMs() {
		if (!psmsParsed) {
			final Set<Psm> hibPSMs = hibProtein.getPsms();
			for (Psm hibPSM : hibPSMs) {
				if (PSMImpl.psmMap.containsKey(hibPSM.getId())) {
					psms.add(PSMImpl.psmMap.get(hibPSM.getId()));
				} else {
					final PSMImpl psmImpl = new PSMImpl(hibPSM);
					psms.add(psmImpl);
					psmImpl.getProteins().add(this);
				}
			}
			psmsParsed = true;
		}
		return psms;
	}

	@Override
	public Set<Peptide> getPeptides() {
		if (peptides == null) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Peptide> hibPeptides = hibProtein.getPeptides();
			for (edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide : hibPeptides) {
				if (PeptideImpl.peptideMap.containsKey(hibPeptide.getId())) {
					peptides.add(PeptideImpl.peptideMap.get(hibPeptide.getId()));
				} else {
					final PeptideImpl peptideImpl = new PeptideImpl(hibPeptide);
					peptides.add(peptideImpl);
					// peptideImpl.getProteins().add(this);
				}
			}
		}
		return peptides;
	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		if (secondaryAccessions == null) {
			secondaryAccessions = new ArrayList<Accession>();
			final Set<ProteinAccession> proteinAccessions = hibProtein.getProteinAccessions();
			for (ProteinAccession proteinAccession : proteinAccessions) {
				if (proteinAccession.isIsPrimary())
					continue;
				Accession accession = new AccessionImpl(proteinAccession);
				secondaryAccessions.add(accession);
			}
		}

		return secondaryAccessions;
	}

	@Override
	public int getDBId() {
		return hibProtein.getId();
	}

	@Override
	public Accession getPrimaryAccession() {
		if (primaryAcc == null) {
			final Set<ProteinAccession> accessions2 = hibProtein.getProteinAccessions();
			if (accessions2 != null) {
				for (ProteinAccession proteinAccession : accessions2) {
					final boolean isPrimary = proteinAccession.isIsPrimary();
					if (isPrimary) {
						if (AccessionImpl.accessionMap.containsKey(proteinAccession.getAccession()))
							primaryAcc = AccessionImpl.accessionMap.get(proteinAccession.getAccession());
						else
							primaryAcc = new AccessionImpl(proteinAccession);
					}
				}
				if (primaryAcc == null) {
					for (ProteinAccession proteinAccession : accessions2) {
						if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.toString())) {
							if (AccessionImpl.accessionMap.containsKey(proteinAccession.getAccession()))
								primaryAcc = AccessionImpl.accessionMap.get(proteinAccession.getAccessionType());
							else
								primaryAcc = new AccessionImpl(proteinAccession);
						}
					}
				}
			}
		}
		return primaryAcc;
	}

	@Override
	public String getAccession() {
		return getPrimaryAccession().getAccession();

	}

	@Override
	public int getLength() {
		if (!lengthParsed) {
			length = hibProtein.getLength();
			lengthParsed = true;
		}
		return length;
	}

	@Override
	public double getPi() {
		if (!piParsed) {
			pi = hibProtein.getPi();
			piParsed = true;
		}
		return pi;
	}

	@Override
	public double getMW() {
		if (!mwParsed) {
			mw = hibProtein.getMw();
			mwParsed = true;
		}
		return mw;
	}

	@Override
	public String getSequence() {
		return null;
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
	public Organism getOrganism() {
		if (!parsedOrganism) {
			if (hibProtein.getOrganism() != null) {
				if (OrganismImpl.organismMap.containsKey(hibProtein.getOrganism().getTaxonomyId())) {
					organism = OrganismImpl.organismMap.get(hibProtein.getOrganism().getTaxonomyId());
				} else {
					organism = new OrganismImpl(hibProtein.getOrganism());
				}
			}
			parsedOrganism = true;
		}
		return organism;
	}

	@Override
	public Set<Score> getScores() {
		if (!parsedScores) {
			final Set<ProteinScore> hibScores = hibProtein.getProteinScores();
			for (ProteinScore hibScore : hibScores) {
				if (scoreMap.containsKey(hibScore.getId())) {
					scores.add(scoreMap.get(hibScore.getId()));
				} else {
					final ScoreImpl scoreImpl = new ScoreImpl(hibScore);
					scoreMap.put(hibScore.getId(), scoreImpl);
					scores.add(scoreImpl);
				}
			}
			parsedScores = true;
		}
		return scores;
	}

	@Override
	public ProteinEvidence getEvidence() {

		return evidence;
	}

	@Override
	public ProteinGroup getProteinGroup() {

		return group;
	}

	@Override
	public Set<Condition> getConditions() {
		if (!conditionsParsed) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditionsTMP = hibProtein
					.getConditions();
			if (conditionsTMP != null) {
				for (edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition : conditionsTMP) {
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
		if (!parsedMSRun) {
			final MsRun hibMSRun = hibProtein.getMsRun();
			if (MSRunImpl.msRuns.containsKey(hibMSRun.getId())) {
				msRun = MSRunImpl.msRuns.get(hibMSRun.getId());
			} else {
				msRun = new MSRunImpl(hibMSRun);
			}
			parsedMSRun = true;
		}
		return msRun;
	}

	@Override
	public List<GroupablePSM> getGroupablePSMs() {
		List<GroupablePSM> ret = new ArrayList<GroupablePSM>();
		ret.addAll(getPSMs());
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
		this.length = length;

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
