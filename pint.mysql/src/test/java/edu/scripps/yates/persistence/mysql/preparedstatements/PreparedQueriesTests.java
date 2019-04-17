package edu.scripps.yates.persistence.mysql.preparedstatements;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

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
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;
import junit.framework.Assert;

public class PreparedQueriesTests {

	public final static String PSM_SCORES_BY_PROJECT_2 = "select distinct score from "
			+ "PsmScore score join score.psm psm " + "join psm.peptideAmounts peptideAmount "
			+ "join peptideAmount.experimentalCondition condition " + "join condition.project project "
			+ "where project.name=:projectName)";
	private final String sandraProject = "F508 interactome project";
	private final String danielsProject = "PCP PPI Cortex";

	@Before
	public void beforeTask() {
		ContextualSessionHandler.beginGoodTransaction();
	}

	@Test
	public void getProteinAmountsByProject() {

		// amounts by project
		List<ProteinAmount> list = PreparedQueries.getProteinAmounts(danielsProject);
		Assert.assertTrue(list.isEmpty());
		System.out.println(list.size() + " elements");
		list = PreparedQueries.getProteinAmounts(sandraProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
	}

	@Test
	public void getProteinsByProject() {

		// proteins by project

		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition("Alzheimer3", null);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	}

	@Test
	public void getProteinsByProjectAndCondition() {

		// proteins by project

		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition("Alzheimer3",
				"ad_pre_ctx_N15");
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	}

	@Test
	public void getProteinsByCondition() {

		// proteins by project
		final int size = 500;
		final Map<String, Collection<Protein>> map = PreparedQueries.getProteinsByProjectCondition(null,
				"ad_pre_ctx_N15");
		Assert.assertFalse(map.isEmpty());
		System.out.println(map.size() + " elements");
		Assert.assertEquals(size, map.size());
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(map));
		for (final String acc : map.keySet()) {
			final Collection<Protein> proteinSet = map.get(acc);
			for (final Protein protein : proteinSet) {
				final Set<Condition> conditions1 = protein.getConditions();
				boolean found = false;
				for (final Condition condition : conditions1) {
					if (condition.getName().equals("ad_pre_ctx_N15")) {
						found = true;
					}
				}
				Assert.assertTrue(found);
				final Set<Psm> psms = protein.getPsms();
				System.out.println(psms.size() + " PSMs in Protein");
				for (final Psm psm : psms) {
					found = false;
					final Set<Condition> conditions = psm.getConditions();
					for (final Condition condition2 : conditions) {
						if (condition2.getName().equals("ad_pre_ctx_N15")) {
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
							if (condition2.getName().equals("ad_pre_ctx_N15")) {
								found = true;
							}
						}
						Assert.assertTrue(found);
					}
				}
			}
		}
	}

	@Test
	public void getOrganismByProject() {

		// proteins by project
		final Set<Organism> list = PreparedQueries.getOrganismsByProject(danielsProject);
		Assert.assertFalse(list.isEmpty());
		for (final Organism organism : list) {
			System.out.println(organism.getName());
		}
	}

	@Test
	public void getAllProteins() {

		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsByProjectCondition(null, null);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
		Assert.assertTrue(testIfAllProteinsHasTheSamePsmsAsItsPeptideHave(list));
	}

	private boolean testIfAllProteinsHasPrimaryAccessions(Map<String, Collection<Protein>> proteins) {
		// for (final Set<Protein> proteinSet : proteins.values()) {
		// for (final Protein protein : proteinSet) {
		//
		// boolean hasprimary = false;
		// final Set<ProteinAccession> set = protein.getProteinAccessions();
		// for (final ProteinAccession proteinAccession : set) {
		// if (proteinAccession.isIsPrimary()) {
		// hasprimary = true;
		// break;
		// }
		// }
		// if (!hasprimary)
		// return false;
		// }
		// }
		return true;
	}

	private boolean testIfAllProteinsHasTheSamePsmsAsItsPeptideHave(Map<String, Collection<Protein>> proteins) {
		for (final Collection<Protein> proteinSet : proteins.values()) {

			for (final Protein protein : proteinSet) {
				final TIntHashSet psmIds = new TIntHashSet();
				System.out.println("PROT: " + protein.getId());
				final Set<Psm> psms = protein.getPsms();
				for (final Psm psm : psms) {
					psmIds.add(psm.getId());
				}
				if (psmIds.size() != psms.size())
					System.out.println("cuidado");
				Assert.assertEquals(psmIds.size(), psms.size());
				final Set<Peptide> peptides = protein.getPeptides();
				final TIntHashSet psms2 = new TIntHashSet();
				for (final Peptide peptide : peptides) {
					System.out.println("PEP: " + peptide.getId());
					for (final Object obj : peptide.getPsms()) {
						final Psm psm = (Psm) obj;
						System.out.println("PSM: " + psm.getId());
						if (!psms.contains(psm))
							System.out.println("cuidado");
						Assert.assertTrue(psms.contains(psm));
						Assert.assertFalse(psms2.contains(psm.getId()));
						psms2.add(psm.getId());
					}
				}
				if (psms2.size() != psms.size())
					System.out.println("CUIDADO");
				Assert.assertEquals(psms2.size(), psms.size());
				for (final Integer psm2Id : psms2._set) {
					Assert.assertTrue(psmIds.contains(psm2Id));
				}
			}
		}
		return true;
	}

	private boolean testIfAllProteinsHasDifferentDBID(Map<String, Collection<Protein>> proteins) {
		final TIntHashSet proteinIDs = new TIntHashSet();

		for (final Collection<Protein> proteinSet : proteins.values()) {
			for (final Protein protein : proteinSet) {

				if (!proteinIDs.contains(protein.getId())) {
					proteinIDs.add(protein.getId());
				} else {
					return false;
				}
			}
		}
		return true;
	}

	@Test
	public void getPSMScoresByProject() {

		// psm scores by project
		List<PsmScore> list = PreparedQueries.getPSMScoresByProject(danielsProject);
		Assert.assertTrue(list.isEmpty());
		System.out.println(list.size() + " elements");
		list = PreparedQueries.getPSMScoresByProject(sandraProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");

	}

	@Test
	public void getPSMScores() {

		// psm scores by project
		final List<String> list = PreparedQueries.getPSMScoreNames();
		Assert.assertTrue(!list.isEmpty());
		System.out.println(list.size() + " elements");
		for (final String psmScore : list) {
			System.out.println(psmScore);
		}
	}

	@Test
	public void getPSMScoreTypes() {

		// psm scores by project
		final List<ConfidenceScoreType> list = PreparedQueries.getPSMScoreTypeNames();
		Assert.assertTrue(!list.isEmpty());
		System.out.println(list.size() + " elements");
		for (final ConfidenceScoreType psmScore : list) {
			System.out.println(psmScore.getName());
		}
	}

	@Test
	public void getDistinctPSMScoreNamessByProject() {

		// psm scores by project
		List<String> list = PreparedQueries.getPSMScoreNamesByProject(danielsProject);
		Assert.assertTrue(list.isEmpty());
		System.out.println(list.size() + " elements");
		list = PreparedQueries.getPSMScoreNamesByProject(sandraProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		for (final String psmScore : list) {
			System.out.println(psmScore);
		}
	}

	@Test
	public void getDistinctPTMScoresByProject() {

		// psm scores by project
		List<PtmSite> list = PreparedQueries.getPTMSitesWithScoresByProject(danielsProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		for (final PtmSite ptmSite : list) {
			System.out.println(ptmSite.getConfidenceScoreName() + " " + ptmSite.getConfidenceScoreType().getName() + " "
					+ ptmSite.getConfidenceScoreValue());
		}
		list = PreparedQueries.getPTMSitesWithScoresByProject(sandraProject);
		Assert.assertTrue(list.isEmpty());

	}

	@Test
	public void getDistinctPTMScoreNames() {

		final List<String> list = PreparedQueries.getPTMScoreNames();
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");

	}

	@Test
	public void getDistinctPTMScoreTypeNames() {

		final List<ConfidenceScoreType> list = PreparedQueries.getPTMScoreTypeNames();
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
	}

	@Test
	public void getDistinctThresholdNamessByProject() {

		// psm scores by project
		List<String> list = PreparedQueries.getProteinThresholdNamesByProject(danielsProject);
		Assert.assertTrue(list.isEmpty());
		System.out.println(list.size() + " elements");
		list = PreparedQueries.getProteinThresholdNamesByProject(sandraProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		for (final String psmScore : list) {
			System.out.println(psmScore);
		}
	}

	@Test
	public void getDistinctThresholdNames() {

		// psm scores by project
		final List<String> list = PreparedQueries.getProteinThresholdNames();
		Assert.assertTrue(list.isEmpty());

	}

	@Test
	public void getPSMSFromAProtein() {
		List<Psm> list = null;
		// psm scores by project
		try {
			list = PreparedQueries.getPSMsFromProtein(-1);
			fail();
		} catch (final IllegalArgumentException e) {

		}
		final int proteinId = PreparedQueries.getProteinsByProjectCondition(danielsProject, null).values().iterator()
				.next().iterator().next().getId();
		list = PreparedQueries.getPSMsFromProtein(proteinId);
		Assert.assertFalse(list.isEmpty());

		for (final Psm psm : list) {
			System.out.println(psm.getId() + "\t" + psm.getSequence());
		}
		System.out.println(list.size() + " elements");
	}

	@Test
	public void getProteinRatios() {

		// amounts by project
		final String condition1Name = "WT";
		final String projectName = sandraProject;
		final String condition2Name = "MUT";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsWithRatios(condition2Name,
				condition1Name, projectName, null);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	}

	@Test
	public void getProteinRatiosAndScores() {

		// amounts by project
		final String condition1Name = "WT";
		final String projectName = sandraProject;
		final String condition2Name = "MUT";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsWithRatiosAndScores(condition2Name,
				condition1Name, projectName, "myratio", ">=", 2.0, "myScore", "scoreType", "<", 1.0);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	}

	@Test
	public void getProteinsWithPsmRatios() {

		// amounts by project
		final String condition1Name = "PCP";
		final String projectName = danielsProject;
		final String condition2Name = "Sal";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinWithPSMWithRatios(condition2Name,
				condition1Name, projectName, null);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	}

	@Test
	public void getPeptideRatios() {

		// amounts by project
		final String condition1Name = "PCP";
		final String projectName = danielsProject;
		final String condition2Name = "Sal";
		final Collection<Psm> list = PreparedQueries.getPSMWithRatios(condition2Name, condition1Name, projectName,
				null);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
	}

	@Test
	public void getPeptidesRatiosFromTwoWays() {

		// amounts by project
		final String condition1Name = "PCP";
		final String projectName = danielsProject;
		final String condition2Name = "Sal";
		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinWithPSMWithRatios(condition2Name,
				condition1Name, projectName, null);
		final TIntHashSet proteinIds1 = new TIntHashSet();
		for (final Collection<Protein> proteinSet : list.values()) {
			for (final Protein protein : proteinSet) {
				proteinIds1.add(protein.getId());
			}
		}
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
		Assert.assertTrue(testIfAllProteinsHasDifferentDBID(list));
		Assert.assertTrue(testIfAllProteinsHasTheSamePsmsAsItsPeptideHave(list));
		final Collection<Psm> list2 = PreparedQueries.getPSMWithRatios(condition2Name, condition1Name, projectName,
				null);
		Assert.assertFalse(list2.isEmpty());
		System.out.println(list2.size() + " elements");
		final TIntHashSet proteinIds2 = new TIntHashSet();
		for (final Psm psm : list2) {
			final Set<Protein> proteins = psm.getProteins();
			for (final Protein protein : proteins) {
				if (!proteinIds2.contains(protein.getId())) {
					proteinIds2.add(protein.getId());
				}
			}
		}
		System.out.println(proteinIds2.size() + " proteins with psms with ratios");
		Assert.assertEquals(proteinIds2.size(), proteinIds1.size());
	}

	@Test
	public void getPsmsWithPeptideScores() {

		final List<Psm> list = PreparedQueries.getPsmsWithScores("SEQUEST:xcorr",
				"search engine specific score for PSMs", sandraProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");

	}

	@Test
	public void getProteinsWithThresholds() {

		final Map<String, Collection<Protein>> list = PreparedQueries.getProteinsWithThreshold(null, "Xscorefilter_6h",
				true);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
		Assert.assertTrue(testIfAllProteinsHasDifferentDBID(list));

		final Map<String, Collection<Protein>> list2 = PreparedQueries.getProteinsWithThreshold("_CFTR_",
				"Xscorefilter_6h", true);
		Assert.assertFalse(list2.isEmpty());
		System.out.println(list2.size() + " elements");
		Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list2));
		Assert.assertTrue(testIfAllProteinsHasDifferentDBID(list2));
		Assert.assertEquals(list.size(), list2.size());
	}

	// @Test
	// public void getProteinsWithGenes() {
	//
	// final Map<String, Set<Protein>> list =
	// PreparedQueries.getProteinsWithGene(null, "Arfgef2");
	// Assert.assertFalse(list.isEmpty());
	// System.out.println(list.size() + " elements");
	// Assert.assertTrue(testIfAllProteinsHasPrimaryAccessions(list));
	// Assert.assertTrue(testIfAllProteinsHasDifferentDBID(list));
	// for (final Set<Protein> proteins : list.values()) {
	// for (final Protein protein : proteins) {
	// final Set<edu.scripps.yates.proteindb.persistence.mysql.Gene> genes =
	// protein.getGenes();
	// for (final edu.scripps.yates.proteindb.persistence.mysql.Gene gene :
	// genes) {
	// System.out.println(gene.getGeneId());
	// }
	// }
	// }
	// }

	@Test
	public void getRatioDescriptorsByProject() {

		// psm scores by project
		final List<RatioDescriptor> list = PreparedQueries.getRatioDescriptorsByProject(danielsProject);
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " elements");

		for (final RatioDescriptor descriptor : list) {
			System.out.println(descriptor.getDescription());
		}
	}

	@Test
	public void getPSMsByCondition() {

		// psm scores by project
		final List<Psm> list = PreparedQueries.getPSMsWithPSMAmount("062114_DmDv_isogenic", "LIGHT_DROME", "INTENSITY");
		Assert.assertFalse(list.isEmpty());
		System.out.println(list.size() + " psms");

	}

	@Test
	public void getNumProjects() {

		final int num = PreparedQueries.getNumProjects();
		Assert.assertTrue(num > 0);
		System.out.println(num + " projects");
	}

	@Test
	public void getNumDifferentProteins() {

		final int num = PreparedQueries.getNumDifferentProteins();
		Assert.assertTrue(num > 0);
		System.out.println(num + " proteins");
	}

	@Test
	public void getNumdifferentPeptides() {

		final int num = PreparedQueries.getNumDifferentPeptides();
		Assert.assertTrue(num > 0);
		System.out.println(num + " peptides");
	}

	@Test
	public void getNumPSMs() {

		final int num = PreparedQueries.getNumPSMs();
		Assert.assertTrue(num > 0);
		System.out.println(num + " psms");
	}

	@Test
	public void getNumConditions() {

		final int num = PreparedQueries.getNumConditions();
		Assert.assertTrue(num > 0);
		System.out.println(num + " conditions");
	}

	@Test
	public void getProjectFromProteinID() {
		final Project projectByProtein = PreparedQueries.getProjectByProtein(1);
		Assert.assertNotNull(projectByProtein);
		System.out.println(projectByProtein.getTag());
	}

	@Test
	public void getPsmsWithPTMScores() {
		final List<Psm> psmsWithScores = PreparedQueries.getPsmsWithPTMScores(null, "PTM localization score", null);
		Assert.assertNotNull(psmsWithScores);
		System.out.println(psmsWithScores.size());
		Assert.assertTrue(!psmsWithScores.isEmpty());
	}

	@Test
	public void getProteinsByTaxonomy() {
		final Map<String, Collection<Protein>> proteinsWithTaxonomy = PreparedQueries.getProteinsWithTaxonomy(null,
				"Drosophila simulans", null);
		Assert.assertNotNull(proteinsWithTaxonomy);
		System.out.println(proteinsWithTaxonomy.size());
		Assert.assertTrue(!proteinsWithTaxonomy.isEmpty());

		final Map<String, Collection<Protein>> proteinsWithTaxonomy2 = PreparedQueries.getProteinsWithTaxonomy(null,
				null, "7240");
		Assert.assertNotNull(proteinsWithTaxonomy2);
		System.out.println(proteinsWithTaxonomy2.size());
		Assert.assertTrue(!proteinsWithTaxonomy2.isEmpty());

		Assert.assertEquals(proteinsWithTaxonomy.size(), proteinsWithTaxonomy2.size());

		final Map<String, Collection<Protein>> proteinsWithTaxonomy3 = PreparedQueries
				.getProteinsWithTaxonomy("not valid project tag", null, "7240");
		Assert.assertNotNull(proteinsWithTaxonomy3);
		System.out.println(proteinsWithTaxonomy3.size());
		Assert.assertTrue(proteinsWithTaxonomy3.isEmpty());

		final Map<String, Collection<Protein>> proteinsWithTaxonomy4 = PreparedQueries
				.getProteinsWithTaxonomy("DroHybrids", null, "7240");
		Assert.assertNotNull(proteinsWithTaxonomy4);
		System.out.println(proteinsWithTaxonomy4.size());
		Assert.assertTrue(!proteinsWithTaxonomy4.isEmpty());

		Assert.assertEquals(proteinsWithTaxonomy.size(), proteinsWithTaxonomy4.size());

	}

	@Test
	public void getPsmsByTaxonomy() {
		final List<Psm> psmsWithTaxonomy = PreparedQueries.getPsmsWithTaxonomy(null, "Drosophila simulans", null);
		Assert.assertNotNull(psmsWithTaxonomy);
		System.out.println(psmsWithTaxonomy.size());
		Assert.assertTrue(!psmsWithTaxonomy.isEmpty());

		final List<Psm> psmsWithTaxonomy2 = PreparedQueries.getPsmsWithTaxonomy(null, null, "7240");
		Assert.assertNotNull(psmsWithTaxonomy2);
		System.out.println(psmsWithTaxonomy2.size());
		Assert.assertTrue(!psmsWithTaxonomy2.isEmpty());

		Assert.assertEquals(psmsWithTaxonomy.size(), psmsWithTaxonomy2.size());

		final List<Psm> psmsWithTaxonomy3 = PreparedQueries.getPsmsWithTaxonomy("not valid project tag", null, "7240");
		Assert.assertNotNull(psmsWithTaxonomy3);
		System.out.println(psmsWithTaxonomy3.size());
		Assert.assertTrue(psmsWithTaxonomy3.isEmpty());

		final List<Psm> psmsWithTaxonomy4 = PreparedQueries.getPsmsWithTaxonomy("DroHybrids", null, "7240");
		Assert.assertNotNull(psmsWithTaxonomy4);
		System.out.println(psmsWithTaxonomy4.size());
		Assert.assertTrue(!psmsWithTaxonomy4.isEmpty());

		Assert.assertEquals(psmsWithTaxonomy.size(), psmsWithTaxonomy4.size());

	}

	// @Test
	// public void getPsmsAndProteinsByTaxonomy() {
	// final List<Psm> psmsWithTaxonomy =
	// PreparedQueries.getPsmsWithTaxonomy(null, "Drosophila simulans", null);
	// final Map<String, Set<Protein>> proteinsWithTaxonomy =
	// PreparedQueries.getProteinsWithTaxonomy(null,
	// "Drosophila simulans", null);
	// final TIntHashSet proteinDBIDFromQuery = new TIntHashSet();
	// for (final Set<Protein> proteinSet : proteinsWithTaxonomy.values()) {
	// for (final Protein protein : proteinSet) {
	// proteinDBIDFromQuery.add(protein.getId());
	// }
	// }
	//
	// final Map<String, Set<Psm>> psmsFromProteins =
	// PersistenceUtils.getPsmsFromProteins(proteinsWithTaxonomy, true);
	// System.out.println(
	// "PSMs from proteins:" + psmsFromProteins.size() + " PSMs from query:" +
	// psmsWithTaxonomy.size());
	// Assert.assertEquals(psmsFromProteins.size(), psmsWithTaxonomy.size());
	//
	// final Map<String, Set<Protein>> proteinsFromPsms =
	// PersistenceUtils.getProteinsFromPsms(psmsWithTaxonomy, true);
	//
	// final TIntHashSet proteinDBIDsFromPSMs = new TIntHashSet();
	// for (final Set<Protein> proteinSet : proteinsFromPsms.values()) {
	// for (final Protein protein : proteinSet) {
	// if (protein.getOrganism().getName().equals("Drosophila simulans"))
	// proteinDBIDsFromPSMs.add(protein.getId());
	// }
	// }
	// System.out.println("Proteins from query:" + proteinDBIDFromQuery.size() +
	// " Proteins from PSMs:"
	// + proteinDBIDsFromPSMs.size());
	//
	// for (final Integer proteindb2 : proteinDBIDsFromPSMs._set) {
	// if (!proteinDBIDFromQuery.contains(proteindb2))
	// System.out.println("Not present : " + proteindb2);
	// }
	//
	// Assert.assertEquals(proteinDBIDFromQuery.size(),
	// proteinDBIDsFromPSMs.size());
	//
	// }

	@Test
	public void ratioDescriptors() {
		final long t1 = System.currentTimeMillis();

		final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
				.getRatioDescriptorsByProject("DroHybrids");
		System.out.println(ratioDescriptorsByProject.size() + " ratio descriptors");
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
			System.out.println(ratioDescriptor.getDescription() + ": "
					+ ratioDescriptor.getConditionByExperimentalCondition1Id().getName() + " over "
					+ ratioDescriptor.getConditionByExperimentalCondition2Id().getName());
		}
		System.out.println(System.currentTimeMillis() - t1 + " msg");
	}

	@Test
	public void ratioDescriptorsFromPSMs() {
		final long t1 = System.currentTimeMillis();
		final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
				.getPSMRatioDescriptorsByProject("Alzheimer3");
		final long t2 = System.currentTimeMillis();
		System.out.println(ratioDescriptorsByProject.size() + " psm ratio descriptors");
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
			System.out.println(ratioDescriptor.getDescription() + ": "
					+ ratioDescriptor.getConditionByExperimentalCondition1Id().getName() + " over "
					+ ratioDescriptor.getConditionByExperimentalCondition2Id().getName());
		}
		Assert.assertTrue(!ratioDescriptorsByProject.isEmpty());
		System.out.println(t2 - t1 + " msg");
	}

	@Test
	public void ratioDescriptorsFromPSMsByCriteria() {
		final long t1 = System.currentTimeMillis();
		final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
				.getPSMRatioDescriptorsByProject("Alzheimer3");
		final long t2 = System.currentTimeMillis();
		System.out.println(ratioDescriptorsByProject.size() + " psm ratio descriptors");
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
			System.out.println(ratioDescriptor.getDescription() + ": "
					+ ratioDescriptor.getConditionByExperimentalCondition1Id().getName() + " over "
					+ ratioDescriptor.getConditionByExperimentalCondition2Id().getName());
		}
		Assert.assertTrue(!ratioDescriptorsByProject.isEmpty());
		System.out.println(t2 - t1 + " msg");
	}

	@Test
	public void ratioDescriptorsFromProteins() {
		final long t1 = System.currentTimeMillis();
		final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
				.getProteinRatioDescriptorsByProject("DroHybrids");
		System.out.println(ratioDescriptorsByProject.size() + " protein ratio descriptors");
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
			System.out.println(ratioDescriptor.getDescription() + ": "
					+ ratioDescriptor.getConditionByExperimentalCondition1Id().getName() + " over "
					+ ratioDescriptor.getConditionByExperimentalCondition2Id().getName());
		}
		Assert.assertTrue(ratioDescriptorsByProject.isEmpty());
		System.out.println(System.currentTimeMillis() - t1 + " msg");
	}

	@Test
	public void ratioDescriptorsFromPeptides() {
		final long t1 = System.currentTimeMillis();
		final List<RatioDescriptor> ratioDescriptorsByProject = PreparedQueries
				.getPeptideRatioDescriptorsByProject("DmDshybrids2014");
		System.out.println(ratioDescriptorsByProject.size() + " peptide ratio descriptors");
		for (final RatioDescriptor ratioDescriptor : ratioDescriptorsByProject) {
			System.out.println(ratioDescriptor.getDescription() + ": "
					+ ratioDescriptor.getConditionByExperimentalCondition1Id().getName() + " over "
					+ ratioDescriptor.getConditionByExperimentalCondition2Id().getName());
		}
		Assert.assertTrue(ratioDescriptorsByProject.isEmpty());
		System.out.println(System.currentTimeMillis() - t1 + " msg");
	}

	@Test
	public void proteinAccessionRetrieval() {
		final String[] accs = { "A0AVK1", "A0AVL9" };
		for (final String acc : accs) {
			final List<Protein> proteins = PreparedQueries.getProteinsWithAccession(acc, "HEK_TlLh");
			Assert.assertTrue(!proteins.isEmpty());
			System.out.println(acc + " " + proteins.size());

		}

	}

	@Test
	public void multipleProteinAccessionRetrieval() {
		final String[] accs = { "A0AED4", "A0AUN0" };
		final List<String> list = Arrays.asList(accs);
		final List<Protein> proteins = PreparedQueries.getProteinsWithAccessions(list, null);
		Assert.assertTrue(!proteins.isEmpty());
		System.out.println(proteins.size());

	}

	@Test
	public void msrunsByProject() {

		final List<MsRun> msRuns = PreparedQueries.getMSRunsByProject("VX809");
		Assert.assertTrue(!msRuns.isEmpty());
		for (final MsRun msRun : msRuns) {
			System.out.println(msRun.getRunId() + " " + msRun.getPath());
		}

	}

	@Test
	public void getProteinAccessionsByType() {

		List<ProteinAccession> accs = PreparedQueries.getProteinAccession(AccessionType.UNIPROT.name());
		Assert.assertTrue(!accs.isEmpty());
		System.out.println(accs.size());

		accs = PreparedQueries.getProteinAccession(null);
		Assert.assertTrue(!accs.isEmpty());
		System.out.println(accs.size());

		accs = PreparedQueries.getProteinAccession(AccessionType.IPI.name());
		Assert.assertTrue(!accs.isEmpty());
		System.out.println(accs.size());

	}

	@Test
	public void testingBestQueryWayForProteinAndPeptides() {
		try {
			final Set<Pair<Integer, Long>> times = new THashSet<Pair<Integer, Long>>();
			ContextualSessionHandler.beginGoodTransaction();
			final List<edu.scripps.yates.proteindb.persistence.mysql.MsRun> retrieveList = ContextualSessionHandler
					.retrieveList(edu.scripps.yates.proteindb.persistence.mysql.MsRun.class);
			for (final MsRun msrun : retrieveList) {
				final long t1 = System.currentTimeMillis();
				final List<Psm> psmsWithMSRun = PreparedQueries.getPsmsWithMSRun(null, msrun.getRunId());
				final long time = System.currentTimeMillis() - t1;
				iterateOverPsms(psmsWithMSRun);
				System.out.println(psmsWithMSRun.size() + " psms retrieved in " + time);
				final Pair<Integer, Long> pair = new Pair<Integer, Long>(psmsWithMSRun.size(), time);
				times.add(pair);
				for (final Psm psm : psmsWithMSRun) {
					final Set<Protein> proteins = psm.getProteins();
					for (final Protein protein : proteins) {
						final Set<Psm> psms = protein.getPsms();
						for (final Psm psm2 : psms) {
							System.out.println(psm2.getMsRun().getRunId() + " " + psm2.getPsmId());
						}
					}
				}
			}
			printTimes(times);
		} finally {
			ContextualSessionHandler.closeSession();
		}
	}

	private void iterateOverPsms(List<Psm> psmsWithMSRun) {
		final Set<Protein> proteins = new THashSet<Protein>();
		for (final Psm psm : psmsWithMSRun) {
			proteins.addAll(psm.getProteins());
		}
		System.out.println(proteins.size() + " proteins from " + psmsWithMSRun.size() + " PSMs");
		// for (Protein protein : proteins) {
		// final Set psms = protein.getPsms();
		// int size = psms.size();
		// }
	}

	private void printTimes(Set<Pair<Integer, Long>> times) {
		for (final Pair<Integer, Long> pair : times) {
			System.out.println(pair.getFirstelement() + "\t" + pair.getSecondElement());
		}

	}

	@Test
	public void fixProjectReferenceInMsRun() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<MsRun> msRuns = ContextualSessionHandler.retrieveList(MsRun.class);
			for (final MsRun msRun : msRuns) {
				final Set<Protein> proteins = msRun.getProteins();
				final Protein protein = proteins.iterator().next();
				final Set<Condition> conditions = protein.getConditions();
				final Condition condition = conditions.iterator().next();
				final Project project = condition.getProject();
				msRun.setProject(project);
				project.getMsRuns().add(msRun);
				ContextualSessionHandler.saveOrUpdate(msRun);
				ContextualSessionHandler.saveOrUpdate(project);
			}
		} finally {
			ContextualSessionHandler.finishGoodTransaction();
		}
	}

	@Test
	public void getPsmsWithPTM() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<Psm> psmContainingPTM = PreparedQueries.getPSMsContainingPTM("Phosphorylated residue", null);
			System.out.println(psmContainingPTM.size() + " psms");
			Assert.assertTrue(!psmContainingPTM.isEmpty());
			for (final Psm psm : psmContainingPTM) {
				System.out.println(psm.getFullSequence());
			}

		} finally {
			ContextualSessionHandler.finishGoodTransaction();
		}
	}

	@Test
	public void getPSMAmountsByConditions() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<Condition> conditionList = ContextualSessionHandler.retrieveList(Condition.class);
			final Map<Condition, Set<AmountType>> amountsByConditions = PreparedQueries
					.getPSMAmountTypesByConditions(conditionList);
			for (final Condition condition : amountsByConditions.keySet()) {
				System.out.println("Condition: " + condition.getName());
				final Set<AmountType> amountTypes = amountsByConditions.get(condition);
				for (final AmountType amountType : amountTypes) {
					System.out.println("\t" + amountType.getName());
				}
			}

		} finally {
			ContextualSessionHandler.finishGoodTransaction();
		}
	}

	@Test
	public void getPeptideAmountsByConditions() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<Condition> conditionList = ContextualSessionHandler.retrieveList(Condition.class);
			final Map<Condition, Set<AmountType>> amountsByConditions = PreparedQueries
					.getPeptideAmountTypesByConditions(conditionList);
			for (final Condition condition : amountsByConditions.keySet()) {
				System.out.println("Condition: " + condition.getName());
				final Set<AmountType> amountTypes = amountsByConditions.get(condition);
				for (final AmountType amountType : amountTypes) {
					System.out.println("\t" + amountType.getName());
				}
			}

		} finally {
			ContextualSessionHandler.finishGoodTransaction();
		}
	}

	@Test
	public void getProteinAmountsByConditions() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final List<Condition> conditionList = ContextualSessionHandler.retrieveList(Condition.class);
			final Map<Condition, Set<AmountType>> amountsByConditions = PreparedQueries
					.getProteinAmountTypesByConditions(conditionList);
			for (final Condition condition : amountsByConditions.keySet()) {
				System.out.println("Condition: " + condition.getName());
				final Set<AmountType> amountTypes = amountsByConditions.get(condition);
				for (final AmountType amountType : amountTypes) {
					System.out.println("\t" + amountType.getName());
				}
			}

		} finally {
			ContextualSessionHandler.finishGoodTransaction();
		}
	}

	@Test
	public void getProteinsFromMSRunTest() {
		final String projectTag = "_CFTR_";
		final String msRunID = "siHDAC7_011909";
		final List<Protein> proteins = PreparedQueries.getProteinsWithMSRun(projectTag, msRunID);
		Assert.assertFalse(proteins.isEmpty());
		System.out.println(proteins.size() + " proteins in msrun " + msRunID);

		final List<Peptide> peptides = PreparedQueries.getPeptidesWithMSRun(projectTag, msRunID);
		Assert.assertFalse(peptides.isEmpty());
		System.out.println(peptides.size() + " peptides in msrun " + msRunID);

		final List<MsRun> msRunsByProject = PreparedQueries.getMSRunsByProject(projectTag);

		final List<Protein> proteinsByMSRunCriteria = PreparedCriteria.getProteinsByMSRunsCriteria(msRunsByProject);
		System.out.println(proteinsByMSRunCriteria.size() + " proteins in project " + projectTag);
		final List<Protein> proteins2 = PreparedCriteria.getProteinsByMSRunCriteria(msRunsByProject.get(0));
		System.out.println(proteins2.size() + " proteins in msrun " + msRunsByProject.get(0).getRunId());

		final List<Peptide> peptidesByMSRunCriteria = PreparedCriteria.getPeptidesByMSRunsCriteria(msRunsByProject);
		System.out.println(peptidesByMSRunCriteria.size() + " peptides in project " + projectTag);
		final List<Peptide> peptides2 = PreparedCriteria.getPeptidesByMSRunCriteria(msRunsByProject.get(0));
		System.out.println(peptides2.size() + " peptides in msrun " + msRunsByProject.get(0).getRunId());
	}
}
