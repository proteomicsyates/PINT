package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mongodb.MongoException;

import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.FastaDefReader;
import edu.scripps.yates.dbindex.mongo.MongoConnect;
import edu.scripps.yates.dbindex.mongo.MongoSeqIter;
import edu.scripps.yates.dbindex.util.ProcessUtil;

/**
 *
 * Index store implementation based on mongo db engine. To support large indexes
 * and search of large databases.
 *
 * @author Adam
 */

/*
 * @author HARSHIL SHAH It might cause problem when having multiple connection
 * in mongoDB with the DBNAME as in the constructor i have get the database .. I
 * am not sure with this,.
 */
public class DBIndexStoreMongoDb implements DBIndexStore {

	// Mongo
	private MongoConnect mongoConnect;

	private static final Logger logger = Logger.getLogger(DBIndexStoreMongoDb.class.getName());

	private boolean inited = false;
	private static int DB_PORT = 27017;
	private final ProcessUtil mongoDbProcess = new ProcessUtil();
	private final FastaDefReader fastadefreader = new FastaDefReader();
	private DBIndexSearchParams sParam;

	DBIndexStoreMongoDb() {
	}

	DBIndexStoreMongoDb(DBIndexSearchParams sparam) {

		sParam = sparam;
		if (sparam.getDatabaseName() != null) {
			fastadefreader.setDefs(sparam.getDatabaseName());
			setProteinCache(fastadefreader.getproteinCache());
		}
	}

	private void stopDB() {
		// db.shutdownServer({timeoutSecs : 5})

		// if (db != null) {
		// db.command("{shutdown : 1, timeoutSecs : 5}");
		// db = null;
		// }

		if (mongoConnect != null) {

			mongoConnect.disconnect();
			mongoConnect = null;
		}

		mongoDbProcess.stop();
	}

	@Override
	protected void finalize() throws Throwable {

		try {
			// in case not yet stopped
			stopDB();
		} finally {
			super.finalize();
		}
	}

	@Override
	public void init(String databaseID) throws DBIndexStoreException {

		// startDB(indexDir);

		try {
			mongoConnect = new MongoConnect(sParam);
			// System.out.println("Connected to the mongoDB database...");
			System.out.println("Connected to MongoD_client");
			// fastadefreader.setDefs(this.);

		} catch (MongoException ex) {
			logger.log(Level.SEVERE, "Cannot connect to MongoDb instance at port: " + DB_PORT, ex);
			throw new DBIndexStoreException("Cannot connect to MongoDb instance at port: " + DB_PORT, ex);
		}

		long massDBCollectionCount = 0;
		if (mongoConnect.getMassDBCollection() != null) {
			massDBCollectionCount = mongoConnect.getMassDBCollection().count();
		}
		long seqDBCollectionCount = 0;
		if (mongoConnect.getSeqDBCollection() != null) {
			seqDBCollectionCount = mongoConnect.getSeqDBCollection().count();
		}
		long protDBCollectionCount = 0;
		if (mongoConnect.getProtDBCollection() != null) {
			protDBCollectionCount = mongoConnect.getProtDBCollection().count();
		}
		if (mongoConnect != null && (massDBCollectionCount + protDBCollectionCount + seqDBCollectionCount) > 0) {
			inited = true;
		}

	}

	@Override
	public void startAddSeq() throws DBIndexStoreException {

	}

	@Override
	public void stopAddSeq() throws DBIndexStoreException {

		stopDB();
	}

	@Override
	public boolean indexExists() throws DBIndexStoreException {
		if (!inited) {
			return false;
		}

		return getNumberSequences() > 0;
	}

	@Override
	public FilterResult filterSequence(double precMass, String sequence) {
		// no filtering, we add every sequence
		return FilterResult.INCLUDE;
	}

	@Override
	public void lastBuffertoDatabase() {

	}

	@Override
	public void addSequence(double precMass, int sequenceOffset, int sequenceLen, String sequence, String resLeft,
			String resRight, long proteinId) throws DBIndexStoreException {

	}

	@Override
	public List<IndexedSequence> getSequences(double precMass, double tolerance) throws DBIndexStoreException {

		List<IndexedSequence> allSeqs = new ArrayList<IndexedSequence>();

		List<MassRange> massRanges = new ArrayList<MassRange>();
		massRanges.add(new MassRange(precMass, tolerance));
		final MongoSeqIter sequencesIter = mongoConnect.getSequencesIter(massRanges);
		while (sequencesIter.hasNext()) {
			allSeqs.add(sequencesIter.next());
		}

		return allSeqs;

	}

	@Override
	public List<IndexedSequence> getSequences(List<MassRange> ranges) throws DBIndexStoreException {
		List<IndexedSequence> list = new ArrayList<IndexedSequence>();
		final MongoSeqIter sequencesIter = mongoConnect.getSequencesIter(ranges);
		while (sequencesIter.hasNext()) {
			list.add(sequencesIter.next());
		}
		return list;
	}

	@Override
	public Iterator<IndexedSequence> getSequencesIterator(List<MassRange> ranges) throws DBIndexStoreException {

		return mongoConnect.getSequencesIter(ranges);

	}

	@Override
	public long addProteinDef(long num, String accession, String protSequence) throws DBIndexStoreException {
		// using protein cache entirely
		// nothing here
		return num;
	}

	@Override
	public void setProteinCache(ProteinCache proteinCache) {
	}

	@Override
	public boolean supportsProteinCache() {
		return false;
	}

	@Override
	public List<IndexedProtein> getProteins(IndexedSequence sequence) throws DBIndexStoreException {
		// get prot id from index and prot ids from cache
		List<IndexedProtein> ret = new ArrayList<IndexedProtein>();

		try {
			final List<String> parents = mongoConnect.getParents(sequence.getSequence(), sParam, false);
			for (String string : parents) {
				final String[] split = string.split("\t");
				long proteinID = -1;
				String name = null;
				if (split[0].contains("||")) {
					final String[] split2 = split[0].split("\\|\\|");
					final String s = split2[0];
					boolean reverse = false;
					if (s.startsWith("Reverse_")) {
						reverse = true;
						proteinID = Long.valueOf(s.substring(8, s.length()));
					} else {
						proteinID = Long.valueOf(s);
					}
					name = split2[1];
					if (reverse) {
						name = "Reverse_" + name;
					}
				} else {
					proteinID = Long.valueOf(split[0]);
					name = split[0];
				}
				IndexedProtein protein = new IndexedProtein(name, proteinID);
				ret.add(protein);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	@Override
	public long getNumberSequences() throws DBIndexStoreException {
		if (!inited) {
			return 0l;
		}

		long numSeqDB = mongoConnect.getSeqDBCollection() != null ? mongoConnect.getSeqDBCollection().count() : 0;

		return numSeqDB;
	}

	@Override
	public ResidueInfo getResidues(IndexedSequence peptideSequence, IndexedProtein protein)
			throws DBIndexStoreException {
		// in this implementation residues are already stored in the index
		return new ResidueInfo(peptideSequence.getResLeft(), peptideSequence.getResLeft());
	}

}
