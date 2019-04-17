package edu.scripps.yates.server.adapters;

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
	private static final ThreadLocal<TIntObjectHashMap<AmountBean>> mapPep = new ThreadLocal<TIntObjectHashMap<AmountBean>>();
	private static final ThreadLocal<TIntObjectHashMap<AmountBean>> mapPro = new ThreadLocal<TIntObjectHashMap<AmountBean>>();
	private static final ThreadLocal<TIntObjectHashMap<AmountBean>> mapPsm = new ThreadLocal<TIntObjectHashMap<AmountBean>>();

	public AmountBeanAdapter(ProteinAmount proteinAmount, MsRun msRun) {
		this.proteinAmount = proteinAmount;
		peptideAmount = null;
		psmAmount = null;
		this.msRun = msRun;
		initializeMap();
	}

	private static void initializeMap() {
		if (mapPep.get() == null) {
			mapPep.set(new TIntObjectHashMap<AmountBean>());
		}
		if (mapPro.get() == null) {
			mapPro.set(new TIntObjectHashMap<AmountBean>());
		}
		if (mapPsm.get() == null) {
			mapPsm.set(new TIntObjectHashMap<AmountBean>());
		}
	}

	public AmountBeanAdapter(PeptideAmount peptideAmount, MsRun msRun) {
		proteinAmount = null;
		this.peptideAmount = peptideAmount;
		psmAmount = null;
		this.msRun = msRun;
		initializeMap();
	}

	public AmountBeanAdapter(PsmAmount psmAmount, MsRun msRun) {
		proteinAmount = null;
		peptideAmount = null;
		this.psmAmount = psmAmount;
		this.msRun = msRun;
		initializeMap();
	}

	@Override
	public AmountBean adapt() {

		final AmountBean ret = new AmountBean();
		ret.addMsRun(new MSRunBeanAdapter(msRun, false).adapt());
		if (proteinAmount != null) {
			if (mapPro.get().containsKey(proteinAmount.getId()))
				return mapPro.get().get(proteinAmount.getId());
			mapPro.get().put(proteinAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(proteinAmount.getAmountType().getName()));
			ret.setComposed(proteinAmount.getCombinationType() != null);
			ret.setValue(proteinAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(proteinAmount.getCondition(), true, true).adapt());
			// in case of protein amount, there is manualSPC attribute
			ret.setManualSPC(proteinAmount.getManualSPC());

		} else if (peptideAmount != null) {
			if (mapPep.get().containsKey(peptideAmount.getId()))
				return mapPep.get().get(peptideAmount.getId());
			mapPep.get().put(peptideAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(peptideAmount.getAmountType().getName()));
			ret.setComposed(peptideAmount.getCombinationType() != null);
			ret.setValue(peptideAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(peptideAmount.getCondition(), true, true).adapt());
			// TODO add PeptideBean when peptide bean exist
		} else if (psmAmount != null) {
			if (mapPsm.get().containsKey(psmAmount.getId()))
				return mapPsm.get().get(psmAmount.getId());
			mapPsm.get().put(psmAmount.getId(), ret);
			ret.setAmountType(AmountType.fromValue(psmAmount.getAmountType().getName()));
			ret.setComposed(psmAmount.getCombinationType() != null);
			ret.setValue(psmAmount.getValue());
			ret.setExperimentalCondition(new ConditionBeanAdapter(psmAmount.getCondition(), true, true).adapt());

		}
		return ret;

	}

	public static void clearStaticMap() {
		if (mapPep.get() != null) {
			mapPep.get().clear();
		}
		if (mapPro.get() != null) {
			mapPro.get().clear();
		}
		if (mapPsm.get() != null) {
			mapPsm.get().clear();
		}
	}

	public static AmountBean getBeanByProteinAmountValueID(int id) {
		initializeMap();
		return mapPro.get().get(id);
	}

	public static AmountBean getBeanByPeptideAmountValueID(int id) {
		initializeMap();
		return mapPep.get().get(id);
	}

	public static AmountBean getBeanByPSMAmountValueID(int id) {
		initializeMap();
		return mapPsm.get().get(id);
	}
}
