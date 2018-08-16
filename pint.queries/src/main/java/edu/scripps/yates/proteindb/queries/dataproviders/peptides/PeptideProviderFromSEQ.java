package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromSEQ extends PeptideDataProvider {

	private final String mySqlRegularExpression;

	public PeptideProviderFromSEQ(String regularExpression) {
		mySqlRegularExpression = getMySQLRegularExpression(regularExpression);
	}

	/**
	 * Change:<br>
	 * X* by %<br>
	 * X+ by _%<br>
	 * X by _<br>
	 *
	 * @param regularExpression
	 * @return
	 */
	private String getMySQLRegularExpression(String regularExpression) {
		regularExpression = regularExpression.toUpperCase();
		regularExpression = regularExpression.replace("\\W*", "%");
		regularExpression = regularExpression.replace("\\W+", "_%");
		regularExpression = regularExpression.replace("\\W", "_");
		String ret = "";

		// replace anything between brakets by %
		boolean insideBraket = false;
		for (int i = 0; i < regularExpression.length(); i++) {
			if (regularExpression.charAt(i) == '[') {
				insideBraket = true;
				ret += "%";
			} else if (regularExpression.charAt(i) == ']') {
				insideBraket = false;
			} else if (regularExpression.charAt(i) == '|') {

			} else {
				if (!insideBraket) {
					ret += regularExpression.charAt(i);
				}
			}
		}
		if (!"%".equals(ret)) {
			return "%" + ret + "%";
		}
		return ret;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (projectTags == null || projectTags.isEmpty()) {
				final List<Peptide> peptides = PreparedCriteria
						.getCriteriaForPeptideSequence(mySqlRegularExpression, null).list();
				if (testMode && numPeptides + peptides.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptides.subList(0,
							Math.min(peptides.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
				} else {
					result.addAll(peptides);
				}
				numPeptides += peptides.size();
			} else {
				for (final String projectTag : projectTags) {
					final List<Peptide> peptides = PreparedCriteria
							.getCriteriaForPeptideSequence(mySqlRegularExpression, projectTag).list();
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