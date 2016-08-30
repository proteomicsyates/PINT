package edu.scripps.yates.server.util;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class ServletContextProperty {
	private static final Logger log = Logger.getLogger(ServletContextProperty.class);
	public static final String PROJECT_FILES_PATH = "projectFilesPath";
	public static final String PROJECT_FILES_PATH_SERVER = "projectFilesPathServer";
	public static final String DOWNLOAD_FILES_PATH = "downloadFilesPath";
	public static final String DOWNLOAD_FILES_PATH_SERVER = "downloadFilesPathServer";
	public static final String INDEX_UNIPROT_ANNOTATIONS = "indexUniprotAnnotations";
	public static final String PRELOAD_PUBLIC_PROJECTS = "preLoadPublicProjects";
	public static final String PROJECTS_TO_PRELOAD = "projectsToPreLoad";
	public static final String PROJECTS_TO_NOT_PRELOAD = "projectsToNotPreLoad";

	public static String getServletContextProperty(ServletContext servletContext, String propertyName) {

		log.info("Getting value from servlet property: " + propertyName);
		try {
			String propertyValue = servletContext.getInitParameter(propertyName);
			log.info(propertyName + "=" + propertyValue);
			return propertyValue;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
