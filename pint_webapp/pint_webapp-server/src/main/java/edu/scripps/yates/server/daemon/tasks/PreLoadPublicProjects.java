package edu.scripps.yates.server.daemon.tasks;

import java.text.DecimalFormat;
import java.util.Set;

import javax.servlet.ServletContext;

import org.hibernate.Session;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.server.ProteinRetrievalServicesServlet;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServerUtil;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.set.hash.THashSet;

public class PreLoadPublicProjects extends PintServerDaemonTask {
	private final static DecimalFormat myFormatter = new DecimalFormat("#.##");
	private final String sessionID;
	private final Set<String> projectsToLoad = new THashSet<String>();

	/**
	 * Preload projects tagged as 'public', in order to allow a faster loading
	 *
	 * @param sessionID
	 * @param servletContext
	 */
	public PreLoadPublicProjects(String sessionID, ServletContext servletContext) {
		super(servletContext);
		log.info("Creating PINT ServerDaemonTask: " + this.getClass().getCanonicalName());
		FileManager.getProjectFilesPath(servletContext);
		this.sessionID = sessionID;
	}

	@Override
	public void run() {
		log.info("Starting " + getTaskName());
		final String projectsToPreload = ServerUtil.getPINTProperties(servletContext).getProjectsToPreLoad();
		if (projectsToPreload != null) {
			log.info("ProjectsToPreload property=" + projectsToPreload);
			if (projectsToPreload.contains(",")) {
				final String[] split = projectsToPreload.split(",");
				for (final String projectToPreload : split) {
					log.info("Project to preload: " + projectToPreload);
					projectsToLoad.add(projectToPreload);
				}
			} else {
				projectsToLoad.add(projectsToPreload);
			}
		}
		Session session = ContextualSessionHandler.getCurrentSession();
		session.beginTransaction();
		final Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans();

		final Boolean preloadPublic = ServerUtil.getPINTProperties(servletContext).isPreLoadPublicProjects();
		if (preloadPublic != null && preloadPublic) {
			log.info("preloadPublic property=" + preloadPublic);

			for (final ProjectBean projectBean : projectBeans) {
				if (projectBean.isPublicAvailable()) {
					projectsToLoad.add(projectBean.getTag());
				}
			}
		}

		final String projectsToNotPreLoad = ServerUtil.getPINTProperties(servletContext).getProjectsToNotPreLoad();
		if (projectsToNotPreLoad != null) {
			log.info("projectsToNotPreLoad property=" + projectsToPreload);

			if (projectsToNotPreLoad.contains(",")) {
				final String[] split = projectsToNotPreLoad.split(",");
				for (final String projectToNotPreLoad : split) {
					projectsToLoad.remove(projectToNotPreLoad);
				}
			} else {
				projectsToLoad.remove(projectsToNotPreLoad);
			}
		}

		if (!projectsToLoad.isEmpty()) {

			for (final ProjectBean projectBean : projectBeans) {
				log.info("Project to preload: " + projectBean.getTag());
			}
			final ProteinRetrievalServicesServlet proteinRetrieval = new ProteinRetrievalServicesServlet();
			proteinRetrieval.setServletContext(servletContext);
			for (final ProjectBean projectBean : projectBeans) {
				if (ServerUtil.isTestServer() && !projectBean.getTag().equals("PCP PPI Cortex")) {
					continue;
				}
				if (projectsToLoad.contains(projectBean.getTag())) {
					try {
						session = ContextualSessionHandler.getCurrentSession();
						session.beginTransaction();
						final long t1 = System.currentTimeMillis();
						log.info("Pre loading project: " + projectBean.getTag());
						final Set<String> projectTagSet = new THashSet<String>();
						projectTagSet.add(projectBean.getTag());
						proteinRetrieval.getProteinsFromProjects(sessionID, projectTagSet, null, false, null, false);
						final double t2 = (System.currentTimeMillis() * 1.0 - t1 * 1.0) / 1000;
						log.info(projectBean.getTag() + " pre loaded in " + myFormatter.format(t2) + " seconds");
					} catch (final Exception e) {
						e.printStackTrace();
						session.getTransaction().rollback();
					} finally {
						session.close();
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
