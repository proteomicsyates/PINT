package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class QueriablePsm {
	private Psm psm;
	private final Set<LinkBetweenQueriableProteinSetAndPSM> links = new THashSet<LinkBetweenQueriableProteinSetAndPSM>();
	private static final TIntObjectHashMap<QueriablePsm> map = new TIntObjectHashMap<QueriablePsm>();
	private final static Logger log = Logger.getLogger(QueriablePsm.class);

	public static QueriablePsm getInstance(Psm psm, boolean forceToCreateNewObject) {
		if (!forceToCreateNewObject && map.containsKey(psm.getId())) {
			final QueriablePsm queriablePsm = map.get(psm.getId());
			return queriablePsm;

		} else {
			final QueriablePsm queriablePsm = new QueriablePsm(psm);
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
		if (!ContextualSessionHandler.getCurrentSession().contains(psm)) {
			psm = (Psm) ContextualSessionHandler.getCurrentSession().merge(psm);
		}
		return psm;
	}

	/**
	 * @return the links
	 */
	public Set<LinkBetweenQueriableProteinSetAndPSM> getLinks() {
		return links;
	}

	public void removeProteinSetLink(LinkBetweenQueriableProteinSetAndPSM link) {
		final boolean removed = links.remove(link);
		if (!removed)
			log.warn("BAD");
	}

	public Set<Organism> getOrganisms() {
		final Set<String> organismNames = new THashSet<String>();
		final Set<Organism> ret = new THashSet<Organism>();
		final Set<LinkBetweenQueriableProteinSetAndPSM> links = getLinks();
		for (final LinkBetweenQueriableProteinSetAndPSM link : links) {
			final Organism organism = link.getQueriableProtein().getOrganism();
			if (!organismNames.contains(organism.getName())) {
				organismNames.add(organism.getName());
				ret.add(organism);
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
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

	public void clearLinks() {
		links.clear();

	}

	public void addLink(LinkBetweenQueriableProteinSetAndPSM link) {
		links.add(link);
	}
}
