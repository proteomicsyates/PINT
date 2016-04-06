package edu.scripps.yates.proteindb.persistence.mysql.access;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;

public class ProteinPsm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7001225269571453678L;
	private String acc;
	private String sequence;
	private Set<Condition> conditions;

	public ProteinPsm() {

	}

	public ProteinPsm(String acc, String sequence) {
		super();
		this.acc = acc;
		this.sequence = sequence;
	}

	/**
	 * @return the acc
	 */
	public String getAcc() {
		return acc;
	}

	/**
	 * @param acc
	 *            the acc to set
	 */
	public void setAcc(String acc) {
		this.acc = acc;
	}

	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the conditions
	 */
	public Set<Condition> getConditions() {
		if (conditions == null) {
			conditions = new HashSet<Condition>();
		}
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(Set<Condition> conditions) {
		this.conditions = conditions;
	}

}
