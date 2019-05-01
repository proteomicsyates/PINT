package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class PSMIDToConditionIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(PSMIDToConditionIDTableMapper.class);
	private static PSMIDToConditionIDTableMapper instance;
	private static String lock = "";

	private PSMIDToConditionIDTableMapper() {
		super();
		log.info("ID mapping table between psms and conditions is loaded:");
		log.info(getConditionsByPSMsTableMap().size() + " psms mapped to " + getPSMsByConditionsTableMap().size()
				+ " conditions");
	}

	public static PSMIDToConditionIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new PSMIDToConditionIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getConditionIDsFromPSMIDs(Collection<Integer> psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		for (final Integer psmID : psmIds) {
			if (getConditionsByPSMsTableMap().containsKey(psmID)) {
				ret.addAll(getConditionsByPSMsTableMap().get(psmID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromPSMID(int psmID) {
		final TIntSet ret = new TIntHashSet();
		if (getConditionsByPSMsTableMap().containsKey(psmID)) {
			ret.addAll(getConditionsByPSMsTableMap().get(psmID));
		}

		return ret;
	}

	public TIntSet getConditionIDsFromPSMIDs(TIntCollection psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		final TIntIterator iterator = psmIds.iterator();
		while (iterator.hasNext()) {
			final Integer psmID = iterator.next();
			if (getConditionsByPSMsTableMap().containsKey(psmID)) {
				ret.addAll(getConditionsByPSMsTableMap().get(psmID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromConditionIDs(Collection<Integer> conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		for (final Integer conditionID : conditionIDs) {
			if (getPSMsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getPSMsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromConditionID(int conditionID) {
		final TIntSet ret = new TIntHashSet();

		if (getPSMsByConditionsTableMap().containsKey(conditionID)) {
			ret.addAll(getPSMsByConditionsTableMap().get(conditionID));
		}

		return ret;
	}

	public TIntSet getPSMIDsFromConditionIDs(TIntCollection conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		final TIntIterator iterator = conditionIDs.iterator();
		while (iterator.hasNext()) {
			final Integer conditionID = iterator.next();
			if (getPSMsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getPSMsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPSMToConditionTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getPSMsByConditionsTableMap().clear();
		getConditionsByPSMsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getPSMsByConditionsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getConditionsByPSMsTableMap() {
		return super.get_2By1Map();
	}
}
