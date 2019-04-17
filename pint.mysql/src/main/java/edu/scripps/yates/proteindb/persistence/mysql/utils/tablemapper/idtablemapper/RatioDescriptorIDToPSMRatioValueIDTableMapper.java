package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class RatioDescriptorIDToPSMRatioValueIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(RatioDescriptorIDToPSMRatioValueIDTableMapper.class);
	private static RatioDescriptorIDToPSMRatioValueIDTableMapper instance;

	private RatioDescriptorIDToPSMRatioValueIDTableMapper() {
		super();
		log.info("ID mapping table between ratio descriptors and psm ratio valuesis loaded:");
		log.info(getPSMRatioValuesByRatioDescriptorsTableMap().size() + " ratio descriptors mapped to "
				+ getRatioDescriptorsByPSMRatioValuesTableMap().size() + " psm ratio values");
	}

	public synchronized static RatioDescriptorIDToPSMRatioValueIDTableMapper getInstance() {
		if (instance == null) {
			instance = new RatioDescriptorIDToPSMRatioValueIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getPSMRatioValueIDsFromRatioDescriptorIDs(Collection<Integer> ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		for (final Integer ratioDescriptorID : ratioDescriptorIDs) {
			if (getPSMRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getPSMRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getPSMRatioValueIDsFromRatioDescriptorID(int ratioDescriptorID) {
		final TIntSet ret = new TIntHashSet();
		if (getPSMRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
			ret.addAll(getPSMRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
		}

		return ret;
	}

	public TIntSet getPSMRatioValueIDsFromRatioDescriptorIDs(TIntCollection ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		final TIntIterator iterator = ratioDescriptorIDs.iterator();
		while (iterator.hasNext()) {
			final Integer ratioDescriptorID = iterator.next();
			if (getPSMRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getPSMRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromPSMRatioValueIDs(Collection<Integer> psmRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(psmRatioValueIDs.size());
		for (final Integer psmRatioValueID : psmRatioValueIDs) {
			if (getRatioDescriptorsByPSMRatioValuesTableMap().containsKey(psmRatioValueID)) {
				ret.addAll(getRatioDescriptorsByPSMRatioValuesTableMap().get(psmRatioValueID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromPSMRatioValueID(int psmRatioValueID) {
		final TIntSet ret = new TIntHashSet();

		if (getRatioDescriptorsByPSMRatioValuesTableMap().containsKey(psmRatioValueID)) {
			ret.addAll(getRatioDescriptorsByPSMRatioValuesTableMap().get(psmRatioValueID));
		}

		return ret;
	}

	public TIntSet getRatioDescriptorsIDsFromPSMRatioValueIDs(TIntCollection psmRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(psmRatioValueIDs.size());
		final TIntIterator iterator = psmRatioValueIDs.iterator();
		while (iterator.hasNext()) {
			final Integer psmRatioValueID = iterator.next();
			if (getRatioDescriptorsByPSMRatioValuesTableMap().containsKey(psmRatioValueID)) {
				ret.addAll(getRatioDescriptorsByPSMRatioValuesTableMap().get(psmRatioValueID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPsmRatioValuesToRatioDescriptorTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getRatioDescriptorsByPSMRatioValuesTableMap().clear();
		getPSMRatioValuesByRatioDescriptorsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getRatioDescriptorsByPSMRatioValuesTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPSMRatioValuesByRatioDescriptorsTableMap() {
		return super.get_2By1Map();
	}
}
