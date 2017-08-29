package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Map;

import edu.scripps.yates.utilities.model.enums.AmountType;
import gnu.trove.map.hash.THashMap;

public class AmountTypeAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.AmountType>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5234342951031375094L;
	private final AmountType amountType;
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.AmountType> map = new THashMap<String, edu.scripps.yates.proteindb.persistence.mysql.AmountType>();

	public AmountTypeAdapter(AmountType amountType) {
		this.amountType = amountType;
	}

	@Override
	public edu.scripps.yates.proteindb.persistence.mysql.AmountType adapt() {
		edu.scripps.yates.proteindb.persistence.mysql.AmountType ret = new edu.scripps.yates.proteindb.persistence.mysql.AmountType();

		if (map.containsKey(amountType.name()))
			return map.get(amountType.name());
		map.put(amountType.name(), ret);
		ret.setName(amountType.name());

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
