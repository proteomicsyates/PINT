package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromPeptideLabeledAmount extends PeptideDataProvider {
	private final String labelName;
	private final Boolean singleton;

	public PeptideProviderFromPeptideLabeledAmount(String labelString, Boolean singleton) {
		labelName = labelString;
		this.singleton = singleton;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (projectTags != null) {
				for (final String projectTag : projectTags) {
					final List<Peptide> peptidesWithLabeledAmount = PreparedQueries
							.getPeptidesWithLabeledAmount(projectTag, labelName, singleton);
					if (testMode
							&& numPeptides + peptidesWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptidesWithLabeledAmount.subList(0, Math.min(peptidesWithLabeledAmount.size(),
								QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
						return result;
					} else {
						result.addAll(peptidesWithLabeledAmount);
					}
					numPeptides += peptidesWithLabeledAmount.size();
				}
			} else {
				final List<Peptide> peptidesWithLabeledAmount = PreparedQueries.getPeptidesWithLabeledAmount(null,
						labelName, singleton);
				if (testMode && numPeptides + peptidesWithLabeledAmount.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptidesWithLabeledAmount.subList(0, Math.min(peptidesWithLabeledAmount.size(),
							QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
					return result;
				} else {
					result.addAll(peptidesWithLabeledAmount);
				}
				numPeptides += peptidesWithLabeledAmount.size();
			}
		}
		return result;
	}

}
