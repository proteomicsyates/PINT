package edu.scripps.yates.utilities.proteomicsmodel;

public interface Peptide extends HasRatios, HasScores, HasAmounts, HasConditions, HasPsms, HasMSRun, HasProteins {
	public int getDBId();

	public String getSequence();

}
