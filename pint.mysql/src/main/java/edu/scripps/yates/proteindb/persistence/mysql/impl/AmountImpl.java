package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import gnu.trove.map.hash.TIntObjectHashMap;

public class AmountImpl implements Amount {
	protected static TIntObjectHashMap<Condition> experimentalConditions = new TIntObjectHashMap<Condition>();
	private final edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount hibProteinAmount;
	private final edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount hibPeptideAmount;
	private final edu.scripps.yates.proteindb.persistence.mysql.PsmAmount hibPsmAmount;

	public AmountImpl(edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount hibProteinAmount) {
		this.hibProteinAmount = hibProteinAmount;
		hibPeptideAmount = null;
		hibPsmAmount = null;
	}

	public AmountImpl(edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount hibPeptideAmount) {
		this.hibPeptideAmount = hibPeptideAmount;
		hibProteinAmount = null;
		hibPsmAmount = null;
	}

	public AmountImpl(edu.scripps.yates.proteindb.persistence.mysql.PsmAmount hibPsmAmount) {
		this.hibPsmAmount = hibPsmAmount;
		hibProteinAmount = null;
		hibPeptideAmount = null;
	}

	@Override
	public CombinationType getCombinationType() {
		if (hibProteinAmount != null && hibProteinAmount.getCombinationType() != null)
			return CombinationType.getCombinationType(hibProteinAmount.getCombinationType().getName());
		if (hibPeptideAmount != null && hibPeptideAmount.getCombinationType() != null)
			return CombinationType.getCombinationType(hibPeptideAmount.getCombinationType().getName());
		if (hibPsmAmount != null && hibPsmAmount.getCombinationType() != null)
			return CombinationType.getCombinationType(hibPsmAmount.getCombinationType().getName());
		return null;
	}

	@Override
	public double getValue() {
		if (hibProteinAmount != null)
			return hibProteinAmount.getValue();
		else if (hibPeptideAmount != null)
			return hibPeptideAmount.getValue();
		else
			return hibPsmAmount.getValue();

	}

	@Override
	public AmountType getAmountType() {
		if (hibProteinAmount != null)
			return AmountType.valueOf(hibProteinAmount.getAmountType().getName());
		if (hibPeptideAmount != null)
			return AmountType.valueOf(hibPeptideAmount.getAmountType().getName());
		if (hibPsmAmount != null)
			return AmountType.valueOf(hibPsmAmount.getAmountType().getName());
		return null;
	}

	@Override
	public Condition getCondition() {
		edu.scripps.yates.proteindb.persistence.mysql.Condition hibCondition = null;
		if (hibProteinAmount != null)
			hibCondition = hibProteinAmount.getCondition();
		else if (hibPeptideAmount != null)
			hibCondition = hibPeptideAmount.getCondition();
		else if (hibPsmAmount != null)
			hibCondition = hibPsmAmount.getCondition();

		if (ConditionImpl.conditionsMap.containsKey(hibCondition.getId())) {
			return ConditionImpl.conditionsMap.get(hibCondition.getId());
		} else {
			return new ConditionImpl(hibCondition);
		}

	}

	@Override
	public Boolean isSingleton() {
		if (hibPsmAmount != null)
			return hibPsmAmount.getSingleton();
		return null;
	}

	@Override
	public Boolean isManualSpc() {
		if (hibProteinAmount != null)
			return hibProteinAmount.getManualSPC();
		return null;
	}
}
