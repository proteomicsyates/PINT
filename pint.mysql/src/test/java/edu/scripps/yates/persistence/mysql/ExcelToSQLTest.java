package edu.scripps.yates.persistence.mysql;

import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import edu.scripps.yates.agent.DataProvider;
import edu.scripps.yates.excel.proteindb.agent.ProteinProviderFromExcel;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ConditionAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProjectAdapter;
import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class ExcelToSQLTest {

	@Test
	public void fullDBImportTestByExperiment() {
		try {
			final DataProvider proteinProvider = new ProteinProviderFromExcel(
					true, 10);
			Map<String, Set<Protein>> proteinList = proteinProvider.getProteinMap();
			final Condition condition = proteinList.values().iterator().next()
					.iterator().next().getPSMs().iterator().next()
					.getConditions().iterator().next();
			Assert.assertNotNull(condition);

			ContextualSessionHandler.beginGoodTransaction();
			edu.scripps.yates.proteindb.persistence.mysql.Project project = new ProjectAdapter(
					new ProjectEx("nuevo proyecto test", "description"))
					.adapt();
			ContextualSessionHandler.save(project);
			final edu.scripps.yates.proteindb.persistence.mysql.Condition adapt = new ConditionAdapter(
					condition, project).adapt();
			new MySQLSaver().saveExperimentalCondition(adapt);

			ContextualSessionHandler.finishGoodTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
			fail();
		}
	}

}
