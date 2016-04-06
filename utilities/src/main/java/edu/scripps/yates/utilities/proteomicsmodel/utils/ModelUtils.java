package edu.scripps.yates.utilities.proteomicsmodel.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;
import edu.scripps.yates.utilities.proteomicsmodel.HasRatios;
import edu.scripps.yates.utilities.proteomicsmodel.HasScores;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class ModelUtils {
	private static final Logger log = Logger.getLogger(ModelUtils.class);
	// private static final HashMap<MSRun, Map<String, Peptide>> peptidesByMSRun
	// = new HashMap<MSRun, Map<String, Peptide>>();
	public static Organism ORGANISM_CONTAMINANT;

	static {
		ORGANISM_CONTAMINANT = new OrganismEx("000000");
		((OrganismEx) ORGANISM_CONTAMINANT).setName(FastaParser.CONTAMINANT_PREFIX);
	}

	public static Set<Ratio> getProteinRatiosBetweenTwoConditions(Protein protein, String condition1Name,
			String condition2Name) {
		Set<Ratio> ret = new HashSet<Ratio>();
		if (protein.getRatios() != null) {
			for (Ratio ratio : protein.getRatios()) {

				if (ratio.getCondition1().getName().equals(condition1Name)) {
					if (ratio.getCondition2().getName().equals(condition2Name)) {
						ret.add(ratio);
					}
				} else if (ratio.getCondition2().getName().equals(condition1Name)) {
					if (ratio.getCondition1().getName().equals(condition2Name)) {
						ret.add(ratio);
					}

				}
			}
		}
		return ret;
	}

	/**
	 * Return a list of protein as a result of the union of the two input list
	 * of proteins
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static Collection<Protein> proteinUnion(Collection<Protein> list1, Collection<Protein> list2) {

		if (list1.isEmpty() && !list2.isEmpty())
			return list2;
		if (list2.isEmpty() && !list1.isEmpty())
			return list1;
		Set<String> proteinPrimaryAccs = new HashSet<String>();
		List<Protein> ret = new ArrayList<Protein>();

		for (Protein protein : list1) {
			if (!proteinPrimaryAccs.contains(protein.getPrimaryAccession().getAccession())) {
				proteinPrimaryAccs.add(protein.getPrimaryAccession().getAccession());
				ret.add(protein);
			}
		}
		for (Protein protein : list2) {
			if (!proteinPrimaryAccs.contains(protein.getPrimaryAccession().getAccession())) {
				proteinPrimaryAccs.add(protein.getPrimaryAccession().getAccession());
				ret.add(protein);
			}
		}
		return ret;
	}

	public static Collection<PSM> psmUnion(Collection<PSM> list1, Collection<PSM> list2) {
		Set<String> ids = new HashSet<String>();
		List<PSM> ret = new ArrayList<PSM>();

		for (PSM psm : list1) {
			if (!ids.contains(psm.getPSMIdentifier())) {
				ids.add(psm.getPSMIdentifier());
				ret.add(psm);
			}
		}
		for (PSM psm : list2) {
			if (!ids.contains(psm.getPSMIdentifier())) {
				ids.add(psm.getPSMIdentifier());
				ret.add(psm);
			}
		}
		return ret;
	}

	public static Set<PSM> getPSMIntersection(Collection<PSM> set1, Collection<PSM> set2) {
		Set<PSM> ret = new HashSet<PSM>();
		for (PSM t1 : set1) {
			if (set2.contains(t1)) {
				ret.add(t1);
			}
		}
		return ret;
	}

	public static Set<Peptide> getPeptideIntersection(Collection<Peptide> set1, Collection<Peptide> set2) {
		Set<Peptide> ret = new HashSet<Peptide>();
		for (Peptide t1 : set1) {
			if (set2.contains(t1)) {
				ret.add(t1);
			}
		}
		return ret;
	}

	public static Set<Protein> getProteinIntersection(Collection<Protein> set1, Collection<Protein> set2) {
		Set<Protein> ret = new HashSet<Protein>();
		for (Protein p1 : set1) {
			for (Protein p2 : set2) {
				if (p1.getPrimaryAccession().getAccession().equals(p2.getPrimaryAccession().getAccession())) {
					ret.add(p1);
					ret.add(p2);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets all accessions, primary or not from the protein that are of a
	 * certain {@link AccessionType}
	 *
	 * @param prot
	 * @param accType
	 * @return
	 */
	public static List<Accession> getAccessions(Protein prot, AccessionType accType) {
		return getAccessions(prot, accType.name());
	}

	/**
	 * Gets all accessions, primeray or not from a set of proteins that are of a
	 * certain {@link AccessionType}
	 *
	 * @param prot
	 * @param accType
	 * @return
	 */
	public static List<Accession> getAccessions(Protein prot, String accType) {
		List<Accession> ret = new ArrayList<Accession>();
		final List<Accession> proteinAccessions = prot.getSecondaryAccessions();
		if (proteinAccessions != null) {
			for (Accession proteinAccession : proteinAccessions) {
				if (proteinAccession.getAccessionType().name().equalsIgnoreCase(accType))
					ret.add(proteinAccession);
			}
		}
		if (prot.getPrimaryAccession().getAccessionType().name().equalsIgnoreCase(accType))
			ret.add(prot.getPrimaryAccession());
		return ret;
	}

	public static List<Protein> getProteinsFromPsms(Collection<PSM> psms) {
		List<Protein> ret = new ArrayList<Protein>();
		Set<String> ids = new HashSet<String>();
		for (PSM psm : psms) {
			final Set<Protein> proteins = psm.getProteins();
			for (Protein protein : proteins) {
				if (!ids.contains(protein.getPrimaryAccession().getAccession())) {
					ret.add(protein);
					ids.add(protein.getPrimaryAccession().getAccession());
				}
			}
		}
		return ret;
	}

	public static List<ProteinAnnotation> getProteinAnnotations(Set<ProteinAnnotation> proteinAnnotations,
			AnnotationType annotationType) {
		List<ProteinAnnotation> ret = new ArrayList<ProteinAnnotation>();
		for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			if (proteinAnnotation.getAnnotationType().getKey().equals(annotationType.getKey()))
				ret.add(proteinAnnotation);
		}
		return ret;
	}

	public static Collection<String> getPrimaryAccessions(Collection<Protein> proteinList) {
		HashSet<String> ret = new HashSet<String>();

		for (Protein protein : proteinList) {
			ret.add(protein.getPrimaryAccession().getAccession());
		}

		return ret;
	}

	public static Collection<String> getPrimaryAccessions(Collection<Protein> proteinList, String accType) {
		HashSet<String> ret = new HashSet<String>();

		for (Protein protein : proteinList) {
			final Accession primaryAccession = protein.getPrimaryAccession();
			if (primaryAccession.getAccessionType().name().equals(accType))
				ret.add(primaryAccession.getAccession());
		}

		return ret;
	}

	public static Collection<String> getPrimaryAccessions(Collection<Protein> proteinList, AccessionType accType) {
		HashSet<String> ret = new HashSet<String>();

		for (Protein protein : proteinList) {
			final Accession primaryAccession = protein.getPrimaryAccession();
			if (primaryAccession.getAccessionType().name().equals(accType.name()))
				ret.add(primaryAccession.getAccession());
		}

		return ret;
	}

	public static Score getScore(HasScores obj, String scoreName) {

		final Set<Score> scores = obj.getScores();
		for (Score score : scores) {
			if (score.getScoreName().equalsIgnoreCase(scoreName))
				return score;
		}

		return null;
	}

	public static List<Amount> getAmounts(HasAmounts obj, String conditionName) {
		List<Amount> ret = new ArrayList<Amount>();

		final Set<Amount> amounts = obj.getAmounts();
		for (Amount amount : amounts) {
			if (amount.getCondition().getName().equalsIgnoreCase(conditionName))
				ret.add(amount);
		}

		return ret;
	}

	public static List<Amount> getAmounts(HasAmounts obj, String conditionName, AmountType amountType) {
		List<Amount> ret = new ArrayList<Amount>();
		final List<Amount> amounts = getAmounts(obj, conditionName);
		for (Amount amount : amounts) {
			if (amount.getAmountType() == amountType)
				ret.add(amount);
		}

		return ret;
	}

	public static List<Ratio> getRatios(HasRatios obj, String condition1, String condition2) {
		List<Ratio> ret = new ArrayList<Ratio>();

		final Set<Ratio> ratios = obj.getRatios();
		for (Ratio ratio : ratios) {
			if (ratio.getCondition1().getName().equalsIgnoreCase(condition1)) {
				if (ratio.getCondition2().getName().equalsIgnoreCase(condition2)) {
					ret.add(ratio);
				}
			} else
			// the other way round
			if (ratio.getCondition1().getName().equalsIgnoreCase(condition2)) {
				if (ratio.getCondition2().getName().equalsIgnoreCase(condition1)) {
					ret.add(ratio);
				}
			}
		}

		return ret;
	}

	public static Map<String, List<Protein>> mergeProteins(List<Protein> proteins) {
		Map<String, List<Protein>> map = new HashMap<String, List<Protein>>();
		for (Protein protein : proteins) {
			final String accession = protein.getPrimaryAccession().getAccession();
			if (map.containsKey(accession)) {
				map.get(accession).add(protein);
			} else {
				List<Protein> list = new ArrayList<Protein>();
				list.add(protein);
				map.put(accession, list);
			}
		}
		return map;
	}

	public static void addToMap(Map<String, Set<Protein>> map, Protein protein) {
		final String primaryAcc = protein.getPrimaryAccession().getAccession();
		if (map.containsKey(primaryAcc)) {
			map.get(primaryAcc).add(protein);
		} else {
			Set<Protein> set = new HashSet<Protein>();
			set.add(protein);
			map.put(primaryAcc, set);
		}
		if (protein.getSecondaryAccessions() != null) {
			for (Accession acc : protein.getSecondaryAccessions()) {
				if (map.containsKey(acc.getAccession())) {
					map.get(acc.getAccession()).add(protein);
				} else {
					Set<Protein> set = new HashSet<Protein>();
					set.add(protein);
					map.put(acc.getAccession(), set);
				}
			}
		}
	}

	public static void addToMap(Map<String, Set<Protein>> map, Collection<Protein> proteins) {
		for (Protein protein : proteins) {
			addToMap(map, protein);
		}

	}

	public static void addToMap(Map<String, Set<Protein>> receiverMap, Map<String, Set<Protein>> donorMap) {
		for (String key : donorMap.keySet()) {
			final Set<Protein> set = donorMap.get(key);
			if (receiverMap.containsKey(key)) {
				receiverMap.get(key).addAll(set);
			} else {
				Set<Protein> set2 = new HashSet<Protein>();
				set2.addAll(set);
				receiverMap.put(key, set2);
			}
		}
	}

	public static List<Protein> getAllProteinsFromMap(Map<String, Set<Protein>> map) {
		List<Protein> list = new ArrayList<Protein>();
		for (Set<Protein> proteinSet : map.values()) {
			for (Protein protein : proteinSet) {
				list.add(protein);
			}
		}
		return list;
	}

	public static Map<String, Set<PSM>> getPSMMapBySequence(Collection<PSM> psms) {
		Map<String, Set<PSM>> ret = new HashMap<String, Set<PSM>>();
		if (psms != null) {
			for (PSM psm : psms) {
				if (psm == null)
					continue;
				final String cleanSequence = FastaParser.cleanSequence(psm.getSequence());
				if (ret.containsKey(cleanSequence)) {
					ret.get(cleanSequence).add(psm);
				} else {
					Set<PSM> set = new HashSet<PSM>();
					set.add(psm);
					ret.put(cleanSequence, set);
				}
			}
		}
		return ret;
	}

	// /**
	// * Create the peptides of a protein taking into account the peptides per
	// * MSRun in an static way
	// *
	// * @param protein
	// */
	// public static void createPeptides(Protein protein) {
	// MSRun msRun = protein.getMSRun();
	// Set<PSM> psms = protein.getPSMs();
	// if (psms == null || psms.isEmpty()) {
	// log.warn("The protein has no psms, so, no peptides can be created");
	// return;
	// }
	// Map<String, Set<PSM>> psmMapBySequence =
	// ModelUtils.getPSMMapBySequence(psms);
	// Map<String, Peptide> peptideSet = null;
	// // Create peptides grouped by the MSRun
	//
	// if (peptidesByMSRun.containsKey(msRun)) {
	// peptideSet = peptidesByMSRun.get(msRun);
	// } else {
	// peptideSet = new HashMap<String, Peptide>();
	// peptidesByMSRun.put(msRun, peptideSet);
	// }
	// for (String sequence : psmMapBySequence.keySet()) {
	// final Set<PSM> psmsWithThatSequence = psmMapBySequence.get(sequence);
	// Peptide peptide = null;
	// if (peptideSet.containsKey(sequence)) {
	// peptide = peptideSet.get(sequence);
	// } else {
	// // create the peptide
	// peptide = new PeptideEx(sequence, msRun);
	// peptideSet.put(sequence, peptide);
	// }
	//
	// for (PSM psmWithThatSequence : psmsWithThatSequence) {
	// // psm-peptide relation
	// peptide.addPSM(psmWithThatSequence);
	// psmWithThatSequence.setPeptide(peptide);
	//
	// // protein-psm
	// protein.addPSM(psmWithThatSequence);
	// psmWithThatSequence.addProtein(protein);
	//
	// }
	// final Set<PSM> psms2 = peptide.getPSMs();
	// for (PSM psm : psms2) {
	// protein.addPSM(psm);
	// psm.addProtein(protein);
	// }
	// // protein-peptide relation
	// protein.addPeptide(peptide);
	// peptide.addProtein(protein);
	//
	// }
	//
	// }

}
