package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

public class QueriableProtein2PSMLink implements LinkBetweenProteinAndPSM {
	private static final Logger log = Logger.getLogger(QueriableProtein2PSMLink.class);
	private final QueriableProtein queriableProtein;
	private final QueriablePsm queriablePsm;
	private Set<QueriableProtein2PSMLink> linkSet;
	private Set<QueriableProtein2PSMLink> linkSetForSameProtein;

	public QueriableProtein2PSMLink(Protein protein, Psm psm) {
		this(QueriableProtein.getInstance(protein, false), QueriablePsm.getInstance(psm, false));
	}

	public QueriableProtein2PSMLink(QueriableProtein queriableProtein, QueriablePsm psm) {
		this.queriableProtein = queriableProtein;
		queriablePsm = psm;

		this.queriableProtein.getLinks().add(this);
		queriablePsm.getProteinLinks().add(this);

	}

	/**
	 * @return the protein
	 */
	@Override
	public QueriableProtein getQueriableProtein() {
		return queriableProtein;
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
	 * Removes the {@link QueriableProtein2PSMLink} from the
	 * {@link QueriableProteinSet} and {@link QueriablePsm}
	 */
	public void detachFromProteinAndPSM() {
		queriableProtein.removeLink(this);
		queriablePsm.removeProteinLink(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return hashCode() + ": Link between " + queriableProtein + " and " + queriablePsm;
	}

	/**
	 * Get the {@link Protein} in the {@link QueriableProtein2PSMLink}
	 *
	 * @return
	 */
	public Protein getProtein() {
		return queriableProtein.getProtein();
	}

	/**
	 * Gets the set of {@link Peptide} in the {@link QueriableProtein2PSMLink}
	 *
	 * @return
	 */
	public Set<Peptide> getPeptides() {
		return queriableProtein.getPeptides();
	}

	/**
	 * @return the linkSetForSameProtein
	 */
	public Set<QueriableProtein2PSMLink> getLinkSetForSameProtein() {
		return linkSetForSameProtein;
	}

	/**
	 * @param linkSetForSameProtein
	 *            the linkSetForSameProtein to set
	 */
	public void setLinkSetForSameProtein(Set<QueriableProtein2PSMLink> linkSetForSameProtein) {
		this.linkSetForSameProtein = linkSetForSameProtein;
	}

}
