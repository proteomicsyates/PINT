package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdDescriptionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean;

public class OrganismSetTypeAdapter implements Adapter<OrganismSetType> {
	private final OrganismSetTypeBean organismSet;

	public OrganismSetTypeAdapter(OrganismSetTypeBean organismSet) {
		this.organismSet = organismSet;
	}

	@Override
	public OrganismSetType adapt() {
		OrganismSetType ret = new OrganismSetType();
		if (organismSet.getOrganism() != null) {
			for (IdDescriptionTypeBean idDescriptionTypeBean : organismSet
					.getOrganism()) {

				ret.getOrganism().add(
						new IdDescriptionTypeAdapter(idDescriptionTypeBean
								.getId(), idDescriptionTypeBean
								.getDescription()).adapt());

			}
		}
		return ret;
	}

}
