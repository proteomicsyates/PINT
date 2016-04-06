/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.scripps.yates.dbindex;

/**
 *
 * @author rpark2
 */
public class IndexedPeptideModel implements Comparable<IndexedPeptideModel> {
    
    private StringBuffer proteinIds=new StringBuffer();
    private double mass;
    private String sequence;
    private int startPos;
    private int offset;

    public void merge(IndexedPeptideModel pm) {
        this.proteinIds.append(pm.getProteinIds());
        
    }
    
    public IndexedPeptideModel(double mass, String proteinId, int startPos, int offset) {
        this.mass = mass;
        this.proteinIds.append(proteinId).append(",");    
        this.startPos = startPos;
        this.offset = offset;
    }
    
    /*
    public IndexedPeptideModel(long proteinId, String sequence, int startPos, int offset) {
        this.proteinIds.append(proteinId).append(",");
        this.sequence = sequence;
        this.startPos = startPos;
        this.offset = offset;
    }*/
    

    public String getProteinIds() {
        return this.proteinIds.toString();
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    
    public int compareTo(IndexedPeptideModel pm) {
        double diff = this.mass - pm.getMass();
        
        if( diff == 0 ) {
            return 0; 
        } else if(diff < 0) {
            return -1;
    
        } else {
            return 1;
        }
    }
    
}
