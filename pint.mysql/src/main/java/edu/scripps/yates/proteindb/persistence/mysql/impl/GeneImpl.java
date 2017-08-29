package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Map;

import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import gnu.trove.map.hash.THashMap;

public class GeneImpl implements Gene {
	private final edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene;
	protected final static Map<String, Gene> genesMap = new THashMap<String, Gene>();

	public GeneImpl(edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene) {
		this.hibGene = hibGene;
		genesMap.put(hibGene.getGeneId(), this);
	}

	@Override
	public String getGeneID() {
		return hibGene.getGeneId();
	}

	@Override
	public String getGeneType() {
		return hibGene.getGeneType();
	}

}
