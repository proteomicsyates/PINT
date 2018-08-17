package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class QueryResult {
	private final static Logger log = Logger.getLogger(QueryResult.class);
	private final Collection<LinkBetweenQueriableProteinSetAndPSM> validLinksBetweenProteinsAndPSMs;
	private final Collection<LinkBetweenQueriableProteinSetAndPeptideSet> validLinksBetweenProteinsAndPeptides;

	private Map<String, QueriableProteinSet> proteinMap;
	private Map<String, QueriablePeptideSet> peptideMap;
	private Set<QueriablePsm> psmSet;

	/**
	 * either validLinksBetweenProteinsAndPSMs or
	 * validLinksBetweenProteinsAndPeptides should be null
	 * 
	 * @param validLinksBetweenProteinsAndPSMs
	 * @param validLinksBetweenProteinsAndPeptides
	 * @param invalidLinksBetweenProteinsAndPSMs
	 */
	public QueryResult(Collection<LinkBetweenQueriableProteinSetAndPSM> validLinksBetweenProteinsAndPSMs,
			Collection<LinkBetweenQueriableProteinSetAndPeptideSet> validLinksBetweenProteinsAndPeptides,
			Collection<LinkBetweenQueriableProteinSetAndPSM> invalidLinksBetweenProteinsAndPSMs,
			Collection<LinkBetweenQueriableProteinSetAndPeptideSet> invalidLinksBetweenProteinsAndPeptides) {
		this.validLinksBetweenProteinsAndPSMs = validLinksBetweenProteinsAndPSMs;
		this.validLinksBetweenProteinsAndPeptides = validLinksBetweenProteinsAndPeptides;
		if (invalidLinksBetweenProteinsAndPSMs != null) {
			for (final LinkBetweenQueriableProteinSetAndPSM invalidLink : invalidLinksBetweenProteinsAndPSMs) {
				// removed the 15 Ago 2018 because it is very expensive and
				// dangerous to remove proteins and psms from each other
				// invalidLink.invalidateLink();
			}
		}
		if (invalidLinksBetweenProteinsAndPeptides != null) {
			for (final LinkBetweenQueriableProteinSetAndPeptideSet invalidLink : invalidLinksBetweenProteinsAndPeptides) {
				// removed the 15 Ago 2018 because it is very expensive and
				// dangerous to remove proteins and peptides from each other
				// invalidLink.invalidateLink();
			}
		}
	}

	/**
	 * @return the proteins
	 */
	public Map<String, QueriableProteinSet> getProteins() {
		if (proteinMap == null) {
			if (validLinksBetweenProteinsAndPSMs != null) {
				log.info("Getting proteins from the query Result, from " + validLinksBetweenProteinsAndPSMs.size()
						+ " protein-PSM links");
				proteinMap = new THashMap<String, QueriableProteinSet>();
				final Iterator<LinkBetweenQueriableProteinSetAndPSM> linksIterator = validLinksBetweenProteinsAndPSMs
						.iterator();
				while (linksIterator.hasNext()) {
					final LinkBetweenQueriableProteinSetAndPSM link = linksIterator.next();
					final QueriableProteinSet queriableProtein = link.getQueriableProtein();
					if (!queriableProtein.getLinksToPSMs().isEmpty()) {
						addToMap(proteinMap, queriableProtein);
					} else {
						throw new IllegalArgumentException("That should not happen");
					}
				}
			} else if (validLinksBetweenProteinsAndPeptides != null) {
				log.info("Getting proteins from the query Result, from " + validLinksBetweenProteinsAndPeptides.size()
						+ " protein-peptide links");
				proteinMap = new THashMap<String, QueriableProteinSet>();
				final Iterator<LinkBetweenQueriableProteinSetAndPeptideSet> linksIterator = validLinksBetweenProteinsAndPeptides
						.iterator();
				while (linksIterator.hasNext()) {
					final LinkBetweenQueriableProteinSetAndPeptideSet link = linksIterator.next();
					final QueriableProteinSet queriableProtein = link.getQueriableProtein();
					if (!queriableProtein.getLinksToPeptides().isEmpty()) {
						addToMap(proteinMap, queriableProtein);
					} else {
						throw new IllegalArgumentException("That should not happen");
					}
				}
			}

		}
		return proteinMap;
	}

	/**
	 * @return the proteins
	 */
	public Map<String, QueriablePeptideSet> getPeptides() {
		if (peptideMap == null) {
			if (validLinksBetweenProteinsAndPeptides != null) {
				log.info("Getting proteins from the query Result, from " + validLinksBetweenProteinsAndPeptides.size()
						+ " protein-peptide links");
				peptideMap = new THashMap<String, QueriablePeptideSet>();
				final Iterator<LinkBetweenQueriableProteinSetAndPeptideSet> linksIterator = validLinksBetweenProteinsAndPeptides
						.iterator();
				while (linksIterator.hasNext()) {
					final LinkBetweenQueriableProteinSetAndPeptideSet link = linksIterator.next();
					final QueriablePeptideSet queriablePeptide = link.getQueriablePeptide();
					if (!queriablePeptide.getLinksToProteins().isEmpty()) {
						addToMap(peptideMap, queriablePeptide);
					} else {
						throw new IllegalArgumentException("That should not happen");
					}
				}
			}

		}
		return peptideMap;
	}

	private static void addToMap(Map<String, QueriableProteinSet> map, QueriableProteinSet queriableProtein) {
		if (map != null) {
			final String primaryAcc = queriableProtein.getPrimaryAccession();
			if (!map.containsKey(primaryAcc)) {
				map.put(primaryAcc, queriableProtein);
			}
		}
	}

	private static void addToMap(Map<String, QueriablePeptideSet> map, QueriablePeptideSet queriablePeptide) {
		if (map != null) {
			final String fullSequence = queriablePeptide.getFullSequence();
			if (map.containsKey(fullSequence)) {
				map.put(fullSequence, queriablePeptide);
			}
		}
	}

	public Set<QueriablePsm> getPsms() {
		if (psmSet == null) {
			if (validLinksBetweenProteinsAndPSMs != null) {
				log.info("Getting queriable PSMs from query result from " + validLinksBetweenProteinsAndPSMs.size()
						+ " protein-PSM links");

				psmSet = new THashSet<QueriablePsm>();
				for (final LinkBetweenQueriableProteinSetAndPSM link : validLinksBetweenProteinsAndPSMs) {
					final QueriablePsm queriablePsm = link.getQueriablePsm();
					psmSet.add(queriablePsm);
				}
			} else if (validLinksBetweenProteinsAndPeptides != null) {
				log.info("Getting queriable PSMs from query result from " + validLinksBetweenProteinsAndPeptides.size()
						+ " protein-peptide links");

				psmSet = new THashSet<QueriablePsm>();
				for (final LinkBetweenQueriableProteinSetAndPeptideSet link : validLinksBetweenProteinsAndPeptides) {
					final Set<Psm> psms = link.getQueriablePeptide().getPsms();
					for (final Psm psm : psms) {
						psmSet.add(QueriablePsm.getInstance(psm, false));
					}
				}
			}
		}
		return psmSet;
	}

}
