package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.Map;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import edu.scripps.yates.utilities.proteomicsmodel.factories.TissueEx;
import gnu.trove.map.hash.THashMap;

public class TissueAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Tissue> {
	private final IdDescriptionType tissue;
	private static final Map<String, Tissue> map = new THashMap<String, Tissue>();

	public TissueAdapter(IdDescriptionType tissue) {
		this.tissue = tissue;
	}

	@Override
	public Tissue adapt() {
		if (map.containsKey(tissue.getId()))
			return map.get(tissue.getId());
		TissueEx ret = new TissueEx(tissue.getId());
		map.put(tissue.getId(), ret);
		ret.setName(tissue.getDescription());
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
