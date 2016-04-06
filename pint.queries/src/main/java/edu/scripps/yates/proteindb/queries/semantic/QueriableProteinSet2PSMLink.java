package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

public class QueriableProteinSet2PSMLink implements LinkBetweenProteinAndPSM {
	private static final Logger log = Logger.getLogger(QueriableProteinSet2PSMLink.class);
	private final QueriableProteinSet queriableProteinSet;
	private final QueriablePsm queriablePsm;
	private final Set<QueriableProtein2PSMLink> protein2PSMLinks = new HashSet<QueriableProtein2PSMLink>();
	// private final Map<Boolean, Set<QueriableProtein2PSMLink>>
	// evaluatedProtein2PSMLinks = new HashMap<Boolean,
	// Set<QueriableProtein2PSMLink>>();
	private Set<QueriableProteinSet2PSMLink> linkSetForSameProtein;

	public QueriableProteinSet2PSMLink(Collection<Protein> proteins, Psm psm) {
		this(QueriableProteinSet.getInstance(proteins, false), QueriablePsm.getInstance(psm, false));
	}

	public QueriableProteinSet2PSMLink(QueriableProteinSet queriableProteinSet, QueriablePsm queriablePsm) {
		this.queriableProteinSet = queriableProteinSet;
		this.queriablePsm = queriablePsm;

		this.queriableProteinSet.getLinks().add(this);
		this.queriablePsm.getProteinSetLinks().add(this);
		for (Protein protein : queriableProteinSet.getProteins()) {
			// make the link only if they were detected in the same msRun
			if (protein.getMsRun().equals(queriablePsm.getPsm().getMsRun())) {
				protein2PSMLinks
						.add(new QueriableProtein2PSMLink(QueriableProtein.getInstance(protein, false), queriablePsm));
			}
		}

	}

	/**
	 * @return the protein2PSMLinks
	 */
	public Set<QueriableProtein2PSMLink> getProtein2PSMLinks() {
		return protein2PSMLinks;
	}

	/**
	 * @return the protein
	 */
	@Override
	public QueriableProteinSet getQueriableProtein() {
		return queriableProteinSet;
	}

	/**
	 * @return the psm
	 */
	@Override
	public QueriablePsm getQueriablePsm() {
		return queriablePsm;
	}

	// /**
	// * Gets the {@link Protein} in the {@link ProteinPSMLink} after removing
	// the
	// * actual {@link Psm} according to its remaining {@link ProteinPSMLink}
	// *
	// * @return
	// */
	// public Protein getProteinAccordingToItsLinks() {
	// final Set<ProteinPSMLink> linksOfTheProtein =
	// queriableProtein.getLinks();
	// final Protein protein = queriableProtein.getProtein();
	// final Iterator<Psm> psmIterator = protein.getPsms().iterator();
	// while (psmIterator.hasNext()) {
	// final Psm psm = psmIterator.next();
	// boolean found = false;
	// for (ProteinPSMLink proteinPSMLink : linksOfTheProtein) {
	// if (psm.equals(proteinPSMLink.getPsm())) {
	// found = true;
	// break;
	// }
	// }
	// if (!found) {
	// // remove psm
	// psmIterator.remove();
	// }
	// }
	// return protein;
	// }

	/**
	 * Removes the {@link QueriableProteinSet2PSMLink} from the
	 * {@link QueriableProteinSet} and {@link QueriablePsm}
	 */
	public void detachFromProteinAndPSM() {
		queriableProteinSet.removeLink(this);
		queriablePsm.removeProteinSetLink(this);
		for (QueriableProtein2PSMLink link : protein2PSMLinks) {
			link.detachFromProteinAndPSM();
		}
		protein2PSMLinks.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return hashCode() + ": Link between " + queriableProteinSet + " and " + queriablePsm;
	}

	/**
	 * Get the {@link Protein} in the {@link QueriableProteinSet2PSMLink}
	 *
	 * @return
	 */
	public Set<Protein> getProtein() {
		return queriableProteinSet.getProteins();
	}

	/**
	 * Gets the set of {@link Peptide} in the
	 * {@link QueriableProteinSet2PSMLink}
	 *
	 * @return
	 */
	public Set<Peptide> getPeptides() {
		return queriableProteinSet.getPeptides();
	}

	/**
	 * @return the linkSetForSameProtein
	 */
	public Set<QueriableProteinSet2PSMLink> getLinkSetForSameProtein() {
		return linkSetForSameProtein;
	}

	/**
	 * @param linkSetForSameProtein
	 *            the linkSetForSameProtein to set
	 */
	public void setLinkSetForSameProtein(Set<QueriableProteinSet2PSMLink> linkSetForSameProtein) {
		this.linkSetForSameProtein = linkSetForSameProtein;
	}

	// public void setProtein2PsmResult(QueriableProtein2PSMLink
	// queriableProtein2PSMLink, boolean result) {
	// if (evaluatedProtein2PSMLinks.containsKey(result)) {
	// evaluatedProtein2PSMLinks.get(result).add(queriableProtein2PSMLink);
	// } else {
	// Set<QueriableProtein2PSMLink> set = new
	// HashSet<QueriableProtein2PSMLink>();
	// set.add(queriableProtein2PSMLink);
	// evaluatedProtein2PSMLinks.put(result, set);
	// }
	// }
	//
	// public Set<QueriableProtein2PSMLink> getProtein2PsmResult(boolean valid)
	// {
	// return evaluatedProtein2PSMLinks.get(valid);
	// }

}
