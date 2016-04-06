package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.OrganismBean;

public class OrganismBeanTypeAdapter implements Adapter<OrganismBean> {

	private final IdDescriptionType organismType;

	public OrganismBeanTypeAdapter(IdDescriptionType organismType) {
		this.organismType = organismType;
	}

	@Override
	public OrganismBean adapt() {
		OrganismBean ret = new OrganismBean();
		ret.setName(organismType.getDescription());
		ret.setNcbiTaxID(organismType.getId());
		return ret;
	}

}
