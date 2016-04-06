package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Tissue;

public class TissueAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.Tissue>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6284922670208575891L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Tissue tissue;
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.Tissue> map = new HashMap<String, Tissue>();

	public TissueAdapter(edu.scripps.yates.utilities.proteomicsmodel.Tissue tissue) {
		this.tissue = tissue;

	}

	@Override
	public Tissue adapt() {
		Tissue ret = new Tissue();
		if (map.containsKey(tissue.getTissueID()))
			return map.get(tissue.getTissueID());
		map.put(tissue.getTissueID(), ret);
		ret.setName(tissue.getName());
		ret.setTissueId(tissue.getTissueID());

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
