/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.scripps.yates.dbindex.model;

/**
 *
 * @author rpark
 */
public class DiffModification {
    public static final int SIZE= 256;
    private static double[] diffMod = new double[SIZE];
    private static boolean[] isDiffMod = new boolean[SIZE];
    //public static void diffModTrue(char ch) {
    //    isDiffMod[ch] = true;
   // }
    
    public static boolean isDiffMod(char ch) {
        return isDiffMod[ch];
    }
    
    public static void setDiffModMass(char ch, double mass) {
        diffMod[ch] = mass;
        isDiffMod[ch] = true;
    }
    
    public static double getDiffModMass(char ch) {
        return diffMod[ch];
    }
    
    public static void test() {
        for(boolean b:isDiffMod )
            System.out.println("==" + b);
    }

    public static double[] getDiffMod() {
        return diffMod;
    }

    public static void setDiffMod(double[] diffMod) {
        DiffModification.diffMod = diffMod;
    }

    public static boolean[] getIsDiffMod() {
        return isDiffMod;
    }

    public static void setIsDiffMod(boolean[] isDiffMod) {
        DiffModification.isDiffMod = isDiffMod;
    }

    
}
