package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface HasAmounts {
	public Set<Amount> getAmounts();

	public void addAmount(Amount amount);
}
