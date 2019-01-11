package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdDescriptionTypeBean;

public class IdDescriptionTypeBeanAdapter implements Adapter<IdDescriptionTypeBean> {
	private final String id;
	private final String description;

	public IdDescriptionTypeBeanAdapter(String id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public IdDescriptionTypeBean adapt() {
		IdDescriptionTypeBean ret = new IdDescriptionTypeBean();

		ret.setDescription(description);
		ret.setId(id);

		return ret;
	}

}
