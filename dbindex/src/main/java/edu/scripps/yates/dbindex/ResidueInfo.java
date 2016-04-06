package edu.scripps.yates.dbindex;

/**
 *
 * Represents residue information for a peptide
 * 
 * @author Adam
 */
public class ResidueInfo {
    
    private String residueLeft;
    private String residueRight;

    public ResidueInfo(String residueLeft, String residueRight) {
        this.residueLeft = residueLeft;
        this.residueRight = residueRight;
    }

    public String getResidueLeft() {
        return residueLeft;
    }

    public String getResidueRight() {
        return residueRight;
    }

    
    
    @Override
    public String toString() {
        return "ResidueInfo{" + "residueLeft=" + residueLeft + ", residueRight=" + residueRight + '}';
    }
    
    
    
}
