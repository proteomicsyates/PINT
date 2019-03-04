package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class DrosophilaProjectSave {
	@Before
	public void defineUniprotAnnotationSettings() {
		UniprotProteinRetrievalSettings.getInstance(new File(
				"C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
	}

	@Test
	public void drosophilaProjectSave_070614_one_embryo_virilis() {
		String cfgFilePath = "C:\\Users\\Salva\\workspace\\proteindb.excel\\import-cfg\\build\\DroMeVi_data_070614_one_embryo_virilis.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(
				new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

	@Test
	public void DroMeVi_data_062114_DmDv_isogenic_Save() {
		String cfgFilePath = "C:\\Users\\Salva\\workspace\\proteindb.excel\\import-cfg\\build\\DroMeVi_data_062114_DmDv_isogenic.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(
				new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

	@Test
	public void DroHybrids() {
		String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\PInt\\xml\\DroHybrids.xml";
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

	@Test
	public void DmDshybrids2014() {
		String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\DmDshybrids2014-2.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project project = importReader.getProjectFromCfgFile(new File(
				cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(project);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

	@Test
	public void drosophilaProjectSave_062114_DmDv_isogenic() {
		String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\PInt\\xml\\DmDv_Comparison_TEST.xml";
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(
				new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

}
