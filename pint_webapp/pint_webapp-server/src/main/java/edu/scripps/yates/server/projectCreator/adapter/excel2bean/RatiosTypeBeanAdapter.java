package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;

public class RatiosTypeBeanAdapter implements Adapter<RatiosTypeBean> {
	private final RatiosType ratios;

	public RatiosTypeBeanAdapter(RatiosType ratios) {
		this.ratios = ratios;
	}

	@Override
	public RatiosTypeBean adapt() {
		RatiosTypeBean ret = new RatiosTypeBean();
		if (ratios.getPsmAmountRatios() != null) {
			ret.setPsmAmountRatios(new PsmRatiosTypeBeanAdapter(ratios.getPsmAmountRatios()).adapt());
		}
		if (ratios.getPeptideAmountRatios() != null) {
			ret.setPeptideAmountRatios(new PeptideRatiosTypeBeanAdapter(ratios.getPeptideAmountRatios()).adapt());
		}
		if (ratios.getProteinAmountRatios() != null) {
			ret.setProteinAmountRatios(new ProteinRatiosTypeBeanAdapter(ratios.getProteinAmountRatios()).adapt());
		}
		return ret;
	}

}
