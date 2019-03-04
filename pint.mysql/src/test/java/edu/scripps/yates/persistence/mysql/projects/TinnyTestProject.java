package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class TinnyTestProject {
	@Before
	public void setAnnotationsIndex() {
		UniprotProteinRetrievalSettings.getInstance(new File(
				"C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);

	}

	@Test
	public void tinny_Test_Project_save() {

		String cfgFilePath = "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\test_tinny_project\\tinny project.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project project = importReader.getProjectFromCfgFile(new File(
				cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(project);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}
}
