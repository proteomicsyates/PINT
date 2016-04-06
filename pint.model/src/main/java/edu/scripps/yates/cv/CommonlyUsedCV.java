package edu.scripps.yates.cv;

public enum CommonlyUsedCV {
	confidenceScoreID("MS:1001193"), psmPValueID("MS:1002352"), XCorrID(
			"MS:1001155"), DeltaCNID("MS:1001156"), quantificationPValueID(
			"MS:1002072"), SEARCH_ENGINE_SPECIFIC_SCORE_FOR_PSM("MS:1001143");
	private final String id;

	private CommonlyUsedCV(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

}
