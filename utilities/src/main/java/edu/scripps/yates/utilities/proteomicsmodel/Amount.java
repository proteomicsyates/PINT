package edu.scripps.yates.utilities.proteomicsmodel;

import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;

public interface Amount {

	public double getValue();

	public AmountType getAmountType();

	public CombinationType getCombinationType();

	public Condition getCondition();

	public Boolean isSingleton();

	public Boolean isManualSpc();

}
