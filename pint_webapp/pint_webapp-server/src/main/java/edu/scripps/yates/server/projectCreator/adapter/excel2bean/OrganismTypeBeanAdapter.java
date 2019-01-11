package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;

public class OrganismTypeBeanAdapter implements Adapter<OrganismTypeBean> {
	private final String id;
	private final String description;

	public OrganismTypeBeanAdapter(String id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public OrganismTypeBean adapt() {
		OrganismTypeBean ret = new OrganismTypeBean();

		ret.setDescription(description);
		ret.setId(id);

		return ret;
	}

}
