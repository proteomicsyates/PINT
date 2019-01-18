package edu.scripps.yates.server.daemon.tasks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLDeleter;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
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
			final List<String> previouslyTried = getLatestProjectsTriedToBeDeleted(servletContext);

			ContextualSessionHandler.beginGoodTransaction();
			final Comparator<ProjectBean> comparatorByID = new Comparator<ProjectBean>() {

				@Override
				public int compare(ProjectBean o1, ProjectBean o2) {
					return o2.getDbId().compareTo(o1.getDbId());
				}
			};
			final List<String> projectTags = RemoteServicesTasks.getProjectBeans(true).stream()
					.filter(project -> project.isHidden()).sorted(comparatorByID).map(project -> project.getTag())
					.collect(Collectors.toList());
			ContextualSessionHandler.finishGoodTransaction();
			for (final String projectTag : projectTags) {
				if (!previouslyTried.contains(projectTag)) {
					try {
						ContextualSessionHandler.beginGoodTransaction();
						log.info("Deleting hidden project " + projectTag + " from daemon");
						final MySQLDeleter mySQLDeleter = new MySQLDeleter();
						mySQLDeleter.deleteProject(projectTag);
						log.info("Finishing transaction");
						ContextualSessionHandler.finishGoodTransaction();
						ContextualSessionHandler.closeSession();
						log.info("transaction finished");
						log.info(projectTag + " deleted from DB");
						// if it was in the list of previously projects, remove
						// it from the list and save the list
						if (previouslyTried.contains(projectTag)) {
							previouslyTried.remove(projectTag);
							setProjectListOfPReviouslyTriedProjects(previouslyTried);
						}
					} catch (final Exception e) {
						e.printStackTrace();
						ContextualSessionHandler.rollbackTransaction();
						addProjectToListOfPreviouslyTriedProjects(projectTag);
					}
				}
			}
		} catch (final IOException e1) {
			e1.printStackTrace();
		} finally {
			// Close the Session
			ContextualSessionHandler.closeSession();
		}
	}

	private void setProjectListOfPReviouslyTriedProjects(List<String> previouslyTried) throws IOException {
		final File file = FileManager.getLatestDeletedProjectsFile(servletContext);
		BufferedWriter output = null;

		try {
			output = new BufferedWriter(new FileWriter(file, false));
			for (final String projectTag : previouslyTried) {
				output.write(projectTag + "\n");
			}
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	private void addProjectToListOfPreviouslyTriedProjects(String projectTag) throws IOException {
		final File file = FileManager.getLatestDeletedProjectsFile(servletContext);
		BufferedWriter output = null;

		try {
			output = new BufferedWriter(new FileWriter(file, true));
			output.write(projectTag + "\n");
		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

	private List<String> getLatestProjectsTriedToBeDeleted(ServletContext servletContext) throws IOException {
		final File file = FileManager.getLatestDeletedProjectsFile(servletContext);
		if (!file.exists()) {
			return Collections.emptyList();
		}
		Stream<String> stream = null;
		try {
			stream = Files.lines(file.toPath()).map(line -> line.trim());
			final List<String> projectPaths = stream.collect(Collectors.toList());
			return projectPaths;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	@Override
	public boolean justRunOnce() {
		return false;
	}

}
