package edu.scripps.yates.proteindb.queries.semantic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinScoreIDTableMapper;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class QueriesUtil {
	public static final Logger log = Logger.getLogger(QueriesUtil.class);
	public static final boolean QUERY_CACHE_ENABLED = true;
	public static final int TEST_MODE_NUM_PSMS = 500;
	public static final int TEST_MODE_NUM_PEPTIDES = 100;
	public static final int TEST_MODE_NUM_PROTEINS = 20;

	public static List<LinkBetweenQueriableProteinSetAndPSM> createProteinPSMLinks(
			Map<String, Collection<Protein>> proteinMap) {
		log.info("Creating proteinPSMLinks from a proteinMap of size: " + proteinMap.size());
		final List<LinkBetweenQueriableProteinSetAndPSM> ret = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();
		final Collection<Collection<Protein>> proteinSets = proteinMap.values();
		int numProteins = 0;
		int numPSMs = 0;
		// clear links before create them
		for (final Collection<Protein> proteinSet : proteinSets) {
			QueriableProteinSet.getInstance(proteinSet, true).clearLinks();
			for (final Protein protein : proteinSet) {
				numProteins++;
				final Set<Psm> psms = protein.getPsms();
				for (final Psm psm : psms) {
					numPSMs++;
					QueriablePsm.getInstance(psm, true).clearLinks();
				}
			}
		}
		log.info("proteinPSMLinks cleared for  " + numProteins + " proteins and " + numPSMs + " PSMs");

		final Map<String, Set<LinkBetweenQueriableProteinSetAndPSM>> linkMapByProteinAcc = new THashMap<String, Set<LinkBetweenQueriableProteinSetAndPSM>>();
		for (final Collection<Protein> proteinSet : proteinSets) {
			for (final Protein protein : proteinSet) {
				final Set<Psm> psms = protein.getPsms();
				for (final Psm psm : psms) {
					// if (psm.getProteins().isEmpty()) {
					// log.info("PSM without proteins: PSM id: " + psm.getId());
					// }
					final LinkBetweenQueriableProteinSetAndPSM proteinSet2PsmLink = new LinkBetweenQueriableProteinSetAndPSM(
							proteinSet, psm);
					ret.add(proteinSet2PsmLink);
					final String accession = proteinSet2PsmLink.getQueriableProtein().getPrimaryAccession();
					if (linkMapByProteinAcc.containsKey(accession)) {
						linkMapByProteinAcc.get(accession).add(proteinSet2PsmLink);
					} else {
						final Set<LinkBetweenQueriableProteinSetAndPSM> linkSet = new THashSet<LinkBetweenQueriableProteinSetAndPSM>();
						linkSet.add(proteinSet2PsmLink);
						linkMapByProteinAcc.put(accession, linkSet);
					}
				}
			}
		}
		// iterate over the set of links for the same protein
		for (final Set<LinkBetweenQueriableProteinSetAndPSM> linkSet : linkMapByProteinAcc.values()) {
			for (final LinkBetweenQueriableProteinSetAndPSM proteinPSMLink : linkSet) {
				proteinPSMLink.setLinkSetForSameProtein(linkSet);
			}
		}
		log.info(ret.size() + " proteinPSMLinks created");
		return ret;
	}

	public static String getPrimaryProteinAccession(QueriableProteinSet queriableProteinInt) {
		String primaryAcc = null;
		final Set<String> proteinAccessions = queriableProteinInt.getProteinAccessions();
		for (final String proteinAccession : proteinAccessions) {

			if (primaryAcc == null) {
				primaryAcc = proteinAccession;
			} else {
				log.warn("Protein contains two primary accessions " + primaryAcc + " and " + proteinAccession);
				// as a temporary action, keep the primary acc first in
				// the alphabet, to be consistent
				if (primaryAcc.compareTo(proteinAccession) > 0) {
					primaryAcc = proteinAccession;
				}
				log.info("Keeping " + primaryAcc);
			}

		}
		if (primaryAcc != null) {
			return primaryAcc;
		}
		final List<String> list = new ArrayList<String>();
		list.addAll(proteinAccessions);
		Collections.sort(list);
		if (!list.isEmpty()) {
			primaryAcc = list.get(0);
		}
		return primaryAcc;
	}

	public static Map<String, Collection<Protein>> getProteinSubList(
			Map<String, Collection<Protein>> proteinsByProjectCondition, int maxIndex) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		final List<Protein> list = new ArrayList<Protein>();
		for (final Collection<Protein> proteinSet : proteinsByProjectCondition.values()) {
			list.addAll(proteinSet);
		}
		PersistenceUtils.addToMapByPrimaryAcc(ret,
				list.subList(0, Math.min(proteinsByProjectCondition.size(), maxIndex)));
		return ret;
	}

	public static Map<String, Collection<Protein>> getProteinSubList(List<Protein> proteins, int maxIndex) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();

		PersistenceUtils.addToMapByPrimaryAcc(ret, proteins.subList(0, Math.min(proteins.size(), maxIndex)));
		return ret;
	}

//	private static Map<String, Set<Peptide>> getPeptideMap(Set<Protein> proteinSet) {
//		final Map<String, Set<Peptide>> map = new THashMap<String, Set<Peptide>>();
//
//		final Set<Integer> proteinIds = proteinSet.stream().map(protein -> protein.getId()).collect(Collectors.toSet());
//		final List<Peptide> peptides = PreparedCriteria.getPeptidesFromProteinIDs(proteinIds);
//		for (final Peptide peptide : peptides) {
//			if (map.containsKey(peptide.getFullSequence())) {
//				map.get(peptide.getFullSequence()).add(peptide);
//			} else {
//				final Set<Peptide> set = new THashSet<Peptide>();
//				set.add(peptide);
//				map.put(peptide.getFullSequence(), set);
//			}
//		}
//
//		return map;
//	}

	private static Map<String, List<Peptide>> getPeptideMap(Collection<Protein> proteinSet) {
		final Map<String, List<Peptide>> map = new THashMap<String, List<Peptide>>();

		final Set<Integer> proteinIds = proteinSet.stream().map(protein -> protein.getId()).collect(Collectors.toSet());
		final TIntSet intSet = new TIntHashSet(proteinIds);
		final TIntSet peptideIDs = PreparedCriteria.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(intSet);
		final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIDs, true, 100);
		for (final Peptide peptide : peptides) {
			if (map.containsKey(peptide.getFullSequence())) {
				if (!map.get(peptide.getFullSequence()).contains(peptide)) {
					map.get(peptide.getFullSequence()).add(peptide);
				}
			} else {
				final List<Peptide> set = new ArrayList<Peptide>();
				set.add(peptide);
				map.put(peptide.getFullSequence(), set);
			}
		}

		return map;
	}

	private static Map<String, Set<Peptide>> getPeptideMap2(Set<Protein> proteinSet) {
		final Map<String, Set<Peptide>> map = new THashMap<String, Set<Peptide>>();
		for (final Protein protein : proteinSet) {
			final Set<Peptide> peptides = protein.getPeptides();
			for (final Peptide peptide : peptides) {
				if (map.containsKey(peptide.getFullSequence())) {
					map.get(peptide.getFullSequence()).add(peptide);
				} else {
					final Set<Peptide> set = new THashSet<Peptide>();
					set.add(peptide);
					map.put(peptide.getFullSequence(), set);
				}
			}
		}

		return map;
	}

	public static List<LinkBetweenQueriableProteinSetAndPeptideSet> createProteinPeptideLinks(
			Map<String, Collection<Protein>> totalProteinMap) {
		// keep the ids of the proteins in the method parameter map, which are
		// the ones that are valid,
		// then, getting the proteins from the peptides, we will not get any
		// other protein that has an id not in that group
		final TIntHashSet validProteinIDs = new TIntHashSet();
		log.info("Creating proteinPeptideLinks from a proteinMap of size: " + totalProteinMap.size());

		final TIntArrayList proteinIDs = new TIntArrayList();
		for (final Collection<Protein> proteins : totalProteinMap.values()) {
			proteins.stream().map(protein -> protein.getId()).forEach(proteinID -> proteinIDs.add(proteinID));
		}
		final TIntSet peptideIDs = ProteinIDToPeptideIDTableMapper.getInstance()
				.getPeptideIDsFromProteinIDs(proteinIDs);
		log.info("Loading " + peptideIDs.size() + " peptides from database");
		PreparedCriteria.getBatchLoadByIDs(Peptide.class, peptideIDs, true, 250);
		log.info("Now loaded...they will remain in cache");
		final TIntSet proteinScoreIDs = ProteinIDToProteinScoreIDTableMapper.getInstance()
				.getProteinScoreIDsFromProteinIDs(proteinIDs);
		log.info("Loading " + proteinScoreIDs.size() + " protein scores from database");
		PreparedCriteria.getBatchLoadByIDs(ProteinScore.class, proteinScoreIDs, true, 250);
		log.info("Now loaded...they will remain in cache");

		int numProteins = 0;
		int numPeptides = 0;
		final Map<String, Collection<Peptide>> totalPeptideMap = new THashMap<String, Collection<Peptide>>();
		// clear links before create them
		final ProgressCounter counter = new ProgressCounter(totalProteinMap.size(),
				ProgressPrintingType.PERCENTAGE_STEPS, 1, true);
		counter.setSuffix("Creating protein-peptide links");
		for (final String proteinACC : totalProteinMap.keySet()) {
			counter.increment();
			final String printIfNecessary = counter.printIfNecessary();
			if (!"".equals(printIfNecessary)) {
				log.info(printIfNecessary);
			}
			final Collection<Protein> proteinSet = totalProteinMap.get(proteinACC);
			for (final Protein protein : proteinSet) {
				validProteinIDs.add(protein.getId());
			}
			QueriableProteinSet.getInstance(proteinSet, true).clearLinks();
			numProteins += proteinSet.size();

			final Map<String, List<Peptide>> peptideMap = getPeptideMap(proteinSet);
			for (final String fullSequence : peptideMap.keySet()) {
				try {
					FastaParser.cleanSequence(fullSequence);

					final List<Peptide> peptideSet = peptideMap.get(fullSequence);
					QueriablePeptideSet.getInstance(peptideSet, true).clearLinks();
					numPeptides += peptideSet.size();
					if (totalPeptideMap.containsKey(fullSequence)) {
						totalPeptideMap.get(fullSequence).addAll(peptideSet);
					} else {
						final List<Peptide> set = new ArrayList<Peptide>();
						set.addAll(peptideSet);
						totalPeptideMap.put(fullSequence, set);
					}
				} catch (final IllegalArgumentException e) {
					continue;
					// to avoid non standard peptides that make an exception in
					// FastaParser.cleanSequence(fullSequence);

				}
			}

		}

		log.info("proteinPeptideLinks cleared for  " + numProteins + " proteins and " + numPeptides
				+ " Peptides doing: proteins->peptides");

		numProteins = 0;
		numPeptides = 0;
		for (final String fullSequence : totalPeptideMap.keySet()) {
			final Collection<Peptide> peptideSet = totalPeptideMap.get(fullSequence);
			QueriablePeptideSet.getInstance(peptideSet, true).clearLinks();
			numPeptides += peptideSet.size();
			final Map<String, List<Protein>> proteinMap = PersistenceUtils
					.getProteinMapUsingProteinToPeptideMappingTable(peptideSet, validProteinIDs);
			for (final String proteinACC : proteinMap.keySet()) {
				final List<Protein> proteinSet = proteinMap.get(proteinACC);
				QueriableProteinSet.getInstance(proteinSet, true).clearLinks();
				numProteins += proteinSet.size();
				if (totalProteinMap.containsKey(proteinACC)) {
					totalProteinMap.get(proteinACC).addAll(proteinSet);
				} else {
					final List<Protein> set = new ArrayList<Protein>();
					set.addAll(proteinSet);
					totalProteinMap.put(proteinACC, set);
				}
			}

		}

		log.info("proteinPeptideLinks cleared for  " + numProteins + " proteins and  " + numPeptides
				+ " Peptides doing: peptides->proteins ");

		final Map<String, Set<LinkBetweenQueriableProteinSetAndPeptideSet>> linkMapByProteinAcc = new THashMap<String, Set<LinkBetweenQueriableProteinSetAndPeptideSet>>();
		final Set<LinkBetweenQueriableProteinSetAndPeptideSet> ret = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();

		// proteins -> peptides
		for (final String proteinACC : totalProteinMap.keySet()) {
			final Collection<Protein> proteinSet = totalProteinMap.get(proteinACC);
			final Map<String, List<Peptide>> peptideMap = getPeptideMap(proteinSet);
			for (final String peptideFullSequence : peptideMap.keySet()) {
				final Collection<Peptide> peptideSet = totalPeptideMap.get(peptideFullSequence);
				if (peptideSet == null) {
					log.debug(peptideFullSequence + " not found in peptides of proteins " + proteinACC);
					continue;
				}
				final LinkBetweenQueriableProteinSetAndPeptideSet proteinSet2PeptideLink = new LinkBetweenQueriableProteinSetAndPeptideSet(
						proteinSet, peptideSet);
				ret.add(proteinSet2PeptideLink);
				final String accession = proteinSet2PeptideLink.getQueriableProtein().getPrimaryAccession();
				if (linkMapByProteinAcc.containsKey(accession)) {
					linkMapByProteinAcc.get(accession).add(proteinSet2PeptideLink);
				} else {
					final Set<LinkBetweenQueriableProteinSetAndPeptideSet> linkSet = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();
					linkSet.add(proteinSet2PeptideLink);
					linkMapByProteinAcc.put(accession, linkSet);
				}
			}
		}
		// peptides -> proteins
		int numNewLinks = 0;
		for (final String peptideFullSequence : totalPeptideMap.keySet()) {
			final Collection<Peptide> peptideSet = totalPeptideMap.get(peptideFullSequence);
			final Set<String> proteinAccs = PersistenceUtils
					.getProteinMapUsingProteinToPeptideMappingTable(peptideSet, validProteinIDs).keySet();
			for (final String proteinAcc : proteinAccs) {
				final Collection<Protein> proteinSet = totalProteinMap.get(proteinAcc);
				final LinkBetweenQueriableProteinSetAndPeptideSet proteinSet2PeptideLink = new LinkBetweenQueriableProteinSetAndPeptideSet(
						proteinSet, peptideSet);
				// even though I am creating a new link, it may be have created
				// previously in the loop from proteins to peptides, so it will
				// not be added to the set here:
				final boolean added = ret.add(proteinSet2PeptideLink);
				if (added) {
					numNewLinks++;
					final String accession = proteinSet2PeptideLink.getQueriableProtein().getPrimaryAccession();
					if (linkMapByProteinAcc.containsKey(accession)) {
						linkMapByProteinAcc.get(accession).add(proteinSet2PeptideLink);
					} else {
						final Set<LinkBetweenQueriableProteinSetAndPeptideSet> linkSet = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();
						linkSet.add(proteinSet2PeptideLink);
						linkMapByProteinAcc.put(accession, linkSet);
					}
				} else {
					log.debug("link already created");
				}
			}
		}

		log.info(numNewLinks + " new links created in the second pass from peptide to proteins");

		// iterate over the set of links for the same protein
		for (final Set<LinkBetweenQueriableProteinSetAndPeptideSet> linkSet : linkMapByProteinAcc.values()) {
			for (final LinkBetweenQueriableProteinSetAndPeptideSet proteinPeptideLink : linkSet) {
				proteinPeptideLink.setLinkSetForSameProtein(linkSet);
			}
		}
		log.info(ret.size() + " proteinPeptideLinks created");
		final List<LinkBetweenQueriableProteinSetAndPeptideSet> retList = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();
		retList.addAll(ret);
		return retList;
	}

}
