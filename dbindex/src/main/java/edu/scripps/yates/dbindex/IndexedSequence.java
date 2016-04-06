package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of an indexed peptide sequence with mass and residues
 *
 * @author Adam
 */
public class IndexedSequence {

	private long id;
	private double mass;
	private String sequence;
	private String resLeft;
	private String resRight;
	private String modSequence;
	/**
	 * Offset is mostly an internal storage details however it can be useful to
	 * the user, e.g. to generate a report view showing where the peptide occurs
	 * in the protein
	 */
	private int sequenceOffset;
	private int sequenceLen;
	private boolean isModified = false;

	// optional if we want to associate proteinIds for some implementations
	private List<Integer> proteinIds = new ArrayList<Integer>();
	private List<String> proteinDescArray = new ArrayList<String>();

	public static final int OFFSET_UNKNOWN = -1;

	// mongoObjectID is a unique string identifier for each MongoDB document
	// (there exists one document per unique peptide sequence in the database)
	// Used for lookups of top scoring peptides after all mass range queries and
	// scoring have been performed
	// Added by Sandip
	private String mongoObjectID;

	// mongoProteinIDStrings is a list of unique string identifiers for each
	// MongoDB document (each individual peptide)
	// This list contains the value of the 'PROT_ID' field for each object in
	// the 'PARENTS' array (for each document)
	// Added by Sandip
	private final List<String> mongoProteinIDStrings = new ArrayList<>();

	// mongoProteinIDs is a list of unique string identifiers for each MongoDB
	// document (each individual peptide)
	// This list contains the value of the 'PROT_ID' field for each object in
	// the 'PARENTS' array (for each document)
	// Added by Sandip
	private final List<Integer> mongoProteinIDs = new ArrayList<>();

	/**
	 * Implementation that does not use sequence offsets
	 *
	 * @param id
	 * @param mass
	 * @param sequence
	 * @param resLeft
	 * @param resRight
	 */

	public IndexedSequence() {
	}

	// constructor for IndexedSequence for mongoconnect package
	// Added by Sandip
	public IndexedSequence(double precMass, String sequence, int sequenceLen, String resLeft, String resRight,
			String objID) {
		mass = precMass;
		this.sequence = sequence;
		this.sequenceLen = sequenceLen;
		this.resLeft = resLeft;
		this.resRight = resRight;
		mongoObjectID = objID;
		sequenceOffset = OFFSET_UNKNOWN;
	}

	// new constructor for IndexedSequence for mongoconnect package
	// for MassDB MongoDB implementation
	// Added by Sandip 6/3/14

	public IndexedSequence(double precMass, String sequence, int sequenceLen, String resLeft, String resRight) {
		mass = precMass;
		this.sequence = sequence;
		this.sequenceLen = sequenceLen;
		this.resLeft = resLeft;
		this.resRight = resRight;
		// this.mongoObjectID = objID;
		sequenceOffset = OFFSET_UNKNOWN;
	}

	public IndexedSequence(double precMass, String sequence, String resLeft, String resRight, int sequenceLen,
			List<Integer> proteinIds) {
		mass = precMass;
		this.sequence = sequence;
		this.resLeft = resLeft;
		this.resRight = resRight;
		this.sequenceLen = sequenceLen;
		this.proteinIds.addAll(proteinIds);
		proteinDescArray.add("proteindescription");
		sequenceOffset = 1;
		modSequence = "mosSequence";
		id = 0;
	}

	public IndexedSequence(long id, double mass, String sequence, String resLeft, String resRight) {
		this.id = id;
		this.mass = mass;
		this.sequence = sequence;
		this.resLeft = resLeft;
		this.resRight = resRight;
		sequenceLen = sequence.length();
		sequenceOffset = OFFSET_UNKNOWN;

	}

	/**
	 * For Implementation that does use sequence offsets
	 *
	 * @param id
	 * @param mass
	 * @param sequenceOffset
	 * @param sequenceLength
	 * @param sequence
	 * @param proteinId
	 * @param resLeft
	 * @param resRight
	 */
	public IndexedSequence(long id, double mass, int sequenceOffset, int sequenceLength, String sequence,
			String resLeft, String resRight) {
		this.id = id;
		this.mass = mass;
		this.sequence = sequence;
		this.sequenceOffset = sequenceOffset;
		sequenceLen = sequenceLength;
		// TODO see at which point we populate residues in this implementation
		this.resLeft = resLeft;
		this.resRight = resRight;
	}

	public IndexedSequence(double mass, int sequenceOffset, int length, int proteinId) {
		this.mass = mass;
		this.sequenceOffset = sequenceOffset;
		sequenceLen = length;
		proteinIds.add(proteinId);
	}

	/**
	 * Case when we have all data stored upfront, e.g. in memory index
	 *
	 * @param precMass
	 * @param sequenceOffset
	 * @param sequenceLen
	 * @param sequence
	 * @param resLeft
	 * @param resRight
	 * @param proteinId
	 */
	public IndexedSequence(double precMass, int sequenceOffset, int sequenceLen, String sequence, String resLeft,
			String resRight, long proteinId) {
		mass = precMass;
		this.sequenceOffset = sequenceOffset;
		this.sequenceLen = sequenceLen;
		this.sequence = sequence;
		this.resLeft = resLeft;
		this.resRight = resRight;
		proteinIds.add((int) proteinId);
	}

	public IndexedSequence(double mass, int sequenceOffset, int length, String[] pArr) {
		this.mass = mass;
		this.sequenceOffset = sequenceOffset;
		sequenceLen = length;

		for (String each : pArr)
			proteinIds.add(Integer.parseInt(each));
	}

	// methods for mongoconnect package
	// Added by Sandip
	public String getObjID() {
		return mongoObjectID;
	}

	@Override
	public String toString() {
		return "IndexedSequence{" + "id=" + id + ", mass=" + mass + ", sequence=" + sequence + ", resLeft=" + resLeft
				+ ", resRight=" + resRight + ", modSequence=" + modSequence + ", sequenceOffset=" + sequenceOffset
				+ ", sequenceLen=" + sequenceLen + ", isModified=" + isModified + ",ProteinDescriptionArray={  "
				+ proteinDescArray + "}" + '}';
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 67 * hash + sequence.hashCode();
		return hash;
	}

	/**
	 * Two sequences are equal if their sequence strings are equals TODO
	 * consider resLeft and resRight for completeness, to qualify seemingly same
	 * sequences This is used to e.g. filter out duplicates like in
	 * MassRangeFilteringIndex, etc.
	 *
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IndexedSequence other = (IndexedSequence) obj;
		if (!sequence.equals(other.sequence)) {
			return false;
		}
		return true;
	}

	public long getId() {
		return id;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getResLeft() {
		return resLeft;
	}

	public void setResLeft(String resLeft) {
		this.resLeft = resLeft;
	}

	public String getResRight() {
		return resRight;
	}

	public void setResRight(String resRight) {
		this.resRight = resRight;
	}

	public String getWholeSequence() {
		StringBuilder sb = new StringBuilder();
		sb.append(resLeft).append(".");
		if (isModified) {
			sb.append(modSequence);
		} else {
			sb.append(sequence);
		}
		sb.append(".").append(resRight);

		return sb.toString();
	}

	public String getSimpleSequence() {
		StringBuilder sb = new StringBuilder();
		sb.append(resLeft.charAt(resLeft.length() - 1)).append(".");
		if (isModified) {
			sb.append(modSequence);
		} else {
			sb.append(sequence);
		}
		sb.append(".").append(resRight.charAt(0));

		return sb.toString();
	}

	public String getModSequence() {
		return modSequence;
	}

	public void setModSequence(String modSequence) {
		this.modSequence = modSequence;
	}

	public boolean isIsModified() {
		return isModified;
	}

	public void setIsModified(boolean isModified) {
		this.isModified = isModified;
	}

	public int getSequenceOffset() {
		return sequenceOffset;
	}

	public int getSequenceLen() {
		return sequenceLen;
	}

	/**
	 * set residue if it is determined after the sequence has been created
	 *
	 * @param res
	 *            residue info to set
	 */
	void setResidues(ResidueInfo res) {
		resLeft = res.getResidueLeft();
		resRight = res.getResidueRight();
	}

	public List<Integer> getProteinIds() {
		return proteinIds;
	}

	public void setProteinIds(List<Integer> proteinIds) {
		this.proteinIds = proteinIds;
	}

	public IndexedSequence getCopy() {

		IndexedSequence iseq = new IndexedSequence(id, mass, sequenceOffset, sequenceLen, sequence, resLeft, resRight);
		iseq.setProteinIds(proteinIds);
		return iseq;
	}

	public List<String> getProteinDescArray() {
		return proteinDescArray;
	}

	public void setProteinDescArray(List<String> proteinDescArray) {
		this.proteinDescArray = proteinDescArray;
	}

	public List<String> getMongoProteinIDStrings() {
		return mongoProteinIDStrings;
	}

	public List<Integer> getMongoProteinIDs() {
		return mongoProteinIDs;
	}

	public void addToMongoProteinIDStrings(String newProtID) {
		mongoProteinIDStrings.add(newProtID);
	}

	public void addToMongoProteinIDs(int newProtID) {
		mongoProteinIDs.add(newProtID);
	}

}
