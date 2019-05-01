package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class RatioDescriptorIDToPeptideRatioValueIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(RatioDescriptorIDToPeptideRatioValueIDTableMapper.class);
	private static RatioDescriptorIDToPeptideRatioValueIDTableMapper instance;
	private static String lock = "";

	private RatioDescriptorIDToPeptideRatioValueIDTableMapper() {
		super();
		log.info("ID mapping table between ratio descriptors and peptide ratio valuesis loaded:");
		log.info(getPeptideRatioValuesByRatioDescriptorsTableMap().size() + " ratio descriptors mapped to "
				+ getRatioDescriptorsByPeptideRatioValuesTableMap().size() + " peptide ratio values");
	}

	public static RatioDescriptorIDToPeptideRatioValueIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new RatioDescriptorIDToPeptideRatioValueIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getPeptideRatioValueIDsFromRatioDescriptorIDs(Collection<Integer> ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		for (final Integer ratioDescriptorID : ratioDescriptorIDs) {
			if (getPeptideRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getPeptideRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideRatioValueIDsFromRatioDescriptorID(int ratioDescriptorID) {
		final TIntSet ret = new TIntHashSet();
		if (getPeptideRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
			ret.addAll(getPeptideRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
		}

		return ret;
	}

	public TIntSet getPeptideRatioValueIDsFromRatioDescriptorIDs(TIntCollection ratioDescriptorIDs) {
		final TIntSet ret = new TIntHashSet(ratioDescriptorIDs.size());
		final TIntIterator iterator = ratioDescriptorIDs.iterator();
		while (iterator.hasNext()) {
			final Integer ratioDescriptorID = iterator.next();
			if (getPeptideRatioValuesByRatioDescriptorsTableMap().containsKey(ratioDescriptorID)) {
				ret.addAll(getPeptideRatioValuesByRatioDescriptorsTableMap().get(ratioDescriptorID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromPeptideRatioValueIDs(Collection<Integer> peptideRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(peptideRatioValueIDs.size());
		for (final Integer peptideRatioValueID : peptideRatioValueIDs) {
			if (getRatioDescriptorsByPeptideRatioValuesTableMap().containsKey(peptideRatioValueID)) {
				ret.addAll(getRatioDescriptorsByPeptideRatioValuesTableMap().get(peptideRatioValueID));
			}
		}
		return ret;
	}

	public TIntSet getRatioDescritptorIDsFromPeptideRatioValueID(int peptideRatioValueID) {
		final TIntSet ret = new TIntHashSet();

		if (getRatioDescriptorsByPeptideRatioValuesTableMap().containsKey(peptideRatioValueID)) {
			ret.addAll(getRatioDescriptorsByPeptideRatioValuesTableMap().get(peptideRatioValueID));
		}

		return ret;
	}

	public TIntSet getRatioDescriptorsIDsFromPeptideRatioValueIDs(TIntCollection peptideRatioValueIDs) {
		final TIntSet ret = new TIntHashSet(peptideRatioValueIDs.size());
		final TIntIterator iterator = peptideRatioValueIDs.iterator();
		while (iterator.hasNext()) {
			final Integer peptideRatioValueID = iterator.next();
			if (getRatioDescriptorsByPeptideRatioValuesTableMap().containsKey(peptideRatioValueID)) {
				ret.addAll(getRatioDescriptorsByPeptideRatioValuesTableMap().get(peptideRatioValueID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPeptideRatioValuesToRatioDescriptorTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getRatioDescriptorsByPeptideRatioValuesTableMap().clear();
		getPeptideRatioValuesByRatioDescriptorsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getRatioDescriptorsByPeptideRatioValuesTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPeptideRatioValuesByRatioDescriptorsTableMap() {
		return super.get_2By1Map();
	}
}
