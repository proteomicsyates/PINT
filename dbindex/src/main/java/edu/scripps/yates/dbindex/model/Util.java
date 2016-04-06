/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.scripps.yates.dbindex.model;

/**
 * General utility class
 */
public class Util {

    /**
     * Write a log message to console
     *
     * @param msg message to be printed to console
     */
    public static void log(String msg) {
        System.out.println(msg);
    }

    public static boolean strContainsChar(String theString, char theChar) {
        return theString.indexOf(theChar) != -1;
    }

    /**
     * Get base name without extension part
     *
     * @param path
     * @return
     */
    public static String getFileBaseName(String path) {
        int dotI = path.lastIndexOf(".");
        if (dotI == -1) {
            return path;
        }
        
        return path.substring(0, dotI) + ".";
    }
}
