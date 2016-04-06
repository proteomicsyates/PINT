package edu.scripps.yates.dbindex;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.dbindex.DBIndexer.IndexType;
import edu.scripps.yates.dbindex.DBIndexer.IndexerMode;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.DBIndexSearchParamsImpl;
import edu.scripps.yates.dbindex.io.SearchParamReader;
import edu.scripps.yates.dbindex.io.SearchParams;
import edu.scripps.yates.dbindex.model.AssignMass;
import edu.scripps.yates.dbindex.util.PropertiesReader;

public class DBIndexInterface {
	private final static Logger log = Logger.getLogger(DBIndexInterface.class);
	private static String dbIndexPath;
	private DBIndexer indexer;
	private static final String PINT_DEVELOPER_ENV_VAR = "PINT_DEVELOPER";

	private final Map<String, Set<IndexedProtein>> proteinsByPeptideSeqs = new HashMap<String, Set<IndexedProtein>>();
	private final static Map<File, DBIndexInterface> dbIndexByFile = new HashMap<File, DBIndexInterface>();
	private final static Map<String, DBIndexInterface> dbIndexByParamKey = new HashMap<String, DBIndexInterface>();

	public static DBIndexInterface getByParamFile(File paramFile) {
		if (dbIndexByFile.containsKey(paramFile)) {
			return dbIndexByFile.get(paramFile);
		}
		return new DBIndexInterface(paramFile);
	}

	public static DBIndexInterface getByParam(DBIndexSearchParams sParam) {
		if (dbIndexByParamKey.containsKey(sParam.getFullIndexFileName())) {
			return dbIndexByParamKey.get(sParam.getFullIndexFileName());
		}
		return new DBIndexInterface(sParam);
	}

	/**
	 * Load the index of a fasta database stated on the paramFile. this will
	 * construct the index if it is not previously build.
	 *
	 * @param paramFile
	 */
	public DBIndexInterface(File paramFile) {
		String paramFileName = SearchParamReader.DEFAULT_PARAM_FILE_NAME;

		try {

			String dbIndexPath = getDBIndexPath();
			SearchParamReader pr = null;
			if (paramFile != null) {
				pr = new SearchParamReader(paramFile);
			} else {
				pr = new SearchParamReader(dbIndexPath, paramFileName);
			}

			SearchParams sParam = pr.getSearchParams();
			final edu.scripps.yates.dbindex.DBIndexer.IndexerMode indexerMode = sParam.isUseIndex()
					? IndexerMode.SEARCH_INDEXED : IndexerMode.SEARCH_UNINDEXED;

			indexer = new DBIndexer(sParam, indexerMode);
			try {
				indexer.init();

			} catch (DBIndexerException ex) {
				indexer = new DBIndexer(sParam, IndexerMode.INDEX);
				try {
					indexer.init();
					indexer.run();
				} catch (DBIndexerException e) {
					e.printStackTrace();
					log.error("Could not initialize the indexer in search mode and init the worker thread");
				}
			}
			dbIndexByFile.put(paramFile, this);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	/**
	 * Load the index of a fasta database stated on the paramFile. this will
	 * construct the index if it is not previously build.
	 *
	 * @param paramFile
	 */
	public DBIndexInterface(DBIndexSearchParams sParam) {
		try {
			final edu.scripps.yates.dbindex.DBIndexer.IndexerMode indexerMode = sParam.isUseIndex()
					? IndexerMode.SEARCH_INDEXED : IndexerMode.SEARCH_UNINDEXED;
			// init the masses
			AssignMass.getInstance(sParam.isUseMonoParent());

			indexer = new DBIndexer(sParam, indexerMode);
			try {
				indexer.init();

			} catch (DBIndexerException ex) {
				indexer = new DBIndexer(sParam, IndexerMode.INDEX);
				try {
					indexer.init();
					indexer.run();
				} catch (DBIndexerException e) {
					e.printStackTrace();
					log.error("Could not initialize the indexer in search mode and init the worker thread");
				}
			}
			dbIndexByParamKey.put(sParam.getFullIndexFileName(), this);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

	}

	public static String getDBIndexPath() {
		if (dbIndexPath == null) {
			Map<String, String> env = System.getenv();
			String dbIndexPathProperty = PropertiesReader.DBINDEX_PATH_SERVER;
			if (env.containsKey(PINT_DEVELOPER_ENV_VAR) && env.get(PINT_DEVELOPER_ENV_VAR).equals("true")) {
				log.info("USING DEVELOPMENT MODE");
				dbIndexPathProperty = PropertiesReader.DBINDEX_PATH;
			}
			log.info("Using property: " + dbIndexPathProperty);
			try {
				dbIndexPath = (String) PropertiesReader.getProperties().get(dbIndexPathProperty);
				log.info("Using local folder: " + dbIndexPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dbIndexPath;
	}

	/**
	 * Get list of sequences in index for the mass, and using tolerance in
	 * SearchParams. Mass dependant tolerance is already scaled/precalculated
	 *
	 * Requires call to init() and run() first, which might perform indexing, if
	 * index for current set of sequest params does not exist
	 *
	 * @param precursorMass
	 *            mass to select by, in ppm
	 * @param massTolerance
	 *            mass tolerance, already calculated for that mass as it is
	 *            mass-dependant
	 * @return list of matching sequences
	 */
	public List<IndexedSequence> getSequences(double precursorMass, double massTolerance) {
		return indexer.getSequencesUsingDaltonTolerance(precursorMass, massTolerance);
	}

	/**
	 * Get list of sequences in index for the ranges specified wit mass .
	 * Requires call to init() and run() first, which might perform indexing, if
	 * index for current set of sequest params does not exist
	 *
	 * @param massRanges
	 *            mass ranges to query
	 * @return list of matching sequences
	 */
	public List<IndexedSequence> getSequences(List<MassRange> massRanges) {
		return indexer.getSequences(massRanges);
	}

	/**
	 * Get proteins associated with the sequence from the index. Requires call
	 * to init() and run() first, which might perform indexing, if index for
	 * current set of sequest params does not exist
	 *
	 * @param seq
	 *            peptide sequence
	 * @return list of indexed protein objects associated with the sequence
	 */
	public List<IndexedProtein> getProteins(IndexedSequence seq) {
		return indexer.getProteins(seq);
	}

	/**
	 * Get proteins associated with the sequence. Requires call to init() and
	 * run() first, which might perform indexing, if index for current set of
	 * sequest params does not exist
	 *
	 * @param seq
	 *            peptide sequence
	 * @return list of indexed protein objects associated with the sequence
	 */
	public Set<IndexedProtein> getProteins(String seq) {
		// look into the cached proteins
		if (proteinsByPeptideSeqs.containsKey(seq) && !proteinsByPeptideSeqs.get(seq).isEmpty())
			return proteinsByPeptideSeqs.get(seq);
		final Set<IndexedProtein> proteins = indexer.getProteins(seq);
		if (proteins != null && !proteins.isEmpty()) {
			proteinsByPeptideSeqs.put(seq, proteins);
		}
		// log.debug(proteins.size() + " proteins contains peptide '" + seq +
		// "'");
		return proteins;
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParams(File fastaFile) {
		return getDefaultDBIndexParams(fastaFile.getAbsolutePath());
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @param inMemoryIndex
	 *            overrides the default property of "in memory index"
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParams(String fastaFilePath, boolean inMemoryIndex) {
		try {
			IndexType indexType = DBIndexer.IndexType.valueOf(
					String.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_INDEX_TYPE)));

			int indexFactor = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_INDEX_FACTOR));
			String dataBaseName = fastaFilePath;
			int maxMissedCleavages = Integer.valueOf(PropertiesReader.getProperties()
					.getProperty(PropertiesReader.DEFAULT_MAX_INTERNAL_CLEAVAGES_SITES));
			double maxPrecursorMass = Double
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MAX_PRECURSOR_MASS));
			double minPrecursorMass = Double
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MIN_PRECURSOR_MASS));
			boolean useIndex = Boolean
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.USE_INDEX));
			String enzymeNocutResidues = String
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_NOCUT_RESIDUES));

			String enzymeResidues = String
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_RESIDUES));

			int enzymeOffset = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_OFFSET));

			boolean useMono = Boolean
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MASS_TYPE_PARENT));

			boolean isH2OPlusProtonAdded = Boolean
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ADD_H2O_PLUS_PROTON));
			int massGroupFactor = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MASS_GROUP_FACTOR));

			DBIndexSearchParamsImpl ret = new DBIndexSearchParamsImpl(indexType, inMemoryIndex, indexFactor,
					dataBaseName, maxMissedCleavages, maxPrecursorMass, minPrecursorMass, useIndex, enzymeNocutResidues,
					enzymeResidues, enzymeOffset, useMono, isH2OPlusProtonAdded, massGroupFactor, null, false);
			return ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParams(String fastaFilePath) {
		boolean inMemoryIndex;
		try {
			Map<String, String> env = System.getenv();
			if (env.containsKey(PINT_DEVELOPER_ENV_VAR) && env.get(PINT_DEVELOPER_ENV_VAR).equals("true")) {
				log.info("USING DEVELOPMENT MODE");

				inMemoryIndex = Boolean.valueOf(
						PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_IN_MEMORY_INDEX));
			} else {
				inMemoryIndex = Boolean.valueOf(
						PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_IN_MEMORY_INDEX));
			}
			log.info("InMemoryIndex=" + inMemoryIndex);
			return getDefaultDBIndexParams(fastaFilePath, inMemoryIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParamsForCrosslinkerAnalysis(File fastaFile) {
		return getDefaultDBIndexParamsForCrosslinkerAnalysis(fastaFile.getAbsolutePath());
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParamsForCrosslinkerAnalysis(String fastaFilePath) {
		boolean inMemoryIndex;
		try {
			Map<String, String> env = System.getenv();
			if (env.containsKey(PINT_DEVELOPER_ENV_VAR) && env.get(PINT_DEVELOPER_ENV_VAR).equals("true")) {
				log.info("USING DEVELOPMENT MODE");
				inMemoryIndex = false;

			} else {
				inMemoryIndex = Boolean.valueOf(
						PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_IN_MEMORY_INDEX));
			}
			log.info("InMemoryIndex=" + inMemoryIndex);
			return getDefaultDBIndexParamsForCrosslinkerAnalysis(fastaFilePath, inMemoryIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets the default search params.<br>
	 * <b>Note that this parameters will not be valid for crosslinker analysis,
	 * since H2O+PROTON mass is added to any peptide mass in the index.
	 *
	 * @param fastaFile
	 * @param inMemoryIndex
	 *            overrides the property inMemoryIndex
	 * @return
	 */
	public static DBIndexSearchParams getDefaultDBIndexParamsForCrosslinkerAnalysis(String fastaFilePath,
			boolean inMemoryIndex) {
		try {
			IndexType indexType = DBIndexer.IndexType.valueOf(
					String.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_INDEX_TYPE)));

			int indexFactor = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.DEFAULT_INDEX_FACTOR));
			String dataBaseName = fastaFilePath;
			int maxMissedCleavages = Integer.valueOf(PropertiesReader.getProperties()
					.getProperty(PropertiesReader.DEFAULT_MAX_INTERNAL_CLEAVAGES_SITES));
			double maxPrecursorMass = Double
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MAX_PRECURSOR_MASS));
			double minPrecursorMass = Double
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MIN_PRECURSOR_MASS));
			boolean useIndex = Boolean
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.USE_INDEX));
			String enzymeNocutResidues = String
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_NOCUT_RESIDUES));

			String enzymeResidues = String
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_RESIDUES));

			int enzymeOffset = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.ENZYME_OFFSET));

			boolean useMono = Boolean
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MASS_TYPE_PARENT));
			// SET TO FALSE TO ALLOW CROSSLINKER ANALYSES
			boolean isH2OPlusProtonAdded = false;
			int massGroupFactor = Integer
					.valueOf(PropertiesReader.getProperties().getProperty(PropertiesReader.MASS_GROUP_FACTOR));

			char[] mandatoryInternalAAs = PropertiesReader.getProperties()
					.getProperty(PropertiesReader.MANDATORY_INTERNAL_AAs).toCharArray();

			DBIndexSearchParamsImpl ret = new DBIndexSearchParamsImpl(indexType, inMemoryIndex, indexFactor,
					dataBaseName, maxMissedCleavages, maxPrecursorMass, minPrecursorMass, useIndex, enzymeNocutResidues,
					enzymeResidues, enzymeOffset, useMono, isH2OPlusProtonAdded, massGroupFactor, mandatoryInternalAAs,
					false);
			return ret;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the indexer
	 */
	public DBIndexer getIndexer() {
		return indexer;
	}
}
