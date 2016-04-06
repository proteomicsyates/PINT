package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;

public class ProteinProviderFromProteinAccs implements ProteinProviderFromDB {
	private final List<String> accs = new ArrayList<String>();
	private Set<String> projectTags;
	private Map<String, Set<Protein>> proteins;

	public ProteinProviderFromProteinAccs(Collection<String> accs) {
		if (accs == null)
			throw new IllegalArgumentException("accession list is null");
		for (String acc : accs) {
			if (acc != null && !"".equals(acc))
				this.accs.add(acc);
		}

	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteins == null) {
			proteins = new HashMap<String, Set<Protein>>();

			if (projectTags != null) {
				for (String projectTag : projectTags) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins,
							PreparedQueries.getProteinsWithAccessions(accs, projectTag));
				}
			} else {
				PersistenceUtils.addToMapByPrimaryAcc(proteins, PreparedQueries.getProteinsWithAccessions(accs, null));
			}

		}
		return proteins;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap() {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap());
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		proteins = null;
	}

	/**
	 * @return the accs
	 */
	public List<String> getAccs() {
		return accs;
	}

	/**
	 * @return the projectTags
	 */
	public Set<String> getProjectTags() {
		return projectTags;
	}
}
