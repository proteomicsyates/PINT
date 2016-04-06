package edu.scripps.yates.dbindex.io;

import edu.scripps.yates.dbindex.DBIndexer.IndexType;
import edu.scripps.yates.dbindex.model.Enzyme;
import edu.scripps.yates.dbindex.util.IndexUtil;

public class DBIndexSearchParamsImpl implements DBIndexSearchParams {
	private IndexType indexType;
	private boolean inMemoryIndex;
	private int indexFactor;
	private String dataBaseName;
	private String fullIndexFileName;
	private int maxMissedCleavages;
	private double maxPrecursorMass;
	private double minPrecursorMass;
	private boolean useIndex = true;
	private String enzymeNocutResidues;
	private String enzymeResidues;
	private int enzymeOffset;
	private boolean isUseMonoParent = true;
	private char[] enzymeArr;
	private Enzyme enzyme;
	private boolean isH2OPlusProtonAdded;
	private int massGroupFactor;
	private char[] mandatoryInternalAAs;
	private boolean usingProtDB;
	private boolean usingSeqDB;
	private String mongoDBURI;
	private String massDBName;
	private String massDBCollection;
	private String seqDBName;
	private String seqDBCollection;
	private String protDBName;
	private String protDBCollection;
	private boolean usingMongoDB;
	private boolean semiCleavage;

	public DBIndexSearchParamsImpl(IndexType indexType, boolean inMemoryIndex, int indexFactor, String dataBaseName,
			int maxMissedCleavages, double maxPrecursorMass, double minPrecursorMass, boolean useIndex,
			String enzymeNocutResidues, String enzymeResidues, int enzymeOffset, boolean isUseMonoParent,
			boolean isH2OPlusProtonAdded, int massGroupFactor, char[] mandatoryInternalAAs, boolean semiCleavage) {
		this.indexType = indexType;
		this.inMemoryIndex = inMemoryIndex;
		this.indexFactor = indexFactor;
		this.dataBaseName = dataBaseName;
		this.maxMissedCleavages = maxMissedCleavages;
		this.maxPrecursorMass = maxPrecursorMass;
		this.minPrecursorMass = minPrecursorMass;
		this.useIndex = useIndex;
		this.enzymeNocutResidues = enzymeNocutResidues;
		this.enzymeResidues = enzymeResidues;
		this.semiCleavage = semiCleavage;
		enzyme = new Enzyme(enzymeArr, maxMissedCleavages, semiCleavage);
		enzymeArr = enzymeResidues.toCharArray();
		for (char ch : enzymeArr)
			enzyme.addCleavePosition(ch);

		this.enzymeOffset = enzymeOffset;
		this.isUseMonoParent = isUseMonoParent;
		this.isH2OPlusProtonAdded = isH2OPlusProtonAdded;
		this.massGroupFactor = massGroupFactor;
		this.mandatoryInternalAAs = mandatoryInternalAAs;

	}

	public DBIndexSearchParamsImpl(String mongoDBURI, String mongoMassDBName, String mongoSeqDBName,
			String mongoProtDBName) {
		this.mongoDBURI = mongoDBURI;
		usingMongoDB = mongoMassDBName != null && mongoDBURI != null;
		usingProtDB = mongoProtDBName != null;
		usingSeqDB = mongoSeqDBName != null;
		protDBCollection = mongoProtDBName;
		protDBName = protDBCollection;
		seqDBCollection = mongoSeqDBName;
		seqDBName = seqDBCollection;
		massDBCollection = mongoMassDBName;
		massDBName = massDBCollection;
		indexType = IndexType.INDEX_LARGE;
		useIndex = true;
	}

	@Override
	public IndexType getIndexType() {

		return indexType;
	}

	@Override
	public boolean isInMemoryIndex() {
		return inMemoryIndex;
	}

	@Override
	public int getIndexFactor() {
		return indexFactor;
	}

	@Override
	public String getDatabaseName() {
		return dataBaseName;
	}

	@Override
	public String getFullIndexFileName() {
		if (fullIndexFileName == null)
			fullIndexFileName = IndexUtil.createFullIndexFileName(this);
		return fullIndexFileName;
	}

	@Override
	public int getMaxMissedCleavages() {
		return maxMissedCleavages;
	}

	@Override
	public double getMaxPrecursorMass() {
		return maxPrecursorMass;
	}

	@Override
	public double getMinPrecursorMass() {
		return minPrecursorMass;
	}

	@Override
	public boolean isUseIndex() {
		return useIndex;
	}

	@Override
	public String getEnzymeNocutResidues() {
		return enzymeNocutResidues;
	}

	@Override
	public String getEnzymeResidues() {

		return enzymeResidues;
	}

	@Override
	public int getEnzymeOffset() {
		return enzymeOffset;
	}

	@Override
	public boolean isUseMonoParent() {
		return isUseMonoParent;
	}

	@Override
	public char[] getEnzymeArr() {
		return enzymeArr;
	}

	@Override
	public Enzyme getEnzyme() {
		return enzyme;
	}

	@Override
	public boolean isH2OPlusProtonAdded() {
		return isH2OPlusProtonAdded;
	}

	@Override
	public int getMassGroupFactor() {
		return massGroupFactor;
	}

	@Override
	public char[] getMandatoryInternalAAs() {
		return mandatoryInternalAAs;
	}

	/**
	 * @param indexType
	 *            the indexType to set
	 */
	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	/**
	 * @param inMemoryIndex
	 *            the inMemoryIndex to set
	 */
	public void setInMemoryIndex(boolean inMemoryIndex) {
		this.inMemoryIndex = inMemoryIndex;
	}

	/**
	 * @param indexFactor
	 *            the indexFactor to set
	 */
	public void setIndexFactor(int indexFactor) {
		this.indexFactor = indexFactor;
	}

	/**
	 * @param dataBaseName
	 *            the dataBaseName to set
	 */
	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

	/**
	 * @param fullIndexFileName
	 *            the fullIndexFileName to set
	 */
	public void setFullIndexFileName(String fullIndexFileName) {
		this.fullIndexFileName = fullIndexFileName;
	}

	/**
	 * @param maxMissedCleavages
	 *            the maxMissedCleavages to set
	 */
	public void setMaxMissedCleavages(int maxMissedCleavages) {
		this.maxMissedCleavages = maxMissedCleavages;
		enzyme = new Enzyme(enzymeArr, maxMissedCleavages, semiCleavage);
	}

	/**
	 * @param maxPrecursorMass
	 *            the maxPrecursorMass to set
	 */
	public void setMaxPrecursorMass(double maxPrecursorMass) {
		this.maxPrecursorMass = maxPrecursorMass;
	}

	/**
	 * @param minPrecursorMass
	 *            the minPrecursorMass to set
	 */
	public void setMinPrecursorMass(double minPrecursorMass) {
		this.minPrecursorMass = minPrecursorMass;
	}

	/**
	 * @param useIndex
	 *            the useIndex to set
	 */
	public void setUseIndex(boolean useIndex) {
		this.useIndex = useIndex;
	}

	/**
	 * @param enzymeNocutResidues
	 *            the enzymeNocutResidues to set
	 */
	public void setEnzymeNocutResidues(String enzymeNocutResidues) {
		this.enzymeNocutResidues = enzymeNocutResidues;
	}

	/**
	 * @param enzymeResidues
	 *            the enzymeResidues to set
	 */
	public void setEnzymeResidues(String enzymeResidues, int maxMissedCleavages, boolean missCleave) {
		this.enzymeResidues = enzymeResidues;
		setEnzymeArr(enzymeResidues.toCharArray(), maxMissedCleavages, missCleave);
	}

	/**
	 * @param enzymeOffset
	 *            the enzymeOffset to set
	 */
	public void setEnzymeOffset(int enzymeOffset) {
		this.enzymeOffset = enzymeOffset;
	}

	/**
	 * @param isUseMonoParent
	 *            the isUseMonoParent to set
	 */
	public void setUseMonoParent(boolean isUseMonoParent) {
		this.isUseMonoParent = isUseMonoParent;
	}

	/**
	 * @param enzymeArr
	 *            the enzymeArr to set
	 */
	public void setEnzymeArr(char[] enzymeArr, int maxMissedCleavages, boolean missCleave) {
		if (enzymeArr.length > 0) {
			this.enzymeArr = enzymeArr;
			enzyme = new Enzyme(enzymeArr, maxMissedCleavages, missCleave);
			this.maxMissedCleavages = maxMissedCleavages;

			enzymeResidues = "";
			for (char ch : enzymeArr) {
				enzyme.addCleavePosition(ch);
				enzymeResidues += ch;
			}
		}
	}

	/**
	 * @param enzyme
	 *            the enzyme to set
	 */
	public void setEnzyme(Enzyme enzyme) {
		this.enzyme = enzyme;
	}

	/**
	 * @param isH2OPlusProtonAdded
	 *            the isH2OPlusProtonAdded to set
	 */
	public void setH2OPlusProtonAdded(boolean isH2OPlusProtonAdded) {
		this.isH2OPlusProtonAdded = isH2OPlusProtonAdded;
	}

	/**
	 * @param massGroupFactor
	 *            the massGroupFactor to set
	 */
	public void setMassGroupFactor(int massGroupFactor) {
		this.massGroupFactor = massGroupFactor;
	}

	/**
	 * @param mandatoryInternalAAs
	 *            the mandatoryInternalAAs to set
	 */
	public void setMandatoryInternalAAs(char[] mandatoryInternalAAs) {
		this.mandatoryInternalAAs = mandatoryInternalAAs;
	}

	@Override
	public boolean isUsingProtDB() {
		return usingProtDB;
	}

	@Override
	public boolean isUsingSeqDB() {
		return usingSeqDB;
	}

	@Override
	public String getMongoDBURI() {
		return mongoDBURI;
	}

	@Override
	public String getMassDBName() {
		return massDBName;
	}

	@Override
	public String getMassDBCollection() {
		return massDBCollection;
	}

	@Override
	public String getSeqDBName() {
		return seqDBName;
	}

	@Override
	public String getSeqDBCollection() {
		return seqDBCollection;
	}

	@Override
	public String getProtDBName() {
		return protDBName;
	}

	@Override
	public String getProtDBCollection() {
		return protDBCollection;
	}

	/**
	 * @param usingProtDB
	 *            the usingProtDB to set
	 */
	public void setUsingProtDB(boolean usingProtDB) {
		this.usingProtDB = usingProtDB;
	}

	/**
	 * @param usingSeqDB
	 *            the usingSeqDB to set
	 */
	public void setUsingSeqDB(boolean usingSeqDB) {
		this.usingSeqDB = usingSeqDB;
	}

	/**
	 * @param mongoDBURI
	 *            the mongoDBURI to set
	 */
	public void setMongoDBURI(String mongoDBURI) {
		this.mongoDBURI = mongoDBURI;
	}

	/**
	 * @param massDBName
	 *            the massDBName to set
	 */
	public void setMassDBName(String massDBName) {
		this.massDBName = massDBName;
	}

	/**
	 * @param massDBCollection
	 *            the massDBCollection to set
	 */
	public void setMassDBCollection(String massDBCollection) {
		this.massDBCollection = massDBCollection;
	}

	/**
	 * @param seqDBName
	 *            the seqDBName to set
	 */
	public void setSeqDBName(String seqDBName) {
		this.seqDBName = seqDBName;
	}

	/**
	 * @param seqDBCollection
	 *            the seqDBCollection to set
	 */
	public void setSeqDBCollection(String seqDBCollection) {
		this.seqDBCollection = seqDBCollection;
	}

	/**
	 * @param protDBName
	 *            the protDBName to set
	 */
	public void setProtDBName(String protDBName) {
		this.protDBName = protDBName;
	}

	/**
	 * @param protDBCollection
	 *            the protDBCollection to set
	 */
	public void setProtDBCollection(String protDBCollection) {
		this.protDBCollection = protDBCollection;
	}

	@Override
	public boolean isUsingMongoDB() {
		return usingMongoDB;
	}

	/**
	 * @param usingMongoDB
	 *            the usingMongoDB to set
	 */
	public void setUsingMongoDB(boolean usingMongoDB) {
		this.usingMongoDB = usingMongoDB;
	}

	@Override
	public boolean isSemiCleavage() {
		return semiCleavage;
	}

	public void setSemiCleavage(boolean semiCleavage) {
		this.semiCleavage = semiCleavage;
		enzyme = new Enzyme(enzymeArr, maxMissedCleavages, semiCleavage);
	}

}
