package edu.scripps.yates.shared.model;

import java.io.Serializable;

public class PTMSiteBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4135959343681112419L;
	private String aa;
	private int position;
	private ScoreBean score;

	public PTMSiteBean() {

	}

	/**
	 * @return the aa
	 */
	public String getAa() {
		return aa;
	}

	/**
	 * @param aa
	 *            the aa to set
	 */
	public void setAa(String aa) {
		this.aa = aa;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	public PTMSiteBean(String aa, int position) {
		this.aa = aa;
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public ScoreBean getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(ScoreBean score) {
		this.score = score;
	}

}
