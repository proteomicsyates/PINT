package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class PALM_project {
	private static final String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\PALM_little.xml";

	@Before
	public void defineUniprotAnnotationSettings() {
		UniprotProteinRetrievalSettings.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
	}

	@Test
	public void PALM_projectSaveTest() {

		ImportCfgFileReader importReader = new ImportCfgFileReader(true, true);
		importReader.ignoreDTASelectParameterT = true;
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}
}
