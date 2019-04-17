package edu.scripps.yates.persistence.mysql.preparedstatements;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import gnu.trove.list.array.TLongArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import junit.framework.Assert;

public class PreparedCriteriaTests {
	@Before
	public void beforeTask() {
		ContextualSessionHandler.beginGoodTransaction();
		ProteinIDToPeptideIDTableMapper.getInstance();
	}

	@After
	public void afterTask() {
		try {
			ContextualSessionHandler.finishGoodTransaction();
			ContextualSessionHandler.closeSession();
		} catch (final Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
		}
	}

	@Test
	public void getNumPSMs() {
		final String projectTag = "_CFTR_";
		final String runID = "_24h_050710";
		final String sampleName = "_24h_30C_14h_37C";
		final String conditionName = "_24h_30C_14h_37C";
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPSMs(projectTag, null, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPSMs(null, runID, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPSMs(null, null, sampleName, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPSMs(null, null, null, conditionName));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPSMs(null, null, null, null));
	}

	@Test
	public void getNumPeptides() {
		final String projectTag = "_CFTR_";
		final String runID = "_24h_050710";
		final String sampleName = "_24h_30C_14h_37C";
		final String conditionName = "_24h_30C_14h_37C";
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPeptides(projectTag, null, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPeptides(null, runID, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPeptides(null, null, sampleName, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPeptides(null, null, null, conditionName));
		System.out.println(PreparedQueries.getNumDifferentPeptides());
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentPeptides(null, null, null, null));
	}

	@Test
	public void getNumProteins() {
		final String projectTag = "_CFTR_";
		final String runID = "_24h_050710";
		final String sampleName = "_24h_30C_14h_37C";
		final String conditionName = "_24h_30C_14h_37C";
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentProteins(projectTag, null, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentProteins(null, runID, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentProteins(null, null, sampleName, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentProteins(null, null, null, conditionName));
		System.out.println(PreparedQueries.getNumDifferentProteins());
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentProteins(null, null, null, null));
	}

	@Test
	public void getNumMSRuns() {
		final String projectTag = "_CFTR_";
		final String sampleName = "_24h_30C_14h_37C";
		final String conditionName = "_24h_30C_14h_37C";
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentMSRuns(projectTag, null, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentMSRuns(null, sampleName, null));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentMSRuns(null, null, conditionName));
		System.out.println(PreparedCriteria.getCriteriaForNumDifferentMSRuns(null, null, null));
	}

	@Test
	public void getProteinsByCondition() {
		final long t1 = System.currentTimeMillis();

		// proteins by project
		final int size = 500;
		final String conditionName = "C in vivo";
		final CriterionSet criterionSet = PreparedCriteria.getProteinsByProjectConditionCriteria(null, conditionName);
		final Criteria criteria = PreparedCriteria.getCriteriaByProteinAcc(0, size, "mw", false, criterionSet);
		final List<Protein> list = criteria.list();
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements in list");
		for (final Protein protein : list) {
			System.out.println(protein.getMw());
		}
		Assert.assertTrue(list.size() >= size);
		final Map<String, Collection<Protein>> map = new THashMap<String, Collection<Protein>>();
		PersistenceUtils.addToMapByPrimaryAcc(map, list);

		Assert.assertEquals(size, map.size());
		final Set<Psm> psmSet = new THashSet<Psm>();
		for (final String acc : map.keySet()) {
			final Collection<Protein> proteinSet = map.get(acc);
			for (final Protein protein : proteinSet) {

				final Set<Condition> conditions1 = protein.getConditions();
				boolean found = false;
				for (final Condition condition : conditions1) {
					if (condition.getName().equals(conditionName)) {
						found = true;
					}
				}
				Assert.assertTrue(found);
				final Set<Psm> psms = protein.getPsms();
				System.out.println(psms.size() + " PSMs in Protein");
				psmSet.addAll(psms);
				for (final Psm psm : psms) {
					found = false;
					final Set<Condition> conditions = psm.getConditions();
					for (final Condition condition2 : conditions) {
						if (condition2.getName().equals(conditionName)) {
							found = true;
						}
					}
					Assert.assertTrue(found);
					final Set<Protein> proteins = psm.getProteins();
					System.out.println(proteins.size() + " proteins in PSM");
					for (final Protein protein2 : proteins) {
						final Set<Condition> conditions2 = protein2.getConditions();
						found = false;
						for (final Condition condition2 : conditions2) {
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
		final long t2 = System.currentTimeMillis();

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
		final Criteria c2 = c1.createCriteria("protein.psms", "psm");
		c2.add(Restrictions.like("psm.sequence", "A%"));

		final List<Protein> proteinList = c1.list();
		for (final Protein protein : proteinList) {
			System.out.println(protein.getAcc());
			final Set<Condition> conditions = protein.getConditions();
			for (final Condition condition : conditions) {
				System.out.print(condition.getName() + "\t");
			}
			System.out.println();
		}
	}

	@Test
	public void testingAccs2ByProject() {
		final Long num = PreparedCriteria.getCriteriaForNumDifferentProteins("_CFTR_", null, null, null);

		System.out.println(num);
		final List<String> list2 = PreparedCriteria.getCriteriaForProteinPrimaryAccs("_CFTR_", null, null, null);

		int i = 0;
		for (final Object object : list2) {
			System.out.println(++i + "\t" + object);
		}

	}

	@Test
	public void testingGetMaxRatio() {
		final String condition1Name = "ad_pre_ctx_N14";
		final String condition2Name = "ad_pre_ctx_N15";
		final String projectTag = "Alzh_tinny";
		final String ratioName = "AREA_RATIO";
		System.out.println(-Double.MAX_VALUE);
		final double max = (double) PreparedCriteria
				.getCriteriaForPsmRatioMaximumValue(condition1Name, condition2Name, projectTag, ratioName)
				.uniqueResult();

		System.out.println("max = " + max);

	}

	@Test
	public void testingGetMinRatio() {
		final String condition1Name = "ad_pre_ctx_N14";
		final String condition2Name = "ad_pre_ctx_N15";
		final String projectTag = "Alzh_tinny";
		final String ratioName = "AREA_RATIO";
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
		final String regexp = "LGSGSAAP[P|Y]AASAELTXER";

		final String mySQLRegularExpression = getMySQLRegularExpression(regexp);
		final List<Psm> psms = PreparedCriteria.getCriteriaForPsmSequence(mySQLRegularExpression, null);

		for (final Psm psm : psms) {
			System.out.println(psm.getSequence());

		}
		System.out.println(mySQLRegularExpression);
		Assert.assertFalse(psms.isEmpty());

	}

	@Test
	public void testingGetConditionsByMSRun() {
		final List<Project> projects = ContextualSessionHandler.createCriteria(Project.class).list();
		for (final Project project : projects) {
			final List<MsRun> msRunsByProject = PreparedQueries.getMSRunsByProject(project.getTag());
			for (final MsRun msRun : msRunsByProject) {
				final List<Condition> conditions = PreparedCriteria.getConditionsByMSRunCriteria(msRun);
				System.out.println("MSRun " + msRun.getRunId() + " is linked with the following conditions:");
				for (final Condition condition : conditions) {
					System.out.println("\t" + condition.getName());
				}
			}
		}
	}

	@Test
	public void testingPeptidesByProject() {
		final Long cr = PreparedCriteria.getCriteriaForNumDifferentPeptides("fmr_silam", null, null, null);

		System.out.println(cr);
	}

	@Test
	public void testingPSMByProject() {

		final Long cr = PreparedCriteria.getCriteriaForNumDifferentPSMs("fmr_silam", null, null, null);
		System.out.println(cr);

	}

	@Test
	public void testGetPeptideIdsFromProteinIds() {
		final String conditionName = "C in vivo";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null,
				conditionName);
		final TIntSet ids = new TIntHashSet();
		for (final Collection<Protein> protein : list.values()) {
			for (final Protein protein2 : protein) {
				ids.add(protein2.getId());
			}
		}

		System.out.println("Getting peptides from " + ids.size() + " proteins");
		long t1 = System.currentTimeMillis();
		final TIntSet peptideIds = PreparedCriteria.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(ids);
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
		System.out.println(peptideIds.size() + " peptide IDs with first method");

//		t1 = System.currentTimeMillis();
//		final List<Peptide> peptides = PreparedCriteria.getPeptidesByIds(peptideIds);
//		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
//		System.out.println(peptides.size() + " peptides objects");

//		t1 = System.currentTimeMillis();
//		final List<Peptide> peptides = PreparedCriteria.getPeptideFromPeptideIDs(peptideIds);
//		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
//		System.out.println(peptides.size() + " peptides objects");
//
//		t1 = System.currentTimeMillis();
//		final List<Peptide> peptides = PreparedCriteria.getPeptideFromPeptideIDs2(peptideIds);
//		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
//		System.out.println(peptides.size() + " peptides objects");
//
//		t1 = System.currentTimeMillis();
//		final List<Peptide> peptides = PreparedCriteria.getPeptideFromPeptideIDs3(peptideIds,true,2500);
//		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
//		System.out.println(peptides.size() + " peptides objects");

		final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIds, true, 500);
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
		System.out.println(peptides.size() + " peptides objects");
		t1 = System.currentTimeMillis();
		Set<String> seqs = peptides.stream().map(peptide -> peptide.getFullSequence()).collect(Collectors.toSet());
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
		System.out.println(seqs.size() + " different sequences");

		t1 = System.currentTimeMillis();
		seqs = peptides.parallelStream().map(peptide -> peptide.getFullSequence()).collect(Collectors.toSet());
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
		System.out.println(seqs.size() + " different sequences in parallel");
	}

	@Test
	public void testGetPeptideIdsFromProteins() {
		final String conditionName = "C in vivo";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null,
				conditionName);
		final Set<Peptide> peptides = new THashSet<Peptide>();
		final long t1 = System.currentTimeMillis();
		for (final Collection<Protein> protein : list.values()) {
			for (final Protein protein2 : protein) {
				peptides.addAll(protein2.getPeptides());
			}
		}
		System.out.println(peptides.size() + " peptides objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetPsmIdsFromProteinIds() {
		final String conditionName = "C in vivo";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null,
				conditionName);
		final HashSet<Integer> ids = new HashSet<Integer>();
		for (final Collection<Protein> protein : list.values()) {
			for (final Protein protein2 : protein) {
				ids.add(protein2.getId());
			}
		}
		final long t1 = System.currentTimeMillis();
		System.out.println("Getting peptides from " + ids.size() + " proteins");
		final List<Integer> psmIds = PreparedCriteria.getPsmIdsFromProteins(ids);
		final TIntHashSet set = new TIntHashSet();
		set.addAll(psmIds);
		System.out.println(psmIds.size() + " psms");
		System.out.println(set.size() + " different psms");
		final List<Psm> psms = PreparedCriteria.getPsmsFromPsmIDs(psmIds, true, 500);
		System.out.println(psms.size() + " psms objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void testGetPsmIdsFromProteins() {
		final String conditionName = "C in vivo";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null,
				conditionName);
		final Set<Psm> psms = new THashSet<Psm>();
		final long t1 = System.currentTimeMillis();
		for (final Collection<Protein> protein : list.values()) {
			for (final Protein protein2 : protein) {
				psms.addAll(protein2.getPsms());
			}
		}
		System.out.println(psms.size() + " psms objects");
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(System.currentTimeMillis() - t1));
	}

	@Test
	public void getCriteriaForConditionsInProjectInMSRunTest() {
		final String projectTag = "_CFTR_";
		final String msRunID = "_24h_050710";
		final List<String> conditionNames = PreparedCriteria.getCriteriaForConditionsInProjectInMSRun(projectTag,
				msRunID);
		System.out.println(conditionNames.size());
	}

	@Test
	public void getCriteriaForConditionsInProjectTest() {
		final long t1 = System.currentTimeMillis();
		final String projectTag = "_CFTR_";
		final List<Condition> conditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag);
		final long x = System.currentTimeMillis() - t1;

		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(x));
		System.out.println(conditions.size());
	}

	@Test
	public void getCriteriaForGettingPeptidesFromProteinIDs() {

		final TLongArrayList times = new TLongArrayList();
		final String projectTag = "Islet Aging";
		int num = 0;
		final List<Condition> conditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag);
		for (final Condition condition : conditions) {
			final List<Integer> proteinIDsByConditionCriteria = PreparedCriteria.getProteinIDsFromCondition(condition);
			final TIntSet intSet = new TIntHashSet(proteinIDsByConditionCriteria);
			final List<Protein> proteins = PreparedCriteria.getProteinsFromIDs(intSet, true, 20);
			final Map<String, Collection<Protein>> map = new THashMap<String, Collection<Protein>>();
			PersistenceUtils.addToMapByPrimaryAcc(map, proteins);
			final ProgressCounter counter = new ProgressCounter(map.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0,
					true);
			for (final String acc : map.keySet()) {
				counter.increment();
				final String printIfNecessary = counter.printIfNecessary();
				if (!"".equals(printIfNecessary)) {
					System.out.println(printIfNecessary);
				}
				final Collection<Protein> proteinSet = map.get(acc);
				final Set<Integer> proteinIDSet = proteinSet.stream().map(protein -> protein.getId())
						.collect(Collectors.toSet());
				final TIntSet tintSet = new TIntHashSet(proteinIDSet);
				final long t1 = System.currentTimeMillis();
				num++;
//				final List<Peptide> peptides = PreparedCriteria.getPeptidesFromProteinIDs(proteinIDSet);
//				final List<Integer> peptideIDs = PreparedCriteria.getPeptideIdsFromProteinIDs(proteinIDSet);
				final TIntSet peptideIDs = PreparedCriteria
						.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(tintSet);
				final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIDs, true, 100);
				for (final Peptide peptide : peptides) {
					final int id = peptide.getId();
				}
				final long x = System.currentTimeMillis() - t1;
				times.add(x);
//				System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(times.sum() / (1.0 * num)));
			}
			break;
		}
		final double y = times.sum() / (1.0 * num);
		System.out.println(y);
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(y));

	}

	@Test
	public void getCriteriaForGettingPeptidesFromProteinIDs2() {

		final TLongArrayList times = new TLongArrayList();
		final String projectTag = "Islet Aging";
		int num = 0;
		final List<Condition> conditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag);
		for (final Condition condition : conditions) {
			final List<Integer> proteinIDsByConditionCriteria = PreparedCriteria.getProteinIDsFromCondition(condition);
			final TIntSet intSet = new TIntHashSet(proteinIDsByConditionCriteria);
			final List<Protein> proteins = PreparedCriteria.getProteinsFromIDs(intSet, true, 20);
			final Map<String, Collection<Protein>> map = new THashMap<String, Collection<Protein>>();
			PersistenceUtils.addToMapByPrimaryAcc(map, proteins);
			final ProgressCounter counter = new ProgressCounter(map.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0,
					true);
			for (final String acc : map.keySet()) {
				counter.increment();
				final String printIfNecessary = counter.printIfNecessary();
				if (!"".equals(printIfNecessary)) {
					System.out.println(printIfNecessary);
				}
				final Collection<Protein> proteinSet = map.get(acc);
				final Set<Integer> proteinIDSet = proteinSet.stream().map(protein -> protein.getId())
						.collect(Collectors.toSet());
				final TIntSet tintSet = new TIntHashSet(proteinIDSet);

				num++;
//				final List<Peptide> peptides = PreparedCriteria.getPeptidesFromProteinIDs(proteinIDSet);
//				final List<Integer> peptideIDs = PreparedCriteria.getPeptideIdsFromProteinIDs(proteinIDSet);
				final TIntSet peptideIDs = PreparedCriteria
						.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(tintSet);
				final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIDs, true, 100);
				final long t1 = System.currentTimeMillis();
				final Map<String, List<Protein>> proteinMap = PersistenceUtils
						.getProteinMapUsingProteinToPeptideMappingTable(peptides, intSet);
				for (final String acc2 : proteinMap.keySet()) {
					final List<Protein> proteinSet2 = proteinMap.get(acc2);
					for (final Protein protein2 : proteinSet2) {
						protein2.getId();
					}
				}
				final long x = System.currentTimeMillis() - t1;
				times.add(x);
//				System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(times.sum() / (1.0 * num)));
			}
			break;
		}
		final double y = times.sum() / (1.0 * num);
		System.out.println(y);
		System.out.println(DatesUtil.getDescriptiveTimeFromMillisecs(y));

	}
}
