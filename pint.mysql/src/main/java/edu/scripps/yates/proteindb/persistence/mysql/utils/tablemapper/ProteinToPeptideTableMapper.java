package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class ProteinToPeptideTableMapper {
	private final static Logger log = Logger.getLogger(ProteinToPeptideTableMapper.class);
	private static ProteinToPeptideTableMapper instance;
	private final TIntObjectHashMap<Peptide> peptidesById = new TIntObjectHashMap<Peptide>();
	private final TIntObjectHashMap<Protein> proteinsById = new TIntObjectHashMap<Protein>();

	private ProteinToPeptideTableMapper() {

	}

	public static ProteinToPeptideTableMapper getInstance() {
		if (instance == null) {
			instance = new ProteinToPeptideTableMapper();
		}
		return instance;
	}

	public void clear() {
		peptidesById.clear();
		proteinsById.clear();
	}

	public void addProteins(THashSet<Protein> proteins) {
		final TIntSet proteinIDs = new TIntHashSet(proteins.size());
		for (final Protein protein : proteins) {
			proteinsById.put(protein.getId(), protein);
			proteinIDs.add(protein.getId());
		}
		log.info("Mapping " + proteins.size() + " proteins to peptides...");

		final TIntSet peptideIDs = PreparedCriteria.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(proteinIDs);
		log.info(peptideIDs.size() + " peptide identifiers are mapped to those proteins");
		log.info("Querying " + peptideIDs.size() + " peptide objects...");
		final List<Peptide> peptides = queryPeptidesByIDs(peptideIDs);
		for (final Peptide peptide : peptides) {
			peptidesById.put(peptide.getId(), peptide);
		}
		log.info(peptides.size() + " peptide objects retrieved. " + peptidesById.size() + " in the mapping");
	}

	private List<Peptide> queryPeptidesByIDs(TIntSet peptideIDs) {
		return PreparedCriteria.getPeptidesFromPeptideIDs(peptideIDs, true, 500);
	}

	public Set<Peptide> getPeptidesByID(Collection<Integer> peptideIds) {
		final Set<Peptide> ret = new THashSet<Peptide>();
		final Set<Integer> notFoundPeptideIDs = new THashSet<Integer>();
		for (final Integer peptideID : peptideIds) {
			if (!peptidesById.containsKey(peptideID)) {
				notFoundPeptideIDs.add(peptideID);
			} else {
				ret.add(peptidesById.get(peptideID));
			}
		}
		if (!notFoundPeptideIDs.isEmpty()) {
			final TIntSet list = new TIntHashSet();
			list.addAll(notFoundPeptideIDs);
			final List<Peptide> peptides = queryPeptidesByIDs(list);
			ret.addAll(peptides);
		}
		return ret;
	}
}
