
package edu.scripps.yates.dbindex;

/**
 * Exception thrown if there is a problem with the index
 * 
 * @author Adam
 */
public class DBIndexerException extends Exception {

    public DBIndexerException(String message) {
        super(message);
    }

    public DBIndexerException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
