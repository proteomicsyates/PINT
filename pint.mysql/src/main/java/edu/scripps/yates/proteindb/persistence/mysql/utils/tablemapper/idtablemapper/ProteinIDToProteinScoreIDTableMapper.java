package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToProteinScoreIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToProteinScoreIDTableMapper.class);
	private static ProteinIDToProteinScoreIDTableMapper instance;
	private static String lock = "";

	private ProteinIDToProteinScoreIDTableMapper() {
		super();
		log.info("ID mapping table between proteins and protein proteinScores loaded:");
		log.info(getProteinScoresByProteinsTableMap().size() + " proteins mapped to "
				+ getProteinsByProteinScoresTableMap().size() + " Protein scores");
	}

	public static ProteinIDToProteinScoreIDTableMapper getInstance() {
		synchronized (lock) {
			if (instance == null) {
				instance = new ProteinIDToProteinScoreIDTableMapper();
			}
			if (instance.get_1By2Map().isEmpty()) {
				instance.processDataFromDB(instance.getMapTableFromDB());
			}
			return instance;
		}
	}

	public TIntSet getProteinScoreIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getProteinScoresByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getProteinScoresByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinScoreIDsFromProteinID(int proteinID) {
		if (getProteinScoresByProteinsTableMap().containsKey(proteinID)) {
			final TIntSet ret = new TIntHashSet();

			ret.addAll(getProteinScoresByProteinsTableMap().get(proteinID));
			return ret;
		}
		return null;

	}

	public TIntSet getProteinScoreIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		final TIntIterator iterator = proteinIds.iterator();
		while (iterator.hasNext()) {
			final Integer proteinID = iterator.next();
			if (getProteinScoresByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getProteinScoresByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromProteinScoreIDs(Collection<Integer> proteinScoreIDs) {
		final TIntSet ret = new TIntHashSet(proteinScoreIDs.size());
		for (final Integer proteinScoreID : proteinScoreIDs) {
			if (getProteinsByProteinScoresTableMap().containsKey(proteinScoreID)) {
				ret.addAll(getProteinsByProteinScoresTableMap().get(proteinScoreID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromProteinScoreID(int proteinScoreID) {
		final TIntSet ret = new TIntHashSet();

		if (getProteinsByProteinScoresTableMap().containsKey(proteinScoreID)) {
			ret.addAll(getProteinsByProteinScoresTableMap().get(proteinScoreID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromProteinScoreIDs(TIntCollection proteinScoreIDs) {
		final TIntSet ret = new TIntHashSet(proteinScoreIDs.size());
		final TIntIterator iterator = proteinScoreIDs.iterator();
		while (iterator.hasNext()) {
			final Integer proteinScoreID = iterator.next();
			if (getProteinsByProteinScoresTableMap().containsKey(proteinScoreID)) {
				ret.addAll(getProteinsByProteinScoresTableMap().get(proteinScoreID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToProteinScoreTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByProteinScoresTableMap().clear();
		getProteinScoresByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByProteinScoresTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getProteinScoresByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
