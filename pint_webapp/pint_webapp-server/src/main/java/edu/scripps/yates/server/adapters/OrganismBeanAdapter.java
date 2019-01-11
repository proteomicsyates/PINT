package edu.scripps.yates.server.adapters;

import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.OrganismBean;
import gnu.trove.map.hash.THashMap;

public class OrganismBeanAdapter implements Adapter<OrganismBean> {
	private final Organism organism;
	private final static ThreadLocal<Map<String, OrganismBean>> map = new ThreadLocal<Map<String, OrganismBean>>();

	public OrganismBeanAdapter(Organism organism) {
		this.organism = organism;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new THashMap<String, OrganismBean>());
		}
	}

	@Override
	public OrganismBean adapt() {
		if (map.get().containsKey(organism.getTaxonomyId()))
			map.get().get(organism.getTaxonomyId());
		OrganismBean ret = new OrganismBean();
		ret.setName(organism.getName());
		ret.setNcbiTaxID(organism.getTaxonomyId());
		map.get().put(organism.getTaxonomyId(), ret);
		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
