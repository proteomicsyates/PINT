package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import gnu.trove.set.hash.THashSet;

/**
 * Class used for checking if a {@link Peptide}, {@link Psm}, {@link Psm},
 * {@link PsmAmount}, {@link QueriableProteinSet}, {@link QueriablePsm} are
 * detected in a certain {@link Condition} + {@link Project}
 *
 * @author Salva
 *
 */
public class ConditionReferenceFromCommandValue {
	private final Set<ConditionProject> conditionProjects = new THashSet<ConditionProject>();

	public class ConditionProject {
		private final String conditionName;
		private final String projectTag;

		ConditionProject(String conditionName, String projectTag) {
			this.conditionName = conditionName;
			this.projectTag = projectTag;
		}

		public String getProjectTag() {
			return projectTag;
		}

		public String getConditionName() {
			return conditionName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Condition) {
				final Condition condition = (Condition) obj;

				if (conditionName != null && (condition.getName().equalsIgnoreCase(conditionName)
						|| conditionName.equalsIgnoreCase(condition.getDescription())) && projectTag == null) {

					return true;
				}
				if (conditionName != null
						&& (condition.getName().equalsIgnoreCase(conditionName)
								|| conditionName.equalsIgnoreCase(condition.getDescription()))
						&& projectTag != null && condition.getProject().getTag().equalsIgnoreCase(projectTag)) {
					return true;
				}
				if (conditionName == null && projectTag != null
						&& condition.getProject().getTag().equalsIgnoreCase(projectTag)) {
					return true;
				}
			}
			return super.equals(obj);
		}
	}

	public ConditionReferenceFromCommandValue(String command) throws MalformedQueryException {

		final String[] split = MyCommandTokenizer.splitCommand(command);
		// has to be even
		if (split.length % 2 == 0) {
			for (int index = 0; index < split.length; index = index + 2) {
				String conditionName = split[index].trim();
				if ("".equals(conditionName)) {
					conditionName = null;
				}
				String projectTag = split[index + 1].trim();
				if ("".equals(projectTag)) {
					projectTag = null;
				}
				if (conditionName == null && projectTag == null) {
					throw new MalformedQueryException(
							"Condition name and project name cannot be null at the same time");
				}
				final ConditionProject conditionProject = new ConditionProject(conditionName, projectTag);
				conditionProjects.add(conditionProject);
			}
			return;
		}

		throw new MalformedQueryException("Command '" + command + "' is not well formed");
	}

	public Set<ConditionProject> getConditionProjects() {
		return conditionProjects;
	}

	/**
	 * Returns true if at least one {@link Condition} pass the test
	 *
	 * @param conditions
	 * @return
	 */
	private boolean passCondition(Collection<Condition> conditions) {
		for (final Condition condition : conditions) {
			if (passCondition(condition))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if all the {@link Condition}s in the
	 * {@link ConditionReferenceFromCommandValue} are found in the argument
	 *
	 * @param conditions
	 * @return
	 */
	public boolean passAllConditions(Collection<Condition> conditions) {
		for (final ConditionProject conditionProject : conditionProjects) {
			boolean found = false;
			for (final Condition condition : conditions) {
				if (conditionProject.equals(condition)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns true only if there is only one {@link Condition} in the
	 * {@link ConditionReferenceFromCommandValue} and is the same as passed in
	 * the argument
	 *
	 * @param condition
	 * @return
	 */
	public boolean passCondition(Condition condition) {

		if (conditionProjects.size() == 1) {
			if (conditionProjects.iterator().next().equals(condition)) {
				return true;
			}

		}
		return false;
	}

	/**
	 * returns true if contains at least one protein in the referenced condition
	 * in the referenced project
	 *
	 * @param protein
	 * @return
	 */
	public boolean passCondition(Protein protein) {
		final Set<Condition> conditions = protein.getConditions();
		return passCondition(conditions);
	}

	/**
	 * returns true if contains at least one protein in the referenced condition
	 * in the referenced project
	 *
	 * @param protein
	 * @return
	 */
	public boolean passCondition(QueriableProteinSet protein) {
		final Set<String> projectTags = new THashSet<String>();
		for (final ConditionProject conditionProject : conditionProjects) {
			projectTags.add(conditionProject.projectTag);
		}
		final Set<Condition> conditions = protein.getConditions();
		return passCondition(conditions);
	}

	/**
	 * Returns true if there at least one protein in the set of proteins pass
	 * the condition, that is that belongs to the referenced condition in the
	 * referenced project
	 *
	 * @param proteins
	 * @return
	 */
	public boolean passConditionProteins(Set<Protein> proteins) {
		for (final Protein protein2 : proteins) {
			if (passCondition(protein2))
				return true;
		}
		return false;
	}

	/**
	 * returns true if contains at least one peptide in the referenced condition
	 * in the referenced project
	 *
	 * @param protein
	 * @return
	 */
	public boolean passCondition(Peptide peptide) {
		final Set<Condition> conditions = peptide.getConditions();
		return passCondition(conditions);
	}

	/**
	 * Returns true if there at least one peptide in the set of peptide pass the
	 * condition, that is that belongs to the referenced condition in the
	 * referenced project
	 *
	 * @param proteins
	 * @return
	 */
	public boolean passConditionPeptides(Set<Peptide> peptides) {
		for (final Peptide peptide : peptides) {
			if (passCondition(peptide))
				return true;
		}
		return false;
	}

	/**
	 * returns true if contains at least one psm in the referenced condition in
	 * the referenced project
	 *
	 * @param psm
	 * @return
	 */
	public boolean passCondition(Psm psm) {
		final Set<Condition> conditions = psm.getConditions();
		return passCondition(conditions);
	}

	/**
	 * returns true if contains at least one psm in the referenced condition in
	 * the referenced project
	 *
	 * @param psm
	 * @return
	 */
	public boolean passCondition(QueriablePsm psm) {
		return passCondition(psm.getPsm());
	}

	/**
	 * Returns true if there at least one psm in the set of psm pass the
	 * condition, that is that belongs to the referenced condition in the
	 * referenced project
	 *
	 * @param psms
	 * @return
	 */
	public boolean passConditionPsms(Set<Psm> psms) {
		for (final Psm psm : psms) {
			if (passCondition(psm))
				return true;
		}
		return false;
	}

	public boolean passCondition(PsmAmount psmAmount) {
		if (psmAmount != null) {
			return passCondition(psmAmount.getCondition());
		}
		return false;
	}
}
