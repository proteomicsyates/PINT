package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ConditionIDToProteinAmountIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ConditionIDToProteinAmountIDTableMapper.class);
	private static ConditionIDToProteinAmountIDTableMapper instance;

	private ConditionIDToProteinAmountIDTableMapper() {
		super();
		log.info("ID mapping table between conditions and protein amounts is loaded:");
		log.info(getProteinAmountsByConditionsTableMap().size() + " conditions mapped to "
				+ getConditionsByProteinAmountsTableMap().size() + " protein amounts");
	}

	public synchronized static ConditionIDToProteinAmountIDTableMapper getInstance() {
		if (instance == null) {
			instance = new ConditionIDToProteinAmountIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getProteinAmountIDsFromConditionIDs(Collection<Integer> conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		for (final Integer conditionID : conditionIDs) {
			if (getProteinAmountsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getProteinAmountsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getProteinAmountIDsFromConditionID(int conditionID) {
		final TIntSet ret = new TIntHashSet();
		if (getProteinAmountsByConditionsTableMap().containsKey(conditionID)) {
			ret.addAll(getProteinAmountsByConditionsTableMap().get(conditionID));
		}

		return ret;
	}

	public TIntSet getProteinAmountIDsFromConditionIDs(TIntCollection conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		final TIntIterator iterator = conditionIDs.iterator();
		while (iterator.hasNext()) {
			final Integer conditionID = iterator.next();
			if (getProteinAmountsByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getProteinAmountsByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromProteinAmountIDs(Collection<Integer> proteinAmountIDs) {
		final TIntSet ret = new TIntHashSet(proteinAmountIDs.size());
		for (final Integer proteinAmountID : proteinAmountIDs) {
			if (getConditionsByProteinAmountsTableMap().containsKey(proteinAmountID)) {
				ret.addAll(getConditionsByProteinAmountsTableMap().get(proteinAmountID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromProteinAmountID(int proteinAmountID) {
		final TIntSet ret = new TIntHashSet();

		if (getConditionsByProteinAmountsTableMap().containsKey(proteinAmountID)) {
			ret.addAll(getConditionsByProteinAmountsTableMap().get(proteinAmountID));
		}

		return ret;
	}

	public TIntSet getConditionIDsFromProteinAmountIDs(TIntCollection proteinAmountIDs) {
		final TIntSet ret = new TIntHashSet(proteinAmountIDs.size());
		final TIntIterator iterator = proteinAmountIDs.iterator();
		while (iterator.hasNext()) {
			final Integer proteinAmountID = iterator.next();
			if (getConditionsByProteinAmountsTableMap().containsKey(proteinAmountID)) {
				ret.addAll(getConditionsByProteinAmountsTableMap().get(proteinAmountID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinAmountsTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getConditionsByProteinAmountsTableMap().clear();
		getProteinAmountsByConditionsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getConditionsByProteinAmountsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getProteinAmountsByConditionsTableMap() {
		return super.get_2By1Map();
	}
}
