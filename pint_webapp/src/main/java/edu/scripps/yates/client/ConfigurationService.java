package edu.scripps.yates.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("configuration")
public interface ConfigurationService extends RemoteService {
	String getOMIMKey();

	void setOMIMKey(String omimKey);

	String getAdminPassword();

	void setAdminPassword(String adminPassword);
}
