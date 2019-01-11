package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationExcelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;

public class QuantificationExcelTypeAdapter implements Adapter<QuantificationExcelType> {
	private final QuantificationExcelTypeBean quantificationExcelTypeBean;

	public QuantificationExcelTypeAdapter(QuantificationExcelTypeBean quantificationExcelTypeBean) {
		this.quantificationExcelTypeBean = quantificationExcelTypeBean;
	}

	@Override
	public QuantificationExcelType adapt() {
		QuantificationExcelType ret = new QuantificationExcelType();
		ret.setMsRunRef(quantificationExcelTypeBean.getMsRunRef());
		if (quantificationExcelTypeBean.getPsmAmounts() != null) {
			for (AmountTypeBean amountTypeBean : quantificationExcelTypeBean.getPsmAmounts()) {
				ret.getPsmAmount().add(new AmountTypeAdapter(amountTypeBean).adapt());
			}
		}
		if (quantificationExcelTypeBean.getPeptideAmounts() != null) {
			for (AmountTypeBean amountTypeBean : quantificationExcelTypeBean.getPeptideAmounts()) {
				ret.getPeptideAmount().add(new AmountTypeAdapter(amountTypeBean).adapt());
			}
		}
		if (quantificationExcelTypeBean.getProteinAmounts() != null) {
			for (AmountTypeBean amountTypeBean : quantificationExcelTypeBean.getProteinAmounts()) {
				ret.getProteinAmount().add(new AmountTypeAdapter(amountTypeBean).adapt());
			}
		}
		return ret;
	}

}
