package edu.scripps.yates.shared.util;

import java.io.Serializable;

import edu.scripps.yates.shared.model.PeptideBean;

public class AlignmentResult implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4257564456172803859L;
	private int finalAlignmentScore;
	private PeptideBean seq1;
	private PeptideBean seq2;
	private int alignmentLength;
	private int identicalLength;
	private String alignmentString;

	public AlignmentResult() {

	}

	/**
	 * @return the finalAlignmentScore
	 */
	public int getFinalAlignmentScore() {
		return finalAlignmentScore;
	}

	/**
	 * @param finalAlignmentScore
	 *            the finalAlignmentScore to set
	 */
	public void setFinalAlignmentScore(int finalAlignmentScore) {
		this.finalAlignmentScore = finalAlignmentScore;
	}

	/**
	 * @return the seq1
	 */
	public PeptideBean getSeq1() {
		return seq1;
	}

	/**
	 * @param peptideBean1
	 *            the seq1 to set
	 */
	public void setSeq1(PeptideBean peptideBean1) {
		seq1 = peptideBean1;
	}

	/**
	 * @return the seq2
	 */
	public PeptideBean getSeq2() {
		return seq2;
	}

	/**
	 * @param seq2
	 *            the seq2 to set
	 */
	public void setSeq2(PeptideBean seq2) {
		this.seq2 = seq2;
	}

	/**
	 * @return the alignmentLength
	 */
	public int getAlignmentLength() {
		return alignmentLength;
	}

	/**
	 * @param alignmentLength
	 *            the alignmentLength to set
	 */
	public void setAlignmentLength(int alignmentLength) {
		this.alignmentLength = alignmentLength;
	}

	/**
	 * @return the identicalLength
	 */
	public int getIdenticalLength() {
		return identicalLength;
	}

	/**
	 * @param identicalLength
	 *            the identicalLength to set
	 */
	public void setIdenticalLength(int identicalLength) {
		this.identicalLength = identicalLength;
	}

	/**
	 * @return the alignmentString
	 */
	public String getAlignmentString() {
		return alignmentString;
	}

	/**
	 * @param alignmentString
	 *            the alignmentString to set
	 */
	public void setAlignmentString(String alignmentString) {
		this.alignmentString = alignmentString;
	}

	public String getAlignmentConnections() {
		if (alignmentString != null) {
			final String[] split = alignmentString.split("\n");
			if (split.length == 4) {
				return split[1];
			}
		}
		return "";
	}

	public String getAlignedSequence1() {
		if (alignmentString != null) {
			final String[] split = alignmentString.split("\n");
			return split[0];

		}
		return "";
	}

	public String getAlignedSequence2() {
		if (alignmentString != null) {
			final String[] split = alignmentString.split("\n");
			return split[2];

		}
		return "";
	}

	/**
	 * 
	 * @return the identity in % (0-100)
	 */
	public double getSequenceIdentity() {
		return 100.0 * identicalLength / alignmentLength;
	}

	/**
	 * True is the alignment doesnt contains any gap and any sustitution
	 */
	public boolean isAnOverlapping() {
		String seq1 = getAlignedSequence1();
		String seq2 = getAlignedSequence2();

		if (seq1.startsWith("-")) {
			seq2 = seq2.substring(getLastStartingFrom(seq1, '-', 0));
			seq1 = seq1.substring(getLastStartingFrom(seq1, '-', 0));
		}
		if (seq2.startsWith("-")) {
			seq1 = seq1.substring(getLastStartingFrom(seq2, '-', 0));
			seq2 = seq2.substring(getLastStartingFrom(seq2, '-', 0));

		}
		if (seq1.endsWith("-")) {
			seq2 = seq2.substring(0, getLastBackStartingFrom(seq1, '-', seq1.length() - 1));
			seq1 = seq1.substring(0, getLastBackStartingFrom(seq1, '-', seq1.length() - 1));
		}
		if (seq2.endsWith("-")) {
			seq1 = seq1.substring(0, getLastBackStartingFrom(seq2, '-', seq2.length() - 1));
			seq2 = seq2.substring(0, getLastBackStartingFrom(seq2, '-', seq2.length() - 1));

		}
		if (seq1.equals(seq2))
			return true;

		return false;

	}

	private int getLastBackStartingFrom(String seq12, char character, int startingPoint) {
		int pos = startingPoint;
		for (pos = startingPoint; pos >= 0; pos--) {
			if (seq12.charAt(pos) != character)
				return pos + 1;
		}
		return pos;
	}

	private int getLastStartingFrom(String seq12, char character, int startingPos) {
		int pos = startingPos;
		for (pos = startingPos; pos < seq12.length(); pos++) {
			if (seq12.charAt(pos) != character)
				return pos - 1;
		}
		return pos;
	}
}
