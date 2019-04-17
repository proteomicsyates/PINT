package edu.scripps.yates.proteindb.queries.dataproviders;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;

public abstract class PsmDataProvider implements DataProviderFromDB {
	protected Set<String> projectTags;
	protected Map<String, Collection<Psm>> result;

	@Override
	public Map<String, Collection<Protein>> getProteinMap(boolean testMode) {
		return PersistenceUtils.getProteinsFromPsms(getPsmMap(testMode), true);
	}

	@Override
	public Collection<Peptide> getPeptideSet(boolean testMode) {
		return PersistenceUtils.getPeptidesFromPsms(getPsmMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		result = null;
	}

}
