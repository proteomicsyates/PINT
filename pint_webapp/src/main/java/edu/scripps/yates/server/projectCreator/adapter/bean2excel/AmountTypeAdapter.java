package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountCombinationType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountTypeType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;

public class AmountTypeAdapter implements Adapter<AmountType> {
	private final AmountTypeBean amountTypeBean;

	public AmountTypeAdapter(AmountTypeBean amountTypeBean) {
		this.amountTypeBean = amountTypeBean;
	}

	@Override
	public AmountType adapt() {
		AmountType ret = new AmountType();

		ret.setColumnRef(amountTypeBean.getColumnRef());
		if (amountTypeBean.getCombinationType() != null) {
			ret.setCombinationType(AmountCombinationType
					.fromValue(amountTypeBean.getCombinationType().name()));
		}
		if (amountTypeBean.getType() != null) {
			ret.setType(AmountTypeType.fromValue(amountTypeBean.getType()
					.name()));
		}

		return ret;
	}

}
