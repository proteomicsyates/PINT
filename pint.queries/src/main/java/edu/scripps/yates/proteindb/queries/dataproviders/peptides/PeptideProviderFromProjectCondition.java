package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromProjectCondition extends PeptideDataProvider {

	private final ConditionReferenceFromCommandValue condition;

	public PeptideProviderFromProjectCondition(ConditionReferenceFromCommandValue condition) {
		this.condition = condition;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			if (condition != null) {
				final Set<ConditionProject> conditionProjects = condition.getConditionProjects();
				for (final ConditionProject conditionProject : conditionProjects) {

					if (conditionProject.getProjectTag() == null) {
						if (projectTags != null && !projectTags.isEmpty()) {
							for (final String projectTag : projectTags) {
								final List<Peptide> peptidesByProjectCondition = PreparedQueries
										.getPeptidesByProjectCondition(projectTag, conditionProject.getConditionName());

								if (testMode && numPeptides
										+ peptidesByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
									result.addAll(peptidesByProjectCondition.subList(0,
											Math.min(peptidesByProjectCondition.size(),
													QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
									return result;
								} else {
									result.addAll(peptidesByProjectCondition);
								}
								numPeptides += peptidesByProjectCondition.size();
							}
						}
					} else {
						if (projectTags == null || projectTags.isEmpty()
								|| projectTags.contains(conditionProject.getProjectTag())) {
							final List<Peptide> peptidesByProjectCondition = PreparedQueries
									.getPeptidesByProjectCondition(conditionProject.getProjectTag(),
											conditionProject.getConditionName());
							if (testMode && numPeptides
									+ peptidesByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
								result.addAll(peptidesByProjectCondition.subList(0,
										Math.min(peptidesByProjectCondition.size(),
												QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
								return result;
							} else {
								result.addAll(peptidesByProjectCondition);
							}
							numPeptides += peptidesByProjectCondition.size();
						}
					}
				}
			} else {
				if (projectTags == null || projectTags.isEmpty()) {
					final List<Peptide> peptidesByProjectCondition = PreparedQueries.getPeptidesByProjectCondition(null,
							null);
					if (testMode
							&& numPeptides + peptidesByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
						result.addAll(peptidesByProjectCondition.subList(0, Math.min(peptidesByProjectCondition.size(),
								QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
					} else {
						result.addAll(peptidesByProjectCondition);
					}
					numPeptides += peptidesByProjectCondition.size();

				} else {
					for (final String projectTag : projectTags) {
						final List<Peptide> psMsByProjectCondition = PreparedQueries
								.getPeptidesByProjectCondition(projectTag, null);
						if (testMode
								&& numPeptides + psMsByProjectCondition.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
							result.addAll(psMsByProjectCondition.subList(0, Math.min(psMsByProjectCondition.size(),
									QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
							return result;
						} else {
							result.addAll(psMsByProjectCondition);
						}
						numPeptides += psMsByProjectCondition.size();

					}
				}
			}
		}
		return result;
	}

}
