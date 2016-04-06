package edu.scripps.yates.utilities.proteomicsmodel;

public interface PTMSite {
	public String getAA();

	/**
	 * Gets the position of the modification in the peptide sequence. The first
	 * AA in the sequence is the position 1.
	 *
	 * @return
	 */
	public int getPosition();

	public Score getScore();
}
