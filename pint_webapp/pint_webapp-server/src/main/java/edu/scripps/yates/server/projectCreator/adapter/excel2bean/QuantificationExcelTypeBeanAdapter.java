package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationExcelType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;

public class QuantificationExcelTypeBeanAdapter implements Adapter<QuantificationExcelTypeBean> {
	private final QuantificationExcelType quantificationExcelType;

	public QuantificationExcelTypeBeanAdapter(QuantificationExcelType quantificationExcelType) {
		this.quantificationExcelType = quantificationExcelType;
	}

	@Override
	public QuantificationExcelTypeBean adapt() {
		QuantificationExcelTypeBean ret = new QuantificationExcelTypeBean();
		ret.setMsRunRef(quantificationExcelType.getMsRunRef());
		if (quantificationExcelType.getPsmAmount() != null) {
			for (AmountType amountType : quantificationExcelType.getPsmAmount()) {
				ret.getPsmAmounts().add(new AmountTypeBeanAdapter(amountType).adapt());
			}
		}
		if (quantificationExcelType.getPeptideAmount() != null) {
			for (AmountType amountType : quantificationExcelType.getPeptideAmount()) {
				ret.getPeptideAmounts().add(new AmountTypeBeanAdapter(amountType).adapt());
			}
		}
		if (quantificationExcelType.getProteinAmount() != null) {
			for (AmountType amountType : quantificationExcelType.getProteinAmount()) {
				ret.getProteinAmounts().add(new AmountTypeBeanAdapter(amountType).adapt());
			}
		}

		return ret;
	}
}
