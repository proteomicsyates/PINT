package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class TissueTypeBeanAdapter implements Adapter<TissueTypeBean> {
	private final String id;
	private final String description;

	public TissueTypeBeanAdapter(String id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public TissueTypeBean adapt() {
		TissueTypeBean ret = new TissueTypeBean();

		ret.setDescription(description);
		ret.setId(id);

		return ret;
	}

}
