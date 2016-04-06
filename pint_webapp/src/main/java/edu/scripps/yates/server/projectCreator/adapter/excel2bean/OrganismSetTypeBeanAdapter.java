package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean;

public class OrganismSetTypeBeanAdapter implements Adapter<OrganismSetTypeBean> {
	private final OrganismSetType organismSet;

	public OrganismSetTypeBeanAdapter(OrganismSetType organismSet) {
		this.organismSet = organismSet;
	}

	@Override
	public OrganismSetTypeBean adapt() {
		OrganismSetTypeBean ret = new OrganismSetTypeBean();
		if (organismSet.getOrganism() != null) {
			for (IdDescriptionType idDescriptionTypeBean : organismSet
					.getOrganism()) {
				ret.getOrganism().add(
						new OrganismTypeBeanAdapter(idDescriptionTypeBean
								.getId(), idDescriptionTypeBean
								.getDescription()).adapt());
			}
		}
		return ret;
	}

}
