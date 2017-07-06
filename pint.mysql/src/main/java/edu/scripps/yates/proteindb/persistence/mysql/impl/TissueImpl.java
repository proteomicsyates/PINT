package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Map;

import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import gnu.trove.map.hash.THashMap;

public class TissueImpl implements Tissue {
	protected final static Map<String, Tissue> tissuesMap = new THashMap<String, Tissue>();

	private final edu.scripps.yates.proteindb.persistence.mysql.Tissue hibTissue;

	public TissueImpl(edu.scripps.yates.proteindb.persistence.mysql.Tissue tissue) {
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
