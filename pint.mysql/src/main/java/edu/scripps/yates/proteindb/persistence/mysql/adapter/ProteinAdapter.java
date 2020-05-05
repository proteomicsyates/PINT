package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PeptideEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinAdapter implements Adapter<Protein>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1770714315274469495L;
	private static final Logger log = Logger.getLogger(ProteinAdapter.class);
	private final edu.scripps.yates.utilities.proteomicsmodel.Protein protein;
	private final Project hibProject;
	private static final TIntObjectHashMap<Protein> map = new TIntObjectHashMap<Protein>();
	// private static final HashMap<MSRun, Map<String, Peptide>> peptidesByMSRun
	// = new THashMap<MSRun, Map<String, Peptide>>();

	public ProteinAdapter(edu.scripps.yates.utilities.proteomicsmodel.Protein protein, Project hibProject) {
		this.protein = protein;
		this.hibProject = hibProject;
	}

	@Override
	public Protein adapt() {
		if (map.containsKey(protein.hashCode()))
			return map.get(protein.hashCode());

		final Protein ret = new Protein();
		map.put(protein.hashCode(), ret);

		ret.setAcc(protein.getAccession());

		if (protein.getLength() != null) {
			ret.setLength(protein.getLength());
		} else {
			ret.setLength(getProteinLengthFromUniprot(protein));
		}
		if (protein.getMw() != null) {
			ret.setMw(protein.getMw().doubleValue());
		} else {
			final Float proteinMWFromUniprot = getProteinMWFromUniprot(protein);
			if (proteinMWFromUniprot != null) {
				ret.setMw(proteinMWFromUniprot.doubleValue());
			}
		}
		if (protein.getPi() != null) {
			ret.setPi(protein.getPi().doubleValue());
		}
		// ret.getProteinAccessions().add(new
		// ProteinAccessionAdapter(protein.getPrimaryAccession(),
		// true).adapt());

		// final List<Accession> accessions = protein.getSecondaryAccessions();
		// if (accessions != null) {
		// for (Accession accession : accessions) {
		// // not adapt the same accession as the primary
		// if
		// (accession.getAccession().equals(protein.getPrimaryAccession().getAccession()))
		// {
		// ret.getProteinAccessions().add(new ProteinAccessionAdapter(accession,
		// false).adapt());
		// }
		// }
		// }
		final ProteinAccession primaryAccession = PersistenceUtils.getPrimaryAccession(ret);
		if (primaryAccession == null)
			log.error("The protein have to have a primary acc");

		// Set<Gene> genes = getGenesFromUniprot(protein);
		// if (genes == null) {
		// genes = protein.getGenes();
		// }
		// for (Gene gene : genes) {
		// final edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene =
		// new GeneAdapter(gene).adapt();
		// ret.getGenes().add(hibGene);
		// }

		// thresholds
		final Set<Threshold> thresholds = protein.getThresholds();
		if (thresholds != null) {
			for (final Threshold threshold : thresholds) {
				final ProteinThreshold hibAppliedThreshold = new ProteinThresholdAdapter(threshold, ret).adapt();
				ret.getProteinThresholds().add(hibAppliedThreshold);
			}
		}
		// protein annotations
		final Set<ProteinAnnotation> proteinAnnotations = protein.getAnnotations();
		if (proteinAnnotations != null) {
			for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {
				final edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation hibProteinAnnotation = new ProteinAnnotationAdapter(
						proteinAnnotation, ret).adapt();
				ret.getProteinAnnotations().add(hibProteinAnnotation);
			}
		}
		// protein ratios
		final Set<Ratio> proteinRatios = protein.getRatios();
		if (proteinRatios != null) {
			for (final Ratio proteinRatioModel : proteinRatios) {
				if (!Double.isNaN(proteinRatioModel.getValue())) {
					final ProteinRatioValue hibProteinRatio = new ProteinRatioValueAdapter(proteinRatioModel, ret,
							hibProject).adapt();
					ret.getProteinRatioValues().add(hibProteinRatio);
				}
			}
		}
		// protein amounts
		boolean hasSPCAmount = false;
		final Set<Amount> amounts = protein.getAmounts();
		if (amounts != null) {
			for (final Amount amount : amounts) {
				if (amount.getCondition() == null) {
					throw new IllegalArgumentException("Error. An amount with no condition stated is found");
				}
				if (!Double.isNaN(amount.getValue())) {
					if (amount.getAmountType() == AmountType.SPC) {
						hasSPCAmount = true;
					}
					ret.getProteinAmounts().add(new ProteinAmountAdapter(amount, hibProject, ret).adapt());
				}
			}
		}
		if (!hasSPCAmount) {
			// commented out because it will be calculated on the fly in the GUI
			// of PINT

			// create a spectral count amount
			// if (protein.getPSMs() != null && !protein.getPSMs().isEmpty()) {
			// Map<edu.scripps.yates.proteindb.persistence.mysql.Condition,
			// Set<PSM>> psmsByCondition = getPSMsByCondition(
			// protein.getPSMs());
			// for (edu.scripps.yates.proteindb.persistence.mysql.Condition
			// condition : psmsByCondition.keySet()) {
			// final Double spc =
			// Double.valueOf(psmsByCondition.get(condition).size());
			// final ProteinAmount amountSPC = new ProteinAmount(ret,
			// new AmountTypeAdapter(AmountType.SPC).adapt(), condition, spc);
			// ret.getProteinAmounts().add(amountSPC);
			// }
			//
			// }
		}
		// scores
		final Set<Score> scores = protein.getScores();
		if (scores != null) {
			for (final Score score : scores) {
				ret.getProteinScores().add(new ProteinScoreAdapter(score, ret).adapt());
			}
		}

		// organism
		// try to get it from Uniprot. Otherwise, get it from
		Organism organism = getOrganismFromUniprot(protein);
		if (organism == null) {
			organism = protein.getOrganism();
		}
		// if (organism != null) {
		// ret.setOrganism(new OrganismAdapter(organism).adapt());
		// } else {
		// final OrganismEx organism2 = new OrganismEx("0000");
		// organism2.setName("Unknown");
		// ret.setOrganism(new OrganismAdapter(organism2).adapt());
		// }
		// conditions
		final Set<Condition> conditions = protein.getConditions();
		if (conditions != null) {
			for (final Condition condition : conditions) {
				ret.getConditions().add(new ConditionAdapter(condition, hibProject).adapt());

			}
		}

		// msrun
		final Set<MSRun> msRuns = protein.getMSRuns();
		for (final MSRun msRun2 : msRuns) {
			ret.getMsRuns().add(new MSRunAdapter(msRun2, hibProject).adapt());
		}

		// PSMS
		final List<PSM> psms2 = protein.getPSMs();
		if (psms2 != null) {
			for (final PSM psm : psms2) {
				final Psm hibPsm = new PSMAdapter(psm, hibProject).adapt();
				ret.getPsms().add(hibPsm);
				hibPsm.getProteins().add(ret);
			}
		}

		// peptides
		final Set<Peptide> peptides = protein.getPeptides();
		if (peptides != null) {
			for (final Peptide peptide : peptides) {
				final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(peptide,
						hibProject).adapt();
				ret.getPeptides().add(hibPeptide);
				hibPeptide.getProteins().add(ret);
			}
		} else {
			// if there is not peptides, try to construct the peptides from
			// PSMs and Proteins
			final List<PSM> psms = protein.getPSMs();
			if (psms != null) {
				final Map<String, Set<PSM>> psmMapByKey = ModelUtils.getPSMMapBySequence(psms, true, true);

				for (final String sequenceKey : psmMapByKey.keySet()) {
					final Set<PSM> psmsWithThatSequence = psmMapByKey.get(sequenceKey);
					Peptide peptide = null;
					if (StaticProteomicsModelStorage.containsPeptide(msRuns, null, sequenceKey)) {
						peptide = StaticProteomicsModelStorage.getSinglePeptide(msRuns, null, sequenceKey);
					} else {
						// create the peptide
						peptide = new PeptideEx(sequenceKey, sequenceKey);

						StaticProteomicsModelStorage.addPeptide(peptide, msRuns, null, sequenceKey);
					}
					peptide.addProtein(protein, true);
					// add the relationships with the psms
					for (final PSM psmWithThatSequence : psmsWithThatSequence) {
						peptide.addPSM(psmWithThatSequence, true);
					}
					// add to this protein
					final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(peptide,
							hibProject).adapt();
					ret.getPeptides().add(hibPeptide);
					hibPeptide.getProteins().add(ret);

				}
			}
		}

		return ret;
	}

	private Map<edu.scripps.yates.proteindb.persistence.mysql.Condition, Set<PSM>> getPSMsByCondition(Set<PSM> psMs) {
		final Map<edu.scripps.yates.proteindb.persistence.mysql.Condition, Set<PSM>> ret = new THashMap<edu.scripps.yates.proteindb.persistence.mysql.Condition, Set<PSM>>();
		if (psMs != null) {
			for (final PSM psm : psMs) {
				final Set<Condition> conditions = psm.getConditions();
				if (conditions != null) {
					for (final Condition condition : conditions) {
						final edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition = new ConditionAdapter(
								condition, hibProject).adapt();
						if (ret.containsKey(hibCondition)) {
							ret.get(hibCondition).add(psm);
						} else {
							final Set<PSM> psmSet = new THashSet<PSM>();
							psmSet.add(psm);
							ret.put(hibCondition, psmSet);
						}
					}
				}
			}
		}
		return ret;
	}

	private Organism getOrganismFromUniprot(edu.scripps.yates.utilities.proteomicsmodel.Protein protein) {
		if (protein.getPrimaryAccession().getAccessionType() != AccessionType.UNIPROT)
			return null;
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		final String accession = protein.getAccession();
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProtein = upr
				.getAnnotatedProtein(accession);
		if (annotatedProtein != null && !annotatedProtein.isEmpty()) {
			final String nonIsoFormaAcc = FastaParser.getNoIsoformAccession(accession);
			if (annotatedProtein.containsKey(accession) && annotatedProtein.get(accession) != null) {
				return annotatedProtein.get(accession).getOrganism();
			} else if (annotatedProtein.containsKey(nonIsoFormaAcc) && annotatedProtein.get(nonIsoFormaAcc) != null) {
				return annotatedProtein.get(nonIsoFormaAcc).getOrganism();
			} else {
				log.debug("check it out");
			}
		}
		return null;
	}

	private Set<Gene> getGenesFromUniprot(edu.scripps.yates.utilities.proteomicsmodel.Protein protein) {
		if (protein.getPrimaryAccession().getAccessionType() != AccessionType.UNIPROT)
			return null;
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		final String accession = protein.getAccession();
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProtein = upr
				.getAnnotatedProtein(accession);
		if (annotatedProtein != null && !annotatedProtein.isEmpty()) {
			final String nonIsoFormaAcc = FastaParser.getNoIsoformAccession(accession);
			if (annotatedProtein.containsKey(accession) && annotatedProtein.get(accession) != null) {
				return annotatedProtein.get(accession).getGenes();
			} else if (annotatedProtein.containsKey(nonIsoFormaAcc) && annotatedProtein.get(nonIsoFormaAcc) != null) {
				return annotatedProtein.get(nonIsoFormaAcc).getGenes();
			} else {
				log.debug("check it out");
			}
		}
		return null;
	}

	private Integer getProteinLengthFromUniprot(edu.scripps.yates.utilities.proteomicsmodel.Protein protein) {
		if (protein.getPrimaryAccession().getAccessionType() != AccessionType.UNIPROT)
			return null;
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		final String accession = protein.getAccession();
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProtein = upr
				.getAnnotatedProtein(accession);
		if (annotatedProtein != null && !annotatedProtein.isEmpty()) {
			final String nonIsoFormaAcc = FastaParser.getNoIsoformAccession(accession);
			if (annotatedProtein.containsKey(accession) && annotatedProtein.get(accession) != null) {
				return annotatedProtein.get(accession).getLength();
			} else if (annotatedProtein.containsKey(nonIsoFormaAcc) && annotatedProtein.get(nonIsoFormaAcc) != null) {
				return annotatedProtein.get(nonIsoFormaAcc).getLength();
			} else {
				log.debug("check it out");
			}
		}
		return null;
	}

	private Float getProteinMWFromUniprot(edu.scripps.yates.utilities.proteomicsmodel.Protein protein) {
		if (protein.getPrimaryAccession().getAccessionType() != AccessionType.UNIPROT)
			return null;
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		final String accession = protein.getAccession();
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProtein = upr
				.getAnnotatedProtein(accession);
		if (annotatedProtein != null && !annotatedProtein.isEmpty()) {
			final String nonIsoFormaAcc = FastaParser.getNoIsoformAccession(accession);
			if (annotatedProtein.containsKey(accession) && annotatedProtein.get(accession) != null) {
				return annotatedProtein.get(accession).getMw();
			} else if (annotatedProtein.containsKey(nonIsoFormaAcc) && annotatedProtein.get(nonIsoFormaAcc) != null) {
				return annotatedProtein.get(nonIsoFormaAcc).getMw();
			} else {
				log.debug("check it out");
			}
		}
		return null;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
