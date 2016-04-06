package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;

public class IdDescriptionTypeAdapter implements Adapter<IdDescriptionType> {
	private final String id;
	private final String description;

	public IdDescriptionTypeAdapter(String id, String description) {
		this.id = id;
		this.description = description;
	}

	@Override
	public IdDescriptionType adapt() {
		IdDescriptionType ret = new IdDescriptionType();

		ret.setDescription(description);
		ret.setId(id);

		return ret;
	}

}
