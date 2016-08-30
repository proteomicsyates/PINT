package edu.scripps.yates.client.gui.configuration;

public class ConfigurationClientManager {
	private static ConfigurationClientManager instance;
	private String omimKey;
	private String adminPassword;

	private ConfigurationClientManager() {

	}

	public static ConfigurationClientManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationClientManager();
		}
		return instance;
	}

	public void setContainsAdmin(String adminPass) {
		adminPassword = adminPass;
	}

	public void setOMIMKey(String omimKey) {
		this.omimKey = omimKey;
	}

	public boolean isConfigurationCheckingIsFinished() {
		if (adminPassword == null || omimKey == null) {
			return false;
		}
		return true;
	}

	public boolean isSomeConfigurationMissing() {
		if (!isConfigurationCheckingIsFinished()) {
			return true;
		} else {
			if (adminPassword == null || "".equals(adminPassword)) {
				return true;
			}
			if (omimKey == null || "".equals(omimKey)) {
				return true;
			}
		}
		return false;
	}

	public String getAdminPassword() {
		return adminPassword;
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

	/**
	 * @param adminPassword
	 *            the adminPassword to set
	 */
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
}
