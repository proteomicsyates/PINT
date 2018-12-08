package edu.scripps.yates.util;

import java.util.Comparator;

import edu.scripps.yates.utilities.proteomicsmodel.Protein;

class ProteinComparatorByDBId implements Comparator<Protein> {

	@Override
	public int compare(Protein prot1, Protein prot2) {
		if (prot1.getUniqueID() > 0 && prot2.getUniqueID() > 0
				&& prot1.getUniqueID() == prot2.getUniqueID())
			return 0;
		return -1;
	}

}
