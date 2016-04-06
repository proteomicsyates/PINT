package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ConditionRefType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;

public class ConditionRefTypeAdapter implements Adapter<ConditionRefType> {
	private final String conditionRefBean;

	public ConditionRefTypeAdapter(String denominator) {
		conditionRefBean = denominator;
	}

	@Override
	public ConditionRefType adapt() {
		ConditionRefType ret = new ConditionRefType();
		ret.setConditionRef(conditionRefBean);
		return ret;
	}

}
