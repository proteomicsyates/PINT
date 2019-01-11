package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OmimEntryBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -8606149359184574262L;
	private Integer id;
	private List<String> alternativeTitles = new ArrayList<String>();
	private String preferredTitle;

	public OmimEntryBean() {

	}

	public OmimEntryBean(Integer omimID) {
		id = omimID;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the alternativeTitles
	 */
	public List<String> getAlternativeTitles() {
		return alternativeTitles;
	}

	/**
	 * @param alternativeTitles
	 *            the alternativeTitles to set
	 */
	public void setAlternativeTitles(List<String> alternativeTitles) {
		this.alternativeTitles = alternativeTitles;
	}

	/**
	 * @return the preferredTitle
	 */
	public String getPreferredTitle() {
		return preferredTitle;
	}

	/**
	 * @param preferredTitle
	 *            the preferredTitle to set
	 */
	public void setPreferredTitle(String preferredTitle) {
		this.preferredTitle = preferredTitle;
	}

}
