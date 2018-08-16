package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromMSRun extends PeptideDataProvider {
	private final Set<String> msRunIDs;

	public PeptideProviderFromMSRun(String sessionId, Set<String> msRunIDs) {
		this.msRunIDs = msRunIDs;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					for (final String msRunID : msRunIDs) {
						final List<Peptide> peptidesWithMSRun = PreparedQueries.getPeptidesWithMSRun(projectTag,
								msRunID);
						if (testMode && numPeptides + peptidesWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
							result.addAll(peptidesWithMSRun.subList(0, Math.min(peptidesWithMSRun.size(),
									QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
							return result;
						} else {
							result.addAll(peptidesWithMSRun);
						}
						numPeptides += peptidesWithMSRun.size();
					}
				}
			} else {
				for (final String msRunID : msRunIDs) {
					final List<Peptide> peptidesWithMSRun = PreparedQueries.getPeptidesWithMSRun(null, msRunID);

					if (testMode && numPeptides + peptidesWithMSRun.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptidesWithMSRun.subList(0,
								Math.min(peptidesWithMSRun.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
						return result;
					} else {
						result.addAll(peptidesWithMSRun);
					}
					numPeptides += peptidesWithMSRun.size();
				}
			}
		}
		return result;
	}

}
