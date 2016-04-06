package edu.scripps.yates.dbindex.io;

import edu.scripps.yates.dbindex.DBIndexer.IndexType;
import edu.scripps.yates.dbindex.model.Enzyme;

public interface DBIndexSearchParams {

	IndexType getIndexType();

	boolean isInMemoryIndex();

	int getIndexFactor();

	String getDatabaseName();

	String getFullIndexFileName();

	int getMaxMissedCleavages();

	double getMaxPrecursorMass();

	double getMinPrecursorMass();

	boolean isUseIndex();

	String getEnzymeNocutResidues();

	String getEnzymeResidues();

	int getEnzymeOffset();

	boolean isUseMonoParent();

	Enzyme getEnzyme();

	char[] getEnzymeArr();

	/**
	 * If true a mass of H2O + PROTON will be added to any sequence in the
	 * indexed database. This is used for regular search engine analysis, but
	 * not for crosslinker analyses. So then, for crosslinker analysis, this
	 * method should return false.
	 *
	 * @return
	 */
	boolean isH2OPlusProtonAdded();

	/**
	 * this is the factor by which each double mass will be multiplied to get
	 * the key in the index.
	 *
	 * @return
	 */
	int getMassGroupFactor();

	/**
	 * At least one of the aminoacids in this char array have to be in the
	 * peptide sequence.<br>
	 * This is useful for crosslinked peptides since we want to index peptides
	 * with a certain potential crosslinked sites.
	 *
	 * @return
	 */
	char[] getMandatoryInternalAAs();

	boolean isUsingProtDB();

	boolean isUsingSeqDB();

	String getMongoDBURI();

	String getMassDBName();

	String getMassDBCollection();

	String getSeqDBName();

	String getSeqDBCollection();

	String getProtDBName();

	String getProtDBCollection();

	boolean isUsingMongoDB();

	boolean isSemiCleavage();

}
