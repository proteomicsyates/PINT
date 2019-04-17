package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.GenericJDBCException;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.AmountType;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * Prepared queries using a session that if a sessionID is provided (not null)
 * the session will be a session per key. If the sessionID is null, a Contextual
 * session will be provided (the type of the session will be according the the
 * appropiate value of the property 'hibernate.current_session_context_class' in
 * hibernate.cfg.xml file)
 *
 * @author Salva
 *
 */
public class PreparedQueries {
	private final static Logger log = Logger.getLogger(PreparedQueries.class);

	private static Query createQuery(String queryString) {
		// if (sessionID != null)
		// return
		// SessionPerKeyHandler.getSessionPerKey(sessionID).createQuery(queryString);
		return ContextualSessionHandler.createQuery(queryString);
	}

	private final static String PROTEIN_AMOUNTS_BY_PROJECT = "select proteinAmount from "
			+ "ProteinAmount proteinAmount join proteinAmount.condition condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ProteinAmount> getProteinAmounts(String projectTag) {

		return parseParametersForQuery(PROTEIN_AMOUNTS_BY_PROJECT, "project.tag=:projectTag", projectTag).list();
	}

	private final static String PROTEINS_WITH_AMOUNT = "select distinct protein from "
			+ "Protein protein join protein.proteinAmounts proteinAmount " + "join proteinAmount.condition condition "
			+ "join proteinAmount.amountType amountType " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithAmount(String projectTag) {
		final List<Protein> list = parseParametersForQuery(PROTEINS_WITH_AMOUNT, "project.tag=:projectTag", projectTag)
				.list();
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, list);
		return ret;
	}

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithAmount(String projectTag, String conditionName,
			String amountType) {
		final List<Protein> list = parseParametersForQuery(PROTEINS_WITH_AMOUNT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditionName, "amountType.name=:amountType", amountType).list();
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, list);
		return ret;
	}

	private final static String PROTEINS_BY_PROJECT = "select distinct protein from "
			+ "Protein protein join protein.conditions condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsByProjectCondition(String projectTag,
			String conditionName) {
		final List<Protein> list = parseParametersForQuery(PROTEINS_BY_PROJECT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditionName).list();
		log.info("Returning " + list.size() + " proteins from the query");

		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, list);
		return ret;
	}

	private final static String PROJECT_BY_PROTEIN = "select distinct project from "
			+ "Protein protein join protein.conditions condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Project getProjectByProtein(Integer proteinID) {
		final List<Project> list = parseParametersForQuery(PROJECT_BY_PROTEIN, "protein.id=:proteinID", proteinID)
				.list();

		return list.get(0);
	}

	private final static String PSMS_WITH_AMOUNT = "select distinct psm from "
			+ "Psm psm join psm.psmAmounts psmAmount " + "join psmAmount.condition condition "
			+ "join psmAmount.amountType amountType " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Psm> getPSMsWithPSMAmount(String projectTag) {

		return parseParametersForQuery(PSMS_WITH_AMOUNT, "project.tag=:projectTag", projectTag).list();
	}

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Psm> getPSMsWithPSMAmount(String projectTag, String conditionName, String amountTypeString) {

		return parseParametersForQuery(PSMS_WITH_AMOUNT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditionName, "amountType.name=:amountType", amountTypeString).list();
	}

	private final static String PSMS_BY_PROJECT = "select distinct psm from " + "Psm psm join psm.conditions condition "
			+ "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Psm> getPSMsByProjectCondition(String projectTag, String conditioName) {

		return parseParametersForQuery(PSMS_BY_PROJECT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditioName).list();
	}

	private final static String PEPTIDES_BY_PROJECT = "select distinct peptide from "
			+ "Peptide peptide join peptide.conditions condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Peptide> getPeptidesByProjectCondition(String projectTag, String conditioName) {

		return parseParametersForQuery(PEPTIDES_BY_PROJECT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditioName).list();
	}

	private final static String PSM_SCORES_BY_PROJECT = "select distinct score from " + "Project project "
			+ "join project.conditions condition " + "join condition.psms psm " + "join psm.psmScores score";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PsmScore> getPSMScoresByProject(String projectTag) {
		return parseParametersForQuery(PSM_SCORES_BY_PROJECT, "project.tag=:projectTag", projectTag).list();
	}

	private final static String PSM_SCORES = "select distinct name from PsmScore";

	/**
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPSMScoreNames() {
		return parseParametersForQuery(PSM_SCORES).list();
	}

	private final static String PROTEIN_SCORES = "select distinct name from ProteinScore";

	/**
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getProteinScoreNames() {
		return parseParametersForQuery(PROTEIN_SCORES).list();
	}

	private final static String PEPTIDE_SCORES = "select distinct name from PeptideScore";

	/**
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPeptideScoreNames() {
		return parseParametersForQuery(PEPTIDE_SCORES).list();
	}

	private final static String PSM_SCORES_TYPES = "select distinct confidenceScoreType from PsmScore";

	/**
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ConfidenceScoreType> getPSMScoreTypeNames() {
		return parseParametersForQuery(PSM_SCORES_TYPES).list();
	}

	private final static String PSM_SCORE_NAMES = "select distinct score.name from "
			+ "PsmScore score join score.psm psm " + "join psm.conditions condition "
			+ "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPSMScoreNamesByProject(String projectTag) {
		return parseParametersForQuery(PSM_SCORE_NAMES, "project.tag=:projectTag", projectTag).list();
	}

	private final static String PTM_SITES_WITH_SCORES_BY_PROJECT = "select distinct ptm_site from " + "Project project "
			+ "join project.conditions condition " + "join condition.psms psm " + "join psm.ptms ptm "
			+ "join ptm.ptmSites ptm_site " + "join ptm_site.confidenceScoreType score_type";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<PtmSite> getPTMSitesWithScoresByProject(String projectTag) {
		return parseParametersForQuery(PTM_SITES_WITH_SCORES_BY_PROJECT, "project.tag=:projectTag", projectTag).list();
	}

	private final static String PTM_SCORE_NAMES = "select distinct confidenceScoreName from PtmSite";

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getPTMScoreNames() {
		return parseParametersForQuery(PTM_SCORE_NAMES).list();
	}

	private final static String PTM_SCORE_TYPE_NAMES = "select distinct confidenceScoreType from PtmSite";

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<ConfidenceScoreType> getPTMScoreTypeNames() {
		return parseParametersForQuery(PTM_SCORE_TYPE_NAMES).list();
	}

	// TODO : as soon as the threshold had a reference to the project and/or to
	// the condition, this query should be changed

	private final static String PROTEIN_THRESHOLD_NAMES_BY_PROJECT = "select distinct proteinThreshold.name from "
			+ "ProteinThreshold proteinThreshold " + "join proteinThreshold.protein protein "
			+ "join protein.conditions condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getProteinThresholdNamesByProject(String projectTag) {
		return parseParametersForQuery(PROTEIN_THRESHOLD_NAMES_BY_PROJECT, "project.tag=:projectTag", projectTag)
				.list();
	}

	private final static String PROTEIN_THRESHOLD_NAMES = "select distinct name from Threshold";

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getProteinThresholdNames() {
		return parseParametersForQuery(PROTEIN_THRESHOLD_NAMES).list();
	}

	// get PSMs from Protein
	private final static String PSMs_OF_A_PROTEIN = "select protein.psms from Protein protein";

	@SuppressWarnings("unchecked")
	public static List<Psm> getPSMsFromProtein(int proteinID) {
		if (proteinID < 1)
			throw new IllegalArgumentException("Not valid protein identifier");
		return parseParametersForQuery(PSMs_OF_A_PROTEIN, "protein.id=:proteinID", proteinID).list();
	}

	private final static String PROTEINS_WITH_RATIOS = "select distinct protein from "
			+ "RatioDescriptor ratioDescriptor "
			+ "join ratioDescriptor.conditionByExperimentalCondition1Id condition1 "
			+ "join ratioDescriptor.conditionByExperimentalCondition2Id condition2 "
			+ "join condition1.project project1 " + "join condition2.project project2 "
			+ "join ratioDescriptor.proteinRatioValues ratio_values " + "join ratio_values.protein protein";

	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithRatios(String condition1Name, String condition2Name,
			String projectTag, String ratioName) {
		final List<Protein> list = parseParametersForQuery(PROTEINS_WITH_RATIOS, "condition1.name=:condition1",
				condition1Name, "condition2.name=:condition2", condition2Name, "project1.tag=:project1", projectTag,
				"project2.tag=:project2", projectTag, "ratioDescriptor.description=:ratioName", ratioName).list();

		// retrieve also the opposite ratios
		final List<Protein> list2 = parseParametersForQuery(PROTEINS_WITH_RATIOS, "condition1.name=:condition1",
				condition2Name, "condition2.name=:condition2", condition1Name, "project1.tag=:project1", projectTag,
				"project2.tag=:project2", projectTag, "ratioDescriptor.description=:ratioName", ratioName).list();
		return PersistenceUtils.proteinUnion(list, list2);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithRatiosAndScores(String condition1Name,
			String condition2Name, String projectTag, String ratioName, String ratioOperator, Double ratioValue,
			String scoreName, String scoreType, String operator, Double scoreValue) {
		Criteria cr = ContextualSessionHandler.createCriteria(Protein.class, "protein");
		if (projectTag != null) {
			cr.createAlias("protein.conditions", "condition");
			cr.createAlias("condition.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.createAlias("protein.proteinRatioValues", "ratio");
		if (ratioValue != null) {
			cr.add(getRestrictionByOperator(ratioOperator, "ratio.value", ratioValue));
		}
		cr.createAlias("ratio.ratioDescriptor", "ratioDescriptor");
		if (scoreType != null && !"".equals(scoreType)) {
			cr.createAlias("ratio.confidenceScoreType", "scoreType");
			cr.add(Restrictions.eq("scoreType.name", scoreType));
		}
		if (condition1Name != null) {
			cr.createAlias("ratioDescriptor.conditionByExperimentalCondition1Id", "condition1");
			cr.add(Restrictions.eq("condition1.name", condition1Name));
		}
		if (condition2Name != null) {
			cr.createAlias("ratioDescriptor.conditionByExperimentalCondition2Id", "condition2");
			cr.add(Restrictions.eq("condition2.name", condition2Name));
		}
		if (scoreName != null && !"".equals(scoreName)) {
			cr.add(Restrictions.eq("ratio.confidenceScoreName", scoreName));
		}

		if (scoreValue != null) {
			cr.add(getRestrictionByOperator(operator, "ratio.confidenceScoreValue", scoreValue));
		}
		cr.setCacheable(true);
		log.info("getProteinsWithRatiosAndScores: " + cr.toString());
		final List<Protein> list = cr.list();
		cr = ContextualSessionHandler.createCriteria(Protein.class, "protein");
		if (projectTag != null) {
			cr.createAlias("protein.conditions", "condition");
			cr.createAlias("condition.project", "project");
			cr.add(Restrictions.eq("project.tag", projectTag));
		}
		cr.createAlias("protein.proteinRatioValues", "ratio");
		if (ratioValue != null) {
			cr.add(getRestrictionByOperator(ratioOperator, "ratio.value", ratioValue));
		}
		cr.createAlias("ratio.ratioDescriptor", "ratioDescriptor");
		if (scoreType != null && !"".equals(scoreType)) {
			cr.createAlias("ratio.confidenceScoreType", "scoreType");
			cr.add(Restrictions.eq("scoreType.name", scoreType));
		}
		if (condition1Name != null) {
			cr.createAlias("ratioDescriptor.conditionByExperimentalCondition1Id", "condition1");
			cr.add(Restrictions.eq("condition1.name", condition2Name));
		}
		if (condition2Name != null) {
			cr.createAlias("ratioDescriptor.conditionByExperimentalCondition2Id", "condition2");
			cr.add(Restrictions.eq("condition2.name", condition1Name));
		}
		if (scoreName != null && !"".equals(scoreName)) {
			cr.add(Restrictions.eq("ratio.confidenceScoreName", scoreName));
		}
		if (scoreValue != null) {
			cr.add(getRestrictionByOperator(operator, "ratio.confidenceScoreValue", scoreValue));
		}
		log.info("getProteinsWithRatiosAndScores: " + cr.toString());
		cr.setCacheable(true);
		final List<Protein> list2 = cr.list();
		return PersistenceUtils.proteinUnion(list, list2);
	}

	private static Criterion getRestrictionByOperator(String operator, String propertyName, Object value) {
		switch (operator) {
		case "<":
			return Restrictions.lt(propertyName, value);
		case ">":
			return Restrictions.gt(propertyName, value);
		case "=":
			return Restrictions.eq(propertyName, value);
		case "<=":
			return Restrictions.le(propertyName, value);
		case ">=":
			return Restrictions.ge(propertyName, value);
		case "!=":
			return Restrictions.ne(propertyName, value);
		default:
			throw new IllegalArgumentException("Operator '" + operator + "' is not supported");
		}
	}

	private final static String PSM_WITH_RATIOS = "select distinct psm from " + "RatioDescriptor ratioDescriptor "
			+ "join ratioDescriptor.conditionByExperimentalCondition1Id condition1 "
			+ "join ratioDescriptor.conditionByExperimentalCondition2Id condition2 "
			+ "join condition1.project project1 " + "join condition2.project project2 "
			+ "join ratioDescriptor.psmRatioValues ratio_values " + "join ratio_values.psm psm";

	@SuppressWarnings("unchecked")
	public static Set<Psm> getPSMWithRatios(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		final List<Psm> list = parseParametersForQuery(PSM_WITH_RATIOS, "condition1.name=:condition1Name",
				condition1Name, "condition2.name=:condition2Name", condition2Name, "project1.tag=:project1Name",
				projectTag, "project2.tag=:project2Name", projectTag, "ratioDescriptor.description=:ratioName",
				ratioName).list();
		// retrieve also the opposite ratios
		final List<Psm> list2 = parseParametersForQuery(PSM_WITH_RATIOS, "condition1.name=:condition1Name",
				condition2Name, "condition2.name=:condition2Name", condition1Name, "project1.tag=:project1Name",
				projectTag, "project2.tag=:project2Name", projectTag, "ratioDescriptor.description=:ratioName",
				ratioName).list();
		return PersistenceUtils.psmUnion(list, list2);
	}

	private final static String PROTEIN_WITH_PSM_WITH_RATIOS = "select distinct protein from "
			+ "RatioDescriptor ratioDescriptor "
			+ "join ratioDescriptor.conditionByExperimentalCondition1Id condition1 "
			+ "join ratioDescriptor.conditionByExperimentalCondition2Id condition2 "
			+ "join condition1.project project1 " + "join condition2.project project2 "
			+ "join ratioDescriptor.psmRatioValues ratio_values " + "join ratio_values.psm psm "
			+ "join psm.proteins protein";

	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinWithPSMWithRatios(String condition1Name,
			String condition2Name, String projectTag, String ratioName) {
		final List<Protein> list = parseParametersForQuery(PROTEIN_WITH_PSM_WITH_RATIOS, "condition1.name=:condition1",
				condition1Name, "condition2.name=:condition2", condition2Name, "project1.tag=:project1", projectTag,
				"project2.tag=:project2", projectTag, "ratioDescriptor.description=:ratioName", ratioName).list();
		// retrieve also the opposite ratios
		final List<Protein> list2 = parseParametersForQuery(PROTEIN_WITH_PSM_WITH_RATIOS, "condition1.name=:condition1",
				condition2Name, "condition2.name=:condition2", condition1Name, "project1.tag=:project1", projectTag,
				"project2.tag=:project2", projectTag, "ratioDescriptor.description=:ratioName", ratioName).list();
		return PersistenceUtils.proteinUnion(list, list2);
	}

	private final static String PSMS_WITH_SCORES = "select distinct psm from " + "PsmScore psm_score "
			+ "join psm_score.psm psm " + "join psm_score.confidenceScoreType score_type "
			+ "join psm.conditions condition " + "join condition.project project";

	/**
	 *
	 * @param scoreNameString
	 * @param scoreTypeString
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Psm> getPsmsWithScores(String scoreNameString, String scoreTypeString, String projectTag) {
		final Query query = parseParametersForQuery(PSMS_WITH_SCORES, "psm_score.name=:score_name", scoreNameString,
				"score_type.name=:score_type", scoreTypeString, "project.tag=:projectTag", projectTag);
		final List<Psm> list1 = query.list();
		return list1;

	}

	private static final String PSMS_WITH_WITH_PTM_SCORES = "select distinct psm from " + "PtmSite ptm_site "
			+ "join ptm_site.ptm ptm " + "join ptm.psm psm " + "join ptm_site.confidenceScoreType score_type "
			+ "join psm.conditions condition " + "join condition.project project";

	/**
	 *
	 * @param scoreNameString
	 * @param scoreTypeString
	 * @param projectTag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Psm> getPsmsWithPTMScores(String scoreNameString, String scoreTypeString, String projectTag) {
		final Query query2 = parseParametersForQuery(PSMS_WITH_WITH_PTM_SCORES,
				"ptm_site.confidenceScoreName=:score_name", scoreNameString, "score_type.name=:score_type",
				scoreTypeString, "project.tag=:projectTag", projectTag);
		final List<Psm> list2 = query2.list();
		return list2;

	}

	private final static String PROTEINS_WITH_SCORES = "select distinct protein from " + "ProteinScore protein_score "
			+ "join protein_score.protein protein " + "join protein_score.confidenceScoreType score_type "
			+ "join protein.conditions condition " + "join condition.project project";

	/**
	 *
	 * @param scoreNameString
	 * @param scoreTypeString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Protein> getProteinsWithScores(String scoreNameString, String scoreTypeString,
			String projectTag) {
		final Query query = parseParametersForQuery(PROTEINS_WITH_SCORES, "protein_score.name=:score_name",
				scoreNameString, "score_type.name=:score_type", scoreTypeString, "project.tag=:projectTag", projectTag);
		final List<Protein> list1 = query.list();
		return list1;

	}

	/**
	 * If the value of a property is null, it will be skiped
	 *
	 * @param baseQuery
	 * @param strings
	 * @return
	 */
	private static Query parseParametersForQuery(String baseQuery, Object... strings) {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {
				if (!"".equals(sb.toString()))
					sb.append(" and " + property);
				else
					sb.append(" where " + property);
			}
		}
		final String preparedQueryString = baseQuery + sb.toString();
		final Query query = createQuery(preparedQueryString);
		log.info("Prepared query: " + preparedQueryString);
		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {
				log.info("Parameter: " + getPropertyName(property) + "=:" + value);
				query.setParameter(getPropertyName(property), value);
			}
		}
		return query;
	}

	private static Query parseParametersForQuerySpecial(String baseQuery, String name, List<String> accessions,
			Object... strings) {
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb2 = new StringBuilder();
		// make a in ('element1','element2')
		sb2.append(" where ").append(name).append(" in (");
		for (int i = 0; i < accessions.size(); i++) {
			if (i != 0) {
				sb2.append(",");
			}
			final String acc = accessions.get(i);
			sb2.append("'").append(acc).append("'");
		}
		sb2.append(")");

		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {

				sb.append(" and ").append(property);

			}
		}
		sb2.append(sb);

		final String preparedQueryString = baseQuery + sb2.toString();
		final Query query = createQuery(preparedQueryString);
		log.info("Prepared query: " + preparedQueryString);
		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {
				log.info("Parameter: " + getPropertyName(property) + "=:" + value);
				query.setParameter(getPropertyName(property), value);
			}
		}
		return query;
	}

	private static Query parseParametersForQuerySpecial2(String baseQuery, String name, String variable,
			Collection<Object> objects, Object... strings) {
		final StringBuilder sb = new StringBuilder();
		final StringBuilder sb2 = new StringBuilder();
		// make a in ('element1','element2')
		sb2.append(" where ").append(name).append(" in (:").append(variable).append(")");

		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {

				sb.append(" and ").append(property);

			}
		}
		sb2.append(sb);

		final String preparedQueryString = baseQuery + sb2.toString();
		final Query query = createQuery(preparedQueryString);
		query.setParameterList(variable, objects);
		log.info("Prepared query: " + preparedQueryString);
		for (int i = 0; i < strings.length; i = i + 2) {
			final String property = String.valueOf(strings[i]);
			final Object value = strings[i + 1];
			if (value != null && !"".equals(value)) {
				log.info("Parameter: " + getPropertyName(property) + "=:" + value);
				query.setParameter(getPropertyName(property), value);
			}
		}
		return query;
	}

	private static String getPropertyName(String property) {
		return property.substring(property.indexOf("=:") + 2);
	}

	private static final String PROTEINS_WITH_THRESHOLD = "select distinct protein from ProteinThreshold threshold "
			+ "join threshold.protein protein " + "join protein.conditions condition "
			+ "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @param thresholdName
	 * @param pass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithThreshold(String projectTag, String thresholdName,
			boolean pass) {

		final Query query = parseParametersForQuery(PROTEINS_WITH_THRESHOLD, "threshold.passThreshold=:pass", pass,
				"threshold.name=:threshold_name", thresholdName, "project.tag=:project_name", projectTag);
		final List<Protein> list = query.list();
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, list);
		return ret;
	}

	private static final String PROTEINS_WITH_GENES = "select distinct protein from Protein protein join"
			+ " protein.genes gene " + "join protein.conditions condition " + "join condition.project project";

	/**
	 * projectTag can be null for querying over all projects
	 *
	 * @param projectTag
	 * @param geneName
	 * @param pass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Collection<Protein>> getProteinsWithGene(String projectTag, String geneName) {

		final Query query = parseParametersForQuery(PROTEINS_WITH_GENES, "gene.geneId=:gene_name", geneName,
				"project.tag=:project_name", projectTag);
		final List<Protein> list = query.list();
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, list);
		return ret;
	}

	private static final String RATIO_DESCRIPTORS = "select distinct ratio_descriptor from "
			+ "RatioDescriptor ratio_descriptor "
			+ "join ratio_descriptor.conditionByExperimentalCondition2Id condition " + "join condition.project project";

	public static List<RatioDescriptor> getRatioDescriptorsByProject(String projectTag) {

		final Query query = parseParametersForQuery(RATIO_DESCRIPTORS, "project.tag=:project_name", projectTag);
		final List<RatioDescriptor> list = query.list();
		return list;
	}

	public static List<RatioDescriptor> getPSMRatioDescriptorsByProject(String projectTag) {

		final List projectList = ContextualSessionHandler.getCurrentSession().createCriteria(Project.class)
				.add(Restrictions.eq("tag", projectTag)).list();
		if (!projectList.isEmpty()) {
			final Project project = (Project) projectList.get(0);
			final Criteria criteria = ContextualSessionHandler.getCurrentSession()
					.createCriteria(RatioDescriptor.class, "ratioDescriptor")
					.add(Restrictions.isNotEmpty("psmRatioValues"))
					.createAlias("ratioDescriptor.conditionByExperimentalCondition1Id", "condition1")
					.createAlias("ratioDescriptor.conditionByExperimentalCondition2Id", "condition2")
					.add(Restrictions.and(Restrictions.eq("condition1.project", project),
							Restrictions.eqOrIsNull("condition2.project", project)));
			final List<RatioDescriptor> list = criteria.list();
			return list;

		}
		return Collections.emptyList();
	}

	public static List<RatioDescriptor> getProteinRatioDescriptorsByProject(String projectTag) {

		final List projectList = ContextualSessionHandler.getCurrentSession().createCriteria(Project.class)
				.add(Restrictions.eq("tag", projectTag)).list();
		if (!projectList.isEmpty()) {
			final Project project = (Project) projectList.get(0);
			final Criteria criteria = ContextualSessionHandler.getCurrentSession()
					.createCriteria(RatioDescriptor.class, "ratioDescriptor")
					.add(Restrictions.isNotEmpty("proteinRatioValues"))
					.createAlias("ratioDescriptor.conditionByExperimentalCondition1Id", "condition1")
					.createAlias("ratioDescriptor.conditionByExperimentalCondition2Id", "condition2")
					.add(Restrictions.and(Restrictions.eq("condition1.project", project),
							Restrictions.eqOrIsNull("condition2.project", project)));
			final List<RatioDescriptor> list = criteria.list();
			return list;

		}
		return Collections.emptyList();
	}

	public static List<RatioDescriptor> getPeptideRatioDescriptorsByProject(String projectTag) {

		final List projectList = ContextualSessionHandler.getCurrentSession().createCriteria(Project.class)
				.add(Restrictions.eq("tag", projectTag)).list();
		if (!projectList.isEmpty()) {
			final Project project = (Project) projectList.get(0);
			final Criteria criteria = ContextualSessionHandler.getCurrentSession()
					.createCriteria(RatioDescriptor.class, "ratioDescriptor")
					.add(Restrictions.isNotEmpty("peptideRatioValues"))
					.createAlias("ratioDescriptor.conditionByExperimentalCondition1Id", "condition1")
					.createAlias("ratioDescriptor.conditionByExperimentalCondition2Id", "condition2")
					.add(Restrictions.and(Restrictions.eq("condition1.project", project),
							Restrictions.eqOrIsNull("condition2.project", project)));
			try {
				final List<RatioDescriptor> list = criteria.list();
				return list;
			} catch (final GenericJDBCException e) {
				e.printStackTrace();
				throw e;
			}

		}
		return Collections.emptyList();
	}

	private static final String NUM_PROJECTS = "select count(*) from Project project";

	public static int getNumProjects() {
		final Query query = parseParametersForQuery(NUM_PROJECTS, "project.hidden=:hidden", false);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_MSRUNS = "select count(*) from MsRun";

	public static int getNumMSRuns() {
		final Query query = createQuery(NUM_MSRUNS);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_DIFFERENT_PROTEINS = "select count (acc) from ProteinAccession acc";

	public static int getNumDifferentProteins() {
		final Query query = parseParametersForQuery(NUM_DIFFERENT_PROTEINS, "acc.isPrimary=:isPrimary", true);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_DIFFERENT_PEPTIDES = "select count(distinct sequence) from Peptide";

	public static int getNumDifferentPeptides() {
		final Query query = createQuery(NUM_DIFFERENT_PEPTIDES);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_PEPTIDES = "select count(sequence) from Peptide";

	public static int getNumPeptides() {
		final Query query = createQuery(NUM_PEPTIDES);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_DIFFERENT_PSMs = "select count (*) from Psm psm";

	public static int getNumPSMs() {
		final Query query = createQuery(NUM_DIFFERENT_PSMs);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String NUM_CONDITIONS = "select count (*) from Condition condition";

	public static int getNumConditions() {
		final Query query = createQuery(NUM_CONDITIONS);
		final Long num = (Long) query.uniqueResult();
		return num.intValue();
	}

	private static final String ORGANISMS_BY_PROJECT = "select distinct organism from "
			+ "Organism organism join organism.samples sample " + "join sample.conditions condition "
			+ "join condition.project project";

	public static Set<Organism> getOrganismsByProject(String projectTag) {
		final Query query = parseParametersForQuery(ORGANISMS_BY_PROJECT, "project.tag=:project_name", projectTag);
		final Set<Organism> set = new HashSet<Organism>();
		set.addAll(query.list());
		return set;
	}

	private static final String SAMPLES_BY_PROJECT = "select distinct sample from "
			+ "Sample sample join sample.conditions condition " + "join condition.project project";

	public static Set<Sample> getSamplesByProject(String projectTag) {
		final Query query = parseParametersForQuery(SAMPLES_BY_PROJECT, "project.tag=:project_name", projectTag);
		final Set<Sample> set = new HashSet<Sample>();
		set.addAll(query.list());
		return set;
	}

	public static boolean isDownloadDataAllowed(String projectTag) {
		if (projectTag == null || "".equals(projectTag))
			return false;

		// if it is private, don't allow donwload
		final Query query = parseParametersForQuery("select project from Project project", "project.tag=:projectTag",
				projectTag);
		final Project project = (Project) query.list().get(0);
		if (project != null && (project.isPrivate_() || project.isBig()))
			return false;
		return true;
	}

	private static final String PROTEINS_BY_TAXONOMY = "select distinct protein from "
			+ "Protein protein join protein.organism organism " + "join protein.conditions condition "
			+ "join condition.project project";

	public static Map<String, Collection<Protein>> getProteinsWithTaxonomy(String projectTag, String organismName,
			String ncbiTaxID) {
		final Query query = parseParametersForQuery(PROTEINS_BY_TAXONOMY, "project.tag=:project_tag", projectTag,
				"organism.taxonomyId=:taxonomy_id", ncbiTaxID, "organism.name=:organism_name", organismName);
		final List<Protein> proteinList = query.list();
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(ret, proteinList);
		return ret;
	}

	private static final String PSMS_BY_TAXONOMY = "select distinct psm from " + "Psm psm join psm.proteins protein "
			+ "join protein.organism organism " + "join protein.conditions condition "
			+ "join condition.project project";

	public static List<Psm> getPsmsWithTaxonomy(String projectTag, String organismName, String ncbiTaxID) {
		final Query query = parseParametersForQuery(PSMS_BY_TAXONOMY, "project.tag=:project_tag", projectTag,
				"organism.taxonomyId=:taxonomy_id", ncbiTaxID, "organism.name=:organism_name", organismName);
		final List<Psm> psmList = query.list();
		return psmList;
	}

	private final static String PSMS_WITH_LABELED_AMOUNT = "select distinct psm from "
			+ "Psm psm join psm.psmAmounts psmAmount " + "join psmAmount.condition condition "
			+ "join condition.sample sample " + "join sample.label label " + "join condition.project project";

	public static List<Psm> getPSMsWithLabeledAmount(String projectTag, String labelName, Boolean singleton) {
		final Query query = parseParametersForQuery(PSMS_WITH_LABELED_AMOUNT, "project.tag=:project_tag", projectTag,
				"label.name=:label_name", labelName, "psmAmount.singleton=:singleton", singleton);
		final List<Psm> psmList = query.list();
		return psmList;
	}

	private final static String PROTEINS_WITH_LABELED_AMOUNT = "select distinct protein from "
			+ "Protein protein join protein.proteinAmounts proteinAmount " + "join proteinAmount.condition condition "
			+ "join condition.sample sample " + "join sample.label label " + "join condition.project project";

	public static List<Protein> getProteinsWithLabeledAmount(String projectTag, String labelName) {
		final Query query = parseParametersForQuery(PROTEINS_WITH_LABELED_AMOUNT, "project.tag=:project_tag",
				projectTag, "label.name=:label_name", labelName);
		final List<Protein> proteinList = query.list();
		return proteinList;
	}

	private final static String PROTEINS_WITH_ACC = "select distinct protein from "
			+ "ProteinAccession protein_accession " + "join protein_accession.proteins protein "
			+ "join protein.conditions condition " + "join condition.project project";
	private final static String PROTEINS_WITH_ACC_NO_PROJECT = "select distinct protein from "
			+ "ProteinAccession protein_accession " + "join protein_accession.proteins protein ";

	/**
	 *
	 * @param scoreNameString
	 * @param scoreTypeString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Protein> getProteinsWithAccession(String accession, String projectTag) {
		final Query query = parseParametersForQuery(PROTEINS_WITH_ACC, "protein_accession.accession=:accession",
				accession, "project.tag=:projectTag", projectTag);
		final List<Protein> list1 = query.list();
		return list1;

	}

	public static List<Protein> getProteinsWithAccessions(Collection<String> accessions, String projectTag) {
		if (accessions.isEmpty()) {
			return Collections.emptyList();
		}
		String baseQuery = PROTEINS_WITH_ACC;
		if (projectTag == null || "".equals(projectTag)) {
			baseQuery = PROTEINS_WITH_ACC_NO_PROJECT;
		}
		final Set<Object> set = new THashSet<Object>();
		set.addAll(accessions);
		final Query query = parseParametersForQuerySpecial2(baseQuery, "protein_accession.accession", "accession", set,
				"project.tag=:projectTag", projectTag);
		final List<Protein> list1 = query.list();
		return list1;

	}

	private final static String PROTEIN_ACCESSION_BY_TYPE = "select distinct protein_accession from "
			+ "ProteinAccession protein_accession ";

	/**
	 *
	 * @param sessionID
	 * @param accessionType
	 * @return
	 */
	public static List<ProteinAccession> getProteinAccession(String accessionType) {
		final Query query = parseParametersForQuery(PROTEIN_ACCESSION_BY_TYPE,
				"protein_accession.accessionType=:accessionType", accessionType);
		final List<ProteinAccession> list1 = query.list();
		return list1;
	}

	private final static String PROTEINS_WITH_MSRUN = "select distinct protein from " + "MsRun msrun "
			+ "join msrun.proteins protein " + "join protein.conditions condition " + "join condition.project project";

	public static List<Protein> getProteinsWithMSRun(String projectTag, String msRunID) {
		final Query query = parseParametersForQuery(PROTEINS_WITH_MSRUN, "msrun.runId=:runId", msRunID,
				"project.tag=:projectTag", projectTag);
		final List<Protein> list1 = query.list();
		return list1;
	}

	private final static String PSM_AMOUNT_TYPES_BY_CONDITION = "select distinct amountType from AmountType amountType join amountType.psmAmounts psmAmount";
	private final static String PEPTIDE_AMOUNT_TYPES_BY_CONDITION = "select distinct amountType from AmountType amountType join amountType.peptideAmounts peptideAmount";
	private final static String PROTEIN_AMOUNT_TYPES_BY_CONDITION = "select distinct amountType from AmountType amountType join amountType.proteinAmounts proteinAmount";

	public static Map<Condition, Set<AmountType>> getPSMAmountTypesByConditions(Collection<Condition> conditions) {
		final Map<Condition, Set<AmountType>> map = new THashMap<Condition, Set<AmountType>>();
		for (final Condition condition : conditions) {
			// PSM AMOUNTS
			final Set<Object> set = new THashSet<Object>();
			set.add(condition);
			final Query query = parseParametersForQuerySpecial2(PSM_AMOUNT_TYPES_BY_CONDITION, "psmAmount.condition",
					"condition", set);
			query.setCacheable(true);
			final List<AmountType> list = query.list();
			if (!list.isEmpty()) {
				if (map.containsKey(condition)) {
					map.get(condition).addAll(list);
				} else {
					final Set<AmountType> set2 = new THashSet<AmountType>();
					set2.addAll(list);
					map.put(condition, set2);
				}
			}
		}
		return map;
	}

	public static Map<Condition, Set<AmountType>> getPeptideAmountTypesByConditions(Collection<Condition> conditions) {
		final Map<Condition, Set<AmountType>> map = new THashMap<Condition, Set<AmountType>>();
		for (final Condition condition : conditions) {
			// PEPTIDE AMOUNTS
			final Set<Object> set = new THashSet<Object>();
			set.add(condition);
			final Query query = parseParametersForQuerySpecial2(PEPTIDE_AMOUNT_TYPES_BY_CONDITION,
					"peptideAmount.condition", "condition", set);
			query.setCacheable(true);
			final List<AmountType> list = query.list();
			if (!list.isEmpty()) {
				if (map.containsKey(condition)) {
					map.get(condition).addAll(list);
				} else {
					final Set<AmountType> set2 = new THashSet<AmountType>();
					set2.addAll(list);
					map.put(condition, set2);
				}
			}

		}
		return map;
	}

	public static Map<Condition, Set<AmountType>> getProteinAmountTypesByConditions(Collection<Condition> conditions) {
		final Map<Condition, Set<AmountType>> map = new THashMap<Condition, Set<AmountType>>();
		for (final Condition condition : conditions) {
			// PROTEIN AMOUNTS
			final Set<Object> set = new THashSet<Object>();
			set.add(condition);
			final Query query = parseParametersForQuerySpecial2(PROTEIN_AMOUNT_TYPES_BY_CONDITION,
					"proteinAmount.condition", "condition", set);
			query.setCacheable(true);
			final List<AmountType> list = query.list();
			if (!list.isEmpty()) {
				if (map.containsKey(condition)) {
					map.get(condition).addAll(list);
				} else {
					final Set<AmountType> set2 = new THashSet<AmountType>();
					set2.addAll(list);
					map.put(condition, set2);
				}
			}
		}
		return map;
	}

	private final static String PSMS_WITH_MSRUN = "select distinct psm from " + "MsRun msrun " + "join msrun.psms psm "
			+ "join psm.conditions condition " + "join condition.project project";

	public static List<Psm> getPsmsWithMSRun(String projectTag, String msRunID) {
		final Query query = parseParametersForQuery(PSMS_WITH_MSRUN, "msrun.runId=:runId", msRunID,
				"project.tag=:projectTag", projectTag);
		final List<Psm> list1 = query.list();
		return list1;
	}

	private final static String PEPTIDES_WITH_MSRUN = "select distinct peptide from " + "MsRun msrun "
			+ "join msrun.peptides peptide " + "join peptide.conditions condition " + "join condition.project project";

	public static List<Peptide> getPeptidesWithMSRun(String projectTag, String msRunID) {
		final Query query = parseParametersForQuery(PEPTIDES_WITH_MSRUN, "msrun.runId=:runId", msRunID,
				"project.tag=:projectTag", projectTag);
		final List<Peptide> list1 = query.list();
		return list1;
	}

	private final static String MSRUNS_BY_PROJECT = "select distinct msrun from " + "MsRun msrun "
			+ "join msrun.project project";

	public static List<MsRun> getMSRunsByProject(String projectTag) {
		final Query query = parseParametersForQuery(MSRUNS_BY_PROJECT, "project.tag=:projectTag", projectTag);
		final List<MsRun> list1 = query.list();
		return list1;
	}

	private final static String PSM_WITH_PTM = "select distinct psm from " + "Project project "
			+ "join project.conditions condition " + "join condition.psms psm " + "join psm.ptms ptm ";

	@SuppressWarnings("unchecked")
	public static List<Psm> getPSMsContainingPTM(String ptmName, String projectTag) {
		return parseParametersForQuery(PSM_WITH_PTM, "ptm.name=:name", ptmName, "project.tag=:projectTag", projectTag)
				.list();
	}

	private final static String PEPTIDES_WITH_AMOUNT = "select distinct peptide from "
			+ "Peptide peptide join peptide.peptideAmounts peptideAmount " + "join peptideAmount.condition condition "
			+ "join peptideAmount.amountType amountType " + "join condition.project project";

	public static List<Peptide> getPeptidesWithPeptideAmount(String projectTag, String conditionName,
			String amountTypeString) {
		return parseParametersForQuery(PEPTIDES_WITH_AMOUNT, "project.tag=:projectTag", projectTag,
				"condition.name=:conditionName", conditionName, "amountType.name=:amountType", amountTypeString).list();
	}

	private final static String PEPTIDES_WITH_LABELED_AMOUNT = "select distinct peptide from "
			+ "Peptide peptide join peptide.peptideAmounts peptideAmount " + "join peptideAmount.condition condition "
			+ "join condition.sample sample " + "join sample.label label " + "join condition.project project";

	public static List<Peptide> getPeptidesWithLabeledAmount(String projectTag, String labelName, Boolean singleton) {
		final Query query = parseParametersForQuery(PEPTIDES_WITH_LABELED_AMOUNT, "project.tag=:project_tag",
				projectTag, "label.name=:label_name", labelName, "peptideAmount.singleton=:singleton", singleton);
		final List<Peptide> peptideList = query.list();
		return peptideList;
	}

	private final static String PEPTIDES_WITH_RATIOS = "select distinct peptide from "
			+ "RatioDescriptor ratioDescriptor "
			+ "join ratioDescriptor.conditionByExperimentalCondition1Id condition1 "
			+ "join ratioDescriptor.conditionByExperimentalCondition2Id condition2 "
			+ "join condition1.project project1 " + "join condition2.project project2 "
			+ "join ratioDescriptor.peptideRatioValues ratio_values " + "join ratio_values.peptide peptide";

	public static Set<Peptide> getPeptidesWithRatios(String condition1Name, String condition2Name, String projectTag,
			String ratioName) {
		final List<Peptide> list = parseParametersForQuery(PEPTIDES_WITH_RATIOS, "condition1.name=:condition1Name",
				condition1Name, "condition2.name=:condition2Name", condition2Name, "project1.tag=:project1Name",
				projectTag, "project2.tag=:project2Name", projectTag, "ratioDescriptor.description=:ratioName",
				ratioName).list();
		// retrieve also the opposite ratios
		final List<Peptide> list2 = parseParametersForQuery(PEPTIDES_WITH_RATIOS, "condition1.name=:condition1Name",
				condition2Name, "condition2.name=:condition2Name", condition1Name, "project1.tag=:project1Name",
				projectTag, "project2.tag=:project2Name", projectTag, "ratioDescriptor.description=:ratioName",
				ratioName).list();
		return PersistenceUtils.peptideUnion(list, list2);
	}

	private final static String PEPTIDES_WITH_SCORES = "select distinct peptide from " + "PeptideScore peptide_score "
			+ "join peptide_score.peptide peptide " + "join peptide_score.confidenceScoreType score_type "
			+ "join peptide.conditions condition " + "join condition.project project";

	public static List<Peptide> getPeptidesWithScores(String scoreNameString, String scoreTypeString,
			String projectTag) {
		final Query query = parseParametersForQuery(PEPTIDES_WITH_SCORES, "peptide_score.name=:score_name",
				scoreNameString, "score_type.name=:score_type", scoreTypeString, "project.tag=:projectTag", projectTag);
		final List<Peptide> list1 = query.list();
		return list1;
	}

	private static final String PEPTIDES_WITH_WITH_PTM_SCORES = "select distinct peptide from " + "PtmSite ptm_site "
			+ "join ptm_site.ptm ptm " + "join ptm.peptide peptide " + "join ptm_site.confidenceScoreType score_type "
			+ "join peptide.conditions condition " + "join condition.project project";

	public static List<Peptide> getPeptidesWithPTMScores(String scoreNameString, String scoreTypeString,
			String projectTag) {
		final Query query2 = parseParametersForQuery(PEPTIDES_WITH_WITH_PTM_SCORES,
				"ptm_site.confidenceScoreName=:score_name", scoreNameString, "score_type.name=:score_type",
				scoreTypeString, "project.tag=:projectTag", projectTag);
		final List<Peptide> list2 = query2.list();
		return list2;
	}

	private final static String PEPTIDE_WITH_PTM = "select distinct peptide from " + "Project project "
			+ "join project.conditions condition " + "join condition.peptides peptide " + "join peptide.ptms ptm ";

	public static List<Peptide> getPeptidesContainingPTM(String ptmName, String projectTag) {
		return parseParametersForQuery(PEPTIDE_WITH_PTM, "ptm.name=:name", ptmName, "project.tag=:projectTag",
				projectTag).list();
	}

	private static final String PEPTIDES_BY_TAXONOMY = "select distinct peptide from "
			+ "Peptide peptide join peptide.proteins protein " + "join protein.organism organism "
			+ "join protein.conditions condition " + "join condition.project project";

	public static List<Peptide> getPeptidesWithTaxonomy(String projectTag, String organismName, String ncbiTaxID) {
		final Query query = parseParametersForQuery(PEPTIDES_BY_TAXONOMY, "project.tag=:project_tag", projectTag,
				"organism.taxonomyId=:taxonomy_id", ncbiTaxID, "organism.name=:organism_name", organismName);
		final List<Peptide> peptideList = query.list();
		return peptideList;
	}

}
