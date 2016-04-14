package edu.scripps.yates.dbindex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import edu.scripps.yates.dbindex.DBIndexStore.FilterResult;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.Fasta;
import edu.scripps.yates.dbindex.io.FastaReader;
import edu.scripps.yates.dbindex.io.SearchParamReader;
import edu.scripps.yates.dbindex.model.AssignMass;
import edu.scripps.yates.dbindex.model.Util;
import edu.scripps.yates.dbindex.util.IndexUtil;
import edu.scripps.yates.utilities.dates.DatesUtil;

/**
 *
 * Supports storing and retrieving proteins and sequences to/from index
 *
 * There can be different underlying store index implementations, implementing
 * DBIndexStore interface
 *
 * Supports modes: indexing only (store), search from indexed store, and search
 * without a store (cut on the fly) with temporary in-memory store
 *
 */
public class DBIndexer {

	/**
	 * Different operational modes supported
	 */
	public enum IndexerMode {

		INDEX, SEARCH_INDEXED, SEARCH_UNINDEXED,
	};

	/**
	 * Different operational modes supported
	 */
	public enum IndexType {

		INDEX_NORMAL {
			@Override
			public String toString() {
				return "Normal index (best for small and medium db)";
			}

		},
		INDEX_LARGE {
			@Override
			public String toString() {
				return "Large index (best for large db)";
			}

		},
	};

	private final IndexerMode mode;
	private IndexType indexType;
	private DBIndexStore indexStore;
	private final DBIndexSearchParams sparam;
	// private double massTolerance;
	private boolean inited;
	private String indexName; // index name that is params-specific
	private long protNum; // protein number in sequence, starting at 1
	private ProteinCache proteinCache;
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DBIndexer.class);
	private static final int MAX_SEQ_LENGTH = 10000;

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Need a directory name");
			System.exit(1);
		}

		runDBIndexer(args[0]);
	}

	public static void runDBIndexer(String path) {
		runDBIndexer(path, SearchParamReader.DEFAULT_PARAM_FILE_NAME);
	}

	public static void mergeLargeDBIndex(String path) {
		SearchParamReader preader;
		try {
			// System.out.println("path" + path);
			preader = new SearchParamReader(path, SearchParamReader.DEFAULT_PARAM_FILE_NAME);
			// preader = new SearchParamReader(args[0],
			// SearchParamReader.DEFAULT_PARAM_FILE_NAME);
		} catch (IOException ex) {
			logger.error("Error getting params", ex);
			return;
		}
		DBIndexSearchParams sparam = preader.getParam();

		if (!sparam.getIndexType().equals(IndexType.INDEX_LARGE)) {
			throw new RuntimeException("Merge only works for large db index type, other indexed have merge built-in");
		}

		// indexer in indexing mode
		DBIndexStoreFiles largeIndex = new DBIndexStoreFiles();
		try {
			largeIndex.merge(sparam);

		} catch (Exception ex) {
			logger.error("Error running merge on large index.", ex);
		}

	}

	public static void runDBIndexer(String path, String paramFile) {
		SearchParamReader preader;
		try {
			preader = new SearchParamReader(path, paramFile);
			// preader = new SearchParamReader(args[0],
			// SearchParamReader.DEFAULT_PARAM_FILE_NAME);
		} catch (IOException ex) {
			logger.error("Error getting params", ex);
			return;
		}
		DBIndexSearchParams sparam = preader.getParam();

		// indexer in indexing mode
		final DBIndexer indexer = new DBIndexer(sparam, IndexerMode.INDEX);
		try {
			indexer.init();
			indexer.run();
		} catch (DBIndexerException ex) {
			logger.error("Error running indexer in the index mode.", ex);
		}

	}

	/**
	 * Create new db indexer, passing params and indexing mode, and the
	 *
	 * @param sparam
	 * @param indexMode
	 *            indexing mode (search indexed, search unindexed, index only)
	 * @param massGroupFactor
	 *            this is the factor by which each double mass will be
	 *            multiplied to get the key in the index. Using any other
	 *            constructor, the massGroupFactor is 10000
	 */
	public DBIndexer(edu.scripps.yates.dbindex.io.DBIndexSearchParams sparam, IndexerMode mode) {
		IndexUtil.setNumRowsToLookup(0);
		this.sparam = sparam;
		this.mode = mode;

		// this.massTolerance = sparam.getRelativePeptideMassTolerance();

		if (!mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			indexType = sparam.getIndexType();
			if (indexType.equals(IndexType.INDEX_NORMAL) && !sparam.isUsingMongoDB()) {
				boolean inMemoryIndex = sparam.isInMemoryIndex();
				// if in memory index, use in-memory for search only
				// in future we can try indexing in-memory, but it will fail for
				// larger dbs
				if (mode.equals(IndexerMode.INDEX)) {
					inMemoryIndex = false;
				}
				indexStore = new DBIndexStoreSQLiteMult(sparam, inMemoryIndex);
				// /this.indexStore = new DBIndexStoreSQLiteString();
			} else {
				// index_type=2
				// this.indexStore = new DBIndexStoreFiles();
				indexStore = new DBIndexStoreMongoDb(sparam);
				// //this.indexStore = new DBIndexStoreLucene();
			}
			logger.info("Using database index type: " + indexType);

		} else {
			// set up in memory temporary "index" that does the filtering
			// this is the nonindexed saerch
			indexStore = new MassRangeFilteringIndex();
			logger.info("Using no-index for the search. ");
		}

		inited = false;
		protNum = -1;
	}

	/**
	 * Cut a Fasta protein sequence according to params spec and index the
	 * protein and generated sequences
	 *
	 * Should be called once per unique Fasta sequence
	 *
	 * @param fasta
	 *            fasta to index
	 * @throws IOException
	 */
	private void cutSeq(final Fasta fasta) throws IOException {
		// change by SALVA in order to keep all fasta information in the index
		// final String protAccession = fasta.getSequestLikeAccession();
		final String protAccession = fasta.getOriginalDefline();
		final String protSeq = fasta.getSequence();

		cutSeq(protAccession, protSeq);

	}

	/**
	 * Cut a Fasta protein sequence according to params spec and index the
	 * protein and generated sequences
	 *
	 * Should be called once per unique Fasta sequence
	 *
	 * @param fasta
	 *            fasta to index
	 * @throws IOException
	 */
	private void cutSeq(final String protAccession, final String protSeq) throws IOException {

		// Enzyme enz = sparam.getEnzyme();
		final int length = protSeq.length();
		//
		// AssignMass aMass = AssignMass.getInstance(true);

		final char[] pepSeq = new char[MAX_SEQ_LENGTH]; // max seq length
		int curSeqI = 0;

		int maxIntCleavage = sparam.getMaxMissedCleavages();
		// at least one of this AAs has to be in the sequence:
		final char[] mandatoryInternalAAs = sparam.getMandatoryInternalAAs();
		try {
			long proteinId = indexStore.addProteinDef(++protNum, protAccession, protSeq);
			// System.out.println(fasta.getSequestLikeAccession());
			// System.out.println(fasta.getDefline());
			// System.out.println(protSeq);

			for (int start = 0; start < length; ++start) {
				int end = start;

				// clear the preallocated seq byte array
				// Arrays.fill(seq, 0, curSeqI > 0?curSeqI-1:0, (byte) 0); //no
				// need, we copy up to curSeqI nowu
				curSeqI = 0;

				// double precMass = Constants.H2O_PROTON_SCALED_DOWN;
				double precMass = 0;

				// Salva added 24Nov2014
				if (sparam.isH2OPlusProtonAdded())
					precMass += Constants.H2O_PROTON;
				precMass += AssignMass.getcTerm();
				precMass += AssignMass.getnTerm();
				// System.out.println("===>>" + precMass + "\t" +
				// Constants.MAX_PRECURSOR);
				// System.out.println("==" + j + " " + length + " " + (j <
				// length));

				// int testC=0;
				int pepSize = 0;

				int intMisCleavageCount = -1;

				// while (precMass <= Constants.MAX_PRECURSOR_MASS && end <
				// length) {
				while (precMass <= sparam.getMaxPrecursorMass() && end < length) {
					pepSize++;

					final char curIon = protSeq.charAt(end);
					pepSeq[curSeqI++] = curIon;
					final double aaMass = AssignMass.getMass(curIon);
					precMass = precMass + aaMass;
					final String peptideSeqString = String.valueOf(Arrays.copyOf(pepSeq, curSeqI));

					if (sparam.getEnzyme().isEnzyme(protSeq.charAt(end))) {
						intMisCleavageCount++;
					}

					final boolean cleavageStatus = sparam.getEnzyme().checkCleavage(protSeq, start, end,
							sparam.getEnzymeNocutResidues());
					if (cleavageStatus) {

						if (intMisCleavageCount > maxIntCleavage) {
							break;
						}

						// if (precMass > Constants.MAX_PRECURSOR_MASS) {
						if (precMass > sparam.getMaxPrecursorMass()) {
							break;
						}

						if (pepSize >= Constants.MIN_PEP_LENGTH && precMass >= sparam.getMinPrecursorMass()) { // Constants.MIN_PRECURSOR
																												// )

							if (mandatoryInternalAAs != null) {
								boolean found = false;
								for (char internalAA : mandatoryInternalAAs) {
									if (peptideSeqString.indexOf(internalAA) >= 0) {
										found = true;
									}
								}
								if (!found) {
									break;
								}
							}

							// qualifies based on params

							// check if index will accept it
							final FilterResult filterResult = indexStore.filterSequence(precMass, peptideSeqString);

							if (filterResult.equals(FilterResult.SKIP_PROTEIN_START)) {
								// bail out earlier as we are no longer
								// interested in this protein starting at start
								break; // move to new start position
							} else if (filterResult.equals(FilterResult.INCLUDE)) {

								final int resLeftI = start >= Constants.MAX_INDEX_RESIDUE_LEN
										? start - Constants.MAX_INDEX_RESIDUE_LEN : 0;
								final int resLeftLen = Math.min(Constants.MAX_INDEX_RESIDUE_LEN, start);
								StringBuilder sbLeft = new StringBuilder(Constants.MAX_INDEX_RESIDUE_LEN);
								for (int ii = 0; ii < resLeftLen; ++ii) {
									sbLeft.append(protSeq.charAt(ii + resLeftI));
								}
								final int resRightI = end + 1;
								final int resRightLen = Math.min(Constants.MAX_INDEX_RESIDUE_LEN, length - end - 1);
								StringBuilder sbRight = new StringBuilder(Constants.MAX_INDEX_RESIDUE_LEN);
								if (resRightI < length) {
									for (int jj = 0; jj < resRightLen; ++jj) {
										sbRight.append(protSeq.charAt(jj + resRightI));
									}
								}

								// add -- markers to fill
								// Constants.MAX_INDEX_RESIDUE_LEN length
								final int lLen = sbLeft.length();
								for (int c = 0; c < Constants.MAX_INDEX_RESIDUE_LEN - lLen; ++c) {
									sbLeft.insert(0, '-');
								}
								final int rLen = sbRight.length();
								for (int c = 0; c < Constants.MAX_INDEX_RESIDUE_LEN - rLen; ++c) {
									sbRight.append('-');
								}

								final String resLeft = sbLeft.toString();
								final String resRight = sbRight.toString();

								indexStore.addSequence(precMass, start, curSeqI, peptideSeqString, resLeft, resRight,
										proteinId);
								// System.out.println("\t" + peptideSeqString);
							} // end if add sequence
						}
					}
					++end;
				}
				//
			}
		} catch (DBIndexStoreException e) {
			logger.error("Error writing sequence to db index store, ", e);
		}

	}

	/**
	 * Initialize the indexer
	 *
	 * @throws IOException
	 */
	public void init() throws DBIndexerException {
		if (inited) {
			throw new IllegalStateException("Already inited");
		}

		// reset prot number
		protNum = -1;

		final String dbName = sparam.getDatabaseName();

		if (!sparam.isUsingMongoDB()) {
			File dbFile = new File(dbName);
			if (!dbFile.exists() || !dbFile.canRead()) {
				throw new DBIndexerException("Cannot read (and index) the Fasta database file: " + dbName);
			}
		}
		// get param specific name for the inde0x
		indexName = getFullIndexFileName();

		try {
			// initialize index storage
			indexStore.init(indexName);
			if (mode.equals(IndexerMode.SEARCH_INDEXED)) {
				// if (mode.equals(IndexerMode.SEARCH_INDEXED) &&
				// sparam.getIndexType() == DBIndexer.IndexType.INDEX_NORMAL) {

				if (!indexStore.indexExists()) {
					logger.error("Error, cannot search, index does not exist: " + indexName);
					throw new DBIndexerException("Error, cannot search, index does not exist: " + indexName);
				}
				setProteinCache();
			} else if (mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
				setProteinCache();
			}
			inited = true;
		} catch (DBIndexStoreException ex) {
			logger.error("Could not initialize index storage.", ex);
			throw new DBIndexerException("Could not initialize index storage.");
		}
	}

	/**
	 * Set protein cache if storage supports it Useful if database has already
	 * been indexed Otherwise, the cache can be populated while indexing is done
	 */
	private void setProteinCache() {
		if (!indexStore.supportsProteinCache()) {
			return;
		}

		final ProteinCache protCache = getProteinCache();

		// ensure only 1 thread at time enters this
		synchronized (protCache) {

			logger.info("Populating protein cache");
			if (protCache.isPopulated() == false) {
				logger.info("Initializing protein cache");
				InputStream fis = null;
				try {
					fis = new FileInputStream(sparam.getDatabaseName());
				} catch (FileNotFoundException ex) {
					logger.error("Could not set protein cache", ex);
					return;
				}
				Iterator<Fasta> itr = null;
				try {
					itr = FastaReader.getFastas(fis);
				} catch (IOException ex) {
					logger.error("Could not set protein cache", ex);
					return;
				}

				while (itr.hasNext()) {
					Fasta fasta = itr.next();
					// SALVA CHANGE
					// protCache.addProtein(fasta.getSequestLikeAccession(),
					// fasta.getSequence());
					protCache.addProtein(fasta.getDefline(), fasta.getSequence());
				}

				logger.info("Done initializing protein cache");
			} else {
				// logger.info(
				// "Protein cache already populated, reusing.");
			}
		}

		indexStore.setProteinCache(protCache);
		// logger.info("Done populating protein cache");

	}

	/**
	 * Indexes the database if database index does not exists
	 *
	 * @throws IOException
	 */
	public void run() throws DBIndexerException {
		if (!inited) {
			throw new IllegalStateException("Not initialized.");
		}

		// if search unindexed, do nothing, will use protein cache to cutSeq on
		// the fly
		if (mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			return;
		}

		try {

			if (mode.equals(IndexerMode.INDEX)) {
				if (indexStore.indexExists()) {
					logger.info("Found existing index, skipping indexing.");

					// skip indexing
					return;
				} else {
					logger.warn("Found no sequences in the index, will now index.");
				}
			}
		} catch (DBIndexStoreException ex) {
			logger.error("Could not query number of sequences", ex);
			return;
		}

		FileInputStream fis = null; // fasta reader

		// setup status writer
		String statusFilePath = Util.getFileBaseName(sparam.getDatabaseName()) + "log";
		FileWriter statusWriter = null;
		int totalProteins = 0;
		String totalProteinsString = null;
		int indexedProteins = 0;
		long t0 = System.currentTimeMillis();
		try {
			statusWriter = new FileWriter(statusFilePath);
			totalProteins = FastaReader.getNumberFastas(sparam.getDatabaseName());
			totalProteinsString = String.valueOf(totalProteins);
		} catch (IOException ex) {
			logger.error("Error initializing index progress writer for file path: " + statusFilePath, ex);
		}

		int currentPercentage = 0;
		// start indexing
		try {
			// index, set protein cache on the fly
			indexStore.startAddSeq();

			fis = new FileInputStream(sparam.getDatabaseName());

			// create prot cache, in case searcher is kicked off in the same
			// process after indexing is done
			ProteinCache protCache = getProteinCache();
			indexStore.setProteinCache(protCache);

			for (Iterator<Fasta> itr = FastaReader.getFastas(fis); itr.hasNext();) {

				Fasta fasta = itr.next();
				// change by SALVA in order to keep all the information of the
				// header in the index
				// protCache.addProtein(fasta.getSequestLikeAccession(),
				// fasta.getSequence());
				protCache.addProtein(fasta.getOriginalDefline(), fasta.getSequence());
				cutSeq(fasta);
				// System.out.print("Printing the last buffer....");
				// indexStore.lastBuffertoDatabase();

				++indexedProteins;
				if (statusWriter != null) {
					statusWriter.append(totalProteinsString).append("\t").append(Integer.toString(indexedProteins))
							.append("\n");
					if (indexedProteins % 100 == 0) {
						statusWriter.flush();
					}
				}
				int percentage = indexedProteins * 100 / totalProteins;
				if (currentPercentage != percentage) {
					logger.info(indexedProteins + " proteins indexed (" + percentage + "%)");
					currentPercentage = percentage;
					// average time per percentage:
					double avgTime = (System.currentTimeMillis() - t0) / currentPercentage;
					int percentageRemaining = 100 - currentPercentage;
					double estimatedRemainingTime = percentageRemaining * avgTime;
					logger.info(DatesUtil.getDescriptiveTimeFromMillisecs(estimatedRemainingTime) + " remaining...");
				}

			}
			// System.out.print("Printing the last buffer....");
			// indexStore.lastBuffertoDatabase();

		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("Error initializing and adding sequences", ex);
			throw new DBIndexerException("Error initializing and adding sequences", ex);
		} catch (DBIndexStoreException ex) {
			ex.printStackTrace();
			logger.error("Error initializing and adding sequences", ex);
			throw new DBIndexerException("Error initializing and adding sequences", ex);
		} finally {
			try {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException ex) {
						ex.printStackTrace();
						logger.error("Cannot close stream", ex);
					}
				}
				indexStore.stopAddSeq();

				if (statusWriter != null) {
					try {
						statusWriter.flush();
						statusWriter.close();
					} catch (IOException ex) {
						ex.printStackTrace();
						logger.warn("Errir closing index status writer", ex);
					}
				}

			} catch (DBIndexStoreException ex) {
				ex.printStackTrace();
				logger.error("Error finalizing adding sequences", ex);
			}
		}

	}

	private ProteinCache getProteinCache() {
		if (proteinCache == null) {
			proteinCache = new ProteinCache();
		}
		return proteinCache;
	}

	private List<IndexedSequence> cutAndSearch(List<MassRange> massRanges) {
		if (!mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			throw new RuntimeException("Cut and search only supported for SEARCH_UNINDEXED mode !");
		}

		// set up in memory temporary "index" that does the filtering
		((MassRangeFilteringIndex) indexStore).init(massRanges);

		// reset prot number
		protNum = -1;

		// cut sequences in prot cache and check if qualify

		final ProteinCache protCache = getProteinCache();

		final int numProteins = protCache.getNumberProteins();

		for (int prot = 0; prot < numProteins; ++prot) {
			final String protDef = protCache.getProteinDef(prot);
			final String protSequence = protCache.getProteinSequence(prot);

			try {
				// stores sequences that match only in our temporary "index"
				cutSeq(protDef, protSequence);
			} catch (IOException ex) {
				logger.error("Error cutting sequence in no-index mode: " + protSequence, ex);
			}

		}

		List<IndexedSequence> sequences = null;
		try {
			sequences = indexStore.getSequences(massRanges);

		} catch (DBIndexStoreException ex) {
			logger.error("Error getting sequences from in-memory filtering index", ex);
		}

		return sequences;

	}

	/**
	 * Get list of sequences in index for the mass, and using tolerance in
	 * SearchParams. Mass dependant tolerance is already scaled/precalculated
	 *
	 * Requires call to init() and run() first, which might perform indexing, if
	 * index for current set of sequest params does not exist
	 *
	 * @param precursorMass
	 *            mass to select by, in Da
	 * @param massToleranceInDa
	 *            mass tolerance, already calculated for that mass as it is
	 *            mass-dependant. In Da.
	 * @return list of matching sequences
	 */
	public List<IndexedSequence> getSequencesUsingDaltonTolerance(double precursorMass, double massToleranceInDa) {
		if (mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			MassRange range = new MassRange(precursorMass, massToleranceInDa);
			List<MassRange> ranges = new ArrayList<MassRange>();
			ranges.add(range);
			return cutAndSearch(ranges);
		} else {
			try {
				return indexStore.getSequences(precursorMass, massToleranceInDa);
			} catch (DBIndexStoreException ex) {
				logger.error("Error getting sequences for the mass.", ex);
				return null;
			}
		}
	}

	/**
	 * Get list of sequences in index for the mass, and using tolerance in
	 * SearchParams. Mass dependant tolerance is NOT already
	 * scaled/precalculated, since it is in PPM
	 *
	 * Requires call to init() and run() first, which might perform indexing, if
	 * index for current set of sequest params does not exist
	 *
	 * @param precursorMass
	 *            mass to select by, in Da
	 * @param massToleranceInPPM
	 *            mass tolerance in PPM.
	 * @return list of matching sequences
	 */
	public List<IndexedSequence> getSequencesUsingPPMTolerance(double precursorMass, double massToleranceInPPM) {

		double massTolerance = IndexUtil.getToleranceInDalton(precursorMass, massToleranceInPPM);

		if (mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			MassRange range = new MassRange(precursorMass, massTolerance);
			List<MassRange> ranges = new ArrayList<MassRange>();
			ranges.add(range);
			return cutAndSearch(ranges);
		} else {
			try {
				final List<IndexedSequence> sequences = indexStore.getSequences(precursorMass, massTolerance);

				// using a predefined PRECISION, go up in the mass range to
				// see
				// if lower bound of higher masses PPM range in lower than
				// the
				// actual precursor mass, and in that case, search for that
				// mass.

				double upperBound = precursorMass + massTolerance;

				while (true) {
					// calculate the tolerance of the lowerBound
					double massTolerance2 = IndexUtil.getToleranceInDalton(upperBound, massToleranceInPPM);
					double lowerBoundOfUpperBoundMass = upperBound - massTolerance2;
					if (lowerBoundOfUpperBoundMass < precursorMass) {
						final List<IndexedSequence> sequences2 = indexStore.getSequences(upperBound, 0.0f);
						if (sequences2.isEmpty()) {
							break;
						} else {

							int numNewSeqs = 0;
							for (IndexedSequence indexedSequence2 : sequences2) {
								if (!sequences.contains(indexedSequence2)) {
									sequences.add(indexedSequence2);
									numNewSeqs++;
								}
							}
							if (numNewSeqs > 0)
								logger.info(numNewSeqs + " new sequences looking in upper mass " + upperBound
										+ " from precursor mass " + precursorMass);
						}
					} else {
						break;
					}
					double newupperBound = upperBound + Constants.PRECISION;
					if (newupperBound == upperBound)
						break;
					upperBound = newupperBound;

				}

				return sequences;
			} catch (DBIndexStoreException ex) {
				logger.error("Error getting sequences for the mass.", ex);
				return null;
			}
		}
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
		List<IndexedSequence> sequences = null;
		long t1 = System.currentTimeMillis();
		if (mode.equals(IndexerMode.SEARCH_UNINDEXED)) {
			sequences = cutAndSearch(massRanges);
		} else {
			try {

				sequences = indexStore.getSequences(massRanges);
			} catch (DBIndexStoreException ex) {
				logger.error("Error getting sequences for the mass ranges: " + massRanges.toString(), ex);
				return null;
			}
		}
		long t2 = System.currentTimeMillis();
		// System.out.println("DEBUG DBINdexer.getSequences(), seqs: " +
		// sequences.size() + ", time: " + ((t2-t1)) + "ms");

		// logger.info("getSequences(): " + sequences.size());
		return sequences;
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
		try {
			return indexStore.getProteins(seq);
		} catch (DBIndexStoreException ex) {
			logger.error("Error getting protein for the sequence", ex);
			return null;
		}
	}

	/**
	 * Get filename for index file base name The filename is specific to set of
	 * params that affect the index
	 *
	 * @return file name that includes base name and unique token for the set of
	 *         params that affect index
	 */
	private String getFullIndexFileName() {
		return sparam.getFullIndexFileName();
	}

	/*
	 * private String getFullIndexFileName(String baseName) { String
	 * uniqueIndexName = baseName + "_"; //generate a unique string based on
	 * current params that affect the index final StringBuilder uniqueParams =
	 * new StringBuilder();
	 * //uniqueParams.append(sparam.getEnzyme().toString());
	 * //uniqueParams.append(sparam.getEnzymeNumber());
	 * uniqueParams.append(sparam.getEnzymeOffset());
	 * uniqueParams.append(sparam.getEnzymeResidues());
	 * uniqueParams.append(sparam.getEnzymeNocutResidues());
	 * uniqueParams.append("\nCleav: ");
	 * uniqueParams.append(sparam.getMaxInternalCleavageSites());
	 * uniqueParams.append(sparam.getMaxMissedCleavages());
	 * uniqueParams.append(sparam.getMaxNumDiffMod());
	 * uniqueParams.append("\nMods:"); for (final ModResidue mod :
	 * sparam.getModList()) { uniqueParams.append(mod.toString()).append(" "); }
	 * uniqueParams.append("\nMods groups:"); for (final List<double>
	 * modGroupList : sparam.getModGroupList()) { for (final double f :
	 * modGroupList) { uniqueParams.append(f).append(" "); } } final String
	 * uniqueParamsStr = uniqueParams.toString(); //logger.info(
	 * "Unique params: " + uniqueParamsStr); }
	 */
	public void close() {
	}

	public boolean isInited() {
		return inited;
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
		Set<IndexedProtein> ret = new HashSet<IndexedProtein>();
		try {
			if (!sparam.isUsingSeqDB()) {
				// get the mass of the sequence
				final boolean h2oPlusProtonAdded = sparam.isH2OPlusProtonAdded();
				double mass = IndexUtil.calculateMass(seq, h2oPlusProtonAdded);
				// get the indexed sequences in the database
				List<IndexedSequence> indexedSequences = getSequencesUsingDaltonTolerance(mass, 0.0f);
				for (IndexedSequence indexedSequence : indexedSequences) {
					final String sequence = indexedSequence.getSequence();
					if (sequence.equals(seq))
						ret.addAll(indexStore.getProteins(indexedSequence));

				}
			} else {
				IndexedSequence indexPeptide = new IndexedSequence();
				indexPeptide.setSequence(seq);
				ret.addAll(indexStore.getProteins(indexPeptide));
			}
		} catch (DBIndexStoreException ex) {
			logger.error("Error getting protein for the sequence", ex);
			return null;
		}
		return ret;
	}

	/**
	 * The DBIndexer builds an index from a Fasta file in the same directory
	 * were the fasta file was located. This function will check if the entered
	 * parameter file is located in the default dbindex location (defined by a
	 * property in the dbindex.properties). Copy the file if it was not located
	 * there, and return the new File.
	 *
	 * @param fastaFile
	 * @return
	 */
	public static File moveFastaToIndexLocation(File fastaFile) {
		try {
			final String dbindexPath = DBIndexInterface.getDBIndexPath();
			final File dbIndexFolder = new File(dbindexPath);
			if (!fastaFile.getParentFile().equals(dbIndexFolder)) {
				File newFile = new File(dbIndexFolder.getAbsolutePath() + File.separator
						+ FilenameUtils.getName(fastaFile.getAbsolutePath()));
				if (newFile.exists())
					return newFile;
				FileUtils.copyFileToDirectory(fastaFile, dbIndexFolder);

				if (newFile.exists())
					return newFile;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fastaFile;
	}
}
