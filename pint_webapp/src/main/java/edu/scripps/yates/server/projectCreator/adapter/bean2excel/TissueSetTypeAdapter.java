package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdDescriptionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean;

public class TissueSetTypeAdapter implements Adapter<TissueSetType> {
	private final TissueSetTypeBean tissueSet;

	public TissueSetTypeAdapter(TissueSetTypeBean tissueSet) {
		this.tissueSet = tissueSet;
	}

	@Override
	public TissueSetType adapt() {
		TissueSetType ret = new TissueSetType();
		if (tissueSet.getTissue() != null) {
			for (IdDescriptionTypeBean idDescriptionTypeBean : tissueSet
					.getTissue()) {
				ret.getTissue().add(
						new IdDescriptionTypeAdapter(idDescriptionTypeBean
								.getId(), idDescriptionTypeBean
								.getDescription()).adapt());
			}
		}
		return ret;
	}
}
