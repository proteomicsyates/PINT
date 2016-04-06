package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.Parameter;

public class MySQLProteinDBInterface {
	private static final Logger log = Logger
			.getLogger(MySQLProteinDBInterface.class);

	public static edu.scripps.yates.proteindb.persistence.mysql.Project getDBProjectByName(
			String name) {

		List<Parameter<String>> listParameter = new ArrayList<Parameter<String>>();
		listParameter.add(new Parameter<String>("name", name));
		try {
			final List<edu.scripps.yates.proteindb.persistence.mysql.Project> retrieveList = ContextualSessionHandler
					.retrieveList(
							listParameter,
							edu.scripps.yates.proteindb.persistence.mysql.Project.class);
			if (!retrieveList.isEmpty()) {
				return retrieveList.get(0);
			}

		} catch (HibernateException e) {
			log.warn(e.getMessage());
			ContextualSessionHandler.rollbackTransaction();
		} finally {
		}
		return null;
	}

	public static edu.scripps.yates.proteindb.persistence.mysql.Project getDBProjectByTag(
			String tag) {

		List<Parameter<String>> listParameter = new ArrayList<Parameter<String>>();
		listParameter.add(new Parameter<String>("tag", tag));
		try {
			final List<edu.scripps.yates.proteindb.persistence.mysql.Project> retrieveList = ContextualSessionHandler
					.retrieveList(
							listParameter,
							edu.scripps.yates.proteindb.persistence.mysql.Project.class);
			if (!retrieveList.isEmpty()) {
				return retrieveList.get(0);
			}

		} catch (HibernateException e) {
			log.warn(e.getMessage());
			ContextualSessionHandler.rollbackTransaction();
		} finally {
		}
		return null;
	}

	public static edu.scripps.yates.proteindb.persistence.mysql.Project getDBProjectById(
			int projectId) {

		try {
			final edu.scripps.yates.proteindb.persistence.mysql.Project project = ContextualSessionHandler
					.load(projectId,
							edu.scripps.yates.proteindb.persistence.mysql.Project.class);
			return project;
		} catch (HibernateException e) {
			log.warn(e.getMessage());
			ContextualSessionHandler.rollbackTransaction();
		} finally {
		}
		return null;
	}

}
