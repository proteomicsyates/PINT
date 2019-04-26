package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.AmountType;
import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
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
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.AmountValueWrapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.RatioValueWrapper;
import gnu.trove.set.TIntSet;

public class PreparedCriteria {
	private static final Logger log = Logger.getLogger(PreparedCriteria.class);

	/**
	 * Returns a criteria after creating it, using the paging on the distinct
	 * accessions, and returning all proteins with that accession.<br>
	 * So, the criteria will probably return a list of proteins bigger than the
	 * paging specification, but then, grouping them by acc, should be collapsed to
	 * the same number as in the paging.
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
	 * paging specification, but then, grouping them by acc, should be collapsed to
	 * the same number as in the paging.<br>
	 * It also applies an order to the results.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param criterionSets
	 * @return
	 */
	public static Criteria getCriteriaByProteinAcc(int pageNumber, int pageSize, String order, boolean ascending,
			CriterionSet... criterionSets) {
		final Criteria cr = getCriteria(Protein.class, criterionSets);
		cr.setProjection(Projections.distinct(Projections.property("acc")));
		cr.setFirstResult(pageNumber);
		cr.setMaxResults(pageSize);
		final List<String> proteinAccs = cr.list();
		final Criteria cr2 = getCriteria(Protein.class, criterionSets);
		cr2.add(Restrictions.in("acc", proteinAccs));
		if (order != null) {
			cr2.addOrder(getOrder(order, ascending));
		}
		return cr2;
	}

	public static List<Protein> getCriteriaByProteinACC(Collection<String> accs, String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		cr.add(Restrictions.in("acc", accs));
		if (projectTag != null && !"".equals(projectTag)) {
			cr.createAlias("protein.conditions", "condition");
			cr.createAlias("condition.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		return cr.list();
	}

	/**
	 * Gets a criteria from a set of {@link CriterionSet} and over a certain root
	 * persistence {@link Class} and applying a paging and an order.
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param clazz
	 * @param criterionSets
	 * @return
	 */
	public static Criteria getCriteria(int pageNumber, int pageSize, String order, boolean ascending, Class clazz,
			CriterionSet... criterionSets) {
		final Criteria cr = getCriteria(clazz, criterionSets);
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

		final String rootClassName = CriterionDescriptor
				.getAssociatedClassFromAssociationPath(clazz.getName().toLowerCase());
		CriterionSet criterionSet = null;
		for (final CriterionSet criterionSet2 : criterionSets) {
			if (criterionSet == null) {
				criterionSet = criterionSet2;
			} else {
				criterionSet.addCriterionSet(criterionSet2);
			}
		}
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(clazz, rootClassName);
		final Map<String, List<CriterionDescriptor>> criterionByAssociationPath = criterionSet
				.getCriterionByAssociationPath();
		for (final String associationPath : criterionByAssociationPath.keySet()) {
			final String rootClass = CriterionDescriptor.getAssociatedClassFromAssociationPath(associationPath);
			final List<CriterionDescriptor> criterionDescriptors = criterionByAssociationPath.get(associationPath);
			if (rootClass.equalsIgnoreCase(rootClassName)) {
				for (final CriterionDescriptor criterionDescriptor : criterionDescriptors) {
					cr.add(criterionDescriptor.getCriterion());
				}
			} else {
				final Criteria cr2 = cr.createCriteria(associationPath,
						CriterionDescriptor.getAssociatedClassFromAssociationPath(associationPath));
				for (final CriterionDescriptor criterionDescriptor : criterionDescriptors) {
					cr2.add(criterionDescriptor.getCriterion());
				}
			}
		}
		return cr;
	}

	public static CriterionSet getProteinsByProjectConditionCriteria(String projectTag, String conditionName) {
		final CriterionSet ret = new CriterionSet();
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

	// public static Criteria getCriteriaForGenes() {
	// final Criteria cr =
	// ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class,
	// "gene")
	// .createAlias("gene.proteins",
	// "protein").createAlias("protein.conditions", "condition")
	// .createAlias("condition.project", "project")
	// .setProjection(Projections.distinct(Projections.property("geneId")));
	// cr.addOrder(Order.asc("geneId").ignoreCase());
	// return cr;
	// }

	// public static Criteria getCriteriaForGenesInProject(String projectTag) {
	// final Criteria cr =
	// ContextualSessionHandler.getCurrentSession().createCriteria(Gene.class,
	// "gene")
	// .createAlias("gene.proteins",
	// "protein").createAlias("protein.conditions", "condition")
	// .createAlias("condition.project", "project")
	// .setProjection(Projections.distinct(Projections.property("geneId")));
	// cr.add(Restrictions.eq("project.tag",
	// projectTag)).add(Restrictions.eq("gene.geneType", "primary"))
	// .addOrder(Order.asc("geneId").ignoreCase());
	// return cr;
	// }

	public static Criteria getCriteriaForOrganismsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Organism.class, "organism")
				.createAlias("organism.samples", "sample").createAlias("sample.conditions", "condition")
				.createAlias("condition.project", "project").add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("taxonomyId").ignoreCase());
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

	public static Criteria getCriteriaForProteinProjection2(String projectTag, String conditionName, String runID,
			String sampleName) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		if (runID != null) {
			cr.createAlias("protein.msRuns", "msRun");
		}
		if (projectTag != null || conditionName != null || sampleName != null) {
			cr.createAlias("protein.conditions", "condition");
		}
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		if (projectTag != null) {
			cr.createAlias("condition.project", "project");
		}
		cr.setProjection(
				Projections.distinct(Projections.projectionList().add(Projections.property("protein.acc"), "acc")));
//		cr.setProjection(				 Projections.projectionList().add(Projections.property("protein.acc"), "acc"));

		if (runID != null) {
			cr.add(Restrictions.eq("msRun.runId", runID));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}

		return cr;
	}

	public static Criteria getCriteriaForProteinRatio(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(ProteinRatioValue.class, "ratio").createAlias("ratio.ratioDescriptor", "descriptor")
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

		final Criteria cr = ContextualSessionHandler.getCurrentSession()
				.createCriteria(PeptideRatioValue.class, "ratio").createAlias("ratio.ratioDescriptor", "descriptor")
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

	public static List<Psm> getCriteriaForPsmSequence(String regexp, String projectTag) {

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm");
		if (projectTag != null && !"".equals(projectTag)) {
			cr.createAlias("psm.msRun", "msRun");
			cr.createAlias("msRun.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.add(Restrictions.like("psm.sequence", regexp));
		return cr.list();
	}

	public static List<Condition> getConditionsByMSRunCriteria(MsRun msRun) {
		if (msRun == null) {
			return Collections.emptyList();
		}
		log.info("Preparing criteria for conditions related to msRun " + msRun.getRunId());
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition");

		cr.createAlias("condition.proteins", "protein");
		cr.createAlias("protein.msRuns", "msRun");
		cr.add(Restrictions.eq("msRun.id", msRun.getId()));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<Protein> getProteinsByMSRunsCriteria(Collection<MsRun> msRuns) {
		if (msRuns == null || msRuns.isEmpty()) {
			return null;
		}
		if (msRuns.size() == 1) {
			return getProteinsByMSRunCriteria(msRuns.iterator().next());
		}

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		cr.createAlias("protein.msRuns", "msrun");
		cr.add(Restrictions.in("msrun.id", msRuns.stream().map(m -> m.getId()).collect(Collectors.toSet())));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<Protein> getProteinsByMSRunCriteria(MsRun msRun) {
		if (msRun == null) {
			return null;
		}

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		cr.createAlias("protein.msRuns", "msrun");
		cr.add(Restrictions.eq("msrun.id", msRun.getId()));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<Peptide> getPeptidesByMSRunsCriteria(Collection<MsRun> msRuns) {
		if (msRuns == null || msRuns.isEmpty()) {
			return null;
		}
		if (msRuns.size() == 1) {
			return getPeptidesByMSRunCriteria(msRuns.iterator().next());
		}

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide");
		cr.createAlias("peptide.msRuns", "msrun");
		cr.add(Restrictions.in("msrun.id", msRuns.stream().map(m -> m.getId()).collect(Collectors.toSet())));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<Peptide> getPeptidesByMSRunCriteria(MsRun msRun) {
		if (msRun == null) {
			return null;
		}

		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide");
		cr.createAlias("peptide.msRuns", "msrun");
		cr.add(Restrictions.eq("msrun.id", msRun.getId()));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<String> getCriteriaForProteinPrimaryAccs(String projectTag, String runID, String sampleName,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		if (runID != null) {
			cr.createAlias("protein.msRuns", "msRun");
		}
		if (projectTag != null || conditionName != null || sampleName != null) {
			cr.createAlias("protein.conditions", "condition");
		}
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		if (projectTag != null) {
			cr.createAlias("condition.project", "project");
		}
		cr.setProjection(Projections.distinct(Projections.property("protein.acc")));

		if (runID != null) {
			cr.add(Restrictions.eq("msRun.runId", runID));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}
		final List<String> accs = cr.list();
		return accs;
	}

	public static Long getCriteriaForNumPSMsPerPeptide(Peptide peptide) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm");
		cr.setProjection(Projections.countDistinct("psm.psmId"));
		cr.add(Restrictions.eq("psm.peptide", peptide));
		final Long num = (Long) cr.uniqueResult();
		return num;
	}

	public static Long getCriteriaForNumDifferentPSMs(String projectTag, String runID, String sampleName,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm");
		if (runID != null) {
			cr.createAlias("psm.msRun", "msRun");
		}
		if (projectTag != null || conditionName != null || sampleName != null) {
			cr.createAlias("psm.conditions", "condition");
		}
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		if (projectTag != null) {
			cr.createAlias("condition.project", "project");
		}
		cr.setProjection(Projections.countDistinct("psm.psmId"));

		if (runID != null) {
			cr.add(Restrictions.eq("msRun.runId", runID));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}

		return (Long) cr.uniqueResult();
	}

	public static Long getCriteriaForNumDifferentProteins(String projectTag, String runID, String sampleName,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		if (runID != null) {
			cr.createAlias("protein.msRuns", "msRun");
		}
		if (projectTag != null || conditionName != null || sampleName != null) {
			cr.createAlias("protein.conditions", "condition");
		}
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		if (projectTag != null) {
			cr.createAlias("condition.project", "project");
		}
		cr.setProjection(Projections.countDistinct("protein.acc"));

		if (runID != null) {
			cr.add(Restrictions.eq("msRun.runId", runID));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}
		final Long num = (Long) cr.uniqueResult();
		return num;
	}

	public static Long getCriteriaForNumDifferentPeptides(String projectTag, String runID, String sampleName,
			String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide");
		if (runID != null) {
			cr.createAlias("peptide.msRuns", "msRun");
		}
		if (projectTag != null || conditionName != null || sampleName != null) {
			cr.createAlias("peptide.conditions", "condition");
		}
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		if (projectTag != null) {
			cr.createAlias("condition.project", "project");
		}
		cr.setProjection(Projections.countDistinct("peptide.sequence"));

		if (runID != null) {
			cr.add(Restrictions.eq("msRun.runId", runID));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}
		final Long num = (Long) cr.uniqueResult();
		return num;
	}

	public static Long getCriteriaForNumDifferentMSRuns(String projectTag, String sampleName, String conditionName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.project", "project").//
				createAlias("project.conditions", "condition");
		if (sampleName != null) {
			cr.createAlias("condition.sample", "sample");
		}
		cr.setProjection(Projections.countDistinct("msRun.runId"));
		if (conditionName != null) {
			cr.add(Restrictions.eq("condition.name", conditionName));
		}
		if (sampleName != null) {
			cr.add(Restrictions.eq("sample.name", sampleName));
		}
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		return (Long) cr.uniqueResult();
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

	private static List<MsRun> getCriteriaForMSRunsInProjectInSample1(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.proteins", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.sample", "sample").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("runId")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("runId").ignoreCase());
		return cr.list();
	}

	private static List<MsRun> getCriteriaForMSRunsInProjectInSample2(String projectTag, String sampleName) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(MsRun.class, "msRun")
				.createAlias("msRun.proteins2", "protein").createAlias("protein.conditions", "condition")
				.createAlias("condition.sample", "sample").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("runId")));
		cr.add(Restrictions.eq("sample.name", sampleName)).add(Restrictions.eq("project.tag", projectTag))
				.addOrder(Order.asc("runId").ignoreCase());
		return cr.list();
	}

	public static List<MsRun> getCriteriaForMSRunsInProjectInSample(String projectTag, String sampleName) {
		return Stream
				.concat(getCriteriaForMSRunsInProjectInSample1(projectTag, sampleName).stream(),
						getCriteriaForMSRunsInProjectInSample2(projectTag, sampleName).stream())
				.distinct().collect(Collectors.toList());
	}

	public static List<String> getCriteriaForConditionsInProjectInMSRun(String projectTag, String msRunID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.proteins", "protein")//
				.createAlias("protein.msRuns", "msRun").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("condition.id")));
		cr.add(Restrictions.eq("msRun.runId", msRunID)).add(Restrictions.eq("project.tag", projectTag));
		return cr.list();
	}

	public static List<String> getCriteriaForSamplesInProjectInMSRun(String projectTag, String msRunID) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Sample.class, "sample")
				.createAlias("sample.conditions", "condition").createAlias("condition.proteins", "protein")
				.createAlias("protein.msRuns", "msRun").createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("sample.id")));

		cr.add(Restrictions.eq("msRun.runId", msRunID)).add(Restrictions.eq("project.tag", projectTag));
		return cr.list();
	}

	public static List<Integer> getCriteriaForConditionIDsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.project", "project")
				.setProjection(Projections.distinct(Projections.property("condition.id")));
		cr.add(Restrictions.eq("project.tag", projectTag));
		return cr.list();
	}

	public static List<Condition> getCriteriaForConditionsInProject(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.project", "project");
		cr.add(Restrictions.eq("project.tag", projectTag));
		return cr.list();
	}

//	public static List<Integer> getPeptideIdsFromProteinIDs(Collection<Integer> proteinIds) {
//
//		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
//				.createAlias("peptide.proteins", "protein").add(Restrictions.in("protein.id", proteinIds))
//				.setProjection(
//						Projections.projectionList().add(Projections.distinct(Projections.property("peptide.id"))));
//		return cr.list();
//
//	}

	public static TIntSet getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(TIntSet proteinIds) {

		return ProteinIDToPeptideIDTableMapper.getInstance().getPeptideIDsFromProteinIDs(proteinIds);

	}

//	public static List<Peptide> getPeptidesFromProteinIDs(Collection<Integer> proteinIds) {
//
//		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
//				.createAlias("peptide.proteins", "protein").add(Restrictions.in("protein.id", proteinIds));
//		return cr.list();
//
//	}

//	public static List<Integer> getPeptidesFromProteinIDs3(Collection<Integer> proteinIds) {
//		final Query query = ContextualSessionHandler.getCurrentSession().createSQLQuery(
//				"Select distinct pp.peptide_id FROM Protein_has_Peptide as pp where pp.protein_id in (:ids)");
//		query.setParameterList("ids", proteinIds);
//		final List<Integer> peptideIds = query.list();
//		return peptideIds;
//	}

//	public static List<Peptide> getPeptidesFromPeptideIDs2(Collection<Integer> peptideIds) {
//		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
//				.add(Restrictions.in("peptide.id", peptideIds));
//		return cr.list();
//	}

//	public static List<Peptide> getPeptidesFromPeptideIDs(Collection<Integer> peptideIds, boolean checkFirstLevelCache,
//			int batchSize) {
//		if (peptideIds instanceof List) {
//			return getPeptidesFromPeptideIDs(peptideIds, checkFirstLevelCache, batchSize);
//		} else {
//			final List<Integer> peptideIDList = new ArrayList<Integer>();
//			peptideIDList.addAll(peptideIds);
//			return getPeptidesFromPeptideIDs(peptideIDList, checkFirstLevelCache, batchSize);
//		}
//	}

	public static List<Peptide> getPeptidesFromPeptideIDs(TIntSet peptideIds, boolean checkFirstLevelCache,
			int batchSize) {
		final MultiIdentifierLoadAccess<Peptide> multiLoadAccess = ContextualSessionHandler.getCurrentSession()
				.byMultipleIds(Peptide.class);

		final List<Integer> asList = new ArrayList<Integer>();
		for (final int id : peptideIds.toArray()) {
			asList.add(id);
		}
		final List<Peptide> ret = multiLoadAccess.withBatchSize(batchSize).enableSessionCheck(checkFirstLevelCache)
				.multiLoad(asList);
		return ret;
	}

	public static List<Psm> getPsmsFromPsmIDs2(Collection<Integer> psmIds) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.add(Restrictions.in("psm.id", psmIds));
		return cr.list();
	}

	public static List<Psm> getPsmsFromPsmIDs(List<Integer> psmIds, boolean checkFirstLevelCache, int batchSize) {
		final MultiIdentifierLoadAccess<Psm> multiLoadAccess = ContextualSessionHandler.getCurrentSession()
				.byMultipleIds(Psm.class);
		final List<Psm> ret = multiLoadAccess.withBatchSize(batchSize).enableSessionCheck(checkFirstLevelCache)
				.multiLoad(psmIds);
		return ret;
	}

	public static List<Protein> getProteinsFromIDs(TIntSet proteinIds, boolean checkFirstLevelCache, int batchSize) {
		final MultiIdentifierLoadAccess<Protein> multiLoadAccess = ContextualSessionHandler.getCurrentSession()
				.byMultipleIds(Protein.class);

		final List<Integer> asList = new ArrayList<Integer>();
		for (final int id : proteinIds.toArray()) {
			asList.add(id);
		}
		final List<Protein> ret = multiLoadAccess.withBatchSize(batchSize).enableSessionCheck(checkFirstLevelCache)
				.multiLoad(asList);
		return ret;
	}

//	public static List<Protein> getProteinsFromIDs2(Collection<Integer> proteinIds) {
//		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
//				.add(Restrictions.in("protein.id", proteinIds));
//		return cr.list();
//	}

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

	public static List<Integer> getPsmIdsFromPeptide(Peptide peptide) {
		final Query query = ContextualSessionHandler.getCurrentSession().getNamedQuery("callGetPSMIDsFromPeptide")
				.setParameter("id", peptide.getId());
		final List<Integer> list = query.list();
		return list;
	}

	public static List<Peptide> getCriteriaForPeptideSequence(String regexp, String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide");
		if (projectTag != null && !"".equals(projectTag)) {
			cr.createAlias("peptide.msRuns", "msRun").createAlias("msRun.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.add(Restrictions.like("peptide.sequence", regexp));
		return cr.list();
	}

	public static List<Integer> getProteinIDsFromCondition(Condition condition) {
		final SQLQuery query = ContextualSessionHandler.getCurrentSession().createSQLQuery(
				"SELECT Protein_id FROM protein_has_condition where protein_has_condition.Condition_id= :condition_id");
		query.setParameter("condition_id", condition.getId());
		return query.list();
	}

	public static List<Integer> getPeptideIDsFromCondition(Condition condition) {
		final SQLQuery query = ContextualSessionHandler.getCurrentSession().createSQLQuery(
				"SELECT Peptide_id FROM peptide_has_condition where peptide_has_condition.Condition_id= :condition_id");
		query.setParameter("condition_id", condition.getId());
		return query.list();
	}

	public static List<Integer> getProteinIDsFromMsRun(MsRun msRun) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		cr.createAlias("protein.msRuns", "msRun");
		cr.add(Restrictions.eq("msRun.id", msRun.getId()));
		cr.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("protein.id"))));
		return cr.list();
	}

	public static List<Integer> getPeptideIDsFromMsRun(MsRun msRun) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide");
		cr.createAlias("peptide.msRuns", "msRuns");
		cr.add(Restrictions.eq("msRuns.id", msRun.getId()));
		cr.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("peptide.id"))));
		return cr.list();
	}

	public static List<RatioDescriptor> getRatioDescriptorsFromCondition(Condition condition) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(RatioDescriptor.class,
				"ratioDescriptor");
		cr.add(Restrictions.or(Restrictions.eq("ratioDescriptor.conditionByExperimentalCondition1Id", condition),
				Restrictions.eq("ratioDescriptor.conditionByExperimentalCondition1Id", condition)));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final List<RatioDescriptor> list = cr.list();
		return list;
	}

	public static List<ProteinRatioValue> getProteinRatioValuesFromRatioDescriptor(RatioDescriptor ratioDescriptor) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(ProteinRatioValue.class,
				"ratio");
		cr.add(Restrictions.eq("ratio.ratioDescriptor", ratioDescriptor));
		final List<ProteinRatioValue> list = cr.list();
		return list;
	}

	public static List<RatioValueWrapper> getProteinRatioValuesWrappersFromRatioDescriptor(
			RatioDescriptor ratioDescriptor) {
		return getRatioValuesWrappersFromRatioDescriptor(ratioDescriptor, "protein_ratio_value", "protein_id");
	}

	public static List<RatioValueWrapper> getPeptideRatioValuesWrappersFromRatioDescriptor(
			RatioDescriptor ratioDescriptor) {
		return getRatioValuesWrappersFromRatioDescriptor(ratioDescriptor, "peptide_ratio_value", "peptide_id");
	}

	public static List<RatioValueWrapper> getPSMRatioValuesWrappersFromRatioDescriptor(
			RatioDescriptor ratioDescriptor) {
		return getRatioValuesWrappersFromRatioDescriptor(ratioDescriptor, "psm_ratio_value", "psm_id");
	}

	private static List<RatioValueWrapper> getRatioValuesWrappersFromRatioDescriptor(RatioDescriptor ratioDescriptor,
			String tableName, String idName) {
		final SQLQuery query = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("SELECT id,confidence_score_type_name," + idName
						+ ",combination_type_name,value,confidence_score_value,confidence_score_name  FROM " + tableName
						+ " where ratio_descriptor_id= :id");
		query.setParameter("id", ratioDescriptor.getId());
		final List<Object[]> lists = query.list();
		final List<RatioValueWrapper> ret = new ArrayList<RatioValueWrapper>();
		for (final Object[] list : lists) {
			final int id = Integer.valueOf(list[0].toString());
			String confidenceScoreType = null;
			if (list[1] != null) {
				confidenceScoreType = list[1].toString();
			}
			final int proteinID = Integer.valueOf(list[2].toString());
			CombinationType combinationType = null;
			if (list[3] != null) {
				combinationType = new CombinationType();
				combinationType.setName(list[3].toString());
			}
			final double value = Double.valueOf(list[4].toString());
			Double confidenceScoreValue = null;
			if (list[5] != null) {
				confidenceScoreValue = Double.valueOf(list[5].toString());
			}
			String confidenceScoreName = null;
			if (list[6] != null) {
				confidenceScoreName = list[6].toString();
			}

			ret.add(new RatioValueWrapper(id, new ConfidenceScoreType(confidenceScoreType), proteinID, combinationType,
					value, confidenceScoreValue, confidenceScoreName, ratioDescriptor.getId()));
		}
		return ret;
	}

	public static List<AmountValueWrapper> getProteinAmountValuesWrappersFromCondition(Condition condition) {
		return getAmountValuesWrappersFromCondition(condition, "protein_amount", "protein_id", ",manual_spc");

	}

	public static List<AmountValueWrapper> getPeptideAmountValuesWrappersFromCondition(Condition condition) {
		return getAmountValuesWrappersFromCondition(condition, "peptide_amount", "peptide_id", "");

	}

	public static List<AmountValueWrapper> getPSMAmountValuesWrappersFromCondition(Condition condition) {
		return getAmountValuesWrappersFromCondition(condition, "psm_amount", "psm_id", "");

	}

	private static List<AmountValueWrapper> getAmountValuesWrappersFromCondition(Condition condition, String tableName,
			String idName, String extraColumn) {
		final SQLQuery query = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("SELECT id,value," + idName + ",amount_type_name,combination_type_name " + extraColumn
						+ " FROM " + tableName + " where condition_id= :id");
		query.setParameter("id", condition.getId());
		final List<Object[]> lists = query.list();
		final List<AmountValueWrapper> ret = new ArrayList<AmountValueWrapper>();
		for (final Object[] list : lists) {
			final int id = Integer.valueOf(list[0].toString());
			final double value = Double.valueOf(list[1].toString());
			final int itemID = Integer.valueOf(list[2].toString());
			AmountType amountType = null;
			if (list[3] != null) {
				amountType = new AmountType();
				amountType.setName(list[3].toString());
			}
			CombinationType combinationType = null;
			if (list[4] != null) {
				combinationType = new CombinationType();
				combinationType.setName(list[4].toString());
			}

			Boolean manualSPC = null;
			if (!"".equals(extraColumn) && list[5] != null) {
				manualSPC = Boolean.valueOf(list[5].toString());
			}

			ret.add(new AmountValueWrapper(id, value, itemID, amountType, combinationType, manualSPC,
					condition.getId()));
		}
		return ret;
	}

	public static List<Protein> getProteinsWithTissues(String projectTag, Set<String> tissueNames) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein")
				.createAlias("protein.conditions", "condition")//
				.createAlias("condition.sample", "sample")//
				.createAlias("sample.tissue", "tissue");//
		LogicalExpression or = null;

		for (final String tissueName : tissueNames) {
			final LogicalExpression newOr = Restrictions.or(Restrictions.eq("tissue.tissueId", tissueName),
					Restrictions.eq("tissue.name", tissueName));
			if (or == null) {
				or = newOr;
			} else {
				or = Restrictions.or(or, newOr);
			}
		}
		cr.add(or);
		if (projectTag != null) {
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return cr.list();
	}

	public static List<Condition> getConditionsByProjectCriteria(String projectTag) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition")
				.createAlias("condition.project", "project");
		cr.add(Restrictions.eq("project.tag", projectTag));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final List<Condition> list = cr.list();
		return list;
	}

	public static List<Integer> getPsmIDsByConditionCriteria(Condition condition) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Psm.class, "psm")
				.createAlias("psm.conditions", "condition");
		cr.add(Restrictions.eq("condition", condition));
		cr.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("psm.id"))));
		final List<Integer> list = cr.list();
		return list;
	}

	public static List<Integer> getPeptideIDsByConditionCriteria(Condition condition) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Peptide.class, "peptide")
				.createAlias("peptide.conditions", "condition");
		cr.add(Restrictions.eq("condition", condition));
		cr.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("peptide.id"))));
		final List<Integer> list = cr.list();
		return list;
	}

	public static List<Condition> getConditionsByIDs(Collection<Integer> ids) {
		final Criteria cr = ContextualSessionHandler.getCurrentSession().createCriteria(Condition.class, "condition");
		cr.add(Restrictions.in("condition.id", ids));
		cr.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		final List<Condition> list = cr.list();
		return list;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the protein
	 * id and the second the peptide id
	 * 
	 * @return
	 */
	public static int[][] getProteinToPeptideTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from protein_has_peptide");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the protein
	 * id and the second the psm id
	 * 
	 * @return
	 */
	public static int[][] getProteinToPSMTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from protein_has_psm");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the protein
	 * id and the second the msRun id
	 * 
	 * @return
	 */
	public static int[][] getProteinToMSRunTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from protein_has_ms_run");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the peptide
	 * id and the second the msRun id
	 * 
	 * @return
	 */
	public static int[][] getPeptideToMSRunTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from peptide_has_ms_run");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the protein
	 * id and the second the condition id
	 * 
	 * @return
	 */
	public static int[][] getProteinToConditionTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from protein_has_condition");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the peptide
	 * id and the second the condition id
	 * 
	 * @return
	 */
	public static int[][] getPeptideToConditionTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from peptide_has_condition");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first, the psm id
	 * and the second the condition id
	 * 
	 * @return
	 */
	public static int[][] getPSMToConditionTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select * from psm_has_condition");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 3 elements, the first, the ratio
	 * descriptor id, the second a condition id and the third a condition id
	 * 
	 * @return
	 */
	public static int[][] getRatioDescriptorsTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession().createSQLQuery(
				"Select id, experimental_condition_1_id, experimental_condition_2_id from ratio_descriptor");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][3];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			ret[i][2] = (int) ids[2];
			i++;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static List<RatioDescriptor> getRatioDescriptorsFromRatioDescriptorIDs(TIntSet ratioDescriptorIDs,
			boolean checkFirstLevelCache, int batchSize) {
		return (List<RatioDescriptor>) getBatchLoadByIDs(RatioDescriptor.class, ratioDescriptorIDs,
				checkFirstLevelCache, batchSize);
	}

	public static List<?> getBatchLoadByIDs(Class<?> clazz, TIntSet ids, boolean checkFirstLevelCache, int batchSize) {
		final MultiIdentifierLoadAccess<?> multiLoadAccess = ContextualSessionHandler.getCurrentSession()
				.byMultipleIds(clazz);

		final List<Integer> asList = new ArrayList<Integer>();
		for (final int id : ids.toArray()) {
			asList.add(id);
		}
		final List<?> ret = multiLoadAccess.withBatchSize(batchSize).enableSessionCheck(checkFirstLevelCache)
				.multiLoad(asList);
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the ratio
	 * descriptor id and the second the protein ratio value ID
	 * 
	 * @return
	 */
	public static int[][] getProteinRatioValuesToRatioDescriptorTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select ratio_descriptor_id, id from protein_ratio_value");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the ratio
	 * descriptor id and the second the peptide ratio value ID
	 * 
	 * @return
	 */
	public static int[][] getPeptideRatioValuesToRatioDescriptorTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select ratio_descriptor_id, id from peptide_ratio_value");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the ratio
	 * descriptor id and the second the psm ratio value ID
	 * 
	 * @return
	 */
	public static int[][] getPsmRatioValuesToRatioDescriptorTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select ratio_descriptor_id, id from psm_ratio_value");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the
	 * condition id and the second the protein amount id
	 * 
	 * @return
	 */
	public static int[][] getProteinAmountsTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select condition_id, id from protein_amount");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the protein
	 * id and the second the threshold id
	 * 
	 * @return
	 */
	public static int[][] getProteinToProteinThresholdTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select protein_id, id from protein_threshold");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the protein
	 * id and the second the protein score id
	 * 
	 * @return
	 */
	public static int[][] getProteinToProteinScoreTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select protein_id, id from protein_score");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the peptide
	 * id and the second the peptide score id
	 * 
	 * @return
	 */
	public static int[][] getPeptideToPeptideScoreTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select peptide_id, id from peptide_score");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the psm id
	 * and the second the psm score id
	 * 
	 * @return
	 */
	public static int[][] getPsmToPsmScoreTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select psm_id, id from psm_score");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

	/**
	 * Gets a matrix in which in each row we have 2 elements, the first the peptide
	 * id and the second the ptm id
	 * 
	 * @return
	 */
	public static int[][] getPeptideToPTMTable() {
		final SQLQuery cr = ContextualSessionHandler.getCurrentSession()
				.createSQLQuery("Select peptide_id, id from ptm where not isnull(peptide_id)");
		final List<Object> list = cr.list();
		final int[][] ret = new int[list.size()][2];
		int i = 0;
		for (final Object object : list) {
			final Object[] ids = (Object[]) object;
			ret[i][0] = (int) ids[0];
			ret[i][1] = (int) ids[1];
			i++;
		}
		return ret;
	}

}
