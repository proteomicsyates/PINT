package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToProteinThresholdIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToProteinThresholdIDTableMapper.class);
	private static ProteinIDToProteinThresholdIDTableMapper instance;

	private ProteinIDToProteinThresholdIDTableMapper() {
		super();
		log.info("ID mapping table between proteins and protein thresholds loaded:");
		log.info(getProteinThresholdsByProteinsTableMap().size() + " proteins mapped to "
				+ getProteinsByProteinThresholdsTableMap().size() + " Protein Thresholds");
	}

	public synchronized static ProteinIDToProteinThresholdIDTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinIDToProteinThresholdIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getProteinThresholdIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getProteinThresholdsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getProteinThresholdsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinThresholdIDsFromProteinID(int proteinID) {
		if (getProteinThresholdsByProteinsTableMap().containsKey(proteinID)) {
			final TIntSet ret = new TIntHashSet();

			ret.addAll(getProteinThresholdsByProteinsTableMap().get(proteinID));
			return ret;
		}
		return null;

	}

	public TIntSet getProteinThresholdIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		final TIntIterator iterator = proteinIds.iterator();
		while (iterator.hasNext()) {
			final Integer proteinID = iterator.next();
			if (getProteinThresholdsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getProteinThresholdsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromProteinThresholdIDs(Collection<Integer> proteinThresholdIDs) {
		final TIntSet ret = new TIntHashSet(proteinThresholdIDs.size());
		for (final Integer proteinThresholdID : proteinThresholdIDs) {
			if (getProteinsByProteinThresholdsTableMap().containsKey(proteinThresholdID)) {
				ret.addAll(getProteinsByProteinThresholdsTableMap().get(proteinThresholdID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromProteinThresholdID(int proteinThresholdID) {
		final TIntSet ret = new TIntHashSet();

		if (getProteinsByProteinThresholdsTableMap().containsKey(proteinThresholdID)) {
			ret.addAll(getProteinsByProteinThresholdsTableMap().get(proteinThresholdID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromProteinThresholdIDs(TIntCollection proteinThresholdIDs) {
		final TIntSet ret = new TIntHashSet(proteinThresholdIDs.size());
		final TIntIterator iterator = proteinThresholdIDs.iterator();
		while (iterator.hasNext()) {
			final Integer proteinThresholdID = iterator.next();
			if (getProteinsByProteinThresholdsTableMap().containsKey(proteinThresholdID)) {
				ret.addAll(getProteinsByProteinThresholdsTableMap().get(proteinThresholdID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToProteinThresholdTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByProteinThresholdsTableMap().clear();
		getProteinThresholdsByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByProteinThresholdsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getProteinThresholdsByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
