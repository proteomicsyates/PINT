package edu.scripps.yates.proteindb.persistence.mysql.impl;

import edu.scripps.yates.utilities.proteomicsmodel.Label;

public class LabelImpl implements Label {
	private final edu.scripps.yates.proteindb.persistence.mysql.Label label;

	public LabelImpl(edu.scripps.yates.proteindb.persistence.mysql.Label label) {
		this.label = label;
	}

	@Override
	public String getName() {
		return label.getName();
	}

	@Override
	public Double getMassDiff() {
		return label.getMassDiff();
	}

}
