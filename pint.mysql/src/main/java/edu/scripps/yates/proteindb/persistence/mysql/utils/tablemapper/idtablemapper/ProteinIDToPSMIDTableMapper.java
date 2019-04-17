package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToPSMIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToPSMIDTableMapper.class);
	private static ProteinIDToPSMIDTableMapper instance;

	private ProteinIDToPSMIDTableMapper() {
		super();
		log.info("ID mapping table between Proteins and PSMs is loaded:");
		log.info(getPSMsByProteinsTableMap().size() + " proteins mapped to " + getProteinsByPSMsTableMap().size()
				+ " PSMs");
	}

	public synchronized static ProteinIDToPSMIDTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinIDToPSMIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getPSMIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getPSMsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getPSMsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final int proteinID : proteinIds.toArray()) {
			if (getPSMsByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getPSMsByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromProteinID(int proteinID) {
		final TIntSet ret = new TIntHashSet();
		if (getPSMsByProteinsTableMap().containsKey(proteinID)) {
			ret.addAll(getPSMsByProteinsTableMap().get(proteinID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromPSMIDs(Collection<Integer> psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		for (final Integer psmID : psmIds) {
			if (getProteinsByPSMsTableMap().containsKey(psmID)) {
				ret.addAll(getProteinsByPSMsTableMap().get(psmID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromPSMID(int psmID) {
		final TIntSet ret = new TIntHashSet();
		if (getProteinsByPSMsTableMap().containsKey(psmID)) {
			ret.addAll(getProteinsByPSMsTableMap().get(psmID));
		}

		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToPSMTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByPSMsTableMap().clear();
		getPSMsByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByPSMsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPSMsByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
