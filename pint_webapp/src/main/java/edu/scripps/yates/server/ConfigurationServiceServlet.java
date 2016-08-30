package edu.scripps.yates.server;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.scripps.yates.client.ConfigurationService;
import edu.scripps.yates.server.configuration.ConfigurationPropertiesIO;
import edu.scripps.yates.server.configuration.PintConfigurationProperties;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServletCommonInit;

public class ConfigurationServiceServlet extends RemoteServiceServlet implements ConfigurationService {

	/**
	 *
	 */
	private static final long serialVersionUID = 3511051571769993339L;

	/*
	 * (non-Javadoc)
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
	public void setOMIMKey(String omimKey) {
		File propertiesFile = FileManager.getPintPropertiesFile();
		ConfigurationPropertiesIO.writeOmimKey(propertiesFile, omimKey);
	}

	@Override
	public String getAdminPassword() {
		return getAdminPasswordConfigurationProperty();
	}

	@Override
	public void setAdminPassword(String adminPassword) {
		File propertiesFile = FileManager.getPintPropertiesFile();
		ConfigurationPropertiesIO.writeAdminPassword(propertiesFile, adminPassword);

	}

	private String getOmimConfigurationProperty() {
		File propertiesFile = FileManager.getPintPropertiesFile();
		PintConfigurationProperties pintConf = ConfigurationPropertiesIO.readProperties(propertiesFile);
		return pintConf.getOmimKey();
	}

	private String getAdminPasswordConfigurationProperty() {
		File propertiesFile = FileManager.getPintPropertiesFile();
		PintConfigurationProperties pintConf = ConfigurationPropertiesIO.readProperties(propertiesFile);
		return pintConf.getAdminPassword();
	}
}
