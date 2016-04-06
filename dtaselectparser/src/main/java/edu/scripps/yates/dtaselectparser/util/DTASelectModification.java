package edu.scripps.yates.dtaselectparser.util;

public class DTASelectModification {
	private final Double modificationShift;
	private final int modPosition;
	private final char aa;

	/**
	 * 
	 * @param modificationShift
	 * @param modPosition
	 *            position of the modification, starting by 1 at the first AA.
	 * @param aa
	 */
	public DTASelectModification(Double modificationShift, int modPosition,
			char aa) {
		this.modificationShift = modificationShift;
		this.modPosition = modPosition;
		this.aa = aa;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + modificationShift + ", aa=" + aa + ", at=" + modPosition
				+ "]";
	}

	/**
	 * @return the modificationShift
	 */
	public Double getModificationShift() {
		return modificationShift;
	}

	/**
	 * Gets the position of the modification in the peptide sequence, being 1
	 * the position for the first AA.
	 * 
	 * @return the modPosition
	 */
	public int getModPosition() {
		return modPosition;
	}

	/**
	 * @return the aa
	 */
	public char getAa() {
		return aa;
	}

}
