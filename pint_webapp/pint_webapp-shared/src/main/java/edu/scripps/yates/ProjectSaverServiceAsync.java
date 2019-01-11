package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProjectSaverServiceAsync
{

    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ProjectSaverService
     */
    void saveProject( java.lang.String sessionID, java.util.List<java.lang.String> projectXmlFileName, AsyncCallback<Void> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ProjectSaverService
     */
    void deleteProject( java.lang.String projectTag, AsyncCallback<java.lang.String> callback );


    /**
     * GWT-RPC service  asynchronous (client-side) interface
     * @see edu.scripps.yates.ProjectSaverService
     */
    void validateProjectXMLCfgFile( java.lang.String sessionID, java.lang.String projectFileName, AsyncCallback<java.util.List<java.lang.String>> callback );


    /**
     * Utility class to get the RPC Async interface from client-side code
     */
    public static final class Util 
    { 
        private static ProjectSaverServiceAsync instance;

        public static final ProjectSaverServiceAsync getInstance()
        {
            if ( instance == null )
            {
                instance = (ProjectSaverServiceAsync) GWT.create( ProjectSaverService.class );
            }
            return instance;
        }

        private Util()
        {
            // Utility class should not be instantiated
        }
    }
}
