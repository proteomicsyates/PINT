package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromTaxonomy extends PeptideDataProvider {
	private final String organismName;
	private final String ncbiTaxID;

	public PeptideProviderFromTaxonomy(String organismName, String ncbiTaxID) {
		this.organismName = organismName;
		this.ncbiTaxID = ncbiTaxID;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Peptide> peptidesWithTaxonomy = PreparedQueries.getPeptidesWithTaxonomy(null, organismName,
						ncbiTaxID);
				if (testMode && numPeptides + result.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptidesWithTaxonomy.subList(0,
							Math.min(0, QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
				} else {
					result.addAll(peptidesWithTaxonomy);
				}
				numPeptides += peptidesWithTaxonomy.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Peptide> peptidesWithTaxonomy = PreparedQueries.getPeptidesWithTaxonomy(projectTag,
							organismName, ncbiTaxID);
					if (testMode && numPeptides + result.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptidesWithTaxonomy.subList(0,
								Math.min(0, QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
					} else {
						result.addAll(peptidesWithTaxonomy);
					}
					numPeptides += peptidesWithTaxonomy.size();
				}
			}
		}
		return result;
	}

}
