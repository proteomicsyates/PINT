package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;

public class RatiosTypeAdapter implements Adapter<RatiosType> {
	private final RatiosTypeBean ratios;

	public RatiosTypeAdapter(RatiosTypeBean ratios) {
		this.ratios = ratios;
	}

	@Override
	public RatiosType adapt() {
		RatiosType ret = new RatiosType();
		if (ratios.getPsmAmountRatios() != null)
			ret.setPsmAmountRatios(new PsmRatiosTypeAdapter(ratios.getPsmAmountRatios()).adapt());
		if (ratios.getPeptideAmountRatios() != null)
			ret.setPeptideAmountRatios(new PeptideRatiosTypeAdapter(ratios.getPeptideAmountRatios()).adapt());
		if (ratios.getProteinAmountRatios() != null)
			ret.setProteinAmountRatios(new ProteinRatiosTypeAdapter(ratios.getProteinAmountRatios()).adapt());
		return ret;
	}

}
