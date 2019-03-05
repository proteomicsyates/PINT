package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class Scramble_project {

	@Before
	public void defineUniprotAnnotationSettings() {
		UniprotProteinRetrievalSettings.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
	}

	@Test
	public void Scramble_projectSaveTest() throws IOException {
		File file = new ClassPathResource("CFTR_Scramble.xml").getFile();
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		ImportCfgFileReader.ignoreDTASelectParameterT = true;
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(file, null);
		final int numProteins = projectFromCfgFile.getConditions().iterator().next().getProteins().size();
		System.out.println(numProteins + " proteins");
		int numProteinsWithPSMs = 0;
		for (Protein protein : projectFromCfgFile.getConditions().iterator().next().getProteins()) {
			if (!protein.getPSMs().isEmpty()) {
				numProteinsWithPSMs++;

			}
		}
		System.out.println(numProteinsWithPSMs + " proteins with psms");
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}
}
