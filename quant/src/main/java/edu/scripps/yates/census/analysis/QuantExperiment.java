package edu.scripps.yates.census.analysis;

import java.util.ArrayList;
import java.util.List;

public class QuantExperiment {
	private final List<QuantReplicate> replicates = new ArrayList<QuantReplicate>();
	private final String name;

	public QuantExperiment(String name) {
		super();
		this.name = name;
	}

	public void addReplicate(QuantReplicate rep) {
		replicates.add(rep);
	}

	/**
	 * @return the replicates
	 */
	public List<QuantReplicate> getReplicates() {
		return replicates;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuantExperiment [" + name + "]";
	}

	public void setChargeStateSensible(boolean chargeSensible) {
		for (QuantReplicate quantReplicate : replicates) {
			quantReplicate.setChargeStateSensible(chargeSensible);
		}

	}
}
