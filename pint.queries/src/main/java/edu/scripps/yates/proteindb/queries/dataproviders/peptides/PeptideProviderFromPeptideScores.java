package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromPeptideScores extends PeptideDataProvider {

	private final String scoreNameString;
	private final String scoreTypeString;

	public PeptideProviderFromPeptideScores(String scoreNameString, String scoreTypeString) {
		this.scoreNameString = scoreNameString;
		this.scoreTypeString = scoreTypeString;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			int numPeptides = 0;
			result = new THashSet<Peptide>();
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Peptide> peptides1 = PreparedQueries.getPeptidesWithScores(scoreNameString, scoreTypeString,
						null);
				final List<Peptide> peptides2 = PreparedQueries.getPeptidesWithPTMScores(scoreNameString,
						scoreTypeString, null);
				final Collection<Peptide> peptideUnion = PersistenceUtils.peptideUnion(peptides1, peptides2);
				final List<Peptide> peptideList = new ArrayList<Peptide>();
				peptideList.addAll(peptideUnion);
				if (testMode && numPeptides + peptideList.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptideList.subList(0,
							Math.min(peptideList.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
				} else {
					result.addAll(peptideUnion);
				}
				numPeptides += peptideList.size();

			} else {
				for (final String projectTag : projectTags) {
					final List<Peptide> peptides1 = PreparedQueries.getPeptidesWithScores(scoreNameString,
							scoreTypeString, projectTag);
					final List<Peptide> peptides2 = PreparedQueries.getPeptidesWithPTMScores(scoreNameString,
							scoreTypeString, projectTag);
					final Set<Peptide> peptideUnion = PersistenceUtils.peptideUnion(peptides1, peptides2);
					final List<Peptide> peptideList = new ArrayList<Peptide>();
					peptideList.addAll(peptideUnion);
					if (testMode && numPeptides + peptideList.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptideList.subList(0,
								Math.min(peptideList.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
						return result;
					} else {
						result.addAll(peptideUnion);
					}
					numPeptides += peptideList.size();
				}
			}
		}
		return result;
	}

}
