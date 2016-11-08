package edu.scripps.yates.proteindb.queries.dataproviders.protein;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;

public class ProteinProviderFromTaxonomy implements ProteinProviderFromDB {
	private final String organismName;
	private final String ncbiTaxID;
	private HashMap<String, Set<Protein>> proteins;
	private Set<String> projectTags;

	public ProteinProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap(boolean testMode) {
		if (proteins == null) {
			int numProteins = 0;
			proteins = new HashMap<String, Set<Protein>>();
			if (projectTags == null || projectTags.isEmpty()) {
				final Map<String, Set<Protein>> proteinsWithTaxonomy = PreparedQueries.getProteinsWithTaxonomy(null,
						organismName, ncbiTaxID);
				if (testMode && numProteins + proteinsWithTaxonomy.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
					PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(proteinsWithTaxonomy,
							QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
					return proteins;
				} else {
					PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithTaxonomy);
				}
				numProteins += proteinsWithTaxonomy.size();
			} else {
				for (String projectTag : projectTags) {
					final Map<String, Set<Protein>> proteinsWithTaxonomy = PreparedQueries
							.getProteinsWithTaxonomy(projectTag, organismName, ncbiTaxID);
					if (testMode && numProteins + proteinsWithTaxonomy.size() > QueriesUtil.TEST_MODE_NUM_PROTEINS) {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, QueriesUtil.getProteinSubList(
								proteinsWithTaxonomy, QueriesUtil.TEST_MODE_NUM_PROTEINS - numProteins));
						return proteins;
					} else {
						PersistenceUtils.addToMapByPrimaryAcc(proteins, proteinsWithTaxonomy);
					}
					numProteins += proteinsWithTaxonomy.size();
				}
			}
		}
		return proteins;
	}

	@Override
	public Map<String, Set<Psm>> getPsmMap(boolean testMode) {
		return PersistenceUtils.getPsmsFromProteins(getProteinMap(testMode));
	}

	@Override
	public void setProjectTags(Set<String> projectTags) {
		this.projectTags = projectTags;
		proteins = null;

	}

}
