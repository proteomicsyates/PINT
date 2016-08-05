package edu.scripps.yates.proteindb.persistence.mysql.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;

public class PersistenceUtils {
	private final static Logger log = Logger.getLogger(PersistenceUtils.class);

	public static ProteinAccession getPrimaryAccession(Protein protein) {
		ProteinAccession primaryAcc = null;
		final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
		for (ProteinAccession proteinAccession : proteinAccessions) {
			if (proteinAccession.isIsPrimary()) {
				if (primaryAcc == null) {
					primaryAcc = proteinAccession;
				} else {
					log.warn("Protein contains two primary accessions " + primaryAcc.getAccession() + " and "
							+ proteinAccession.getAccession());
					// as a temporary action, keep the primary acc first in
					// the alphabet, to be consistent
					if (primaryAcc.getAccession().compareTo(proteinAccession.getAccession()) > 0) {
						primaryAcc = proteinAccession;
					}
					log.info("Keeping " + primaryAcc.getAccession());
				}
			}
		}
		if (primaryAcc != null) {
			return primaryAcc;
		}
		List<ProteinAccession> list = new ArrayList<ProteinAccession>();
		list.addAll(proteinAccessions);
		Collections.sort(list, new Comparator<ProteinAccession>() {

			@Override
			public int compare(ProteinAccession o1, ProteinAccession o2) {
				String acc1 = "";
				String acc2 = "";
				if (o1 != null && o1.getAccession() != null) {
					acc1 = o1.getAccession();
				}
				if (o2 != null && o2.getAccession() != null) {
					acc2 = o2.getAccession();
				}
				return acc1.compareTo(acc2);
			}
		});
		if (!list.isEmpty()) {
			primaryAcc = list.get(0);
		}
		return primaryAcc;
	}

	/**
	 * gets the union of two collections of proteins by using the primary
	 * accession as key
	 *
	 * @param proteins1
	 * @param proteins2
	 * @return
	 */
	public static Map<String, Set<Protein>> proteinUnion(Map<String, Set<Protein>> proteins1,
			Map<String, Set<Protein>> proteins2) {

		Map<String, Set<Protein>> ret = new HashMap<String, Set<Protein>>();
		// add all proteins in query1
		addToMapByPrimaryAcc(ret, proteins1);
		// add all proteins in query2
		addToMapByPrimaryAcc(ret, proteins2);
		return ret;
	}

	public static Map<String, Set<Protein>> proteinUnion(List<Protein> list1, List<Protein> list2) {
		Map<String, Set<Protein>> ret = new HashMap<String, Set<Protein>>();

		for (Protein protein : list1) {
			addToMapByPrimaryAcc(ret, protein);
		}
		for (Protein protein : list2) {
			addToMapByPrimaryAcc(ret, protein);
		}
		return ret;
	}

	/**
	 * Gets the union of two lists of PSMs by using the getPsmId() method as the
	 * key
	 *
	 * @param psmCollection1
	 * @param psmCollection2
	 * @return
	 */
	// public static Set<Psm> psmUnion(Collection<Psm> list1, Collection<Psm>
	// list2) {
	// Set<String> ids = new HashSet<String>();
	// Set<Psm> ret = new HashSet<Psm>();
	//
	// for (Psm psm : list1) {
	// if (!ids.contains(psm.getPsmId())) {
	// ids.add(psm.getPsmId());
	// ret.add(psm);
	// }
	// }
	// for (Psm psm : list2) {
	// if (!ids.contains(psm.getPsmId())) {
	// ids.add(psm.getPsmId());
	// ret.add(psm);
	// }
	// }
	// return ret;
	// }

	public static Set<Psm> psmUnion(Collection<Psm> psmCollection1, Collection<Psm> psmCollection2) {
		Set<Psm> ret = new HashSet<Psm>();
		ret.addAll(psmCollection1);
		ret.addAll(psmCollection2);
		return ret;
	}

	public static List<ProteinAccession> getAccessionsByAccType(Protein prot, AccessionType accType) {
		return getAccessionsByAccType(prot, accType.name());
	}

	public static List<ProteinAccession> getAccessionsByAccType(Protein prot, String accType) {
		List<ProteinAccession> ret = new ArrayList<ProteinAccession>();
		final Set<ProteinAccession> proteinAccessions = prot.getProteinAccessions();
		for (ProteinAccession proteinAccession : proteinAccessions) {
			if (proteinAccession.getAccessionType().equalsIgnoreCase(accType))
				ret.add(proteinAccession);
		}
		return ret;
	}

	/**
	 * Get Proteins from PSMs
	 *
	 * @param psms
	 * @return
	 */
	public static Map<String, Set<Protein>> getProteinsFromPsms(Collection<Psm> psms, boolean removePsmsAndPeptides) {
		Map<String, Set<Protein>> ret = new HashMap<String, Set<Protein>>();
		Set<Protein> proteinSet = new HashSet<Protein>();
		for (Psm psm : psms) {
			final Set<Protein> proteins = psm.getProteins();
			PersistenceUtils.addToMapByPrimaryAcc(ret, proteins);
			proteinSet.addAll(proteins);
		}
		if (removePsmsAndPeptides) {
			HashSet<Psm> psmSet = new HashSet<Psm>();
			psmSet.addAll(psms);
			removePsmsAndPeptidesFromProteins(proteinSet, psmSet);
		}

		log.info("Resulting " + ret.size() + " proteins from " + psms.size() + " psms");
		return ret;
	}

	/**
	 * Gets the proteins in a map of PSMs
	 *
	 * @param psmResult
	 * @return
	 */
	public static Map<String, Set<Protein>> getProteinsFromPsms(Map<String, Set<Psm>> psmResult,
			boolean removePsmsAndPeptides) {
		Map<String, Set<Protein>> ret = new HashMap<String, Set<Protein>>();
		if (psmResult != null) {
			HashSet<Psm> psmSet = getPsmSetFromMap(psmResult);
			final Map<String, Set<Protein>> proteinsFromPsms = getProteinsFromPsms(psmSet, removePsmsAndPeptides);
			addToMapByPrimaryAcc(ret, proteinsFromPsms);
			log.info(ret.size() + " proteins comming from " + psmResult.size() + " PSMs");
		}

		return ret;

	}

	private static HashSet<Protein> getProteinSetFromMap(Map<String, Set<Protein>> proteinMap) {
		HashSet<Protein> ret = new HashSet<Protein>();
		for (Set<Protein> proteinSet : proteinMap.values()) {
			ret.addAll(proteinSet);
		}
		return ret;
	}

	private static HashSet<Psm> getPsmSetFromMap(Map<String, Set<Psm>> psmMap) {
		HashSet<Psm> ret = new HashSet<Psm>();
		for (Set<Psm> psmSet : psmMap.values()) {
			ret.addAll(psmSet);
		}
		return ret;
	}

	/**
	 * Remove all the psms that are not in the collection of psms from the
	 * proteins passed in the argument. Also remove the peptides of the proteins
	 * that have not anymore psms associated.
	 *
	 * @param proteins
	 * @param psms
	 */
	private static void removePsmsAndPeptidesFromProteins(Collection<Protein> proteins, HashSet<Psm> psms) {
		Set<String> sequenceSet = new HashSet<String>();
		for (Protein protein : proteins) {
			sequenceSet.clear();
			// for each protein, remove the psms not in the HashSet of psms
			int initialNumPsms = protein.getPsms().size();
			final Iterator<Psm> psmIterator = protein.getPsms().iterator();
			while (psmIterator.hasNext()) {
				Psm psm = psmIterator.next();
				if (!psms.contains(psm)) {
					psmIterator.remove();
					psm.getProteins().remove(protein);
				} else {
					sequenceSet.add(psm.getSequence());
				}
			}
			if (protein.getPsms().isEmpty()) {
				log.warn("This should not happen. All proteins here should have at least one psm");
			}
			// look if some peptides are not in the sequenceSet, then remove
			// them from proteins
			int initialNumPeptides = protein.getPeptides().size();
			final Iterator<Peptide> peptideIterator = protein.getPeptides().iterator();
			while (peptideIterator.hasNext()) {
				final Peptide peptide = peptideIterator.next();
				if (!sequenceSet.contains(peptide.getSequence())) {
					peptideIterator.remove();
					peptide.getProteins().remove(protein);
				}
			}
			if (initialNumPeptides != protein.getPeptides().size() || initialNumPsms != protein.getPsms().size()) {
				log.info("Protein " + protein.getAcc() + " PSMs: from " + initialNumPsms + " to "
						+ protein.getPsms().size() + ", Peptides: from " + initialNumPeptides + " to "
						+ protein.getPeptides().size());
			}
		}

	}

	public static List<ProteinAnnotation> getProteinAnnotations(Set<ProteinAnnotation> proteinAnnotations,
			AnnotationType annotationType) {
		List<ProteinAnnotation> ret = new ArrayList<ProteinAnnotation>();
		for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			if (proteinAnnotation.getAnnotationType() != null && proteinAnnotation.getAnnotationType().getName() != null
					&& proteinAnnotation.getAnnotationType().getName().equals(annotationType.getKey()))
				ret.add(proteinAnnotation);
		}
		return ret;
	}

	public static Set<String> getPrimaryAccessions(Collection<Protein> proteinList) {
		Set<String> ret = new HashSet<String>();

		for (Protein protein : proteinList) {
			// ret.add(getPrimaryAccession(protein).getAccession());
			ret.add(protein.getAcc());
		}

		return ret;
	}

	public static boolean addToMapByPrimaryAcc(Map<String, Set<Protein>> map, Protein protein) {
		if (protein == null)
			return false;
		// if (protein.getPsms().isEmpty()) {
		// log.info("No adding Protein to the map because it has no PSMs");
		// return;
		// }

		final String primaryAcc = protein.getAcc();
		if (map.containsKey(primaryAcc)) {
			map.get(primaryAcc).add(protein);
		} else {

			Set<Protein> set = new HashSet<Protein>();
			set.add(protein);
			map.put(primaryAcc, set);
		}
		return true;

	}

	public static void addToMapByPrimaryAcc(Map<String, Set<Protein>> map, Collection<Protein> proteins) {
		for (Protein protein : proteins) {
			addToMapByPrimaryAcc(map, protein);
		}
	}

	public static void addToMapByPrimaryAcc(Map<String, Set<Protein>> receiverMap, Map<String, Set<Protein>> donorMap) {
		for (String key : donorMap.keySet()) {
			final Set<Protein> set = donorMap.get(key);
			addToMapByPrimaryAcc(receiverMap, set);
		}
	}

	public static Set<String> getAccessionsByAccType(Map<String, Set<Protein>> proteinMap, AccessionType uniprot) {
		Set<String> ret = new HashSet<String>();
		final Collection<Set<Protein>> proteinSets = proteinMap.values();
		for (Set<Protein> proteinSet : proteinSets) {
			for (Protein protein : proteinSet) {
				final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
				for (ProteinAccession accession : proteinAccessions) {
					if (accession.getAccessionType().equals(uniprot.name())) {
						ret.add(accession.getAccession());
					}
				}
			}
		}
		return ret;
	}

	public static Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(Protein protein, String condition1Name,
			String condition2Name, String ratioName) {
		Set<ProteinRatioValue> ret = new HashSet<ProteinRatioValue>();
		if (protein.getProteinRatioValues() != null) {
			// TODO
			// if (!Hibernate.isInitialized(protein.getProteinRatioValues())) {
			// Hibernate.initialize(protein.getProteinRatioValues());
			// }
			for (Object obj : protein.getProteinRatioValues()) {
				ProteinRatioValue ratioValue = (ProteinRatioValue) obj;
				final RatioDescriptor ratioDescriptor = ratioValue.getRatioDescriptor();
				if (ratioName != null && !ratioDescriptor.getDescription().equals(ratioName)) {
					continue;
				}
				if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}
				} else if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}
				}
			}
		}
		return ret;
	}

	public static Set<PsmRatioValue> getPsmRatiosBetweenTwoConditions(Psm psm, String condition1Name,
			String condition2Name, String ratioName) {
		Set<PsmRatioValue> ret = new HashSet<PsmRatioValue>();
		if (psm.getPsmRatioValues() != null) {
			for (Object obj : psm.getPsmRatioValues()) {
				PsmRatioValue ratioValue = (PsmRatioValue) obj;
				final RatioDescriptor ratioDescriptor = ratioValue.getRatioDescriptor();
				if (ratioName != null && !ratioDescriptor.getDescription().equals(ratioName)) {
					continue;
				}
				if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}
				} else if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}

				}
			}
		}
		return ret;
	}

	public static Set<PeptideRatioValue> getPeptideRatiosBetweenTwoConditions(Peptide peptide, String condition1Name,
			String condition2Name, String ratioName) {
		Set<PeptideRatioValue> ret = new HashSet<PeptideRatioValue>();
		if (peptide.getPeptideRatioValues() != null) {
			for (Object obj : peptide.getPeptideRatioValues()) {
				PeptideRatioValue ratioValue = (PeptideRatioValue) obj;
				final RatioDescriptor ratioDescriptor = ratioValue.getRatioDescriptor();
				if (ratioName != null && !ratioDescriptor.getDescription().equals(ratioName)) {
					continue;
				}
				if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}
				} else if (ratioDescriptor.getConditionByExperimentalCondition2Id().getName().equals(condition1Name)) {
					if (ratioDescriptor.getConditionByExperimentalCondition1Id().getName().equals(condition2Name)) {
						ret.add(ratioValue);
					}

				}
			}
		}
		return ret;
	}

	public static void addToPSMMapByPsmId(Map<String, Set<Psm>> map, Collection<Psm> psmList) {
		for (Psm psm : psmList) {
			addToPSMMapByPsmId(map, psm);
		}
	}

	public static void addToPSMMapByPsmId(Map<String, Set<Psm>> map, Psm psm) {
		if (psm == null)
			return;
		final String psmID = psm.getPsmId();
		if (psm.getProteins().isEmpty()) {
			log.info("Not adding this PSM because has no Proteins associated");
			return;
		}
		if (map.containsKey(psmID)) {
			map.get(psmID).add(psm);
		} else {

			Set<Psm> set = new HashSet<Psm>();
			set.add(psm);
			map.put(psmID, set);
		}
	}

	public static void addToPSMMapByPsmId(Map<String, Set<Psm>> receiverMap, Map<String, Set<Psm>> donorMap) {
		for (String key : donorMap.keySet()) {
			final Set<Psm> set = donorMap.get(key);
			addToPSMMapByPsmId(receiverMap, set);
		}

	}

	public static Map<String, Set<Psm>> psmUnion(Map<String, Set<Psm>> psms1, Map<String, Set<Psm>> psms2) {

		Map<String, Set<Psm>> ret = new HashMap<String, Set<Psm>>();
		// add all psms in query1
		addToPSMMapByPsmId(ret, psms1);
		// add all psms in query2
		addToPSMMapByPsmId(ret, psms2);
		return ret;
	}

	/**
	 * Gets a Map of PSMs in which the key is the PSMID
	 *
	 * @param proteinsMap
	 * @return
	 */
	// public static Map<String, Set<Psm>> getPsmsFromProteins(
	// Map<String, Set<Protein>> proteinsMap) {
	// Map<String, Set<Psm>> ret = new HashMap<String, Set<Psm>>();
	// if (proteinsMap != null) {
	// for (String acc : proteinsMap.keySet()) {
	// Set<Protein> proteinSet = proteinsMap.get(acc);
	// Set<String> psmIds = new HashSet<String>();
	// for (Protein protein : proteinSet) {
	// final Set<Psm> psms = protein.getPsms();
	// if (psms != null) {
	// for (Psm psm : psms) {
	// psmIds.add(psm.getPsmId());
	// }
	// PersistenceUtils.addToPSMMap(ret, psms);
	// }
	// }
	// }
	// }
	// return ret;
	// }

	public static Map<String, Set<Psm>> getPsmsFromProteins(Map<String, Set<Protein>> proteinResult) {
		Map<String, Set<Psm>> ret = new HashMap<String, Set<Psm>>();
		if (proteinResult != null) {
			Set<Protein> proteins = new HashSet<Protein>();
			for (Set<Protein> proteins2 : proteinResult.values()) {
				proteins.addAll(proteins2);
			}
			final Map<String, Set<Psm>> psmsFromPsms = getPsmsFromProteins(proteins);
			addToPSMMapByPsmId(ret, psmsFromPsms);
			log.info(ret.size() + " psms comming from " + proteinResult.size() + " proteins");
		}

		return ret;

	}

	public static Map<String, Set<Psm>> getPsmsFromProteins(Collection<Protein> proteins) {
		Map<String, Set<Psm>> ret = new HashMap<String, Set<Psm>>();

		for (Protein protein : proteins) {
			final Set<Psm> psms = protein.getPsms();

			PersistenceUtils.addToPSMMapByPsmId(ret, psms);
		}
		return ret;
	}

	/**
	 * Parse a ratio value and if the value is equal to Double.MAX_VALUE, it
	 * will be returned as Double.POSITIVE_INFINITY. If the value is equal to
	 * -Double.MAX_VALUE, it will be returned as Double.NEGATIVE_INFINITY
	 *
	 * @param value
	 * @return
	 */
	public static double parseRatioValueConvert2Infinities(double value) {
		if (Double.compare(value, Double.MAX_VALUE) == 0)
			return Double.POSITIVE_INFINITY;
		if (Double.compare(value, -Double.MAX_VALUE) == 0)
			return Double.NEGATIVE_INFINITY;
		return value;
	}

	/**
	 * Parse a ratio value and if the value is equal to
	 * Double.POSITIVE_INFINITY, it will be returned as Double.MAX_VALUE. If the
	 * value is equal to Double.NEGATIVE_INFINITY, it will be returned as
	 * -Double.MAX_VALUE
	 *
	 * @param value
	 * @return
	 */
	public static double parseRatioValueRemoveInfinities(double value) {
		if (Double.compare(value, Double.POSITIVE_INFINITY) == 0)
			return Double.MAX_VALUE;
		if (Double.compare(value, Double.NEGATIVE_INFINITY) == 0)
			return -Double.MAX_VALUE;
		return value;
	}

	public static void detachPSM(Psm psm, boolean psmDetached, boolean peptideDetached, boolean proteinDetached) {
		final Set<Protein> proteins = psm.getProteins();
		Set<Protein> proteinsToDetach = new HashSet<Protein>();
		for (Protein protein : proteins) {
			boolean removed = protein.getPsms().remove(psm);
			if (!proteinDetached) {
				if (protein.getPsms().isEmpty()) {
					proteinsToDetach.add(protein);
				}
			}
		}
		for (Protein protein : proteinsToDetach) {
			detachProtein(protein, true, peptideDetached, proteinDetached);
		}

		final Peptide peptide = psm.getPeptide();
		boolean removed = peptide.getPsms().remove(psm);
		if (!peptideDetached) {
			if (peptide.getPsms().isEmpty()) {
				detachPeptide(peptide, true, peptideDetached, proteinDetached);
			}
		}
	}

	public static void detachPeptide(Peptide peptide, boolean psmDetached, boolean peptideDetached,
			boolean proteinDetached) {
		if (!psmDetached) {
			Set<Psm> psms = peptide.getPsms();
			Set<Psm> psmsToDetach = new HashSet<Psm>();
			for (Psm psm : psms) {
				// if (psm.getPeptide() != null) {
				if (psm.getPeptide().equals(peptide)) {
					// psm.setPeptide(null);
					psmsToDetach.add(psm);

				}
				// }
			}
			for (Psm psm : psmsToDetach) {
				detachPSM(psm, psmDetached, true, proteinDetached);
			}
		}

		final Set<Protein> proteins = peptide.getProteins();
		Set<Protein> proteinsToDetach = new HashSet<Protein>();
		for (Protein protein : proteins) {
			boolean removed = protein.getPeptides().remove(peptide);
			if (!proteinDetached) {
				if (protein.getPeptides().isEmpty()) {
					proteinsToDetach.add(protein);
				}
			}
		}
		for (Protein protein : proteinsToDetach) {
			detachProtein(protein, psmDetached, true, proteinDetached);
		}
	}

	public static void detachProtein(Protein protein, boolean psmDetached, boolean peptideDetached,
			boolean proteinDetached) {
		final Set<Peptide> peptides = protein.getPeptides();
		Set<Peptide> peptidesToDetach = new HashSet<Peptide>();
		for (Peptide peptide : peptides) {
			boolean removed = peptide.getProteins().remove(protein);
			if (!peptideDetached) {
				if (peptide.getProteins().isEmpty()) {
					peptidesToDetach.add(peptide);
				}
			}
		}
		for (Peptide peptide : peptidesToDetach) {
			detachPeptide(peptide, psmDetached, peptideDetached, true);
		}

		final Set<Psm> psms = protein.getPsms();
		Set<Psm> psmsToDetach = new HashSet<Psm>();
		for (Psm psm : psms) {
			boolean removed = psm.getProteins().remove(protein);
			if (!psmDetached) {
				if (psm.getProteins().isEmpty()) {
					psmsToDetach.add(psm);
				}
			}
		}
		for (Psm psm : psmsToDetach) {
			detachPSM(psm, psmDetached, peptideDetached, true);
		}
	}
}
