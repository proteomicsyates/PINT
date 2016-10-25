package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

public class ProteinThresholdAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3053740333115166413L;
	private final Threshold threshold;
	private final Protein protein;

	public ProteinThresholdAdapter(Threshold threshold2, Protein ret) {
		threshold = threshold2;
		protein = ret;
	}

	@Override
	public ProteinThreshold adapt() {
		ProteinThreshold ret = new ProteinThreshold();

		ret.setPassThreshold(threshold.isPassThreshold());
		ret.setThreshold(new ThresholdAdapter(threshold, ret).adapt());
		ret.setProtein(protein);

		return ret;
	}

}
