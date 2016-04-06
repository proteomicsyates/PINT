package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.HashMap;

import edu.scripps.yates.utilities.proteomicsmodel.Tissue;

public class TissueImpl implements Tissue {
	protected final static HashMap<String, Tissue> tissuesMap = new HashMap<String, Tissue>();

	private final edu.scripps.yates.proteindb.persistence.mysql.Tissue hibTissue;

	public TissueImpl(
			edu.scripps.yates.proteindb.persistence.mysql.Tissue tissue) {
		hibTissue = tissue;
		tissuesMap.put(tissue.getTissueId(), this);
	}

	@Override
	public String getTissueID() {
		return hibTissue.getTissueId();
	}

	@Override
	public String getName() {
		return hibTissue.getName();
	}

}
