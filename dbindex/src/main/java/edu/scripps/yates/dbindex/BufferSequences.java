/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Harshil
 */
public class BufferSequences 
{
    private String seq;
    private int length;
    private String resLeft ;
    private String resRight;
    private List protId = new ArrayList();


    /**
     * @return the seq
     */
    public void printObject()
    {
        System.out.println("SEQ: " + seq + " protid: "   + getProtId() );
    }
    public String getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(String seq) {
        this.seq = seq;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the resLeft
     */
    public String getResLeft() {
        return resLeft;
    }

    /**
     * @param resLeft the resLeft to set
     */
    public void setResLeft(String resLeft) {
        this.resLeft = resLeft;
    }

    /**
     * @return the resRight
     */
    public String getResRight() {
        return resRight;
    }

    /**
     * @param resRight the resRight to set
     */
    public void setResRight(String resRight) {
        this.resRight = resRight;
    }

    /**
     * @return the protId
     */
    public List getProtId() {
        return protId;
    }

    /**
     * @param protId the protId to set
     */
    public void setProtId(List protId) {
        this.protId = protId;
    }

    void addprotId(int proteinId) 
    {
       this.protId.add(proteinId);
    }

    void setData(String sequence, int sequenceLen, String resLeft, String resRight, int proteinId)
    {
        this.seq=sequence;this.length=(sequenceLen);this.resLeft=resLeft;this.resRight=resRight;this.protId.add(proteinId);
    }

    
    
    
}
