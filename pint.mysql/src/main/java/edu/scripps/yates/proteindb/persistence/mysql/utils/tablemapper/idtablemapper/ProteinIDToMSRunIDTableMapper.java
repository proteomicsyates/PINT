package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToMSRunIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToMSRunIDTableMapper.class);
	private static ProteinIDToMSRunIDTableMapper instance;
	private static String lock = "";

	private ProteinIDToMSRunIDTableMapper() {
		super();
		log.info("ID mapping table between proteins and MS runs is loaded:");
		log.info(getMSRunsByProteinsTableMap().size() + " proteins mapped to " + getProteinsByMSRunsTableMap().size()
				+ " MS Runs");
	}

	public static ProteinIDToMSRunIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new ProteinIDToMSRunIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getMSRunIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getMSRunsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getMSRunsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getMSRunIDsFromProteinID(int proteinID) {
		final TIntSet ret = new TIntHashSet();
		if (getMSRunsByProteinsTableMap().containsKey(proteinID)) {
			ret.addAll(getMSRunsByProteinsTableMap().get(proteinID));
		}

		return ret;
	}

	public TIntSet getMSRunIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		final TIntIterator iterator = proteinIds.iterator();
		while (iterator.hasNext()) {
			final Integer proteinID = iterator.next();
			if (getMSRunsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getMSRunsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromMSRunIDs(Collection<Integer> msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		for (final Integer msRunID : msRunIDs) {
			if (getProteinsByMSRunsTableMap().containsKey(msRunID)) {
				ret.addAll(getProteinsByMSRunsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromMSRunID(int msRunID) {
		final TIntSet ret = new TIntHashSet();

		if (getProteinsByMSRunsTableMap().containsKey(msRunID)) {
			ret.addAll(getProteinsByMSRunsTableMap().get(msRunID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromMSRunIDs(TIntCollection msRunIDs) {
		final TIntSet ret = new TIntHashSet(msRunIDs.size());
		final TIntIterator iterator = msRunIDs.iterator();
		while (iterator.hasNext()) {
			final Integer msRunID = iterator.next();
			if (getProteinsByMSRunsTableMap().containsKey(msRunID)) {
				ret.addAll(getProteinsByMSRunsTableMap().get(msRunID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToMSRunTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByMSRunsTableMap().clear();
		getMSRunsByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByMSRunsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getMSRunsByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
