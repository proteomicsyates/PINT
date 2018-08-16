package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromPTM extends PeptideDataProvider {

	private final String ptmName;

	public PeptideProviderFromPTM(String ptmName) {
		this.ptmName = ptmName;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Peptide> peptides = PreparedQueries.getPeptidesContainingPTM(ptmName, null);
				if (testMode && numPeptides + peptides.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptides.subList(0,
							Math.min(peptides.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
				} else {
					result.addAll(peptides);
				}
				numPeptides += peptides.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Peptide> peptides = PreparedQueries.getPeptidesContainingPTM(ptmName, projectTag);
					if (testMode && numPeptides + peptides.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptides.subList(0,
								Math.min(peptides.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
						return result;
					} else {
						result.addAll(peptides);
					}
					numPeptides += peptides.size();
				}
			}
		}
		return result;
	}

}
