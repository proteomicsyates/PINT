package edu.scripps.yates.proteindb.queries.semantic.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class QueriesUtil {
	public static final Logger log = Logger.getLogger(QueriesUtil.class);
	public static final boolean QUERY_CACHE_ENABLED = true;
	public static final int TEST_MODE_NUM_PSMS = 500;
	public static final int TEST_MODE_NUM_PEPTIDES = 100;
	public static final int TEST_MODE_NUM_PROTEINS = 20;

	public static List<LinkBetweenQueriableProteinSetAndPSM> createProteinPSMLinks(
			Map<String, Set<Protein>> proteinMap) {
		log.info("Creating proteinPSMLinks from a proteinMap of size: " + proteinMap.size());
		final List<LinkBetweenQueriableProteinSetAndPSM> ret = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();
		final Collection<Set<Protein>> proteinSets = proteinMap.values();
		int numProteins = 0;
		int numPSMs = 0;
		// clear links before create them
		for (final Set<Protein> proteinSet : proteinSets) {
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
		for (final Set<Protein> proteinSet : proteinSets) {
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

	public static Map<String, Set<Protein>> getProteinSubList(Map<String, Set<Protein>> proteinsByProjectCondition,
			int maxIndex) {
		final Map<String, Set<Protein>> ret = new THashMap<String, Set<Protein>>();
		final List<Protein> list = new ArrayList<Protein>();
		for (final Set<Protein> proteinSet : proteinsByProjectCondition.values()) {
			list.addAll(proteinSet);
		}
		PersistenceUtils.addToMapByPrimaryAcc(ret,
				list.subList(0, Math.min(proteinsByProjectCondition.size(), maxIndex)));
		return ret;
	}

	private static Map<String, Set<Peptide>> getPeptideMap(Set<Protein> proteinSet) {
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
			Map<String, Set<Protein>> totalProteinMap) {
		log.info("Creating proteinPeptideLinks from a proteinMap of size: " + totalProteinMap.size());
		int numProteins = 0;
		int numPeptides = 0;

		final Map<String, Set<Peptide>> totalPeptideMap = new THashMap<String, Set<Peptide>>();
		// clear links before create them
		for (final Set<Protein> proteinSet : totalProteinMap.values()) {
			QueriableProteinSet.getInstance(proteinSet, true).clearLinks();
			numProteins += proteinSet.size();
			final Map<String, Set<Peptide>> peptideMap = getPeptideMap(proteinSet);
			for (final Set<Peptide> peptideSet : peptideMap.values()) {
				QueriablePeptideSet.getInstance(peptideSet, true).clearLinks();
				numPeptides += peptideSet.size();
			}
			totalPeptideMap.putAll(peptideMap);
		}
		log.info("proteinPeptideLinks cleared for  " + numProteins + " proteins and " + numPeptides
				+ " Peptides doing: proteins->peptides");
		numProteins = 0;
		numPeptides = 0;
		for (final Set<Peptide> peptideSet : totalPeptideMap.values()) {
			QueriablePeptideSet.getInstance(peptideSet, true).clearLinks();
			numPeptides += peptideSet.size();
			final Map<String, Set<Protein>> proteinMap = getProteinMap(peptideSet);
			for (final Set<Protein> proteinSet : proteinMap.values()) {
				QueriableProteinSet.getInstance(proteinSet, true).clearLinks();
				numProteins += proteinSet.size();
			}
			totalProteinMap.putAll(proteinMap);
		}

		log.info("proteinPeptideLinks cleared for  " + numProteins + " proteins and " + numPeptides
				+ " Peptides doing: peptides->proteins");

		final Map<String, Set<LinkBetweenQueriableProteinSetAndPeptideSet>> linkMapByProteinAcc = new THashMap<String, Set<LinkBetweenQueriableProteinSetAndPeptideSet>>();
		final Set<LinkBetweenQueriableProteinSetAndPeptideSet> ret = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();

		// proteins -> peptides
		for (final Set<Protein> proteinSet : totalProteinMap.values()) {
			final Set<String> peptideFullSequences = getPeptideMap(proteinSet).keySet();
			for (final String peptideFullSequence : peptideFullSequences) {
				final Set<Peptide> peptideSet = totalPeptideMap.get(peptideFullSequence);

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
			final Set<Peptide> peptideSet = totalPeptideMap.get(peptideFullSequence);
			final Set<String> proteinAccs = getProteinMap(peptideSet).keySet();
			for (final String proteinAcc : proteinAccs) {
				final Set<Protein> proteinSet = totalProteinMap.get(proteinAcc);
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

	private static Map<String, Set<Protein>> getProteinMap(Set<Peptide> peptideSet) {
		final Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
		for (final Peptide peptide : peptideSet) {
			final Set<Protein> proteins = peptide.getProteins();
			for (final Protein protein : proteins) {
				if (map.containsKey(protein.getAcc())) {
					map.get(protein.getAcc()).add(protein);
				} else {
					final Set<Protein> set = new THashSet<Protein>();
					set.add(protein);
					map.put(protein.getAcc(), set);
				}
			}
		}

		return map;
	}
}
