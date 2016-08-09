package edu.scripps.yates.proteindb.queries.semantic;

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

public class QueryResult {
	private final static Logger log = Logger.getLogger(QueryResult.class);
	private final Collection<LinkBetweenQueriableProteinSetAndPSM> validLinks;
	private Map<String, Set<QueriableProteinSet>> proteinMap;
	private Map<String, Set<QueriablePsm>> psmMap;
	private Set<QueriablePsm> psmSet;
	private HashMap<String, Set<Peptide>> peptideMap;
	private HashSet<Psm> individualPSMs;
	private final Collection<LinkBetweenQueriableProteinSetAndPSM> invalidLinks;

	public QueryResult(Collection<LinkBetweenQueriableProteinSetAndPSM> validLinks,
			Collection<LinkBetweenQueriableProteinSetAndPSM> invalidLinks) {
		this.validLinks = validLinks;
		this.invalidLinks = invalidLinks;
		for (LinkBetweenQueriableProteinSetAndPSM invalidLink : invalidLinks) {
			invalidLink.invalidateLink();
		}
	}

	public QueryResult(Collection<LinkBetweenQueriableProteinSetAndPSM> links, boolean b) {
		validLinks = links;
		invalidLinks = links;
		log.info("Polishing results from " + links.size() + " links");
		Set<Psm> validPSMs = getIndividualPSMs();
		// go for all items at protein level and remove all the psms not in the
		// valid psmSet
		log.info("Collecting psms to detach...");
		Set<Psm> psmsToDetach = new HashSet<Psm>();
		for (LinkBetweenQueriableProteinSetAndPSM link : links) {
			final Set<Protein> proteins = link.getIndividualProteins();
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
			for (Protein protein : link.getIndividualProteins()) {
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

	/**
	 * @return the proteins
	 */
	public Map<String, Set<QueriableProteinSet>> getProteins() {
		if (proteinMap == null) {
			proteinMap = new HashMap<String, Set<QueriableProteinSet>>();
			final Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = validLinks.iterator();
			while (linksIterator.hasNext()) {
				LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
				final QueriableProteinSet queriableProtein = link.getQueriableProtein();
				if (!queriableProtein.getLinks().isEmpty()) {
					addToMap(proteinMap, queriableProtein);
				} else {
					throw new IllegalArgumentException("That should not happen");
				}
			}

		}
		return proteinMap;
	}

	/**
	 * @return the peptides
	 */
	public Map<String, Set<Peptide>> getPeptides() {
		if (peptideMap == null) {
			peptideMap = new HashMap<String, Set<Peptide>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
				final Peptide peptide = link.getPeptide();
				if (peptideMap.containsKey(peptide.getSequence())) {
					peptideMap.get(peptide.getSequence()).add(peptide);
				} else {
					Set<Peptide> set = new HashSet<Peptide>();
					set.add(peptide);
					peptideMap.put(peptide.getSequence(), set);
				}
			}
		}
		return peptideMap;
	}

	private static void addToMap(Map<String, Set<QueriableProteinSet>> map, QueriableProteinSet queriableProtein) {
		Set<Psm> psms = queriableProtein.getPsms();
		if (map != null) {
			String primaryAcc = queriableProtein.getPrimaryAccession();
			if (map.containsKey(primaryAcc)) {
				map.get(primaryAcc).add(queriableProtein);
			} else {
				Set<QueriableProteinSet> set = new HashSet<QueriableProteinSet>();
				set.add(queriableProtein);
				map.put(primaryAcc, set);
			}
		}
	}

	public Map<String, Set<QueriablePsm>> getPsmMap() {
		if (psmMap == null) {
			psmMap = new HashMap<String, Set<QueriablePsm>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
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

	public Set<QueriablePsm> getPsms() {
		if (psmSet == null) {
			psmSet = new HashSet<QueriablePsm>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
				final QueriablePsm queriablePsm = link.getQueriablePsm();
				psmSet.add(queriablePsm);
			}
		}
		return psmSet;
	}

	/**
	 * An individual {@link Protein} is valid if contains at least one Psm in
	 * the Set of individual psms
	 *
	 * @param protein
	 * @return
	 */
	private boolean isValidIndividualProtein(Protein protein) {
		final Iterator<Psm> iterator = protein.getPsms().iterator();
		while (iterator.hasNext()) {
			if (getIndividualPSMs().contains(iterator.next())) {
				return true;
			}
		}
		return false;
	}

	private Set<Psm> getIndividualPSMs() {
		if (individualPSMs == null) {
			log.info("Getting individual PSMs from " + validLinks.size() + " links");
			individualPSMs = new HashSet<Psm>();
			for (QueriablePsm queriablePSM : getPsms()) {
				individualPSMs.add(queriablePSM.getPsm());
			}
			log.info(individualPSMs.size() + " PSMs collected");
		}
		return individualPSMs;
	}
}
