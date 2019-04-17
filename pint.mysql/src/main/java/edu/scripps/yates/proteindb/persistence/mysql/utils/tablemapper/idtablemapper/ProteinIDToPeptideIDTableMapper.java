package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper;

import java.util.Collection;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinIDToPeptideIDTableMapper extends IDTableMapper {
	private final static Logger log = Logger.getLogger(ProteinIDToPeptideIDTableMapper.class);
	private static ProteinIDToPeptideIDTableMapper instance;

	private ProteinIDToPeptideIDTableMapper() {
		super();
		log.info("ID mapping table between proteins and peptides is loaded:");
		log.info(getPeptidesByProteinsTableMap().size() + " proteins mapped to "
				+ getProteinsByPeptidesTableMap().size() + " peptides");
	}

	public synchronized static ProteinIDToPeptideIDTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinIDToPeptideIDTableMapper();
		}
		if (instance.get_1By2Map().isEmpty()) {
			instance.processDataFromDB(instance.getMapTableFromDB());
		}
		return instance;
	}

	public TIntSet getPeptideIDsFromProteinIDs(Collection<Integer> proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		for (final Integer proteinID : proteinIds) {
			if (getPeptidesByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getPeptidesByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getPeptideIDsFromProteinID(int proteinID) {
		final TIntSet ret = new TIntHashSet();
		if (getPeptidesByProteinsTableMap().containsKey(proteinID)) {
			ret.addAll(getPeptidesByProteinsTableMap().get(proteinID));
		}

		return ret;
	}

	public TIntSet getPeptideIDsFromProteinIDs(TIntCollection proteinIds) {
		final TIntSet ret = new TIntHashSet(proteinIds.size());
		final TIntIterator iterator = proteinIds.iterator();
		while (iterator.hasNext()) {
			final Integer proteinID = iterator.next();
			if (getPeptidesByProteinsTableMap().containsKey(proteinID)) {
				ret.addAll(getPeptidesByProteinsTableMap().get(proteinID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromPeptideIDs(Collection<Integer> psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		for (final Integer psmID : psmIds) {
			if (getProteinsByPeptidesTableMap().containsKey(psmID)) {
				ret.addAll(getProteinsByPeptidesTableMap().get(psmID));
			}
		}
		return ret;
	}

	public TIntSet getProteinIDsFromPeptideID(int psmID) {
		final TIntSet ret = new TIntHashSet();

		if (getProteinsByPeptidesTableMap().containsKey(psmID)) {
			ret.addAll(getProteinsByPeptidesTableMap().get(psmID));
		}

		return ret;
	}

	public TIntSet getProteinIDsFromPeptideIDs(TIntCollection psmIds) {
		final TIntSet ret = new TIntHashSet(psmIds.size());
		final TIntIterator iterator = psmIds.iterator();
		while (iterator.hasNext()) {
			final Integer psmID = iterator.next();
			if (getProteinsByPeptidesTableMap().containsKey(psmID)) {
				ret.addAll(getProteinsByPeptidesTableMap().get(psmID));
			}
		}
		return ret;
	}

	@Override
	protected int[][] getMapTableFromDB() {
		return PreparedCriteria.getProteinToPeptideTable();
	}

	/**
	 * Removes mapping tables and instance, so that next call to getInstance() will
	 * query the database again to populate maps
	 */
	@Override
	public void clear() {
		getProteinsByPeptidesTableMap().clear();
		getPeptidesByProteinsTableMap().clear();
		instance = null;
	}

	private TIntObjectHashMap<TIntSet> getProteinsByPeptidesTableMap() {
		return super.get_1By2Map();
	}

	private TIntObjectHashMap<TIntSet> getPeptidesByProteinsTableMap() {
		return super.get_2By1Map();
	}
}
