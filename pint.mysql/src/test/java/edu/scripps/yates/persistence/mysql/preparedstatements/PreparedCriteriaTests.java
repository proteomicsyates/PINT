package edu.scripps.yates.persistence.mysql.preparedstatements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.HibernateUtil;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.CriterionSet;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.dates.DatesUtil;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import junit.framework.Assert;

public class PreparedCriteriaTests {
	@Before
	public void beforeTask() {
		ContextualSessionHandler.openSession();
		ContextualSessionHandler.beginGoodTransaction();
	}

	@After
	public void afterTask() {
		try {
			ContextualSessionHandler.finishGoodTransaction();
			ContextualSessionHandler.closeSession();
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
		}
	}

	@Test
	public void getProteinsByCondition() {
		long t1 = System.currentTimeMillis();

		// proteins by project
		int size = 500;
		final String conditionName = "C in vivo";
		CriterionSet criterionSet = PreparedCriteria.getProteinsByProjectConditionCriteria(null, conditionName);
		Criteria criteria = PreparedCriteria.getCriteriaByProteinAcc(0, size, "mw", false, criterionSet);
		final List<Protein> list = criteria.list();
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements in list");
		for (Protein protein : list) {
			System.out.println(protein.getMw());
		}
		Assert.assertTrue(list.size() >= size);
		Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(map, list);

		Assert.assertEquals(size, map.size());
		Set<Psm> psmSet = new THashSet<Psm>();
		for (String acc : map.keySet()) {
			final Set<Protein> proteinSet = map.get(acc);
			for (Protein protein : proteinSet) {

				final Set<Condition> conditions1 = protein.getConditions();
				boolean found = false;
				for (Condition condition : conditions1) {
					if (condition.getName().equals(conditionName)) {
						found = true;
					}
				}
				Assert.assertTrue(found);
				final Set<Psm> psms = protein.getPsms();
				System.out.println(psms.size() + " PSMs in Protein");
				psmSet.addAll(psms);
				for (Psm psm : psms) {
					found = false;
					final Set<Condition> conditions = psm.getConditions();
					for (Condition condition2 : conditions) {
						if (condition2.getName().equals(conditionName)) {
							found = true;
						}
					}
					Assert.assertTrue(found);
					final Set<Protein> proteins = psm.getProteins();
					System.out.println(proteins.size() + " proteins in PSM");
					for (Protein protein2 : proteins) {
						final Set<Condition> conditions2 = protein2.getConditions();
						found = false;
						for (Condition condition2 : conditions2) {
							if (condition2.getName().equals(conditionName)) {
								found = true;
							}
						}
						Assert.assertTrue(found);
					}
				}
			}
		}
		System.out.println(psmSet.size() + " psms in " + map.size() + " proteins");
		final Statistics statistics = HibernateUtil
				.getInstance("salvador", "natjeija", "jdbc:mysql://localhost:3306/interactome_db").getSessionFactory()
				.getStatistics();
		printQueryStats(statistics);
		long t2 = System.currentTimeMillis();

		System.out.println(t2 - t1 + " ms");
	}

	private void printQueryStats(Statistics statistics) {
		System.out.println();
		final String[] queries = statistics.getQueries();
		final long queryExecutionCount = queries.length;
		final long queryExecutionMaxTime = statistics.getQueryExecutionMaxTime();
		final String queryExecutionMaxTimeQueryString = statistics.getQueryExecutionMaxTimeQueryString();
		System.out.println(statistics);
		System.out.println("Num queries run: " + queryExecutionCount);
		// System.out.println("Slowest query: " +
		// queryExecutionMaxTimeQueryString);
		System.out.println("Slowest query taking: " + queryExecutionMaxTime + " ms");

	}

	@Test
	public void testingCriterias() {
		final Criteria c1 = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class, "protein");
		c1.add(Restrictions.like("acc", "P0%"));
		Criteria c2 = c1.createCriteria("protein.psms", "psm");
		c2.add(Restrictions.like("psm.sequence", "A%"));

		final List<Protein> proteinList = c1.list();
		for (Protein protein : proteinList) {
			System.out.println(protein.getAcc());
			final Set<Condition> conditions = protein.getConditions();
			for (Condition condition : conditions) {
				System.out.print(condition.getName() + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void testingGetMaxRatio() {
		String condition1Name = "ad_pre_ctx_N14";
		String condition2Name = "ad_pre_ctx_N15";
		String projectTag = "Alzh_tinny";
		String ratioName = "AREA_RATIO";
		System.out.println(-Double.MAX_VALUE);
		final double max = (double) PreparedCriteria
				.getCriteriaForPsmRatioMaximumValue(condition1Name, condition2Name, projectTag, ratioName)
				.uniqueResult();

		System.out.println("max = " + max);

	}

	@Test
	public void testingGetMinRatio() {
		String condition1Name = "ad_pre_ctx_N14";
		String condition2Name = "ad_pre_ctx_N15";
		String projectTag = "Alzh_tinny";
		String ratioName = "AREA_RATIO";
		System.out.println(-Double.MAX_VALUE);

		final double min = (double) PreparedCriteria
				.getCriteriaForPsmRatioMinimumValue(condition1Name, condition2Name, projectTag, ratioName)
				.uniqueResult();

		System.out.println("min=" + min);

	}

	private String getMySQLRegularExpression(String regularExpression) {
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

			} else if (regularExpression.charAt(i) == 'X') {
				ret += "%";
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

	@Test
	public void testingGetPeptidesBySeqRegexp() {
		String regexp = "LGSGSAAP[P|Y]AASAELTXER";

		final String mySQLRegularExpression = getMySQLRegularExpression(regexp);
		final List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySQLRegularExpression, null).list();
		for (Psm psm : psms) {
			System.out.println(psm.getSequence());

		}
		System.out.println(mySQLRegularExpression);
		Assert.assertFalse(psms.isEmpty());

	}

	@Test
	public void testingGetConditionsByMSRun() {
		List<Project> projects = ContextualSessionHandler.createCriteria(Project.class).list();
		for (Project project : projects) {
			final List<MsRun> msRunsByProject = PreparedQueries.getMSRunsByProject(project.getTag());
			for (MsRun msRun : msRunsByProject) {
				final List<Condition> conditions = PreparedCriteria.getConditionsByMSRunCriteria(msRun).list();
				System.out.println("MSRun " + msRun.getRunId() + " is linked with the following conditions:");
				for (Condition condition : conditions) {
					System.out.println("\t" + condition.getName());
				}
			}
		}
	}

	@Test
	public void testGetPeptideIdsFromProteinIds() {
		final String conditionName = "C in vivo";
		final Map<String, Set<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null, conditionName);
		HashSet<Integer> ids = new HashSet<Integer>();
		for (Set<Protein> protein : list.values()) {
			for (Protein protein2 : protein) {
				ids.add(protein2.getId());
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Getting peptides from " + ids.size() + " proteins");
		List<Integer> peptideIds = PreparedCriteria.getPeptideIdsFromProteins(ids);
		System.out.println(peptideIds.size() + " peptides");
		List<Peptide> peptides = PreparedCriteria.getPeptidesByIds(peptideIds);
		System.out.println(peptides.size() + " peptides objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetPeptideIdsFromProteins() {
		final String conditionName = "C in vivo";
		final Map<String, Set<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null, conditionName);
		Set<Peptide> peptides = new THashSet<Peptide>();
		long t1 = System.currentTimeMillis();
		for (Set<Protein> protein : list.values()) {
			for (Protein protein2 : protein) {
				peptides.addAll(protein2.getPeptides());
			}
		}
		System.out.println(peptides.size() + " peptides objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetPsmIdsFromProteinIds() {
		final String conditionName = "C in vivo";
		final Map<String, Set<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null, conditionName);
		HashSet<Integer> ids = new HashSet<Integer>();
		for (Set<Protein> protein : list.values()) {
			for (Protein protein2 : protein) {
				ids.add(protein2.getId());
			}
		}
		long t1 = System.currentTimeMillis();
		System.out.println("Getting peptides from " + ids.size() + " proteins");
		List<Integer> psmIds = PreparedCriteria.getPsmIdsFromProteins(ids);
		TIntHashSet set = new TIntHashSet();
		set.addAll(psmIds);
		System.out.println(psmIds.size() + " psms");
		System.out.println(set.size() + " different psms");
		List<Psm> psms = PreparedCriteria.getPsmsByIds(psmIds);
		System.out.println(psms.size() + " psms objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetPsmIdsFromProteins() {
		final String conditionName = "C in vivo";
		final Map<String, Set<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null, conditionName);
		Set<Psm> psms = new THashSet<Psm>();
		long t1 = System.currentTimeMillis();
		for (Set<Protein> protein : list.values()) {
			for (Protein protein2 : protein) {
				psms.addAll(protein2.getPsms());
			}
		}
		System.out.println(psms.size() + " psms objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetTissuesFromProtein() {
		Collection<String> accs = new THashSet<String>();
		accs.add("Q01726");
		Set<String> tissueNames = new THashSet<String>();
		tissueNames.add("brain tissue");
		tissueNames.add("Embryos");
		List<Protein> proteinsWithTissues = PreparedCriteria.getProteinsWithTissues(null, tissueNames);

		System.out.println(proteinsWithTissues.size() + " proteins");
	}
}
