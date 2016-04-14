package edu.scripps.yates.dbindex;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.util.IndexUtil;

/**
 * a variation that performs merge at index time instead of merge at search time
 */
public class DBIndexStoreSQLiteByteIndexMerge extends DBIndexStoreSQLiteByte {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(DBIndexStoreSQLiteByteIndexMerge.class);
	private static final int SEQ_SEPARATOR_INT = Integer.MAX_VALUE;
	private static final byte[] SEQ_SEPARATOR = DynByteBuffer.toByteArray(SEQ_SEPARATOR_INT); // read
																								// protein
																								// ids
																								// until
																								// hitting
																								// the
																								// separator
	private PreparedStatement getMassDataStatement;

	DBIndexStoreSQLiteByteIndexMerge(int bucketId, ProteinCache proteinCache) {
		super(bucketId, proteinCache);
	}

	DBIndexStoreSQLiteByteIndexMerge(boolean inMemory, int bucketId, ProteinCache proteinCache) {
		super(inMemory, bucketId, proteinCache);
	}

	DBIndexStoreSQLiteByteIndexMerge(DBIndexSearchParams searchParams, boolean inMemory, int bucketId,
			ProteinCache proteinCache) {
		super(searchParams, inMemory, bucketId, proteinCache);
	}

	@Override
	protected void initStatements() throws DBIndexStoreException {
		try {
			super.initStatements();

			getMassDataStatement = con
					.prepareStatement("SELECT precursor_mass_key, data " + "FROM " + getIndexTableName());
		} catch (SQLException ex) {
			throw new DBIndexStoreException("Error initializing get mass data st", ex);
		}
	}

	@Override
	protected void createIndex() throws DBIndexStoreException {
		// using the createIndex hook to merge peptides at end of the stop()

		logger.info("Merging sequences and compacting the index db: " + dbPath);
		mergePeptides();

		try {
			// vacuum, should trim 30-50% size
			// also consider TEMP tables
			executeStatement("VACUUM;");
		} catch (SQLException ex) {
			ex.printStackTrace();
			logger.error("Error compacting the index after the merge", ex);
		}

		// now can create the index
		super.createIndex();

		logger.info("Done merging sequences and compacting the index db: " + dbPath);
	}

	private void mergePeptides() throws DBIndexStoreException {
		// logger.info( "Merging sequence index start");
		ResultSet massKeysRs = null;
		try {
			con.setAutoCommit(false);

			massKeysRs = getMassDataStatement.executeQuery();

			// go over each row and merge peptides
			while (massKeysRs.next()) {
				final int massKey = massKeysRs.getInt(1);

				final byte[] seqData = massKeysRs.getBytes(2);
				if (seqData.length % 4 != 0) {
					System.out.println("Cuidado " + massKey);

				}
				final byte[] seqDataMerged = getMergedData(seqData);
				if (seqDataMerged != null) {
					updateSeqStatement.setBytes(1, seqDataMerged);
					updateSeqStatement.setInt(2, massKey);
					updateSeqStatement.execute();
				}
			}

			con.commit();

		} catch (SQLException ex) {
			ex.printStackTrace();
			logger.error("Error merging sequences", ex);
		} finally {
			try {
				if (massKeysRs != null) {
					massKeysRs.close();
				}
				con.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
				logger.error("Error restoring autocommit", ex);
			}
		}
		// logger.info( "Merging sequence index end");
	}

	@Override
	protected void closeConnection() {

		if (getMassDataStatement != null) {
			try {
				getMassDataStatement.close();
				getMassDataStatement = null;
			} catch (SQLException ex) {
				logger.error("Error closing get mass data st.", ex);
			}
		}

		super.closeConnection();

	}

	@Override
	public List<IndexedSequence> getSequences(double precMass, double tolerance) throws DBIndexStoreException {
		if (!inited) {
			throw new DBIndexStoreException("Indexer is not initialized");
		}

		// logger.log(Level.FINE, "Starting peptide sequences query");
		// long start = System.currentTimeMillis();

		double toleranceFactor = tolerance; // precMass * tolerance;
		double minMassF = precMass - toleranceFactor;
		if (minMassF < 0.0) {
			minMassF = 0.0;
		}
		double maxMassF = precMass + toleranceFactor;

		// changed by Salva 11Nov2014, using the value on IndexUtil
		int precMassInt = (int) (precMass * sparam.getMassGroupFactor());
		// long toleranceInt = (long) (tolerance * MASS_STORE_MULT);
		// changed by Salva 11Nov2014, using the value on IndexUtil
		int toleranceInt = (int) (toleranceFactor * sparam.getMassGroupFactor()); // Robin
		// fixed the ppm calculation

		int minMass = precMassInt - toleranceInt;
		// change by Salva 21Nov2014
		minMass = (int) (minMassF * sparam.getMassGroupFactor());
		// System.out.println(precMassInt + " " + toleranceInt);

		if (minMass < 0) {
			minMass = 0;
		}
		int maxMass = precMassInt + toleranceInt;
		// change by Salva 21Nov2014
		maxMass = (int) (maxMassF * sparam.getMassGroupFactor());

		List<IndexedSequence> ret = new ArrayList<IndexedSequence>();

		ResultSet rs = null;
		try {
			// changed by Salva 11Nov2014, using the value on IndexUtil
			getSeqStatement.setInt(1, minMass - IndexUtil.getNumRowsToLookup());// PPM_PER_ENTRY);
			// changed by Salva 11Nov2014, using the value on IndexUtil
			getSeqStatement.setInt(2, maxMass + IndexUtil.getNumRowsToLookup());// PPM_PER_ENTRY);
			rs = getSeqStatement.executeQuery();

			while (rs.next()) {
				// final int massKey = rs.getInt(1);
				final byte[] seqData = rs.getBytes(2);

				parseAddPeptideInfo(seqData, ret, minMassF, maxMassF);

			}

		} catch (SQLException e) {
			String msg = "Error getting peptides ";
			logger.error(msg, e);
			throw new DBIndexStoreException(msg, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.error("Error closing result set after getting sequences", ex);
				}
			}
		}

		// long end = System.currentTimeMillis();
		// logger.info( "Peptide sequences query took: " + (end -
		// start) + " milisecs, results: " + ret.size());

		return ret;
	}

	@Override
	public List<IndexedSequence> getSequences(List<MassRange> ranges) throws DBIndexStoreException {

		int numRanges = ranges.size();

		if (numRanges == 1) {
			// use the standard method for the single range, as it is
			// slightly more optimized
			MassRange range = ranges.get(0);
			return getSequences(range.getPrecMass(), range.getTolerance());
		}

		ArrayList<Interval> intervals = new ArrayList<Interval>();

		for (MassRange range : ranges) {
			Interval ith = Interval.massRangeToInterval(range);
			intervals.add(ith);
		}

		return getSequencesIntervals(intervals);

	}

	public List<IndexedSequence> getSequencesIntervals(List<Interval> ranges) throws DBIndexStoreException {

		int numRanges = ranges.size();

		List<IndexedSequence> ret = new ArrayList<IndexedSequence>();

		PreparedStatement rangesStatement = null;
		boolean tempStatement = false;
		if (numRanges <= Constants.MAX_MASS_RANGES) {
			rangesStatement = getSeqMassRanges2_Statements[numRanges - 1];
		} else {
			// fallback to build non-optimized statement on the fly

			StringBuilder stSb = new StringBuilder();
			stSb.append("SELECT DISTINCT precursor_mass_key, data ");
			stSb.append("FROM ").append(getIndexTableName()).append(" WHERE ");

			for (int st = 0; st < ranges.size(); ++st) {
				Interval range = ranges.get(st);
				if (st > 0) {
					stSb.append(" OR ");
				}

				double minMassF = range.getStart();
				double maxMassF = range.getEnd();
				stSb.append("precursor_mass_key BETWEEN ").append(minMassF).append(" AND ").append(maxMassF);
			}
			try {
				rangesStatement = con.prepareStatement(stSb.toString());
			} catch (SQLException ex) {
				logger.error("Error preparing a temp statement: " + stSb.toString(), ex);
			}
			tempStatement = true;

		}

		// setup the query
		// supporting 2, 3 or 4+ ranges per query
		int curRange = 0;
		// record actual double min and max masses to reuse when filtering
		// out sequences within a row
		double[] minMassesF = new double[numRanges];
		double[] maxMassesF = new double[numRanges];

		for (Interval range : ranges) {

			// double precMass = range.getPrecMass();
			// double tolerance = range.getTolerance();

			// double toleranceFactor = tolerance; //precMass * tolerance;
			double minMassF = range.getStart();
			double maxMassF = range.getEnd();

			// int precMassInt = (int) (precMass * MASS_GROUP_FACTOR);
			// long toleranceInt = (long) (tolerance * MASS_STORE_MULT);
			// int toleranceInt = (int) (toleranceFactor *
			// MASS_GROUP_FACTOR); //Robin fixed the ppm calculation

			// figure out the rows to query
			// changed by Salva 11Nov2014
			// int minMass = (int) minMassF;
			int minMass = (int) minMassF - IndexUtil.getNumRowsToLookup();
			// System.out.println(precMassInt + " " + toleranceInt);
			// changed by Salva 11Nov2014
			int maxMass = (int) (maxMassF + IndexUtil.getNumRowsToLookup());// PPM_PER_ENTRY);

			int startCol = 2 * curRange + 1;
			try {
				if (tempStatement == false) {
					// use optimized prep statements
					rangesStatement.setInt(startCol, minMass); // -
																// PPM_PER_ENTRY);
																// //TODO do
																// we need
																// to look
																// in
																// another
																// +-1 row ?
																// I don't
																// think so
					rangesStatement.setInt(startCol + 1, maxMass); // +
																	// PPM_PER_ENTRY);
																	// //since
																	// BETWEEN
																	// query
																	// is
																	// inclusive
				}
			} catch (SQLException ex) {
				logger.error("Error setting up range query for range: " + curRange, ex);
				throw new DBIndexStoreException("Error setting up range query for range: " + curRange, ex);
			}

			minMassesF[curRange] = minMassF;
			maxMassesF[curRange] = maxMassF;

			++curRange;
		}

		// run the query
		ResultSet rs = null;

		try {
			rs = rangesStatement.executeQuery();
			while (rs.next()) {
				// final int massKey = rs.getInt(1);
				final byte[] seqData = rs.getBytes(2);
				parseAddPeptideInfo(seqData, ret, minMassesF, maxMassesF);
			}
		} catch (SQLException e) {
			String msg = "Error getting peptides ";
			logger.error(msg, e);
			throw new DBIndexStoreException(msg, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
					logger.error("Error closing result set after getting sequences", ex);
				}
			}
			if (rangesStatement != null && tempStatement == true) {
				try {
					// destroy if temp statement
					rangesStatement.close();
				} catch (SQLException ex) {
					logger.error("Error closing temp statement after getting sequences", ex);
				}
			}
		}

		return ret;
	}

	/**
	 * Parse the encoded sequences and return them in toInsert
	 *
	 * @param data
	 * @param toInsert
	 * @param minMass
	 *            used to eliminate not matching masses in the 1ppm row
	 * @param maxMass
	 *            used to eliminate not matching masses in the 1ppm row
	 */
	private void parseAddPeptideInfo(byte[] data, List<IndexedSequence> toInsert, double minMass, double maxMass) {

		int dataLength = data.length;

		// if (dataLength % 4 != 0) {
		// throw new RuntimeException("Unexpected number of peptide items: "
		// + dataLength);
		// }
		if (dataLength % 4 != 0) {
			System.out.println(dataLength);
			return;
		}
		for (int i = 0; i < dataLength;) {
			final int zeroeth = i;
			i += 8;// double
			final int first = i;
			i += 4;
			final int second = i;
			i += 4;
			final int third = i;
			i += 4;
			final int fourth = i;

			byte[] slice = Arrays.copyOfRange(data, zeroeth, first);
			double seqMass = DynByteBuffer.toDouble(slice);

			// since it's sorted
			// if current mass great than maxmass, break - optimize
			if (seqMass > maxMass) {
				break;
			}

			if (seqMass < minMass) {
				// skip the sequence, it qualified the row, but not the
				// actual peptide by precise mass

				// skip to next sequence
				while (true) {
					slice = Arrays.copyOfRange(data, i, i + 4);
					i += 4;
					int proteinId = DynByteBuffer.toInt(slice);
					if (proteinId == SEQ_SEPARATOR_INT) {
						break;
					}
				}
				// go to next seq
				continue;
			}

			// create sequence object to return

			slice = Arrays.copyOfRange(data, first, second);
			int offset = DynByteBuffer.toInt(slice);
			slice = Arrays.copyOfRange(data, second, third);
			int length = DynByteBuffer.toInt(slice);

			slice = Arrays.copyOfRange(data, third, fourth);
			int proteinId = DynByteBuffer.toInt(slice);

			// System.out.print("prot id: " + proteinId + " offset: " +
			// offset + " len: " + length);

			String peptideSequence = proteinCache.getPeptideSequence(proteinId, offset, length);
			// System.out.println("pep: " + peptideSequence);

			List<Integer> proteinIds = new ArrayList<Integer>();
			proteinIds.add(proteinId);
			IndexedSequence firstSequence = new IndexedSequence(0, seqMass, peptideSequence, "", "");
			firstSequence.setProteinIds(proteinIds);
			// set residues
			final String protSequence = proteinCache.getProteinSequence(proteinId);
			ResidueInfo residues = Util.getResidues(null, offset, length, protSequence);
			firstSequence.setResidues(residues);

			toInsert.add(firstSequence);

			// loop over more proteins ids until separator
			while (true) {
				slice = Arrays.copyOfRange(data, i, i + 4);
				i += 4;
				proteinId = DynByteBuffer.toInt(slice);

				if (proteinId == SEQ_SEPARATOR_INT) {
					break;
				} else {
					proteinIds.add(proteinId);
				}
			}

		} // end for every byte

	}

	/**
	 * Parse the encoded sequences and return them in toInsert This is version
	 * with multiple mass ranges (slightly slower as we need to check every
	 * range if sequence qualifies)
	 *
	 * @param data
	 *            the data row for all peptides grouped by a single key, i.e. 1
	 *            ppm value
	 * @param toInsert
	 *            parsed sequences that qualify
	 * @param minMasses
	 *            used to eliminate not matching masses in the 1ppm row
	 * @param maxMasses
	 *            used to eliminate not matching masses in the 1ppm row
	 */
	private void parseAddPeptideInfo(byte[] data, List<IndexedSequence> toInsert, double[] minMasses,
			double[] maxMasses) {

		int dataLength = data.length;

		// if (dataLength % 4 != 0) {
		// throw new RuntimeException("Unexpected number of peptide items: "
		// + dataLength);
		// }

		final int numRanges = minMasses.length;

		// go over every sequence in the data row
		for (int i = 0; i < dataLength;) {
			final int zeroeth = i;
			i += 8; // double mass
			final int first = i;
			i += 4;
			final int second = i;
			i += 4;
			final int third = i;
			i += 4;
			final int fourth = i;

			byte[] slice = Arrays.copyOfRange(data, zeroeth, first);
			double seqMass = DynByteBuffer.toDouble(slice);

			// check how the actual sequence mass fits in all mass ranges
			// requested
			// we should bail out if pass all the ranges
			boolean greaterThanMax = true;
			// boolean lesserThanMin = true;
			boolean qualifiesRange = false; // check if qualifies in any
											// range supplied
			for (int range = 0; range < numRanges; ++range) {
				if (seqMass < maxMasses[range]) {
					greaterThanMax = false;
				}

				// if (seqMass > minMasses[range]) {
				// lesserThanMin = false;
				// }

				if (seqMass >= minMasses[range] && seqMass <= maxMasses[range]) {
					qualifiesRange = true;
				}

				// if fits, bail out of this check earlier, otherwise check
				// all ranges
				if (qualifiesRange == true) {
					break;
				}

				// check if makes any range

			}

			// since it's sorted
			// if current mass great than maxmass, break early - optimize
			if (greaterThanMax && !qualifiesRange) {
				break;
			}

			// if (lesserThanMin && !qualifiesRange) {
			if (!qualifiesRange) {
				// skip the sequence, it qualified the row, but not the
				// actual peptide by precise mass

				// skip to next sequence
				while (true) {
					slice = Arrays.copyOfRange(data, i, i + 4);
					i += 4;
					int proteinId = DynByteBuffer.toInt(slice);
					if (proteinId == SEQ_SEPARATOR_INT) {
						break;
					}
				}
				// go to next seq
				continue;
			}

			// create sequence object to return

			slice = Arrays.copyOfRange(data, first, second);
			int offset = DynByteBuffer.toInt(slice);
			slice = Arrays.copyOfRange(data, second, third);
			int length = DynByteBuffer.toInt(slice);

			slice = Arrays.copyOfRange(data, third, fourth);
			int proteinId = DynByteBuffer.toInt(slice);

			// System.out.print("prot id: " + proteinId + " offset: " +
			// offset + " len: " + length);

			String peptideSequence = proteinCache.getPeptideSequence(proteinId, offset, length);
			// System.out.println("pep: " + peptideSequence);

			List<Integer> proteinIds = new ArrayList<Integer>();
			proteinIds.add(proteinId);
			IndexedSequence firstSequence = new IndexedSequence(0, seqMass, peptideSequence, "", "");
			firstSequence.setProteinIds(proteinIds);
			// set residues
			final String protSequence = proteinCache.getProteinSequence(proteinId);
			ResidueInfo residues = Util.getResidues(null, offset, length, protSequence);
			firstSequence.setResidues(residues);

			toInsert.add(firstSequence);

			// loop over more proteins ids until separator
			while (true) {
				slice = Arrays.copyOfRange(data, i, i + 4);
				i += 4;
				proteinId = DynByteBuffer.toInt(slice);

				if (proteinId == SEQ_SEPARATOR_INT) {
					break;
				} else {
					proteinIds.add(proteinId);
				}
			}

		} // end for every byte

	}

	private byte[] getMergedData(byte[] seqData) {
		DynByteBuffer seqDataMerged = new DynByteBuffer();

		// to collapse multiple sequences into single one, with multproteins
		Map<String, List<IndexedSeqInternal>> temp = new HashMap<String, List<IndexedSeqInternal>>();

		int dataLength = seqData.length;

		// if (dataLength % 4 != 0) {
		// throw new RuntimeException("Unexpected number of peptide items: "
		// + dataLength);
		// }

		// modification made by Salvador Martinez on 5/21/2014
		if (dataLength % 4 != 0) {
			System.out.println("CUIDADO " + dataLength);
			// throw new RuntimeException("Unexpected number of peptide items: "
			// + dataLength);
		}
		for (int i = 0; i < dataLength;) {
			// for (int i = 0; i + 16 <= dataLength;) {
			final int zeroeth = i;
			i += 8; // double mass
			final int first = i;
			i += 4;
			final int second = i;
			i += 4;
			final int third = i;
			i += 4;
			final int fourth = i;

			byte[] slice = Arrays.copyOfRange(seqData, zeroeth, first);
			double seqMass = DynByteBuffer.toDouble(slice);
			slice = Arrays.copyOfRange(seqData, first, second);
			int offset = DynByteBuffer.toInt(slice);
			slice = Arrays.copyOfRange(seqData, second, third);
			int length = DynByteBuffer.toInt(slice);
			slice = Arrays.copyOfRange(seqData, third, fourth);
			int proteinId = DynByteBuffer.toInt(slice);

			String peptideSequence = null;
			// System.out.print("prot id: " + proteinId + " offset: " +
			// offset + " len: " + length);

			peptideSequence = proteinCache.getPeptideSequence(proteinId, offset, length);
			// System.out.println("pep: " + peptideSequence);

			final IndexedSeqInternal tempSequence = new IndexedSeqInternal(seqMass, offset, length, proteinId, null);

			List<IndexedSeqInternal> sequences = temp.get(peptideSequence);
			if (sequences == null) {
				sequences = new ArrayList<IndexedSeqInternal>();
				temp.put(peptideSequence, sequences);
			}
			sequences.add(tempSequence);

		}

		// sort sequences by masses for search optimization
		List<IndexedSeqMerged> sortedMerged = new ArrayList<IndexedSeqMerged>();

		// group the same peptides from many proteins into single peptide
		// with protein id list
		// sort them by mass for optimization
		for (String pepSeqKey : temp.keySet()) {
			// for each sequence str

			List<IndexedSeqInternal> sequences = temp.get(pepSeqKey);

			List<Integer> proteinIds = new ArrayList<Integer>();
			for (IndexedSeqInternal tempSeq : sequences) {
				proteinIds.add(tempSeq.proteinId);
			}

			// make sure the 1st protein id is that of the first sequence
			IndexedSeqInternal firstSeq = sequences.get(0);

			IndexedSeqMerged merged = new IndexedSeqMerged(firstSeq.mass, firstSeq.offset, firstSeq.length, proteinIds);

			sortedMerged.add(merged);

		} // end of this sequence

		Collections.sort(sortedMerged);

		// write out grouped merged peptides to byte buf
		for (IndexedSeqMerged merged : sortedMerged) {
			// for each sequence str
			// System.out.println(merged);

			byte[] seqMassB = DynByteBuffer.toByteArray(merged.mass);
			seqDataMerged.add(seqMassB);

			byte[] seqOffsetB = DynByteBuffer.toByteArray(merged.offset);
			seqDataMerged.add(seqOffsetB);

			byte[] seqLengthB = DynByteBuffer.toByteArray(merged.length);
			seqDataMerged.add(seqLengthB);

			for (int proteinId : merged.proteinIds) {
				byte[] proteinIdB = DynByteBuffer.toByteArray(proteinId);
				seqDataMerged.add(proteinIdB);
			}
			// add separator
			seqDataMerged.add(SEQ_SEPARATOR);

		} // end of this sequence

		return seqDataMerged.getData();
	}
}
