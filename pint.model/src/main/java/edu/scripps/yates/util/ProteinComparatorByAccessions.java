package edu.scripps.yates.util;

import java.util.Comparator;

import edu.scripps.yates.utilities.proteomicsmodel.Protein;

class ProteinComparatorByAccessions implements Comparator<Protein> {

	@Override
	public int compare(Protein prot1, Protein prot2) {

		return prot1.getAccession().compareTo(prot2.getAccession());
	}
}
