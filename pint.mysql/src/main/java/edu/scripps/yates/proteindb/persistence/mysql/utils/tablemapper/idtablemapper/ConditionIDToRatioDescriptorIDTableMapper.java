package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ConditionIDToRatioDescriptorIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ConditionIDToRatioDescriptorIDTableMapper.class);
	private static ConditionIDToRatioDescriptorIDTableMapper instance;

	private ConditionIDToRatioDescriptorIDTableMapper() {
		super();
		log.info("ID mapping table between conditions and ratio descriptors is loaded:");
		log.info(getRatioDescriptorsByConditionsTableMap().size() + " conditions mapped to "
				+ getConditionsByRatioDescriptorsTableMap().size() + " ratio descriptors");
	}

	public synchronized static ConditionIDToRatioDescriptorIDTableMapper getInstance() {
		if (instance == null) {
			instance = new ConditionIDToRatioDescriptorIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}

		return instance;
	}

	public TIntSet getRatioDescriptorIDsFromConditionIDs(Collection<Integer> conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		for (final Integer conditionID : conditionIDs) {
			if (getRatioDescriptorsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getRatioDescriptorsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescriptorIDsFromConditionID(int conditionID) {
		final TIntSet ret = new TIntHashSet();
		if (getRatioDescriptorsByConditionsTableMap().containsKey(conditionID)) {
			ret.addAll(getRatioDescriptorsByConditionsTableMap().get(conditionID));
		}

		return ret;
	}

	public TIntSet getRatioDescriptorIDsFromConditionIDs(TIntCollection conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		final TIntIterator iterator = conditionIDs.iterator();
		while (iterator.hasNext()) {
			final Integer conditionID = iterator.next();
			if (getRatioDescriptorsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getRatioDescriptorsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromRatioDescriptorIDs(Collection<Integer> ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		for (final Integer ratioDescriptorID : ratioDescriptorIDs) {
			if (getConditionsByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getConditionsByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromRatioDescriptorID(int ratioDescriptorID) {
		final TIntSet ret = new TIntHashSet();

		if (getConditionsByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
			ret.addAll(getConditionsByRatioDescriptorsTableMap().get(ratioDescriptorID));
		}

		return ret;
	}

	public TIntSet getConditionIDsFromRatioDescriptorIDs(TIntCollection ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		final TIntIterator iterator = ratioDescriptorIDs.iterator();
		while (iterator.hasNext()) {
			final Integer ratioDescriptorID = iterator.next();
			if (getConditionsByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getConditionsByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		final int[][] ratioDescriptors = PreparedCriteria.getRatioDescriptorsTable();
//		this table has 3 elements per row, the descriptor id, and 2 condition ids
		// so, because the descriptor id is always unique, we create a matrix of size of
		// double the number of rows
		final int[][] ret = new int[ratioDescriptors.length * 2][2];
		int i = 0;
		for (final int[] tripplet : ratioDescriptors) {
			final int ratioDescriptorID = tripplet[0];
			final int condition1ID = tripplet[1];
			final int condition2ID = tripplet[2];
			ret[i][0] = condition1ID;
			ret[i][1] = ratioDescriptorID;
			i++;
			ret[i][0] = condition2ID;
			ret[i][1] = ratioDescriptorID;
			i++;

		}
		return ret;
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getConditionsByRatioDescriptorsTableMap().clear();
		getRatioDescriptorsByConditionsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getConditionsByRatioDescriptorsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getRatioDescriptorsByConditionsTableMap() {
		return super.get_2By1Map();
	}
}
