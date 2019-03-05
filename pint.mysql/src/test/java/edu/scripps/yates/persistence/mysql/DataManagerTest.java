package edu.scripps.yates.persistence.mysql;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.cv.CommonlyUsedCV;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ConditionEx;
import edu.scripps.yates.utilities.model.factories.GeneEx;
import edu.scripps.yates.utilities.model.factories.LabelEx;
import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.model.factories.PSMEx;
import edu.scripps.yates.utilities.model.factories.PTMEx;
import edu.scripps.yates.utilities.model.factories.PTMSiteEx;
import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.model.factories.ProteinEx;
import edu.scripps.yates.utilities.model.factories.RatioEx;
import edu.scripps.yates.utilities.model.factories.SampleEx;
import edu.scripps.yates.utilities.model.factories.ScoreEx;
import edu.scripps.yates.utilities.model.factories.ThresholdEx;
import edu.scripps.yates.utilities.model.factories.TissueEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.Label;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.set.hash.THashSet;
import junit.framework.Assert;

public class DataManagerTest {
	private static final String EXPERIMENT_NAME = "experimento 1";
	private static final String PROJECT_NAME = "mi proyecto de prueba";
	private static final String EXPERIMENTAL_CONDITION_NAME = edu.scripps.yates.excel.proteindb.enums.ConditionName._6H_30C
			.name();
	private static final String EXPERIMENTAL_CONDITION_NAME2 = edu.scripps.yates.excel.proteindb.enums.ConditionName.BACKGROUND
			.name();

	private static final String SAMPLE_NAME = "SAMPLE number 1";
	private static final String MS_RUN_ID = "ms run id";
	private static final String MS_RUN_PATH = "path of the ms run";
	private static final String ORGANISM_ID = "organism id";
	private static final String ORGANISM_NAME = "organism name";
	private static final String TISSUE_ID = "tissue id";
	private static final String TISSUE_NAME = "tissue name";
	private static final Accession PROTEIN_IPI_ACC = new AccessionEx("protein ipi acc", AccessionType.IPI);
	private static final String PROTEIN_GENE = "protein gene";
	private static final Accession UNIPROT_ACC = new AccessionEx("uniprot acc", AccessionType.UNIPROT);
	private static final Double PROTEIN_AMOUNT_VALUE = 123.123;
	private static final String ANNOTATION = "CATALITIC ACTIVITY ANNOTATION";
	private static final double PROTEIN_RATIO_VALUE = 2.123;
	private static final String SCORE_VALUE = "0.123";
	private static final String PSM_ID = "psm id";
	private static final String PSM_FULL_SEQUENCE = "ASDFASDFASDF(+12.123)DFS";
	private static final String PSM_SEQUENCE = "ASDFASDFASDFDFS";
	private static final String PTM_NAME = "Phospho";
	private static final String AA = "S";
	private static final int PTM_POSITION = 2;
	private static final String THRESHOLD_NAME = "p value <0.1";
	private static final Date PROJECT_RELEASE_DATE = new Date(2014, 6, 21);
	private static final Date MSRUN_DATE = new Date(2014, 5, 14);
	private ProteinEx protein;
	private ThresholdEx threhold;
	private PSMEx psm;
	private PTMEx ptm;
	private PTMSiteEx ptmsite;
	private RatioEx ratio;
	private ScoreEx score;
	private ProteinAnnotationEx proteinAnnotation;
	private AmountEx amount;
	private ConditionEx condition1;
	private MSRunEx run;
	private ConditionEx condition2;
	private ProjectEx project;
	private SampleEx sample;
	private OrganismEx organism;
	private TissueEx tissue;
	private LabelEx label;

	private void compareProteins(Protein protein, Protein protein2) {

		compareGenes(protein.getGenes().iterator().next(), protein2.getGenes().iterator().next());

		compareProteinAmounts(protein.getAmounts().iterator().next(), protein2.getAmounts().iterator().next());

		compareProteinAnnotations(protein.getAnnotations().iterator().next(),
				protein2.getAnnotations().iterator().next());
		compareProteinRatios(protein.getRatios().iterator().next(), protein2.getRatios().iterator().next());
		String conditionRef2 = getExperimentalCondition2().getName();
		String conditionRef1 = getExperimentalCondition().getName();

		comparePSMs(protein.getPSMs().iterator().next(), protein2.getPSMs().iterator().next());
		compareThresholds(protein.getThresholds().iterator().next(), protein2.getThresholds().iterator().next());
		compareProteinAccessions(ModelUtils.getAccessions(protein, AccessionType.IPI).iterator().next(),
				ModelUtils.getAccessions(protein2, AccessionType.IPI).iterator().next());
		compareProteinAccessions(ModelUtils.getAccessions(protein, AccessionType.UNIPROT).iterator().next(),
				ModelUtils.getAccessions(protein2, AccessionType.UNIPROT).iterator().next());

	}

	private void compareGenes(Gene gene1, Gene gene2) {
		Assert.assertEquals(gene1.getGeneID(), gene2.getGeneID());
		Assert.assertEquals(gene1.getGeneType(), gene2.getGeneType());
	}

	private void compareProteinAccessions(Accession accession, Accession accession2) {
		Assert.assertEquals(accession.getAccession(), accession2.getAccession());
		Assert.assertEquals(accession.getAccessionType(), accession2.getAccessionType());
		Assert.assertEquals(accession.getDescription(), accession2.getDescription());
	}

	private void compareThresholds(Threshold threshold1, Threshold threshold2) {
		Assert.assertEquals(threshold1.getName(), threshold2.getName());
		Assert.assertEquals(threshold1.getDescription(), threshold2.getDescription());
	}

	private void comparePSMs(PSM psm1, PSM psm2) {
		Assert.assertEquals(psm1.getFullSequence(), psm2.getFullSequence());
		Assert.assertEquals(psm1.getSequence(), psm2.getSequence());
		Assert.assertEquals(psm1.getIdentifier(), psm2.getIdentifier());
		Assert.assertEquals(psm1.getCalcMH(), psm2.getCalcMH());
		Assert.assertEquals(psm1.getExperimentalMH(), psm2.getExperimentalMH());
		Assert.assertEquals(psm1.getIonProportion(), psm2.getIonProportion());
		Assert.assertEquals(psm1.getMassErrorPPM(), psm2.getMassErrorPPM());
		compareMSRun(psm1.getMSRun(), psm2.getMSRun());
		Assert.assertEquals(psm1.getPI(), psm2.getPI());

		comparePTMs(psm1.getPTMs().get(0), psm2.getPTMs().get(0));
		Assert.assertEquals(psm1.getSPR(), psm2.getSPR());
		Assert.assertEquals(psm1.getTotalIntensity(), psm2.getTotalIntensity());

		compareScores(psm1.getScores().iterator().next(), psm2.getScores().iterator().next());
	}

	private void comparePTMs(PTM ptm1, PTM ptm2) {
		Assert.assertEquals(ptm1.getCVId(), ptm2.getCVId());
		Assert.assertEquals(ptm1.getName(), ptm2.getName());
		Assert.assertEquals(ptm1.getMassShift(), ptm2.getMassShift());
		comparePTMSites(ptm1.getPTMSites().get(0), ptm2.getPTMSites().get(0));

	}

	private void comparePTMSites(PTMSite ptmSite1, PTMSite ptmSite2) {
		Assert.assertEquals(ptmSite1.getAA(), ptmSite2.getAA());
		Assert.assertEquals(ptmSite1.getPosition(), ptmSite2.getPosition());
		compareScores(ptmSite1.getScore(), ptmSite2.getScore());

	}

	private void compareProteinRatios(Ratio ratio1, Ratio ratio2) {
		Assert.assertEquals(ratio1.getDescription(), ratio2.getDescription());
		Assert.assertEquals(ratio1.getValue(), ratio2.getValue());
		Assert.assertEquals(ratio1.getCombinationType(), ratio2.getCombinationType());
		compareExperimentalConditions(ratio1.getCondition1(), ratio2.getCondition1());
		compareExperimentalConditions(ratio1.getCondition2(), ratio2.getCondition2());
		compareScores(ratio1.getAssociatedConfidenceScore(), ratio2.getAssociatedConfidenceScore());
	}

	private void compareScores(Score score1, Score score2) {
		Assert.assertEquals(score1.getScoreDescription(), score2.getScoreDescription());
		Assert.assertEquals(score1.getValue(), score2.getValue());
		Assert.assertEquals(score1.getScoreName(), score2.getScoreName());
		Assert.assertEquals(score1.getScoreType(), score2.getScoreType());
	}

	private void compareProteinAnnotations(ProteinAnnotation annotation1, ProteinAnnotation annotation2) {
		Assert.assertEquals(annotation1.getValue(), annotation2.getValue());
		Assert.assertEquals(annotation1.getName(), annotation2.getName());
		Assert.assertEquals(annotation1.getSource(), annotation2.getSource());
		Assert.assertEquals(annotation1.getAnnotationType(), annotation2.getAnnotationType());
	}

	private void compareProteinAmounts(Amount amount1, Amount amount2) {
		Assert.assertEquals(amount1.getAmountType(), amount2.getAmountType());
		Assert.assertEquals(amount1.getValue(), amount2.getValue());
		Assert.assertEquals(amount1.getCombinationType(), amount2.getCombinationType());

	}

	private Protein getProtein() {
		if (protein == null) {
			protein = new ProteinEx(UNIPROT_ACC);
			List<Gene> genes = new ArrayList<Gene>();
			final GeneEx gene = new GeneEx(PROTEIN_GENE);
			gene.setGeneType("primary");
			genes.add(gene);
			for (Gene gene2 : genes) {
				protein.addGene(gene2);
			}
			Set<Amount> proteinAmounts = new THashSet<Amount>();
			proteinAmounts.add(getProteinAmount());
			for (Amount amount : proteinAmounts) {
				protein.addAmount(amount);
			}
			Set<ProteinAnnotation> proteinAnnotations = new THashSet<ProteinAnnotation>();
			proteinAnnotations.add(getProteinAnnotation());
			protein.setProteinAnnotations(proteinAnnotations);
			Set<Ratio> proteinRatios = new THashSet<Ratio>();
			proteinRatios.add(getProteinRatio());
			for (Ratio ratio : proteinRatios) {
				protein.addRatio(ratio);
			}
			List<PSM> psms = new ArrayList<PSM>();
			psms.add(getPSM());
			for (PSM psm : psms) {
				protein.addPSM(psm);
			}
			Set<Threshold> thresholds = new THashSet<Threshold>();
			thresholds.add(getThreshold());
			protein.setThresholds(thresholds);

			protein.addSecondaryAccession(PROTEIN_IPI_ACC);
		}
		return protein;
	}

	private Threshold getThreshold() {
		if (threhold == null) {
			threhold = new ThresholdEx(THRESHOLD_NAME, true);
			threhold.setDescription("description");
		}
		return threhold;
	}

	private PSM getPSM() {
		if (psm == null) {
			psm = new PSMEx(PSM_ID, PSM_FULL_SEQUENCE, PSM_SEQUENCE);
			psm.setMSRun(getMSRun());
			psm.setCalculatedMH(34.34);
			psm.setExperimentalMH(34.33);
			psm.setIonProportion(123.12);
			psm.setMassErrorPPM(10.2);
			psm.setPi(39.0);
			psm.setSpr(90);
			psm.setTotalIntensity(14000.0);
			psm.addScore(getScore());
			psm.addPtm(getPTM());
		}
		return psm;
	}

	private PTM getPTM() {
		if (ptm == null) {
			ptm = new PTMEx(PTM_NAME, 80.0);
			ptm.setCvId("MOD:1233");
			ptm.addPtmSite(getPTMSite());
		}
		return ptm;
	}

	private PTMSite getPTMSite() {
		if (ptmsite == null) {
			ptmsite = new PTMSiteEx(AA, PTM_POSITION);
			ptmsite.setScore(getScore());
		}
		return ptmsite;
	}

	private Ratio getProteinRatio() {
		if (ratio == null) {
			ratio = new RatioEx(PROTEIN_RATIO_VALUE, getExperimentalCondition(), getExperimentalCondition2(),
					"ratio description", AggregationLevel.PROTEIN);
			ratio.setAssociatedConfidenceScore(getScore());
		}
		return ratio;
	}

	private Score getScore() {
		if (score == null) {
			score = new ScoreEx(SCORE_VALUE, "my pvalue",
					CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID), "score description");
		}
		return score;
	}

	private ProteinAnnotation getProteinAnnotation() {
		if (proteinAnnotation == null) {
			proteinAnnotation = new ProteinAnnotationEx(AnnotationType.catalytic_activity,
					AnnotationType.catalytic_activity.getKey(), ANNOTATION);
			proteinAnnotation.setSource("source");
		}
		return proteinAnnotation;

	}

	private Amount getProteinAmount() {
		if (amount == null) {
			AmountType amountType = AmountType.AREA;
			amount = new AmountEx(PROTEIN_AMOUNT_VALUE, amountType, getExperimentalCondition());
		}
		return amount;
	}

	private void compareProjects(Project project, Project project2) {
		Assert.assertEquals(project.getName(), project2.getName());
		Assert.assertEquals(project.getDescription(), project2.getDescription());
		Assert.assertEquals(project.getPubmedLink().toString(), project2.getPubmedLink().toString());
		final Date releaseDate = project2.getReleaseDate();
		final Date releaseDate2 = project.getReleaseDate();
		Assert.assertEquals(releaseDate2, releaseDate);
		Assert.assertEquals(project.isPrivate(), project2.isPrivate());
	}

	private void compareExperimentalConditions(Condition experimentalCondition1, Condition experimentalCondition2) {
		Assert.assertEquals(experimentalCondition1.getName(), experimentalCondition2.getName());
		Assert.assertEquals(experimentalCondition1.getDescription(), experimentalCondition2.getDescription());
		Assert.assertEquals(experimentalCondition1.getUnit(), experimentalCondition2.getUnit());
		compareSamples(experimentalCondition1.getSample(), experimentalCondition2.getSample());

	}

	private void compareMSRun(MSRun runByPath, MSRun msrun) {
		Assert.assertEquals(runByPath.getPath(), msrun.getPath());
		Assert.assertEquals(runByPath.getRunId(), msrun.getRunId());
		Assert.assertEquals(runByPath.getDate(), msrun.getDate());

	}

	private void compareSamples(Sample sampleDB, Sample sample) {
		Assert.assertEquals(sampleDB.getName(), sample.getName());
		Assert.assertEquals(sampleDB.getDescription(), sample.getDescription());
		compareOrganisms(sampleDB.getOrganism(), sample.getOrganism());
		compareTissues(sampleDB.getTissue(), sample.getTissue());
	}

	private void compareTissues(Tissue tissue, Tissue tissue2) {
		Assert.assertEquals(tissue.getName(), tissue2.getName());
		Assert.assertEquals(tissue.getTissueID(), tissue2.getTissueID());
	}

	private void compareOrganisms(Organism organism, Organism organism2) {
		Assert.assertEquals(organism.getName(), organism2.getName());
		Assert.assertEquals(organism.getOrganismID(), organism2.getOrganismID());
	}

	private MSRun getMSRun() {
		if (run == null) {
			run = new MSRunEx(MS_RUN_ID, MS_RUN_PATH);
			run.setDate(MSRUN_DATE);
		}
		return run;
	}

	private Condition getExperimentalCondition() {
		if (condition1 == null) {
			condition1 = new ConditionEx(EXPERIMENTAL_CONDITION_NAME, getSample(), project);
			condition1.setDescription("experimental condition description");
			condition1.setUnit("unit@");
			condition1.setValue(213123.23);
		}
		return condition1;
	}

	private Condition getExperimentalCondition2() {
		if (condition2 == null) {
			condition2 = new ConditionEx(EXPERIMENTAL_CONDITION_NAME2, getSample(), project);
			condition2.setDescription("213432asdfexperimental condition description");
			condition2.setUnit("unitasdf@");
			condition2.setValue(123123.23);
		}
		return condition2;
	}

	private Project getProject() {
		if (project == null) {
			project = new ProjectEx(PROJECT_NAME, "DESCRIPTION");
			try {
				project.setPubmedLink(new URL("http://www.google.com"));
			} catch (MalformedURLException e) {
				fail();
			}
			project.setReleaseDate(PROJECT_RELEASE_DATE);
		}
		return project;
	}

	private Sample getSample() {
		if (sample == null) {
			sample = new SampleEx(SAMPLE_NAME);
			sample.setDescription("DESCRIPTION SAMPLE");
			sample.setLabel(getLabel());
			sample.setOrganism(getOrgnism());
			sample.setTissue(getTissue());
			sample.setWildType(true);
		}
		return sample;
	}

	private Label getLabel() {
		if (label == null) {
			label = new LabelEx("HEAVY");
			label.setMassDiff(234.23);

		}
		return label;
	}

	private Organism getOrgnism() {
		if (organism == null) {
			organism = new OrganismEx(ORGANISM_ID);
			organism.setName(ORGANISM_NAME);
		}
		return organism;
	}

	private Tissue getTissue() {
		if (tissue == null) {
			tissue = new TissueEx(TISSUE_ID);
			tissue.setName(TISSUE_NAME);
		}
		return tissue;
	}
}
