package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;

public class PreparedCriteria {

	/**
	 * Returns a criteria after creating it, using the paging on the distinct
	 * accessions, and returning all proteins with that accession.<br>
	 * So, the criteria will probably return a list of proteins bigger than the
	 * paging specification, but then, grouping them by acc, should be collapsed
	 * to the same number as in the paging.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param criterionSets
	 * @return
	 */
	public static Criteria getCriteriaByProteinAcc(int pageNumber, int pageSize, CriterionSet... criterionSets) {
		return getCriteriaByProteinAcc(pageNumber, pageSize, null, false, criterionSets);
	}

	/**
	 * Returns a criteria after creating it, using the paging on the distinct
	 * accessions, and returning all proteins with that accession.<br>
	 * So, the criteria will probably return a list of proteins bigger than the
	 * paging specification, but then, grouping them by acc, should be collapsed
	 * to the same number as in the paging.<br>
	 * It also applies an order to the results.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param criterionSets
	 * @return
	 */
	public static Criteria getCriteriaByProteinAcc(int pageNumber, int pageSize, String order, boolean ascending,
			CriterionSet... criterionSets) {
		Criteria cr = getCriteria(Protein.class, criterionSets);
		cr.setProjection(Projections.distinct(Projections.property("acc")));
		cr.setFirstResult(pageNumber);
		cr.setMaxResults(pageSize);
		List<String> proteinAccs = cr.list();
		Criteria cr2 = getCriteria(Protein.class, criterionSets);
		cr2.add(Restrictions.in("acc", proteinAccs));
		if (order != null) {
			cr2.addOrder(getOrder(order, ascending));
		}
		return cr2;
	}

	/**
	 * Gets a criteria from a set of {@link CriterionSet} and over a certain
	 * root persistence {@link Class} and applying a paging and an order.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param clazz
	 * @param criterionSets
	 * @return
	 */
	public static Criteria getCriteria(int pageNumber, int pageSize, String order, boolean ascending, Class clazz,
			CriterionSet... criterionSets) {
		Criteria cr = getCriteria(clazz, criterionSets);
		cr.setFirstResult(pageNumber);
		cr.setMaxResults(pageSize);
		if (order != null) {
			cr.addOrder(getOrder(order, ascending));
		}
		return cr;
	}

	private static Order getOrder(String orderProperty, boolean ascending) {
		if (ascending) {
			return Order.asc(orderProperty);
		} else {
			return Order.desc(orderProperty);
		}
	}

	public static Criteria getCriteria(Class clazz, CriterionSet... criterionSets) {

		String rootClassName = CriterionDescriptor.getAssociatedClassFromAssociationPath(clazz.getName().toLowerCase());
		CriterionSet criterionSet = null;
		for (CriterionSet criterionSet2 : criterionSets) {
			if (criterionSet == null) {
				criterionSet = criterionSet2;
			} else {
				criterionSet.addCriterionSet(criterionSet2);
			}
		}
		final Criteria cr = ContextualSessionHandler.getSession().createCriteria(clazz, rootClassName);
		final Map<String, List<CriterionDescriptor>> criterionByAssociationPath = criterionSet
				.getCriterionByAssociationPath();
		for (String associationPath : criterionByAssociationPath.keySet()) {
			final String rootClass = CriterionDescriptor.getAssociatedClassFromAssociationPath(associationPath);
			final List<CriterionDescriptor> criterionDescriptors = criterionByAssociationPath.get(associationPath);
			if (rootClass.equalsIgnoreCase(rootClassName)) {
				for (CriterionDescriptor criterionDescriptor : criterionDescriptors) {
					cr.add(criterionDescriptor.getCriterion());
				}
			} else {
				final Criteria cr2 = cr.createCriteria(associationPath,
						CriterionDescriptor.getAssociatedClassFromAssociationPath(associationPath));
				for (CriterionDescriptor criterionDescriptor : criterionDescriptors) {
					cr2.add(criterionDescriptor.getCriterion());
				}
			}
		}
		return cr;
	}

	public static CriterionSet getProteinsByProjectConditionCriteria(String projectTag, String conditionName) {
		CriterionSet ret = new CriterionSet();
		if (projectTag == null && conditionName == null) {
			// final Criteria cr =
			// ContextualSessionHandler.createCriteria(Protein.class);
			// cr.setMaxResults(pageSize);
			// cr.setFirstResult(numPage);
			// ret.addAll(cr);
			return ret;
		}
		Project project = null;
		if (projectTag != null) {
			final List<Project> projectList = ContextualSessionHandler.createCriteria(Project.class)
					.add(Restrictions.eq("tag", projectTag)).list();
			if (!projectList.isEmpty()) {
				project = projectList.get(0);
			}
		}

		if (conditionName != null) {
			final Criterion restriction = Restrictions.eq("conditions.name", conditionName);
			ret.addCriterion("protein.conditions", restriction);

			if (project != null) {
				ret.addCriterion("protein.conditions", Restrictions.eq("conditions.project", project));
			}
		} else {
			// if project is not null, restrict by the project
			if (project != null) {
				ret.addCriterion("protein.conditions", Restrictions.eq("conditions.project", project));
			}
		}
		return ret;
	}

	public static Criteria getCriteriaForGenesInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForOrganismsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getSession().createCriteria(Organism.class, "organism")
				.createAlias("organism.samples", "sample").createAlias("sample.conditions", "condition")
				.createAlias("condition.project", "project").add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("taxonomyId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinAccsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("accession")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("accession").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinNamesInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("description")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("description").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinProjectionInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.genes", "gene").createAlias("protein.proteinAccessions", "proteinAccession")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections
						.distinct(Projections.projectionList().add(Projections.property("gene.geneId"), "gene")
								.add(Projections.property("proteinAccession.accession"), "acc")
								.add(Projections.property("proteinAccession.description"), "description")));

		cr.add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("proteinAccession.accession").ignoreCase());

		return cr;
	}
}
