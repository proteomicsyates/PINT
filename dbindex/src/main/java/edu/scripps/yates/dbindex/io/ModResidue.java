/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.scripps.yates.dbindex.io;

/**
 * 
 * @author rpark
 */

public class ModResidue {

	private char residue;
	private double massShift;

	public ModResidue(char residue, double massShift) {
		this.residue = residue;
		this.massShift = massShift;
	}

	@Override
	public String toString() {
		return "ModResidue{" + "residue=" + residue + ", massShift="
				+ massShift + '}';
	}

	public char getResidue() {
		return residue;
	}

	public void setResidue(char residue) {
		this.residue = residue;
	}

	public void setMassShift(double massShift) {
		this.massShift = massShift;
	}

	public double getMassShift() {
		return massShift;
	}

}
