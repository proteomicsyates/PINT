package edu.scripps.yates.util;

import java.util.Comparator;

import edu.scripps.yates.utilities.proteomicsmodel.Protein;

class ProteinComparatorByDBId implements Comparator<Protein> {

	@Override
	public int compare(Protein prot1, Protein prot2) {
		if (prot1.getDBId() > 0 && prot2.getDBId() > 0
				&& prot1.getDBId() == prot2.getDBId())
			return 0;
		return -1;
	}

}
