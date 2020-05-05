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

public class CensusOutProject {
	private static String cfgFilePath = null;
	private static String cfgFilePath2 = null;

	@Before
	public void before() {
		try {
			cfgFilePath = new ClassPathResource("census_out_test.xml").getFile().getAbsolutePath();
			cfgFilePath2 = new ClassPathResource("HS_Hek293.xml").getFile().getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings settings = UniprotProteinRetrievalSettings
				.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);

	}

	@Test
	public void save1() {
		ImportCfgFileReader importReader = new ImportCfgFileReader(true, true);
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		// ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

	@Test
	public void save2() {
		ImportCfgFileReader importReader = new ImportCfgFileReader(true, true);
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(new File(cfgFilePath2), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}
}
