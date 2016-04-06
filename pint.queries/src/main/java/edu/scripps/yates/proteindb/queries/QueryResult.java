package edu.scripps.yates.proteindb.queries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinInterface;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet2PSMLink;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;

public class QueryResult {

	private boolean aProteinAmountSpectraCountIsInvolved;
	private final List<AbstractQuery> specialQueries = new ArrayList<AbstractQuery>();
	private final Collection<QueriableProteinSet2PSMLink> links;
	private Map<String, Set<QueriableProteinInterface>> proteinMap;
	private Map<String, Set<QueriablePsm>> psmMap;
	private Set<QueriablePsm> psmSet;
	private HashMap<String, Set<Peptide>> peptideMap;

	public QueryResult(Collection<QueriableProteinSet2PSMLink> links) {
		this.links = links;
	}

	/**
	 * @return the proteins
	 */
	public Map<String, Set<QueriableProteinInterface>> getProteins() {
		if (proteinMap == null) {
			proteinMap = new HashMap<String, Set<QueriableProteinInterface>>();
			for (QueriableProteinSet2PSMLink link : links) {

				addToMap(proteinMap, link.getQueriableProtein());

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
			peptideMap = new HashMap<String, Set<Peptide>>();
			for (QueriableProteinSet2PSMLink link : links) {

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
			psmMap = new HashMap<String, Set<QueriablePsm>>();
			for (QueriableProteinSet2PSMLink link : links) {

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
			psmSet = new HashSet<QueriablePsm>();
			for (QueriableProteinSet2PSMLink link : links) {
				psmSet.add(link.getQueriablePsm());
			}
		}
		return psmSet;
	}
}
