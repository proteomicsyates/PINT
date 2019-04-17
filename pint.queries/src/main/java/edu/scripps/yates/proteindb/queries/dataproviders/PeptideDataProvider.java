package edu.scripps.yates.proteindb.queries.dataproviders;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;

public abstract class PeptideDataProvider implements DataProviderFromDB {
	protected Set<String> projectTags;
	protected Set<Peptide> result;

	@Override
	public Map<String, Collection<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPeptidesUsingProteinToPeptideMappingTable(getPeptideSet(testMode), true);
	}

	@Override
	public Map<String, Collection<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromPeptides(getPeptideSet(testMode), true);
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		result = null;
	}
}
