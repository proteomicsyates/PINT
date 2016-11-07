package edu.scripps.yates.server.daemon.tasks;

import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLDeleter;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.shared.model.ProjectBean;

/**
 * Delete from the DB all the projects tagged as 'hidden'
 *
 * @author Salva
 *
 */
public class DeleteHiddenProjects extends PintServerDaemonTask {

	/**
	 * Delete from the DB all the projects tagged as 'hidden'
	 *
	 * @param servletContext
	 */
	public DeleteHiddenProjects(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans(true);
			ContextualSessionHandler.finishGoodTransaction();
			for (ProjectBean projectBean : projectBeans) {
				if (projectBean.isHidden()) {
					ContextualSessionHandler.beginGoodTransaction();

					final MySQLDeleter mySQLDeleter = new MySQLDeleter();
					mySQLDeleter.deleteProject(projectBean.getTag());
					log.info("Finishing transaction");
					ContextualSessionHandler.finishGoodTransaction();
					ContextualSessionHandler.closeSession();
					log.info("transaction finished");
					log.info(projectBean.getTag() + " deleted from DB");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.getSession().getTransaction().rollback();
		}

	}

	@Override
	public boolean justRunOnce() {
		return false;
	}

}
