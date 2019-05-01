package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideIDToMSRunIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(PeptideIDToMSRunIDTableMapper.class);
	private static PeptideIDToMSRunIDTableMapper instance;
	private static String lock = "";

	private PeptideIDToMSRunIDTableMapper() {
		super();
		log.info("ID mapping table between peptides and MS runs is loaded:");
		log.info(getMSRunsByPeptidesTableMap().size() + " peptides mapped to " + getPeptidesByMSRunsTableMap().size()
				+ " MS Runs");
	}

	public static PeptideIDToMSRunIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new PeptideIDToMSRunIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getMSRunIDsFromPeptideIDs(Collection<Integer> peptideIDs) {
		final TIntSet ret = new TIntHashSet(peptideIDs.size());
		for (final Integer peptideID : peptideIDs) {
			if (getMSRunsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getMSRunsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getMSRunIDsFromPeptideID(int peptideID) {
		final TIntSet ret = new TIntHashSet();
		if (getMSRunsByPeptidesTableMap().containsKey(peptideID)) {
			ret.addAll(getMSRunsByPeptidesTableMap().get(peptideID));
		}

		return ret;
	}

	public TIntSet getMSRunIDsFromPeptideIDs(TIntCollection peptideIDs) {
		final TIntSet ret = new TIntHashSet(peptideIDs.size());
		final TIntIterator iterator = peptideIDs.iterator();
		while (iterator.hasNext()) {
			final Integer peptideID = iterator.next();
			if (getMSRunsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getMSRunsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromMSRunIDs(Collection<Integer> msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		for (final Integer msRunID : msRunIDs) {
			if (getPeptidesByMSRunsTableMap().containsKey(msRunID)) {
				ret.addAll(getPeptidesByMSRunsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromMSRunID(int msRunID) {
		final TIntSet ret = new TIntHashSet();

		if (getPeptidesByMSRunsTableMap().containsKey(msRunID)) {
			ret.addAll(getPeptidesByMSRunsTableMap().get(msRunID));
		}

		return ret;
	}

	public TIntSet getPeptideIDsFromMSRunIDs(TIntCollection msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		final TIntIterator iterator = msRunIDs.iterator();
		while (iterator.hasNext()) {
			final Integer msRunID = iterator.next();
			if (getPeptidesByMSRunsTableMap().containsKey(msRunID)) {
				ret.addAll(getPeptidesByMSRunsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPeptideToMSRunTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getPeptidesByMSRunsTableMap().clear();
		getMSRunsByPeptidesTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getPeptidesByMSRunsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getMSRunsByPeptidesTableMap() {
		return super.get_2By1Map();
	}
}
