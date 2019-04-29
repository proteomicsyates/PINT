package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideIDToPSMIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(PeptideIDToPSMIDTableMapper.class);
	private static PeptideIDToPSMIDTableMapper instance;

	private PeptideIDToPSMIDTableMapper() {
		super();
		log.info("ID mapping table between Peptides and PSMs is loaded:");
		log.info(getPSMsByPeptidesTableMap().size() + " peptides mapped to " + getPeptidesByPSMsTableMap().size()
				+ " PSMs");
	}

	public synchronized static PeptideIDToPSMIDTableMapper getInstance() {
		if (instance == null) {
			instance = new PeptideIDToPSMIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getPSMIDsFromPeptideIDs(Collection<Integer> PeptideIds) {
		final TIntSet ret = new TIntHashSet(PeptideIds.size());
		for (final Integer PeptideID : PeptideIds) {
			if (getPSMsByPeptidesTableMap().containsKey(PeptideID)) {
				ret.addAll(getPSMsByPeptidesTableMap().get(PeptideID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromPeptideIDs(TIntCollection PeptideIds) {
		final TIntSet ret = new TIntHashSet(PeptideIds.size());
		for (final int PeptideID : PeptideIds.toArray()) {
			if (getPSMsByPeptidesTableMap().containsKey(PeptideID)) {
				ret.addAll(getPSMsByPeptidesTableMap().get(PeptideID));
			}
		}
		return ret;
	}

	public TIntSet getPSMIDsFromPeptideID(int PeptideID) {
		final TIntSet ret = new TIntHashSet();
		if (getPSMsByPeptidesTableMap().containsKey(PeptideID)) {
			ret.addAll(getPSMsByPeptidesTableMap().get(PeptideID));
		}

		return ret;
	}

	public TIntSet getPeptideIDsFromPSMIDs(Collection<Integer> psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		for (final Integer psmID : psmIds) {
			if (getPeptidesByPSMsTableMap().containsKey(psmID)) {
				ret.addAll(getPeptidesByPSMsTableMap().get(psmID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromPSMID(int psmID) {
		final TIntSet ret = new TIntHashSet();
		if (getPeptidesByPSMsTableMap().containsKey(psmID)) {
			ret.addAll(getPeptidesByPSMsTableMap().get(psmID));
		}

		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getPeptideToPSMTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getPeptidesByPSMsTableMap().clear();
		getPSMsByPeptidesTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getPeptidesByPSMsTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPSMsByPeptidesTableMap() {
		return super.get_2By1Map();
	}
}
