package edu.scripps.yates.dtaselect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import edu.scripps.yates.utilities.model.factories.AccessionEx;
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
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.ProteomicsModelStaticStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.util.Pair;

public class ProteinImplFromDTASelect implements Protein {
	private static final Logger log = Logger.getLogger(ProteinImplFromDTASelect.class);
	private final DTASelectProtein dtaSelectProtein;
	private Accession primaryAccession;
	private final Set<PSM> psms = new HashSet<PSM>();
	private final Set<Peptide> peptides = new HashSet<Peptide>();
	private ProteinGroup proteinGroup;
	private ProteinEvidence evidence;
	private Organism organism;
	private final Set<Condition> conditions = new HashSet<Condition>();
	private MSRun msrun;
	private final Set<Amount> amounts = new HashSet<Amount>();
	private final Set<Ratio> ratios = new HashSet<Ratio>();
	private final Set<Score> scores = new HashSet<Score>();
	private boolean conditionsParsed = false;
	private boolean psmsParsed = false;
	private double mw;
	private double pi;
	private int length;
	private final Set<Gene> genes = new HashSet<Gene>();
	private boolean genesParsed = false;
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private boolean peptidesParsed;
	private final boolean forceAllPSMsToBeFromThisMSRun;
	private static int max = 0;
	private final static Map<String, MSRun> msRunMap = new HashMap<String, MSRun>();

	public ProteinImplFromDTASelect(DTASelectProtein dtaSelectProtein, String msRunID) {
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
		forceAllPSMsToBeFromThisMSRun = false;

		final int length2 = getPrimaryAccession().getAccession().length();
		if (length2 > max) {
			log.debug(getPrimaryAccession().getAccession() + " length=" + length2);
			max = length2;
		}
	}

	public ProteinImplFromDTASelect(DTASelectProtein dtaSelectProtein2, MSRun msrun2) {
		dtaSelectProtein = dtaSelectProtein2;
		if (dtaSelectProtein.getLength() != null)
			length = dtaSelectProtein.getLength();
		mw = dtaSelectProtein.getMw();
		pi = dtaSelectProtein.getPi();

		msrun = msrun2;
		msRunMap.put(msrun.getRunId(), msrun);
		forceAllPSMsToBeFromThisMSRun = true;
		final int length2 = getPrimaryAccession().getAccession().length();
		if (length2 > max) {
			log.info(getPrimaryAccession().getAccession() + " length=" + length2);
			max = length2;
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
		Set<ProteinAnnotation> set = new HashSet<ProteinAnnotation>();

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
				// only assign the ones from the same msRun, that is, fileName
				// otherwise, we are going to assign psms to a protein belonging
				// to another MSRun
				if (dtaSelectPSM.getMsRunId().equals(msrun.getRunId())) {
					PSM psm = null;
					// if
					// (PSMImplFromDTASelect.psmMap.containsKey(dtaSelectPSM)) {
					// psm = PSMImplFromDTASelect.psmMap.get(dtaSelectPSM);
					if (ProteomicsModelStaticStorage.containsPSM(msrun, null, dtaSelectPSM.getPsmIdentifier())) {
						psm = ProteomicsModelStaticStorage.getSinglePSM(msrun, null, dtaSelectPSM.getPsmIdentifier());
						psm.addProtein(this);
						psms.add(psm);
					} else {
						psm = new PSMImplFromDTASelect(dtaSelectPSM, msrun);
						psm.addProtein(this);
						psms.add(psm);
						ProteomicsModelStaticStorage.addPSM(psm, msrun, null);
					}
					// create or retrieve the corresponding Peptide
					final String sequence = dtaSelectPSM.getSequence().getSequence();
					Peptide peptide = null;
					if (ProteomicsModelStaticStorage.containsPeptide(msrun, null, sequence)) {
						peptide = ProteomicsModelStaticStorage.getSinglePeptide(msrun, null, sequence);
					} else {
						peptide = new PeptideEx(sequence, msrun);
						ProteomicsModelStaticStorage.addPeptide(peptide, msrun, null);
					}
					// Map<String, Peptide> peptideMap = null;
					// if
					// (PSMImplFromDTASelect.peptideMapByMSRun.containsKey(msrun.getRunId()))
					// {
					// peptideMap =
					// PSMImplFromDTASelect.peptideMapByMSRun.get(msrun.getRunId());
					// } else {
					// peptideMap = new HashMap<String, Peptide>();
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
		// ModelUtils.createPeptides(this);
		if (!peptidesParsed) {

			for (PSM psm : getPSMs()) {
				final String sequence = psm.getSequence();
				Peptide peptide = null;
				if (ProteomicsModelStaticStorage.containsPeptide(msrun, null, sequence)) {
					peptide = ProteomicsModelStaticStorage.getSinglePeptide(msrun, null, sequence);
				} else {
					peptide = new PeptideEx(sequence, msrun);
					peptide.addPSM(psm);
					psm.setPeptide(peptide);
					ProteomicsModelStaticStorage.addPeptide(peptide, msrun, null);
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
		if (!conditions.contains(condition))
			conditions.add(condition);
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
		if (!amounts.isEmpty())
			System.out.println("SECOND AMOUNT");
		if (!amounts.contains(amount))
			amounts.add(amount);
	}

	@Override
	public void addPSM(PSM psm) {
		if (!psms.contains(psm))
			psms.add(psm);

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
		if (!peptides.contains(peptide))
			peptides.add(peptide);

	}

	@Override
	public void addGene(Gene gene) {
		if (!genes.contains(gene))
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

}
