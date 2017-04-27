package edu.scripps.yates.shared.configuration;

import java.io.Serializable;

public class PintConfigurationProperties implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8891097817678540987L;
	private String adminPassword;
	private String omimKey;

	private String db_username;
	private String db_password;
	private String db_url;
	private String projectFilesPath;
	private Boolean preLoadPublicProjects;
	private String projectsToPreLoad;
	private String projectsToNotPreLoad;

	public PintConfigurationProperties() {

	}

	public String getDb_username() {
		return db_username;
	}

	public void setDb_username(String db_username) {
		this.db_username = db_username;
	}

	public String getDb_password() {
		return db_password;
	}

	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}

	public String getDb_url() {
		return db_url;
	}

	public void setDb_url(String db_url) {
		this.db_url = db_url;
	}

	public String getProjectFilesPath() {
		return projectFilesPath;
	}

	public void setProjectFilesPath(String projectFilesPath) {
		this.projectFilesPath = projectFilesPath;
	}

	public Boolean isPreLoadPublicProjects() {
		return preLoadPublicProjects;
	}

	public void setPreLoadPublicProjects(Boolean preLoadPublicProjects) {
		this.preLoadPublicProjects = preLoadPublicProjects;
	}

	public String getProjectsToPreLoad() {
		return projectsToPreLoad;
	}

	public void setProjectsToPreLoad(String projectsToPreLoad) {
		this.projectsToPreLoad = projectsToPreLoad;
	}

	public String getProjectsToNotPreLoad() {
		return projectsToNotPreLoad;
	}

	public void setProjectsToNotPreLoad(String projectsToNotPreLoad) {
		this.projectsToNotPreLoad = projectsToNotPreLoad;
	}

	/**
	 * @return the adminPassword
	 */
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * @param adminPassword
	 *            the adminPassword to set
	 */
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	/**
	 * @return the omimKey
	 */
	public String getOmimKey() {
		return omimKey;
	}

	/**
	 * @param omimKey
	 *            the omimKey to set
	 */
	public void setOmimKey(String omimKey) {
		this.omimKey = omimKey;
	}

	public boolean isSomeConfigurationMissing() {

		if (adminPassword == null || "".equals(adminPassword)) {
			return true;
		}
		// if (omimKey == null || "".equals(omimKey)) {
		// return true;
		// }
		// if (this.preLoadPublicProjects == null) {
		// return true;
		// }
		if (this.db_password == null || "".equals(db_password)) {
			return true;
		}
		if (this.db_url == null || "".equals(db_url)) {
			return true;
		}
		if (this.db_username == null || "".equals(db_username)) {
			return true;
		}
		if (this.projectFilesPath == null || "".equals(projectFilesPath)) {
			return true;
		}
		// if (this.projectsToNotPreLoad == null ||
		// "".equals(projectsToNotPreLoad)) {
		// return true;
		// }
		// if (this.projectsToPreLoad == null || "".equals(projectsToPreLoad)) {
		// return true;
		// }

		return false;
	}
}