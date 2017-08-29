package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import gnu.trove.map.hash.THashMap;

public class GeneAdapter implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.Gene>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -334183663631843118L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Gene entrezGeneID;
	private static final Map<String, Gene> map = new THashMap<String, Gene>();

	public GeneAdapter(edu.scripps.yates.utilities.proteomicsmodel.Gene gene) {
		entrezGeneID = gene;
	}

	@Override
	public Gene adapt() {
		Gene ret = new Gene(entrezGeneID.getGeneID());
		if (map.containsKey(entrezGeneID.getGeneID())) {
			return map.get(entrezGeneID.getGeneID());
		}
		map.put(entrezGeneID.getGeneID(), ret);
		ret.setGeneType(entrezGeneID.getGeneType());
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
