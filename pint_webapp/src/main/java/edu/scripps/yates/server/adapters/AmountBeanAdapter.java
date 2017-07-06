package edu.scripps.yates.server.adapters;

import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import gnu.trove.map.hash.TIntObjectHashMap;

public class AmountBeanAdapter implements Adapter<AmountBean> {
	private final ProteinAmount proteinAmount;
	private final PeptideAmount peptideAmount;
	private final PsmAmount psmAmount;
	private final MsRun msRun;
	private static final TIntObjectHashMap< AmountBean> mapPep = new TIntObjectHashMap<AmountBean>();
	private static final TIntObjectHashMap< AmountBean> mapPro = new TIntObjectHashMap<AmountBean>();
	private static final TIntObjectHashMap< AmountBean> mapPsm = new TIntObjectHashMap<AmountBean>();

	public AmountBeanAdapter(ProteinAmount proteinAmount, MsRun msRun) {
		this.proteinAmount = proteinAmount;
		peptideAmount = null;
		psmAmount = null;
		this.msRun = msRun;
	}

	public AmountBeanAdapter(PeptideAmount peptideAmount, MsRun msRun) {
		proteinAmount = null;
		this.peptideAmount = peptideAmount;
		psmAmount = null;
		this.msRun = msRun;
	}

	public AmountBeanAdapter(PsmAmount psmAmount, MsRun msRun) {
		proteinAmount = null;
		peptideAmount = null;
		this.psmAmount = psmAmount;
		this.msRun = msRun;
	}

	@Override
	public AmountBean adapt() {

		AmountBean ret = new AmountBean();
		ret.setMsRun(new MSRunBeanAdapter(msRun).adapt());
		if (proteinAmount != null) {
			if (mapPro.containsKey(proteinAmount.getId()))
				return mapPro.get(proteinAmount.getId());
			mapPro.put(proteinAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(proteinAmount.getAmountType().getName()));
			ret.setComposed(proteinAmount.getCombinationType() != null);
			ret.setValue(proteinAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(proteinAmount.getCondition()).adapt());
			// in case of protein amount, there is manualSPC attribute
			ret.setManualSPC(proteinAmount.getManualSPC());

		} else if (peptideAmount != null) {
			if (mapPep.containsKey(peptideAmount.getId()))
				return mapPep.get(peptideAmount.getId());
			mapPep.put(peptideAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(peptideAmount.getAmountType().getName()));
			ret.setComposed(peptideAmount.getCombinationType() != null);
			ret.setValue(peptideAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(peptideAmount.getCondition()).adapt());
			// TODO add PeptideBean when peptide bean exist
		} else if (psmAmount != null) {
			if (mapPsm.containsKey(psmAmount.getId()))
				return mapPsm.get(psmAmount.getId());
			mapPsm.put(psmAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(psmAmount.getAmountType().getName()));
			ret.setComposed(psmAmount.getCombinationType() != null);
			ret.setValue(psmAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(psmAmount.getCondition()).adapt());

		}
		return ret;

	}

}
