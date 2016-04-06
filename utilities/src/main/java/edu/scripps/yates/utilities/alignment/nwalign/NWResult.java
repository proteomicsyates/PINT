package edu.scripps.yates.utilities.alignment.nwalign;

import java.io.Serializable;

public class NWResult implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4678253699294033158L;
	private int finalAlignmentScore;
	private String seq1;
	private String seq2;
	private int alignmentLength;
	private int identicalLength;
	private String alignmentString;

	public NWResult() {

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
	public String getSeq1() {
		return seq1;
	}

	/**
	 * @param seq1
	 *            the seq1 to set
	 */
	public void setSeq1(String seq1) {
		this.seq1 = seq1;
	}

	/**
	 * @return the seq2
	 */
	public String getSeq2() {
		return seq2;
	}

	/**
	 * @param seq2
	 *            the seq2 to set
	 */
	public void setSeq2(String seq2) {
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

	public double getSequenceIdentity() {
		return 1.0 * identicalLength / alignmentLength;
	}

	public String printAlignment() {
		return alignmentString;
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "finalAlignmentScore=" + finalAlignmentScore + ",\nseq1=" + seq1 + ",\nseq2=" + seq2
				+ ",\nalignmentLength=" + alignmentLength + ",\nidenticalLength=" + identicalLength + ",\nidentity="
				+ getSequenceIdentity() + ",\nMax consecutive identity: " + getMaxConsecutiveIdenticalAlignment()
				+ ",\nalignmentString=\n" + alignmentString;
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

	private String getAlignedSequence2() {
		return getAlignmentString().split("\n")[2];
	}

	private String getAlignedSequence1() {
		return getAlignmentString().split("\n")[0];
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
				return pos;
		}
		return pos;
	}

	public int getMaxConsecutiveIdenticalAlignment() {
		final String alignmentString2 = getAlignmentString();
		int max = 0;
		int maxTmp = 0;
		for (int index = 0; index < alignmentString2.length(); index++) {
			if (alignmentString2.charAt(index) == ':') {
				maxTmp++;
			} else {
				if (maxTmp > max) {
					max = maxTmp;
				}
				maxTmp = 0;
			}
		}
		return max;
	}
}
