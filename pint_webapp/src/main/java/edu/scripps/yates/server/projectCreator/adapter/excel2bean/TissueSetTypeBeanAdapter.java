package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean;

public class TissueSetTypeBeanAdapter implements Adapter<TissueSetTypeBean> {
	private final TissueSetType tissueSet;

	public TissueSetTypeBeanAdapter(TissueSetType tissueSet) {
		this.tissueSet = tissueSet;
	}

	@Override
	public TissueSetTypeBean adapt() {
		TissueSetTypeBean ret = new TissueSetTypeBean();
		if (tissueSet.getTissue() != null) {
			for (IdDescriptionType idDescriptionType : tissueSet.getTissue()) {
				ret.getTissue().add(
						new TissueTypeBeanAdapter(idDescriptionType.getId(),
								idDescriptionType.getDescription()).adapt());
			}
		}
		return ret;
	}
}
