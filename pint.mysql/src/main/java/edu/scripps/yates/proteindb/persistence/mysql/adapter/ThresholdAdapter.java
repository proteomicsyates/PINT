package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;

public class ThresholdAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.Threshold>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6872165014895642210L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Threshold threshold;
	private final ProteinThreshold hibAppliedThreshold;
	private final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.Threshold> map = new HashMap<String, edu.scripps.yates.proteindb.persistence.mysql.Threshold>();

	public ThresholdAdapter(edu.scripps.yates.utilities.proteomicsmodel.Threshold threshold,
			ProteinThreshold hibAppliedThreshold) {
		this.threshold = threshold;
		this.hibAppliedThreshold = hibAppliedThreshold;
	}

	@Override
	public edu.scripps.yates.proteindb.persistence.mysql.Threshold adapt() {
		edu.scripps.yates.proteindb.persistence.mysql.Threshold ret = new edu.scripps.yates.proteindb.persistence.mysql.Threshold();
		if (map.containsKey(threshold.getName() + threshold.getDescription()))
			return map.get(threshold.getName() + threshold.getDescription());
		map.put(threshold.getName() + threshold.getDescription(), ret);
		ret.setDescription(threshold.getDescription());
		ret.setName(threshold.getName());
		ret.getProteinThresholds().add(hibAppliedThreshold);

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
