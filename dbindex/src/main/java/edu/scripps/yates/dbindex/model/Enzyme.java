/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.scripps.yates.dbindex.model;

/**
 *
 * @author rpark
 */
public class Enzyme {

	public static final int SIZE = 256;
	private final boolean[] enzymeArr = new boolean[SIZE];

	private final int maxNumberOfMissedCleavages;
	// private int maxInternalMiscleavage=-1;
	private final boolean semiCleave;

	public Enzyme(char[] cleavePositions, int misscleavages, boolean semiCleave) {
		if (cleavePositions != null) {
			for (char c : cleavePositions) {
				addCleavePosition(c);
			}
		}
		maxNumberOfMissedCleavages = misscleavages;
		this.semiCleave = semiCleave;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Enzymes:");
		for (int i = 0; i < enzymeArr.length; ++i) {
			if (enzymeArr[i] == true) {
				sb.append((char) i);
			}
		}
		sb.append(" ");

		return sb.toString();
	}

	public boolean checkCleavage(String proteinSequence, int start, int end, String enzymeNoCutAA) {

		if (start > 0) {
			// that is the case when is fully triptic but the preAA is not a
			// cleave site
			final char preAA = proteinSequence.charAt(start - 1);
			if (!enzymeArr[preAA] && !semiCleave) {
				return false;
			}
		} else {
			// if the peptide starts at the N-terminal of the protein
		}

		// if the last AA is not a cleavage site
		final char lastAA = proteinSequence.charAt(end);
		if (!enzymeArr[lastAA]) {
			// if the lastAA of the peptide is not the Cterminal of the protein
			// and is fully triptic
			if (!semiCleave && proteinSequence.length() != end + 1) {
				return false;
			}
		}

		// check the postAA to see if is in the enzymeNoCutAA
		if (enzymeNoCutAA != null && proteinSequence.length() > end + 1) {
			final String postAA = proteinSequence.substring(end + 1, end + 2);
			if (enzymeNoCutAA.contains(postAA)) {
				return false;
			}
		}
		// check the number of missedCleavages
		int numMissedCleavages = 0;
		final String peptideSequence = proteinSequence.substring(start, end + 1);
		for (int index = 0; index < peptideSequence.length() - 1; index++) {
			char aa = peptideSequence.charAt(index);
			if (enzymeArr[aa]) {
				numMissedCleavages++;
			}
		}
		if (numMissedCleavages > maxNumberOfMissedCleavages) {
			return false;
		}

		return true;
	}

	public void addCleavePosition(char ch) {
		enzymeArr[ch] = true;
	}

	public boolean[] getEnzymeArr() {
		return enzymeArr;
	}

	public boolean isEnzyme(char ch) {
		return enzymeArr[ch];
	}

	/**
	 * @return the misscleavages
	 */
	public int getMisscleavages() {
		return maxNumberOfMissedCleavages;
	}

	/**
	 * @return the semiCleave
	 */
	public boolean isSemiCleave() {
		return semiCleave;
	}

	/*
	 * public int getMaxInternalMiscleavage() { return maxInternalMiscleavage; }
	 * public void setMaxInternalMiscleavage(int maxInternalMiscleavage) {
	 * this.maxInternalMiscleavage = maxInternalMiscleavage; } public int
	 * getMiscleavage() { return miscleavage; } public void setMiscleavage(int
	 * miscleavage) { this.miscleavage = miscleavage; }
	 */
}
