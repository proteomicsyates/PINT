package edu.scripps.yates.persistence.mysql.projects;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLDeleter;

public class ProjectDeleter {
	@Test
	public void deleteProject() {
		try {
			final MySQLDeleter deleter = new MySQLDeleter();
			deleter.deleteProject("alzheimeno");
			ContextualSessionHandler.finishGoodTransaction();

		} catch (final Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
			fail();

		} finally {
			ContextualSessionHandler.closeSession();
		}
	}
}
