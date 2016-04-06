package edu.scripps.yates.proteindb.queries.semantic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;

public class QueriablePsm {
	private Psm psm;
	private final Set<QueriableProteinSet2PSMLink> proteinSetLinks = new HashSet<QueriableProteinSet2PSMLink>();
	private final Set<QueriableProtein2PSMLink> proteinLinks = new HashSet<QueriableProtein2PSMLink>();
	private static final Map<Integer, QueriablePsm> map = new HashMap<Integer, QueriablePsm>();
	private final static Logger log = Logger.getLogger(QueriablePsm.class);

	public static QueriablePsm getInstance(Psm psm, boolean forceToCreateNewObject) {
		if (!forceToCreateNewObject && map.containsKey(psm.getId())) {
			final QueriablePsm queriablePsm = map.get(psm.getId());
			return queriablePsm;

		} else {
			QueriablePsm queriablePsm = new QueriablePsm(psm);
			map.put(psm.getId(), queriablePsm);
			return queriablePsm;
		}
	}

	private QueriablePsm(Psm psm) {
		this.psm = psm;
	}

	/**
	 * @return the psm
	 */
	public Psm getPsm() {
		if (!ContextualSessionHandler.getSession().contains(psm)) {
			psm = (Psm) ContextualSessionHandler.getSession().merge(psm);
		}
		return psm;
	}

	/**
	 * @return the links
	 */
	public Set<QueriableProteinSet2PSMLink> getProteinSetLinks() {
		return proteinSetLinks;
	}

	/**
	 * @return the links
	 */
	public Set<QueriableProtein2PSMLink> getProteinLinks() {
		return proteinLinks;
	}

	public void removeProteinSetLink(QueriableProteinSet2PSMLink link) {
		boolean removed = proteinSetLinks.remove(link);
		if (!removed)
			log.warn("BAD");
	}

	public void removeProteinLink(QueriableProtein2PSMLink link) {
		boolean removed = proteinLinks.remove(link);
		if (!removed)
			log.warn("BAD");
	}

	public Set<Organism> getOrganisms() {
		Set<Organism> ret = new HashSet<Organism>();
		final Set<Protein> proteins = getPsm().getProteins();
		for (Protein protein : proteins) {
			ret.add(protein.getOrganism());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPsm().getSequence();
	}

	public Set<Condition> getConditions() {
		return getPsm().getConditions();
	}

	public String getSequence() {
		return getPsm().getSequence();
	}
}
