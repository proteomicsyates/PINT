package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Organism;

public class OrganismAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.Organism>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2694597187915802439L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Organism organism;
	private static final Logger log = Logger.getLogger(OrganismAdapter.class);
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.Organism> map = new HashMap<String, Organism>();

	public OrganismAdapter(edu.scripps.yates.utilities.proteomicsmodel.Organism organism2) {
		if (organism2 == null)
			log.info("CUIDADO");
		organism = organism2;
	}

	@Override
	public Organism adapt() {
		Organism ret = new Organism();
		if (map.containsKey(organism.getOrganismID()))
			return map.get(organism.getOrganismID());
		map.put(organism.getOrganismID(), ret);
		ret.setName(organism.getName());
		ret.setTaxonomyId(organism.getOrganismID());

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
