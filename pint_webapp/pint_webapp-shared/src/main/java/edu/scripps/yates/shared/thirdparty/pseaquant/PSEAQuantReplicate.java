package edu.scripps.yates.shared.thirdparty.pseaquant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PSEAQuantReplicate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9014125347630708257L;

	private List<Map<String, String>> listOfPairs = new ArrayList<Map<String, String>>();

	private boolean msRun;
	private boolean condition;

	public PSEAQuantReplicate() {

	}

	/**
	 * @return the listOfPairs
	 */
	public List<Map<String, String>> getListOfPairs() {
		return listOfPairs;
	}

	/**
	 * @param listOfPairs
	 *            the listOfPairs to set
	 */
	public void setListOfPairs(List<Map<String, String>> listOfPairs) {
		this.listOfPairs = listOfPairs;
	}

	public void addCondition(String projectTag, String conditionName) {
		condition = true;
		msRun = false;
		Map<String, String> map = new HashMap<String, String>();
		map.put(projectTag, conditionName);
		listOfPairs.add(map);
	}

	public void addMSRun(String projectTag, String msRunID) {
		condition = false;
		msRun = true;
		Map<String, String> map = new HashMap<String, String>();
		map.put(projectTag, msRunID);
		listOfPairs.add(map);
	}

	/**
	 * @return the msRun
	 */
	public boolean isMsRun() {
		return msRun;
	}

	/**
	 * @param msRun
	 *            the msRun to set
	 */
	public void setMsRun(boolean msRun) {
		this.msRun = msRun;
	}

	/**
	 * @return the condition
	 */
	public boolean isCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(boolean condition) {
		this.condition = condition;
	}
}
