package edu.scripps.yates;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.scripps.yates.shared.configuration.PintConfigurationProperties;
import edu.scripps.yates.shared.exceptions.PintException;

@RemoteServiceRelativePath("configuration")
public interface ConfigurationService extends RemoteService {
	String getOMIMKey();

	void setOMIMKey(String omimKey) throws PintException;

	String getAdminPassword();

	void setAdminPassword(String adminPassword) throws PintException;

	PintConfigurationProperties getPintConfigurationProperties();

	String getDBPassword();

	void setDBPassword(String dbPassword) throws PintException;

	String getDBURL();

	void setDBURL(String dbURL) throws PintException;

	String getDBUserName();

	void setDBUserName(String dbUserName) throws PintException;

	String getProjectToPreLoad();

	void setProjectsToPreload(String projectsToPreload) throws PintException;

	String getProjectToNotPreLoad();

	void setProjectsToNotPreload(String projectsToNotPreload) throws PintException;

	void setPreLoadPublicProjects(boolean preLoadPublicProjects);

	Boolean isPreLoadPublicProjects();

	void setPSMCentric(boolean psmCentric);

	Boolean isPSMCentric();

}
