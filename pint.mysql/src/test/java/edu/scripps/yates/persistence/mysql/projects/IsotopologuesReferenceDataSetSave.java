package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class IsotopologuesReferenceDataSetSave {
	@Before
	public void defineUniprotAnnotationSettings() {
		UniprotProteinRetrievalSettings.getInstance(new File(
				"C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);

	}

	@Test
	public void referencedataSetSave() {

		String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\PInt\\xml\\HEK_TlLh.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project project = importReader.getProjectFromCfgFile(new File(
				cfgFilePath), null);

		// look for singleton psms
		IsotopologuesTestUtils.lookForsingletonPSMS(project);

		// look for psms with ratios
		IsotopologuesTestUtils.lookForPSMsWithRatios(project);
		// look for repeated psms (same psm id), but belonging to different
		// conditions
		IsotopologuesTestUtils
				.lookForRepeatedPSMsBelongingToDifferentConditions(project);

		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(project);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");

	}
}
