package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ThresholdImpl implements Threshold {
	protected static TIntObjectHashMap<Threshold> thresholdsMap = new TIntObjectHashMap<Threshold>();

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
