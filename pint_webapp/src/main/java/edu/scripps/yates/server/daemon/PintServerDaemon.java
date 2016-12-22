package edu.scripps.yates.server.daemon;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.server.daemon.tasks.DeleteHiddenProjects;
import edu.scripps.yates.server.daemon.tasks.PintServerDaemonTask;
import edu.scripps.yates.server.daemon.tasks.ProteinUniprotAnnotationUpdater;
import edu.scripps.yates.shared.util.SharedConstants;

public class PintServerDaemon implements ServletContextListener {
	private static final Logger log = Logger.getLogger(PintServerDaemon.class);
	public static final int DELAY_IN_MINUTES = 30;

	private final List<PintServerDaemonTask> pintServerDaemonTasks = new ArrayList<PintServerDaemonTask>();
	private Timer timer;

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		// check database connection
		try {
			ContextualSessionHandler.openSession();
			ContextualSessionHandler.beginGoodTransaction();
			ContextualSessionHandler.finishGoodTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			log.error("Some error happened trying to initiate the database connection: " + e.getMessage());
			System.exit(-1);
		} finally {
			ContextualSessionHandler.closeSession();
		}
		//
		log.info("Starting PintServerDaemon...");
		final ServletContext servletContext = sce.getServletContext();

		if (SharedConstants.DAEMON_TASKS_ENABLED) {
			// /////////////////////////////////////////////////
			// REGISTER MAINTENANCE TASKS HERE
			// pintServerDaemonTasks.add(new
			// ProteinAccessionsUpdater(servletContext));
			pintServerDaemonTasks.add(new DeleteHiddenProjects(servletContext));
			// pintServerDaemonTasks.add(new
			// PreLoadPublicProjects("DAEMON_SESSION", servletContext));
			pintServerDaemonTasks.add(new ProteinUniprotAnnotationUpdater(servletContext));

			// pintServerDaemonTasks.add(new GeneInformationConsolidation(
			// servletContext));
			// pintServerDaemonTasks.add(new
			// SpectralCountMarker(servletContext));
			// pintServerDaemonTasks.add(new
			// BatchQueryExecutionTask(servletContext));
			// /////////////////////////////////////////////////
		}
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {

				log.info("Starting " + pintServerDaemonTasks.size() + " registered tasks in PintServerDaemon");
				for (PintServerDaemonTask task : pintServerDaemonTasks) {
					if (task.justRunOnce() && task.getNumRuns() > 0) {
						log.info("Task " + task.getName() + " is only configured to run once. Skipping it");
						continue;
					}
					log.info("Launching " + task.getName());
					// task.start();
					task.startRun();
				}
				log.info("All tasks launched");

				for (PintServerDaemonTask task : pintServerDaemonTasks) {
					try {
						log.info("Waiting for " + task.getTaskName() + " to be complete...");
						task.join();
						log.info(task.getTaskName() + " complete");
					} catch (InterruptedException e) {
						e.printStackTrace();
						log.info(task.getTaskName() + " interrupted");
					}

				}
				log.info("Scheduled run for registered tasks is finished");
				log.info("Next run in " + DELAY_IN_MINUTES + " minutes");
			}
		};
		timer = new Timer("PintServerDaemon", true);

		timer.scheduleAtFixedRate(timerTask, 0, DELAY_IN_MINUTES * 60 * 1000);

		// }
		// };
		// th.setDaemon(true);
		// th.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// you could notify your thread you're shutting down if
		// you need it to clean up after itself
		log.info("context destroyed at PintServerDaemon...");
		log.info("Interrupting all running tasks");
		for (PintServerDaemonTask task : pintServerDaemonTasks) {
			if (task.isAlive()) {
				log.info("Interrupting " + task.getName());
				task.interrupt();
			} else {
				log.info("Skipping task " + task.getName() + " because it was not alive");
			}
		}
		log.info("All task were interrupted");
		if (timer != null) {
			log.info("cancelling timer");
			try {
				timer.cancel();
				log.info("Timer cancelled");
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
		}
	}

	// private void syncronizeUniprotVersionsAndProjectsInDB() {
	// final Set<ProjectBean> projectBeans = RemoteServicesTasks
	// .getProjectBeans();
	// List<String> availableUniprotVersions = getUniprotVersionsOnServer();
	// for (ProjectBean projectBean : projectBeans) {
	// // get the uniprot versions available in the server, starting on the
	// // oldest one.
	// log.info("Checking uniprot version for project "
	// + projectBean.getTag());
	// for (int i = availableUniprotVersions.size() - 1; i >= 0; i--) {
	// String uniprotVersion = availableUniprotVersions.get(i);
	// log.info("Checking uniprot version " + uniprotVersion);
	// if (allProteinsAreInTheIndex(projectBean, uniprotVersion)) {
	// log.info("All proteins in " + projectBean.getTag()
	// + " are annotated with version " + uniprotVersion);
	// final Date uniprotReleaseDate = UniprotVersionReleasesDates
	// .getInstance(projectFilesPath)
	// .getUniprotReleaseDatesByVersions()
	// .get(uniprotVersion);
	// updateUploadDateOfProject(projectBean, uniprotReleaseDate);
	// } else {
	// log.info("Some protein in project " + projectBean.getTag()
	// + " is not annotated in uniprot version "
	// + uniprotVersion);
	// break;
	// }
	// }
	// }
	// }

	// private void updateUploadDateOfProject(ProjectBean projectBean,
	// Date uniprotReleaseDate) {
	// try {
	// ThreadSessionHandler.beginGoodTransaction();
	// final Project project = ThreadSessionHandler.load(
	// projectBean.getDbId(), Project.class);
	// if (project.getUploadedDate().compareTo(uniprotReleaseDate) < 0) {
	// Date newDate = new Date(uniprotReleaseDate.getYear(),
	// uniprotReleaseDate.getMonth(),
	// uniprotReleaseDate.getDay() + 1);
	// log.info("Updating release date of project "
	// + projectBean.getTag() + " to " + newDate
	// + " (one day after the uniprot version was released)");
	// project.setReleaseDate(newDate);
	// ThreadSessionHandler.saveOrUpdate(project);
	// }
	// } catch (Exception e) {
	// ThreadSessionHandler.rollbackTransaction();
	// } finally {
	// ThreadSessionHandler.closeSession();
	// }
	//
	// }

	// private boolean allProteinsAreInTheIndex(ProjectBean projectBean,
	// String uniprotVersion) {
	// try {
	// final UniprotProteinRetriever uplr = new UniprotProteinRetriever(
	// uniprotVersion, urs.getUniprotReleasesFolder(),
	// urs.isUseIndex());
	//
	// ThreadSessionHandler.beginGoodTransaction();
	// final Map<String, Set<Protein>> proteins = PreparedQueries
	// .getProteinsByProjectCondition(projectBean.getTag(), null);
	// final Set<String> accs = new HashSet<String>();
	// for (String acc : proteins.keySet()) {
	// final ProteinAccession primaryAccession = PersistenceUtils
	// .getPrimaryAccession(proteins.get(acc).iterator()
	// .next());
	// if (primaryAccession.isIsPrimary()
	// && primaryAccession.getAccessionType()
	// .equalsIgnoreCase(AccessionType.UNIPROT.name())) {
	// accs.add(primaryAccession.getAccession());
	// }
	// }
	// for (String acc : accs) {
	// final Map<String, edu.scripps.yates.model.Protein> annotatedProtein =
	// uplr
	// .getAnnotatedProtein(acc);
	// if (annotatedProtein == null || annotatedProtein.isEmpty())
	// return false;
	// }
	// return true;
	// } catch (Exception e) {
	// ThreadSessionHandler.rollbackTransaction();
	// } finally {
	// ThreadSessionHandler.closeSession();
	// }
	//
	// return false;
	// }
	//
	// private List<String> getUniprotVersionsOnServer() {
	// List<String> ret = new ArrayList<String>();
	// final File uniprotReleasesFolder = FileManager
	// .getUniprotReleasesFolder(projectFilesPath);
	// final File[] listFolders = uniprotReleasesFolder
	// .listFiles(new FileFilter() {
	//
	// @Override
	// public boolean accept(File pathname) {
	// if (pathname.isDirectory())
	// return true;
	// return false;
	// }
	// });
	// for (File uniprotVersionFolder : listFolders) {
	// ret.add(FilenameUtils.getName(uniprotVersionFolder
	// .getAbsolutePath()));
	// }
	// Collections.sort(ret);
	// return ret;
	// }
}
