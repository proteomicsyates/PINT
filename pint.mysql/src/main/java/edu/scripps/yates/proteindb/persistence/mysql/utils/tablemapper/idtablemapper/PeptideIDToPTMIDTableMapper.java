package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideIDToPTMIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(PeptideIDToPTMIDTableMapper.class);
	private static PeptideIDToPTMIDTableMapper instance;
	private static String lock = "";

	private PeptideIDToPTMIDTableMapper() {
		super();
		log.info("ID mapping table between peptides and ptm is loaded:");
		log.info(getPTMsByPeptidesTableMap().size() + " peptides mapped to " + getPeptidesByPTMsTableMap().size()
				+ " ptms");
	}

	public static PeptideIDToPTMIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new PeptideIDToPTMIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getPTMIDsFromPeptideIDs(Collection<Integer> peptideIDs) {
		final TIntSet ret = new TIntHashSet(peptideIDs.size());
		for (final Integer peptideID : peptideIDs) {
			if (getPTMsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getPTMsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getPTMIDsFromPeptideID(int peptideID) {
		final TIntSet ret = new TIntHashSet();
		if (getPTMsByPeptidesTableMap().containsKey(peptideID)) {
			ret.addAll(getPTMsByPeptidesTableMap().get(peptideID));
		}

		return ret;
	}

	public TIntSet getPTMIDsFromPeptideIDs(TIntCollection peptideIDs) {
		final TIntSet ret = new TIntHashSet(peptideIDs.size());
		final TIntIterator iterator = peptideIDs.iterator();
		while (iterator.hasNext()) {
			final Integer peptideID = iterator.next();
			if (getPTMsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getPTMsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromPTMIDs(Collection<Integer> msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		for (final Integer msRunID : msRunIDs) {
			if (getPeptidesByPTMsTableMap().containsKey(msRunID)) {
				ret.addAll(getPeptidesByPTMsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromPTMID(int msRunID) {
		final TIntSet ret = new TIntHashSet();

		if (getPeptidesByPTMsTableMap().containsKey(msRunID)) {
			ret.addAll(getPeptidesByPTMsTableMap().get(msRunID));
		}

		return ret;
	}

	public TIntSet getPeptideIDsFromPTMIDs(TIntCollection msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		final TIntIterator iterator = msRunIDs.iterator();
		while (iterator.hasNext()) {
			final Integer msRunID = iterator.next();
			if (getPeptidesByPTMsTableMap().containsKey(msRunID)) {
				ret.addAll(getPeptidesByPTMsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPeptideToPTMTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getPeptidesByPTMsTableMap().clear();
		getPTMsByPeptidesTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getPeptidesByPTMsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPTMsByPeptidesTableMap() {
		return super.get_2By1Map();
	}
}
