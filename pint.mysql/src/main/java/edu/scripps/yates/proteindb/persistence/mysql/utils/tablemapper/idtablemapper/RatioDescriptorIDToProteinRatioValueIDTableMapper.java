package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class RatioDescriptorIDToProteinRatioValueIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(RatioDescriptorIDToProteinRatioValueIDTableMapper.class);
	private static RatioDescriptorIDToProteinRatioValueIDTableMapper instance;
	private static String lock = "";

	private RatioDescriptorIDToProteinRatioValueIDTableMapper() {
		super();
		log.info("ID mapping table between ratio descriptors and protein ratio valuesis loaded:");
		log.info(getProteinRatioValuesByRatioDescriptorsTableMap().size() + " ratio descriptors mapped to "
				+ getRatioDescriptorsByProteinRatioValuesTableMap().size() + " protein ratio values");
	}

	public static RatioDescriptorIDToProteinRatioValueIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new RatioDescriptorIDToProteinRatioValueIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getProteinRatioValueIDsFromRatioDescriptorIDs(Collection<Integer> ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		for (final Integer ratioDescriptorID : ratioDescriptorIDs) {
			if (getProteinRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getProteinRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getProteinRatioValueIDsFromRatioDescriptorID(int ratioDescriptorID) {
		final TIntSet ret = new TIntHashSet();
		if (getProteinRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
			ret.addAll(getProteinRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
		}

		return ret;
	}

	public TIntSet getProteinRatioValueIDsFromRatioDescriptorIDs(TIntCollection ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		final TIntIterator iterator = ratioDescriptorIDs.iterator();
		while (iterator.hasNext()) {
			final Integer ratioDescriptorID = iterator.next();
			if (getProteinRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getProteinRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromProteinRatioValueIDs(Collection<Integer> proteinRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(proteinRatioValueIDs.size());
		for (final Integer proteinRatioValueID : proteinRatioValueIDs) {
			if (getRatioDescriptorsByProteinRatioValuesTableMap().containsKey(proteinRatioValueID)) {
				ret.addAll(getRatioDescriptorsByProteinRatioValuesTableMap().get(proteinRatioValueID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromProteinRatioValueID(int proteinRatioValueID) {
		final TIntSet ret = new TIntHashSet();

		if (getRatioDescriptorsByProteinRatioValuesTableMap().containsKey(proteinRatioValueID)) {
			ret.addAll(getRatioDescriptorsByProteinRatioValuesTableMap().get(proteinRatioValueID));
		}

		return ret;
	}

	public TIntSet getRatioDescriptorsIDsFromProteinRatioValueIDs(TIntCollection proteinRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(proteinRatioValueIDs.size());
		final TIntIterator iterator = proteinRatioValueIDs.iterator();
		while (iterator.hasNext()) {
			final Integer proteinRatioValueID = iterator.next();
			if (getRatioDescriptorsByProteinRatioValuesTableMap().containsKey(proteinRatioValueID)) {
				ret.addAll(getRatioDescriptorsByProteinRatioValuesTableMap().get(proteinRatioValueID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinRatioValuesToRatioDescriptorTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getRatioDescriptorsByProteinRatioValuesTableMap().clear();
		getProteinRatioValuesByRatioDescriptorsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getRatioDescriptorsByProteinRatioValuesTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getProteinRatioValuesByRatioDescriptorsTableMap() {
		return super.get_2By1Map();
	}
}
