package edu.scripps.yates.server.util;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;

/**
 * This class contains a common method to run from all servlets in the init()
 * method.
 *
 * @author Salva
 *
 */
public class ServletCommonInit {
	private final static Logger log = Logger.getLogger(ServletCommonInit.class);

	public static void init(ServletContext context) {
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
		// configure the Uniprot annotations retrieval for using the local
		// folder and whether to index or not the annotations file, all
		// configured in the servlet context (web.xml file)
		FileManager.getProjectFilesPath(context);

		// use the index
		final boolean useIndex = Boolean.valueOf(ServletContextProperty.getServletContextProperty(context,
				ServletContextProperty.INDEX_UNIPROT_ANNOTATIONS));
		// uniprot releases folder
		final File uniprotReleasesFolder = FileManager.getUniprotReleasesFolder();
		// configure the UniprotProteinRetrievalSettings
		UniprotProteinRetrievalSettings.getInstance(uniprotReleasesFolder, useIndex);
	}
}
