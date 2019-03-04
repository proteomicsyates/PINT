package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import junit.framework.Assert;

public class NavinsProjectSave {
	private static final String cfgFilePath = "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Navin - WT vs Mut vs Tshift\\CF label-free-local.xml";

	@Before
	public void before() {
		edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings settings = UniprotProteinRetrievalSettings
				.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
		ImportCfgFileReader.ignoreDTASelectParameterT = true;
	}

	@Test
	public void navinsProjectSaveTestData() {
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		for (Condition condition : projectFromCfgFile.getConditions()) {
			if (condition.getName().equals("WT")) {
				for (edu.scripps.yates.utilities.proteomicsmodel.Protein protein : condition.getProteins()) {
					// O60427
					if (protein.getAccession().equals("O60427")) {
						final Set<Ratio> ratios = protein.getRatios();
						for (Ratio ratio : ratios) {
							if (ratio.getCondition1().getName().equals("WT")
									&& ratio.getCondition2().getName().equals("MUT")
									|| ratio.getCondition1().getName().equals("MUT")
											&& ratio.getCondition2().getName().equals("WT")) {
								final double value = ratio.getValue();
								Assert.assertEquals(0.0, value);
							}
							if (ratio.getCondition1().getName().equals("WT")
									&& ratio.getCondition2().getName().equals("TShift")
									|| ratio.getCondition1().getName().equals("TShift")
											&& ratio.getCondition2().getName().equals("WT")) {
								final double value = ratio.getValue();
								Assert.assertEquals(0.0, value);
							}
							final String value = ratio.getAssociatedConfidenceScore().getValue();
							Assert.assertEquals("0.7308237121", value);
						}

					}
					//
					if (protein.getAccession().equals("P55263")) {
						final Set<Ratio> ratios = protein.getRatios();
						for (Ratio ratio : ratios) {
							if (ratio.getCondition1().getName().equals("WT")
									&& ratio.getCondition2().getName().equals("MUT")
									|| ratio.getCondition1().getName().equals("MUT")
											&& ratio.getCondition2().getName().equals("WT")) {
								final double value = ratio.getValue();
								Assert.assertEquals(1.40195287931765, value, 0.00001);
							}
							if (ratio.getCondition1().getName().equals("WT")
									&& ratio.getCondition2().getName().equals("TShift")
									|| ratio.getCondition1().getName().equals("TShift")
											&& ratio.getCondition2().getName().equals("WT")) {
								final double value = ratio.getValue();
								Assert.assertEquals(1.33228649218525, value, 0.00001);
							}
							final String value = ratio.getAssociatedConfidenceScore().getValue();
							Assert.assertEquals("0.6825873401", value);
						}

					}
				}
			}
		}

	}

	@Test
	public void navinsProjectSave() {
		ImportCfgFileReader importReader = new ImportCfgFileReader();
		final Project projectFromCfgFile = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(projectFromCfgFile);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}
}
