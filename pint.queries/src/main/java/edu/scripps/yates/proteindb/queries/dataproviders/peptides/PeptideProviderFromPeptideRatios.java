package edu.scripps.yates.proteindb.queries.dataproviders.peptides;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.queries.dataproviders.PeptideDataProvider;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue;
import edu.scripps.yates.proteindb.queries.semantic.ConditionReferenceFromCommandValue.ConditionProject;
import edu.scripps.yates.proteindb.queries.semantic.util.QueriesUtil;
import gnu.trove.set.hash.THashSet;

public class PeptideProviderFromPeptideRatios extends PeptideDataProvider {

	private final ConditionReferenceFromCommandValue condition1;
	private final ConditionReferenceFromCommandValue condition2;
	private final String ratioName;

	public PeptideProviderFromPeptideRatios(ConditionReferenceFromCommandValue condition1,
			ConditionReferenceFromCommandValue condition2, String ratioName) {
		this.condition1 = condition1;
		this.condition2 = condition2;
		this.ratioName = ratioName;
	}

	@Override
	public Set<Peptide> getPeptideSet(boolean testMode) {
		if (result == null) {
			result = new THashSet<Peptide>();
			int numPeptides = 0;
			// condition1 and condition2 only can contain one ConditionProject
			if (condition1.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("First condition con only be referring to one condition");
			}
			if (condition2.getConditionProjects().size() != 1) {
				throw new IllegalArgumentException("Second condition con only be referring to one condition");
			}
			final ConditionProject conditionProject1 = condition1.getConditionProjects().iterator().next();
			final ConditionProject conditionProject2 = condition2.getConditionProjects().iterator().next();
			if (projectTags == null || projectTags.isEmpty() || projectTags.contains(conditionProject2.getProjectTag())
					|| conditionProject2.getProjectTag() == null) {
				final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
						.getRatioDescriptorsByProject(conditionProject1.getProjectTag());
				String actualRatioName = null;
				// first look if the ratioName exists as it is provided (case
				// sentitive)
				for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
					if (ratioDescriptor.getDescription().equals(ratioName)) {
						actualRatioName = ratioName;
					}
				}
				if (actualRatioName == null) {
					// try to look for the ratioName case insensitive and use it
					for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
						if (ratioDescriptor.getDescription().equalsIgnoreCase(ratioName)) {
							actualRatioName = ratioDescriptor.getDescription();
						}
					}
				}
				if (actualRatioName == null)
					actualRatioName = ratioName;
				final Collection<Peptide> peptidesWithRatios = PreparedQueries.getPeptidesWithRatios(
						conditionProject1.getConditionName(), conditionProject2.getConditionName(),
						conditionProject1.getProjectTag(), actualRatioName);
				final List<Peptide> peptideList = new ArrayList<Peptide>();
				peptideList.addAll(peptidesWithRatios);
				if (testMode && numPeptides + peptideList.size() > QueriesUtil.TEST_MODE_NUM_PEPTIDES) {
					result.addAll(peptideList.subList(0,
							Math.min(peptideList.size(), QueriesUtil.TEST_MODE_NUM_PEPTIDES - numPeptides)));
				} else {
					result.addAll(peptideList);
				}
				numPeptides += peptideList.size();
			}
		}
		return result;

	}

}
