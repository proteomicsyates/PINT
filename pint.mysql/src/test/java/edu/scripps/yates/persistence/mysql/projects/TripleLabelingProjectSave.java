package edu.scripps.yates.persistence.mysql.projects;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProjectAdapter;
import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class TripleLabelingProjectSave {
	private static final String cfgFileName = "tripleLabeling.xml";

	@Before
	public void before() {
		edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings settings = UniprotProteinRetrievalSettings
				.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
		ContextualSessionHandler.openSession();
	}

	@Test
	public void projectSave() {
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		Project project;
		try {
			project = importReader.getProjectFromCfgFile(new ClassPathResource(cfgFileName).getFile(), null);
			final String newTag = project.getTag().substring(0, project.getTag().length() - 1) + "2";
			((ProjectEx) project).setTag(newTag);
			checkSamplesInConditions(project);
			checkUniquenessOfPSMs(project);
			checkUniquenessOfPeptides(project);
			checkUniquenessOfProteins(project);
			final edu.scripps.yates.proteindb.persistence.mysql.Project hibProject = new ProjectAdapter(project)
					.adapt();
			checkSamplesInConditionsInSQLModel(hibProject);
			checkUniquenessOfPsmsInSQLModel(hibProject);
			checkUniquenessOfPeptidesInSQLModel(hibProject);
			checkUniquenessOfProteinsInSQLModel(hibProject);
			ContextualSessionHandler.beginGoodTransaction();

			final Integer projectId = new MySQLSaver().saveProject(project);
			//
			ContextualSessionHandler.finishGoodTransaction();
			System.out.println("Everything is OK!");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void checkSamplesInConditions(Project project) {
		final Set<Condition> conditions = project.getConditions();
		for (Condition condition : conditions) {
			if (condition.getSample() == null) {
				fail();
			}
		}
	}

	private void checkSamplesInConditionsInSQLModel(edu.scripps.yates.proteindb.persistence.mysql.Project project) {
		final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditions = project.getConditions();
		for (edu.scripps.yates.proteindb.persistence.mysql.Condition condition : conditions) {
			if (condition.getSample() == null) {
				fail();
			}
		}
	}

	private void checkUniquenessOfPsmsInSQLModel(edu.scripps.yates.proteindb.persistence.mysql.Project project) {
		boolean fail = false;
		final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<Psm> psmsTotal = new THashSet<Psm>();
		for (edu.scripps.yates.proteindb.persistence.mysql.Condition condition : conditions) {
			final Set<Psm> psms = condition.getPsms();
			for (Psm psm : psms) {
				if (psm.getConditions().isEmpty()) {
					fail();
				}
				psmsTotal.add(psm);
			}
		}
		TIntHashSet psmHasCodes = new TIntHashSet();
		for (Psm psm : psmsTotal) {
			if (psmHasCodes.contains(psm.hashCode())) {
				fail = true;
			}
			psmHasCodes.add(psm.hashCode());
		}

		Set<String> psmIds = new THashSet<String>();
		for (Psm psm : psmsTotal) {
			if (psmIds.contains(psm.getPsmId())) {
				fail = true;
			}
			psmIds.add(psm.getPsmId());
		}
		System.out.println(
				psmHasCodes.size() + " hashcodes " + psmIds.size() + " PSMs IDs " + psmsTotal.size() + " PSMs");
		if (fail) {
			fail();
		}

	}

	private void checkUniquenessOfPeptidesInSQLModel(edu.scripps.yates.proteindb.persistence.mysql.Project project) {
		boolean fail = false;
		final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<edu.scripps.yates.proteindb.persistence.mysql.Peptide> peptidesTotal = new THashSet<edu.scripps.yates.proteindb.persistence.mysql.Peptide>();
		for (edu.scripps.yates.proteindb.persistence.mysql.Condition condition : conditions) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Peptide> peptides = condition.getPeptides();
			for (edu.scripps.yates.proteindb.persistence.mysql.Peptide peptide : peptides) {
				if (peptide.getConditions().isEmpty()) {
					fail();
				}
				peptidesTotal.add(peptide);
			}
		}
		TIntHashSet peptideHasCodes = new TIntHashSet();
		for (edu.scripps.yates.proteindb.persistence.mysql.Peptide peptide : peptidesTotal) {
			if (peptideHasCodes.contains(peptide.hashCode())) {
				fail = true;
			}
			peptideHasCodes.add(peptide.hashCode());
		}

		Set<String> peptideSequences = new THashSet<String>();
		for (edu.scripps.yates.proteindb.persistence.mysql.Peptide peptide : peptidesTotal) {
			if (peptideSequences.contains(peptide.getSequence())) {
				fail = true;
			}
			peptideSequences.add(peptide.getSequence());
		}
		System.out.println(peptideHasCodes.size() + " hashcodes " + peptideSequences.size() + " Peptide sequences "
				+ peptidesTotal.size() + " Peptides");
		if (fail) {
			fail();
		}

	}

	private void checkUniquenessOfProteinsInSQLModel(edu.scripps.yates.proteindb.persistence.mysql.Project project) {
		boolean fail = false;
		final Set<edu.scripps.yates.proteindb.persistence.mysql.Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteinsTotal = new THashSet<edu.scripps.yates.proteindb.persistence.mysql.Protein>();
		for (edu.scripps.yates.proteindb.persistence.mysql.Condition condition : conditions) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = condition.getProteins();
			for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteins) {
				if (protein.getConditions().isEmpty()) {
					fail();
				}
				proteinsTotal.add(protein);
			}
		}
		TIntHashSet peptideHasCodes = new TIntHashSet();
		for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteinsTotal) {
			if (peptideHasCodes.contains(protein.hashCode())) {
				fail = true;
			}
			peptideHasCodes.add(protein.hashCode());
		}

		Set<String> proteinAccs = new THashSet<String>();
		for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteinsTotal) {
			if (proteinAccs.contains(protein.getAcc())) {
				fail = true;
			}
			proteinAccs.add(protein.getAcc());
		}
		System.out.println(peptideHasCodes.size() + " hashcodes " + proteinAccs.size() + " Protein accessions "
				+ proteinsTotal.size() + " Proteins");
		if (fail) {
			fail();
		}

	}

	private void checkUniquenessOfPSMs(Project project) {
		final Set<Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<PSM> psmsTotal = new THashSet<PSM>();
		for (Condition condition : conditions) {
			final Set<PSM> psMs = condition.getPSMs();
			for (PSM psm : psMs) {
				psmsTotal.add(psm);
				if (psm.getConditions().isEmpty()) {
					fail();
				}
			}
		}
		TIntHashSet psmHasCodes = new TIntHashSet();
		for (PSM psm : psmsTotal) {
			if (psmHasCodes.contains(psm.hashCode())) {
				fail();
			}
			psmHasCodes.add(psm.hashCode());
		}
		Set<String> psmIds = new THashSet<String>();
		for (PSM psm : psmsTotal) {
			if (psmIds.contains(psm.getIdentifier())) {
				fail();
			}
			psmIds.add(psm.getIdentifier());
		}

		System.out.println(
				psmHasCodes.size() + " hashcodes " + psmIds.size() + " PSMs IDs " + psmsTotal.size() + " PSMs");

	}

	private void checkUniquenessOfPeptides(Project project) {
		final Set<Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<Peptide> peptidesTotal = new THashSet<Peptide>();
		for (Condition condition : conditions) {
			final Set<Peptide> peptides = condition.getPeptides();
			for (Peptide peptide : peptides) {
				peptidesTotal.add(peptide);
				if (peptide.getConditions().isEmpty()) {
					fail();
				}
			}
		}
		TIntHashSet peptideHasCodes = new TIntHashSet();
		for (Peptide peptide : peptidesTotal) {
			if (peptideHasCodes.contains(peptide.hashCode())) {
				fail();
			}
			peptideHasCodes.add(peptide.hashCode());
		}
		Set<String> peptideSequences = new THashSet<String>();
		for (Peptide peptide : peptidesTotal) {
			if (peptideSequences.contains(peptide.getSequence())) {
				fail();
			}
			peptideSequences.add(peptide.getSequence());
		}

		System.out.println(peptideHasCodes.size() + " hashcodes " + peptideSequences.size() + " Peptide sequences "
				+ peptidesTotal.size() + " Peptides");

	}

	private void checkUniquenessOfProteins(Project project) {
		final Set<Condition> conditions = project.getConditions();
		System.out.println(conditions.size() + " conditions");
		Set<Protein> proteinTotal = new THashSet<Protein>();
		for (Condition condition : conditions) {
			final Set<Protein> proteins = condition.getProteins();
			for (Protein protein : proteins) {
				proteinTotal.add(protein);
				if (protein.getConditions().isEmpty()) {
					fail();
				}
			}
		}
		TIntHashSet proteinHasCodes = new TIntHashSet();
		for (Protein protein : proteinTotal) {
			if (proteinHasCodes.contains(protein.hashCode())) {
				fail();
			}
			proteinHasCodes.add(protein.hashCode());
		}
		Set<String> proteinACCs = new THashSet<String>();
		for (Protein protein : proteinTotal) {
			if (proteinACCs.contains(protein.getAccession())) {
				fail();
			}
			proteinACCs.add(protein.getAccession());
		}

		System.out.println(proteinHasCodes.size() + " hashcodes " + proteinACCs.size() + " protein Accessions "
				+ proteinTotal.size() + " Proteins");

	}
}
