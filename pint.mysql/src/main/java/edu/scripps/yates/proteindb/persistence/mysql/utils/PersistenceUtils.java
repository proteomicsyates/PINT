package edu.scripps.yates.proteindb.persistence.mysql.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;

public class PersistenceUtils {
	private final static Logger log = Logger.getLogger(PersistenceUtils.class);

	// public static ProteinAccession getPrimaryAccession(Protein protein) {
	// ProteinAccession primaryAcc = null;
	// final Set<ProteinAccession> proteinAccessions =
	// protein.getProteinAccessions();
	// for (final ProteinAccession proteinAccession : proteinAccessions) {
	// if (proteinAccession.isIsPrimary()) {
	// if (primaryAcc == null) {
	// primaryAcc = proteinAccession;
	// } else {
	// log.warn("Protein contains two primary accessions " +
	// primaryAcc.getAccession() + " and "
	// + proteinAccession.getAccession());
	// // as a temporary action, keep the primary acc first in
	// // the alphabet, to be consistent
	// if (primaryAcc.getAccession().compareTo(proteinAccession.getAccession())
	// > 0) {
	// primaryAcc = proteinAccession;
	// }
	// log.info("Keeping " + primaryAcc.getAccession());
	// }
	// }
	// }
	// if (primaryAcc != null) {
	// return primaryAcc;
	// }
	// final List<ProteinAccession> list = new ArrayList<ProteinAccession>();
	// list.addAll(proteinAccessions);
	// Collections.sort(list, new Comparator<ProteinAccession>() {
	//
	// @Override
	// public int compare(ProteinAccession o1, ProteinAccession o2) {
	// String acc1 = "";
	// String acc2 = "";
	// if (o1 != null && o1.getAccession() != null) {
	// acc1 = o1.getAccession();
	// }
	// if (o2 != null && o2.getAccession() != null) {
	// acc2 = o2.getAccession();
	// }
	// return acc1.compareTo(acc2);
	// }
	// });
	// if (!list.isEmpty()) {
	// primaryAcc = list.get(0);
	// }
	// return primaryAcc;
	// }
	public static ProteinAccession getPrimaryAccession(Protein protein) {
		ProteinAccession primaryAcc = null;
		final String proteinAccession = protein.getAcc();

		final String accessionType = FastaParser.getACC(proteinAccession).getAccessionType().name();
		primaryAcc = new ProteinAccession(proteinAccession, accessionType, true);

		return primaryAcc;
	}

	/**
	 * gets the union of two collections of proteins by using the primary accession
	 * as key
	 *
	 * @param proteins1
	 * @param proteins2
	 * @return
	 */
	public static Map<String, Collection<Protein>> proteinUnion(Map<String, Collection<Protein>> proteins1,
			Map<String, Collection<Protein>> proteins2) {

		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		// add all proteins in query1
		addToMapByPrimaryAcc(ret, proteins1);
		// add all proteins in query2
		addToMapByPrimaryAcc(ret, proteins2);
		return ret;
	}

	public static Map<String, Collection<Protein>> proteinUnion(Collection<Protein> list1, Collection<Protein> list2) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();

		for (final Protein protein : list1) {
			addToMapByPrimaryAcc(ret, protein);
		}
		for (final Protein protein : list2) {
			addToMapByPrimaryAcc(ret, protein);
		}
		return ret;
	}

	/**
	 * Gets the union of two lists of PSMs by using the getPsmId() method as the key
	 *
	 * @param psmCollection1
	 * @param psmCollection2
	 * @return
	 */
	// public static Set<Psm> psmUnion(Collection<Psm> list1, Collection<Psm>
	// list2) {
	// Set<String> ids = new THashSet<String>();
	// Set<Psm> ret = new THashSet<Psm>();
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
		final Set<Psm> ret = new THashSet<Psm>();
		ret.addAll(psmCollection1);
		ret.addAll(psmCollection2);
		return ret;
	}

	public static Set<Peptide> peptideUnion(Collection<Peptide> peptideCollection1,
			Collection<Peptide> peptideCollection2) {
		final Set<Peptide> ret = new THashSet<Peptide>();
		ret.addAll(peptideCollection1);
		ret.addAll(peptideCollection2);
		return ret;
	}

	/**
	 * Get Proteins from PSMs
	 *
	 * @param psms
	 * @return
	 */
	public static Map<String, Collection<Protein>> getProteinsFromPsms(Collection<Psm> psms,
			boolean removePsmsAndPeptides) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		final Set<Protein> proteinSet = new THashSet<Protein>();
		for (final Psm psm : psms) {
			final Set<Protein> proteins = psm.getProteins();
			PersistenceUtils.addToMapByPrimaryAcc(ret, proteins);
			proteinSet.addAll(proteins);
		}
		if (removePsmsAndPeptides) {
			final Set<Psm> psmSet = new THashSet<Psm>();
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
	public static Map<String, Collection<Protein>> getProteinsFromPsms(Map<String, Collection<Psm>> psmResult,
			boolean removePsmsAndPeptides) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		if (psmResult != null) {
			final Collection<Psm> psmSet = getPsmSetFromMap(psmResult);
			final Map<String, Collection<Protein>> proteinsFromPsms = getProteinsFromPsms(psmSet,
					removePsmsAndPeptides);
			addToMapByPrimaryAcc(ret, proteinsFromPsms);
			log.info(ret.size() + " proteins comming from " + psmResult.size() + " PSMs");
		}

		return ret;

	}

	private static Set<Protein> getProteinSetFromMap(Map<String, Set<Protein>> proteinMap) {
		final Set<Protein> ret = new THashSet<Protein>();
		for (final Set<Protein> proteinSet : proteinMap.values()) {
			ret.addAll(proteinSet);
		}
		return ret;
	}

	private static Set<Psm> getPsmSetFromMap(Map<String, Collection<Psm>> psmMap) {
		final Set<Psm> ret = new THashSet<Psm>();
		for (final Collection<Psm> psmSet : psmMap.values()) {
			ret.addAll(psmSet);
		}
		return ret;
	}

	/**
	 * Remove all the peptides that are not in the collection of peptides from the
	 * proteins passed in the argument.
	 *
	 * @param proteins
	 * @param psms
	 */
	private static void removePeptidesFromProteins(Collection<Protein> proteins, Collection<Peptide> peptides) {
		final Set<String> sequenceSet = new THashSet<String>();
		for (final Protein protein : proteins) {
			sequenceSet.clear();
			// for each protein, remove the peptides not in the HashSet of
			// peptides
			final int initialNumPeptides = protein.getPeptides().size();
			final Iterator<Peptide> peptideIterator = protein.getPeptides().iterator();
			while (peptideIterator.hasNext()) {
				final Peptide peptide = peptideIterator.next();
				if (!peptides.contains(peptide)) {
					peptideIterator.remove();
					peptide.getProteins().remove(protein);
				} else {
					sequenceSet.add(peptide.getSequence());
				}
			}
			if (protein.getPeptides().isEmpty()) {
				log.warn("This should not happen. All proteins here should have at least one peptide");
			}

			if (initialNumPeptides != protein.getPeptides().size()) {
				log.info("Protein " + protein.getAcc() + " Peptides: from " + initialNumPeptides + " to "
						+ protein.getPeptides().size());
			}
		}

	}

	/**
	 * Remove all the psms that are not in the collection of psms from the proteins
	 * passed in the argument. Also remove the peptides of the proteins that have
	 * not anymore psms associated.
	 *
	 * @param proteins
	 * @param psms
	 */
	private static void removePsmsAndPeptidesFromProteins(Collection<Protein> proteins, Collection<Psm> psms) {
		final Set<String> sequenceSet = new THashSet<String>();
		for (final Protein protein : proteins) {
			sequenceSet.clear();
			// for each protein, remove the psms not in the HashSet of psms
			final int initialNumPsms = protein.getPsms().size();
			final Iterator<Psm> psmIterator = protein.getPsms().iterator();
			while (psmIterator.hasNext()) {
				final Psm psm = psmIterator.next();
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
			final int initialNumPeptides = protein.getPeptides().size();
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
		final List<ProteinAnnotation> ret = new ArrayList<ProteinAnnotation>();
		for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {
			if (proteinAnnotation.getAnnotationType() != null && proteinAnnotation.getAnnotationType().getName() != null
					&& proteinAnnotation.getAnnotationType().getName().equals(annotationType.getKey()))
				ret.add(proteinAnnotation);
		}
		return ret;
	}

	public static Set<String> getPrimaryAccessions(Collection<Protein> proteinList) {
		final Set<String> ret = new THashSet<String>();

		for (final Protein protein : proteinList) {
			// ret.add(getPrimaryAccession(protein).getAccession());
			ret.add(protein.getAcc());
		}

		return ret;
	}

	public static boolean addToMapToSetByPrimaryAcc(Map<String, Set<Protein>> map, Protein protein) {
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

			final Set<Protein> set = new THashSet<Protein>();
			set.add(protein);
			map.put(primaryAcc, set);
		}
		return true;

	}

	public static boolean addToMapByPrimaryAcc(Map<String, Collection<Protein>> map, Protein protein) {
		if (protein == null)
			return false;
		// if (protein.getPsms().isEmpty()) {
		// log.info("No adding Protein to the map because it has no PSMs");
		// return;
		// }

		final String primaryAcc = protein.getAcc();
		if (map.containsKey(primaryAcc)) {
			if (!map.get(primaryAcc).contains(protein)) {
				map.get(primaryAcc).add(protein);
			}
		} else {

			final List<Protein> set = new ArrayList<Protein>();
			set.add(protein);
			map.put(primaryAcc, set);
		}
		return true;

	}

	public static void addToMapByPrimaryAcc(Map<String, Collection<Protein>> map, Collection<Protein> proteins) {
		for (final Protein protein : proteins) {
			addToMapByPrimaryAcc(map, protein);
		}
	}

	public static void addToMapByPrimaryAcc(Map<String, Collection<Protein>> receiverMap,
			Map<String, Collection<Protein>> donorMap) {
		for (final String key : donorMap.keySet()) {
			final Collection<Protein> set = donorMap.get(key);
			addToMapByPrimaryAcc(receiverMap, set);
		}
	}

	// ** commented out because it is not efficient
	// public static Set<String> getAccessionsByAccType(Map<String,
	// Set<Protein>> proteinMap, AccessionType uniprot) {
	// Set<String> ret = new THashSet<String>();
	// final Collection<Set<Protein>> proteinSets = proteinMap.values();
	// for (Set<Protein> proteinSet : proteinSets) {
	// for (Protein protein : proteinSet) {
	// final Set<ProteinAccession> proteinAccessions =
	// protein.getProteinAccessions();
	// for (ProteinAccession accession : proteinAccessions) {
	// if (accession.getAccessionType().equals(uniprot.name())) {
	// ret.add(accession.getAccession());
	// }
	// }
	// }
	// }
	// return ret;
	// }

	public static Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(Protein protein, String condition1Name,
			String condition2Name, String ratioName) {
		final Set<ProteinRatioValue> ret = new THashSet<ProteinRatioValue>();
		if (protein.getProteinRatioValues() != null) {
			// TODO
			// if (!Hibernate.isInitialized(protein.getProteinRatioValues())) {
			// Hibernate.initialize(protein.getProteinRatioValues());
			// }
			for (final Object obj : protein.getProteinRatioValues()) {
				final ProteinRatioValue ratioValue = (ProteinRatioValue) obj;
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
		final Set<PsmRatioValue> ret = new THashSet<PsmRatioValue>();
		if (psm.getPsmRatioValues() != null) {
			for (final Object obj : psm.getPsmRatioValues()) {
				final PsmRatioValue ratioValue = (PsmRatioValue) obj;
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
		return getPeptideRatiosBetweenTwoConditions(peptide.getPeptideRatioValues(), condition1Name, condition2Name,
				ratioName);
	}

	public static Set<PeptideRatioValue> getPeptideRatiosBetweenTwoConditions(Set<PeptideRatioValue> ratios,
			String condition1Name, String condition2Name, String ratioName) {
		final Set<PeptideRatioValue> ret = new THashSet<PeptideRatioValue>();

		for (final Object obj : ratios) {
			final PeptideRatioValue ratioValue = (PeptideRatioValue) obj;
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

		return ret;
	}

	public static void addToPSMMapByPsmId(Map<String, Collection<Psm>> map, Collection<Psm> psmList) {
		for (final Psm psm : psmList) {
			addToPSMMapByPsmId(map, psm);
		}
	}

	public static void addToPSMMapByPsmId(Map<String, Collection<Psm>> map, Psm psm) {
		if (psm == null)
			return;
		final String psmID = psm.getPsmId();
		// commented out because the call to .isempty() is expensive (4% of time)
//		if (psm.getProteins().isEmpty()) {
//			log.info("Not adding this PSM because has no Proteins associated");
//			return;
//		}
		if (map.containsKey(psmID)) {
			if (!map.get(psmID).contains(psm)) {
				map.get(psmID).add(psm);
			} else {
				log.info("asdf");
			}
		} else {

			final List<Psm> set = new ArrayList<Psm>();
			set.add(psm);
			map.put(psmID, set);
		}
	}

	public static void addToPSMMapByPsmId(Map<String, Collection<Psm>> receiverMap,
			Map<String, Collection<Psm>> donorMap) {
		for (final String key : donorMap.keySet()) {
			final Collection<Psm> set = donorMap.get(key);
			addToPSMMapByPsmId(receiverMap, set);
		}

	}

	public static Map<String, Collection<Psm>> psmUnion(Map<String, Collection<Psm>> psms1,
			Map<String, Collection<Psm>> psms2) {

		final Map<String, Collection<Psm>> ret = new THashMap<String, Collection<Psm>>();
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
	// Map<String, Set<Psm>> ret = new THashMap<String, Set<Psm>>();
	// if (proteinsMap != null) {
	// for (String acc : proteinsMap.keySet()) {
	// Set<Protein> proteinSet = proteinsMap.get(acc);
	// Set<String> psmIds = new THashSet<String>();
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

	public static Map<String, Collection<Psm>> getPsmsFromProteins(Map<String, Collection<Protein>> proteinResult,
			boolean removeProteins) {
		final Map<String, Collection<Psm>> ret = new THashMap<String, Collection<Psm>>();
		if (proteinResult != null) {
			final Set<Protein> proteins = new THashSet<Protein>();
			for (final Collection<Protein> proteins2 : proteinResult.values()) {
				proteins.addAll(proteins2);
			}
			final Map<String, Collection<Psm>> psmsFromProteins = getPsmsFromProteins(proteins, removeProteins);
			addToPSMMapByPsmId(ret, psmsFromProteins);
			log.info(ret.size() + " psms comming from " + proteinResult.size() + " proteins");
		}

		return ret;

	}

//	public static Set<Peptide> getPeptidesFromProteins(Map<String, Set<Protein>> proteinResult) {
//		final Set<Peptide> ret = new THashSet<Peptide>();
//		if (proteinResult != null) {
//			final Set<Protein> proteins = new THashSet<Protein>();
//			for (final Set<Protein> proteins2 : proteinResult.values()) {
//				proteins.addAll(proteins2);
//			}
//			final Set<Peptide> peptidesFromProteins = getPeptidesFromProteins(proteins);
//
//			log.info(peptidesFromProteins.size() + " peptides comming from " + proteinResult.size() + " proteins");
//		}
//
//		return Collections.emptySet();
//
//	}

	public static List<Peptide> getPeptidesFromProteinsUsingPeptideToProteinMappingTable(
			Map<String, Collection<Protein>> proteinResult) {
		if (proteinResult != null) {
			final Set<Protein> proteins = new THashSet<Protein>();
			for (final Collection<Protein> proteins2 : proteinResult.values()) {
				proteins.addAll(proteins2);
			}
			final List<Peptide> peptidesFromProteins = getPeptidesFromProteinsUsingProteinToPeptideMappingTable(
					proteins);

			log.info(peptidesFromProteins.size() + " peptides comming from " + proteinResult.size() + " proteins");

			return peptidesFromProteins;
		}

		return Collections.emptyList();

	}

	public static Map<String, Collection<Psm>> getPsmsFromProteins(Collection<Protein> proteins,
			boolean removeProteins) {
		final Map<String, Collection<Psm>> ret = new THashMap<String, Collection<Psm>>();
		for (final Protein protein : proteins) {
			final List<Psm> psms = new ArrayList<Psm>();
			psms.addAll(protein.getPsms());
			if (removeProteins) {
				removePsmsAndPeptidesFromProteins(proteins, psms);
			}
			PersistenceUtils.addToPSMMapByPsmId(ret, psms);
		}
		return ret;
	}

	public static Map<String, Collection<Psm>> getPsmsFromPeptides(Collection<Peptide> peptides,
			boolean removePeptides) {
		final Map<String, Collection<Psm>> ret = new THashMap<String, Collection<Psm>>();
		for (final Peptide peptide : peptides) {
			final Set<Psm> psms = new THashSet<Psm>();
			psms.addAll(peptide.getPsms());

			if (removePeptides) {
				final Iterator<Psm> iterator = psms.iterator();
				while (iterator.hasNext()) {
					final Psm psm = iterator.next();
					if (!peptides.contains(psm.getPeptide())) {
						iterator.remove();
					}
				}
			}
			PersistenceUtils.addToPSMMapByPsmId(ret, psms);
		}
		return ret;
	}

//	public static Set<Peptide> getPeptidesFromProteins(Collection<Protein> proteins) {
//		final Set<Peptide> ret = new THashSet<Peptide>();
//		for (final Protein protein : proteins) {
//			final Set<Peptide> peptides = protein.getPeptides();
//			ret.addAll(peptides);
//		}
//		log.info(ret.size() + " peptides comming from " + proteins.size() + " proteins");
//
//		return ret;
//	}

	public static List<Peptide> getPeptidesFromProteinsUsingProteinToPeptideMappingTable(Collection<Protein> proteins) {
		final Set<Peptide> set = new THashSet<Peptide>();
		for (final Protein protein : proteins) {
			final TIntSet peptideIDsFromProteinID = ProteinIDToPeptideIDTableMapper.getInstance()
					.getPeptideIDsFromProteinID(protein.getId());

			final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIDsFromProteinID, true,
					100);
			set.addAll(peptides);
		}
		log.info(set.size() + " peptides comming from " + proteins.size() + " proteins");
		final List<Peptide> ret = new ArrayList<Peptide>();
		ret.addAll(set);
		return ret;
	}

	/**
	 * Parse a ratio value and if the value is equal to Double.MAX_VALUE, it will be
	 * returned as Double.POSITIVE_INFINITY. If the value is equal to
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
	 * Parse a ratio value and if the value is equal to Double.POSITIVE_INFINITY, it
	 * will be returned as Double.MAX_VALUE. If the value is equal to
	 * Double.NEGATIVE_INFINITY, it will be returned as -Double.MAX_VALUE
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
		final Set<Protein> proteinsToDetach = new THashSet<Protein>();
		for (final Protein protein : proteins) {
			final boolean removed = protein.getPsms().remove(psm);
			if (!proteinDetached) {
				if (protein.getPsms().isEmpty()) {
					proteinsToDetach.add(protein);
				}
			}
		}
		for (final Protein protein : proteinsToDetach) {
			detachProtein(protein, true, peptideDetached, proteinDetached);
		}

		final Peptide peptide = psm.getPeptide();
		final boolean removed = peptide.getPsms().remove(psm);
		if (!peptideDetached) {
			if (peptide.getPsms().isEmpty()) {
				detachPeptide(peptide, true, peptideDetached, proteinDetached);
			}
		}
	}

	public static void detachPeptide(Peptide peptide, boolean psmDetached, boolean peptideDetached,
			boolean proteinDetached) {
		if (!psmDetached) {
			final Set<Psm> psms = peptide.getPsms();
			final Set<Psm> psmsToDetach = new THashSet<Psm>();
			for (final Psm psm : psms) {
				// if (psm.getPeptide() != null) {
				if (psm.getPeptide().equals(peptide)) {
					// psm.setPeptide(null);
					psmsToDetach.add(psm);

				}
				// }
			}
			for (final Psm psm : psmsToDetach) {
				detachPSM(psm, psmDetached, true, proteinDetached);
			}
		}

		final Set<Protein> proteins = peptide.getProteins();
		final Set<Protein> proteinsToDetach = new THashSet<Protein>();
		for (final Protein protein : proteins) {
			final boolean removed = protein.getPeptides().remove(peptide);
			if (!proteinDetached) {
				if (protein.getPeptides().isEmpty()) {
					proteinsToDetach.add(protein);
				}
			}
		}
		for (final Protein protein : proteinsToDetach) {
			detachProtein(protein, psmDetached, true, proteinDetached);
		}
	}

	public static void detachProtein(Protein protein, boolean psmDetached, boolean peptideDetached,
			boolean proteinDetached) {
		final Set<Peptide> peptides = protein.getPeptides();
		final Set<Peptide> peptidesToDetach = new THashSet<Peptide>();
		for (final Peptide peptide : peptides) {
			if (!peptide.getProteins().isEmpty()) {
				final boolean removed = peptide.getProteins().remove(protein);
			}
			if (!peptideDetached) {
				if (peptide.getProteins().isEmpty()) {
					peptidesToDetach.add(peptide);
				}
			}
		}
		for (final Peptide peptide : peptidesToDetach) {
			detachPeptide(peptide, psmDetached, peptideDetached, true);
		}

		final Set<Psm> psms = protein.getPsms();
		final Set<Psm> psmsToDetach = new THashSet<Psm>();
		for (final Psm psm : psms) {
			if (!psm.getProteins().isEmpty()) {
				final boolean removed = psm.getProteins().remove(protein);
			}
			if (!psmDetached) {
				if (psm.getProteins().isEmpty()) {
					psmsToDetach.add(psm);
				}
			}
		}
		for (final Psm psm : psmsToDetach) {
			detachPSM(psm, psmDetached, peptideDetached, true);
		}
	}

//	public static Map<String, Set<Protein>> getProteinsFromPeptides(Collection<Peptide> peptideSet,
//			boolean removePeptides) {
//		final Map<String, Set<Protein>> ret = new THashMap<String, Set<Protein>>();
//		final Set<Protein> proteinSet = new THashSet<Protein>();
//		for (final Peptide peptide : peptideSet) {
//			final Set<Protein> proteins = peptide.getProteins();
//			PersistenceUtils.addToMapByPrimaryAcc(ret, proteins);
//			proteinSet.addAll(proteins);
//		}
//		if (removePeptides) {
//			removePeptidesFromProteins(proteinSet, peptideSet);
//		}
//
//		log.info("Resulting " + ret.size() + " proteins from " + peptideSet.size() + " peptides");
//		return ret;
//	}

	public static Map<String, Collection<Protein>> getProteinsFromPeptidesUsingProteinToPeptideMappingTable(
			Collection<Peptide> peptideSet, boolean removePeptides) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		final Set<Protein> proteinSet = new THashSet<Protein>();
		for (final Peptide peptide : peptideSet) {
			final TIntSet proteinIDs = ProteinIDToPeptideIDTableMapper.getInstance()
					.getProteinIDsFromPeptideID(peptide.getId());
			final List<Protein> proteins = PreparedCriteria.getProteinsFromIDs(proteinIDs, true, 100);
			PersistenceUtils.addToMapByPrimaryAcc(ret, proteins);
			proteinSet.addAll(proteins);
		}
		if (removePeptides) {
			removePeptidesFromProteins(proteinSet, peptideSet);
		}

		log.debug("Resulting " + ret.size() + " proteins from " + peptideSet.size() + " peptides");
		return ret;
	}

	public static List<Peptide> getPeptidesFromPsms(Map<String, Collection<Psm>> psmMap) {
		final Set<Peptide> ret = new THashSet<Peptide>();
		for (final Collection<Psm> psmSet : psmMap.values()) {
			for (final Psm psm : psmSet) {
				ret.add(psm.getPeptide());
			}
		}
		log.info(ret.size() + " peptides comming from " + psmMap.size() + " proteins");
		final List<Peptide> list = new ArrayList<Peptide>();
		list.addAll(ret);
		return list;
	}

	public static void addToPeptideMapByFullSequence(Map<String, List<Peptide>> peptideMap, List<Peptide> peptides) {
		for (final Peptide peptide : peptides) {
			final String key = peptide.getFullSequence();
			if (!peptideMap.containsKey(key)) {
				final List<Peptide> peptideSet = new ArrayList<Peptide>();
				peptideSet.add(peptide);
				peptideMap.put(key, peptideSet);
			} else {
				if (!peptideMap.get(key).contains(peptide)) {
					peptideMap.get(key).add(peptide);
				}
			}
		}

	}

//	public static Map<String, Set<Protein>> getProteinMap(Collection<Peptide> peptideSet, TIntSet validProteinIDs) {
//		final Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
//		for (final Peptide peptide : peptideSet) {
//			final Set<Protein> proteins = peptide.getProteins();
//			for (final Protein protein : proteins) {
//				if (validProteinIDs.contains(protein.getId())) {
//					if (map.containsKey(protein.getAcc())) {
//						map.get(protein.getAcc()).add(protein);
//					} else {
//						final Set<Protein> set = new THashSet<Protein>();
//						set.add(protein);
//						map.put(protein.getAcc(), set);
//					}
//				} else {
//					log.debug("Protein " + protein.getId() + " was not in the original valid protein set");
//				}
//			}
//		}
//
//		return map;
//	}

	public static Map<String, List<Protein>> getProteinMapUsingProteinToPeptideMappingTable(
			Collection<Peptide> peptideSet, TIntSet validProteinIDs) {
		final Map<String, List<Protein>> map = new THashMap<String, List<Protein>>();
		for (final Peptide peptide : peptideSet) {
			final TIntSet proteinIDs = ProteinIDToPeptideIDTableMapper.getInstance()
					.getProteinIDsFromPeptideID(peptide.getId());

			final List<Protein> proteins = PreparedCriteria.getProteinsFromIDs(proteinIDs, true, 100);
			for (final Protein protein : proteins) {
				if (validProteinIDs.contains(protein.getId())) {
					if (map.containsKey(protein.getAcc())) {
						map.get(protein.getAcc()).add(protein);
					} else {
						final List<Protein> list = new ArrayList<Protein>();
						list.add(protein);
						map.put(protein.getAcc(), list);
					}
				} else {
					log.debug("Protein " + protein.getId() + " was not in the original valid protein set");
				}
			}
		}

		return map;
	}
}
