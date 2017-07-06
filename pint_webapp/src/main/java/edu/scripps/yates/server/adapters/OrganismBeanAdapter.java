package edu.scripps.yates.server.adapters;

import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.OrganismBean;
import gnu.trove.map.hash.THashMap;

public class OrganismBeanAdapter implements Adapter<OrganismBean> {
	private final Organism organism;
	private final static Map<String, OrganismBean> map = new THashMap<String, OrganismBean>();

	public OrganismBeanAdapter(Organism organism) {
		this.organism = organism;
	}

	@Override
	public OrganismBean adapt() {
		if (map.containsKey(organism.getTaxonomyId()))
			map.get(organism.getTaxonomyId());
		OrganismBean ret = new OrganismBean();
		ret.setName(organism.getName());
		ret.setNcbiTaxID(organism.getTaxonomyId());
		map.put(organism.getTaxonomyId(), ret);
		return ret;
	}

}
