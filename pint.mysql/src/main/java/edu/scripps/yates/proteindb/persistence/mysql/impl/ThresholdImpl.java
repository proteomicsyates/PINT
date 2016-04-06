package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.HashMap;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

public class ThresholdImpl implements Threshold {
	protected static HashMap<Integer, Threshold> thresholdsMap = new HashMap<Integer, Threshold>();

	private final ProteinThreshold hibProteinThreshold;

	public ThresholdImpl(ProteinThreshold hibProteinThreshold) {
		this.hibProteinThreshold = hibProteinThreshold;
		thresholdsMap.put(hibProteinThreshold.getId(), this);
	}

	@Override
	public boolean isPassThreshold() {
		return hibProteinThreshold.isPassThreshold();
	}

	@Override
	public String getName() {
		return hibProteinThreshold.getThreshold().getName();
	}

	@Override
	public String getDescription() {
		return hibProteinThreshold.getThreshold().getDescription();
	}

}
