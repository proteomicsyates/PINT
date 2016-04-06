package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface HasConditions {
	public Set<Condition> getConditions();

	public void addCondition(Condition condition);
}
