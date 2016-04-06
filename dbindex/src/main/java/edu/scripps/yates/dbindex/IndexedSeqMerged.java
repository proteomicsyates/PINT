package edu.scripps.yates.dbindex;

import java.util.List;

/**
 * internal temporarly representation of indexed sequence before sequences are
 * merged into IndexedSequence
 * 
 * two IndexedSeqInternal are equal if they sequence strings are equal
 * 
 * two IndexedSeqInternal sequences are comparable by their mass
 * 
 */
class IndexedSeqMerged implements Comparable<IndexedSeqMerged> {

	public IndexedSeqMerged(double mass, int offset, int length,
			List<Integer> proteinIds) {
		this.mass = mass;
		this.offset = offset;
		this.length = length;
		this.proteinIds = proteinIds;

	}

	double mass;
	int offset;
	int length;
	List<Integer> proteinIds;

	@Override
	public int compareTo(IndexedSeqMerged o) {
		if (mass < o.mass) {
			return -1;
		} else if (mass > o.mass) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "IndexedSeqMerged{" + "mass=" + mass + ", offset=" + offset
				+ ", length=" + length + ", proteinIds=" + proteinIds + '}';
	}
}
