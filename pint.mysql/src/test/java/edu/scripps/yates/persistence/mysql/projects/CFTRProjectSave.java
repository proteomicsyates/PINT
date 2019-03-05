package edu.scripps.yates.persistence.mysql.projects;

import static org.junit.Assert.fail;

import java.io.File;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.agent.DataProvider;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.excel.proteindb.ProteinSetAdapter;
import edu.scripps.yates.excel.proteindb.agent.ProteinProviderFromExcel;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.proteomicsmodel.Project;

public class CFTRProjectSave {
	@Before
	public void setupUniprotIndex() {
		UniprotProteinRetrievalSettings.getInstance(new File(
				"C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
	}

	@Test
	public void CFTRProjectSave() {
		try {
			final DataProvider proteinProvider = new ProteinProviderFromExcel(
					true, 100);
			// Collection<Protein> proteinList =
			// proteinProvider.getProteinSet();
			// final Project project = proteinList.iterator().next().getPSMs()
			// .iterator().next().getConditions().iterator().next()
			// .getProject();
			Project project = ProteinSetAdapter.generalProject;

			Assert.assertNotNull(project);

			ContextualSessionHandler.beginGoodTransaction();
			new MySQLSaver().saveProject(project);

			ContextualSessionHandler.finishGoodTransaction();
			System.out.println("Everything is OK!");
		} catch (Exception e) {
			ContextualSessionHandler.rollbackTransaction();
			e.printStackTrace();
			fail();
		}
	}
}
