package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

public class LinkBetweenQueriableProteinSetAndPSM {
	private static final Logger log = Logger.getLogger(LinkBetweenQueriableProteinSetAndPSM.class);
	private final QueriableProteinSet queriableProteinSet;
	private final QueriablePsm queriablePsm;
	// private final Map<Boolean, Set<QueriableProtein2PSMLink>>
	// evaluatedProtein2PSMLinks = new HashMap<Boolean,
	// Set<QueriableProtein2PSMLink>>();
	private Set<LinkBetweenQueriableProteinSetAndPSM> linkSetForSameProtein;

	public LinkBetweenQueriableProteinSetAndPSM(Collection<Protein> proteins, Psm psm) {
		this(QueriableProteinSet.getInstance(proteins, false), QueriablePsm.getInstance(psm, false));
	}

	public LinkBetweenQueriableProteinSetAndPSM(QueriableProteinSet queriableProteinSet, QueriablePsm queriablePsm) {
		this.queriableProteinSet = queriableProteinSet;
		this.queriablePsm = queriablePsm;

		this.queriableProteinSet.addLink(this);
		this.queriablePsm.addLink(this);

	}

	/**
	 * @return the protein
	 */

	public QueriableProteinSet getQueriableProtein() {
		return queriableProteinSet;
	}

	/**
	 * @return the psm
	 */

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
	 * Removes the {@link LinkBetweenQueriableProteinSetAndPSM} from the
	 * {@link QueriableProteinSet} and {@link QueriablePsm}
	 */
	public void detachFromProteinAndPSM() {
		queriableProteinSet.removeLink(this);
		queriablePsm.removeProteinSetLink(this);

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
	 * Get the {@link Protein} in the
	 * {@link LinkBetweenQueriableProteinSetAndPSM}
	 *
	 * @return
	 */
	public Set<Protein> getIndividualProteins() {
		return queriableProteinSet.getIndividualProteins();
	}

	/**
	 * Gets the {@link Peptide} in the
	 * {@link LinkBetweenQueriableProteinSetAndPSM}
	 *
	 * @return
	 */
	public Peptide getPeptide() {
		return queriablePsm.getPsm().getPeptide();
	}

	/**
	 * @return the linkSetForSameProtein
	 */
	public Set<LinkBetweenQueriableProteinSetAndPSM> getLinkSetForSameProtein() {
		return linkSetForSameProtein;
	}

	/**
	 * @param linkSetForSameProtein
	 *            the linkSetForSameProtein to set
	 */
	public void setLinkSetForSameProtein(Set<LinkBetweenQueriableProteinSetAndPSM> linkSetForSameProtein) {
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
