package edu.scripps.yates.dbindex;

import java.util.Iterator;
import java.util.List;

/*
 * Interface that abstracts peptide database store Database store supports
 * storage and retrieval of peptide sequences by mass of the precursor, within a
 * given tolerance. The storage interface may be implemented by SQL database,
 * file storage, etc
 * @author Adam
 */
public interface DBIndexStore {
	public void lastBuffertoDatabase();

	public enum FilterResult {
		INCLUDE, SKIP, SKIP_PROTEIN_START
	};

	/*
	 * Initialize the database with the ID, and create one if it does not exist.
	 * If database already exists, new sequences added will be appended. Care
	 * must be taken by the client not to index the same database multiple times
	 * and create duplicate entries.
	 * @param databaseID id to associate with the storage
	 * @throws DBIndexStoreException exception thrown when an error associates
	 * with the underlying DB storage occured
	 */
	void init(String databaseID) throws DBIndexStoreException;

	/**
	 * A marker to set to indicate sequences will be added in bulk
	 *
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associates with the underlying
	 *             DB storage occured
	 */
	void startAddSeq() throws DBIndexStoreException;

	/**
	 * A marker to indicate bulk addition of adding sequences is done, via
	 * addSequence() method
	 *
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associates with the underlying
	 *             DB storage occured
	 */
	void stopAddSeq() throws DBIndexStoreException;

	/**
	 * Return true if index exists, must be called after init
	 *
	 * @return
	 */
	boolean indexExists() throws DBIndexStoreException;

	/**
	 * Filter out sequence before it is being added with addSequence() This hook
	 * can optimize and result in not calculating residues and calling
	 * addSequence() if not needed
	 *
	 * @param precMass
	 *            mass of the sequence
	 * @param sequence
	 *            the sequence string
	 * @return filter result, if the sequence passed filters and should be
	 *         added, should not be added, or if should not be added the the
	 *         entire protein should be skipped
	 */
	FilterResult filterSequence(double precMass, String sequence);

	/**
	 * Add a peptide sequence by offset and length into the protein sequence If
	 * startAddSeq() has been called, if supported, the addition of sequence
	 * might not be committed until stopAddSeq() is called.
	 *
	 * @param precMass
	 *            precursor mass to add as a key
	 * @param sequenceOffset
	 *            sequence offset into the original protein sequence to insert
	 *            as a value
	 * @param sequenceLen
	 *            sequence length into the original protein sequence to insert
	 *            as a value
	 * @param sequence
	 *            peptide sequence to add, or null if cached version is used
	 * @param resLeft
	 *            residue on left hand side of the sequence, or null if cached
	 *            version is used
	 * @param resLeft
	 *            residue on right hand side of the sequence, or null if cached
	 *            version is used
	 * @param proteinId
	 *            ID of the protein already in the index
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred
	 */
	void addSequence(double precMass, int sequenceOffset, int sequenceLen, String sequence, String resLeft,
			String resRight, long proteinId) throws DBIndexStoreException;

	/**
	 * Get list of peptide sequences for the given precursor mass, within
	 * tolerance Sequences are UNIQUE, i.e. the same sequence appears only once,
	 * even if derived from different proteins Use getProteins() to get a true
	 * list of all proteins associated with the sequence
	 *
	 * @param precMass
	 *            precursor mass to lookup by, in amu
	 * @param tolerance
	 *            tolerance to use for the precursor mass, in amu tolerance is
	 *            mass dependant and it is already scaled / pre-calculated
	 *            tolerance for that mass to use example: resulting mass range
	 *            will be <precMass-tolerance, precMass+tolerance>
	 * @return list of sequences that for the precursor mass within the
	 *         tolerance
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred
	 */
	List<IndexedSequence> getSequences(double precMass, double tolerance) throws DBIndexStoreException;

	/**
	 * Get list of peptide sequences for the given multiple ranges with
	 * precursor mass, within tolerance Sequences are UNIQUE, i.e. the same
	 * sequence appears only once, even if derived from different proteins Use
	 * getProteins() to get a true list of all proteins associated with the
	 * sequence
	 *
	 * @param massRanges
	 * @return list of sequences that for the precursor mass within the
	 *         tolerance
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred
	 */
	List<IndexedSequence> getSequences(List<MassRange> ranges) throws DBIndexStoreException;

	Iterator<IndexedSequence> getSequencesIterator(List<MassRange> ranges) throws DBIndexStoreException;

	/**
	 * Add a protein accession (from fasta header) to the index
	 *
	 * @param num
	 *            protein number, starting at 1 and incremented each time
	 * @param accession
	 *            the fasta definition/accession string from the fasta header
	 * @param protSequence
	 *            the protein sequence - does not need to be used by underlying
	 *            storage, if cache is used
	 * @return id in the index of the added definition, or 0 if not used
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred and protein could not be added to the
	 *             index
	 */
	long addProteinDef(long num, String accession, String protSequence) throws DBIndexStoreException;

	/**
	 * Set in memory protein database
	 *
	 * @param proteinCache
	 *            in memory protein database, map of definition accession to
	 *            sequence
	 */
	void setProteinCache(ProteinCache proteinCache);

	/**
	 * return true if the implementation uses in memory protein db cache
	 *
	 * @return true if it supports the protein cache
	 */
	boolean supportsProteinCache();

	/**
	 * Get indexed proteins for the sequence
	 *
	 * @param sequence
	 *            indexed protein sequence to get proteins for
	 * @return indexed protein sequence object representing the fasta definition
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred and could not retrieve the protein
	 *             definition, or if no protein found in the index by that id
	 */
	List<IndexedProtein> getProteins(IndexedSequence sequence) throws DBIndexStoreException;

	/**
	 * Get number of sequences currently in the index
	 *
	 * @return number of sequences
	 * @throws DBIndexStoreException
	 *             exception thrown when an error associated with the underlying
	 *             DB storage occurred
	 */
	long getNumberSequences() throws DBIndexStoreException;

	/**
	 * Get residues for the peptide
	 *
	 * @param peptideSequence
	 *            peptide to get residues for
	 * @param protein
	 *            protein from which the peptide sequence was derived
	 * @return residues
	 * @throws DBIndexStoreException
	 *             exception thrown if could not get residues
	 */
	ResidueInfo getResidues(IndexedSequence peptideSequence, IndexedProtein protein) throws DBIndexStoreException;

}

/**
 * Exception thrown when error accessing the db store occurred
 */
class DBIndexStoreException extends Throwable {

	public DBIndexStoreException(String message) {
		super(message);
	}

	public DBIndexStoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public DBIndexStoreException(Throwable cause) {
		super(cause);
	}

}
