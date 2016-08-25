package edu.scripps.yates.server.daemon.tasks;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.server.ProteinRetrievalServicesServlet;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServletContextProperty;
import edu.scripps.yates.shared.model.ProjectBean;

public class PreLoadPublicProjects extends PintServerDaemonTask {
	private final static DecimalFormat myFormatter = new DecimalFormat("#.##");
	private final String sessionID;
	private final Set<String> projectsToLoad = new HashSet<String>();

	public PreLoadPublicProjects(String sessionID, ServletContext servletContext) {
		super(servletContext);
		FileManager.getProjectFilesPath(servletContext);
		this.sessionID = sessionID;
	}

	@Override
	public void run() {
		numRuns++;
		log.info("Starting " + getTaskName());
		final String projectsToPreload = ServletContextProperty.getServletContextProperty(servletContext,
				ServletContextProperty.PROJECTS_TO_PRELOAD);
		if (projectsToPreload != null) {
			if (projectsToPreload.contains(",")) {
				final String[] split = projectsToPreload.split(",");
				for (String projectToPreload : split) {
					projectsToLoad.add(projectToPreload);
				}
			} else {
				projectsToLoad.add(projectsToPreload);
			}
		}
		ContextualSessionHandler.getSession().beginTransaction();
		final Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans();

		final String preLoadPublicProjects = ServletContextProperty.getServletContextProperty(servletContext,
				ServletContextProperty.PRELOAD_PUBLIC_PROJECTS);
		if (preLoadPublicProjects != null) {
			try {
				final Boolean preloadPublic = Boolean.valueOf(preLoadPublicProjects);
				if (preloadPublic != null && preloadPublic) {
					for (ProjectBean projectBean : projectBeans) {
						if (projectBean.isPublicAvailable()) {
							projectsToLoad.add(projectBean.getTag());
						}
					}
				}
			} catch (Exception e) {

			}
		}

		final String projectsToNotPreLoad = ServletContextProperty.getServletContextProperty(servletContext,
				ServletContextProperty.PROJECTS_TO_NOT_PRELOAD);
		if (projectsToNotPreLoad != null) {
			if (projectsToNotPreLoad.contains(",")) {
				final String[] split = projectsToNotPreLoad.split(",");
				for (String projectToNotPreLoad : split) {
					projectsToLoad.remove(projectToNotPreLoad);
				}
			} else {
				projectsToLoad.remove(projectsToNotPreLoad);
			}
		}

		if (!projectsToLoad.isEmpty()) {
			ProteinRetrievalServicesServlet proteinRetrieval = new ProteinRetrievalServicesServlet();
			proteinRetrieval.setServletContext(servletContext);
			for (ProjectBean projectBean : projectBeans) {
				if (projectsToLoad.contains(projectBean.getTag())) {
					try {
						ContextualSessionHandler.getSession().beginTransaction();
						long t1 = System.currentTimeMillis();
						log.info("Pre loading project: " + projectBean.getTag());
						Set<String> projectTagSet = new HashSet<String>();
						projectTagSet.add(projectBean.getTag());
						proteinRetrieval.getProteinsFromProjects(sessionID, projectTagSet, null, false, null);
						double t2 = (System.currentTimeMillis() * 1.0 - t1 * 1.0) / 1000;
						log.info(projectBean.getTag() + " pre loaded in " + myFormatter.format(t2) + " seconds");
					} catch (Exception e) {
						e.printStackTrace();
						ContextualSessionHandler.getSession().getTransaction().rollback();
					} finally {
						ContextualSessionHandler.getSession().close();
					}
				}
			}
		}

	}

	@Override
	public boolean justRunOnce() {
		return true;
	}
}
