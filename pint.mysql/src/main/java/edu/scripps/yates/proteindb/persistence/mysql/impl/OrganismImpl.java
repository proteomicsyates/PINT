package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Map;

import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import gnu.trove.map.hash.THashMap;

public class OrganismImpl implements Organism {
	protected static final Map<String, Organism> organismMap = new THashMap<String, Organism>();
	private final edu.scripps.yates.proteindb.persistence.mysql.Organism hibOrganism;

	public OrganismImpl(edu.scripps.yates.proteindb.persistence.mysql.Organism organism) {
		hibOrganism = organism;
		organismMap.put(organism.getTaxonomyId(), this);
	}

	@Override
	public String getOrganismID() {
		return hibOrganism.getTaxonomyId();
	}

	@Override
	public String getName() {
		return hibOrganism.getName();
	}

}
