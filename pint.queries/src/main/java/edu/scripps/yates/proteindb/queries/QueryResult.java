package edu.scripps.yates.proteindb.queries;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinInterface;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;

public class QueryResult {
	private final static Logger log = Logger.getLogger(QueryResult.class);
	private final Collection<LinkBetweenQueriableProteinSetAndPSM> links;
	private Map<String, Set<QueriableProteinInterface>> proteinMap;
	private Map<String, Set<QueriablePsm>> psmMap;
	private Set<QueriablePsm> psmSet;
	private HashMap<String, Set<Peptide>> peptideMap;

	public QueryResult(Collection<LinkBetweenQueriableProteinSetAndPSM> links) {
		this.links = links;
		log.info("Polishing results from " + links.size() + " links");
		Set<Psm> validPSMs = getPSMSetFromLinks(links);
		// go for all items at protein level and remove all the psms not in the
		// valid psmSet
		log.info("Collecting psms to detach...");
		Set<Psm> psmsToDetach = new HashSet<Psm>();
		for (LinkBetweenQueriableProteinSetAndPSM link : links) {
			final Set<Protein> proteins = link.getProtein();
			for (Protein protein : proteins) {
				final Iterator<Psm> psmIterator = protein.getPsms().iterator();
				while (psmIterator.hasNext()) {
					final Psm psm = psmIterator.next();
					if (!validPSMs.contains(psm)) {
						psmsToDetach.add(psm);
					}
				}
			}
		}
		log.info(psmsToDetach.size() + " psms to detach.");
		// detach PSMs
		log.info("Detaching " + psmsToDetach.size() + " PSMs");
		int num = 0;
		for (Psm psmToDetach : psmsToDetach) {
			log.info("Detaching " + ++num + "/" + psmsToDetach.size() + " PSMs");
			PersistenceUtils.detachPSM(psmToDetach, false, false, false);
		}
		log.info("Detached PSMs finished");

		log.info("Collecting proteins to detach...");
		Set<Protein> proteinsToDetach = new HashSet<Protein>();
		Set<Protein> validProteins = getProteinSetFromLinks(links);
		for (Psm psm : validPSMs) {
			final Set<Protein> proteins = psm.getProteins();
			for (Protein protein : proteins) {
				if (!validProteins.contains(protein)) {
					proteinsToDetach.add(protein);
				}
			}
		}
		log.info(proteinsToDetach.size() + " proteins to detach."); // detach
																	// proteins
		log.info("Detaching " + proteinsToDetach.size() + " proteins");
		num = 0;
		for (Protein protein : proteinsToDetach) {
			log.info("Detaching " + ++num + "/" + proteinsToDetach.size() + " proteins");
			PersistenceUtils.detachProtein(protein, false, false, false);
		}
		log.info("Detached proteins finished");
		log.info("Removing links now...");
		int linksRemovedBecauseOfProteinsWithNoPSMs = 0;
		int linksRemovedBecauseOfPSMsWithNoProteins = 0;
		Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = links.iterator();
		while (linksIterator.hasNext()) {
			LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
			if (link.getQueriableProtein().getPsms().isEmpty()) {
				// remove the link
				linksIterator.remove();
				linksRemovedBecauseOfProteinsWithNoPSMs++;
			}
			if (link.getQueriablePsm().getPsm().getProteins().isEmpty()) {
				// remove the link
				linksIterator.remove();
				linksRemovedBecauseOfPSMsWithNoProteins++;
			}
		}
		log.info(linksRemovedBecauseOfProteinsWithNoPSMs + linksRemovedBecauseOfPSMsWithNoProteins + " links removed");
		log.info(linksRemovedBecauseOfProteinsWithNoPSMs + " proteins with no PSMs");
		log.info(linksRemovedBecauseOfPSMsWithNoProteins + " PSMs with no Proteins");

	}

	private Set<Protein> getProteinSetFromLinks(Collection<LinkBetweenQueriableProteinSetAndPSM> links2) {
		Set<Protein> ret = new HashSet<Protein>();
		log.info("Getting proteins from " + links2.size() + " links");

		for (LinkBetweenQueriableProteinSetAndPSM link : links2) {
			for (Protein protein : link.getProtein()) {
				if (protein.getPsms().isEmpty()) {
					log.info("Protein with no psms: Protein id: " + protein.getId());
				} else {
					ret.add(protein);
				}
			}
		}
		log.info(ret.size() + " proteins collected");

		return ret;
	}

	private Set<Psm> getPSMSetFromLinks(Collection<LinkBetweenQueriableProteinSetAndPSM> links2) {
		log.info("Getting psms from " + links2.size() + " links");
		HashSet<Psm> ret = new HashSet<Psm>();
		for (LinkBetweenQueriableProteinSetAndPSM link : links2) {
			if (link.getQueriablePsm().getPsm().getProteins().isEmpty()) {
				log.info("queriable psm with no proteins: PSM id: " + link.getQueriablePsm().getPsm().getId());
			} else {
				ret.add(link.getQueriablePsm().getPsm());
			}
		}
		log.info(ret.size() + " psms collected");
		return ret;
	}

	// private void asdf(LinkBetweenProteinAndPSM link) {
	//
	// // remove the psm from the peptide
	// final QueriablePsm queriablePsm = link.getQueriablePsm();
	// final Psm psm = queriablePsm.getPsm();
	// boolean deleted = psm.getPeptide().getPsms().remove(psm);
	// if (!deleted) {
	// log.info("BAD");
	// }
	// // if the peptide has no psms anymore:
	// if (psm.getPeptide().getPsms().isEmpty()) {
	// // remove the peptide from the protein
	// final QueriableProteinSet queriableProtein = link.getQueriableProtein();
	// final Set<Protein> proteins = queriableProtein.getProteins();
	// boolean someDeleted = false;
	// for (Protein protein : proteins) {
	// boolean deleted2 = protein.getPeptides().remove(psm.getPeptide());
	// if (deleted2) {
	// someDeleted = true;
	// }
	// }
	// if (!someDeleted) {
	// log.info("BAD");
	// }
	// }
	// // remove the psm from the protein
	// final QueriableProteinSet queriableProtein = link.getQueriableProtein();
	// final Set<Protein> proteins = queriableProtein.getProteins();
	// boolean someDeleted = false;
	// for (Protein protein : proteins) {
	// boolean deleted2 = protein.getPsms().remove(psm);
	// if (deleted2) {
	// someDeleted = true;
	// }
	// // if the protein has no psms anymore:
	// if (protein.getPsms().isEmpty()) {
	// // remove the protein from the peptide
	// boolean deleted3 = psm.getPeptide().getProteins().remove(protein);
	// if (!deleted3) {
	// log.info("BAD");
	// }
	// }
	// }
	// if (!someDeleted) {
	// log.info("BAD");
	// }
	// }

	/**
	 * @return the proteins
	 */
	public Map<String, Set<QueriableProteinInterface>> getProteins() {
		if (proteinMap == null) {
			proteinMap = new HashMap<String, Set<QueriableProteinInterface>>();
			final Set<QueriablePsm> queriablePsmsInResult = getPsms();
			Set<Psm> psmsInResult = new HashSet<Psm>();
			for (QueriablePsm queriablePsm : queriablePsmsInResult) {
				psmsInResult.add(queriablePsm.getPsm());
			}
			final Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = links.iterator();
			while (linksIterator.hasNext()) {
				LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
				final QueriableProteinSet queriableProtein = link.getQueriableProtein();
				final Set<Psm> psmsFromProtein = queriableProtein.getPsms();
				for (Psm psmFromProtein : psmsFromProtein) {
					if (!psmsInResult.contains(psmFromProtein)) {
						queriableProtein.remove(psmFromProtein);
					}
				}
				if (!queriableProtein.getPsms().isEmpty()) {
					addToMap(proteinMap, queriableProtein);
				} else {
					// remove the link
					linksIterator.remove();
				}
			}

		}
		return proteinMap;
	}

	public Map<String, Set<Protein>> getProteins2() {
		final Map<String, Set<QueriableProteinInterface>> proteins = getProteins();
		Map<String, Set<Protein>> ret = new HashMap<String, Set<Protein>>();
		for (String acc : proteins.keySet()) {
			final Set<QueriableProteinInterface> set = proteins.get(acc);
			Set<Protein> proteinSet = new HashSet<Protein>();
			for (QueriableProteinInterface protein : set) {
				proteinSet.addAll(protein.getProteins());
			}
			PersistenceUtils.addToMapByPrimaryAcc(ret, proteinSet);
		}
		return ret;
	}

	// /**
	// * @return the proteins
	// */
	// public Map<String, Set<QueriableProteinInterface>> getProteins() {
	// if (proteinMap == null) {
	// proteinMap = new HashMap<String, Set<QueriableProteinInterface>>();
	// for (QueriableProteinSet2PSMLink link : links) {
	// final Set<QueriableProtein2PSMLink> protein2PsmResult =
	// link.getProtein2PsmResult(true);
	// if (protein2PsmResult != null) {
	// for (QueriableProtein2PSMLink queriableProtein2PSMLink :
	// protein2PsmResult) {
	// addToMap(proteinMap, queriableProtein2PSMLink.getQueriableProtein());
	// }
	// }
	// }
	// }
	// return proteinMap;
	// }
	/**
	 * @return the peptides
	 */
	public Map<String, Set<Peptide>> getPeptides() {
		if (peptideMap == null) {
			// call first to get proteins because maybe some links will be
			// removed
			getProteins();
			peptideMap = new HashMap<String, Set<Peptide>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : links) {

				final Set<Peptide> peptides = link.getPeptides();
				for (Peptide peptide : peptides) {
					if (peptideMap.containsKey(peptide.getSequence())) {
						peptideMap.get(peptide.getSequence()).add(peptide);
					} else {
						Set<Peptide> set = new HashSet<Peptide>();
						set.add(peptide);
						peptideMap.put(peptide.getSequence(), set);
					}
				}

			}
		}
		return peptideMap;
	}

	// /**
	// * @return the peptides
	// */
	// public Map<String, Set<Peptide>> getPeptides() {
	// if (peptideMap == null) {
	// peptideMap = new HashMap<String, Set<Peptide>>();
	// for (QueriableProteinSet2PSMLink link : links) {
	// final Set<QueriableProtein2PSMLink> validProtein2PsmResults =
	// link.getProtein2PsmResult(true);
	// if (validProtein2PsmResults != null) {
	// for (QueriableProtein2PSMLink queriableProtein2PSMLink :
	// validProtein2PsmResults) {
	// final Set<Peptide> peptides = queriableProtein2PSMLink.getPeptides();
	// for (Peptide peptide : peptides) {
	// if (peptideMap.containsKey(peptide.getSequence())) {
	// peptideMap.get(peptide.getSequence()).add(peptide);
	// } else {
	// Set<Peptide> set = new HashSet<Peptide>();
	// set.add(peptide);
	// peptideMap.put(peptide.getSequence(), set);
	// }
	// }
	// }
	// }
	// }
	// }
	// return peptideMap;
	// }

	private static void addToMap(Map<String, Set<QueriableProteinInterface>> map,
			QueriableProteinInterface queriableProtein) {
		Set<Psm> psms = queriableProtein.getPsms();
		if (map != null) {
			String primaryAcc = queriableProtein.getPrimaryAccession();
			if (map.containsKey(primaryAcc)) {
				map.get(primaryAcc).add(queriableProtein);
			} else {
				Set<QueriableProteinInterface> set = new HashSet<QueriableProteinInterface>();
				set.add(queriableProtein);
				map.put(primaryAcc, set);
			}
		}
	}

	public Map<String, Set<QueriablePsm>> getPsmMap() {
		if (psmMap == null) {
			// call first to get proteins because maybe some links will be
			// removed
			getProteins();
			psmMap = new HashMap<String, Set<QueriablePsm>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : links) {

				final String sequence = link.getQueriablePsm().getPsm().getSequence();
				if (psmMap.containsKey(sequence)) {
					psmMap.get(sequence).add(link.getQueriablePsm());
				} else {
					Set<QueriablePsm> set = new HashSet<QueriablePsm>();
					set.add(link.getQueriablePsm());
					psmMap.put(sequence, set);
				}

			}
		}
		return psmMap;
	}
	// public Map<String, Set<QueriablePsm>> getPsmMap() {
	// if (psmMap == null) {
	// psmMap = new HashMap<String, Set<QueriablePsm>>();
	// for (QueriableProteinSet2PSMLink link : links) {
	// final Set<QueriableProtein2PSMLink> validProtein2PsmResults =
	// link.getProtein2PsmResult(true);
	// if (validProtein2PsmResults != null) {
	// for (QueriableProtein2PSMLink queriableProtein2PSMLink :
	// validProtein2PsmResults) {
	// final String sequence =
	// queriableProtein2PSMLink.getQueriablePsm().getPsm().getSequence();
	// if (psmMap.containsKey(sequence)) {
	// psmMap.get(sequence).add(queriableProtein2PSMLink.getQueriablePsm());
	// } else {
	// Set<QueriablePsm> set = new HashSet<QueriablePsm>();
	// set.add(queriableProtein2PSMLink.getQueriablePsm());
	// psmMap.put(sequence, set);
	// }
	// }
	// }
	// }
	// }
	// return psmMap;
	// }

	// public Set<QueriablePsm> getPsms() {
	// if (psmSet == null) {
	// psmSet = new HashSet<QueriablePsm>();
	// for (QueriableProteinSet2PSMLink link : links) {
	// final Set<QueriableProtein2PSMLink> validProtein2PsmResult =
	// link.getProtein2PsmResult(true);
	// if (validProtein2PsmResult != null) {
	// for (QueriableProtein2PSMLink queriableProtein2PSMLink :
	// validProtein2PsmResult) {
	// psmSet.add(queriableProtein2PSMLink.getQueriablePsm());
	// }
	// }
	// }
	// }
	// return psmSet;
	// }
	public Set<QueriablePsm> getPsms() {
		if (psmSet == null) {
			// call first to get proteins because maybe some links will be
			// removed
			getProteins();
			psmSet = new HashSet<QueriablePsm>();
			for (LinkBetweenQueriableProteinSetAndPSM link : links) {
				final QueriablePsm queriablePsm = link.getQueriablePsm();
				psmSet.add(queriablePsm);
			}
		}
		return psmSet;
	}
}
