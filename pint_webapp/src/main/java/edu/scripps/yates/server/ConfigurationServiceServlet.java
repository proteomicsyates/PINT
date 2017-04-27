package edu.scripps.yates.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import edu.scripps.yates.client.ConfigurationService;
import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.client.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.server.configuration.PintConfigurationPropertiesIO;
import edu.scripps.yates.server.util.ServerUtil;
import edu.scripps.yates.server.util.ServletCommonInit;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;

public class ConfigurationServiceServlet extends RemoteServiceServlet implements ConfigurationService {

	/**
	 *
	 */
	private static final long serialVersionUID = 3511051571769993339L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.server.rpc.RemoteServiceServlet#init(javax.servlet.
	 * ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletCommonInit.init(getServletContext());

	}

	@Override
	public String getOMIMKey() {
		return getOmimConfigurationProperty();
	}

	@Override
	public void setOMIMKey(String omimKey) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeOmimKey(omimKey, ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public String getAdminPassword() {

		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getAdminPassword();
	}

	@Override
	public void setAdminPassword(String adminPassword) throws PintException {
		PintConfigurationPropertiesIO.writeAdminPassword(adminPassword,
				ServerUtil.getPINTPropertiesFile(getServletContext()));
	}

	@Override
	public PintConfigurationProperties getPintConfigurationProperties() {
		return PintConfigurationPropertiesIO.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
	}

	private String getOmimConfigurationProperty() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getOmimKey();
	}

	@Override
	public String getDBPassword() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getDb_password();
	}

	@Override
	public void setDBPassword(String dbPassword) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeDBPassword(dbPassword,
					ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public String getDBURL() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getDb_url();
	}

	@Override
	public void setDBURL(String dbURL) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeDBURL(dbURL, ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			if (e.getCause() instanceof CommunicationsException) {
				throw new PintException("Error trying to reach the server with URL '" + dbURL + "'",
						PINT_ERROR_TYPE.DB_ACCESS_ERROR);
			}
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public String getDBUserName() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getDb_username();
	}

	@Override
	public void setDBUserName(String dbUserName) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeDBUserName(dbUserName,
					ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public String getProjectFilesPath() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getProjectFilesPath();
	}

	@Override
	public void setProjectFilesPath(String projectFilesPath) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeProjectFilesPath(projectFilesPath,
					ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public String getProjectToPreLoad() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getProjectsToPreLoad();
	}

	@Override
	public void setProjectsToPreload(String projectsToPreload) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeProjectsToPreload(projectsToPreload,
					ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public String getProjectToNotPreLoad() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.getProjectsToNotPreLoad();
	}

	@Override
	public void setProjectsToNotPreload(String projectsToNotPreload) throws PintException {
		try {
			PintConfigurationPropertiesIO.writeProjectsToNotPreload(projectsToNotPreload,
					ServerUtil.getPINTPropertiesFile(getServletContext()));
		} catch (Exception e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		}
	}

	@Override
	public Boolean isPreLoadPublicProjects() {
		PintConfigurationProperties pintConf = PintConfigurationPropertiesIO
				.readProperties(ServerUtil.getPINTPropertiesFile(getServletContext()));
		return pintConf.isPreLoadPublicProjects();
	}

	@Override
	public void setPreLoadPublicProjects(boolean preLoadPublicProjects) {
		PintConfigurationPropertiesIO.writePreLoadPublicProjects(preLoadPublicProjects,
				ServerUtil.getPINTPropertiesFile(getServletContext()));
	}
}
