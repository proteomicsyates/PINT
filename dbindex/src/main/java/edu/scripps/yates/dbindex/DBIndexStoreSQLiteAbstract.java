package edu.scripps.yates.dbindex;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteJDBCLoader;

import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.SearchParams;

/**
 *
 * SQLite store abstract class with common code
 *
 * @author Adam
 */
public abstract class DBIndexStoreSQLiteAbstract implements DBIndexStore {

	protected boolean inTransaction;
	protected String dbPath;
	protected Connection con;
	protected final static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(DBIndexStoreSQLiteAbstract.class);
	protected boolean inited;
	protected static final String DB_NAME_SUFFIX = ".idx";
	protected static final long MASS_STORE_MULT = 10 * 1000 * 1000L; // store
																		// long,
																		// not
																		// doubles
	// we keep track of new primary keys (faster then query them)
	protected long curProtId = -1;
	protected long curSeqId = 0;
	// in-memory protein cache
	protected ProteinCache proteinCache;
	private PreparedStatement getMaxProtDefIdStatement;
	private PreparedStatement getMaxSeqIdStatement;
	private boolean inMemory = false;
	private boolean needBackup = false;
	private final String blazmass_sequences_pri_key = "id";

	protected DBIndexSearchParams sparam;

	/**
	 * construct new store
	 */
	public DBIndexStoreSQLiteAbstract(ProteinCache proteinCache) {
		inTransaction = false;
		inited = false;
		this.proteinCache = proteinCache;
		sparam = SearchParams.getInstance();

	}

	/**
	 * Construct new store, with in memory option
	 *
	 * @param inMemory
	 *            true if should use in memory database
	 */
	public DBIndexStoreSQLiteAbstract(DBIndexSearchParams sparam, boolean inMemory, ProteinCache proteinCache) {
		this(proteinCache);
		this.sparam = sparam;
		this.inMemory = inMemory;

	}

	@Override
	public final void init(String databaseID) throws DBIndexStoreException {
		if (databaseID == null || databaseID.equals("")) {
			throw new DBIndexStoreException("Index path is missing, cannot initialize the indexer.");
		}

		if (inited) {
			throw new DBIndexStoreException("Already intialized");
		}

		dbPath = databaseID;
		if (!dbPath.endsWith(DB_NAME_SUFFIX)) {
			dbPath = dbPath + DB_NAME_SUFFIX;
		}

		File indexFile = new File(dbPath);
		if (indexFile.exists() && !indexFile.canRead()) {
			throw new DBIndexStoreException("Index file already exists and is not readable: " + databaseID
					+ ", cannot initialize the indexer.");
		}

		Statement statement = null;
		try {
			try {
				// load driver
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException ex) {
				logger.error(null, ex);
				throw new DBIndexStoreException("Could not load sqlite driver, ", ex);
			}

			// increase cache size from 2k to 100k pages (100k * 4k bytes)
			final int indexFactor = sparam.getIndexFactor();
			final int cacheSize = 100000 / indexFactor;
			final int pageSize = 4096;

			SQLiteConfig config = new SQLiteConfig();
			// optimize for multiple connections that can share data structures
			config.setSharedCache(true);
			config.setCacheSize(cacheSize);
			config.setPageSize(pageSize);
			config.setJournalMode(SQLiteConfig.JournalMode.OFF);
			config.enableEmptyResultCallBacks(false);
			config.enableCountChanges(false);
			config.enableFullSync(false);
			config.enableRecursiveTriggers(false);
			config.setLockingMode(SQLiteConfig.LockingMode.NORMAL);
			config.setSynchronous(SQLiteConfig.SynchronousMode.OFF); // TODO may
																		// be
																		// dangerous
																		// on
																		// some
																		// systems
																		// to
																		// have
																		// off

			// TODO handle case when we want in memory for search only, not for
			// indexing
			if (inMemory) {
				File dbFile = new File(dbPath);
				if (dbFile.exists()) {
					// search mode
					// logger.info( "Database index: " + dbPath +
					// " already exists, will use that in memory");
					// inMemory = false;
					needBackup = false;
				} else {
					needBackup = true;
				}
			}

			if (inMemory) {
				// lower cache size
				config.setCacheSize(10000 / sparam.getIndexFactor());
				// limit to 10GB
				config.setMaxPageCount(10 * 1000 * 1000 * 1000 / pageSize);
				con = DriverManager.getConnection("jdbc:sqlite::memory:"); // ,
																			// config.toProperties());
				logger.debug("Using in-memory database");

				if (needBackup == false) {
					// already exists (search mode) so load it
					logger.info("Loading the in-memory database");
					con.prepareStatement("ATTACH DATABASE \"" + dbPath + "\" AS  inputDB").execute();

					// copy all the tables

					// create index and table
					con.prepareStatement("CREATE TABLE IF NOT EXISTS blazmass_sequences "
							+ "(precursor_mass_key INTEGER PRIMARY KEY, " + "data BINARY" + ");").execute();
					con.prepareStatement("CREATE INDEX IF NOT EXISTS precursor_mass_key_index_dsc ON "
							+ " blazmass_sequences (precursor_mass_key DESC);").execute();

					// copy data
					con.prepareStatement("INSERT INTO blazmass_sequences  SELECT * FROM inputDB.blazmass_sequences")
							.execute();
					// TODO create index?
					logger.info("Done loading the in-memory database");
				}

			} else {
				if (new File(dbPath).isDirectory()) {
					throw new RuntimeException(
							"Index should be a file, check if you are using the new version of indexer"
									+ " and delete the old index directory: " + dbPath);
				}
				con = DriverManager.getConnection("jdbc:sqlite:" + dbPath); // ,
																			// config.toProperties());

			}
			statement = con.createStatement();
			// reduce i/o operations, we have no OS crash recovery anyway
			// statement.execute("PRAGMA synchronous = OFF;"); //causes issues
			// on some systems, fastest
			// statement.execute("PRAGMA synchronous = FULL;"); //default,
			// safest
			// statement.execute("PRAGMA synchronous = NORMAL;");

			// change from default 1024 to speed up indexing
			statement.execute("PRAGMA page_size = " + pageSize + ";");

			// UTF8 uses 1 byte for ASCII
			statement.execute("PRAGMA encoding = \"UTF-8\";");

			// disable journal, we don't care about rollback
			statement.execute("PRAGMA journal_mode = OFF");
			// statement.execute("PRAGMA journal_mode = WAL");

			// increase cache size from 2k to 100k pages (100k * 4k bytes)
			statement.execute("PRAGMA cache_size = " + cacheSize);

			createTables();

			initStatementsCommon();
			initStatements();
			try {
				if (SQLiteJDBCLoader.isNativeMode() == false) {
					logger.warn(String.format("sqlite-jdbc version %s loaded in %s mode", SQLiteJDBCLoader.getVersion(),
							SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));
				}
			} catch (Exception ex) {
				logger.error("Can't check sqlite native mode", ex);
			}

			inited = true;

		} catch (SQLException e) {
			logger.error("Error initializing db, path: " + dbPath, e);
			throw new DBIndexStoreException("Error initializing db, path: " + dbPath, e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException ex) {
				logger.error(null, ex);
			}
		}
	}

	@Override
	public void startAddSeq() throws DBIndexStoreException {
		if (!inited) {
			throw new DBIndexStoreException("Indexer is not initialized");
		}

		if (inTransaction) {
			throw new DBIndexStoreException("In transaction already");
		}

		createTempIndex();

		try {
			con.setAutoCommit(false);

			// intialize primary key IDs we track
			curProtId = -1; // this.getLastProteinDefId(); //we do not support
							// reindex
			curSeqId = 0; // this.getLastSequenceId();

			// if (inMemory == true && this.curSeqId == 0) {
			// logger.info(
			// "Will create backup of the in-memory db when done");
			// needBackup = true;
			// }

		} catch (SQLException e) {
			logger.error("Unable to start add sequence transaction, ", e);
			throw new DBIndexStoreException("Unable to start add sequence transaction, ", e);
		}

		inTransaction = true;
	}

	/**
	 * Hook to flush out any cached data to db before final commit
	 *
	 * @throws SQLException
	 */
	protected void commitCachedData() throws SQLException {
	}

	@Override
	public void stopAddSeq() throws DBIndexStoreException {
		if (!inited) {
			throw new DBIndexStoreException("Indexer is not initialized");
		}

		if (!inTransaction) {
			throw new IllegalStateException("Not in transaction.");
		}

		// delete the temp index before commit
		deleteTempIndex();

		// commit
		try {
			// logger.info( "Commiting cached data if any.");
			commitCachedData();
			// logger.info( "Commiting index start.");
			con.commit();
			// logger.info( "Commiting index end.");
			con.setAutoCommit(true);
			inTransaction = false;
			// logger.info( "Creating db index start.");
			createIndex();
			// logger.info( "Creating db index end.");

			final long numSequences = getNumberSequences();
			// logger.info( "Number of sequences in " + this.dbPath +
			// " index: " + numSequences);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error commiting transaction, ", e);
			throw new DBIndexStoreException("Error committing transaction", e);
		}

		if (needBackup) {
			// logger.info( "Creating backup to " + this.dbPath);
			try {
				Statement st = con.createStatement();
				st.executeUpdate("backup to " + dbPath);
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Error creating backup, ", e);
				throw new DBIndexStoreException("Error creating backup", e);
			}
			// logger.info( "Done creating backup to " + this.dbPath);
		}

	}

	@Override
	public long getNumberSequences() throws DBIndexStoreException {
		if (!inited) {
			throw new DBIndexStoreException("Indexer is not initialized");
		}

		try {
			return getLastSequenceId();
		} catch (SQLException ex) {
			logger.error("Error executing query to get number of sequences.", ex);
			throw new DBIndexStoreException("Error executing query to get number of sequences.", ex);
		}

	}

	@Override
	public boolean indexExists() throws DBIndexStoreException {
		if (!inited) {
			throw new DBIndexStoreException("Not intialized");
		}
		try {
			return getNumberSequences() > 0;
		} catch (DBIndexStoreException ex) {
			logger.error("Could not check if index exists.", ex);
			return false;
		}
	}

	/**
	 * Helper to get id of lastly indexed protein def
	 *
	 * @return id
	 * @throws SQLException
	 *             exception throws if query errored
	 */
	protected long getLastProteinDefId() throws SQLException {
		if (getMaxProtDefIdStatement == null) {
			return -1;
		}
		ResultSet idRs = null;
		try {
			idRs = getMaxProtDefIdStatement.executeQuery();
			return idRs.getLong(1);
		} finally {
			if (idRs != null) {
				idRs.close();
			}
		}
	}

	/**
	 * Helper to get id of lastly indexed sequence
	 *
	 * @return id
	 * @throws SQLException
	 *             exception throws if query errored
	 */
	protected long getLastSequenceId() throws SQLException {
		if (getMaxSeqIdStatement == null) {
			return 0;
		}
		ResultSet idRs = null;
		try {
			idRs = getMaxSeqIdStatement.executeQuery();
			return idRs.getLong(1);
		} finally {
			if (idRs != null) {
				idRs.close();
			}
		}
	}

	protected void executeStatement(String statement) throws SQLException {
		Statement createSt = null;
		try {
			createSt = con.createStatement();
			createSt.execute(statement);
		} finally {
			if (createSt != null) {
				try {
					createSt.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
					logger.error("Error closing statement " + statement, ex);
				}
			}
		}

	}

	// caller must close the ResultSet and its Statement
	protected ResultSet executeQuery(String statement) throws SQLException {
		Statement createSt = null;
		ResultSet rs = null;
		try {
			createSt = con.createStatement();
			rs = createSt.executeQuery(statement);
		} finally {
			if (createSt != null) {
				// try {
				// createSt.close();
				// } catch (SQLException ex) {
				// logger.error( "Error closing statement " +
				// statement, ex);
				// }
			}
		}
		return rs;

	}

	/**
	 * Delete db index, implementation specific
	 *
	 * @throws DBIndexStoreException
	 */
	protected abstract void deleteTempIndex() throws DBIndexStoreException;

	/**
	 * Create temp db index, implementation specific
	 *
	 * @throws DBIndexStoreException
	 */
	protected abstract void createTempIndex() throws DBIndexStoreException;

	/**
	 * Create db index, implementation specific
	 *
	 * @throws DBIndexStoreException
	 */
	protected abstract void createIndex() throws DBIndexStoreException;

	/**
	 * Create db tables, implementation specific
	 *
	 * @throws DBIndexStoreException
	 */
	protected abstract void createTables() throws DBIndexStoreException;

	/**
	 * init statements, implementation specific
	 *
	 * @throws DBIndexStoreException
	 */
	protected abstract void initStatements() throws DBIndexStoreException;

	/**
	 * init statements common
	 *
	 * @throws DBIndexStoreException
	 */
	protected void initStatementsCommon() throws DBIndexStoreException {
		try {
			getMaxProtDefIdStatement = con.prepareStatement("SELECT MAX(id) FROM blazmass_proteins;");
			getMaxSeqIdStatement = con
					.prepareStatement("SELECT MAX(" + getSequencesTablePriKey() + ") FROM blazmass_sequences;");
		} catch (SQLException e) {
			logger.error("Could not initialize statement", e);
			throw new DBIndexStoreException("Could not initialize statement", e);
		}
	}

	protected String getSequencesTablePriKey() {
		return blazmass_sequences_pri_key;
	}

	@Override
	public void finalize() {
		try {
			super.finalize();
		} catch (Throwable ex) {
			logger.error(null, ex);
		}
		try {

			if (getMaxProtDefIdStatement != null) {
				getMaxProtDefIdStatement.close();
			}

			if (getMaxSeqIdStatement != null) {
				getMaxSeqIdStatement.close();
			}

			closeConnection();

			// if (con != null) {
			// con.close();
			// }
		} catch (SQLException ex) {
			logger.error("Error closing SQLite connection", ex);
		}
	}

	/**
	 * Cleanup any prepared statements/connections
	 */
	protected abstract void closeConnection();
}
