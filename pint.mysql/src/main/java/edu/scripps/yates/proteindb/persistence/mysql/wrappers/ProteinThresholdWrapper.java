package edu.scripps.yates.proteindb.persistence.mysql.wrappers;

public class ProteinThresholdWrapper {
	private final int id;
	private final int proteinID;
	private final int thresholdID;
	private final boolean pass;

	public ProteinThresholdWrapper(Object[] threeObjects) {
		id = Integer.valueOf(threeObjects[0].toString());
		proteinID = Integer.valueOf(threeObjects[1].toString());
		thresholdID = Integer.valueOf(threeObjects[2].toString());
		pass = Boolean.valueOf(threeObjects[3].toString());
	}

	public int getProteinID() {
		return proteinID;
	}

	public int getThresholdID() {
		return thresholdID;
	}

	public boolean isPass() {
		return pass;
	}

	public int getId() {
		return id;
	}
}
