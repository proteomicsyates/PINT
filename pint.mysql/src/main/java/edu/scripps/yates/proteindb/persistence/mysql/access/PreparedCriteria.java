package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;

public class PreparedCriteria {
	private static final Logger log = Logger.getLogger(PreparedCriteria.class);

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
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(clazz, rootClassName);
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

	public static Criteria getCriteriaForGenes() {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForGenesInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.add(Restrictions.eq("project.tag", projectTag)).add(Restrictions.eq("gene.geneType", "primary"))
				.addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForOrganismsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Organism.class, "organism")
				.createAlias("organism.samples", "sample").createAlias("sample.conditions", "condition")
				.createAlias("condition.project", "project").add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("taxonomyId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinPrimaryAccsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("accession")));
		cr.add(Restrictions.eq("project.tag", projectTag)).add(Restrictions.eq("proteinAccession.isPrimary", true))
				.addOrder(Order.asc("accession").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinNamesInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("description")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("description").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinProjectionInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.genes", "gene").createAlias("protein.proteinAccessions", "proteinAccession")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections
						.distinct(Projections.projectionList().add(Projections.property("gene.geneId"), "gene")
								.add(Projections.property("proteinAccession.accession"), "acc")
								.add(Projections.property("proteinAccession.description"), "description")));

		cr.add(Restrictions.eq("project.tag", projectTag));

		return cr;
	}

	public static Criteria getCriteriaForProteinProjectionByProteinACCInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.genes", "gene").createAlias("protein.proteinAccessions", "proteinAccession")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.projectionList()
						.add(Projections.distinct(Projections.property("proteinAccession.accession")), "acc")
						.add(Projections.property("gene.geneId"), "gene")
						.add(Projections.property("proteinAccession.description"), "description"));

		cr.add(Restrictions.eq("project.tag", projectTag));

		return cr;
	}

	public static Criteria getCriteriaForProteinProjectionByProteinNameInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.genes", "gene").createAlias("protein.proteinAccessions", "proteinAccession")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.projectionList()
						.add(Projections.distinct(Projections.property("proteinAccession.description")), "description")
						.add(Projections.property("proteinAccession.accession"), "acc")
						.add(Projections.property("gene.geneId"), "gene"));

		cr.add(Restrictions.eq("project.tag", projectTag));

		return cr;
	}

	public static Criteria getCriteriaForProteinProjectionByGeneNameInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.genes", "gene").createAlias("protein.proteinAccessions", "proteinAccession")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.projectionList()
						.add(Projections.distinct(Projections.property("gene.geneId")), "gene")
						.add(Projections.property("proteinAccession.accession"), "acc")
						.add(Projections.property("proteinAccession.description"), "description"));

		cr.add(Restrictions.eq("project.tag", projectTag));

		return cr;
	}

	public static Criteria getCriteriaForProteinRatio(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(ProteinRatioValue.class, "ratio")
				.createAlias("ratio.ratioDescriptor", "descriptor")
				.createAlias("descriptor.conditionByExperimentalCondition1Id", "condition1")
				.createAlias("descriptor.conditionByExperimentalCondition2Id", "condition2")
				.createAlias("condition1.project", "project");
		cr.add(Restrictions.eq("project.tag", projectTag));
		cr.add(Restrictions.eq("condition1.name", condition1Name));
		cr.add(Restrictions.eq("condition2.name", condition2Name));
		cr.add(Restrictions.eq("descriptor.description", ratioName));
		return cr;
	}

	public static Criteria getCriteriaForPeptideRatio(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(PeptideRatioValue.class, "ratio")
				.createAlias("ratio.ratioDescriptor", "descriptor")
				.createAlias("descriptor.conditionByExperimentalCondition1Id", "condition1")
				.createAlias("descriptor.conditionByExperimentalCondition2Id", "condition2")
				.createAlias("condition1.project", "project");
		cr.add(Restrictions.eq("project.tag", projectTag));
		cr.add(Restrictions.eq("condition1.name", condition1Name));
		cr.add(Restrictions.eq("condition2.name", condition2Name));
		cr.add(Restrictions.eq("descriptor.description", ratioName));
		return cr;
	}

	public static Criteria getCriteriaForPsmRatio(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(PsmRatioValue.class, "ratio")
				.createAlias("ratio.ratioDescriptor", "descriptor")
				.createAlias("descriptor.conditionByExperimentalCondition1Id", "condition1")
				.createAlias("descriptor.conditionByExperimentalCondition2Id", "condition2")
				.createAlias("condition1.project", "project");
		cr.add(Restrictions.eq("project.tag", projectTag));
		cr.add(Restrictions.eq("condition1.name", condition1Name));
		cr.add(Restrictions.eq("condition2.name", condition2Name));
		cr.add(Restrictions.eq("descriptor.description", ratioName));
		return cr;
	}

	public static Criteria getCriteriaForProteinRatioMaximumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for max protein ratio: " + condition1Name + " vs " + condition2Name
				+ " ratioName: " + ratioName + " in project " + projectTag);
		final Criteria cr = getCriteriaForProteinRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", Double.MAX_VALUE));
		cr.setProjection(Projections.max("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForProteinRatioMinimumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for min protein ratio: " + condition1Name + " vs " + condition2Name
				+ " ratioName: " + ratioName + " in project " + projectTag);

		final Criteria cr = getCriteriaForProteinRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", -Double.MAX_VALUE));
		cr.setProjection(Projections.min("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForPeptideRatioMaximumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for max peptide ratio: " + condition1Name + " vs " + condition2Name
				+ " ratioName: " + ratioName + " in project " + projectTag);

		final Criteria cr = getCriteriaForPeptideRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", Double.MAX_VALUE));
		cr.setProjection(Projections.max("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForPeptideRatioMinimumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for min peptide ratio: " + condition1Name + " vs " + condition2Name
				+ " ratioName: " + ratioName + " in project " + projectTag);

		final Criteria cr = getCriteriaForPeptideRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", -Double.MAX_VALUE));
		cr.setProjection(Projections.min("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForPsmRatioMaximumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for max psm ratio: " + condition1Name + " vs " + condition2Name + " ratioName: "
				+ ratioName + " in project " + projectTag);

		final Criteria cr = getCriteriaForPsmRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", Double.MAX_VALUE));
		cr.setProjection(Projections.max("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForPsmRatioMinimumValue(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		log.info("Preparing criteria for min psm ratio: " + condition1Name + " vs " + condition2Name + " ratioName: "
				+ ratioName + " in project " + projectTag);

		final Criteria cr = getCriteriaForPsmRatio(condition1Name, condition2Name, projectTag, ratioName);
		cr.add(Restrictions.ne("ratio.value", -Double.MAX_VALUE));
		cr.setProjection(Projections.min("ratio.value"));
		return cr;
	}

	public static Criteria getCriteriaForPsmSequence(String regexp, String projectTag) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm");
		if (projectTag != null && !"".equals(projectTag)) {
			cr.createAlias("psm.msRun", "msRun").createAlias("msRun.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.add(Restrictions.like("psm.sequence", regexp));
		return cr;
	}

	public static Criteria getConditionsByMSRunCriteria(MsRun msRun) {
		if (msRun == null) {
			return null;
		}
		log.info("Preparing criteria for conditions related to msRun " + msRun.getRunId());
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition");

		cr.createAlias("condition.proteins", "protein");
		cr.add(Restrictions.eq("protein.msRun", msRun));
		cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return cr;
	}

	public static Criteria getCriteriaForDifferentPeptidesInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("sequence")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("sequence").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPSMsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("psmId")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("psmId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPSMs() {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("psmId")));
		cr.addOrder(Order.asc("psmId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinPrimaryAccsInProjectInMSRun(String projectTag, String runID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("protein.msRun", "msRun").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("accession")));
		cr.add(Restrictions.eq("msRun.runId", runID)).add(Restrictions.eq("project.tag", projectTag))
				.add(Restrictions.eq("proteinAccession.isPrimary", true)).addOrder(Order.asc("accession").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPeptidesInProjectInMSRun(String projectTag, String runID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.msRun", "msRun").createAlias("psm.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("sequence")));
		cr.add(Restrictions.eq("msRun.runId", runID)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("sequence").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPSMsInProjectInMSRun(String projectTag, String runID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.msRun", "msRun").createAlias("psm.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("psmId")));
		cr.add(Restrictions.eq("msRun.runId", runID)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("psmId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForGenesInProjectInMSRun(String projectTag, String runID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.msRun", "msRun")
				.createAlias("protein.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.add(Restrictions.eq("msRun.runId", runID)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinPrimaryAccsInProjectInCondition(String projectTag,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("accession")));
		cr.add(Restrictions.eq("condition.name", conditionName)).add(Restrictions.eq("project.tag", projectTag))
				.add(Restrictions.eq("proteinAccession.isPrimary", true)).addOrder(Order.asc("accession").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPeptidesInProjectInCondition(String projectTag,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("sequence")));
		cr.add(Restrictions.eq("condition.name", conditionName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("sequence").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPSMsInProjectInCondition(String projectTag, String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("psmId")));
		cr.add(Restrictions.eq("condition.name", conditionName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("psmId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForGenesInProjectInCondition(String projectTag, String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.add(Restrictions.eq("condition.name", conditionName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForMSRunsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("runId")));
		cr.add(Restrictions.eq("project.tag", projectTag)).addOrder(Order.asc("runId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForMSRunsInProjectInCondition(String projectTag, String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("runId")));
		cr.add(Restrictions.eq("condition.name", conditionName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("runId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForProteinPrimaryAccsInProjectInSample(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinAccession.class, "proteinAccession")
				.createAlias("proteinAccession.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.project", "project").createAlias("condition.sample", "sample")
				.setProjection(Projections.distinct(Projections.property("accession")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.add(Restrictions.eq("proteinAccession.isPrimary", true)).addOrder(Order.asc("accession").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPeptidesInProjectInSample(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.createAlias("condition.sample", "sample")
				.setProjection(Projections.distinct(Projections.property("sequence")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("sequence").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForDifferentPSMsInProjectInSample(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition").createAlias("condition.project", "project")
				.createAlias("condition.sample", "sample")
				.setProjection(Projections.distinct(Projections.property("psmId")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("psmId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForGenesInProjectInSample(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class, "gene")
				.createAlias("gene.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.sample", "sample").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("geneId")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("geneId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForMSRunsInProjectInSample(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.sample", "sample").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("runId")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("runId").ignoreCase());
		return cr;
	}

	public static Criteria getCriteriaForConditionsInProjectInMSRun(String projectTag, String msRunID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.proteins", "protein").createAlias("protein.msRun", "msRun")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("condition.id")));
		cr.add(Restrictions.eq("msRun.runId", msRunID)).add(Restrictions.eq("project.tag", projectTag));
		return cr;
	}

	public static Criteria getCriteriaForSamplesInProjectInMSRun(String projectTag, String msRunID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Sample.class, "sample")
				.createAlias("sample.conditions", "condition").createAlias("condition.proteins", "protein")
				.createAlias("protein.msRun", "msRun").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("sample.id")));

		cr.add(Restrictions.eq("msRun.runId", msRunID)).add(Restrictions.eq("project.tag", projectTag));
		return cr;
	}

	public static Criteria getCriteriaForConditionsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.proteins", "protein").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("condition.id")));
		cr.add(Restrictions.eq("project.tag", projectTag));
		return cr;
	}

	public static List<Peptide> getPeptidesByIds(Collection<Integer> peptideIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
				.add(Restrictions.in("peptide.id", peptideIds));
		return cr.list();
	}

	public static List<Protein> getProteinsByIds(Collection<Integer> proteinIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.add(Restrictions.in("protein.id", proteinIds));
		return cr.list();
	}

	public static List<Psm> getPsmsByIds(Collection<Integer> psmIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.add(Restrictions.in("psm.id", psmIds));
		return cr.list();
	}

	public static List<Integer> getPeptideIdsFromProteins(Collection<Integer> proteinIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
				.createAlias("peptide.proteins", "protein").add(Restrictions.in("protein.id", proteinIds))
				.setProjection(
						Projections.projectionList().add(Projections.distinct(Projections.property("peptide.id"))));
		return cr.list();

	}

	public static List<Integer> getPsmIdsFromProteins(Collection<Integer> proteinIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.proteins", "protein").add(Restrictions.in("protein.id", proteinIds))
				.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("psm.id"))));
		return cr.list();

	}

	public static List<Integer> getPsmIdsFromPeptides(Collection<Integer> peptideIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.peptides", "peptide").add(Restrictions.in("peptide.id", peptideIds))
				.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("psm.id"))));
		return cr.list();

	}
}
