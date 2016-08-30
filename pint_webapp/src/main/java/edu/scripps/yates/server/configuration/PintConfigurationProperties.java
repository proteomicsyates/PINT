package edu.scripps.yates.server.configuration;

public class PintConfigurationProperties {
	private String adminPassword;
	private String omimKey;

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

}
