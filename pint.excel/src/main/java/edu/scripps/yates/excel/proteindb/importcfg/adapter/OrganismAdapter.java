package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.Map;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.factories.OrganismEx;
import gnu.trove.map.hash.THashMap;

public class OrganismAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Organism> {
	private final IdDescriptionType organism;
	private static final Map<String, Organism> map = new THashMap<String, Organism>();

	public OrganismAdapter(IdDescriptionType organism) {
		this.organism = organism;
	}

	@Override
	public Organism adapt() {
		if (map.containsKey(organism.getId()))
			return map.get(organism.getId());
		OrganismEx ret = null;
		// getDescription has the NCBI taxID
		if (organism.getDescription() != null && !"".equals(organism.getDescription())) {
			ret = new OrganismEx(organism.getDescription());
		} else {
			ret = new OrganismEx(organism.getId());
		}

		map.put(organism.getId(), ret);
		ret.setName(organism.getId());
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
