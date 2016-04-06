package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.HashMap;

import edu.scripps.yates.utilities.proteomicsmodel.Gene;

public class GeneImpl implements Gene {
	private final edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene;
	protected final static HashMap<String, Gene> genesMap = new HashMap<String, Gene>();

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
