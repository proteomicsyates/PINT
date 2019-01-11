package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getOMIMKey( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setOMIMKey( java.lang.String omimKey, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getAdminPassword( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setAdminPassword( java.lang.String adminPassword, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getPintConfigurationProperties( AsyncCallback<edu.scripps.yates.shared.configuration.PintConfigurationProperties> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getDBPassword( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setDBPassword( java.lang.String dbPassword, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getDBURL( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setDBURL( java.lang.String dbURL, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getDBUserName( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setDBUserName( java.lang.String dbUserName, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getProjectToPreLoad( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setProjectsToPreload( java.lang.String projectsToPreload, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void getProjectToNotPreLoad( AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setProjectsToNotPreload( java.lang.String projectsToNotPreload, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setPreLoadPublicProjects( boolean preLoadPublicProjects, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void isPreLoadPublicProjects( AsyncCallback<java.lang.Boolean> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void setPSMCentric( boolean psmCentric, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ConfigurationService
     */
    void isPSMCentric( AsyncCallback<java.lang.Boolean> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ConfigurationServiceAsync instance;

        public static final ConfigurationServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ConfigurationServiceAsync) GWT.create( ConfigurationService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
