package edu.scripps.yates.server.daemon.tasks;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServletContextProperty;

public abstract class PintServerDaemonTask extends Thread {
	protected final Logger log = Logger.getLogger(PintServerDaemonTask.class);
	protected final UniprotProteinRetrievalSettings urs;

	protected int numRuns = 0;
	protected ServletContext servletContext;

	public String getTaskName() {
		return getClass().getCanonicalName();
	}

	public PintServerDaemonTask(ServletContext servletContext) {
		this.servletContext = servletContext;
		// configure the Uniprot annotations retrieval for using the local
		// folder and whether to index or not the annotations file, all
		// configured in the servlet context (web.xml file)
		FileManager.getProjectFilesPath(servletContext);

		// use the index
		final boolean useIndex = Boolean.valueOf(ServletContextProperty.getServletContextProperty(servletContext,
				ServletContextProperty.INDEX_UNIPROT_ANNOTATIONS));
		File uniprotReleasesFolder = FileManager.getUniprotReleasesFolder();
		urs = UniprotProteinRetrievalSettings.getInstance(uniprotReleasesFolder, useIndex);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public abstract void run();

	public void startRun() {
		ContextualSessionHandler.openSession();
		run();
		numRuns++;
	}

	public abstract boolean justRunOnce();

	public int getNumRuns() {
		return numRuns;
	}
}
