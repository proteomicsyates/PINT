package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideIDToConditionIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(PeptideIDToConditionIDTableMapper.class);
	private static PeptideIDToConditionIDTableMapper instance;

	private PeptideIDToConditionIDTableMapper() {
		super();
		log.info("ID mapping table between pepties and conditions is loaded:");
		log.info(getConditionsByPeptidesTableMap().size() + " peptides mapped to "
				+ getPeptidesByConditionsTableMap().size() + " conditions");
	}

	public synchronized static PeptideIDToConditionIDTableMapper getInstance() {
		if (instance == null) {
			instance = new PeptideIDToConditionIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getConditionIDsFromPeptideIDs(Collection<Integer> peptideIds) {
		final TIntSet ret = new TIntHashSet(peptideIds.size());
		for (final Integer peptideID : peptideIds) {
			if (getConditionsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getConditionsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getConditionIDsFromPeptideID(int peptideID) {
		final TIntSet ret = new TIntHashSet();
		if (getConditionsByPeptidesTableMap().containsKey(peptideID)) {
			ret.addAll(getConditionsByPeptidesTableMap().get(peptideID));
		}

		return ret;
	}

	public TIntSet getConditionIDsFromPeptideIDs(TIntCollection peptideIds) {
		final TIntSet ret = new TIntHashSet(peptideIds.size());
		final TIntIterator iterator = peptideIds.iterator();
		while (iterator.hasNext()) {
			final Integer peptideID = iterator.next();
			if (getConditionsByPeptidesTableMap().containsKey(peptideID)) {
				ret.addAll(getConditionsByPeptidesTableMap().get(peptideID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromConditionIDs(Collection<Integer> conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		for (final Integer conditionID : conditionIDs) {
			if (getPeptidesByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getPeptidesByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromConditionID(int conditionID) {
		final TIntSet ret = new TIntHashSet();

		if (getPeptidesByConditionsTableMap().containsKey(conditionID)) {
			ret.addAll(getPeptidesByConditionsTableMap().get(conditionID));
		}

		return ret;
	}

	public TIntSet getPeptideIDsFromConditionIDs(TIntCollection conditionIDs) {
		final TIntSet ret = new TIntHashSet(conditionIDs.size());
		final TIntIterator iterator = conditionIDs.iterator();
		while (iterator.hasNext()) {
			final Integer conditionID = iterator.next();
			if (getPeptidesByConditionsTableMap().containsKey(conditionID)) {
				ret.addAll(getPeptidesByConditionsTableMap().get(conditionID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPeptideToConditionTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getPeptidesByConditionsTableMap().clear();
		getConditionsByPeptidesTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getPeptidesByConditionsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getConditionsByPeptidesTableMap() {
		return super.get_2By1Map();
	}
}
