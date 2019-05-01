package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToConditionIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToConditionIDTableMapper.class);
	private static ProteinIDToConditionIDTableMapper instance;
	private static String lock = "";

	private ProteinIDToConditionIDTableMapper() {
		super();
		log.info("ID mapping table between proteins and conditions is loaded:");
		log.info(getConditionsByProteinsTableMap().size() + " proteins mapped to "
				+ getProteinsByConditionsTableMap().size() + " conditions");
	}

	public static ProteinIDToConditionIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new ProteinIDToConditionIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getConditionIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getConditionsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getConditionsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromProteinID(int proteinID) {
		final TIntSet ret = new TIntHashSet();
		if (getConditionsByProteinsTableMap().containsKey(proteinID)) {
			ret.addAll(getConditionsByProteinsTableMap().get(proteinID));
		}

		return ret;
	}

	public TIntSet getConditionIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		final TIntIterator iterator = proteinIds.iterator();
		while (iterator.hasNext()) {
			final Integer proteinID = iterator.next();
			if (getConditionsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getConditionsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromConditionIDs(Collection<Integer> conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		for (final Integer conditionID : conditionIDs) {
			if (getProteinsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getProteinsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromConditionID(int conditionID) {
		final TIntSet ret = new TIntHashSet();

		if (getProteinsByConditionsTableMap().containsKey(conditionID)) {
			ret.addAll(getProteinsByConditionsTableMap().get(conditionID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromConditionIDs(TIntCollection conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		final TIntIterator iterator = conditionIDs.iterator();
		while (iterator.hasNext()) {
			final Integer conditionID = iterator.next();
			if (getProteinsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getProteinsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToConditionTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByConditionsTableMap().clear();
		getConditionsByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByConditionsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getConditionsByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
