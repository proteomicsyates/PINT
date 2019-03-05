package edu.scripps.yates.persistence.mysql.projects;

import static org.junit.Assert.fail;

import org.junit.Test;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLDeleter;

public class ProjectDeleter {
	@Test
	public void deleteProject() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			MySQLDeleter deleter = new MySQLDeleter();
			deleter.deleteProject("4DNucleome2");
			ContextualSessionHandler.finishGoodTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			ContextualSessionHandler.rollbackTransaction();
		} finally {
			ContextualSessionHandler.closeSession();
		}
	}
}
