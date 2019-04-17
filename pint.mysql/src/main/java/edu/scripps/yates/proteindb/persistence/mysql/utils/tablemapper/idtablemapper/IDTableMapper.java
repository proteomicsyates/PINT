package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.memory.MemoryUsageReport;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public abstract class IDTableMapper {
	private final TIntObjectHashMap<TIntSet> _2By1Map = new TIntObjectHashMap<TIntSet>();
	private final TIntObjectHashMap<TIntSet> _1By2Map = new TIntObjectHashMap<TIntSet>();
	private final static Logger log = Logger.getLogger(IDTableMapper.class);

	public IDTableMapper() {
		// register it in the TableMapperRegistry, so that we can clear all of them at
		// once when adding or removing stuff to the database
		IDTableMapperRegistry.register(this);

		processDataFromDB(getMapTableFromDB());

	}

	protected void processDataFromDB(int[][] proteinToPeptideTable) {
		log.info("Loading ID table map from DB to memory");
		log.info(MemoryUsageReport.getMemoryUsageReport());
		for (final int[] ids : proteinToPeptideTable) {
			final int _1ID = ids[0];
			final int _2ID = ids[1];
			//
			if (_1By2Map.contains(_2ID)) {
				_1By2Map.get(_2ID).add(_1ID);
			} else {
				final TIntHashSet set = new TIntHashSet();
				set.add(_1ID);
				_1By2Map.put(_2ID, set);
			}
			//
			if (_2By1Map.contains(_1ID)) {
				_2By1Map.get(_1ID).add(_2ID);
			} else {
				final TIntHashSet set = new TIntHashSet();
				set.add(_2ID);
				_2By1Map.put(_1ID, set);
			}
		}
		log.info(MemoryUsageReport.getMemoryUsageReport());
		System.gc();
		log.info(MemoryUsageReport.getMemoryUsageReport());
	}

	protected abstract void clear();

	/**
	 * An array of pairs of identifiers between the two types of objects, that is a
	 * matrix of size nx2
	 * 
	 * @return
	 */
	protected abstract int[][] getMapTableFromDB();

	/**
	 * map to get sets of 2 by indexes of 1
	 * 
	 * @return
	 */
	protected TIntObjectHashMap<TIntSet> get_2By1Map() {
		return _2By1Map;
	}

	/**
	 * Map to get sets of 1 by indexes of 2
	 * 
	 * @return
	 */
	protected TIntObjectHashMap<TIntSet> get_1By2Map() {
		return _1By2Map;
	}
}
