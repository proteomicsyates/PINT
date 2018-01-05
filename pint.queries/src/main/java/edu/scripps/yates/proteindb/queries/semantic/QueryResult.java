package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class QueryResult {
	private final static Logger log = Logger.getLogger(QueryResult.class);
	private final Collection<LinkBetweenQueriableProteinSetAndPSM> validLinks;
	private Map<String, Set<QueriableProteinSet>> proteinMap;
	private Map<String, Set<QueriablePsm>> psmMap;
	private Set<QueriablePsm> psmSet;
	private Map<String, Set<Peptide>> peptideMap;

	public QueryResult(Collection<LinkBetweenQueriableProteinSetAndPSM> validLinks,
			Collection<LinkBetweenQueriableProteinSetAndPSM> invalidLinks) {
		this.validLinks = validLinks;
		for (LinkBetweenQueriableProteinSetAndPSM invalidLink : invalidLinks) {
			invalidLink.invalidateLink();
		}
	}

	/**
	 * @return the proteins
	 */
	public Map<String, Set<QueriableProteinSet>> getProteins() {
		if (proteinMap == null) {
			log.info("Getting proteins from the query Result, from " + validLinks.size() + " links");
			proteinMap = new THashMap<String, Set<QueriableProteinSet>>();
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
			log.info("Getting peptides from query result from " + validLinks.size() + " links");
			peptideMap = new THashMap<String, Set<Peptide>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
				final Peptide peptide = link.getPeptide();
				if (peptideMap.containsKey(peptide.getSequence())) {
					peptideMap.get(peptide.getSequence()).add(peptide);
				} else {
					Set<Peptide> set = new THashSet<Peptide>();
					set.add(peptide);
					peptideMap.put(peptide.getSequence(), set);
				}
			}

		}
		return peptideMap;
	}

	private static void addToMap(Map<String, Set<QueriableProteinSet>> map, QueriableProteinSet queriableProtein) {
		if (map != null) {
			String primaryAcc = queriableProtein.getPrimaryAccession();
			if (map.containsKey(primaryAcc)) {
				map.get(primaryAcc).add(queriableProtein);
			} else {
				Set<QueriableProteinSet> set = new THashSet<QueriableProteinSet>();
				set.add(queriableProtein);
				map.put(primaryAcc, set);
			}
		}
	}

	public Map<String, Set<QueriablePsm>> getPsmMap() {
		if (psmMap == null) {
			log.info("Getting PSMs from query result from " + validLinks.size() + " links");
			psmMap = new THashMap<String, Set<QueriablePsm>>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
				final String sequence = link.getQueriablePsm().getPsm().getSequence();
				if (psmMap.containsKey(sequence)) {
					psmMap.get(sequence).add(link.getQueriablePsm());
				} else {
					Set<QueriablePsm> set = new THashSet<QueriablePsm>();
					set.add(link.getQueriablePsm());
					psmMap.put(sequence, set);
				}

			}
		}
		return psmMap;
	}

	public Set<QueriablePsm> getPsms() {
		if (psmSet == null) {
			log.info("Getting queriable PSMs from query result from " + validLinks.size() + " links");

			psmSet = new THashSet<QueriablePsm>();
			for (LinkBetweenQueriableProteinSetAndPSM link : validLinks) {
				final QueriablePsm queriablePsm = link.getQueriablePsm();
				psmSet.add(queriablePsm);
			}
		}
		return psmSet;
	}

}
