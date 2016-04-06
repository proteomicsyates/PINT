package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.TissueBean;

public class TissueBeanAdapter implements Adapter<TissueBean> {

	private final IdDescriptionType tissueType;

	public TissueBeanAdapter(IdDescriptionType tissueType) {
		this.tissueType = tissueType;
	}

	@Override
	public TissueBean adapt() {
		TissueBean ret = new TissueBean();
		ret.setDescription(tissueType.getDescription());
		ret.setTissueID(tissueType.getId());
		return ret;
	}

}
