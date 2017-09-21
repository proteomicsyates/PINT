package edu.scripps.yates.dtaselect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.util.ProteinExistenceUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.GeneEx;
import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
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
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinImplFromDTASelect implements Protein {
	private static final Logger log = Logger.getLogger(ProteinImplFromDTASelect.class);
	private final DTASelectProtein dtaSelectProtein;
	private Accession primaryAccession;
	private final Set<PSM> psms = new THashSet<PSM>();
	private final Set<Peptide> peptides = new THashSet<Peptide>();
	private ProteinGroup proteinGroup;
	private ProteinEvidence evidence;
	private Organism organism;
	private final Set<Condition> conditions = new THashSet<Condition>();
	private MSRun msrun;
	private final Set<Amount> amounts = new THashSet<Amount>();
	private final Set<Ratio> ratios = new THashSet<Ratio>();
	private final Set<Score> scores = new THashSet<Score>();
	private boolean conditionsParsed = false;
	private boolean psmsParsed = false;
	private double mw;
	private double pi;
	private int length;
	private final Set<Gene> genes = new THashSet<Gene>();
	private boolean genesParsed = false;
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private boolean peptidesParsed;
	private boolean forceAllPSMsToBeFromThisMSRun;
	private static int max = 0;
	private final static Map<String, MSRun> msRunMap = new THashMap<String, MSRun>();

	public ProteinImplFromDTASelect(DTASelectProtein dtaSelectProtein, String msRunID,
			boolean forceAllPSMsToBeFromThisMSRun) {
		this.dtaSelectProtein = dtaSelectProtein;
		if (dtaSelectProtein.getLength() != null)
			length = dtaSelectProtein.getLength();
		mw = dtaSelectProtein.getMw();
		pi = dtaSelectProtein.getPi();
		if (!msRunMap.containsKey(msRunID)) {
			msrun = new MSRunEx(msRunID, null);
			msRunMap.put(msRunID, msrun);
		} else {
			msrun = msRunMap.get(msRunID);
		}
		this.forceAllPSMsToBeFromThisMSRun = forceAllPSMsToBeFromThisMSRun;

		final int length2 = getPrimaryAccession().getAccession().length();
		if (length2 > max) {
			log.debug(getPrimaryAccession().getAccession() + " length=" + length2);
			max = length2;
		}
		createAmounts();
		// create peptide and psm objects
		getPeptides();
	}

	public ProteinImplFromDTASelect(DTASelectProtein dtaSelectProtein2, MSRun msrun2,
			boolean forceAllPSMsToBeFromThisMSRun) {
		dtaSelectProtein = dtaSelectProtein2;
		if (dtaSelectProtein.getLength() != null)
			length = dtaSelectProtein.getLength();
		mw = dtaSelectProtein.getMw();
		pi = dtaSelectProtein.getPi();

		msrun = msrun2;
		msRunMap.put(msrun.getRunId(), msrun);
		this.forceAllPSMsToBeFromThisMSRun = forceAllPSMsToBeFromThisMSRun;
		final int length2 = getPrimaryAccession().getAccession().length();
		if (length2 > max) {
			log.info(getPrimaryAccession().getAccession() + " length=" + length2);
			max = length2;
		}
		createAmounts();
		// create peptide and psm objects
		getPeptides();
	}

	private void createAmounts() {
		Set<Condition> conditions = new THashSet<Condition>();
		conditions.addAll(getConditions());
		if (conditions.isEmpty()) {
			// I just need one element, I dont care if it is null
			conditions.add(null);
		}
		for (Condition condition : conditions) {
			if (this.getNSAF() != null) {
				Amount amount = new AmountEx(getNSAF(), AmountType.NSAF, condition);
				this.amounts.add(amount);
			}
			if (this.getNSAFNorm() >= 0) {
				Amount amount = new AmountEx(getNSAFNorm(), AmountType.NSAF_NORM, condition);
				this.amounts.add(amount);
			}
			if (this.getEMPai() != null) {
				Amount amount = new AmountEx(getEMPai(), AmountType.EMPAI, condition);
				this.amounts.add(amount);
			}
			if (this.getEMPaiCov() >= 0) {
				Amount amount = new AmountEx(getEMPaiCov(), AmountType.EMPAI_COV, condition);
				this.amounts.add(amount);
			}
		}
	}

	@Override
	public int getDBId() {
		return hashCode();
	}

	@Override
	public Accession getPrimaryAccession() {

		if (primaryAccession == null) {
			primaryAccession = ProteinDTASelectParser.getProteinAccessionFromDTASelectProtein(dtaSelectProtein);
			final Pair<String, String> acc = FastaParser.getACC(primaryAccession.getAccession());
			if (acc.getSecondElement().equals("IPI")) {
				Pair<Accession, Set<Accession>> pair = IPI2UniprotACCMap.getInstance()
						.getPrimaryAndSecondaryAccessionsFromIPI(primaryAccession);
				if (pair.getFirstelement() != null) {
					primaryAccession = pair.getFirstelement();
				}
				if (pair.getSecondElement() != null) {
					secondaryAccessions.addAll(pair.getSecondElement());
				}
			} else if (acc.getSecondElement().equals("UNIPROT")) {
				List<String> ipis = IPI2UniprotACCMap.getInstance().map2IPI(acc.getFirstelement());

				if (ipis != null) {
					for (String ipi : ipis) {
						secondaryAccessions.add(new AccessionEx(ipi, AccessionType.IPI));
					}

				}
			}
		}
		return primaryAccession;
	}

	@Override
	public String getAccession() {
		final String accession = getPrimaryAccession().getAccession();
		return accession;
	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		return secondaryAccessions;
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			final String geneName = FastaParser.getGeneFromFastaHeader(dtaSelectProtein.getDescription());
			if (geneName != null) {
				final GeneEx geneEx = new GeneEx(geneName);
				genes.add(geneEx);
			}
			genesParsed = true;
		}
		return genes;
	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		Set<ProteinAnnotation> set = new THashSet<ProteinAnnotation>();

		final String proteinExistence = FastaParser
				.getProteinExistenceFromFastaHeader(dtaSelectProtein.getDescription());
		if (proteinExistence != null) {
			final AnnotationType annotationType = ProteinExistenceUtil.getAnnotationType(proteinExistence);
			if (annotationType != null)
				set.add(new ProteinAnnotationEx(annotationType, proteinExistence));
		}

		final String seqVersion = FastaParser.getSequenceVersionFromFastaHeader(dtaSelectProtein.getDescription());
		if (seqVersion != null) {
			set.add(new ProteinAnnotationEx(AnnotationType.sequenceVersion, AnnotationType.sequenceVersion.getKey(),
					seqVersion));
		}

		return set;
	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public Set<Threshold> getThresholds() {
		return null;
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		return null;
	}

	@Override
	public Set<PSM> getPSMs() {
		if (!psmsParsed) {
			final List<DTASelectPSM> dtaSelectPSMs = dtaSelectProtein.getPSMs();
			for (DTASelectPSM dtaSelectPSM : dtaSelectPSMs) {
				if (forceAllPSMsToBeFromThisMSRun) {
					dtaSelectPSM.setMsRunId(getMSRun().getRunId());
				}
				// only assign the ones from the same msRun, that is, raw
				// fileName otherwise, we are going to assign psms to a protein
				// belonging to another MSRun
				if (dtaSelectPSM.getMsRunId().equals(msrun.getRunId())) {
					PSM psm = null;
					// if
					// (PSMImplFromDTASelect.psmMap.containsKey(dtaSelectPSM)) {
					// psm = PSMImplFromDTASelect.psmMap.get(dtaSelectPSM);
					if (StaticProteomicsModelStorage.containsPSM(msrun, null, dtaSelectPSM.getPsmIdentifier())) {
						psm = StaticProteomicsModelStorage.getSinglePSM(msrun, null, dtaSelectPSM.getPsmIdentifier());
						psm.addProtein(this);
						psms.add(psm);
					} else {
						psm = new PSMImplFromDTASelect(dtaSelectPSM, msrun);
						StaticProteomicsModelStorage.addPSM(psm, msrun, null);
						psms.add(psm);
						psm.addProtein(this);

					}
					// create or retrieve the corresponding Peptide
					final String sequence = dtaSelectPSM.getSequence().getSequence();
					Peptide peptide = null;
					if (StaticProteomicsModelStorage.containsPeptide(msrun, null, sequence)) {
						peptide = StaticProteomicsModelStorage.getSinglePeptide(msrun, null, sequence);
					} else {
						peptide = new PeptideEx(sequence, msrun);
						StaticProteomicsModelStorage.addPeptide(peptide, msrun, null);
					}
					// Map<String, Peptide> peptideMap = null;
					// if
					// (PSMImplFromDTASelect.peptideMapByMSRun.containsKey(msrun.getRunId()))
					// {
					// peptideMap =
					// PSMImplFromDTASelect.peptideMapByMSRun.get(msrun.getRunId());
					// } else {
					// peptideMap = new THashMap<String, Peptide>();
					// PSMImplFromDTASelect.peptideMapByMSRun.put(msrun.getRunId(),
					// peptideMap);
					// }
					// if (peptideMap.containsKey(sequence)) {
					// peptide = peptideMap.get(sequence);
					// } else {
					// peptide = new PeptideEx(sequence, msrun);
					// peptideMap.put(sequence, peptide);
					// }

					peptide.addPSM(psm);
					psm.setPeptide(peptide);
					for (Protein protein : psm.getProteins()) {
						protein.addPeptide(peptide);
						peptide.addProtein(protein);
					}
				}
			}
			// }
			psmsParsed = true;
		}
		return psms;
	}

	@Override
	public Set<Peptide> getPeptides() {
		if (!peptidesParsed) {

			for (PSM psm : getPSMs()) {
				final String sequence = psm.getSequence();
				Peptide peptide = null;
				if (StaticProteomicsModelStorage.containsPeptide(msrun, null, sequence)) {
					peptide = StaticProteomicsModelStorage.getSinglePeptide(msrun, null, sequence);
				} else {
					peptide = new PeptideEx(sequence, msrun);
					StaticProteomicsModelStorage.addPeptide(peptide, msrun, null);
					peptide.addPSM(psm);
					psm.setPeptide(peptide);
				}
				peptides.add(peptide);
			}

			peptidesParsed = true;
		}
		return peptides;
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
		this.proteinGroup = proteinGroup;

	}

	@Override
	public Organism getOrganism() {
		if (organism == null) {
			final String taxonomyFromFastaHeader = FastaParser
					.getOrganismNameFromFastaHeader(dtaSelectProtein.getDescription(), dtaSelectProtein.getLocus());
			if (taxonomyFromFastaHeader != null) {
				organism = new OrganismEx(taxonomyFromFastaHeader);
			}
		}
		return organism;
	}

	@Override
	public Set<Score> getScores() {
		return scores;
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
		if (!conditionsParsed) {

			final Set<PSM> psMs2 = getPSMs();
			for (PSM psm : psMs2) {
				final Set<Condition> conditions2 = psm.getConditions();
				for (Condition condition : conditions2) {
					if (!conditions.contains(condition))
						conditions.add(condition);
				}
			}
			conditionsParsed = true;
		}
		return conditions;
	}

	@Override
	public void addCondition(Condition condition) {
		if (condition != null && !conditions.contains(condition)) {
			conditions.add(condition);
			// set condition to amounts
			for (Amount amount : getAmounts()) {
				if (amount.getCondition() == null && amount instanceof AmountEx) {
					((AmountEx) amount).setCondition(condition);
				}
			}
		}

		if (condition.getSample() != null && condition.getSample().getOrganism() != null)
			setOrganism(condition.getSample().getOrganism());
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
		List<GroupablePSM> ret = new ArrayList<GroupablePSM>();
		ret.addAll(getPSMs());
		return ret;
	}

	@Override
	public void addScore(Score score) {
		if (score != null && !scores.contains(score))
			scores.add(score);
	}

	@Override
	public void addRatio(Ratio ratio) {
		if (ratio != null && !ratios.contains(ratio))
			ratios.add(ratio);

	}

	@Override
	public void addAmount(Amount amount) {
		if (amount != null && !amounts.contains(amount)) {
			amounts.add(amount);
		}
	}

	@Override
	public void addPSM(PSM psm) {
		if (psm != null && !psms.contains(psm)) {
			psms.add(psm);

			//
			psm.addProtein(this);
			Peptide peptide = psm.getPeptide();
			if (peptide != null) {
				addPeptide(peptide);
				peptide.addProtein(this);
			}
		}

	}

	@Override
	public void setOrganism(Organism organism) {
		if (FastaParser.isContaminant(getAccession())) {
			this.organism = ModelUtils.ORGANISM_CONTAMINANT;
			return;
		}
		this.organism = organism;

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
		if (peptide != null && !peptides.contains(peptide)) {
			peptides.add(peptide);
			peptide.addProtein(this);
			Set<PSM> psMs2 = peptide.getPSMs();
			for (PSM psm : psMs2) {
				addPSM(psm);
				psm.addProtein(this);
			}
		}

	}

	@Override
	public void addGene(Gene gene) {
		if (gene != null && !genes.contains(gene))
			genes.add(gene);
	}

	public double getCoverage() {
		return dtaSelectProtein.getCoverage();
	}

	public Double getEMPai() {
		return dtaSelectProtein.getEmpai();
	}

	public double getEMPaiCov() {
		return dtaSelectProtein.getEmpaiCov();
	}

	public double getNSAFNorm() {
		return dtaSelectProtein.getNsaf_norm();
	}

	public Double getNSAF() {
		return dtaSelectProtein.getNsaf();
	}

	@Override
	public String toString() {
		return getPrimaryAccession().getAccession() + " in MSRun " + getMSRun().getRunId();
	}

	public void setForceAllPSMsToBeFromThisMSRun(boolean forceAllPSMsToBeFromThisMSRun) {
		this.forceAllPSMsToBeFromThisMSRun = forceAllPSMsToBeFromThisMSRun;
	}
}
