package edu.scripps.yates.dbindex;

/**
 * internal temporarly representation of indexed sequence before sequences are
 * merged into IndexedSequence
 * 
 * two IndexedSeqInternal are equal if they sequence strings are equal
 * 
 * two IndexedSeqInternal sequences are comparable by their mass
 * 
 * @author Adam
 */
class IndexedSeqInternal implements Comparable<IndexedSeqInternal> {

	public IndexedSeqInternal(double mass, int offset, int length,
			int proteinId, String sequence, String protDescription) {
		this.mass = mass;
		this.offset = offset;
		this.length = length;
		this.proteinId = proteinId;
		this.sequence = sequence;
		this.protDescription = protDescription;

	}

	public IndexedSeqInternal(double mass, int offset, int length,
			int proteinId, String sequence) {
		this.mass = mass;
		this.offset = offset;
		this.length = length;
		this.proteinId = proteinId;
		this.sequence = sequence;

	}

	double mass;
	int offset;
	int length;
	int proteinId;
	String[] proteinIdArr;
	String protDescription;
	String sequence;

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (sequence != null ? sequence.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IndexedSeqInternal other = (IndexedSeqInternal) obj;
		if ((sequence == null) ? (other.sequence != null) : !sequence
				.equals(other.sequence)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(IndexedSeqInternal o) {
		if (mass < o.mass) {
			return -1;
		} else if (mass > o.mass) {
			return 1;
		} else {
			return 0;
		}
	}

	public String[] getProteinIdArr() {
		return proteinIdArr;
	}

	public void setProteinIdArr(String[] proteinIdArr) {
		this.proteinIdArr = proteinIdArr;
	}

}
