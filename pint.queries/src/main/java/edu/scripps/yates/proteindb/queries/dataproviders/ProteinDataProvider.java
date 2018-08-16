package edu.scripps.yates.proteindb.queries.dataproviders;

import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;

public abstract class ProteinDataProvider implements DataProviderFromDB {
	protected Set<String> projectTags;
	protected Map<String, Set<Protein>> result;

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode), true);
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		return PersistenceUtils.getPeptidesFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		result = null;
	}

}
