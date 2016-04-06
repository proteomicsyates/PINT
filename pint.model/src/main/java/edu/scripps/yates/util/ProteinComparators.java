package edu.scripps.yates.util;

import java.util.Comparator;

import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class ProteinComparators {

	private static ProteinComparatorByAccessions proteinComparatorByAccessions = new ProteinComparatorByAccessions();
	private static ProteinComparatorByDBId proteinComparatorByDBId = new ProteinComparatorByDBId();

	public static Comparator<Protein> getProteinComparatorByAccessions() {
		return proteinComparatorByAccessions;
	}

	public static Comparator<Protein> getProteinComparatorByDBIds() {
		return proteinComparatorByDBId;
	}
}
