package edu.scripps.yates.shared.thirdparty.pseaquant;

import java.io.Serializable;

public class PSEAQuantResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3625210000493077856L;
	private String linkToRatios;
	private String linkToResults;
	private String errorMessage;

	public PSEAQuantResult() {

	}

	/**
	 * @return the linkToRatios
	 */
	public String getLinkToRatios() {
		return linkToRatios;
	}

	/**
	 * @param linkToRatios
	 *            the linkToRatios to set
	 */
	public void setLinkToRatios(String linkToRatios) {
		this.linkToRatios = linkToRatios;
	}

	/**
	 * @return the linkToResults
	 */
	public String getLinkToResults() {
		return linkToResults;
	}

	/**
	 * @param linkToResults
	 *            the linkToResults to set
	 */
	public void setLinkToResults(String linkToResults) {
		this.linkToResults = linkToResults;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
